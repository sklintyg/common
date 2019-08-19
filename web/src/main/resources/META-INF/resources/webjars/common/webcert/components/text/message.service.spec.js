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

describe('messageService', function() {
  'use strict';

  var messageService;
  var $rootScope;

  beforeEach(angular.mock.module('common'));

  beforeEach(angular.mock.inject(['common.messageService', '$rootScope',
    function(_messageService_, _$rootScope_) {

      messageService = _messageService_;
      $rootScope = _$rootScope_;
      messageService.addResources({
        en: {correct: 'Correct', hello: 'Hello ${prefix} ${user}!', empty: ''},
        sv: {correct: 'Rätt', hello: 'Hej ${prefix} ${user}!', empty: ''}
      });
    }
  ]));

  describe('#getProperty', function() {

    it('should return the correct value if a language is specified', function() {
      var enMessage = messageService.getProperty('correct', null, null, 'en');
      var svMessage = messageService.getProperty('correct', null, null, 'sv');

      expect(enMessage).toBe('Correct');
      expect(svMessage).toBe('Rätt');
    });

    it('should return an empty string if the message is an empty string', function() {
      var message = messageService.getProperty('empty', null, null, 'sv');

      expect(message).toBe('');
    });

    it('should return the correct value if a language is set in the root scope', function() {
      $rootScope.lang = 'sv';

      var message = messageService.getProperty('correct');

      expect(message).toBe('Rätt');
    });

    it('should return the correct value if a default language is set', function() {
      $rootScope.DEFAULT_LANG = 'sv';

      var message = messageService.getProperty('correct', null, null, null, true);

      expect(message).toBe('Rätt');
    });

    it('should return the default value if the key is not present in the resources', function() {
      var message = messageService.getProperty('emptystring', null, '', 'sv');

      expect(message).toBe('');
    });

    it('should return the missing key if the key is not present in the resources', function() {
      var message = messageService.getProperty('emptystring', null, null, 'sv');

      expect(message).toBe('[Missing "emptystring"]');
    });

    it('should return missing language if no language is set', function() {
      var message = messageService.getProperty('emptystring');

      expect(message).toBe('[Missing language]');
    });

    it('should return string with expanded string if variable available', function() {
      var message = messageService.getProperty('hello', {prefix: 'Stora', user: 'Världen'}, null, 'sv');

      expect(message).toBe('Hej Stora Världen!');
    });
  });
});
