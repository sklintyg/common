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

angular.module('common').directive('ueSjukfranvaro', ['common.SjukfranvaroViewStateService',
  'common.UtkastValidationService', 'common.messageService', 'common.UtkastViewStateService', 'common.AtticHelper', '$timeout',
  function(viewstate, UtkastValidationService, messageService, UtkastViewState, AtticHelper, $timeout) {
    'use strict';
    return {
      restrict: 'E',
      scope: {
        form: '=',
        config: '=',
        model: '='
      },
      templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueSjukfranvaro/ueSjukfranvaro.directive.html',
      link: function($scope) {

        AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

        // Clear attic model and destroy watch on scope destroy
        AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

        var sjukfranvaroArray = $scope.model[$scope.config.modelProp];

        if (sjukfranvaroArray === undefined) {
          sjukfranvaroArray = [];
        }

        if (sjukfranvaroArray.length === 0) {
          sjukfranvaroArray.push({
            niva: 100,
            checked: false,
            period: {
              from: '',
              tom: ''
            }
          });
          sjukfranvaroArray.push({
            niva: undefined,
            checked: false,
            period: {
              from: '',
              tom: ''
            }
          });
        }

        var validation = $scope.validation = UtkastViewState.validation;

        var indexFieldMatcher = new RegExp('^' + $scope.config.modelProp + '\\[(\\d+)\\]\\.(.*)');

        $scope.$watch('validation.messages', function() {
              $scope.validationMessages = [];
              $scope.overlapValidations = [];

              if (!validation.messages) {
                return;
              }

              angular.forEach(validation.messages, function(message) {
                var fieldName = message.field;

                var matches = fieldName.match(indexFieldMatcher);
                if (matches !== null) {
                  var index = matches[1];
                  var field = matches[2];
                  if (message.type === 'PERIOD_OVERLAP') {
                    if ($scope.overlapValidations.length < 1) {
                      $scope.overlapValidations.push(message);
                    }
                  } else {
                    if (!$scope.validationMessages[index]) {
                      $scope.validationMessages[index] = {};
                    }

                    if (!$scope.validationMessages[index][field]) {
                      $scope.validationMessages[index][field] = [];
                    }
                    $scope.validationMessages[index][field].push(message);
                  }

                }
              });
            }
        );

        $scope.hasValidationError = function(index, type) {
          var key = $scope.config.modelProp + '[' + index + '].' + type;

          return validation.messagesByField &&
              validation.messagesByField[key];
        };

        $scope.validate = function() {
          // When a date is selected from the date popup a blur event is sent.
          // In the current version of Angular UI this blur event is sent before utkast model is updated
          // This timeout ensures we get the new value in $scope.model
          $timeout(function() {
            UtkastValidationService.validate($scope.model);
          });
        };
        $scope.viewstate = viewstate.reset();

        function setup() {

          viewstate.setup($scope.model[$scope.config.modelProp], $scope.config.maxRows, function() {
            $scope.form.$setDirty();
          });

          viewstate.updatePeriods();
          if ($scope.model.grundData.relation.sistaGiltighetsDatum) {
            $scope.lastEffectiveDateNoticeText = messageService
            .getProperty($scope.model.typ + '.help.sjukskrivningar.sista-giltighets-datum')
            .replace('{{lastEffectiveDate}}', $scope.model.grundData.relation.sistaGiltighetsDatum)
            .replace('{{sjukskrivningsgrad}}', $scope.model.grundData.relation.sistaSjukskrivningsgrad);
          }
        }

        setup();
        $scope.$on('intyg.loaded', function() {
          setup();
        });

        $scope.$watch('model.' + $scope.config.modelProp, function(newValue, oldValue) {
          viewstate.updatePeriods();
        }, true);
      }
    }
        ;

  }])
;
