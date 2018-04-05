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
        } ];
        expect(_filter(events).length).toEqual(3);
        expect(_filter(events)[0].type).toEqual('SENT');
        expect(_filter(events)[1].type).toEqual('ERSATT');
        expect(_filter(events)[2].type).toEqual('ERSATTER');
    });

    it('should handle no events', function() {
        var events = [];

        expect(_filter(events)).toEqual([]);
        expect(_filter(undefined)).toEqual([]);
    });

});
