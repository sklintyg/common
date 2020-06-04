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
angular.module('common').directive('wcSrsRiskDiagram',
    [ 'common.srsViewState', 'common.wcSrsChartFactory', '$timeout', '$window',
        function(srsViewState, chartFactory, $timeout, $window) {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    // config: '='
                },
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsRiskDiagram.directive.html',
                link: function($scope, $element, $attrs) {
                    $scope.srs = srsViewState;

                    // TODO: Flytta till där webcert-modulen definieras på samma sätt som i statistik:app.run.js? eller boot-app.jsp?
                    Highcharts.seriesTypes.line.prototype.drawLegendSymbol = Highcharts.seriesTypes.area.prototype.drawLegendSymbol;
                    Highcharts.setOptions({
                        lang: { thousandsSep: ' ' }
                    });


                    var riskChart = {};
                    var chartData = {
                        risk: {
                            chartData: [
                                {
                                    name: 'Genomsnittlig risk',
                                    type: 'GENOMSNITT_RISK',
                                    y: null,
                                    enabled: true
                                },
                                {
                                    name: 'Tidigare risk',
                                    type: 'TIDIGARE_RISK',
                                    y: null,
                                    enabled: true,
                                    date: null,
                                    opinion: null
                                },
                                {
                                    name: 'Aktuell risk',
                                    type: 'RISK',
                                    y: null,
                                    enabled: false,
                                    date: null,
                                    opinion: null
                                },
                                {
                                    name: '',
                                    type: 'RISK',
                                    y: null,
                                    enabled: false,
                                    date: null,
                                    opinion: null
                                }
                            ]
                        }
                    };
                    var responsiveSize = 'INIT'; // will be overruled during first render (see paintBarChart below)
                    var chartWidth = 0;
                    var chartHeight = 0;

                    var updateCharts = function (result) {
                        chartFactory.addColor(result.risk.chartData);
                        updateResponsiveDesign();
                        riskChart = paintBarChart('riskChart', result.risk.chartData);
                    };

                    var dataReceivedSuccess = function(result) {
                        $timeout(function() {
                            updateCharts(result);
                        }, 1);
                    };

                    var calculateResponsiveSize = function(currentResponsiveSize) {
                        var windowWidth = $window.innerWidth;
                        var newSize = null;
                        if (windowWidth >= 1300 && currentResponsiveSize !== 'larger') {
                            newSize = {
                                responsiveSize: 'larger',
                                width: 500,
                                height: 267
                            };
                        } else if (windowWidth < 1300 && windowWidth >= 1020 && currentResponsiveSize !== 'normal') {
                            newSize = {
                                responsiveSize: 'normal',
                                width: 360,
                                height: 240
                            };
                        }
                        else if (windowWidth < 1020 && windowWidth >= 870 && currentResponsiveSize !== 'smaller') {
                            newSize = {
                                responsiveSize: 'smaller',
                                width: 300,
                                height: 200
                            };
                        }
                        else if (windowWidth < 870 && currentResponsiveSize !== 'smallest') {
                            newSize = {
                                responsiveSize: 'smallest',
                                width: 230,
                                height: 160
                            };
                        }
                        return newSize;
                    };

                    function setBarNames(chartData, meanName, currentName, previousName, calculateRisk) {
                        chartData[0].name = meanName;
                        chartData[0].type = 'GENOMSNITT_RISK';
                        if (!chartData[2] || chartData[2].enabled===false) {
                            chartData[1].name = chartData[1].y?currentName:calculateRisk;
                            chartData[1].type = 'RISK';
                        } else if (chartData[2] && chartData[2].enabled===true) {
                            chartData[1].name = previousName;
                            chartData[1].type = 'TIDIGARE_RISK';
                            chartData[2].name = chartData[2].y?currentName:calculateRisk;
                            chartData[2].type = 'RISK';
                        }
                    }

                    function updateResponsiveDesign() {
                        var newSize = calculateResponsiveSize(responsiveSize);
                        if (newSize) {
                            chartWidth = newSize.width;
                            chartHeight = newSize.height;
                            if (responsiveSize !== 'INIT') {
                                // The timeout is needed to get things working in IE during resize
                                setTimeout(function () {
                                    riskChart = paintBarChart('riskChart', chartData.risk.chartData);
                                }, 1);
                            }
                            responsiveSize = newSize.responsiveSize;
                        }

                        if (responsiveSize === 'smallest' && chartData.risk) {
                            setBarNames(chartData.risk.chartData, 'Gen.sn.', 'Akt.', 'Tid.', 'Ber.');
                        } else if (responsiveSize === 'smaller' && chartData.risk) {
                            setBarNames(chartData.risk.chartData, 'Genomsnitt', 'Aktuell', 'Tidigare', 'Beräkna');
                        } else if (chartData.risk) {
                            setBarNames(chartData.risk.chartData, 'Genomsnittlig risk', 'Aktuell risk', 'Tidigare risk', 'Beräkna aktuell risk');
                        }
                    }

                    function onResize(event) {
                        updateResponsiveDesign();
                    }

                    function paintBarChart(containerId, chartData) {
                        var series = [
                            {
                                name: 'Risk',
                                data: chartData.filter(function(cd){ return cd.enabled === true; }),
                                color: chartFactory.getColors().risk
                            }
                        ];
                        var categories = chartData.map(function (e) {
                            return {name: e.name};
                        });

                        var chartConfigOptions = {
                            categories: categories,
                            series: series,
                            type: 'column',
                            renderTo: containerId,
                            unit: '%',
                            maxWidthPercentage: 80,
                            marginRight: 120
                        };

                        var chartOptions = chartFactory.getHighChartConfigBase(chartConfigOptions);
                        chartOptions.chart.width = chartWidth;
                        chartOptions.chart.height = chartHeight;
                        chartOptions.chart.plotBorderWidth = 0;
                        chartOptions.subtitle.text = null;
                        chartOptions.yAxis[0].tickInterval = 20;
                        chartOptions.yAxis[0].max = 100;
                        chartOptions.yAxis[0].gridLineWidth=0;
                        chartOptions.yAxis[0].lineWidth = 1;
                        chartOptions.yAxis[0].lineColor = '#c7c7c7';
                        chartOptions.yAxis[0].alternateGridColor = true;
                        chartOptions.legend.enabled = false;
                        chartOptions.yAxis[0].plotLines = [
                            {
                                color: '#C7C7C7',
                                width: 1,
                                value: 39
                            },
                            {
                                color: '#C7C7C7',
                                width: 1,
                                value: 62
                            },
                            {
                                color: '#C7C7C7',
                                width: 1,
                                value: 100
                            }
                        ];

                        chartOptions.yAxis[0].plotBands = [{
                            color: 'white',
                            from: 0,
                            to: 39,
                            label: {
                                text:'Måttlig risk',
                                align: 'right',
                                rotation: -15,
                                textAlign: 'left'
                            }
                        },
                            {
                                color: 'white',
                                from: 39,
                                to: 62,
                                label: {
                                    text:'Hög risk',
                                    align: 'right',
                                    rotation: -15,
                                    textAlign: 'left'
                                }
                            },
                            {
                                color: 'white',
                                from: 62,
                                to: 100,
                                label: {
                                    text:'Mycket hög risk',
                                    align: 'right',
                                    rotation: -15,
                                    textAlign: 'left'
                                }
                            }
                        ];
                        chartOptions.accessibility = {
                            description: 'Riskdiagram'
                        };
                        return new Highcharts.Chart(chartOptions);
                    }

                    $scope.$on('$destroy', function() {
                        $window.removeEventListener('resize', onResize);
                        if(riskChart && typeof riskChart.destroy === 'function') {
                            riskChart.destroy();
                        }
                    });

                    $scope.$watchCollection('srs.predictions', function(newPredictions, oldPredictions) {
                        // Reset
                        chartData.risk.chartData.forEach(function(cd) {
                            cd.y=null;
                            cd.enabled = false;
                        });
                        // if we change to nothing then just return after the reset
                        if (!newPredictions || !newPredictions[0]) {
                            return;
                        }

                        // Current prevalence (always furthest to the left, i.e. index 0)
                        if (newPredictions[0].prevalence !== null) {
                            chartData.risk.chartData[0].enabled = true;
                            chartData.risk.chartData[0].y = Math.round(newPredictions[0].prevalence * 100);
                            // name/title is set via updateResponsiveDesign
                        } else {
                            chartData.risk.chartData[0].enabled = false;
                            chartData.risk.chartData[0].y = 0;
                            chartData.risk.chartData[0].name = '';
                            chartData.risk.chartData[0].date = null;
                            chartData.risk.chartData[0].opinion = null;
                        }

                        // Previous prediction (if we have a previous prediction newPrediction[1], add it on position 1)
                        if (newPredictions[1] && newPredictions[1].probabilityOverLimit !== null) {
                            chartData.risk.chartData[1].enabled = true;
                            chartData.risk.chartData[1].y = Math.round(newPredictions[1].probabilityOverLimit * 100);
                            chartData.risk.chartData[1].date = newPredictions[1].date;
                            chartData.risk.chartData[1].opinion = newPredictions[1].opinion;
                            // add current prediction if available
                            if (newPredictions[0].probabilityOverLimit !== null) { // if we also have a current prediction newPrediction[1] add that to the right
                                chartData.risk.chartData[2].enabled = true;
                                chartData.risk.chartData[2].y = Math.round(newPredictions[0].probabilityOverLimit * 100);
                                chartData.risk.chartData[2].date = newPredictions[0].date;
                                chartData.risk.chartData[2].opinion = newPredictions[0].opinion;
                            }
                            // name/title is set via updateResponsiveDesign
                        }
                        // No previous prediction but current (if we don't have a previous one, just add the current at the middle spot)
                        else if (newPredictions[0].probabilityOverLimit !== null) {
                            chartData.risk.chartData[1].enabled = true;
                            chartData.risk.chartData[1].y = Math.round(newPredictions[0].probabilityOverLimit * 100);
                            chartData.risk.chartData[1].date = newPredictions[0].date;
                            chartData.risk.chartData[1].opinion = newPredictions[0].opinion;
                        }
                        // No prediction
                        else {
                            chartData.risk.chartData[1].enabled = false;
                            chartData.risk.chartData[1].y = null;
                            chartData.risk.chartData[1].name = '';
                            chartData.risk.chartData[1].date = null;
                            chartData.risk.chartData[1].opinion = null;
                        }
                        dataReceivedSuccess(chartData);
                    });

                    // Initialize
                    $timeout(function () {
                        $window.removeEventListener('resize', onResize);
                        $window.addEventListener('resize', onResize);
                    });

                }
        };
} ]);