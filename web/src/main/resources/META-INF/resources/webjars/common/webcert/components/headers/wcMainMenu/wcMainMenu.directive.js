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
angular.module('common').directive('wcMainMenu', ['$state', '$location', 'common.UserModel', 'common.featureService',
    function($state, $location, UserModel, featureService) {
    'use strict';

    return {
        restrict: 'E',
        scope: {},
        templateUrl: '/web/webjars/common/webcert/components/headers/wcMainMenu/wcMainMenu.directive.html',
        link: function($scope) {

            $scope.menuDefs = [];
            //Set default stats
            $scope.stat = {
                fragaSvarValdEnhet: 0,
                fragaSvarAndraEnheter: 0,
                intygValdEnhet: 0,
                intygAndraEnheter: 0,
                vardgivare: []
            };
            /**
             * Event listeners
             */
            $scope.$on('statService.stat-update', function(event, message) {
                $scope.stat = message;
            });

            if (!UserModel.user) {
                return;
            }

            $scope.isDoctor = UserModel.user.isLakareOrPrivat;

            $scope.isActive = function(page) {
                if (!page) {
                    return false;
                }

                page = page.substr(page.lastIndexOf('/') + 1);
                if (($state.current.data && angular.isString($state.current.data.defaultActive)) &&
                    (page === $state.current.data.defaultActive)) {
                    return true;
                }

                var currentRoute = $location.path().substr($location.path().lastIndexOf('/') + 1);
                return page === currentRoute;
            };

            function buildMenu() {
                var menu = [];
                if (featureService.isFeatureActive(featureService.features.HANTERA_FRAGOR)) {
                    menu.push({
                        link: '/#/enhet-arenden',
                        label: 'Ej hanterade ärenden',
                        requiresDoctor: false,
                        statNumberId: 'stat-unitstat-unhandled-question-count',
                        statTooltip: 'not set',
                        id: 'menu-enhet-arenden',
                        getStat: function() {
                            this.statTooltip = 'Vårdenheten har ' + $scope.stat.fragaSvarValdEnhet +
                                ' ej hanterade ärenden.';
                            return $scope.stat.fragaSvarValdEnhet || '';
                        }
                    });
                }

                if (featureService.isFeatureActive(featureService.features.HANTERA_INTYGSUTKAST)) {
                    menu.push({
                        link: '/#/unsigned',
                        label: 'Ej signerade utkast',
                        requiresDoctor: false,
                        statNumberId: 'stat-unitstat-unsigned-certs-count',
                        statTooltip: 'not set',
                        id: 'menu-unsigned',
                        getStat: function() {
                            this.statTooltip =
                                'Vårdenheten har ' + $scope.stat.intygValdEnhet + ' ej signerade utkast.';
                            return $scope.stat.intygValdEnhet || '';
                        }
                    });
                }

                menu.push({
                    link: '/#/signed-certificates',
                    label: 'Signerade intyg',
                    requiresDoctor: true,
                    statTooltip: null,
                    id: 'menu-signed-certificates'
                });

                if (featureService.isFeatureActive(featureService.features.HANTERA_INTYGSUTKAST)) {
                    var writeCertMenuDef = {
                        link: '/#/create/index',
                        label: 'Sök / skriv intyg',
                        requiresDoctor: false,
                        id: 'menu-skrivintyg',
                        getStat: function() {
                            return '';
                        }
                    };

                    if (UserModel.user.isLakareOrPrivat) {
                        menu.splice(0, 0, writeCertMenuDef);
                    } else {
                        menu.push(writeCertMenuDef);
                    }
                }


                return menu;
            }

            $scope.menuDefs = buildMenu();

        }
    };
}]);
