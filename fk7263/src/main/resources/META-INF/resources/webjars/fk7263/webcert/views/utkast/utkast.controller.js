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
angular.module('fk7263').controller('fk7263.EditCertCtrl',
    ['$rootScope', '$location', '$scope', '$log',  '$state', '$stateParams', '$q',
        'common.UtkastService', 'common.UserModel',
         'common.UtilsService', 'fk7263.Domain.IntygModel', 'fk7263.supportPanelConfigFactory', 'fk7263.viewConfigFactory',
        'fk7263.EditCertCtrl.ViewStateService', 'common.ObjectHelper', 'common.IntygProxy', 'common.IntygHelper',
        function($rootScope, $location, $scope, $log,  $state, $stateParams, $q,
            UtkastService, UserModel, utils, IntygModel, supportPanelConfigFactory, viewConfigFactory, viewState, ObjectHelper, IntygProxy, IntygHelper) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            // create a new intyg model and reset all viewStates
            viewState.reset();
            $scope.viewState = viewState;

            // Page states
            $scope.user = UserModel.user;

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/

            // Get the certificate draft from the server.
            UtkastService.load(viewState).then(function(intygModel) {

                //Expose pdf download link
                viewState.common.intyg.pdfUrl = '/moduleapi/intyg/'+ viewState.common.intyg.type +'/' + intygModel.id + '/pdf';

                // need intygProperties since we're using a intyg Template.
                viewState.common.intygProperties = {
                    type: 'fk7263'
                };
                $scope.uvConfig = viewConfigFactory.getViewConfig(true, true);
                $scope.cert = viewState.common.__utlatandeJson.content;
                //We now have all info needed to build support-panel config (id, isSigned, isKompletteringsUtkast)
                $scope.supportPanelConfig = supportPanelConfigFactory.getConfig(intygModel.id, '1.0', false, viewState.common.intyg.isKomplettering);
                viewState.common.showTemplate = true;

            }, function(error) {

                if(typeof error === 'object') {
                    $log.error('Failed to load utkast. Reason: ' + error.errorCode + ': ' + error.message);
                }

                // Failed to load parent intyg. Tell frågasvar
                $rootScope.$broadcast('ViewCertCtrl.load', null, {
                    isSent: false,
                    isRevoked: false
                });
            });

            $scope.gotoRelatedIntyg = function(intyg) {
                if (intyg.status === 'SIGNED') {
                    IntygHelper.goToIntyg('fk7263', '1.0', intyg.intygsId);
                }
                else {
                    IntygHelper.goToDraft('fk7263', '1.0', intyg.intygsId);
                }
            };

        }]);
