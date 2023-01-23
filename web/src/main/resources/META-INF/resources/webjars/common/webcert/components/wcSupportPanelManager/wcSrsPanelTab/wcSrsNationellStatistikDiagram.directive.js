/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcSrsNationellStatistikDiagram',
    [ 'common.srsViewState', 'common.wcSrsChartFactory', '$timeout', '$window',
        function(srsViewState, chartFactory, $timeout, $window) {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    // config: '='
                },
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsNationellStatistikDiagram.directive.html',
                link: function($scope, $element, $attrs) {
                    $scope.srs = srsViewState;

                    // TODO: Flytta till där webcert-modulen definieras på samma sätt som i statistik:app.run.js? eller boot-app.jsp?
                    Highcharts.seriesTypes.line.prototype.drawLegendSymbol = Highcharts.seriesTypes.area.prototype.drawLegendSymbol;
                    Highcharts.setOptions({
                        lang: { thousandsSep: ' ' }
                    });

                    var statistikChart = {};
                    var chartData = {
                        statistik: [
                            {
                                name: '30',
                                type: 'ÅTERGÅNG',
                                x: 30,
                                y: 0
                            },
                            {
                                name: '90',
                                type: 'ÅTERGÅNG',
                                x: 90,
                                y: 0
                            },
                            {
                                name: '180',
                                type: 'ÅTERGÅNG',
                                x: 180,
                                y: 0
                            },
                            {
                                name: '365',
                                type: 'ÅTERGÅNG',
                                x: 365,
                                y: 0
                            }
                        ]
                    };
                    var responsiveSize = 'INIT'; // will be overruled during first render (see paintBarChart below)
                    var chartWidth = 0;
                    var chartHeight = 0;

                    var updateCharts = function (result) {
                        chartFactory.addColor(result.statistik);
                        updateResponsiveDesign();
                        statistikChart = paintChart('nationalStatisticsChart', result.statistik);
                    };

                    var dataReceivedSuccess = function(result) {
                        $scope.statisticNotDone = false;
                        $scope.doneLoading = true;
                        $timeout(function() {
                            updateCharts(result);
                        }, 1);
                    };

                    var calculateResponsiveSize = function(currentResponsiveSize) {
                        var windowWidth = $window.innerWidth;
                        var newSize = null;

                        if (windowWidth >= 1440 && currentResponsiveSize !== 'largest') {
                            newSize = {
                                responsiveSize: 'largest',
                                width: 490,
                                height: 280
                            };
                        }
                        else if (windowWidth < 1400 && windowWidth >= 1200 && currentResponsiveSize !== 'larger') {
                            newSize = {
                                responsiveSize: 'larger',
                                width: 420,
                                height: 240
                            };
                        } else if (windowWidth < 1200 && windowWidth >= 1000 && currentResponsiveSize !== 'normal') {
                            newSize = {
                                responsiveSize: 'normal',
                                width: 350,
                                height: 200
                            };
                        }
                        else if (windowWidth < 1000 && windowWidth >= 800 && currentResponsiveSize !== 'smaller') {
                            newSize = {
                                responsiveSize: 'smaller',
                                width: 270,
                                height: 160
                            };
                        }
                        else if (windowWidth < 800 && currentResponsiveSize !== 'smallest') {
                            newSize = {
                                responsiveSize: 'smallest',
                                width: 180,
                                height: 137
                            };
                        }
                        return newSize;
                    };

                    function updateResponsiveDesign() {
                        var newSize = calculateResponsiveSize(responsiveSize);
                        if (newSize) {
                            chartWidth = newSize.width;
                            chartHeight = newSize.height;
                            responsiveSize = newSize.responsiveSize;
                            if (responsiveSize !== 'INIT') {
                                // The timeout is needed to get things working in IE during resize
                                setTimeout(function () {
                                    statistikChart = paintChart('nationalStatisticsChart', chartData.statistik);
                                }, 100);
                            }

                        }
                    }

                    function onResize(event) {
                        updateResponsiveDesign();
                    }

                    var getResponsiveXLabel = function(val) {
                        var responsiveDayLabel = 'dagar';
                        if (responsiveSize === 'smallest' || responsiveSize === 'smaller') {
                            responsiveDayLabel = 'd.';
                        } else if (responsiveSize === 'normal') {
                            responsiveDayLabel = 'dag.';
                        }
                        return '<div>' + val + ' ' + responsiveDayLabel+'</div>';
                    };

                    function paintChart(containerId, chartData) {
                        var series = [
                            {
                                name: 'Återgång i arbete',
                                marker: {
                                    radius: 4
                                },
                                data: chartData,
                                color: chartFactory.getColors().overview
                            }
                        ];
                        var chartOptions = {
                            chart : {
                                animation: false,
                                renderTo : containerId,
                                type: 'line',
                                backgroundColor : null, //transparent
                                plotBorderWidth: 1,
                                width: chartWidth,
                                height: chartHeight,
                                marginTop: 14
                            },
                            title: {
                                text: null
                            },
                            subtitle : {},
                            legend: {
                                enabled: false
                            },
                            yAxis: {
                                type:'line',
                                lineWidth: 1,
                                lineColor: '#3D4260',
                                tickInterval: 20,
                                max: 100,
                                allowDecimals : false,
                                min : 0,
                                title : {
                                    text : null
                                },
                                labels : {
                                    formatter : function() {
                                        return this.value + '%';
                                    }
                                }
                            },
                            xAxis: {
                                lineWidth: 1,
                                lineColor: '#3D4260',
                                tickPositions: [30, 90, 180, 365],
                                type: 'category',
                                labels: {
                                    formatter: function() {
                                        return getResponsiveXLabel(this.value);
                                    }
                                },
                                min: 0,
                                max: 400
                            },
                            plotOptions : {
                                line : {
                                    marker : {
                                        enabled : true,
                                        symbol : 'circle'
                                    },
                                    dataLabels: {
                                        enabled: true,
                                        crop: false,
                                        overflow: 'none',
                                        formatter: function() {
                                            return this.y + '%';
                                        }
                                    },
                                    events : {
                                        legendItemClick : function() { // This function removes interaction for plot and legend-items
                                            return false;
                                        }
                                    }
                                },
                                series: {
                                }
                            },
                            tooltip : {
                                hideDelay: 500,
                                backgroundColor : '#fff',
                                borderWidth : 2,
                                padding: 9,
                                style: {
                                    whiteSpace: 'nowrap',
                                    width: '600px'
                                },
                                useHTML: false,
                                formatter: function() {
                                    var title = this.x ? this.x : this.point.name;
                                    if (angular.isObject(title)) {
                                        title = title.oldName ? title.oldName : title.name;
                                    }

                                    return title + ' dagar <b>' + this.y + '%</b>';
                                }
                            },
                            credits : {
                                enabled : false
                            },
                            series : series
                        };

                        return new Highcharts.Chart(chartOptions);
                    }

                    $scope.$on('$destroy', function() {
                        $window.removeEventListener('resize', onResize);
                        if(statistikChart && typeof statistikChart.destroy === 'function') {
                            statistikChart.destroy();
                        }
                    });

                    $scope.$watch('srs.statistik', function(newVal, oldVal) {
                        if (newVal.nationellStatistik !== null &&
                            Array.isArray(newVal.nationellStatistik) &&
                            newVal.nationellStatistik.length === 5) {
                            for (var i = 0; i<4; i++) {
                                chartData.statistik[i].y = Math.round((newVal.nationellStatistik[i]/newVal.nationellStatistik[4]) * 100);
                            }

                        }
                        dataReceivedSuccess(chartData);
                    });

                    // Set up component
                    $timeout(function () {
                        $window.removeEventListener('resize', onResize);
                        $window.addEventListener('resize', onResize);
                    });

                }
        };
} ]);