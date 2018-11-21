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
 * arendeNew directive. Common directive for new arende form.
 */
angular.module('common').directive('arendeNew',
    [ '$window', '$log', '$timeout', '$state', '$stateParams', '$rootScope', '$uibModal', 'common.User', 'common.statService', 'common.ObjectHelper',
        'common.ErrorHelper', 'common.ArendeProxy', 'common.ArendeNewModel', 'common.ArendeNewViewStateService', 'common.ArendeHelper',
        'common.ArendeListItemModel', 'common.ArendeDraftProxy', 'common.dialogService',
        function($window, $log, $timeout, $state, $stateParams, $rootScope, $uibModal, User, statService, ObjectHelper, ErrorHelper, ArendeProxy,
            ArendeNewModel, ArendeNewViewStateService, ArendeHelper, ArendeListItemModel, ArendeDraftProxy, DialogService) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/new/arendeNew.directive.html',
                scope: {
                    parentViewState: '='
                },
                controller: function($scope, $element, $attrs) {

                    // Create viewstate
                    var ArendeNewViewState = ArendeNewViewStateService.reset();
                    ArendeNewViewState.parentViewState = $scope.parentViewState;
                    $scope.localViewState = ArendeNewViewState;

                    // Create model
                    var arendeNewModel = ArendeNewModel.build($scope.parentViewState.intygProperties.type);
                    $scope.arendeNewModel = arendeNewModel;
                    var intygLoaded = false;

                    var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', onIntygLoaded);
                    $scope.$on('$destroy', unbindFastEvent);

                    function onIntygLoaded(event, intyg, intygProperties) {

                        if (intyg !== null && !isNotSent()) {
                            ArendeDraftProxy.getDraft(intyg.id, function(data) {
                                if (data.text !== undefined) {
                                    $scope.arendeNewModel.frageText = data.text;
                                }
                                if (data.amne !== undefined) {
                                    angular.forEach($scope.arendeNewModel.topics, function(topic) {
                                        if (topic.value === data.amne) {
                                            $scope.arendeNewModel.chosenTopic = topic.id;
                                            return;
                                        }
                                    });
                                }
                            }, function(data) {
                            });
                        }

                        intygLoaded = true;
                    }

                    onIntygLoaded(null, ArendeNewViewState.parentViewState.intyg, ArendeNewViewState.parentViewState.intygProperties);

                    function isNotSent() {
                        var notSent = ArendeNewViewState.parentViewState.intygProperties.isSent === false;

                        return notSent && (ArendeNewViewState.parentViewState.arendeList.length < 1);
                    }

                    /**
                     * Exposed interactions
                     */

                    $scope.showCreateArende = function() {
                        var notRevoked = !ArendeNewViewState.parentViewState.intygProperties.isRevoked;
                        return intygLoaded && notRevoked;
                    };

                    $scope.cancelQuestion = function() {
                        DialogService.showDialog({
                            dialogId: 'delete-arende-draft-dialog',
                            titleId: 'common.arende.draft.delete.question.title',
                            templateUrl: '/app/partials/arende-draft-dialog.html',
                            model: {},
                            button1click: function (modalInstance) {
                                var onSuccess = function() {
                                    arendeNewModel.reset();
                                    modalInstance.close();
                                };
                                var onError = function() {
                                    modalInstance.close();
                                };
                                ArendeDraftProxy.deleteQuestionDraft(ArendeNewViewState.parentViewState.intyg.id, onSuccess, onError);
                            },
                            button2click: function(modalInstance){
                                modalInstance.close();
                            },
                            button1text: 'common.arende.draft.delete.yes',
                            button2text: 'common.cancel',
                            bodyText: 'common.arende.draft.delete.question.body',
                            autoClose: false
                        });
                    };

                    $scope.sendNewArende = function() {
                        if (arendeNewModel.frageText) {
                            _sendNewArende();
                        }
                        else {
                            var modalInstance = $uibModal.open({
                                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/new/arendeNewModal.template.html',
                                size: 'md',
                                controller: function($scope, $uibModalInstance) {
                                    $scope.confirm = function() {
                                        _sendNewArende();
                                        $uibModalInstance.close();
                                    };
                                    $scope.abort = function() {
                                        $uibModalInstance.close();
                                    };
                                }
                            });
                            //angular > 1.5 warns if promise rejection is not handled (e.g backdrop-click == rejection)
                            modalInstance.result.catch(function () {}); //jshint ignore:line
                        }
                    };

                    function _sendNewArende() {
                        $log.debug('sendQuestion:' + arendeNewModel);
                        ArendeNewViewState.updateInProgress = true; // trigger local spinner

                        ArendeProxy.sendNewArende($stateParams.certificateId, ArendeNewViewState.parentViewState.intygProperties.type, arendeNewModel,
                            function(arendeModel) {

                                $log.debug('Got saveNewQuestion result:' + arendeModel);
                                ArendeNewViewState.updateInProgress = false;
                                ArendeNewViewState.activeErrorMessageKey = null;

                                if (arendeModel !== null) {

                                    // add new arende to open list
                                    var ArendeListViewState = ArendeNewViewState.parentViewState;
                                    ArendeListViewState.arendeList.push(ArendeListViewState.createArendeListItem(arendeModel,
                                            ArendeListViewState.intygProperties.type));

                                    arendeNewModel.reset();

                                    // update stats (and bubbles on menu)
                                    statService.refreshStat();
                                    $rootScope.$broadcast('arenden.updated');
                                }
                            }, function(errorData) {
                                // show error view
                                ArendeNewViewState.updateInProgress = false;
                                ArendeNewViewState.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                            });
                    }

                    $scope.isArendeValidForSubmit = function() {
                        var validToSend = (arendeNewModel.chosenTopic && !ArendeNewViewState.updateInProgress);
                        return validToSend;
                    };

                    // Returns false if either a topic has been chosen or a frageText exists. May not be invoked during
                    // updateInProgress just like the submit above.
                    $scope.isArendeNonCancellable = function() {
                        var notValidToCancel = !((arendeNewModel.chosenTopic || arendeNewModel.frageText) && !ArendeNewViewState.updateInProgress);
                        return notValidToCancel;
                    };
                }
            };
        }]);
