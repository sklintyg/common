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
 * Created by marced on 2016-08-31.
 */

/**
 * arendePanelSvarKompletteringsatgard directive. Handles kompletteringsatgard choice in a modal dialog.
 * If user selects to reply with a new Intyg, this component handles that, if user choose to reply with a message, the calling component
 * get's a callback through 'onAnswerWithMessage' and must handle it there
 */
angular.module('common').directive('wcArendeFooter',
    [ '$rootScope', '$q', '$state', 'common.UserModel', 'common.ObjectHelper', 'common.ArendeListViewStateService',
        'common.statService', 'common.dialogService', 'common.IntygProxy', 'common.IntygCopyRequestModel', 'common.ArendeHelper',
        function($rootScope, $q, $state, UserModel, ObjectHelper, ArendeListViewState, statService,
            dialogService, IntygProxy, IntygCopyRequestModel, ArendeHelper) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/wcArendeFooter/wcArendeFooter.directive.html',
                scope: {
                },
                controller: function($scope, $element, $attrs) {

                    $scope.kompletteringConfig = {
                        //Existence of complementedByUtkast means an utkast with complemented relation exist.
                        redirectToExistingUtkast: false,
                        svaraMedNyttIntygDisabled: ArendeListViewState.isSvaraMedNyttIntygDisabled(),
                        showOvrigaUpplysningar: !UserModel.hasRole('VARDADMINISTRATOR')
                    };

                    var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', function(event, intyg, intygProperties) {
                        $scope.kompletteringConfig.redirectToExistingUtkast = !!ArendeListViewState.intygProperties.latestChildRelations.complementedByUtkast;
                    });
                    $scope.$on('$destroy', unbindFastEvent);

                    $scope.showKompletteringButton = function() {
                        return !$scope.kompletteringConfig.redirectToExistingUtkast && !$scope.kompletteringConfig.svaraMedNyttIntygDisabled;
                    }

                    $scope.kompletteraIntyg = function(modalInstance) {
                        if (!ObjectHelper.isDefined(ArendeListViewState.intygProperties)) {
                            $scope.activeKompletteringErrorMessageKey = 'komplettera-no-intyg';
                            return;
                        }

                        // The actual process of answering with a new intyg is rather complex, so defer that
                        // to calling code, and act on outcome of it here (keep dialog or close it)
                        $scope.onAnswerWithIntyg().then(function(result) {

                            statService.refreshStat();

                            function goToDraft(type, intygId) {
                                $state.go(type + '-edit', {
                                    certificateId: intygId
                                });
                            }

                            goToDraft(ArendeListViewState.intygProperties.type, result.intygsUtkastId);

                        });
                    };

                    $scope.onAnswerWithIntyg = function() {

                        var deferred = $q.defer();

                        $scope.updateInProgress = true; // trigger local spinner
                        $scope.activeKompletteringErrorMessageKey = null;

                        IntygProxy.answerWithIntyg(null, ArendeListViewState.intygProperties.type,
                            IntygCopyRequestModel.build({
                                intygId: ArendeListViewState.intyg.id,
                                intygType: ArendeListViewState.intygProperties.type,
                                patientPersonnummer: ArendeListViewState.intyg.grundData.patient.personId
                            }), function(result) {

                                $scope.updateInProgress = false;
                                $scope.activeKompletteringErrorMessageKey = null;
                                deferred.resolve(result);

                            }, function(errorData) {
                                // show error view
                                $scope.updateInProgress = false;
                                $scope.activeKompletteringErrorMessageKey = errorData.errorCode;
                                deferred.reject(errorData);
                            });
                        return deferred.promise;
                    };

                    var kompletteringDialog;

                    $scope.openKompletteringDialog = function() {

                        var dialogModel = {
                            enhetsid: ArendeListViewState.intyg.grundData.skapadAv.vardenhet.enhetsid,
                            updateInProgress: false,
                            kompletteringConfig: $scope.kompletteringConfig,
                            komplUtkastFinns: (!!ArendeListViewState.intygProperties.latestChildRelations.complementedByUtkast)
                        };

                        kompletteringDialog = dialogService.showDialog({
                            dialogId: 'komplettering-modal-dialog',
                            titleId: $scope.kompletteringConfig.showOvrigaUpplysningar ?
                                'common.arende.komplettering.kompletteringsatgard.dialogtitle' :
                                'common.arende.komplettering.kompletteringsatgard.dialogtitlevardadmin',
                            templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/komplettera/komplettering-modal-dialog.html',
                            windowClass: 'dialog-placement',
                            model: dialogModel,
                            button1click: function(modalInstance) {
                                if (!ObjectHelper.isDefined(ArendeListViewState.intygProperties)) {
                                    $scope.activeKompletteringErrorMessageKey = 'komplettera-no-intyg'; // TODO replace with local footer error handling
                                    return;
                                }

                                // The actual process of answering with a new intyg is rather complex, so defer that
                                // to calling code, and act on outcome of it here (keep dialog or close it)
                                $scope.onAnswerWithIntyg().then(function(result) {
                                    //If successful, wer'e done here
                                    modalInstance.close();

                                    statService.refreshStat();

                                    function goToDraftThenScrollToOvrigt(type, intygId) {
                                        var stateParams = {
                                            certificateId: intygId
                                        };
                                        stateParams.focusOn = 'focusOvrigt';
                                        $state.go(type + '-edit', stateParams);
                                    }

                                    goToDraftThenScrollToOvrigt(ArendeListViewState.intygProperties.type, result.intygsUtkastId);

                                }, function(errorResult) {
                                    //Keep dialog open so that activeKompletteringErrorMessageKey is displayed to user.
                                });
                            },
                            button1id: 'button1answerintyg-dialog',
                            button2click: function(modalInstance) {
                                // Answer with text message is not handled here, so just close the
                                // dialog and notify calling code that this is the choice of the user.
                                modalInstance.close();
                                $scope.onAnswerWithMessage();
                            },
                            button2id: 'button2answermessage-dialog',
                            button3click: function(modalInstance) {
                                modalInstance.close();
                                $scope.openKompletteringsUtkast();
                            },
                            button3id: 'button3gotoutkast-dialog',
                            autoClose: false,
                            size: 'lg'
                        }).result.then(function() {
                            kompletteringDialog = null; // Dialog closed
                        }, function() {
                            kompletteringDialog = null; // Dialog dismissed
                        });

                    };
                }
            };
        }]);
