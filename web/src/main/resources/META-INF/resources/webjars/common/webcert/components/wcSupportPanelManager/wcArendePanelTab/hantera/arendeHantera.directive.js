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
/**
 * Created by BESA on 2015-05-09.
 */

/**
 * arendeHantera directive. Common directive for Hanterad checkbox
 */
angular.module('common').directive('arendeHantera',
    ['$log', '$rootScope', '$uibModal', '$timeout', 'common.statService', 'common.ErrorHelper', 'common.ArendeProxy',
        'common.ArendeHelper', 'common.dynamicLabelService',
        'common.ResourceLinkService', 'common.messageService', 'common.dialogService',
        function($log, $rootScope, $uibModal, $timeout, statService, ErrorHelper, ArendeProxy, ArendeHelper, dynamicLabelService,
            ResourceLinkService, messageService, dialogService) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/hantera/arendeHantera.directive.html',
                scope: {
                    arendeListItem: '=',
                    parentViewState: '='
                },
                require: '^^arendePanel',
                link: function($scope, $element, $attrs, ArendePanelController) {

                    $scope.showHandleToggle = function() {
                        var arendeModel = $scope.arendeListItem;
                        var isRevoked = $scope.parentViewState.intygProperties.isRevoked;

                        // If no access is given from backend, it should not be possible to toggle handled.
                        if (!hasAccess()) {
                            return false;
                        }

                        //Special case - fraga from FK on revoked intyg and not handled already: allow to mark as handled (see INTYG-3617)
                        if (isRevoked && arendeModel.arende.fraga.frageStallare === 'FK' &&
                            arendeModel.arende.fraga.status !== 'CLOSED') {
                            return true;
                        }

                        //Rule 1: Revoked intyg can't be toggled at all
                        if (isRevoked) {
                            $scope.handledText = 'Hanterad';
                            return false;
                        }

                        //Rule 2: A closed komplettering (answered with new intyg or text message) isn't allowed to be toggled back to
                        // unhandled (See INTYG-6460, INTYG-3792)
                        if (arendeModel.isKomplettering() && arendeModel.arende.fraga.status === 'CLOSED') {
                            return false;
                        }

                        // Enforce default business rule FS-011, from FK + answer should remain closed
                        return arendeModel.arende.fraga.frageStallare === 'WC' || !arendeModel.arende.svar.meddelande;
                    };

                    $scope.showReadOnlyCheckBox = function() {
                        // If no access is given from backend, it should be possible to toggle handled.
                        if (!hasAccess()) {
                            return $scope.arendeListItem.arende.fraga.status === 'CLOSED';
                        } else {
                            return true;
                        }
                    };

                    $scope.handledFunction = function(newState) {
                        if (arguments.length) {
                            if (newState) {
                                if($scope.showReadOnlyCheckBox() && $scope.arendeListItem.isKomplettering()) {
                                    $scope.showHandledConfirmModal();
                                } else {
                                    $scope.updateAsHandled($scope.arendeListItem);
                                }
                            } else {
                                $scope.updateAsUnhandled($scope.arendeListItem);
                            }
                        } else {
                            return $scope.arendeListItem.arende.fraga.status === 'CLOSED';
                        }
                    };

                    var dialogInstance;
                    $scope.showHandledConfirmModal = function() {
                        var arendeListItem = $scope.arendeListItem;
                            dialogInstance = dialogService.showDialog({
                                dialogId: 'modal-confirm-mark-handled',
                                titleId: 'common.arende.atgard.markhandled',
                                bodyText: messageService.getProperty('common.modal.confirm_handled.text'),
                                button1text: 'common.arende.atgard.markhandled',
                                button2text: 'common.cancel',
                                button1click: function() {
                                    _updateAsHandled(arendeListItem);
                                    dialogInstance.close();
                                },
                                button2click: function() {
                                    dialogInstance.close();
                                },
                                autoClose: false,
                                size: 'md'
                            });
                    };

                    $scope.updateAsHandled = function(arendeListItem) {
                        $log.debug('updateAsHandled:' + arendeListItem.arende);

                        if (ArendePanelController.getArendePanelSvar() &&
                            ArendePanelController.getArendePanelSvar().hasSvaraDraft()) {
                            var modalInstance = $uibModal.open({
                                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/hantera/arendeHanteraModal.template.html',
                                size: 'md',
                                controller: function($scope, $uibModalInstance) {
                                    $scope.confirm = function() {
                                        ArendePanelController.getArendePanelSvar().deleteSvaraDraft();
                                        _updateAsHandled(arendeListItem);
                                        $uibModalInstance.close();
                                    };
                                    $scope.abort = function() {
                                        $uibModalInstance.close();
                                    };
                                }
                            });
                            //angular > 1.5 warns if promise rejection is not handled (e.g backdrop-click == rejection)
                            modalInstance.result.catch(function() { //jshint ignore:line
                            }); //jshint ignore:line
                        } else {
                            _updateAsHandled(arendeListItem);
                        }
                    };

                    function hasAccess() {
                        // If no access is given from backend, it should be possible to toggle handled.
                        if ($scope.arendeListItem.isKomplettering()) {
                            return ResourceLinkService.isLinkTypeExists($scope.parentViewState.intygProperties.links,
                                'MARKERA_KOMPLETTERING_SOM_HANTERAD');
                        } else {
                            return ResourceLinkService.isLinkTypeExists($scope.parentViewState.intygProperties.links,
                                'MARKERA_FRAGA_SOM_HANTERAD');
                        }
                    }

                    function _updateAsHandled(arendeListItem) {
                        arendeListItem.updateHandledStateInProgress = true;

                        ArendeProxy.closeAsHandled(arendeListItem.arende.fraga.internReferens,
                            $scope.parentViewState.intygProperties.type, function(result) {
                                arendeListItem.activeErrorMessageKey = null;
                                arendeListItem.updateHandledStateInProgress = false;
                                if (result !== null) {
                                    angular.copy(result, arendeListItem.arende);
                                    arendeListItem.updateArendeListItem();
                                    $rootScope.$broadcast('arenden.updated');

                                    statService.refreshStat();
                                }
                            }, function(errorData) {
                                // show error view
                                arendeListItem.updateHandledStateInProgress = false;
                                arendeListItem.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                            });
                    }

                    $scope.updateAsUnhandled = function(arendeListItem) {
                        $log.debug('updateAsUnHandled:' + arendeListItem);
                        arendeListItem.updateHandledStateInProgress = true; // trigger local

                        ArendeProxy.openAsUnhandled(arendeListItem.arende.fraga.internReferens,
                            $scope.parentViewState.intygProperties.type, function(result) {
                                $log.debug('Got openAsUnhandled result:' + result);
                                arendeListItem.activeErrorMessageKey = null;
                                arendeListItem.updateHandledStateInProgress = false;

                                if (result !== null) {
                                    angular.copy(result, arendeListItem.arende);
                                    arendeListItem.updateArendeListItem();

                                    $rootScope.$broadcast('arenden.updated');

                                    statService.refreshStat();
                                }
                            }, function(errorData) {
                                // show error view
                                arendeListItem.updateHandledStateInProgress = false;
                                arendeListItem.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                            });
                    };

                }
            };
        }]);
