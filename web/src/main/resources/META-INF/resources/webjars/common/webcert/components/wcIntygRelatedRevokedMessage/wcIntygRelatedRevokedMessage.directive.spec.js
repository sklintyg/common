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

describe('wcIntygRelatedRevokedMessageDirective', function() {
    'use strict';

    var element;
    var parentScope;
    var isolateScope;
    var controller;

    var intygsId = 'intyg-1';
    var intygsTyp = 'lisjp';

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));
    beforeEach(angular.mock.module(function($provide) {
        var IntygProxy = jasmine.createSpyObj('common.IntygProxy', [ 'getIntyg', 'load' ]);
        $provide.value('common.IntygProxy', IntygProxy);
    }));
    beforeEach(angular.mock.module({
        'common.messageService' : {
            getProperty: jasmine.createSpy(function(key) {return 'textfor:"' + key + '"';}),
            // Unused mock needed to avoid race condition in test where common module hasn't loaded completely and calls addResources().
            addResources: function() {},
            propertyExists: function() { return true; }
        }
    }));

    beforeEach(angular.mock.inject(['$compile', '$rootScope', function($compile, $rootScope) {
        parentScope = $rootScope.$new();

        $rootScope.lang = 'sv';

        parentScope.intygViewState = {
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
        parentScope.intygRelation = {};

        // Instantiate directive.
        // gotcha: Controller and link functions will execute.
        element = $compile(
            '<wc-intyg-related-revoked-message intyg-view-state="intygViewState"></wc-intyg-related-revoked-message>'
        )(parentScope);
        $rootScope.$digest();

        // Grab controller instance
        controller = element.controller('wcIntygRelatedRevokedMessageDirective');

        // Grab scope. Depends on type of scope.
        // See angular.element documentation.
        isolateScope = element.isolateScope();
    }]));


    xit('should display a replaced warning message when relation exists and latest state is other than CANCELLED ', function() {
        var json = {
            statuses: [
                {type: 'SENT', target: 'HSVARD', timestamp: '2016-08-10T16:05:07.000'},
                {type: 'RECEIVED', target: 'HSVARD', timestamp: '2016-08-10T16:04:07.000'}
            ]
        };

        parentScope.intygRelation.states = json.statuses;
        parentScope.$digest();

        expect(isolateScope.showMessage()).toBe(true);
        expect($(element).html()).toContain('id="certificate-is-revoked-message-text"');
        expect($(element).html()).toContain('id="certificate-revoked-replace-other-it-message-text"');
    });

    xit('should NOT display a replaced warning message when relation exists and latest state is CANCELLED ', function() {
        var json =
            {
                statuses: [
                    {type: 'CANCELLED', target: 'HSVARD', timestamp: '2016-08-10T16:07:12.000'},
                    {type: 'SENT', target: 'HSVARD', timestamp: '2016-08-10T16:05:07.000'},
                    {type: 'RECEIVED', target: 'HSVARD', timestamp: '2016-08-10T16:04:07.000'}
                ]
            };

        parentScope.intygRelation.states = json.statuses;
        parentScope.$digest();

        expect(isolateScope.showMessage()).toBe(false);
        expect($(element).html()).toContain('id="certificate-is-revoked-message-text"');
        expect($(element).html()).not.toContain('id="certificate-revoked-replace-other-it-message-text"');
    });

    it('should NOT display any warning message when certificate is not revoked ', function() {
        parentScope.intygViewState.common.intygProperties.isRevoked = false;
        parentScope.$digest();

        expect($(element).html()).not.toContain('id="certificate-is-revoked-message-text"');
    });

});
