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

        it ('should be unhandled if qa.status === ANSWERED', function () {
            var qa = {status:'ANSWERED'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeTruthy();
        });


        it ('should be unhandled if qa.status === PENDING_INTERNAL_ACTION && qa.amne === PAMINNELSE', function () {
            var qa = {status:'PENDING_INTERNAL_ACTION', amne : 'PAMINNELSE'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeTruthy();
        });


        it ('should be unhandled if qa.status === PENDING_EXTERNAL_ACTION && qa.amne === MAKULERING', function () {
            var qa = {status:'PENDING_EXTERNAL_ACTION', amne : 'MAKULERING'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeFalsy();
        });

        it ('should be unhandled if qa.status === ANSWERED && qa.amne === *', function () {
            var qa = {status:'ANSWERED', amne : 'PAMINNELSE'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeTruthy();
        });


        it ('should be unhandled if qa.status === PENDING_EXTERNAL_ACTION && qa.amne === MAKULERING', function () {
            var qa = {status:'PENDING_EXTERNAL_ACTION', amne : 'AHHG'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeFalsy();
        });

        it ('should be handled if qa.status != ANSWERED', function () {
            var qa = {status:'CLOSED'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeFalsy();
        });
    });

});
