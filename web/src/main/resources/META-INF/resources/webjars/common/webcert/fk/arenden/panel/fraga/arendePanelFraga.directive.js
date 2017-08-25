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

/**
 * Created by BESA on 2015-05-09.
 */

/**
 * arendePanelFraga directive. Handles fraga part of a arendePanel.
 */
angular.module('common').directive('arendePanelFraga',
    [ 'common.anchorScrollService',
        function(anchorScroll) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                templateUrl: '/web/webjars/common/webcert/fk/arenden/panel/fraga/arendePanelFraga.directive.html',
                scope: {
                    panelId: '@',
                    arendeListItem: '='
                },
                link:function(scope) {
                    scope.scrollToFraga = function(komplettering) {
                        //Both uv-wc-fraga (intyg) and wc-fraga-formly wrapper (utkast) sets this anchor id format
                        var target = 'komplettering_' + komplettering.id;

                        var offset = 10;
                        var topMenuElements = angular.element.find('.header-fix-top');
                        if (topMenuElements.length > 0) {
                            var headerHeight = angular.element(topMenuElements[0]).prop('offsetHeight');
                            offset += ($(window).height() - headerHeight) / 2 + headerHeight;
                        }
                        else {
                            offset += $(window).height() / 2;
                        }

                        // Flooring because previous calcs can generate half pixels which will screw calcs in
                        // anchorscroll and make scrolling get stuck in IE when
                        // window height is even
                        offset = Math.floor(offset);

                        anchorScroll.scrollTo(target, offset);
                    };
                }
            };
        }]);
