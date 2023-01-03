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
/**
 * Created by BESA on 2015-03-05.
 */

/**
 * arendePanelSvar directive. Handles all komplettering and svar components.
 */
angular.module('common').directive('arendePanelSvar',
    ['$window', '$log', '$rootScope', '$state', '$stateParams', '$q',
        'common.ArendeProxy', 'common.ArendeHelper', 'common.statService', 'common.ObjectHelper', 'common.ErrorHelper',
        'common.IntygCopyRequestModel', 'common.ArendeSvarModel', 'common.FocusElementService',
        'common.ArendeDraftProxy',
        'common.dialogService', 'common.messageService', 'common.ResourceLinkService',
        function($window, $log, $rootScope, $state, $stateParams, $q, ArendeProxy, ArendeHelper, statService,
            ObjectHelper,
            ErrorHelper, IntygCopyRequestModel, ArendeSvarModel, focusElement, ArendeDraftProxy, DialogService,
            messageService, ResourceLinkService) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/panel/svar/arendePanelSvar.directive.html',
                scope: {
                    panelId: '@',
                    arendeList: '=',
                    arendeListItem: '=',
                    parentViewState: '='
                },
                require: '?^^arendePanel',
                link: function($scope, $element, $attrs, ArendePanelController) {

                    // For readability, keep a local struct with the values used from parent scope
                    function _buildArendeSvarModel() {
                        var ArendeSvar = ArendeSvarModel.build($scope.parentViewState, $scope.arendeListItem);
                        $scope.arendeSvar = ArendeSvar;
                        return ArendeSvar;
                    }

                    var ArendeSvar = _buildArendeSvarModel();
                    $scope.$on('arenden.updated', function() {
                        ArendeSvar = _buildArendeSvarModel();
                    });

                    $scope.canAnswer = function() {
                        return $scope.parentViewState.intygProperties.isInteractionEnabled &&
                            ResourceLinkService.isLinkTypeExists($scope.parentViewState.intygProperties.links,
                                'BESVARA_FRAGA') &&
                            ArendeSvar.status === 'PENDING_INTERNAL_ACTION' &&
                            !ArendeSvar.intygProperties.isRevoked &&
                            !$scope.arendeListItem.isKomplettering() &&
                            !$scope.arendeListItem.isPaminnelse();
                    };

                    $scope.showAnswer = function() {
                        // If closed and has a meddelande it is answered by message
                        // If closed and has answeredWithIntyg object it was answered with intyg
                        return (ArendeSvar.status === 'CLOSED' &&
                            (ObjectHelper.isEmpty(ArendeSvar.meddelande) === false ||
                                ObjectHelper.isDefined(ArendeSvar.answeredWithIntyg))) ||
                            (ArendeSvar.status === 'ANSWERED');
                    };

                    // Check if we have a draft meddelande saved
                    $scope.answerPanelOpen = !!ArendeSvar.meddelande && $scope.canAnswer();

                    $scope.openAnswerPanel = function() {
                        $scope.answerPanelOpen = true;
                    };

                    //Disable button if only space and no characters
                    $scope.onlySpace = function(message) {
                        if (message.trim().length > 0) {
                            return false;
                        }
                        return true;
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

                                $scope.arendeListItem.updateArendeListItem();
                                $rootScope.$broadcast('arenden.updated');

                                ArendeSvar.update($scope.parentViewState, $scope.arendeListItem);
                                statService.refreshStat();
                                $scope.answerPanelOpen = false;
                            }
                        }, function(errorData) {
                            // show error view
                            ArendeSvar.updateInProgress = false;
                            ArendeSvar.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                            DialogService.showErrorMessageDialog(
                                messageService.getProperty('common.error.' + ErrorHelper.safeGetError(errorData)),
                                undefined,
                                'common.arende.error.send.title');
                        });
                    };

                    $scope.deleteArendeDraft = function() {
                        DialogService.showDialog({
                            dialogId: 'delete-arende-draft-dialog',
                            titleId: 'common.arende.draft.delete.answer.title',
                            templateUrl: '/app/partials/arende-draft-dialog.html',
                            model: {},
                            button1click: function(modalInstance) {
                                ArendeSvar.meddelande = '';
                                ArendeSvar.draftState = 'normal';
                                ArendeDraftProxy.deleteDraft($scope.parentViewState.intyg.id,
                                    ArendeSvar.fragaInternReferens, function() {
                                    }, function() {
                                    });
                                modalInstance.close();
                                $scope.answerPanelOpen = false;
                            },
                            button2click: function(modalInstance) {
                                modalInstance.close();
                            },
                            button1text: 'common.arende.draft.delete.yes',
                            button2text: 'common.cancel',
                            bodyText: 'common.arende.draft.delete.answer.body',
                            autoClose: false
                        });
                    };

                    $scope.goToIntyg = function() {
                        $state.go('webcert.intyg.' + ArendeSvar.intygProperties.type,
                            {certificateId: ArendeSvar.answeredWithIntyg.intygsId});
                    };

                    if (ArendePanelController) {
                        ArendePanelController.registerArendePanelSvar({
                            hasSvaraDraft: function() {
                                return $scope.answerPanelOpen;
                            },
                            deleteSvaraDraft: function() {
                                ArendeSvar.meddelande = '';
                                ArendeSvar.draftState = 'normal';
                                ArendeDraftProxy.deleteDraft($scope.parentViewState.intyg.id,
                                    ArendeSvar.fragaInternReferens, function() {
                                    }, function() {
                                    });
                                $scope.answerPanelOpen = false;
                            }
                        });
                    }
                }
            };
        }]);
