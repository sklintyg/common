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

angular.module('common').directive('wcShow',
    function($window, $animate, $log) {
        'use strict';

        return {
            restrict: 'A',
            multiElement: true,
            link: function(scope, element, attr) {
                scope.$watch(attr.wcShow, function ngShowWatchAction(value){

                    $animate[value ? 'removeClass' : 'addClass'](element, 'ng-hide');

                    element.on('$animate:before', function() {
                        $window.animations++;
                        $window.rendered = false;
                    });
                    element.on('$animate:close', function() {
                        $window.animations--;
                        if ($window.animations === 0) {
                            $window.rendered = true;
                        }
                    });
                });
            }
        };
    });
