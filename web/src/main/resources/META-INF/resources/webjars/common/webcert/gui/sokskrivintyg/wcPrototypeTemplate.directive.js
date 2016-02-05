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

/*
 templateUrl: function(elem, attr){
 return 'customer-'+attr.type+'.html';
 } */
angular.module('common').directive('wcPrototypeTemplate',
    ['$http', '$templateCache', '$compile',
        function($http, $templateCache, $compile) {
            'use strict';

            var templateLoader;

            return {
                restrict: 'E',
                replace: true,
                link: {

                }
            };
        }]);

/*
 *
 * // activity items can be of several types .. post-photo, status-change, etc. (think facebook activity stream)
 App.directive('activityItem', ['$compile', '$http', '$templateCache', function($compile, $http, $templateCache) {
 var templateText, templateLoader,
 baseURL = 'partial/tpl/',
 typeTemplateMapping = {
 photo:    'photo-activity-item.html',
 status: 'status-activity-item.html'
 };

 return {
 restrict: 'E',
 replace: true,
 transclude: true,
 scope: {
 type: '@type', title: '=', authorName: '=', avatar: '=', timeAgo: '=',
 statsLikes: '=', statsViews: '=' // some basic stats for your post
 },
 compile: function(tElement, tAttrs) {
 var tplURL = baseURL + typeTemplateMapping[tAttrs.type];
 templateLoader = $http.get(tplURL, {cache: $templateCache})
 .success(function(html) {
 tElement.html(html);
 });

 return function (scope, element, attrs) {
 templateLoader.then(function (templateText) {
 element.html($compile(tElement.html())(scope));
 });
 };
 }
 };
 }])
 */

