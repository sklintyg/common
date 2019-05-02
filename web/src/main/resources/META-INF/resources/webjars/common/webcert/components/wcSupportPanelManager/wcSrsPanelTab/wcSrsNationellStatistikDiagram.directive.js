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
angular.module('common').directive('wcSrsNationellStatistikDiagram',
    [ 'common.srsViewState', 'common.wcSrsChartFactory', '$timeout',
        function(srsViewState, chartFactory, $timeout) {
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
                                y: 0,
                            },
                            {
                                name: '90 dagar',
                                type: 'ÅTERGÅNG',
                                y: 0,
                            },
                            {
                                name: '180 dagar',
                                type: 'ÅTERGÅNG',
                                y: 0,
                            },
                            {
                                name: '365 dagar',
                                type: 'ÅTERGÅNG',
                                y: 0,
                            },
                            {
                                name: '365+ dagar',
                                type: 'ÅTERGÅNG',
                                y: 0,
                            },
                        ],
                    }

                    var setTooltipText = function (result) {
                        $scope.popoverTextRiskChart = 'Diagrammet visar antal individer som återgått i arbete efter givet antal dagar.' +
                            '<br><br>Ställ markören i respektive stapel för att se respektive riskvärde.';
                    };

                    var dataReceivedSuccess = function(result) {
                        // $scope.subTitlePeriod = result.periodText;
                        setTooltipText(result);
                        $scope.statisticNotDone = false;
                        $scope.doneLoading = true;
                        $timeout(function() {
                            updateCharts(result);
                        }, 1);
                    };

                    var dataReceived = function (result) {
                        dataReceivedSuccess(result);
                    };

                    var updateCharts = function (result) {
                        // console.log('update chart with new data', result.statistik)
                        chartFactory.addColor(result.statistik);
                        statistikChart = paintChart('nationalStatisticsChart', result.statistik);
                    };

                    // function populatePageWithData(result) {
                    //     $timeout(function () {
                    //         updateCharts(result);
                    //     }, 100);
                    // }

                    function paintChart(containerId, chartData) {
                        var series = [
                            {
                                name: 'Återgång i arbete',
                                // data: _.map(chartData, function (e) {
                                //     return e.quantity;
                                // }),
                                marker: {
                                    radius: 4
                                },
                                data: chartData,
                                color: chartFactory.getColors().overview
                            }
                        ];
                        var categories = _.map(chartData, function (e) {
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
                        // chartOptions.yAxis.title = {text: 'Risk', style : chartOptions.subtitle.style };
                        chartOptions.yAxis.title.text = 'Antal individer';
                        // chartOptions.yAxis.max = 100;
                        chartOptions.yAxis.tickPixelInterval = 30;
                        chartOptions.legend.enabled = false;

                        return new Highcharts.Chart(chartOptions);
                    }

                    // statisticsData.getOverview(dataReceived, function () {
                    //     $scope.dataLoadingError = true;
                    // });
                    // $scope.subTitle = messageService.getProperty('national.overview-header2');
                    // $scope.spinnerText = 'Laddar information...';
                    // $scope.doneLoading = false;
                    // $scope.dataLoadingError = false;
                    // $scope.chartFootnotes = ['help.nationell.overview'];

                    $scope.$on('$destroy', function() {
                        if(statistikChart && typeof statistikChart.destroy === 'function') {
                            statistikChart.destroy();
                        }
                    });

                    $scope.$watch('srs.statistik', function(newVal, oldVal) {
                        // console.log('SRS SCOPE DATA WAS UPDATED FOR CHART, statistik', newVal)
                        // console.log('typeof', Array.isArray(newVal.nationellStatistik))
                        if (newVal.nationellStatistik != null
                            && Array.isArray(newVal.nationellStatistik)
                            && newVal.nationellStatistik.length === 5) {
                            // console.log('got new Nationell statistik for chart')
                            for (var i = 0; i<5; i++) {
                                chartData.statistik[i].y = newVal.nationellStatistik[i]
                            }
                        }
                        // if (newVal.prevalence != null) {
                        //     chartData.risk.chartData[0].y = Math.round(newVal.prevalence * 100)
                        //     chartData.risk.chartData[0].name = 'Genomsnittlig risk';
                        // } else {
                        //     chartData.risk.chartData[0].y = 0;
                        //     chartData.risk.chartData[0].name = '';
                        // }
                        // if (newVal.probabilityOverLimit != null) {
                        //     chartData.risk.chartData[1].y = Math.round(newVal.probabilityOverLimit * 100)
                        //     chartData.risk.chartData[1].name = 'Individuell risk';
                        // } else {
                        //     chartData.risk.chartData[1].y = 0;
                        //     chartData.risk.chartData[1].name = '';
                        // }
                        updateCharts(chartData);
                    });

                    // Kick start rendering
                    $timeout(function () {
                        dataReceivedSuccess(chartData);
                    });

                }
        };
} ]);