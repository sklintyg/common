/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
    [ 'common.srsViewState', 'common.wcSrsChartFactory', '$timeout', '$window', '$compile',
        function(srsViewState, chartFactory, $timeout, $window, $compile) {
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
                                    y: 0,
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
                        // Make the tooltip appear at startup on the latest calculation
                        if (riskChart.series && riskChart.series[0] && riskChart.series[0].points) {
                            if(riskChart.series[0].points.length > 2 && riskChart.series[0].points[2].y && riskChart.series[0].points[2].y !== 0) {
                                riskChart.tooltip.refresh(riskChart.series[0].points[2]);
                            } else if(riskChart.series[0].points.length > 1 && riskChart.series[0].points[1].y && riskChart.series[0].points[1].y !== 0) {
                                riskChart.tooltip.refresh(riskChart.series[0].points[1]);
                            }
                        }
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
                                width: 480,
                                height: 267
                            };
                        } else if (windowWidth < 1300 && windowWidth >= 1020 && currentResponsiveSize !== 'normal') {
                            newSize = {
                                responsiveSize: 'normal',
                                width: 420,
                                height: 240
                            };
                        }
                        else if (windowWidth < 1020 && windowWidth >= 870 && currentResponsiveSize !== 'smaller') {
                            newSize = {
                                responsiveSize: 'smaller',
                                width: 320,
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

                    function setBarNames(chartData, meanName, currentName, previousName, calculateRisk, cannotCalculate, previousNotCalcName) {
                        chartData[0].name = meanName;
                        chartData[0].type = 'GENOMSNITT_RISK';
                        if (!chartData[2] || chartData[2].enabled===false) {
                            chartData[1].name = $scope.srs.selectedView==='LATE_EXT'?cannotCalculate:chartData[1].y?currentName:calculateRisk;
                            chartData[1].type = 'RISK';
                        } else if (chartData[2] && chartData[2].enabled===true) {
                            chartData[1].name = chartData[1].y && chartData[1].y>0 ? previousName : previousNotCalcName;
                            chartData[1].type = 'TIDIGARE_RISK';
                            chartData[2].name = $scope.srs.selectedView==='LATE_EXT'?cannotCalculate:chartData[2].y?currentName:calculateRisk;
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
                            setBarNames(chartData.risk.chartData, 'G.', 'A.', 'T.', 'A.', 'K.', 'T.');
                        } else if (responsiveSize === 'smaller' && chartData.risk) {
                            setBarNames(chartData.risk.chartData, 'G.snitt', 'Akt.', 'Tidig.', 'Akt.', 'Kan ej.', 'Tidig.');
                        } else if (responsiveSize === 'normal' && chartData.risk) {
                            setBarNames(chartData.risk.chartData, 'Genomsnittlig risk', 'Aktuell risk', 'Tidigare risk', 'Aktuell risk', 'Kan ej ber.', 'Tidigare ber.');
                        } else if (chartData.risk) {
                            setBarNames(chartData.risk.chartData, 'Genomsnittlig risk', 'Aktuell risk', 'Tidigare risk', 'Aktuell risk', 'Kan ej beräknas', 'Tidigare beräk.');
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
                            return {
                                name: e.name,
                                tooltip: e.tooltip,
                                info: e.info
                            };
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
                        chartOptions.title.text = 'Riskdiagram';
                        chartOptions.title.style.display = 'none';
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
                        $timeout(function() {
                            initToolTip();
                        }, 200);
                        return new Highcharts.Chart(chartOptions);
                    }

                    $scope.$on('$destroy', function() {
                        $window.removeEventListener('resize', onResize);
                        if(riskChart && typeof riskChart.destroy === 'function') {
                            riskChart.destroy();
                        }
                    });
                    $scope.$watch('srs.selectedView', function(newSelectedView, oldSelectedView) {
                        if (newSelectedView === 'LATE_EXT') {
                            if (chartData.risk.chartData[2].enabled === true) {
                                chartData.risk.hiddenRisk = {};
                                Object.assign(chartData.risk.hiddenRisk, chartData.risk.chartData[2]);
                                chartData.risk.chartData[2].enabled = true;
                                chartData.risk.chartData[2].y = 0;
                                chartData.risk.chartData[2].date = null;
                                chartData.risk.chartData[2].daysIntoSickLeave = null;
                                chartData.risk.chartData[2].opinion = null;
                                chartData.risk.chartData[2].info = 'Det går inte att beräkna nuvarande risk - ' +
                                    'sjukskrivningen har pågått över 60 dagar';
                            } else {
                                chartData.risk.hiddenRisk = {};
                                Object.assign(chartData.risk.hiddenRisk, chartData.risk.chartData[1]);
                                chartData.risk.chartData[1].enabled = true;
                                chartData.risk.chartData[1].y = 0;
                                chartData.risk.chartData[1].date = null;
                                chartData.risk.chartData[1].daysIntoSickLeave = null;
                                chartData.risk.chartData[1].opinion = null;
                                chartData.risk.chartData[1].info = 'Det går inte att beräkna nuvarande risk - ' +
                                    'sjukskrivningen har pågått över 60 dagar';
                            }
                        }
                        if (oldSelectedView === 'LATE_EXT' && newSelectedView !== 'LATE_EXT' && chartData.risk.hiddenRisk) {
                            if (chartData.risk.chartData[2].enabled === true) {
                                chartData.risk.chartData[2] = chartData.risk.hiddenRisk;
                            } else {
                                chartData.risk.chartData[1] = chartData.risk.hiddenRisk;
                            }
                        }

                        updateResponsiveDesign();
                        riskChart = paintBarChart('riskChart', chartData.risk.chartData);
                    });


                    function updateWithPredictions(newPredictions) {
                        /*jshint maxcomplexity:12 */

                        // reset any hidden risk prediction when we get new data
                        chartData.risk.hiddenRisk = null;
                        // Reset
                        chartData.risk.chartData.forEach(function(cd) {
                            cd.y=0;
                            cd.enabled = false;
                            cd.info = null;
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
                            chartData.risk.chartData[0].daysIntoSickLeave = null;
                            chartData.risk.chartData[0].opinion = null;
                        }

                        // Previous prediction (if we have a previous prediction newPrediction[1], add it on position 1)
                        // only do this if the first three characters of diagnosis code (the diagnosis group) is the same,
                        // i.e. the main diagnosis hasn't changed since the first certificate
                        if (newPredictions[1]) {
                            chartData.risk.chartData[1].enabled = true;
                            if (newPredictions[1].probabilityOverLimit) {
                                chartData.risk.chartData[1].y = Math.round(newPredictions[1].probabilityOverLimit * 100);
                                chartData.risk.chartData[1].date = newPredictions[1].date;
                                chartData.risk.chartData[1].daysIntoSickLeave = newPredictions[1].daysIntoSickLeave;
                                chartData.risk.chartData[1].opinion = newPredictions[1].opinion;
                            } else {
                                chartData.risk.chartData[1].y = 0;
                                chartData.risk.chartData[1].date = null;
                                chartData.risk.chartData[1].daysIntoSickLeave = null;
                                chartData.risk.chartData[1].opinion = null;
                                if (newPredictions[1].diagnosisCode !== newPredictions[0].diagnosisCode) {
                                    chartData.risk.chartData[1].info = 'På grund av diagnosbyte visas ej tidigare beräknade risker';
                                }
                            }
                            // add current prediction if available
                            if (newPredictions[0].probabilityOverLimit && $scope.srs.selectedView !== 'LATE_EXT') { // if we also have a current prediction in newPrediction[0] add that to the right
                                chartData.risk.chartData[2].enabled = true;
                                chartData.risk.chartData[2].y = Math.round(newPredictions[0].probabilityOverLimit * 100);
                                chartData.risk.chartData[2].date = newPredictions[0].date;
                                chartData.risk.chartData[2].daysIntoSickLeave = newPredictions[0].daysIntoSickLeave;
                                chartData.risk.chartData[2].opinion = newPredictions[0].opinion;
                            } else {
                                chartData.risk.chartData[2].enabled = true;
                                chartData.risk.chartData[2].y = 0;
                                chartData.risk.chartData[2].date = null;
                                chartData.risk.chartData[2].daysIntoSickLeave = null;
                                chartData.risk.chartData[2].opinion = null;
                                if ($scope.srs.selectedView === 'LATE_EXT') {
                                    chartData.risk.chartData[2].info = 'Det går inte att beräkna nuvarande risk - ' +
                                        'sjukskrivningen har pågått över 60 dagar';
                                } else {
                                    chartData.risk.chartData[2].info = 'OBS ingen riskberäkning är gjord';
                                }
                            }
                            // name/title is set via updateResponsiveDesign
                        }
                        // No previous prediction but current (if we don't have a previous one, just add the current at the middle spot)
                        else if (newPredictions[0].probabilityOverLimit) {
                            chartData.risk.chartData[1].enabled = true;
                            chartData.risk.chartData[1].y = Math.round(newPredictions[0].probabilityOverLimit * 100);
                            chartData.risk.chartData[1].date = newPredictions[0].date;
                            chartData.risk.chartData[1].daysIntoSickLeave = newPredictions[0].daysIntoSickLeave;
                            chartData.risk.chartData[1].opinion = newPredictions[0].opinion;
                        }
                        // No prediction
                        else {
                            chartData.risk.chartData[1].enabled = true;
                            chartData.risk.chartData[1].y = 0;
                            // chartData.risk.chartData[1].name = '';
                            chartData.risk.chartData[1].date = null;
                            chartData.risk.chartData[1].daysIntoSickLeave = null;
                            chartData.risk.chartData[1].opinion = null;
                            if ($scope.srs.selectedView === 'LATE_EXT') {
                                chartData.risk.chartData[1].info = 'Det går inte att beräkna nuvarande risk - ' +
                                    'sjukskrivningen har pågått över 60 dagar';
                            } else {
                                chartData.risk.chartData[1].info = 'OBS ingen riskberäkning är gjord';
                            }
                        }

                        dataReceivedSuccess(chartData);
                    }

                    $scope.$watchCollection('srs.predictions', function(newPredictions, oldPredictions) {
                        updateWithPredictions(newPredictions);
                    });

                    // Initialize
                    $timeout(function () {
                        $window.removeEventListener('resize', onResize);
                        $window.addEventListener('resize', onResize);
                    });

                    function initToolTip() {
                        // Recompile the chart element to enable popovers
                        var chartElement = angular.element( document.querySelector( '#riskChart' ) );
                        $compile(chartElement)($scope);
                        // The following lines enables tooltips on labels using jquery
                        // If there is a need for enabling this, make sure jquery is ok to use
                        // $('[data-toggle="tooltip"]').tooltip({
                        //     container: '#srsDiagramLabelTooltip'
                        // });
                    }

                }
        };
} ]);