describe('Filter: miRelevantStatus', function() {
    'use strict';

    var _filter;

    beforeEach(angular.mock.module('common'));

    beforeEach(inject(function(_miRelevantStatusFilterFilter_) {
        _filter = _miRelevantStatusFilterFilter_;
    }));

    it('should filter non-wanted statuses', function() {
        var statuses = [ {
            type: 'OTHER'
        }, {
            type: 'SENT'
        }, {
            type: 'UNKNOWN'
        } ];

        expect(_filter(statuses)).toEqual([statuses[1]]);
    });

    it('should handle no statuses', function() {
        var statuses = [];

        expect(_filter(statuses)).toEqual([]);
        expect(_filter(undefined)).toEqual([]);
    });

});
