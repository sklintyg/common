/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
/**
 * message directive for externalizing text resources.
 *
 * All resourcekeys are expected to be defined in lowercase and available in a
 * global js object named "messages"
 * Also supports dynamic key values such as key="status.{{scopedvalue}}"
 *
 * Usage: <message key="some.resource.key" [fallback="defaulttextifnokeyfound"]/>
 */
angular.module('common').factory('common.messageService', ['$rootScope', 'common.dynamicLinkService',
  function($rootScope, dynamicLinkService) {
    'use strict';

    var _messageResources = null;

    function _propertyExists(key) {
      var value;
      var language = $rootScope.lang;
      if (language) {
        _checkResources();
        value = _messageResources[language][key];
        if (value === null || value === undefined) {
          return false;
        }
      } else {
        value = false;
      }

      return value;
    }

    function _getProperty(key, variables, defaultValue, language, fallbackToDefaultLanguage) {
      var value;

      if (!language) {
        language = $rootScope.lang;
        if (!language && fallbackToDefaultLanguage) {
          language = $rootScope.DEFAULT_LANG;
        }
      }

      if (language) {
        value = _getPropertyInLanguage(language, key, variables);
        if (value === null || value === undefined) {
          value = defaultValue === null || defaultValue === undefined ?
              '[Missing "' + key + '"]' : defaultValue;
        }
      } else {
        value = '[Missing language]';
      }

      return value;
    }

    function _getPropertyInLanguage(lang, key, variables) {
      _checkResources();
      var message = _messageResources[lang][key];

      if (message) {
        angular.forEach(variables, function(value, key) {
          var regexp = new RegExp('\\$\\{' + key + '\\}', 'g');
          message = message.replace(regexp, value);
        });

        message = dynamicLinkService.processLinkTags(message);
      }

      return message;
    }

    function _addResources(resources) {
      _checkResources();
      angular.extend(_messageResources.sv, resources.sv);
      angular.extend(_messageResources.en, resources.en);
    }

    function _checkResources() {
      if (_messageResources === null) {
        _messageResources = {
          'sv': {
            'initial.key': 'Initial nyckel'
          },
          'en': {
            'initial.key': 'Initial key'
          }
        };
      }
    }

    return {
      propertyExists: _propertyExists,
      getProperty: _getProperty,
      addResources: _addResources
    };
  }]
);
