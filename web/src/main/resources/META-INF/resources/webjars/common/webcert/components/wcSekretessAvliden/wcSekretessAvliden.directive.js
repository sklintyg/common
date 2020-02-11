/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
 * Renders icon(s) and optionally text for patients having avliden, sekretessmarkering or both.
 */
angular.module('common').directive('wcSekretessAvliden',
    [
        '$window', '$rootScope', '$uibModal', 'common.UtilsService', 'common.authorityService', 'common.AvtalProxy', 'moduleConfig',
        function($window, $rootScope, $uibModal, UtilsService, authorityService, avtalProxy, moduleConfig) {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    uuid: '=',
                    sekretessmarkering: '=',
                    avliden: '=',
                    testindicator: '='
                },
                templateUrl: '/web/webjars/common/webcert/components/wcSekretessAvliden/wcSekretessAvliden.directive.html',
                link: function($scope) {
                    var aboutModalInstance;

                    $scope.onSekretessClick = function () {
                        aboutModalInstance = $uibModal.open({
                            templateUrl: '/web/webjars/common/webcert/components/wcSekretessAvliden/aboutSekretessDialog.template.html',
                            size: 'lg',
                            controller: function($scope, $uibModalInstance) {

                                $scope.close = function() {
                                    $uibModalInstance.close();
                                };
                            }
                        });
                        //angular > 1.5 warns if promise rejection is not handled (e.g backdrop-click == rejection)
                        aboutModalInstance.result.catch(function () {}); //jshint ignore:line
                    };

                    $scope.onTestIndicatorClick = function () {
                        aboutModalInstance = $uibModal.open({
                            templateUrl: '/web/webjars/common/webcert/components/wcSekretessAvliden/aboutTestIndicatorDialog.template.html',
                            size: 'lg',
                            controller: function($scope, $uibModalInstance) {

                                $scope.close = function() {
                                    $uibModalInstance.close();
                                };
                            }
                        });
                        //angular > 1.5 warns if promise rejection is not handled (e.g backdrop-click == rejection)
                        aboutModalInstance.result.catch(function () {}); //jshint ignore:line
                    };
                }

            };
        }]);
