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

        IntygProxy.sendIntyg('intygsId', 'intygsTyp', 'recipientId', true, function(){}, function(){});

        $httpBackend.flush();
    });

});