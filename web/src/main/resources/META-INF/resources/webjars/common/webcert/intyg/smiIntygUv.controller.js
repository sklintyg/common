/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('common').controller('smi.ViewCertCtrlUv',
    ['$log', '$timeout', '$rootScope', '$stateParams', '$scope', '$state', 'common.IntygProxy',
        'common.UserModel', 'ViewState',
        'ViewConfigFactory', 'common.dynamicLabelService', 'common.IntygViewStateService', 'uvUtil',
        'supportPanelConfigFactory',
        'common.receiverService', 'common.authorityService', 'common.UtkastSignService', 'common.IntygHeaderViewState',
        function($log, $timeout, $rootScope, $stateParams, $scope, $state, IntygProxy,
            UserModel, ViewState, viewConfigFactory, DynamicLabelService, IntygViewStateService, uvUtil,
            supportPanelConfigFactory, receiverService, authorityService, utkastSignService, IntygHeaderViewState) {
            'use strict';

            ViewState.reset();
            $scope.viewState = ViewState;
            $scope.intygHeaderViewState = IntygHeaderViewState;

            // Check if the user used the special qa-link to get here.
            if ($stateParams.qaOnly) {
                $scope.isQaOnly = true;
            }

            // Page setup
            $scope.user = UserModel;

            ViewState.intygModel = {};
            ViewState.intygModel.filledAlways = true;
            $scope.cert = undefined;
            $scope.isLatestMajorTextVersion = true;

            $scope.uvConfig = viewConfigFactory.getViewConfig(true);

            //Did we just sign this intyg, and do we need to show approve receivers dialog for it?
            if ($stateParams.signed){
                if(receiverService.getData().showApproveDialog){
                    handleApprovalOfReceiversDialog();
                }
                else {
                    utkastSignService.checkForApprovalOfReceivers(ViewState.common.intygProperties.type, $stateParams.certificateId,
                        function(result) {
                            if (result) {
                                handleApprovalOfReceiversDialog();
                            } else if (authorityService.isAuthorityActive({
                                authority: UserModel.privileges.GODKANNA_MOTTAGARE,
                                intygstyp: ViewState.common.intygProperties.type
                            })) {
                                // potentially this intyg/user could require approval of receivers. If this is the case - we prefetch the configuration of approved receivers for
                                // this intyg right now, so that other components, such as wcApproveReceiversButton can determine it's enabled state without delay.
                                receiverService.getApprovedReceivers(ViewState.common.intygProperties.type, $stateParams.certificateId);
                            }
                        });
                }
            }

            function handleApprovalOfReceiversDialog(){
                receiverService.getData().showApproveDialog = false;
                receiverService.openConfigDialogForIntyg(ViewState.common.intygProperties.type,
                    $stateParams.certificateId, false);
            }

            /**
             * Private
             */
            function handleLoadingError(error) {
                $rootScope.$emit('ViewCertCtrl.load', null, null);
                $rootScope.$broadcast('intyg.loaded', null);
                ViewState.common.doneLoading = true;
                ViewState.common.updateActiveError(error, $stateParams.signed);
            }

            function loadIntyg() {
                $log.debug('Loading intyg ' + $stateParams.certificateId);
                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intygProperties.type,
                    function(result) {

                        ViewState.common.doneLoading = true;
                        if (result !== null && result !== '') {
                            ViewState.intygModel = result.contents;
                            ViewState.relations = result.relations;

                        DynamicLabelService.updateDynamicLabels(ViewState.common.intygProperties.type, ViewState.intygModel.textVersion).then(
                            function(labels) {
                                if(angular.isDefined(labels)) {
                                    DynamicLabelService.updateTillaggsfragorToModel(labels.tillaggsfragor, ViewState.intygModel);
                                }
                            }, function(textLoadingError) {
                                handleLoadingError(textLoadingError);
                            });

                            if (ViewState.intygModel !== undefined && ViewState.intygModel.grundData !== undefined) {
                                ViewState.enhetsId = ViewState.intygModel.grundData.skapadAv.vardenhet.enhetsid;
                            }

                            ViewState.common.updateIntygProperties(result, ViewState.intygModel.id);

                            //We now have all info needed to build support-panel config (id, intygTypeVersion, isSigned, isKompletteringsUtkast, isLocked, links)
                            $scope.supportPanelConfig = supportPanelConfigFactory.getConfig($stateParams.certificateId,
                                ViewState.intygModel.textVersion, true, false, false,
                                ViewState.common.intygProperties.links);

                            $scope.cert = result.contents;
                            $scope.isLatestMajorTextVersion = result.latestMajorTextVersion;

                            //The wcArendePanelTab will listen to 'ViewCertCtrl.load' event, so let it render first..
                            $timeout(function() {
                                $rootScope.$emit('ViewCertCtrl.load', ViewState.intygModel,
                                    ViewState.common.intygProperties);
                                $rootScope.$broadcast('intyg.loaded', ViewState.intygModel);
                            });

                        } else {
                            $rootScope.$emit('ViewCertCtrl.load', null, null);
                            $rootScope.$broadcast('intyg.loaded', null);

                            if ($stateParams.signed) {
                                ViewState.common.activeErrorMessageKey = 'common.error.sign.not_ready_yet';
                            } else {
                                ViewState.common.activeErrorMessageKey = 'common.error.could_not_load_cert';
                            }
                        }

                }, function(error) {
                    handleLoadingError(error);
                });
            }

            loadIntyg();

            $scope.$on('loadCertificate', loadIntyg);

        }]);
