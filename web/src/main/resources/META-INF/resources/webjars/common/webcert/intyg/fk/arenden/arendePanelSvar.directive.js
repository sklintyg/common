/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
 * Created by BESA on 2015-03-05.
 */

/**
 * arendePanelSvar directive. Handles all komplettering and svar components.
 */
angular.module('common').directive('arendePanelSvar',
    [ '$window', '$log', '$state', '$stateParams',
        'common.ArendeProxy', 'common.ArendeHelper', 'common.statService', 'common.ObjectHelper',
        'common.IntygCopyRequestModel',
        function($window, $log, $state, $stateParams, ArendeProxy, ArendeHelper, statService, ObjectHelper,
            IntygCopyRequestModel) {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                templateUrl: '/web/webjars/common/webcert/intyg/fk/arenden/arendePanelSvar.directive.html',
                scope: {
                    panelId: '@',
                    arendeListItem: '=',
                    parentViewState: '='
                },
                controller: function($scope, $element, $attrs) {

                    $scope.cannotKomplettera = false;

                    /**
                     * Svara på ärende från fk
                     */
                    $scope.sendAnswer = function sendAnswer(arendeListItem) {
                        arendeListItem.updateInProgress = true; // trigger local spinner

                        ArendeProxy.saveAnswer(arendeListItem.arende, 'luse', function(result) {
                            $log.debug('Got saveAnswer result:' + result);
                            arendeListItem.updateInProgress = false;
                            arendeListItem.activeErrorMessageKey = null;
                            if (result !== null) {
                                ArendeHelper.updateArendeListItem(result);
                                // update real item
                                angular.copy(result, arendeListItem);
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            arendeListItem.updateInProgress = false;
                            arendeListItem.activeErrorMessageKey = errorData.errorCode;
                        });
                    };

                    /**
                     * Svara med nytt intyg
                     * @param arendeListItem
                     * @param intyg
                     */
                    $scope.answerWithIntyg = function(arendeListItem, intyg) {

                        if (!ObjectHelper.isDefined(intyg)) {
                            arendeListItem.activeErrorMessageKey = 'komplettera-no-intyg';
                            return;
                        }

                        arendeListItem.updateInProgress = true; // trigger local spinner
                        arendeListItem.activeErrorMessageKey = null;
                        ArendeProxy.answerWithIntyg(arendeListItem.arende, intyg.typ,
                            IntygCopyRequestModel.build({
                                intygId: intyg.id,
                                intygType: intyg.typ,
                                patientPersonnummer: intyg.grundData.patient.personId,
                                nyttPatientPersonnummer: $stateParams.patientId
                            }), function(result) {

                                arendeListItem.updateInProgress = false;
                                arendeListItem.activeErrorMessageKey = null;
                                statService.refreshStat();

                                function goToDraft(type, intygId) {
                                    $state.go(type + '-edit', {
                                        certificateId: intygId
                                    });
                                }

                                goToDraft(intyg.typ, result.intygsUtkastId);

                            }, function(errorData) {
                                // show error view
                                arendeListItem.updateInProgress = false;
                                arendeListItem.activeErrorMessageKey = errorData.errorCode;
                            });
                    };

                    $scope.updateAnsweredAsHandled = function(deferred, unhandledarendes) {
                        if (unhandledarendes === undefined || unhandledarendes.length === 0) {
                            return;
                        }
                        ArendeProxy.closeAllAsHandled(unhandledarendes,
                            function(arendes) {
                                if (arendes) {
                                    angular.forEach(arendes, function(arende) { //unused parameter , key
                                        ArendeHelper.decorateSingleItem(arende);
                                    });
                                    statService.refreshStat();
                                }
                                $window.doneLoading = true;
                                if (deferred) {
                                    deferred.resolve();
                                }
                            }, function() { // unused parameter: errorData
                                // show error view
                                $window.doneLoading = true;
                                if (deferred) {
                                    deferred.resolve();
                                }
                            });
                    };

                    // listeners - interscope communication
                    var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent',
                        function($event, deferred, unhandledarendes) {
                            $scope.updateAnsweredAsHandled(deferred, unhandledarendes);
                        });
                    $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);
                }
            };
        }]);
