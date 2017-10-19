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

angular.module('common').controller('common.UtkastFooter',
    ['$scope', '$rootScope', '$timeout',
        'common.UtkastSignService', 'common.UtkastNotifyService', 'common.UtkastValidationService',
        'common.UtkastViewStateService', 'common.UtkastService', 'common.UtkastValidationViewState',
        'common.featureService',
        function($scope, $rootScope, $timeout,
            UtkastSignService, UtkastNotifyService, UtkastValidationService, CommonViewState, UtkastService,
            utkastValidationViewState, featureService) {
            'use strict';

            var viewState = $scope.viewState;

            /**
             * Handle vidarebefordra dialog
             */
            $scope.vidarebefordraUtkast = function() {
                UtkastNotifyService.notifyUtkast(viewState.intygModel.id, viewState.common.intyg.type,
                    viewState.draftModel, viewState.common);
            };

            $scope.onVidarebefordradChange = function() {
                UtkastNotifyService.onNotifyChange(viewState.intygModel.id, viewState.common.intyg.type,
                    viewState.draftModel, viewState.common);
            };

            /**
             * Handle notifieraUtkast, dvs. notifering till journalsystem via statusuppdatering
             */
            $scope.notifieraUtkast = function() {
                    UtkastNotifyService.notifyJournalsystem(viewState.intygModel.id, viewState.common.intyg.type,
                        viewState.draftModel, viewState.common, function() {
                            viewState.klartForSigneringDatum = true;
                        });
            };

            /**
             * Handle the problem of jumping /scolling of content in regard to clicking sign/visa fel.
             * We need to store the buttons position asap (mousedown) because validation triggered onblur will
             * change the DOM before the ng-click (mouse-down+ some time + mouseup = click) event happens.
             */
            var savedElementTop  = 0;
            var lastClickedButtonId = null;

            $scope.initValidationSequence = function(btnId) {
                lastClickedButtonId = btnId;
                //Save the current top of the button clicked
                savedElementTop = $('#' + lastClickedButtonId).offset().top - $(window).scrollTop();
            };

            $scope.checkMissing = function() {

                if($scope.signingWithSITHSInProgress){
                    return false;
                }

                if(!viewState.common.intyg.isComplete || $scope.certForm.$dirty){

                    CommonViewState.setShowComplete();
                    UtkastService.save();
                    UtkastValidationService.filterValidationMessages();

                    return false;
                }

                return true;
            };

            $scope.isSignAndSend = function() {
                return featureService.isFeatureActive(featureService.features.SIGNERA_SKICKA_DIREKT, viewState.common.intyg.type);
            };

            $scope.isSignAndSendOrKomp = function() {
                return viewState.common.intyg.isKomplettering || $scope.isSignAndSend();
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                if(!$scope.checkMissing()){
                    return;
                }

                utkastValidationViewState.reset();

                UtkastSignService.signera(viewState.common.intyg.type, viewState.draftModel.version).then(
                    function(result) {
                        if (result.newVersion) {
                            viewState.draftModel.version = result.newVersion;
                        }
                    }
                );
            };

            /**
             * Whenever a validation round is completed, either directly by clicking a button or by bluring a validated field -
             * scroll (back) to where we were before 'content-changed-above' scrolling occurred.
             */
            var unbindFastEvent = $rootScope.$on('validation.messages-updated', function () {
                var focusedElement = $(':focus');
                var focusedElementId = focusedElement.attr('id');
                if(lastClickedButtonId === focusedElementId) {
                    //Need a timeout here so that the focused button has appeared in it's new position
                    $timeout(function() {
                        if (savedElementTop > 0) {
                            //restore scroll position
                            $(window).scrollTop(focusedElement.offset().top - savedElementTop);
                        }
                    }, 200);
                }
            });
            $scope.$on('$destroy', unbindFastEvent);

        }
    ]
);
