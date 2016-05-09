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
 * arendePanel directive. Common directive for both unhandled and handled questions/answers
 */
angular.module('common').directive('arendePanel',
    [ '$window', '$log', '$timeout', '$state', '$stateParams',
        'common.User', 'common.ArendeProxy', 'common.statService', 'common.ObjectHelper', 'common.IntygCopyRequestModel',
        function($window, $log, $timeout, $state, $stateParams,
            User, ArendeProxy, statService, ObjectHelper, IntygCopyRequestModel) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                templateUrl: '/web/webjars/common/webcert/intyg/fk/arenden/arendePanel.directive.html',
                scope: {
                    panelId: '@',
                    arendeListItem: '=',
                    arendeList: '=',
                    parentViewState: '='
                },
                controller: function($scope, $element, $attrs) {

                    $scope.cannotKomplettera = false;

                    $scope.handledFunction = function(newState) {
                        if (arguments.length) {
                            if (newState) {
                                $scope.updateAsHandled($scope.arendeListItem);
                            }
                            else {
                                $scope.updateAsUnhandled($scope.arendeListItem);
                            }
                        }
                        else {
                            return $scope.arendeListItem.arende.fraga.status === 'CLOSED';
                        }
                    };

                    $scope.updateAsHandled = function(arendeListItem, deferred) {
                        $log.debug('updateAsHandled:' + arendeListItem.arende);
                        arendeListItem.updateHandledStateInProgress = true;

                        ArendeProxy.closeAsHandled(arendeListItem.arende.fraga.internReferens, $scope.parentViewState.intygProperties.type, function(result) {
                            arendeListItem.activeErrorMessageKey = null;
                            arendeListItem.updateHandledStateInProgress = false;
                            if (result !== null) {
                                angular.copy(result, arendeListItem.arende);
                                arendeListItem.updateArendeListItem();
                                statService.refreshStat();
                            }
                            $window.doneLoading = true;
                            if(deferred) {
                                deferred.resolve();
                            }
                        }, function(errorData) {
                            // show error view
                            arendeListItem.updateHandledStateInProgress = false;
                            arendeListItem.activeErrorMessageKey = errorData.errorCode;
                            $window.doneLoading = true;
                            if(deferred) {
                                deferred.resolve();
                            }
                        });
                    };

                    $scope.updateAsUnhandled = function(arendeListItem) {
                        $log.debug('updateAsUnHandled:' + arendeListItem);
                        arendeListItem.updateHandledStateInProgress = true; // trigger local

                        ArendeProxy.openAsUnhandled(arendeListItem.arende.fraga.internReferens, $scope.parentViewState.intygProperties.type, function(result) {
                            $log.debug('Got openAsUnhandled result:' + result);
                            arendeListItem.activeErrorMessageKey = null;
                            arendeListItem.updateHandledStateInProgress = false;

                            if (result !== null) {
                                angular.copy(result, arendeListItem.arende);
                                arendeListItem.updateArendeListItem();
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            arendeListItem.updateHandledStateInProgress = false;
                            arendeListItem.activeErrorMessageKey = errorData.errorCode;
                        });
                    };
/*
                    /**
                        * Svara på ärende från fk

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
*/
                    /**
                     * Svara med nytt intyg
                     * @param arendeListItem
                     * @param intyg
                     */
                    $scope.answerWithIntyg = function(arendeListItem, intyg) {

                        if(!ObjectHelper.isDefined(intyg)) {
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
/*
                    $scope.onVidareBefordradChange = function(arende) {
                        arende.forwardInProgress = true;

                        fragaSvarCommonService.setVidareBefordradState(arende.internReferens, 'luse', arende.vidarebefordrad,
                            function(result) {
                                arende.forwardInProgress = false;

                                if (result !== null) {
                                    arende.vidarebefordrad = result.vidarebefordrad;
                                } else {
                                    arende.vidarebefordrad = !arende.vidarebefordrad;
                                    dialogService.showErrorMessageDialog('Kunde inte markera/avmarkera frågan som vidarebefordrad. ' +
                                        'Försök gärna igen för att se om felet är tillfälligt. Annars kan du kontakta supporten');
                                }
                            });
                    };

 $scope.hasUnhandledarendes = function(){
 if(!$scope.arendeList || $scope.arendeList.length === 0){
 return false;
 }
 for (var i = 0, len = $scope.arendeList.length; i < len; i++) {
 var arende = $scope.arendeList[i];
 var isUnhandled = fragaSvarCommonService.isUnhandled(arende);
 var fromFk = fragaSvarCommonService.fromFk(arende);
 if(arende.status === 'ANSWERED' || (isUnhandled && fromFk)){
 return true;
 }
 }
 return false;
 };

                    $scope.updateAnsweredAsHandled = function(deferred, unhandledarendes){
                        if(unhandledarendes === undefined || unhandledarendes.length === 0 ){
                            return;
                        }
                        fragaSvarProxy.closeAllAsHandled(unhandledarendes,
                            function(arendes){
                                if(arendes) {
                                    angular.forEach(arendes, function(arende) { //unused parameter , key
                                        fragaSvarCommonService.decorateSingleItem(arende);
                                        //addListMessage(arendes, arende, 'luse.fragasvar.marked.as.hanterad'); // TODOOOOOOOO TEST !!!!!!!!!!
                                    });
                                    statService.refreshStat();
                                }
                                $window.doneLoading = true;
                                if(deferred) {
                                    deferred.resolve();
                                }
                            },function() { // unused parameter: errorData
                                // show error view
                                $window.doneLoading = true;
                                if(deferred) {
                                    deferred.resolve();
                                }
                            });
                    };



                    // Handle vidarebefordra dialog
                    $scope.openMailDialog = function(arende) {
                        // use timeout so that external mail client has a chance to start before showing dialog
                        $timeout(function() {
                            fragaSvarCommonService.handleVidareBefodradToggle(arende, $scope.onVidareBefordradChange);
                        }, 1000);
                        // Launch mail client
                        $window.location = fragaSvarCommonService.buildMailToLink(arende);
                    };

                    // listeners - interscope communication
                    var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent', function($event, deferred, unhandledarendes) {
                        $scope.updateAnsweredAsHandled(deferred, unhandledarendes);
                    });
                    $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);
*/
                }
            };
        }]);
