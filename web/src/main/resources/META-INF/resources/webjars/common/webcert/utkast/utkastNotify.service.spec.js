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
