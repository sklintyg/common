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
                    ArendeNewViewState.intygProperties = $scope.intygProperties;
                    $scope.localViewState = ArendeNewViewState;

                    // Create model
                    var arendeNewModel = ArendeNewModel.build();
                    $scope.arendeNewModel = arendeNewModel;

                    /**
                     * Exposed interactions
                     */

                    function isNew() {
                        var notKomplettering = !ArendeNewViewState.intygProperties.kompletteringOnly;
                        var notRevoked = !ArendeNewViewState.intygProperties.isRevoked;
                        var newArendeFormClosed = !ArendeNewViewState.arendeNewOpen;
                        var intygSentOrArendenAvailable = ($scope.parentViewState.common.isIntygOnSendQueue ||
                                                            ArendeNewViewState.intygProperties.isSent ||
                                                            $scope.arendeList.length > 0);

                        return notKomplettering && notRevoked && newArendeFormClosed && intygSentOrArendenAvailable;
                    }

                    function isNotSent() {
                        var notSent = $scope.parentViewState.common.isIntygOnSendQueue === false &&
                                        ArendeNewViewState.intygProperties.isSent === false;

                        return notSent && ($scope.arendeList.length < 1);
                    }

                    function isNoArenden() {
                        return ArendeNewViewState.intygProperties.isSent === undefined &&
                            ($scope.arendeList.length < 1);
                    }

                    $scope.getNewArendeState = function() {
                        var newArendeState = 'none';
                        if(isNew()) {
                            newArendeState = 'new';
                        } else if(isNotSent()) {
                            newArendeState = 'not-sent';
                        } else if(isNoArenden()) {
                            newArendeState = 'no-arenden';
                        }
                        return newArendeState;
                    };

                    $scope.toggleArendeForm = function() {
                        ArendeNewViewState.arendeNewOpen = !ArendeNewViewState.arendeNewOpen;
                        if(ArendeNewViewState.arendeNewOpen) {
                            ArendeNewViewState.focusQuestion = true;
                        }
                        ArendeNewViewState.showSentMessage = false;
                    };

                    $scope.dismissSentMessage = function() {
                        ArendeNewViewState.showSentMessage = false;
                    };

                    $scope.sendNewArende = function() {

                        $log.debug('sendQuestion:' + arendeNewModel);
                        ArendeNewViewState.updateInProgress = true; // trigger local spinner
                        ArendeNewViewState.showSentMessage = false; // reset sent message info box

                        ArendeProxy.sendNewArende($stateParams.certificateId, ArendeNewViewState.intygProperties.intygType, arendeNewModel,
                            function(arendeModel) {
                                
                                $log.debug('Got saveNewQuestion result:' + arendeModel);
                                ArendeNewViewState.updateInProgress = false;
                                ArendeNewViewState.activeErrorMessageKey = null;

                                if (arendeModel !== null) {

                                    // add new arende to open list
                                    $scope.arendeList.push(ArendeHelper.createArendeListItem(arendeModel));

                                    // close form
                                    $scope.toggleArendeForm();

                                    // show message that arende is sent to server
                                    ArendeNewViewState.showSentMessage = true;

                                    // update stats (and bubbles on menu)
                                    statService.refreshStat();
                                }
                            }, function(errorData) {
                                // show error view
                                ArendeNewViewState.updateInProgress = false;
                                ArendeNewViewState.activeErrorMessageKey = errorData.errorCode;
                            });
                    };

                    $scope.isArendeValidForSubmit = function() {
                        var validToSend = arendeNewModel.chosenTopic.value &&
                                            !ObjectHelper.isEmpty(arendeNewModel.frageText) &&
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
