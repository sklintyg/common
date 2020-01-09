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
/**
 * Created by BESA on 2015-03-05.
 */

/**
 * arendePanel directive. Common directive for both unhandled and handled questions/answers
 */
angular.module('common').directive('arendePanel',
    [ 'common.ObjectHelper', 'common.ArendeSvarModel',
        function(ObjectHelper, ArendeSvarModel) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/panel/arendePanel.directive.html',
                scope: {
                    panelId: '@',
                    arendeListItem: '=',
                    arendeList: '=',
                    parentViewState: '=',
                    lastCard: '<',
                    config: '='
                },
                controller: function($scope) {

                    $scope.unhandledKompletteringCount = 0;
                    $scope.unhandledAdminFragor = 0;

                    $scope.showAnswer = function() {
                        var ArendeSvar = $scope.arendeListItem.arende.svar;
                        // If closed and has a meddelande it is answered by message
                        // If closed and has answeredWithIntyg object it was answered with intyg
                        return (ArendeSvar.status === 'CLOSED' &&
                            (ObjectHelper.isEmpty(ArendeSvar.meddelande) === false ||
                                ObjectHelper.isDefined(ArendeSvar.answeredWithIntyg))) ||
                            (ArendeSvar.status === 'ANSWERED');
                    };

                    function _buildArendeSvarModel() {
                        var ArendeSvar = ArendeSvarModel.build($scope.parentViewState, $scope.arendeListItem);
                        $scope.arendeSvar = ArendeSvar;
                        return ArendeSvar;
                    }

                    var ArendeSvar = _buildArendeSvarModel();
                    $scope.$on('arenden.updated', function() {
                        ArendeSvar = _buildArendeSvarModel();
                    });

                    angular.forEach($scope.parentViewState.arendeList, function(arendeListItem) {
                        if (arendeListItem.isOpen()) {
                            if (arendeListItem.isKomplettering()) {
                                $scope.unhandledKompletteringCount++;
                            } else {
                                $scope.unhandledAdminFragor++;
                            }
                        }
                    });

                    var arendePanelSvarController;
                    this.registerArendePanelSvar = function(controller) {
                        arendePanelSvarController = controller;
                    };
                    this.getArendePanelSvar = function() {
                        return arendePanelSvarController;
                    };
                }
            };
        }]);
