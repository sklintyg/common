/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

angular.module('common').directive('wcArendeKompletteraUtkastWarning',
    [ '$rootScope', 'common.ArendeListViewStateService', 'common.ArendeHelper',
        function($rootScope, ArendeListViewState, ArendeHelper) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/wcArendeKompletteraUtkastWarning/wcArendeKompletteraUtkastWarning.directive.html',
                scope: {
                },
                controller: function($scope, $element, $attrs) {

                    // Continue draft warning message on intyg
                    $scope.kompletteringConfig = {
                        //Existence of complementedByUtkast means an utkast with complemented relation exist.
                        redirectToExistingUtkast: false,
                        svaraMedNyttIntygDisabled: ArendeHelper.isSvaraMedNyttIntygDisabled(),
                    };

                    var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', function(event, intyg, intygProperties) {
                        $scope.intygType = ArendeListViewState.intygProperties.type;
                        $scope.intygId = ArendeListViewState.intygProperties.latestChildRelations.complementedByUtkast.intygsId;
                        $scope.kompletteringConfig.redirectToExistingUtkast = !!ArendeListViewState.intygProperties.latestChildRelations.complementedByUtkast;
                    });
                    $scope.$on('$destroy', unbindFastEvent);

                    $scope.showFortsattUtkastWarning = function() {
                        return $scope.kompletteringConfig.redirectToExistingUtkast && !$scope.kompletteringConfig.svaraMedNyttIntygDisabled;
                    }
                }
            };
        }]);
