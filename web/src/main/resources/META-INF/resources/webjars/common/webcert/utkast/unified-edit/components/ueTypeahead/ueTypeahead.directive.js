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

angular.module('common').directive('ueTypeahead', [ '$http', '$log', 'ueUtil',
    function($http, $log, ueUtil) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                form: '=',
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueTypeahead/ueTypeahead.directive.html',
            link: function($scope) {

                ueUtil.standardSetup($scope);

                $scope.values = [];
                $http.get($scope.config.valuesUrl).then(function(response) {
                    $scope.values = response.data;
                    if($scope.config.orderByBeginning) {
                      $scope.values.sort();
                    }
                }, function(response) {
                    $log.error('failed to load typeahead values. Error ' + response.status);
                });

                $scope.orderBy = function(viewValue) {
                    if($scope.config.orderByBeginning) {
                        return function(element) {
                            return element.substr(0, viewValue.length).toLowerCase() !== viewValue.toLowerCase();
                        };
                    }
                };

            }
        };
    }
]);

