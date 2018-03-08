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

angular
    .module('common')
    .directive('wcUtkastFooterSign',
    ['common.dynamicLabelService', 'common.UtkastValidationViewState', 'common.UtkastSignService', 'common.UtkastProxy', 'common.featureService', 'common.UtkastFooterService', '$q',
        function(dynamicLabelService, utkastValidationViewState, UtkastSignService, commonUtkastProxy, featureService, UtkastFooterService,  $q) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/wcUtkastFooterSign/wcUtkastFooterSign.directive.html',
                scope: {
                    viewState: '=',
                    certForm: '<'
                },
                controller: function($scope) {
                    var viewState = $scope.viewState;

                    var previousWarningMessage = null;
                    var previousUtkastWarnings = {};
                    var previousIntygWarnings = {};
                    var waitingForSignCompletion = $q.resolve();

                    var messageKey = 'common.' + (isSignAndSendOrKomp() ? 'signsend' : 'sign');

                    $scope.signBtnText = dynamicLabelService.getProperty(messageKey);
                    $scope.signBtnTooltip = dynamicLabelService.getProperty(messageKey + '.tooltip');

                    $scope.getPreviousIntygWarning = function() {
                        return previousWarningMessage;
                    };

                    $scope.isSignAndSendOrKomp = isSignAndSendOrKomp;
                    $scope.getCurrentSignStatus = getCurrentSignStatus;

                    /**
                     * Action to sign the certificate draft and return to Webcert again.
                     */
                    $scope.sign = function() {
                        if(!UtkastFooterService.checkMissing(viewState, $scope.certForm, getCurrentSignStatus())){
                            return;
                        }

                        utkastValidationViewState.reset();

                        waitingForSignCompletion = UtkastSignService.signera(viewState.common.intyg.type, viewState.draftModel.version).then(
                            function(result) {
                                if (result.newVersion) {
                                    viewState.draftModel.version = result.newVersion;
                                }
                            }
                        );
                    };

                    $scope.disableSign = function() {
                        var previousIntyg = false;
                        if (featureService.isFeatureActive(featureService.features.UNIKT_INTYG, viewState.common.intyg.type) &&
                            previousIntygWarnings !== undefined) {

                            previousIntyg = previousIntygWarnings[viewState.common.intyg.type] === false;

                            if (previousIntyg) {
                                previousWarningMessage = viewState.common.intyg.type + '.warn.previouscertificate.differentvg';
                            }
                        }

                        return previousIntyg;
                    };

                    function isSignAndSendOrKomp() {
                        return viewState.common.intyg.isKomplettering || UtkastFooterService.isSignAndSend(viewState);
                    }

                    function getCurrentSignStatus() {
                        // 0: pending, 1: resolved, 2: rejected. Works for later versions of AngularJS.
                        switch(waitingForSignCompletion.$$state.status) {
                        case 0:
                            return 'pending';
                        case 1:
                            return 'resolved';
                        case 2:
                            return 'rejected';
                        default:
                            console.error('Unknown promise state. $q changed internal data representation?');
                        }
                    }

                    $scope.$on('intyg.loaded', function() {
                        previousWarningMessage = {};
                        commonUtkastProxy.getPrevious(viewState.intygModel.grundData.patient.personId, function(existing) {
                            previousUtkastWarnings = existing.utkast;
                            previousIntygWarnings = existing.intyg;

                        });
                    });
                }
            };
        } ]);
