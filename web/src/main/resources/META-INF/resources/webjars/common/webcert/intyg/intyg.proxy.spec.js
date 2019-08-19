/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

/* globals readJSON */
/* globals tv4 */
describe('IntygProxy', function() {
    'use strict';

    var $httpBackend;
    var IntygProxy;

    beforeEach(angular.mock.module('common', function($provide) {}));

    beforeEach(angular.mock.inject(['$httpBackend', 'common.IntygProxy', function(_$httpBackend_, _IntygProxy_) {
        $httpBackend = _$httpBackend_;
        IntygProxy = _IntygProxy_;
    }]));

    it('sendIntyg request should validate with json schema', function() {

        var data = function(data) {
            data = JSON.parse(data);
            var schema = readJSON('test/resources/jsonschema/webcert-send-intyg-request-schema.json');
            return tv4.validate(data, schema);
        };
        data.toString = function() {
            return tv4.error.toString();
        };

        $httpBackend.expectPOST('/moduleapi/intyg/intygsTyp/intygsId/skicka', data).respond(200);

        IntygProxy.sendIntyg('intygsId', 'intygsTyp', 'recipientId',  function(){}, function(){});

        $httpBackend.flush();
    });

});
