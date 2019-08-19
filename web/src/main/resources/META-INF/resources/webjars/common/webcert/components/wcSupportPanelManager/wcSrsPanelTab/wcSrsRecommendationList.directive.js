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
/**
 * Recommendation list directive
 */
angular.module('common').directive('wcSrsRecommendationList', [
  'common.srsProxy',
  function(srsProxy) {
    'use strict';

    return {
      restrict: 'E',
      scope: {
        title: '@',
        moreTitle: '@',
        lessTitle: '@',
        recommendations: '=',
        srs: '=srsScope'
      },
      link: function($scope, element, $attrs) {
        $scope.$watch('recommendations', function(newValue, oldValue) {
          if ($scope.recommendations) {
            // Slice åtgärder into 4 + the rest
            $scope.diagnosisCode = $scope.recommendations.diagnosisCode;
            $scope.diagnosisDescription = $scope.recommendations.diagnosisDescription;
            $scope.firstRecommendations = $scope.recommendations.atgarder.slice(0, 4);
            $scope.moreRecommendations =
                $scope.recommendations.atgarder.slice(4).length > 0 ? $scope.recommendations.atgarder.slice(4) : null;
          }
        });
        $scope.listCollapserClicked = function() {
          if ($scope.isMoreCollapsed) {
            srsProxy.logSrsMeasuresShowMoreClicked($scope.srs.userClientContext, $scope.srs.intygId,
                $scope.srs.vardgivareHsaId, $scope.srs.hsaId);
          }
          $scope.isMoreCollapsed = !$scope.isMoreCollapsed;
        };
        if (!$attrs.moreTitle) {
          $scope.moreTitle = 'Se fler';
        }
        if (!$attrs.lessTitle) {
          $scope.lessTitle = 'Se färre';
        }
        $scope.diagnosisCode = 'Laddar diagnoskod';
        $scope.diagnosisDescription = 'Laddar diagnosbeskrivning';
        $scope.isMoreCollapsed = true;
      },
      templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsRecommendationList.directive.html'
    };
  }]);
