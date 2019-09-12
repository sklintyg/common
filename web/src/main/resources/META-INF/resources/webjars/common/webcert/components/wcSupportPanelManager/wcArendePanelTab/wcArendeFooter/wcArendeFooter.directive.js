/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
 * Created by marced on 2016-08-31.
 */

/**
 * arendePanelSvarKompletteringsatgard directive. Handles kompletteringsatgard choice in a modal dialog.
 * If user selects to reply with a new Intyg, this component handles that, if user choose to reply with a message, the calling component
 * get's a callback through 'onAnswerWithMessage' and must handle it there
 */
angular.module('common').directive('wcArendeFooter',
    ['$log', '$rootScope', '$q', '$state', '$timeout', '$window',
        'common.UserModel', 'common.ObjectHelper', 'common.ArendeListViewStateService', 'common.statService',
        'common.messageService',
        'common.dialogService', 'common.IntygProxy', 'common.IntygCopyRequestModel',
        'common.ArendeHelper', 'common.ArendeProxy', 'common.ArendeSvarModel', 'common.ErrorHelper',
        'common.ArendeVidarebefordraHelper',
        'common.IntygHelper', 'common.IntygHeaderViewState', 'common.ResourceLinkService',
        function($log, $rootScope, $q, $state, $timeout, $window,
            UserModel, ObjectHelper, ArendeListViewState, statService, messageService,
            DialogService, IntygProxy, IntygCopyRequestModel,
            ArendeHelper, ArendeProxy, ArendeSvarModel, ErrorHelper, ArendeVidarebefordraHelper,
            IntygHelper, IntygHeaderViewState, ResourceLinkService) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/wcArendeFooter/wcArendeFooter.directive.html',
                scope: {
                    arendeList: '='
                },
                controller: function($scope, $element, $attrs) {

                    $scope.intygProperties = {};
                    $scope.kompletteringConfig = {
                        enhetsid: ArendeListViewState.intyg.grundData.skapadAv.vardenhet.enhetsid,
                        //Existence of complementedByUtkast means an utkast with complemented relation exist.
                        redirectToExistingUtkast: false
                    };

                    function onIntygLoaded(event, intyg, intygProperties) {
                        $scope.kompletteringConfig.redirectToExistingUtkast =
                            !!ArendeListViewState.intygProperties.latestChildRelations.complementedByUtkast;
                        $scope.intygProperties = intygProperties;
                    }

                    var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', onIntygLoaded);
                    $scope.$on('$destroy', unbindFastEvent);

                    onIntygLoaded(null, ArendeListViewState.intyg, ArendeListViewState.intygProperties);

                    $scope.showKompletteringButtons = function() {
                        return ArendeListViewState.getUnhandledKompletteringCount() > 0;
                    };

                    $scope.showKompletteraButton = function() {
                        return ResourceLinkService.isLinkTypeExists(ArendeListViewState.intygProperties.links,
                            'BESVARA_KOMPLETTERING');
                    };

                    $scope.showKanInteKompletteraButton = function() {
                        return ResourceLinkService.isLinkTypeExists(ArendeListViewState.intygProperties.links,
                            'BESVARA_KOMPLETTERING_MED_MEDDELANDE');
                    };

                    $scope.showVidarebefodraButton = function() {
                        return !ArendeListViewState.intygProperties.isRevoked &&
                            ResourceLinkService.isLinkTypeExists(ArendeListViewState.intygProperties.links,
                                'VIDAREBEFODRA_FRAGA');
                    };

                    var _answerWithIntyg = function(kommentar) {

                        var deferred = $q.defer();

                        $scope.updateInProgress = true; // trigger local spinner
                        $scope.activeKompletteringErrorMessageKey = null;

                        IntygProxy.answerWithIntyg(ArendeListViewState.intygProperties.type,
                            IntygCopyRequestModel.build({
                                intygId: ArendeListViewState.intyg.id,
                                intygType: ArendeListViewState.intygProperties.type,
                                patientPersonnummer: ArendeListViewState.intyg.grundData.patient.personId,
                                kommentar: kommentar
                            }), function(result) {

                                $scope.updateInProgress = false;
                                $scope.activeKompletteringErrorMessageKey = null;
                                deferred.resolve(result);

                            }, function(errorData) {
                                // show error view
                                $scope.updateInProgress = false;
                                if (errorData) {
                                    $scope.activeKompletteringErrorMessageKey = errorData.errorCode;
                                } else {
                                    $scope.activeKompletteringErrorMessageKey = 'unknown';
                                }
                                deferred.reject(errorData);
                            });
                        return deferred.promise;
                    };


                    $scope.kompletteraIntyg = function(modalInstance) {
                        if (!ObjectHelper.isDefined(ArendeListViewState.intygProperties)) {
                            $scope.activeKompletteringErrorMessageKey = 'komplettera-no-intyg';
                            return;
                        }

                        // The actual process of answering with a new intyg is rather complex, so defer that
                        // to calling code, and act on outcome of it here (keep dialog or close it)
                        _answerWithIntyg().then(function(result) {
                            IntygHeaderViewState.utkastCreatedFrom = ArendeListViewState.intyg.id;

                            statService.refreshStat();
                            IntygHelper.goToDraft(result.intygsTyp, result.intygTypeVersion, result.intygsUtkastId);

                        }, function(error) {
                            $log.error(error);
                            if (!error) {
                                DialogService.showErrorMessageDialog(
                                    messageService.getProperty('common.arende.error.unknown'));
                            } else if (error.errorCode === 'PU_PROBLEM') {
                                DialogService.showMessageDialog('common.arende.error.pu_problem.modalheader',
                                    messageService.getProperty('common.arende.error.' + error.errorCode.toLowerCase()));
                            } else {
                                DialogService.showErrorMessageDialog(
                                    messageService.getProperty('common.arende.error.' + error.errorCode.toLowerCase()));
                            }
                        });
                    };

                    var kompletteringDialog;

                    $scope.openKompletteringDialog = function() {
                        var dialogModel = {
                            enhetsid: ArendeListViewState.intyg.grundData.skapadAv.vardenhet.enhetsid,
                            updateInProgress: false,
                            kompletteringConfig: $scope.kompletteringConfig,
                            showLamnaOvrigaUpplysningar: ResourceLinkService.isLinkTypeExists(ArendeListViewState.intygProperties.links,
                                'BESVARA_KOMPLETTERING')
                        };

                        if (!dialogModel.showLamnaOvrigaUpplysningar) {
                            dialogModel.answerWithIntyg = false;
                        }

                        kompletteringDialog = DialogService.showDialog({
                            dialogId: 'komplettering-modal-dialog',
                            titleId: 'common.arende.komplettering.kompletteringsatgard.dialogtitle',
                            templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/komplettera/komplettering-modal-dialog.html',
                            windowClass: 'dialog-placement',
                            model: dialogModel,
                            button1click: function(modalInstance) {
                                if (!ObjectHelper.isDefined(ArendeListViewState.intygProperties)) {
                                    $scope.activeKompletteringErrorMessageKey = 'komplettera-no-intyg'; // TODO replace with local footer error handling
                                    return;
                                }

                                dialogModel.updateInProgress = true;
                                dialogModel.activeKompletteringErrorMessageKey = null;

                                if (dialogModel.answerWithIntyg) {
                                    // The actual process of answering with a new intyg is rather complex, so defer that
                                    // to calling code, and act on outcome of it here (keep dialog or close it)
                                    _answerWithIntyg(dialogModel.ovrigaUpplysningar).then(function(result) {
                                        //If successful, wer'e done here
                                        modalInstance.close();

                                        statService.refreshStat();

                                        var extraStateParams = {
                                            focusOn: 'ovrigt'
                                        };
                                        IntygHelper.goToDraft(ArendeListViewState.intygProperties.type,
                                            result.intygTypeVersion, result.intygsUtkastId, extraStateParams);
                                    }, function(errorResult) {
                                        //Keep dialog open so that activeKompletteringErrorMessageKey is displayed to user.
                                        dialogModel.updateInProgress = false;
                                        dialogModel.activeKompletteringErrorMessageKey =
                                            ErrorHelper.safeGetError(errorResult);
                                    });
                                } else {
                                    ArendeProxy.saveKompletteringAnswer(dialogModel.meddelandeText,
                                        ArendeListViewState.intygProperties.type, ArendeListViewState.intyg.id,
                                        function(result) {
                                            modalInstance.close();

                                            if (result !== null) {
                                                ArendeListViewState.setArendeList(result);
                                                statService.refreshStat();
                                            }
                                        }, function(errorData) {
                                            // show error view
                                            dialogModel.updateInProgress = false;
                                            dialogModel.activeKompletteringErrorMessageKey =
                                                ErrorHelper.safeGetError(errorData);
                                        });
                                }

                            },
                            button2click: function(modalInstance) {
                                modalInstance.close();
                            },
                            autoClose: false,
                            size: 'md'
                        }).result.then(function() {
                            kompletteringDialog = null; // Dialog closed
                        }, function() {
                            kompletteringDialog = null; // Dialog dismissed
                        });

                    };

                    $scope.openUthoppInfoModal = function() {
                        DialogService.showMessageDialog('common.arende.komplettering.uthopp.modal.header',
                            messageService.getProperty('common.arende.komplettering.uthopp.modal.body'));
                    };

                    $scope.openMailDialog = function() {
                        // Handle vidarebefordra dialog
                        // use timeout so that external mail client has a chance to start before showing dialog
                        $timeout(function() {
                            ArendeVidarebefordraHelper.handleVidareBefordradToggle($scope.onVidarebefordradChange);
                        }, 1000);

                        // Launch mail client
                        var arendeMailModel = {
                            intygId: ArendeListViewState.intyg.id,
                            intygType: ArendeListViewState.intygProperties.type
                        };
                        $window.location = ArendeVidarebefordraHelper.buildMailToLink(arendeMailModel);
                    };

                    $scope.onVidarebefordradChange = function() {
                        $scope.forwardInProgress = true;
                        ArendeProxy.setVidarebefordradState(
                            ArendeListViewState.intyg.id,
                            ArendeListViewState.intygProperties.type,
                            function(result) {
                                $scope.forwardInProgress = false;
                                if (result) {
                                    ArendeListViewState.setArendeList(result);
                                } else {
                                    DialogService.showErrorMessageDialog(
                                        'Kunde inte markera/avmarkera frågan som vidarebefordrad. ' +
                                        'Försök gärna igen för att se om felet är tillfälligt. Annars kan du kontakta supporten');
                                }
                            });
                    };

                }
            };
        }]);
