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
angular.module('common').controller('smi.EditCertCtrl',
    ['$scope', '$state',
        'common.UtkastService', 'common.UserModel', 'common.fmbService', 'common.fmbViewState',
        'ViewState', 'UtkastConfigFactory', 'common.PrefilledUserDataService', 'supportPanelConfigFactory',
        function($scope, $state,
            UtkastService, UserModel, fmbService, fmbViewState, viewState, utkastConfigFactory, prefilledUserDataService, supportPanelConfigFactory) {
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

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/

            // Get the certificate draft from the server.
            UtkastService.load(viewState).then(function(intygModel) {
                if (viewState.common.textVersionUpdated) {
                    $scope.certForm.$setDirty();
                }
                //Expose pdf download link
                viewState.common.intyg.pdfUrl = '/moduleapi/intyg/'+ viewState.common.intyg.type +'/' + intygModel.id + '/pdf';

                if($state.current.data.useFmb) {
                    fmbService.updateFmbTextsForAllDiagnoses(intygModel.diagnoser);
                }

                //We now have all info needed to build support-panel config (id, isSigned, isSent, isKompletteringsUtkast)
                $scope.supportPanelConfig = supportPanelConfigFactory.getConfig(intygModel.id, false, false, viewState.common.intyg.isKomplettering);
            });

            $scope.$on('saveRequest', function($event, saveDeferred) {
                $scope.certForm.$commitViewValue();
                $scope.certForm.$setPristine();
                var intygState = {
                    viewState : viewState,
                    formFail : function(){
                        $scope.certForm.$setDirty();
                    }
                };
                saveDeferred.resolve(intygState);
            });

            $scope.$on('$destroy', function() {
                if(!$scope.certForm.$dirty){
                    $scope.destroyList();
                }

                if($state.current.data && $state.current.data.useFmb) {
                    fmbViewState.reset();
                }
            });

            $scope.destroyList = function(){
                viewState.clearModel();
            };

        }]);
