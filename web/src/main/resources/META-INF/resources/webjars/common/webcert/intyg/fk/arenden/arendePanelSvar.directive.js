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

                    function initArendeSvar() {
                        return {
                            cannotKomplettera: false
                        };
                    }

                    function updateArendeSvar(ArendeSvar) {
                        ArendeSvar.enhetsId = $scope.parentViewState.intygModel.grundData.skapadAv.vardenhet.enhetsid;

                        ArendeSvar.intygProperties = $scope.parentViewState.intygProperties;

                        ArendeSvar.answerDisabled = $scope.arendeListItem.answerDisabled;
                        ArendeSvar.answerDisabledReason = $scope.arendeListItem.answerDisabledReason;
                        ArendeSvar.svaraMedNyttIntygDisabled = $scope.arendeListItem.svaraMedNyttIntygDisabled;
                        ArendeSvar.svaraMedNyttIntygDisabledReason = $scope.arendeListItem.svaraMedNyttIntygDisabledReason;

                        ArendeSvar.amne = $scope.arendeListItem.arende.fraga.amne;
                        ArendeSvar.status = $scope.arendeListItem.arende.fraga.status;
                        ArendeSvar.frageStallare = $scope.arendeListItem.fraga.frageStallare;
                        ArendeSvar.vardAktorNamn = $scope.arendeListItem.fraga.vardAktorNamn;

                        ArendeSvar.meddelande = $scope.arendeListItem.arende.svar.meddelande;
                        ArendeSvar.internReferens = $scope.arendeListItem.arende.svar.internReferens;
                        ArendeSvar.svarSkickadDatum = $scope.arendeListItem.arende.svar.svarSkickadDatum;
                    }

                    // For readability, keep a local struct with the values used from parent scope
                    var ArendeSvar = initArendeSvar();
                    updateArendeSvar(ArendeSvar);
                    $scope.arendeSvar = ArendeSvar;

                    /*$scope.$watch('[arendeListItem, parentViewState]', function(newVal, oldVal) {
                        updateArendeSvar(ArendeSvar);
                    });*/

                    $scope.showAnswerPanel = function() {
                        return ArendeSvar.intygProperties.kompletteringOnly ||
                            (ArendeSvar.amne !== 'KOMPLT') ||
                            (ArendeSvar.amne === 'KOMPLT' && (ArendeSvar.cannotKomplettera ||
                                (ArendeSvar.meddelande && ArendeSvar.status === 'CLOSED')));
                    };

                    $scope.showAnswer = function() {
                        return (ArendeSvar.status === 'CLOSED' && ArendeSvar.meddelande) || (ArendeSvar.status === 'ANSWERED');
                    };

                    $scope.showKompletteringButtons = function() {
                        return !ArendeSvar.intygProperties.kompletteringOnly &&
                            $scope.isAnswerAllowed() &&
                            ArendeSvar.amne === 'KOMPLT';
                    };

                    $scope.showSvaraMedNyttIntygButton = function() {
                        return !ArendeSvar.svaraMedNyttIntygDisabled && ArendeSvar.status !== 'CLOSED';
                    };

                    $scope.showRegularAnswer = function() {
                        return $scope.isAnswerAllowed() && !ArendeSvar.cannotKomplettera;
                    };

                    $scope.showKompletteringWarning = function() {
                        return $scope.isAnswerAllowed() && ArendeSvar.cannotKomplettera;
                    };

                    $scope.showButtonBar = function() {
                        // VÄNTAR på svar från Vårdenheten och det är inte kompletteringsvy vi renderar
                        return !ArendeSvar.intygProperties.kompletteringOnly && ArendeSvar.status === 'PENDING_INTERNAL_ACTION';
                    };

                    $scope.isAnswerAllowed = function() {
                        return !ArendeSvar.answerDisabled && !ArendeSvar.intygProperties.isRevoked;
                    };

                    /**
                     * Svara på ärende från fk
                     */
                    $scope.sendAnswer = function sendAnswer() {
                        ArendeSvar.updateInProgress = true; // trigger local spinner

                        ArendeProxy.saveAnswer($scope.arendeListItem.arende, ArendeSvar.intygProperties.typ, function(result) {
                            $log.debug('Got saveAnswer result:' + result);
                            ArendeSvar.updateInProgress = false;
                            ArendeSvar.activeErrorMessageKey = null;
                            if (result !== null) {
                                ArendeHelper.updateArendeListItem(result);
                                // update real item
                                angular.copy(result, $scope.arendeListItem);
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            ArendeSvar.updateInProgress = false;
                            ArendeSvar.activeErrorMessageKey = errorData.errorCode;
                        });
                    };

                    /**
                     * Svara med nytt intyg
                     * @param arendeListItem
                     * @param intyg
                     */
                    $scope.answerWithIntyg = function() {

                        if (!ObjectHelper.isDefined(ArendeSvar.intygProperties)) {
                            ArendeSvar.activeErrorMessageKey = 'komplettera-no-intyg';
                            return;
                        }

                        ArendeSvar.updateInProgress = true; // trigger local spinner
                        ArendeSvar.activeErrorMessageKey = null;
                        ArendeProxy.answerWithIntyg($scope.arendeListItem.arende, ArendeSvar.intygProperties.typ,
                            IntygCopyRequestModel.build({
                                intygId: intyg.id,
                                intygType: ArendeSvar.intygProperties.typ,
                                patientPersonnummer: intyg.grundData.patient.personId,
                                nyttPatientPersonnummer: $stateParams.patientId
                            }), function(result) {

                                ArendeSvar.updateInProgress = false;
                                ArendeSvar.activeErrorMessageKey = null;
                                statService.refreshStat();

                                function goToDraft(type, intygId) {
                                    $state.go(type + '-edit', {
                                        certificateId: intygId
                                    });
                                }

                                goToDraft(intyg.typ, result.intygsUtkastId);

                            }, function(errorData) {
                                // show error view
                                ArendeSvar.updateInProgress = false;
                                ArendeSvar.activeErrorMessageKey = errorData.errorCode;
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
