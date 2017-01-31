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
        'common.UtkastViewStateService', 'common.UtkastService',
        function($scope, $rootScope, $timeout,
            UtkastSignService, UtkastNotifyService, UtkastValidationService, CommonViewState, UtkastService) {
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

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                if(!$scope.checkMissing()){
                    return;
                }

                UtkastSignService.signera(viewState.common.intyg.type, viewState.draftModel.version).then(
                    function(result) {
                        if (result.newVersion) {
                            viewState.draftModel.version = result.newVersion;
                        }
                    }
                );
            };

            /**
             * Whenever a validation round is completed, scroll (back) to bottom if the current element with
             * focus is the signera button - i.e we must just have clicked it.
             *
             */
            var unbindFastEvent = $rootScope.$on('validation.messages-updated', function (event) {
                if($(':focus').attr('id') === 'signera-utkast-button') {
                    $timeout(function() {
                        $('html, body').scrollTop($(document).height());
                    }, 200);
                }
            });
            $scope.$on('$destroy', unbindFastEvent);

        }
    ]
);
