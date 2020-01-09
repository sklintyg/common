/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

describe('wcIntygHasKomplettering Directive', function() {
    'use strict';

    var $scope;
    var element;
    var kompletteringCount;


    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.ArendeListViewStateService', {
            getUnhandledKompletteringCount: function() {
                return kompletteringCount;
            }
        });
    }));

    beforeEach(angular.mock.inject([ '$compile', '$rootScope', function($compile, $rootScope) {
        $scope = $rootScope.$new();
        element = $compile('<wc-intyg-has-komplettering-message></wc-intyg-has-komplettering-message>')($scope);

    } ]));

    it('should display message when komplettering exists', function() {
        kompletteringCount = 1;
        $scope.$digest();
        expect($(element).find('#intyg-has-komplettering-message').length).toBe(1);
    });

    it('should not display message when no komplettering exists', function() {
        kompletteringCount = 0;
        $scope.$digest();
        expect($(element).find('#intyg-has-komplettering-message').length).toBe(0);
    });


});
