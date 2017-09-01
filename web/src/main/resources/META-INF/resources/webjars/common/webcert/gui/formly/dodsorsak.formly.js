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
        name: 'dodsorsak',
        templateUrl: '/web/webjars/common/webcert/gui/formly/dodsorsak.formly.html',
        defaultOptions: {
            className: 'slide-animation'
        },
        controller: ['$scope', '$timeout', 'common.dynamicLabelService', 'common.ObjectHelper', 'common.ArendeListViewStateService',
            'common.UtkastValidationService',
        function($scope, $timeout, dynamicLabelService, objectHelper, ArendeListViewState, UtkastValidationService) {
            var chooseOption = {
                id: null,
                label: 'Välj...'
            };

            // Make sure the right amount of rows exist
            var emptyRow = {
                beskrivning: undefined,
                datum: '',
                specifikation: null
            };

            function setupRows(){
                var rows = $scope.to.maxRows;
                var modelArray = $scope.model[$scope.options.key];
                if(!modelArray || modelArray.length === 0){
                    if(rows > 1){
                        modelArray.push(angular.copy(emptyRow));
                    }
                }
            }

            $scope.$on('intyg.loaded', setupRows);

            $scope.hasValidationError = function(field, index) {
                return $scope.formState.viewState.common.validation.messagesByField &&
                    !!$scope.formState.viewState.common.validation.messagesByField[$scope.options.key + '.' + index + '.' + field];
            };

            $scope.validate = function() {
                // When a date is selected from the date popup a blur event is sent.
                // In the current version of Angular UI this blur event is sent before utkast model is updated
                // This timeout ensures we get the new value in $scope.model
                $timeout(function() {
                    UtkastValidationService.validate($scope.model);
                });
            };

            $scope.$watch('formState.viewState.common.validation.messagesByField', function() {
                $scope.orsakValidations = [];
                angular.forEach($scope.formState.viewState.common.validation.messagesByField, function(validations, key) {
                    if (key.substr(0, $scope.options.key.length) === $scope.options.key.toLowerCase()) {
                        $scope.orsakValidations = $scope.orsakValidations.concat(validations);
                    }
                });
            });

            /*

            $scope.previousUnderlagIncomplete = function() {
                var prev = orsaker[orsaker.length - 1];
                return objectHelper.isEmpty(prev.typ) || objectHelper.isEmpty(prev.datum) || objectHelper.isEmpty(prev.hamtasFran);
            };

             */
            function updateOrsaker() {
                $scope.orsakOptions = [chooseOption];

                if ($scope.to.orsaksTyper) {
                    $scope.to.orsaksTyper.forEach(function (orsaksTyp) {
                        $scope.orsakOptions.push({
                            'id': orsaksTyp,
                            'label': dynamicLabelService.getProperty('KV_ORSAKSTYPER.' + orsaksTyp + '.RBK')
                        });
                    });
                }
            }

            $scope.$on('dynamicLabels.updated', function() {
                updateOrsaker();
            });

            updateOrsaker();
        }]
    });

});
