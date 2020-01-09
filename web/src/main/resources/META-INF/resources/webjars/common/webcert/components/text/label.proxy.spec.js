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

describe('Proxy: DynamicLabelProxy', function() {
    'use strict';

    // Load the module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    var DynamicLabelProxy, $rootScope, $httpBackend;

    var textResponse = {'text':''};

    // Initialize the controller and a mock scope
    beforeEach(angular.mock.inject(['$rootScope', '$httpBackend', 'common.DynamicLabelProxy',
        function(_$rootScope_, _$httpBackend_, _dynamicLabelProxy_) {
            $httpBackend = _$httpBackend_;
            DynamicLabelProxy = _dynamicLabelProxy_;
            $rootScope = _$rootScope_;
        }]));

    describe('getDynamicLabels', function() {
        it('should get the dynamiclabels for intystyp and version', function() {
            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');
            $httpBackend.expectGET('/api/utkast/questions/luse/1.0').respond(textResponse);

            DynamicLabelProxy.getDynamicLabels('luse', '1.0').then(onSuccess, onError);
            $httpBackend.flush();
            // promises are resolved/dispatched only on next $digest cycle
            $rootScope.$apply();

            expect(onSuccess).toHaveBeenCalled();
            expect(onError).not.toHaveBeenCalled();
        });
    });
});
