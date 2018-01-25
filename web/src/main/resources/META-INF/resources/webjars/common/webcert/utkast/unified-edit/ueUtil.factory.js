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

angular.module('common').factory('ueUtil', ['$timeout', 'common.UtkastValidationService',
    function($timeout, UtkastValidationService) {
        'use strict';

        return {
            updateValidation: function(form, model) {
                form.$commitViewValue();
                // $timeout is needed to allow for the attic functionality to clear the model value for hidden fields
                $timeout(function() {
                    UtkastValidationService.validate(model);
                });
            },

            setupWatchers: function _setupWatchers(scope, config) {
                if (config.watcher) {
                    config.watcher.forEach(function(watcher) {
                        scope.$watch(watcher.expression, watcher.listener, watcher.watchDeep);
                    });
                }
            }
        };
    } ]);
