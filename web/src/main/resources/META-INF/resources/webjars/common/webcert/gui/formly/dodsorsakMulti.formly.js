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
        name: 'dodsorsakMulti',
        templateUrl: '/web/webjars/common/webcert/gui/formly/dodsorsakMulti.formly.html',
        defaultOptions: {
            className: 'slide-animation'
        },
        controller: ['$scope', '$timeout', 'common.UtkastValidationService', 'common.dynamicLabelService',
            function($scope, $timeout, UtkastValidationService, dynamicLabelService) {

                var rows;

                $scope.addRow = function() {

                    if(rows.length >= $scope.to.maxRows){
                        return;
                    }

                    rows.push({beskrivning: null, datum: null, specifikation: null});
                    $scope.form.$setDirty();
                };

                $scope.deleteRow = function(index) {

                    if(index === 0 && rows.length === 1){
                        rows[0] = {beskrivning: null, datum: null, specifikation: null};
                    } else {
                        rows.splice(index, 1);
                    }

                    $scope.form.$setDirty();
                };

                $scope.hasValidationError = function(field, index) {
                    return $scope.formState.viewState.common.validation && $scope.formState.viewState.common.validation.messagesByField &&
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
                    if ($scope.formState.viewState.common.validation) {
                        $scope.orsakValidations = [];
                        angular.forEach($scope.formState.viewState.common.validation.messagesByField, function(validations, key) {
                            if (key.substr(0, $scope.options.key.length) === $scope.options.key.toLowerCase()) {
                                $scope.orsakValidations = $scope.orsakValidations.concat(validations);
                            }
                        });
                    }
                });

                var chooseOption = {
                    id: null,
                    label: 'Välj...'
                };

                function update() {

                    rows = $scope.model[$scope.options.key];

                    $scope.orsakOptions = [chooseOption];

                    if ($scope.to.orsaksTyper) {
                        $scope.to.orsaksTyper.forEach(function(orsaksTyp) {
                            $scope.orsakOptions.push({
                                'id': orsaksTyp,
                                'label': dynamicLabelService.getProperty('KV_ORSAKSTYPER.' + orsaksTyp + '.RBK')
                            });
                        });
                    }
                }

                $scope.$on('intyg.loaded', function() {
                    update();
                });

            }]
    });

});
