
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
 * Display SRS questionaire
 */
angular.module('common').directive('wcSrsResult', ['$window', 'common.ObjectHelper', 'common.srsProxy', 'common.srsLinkCreator',
    function($window, ObjectHelper, srsProxy, srsLinkCreator) {
        'use strict';

        return {
            restrict: 'E',
            link: function (scope, element, attrs) {
                scope.clampSet = false;
                scope.srsViewState.status.riskInfoOpen = false;
                scope.closeSrs = function(){
                    scope.status.riskInfoOpen = false;
                };

                scope.externalRisk = {
                    templateUrl: '/web/webjars/common/webcert/srs/wcSrsResult.risk-popover.html'
                };

                scope.parentHasRendered = function(){
                    if(scope.status.open){
                        if(scope.clampSet){
                            return true;
                        }
                        var divsize = angular.element(document.getElementById('atgarder' + scope.id)).prop('offsetWidth');
                        if(divsize > 0){
                            scope.clampSet = true;
                            return true;
                        }
                    }
                    return false;
                };

                scope.readMoreRisk = function(){
                    $window.open(srsLinkCreator.createPrediktionsModellLink, '_blank');
                };

                scope.redirectToAtgardExternalSite = function(){
                    window.open(srsLinkCreator.createAtgardsrekommendationLink(scope.srsViewState.getApiDiagnosKod()));
                };

                scope.redirectToStatistikExternalSite = function(){
                    window.open(srsLinkCreator.createStatistikLink(scope.srsViewState.getApiDiagnosKod()));
                };

                scope.$watch('status', function(status){
                    if(!scope.status.open){
                        scope.srsViewState.status.riskInfoOpen = false;
                    }
                }, true);

            },
            templateUrl: '/web/webjars/common/webcert/srs/wcSrsResult.directive.html'
        };
    }]);
