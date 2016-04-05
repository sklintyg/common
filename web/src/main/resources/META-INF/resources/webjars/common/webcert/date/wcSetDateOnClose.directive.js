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
angular.module('common')
/**
 * Patches the uib-datepicker to always set the date on close.
 * Angular UI doesn't provide any good way to extend it's functionality,
 * therefor this code needs to be somewhat hacky in order to achieve our goal.
 */
.directive('wcSetDateOnClose', function () {
    'use strict';
    return {
        restrict: 'A',
        link: function (scope, elem) {
            var isoScope = elem.isolateScope(),
                watch,
                origCloseFn;
            /**
             * Initial watch
             *
             * Tries to patch uib-bootstrap close method if scope is found
             */
            watch = scope.$watch(
                function () {
                    // Watch some properties which is related to uib-datepicker
                    return !!(
                        isoScope.dateDisabled &&
                        isoScope.dateSelection &&
                        isoScope.close
                    );
                },
                function (newVal, oldVal) {
                    if (newVal) {
                        // Patch the close callback once and then destroy the watch
                        patchClose();
                        watch();
                    }
                }
            );

            /**
             * Tries to get the active date from the uib-datepicker scope
             */
            function getActiveDate (parentScope) {
                var datePickerScope,
                    dateScope;
                // Locate the isolate scope we want by traversing the DOM
                datePickerScope = angular
                    .element('[uib-datepicker-popup-wrap] [uib-datepicker]')
                    .isolateScope();

                // uib-datepicker keeps the active date id internally.
                if (datePickerScope && datePickerScope.activeDateId) {
                    // Use the active date id to get the scope and model from
                    // the DOM. Gold is achieved.
                    dateScope = angular
                        .element('#' + datePickerScope.activeDateId)
                        .scope();
                    return dateScope.dt.date;
                }
            }

            /**
             * Patched uib-datepicker close callback
             */
            function closePatched (evt) {
                /* jshint validthis: true */
                var activeDate = getActiveDate(isoScope);
                if (angular.isDate(activeDate)) {
                    // `.select` is the same method called internally by uib-datepicker
                    isoScope.select(activeDate, evt);
                }
                // Call original close callback
                origCloseFn.call(this, evt);
            }

            /**
             * Does some patching to the uib-datepicker close callback
             */
            function patchClose () {
                // Perform sanity check
                if (!angular.isFunction(isoScope.close)) { return; }
                // Keep old close callback
                origCloseFn = isoScope.close;
                // Patch with our own callback
                isoScope.close = closePatched;
            }

            // Destroy watch on scope destruction to avoid memory leaks.
            scope.$on('$destroy', watch);
        }
    };
});
