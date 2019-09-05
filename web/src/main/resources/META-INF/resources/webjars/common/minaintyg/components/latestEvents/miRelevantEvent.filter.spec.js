describe('Filter: miRelevantEvent', function() {
    'use strict';

    var _filter;

    beforeEach(angular.mock.module('common'));

    beforeEach(inject(function(_miRelevantEventFilterFilter_) {
        _filter = _miRelevantEventFilterFilter_;
    }));

    it('should filter non-wanted events', function() {
        var events = [ {
            type: 'OTHER'
        }, {
            type: 'SENT'
        }, {
            type: 'UNKNOWN'
        }, {
            type: 'ERSATT'
        }, {
            type: 'ERSATTER'
        }, {
            type: 'KOMPLETTERAT'
        }, {
            type: 'KOMPLETTERAR'
        } ];
        expect(_filter(events).length).toEqual(5);
        expect(_filter(events)[0].type).toEqual('SENT');
        expect(_filter(events)[1].type).toEqual('ERSATT');
        expect(_filter(events)[2].type).toEqual('ERSATTER');
        expect(_filter(events)[3].type).toEqual('KOMPLETTERAT');
        expect(_filter(events)[4].type).toEqual('KOMPLETTERAR');
    });

    it('should handle no events', function() {
        var events = [];

        expect(_filter(events)).toEqual([]);
        expect(_filter(undefined)).toEqual([]);
    });

});
