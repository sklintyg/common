/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
 Note: This directive is not rendered unless a valid userModel is available, so all access to $scope.userModel can skips such checks.
 */
angular.module('common').directive('wcHeaderUnit', [ '$uibModal', 'common.authorityService', 'common.statService',
    'common.sessionCheckService', 'common.User', 'common.UserModel',
    function($uibModal, authorityService, statService, sessionCheckService, UserService, UserModel) {
    'use strict';

    return {
        restrict: 'E',
        scope: {},
        templateUrl: '/web/webjars/common/webcert/components/headers/wcAppHeader/wcHeaderUnit/wcHeaderUnit.directive.html',
        link: function($scope) {



            $scope.statService = statService;
            $scope.statService.startPolling();
            $scope.stat = {
                fragaSvarValdEnhet: 0,
                fragaSvarAndraEnheter: 0,
                intygValdEnhet: 0,
                intygAndraEnheter: 0,
                vardgivare: []
            };

            /**
             * Start polling session status
             * This will ensure correct logout when session has timed out.
             *
             */
            $scope.sessionCheckService = sessionCheckService;
            $scope.sessionCheckService.startPolling();

            /**
             * Event listeners
             */
            $scope.$on('statService.stat-update', function(event, message) {
                $scope.stat = message;
            });


            $scope.getUser = function () {
                return UserModel.user;
            };
            /**
             * All but DJUPINTEGRERAD can potentially see stats about other units
             *
             * @returns {boolean}
             */
            $scope.showStatsStatus = function() {
                return !UserModel.isDjupintegration() && $scope.otherLocationsStatsCount() > 0;
            };

            $scope.otherLocationsStatsCount = function() {
                return ($scope.stat.intygAndraEnheter + $scope.stat.fragaSvarAndraEnheter);
            };

            /**
             * Only show inactiveUnit warning if set in integrationparams
             * @returns {boolean}
             */
            $scope.showInactiveUnitStatus = function() {
                return UserModel.getIntegrationParam('inactiveUnit');
            };


            function _showMenu() {
                return authorityService.isAuthorityActive({
                    authority: 'ATKOMST_ANDRA_ENHETER'
                }) && UserModel.user.totaltAntalVardenheter > 1;
            }

            $scope.menu = {
                enabled: _showMenu(),
                expanded: false
            };

            $scope.toggleMenu = function($event) {
                $event.stopPropagation();
                $scope.menu.expanded = !$scope.menu.expanded;

            };

            $scope.showEnhetName = function() {
                //Privatlakare har samma namn på vg/ve, så vi skippar ve namnet.
                return !UserModel.isPrivatLakare();
            };

            $scope.onChangeActiveUnitClick = function() {

                    var changeUnitDialogInstance = $uibModal.open({
                        templateUrl: '/web/webjars/common/webcert/components/headers/wcAppHeader/wcHeaderUnit/wcChangeActiveUnitDialog.html',
                        controller: 'wcChangeActiveUnitDialogCtrl',
                        size: 'md',
                        id: 'wcChangeActiveUnitDialog',
                        keyboard: true,
                        windowClass: 'wc-header-care-unit-dialog-window-class'
                    });
                //angular > 1.5 warns if promise rejection is not handled (e.g backdrop-click == rejection)
                changeUnitDialogInstance.result.catch(function () {}); //jshint ignore:line

            };

        }
    };
} ]);
