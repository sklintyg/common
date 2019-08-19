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
angular.module('common').directive('ueVagueDate', [ 'ueUtil', 'common.UtkastValidationService', 'common.UtkastValidationViewState',
    function(ueUtil, UtkastValidationService, UtkastValidationViewState) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                form: '=',
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueVagueDate/ueVagueDate.directive.html',
            controller: function($scope) {
                $scope.validation = UtkastValidationViewState;
                $scope.monthValidationKey = $scope.config.modelProp + '.month';

                $scope.$on('$destroy', function() {
                    $scope.model.clear($scope.config.modelProp);
                });

                $scope.vagueDateModel = {
                    year: '',
                    month: '',
                    monthEnabled: false
                };

                createYears(true);
                createMonths(true);
                parseModel();

                $scope.$watch(function() {
                    return $scope.model[$scope.config.modelProp];
                }, function(newValue, oldValue) {
                    //if(newValue !== oldValue){
                    parseModel();
                    //}
                });

                $scope.validate = function() {
                    UtkastValidationService.validate($scope.model);
                };

                $scope.hasValidationError = function(field) {
                    return UtkastValidationViewState.messagesByField &&
                        (!!UtkastValidationViewState.messagesByField[$scope.config.modelProp] ||
                        !!UtkastValidationViewState.messagesByField[$scope.config.modelProp + '.' + field]);
                };

                $scope.$watch('vagueDateModel.year', function(newValue) {
                    $scope.vagueDateModel.monthEnabled = false;
                    if (newValue !== '') {
                        updateModel();
                        if (newValue === '0000') {
                            $scope.vagueDateModel.month = '00';
                            createMonths(false);
                            updateModel();
                        }
                        else {
                            $scope.vagueDateModel.monthEnabled = true;
                            if ($scope.vagueDateModel.month === '' || $scope.vagueDateModel.month === '00') {
                                createMonths(true);
                                $scope.vagueDateModel.month = '';
                                updateModel();
                            } else {
                                createMonths(false);
                            }
                        }
                        createYears(false);
                    }
                    else {
                        createYears(true);
                    }
                });
                $scope.$watch('vagueDateModel.month', function(newValue) {
                    if (newValue !== '') {
                        updateModel();
                        createMonths(false);
                    }
                    else {
                        createMonths(true);
                    }
                });

                function parseModel() {
                    var modelDate = $scope.model[$scope.config.modelProp];
                    if (modelDate) {
                        var result = /^([0-9]{0,4})(-([0-9]{0,2}))?(-([0-9]{0,2}))?/.exec(modelDate);
                        if (result[1]) {
                            $scope.vagueDateModel.year = result[1];
                        } else {
                            $scope.vagueDateModel.year = '';
                        }
                        if (result[3]) {
                            $scope.vagueDateModel.month = result[3];
                        } else {
                            $scope.vagueDateModel.month = '';
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
                    $scope.model[$scope.config.modelProp] = modelValue;
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
                        {id: '0000', label: '0000 (ej känt)'},
                        {id: thisYear, label: thisYear},
                        {id: lastYear, label: lastYear}
                    ];
                    if (placeholder) {
                        $scope.years.unshift({id: '', label: 'Ange år'});
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
                    $scope.months = [{id: '00', label: '00 (ej känt)'}];
                    for (var month = 1; month <= 12; month++) {
                        var monthPadded = month.toString();
                        if (monthPadded < 10) {
                            monthPadded = '0' + monthPadded;
                        }
                        $scope.months.push({id: monthPadded, label: monthPadded});
                    }
                    if (placeholder) {
                        $scope.months.unshift({id: '', label: 'Ange månad'});
                    }
                }

            }
        };
    }]);
