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
angular.module('common').directive('ueDodsorsakMulti',
    ['ueUtil', '$timeout', 'common.UtkastValidationService', 'common.UtkastValidationViewState', 'common.dynamicLabelService',
      function(ueUtil, $timeout, UtkastValidationService, UtkastValidationViewState, dynamicLabelService) {
        'use strict';

        return {
          restrict: 'E',
          scope: {
            form: '=',
            config: '=',
            model: '='
          },
          templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueDodsorsak/ueDodsorsakMulti.directive.html',
          link: function($scope) {
            ueUtil.standardSetup($scope);

            $scope.validation = UtkastValidationViewState;

            var rows;

            $scope.addRow = function() {

              if (rows.length >= $scope.config.maxRows) {
                return;
              }

              rows.push({beskrivning: null, datum: null, specifikation: ''});
              $scope.form.$setDirty();
            };

            $scope.deleteRow = function(index) {

              if (index === 0 && rows.length === 1) {
                rows[0] = {beskrivning: null, datum: null, specifikation: null};
              } else {
                rows.splice(index, 1);
              }

              $scope.form.$setDirty();
            };

            $scope.hasValidationError = function(field, index) {
              return $scope.validation && $scope.validation.messagesByField &&
                  !!$scope.validation.messagesByField[$scope.config.modelProp.toLowerCase() + '[' + index +
                  '].' + field];
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
              if ($scope.validation) {
                $scope.orsakValidations = [];
                angular.forEach($scope.validation.messagesByField, function(validations, key) {
                  if (key.substr(0, $scope.config.modelProp.length) ===
                      $scope.config.modelProp.toLowerCase()) {
                    $scope.orsakValidations = $scope.orsakValidations.concat(validations);
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
              if ($scope.config.orsaksTyper) {
                $scope.config.orsaksTyper.forEach(function(orsaksTyp) {
                  $scope.orsakOptions.push({
                    'id': orsaksTyp,
                    'label': dynamicLabelService.getProperty('DELAD_SVAR.' + orsaksTyp + '.RBK')
                  });
                });
              }
            }

            $scope.$on('dynamicLabels.updated', update);

            function setRows() {
              rows = $scope.model[$scope.config.modelProp];
            }

            $scope.$on('intyg.loaded', setRows);

            setRows();
            update();
          }
        };
      }
    ]);
