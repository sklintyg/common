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
angular.module('common').directive('uvList', ['uvUtil', function(uvUtil) {
  'use strict';

  return {
    restrict: 'E',
    scope: {
      config: '=',
      viewData: '='
    },
    templateUrl: '/web/webjars/common/app-shared/unified-view/components/uvList/uvList.directive.html',
    link: function($scope) {
      $scope.valueList = _getValues();

      function _getValues() {

        var listData = uvUtil.getValue($scope.viewData, $scope.config.modelProp);

        var finalListData = [];

        if (typeof $scope.config.listKey === 'function') {
          for (var i = 0; i < listData.length; i++) {
            var result = $scope.config.labelKey;
            var listValue = $scope.config.listKey(listData[i], i, listData.length);
            if (listValue) {
              finalListData.push(result.replace('{var}', listValue));
            }
          }
        } else {
          finalListData = replaceVar(listData);
        }

        function replaceVar(listData) {
          var tempListData = [];
          // Use labelKey as pattern if a {var} is present, otherwise just use the list as is
          if ($scope.config.labelKey.indexOf('{var}') !== -1 && listData) {
            tempListData = listData.map(function(item) {
              var result = $scope.config.labelKey;
              return result.replace('{var}', typeof item === 'object' ? item[$scope.config.listKey] : item);
            });
          } else {
            tempListData = listData;
          }

          return tempListData;
        }

        return finalListData;

      }
    }

  };
}]);
