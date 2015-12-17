/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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
 * Display FMB help texts
 */
angular.module('common').directive('wcFmbHelpDisplay',
        function() {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    helpTextContents: '=',
                    diagnosisDescription: '=',
                    diagnosisCode: '=',
                    relatedFormId: '@'
                },
                link: function(scope, element, attrs) {

                    //Some status we need to have on the accordion
                    scope.status = {
                        open: true
                    };
                },
                templateUrl: '/web/webjars/common/webcert/fmb/wcFmbHelpDisplay.directive.html'
            };
        });
