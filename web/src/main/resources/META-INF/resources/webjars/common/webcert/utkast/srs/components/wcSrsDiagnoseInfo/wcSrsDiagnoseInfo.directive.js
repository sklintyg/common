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
/**
 * Displays srs recommendations and statistic image for the given diagnose code.
 */
angular.module('common').directive('wcSrsDiagnoseInfo',
        [ '$log', 'common.messageService', 'common.srsProxy', 'common.srsLinkCreator', function($log, messageService, srsProxy, srsLinkCreator) {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    config: '='
                },
                templateUrl: '/web/webjars/common/webcert/utkast/srs/components/wcSrsDiagnoseInfo/wcSrsDiagnoseInfo.directive.html',
                link: function($scope) {

                    //Set up initial state
                    var msg = messageService.getProperty;

                    var viewState = {
                        doneLoading: false,
                        mainMessageKey: null,
                        atgarderStatusMessage: null,
                        atgarderLink: null,
                        statistikStatusMessage: null,
                        statistikLink: null,
                        srsdata: {}
                    };
                    //Expose viewState to template
                    $scope.viewState = viewState;


                    function _updateStatusMessages() {

                        //Both missing..
                        if (viewState.srsdata.atgarderStatusCode === 'INFORMATION_SAKNAS' && viewState.srsdata.statistikStatusCode === 'STATISTIK_SAKNAS') {
                            viewState.mainMessageKey = {key: 'srs.srsfordiagnose.load.nodata', type: 'info' };
                        } else {

                            //Atgarder missing?
                            if (viewState.srsdata.atgarderStatusCode === 'INFORMATION_SAKNAS') {
                                viewState.atgarderStatusMessage = msg('srs.srsfordiagnose.atgarder.missing');
                            } else {
                                viewState.atgarderLink = srsLinkCreator.createAtgardsrekommendationLink(viewState.srsdata.diagnosisCode);
                            }
                            //Atgarder on higher level diagnose?
                            if (viewState.srsdata.atgarderStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                                viewState.atgarderStatusMessage = msg('srs.srsfordiagnose.atgarder.highercode', {code: viewState.srsdata.diagnosisCode});
                            }

                            //Statistik missing?
                            if (viewState.srsdata.statistikStatusCode === 'STATISTIK_SAKNAS') {
                                viewState.statistikStatusMessage = msg('srs.srsfordiagnose.statistik.missing');
                            } else {
                                viewState.statistikLink = srsLinkCreator.createStatistikLink(viewState.srsdata.diagnosisCode);
                            }

                        }



                    }

                    function _loadSrs() {
                        $log.debug('getSrsForDiagnoseOnly : ' + $scope.config.diagnoseCode + ' =>');
                        viewState.doneLoading = false;
                        srsProxy.getSrsForDiagnoseOnly($scope.config.diagnoseCode).then(function(result) {
                            $log.debug('<=  getSrsForDiagnoseOnly success!');
                            viewState.srsdata = result.data;
                        }, function(errorResult, status) {
                            $log.error('<= getSrsForDiagnoseOnly failed with status: ' + status);
                            viewState.mainMessageKey = {key: 'srs.srsfordiagnose.load.error', type: 'danger' };

                        }).finally(function() {     // jshint ignore:line
                            viewState.doneLoading = true;
                            _updateStatusMessages();
                        });
                    }


                    _loadSrs();

                }
            };
        } ]);
