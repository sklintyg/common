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
/**
 * Created by BESA on 2015-03-05.
 */

/**
 * arendeVidarebefordra directive. Component for Vidarebefordra button, checkbox and loading animation
 */
angular.module('common').directive('arendeVidarebefordra',
    ['$window', '$log', '$timeout', 'common.ArendeVidarebefordraHelper', 'common.ArendeProxy', 'common.dialogService',
        'common.ObjectHelper', 'common.authorityService', 'common.UserModel',
        function($window, $log, $timeout, ArendeVidarebefordraHelper, ArendeProxy, DialogService, ObjectHelper, authorityService, UserModel) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/vidarebefordra/arendeVidarebefordra.directive.html',
                scope: {
                    panelId: '@',
                    arendeListItem: '=',
                    parentViewState: '='
                },
                controller: function($scope, $element, $attrs) {
                    $scope.forwardInProgress = false;

                    $scope.showVidarebefordra = function() {
                        var hasAuthPermission = authorityService.isAuthorityActive({
                            authority: UserModel.privileges.VIDAREBEFORDRA_FRAGASVAR,
                            intygstyp: $scope.parentViewState.intygProperties.type });
                        return hasAuthPermission && $scope.arendeListItem.arende.fraga.status !== 'CLOSED';
                    };

                    $scope.openMailDialog = function(arende) {
                        // Handle vidarebefordra dialog
                        // use timeout so that external mail client has a chance to start before showing dialog
                        $timeout(function() {
                            ArendeVidarebefordraHelper.handleVidareBefodradToggle(arende.fraga,
                                $scope.onVidarebefordradChange);
                        }, 1000);

                        // Launch mail client
                        if (ObjectHelper.isDefined(arende.fraga)) {
                            var arendeMailModel = {
                                intygId: arende.fraga.intygId,
                                intygType: $scope.parentViewState.intygProperties.type,
                                enhetsnamn: arende.fraga.enhetsnamn,
                                vardgivarnamn: arende.fraga.vardgivarnamn
                            };
                            $window.location = ArendeVidarebefordraHelper.buildMailToLink(arendeMailModel);
                        } else {
                            $log.debug('openMailDialog - cant build mailto link. arande.fraga is not defined');
                        }
                    };

                    $scope.onVidarebefordradChange = function() {
                        $scope.forwardInProgress = true;
                        ArendeProxy.setVidarebefordradState($scope.arendeListItem.arende.fraga.internReferens,
                            $scope.parentViewState.intygProperties.type,
                            $scope.arendeListItem.arende.fraga.vidarebefordrad,
                            function(result) {
                                $scope.forwardInProgress = false;
                                if (result && result.fraga) {
                                    $scope.arendeListItem.arende.fraga.vidarebefordrad =
                                        result.fraga.vidarebefordrad;
                                } else {
                                    $scope.arendeListItem.arende.fraga.vidarebefordrad =
                                        !$scope.arendeListItem.arende.fraga.vidarebefordrad;
                                    DialogService.showErrorMessageDialog(
                                        'Kunde inte markera/avmarkera frågan som vidarebefordrad. ' +
                                        'Försök gärna igen för att se om felet är tillfälligt. Annars kan du kontakta supporten');
                                }
                            });
                    };
                }
            };
        }]);
