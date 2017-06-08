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

describe('wcIntygRelatedOtherIntygMessageDirective', function() {
    'use strict';

    var $scope;
    var element;

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.inject(['$compile', '$rootScope', function($compile, $rootScope) {
        $scope = $rootScope.$new();

        $rootScope.lang = 'sv';

        $scope.viewState =
        {
            common: {
                intygProperties: {
                    relation: {}
                }
            }
        };
        element = $compile(
            '<div wc-intyg-related-other-intyg-message text-before-relation="ersatts av" view-state="viewState" relation="relation"></div>'
        )($scope);

    }]));

    it('should display warning message when relation exists', function() {
        $scope.viewState.common.intygProperties.relation =
            {
                status: 'SIGNED'
            };
        $scope.$digest();

        expect(element.isolateScope().showMessage).toBe(true);
        expect($(element).find('span').text()).toContain('Intyget har ersatts av');
    });

    it('should NOT display warning message when relation exists but is to UTKAST', function() {
        $scope.viewState.common.intygProperties.relation =
            {
                status: 'DRAFT_COMPLETE'
            };
        $scope.$digest();

        expect(element.isolateScope().showMessage).toBe(false);
    });

    it('should NOT display warning message when relation is missing ', function() {
        $scope.viewState.common.intygProperties.relation = undefined;
        $scope.$digest();

        expect(element.isolateScope().showMessage).toBe(false);
    });

});
