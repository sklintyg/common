/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

describe('srsProxy', function() {
    'use strict';

    var $httpBackend;
    var SrsProxy;

    beforeEach(angular.mock.module('common', function($provide) {}));

    beforeEach(angular.mock.inject(['$httpBackend', 'common.srsProxy', function(_$httpBackend_, _SrsProxy_) {
        $httpBackend = _$httpBackend_;
        SrsProxy = _SrsProxy_;
    }]));

    it('getPredictionsFromResponseData should return according to data', function() {

        var data = {
            predictions: [{
                diagnosisDescription: 'pdd',
                diagnosisCode: 'pdc',
                description: 'pd',
                level: 'pl',
                statusCode: 'psc',
                probabilityOverLimit: 'ppol',
                prevalence: 'pp',
                physiciansOwnOpinionRisk: 'ppoor',
                questionsResponses: 'pqr',
                timestamp: '2019-06-11T09:34:04.000',
                somethingCompletelyDifferent: 'FOO'
            }]
        };
        var r = SrsProxy.__test__.getPredictionsFromResponseData(data);

        expect(r.length).toEqual(1);
        expect(r[0].diagnosisDescription).toEqual('pdd');
        expect(r[0].description).toEqual('pd');
        expect(r[0].statusCode).toEqual('psc');
        expect(r[0].timestamp).toEqual('2019-06-11T09:34:04.000');
        expect(r[0].date).toEqual('2019-06-11');
        expect(r[0].somethingCompletelyDifferent).toEqual(undefined);

    });

    it('getPredictionsFromResponseData should not fail on undefined entries', function() {

        var data = {
            predictions: [{
                diagnosisDescription: 'pdd',
                diagnosisCode: 'pdc',
                description: 'pd',
                level: 'pl',
                statusCode: 'psc',
                probabilityOverLimit: 'ppol',
                prevalence: 'pp',
                physiciansOwnOpinionRisk: 'ppoor',
                questionsResponses: 'pqr',
                completelyDifferent: 'FOO'
            }]
        };
        var r = SrsProxy.__test__.getPredictionsFromResponseData(data);

        expect(r.length).toEqual(1);
        expect(r[0].diagnosisDescription).toEqual('pdd');
        expect(r[0].description).toEqual('pd');
        expect(r[0].statusCode).toEqual('psc');
        expect(r[0].timestamp).toEqual(undefined);
        expect(r[0].date).toEqual(undefined);
        expect(r[0].somethingCompletelyDifferent).toEqual(undefined);

    });

    it('getPredictionsFromResponseData should return error on error', function() {
        var data = 'error';
        var r = SrsProxy.__test__.getPredictionsFromResponseData(data);
        expect(r).toEqual('error');

    });

    it('getStatistikFromResponseData should return null on no valid indata', function() {
        var data = {foo:'bar'};
        var r = SrsProxy.__test__.getStatistikFromResponseData(data);
        expect(r).toEqual(null);

    });

    it('logSrsMonitoring should request log on correct input', function() {
        SrsProxy.__test__.logSrsMonitoring('SRS_LOADED', 'UTK', 'iId', 'cgId', 'cuId','D01' );

        var expectedData = {
            event: 'SRS_LOADED',
            info: {
                userClientContext: 'UTK',
                intygId: 'iId',
                caregiverId: 'cgId',
                careUnitId: 'cuId',
                mainDiagnosisCode: 'D01'
            }
        };

        $httpBackend.expectPOST('/api/jslog/srs', expectedData).respond(200);
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.flush();

    });

    it('logSrsPanelActivated should send correct log event', function() {
        SrsProxy.logSrsPanelActivated('UTK', 'iId', 'cgId', 'cuId', 'D01');

        var expectedData = {
            event: 'SRS_PANEL_ACTIVATED',
            info: {
                userClientContext: 'UTK',
                intygId: 'iId',
                caregiverId: 'cgId',
                careUnitId: 'cuId',
                mainDiagnosisCode: undefined
            }
        };
        $httpBackend.expectPOST('/api/jslog/srs', expectedData).respond(200);
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.flush();

    });


    it('logSrsMonitoring should not request log on incorrect input', function() {
        SrsProxy.__test__.logSrsMonitoring('NON_EXISTING_LOG_EVENT_TYPE');
        $httpBackend.verifyNoOutstandingRequest();
    });

});
