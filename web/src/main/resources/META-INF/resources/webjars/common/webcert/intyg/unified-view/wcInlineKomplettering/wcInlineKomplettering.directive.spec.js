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

describe('wcInlineKomplettering Directive', function() {
    'use strict';

    var $scope;
    var element;

    var answer = [ {
        text: 'kompletteringstext1'
    } ];
    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.ArendeListViewStateService', {
            getKompletteringarForFraga: function(frageId) {
                return answer;
            }
        });
    }));

    beforeEach(angular.mock.inject([ '$compile', '$rootScope', function($compile, $rootScope) {
        $scope = $rootScope.$new();

        element = $compile('<wc-inline-komplettering frage-id="frageId"></wc-inline-komplettering>')($scope);

    } ]));

    it('should render kompletteringstexten when komplettering exists', function() {
        $scope.frageId = 'FRG_25.RBK';
        $scope.$digest();
        expect($(element).find('.inline-komplettering-text').text()).toBe('Kompletteringsbegäran: kompletteringstext1');
    });

    it('should render nothing when no komplettering exists', function() {
        $scope.frageId = 'apa';
        $scope.$digest();
        expect($(element).find('div.alert-warning').length).toBe(0);
    });


});
