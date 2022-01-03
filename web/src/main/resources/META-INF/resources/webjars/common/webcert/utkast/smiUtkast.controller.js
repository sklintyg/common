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
angular.module('common').controller('smi.EditCertCtrl',
    ['$scope', '$state', '$stateParams',
        'common.UtkastService', 'common.UserModel', 'common.fmbService', 'common.fmbViewState',
        'ViewState', 'UtkastConfigFactory', 'common.PrefilledUserDataService', 'supportPanelConfigFactory',
        'common.receiverService', 'common.ResourceLinkService', 'common.UtkastSignService',
        function($scope, $state, $stateParams,
            UtkastService, UserModel, fmbService, fmbViewState, viewState, utkastConfigFactory,
            prefilledUserDataService, supportPanelConfigFactory, receiverService, ResourceLinkService, UtkastSignService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            // create a new intyg model and reset all viewStates
            viewState.reset();
            $scope.viewState = viewState;

            // Page states
            $scope.user = UserModel;

            $scope.categoryIds = utkastConfigFactory.getCategoryIds();

            $scope.editEnabled = false;
            $scope.lockedAlerts = [];
            $scope.isLatestMajorTextVersion = true;

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/
            // Get the certificate draft from the server.
            function loadIntyg() {
                UtkastService.load(viewState).then(function(intygModel) {
                    receiverService.updatePossibleReceivers(viewState.common.intyg.type);

                    if (viewState.common.textVersionUpdated) {
                        $scope.certForm.$setDirty();
                    }
                    //Expose pdf download link
                    viewState.common.intyg.pdfUrl =
                        '/moduleapi/intyg/' + viewState.common.intyg.type + '/' + intygModel.id + '/pdf';

                    if ($state.current.data.useFmb) {
                        fmbService.updateFmbTextsForAllDiagnoses(intygModel.diagnoser);
                    }

                    if (ResourceLinkService.isLinkTypeExists(viewState.draftModel.links, 'REDIGERA_UTKAST')) {
                        $scope.editEnabled = true;
                    } else {
                        if (viewState.draftModel.isLocked() && angular.isFunction(viewState.getLockedDraftAlert)) {
                            UtkastService.updatePreviousIntygUtkast(intygModel.grundData.patient.personId).then(function() {
                                $scope.lockedAlerts = viewState.getLockedDraftAlert();
                            });
                        }
                    }

                    if (viewState.common.__utlatandeJson.candidateMetaData) {
                            UtkastService.copyFromCandidate(viewState.common.__utlatandeJson)
                            .then(function(success) {
                                $state.reload();
                            }, function(error) {
                                // Error
                            });
                    }

                    if ($stateParams.error) {
                        UtkastSignService.showSignServiceError(viewState.common.intyg.type, $stateParams.ticket);
                    }

                    $scope.isLatestMajorTextVersion = viewState.common.__utlatandeJson.latestMajorTextVersion;
                });
            }

            // Do load certificate
            loadIntyg();

            $scope.$on('utkast.supportPanelConfig', function(event, isKomplettering, intygTypeVersion) {
                //We now have all info needed to build support-panel config (id, intygTypeVersion, isSigned, isKompletteringsUtkast, isLocked, links)
                $scope.supportPanelConfig =
                    supportPanelConfigFactory.getConfig($stateParams.certificateId, intygTypeVersion, false,
                        isKomplettering, viewState.draftModel.isLocked(), viewState.draftModel.links);
            });

            $scope.$on('saveRequest', function($event, saveDeferred) {
                $scope.certForm.$commitViewValue();
                var intygState = {
                    viewState: viewState,
                    formFail: function() {
                        if ($scope.certForm) {
                            $scope.certForm.$setDirty();
                        }
                    },
                    formPristine: function() {
                        $scope.certForm.$setPristine();
                    }
                };
                saveDeferred.resolve(intygState);
            });

            $scope.$on('$destroy', function() {
                if (!$scope.certForm.$dirty) {
                    $scope.destroyList();
                }

                if ($state.current.data && $state.current.data.useFmb) {
                    fmbViewState.reset();
                }
            });

            $scope.destroyList = function() {
                viewState.clearModel();
            };

        }]);
