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

describe('UtkastValidationService', function() {
  'use strict';

  var utkastValidationService;
  var commonViewState;
  var viewState;
  var $httpBackend;
  var $rootScope;

  var utkastContent;

  beforeEach(angular.mock.module('common', function($provide) {
  }));

  beforeEach(angular.mock.inject(['common.UtkastValidationService', 'common.UtkastViewStateService', '$httpBackend', '$rootScope',
    function(_utkastValidationService_, _commonViewState_, _$httpBackend_, _$rootScope_) {
      utkastValidationService = _utkastValidationService_;
      commonViewState = _commonViewState_;
      $httpBackend = _$httpBackend_;
      $rootScope = _$rootScope_;

      commonViewState.reset();

      utkastContent = {
        grundData: {
          skapadAv: {
            vardenhet: {
              postadress: null,
              postnummer: null,
              postort: null,
              telefonnummer: null
            }
          },
          patient: {
            fornamn: 'Tolvan',
            efternamn: 'Tolvansson',
            personId: '19121212-1212'
          }
        },
        id: 'testIntygId',
        typ: 'testIntyg',
        textVersion: '1.0'
      };

      // viewState.intygModel.grundData.patient.personId
      viewState = {
        common: commonViewState,
        draftModel: {
          isSigned: function() {
          },
          isDraftComplete: function() {
          },
          update: function(data) {
            viewState.draftModel.content = data.content;
          },
          content: utkastContent,
          version: 1
        }
      };
      viewState.intygModel = viewState.draftModel.content;
      viewState.intygModel.toSendModel = function() {
        return viewState.intygModel;
      };
      commonViewState.intyg.type = 'testIntyg';
    }
  ]));

  it('successful utkast validate and draft incomplete', function() {
    var validateResponse = {
      'status': 'DRAFT_INCOMPLETE',
      'messages': [{'field': 'errorField', 'type': 'EMPTY', 'message': 'error.message'}]
    };

    $httpBackend.expectPOST('/moduleapi/utkast/testIntyg/testIntygId/validate').respond(200, validateResponse);

    utkastValidationService.validate(utkastContent);

    $rootScope.$apply();
    $httpBackend.flush();

    expect(viewState.common.validation.messagesGrouped).toEqual({});
    expect(viewState.common.validation.messages).toEqual([]);
    expect(viewState.common.validation.sections).toEqual([]);
  });

  it('successful utkast save and draft incorrect format', function() {
    var validateResponse = {
      'status': 'DRAFT_INCOMPLETE',
      'messages': [{'category': 'categoryId', 'field': 'errorField', 'type': 'INCORRECT_FORMAT', 'message': 'error.message'},
        {'category': 'categoryId2', 'field': 'errorField2', 'type': 'EMPTY', 'message': 'error.message2'}]
    };

    $httpBackend.expectPOST('/moduleapi/utkast/testIntyg/testIntygId/validate').respond(200, validateResponse);

    utkastValidationService.validate(utkastContent);

    $rootScope.$apply();
    $httpBackend.flush();

    expect(viewState.common.validation.messagesGrouped).toEqual(
        {categoryid: [{'category': 'categoryId', 'field': 'errorField', 'type': 'INCORRECT_FORMAT', 'message': 'error.message'}]});
    expect(viewState.common.validation.messages).toEqual([
      {'category': 'categoryId', 'field': 'errorField', 'type': 'INCORRECT_FORMAT', 'message': 'error.message'}]);
    expect(viewState.common.validation.sections).toEqual(['categoryid']);

    expect(viewState.common.validation.warningMessages).toEqual([]);
  });

  it('successful utkast save and draft incorrect format, showComplete is true', function() {
    viewState.common.showComplete = true;
    var validateResponse = {
      'status': 'DRAFT_INCOMPLETE',
      'messages': [{'category': 'categoryId', 'field': 'errorField', 'type': 'INCORRECT_FORMAT', 'message': 'error.message'},
        {'category': 'categoryId2', 'field': 'errorField2', 'type': 'EMPTY', 'message': 'error.message2'}]
    };

    $httpBackend.expectPOST('/moduleapi/utkast/testIntyg/testIntygId/validate').respond(200, validateResponse);

    utkastValidationService.validate(utkastContent);

    $rootScope.$apply();
    $httpBackend.flush();

    expect(viewState.common.validation.messagesGrouped).toEqual({
      categoryid: [{'category': 'categoryId', 'field': 'errorField', 'type': 'INCORRECT_FORMAT', 'message': 'error.message'}],
      categoryid2: [{'category': 'categoryId2', 'field': 'errorField2', 'type': 'EMPTY', 'message': 'error.message2'}]
    });
    expect(viewState.common.validation.messages).toEqual([
      {'category': 'categoryId', 'field': 'errorField', 'type': 'INCORRECT_FORMAT', 'message': 'error.message'},
      {'category': 'categoryId2', 'field': 'errorField2', 'type': 'EMPTY', 'message': 'error.message2'}]);
    expect(viewState.common.validation.sections).toEqual(['categoryid', 'categoryid2']);
  });

  it('successful utkast save and draft complete', function() {
    var validateResponse = {
      'status': 'DRAFT_COMPLETE',
      'messages': []
    };

    $httpBackend.expectPOST('/moduleapi/utkast/testIntyg/testIntygId/validate').respond(200, validateResponse);

    utkastValidationService.validate(utkastContent);

    $rootScope.$apply();
    $httpBackend.flush();

    expect(viewState.common.validation.messagesGrouped).toEqual({});
    expect(viewState.common.validation.messages).toEqual([]);
    expect(viewState.common.validation.sections).toEqual([]);
  });

  it('successful utkast save and draft complete, with warnings', function() {
    var validateResponse = {
      'status': 'DRAFT_COMPLETE',
      'messages': [],
      'warnings': [{'field': 'warning.field', 'type': 'WARN', 'message': 'warn.message'}]
    };

    $httpBackend.expectPOST('/moduleapi/utkast/testIntyg/testIntygId/validate').respond(200, validateResponse);

    utkastValidationService.validate(utkastContent);

    $rootScope.$apply();
    $httpBackend.flush();

    expect(viewState.common.validation.messagesGrouped).toEqual({});
    expect(viewState.common.validation.messages).toEqual([]);
    expect(viewState.common.validation.sections).toEqual([]);

    expect(viewState.common.validation.warningMessages).toEqual([{'field': 'warning.field', 'type': 'WARN', 'message': 'warn.message'}]);
    expect(viewState.common.validation.warningMessagesByField['warning.field']).toBeDefined();
  });
});
