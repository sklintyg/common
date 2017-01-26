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
    ['$scope', '$timeout',
        'common.UtkastSignService', 'common.UtkastNotifyService', 'common.UtkastValidationService',
        'common.UtkastViewStateService', 'common.UtkastService',
        function($scope, $timeout,
            UtkastSignService, UtkastNotifyService, UtkastValidationService, CommonViewState, UtkastService) {
            'use strict';

            var viewState = $scope.viewState;

            var firstSignAttempt = true;

            /*
             Validation errors (boxes/borders) will not appear in the DOM until clicking 'signera/visa vad som saknas'. This will push existing content in the users current viewport down
             and is very confusing. To fix this, we temporarily fixate the footer while the current angular digestloop renders the validation errors, and then scrolls
             back to bottom of page and un-fixate the footer. Since we want these DOM changes to take effect immediately - we cannot rely on angular using ng-class
             or similar since that would be a part of the digest loop, so it's done with jquery. (see INTYG-3504, INTYG-3508)
             */
            function toggleFixedFooter(enable) {
                $('#utkast-footer').toggleClass('fixed-bottom', enable);
                if (!enable) {
                    $('html, body').scrollTop($(document).height());
                }
            }

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

                    if (firstSignAttempt) {
                        toggleFixedFooter(true);
                    }

                    CommonViewState.setShowComplete();
                    UtkastService.save();
                    //anchorScrollService.scrollTo('bottom');
                    UtkastValidationService.filterValidationMessages();
                    if (firstSignAttempt) {
                        //give current digestloop a chance to render validation errors before scrolling to bottom of page again.
                        $timeout(function() {
                            toggleFixedFooter(false);
                        }, 100);
                        firstSignAttempt = false;
                    }
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

        }
    ]
);
