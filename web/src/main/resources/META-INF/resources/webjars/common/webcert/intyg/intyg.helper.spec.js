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

describe('IntygHelper', function() {
    'use strict';

    var IntygHelper, $stateSpy, $logSpy;

    beforeEach(angular.mock.module('common', function($provide) {
        $stateSpy = jasmine.createSpyObj('$state', [ 'go' ]);
        $provide.value('$state', $stateSpy);
        $logSpy = jasmine.createSpyObj('$log', [ 'error' ]);
        $provide.value('$log', $logSpy);
    }));

    beforeEach(angular.mock.inject([ 'common.IntygHelper', function(_IntygHelper_) {
        IntygHelper = _IntygHelper_;
    } ]));
    //Draft
    it('should go to draft if all mandatory parameters are present', function() {

        IntygHelper.goToDraft('type', '1.2', 'abc123');
        expect($stateSpy.go).toHaveBeenCalledWith('type.utkast', {
            certificateId: 'abc123',
            intygTypeVersion: '1.2'
        });

    });

    it('should log error if type missing', function() {
        IntygHelper.goToDraft('', '1.2', 'abc123');
        expect($logSpy.error).toHaveBeenCalled();
        expect($stateSpy.go).not.toHaveBeenCalled();
    });

    it('should log error if intygTypeVersion missing', function() {
        IntygHelper.goToDraft('type', '', 'abc123');
        expect($logSpy.error).toHaveBeenCalled();
        expect($stateSpy.go).not.toHaveBeenCalled();
    });

    it('should log error if certificateId missing', function() {
        IntygHelper.goToDraft('type', '1.2');
        expect($logSpy.error).toHaveBeenCalled();
        expect($stateSpy.go).not.toHaveBeenCalled();
    });

    //Intyg

    it('should go to intyg if all mandatory parameters are present', function() {

        IntygHelper.goToIntyg('type', '1.2', 'abc123');
        expect($stateSpy.go).toHaveBeenCalledWith('webcert.intyg.type', {
            certificateId: 'abc123',
            intygTypeVersion: '1.2'
        });

    });

    it('should log error if type missing', function() {
        IntygHelper.goToIntyg('', '1.2', 'abc123');
        expect($logSpy.error).toHaveBeenCalled();
        expect($stateSpy.go).not.toHaveBeenCalled();
    });

    it('should log error if intygTypeVersion missing', function() {
        IntygHelper.goToIntyg('type', '', 'abc123');
        expect($logSpy.error).toHaveBeenCalled();
        expect($stateSpy.go).not.toHaveBeenCalled();
    });

    it('should log error if certificateId missing', function() {
        IntygHelper.goToIntyg('type', '1.2');
        expect($logSpy.error).toHaveBeenCalled();
        expect($stateSpy.go).not.toHaveBeenCalled();
    });

});
