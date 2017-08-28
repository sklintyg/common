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

angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'vagueDate',
        templateUrl: '/web/webjars/common/webcert/gui/formly/vagueDate.formly.html',
        controller: ['$scope', 'common.UtkastValidationService',
            'common.AtticHelper', function($scope, UtkastValidationService) {

                $scope.vagueDateModel = {
                    year:undefined,
                    month:undefined,
                    day:undefined
                };

                $scope.validate = function() {
                    UtkastValidationService.validate($scope.model);
                };

                parseModel();
                $scope.$watch('model.' + $scope.options.key, function(newValue, oldValue) {
                    if (newValue && !oldValue) {
                        parseModel();
                    }
                });

                $scope.$watch('vagueDateModel.year', function(newValue) {
                    if (newValue !== undefined) {
                        updateModel();
                    }
                });
                $scope.$watch('vagueDateModel.month', function(newValue) {
                    if (newValue !== undefined) {
                        updateModel();
                    }
                });
                $scope.$watch('vagueDateModel.day', function(newValue) {
                    if (newValue !== undefined) {
                        updateModel();
                    }
                });

                function forceLength(number, length) {
                    var returnValue = '';
                    if (number && number.length >= length)
                    {
                        returnValue += number.substring(0, 4);
                    }
                    else {
                        if (number) {
                            returnValue += number;
                        }
                        while(returnValue.length < length) {
                            returnValue = '0'+returnValue;
                        }
                    }
                    return returnValue;
                }

                function parseModel() {
                    var modelDate = $scope.model[$scope.options.key];
                    if (modelDate) {
                        $scope.vagueDateModel.year = modelDate.substring(0, 4);
                        $scope.vagueDateModel.month = modelDate.substring(5, 7);
                        $scope.vagueDateModel.day = modelDate.substring(8, 10);
                    }
                }

                function updateModel() {
                    $scope.model[$scope.options.key] =
                        forceLength($scope.vagueDateModel.year, 4) + '-' +
                        forceLength($scope.vagueDateModel.month, 2) + '-' +
                        forceLength($scope.vagueDateModel.day, 2);
                }

            }]
    });

});
