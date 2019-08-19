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
angular.module('common').factory('common.dynamicLinkService',
    function() {
        'use strict';
        
        var _links = {};

        function _getLink(key) {
            return _links[key];
        }

        function _addLinks(links) {
            _links = links;
        }

        function _processLinkTags(input){
            var regex2 = /<LINK:(.*?)>/gi, result;

            while ( (result = regex2.exec(input)) ) {
                var replace = result[0];
                var linkKey = result[1];

                //var dynamicLink = _buildExternalLink(linkKey);

                var dynamicLink = '<span dynamiclink key="' + linkKey + '"></span>';

                var regexp = new RegExp(replace, 'g');
                input = input.replace(regexp, dynamicLink);
            }
            return input;
        }
/*
        function _buildExternalLink(linkKey) {
            var dynamicLink = '';
            dynamicLink += _links[linkKey].target ? '<span class="unbreakable">' : '';
            dynamicLink += '<a href="' + _links[linkKey].url + '" class="external-link" rel="noopener noreferrer" ';
            dynamicLink += _links[linkKey].tooltip ? ' title="' + _links[linkKey].tooltip + '"' : '';
            dynamicLink += _links[linkKey].target ? ' target="' + _links[linkKey].target + '">' : '>';
            dynamicLink += _links[linkKey].text + '</a>';
            dynamicLink += _links[linkKey].target ? ' <i ng-show="target" class="external-link-icon material-icons">launch</i></span>' : '';
            return dynamicLink;
        }
*/
        return {
            getLink: _getLink,
            addLinks: _addLinks,
            //buildExternalLink: _buildExternalLink,
            processLinkTags: _processLinkTags
        };
    }
);
