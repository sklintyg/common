/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
    ['$q', '$stateParams', 'common.dynamicLabelService', 'common.UtkastValidationViewState', 'common.UtkastSignService', 'common.UtkastProxy', 'common.featureService', 'common.UtkastFooterService',
        'common.messageService', 'common.moduleService', 'common.dialogService', 'common.ResourceLinkService',
        function($q, $stateParams, dynamicLabelService, utkastValidationViewState, UtkastSignService, commonUtkastProxy, featureService, UtkastFooterService,
            messageService, moduleService, dialogService, ResourceLinkService) {
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
                    var messageKey = 'common.';

                    if (viewState.common.intyg.isKomplettering) {
                        messageKey += 'signsend.completion';
                    }
                    else if (isSignAndSend()) {
                        messageKey += 'signsend';
                    }
                    else {
                        messageKey += 'sign';
                    }

                    $scope.signBtnText = dynamicLabelService.getProperty(messageKey);
                    $scope.signBtnTooltip = messageService.getProperty(messageKey + '.tooltip', {
                        'recipient': messageService.getProperty('common.recipient.' + moduleService.getModule(viewState.common.intyg.type).defaultRecipient.toLowerCase())
                    });

                    $scope.getPreviousIntygWarning = function() {
                        return previousWarningMessage;
                    };

                    $scope.isSignAndSend = isSignAndSend;
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
                        if(featureService.isFeatureActive(featureService.features.UNIKT_INTYG, viewState.common.intyg.type) ||
                            featureService.isFeatureActive(featureService.features.UNIKT_INTYG_INOM_VG, viewState.common.intyg.type)){
                            validateSigning();
                        } else {
                            doSignRequest();
                        }
                    };

                    function doSignRequest() {
                        // After signing is done, there is no access to links when evaluating if the receiver dialog should be displayed.
                        $stateParams.accessToApproveReceivers = ResourceLinkService.isLinkTypeExists(viewState.draftModel.links, 'GODKANNA_MOTTAGARE');
                        waitingForSignCompletion = UtkastSignService.signera(viewState.common.intyg.type, viewState.draftModel.version).then(
                            function(result) {
                                if (result.newVersion) {
                                    viewState.draftModel.version = result.newVersion;
                                }
                            }
                        );
                    }

                    function validateSigning() {
                        getPreviousIntyg(function() {
                            var previousCertificate = previousIntygWarnings[viewState.common.intyg.type];
                            var previousDraft = previousUtkastWarnings[viewState.common.intyg.type];

                            var message = '';
                            var title = '';
                            if (featureService.isFeatureActive(featureService.features.UNIKT_INTYG, viewState.common.intyg.type) &&
                                previousCertificate && !previousCertificate.sameVardgivare) {
                                message = viewState.common.intyg.type + '.warn.previouscertificate.differentvg.sign';
                                title = viewState.common.intyg.type + '.warn.previouscertificate.title';
                                showSignNotPossibleModal(message, title);
                                return;
                            } else if (previousCertificate && !previousCertificate.sameVardgivare) {
                                message = viewState.common.intyg.type + '.warn.previouscertificate.differentvg';
                                title = viewState.common.intyg.type + '.warn.previouscertificate.title';
                            } else if (previousDraft && !previousDraft.sameVardgivare) {
                                message = viewState.common.intyg.type + '.warn.previousdraft.differentvg';
                                title = viewState.common.intyg.type + '.warn.previousdraft.title';
                            }
                            if(message !== '' && title !== '') {
                                showSignModal(message, title);
                                return;
                            }
                            doSignRequest();
                        });
                    }

                    function showSignModal(message, title) {
                        dialogService.showDialog({
                            dialogId: 'sign-dialog',
                            titleText: title,
                            templateUrl: '/app/partials/uniqueInfo.dialog.html',
                            button1click: function (modalInstance) {
                                modalInstance.close();
                                doSignRequest();
                            },
                            button2click: function (modalInstance) {
                                modalInstance.close();
                            },
                            button1text: 'common.shortsign',
                            button2text: 'common.cancel',
                            bodyText: message,
                            autoClose: false
                        });
                    }

                    function showSignNotPossibleModal(message, title) {
                        dialogService.showDialog({
                            dialogId: 'sign-not-possible-dialog',
                            titleText: title,
                            templateUrl: '/app/partials/uniqueInfo.dialog.html',
                            button1click: function (modalInstance) {
                                modalInstance.close();
                            },
                            button1text: 'common.close',
                            button2visible: false,
                            bodyText: message,
                            autoClose: false
                        });
                    }

                    function isSignAndSend() {
                        return featureService.isFeatureActive(featureService.features.SIGNERA_SKICKA_DIREKT, viewState.common.intyg.type) &&
                            ResourceLinkService.isLinkTypeExists(viewState.draftModel.links, 'SKICKA_INTYG');
                    }

                    function isSignAndSendOrKomp() {
                        return viewState.common.intyg.isKomplettering || isSignAndSend();
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

                    function getPreviousIntyg(onComplete) {

                        previousWarningMessage = {};

                        commonUtkastProxy.getPrevious(viewState.intygModel.grundData.patient.personId, viewState.common.intyg.certificateId, function(existing) {
                            previousUtkastWarnings = existing.utkast;
                            previousIntygWarnings = existing.intyg;
                            if(typeof onComplete === 'function') {
                                onComplete();
                            }
                        });
                    }

                    $scope.$on('intyg.loaded', getPreviousIntyg);

                }
            };
        } ]);
