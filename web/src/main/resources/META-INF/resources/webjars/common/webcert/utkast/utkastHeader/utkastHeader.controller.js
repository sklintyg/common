/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').controller('common.UtkastHeader', [ '$scope', '$state', '$window',
    'ViewState', 'common.UtkastHeaderViewState', 'common.UtkastService', '$http', '$timeout',
    function($scope, $state, $window,
        ViewState, UtkastHeaderViewState, UtkastService, $http, $timeout) {
       'use strict';

        $scope.utkastViewState = ViewState;

        var intygType = $state.current.data.intygType; // get type from state so we dont have to wait for intyg.load

        UtkastHeaderViewState.setIntygViewState(ViewState, intygType);
        var oldUnload = $window.onbeforeunload;

        $window.onbeforeunload = function(event) {
            if (oldUnload !== undefined) {
                oldUnload(event);
                // Timeout is stalled while leave/stay is shown.
                $timeout(function() {
                    $http.get('/api/anvandare/logout/cancel');
                }, 500);
            }
            $scope.certForm.$commitViewValue(); 
            if ($scope.certForm.$dirty) {
                // Trigger a save now. If the user responds with "Leave the page" we may not have time to save
                // before the page is closed. We could use an ajax request with async:false this will force the
                // browser to "hang" the page until the request is complete. But using async:false is deprecated
                // and will be removed in future browsers.
                UtkastService.save();
                var message = 'Om du väljer "Lämna sidan" kan ändringar försvinna. Om du väljer "Stanna kvar på sidan" autosparas ändringarna.';
                if (typeof event === 'undefined') {
                    event = $window.event;
                }
                if (event) {
                    event.returnValue = message;
                }
                return message;
            }
        };

        $scope.$on('$destroy', function() {
            $window.onbeforeunload = oldUnload;
        });
    }]);
