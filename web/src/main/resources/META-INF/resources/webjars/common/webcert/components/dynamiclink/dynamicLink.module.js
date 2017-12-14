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
 * Links can be dynamically populated from each application's links.json file served at application startup.
 *
 * Given this dynamic link JSON:
 * {
 *   "key": "someLinkKey",
 *   "url": "http://some.url",
 *   "text": "Some text",
 *   "tooltip": "Some tooltip",
 *   "target": "_blank"
 * }
 *
 * Usage: <span dynamiclink key="someLinkKey"/>
 *
 * Produces: <a href="http://some.url" target="_blank" title="Some tooltip">Some text</a>
 */
angular.module('common.dynamiclink', []);

angular.module('common.dynamiclink').factory('dynamicLinkService',
    function() {
        'use strict';

        var _links = {};

        function _getLink(key) {
            return _links[key];
        }

        function _addLinks(links) {
            _links = links;
        }

        return {
            getLink: _getLink,
            addLinks: _addLinks
        };
    }
);

angular.module('common.dynamiclink').directive('dynamiclink',
    ['$log', '$rootScope', '$sce', '$compile', 'dynamicLinkService',
    function($log, $rootScope, $sce, $compile, dynamicLinkService) {
        'use strict';

        return {
            restrict: 'EA',
            scope: {
                'key': '@',
                'linkclass': '@'
            },
            template: '<a href="{{ url }}" class="external-link {{linkclass}}" ng-attr-target="{{ target || undefined}}" ' +
            'ng-attr-title="{{ tooltip || undefined }}" ng-bind-html="text"></a> <i ng-show="target" class="material-icons">launch</i>',
            link: function(scope) { //  element, attr
                scope.$watch(function() {
                    return dynamicLinkService.getLink(scope.key);
                }, function(value) {

                    if (angular.isDefined(value)) {
                        scope.url = value.url;
                        scope.text = $sce.trustAsHtml(value.text);
                        scope.tooltip = value.tooltip;
                        scope.target = value.target;
                    } else {
                        scope.url = '#';
                        scope.text = $sce.trustAsHtml('WARNING: could not resolve dynamic link: ' + scope.key);
                        scope.tooltip = null;
                        scope.target = null;
                    }
                });
            }
        };
    }]);

