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
angular.module('fk7263').controller('fk7263.ViewCertCtrl',
    [ '$log', '$rootScope', '$stateParams', '$scope',
        'common.IntygProxy', 'common.UserModel', 'common.IntygHelper', 'fk7263.IntygController.ViewStateService', 'fk7263.viewConfigFactory',
        'supportPanelConfigFactory',
        function($log, $rootScope, $stateParams, $scope,
            IntygProxy, UserModel, IntygHelper, ViewState, viewConfigFactory, supportPanelConfigFactory) {
            'use strict';

            ViewState.reset();
            ViewState.intygModel = {};
            ViewState.intygModel.filledAlways = true;

            $scope.viewState = ViewState;
            $scope.user = UserModel;
            $scope.cert = undefined;

            $scope.uvConfig = viewConfigFactory.getViewConfig(true);
            $scope.supportPanelConfig = supportPanelConfigFactory.getConfig($stateParams.certificateId, '1.0', true);

            // Check if the user used the special qa-link to get here.
            $scope.isQaOnly = UserModel.isUthopp();


            $scope.gotoRelatedIntyg = function(intyg) {
                if (intyg.status === 'SIGNED') {
                    IntygHelper.goToIntyg('fk7263', '1.0', intyg.intygsId);
                }
                else {
                    IntygHelper.goToDraft('fk7263', '1.0', intyg.intygsId);
                }
            };

            /**
             * Private
             */

            function loadIntyg() {
                $log.debug('Loading certificate ' + $stateParams.certificateId);

                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intygProperties.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        ViewState.intygModel = result.contents;
                        ViewState.relations = result.relations;

                        if(ViewState.intygModel !== undefined && ViewState.intygModel.grundData !== undefined){
                            ViewState.enhetsId = ViewState.intygModel.grundData.skapadAv.vardenhet.enhetsid;
                        }

                        ViewState.common.updateIntygProperties(result, ViewState.intygModel.id);

                        $scope.cert = result.contents;
                        $rootScope.$emit('ViewCertCtrl.load', ViewState.intygModel, ViewState.common.intygProperties);
                        $rootScope.$broadcast('intyg.loaded', ViewState.intygModel);

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
                    $rootScope.$emit('ViewCertCtrl.load', null, null);
                    $rootScope.$broadcast('intyg.loaded', null);
                    ViewState.common.doneLoading = true;
                    ViewState.common.updateActiveError(error, $stateParams.signed);
                });
            }

            loadIntyg();

            $scope.$on('loadCertificate', loadIntyg);
        }]);
