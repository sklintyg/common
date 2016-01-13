describe('UtkastNotifyService', function() {
    'use strict';

    var utkastNotifyService;

    beforeEach(angular.mock.module('common'), function($provide){
        $provide.value('common.UtkastNotifyProxy', {});
        $provide.value('common.messageService', {});
        $provide.value('common.dialogService', {});
        $provide.value('common.UtkastProxy', {});
    });

    beforeEach(angular.mock.inject(['common.UtkastNotifyService',
        function(_utkastNotifyService_) {
            utkastNotifyService = _utkastNotifyService_;
        }
    ]));

    describe('#notificationEncoding', function() {

        it ('should be unhandled if qa.status === ANSWERED', function () {
            var intygId = 'intyg-1';
            var intygType = 'fk7263';
            var enhetsNamn = 'Vårdenhet åäöÅÄÖ';
            var vardgivareNamn = 'Vårdgivare åäöÅÄÖ';
            expect(
                utkastNotifyService.buildNotifyDoctorMailToLink(intygId, intygType, enhetsNamn, vardgivareNamn).indexOf('aaoAAO') > -1
            ).toBeTruthy();
        });

    });

});
