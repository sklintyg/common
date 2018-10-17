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
angular.module('ts-diabetes').controller('ts-diabetes.IntygController',
    [ '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygProxy', 'common.UserModel',
        'common.dynamicLabelService', 'ViewConfigFactory', 'ViewState', 'supportPanelConfigFactory',
        function($log, $rootScope, $stateParams, $scope, IntygProxy,
            UserModel, DynamicLabelService, viewConfigFactory, ViewState, supportPanelConfigFactory) {
            'use strict';

            /*********************************************************************
             * Page state
             *********************************************************************/

            ViewState.reset();
            $scope.viewState = ViewState;
            $scope.cert = undefined;
            $scope.user = UserModel.user;
            $scope.uvConfig = viewConfigFactory.getViewConfig(true);

            /*********************************************************************
             * Private support functions
             *********************************************************************/

            function createKorkortstypListString(list) {

                var tempList = [];
                angular.forEach(list, function(key) {
                    if (key.selected) {
                        this.push(key);
                    }
                }, tempList);

                var resultString = '';
                for (var i = 0; i < tempList.length; i++) {
                    if (i < tempList.length - 1) {
                        resultString += tempList[i].type + (', ');
                    } else {
                        resultString += tempList[i].type;
                    }
                }

                return resultString;
            }

            function loadIntyg() {
                $log.debug('Loading intyg ' + $stateParams.certificateId);
                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intygProperties.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        ViewState.intygModel = result.contents;
                        ViewState.relations = result.relations;

                        DynamicLabelService.updateDynamicLabels(ViewState.common.intygProperties.type, ViewState.intygModel.textVersion);

                        ViewState.intygAvser = createKorkortstypListString(ViewState.intygModel.intygAvser.korkortstyp);
                        ViewState.bedomning = createKorkortstypListString(ViewState.intygModel.bedomning.korkortstyp);

                        if(ViewState.intygModel !== undefined && ViewState.intygModel.grundData !== undefined){
                            ViewState.enhetsId = ViewState.intygModel.grundData.skapadAv.vardenhet.enhetsid;
                        }

                        ViewState.common.updateIntygProperties(result, ViewState.intygModel.id);
                        $scope.cert = result.contents;

                        $scope.supportPanelConfig = supportPanelConfigFactory.getConfig($stateParams.certificateId, true);

                        $rootScope.$emit('ViewCertCtrl.load', ViewState.intygModel);
                        $rootScope.$broadcast('intyg.loaded', ViewState.intygModel, ViewState.common.intygProperties);

                    } else {
                        $rootScope.$emit('ViewCertCtrl.load', null, null);
                        $rootScope.$broadcast('intyg.loaded', null);
                        ViewState.common.activeErrorMessageKey = 'common.error.data_not_found';
                    }
                }, function(error) {
                    $rootScope.$emit('ViewCertCtrl.load', null, null);
                    $rootScope.$broadcast('intyg.loaded', null);
                    ViewState.common.doneLoading = true;
                    ViewState.common.updateActiveError(error, $stateParams.signed);
                });
            }

            /*********************************************************************
             * Exposed scope interaction functions
             *********************************************************************/

            /*********************************************************************
             * Page load
             *********************************************************************/
            loadIntyg();

            $scope.$on('loadCertificate', loadIntyg);
        }]);
