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
        } ];

        expect(_filter(events)).toEqual([events[1]]);
    });

    it('should handle no events', function() {
        var events = [];

        expect(_filter(events)).toEqual([]);
        expect(_filter(undefined)).toEqual([]);
    });

});
