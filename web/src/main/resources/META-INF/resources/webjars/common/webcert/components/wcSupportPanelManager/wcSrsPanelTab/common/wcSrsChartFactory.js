/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* globals Highcharts */
angular.module('common').factory('common.wcSrsChartFactory',
    [ '$filter', '$log',
    function($filter, $log) {
        'use strict';

        /* Color definitions to be used with highcharts */
        var colors = {
            total: '#5D5D5D',
            // overview: '#57843B',
            overview: '#3D4260',
            high: '#E10934',
            medium: '#FFBA3E',
            low: '#799745',
            risk: '#5C6381',
            meanRiskColor: '#CDCED6',
            earlierRiskColor: ' #A7ACC1',
            other: [
                '#E11964',
                '#032C53',
                '#FFBA3E',
                '#799745',
                '#3CA3FF',
                '#C37EB2',
                '#2A5152',
                '#FB7F4D',
                '#5CC2BC',
                '#704F38',
                '#600030',
                '#006697']
        };
        var COLORS = colors;
        var CATEGORY_TO_HIDE = 'Totalt';

        var getColors = function() {
            return COLORS;
        };

        var ControllerCommons = {
            htmlsafe: function(string) {
                return string.replace(/&/g, '&amp;').replace(/</g, '&lt;');
            },

            isNumber: function(n) {
                return !isNaN(parseFloat(n)) && isFinite(n);
            },

            makeThousandSeparated: function(input) {
                if (this.isNumber(input)) {
                    var splittedOnDot = input.toString().split('\.');
                    var integerPartThousandSeparated = splittedOnDot[0].split('').reverse().join('')
                        .match(/.{1,3}/g).join('\u00A0').split('').reverse().join('');
                    if (splittedOnDot.length === 1) {
                        return integerPartThousandSeparated;
                    }
                    return integerPartThousandSeparated + ',' + splittedOnDot[1];
                }
                return input;
            }
        };

        var labelFormatter = function(maxWidth, sameLengthOnAll) {

            return function() {
                //If the label is more than 30 characters then cut the text and add ellipsis
                var numberOfChars = maxWidth;

                if (!sameLengthOnAll && this.isFirst) {
                    numberOfChars = maxWidth - 10;
                }

                return _formatter(this.value, numberOfChars);
            };
        };

        function _formatter(value, numberOfChars) {
            var textToFormat;
            var tooltip;
            var info;

            var isObject = angular.isObject(value);

            if (isObject) {
                textToFormat = value.name;
                tooltip = value.tooltip;
                info = value.info;
            } else {
                textToFormat = value;
                tooltip = value;
                info = null;
            }

            var text = textToFormat.length > numberOfChars ? textToFormat.substring(0, numberOfChars) + '...' : textToFormat;
            if (info) {
                // popover attributes is used for info messages, data-* can be activated using jquery (see wcSrsRiskDiagram)
                return '<span uib-popover="' + info + '" popover-popup-delay="300" popover-append-to-body="true"' +
                    ' popover-placement="top" data-original-title="' + tooltip + '" data-placement="auto right" data-toggle="tooltip">' +
                    text + ' <i class="material-icons md-18 diagnosinfo">info_outline</i></span>';
            } else {
                return '<span data-original-title="' + tooltip + '" data-placement="auto right" data-toggle="tooltip">' + text + '</span>';
            }

        }

        function _getMaxLength(maxLength) {
            return maxLength ? maxLength : 30;
        }

        function getTextWidth(container, text) {
            //Temporary add, measure and remove the chip's html equivalent.
            var elem = $('<span class="temp-highcharts-label">' + text + '</span>');
            container.append(elem);
            var width = elem.outerWidth(true);
            elem.remove();
            return width;
        }

        function _getCategoryLength(chartCategories, maxLength) {
            var categoryLength = 0;
            var container = $('.collapsible-panel-body');
            var labelLength = _getMaxLength(maxLength);

            angular.forEach(chartCategories, function(category) {
                var length = getTextWidth(container, _formatter(category.name, labelLength));

                if (categoryLength < length) {
                    categoryLength = length;
                }
            });

            return categoryLength;
        }

        function _getLabelHeight(chartCategories, verticalLabel, maxLength) {
            if (verticalLabel) {
                return _getCategoryLength(chartCategories, maxLength);
            }

            return 40;
        }

        function _opinionToText(opinion) {
            switch (opinion) {
                case 'HOGRE':
                    return 'Högre';
                case 'KORREKT':
                    return 'Korrekt';
                case 'LAGRE':
                    return 'Lägre';
                case 'KAN_EJ_BEDOMA':
                    return 'Kan ej bedöma';
                default:
                    return 'Bedömning ej tillgänglig';
            }
        }

        function _getLabelForDaysIntoSickLeave(daysIntoSickLeave) {
            switch (daysIntoSickLeave) {
                case 15:
                    return 'Ny sjukskrivning';
                case 45:
                    return 'Förlängning';
                default:
                    return 'Okänd sjukskrivningsdag';
            }
        }

        function _getTooltip(percentChart, unit, chartType, usingAndel, maxWidth) {

            var formatter;

            if (chartType === 'bubble') {
                formatter = function() {
                    var value = percentChart ?
                        Highcharts.numberFormat(this.percentage, 0, ',') + '%' :
                        ControllerCommons.makeThousandSeparated(this.point.z);
                    return '<b>' + value + '</b> ' + unit + ' för ' + this.series.name;
                };
            } else {
                formatter = function() {
                    var value = percentChart ?
                        Highcharts.numberFormat(this.percentage, 0, ',') + '%' :
                        ControllerCommons.makeThousandSeparated(this.y) + (usingAndel ? '%' : '');

                    var title = this.x ? this.x : this.point.name;

                    if (angular.isObject(title)) {
                        title = title.oldName ? title.oldName : title.name;
                    }

                    var popupText = [];
                    if (this.point.daysIntoSickLeave) {
                        popupText.push('<b>Riskberäkningen gäller:</b> ');
                        popupText.push(_getLabelForDaysIntoSickLeave(this.point.daysIntoSickLeave));
                        popupText.push('<br/>');
                    }
                    popupText.push('<b>' + title + ':</b> ');
                    if (value !== '0') {
                        popupText.push(value + unit);
                    } else {
                        popupText.push('Ej beräknad');
                    }
                    if (this.point.date) {
                        popupText.push('<br/> <b>Beräknades:</b> ');
                        popupText.push(this.point.date);
                    }
                    if (this.point.opinion) {
                        popupText.push('<br/> <b>Läkarens bedömning:</b> ');
                        popupText.push(_opinionToText(this.point.opinion));
                    }
                    return  popupText.join('');
                };
            }

            return {
                hideDelay: 500,
                backgroundColor : '#fff',
                borderWidth : 2,
                padding: 9,
                style: {
                    whiteSpace: 'nowrap',
                    width: '600px'
                },
                responsiveWidthPercentage: maxWidth || null,
                useHTML: false,
                outside: typeof(maxWidth) === 'undefined',
                formatter: formatter
            };
        }

        function onChartRender() {
            /* jshint ignore:start */
            this.tooltip.update({
                style: {
                    width: Math.floor(0.01 * this.tooltip.options.responsiveWidthPercentage * this.chartWidth) + 'px'
                }});
            /* jshint ignore:end */
        }

        function processCategories(categories) {

            // Ta bort kategorienamnet om det bara finns Totalt
            if (categories && categories.length === 1 && categories[0].name === CATEGORY_TO_HIDE) {
                return [{
                    name: '',
                    marked: false,
                    oldName: categories[0].name
                }];
            }

            return categories && Array.isArray(categories) && categories.map(function(category) {
                var tooltip = category.tooltip ? category.tooltip : '';
                var info = category.info ? category.info : '';
                return {
                    name: ControllerCommons.htmlsafe(category.name),
                    tooltip: ControllerCommons.htmlsafe(tooltip),
                    info: ControllerCommons.htmlsafe(info),
                    marked: category.marked
                };
            });
        }

        function _addChartEvent(config, eventName, callback) {
            if(typeof(config.chart.events) !== 'object') {
                config.chart.events = {};
            }
            config.chart.events[eventName] = callback;
        }

        /**
         * Hämtar en config för ett highcharts diagram
         *
         *
         * Options består av:
         *
         * categories: array,
         * series: array,
         * type: string,
         * doneLoadingCallback: function,
         * percentChart: boolean,
         * stacked: boolean,
         * verticalLabel: boolean,
         * labelMaxLength: number,
         * renderTo: string
         * unit: string
         *
         *
         * @param options
         * @returns {}  // chart object
         */
        var getHighChartConfigBase = function(options) {
            /*jshint maxcomplexity:12 */
            var labelHeight = _getLabelHeight(options.categories, options.verticalLabel, options.labelMaxLength);

            var config = {
                chart : {
                    animation: false,
                    renderTo : options.renderTo ? options.renderTo : 'chart1',
                    type: options.type,
                    backgroundColor : null, //transparent
                    plotBorderWidth: options.borderWidth ? options.borderWidth : 1,
                    marginRight: options.marginRight ? options.marginRight : undefined,
                    width: options.width ? options.width : 360,
                    marginBottom: options.verticalLabel ? labelHeight + 25 : null
                },
                title: {
                    text: null,
                    style: {
                        fontSize: '16px'
                    }
                },
                subtitle : {},
                legend: {},
                xAxis : {
                    labels : {
                        rotation : 0,
                        align : 'center',
                        style: {
                            textAlign: 'center'
                        },
                        useHTML: true,
                        formatter: labelFormatter(_getMaxLength(options.labelMaxLength), options.verticalLabel),
                        step: 1
                    },
                    categories : processCategories(options.categories)
                },
                yAxis : [{
                    id: 'yAxis1',
                    allowDecimals : false,
                    min : 0,
                    minRange : 0.1,
                    title : {
                        text : null
                    },
                    labels : {
                        formatter : function() {
                            return ControllerCommons.makeThousandSeparated(this.value) + (options.unit ? options.unit : (options.percentChart || options.usingAndel ? '%' : ''));
                        }
                    }
                }],
                plotOptions : {
                    line : {
                        animation: false,
                        softThreshold: false,
                        allowPointSelect : false,
                        marker : {
                            enabled : true,
                            symbol : 'circle'
                        },
                        dataLabels : {
                            enabled : false
                        },
                        events : {
                            legendItemClick : function() { // This function removes interaction for plot and legend-items
                                return false;
                            }
                        },
                        showInLegend : true,
                        stacking: null
                    },
                    column : {
                        animation: false,
                        softThreshold: false,
                        showInLegend : true,
                        stacking: options.percentChart ? 'percent' : (options.stacked ? 'normal' : null),
                        dataLabels: {
                            enabled: true,
                            crop: false,
                            overflow: 'none',
                            formatter: function() {
                                if (this.y === 0) {
                                    return '-';
                                } else {
                                    return this.y + (options.unit ? options.unit : '');
                                }
                            }
                        }
                    },
                    series: {
                        borderColor: '#b7b7b7',
                        borderWidth: 1
                    },
                    area : {
                        animation: false,
                        lineColor : '#666666',
                        lineWidth : 1,
                        marker : {
                            enabled : false,
                            symbol : 'circle'
                        },
                        showInLegend : true,
                        stacking: options.percentChart ? 'percent' : 'normal'
                    },
                    pie : {
                        animation: false,
                        dataLabels : {
                            enabled : false
                        },
                        showInLegend : false
                    }
                },
                tooltip : _getTooltip(options.percentChart, options.unit, options.type, options.usingAndel, options.maxWidthPercentage),
                credits : {
                    enabled : false
                },
                series : options.series && Array.isArray(options.series) ? options.series.map(function (series) {
                    //This enables the marker for series with single data points
                    if (series.data.length === 1) {
                        if (series.marker) {
                            series.marker.enabled = true;
                        } else {
                            series.marker = {enabled: true};
                        }
                    }
                    return series;
                }): null
            };

            if (options.doneLoadingCallback) {
                _addChartEvent(config, 'load', options.doneLoadingCallback);
            }

            if (typeof(options.maxWidthPercentage) === 'number') {
                _addChartEvent(config, 'render', onChartRender);
            }

            return config;
        };

        var showInLegend = function(series, index) {
            if (series && series.length > index) {
                return series[index].options.showInLegend;
            }

            return false;
        };

        var addColor = function (rawData) {
            var colorSelector = 0;

            var colors = COLORS.other,
            riskColor = COLORS.risk,
            meanRiskColor = COLORS.meanRiskColor,
            earlierRisk = COLORS.earlierRiskColor;

            angular.forEach(rawData, function (data) {
                // continue if color is set
                if (data.color) {
                    return;
                }

                if (data.type === 'RISK') {
                    data.color = riskColor;
                } else if (data.type === 'GENOMSNITT_RISK') {
                    data.color = meanRiskColor;
                } else if (data.type === 'TIDIGARE_RISK') {
                    data.color = earlierRisk;
                } else if (data.type === 'ÅTERGÅNG') {
                    data.color = colors.overview;
                } else {
                    if(colorSelector === colors.length) {
                        //Begin a new with colors array
                        colorSelector = 0;
                    }
                    data.color = colors[colorSelector++];
                }

            });
            return rawData;
        };

        //This is the public api accessible to customers of this factory
        return {
            addColor: addColor,
            getHighChartConfigBase: getHighChartConfigBase,
            showInLegend: showInLegend,
            getColors: getColors
        };
    }]);
