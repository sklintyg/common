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

describe('diagnos', function() {
    'use strict';

    var $scope;
    var $httpBackend;
    var element;

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.module('common'));

    beforeEach(inject(function($compile, $rootScope, _$httpBackend_) {
        $httpBackend = _$httpBackend_;

        $scope = $rootScope.$new();
        $scope.model = {
            diagnoser:[{}]
        };
        $scope.options = {
            formState:{viewState:{common:{validation:{}}}}
        };
        $scope.formFields = [{
            key: 'diagnoser',
            type: 'diagnos',
            templateOptions: { diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2' }
        }];
        element =
            angular.element('<form><formly-form model="model" fields="formFields" options="options"></formly-form></form>');
        $compile(element)($scope);
        $scope.$digest();
        $scope = element.find('#form_diagnoser').scope();
    }));

    it('should mark psykisk code with less than 4 characters as shortPsykiskDiagnos', function() {
        $httpBackend.expectPOST('/moduleapi/diagnos/kod/sok').respond({
            'resultat':'OK',
            'diagnoser':[
                {'kod':'F23','beskrivning':'Akuta och övergående psykotiska syndrom'},
                {'kod':'F230','beskrivning':'Akut polymorf psykos utan egentlig schizofren sjukdomsbild'},
                {'kod':'F231','beskrivning':'Akut polymorf psykos med schizofren sjukdomsbild'},
                {'kod':'F231','beskrivning':'Akut polymorf psykos med schizofren sjukdomsbild'},
                {'kod':'Z73','beskrivning':'Problem som har samband med svårigheter att kontrollera livssituationen'},
                {'kod':'Z730','beskrivning':'Utbrändhet'},
                {'kod':'G90','beskrivning':'Sjukdomar i autonoma nervsystemet'},
                {'kod':'F90','beskrivning': 'Hyperaktivitetsstörningar'}
            ],
            'moreResults':false
        });

        var diagnoses = null;
        $scope.getDiagnoseCodes('', 'F23').then(function(result) {
            diagnoses = result;
        });
        $httpBackend.flush();
        expect(diagnoses[0].shortPsykiskDiagnos).toBeTruthy();
        expect(diagnoses[1].shortPsykiskDiagnos).toBeFalsy();
        expect(diagnoses[2].shortPsykiskDiagnos).toBeFalsy();
        expect(diagnoses[3].shortPsykiskDiagnos).toBeFalsy();
        expect(diagnoses[4].shortPsykiskDiagnos).toBeTruthy();
        expect(diagnoses[5].shortPsykiskDiagnos).toBeFalsy();
        expect(diagnoses[6].shortPsykiskDiagnos).toBeFalsy();
        expect(diagnoses[7].shortPsykiskDiagnos).toBeTruthy();
    });

});

