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

/* globals readJSON */
/* globals tv4 */
describe('IntygSendService', function() {
  'use strict';

  var IntygSend;
  var $httpBackend;
  var $state;
  var $timeout;
  var dialogService;
  var UserModel;

  beforeEach(angular.mock.module('common', function($provide) {
    $provide.value('common.messageService',
        jasmine.createSpyObj('common.messageService', ['getProperty', 'addResources']));
    $provide.value('$stateParams', {});
    $provide.value('common.statService', jasmine.createSpyObj('common.statService', ['refreshStat']));
    $provide.value('common.UtkastViewStateService', {});
    $provide.value('common.utkastNotifyService', {});
    $provide.value('common.domain.DraftModel', {});

    $provide.value('common.User', jasmine.createSpyObj('common.User', ['storeAnvandarPreference']));
  }));

  beforeEach(angular.mock.inject(['common.IntygSend', '$httpBackend', '$state', '$timeout',
    'common.dialogService', 'common.UserModel',
    function(_IntygSend_, _$httpBackend_, _$state_, _$timeout_, _dialogService_, _UserModel_) {
      IntygSend = _IntygSend_;
      $httpBackend = _$httpBackend_;
      $state = _$state_;
      $timeout = _$timeout_;
      dialogService = _dialogService_;
      UserModel = _UserModel_;

      UserModel.setUser({
        anvandarPreference: {}
      });
    }]));

  describe('send', function() {

    it('should request intyg send with valid json request', function() {

      var data = function(data) {
        data = JSON.parse(data);
        var schema = readJSON('test/resources/jsonschema/webcert-send-intyg-request-schema.json');
        return tv4.validate(data, schema);
      };
      data.toString = function() {
        return tv4.error.toString();
      };

      $httpBackend.expectPOST('/moduleapi/intyg/intygsTyp/intygsId/skicka', data).respond(200);

      spyOn(dialogService, 'showDialog').and.callFake(function(options) {
        $timeout(function() {
          options.button1click();
        });
        return {
          opened: {
            then: function() {
            }
          },
          close: function() {
          }
        };
      });

      IntygSend.send({}, 'intygsId', 'intygsTyp', 'recipientId', {}, function() {
      });

      $timeout.flush();

      $httpBackend.flush();
    });
  });

});
