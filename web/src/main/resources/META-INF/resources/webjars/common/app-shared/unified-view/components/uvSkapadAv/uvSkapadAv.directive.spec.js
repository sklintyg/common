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

describe('uvSkapadAv Directive', function() {
  'use strict';

  var $scope;
  var element;

  beforeEach(angular.mock.module('htmlTemplates'));
  beforeEach(angular.mock.module('common'));

  beforeEach(angular.mock.inject(['$compile', '$rootScope', function($compile, $rootScope) {
    $scope = $rootScope.$new();

    $scope.viewDataMock = {
      uppgifter: {
        skapadAv: {
          personId: 'personId',
          fullstandigtNamn: 'fullstandigtNamn',
          vardenhet: {
            enhetsnamn: 'enhetsnamn',
            postadress: 'postadress',
            postnummer: 'postnummer',
            postort: 'postort',
            telefonnummer: 'telefonnummer',
            vardgivare: {
              vardgivarnamn: 'vardgivarnamn'
            }

          }
        }
      }
    };

    $scope.configMock = {
      modelProp: 'uppgifter.skapadAv'
    };

    element = $compile('<uv-skapad-av config="configMock" view-data="viewDataMock"></uv-skapad-av>')($scope);

  }]));

  it('should extract correct value from viewData when value exists', function() {
    $scope.$digest();
    expect(element.isolateScope().vm).toEqual($scope.viewDataMock.uppgifter.skapadAv);
  });

  it('should display correct info', function() {
    $scope.$digest();

    expect($(element).find('#fullstandigtNamn').text()).toContain($scope.viewDataMock.uppgifter.skapadAv.fullstandigtNamn);
    expect($(element).find('#vardenhet-telefon').text()).toContain($scope.viewDataMock.uppgifter.skapadAv.vardenhet.telefonnummer);
    expect($(element).find('#vardenhet-namn').text()).toContain($scope.viewDataMock.uppgifter.skapadAv.vardenhet.enhetsnamn);
    expect($(element).find('#vardenhet-namn').text()).toContain($scope.viewDataMock.uppgifter.skapadAv.vardenhet.vardgivare.vardgivarnamn);
    expect($(element).find('#vardenhet-adress').text()).toContain($scope.viewDataMock.uppgifter.skapadAv.vardenhet.postadress);
    expect($(element).find('#vardenhet-adress').text()).toContain($scope.viewDataMock.uppgifter.skapadAv.vardenhet.postnummer);
    expect($(element).find('#vardenhet-adress').text()).toContain($scope.viewDataMock.uppgifter.skapadAv.vardenhet.postort);
  });

});
