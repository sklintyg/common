/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

describe('wcSrsHelpDisplayDirective', function() {
    'use strict';

    var $httpBackend;
    var $scope;
    var element;
    var srsService;
    var srsViewState;
    var srsProxy;
    var authorityService;
    var $compile;
    var $q;
    var $rootScope;

    /* jshint maxlen: false */
    var diagnosisTestJson = ['M18', 'J20', 'Q10'];
    var questionsTestJson = [{'questionId':'2','text':'Fragetext 2','helpText':'Hjälptext 2','priority':3,'answerOptions':[{'text':'Svarsalternativ 2','id':'stud','priority':2,'defaultValue':false},{'text':'Svarsalternativ 1','id':'stud','priority':8,'defaultValue':true},{'text':'Svarsalternativ 3','id':'stud','priority':10,'defaultValue':false},{'text':'Svarsalternativ 4','id':'stud','priority':10,'defaultValue':false}]},{'questionId':'3','text':'Fragetext 3','helpText':'Hjälptext 3','priority':3,'answerOptions':[{'text':'Svarsalternativ 2','id':'stud','priority':1,'defaultValue':false},{'text':'Svarsalternativ 3','id':'stud','priority':4,'defaultValue':false},{'text':'Svarsalternativ 4','id':'stud','priority':4,'defaultValue':false},{'text':'Svarsalternativ 1','id':'stud','priority':5,'defaultValue':true}]},{'questionId':'1','text':'Fragetext 1','helpText':'Hjälptext 1','priority':9,'answerOptions':[{'text':'Svarsalternativ 2','id':'stud','priority':5,'defaultValue':false},{'text':'Svarsalternativ 1','id':'stud','priority':6,'defaultValue':true},{'text':'Svarsalternativ 3','id':'stud','priority':6,'defaultValue':false},{'text':'Svarsalternativ 4','id':'stud','priority':7,'defaultValue':false}]}];
    var srsTestJson = {'atgarderObs':['Atgardsforslag OBS 1','Atgardsforslag OBS 2','Atgardsforslag OBS 3'],'atgarderRek':['Atgardsforslag REK 1','Atgardsforslag REK 2','Atgardsforslag REK 3'],'statistikBild':'/services/stubs/srs-statistics-stub','diagnosisCode':'J20','atgarderStatusCode':'OK','statistikStatusCode':'OK'};

    beforeEach(angular.mock.module('common'), function($provide) {
    });
    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.inject(['$rootScope', '$compile', '$httpBackend', '$q',
        'common.srsService', 'common.srsViewState', 'common.srsProxy', 'common.authorityService',
        function(_$rootScope_, _$compile_, _$httpBackend_, _$q_, _srsService_, _srsViewState_, _srsProxy_, _authorityService_) {
            $httpBackend = _$httpBackend_;
            srsService = _srsService_;
            srsViewState = _srsViewState_;
            srsProxy = _srsProxy_;
            authorityService = _authorityService_;
            $compile = _$compile_;
            $q = _$q_;
            $rootScope = _$rootScope_;

            srsService.updateHsaId('fake enhetid');
            srsService.updateIntygsTyp('fk7263');
            srsService.updatePersonnummer('fake personid');

            spyOn(authorityService, 'isAuthorityActive').and.returnValue(true);

            spyOn(srsProxy, 'getDiagnosisCodes').and.callFake(function() {
                var promise = $q.defer();
                promise.resolve(diagnosisTestJson);
                return promise.promise;
            });

            spyOn(srsProxy, 'getQuestions').and.callFake(function() {
                var promise = $q.defer();
                promise.resolve(questionsTestJson);
                return promise.promise;
            });

            spyOn(srsProxy, 'getPrediction').and.callFake(function() {
                var promise = $q.defer();
                promise.resolve(srsTestJson);
                return promise.promise;
            });

            spyOn(srsProxy, 'getAtgarderAndStatistikAndHistoricPredictionForDiagnosis').and.callFake(function() {
                var promise = $q.defer();
                promise.resolve(srsTestJson);
                return promise.promise;
            });

            $scope = $rootScope.$new();
        }
    ]));
    beforeEach(function(){
        $httpBackend.expectPOST('/api/jslog/srs').respond(200);
        $httpBackend.expectPOST('/api/jslog/srs').respond(200);
    });

    describe('User consent === \'INGET\'', function() {
        beforeEach(function(){
            spyOn(srsProxy, 'getConsent').and.callFake(function() {
                var promise = $q.defer();
                promise.resolve({data: 'INGET', status: 200});
                return promise.promise;
            });

            element = $compile('<wc-srs-help-display id=\'2\'>')($scope);
            $scope.$apply();
            $rootScope.$broadcast('intyg.loaded');
            $scope.$apply();

        });

        it('Should set consent in viewState to \'INGET\'', function(){
            expect(srsViewState.consent).toBe('INGET');
            expect(srsProxy.getConsent).toHaveBeenCalledTimes(1);
        });

        it('Should not call _any_ rest endpoint except getConsent', function() {
            var changedButValidDiagnosKod = srsViewState.diagnosKod === diagnosisTestJson[0] ? diagnosisTestJson[1] : diagnosisTestJson[0];
            srsService.updateDiagnosKod(changedButValidDiagnosKod);
            $scope.$apply();

            expect(srsProxy.getQuestions).not.toHaveBeenCalled();
            expect(srsProxy.getPrediction).not.toHaveBeenCalled();
            expect(srsProxy.getAtgarderAndStatistikAndHistoricPredictionForDiagnosis).not.toHaveBeenCalled();
        });

    });

    describe('User consent === \'JA\'', function(){
        beforeEach(function(){
            spyOn(srsProxy, 'getConsent').and.callFake(function() {
                var promise = $q.defer();
                promise.resolve({data : 'JA', status: 200});
                return promise.promise;
            });

            element = $compile('<wc-srs-help-display id=\'2\'>')($scope);
            $scope.$apply();
            $rootScope.$broadcast('intyg.loaded');
            $scope.$apply();
        });

        it('Should set consent in viewState to \'JA\'', function(){
            expect(srsViewState.consent).toBe('JA');
            expect(srsProxy.getConsent).toHaveBeenCalledTimes(1);
        });

        it('Should not display SRS if not available for diagnoskod', function() {
            srsService.updateDiagnosKod('A10');
            srsService.updateDiagnosBeskrivning('Bla bla');

            $scope.$apply();

            expect(srsViewState.diagnosKod).toBe('A10');
            expect(srsViewState.srsApplicable).toBeFalsy();
            //expect($(element).find('#srs_diagnos_heading_').text()).toBe('A10 - Bla bla');

            expect(srsProxy.getQuestions).not.toHaveBeenCalled();
            expect(srsProxy.getPrediction).not.toHaveBeenCalled();
            expect(srsProxy.getAtgarderAndStatistikAndHistoricPredictionForDiagnosis).not.toHaveBeenCalled();
        });

        it('Should display SRS if available for diagnoskod', function() {
            srsService.updateDiagnosKod('J20');
            srsService.updateDiagnosBeskrivning('Bla bla');

            $scope.$apply();

            expect(srsViewState.diagnosKod).toBe('J20');
            expect(srsViewState.srsApplicable).toBeTruthy();
            //expect($(element).find('#srs_diagnos_heading_').text()).toBe('J20 - Bla bla');

            expect(srsProxy.getQuestions).toHaveBeenCalled();
            expect(srsProxy.getAtgarderAndStatistikAndHistoricPredictionForDiagnosis).toHaveBeenCalled();
        });

        it('Should close SRS if diagnos is cleared', function() {
            srsService.updateDiagnosKod('J201');
            srsService.updateDiagnosBeskrivning('Bla bla');

            $scope.$apply();

            expect(srsViewState.diagnosKod).toBe('J201');
            expect(srsViewState.srsApplicable).toBeTruthy();
            //expect($(element).find('#srs_diagnos_heading_').text()).toBe('J201 - Bla bla');

            srsService.updateDiagnosKod('');
            srsService.updateDiagnosBeskrivning('');

            $scope.$apply();

            expect(srsViewState.diagnosKod).toBe('');
            expect(srsViewState.srsApplicable).toBeFalsy();
        });
    });

});
