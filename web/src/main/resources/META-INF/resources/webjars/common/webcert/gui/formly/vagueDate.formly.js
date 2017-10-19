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
        controller: ['$scope', 'common.UtkastValidationService', function($scope, UtkastValidationService) {

                $scope.vagueDateModel = {
                    year:undefined,
                    month:undefined,
                    monthEnabled: false
                };

                createYears(true);
                createMonths(true);
                parseModel();

                $scope.validate = function() {
                    UtkastValidationService.validate($scope.model);
                };

                $scope.$watch('vagueDateModel.year', function(newValue) {
                    $scope.vagueDateModel.monthEnabled = false;
                    if (newValue !== undefined) {
                        updateModel();
                        if (newValue === '0000') {
                            $scope.vagueDateModel.month = '00';
                        }
                        else {
                            $scope.vagueDateModel.monthEnabled = true;
                        }
                        createYears(false);
                    }
                    else {
                        createYears(true);
                    }
                });
                $scope.$watch('vagueDateModel.month', function(newValue) {
                    if (newValue !== undefined) {
                        updateModel();
                        createMonths(false);
                    }
                    else {
                        createMonths(true);
                    }
                });

                function parseModel() {
                    var modelDate = $scope.model[$scope.options.key];
                    if (modelDate) {
                        var result = /^([0-9]{0,4})(-([0-9]{0,2}))?(-([0-9]{0,2}))?/.exec(modelDate);
                        if (result[1]) {
                            $scope.vagueDateModel.year = result[1];
                        } else {
                            $scope.vagueDateModel.year = undefined;
                        }
                        if (result[3]) {
                            $scope.vagueDateModel.month = result[3];
                        } else {
                            $scope.vagueDateModel.month = undefined;
                        }
                    }
                }

                function updateModel() {
                    var modelValue = '';
                    if ($scope.vagueDateModel.year) {
                        modelValue += $scope.vagueDateModel.year;
                    }
                    modelValue += '-';
                    if ($scope.vagueDateModel.month) {
                        modelValue += $scope.vagueDateModel.month;
                    }
                    modelValue += '-00';
                    $scope.model[$scope.options.key] = modelValue;
                }


                function createYears(placeholder) {
                    // No need to recreate if the list has correct length
                    if ($scope.years) {
                        if ($scope.years.length === 4 && placeholder) {
                            return;
                        }
                        if ($scope.years.length === 3 && !placeholder) {
                            return;
                        }
                    }
                    var thisYear = moment().format('YYYY');
                    var lastYear = (thisYear - 1).toString();
                    $scope.years = [
                        {value:'0000', label:'0000 (ej känt)'},
                        {value:thisYear, label:thisYear},
                        {value:lastYear, label:lastYear}
                    ];
                    if (placeholder) {
                        $scope.years.unshift({value:undefined, label:'Ange år'});
                    }
                }

                function createMonths(placeholder) {
                    if ($scope.months) {
                        if ($scope.months.length === 14 && placeholder) {
                            return;
                        }
                        if ($scope.months.length === 13 && !placeholder) {
                            return;
                        }
                    }
                    $scope.months = [{value: '00', label: '00 (ej känt)'}];
                    for (var month = 1; month <= 12; month++) {
                        var monthPadded = month;
                        if (monthPadded < 10) {
                            monthPadded = '0' + monthPadded;
                        }
                        $scope.months.push({value: monthPadded, label: monthPadded});
                    }
                    if (placeholder) {
                        $scope.months.unshift({value: undefined, label: 'Ange månad'});
                    }
                }

            }]
    });

});
