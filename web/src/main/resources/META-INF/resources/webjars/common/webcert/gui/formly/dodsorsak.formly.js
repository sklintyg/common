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
/*
            var chooseOption = {
                id: null,
                label: 'Välj...'
            };

            $scope.underlagOptions = [];
            var underlag = $scope.model[$scope.options.key];

            $scope.previousUnderlagIncomplete = function() {
                var prev = underlag[underlag.length - 1];
                return objectHelper.isEmpty(prev.typ) || objectHelper.isEmpty(prev.datum) || objectHelper.isEmpty(prev.hamtasFran);
            };

            $scope.hasKomplettering = function() {
                return ArendeListViewState.hasKompletteringar($scope.options.key);
            };

            $scope.hasValidationError = function(field, index) {
                return $scope.formState.viewState.common.validation.messagesByField &&
                    !!$scope.formState.viewState.common.validation.messagesByField['underlag.' + index + '.' + field];
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
                $scope.underlagValidations = [];
                angular.forEach($scope.formState.viewState.common.validation.messagesByField, function(validations, key) {
                    if (key.substr(0, $scope.options.key.length) === $scope.options.key.toLowerCase()) {
                        $scope.underlagValidations = $scope.underlagValidations.concat(validations);
                    }
                });
            });

            function updateUnderlag() {
                $scope.underlagOptions = [chooseOption];

                if ($scope.to.underlagsTyper) {
                    $scope.to.underlagsTyper.forEach(function (underlagsTyp) {
                        $scope.underlagOptions.push({
                            'id': underlagsTyp,
                            'label': dynamicLabelService.getProperty('KV_FKMU_0005.' + underlagsTyp + '.RBK')
                        });
                    });
                }
            }

            $scope.$on('dynamicLabels.updated', function() {
                updateUnderlag();
            });

            updateUnderlag();*/
        }]
    });

});
