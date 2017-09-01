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
        defaultOptions: {
            className: 'fold-animation'
        },
        templateUrl: '/web/webjars/common/webcert/gui/formly/vagueDate.formly.html',
        controller: ['$scope', 'common.UtkastValidationService',
            'common.AtticHelper', function($scope, UtkastValidationService) {

                $scope.vagueDateModel = {
                    year:undefined,
                    month:undefined,
                    day:undefined
                };

                parseModel();

                $scope.validate = function() {
                    UtkastValidationService.validate($scope.model);
                };

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

                function parseModel() {
                    var modelDate = $scope.model[$scope.options.key];
                    if (modelDate) {
                        var result = /^([0-9]{0,4})(-([0-9]{0,2}))?(-([0-9]{0,2}))?/.exec(modelDate);
                        $scope.vagueDateModel.year = result[1];
                        $scope.vagueDateModel.month = result[3];
                        $scope.vagueDateModel.day = result[5];
                    }
                }

                function updateModel() {
                    $scope.model[$scope.options.key] =
                        $scope.vagueDateModel.year + '-' +
                        $scope.vagueDateModel.month + '-' +
                        $scope.vagueDateModel.day;
                }

            }]
    });

});
