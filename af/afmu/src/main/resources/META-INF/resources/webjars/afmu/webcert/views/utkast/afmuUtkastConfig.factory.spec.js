/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
describe('afmuFormFactory', function() {
    'use strict';

    var element;
    var $scope;

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.anchorScrollService', {scrollTo: function() {}});
    }));
    beforeEach(angular.mock.module('afmu'));
    beforeEach(inject(['$compile', '$rootScope', 'afmu.UtkastConfigFactory', 'afmu.Domain.IntygModel',
        function($compile, $rootScope, _afmuUtkastConfigFactory_, _afmuIntygModel_) {

        $scope = $rootScope.$new();
        $scope.model = _afmuIntygModel_._members.build().content;
        $scope.ueConfig = _afmuUtkastConfigFactory_.getConfig();
        element = angular.element(
            '<form name="certForm"><ue-render-components form="::certForm" config="::ueConfig" model="::model"></ue-render-components></form>');
        $compile(element)($scope);
        $scope.$digest();
    }]));

    // var grundData = {
    //     'skapadAv':{'personId':'TSTNMT2321000156-1079','fullstandigtNamn':'Arnold Johansson','vardenhet':{'enhetsid':'TSTNMT2321000156-1077',
    //         'enhetsnamn':'NMT vg3 ve1','postadress':'NMT gata 3','postnummer':'12345','postort':'Testhult','telefonnummer':'0101112131416',
    //         'epost':'enhet3@webcert.invalid.se','vardgivare':{'vardgivarid':'TSTNMT2321000156-102Q','vardgivarnamn':'NMT vg3'},'arbetsplatsKod':'1234567890'}},
    //     'patient':{'personId':'19121212-1212','fullstandigtNamn':'Tolvan Tolvansson','fornamn':'Tolvan','efternamn':'Tolvansson',
    //         'postadress':'Svensson, Storgatan 1, PL 1234','postnummer':'12345','postort':'Småmåla','samordningsNummer':false},'relation':{}};

    // var utkastData = {
    //     'grundData':grundData,
    //     'funktionsnedsattning':'Inga fynd gjordes','aktivitetsbegransning':'Har svårt att sitta och ligga.. Och stå. Får huka sig.',
    //     'pagaendeBehandling':'Meditering, självmedicinering','planeradBehandling':'Inga planerade åtgärder. Patienten har ingen almanacka.',
    //     'ovrigt':'Inga övriga upplysningar.'};

});
