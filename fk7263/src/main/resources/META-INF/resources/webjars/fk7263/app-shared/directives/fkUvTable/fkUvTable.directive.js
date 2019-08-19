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
angular.module('fk7263').directive('fkUvTable', [
  '$log', 'uvUtil',
  function($log, uvUtil) {
    'use strict';

    return {
      restrict: 'E',
      scope: {
        config: '=',
        viewData: '='
      },
      templateUrl: '/web/webjars/fk7263/app-shared/directives/fkUvTable/fkUvTable.directive.html',
      link: function($scope) {

        if (!$scope.config) {
          $log.debug('no view config present for uv-table controller');
          return;
        }

        $scope.viewModel = {
          headers: [
            // 'headerValue1','headerValue2', 'headerValue3'
          ],
          rows: [
            // {valueProps: ['colValue1', 'colValue2', 'colValue3']},
          ]
        };

        // Convert headers config to viewModel values
        $scope.viewModel.headers = [];
        angular.forEach($scope.config.headers, function(header) {
          this.push(uvUtil.getTextFromConfig(header));
        }, $scope.viewModel.headers);

        // Check if valueProps are dot-paths or a function or just a simple value and resolve viewData accordingly
        $scope.viewModel.rows = [];
        angular.forEach($scope.config.rows, function(row) {

          var data = uvUtil.getValue($scope.viewData, row.key);

          if (uvUtil.isValidValue(data)) {
            var label = uvUtil.getTextFromConfig(row.label);
            var values = [label, data.from, data.tom];

            $scope.viewModel.rows.push({values: values, key: row.key});
          }
        });
      }
    };
  }]);
