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

describe('dynamiclink', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var $scope;
    var $compile;
    var _links = {
        'somekey': {
            'text': 'real text',
            'url': 'http://some.url',
            'tooltip': 'My tooltip!',
            'target': '_blank'
        }
    };

    // Create a <p> with a dynamic link to test the validation directive on.
    beforeEach(inject(
        ['$compile', '$rootScope', 'common.dynamicLinkService',
        function(_$compile_, $rootScope, dynamicLinkService) {
            dynamicLinkService.addLinks(_links);
            $scope = $rootScope.$new();
            $compile = _$compile_;
        }]
    ));


    it('Should print an anchor when key exists', function() {
        var el = angular.element('<p>This is text with <span dynamiclink key="somekey"></span></p>');
        $compile(el)($scope);
        $scope.$digest();

        expect(el.html()).toContain('href="http://some.url"');
        expect(el.html()).toContain('>real text<');
        expect(el.html()).toContain('title="My tooltip!"');
        expect(el.html()).toContain('target="_blank"');
    });

    it('Should print a warning anchor when key does not exist', function() {
        var el = angular.element('<p>This is text with <span dynamiclink key="otherkey"></span></p>');
        $compile(el)($scope);
        $scope.$digest();

        expect(el.html()).toContain('href="#"');
        expect(el.html()).toContain('>WARNING: could not resolve dynamic link: otherkey');
    });
});
