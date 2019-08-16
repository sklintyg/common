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
 * wc-maxlength directive which limits amount of characters that can be entered in a input box/textarea and adds a
 * counter below the element
 *
 * usage:
 *  directive demands the following attributes on the element:
 *  wc-maxlength
 *  name (for unique id for counter scope name)
 *  ng-model (for mapping model)
 *  maxlength (for setting maxlength)
 */
angular.module('common').directive('wcMaxlength',
    function($compile) {
      'use strict';

      return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attrs, controller) {

          scope.$evalAsync(function() {
            var counterName = 'charsRemaining' + attrs.id;
            counterName = counterName.replace(/\./g, '');
            counterName = counterName.replace(/-/g, '');
            scope[counterName] = attrs.maxlength;

            var counter = angular.element('<span class="counter">Tecken kvar: {{' + counterName + '}}</span>');
            $compile(counter)(scope);
            element.after(counter);

            function limitLength(text) {
              if (text === undefined) {
                return;
              }
              if (text.length > attrs.maxlength) {
                var transformedInput = text.substring(0, attrs.maxlength);
                controller.$setViewValue(transformedInput);
                controller.$render();
                return transformedInput;
              }
              scope[counterName] = attrs.maxlength - text.length;
              return text;
            }

            controller.$formatters.unshift(limitLength);
            controller.$parsers.unshift(limitLength);
          });
        }
      };
    });
