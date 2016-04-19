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
 * arendeNew directive. Common directive for new arende form.
 */
angular.module('common').directive('arendeNew',
    [ '$window', '$log', '$timeout', '$state', '$stateParams',
        'common.User', 'common.statService', 'common.ObjectHelper',
        'common.ArendeProxy', 'common.ArendeNewModel', 'common.ArendeNewViewStateService', 'common.ArendeHelper', 'common.ArendeListItemModel',
        function($window, $log, $timeout, $state, $stateParams,
            User, statService, ObjectHelper,
                 ArendeProxy, ArendeNewModel, ArendeNewViewStateService, ArendeHelper, ArendeListItemModel) {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                templateUrl: '/web/webjars/common/webcert/intyg/fk/arenden/arendeNew.directive.html',
                scope: {
                    arendeList: '=',
                    parentViewState: '=',
                    intygProperties: '='
                },
                controller: function($scope, $element, $attrs) {

                    // Create viewstate
                    var ArendeNewViewState = ArendeNewViewStateService.reset();
                    $scope.localViewState = ArendeNewViewState;

                    // Create modelF
                    var ArendeNewModel = ArendeNewModel.build();
                    $scope.arendeNewModel = ArendeNewModel;

                    /**
                     * Exposed interactions
                     */

                    $scope.getNewArendeState = function() {
                        var newArendeState = 'none';
                        if(!intygProperties.kompletteringOnly && !intygProperties.isRevoked && !ArendeNewViewState.arendeNewOpen && (ArendeNewViewState.isIntygOnSendQueue || intygProperties.isSent || arendeList.length > 0)) {
                            newArendeState = 'new';
                        } else if(ArendeNewViewState.isIntygOnSendQueue === false && intygProperties.isSent === false && (arendeList.length < 1)) {
                            newArendeState = 'not-sent';
                        } else if(intygProperties.isSent === undefined && (arendeList.length < 1)) {
                            newArendeState = 'no-arenden';
                        }
                        return newArendeState;
                    };

                    $scope.toggleQuestionForm = function() {
                        ArendeNewViewState.arendeNewOpen = !ArendeNewViewState.arendeNewOpen;
                        if(ArendeNewViewState.arendeNewOpen) {
                            ArendeNewViewState.focusQuestion = true;
                        }
                        ArendeNewViewState.showSentMessage = false;
                    };
                    
                    $scope.sendNewArende = function () {
                        $log.debug('sendQuestion:' + ArendeNewModel);
                        ArendeNewViewState.updateInProgress = true; // trigger local spinner
                        ArendeNewViewState.showSentMessage = false; // reset sent message info box

                        ArendeProxy.sendNewArende($stateParams.certificateId, intygType, ArendeNewModel,
                            function(arendeModel) {
                                $log.debug('Got saveNewQuestion result:' + arendeModel);
                                ArendeNewViewState.updateInProgress = false;
                                ArendeNewViewState.activeErrorMessageKey = null;
                                if (arendeModel !== null) {
                                    //$scope.activeQA = result.internReferens;
                                    // close question form
                                    // result is a new FragaSvar Instance: add it to our local repo

                                    var arendeListItem = ArendeListItemModel.build();
                                    arendeListItem.arende = arendeModel;
                                    ArendeHelper.updateArendeListItem(arendeListItem);
                                    $scope.arendeList.push(arendeListItem);

                                    // close form
                                    $scope.toggleQuestionForm();
                                    ArendeNewViewState.showSentMessage = true;

                                    // update stats
                                    statService.refreshStat();
                                }
                            }, function(errorData) {
                                // show error view
                                ArendeNewViewState.updateInProgress = false;
                                ArendeNewViewState.activeErrorMessageKey = errorData.errorCode;
                            });
                    };

                    $scope.isArendeValidForSubmit = function() {
                        var validToSend = ArendeNewModel.chosenTopic.value &&
                            !ObjectHelper.isEmpty(ArendeNewModel.frageText) &&
                            !ArendeNewViewState.updateInProgress;

                        ArendeNewViewState.sendButtonToolTip = 'Skicka frågan';
                        if (!validToSend) {
                            ArendeNewViewState.sendButtonToolTip =
                                'Du måste välja ett ämne och skriva en frågetext innan du kan skicka frågan';
                        }
                        return validToSend;
                    };
                }
            };
        }]);
