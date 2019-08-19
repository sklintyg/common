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

angular.module('common').directive('ueDiagnos', ['$log', '$timeout', 'common.DiagnosProxy', 'common.fmbViewState',
  'common.fmbService', 'common.srsService', 'common.ObjectHelper', 'common.MonitoringLogService',
  'common.ArendeListViewStateService', 'common.UtkastValidationService', 'common.UtkastViewStateService', 'common.AtticHelper',
  function($log, $timeout, diagnosProxy, fmbViewState, fmbService, srsService, ObjectHelper, monitoringService,
      ArendeListViewState, UtkastValidationService, UtkastViewState, AtticHelper) {
    'use strict';

    return {
      restrict: 'E',
      scope: {
        config: '=',
        model: '='
      },
      templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueDiagnos/ueDiagnos.directive.html',
      link: function($scope) {

        AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

        // Clear attic model and destroy watch on scope destroy
        AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

        $scope.validation = UtkastViewState.validation;

        $scope.hasKomplettering = function() {
          return ArendeListViewState.hasKompletteringar($scope.config.modelProp);
        };

        // Only add listener if fmb and/or srs should be notified
        if ($scope.config.notifyFmb || $scope.config.notifySrs) {
          $scope.$watch('model.' + $scope.config.modelProp + '[0].diagnosKod', function(newVal) {
            if ($scope.config.notifyFmb) {
              updateFmb(0, newVal);
            }
            if ($scope.config.notifySrs) {
              updateSrs(0, newVal);
            }
          });
        }

        // Only add listeners if fmb should be notified.
        if ($scope.config.notifyFmb) {
          $scope.$watch('model.' + $scope.config.modelProp + '[1].diagnosKod', function(newVal) {
            updateFmb(1, newVal);
          });

          $scope.$watch('model.' + $scope.config.modelProp + '[2].diagnosKod', function(newVal) {
            updateFmb(2, newVal);
          });
        }

        function updateFmb(index, newVal) {
          if (ObjectHelper.isEmpty(newVal) || newVal.length < 3) {
            fmbViewState.reset(index);
            fmbService.updateFmbText(index, null);
          } else {
            fmbService.updateFmbText(index,
                $scope.model[$scope.config.modelProp][index].diagnosKod,
                $scope.model[$scope.config.modelProp][index].diagnosKodSystem,
                $scope.model[$scope.config.modelProp][index].diagnosBeskrivning);
          }
        }

        function updateSrs(index, newVal) {
          if (ObjectHelper.isEmpty(newVal) || newVal.length < 3) {
            srsService.updateDiagnosKod(null);
            srsService.updateDiagnosBeskrivning(null);
          } else {
            srsService.updateDiagnosKod(newVal);
            srsService.updateDiagnosBeskrivning($scope.model[$scope.config.modelProp][index].diagnosBeskrivning);
          }
        }

        // Split validations on different rows
        $scope.$watch('validation.messagesByField', function() {
          $scope.diagnosValidations = [];
          angular.forEach($scope.validation.messagesByField,
              function(validations, key) {
                if (key.substr(0, $scope.config.modelProp.length + 1) === $scope.config.modelProp.toLowerCase() + '[') {
                  var index = parseInt(key.substr($scope.config.modelProp.length + 1, 1), 10);
                  if (typeof $scope.diagnosValidations[index] === 'undefined') {
                    $scope.diagnosValidations[index] = [];
                  }
                  $scope.diagnosValidations[index].push(validations[0]);
                }
              });
        });

        // Return validation errors for the specific row (previously splitted)
        $scope.getValidationErrors = function(index) {
          return $scope.diagnosValidations[index] || undefined;
        };
      }
    };
  }]);

