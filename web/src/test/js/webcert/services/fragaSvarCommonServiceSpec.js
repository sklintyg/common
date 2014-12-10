describe('fragaSvarCommonService', function() {
    'use strict';

    var fragaSvarCommonService;

    beforeEach(angular.mock.module('common'), function($provide){
        $provide.value('common.dialogService', {});
        $provide.value('common.User', {});
    });

    beforeEach(angular.mock.inject(['common.fragaSvarCommonService',
        function(_fragaSvarCommonService_) {
            fragaSvarCommonService = _fragaSvarCommonService_;
        }
    ]));

    describe('#isUnhandled', function() {

        it ('should be unhandled if qa.status == ANSWERED', function () {
            var qa = {status:'ANSWERED'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeTruthy();
        });

        it ('should be handled if qa.status != ANSWERED', function () {
            var qa = {status:'CLOSED'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeFalsy();
        });
    });

});
