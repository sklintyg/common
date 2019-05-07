/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
                                name: '30 dagar',
                                type: 'ÅTERGÅNG',
                                y: 0
                            },
                            {
                                name: '90 dagar',
                                type: 'ÅTERGÅNG',
                                y: 0
                            },
                            {
                                name: '180 dagar',
                                type: 'ÅTERGÅNG',
                                y: 0
                            },
                            {
                                name: '365 dagar',
                                type: 'ÅTERGÅNG',
                                y: 0
                            },
                            {
                                name: '365+ dagar',
                                type: 'ÅTERGÅNG',
                                y: 0
                            }
                        ]
                    };

                    var setTooltipText = function (result) {
                        $scope.popoverTextRiskChart = 'Diagrammet visar antal individer som återgått i arbete efter givet antal dagar.' +
                            '<br><br>Ställ markören i respektive stapel för att se respektive riskvärde.';
                    };

                    var updateCharts = function (result) {
                        chartFactory.addColor(result.statistik);
                        statistikChart = paintChart('nationalStatisticsChart', result.statistik);
                    };

                    var dataReceivedSuccess = function(result) {
                        setTooltipText(result);
                        $scope.statisticNotDone = false;
                        $scope.doneLoading = true;
                        $timeout(function() {
                            updateCharts(result);
                        }, 1);
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
                        var categories = $window._.map(chartData, function (e) {
                            return {name: e.name};
                        });

                        var chartConfigOptions = {
                            categories: categories,
                            series: series,
                            type: 'line',
                            // overview: true,
                            plotOptions: {
                                series: {
                                    label: {
                                        connectorAllowed: false
                                    }
                                }
                            },
                            renderTo: containerId,
                            unit: 'individer',
                            maxWidthPercentage: 80
                        };

                        var chartOptions = chartFactory.getHighChartConfigBase(chartConfigOptions);
                        chartOptions.chart.height = 240;
                        chartOptions.subtitle.text = null;
                        chartOptions.yAxis.title.text = 'Antal individer';
                        chartOptions.yAxis.tickPixelInterval = 30;
                        chartOptions.legend.enabled = false;

                        return new Highcharts.Chart(chartOptions);
                    }

                    $scope.$on('$destroy', function() {
                        if(statistikChart && typeof statistikChart.destroy === 'function') {
                            statistikChart.destroy();
                        }
                    });

                    $scope.$watch('srs.statistik', function(newVal, oldVal) {
                        if (newVal.nationellStatistik !== null &&
                            Array.isArray(newVal.nationellStatistik) &&
                            newVal.nationellStatistik.length === 5) {
                            for (var i = 0; i<5; i++) {
                                chartData.statistik[i].y = newVal.nationellStatistik[i];
                            }
                        }
                        updateCharts(chartData);
                    });

                    // Kick start rendering
                    $timeout(function () {
                        dataReceivedSuccess(chartData);
                    });

                }
        };
} ]);