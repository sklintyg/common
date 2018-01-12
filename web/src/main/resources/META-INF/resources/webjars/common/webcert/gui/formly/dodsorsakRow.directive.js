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
 * Enable help marks with tooltip for other components than wcFields
 */
angular.module('common').directive('dodsorsakRow',
    [ '$log', '$rootScope', '$timeout', 'common.dynamicLabelService', 'common.UtkastValidationService', 'common.ObjectHelper',
        function($log, $rootScope, $timeout, dynamicLabelService, UtkastValidationService, ObjectHelper) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    model: '=',
                    key: '=',
                    to: '=templateOptions',
                    requireDescription: '@',
                    validation: '=',
                    rowIndex: '='
                },
                templateUrl: '/web/webjars/common/webcert/gui/formly/dodsorsakRow.directive.html',
                link: function($scope, element, attr) {

                    if(!$scope.requireDescription){
                        $scope.requireDescription = false;
                    }

                    // Use the assigned big letter from templateoptions. if its an array, pick the one meant for us using rowIndex
                    $scope.letter = $scope.to.letter;
                    if(Array.isArray($scope.letter)){
                        $scope.letter = $scope.letter[$scope.rowIndex];
                    }

                    $scope.hasValidationError = function(field, index) {
                        return $scope.validation && $scope.validation.messagesByField &&
                            !!$scope.validation.messagesByField[$scope.key.toLowerCase()];
                    };

                    $scope.validate = function() {
                        // When a date is selected from the date popup a blur event is sent.
                        // In the current version of Angular UI this blur event is sent before utkast model is updated
                        // This timeout ensures we get the new value in $scope.model
                        $timeout(function() {
                            UtkastValidationService.validate($scope.model);
                        });
                    };

                    $scope.$watch('validation.messagesByField', function() {
                        if($scope.validation){
                            $scope.orsakValidations = [];
                            $scope.dateValidations = [];
                            angular.forEach($scope.validation.messagesByField, function(validation, key) {

                                // Only care if validation messages have our index
                                var validationIndex = key.indexOf('.')+1;
                                validationIndex = key.substr(validationIndex, 1);
                                validationIndex = parseInt(validationIndex, 10);
                                var isValidIndex = validationIndex >= 0;
                                if(!isValidIndex || validationIndex === $scope.rowIndex){
                                    if (key.substr(0, $scope.key.length) === $scope.key.toLowerCase()) {
                                        if (key.substr(key.lastIndexOf('.')) === '.datum') {
                                            $scope.dateValidations = $scope.dateValidations.concat(validation);
                                        }
                                        else {
                                            $scope.orsakValidations = $scope.orsakValidations.concat(validation);
                                        }
                                    }
                                }

                            });
                        }
                    });

                    var chooseOption = {
                        id: '',
                        label: 'Välj...'
                    };

                    function update() {

                        $scope.orsakOptions = [chooseOption];

                        if ($scope.to.orsaksTyper) {
                            $scope.to.orsaksTyper.forEach(function (orsaksTyp) {
                                $scope.orsakOptions.push({
                                    'id': orsaksTyp,
                                    'label': dynamicLabelService.getProperty('DELAD_SVAR.' + orsaksTyp + '.RBK')
                                });
                            });
                        }
                    }

                    $scope.$on('dynamicLabels.updated', function() {
                        update();
                    });

                    $scope.$on('intyg.loaded', function() {
                        // Pick modelvalue from array if used in an array context otherwise just pick the model key value
                        $scope.modelValue = ObjectHelper.isDefined($scope.rowIndex) ? $scope.model[$scope.key][$scope.rowIndex] : $scope.model[$scope.key];
                    });

                    update();
                }
            };
        }]);
