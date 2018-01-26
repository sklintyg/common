/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
    [ '$window', '$log', '$state', '$stateParams', '$q',
        'common.ArendeProxy', 'common.ArendeHelper', 'common.statService', 'common.ObjectHelper', 'common.ErrorHelper',
        'common.IntygCopyRequestModel', 'common.ArendeSvarModel', 'common.FocusElementService', 'common.ArendeDraftProxy',
        'common.dialogService',
        function($window, $log, $state, $stateParams, $q, ArendeProxy, ArendeHelper, statService, ObjectHelper,
            ErrorHelper, IntygCopyRequestModel, ArendeSvarModel, focusElement, ArendeDraftProxy, DialogService) {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                templateUrl: '/web/webjars/common/webcert/fk/arenden/panel/svar/arendePanelSvar.directive.html',
                scope: {
                    panelId: '@',
                    arendeList: '=',
                    arendeListItem: '=',
                    parentViewState: '='
                },
                controller: function($scope, $element, $attrs) {

                    // For readability, keep a local struct with the values used from parent scope
                    var ArendeSvar = ArendeSvarModel.build($scope.parentViewState, $scope.arendeListItem);
                    $scope.arendeSvar = ArendeSvar;

                    $scope.showAnswerPanel = function() {
                        if (ArendeSvar.amne === 'KOMPLT' && ArendeSvar.answerKompletteringWithText === false && ArendeSvar.status === 'PENDING_INTERNAL_ACTION') {
                            return false;
                        }

                        var hasMeddelandeIsClosed = ObjectHelper.isEmpty(ArendeSvar.meddelande) === false && ArendeSvar.status === 'CLOSED';
                        var cannotKomplettera = ArendeSvar.answerKompletteringWithText || hasMeddelandeIsClosed;
                        return (ArendeSvar.amne !== 'KOMPLT') ||
                            (ArendeSvar.amne === 'KOMPLT' && (cannotKomplettera || ObjectHelper.isDefined(ArendeSvar.answeredWithIntyg)));
                    };

                    $scope.showAnswer = function() {
                        // If closed and has a meddelande it is answered by message
                        // If closed and has answeredWithIntyg object it was answered with intyg
                        return (ArendeSvar.status === 'CLOSED' &&
                            (ObjectHelper.isEmpty(ArendeSvar.meddelande) === false || ObjectHelper.isDefined(ArendeSvar.answeredWithIntyg))) ||
                            (ArendeSvar.status === 'ANSWERED');
                    };

                    $scope.showKompletteringControls = function() {
                        return $scope.isAnswerAllowed() &&
                            ArendeSvar.amne === 'KOMPLT' && !ArendeSvar.answerKompletteringWithText;
                    };

                    $scope.showButtonBar = function() {
                        // VÄNTAR på svar från Vårdenheten
                        return ArendeSvar.status === 'PENDING_INTERNAL_ACTION';
                    };

                    $scope.isAnswerAllowed = function() {
                        return !ArendeSvar.answerDisabled && !ArendeSvar.intygProperties.isRevoked;
                    };

                    /**
                     * Svara på ärende från fk
                     */
                    $scope.sendAnswer = function sendAnswer() {
                        ArendeSvar.updateInProgress = true; // trigger local spinner

                        ArendeProxy.saveAnswer(ArendeSvar, ArendeSvar.intygProperties.type, function(result) {
                            $log.debug('Got saveAnswer result:' + result);
                            ArendeSvar.updateInProgress = false;
                            ArendeSvar.activeErrorMessageKey = null;
                            if (result !== null) {
                                // update real item
                                angular.copy(result, $scope.arendeListItem.arende);

                                if ($scope.arendeListItem.extraKompletteringarArenden.length > 0) {
                                    angular.forEach($scope.arendeListItem.extraKompletteringarArenden,
                                        function(listItem) {
                                            listItem.arende.fraga.status = result.fraga.status;
                                            // Update kompletteringar in the common intyg view state
                                            $scope.parentViewState.updateKompletteringarArende(listItem.arende);
                                        });
                                    ArendeHelper.splitAllToSingleItems($scope.arendeListItem, $scope.arendeList);
                                }
                                $scope.arendeListItem.updateArendeListItem(result);
                                $scope.parentViewState.updateKompletteringarArende($scope.arendeListItem.arende);

                                ArendeSvar.update($scope.parentViewState, $scope.arendeListItem);
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            ArendeSvar.updateInProgress = false;
                            ArendeSvar.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                        });
                    };

                    $scope.answerWithMessage = function() {
                        ArendeSvar.answerKompletteringWithText = true;
                        focusElement('answerText-' + ArendeSvar.fragaInternReferens);
                    };

                    $scope.abortTextAnswer = function() {
                        //Should we empty the svarstext input field?
                        ArendeSvar.answerKompletteringWithText = false;
                    };

                    $scope.deleteArendeDraft = function() {
                        DialogService.showDialog({
                            dialogId: 'delete-arende-draft-dialog',
                            titleId: 'common.arende.draft.delete.answer.title',
                            templateUrl: '/app/partials/arende-draft-dialog.html',
                            model: {},
                            button1click: function (modalInstance) {
                                ArendeSvar.meddelande = '';
                                ArendeSvar.draftState = 'normal';
                                ArendeDraftProxy.deleteDraft($scope.parentViewState.intyg.id, ArendeSvar.fragaInternReferens, function() {}, function() {});
                                modalInstance.close();
                            },
                            button2click: function(modalInstance){
                                modalInstance.close();
                            },
                            button1text: 'common.arende.draft.delete.yes',
                            button2text: 'common.cancel',
                            bodyText: 'common.arende.draft.delete.answer.body',
                            autoClose: false
                        });
                    };

                    $scope.goToIntyg = function() {
                        $state.go('webcert.intyg.fk.' + ArendeSvar.intygProperties.type, {certificateId: ArendeSvar.answeredWithIntyg.intygsId});
                    };
                }
            };
        }]);
