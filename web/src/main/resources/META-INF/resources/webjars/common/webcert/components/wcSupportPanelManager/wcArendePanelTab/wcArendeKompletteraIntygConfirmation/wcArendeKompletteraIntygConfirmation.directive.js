/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcArendeKompletteraIntygConfirmation',
    [ '$rootScope', 'common.ArendeListViewStateService', 'common.ArendeHelper',
        function($rootScope, ArendeListViewState, ArendeHelper) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/wcArendeKompletteraIntygConfirmation/wcArendeKompletteraIntygConfirmation.directive.html',
                scope: {
                },
                controller: function($scope, $element, $attrs) {

                    function onIntygLoaded(event, intyg, intygProperties) {
                        if (ArendeListViewState.intygProperties.latestChildRelations.complementedByIntyg) {
                            $scope.intygType = ArendeListViewState.intygProperties.type;
                            $scope.intygId = ArendeListViewState.intygProperties.latestChildRelations.complementedByIntyg.intygsId;
                            $scope.intygMakulerat = ArendeListViewState.intygProperties.latestChildRelations.complementedByIntyg.makulerat;
                            $scope.intygTypeVersion = ArendeListViewState.intygProperties.intygTypeVersion;
                        }
                        else {
                            $scope.intygType = undefined;
                            $scope.intygId = undefined;
                            $scope.intygTypeVersion = undefined;
                            $scope.intygMakulerat = undefined;
                        }
                    }

                    var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', onIntygLoaded);
                    $scope.$on('$destroy', unbindFastEvent);

                    onIntygLoaded(null, ArendeListViewState.intyg, ArendeListViewState.intygProperties);

                    $scope.showKompletteringsIntyg = function() {
                        return $scope.intygType && $scope.intygId && !$scope.intygMakulerat;
                    };
                }
            };
        }]);
