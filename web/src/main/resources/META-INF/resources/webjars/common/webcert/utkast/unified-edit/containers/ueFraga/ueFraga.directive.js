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
angular.module('common').directive('ueFraga', ['common.UtkastValidationViewState',
    function(UtkastValidationViewState) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                form: '=',
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/containers/ueFraga/ueFraga.directive.html',
            link: function($scope, $element) {

                // Not using ng-class for performance (IE 11)
                if ($scope.config.cssClass) {
                    $element.addClass($scope.config.cssClass);
                }


                $scope.validation = UtkastValidationViewState;

                // One time bindings will stop watching when the result is no longer undefined.
                if (!$scope.config.label) {
                    // To stop the watch we need to set the value to something that is not undefined.
                    $scope.config.label = null;
                }

                //Returns an array with validationKeys for all child components by recursively iterating the config structure..
                //This only needs to be done once, since the config tree itself is static
                function _collectChildValidationKeys(arr, components) {
                    angular.forEach(components, function(c) {
                        //ue-grid has double array..
                        if (angular.isArray(c)) {
                            _collectChildValidationKeys(arr, c);
                        }

                        //If child should have it's own validation, don't add it to arr.
                        //But we do want validation keys grouped to row
                        if(c.independentRowValidation) {
                            if(c.components) {
                                c.validationRows = c.components.map(function(component) {
                                    return _collectChildValidationKeys([], [component]);
                                });
                            }
                            return;
                        }

                        //Add itself...
                        if (c.modelProp) {
                            // Unfortunately we have duplicate modelProps in db/doi for dodsdatum
                            if (arr.filter(function(item)  {
                                    return item.key === c.modelProp;
                                }).length === 0) {
                                arr.push({key: c.modelProp.toLowerCase(), type: c.type });
                            }
                        }

                        //.. and any children
                        if (c.components) {
                           return _collectChildValidationKeys(arr, c.components);
                        }
                    });

                    return arr;
                }

                $scope.validationKeys = _collectChildValidationKeys([], $scope.config.components);
                //Also, add the validationKey for the actual fraga (for frage-level EMPTY-type validationmessages)
                if ($scope.config.validationContext) {
                    $scope.validationKeys.push({key: $scope.config.validationContext.key.toLowerCase(), type: $scope.config.validationContext.type});
                }
            }
        };
    }]);
