/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
 * Display SRS questionnaire
 */
angular.module('common').directive('wcSrsResult', ['$window', 'common.ObjectHelper', 'common.srsProxy', 'common.srsLinkCreator',
    function($window, ObjectHelper, srsProxy, srsLinkCreator) {
        'use strict';

        return {
            restrict: 'E',
            link: function (scope, element, attrs) {
                scope.externalRisk = {
                    templateUrl: '/web/webjars/common/webcert/utkast/srs/wcSrsResult.risk-popover.html'
                };

                scope.logAtgarderLasMerButtonClicked = function() {
                    srsProxy.logSrsMeasuresLinkClicked(scope.srs.userClientContext, scope.srs.intygId,
                        scope.srs.vardgivareHsaId, scope.srs.hsaId);
                };

                scope.logStatistikLasMerButtonClicked = function() {
                    srsProxy.logSrsStatisticsLinkClicked(scope.srs.userClientContext, scope.srs.intygId,
                        scope.srs.vardgivareHsaId, scope.srs.hsaId);
                };

                scope.setActiveTab = function(tabname) {
                    if (tabname === 'statistics') {
                        srsProxy.logSrsStatisticsActivated(scope.srs.userClientContext, scope.srs.intygId,
                            scope.srs.vardgivareHsaId, scope.srs.hsaId);
                    }
                    scope.srs.activeTab = tabname;
                };

                scope.readMoreRisk = function(){
                    $window.open(srsLinkCreator.createPrediktionsModellLink, '_blank');
                };

                scope.redirectToAtgardExternalSite = function(){
                    window.open(srsLinkCreator.createAtgardsrekommendationLink(scope.srs.diagnosKod));
                };

                scope.redirectToStatistikExternalSite = function(){
                    window.open(srsLinkCreator.createStatistikLink(scope.srs.diagnosKod));
                };

            },
            templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsResult.directive.html'
        };
    }]);
