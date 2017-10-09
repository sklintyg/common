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
 * Display SRS help texts
 */
angular.module('common').directive('wcSrsContent', ['$window', 'common.srsLinkCreator',
    function($window, srsLinkCreator) {
        'use strict';

        return {
            restrict: 'EA',
            link: function(scope, element, attrs) {
                scope.isCollapsed = true;
                //$('.my_tooltip').tooltip({ html: true })
                scope.infoMessage = '';
                scope.errorMessage = '';

                scope.externalConsent = {
                    templateUrl: '/web/webjars/common/webcert/srs/wcSrsContent.consent-popover.html'
                };

                scope.consentInfoOpen = false;
                scope.closeSrs = function(){
                    scope.status.open = !scope.status.open;
                    scope.consentInfoOpen = false;
                };

                scope.readMoreConsent = function(){
                    $window.open(srsLinkCreator.createSamtyckeLink, '_blank');
                };

                var riskUrl = srsLinkCreator.createPrediktionsModellLink;
                scope.externalRiskInfo =
                    'Risksignalen för att sjukfallet varar mer än 90 dagar beräknas med matematiska metoder ' +
                    'tillämpade på en rad variabler som till exempel ålder, kön, bostadsort och tidigare vårdkontakter ' +
                    'för att försöka detektera om den aktuella individens risk skiljer sig från andra patienter inom samma ' +
                    'diagnosgrupp. Metoden ska ses som ett komplement inför den egna professionella bedömningen.<br><button ' +
                    'class="btn btn-info ng-binding" onClick="window.open(\'' + riskUrl +
                    '\')">Läs mer</button>';


                scope.$watch('status', function(status){
                    if(!status.open){
                        scope.consentInfoOpen = false;
                    }
                }, true);

                //scope.test = "window.open('http://rattsjukskrivning.se/tjanster/omwebbplatsen/inloggning.14.html?redirect=http%3A%2F%2Frattsjukskrivning.se%2F')";

            },
            templateUrl: '/web/webjars/common/webcert/srs/wcSrsContent.directive.html'
        };
    }]);
