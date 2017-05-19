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

    describe('enableLinks', function() {

        beforeEach(angular.mock.inject(['$controller', '$compile', '$rootScope',
            function($controller, $compile, $rootScope) {
                $scope = $rootScope.$new();
                element = $compile('<uv-table></uv-table>')($scope);

                $rootScope.$digest();
                $scope = element.isolateScope();
            }]));

        it('should default to enabled', function() {
            $scope.$apply();
            expect($scope.enableLinks).toBe(true);
        });

    });

});
