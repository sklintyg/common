/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcHeaderUnit', [ '$uibModal', 'common.authorityService', 'common.statService', function($uibModal, authorityService, statService) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            userModel: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/headers/wcHeader/wcHeaderUnit/wcHeaderUnit.directive.html',
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
             * Event listeners
             */
            $scope.$on('statService.stat-update', function(event, message) {
                $scope.stat = message;
            });

            $scope.otherLocationsStatsCount = function() {
                return ($scope.stat.intygAndraEnheter + $scope.stat.fragaSvarAndraEnheter);
            };




            function _showMenu() {
                return authorityService.isAuthorityActive({
                    authority: 'ATKOMST_ANDRA_ENHETER'
                }) && $scope.userModel.totaltAntalVardenheter > 1;
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
                return !$scope.userModel.privatLakare;
            };

            $scope.onChangeActiveUnitClick = function() {

                    var changeUnitDialogInstance = $uibModal.open({
                        templateUrl: '/web/webjars/common/webcert/components/headers/wcHeader/wcHeaderUnit/wcChangeActiveUnitDialog.html',
                        controller: 'wcChangeActiveUnitDialogCtrl',
                        size: 'md',
                        id: 'wcChangeActiveUnitDialog',
                        backdrop: 'static',
                        keyboard: false,
                        resolve: {
                            stats: function() {
                                return angular.copy($scope.stat);
                            },
                            vardgivare: function() {
                                return angular.copy($scope.userModel.vardgivare);
                            },
                            valdEnhet: function() {
                                return angular.copy($scope.userModel.valdVardenhet);
                            }
                        }
                    });

            };

        }
    };
} ]);
