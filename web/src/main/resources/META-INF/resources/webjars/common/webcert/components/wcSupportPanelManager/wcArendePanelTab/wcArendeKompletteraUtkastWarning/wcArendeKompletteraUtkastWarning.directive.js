/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
    [ '$rootScope', 'common.ArendeListViewStateService', 'common.ArendeHelper', 'common.ObjectHelper',
        function($rootScope, ArendeListViewState, ArendeHelper, ObjectHelper) {
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
                        redirectToExistingUtkast: false
                    };

                    function onIntygLoaded(event, intyg, intygProperties) {
                        if(ObjectHelper.isDefined(ArendeListViewState.intygProperties.latestChildRelations)) {
                            if (ArendeListViewState.intygProperties.latestChildRelations.complementedByUtkast) {
                                $scope.intygType = ArendeListViewState.intygProperties.type;
                                //WE only allow propagation to same version, so use this version.
                                $scope.intygTypeVersion = intyg.textVersion;
                                $scope.intygId = ArendeListViewState.intygProperties.latestChildRelations.complementedByUtkast.intygsId;
                                $scope.kompletteringConfig.redirectToExistingUtkast = true;
                            }
                        }

                    }

                    var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', onIntygLoaded);
                    $scope.$on('$destroy', unbindFastEvent);

                    onIntygLoaded(null, ArendeListViewState.intyg, ArendeListViewState.intygProperties);

                    $scope.showFortsattUtkastWarning = function() {
                        return $scope.kompletteringConfig.redirectToExistingUtkast;
                    };
                }
            };
        }]);
