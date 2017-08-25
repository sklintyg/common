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

fdescribe('wcIntygRelatedRevokedMessageDirective', function() {
    'use strict';

    var element;
    var scope;
    var controller;

    var IntygProxy;
    var intygsId = 'intyg-1';
    var intygsTyp = 'lisjp';

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.module('common', function($provide) {
        IntygProxy = jasmine.createSpyObj('common.IntygProxy', [ 'getIntyg', 'load' ]);
        $provide.value('common.IntygProxy', IntygProxy);
    }));

    beforeEach(angular.mock.inject(['$compile', '$rootScope', function($compile, $rootScope) {
        scope = $rootScope.$new();

        $rootScope.lang = 'sv';

        scope.viewState =
        {
            common: {
                intygProperties: {
                    isRevoked: true,
                    parent: {
                        intygsId: intygsId,
                        relationKod: 'ERSATT',
                        status: 'SIGNED'
                    },
                    type: intygsTyp
                },
                isIntygOnRevokeQueue: false
            }
        };
        scope.intygRelation = {};

        // Instantiate directive.
        // gotcha: Controller and link functions will execute.
        element = $compile(
            '<div wc-intyg-related-revoked-message view-state="viewState"></div>'
        )(scope);
        $rootScope.$digest();

        // Grab controller instance
        controller = element.controller('wcIntygRelatedRevokedMessageDirective');

        // Grab scope. Depends on type of scope.
        // See angular.element documentation.
        scope = element.isolateScope() || element();
    }]));


    it('should display a replaced warning message when relation exists and latest state is other than CANCELLED ', function() {
        var json =
            {
                statuses: [
                    {type: 'SENT', target: 'HSVARD', timestamp: '2016-08-10T16:05:07.000'},
                    {type: 'RECEIVED', target: 'HSVARD', timestamp: '2016-08-10T16:04:07.000'}
                ]
            }

        scope.intygRelation.states = json.statuses;
        scope.$digest();

        expect(scope.showMessage()).toBe(true);
        expect($(element).html()).toContain('id="certificate-is-revoked-message-text"');
        expect($(element).html()).toContain('id="certificate-revoked-replace-other-it-message-text"');
    });

    it('should NOT display a replaced warning message when relation exists and latest state is CANCELLED ', function() {
        var json =
            {
                statuses: [
                    {type: 'CANCELLED', target: 'HSVARD', timestamp: '2016-08-10T16:07:12.000'},
                    {type: 'SENT', target: 'HSVARD', timestamp: '2016-08-10T16:05:07.000'},
                    {type: 'RECEIVED', target: 'HSVARD', timestamp: '2016-08-10T16:04:07.000'}
                ]
            }

        scope.intygRelation.states = json.statuses;
        scope.$digest();

        expect(scope.showMessage()).toBe(false);
        expect($(element).html()).toContain('id="certificate-is-revoked-message-text"');
        expect($(element).html()).not.toContain('id="certificate-revoked-replace-other-it-message-text"');
    });

    xit('should NOT display any warning message when certificate is not revoked ', function() {
        scope.viewState.common.intygProperties.isRevoked = false;
        scope.$digest();

        expect($(element).html()).not.toContain('id="certificate-is-revoked-message-text"');
    });

});
