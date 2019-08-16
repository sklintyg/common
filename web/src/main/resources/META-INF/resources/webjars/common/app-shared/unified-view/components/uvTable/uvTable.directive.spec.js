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

describe('uvTable', function() {
  'use strict';

  var $scope;
  var element;
  var $state;
  var $window;

  // Load the webcert module and mock away everything that is not necessary.
  beforeEach(angular.mock.module('common', function($provide) {
  }));

  beforeEach(angular.mock.module('htmlTemplates'));

  beforeEach(angular.mock.inject(['$window', '$state',
    function(_$window_, _$state_) {
      $window = _$window_;
      $state = _$state_;
    }]));

  describe('config', function() {

    describe('simple', function() {

      beforeEach(angular.mock.inject(['$controller', '$compile', '$rootScope',
        function($controller, $compile, $rootScope) {
          $scope = $rootScope.$new();

          $scope.config = {
            type: 'uv-table',
            headers: ['DFR_6.2.RBK', ''], // labels for th cells
            valueProps: ['diagnosKod', 'diagnosBeskrivning'], // properties on diagnoser entries to use in each rows cells
            modelProp: 'diagnoser'
          };

          $scope.viewData = {};
          $scope.viewData.diagnoser = [
            {
              diagnosBeskrivning: 'Brännskada av första graden på höft och nedre extremitet utom fotled och fot',
              diagnosKod: 'T241',
              diagnosKodSystem: 'ICD_10_SE'
            }
          ];

          element = $compile('<uv-table config="config" view-data="viewData"></uv-table>')($scope);

          $rootScope.$digest();
          $scope = element.isolateScope();
        }]));

      it('should generate viewModel to template', function() {
        $scope.$apply();
        expect($scope.viewModel.modelProp).toBeDefined();
        expect($scope.viewModel.headers.length).toBe(2);
        expect($scope.viewModel.rows.length).toBe(1);
      });
    });

    describe('complex', function() {

      beforeEach(angular.mock.inject(['$controller', '$compile', '$rootScope',
        function($controller, $compile, $rootScope) {
          $scope = $rootScope.$new();

          $scope.config = {
            type: 'uv-table',
            headers: ['Nedsättningsgrad', 'Från och med', 'Till och med'],
            valueProps: [
              'KV_FKMU_0003.{sjukskrivningsgrad}.RBK',
              'period.from',
              'period.tom'
            ],
            modelProp: 'sjukskrivningar'
          };

          $scope.viewData = {};
          $scope.viewData.sjukskrivningar = [
            {
              sjukskrivningsgrad: 'HELT_NEDSATT',
              period: {
                from: '2017-01-01',
                tom: '2017-02-02'
              }
            }
          ];

          element = $compile('<uv-table config="config" view-data="viewData"></uv-table>')($scope);

          $rootScope.$digest();
          $scope = element.isolateScope();
        }]));

      it('should generate viewModel', function() {
        $scope.$apply();
        expect($scope.viewModel.modelProp).toBeDefined();
        expect($scope.viewModel.headers.length).toBe(3);
        expect($scope.viewModel.rows.length).toBe(1);
      });
    });

  });

});
