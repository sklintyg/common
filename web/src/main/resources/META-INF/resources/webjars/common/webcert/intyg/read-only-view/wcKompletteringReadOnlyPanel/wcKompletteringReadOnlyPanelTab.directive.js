/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

angular.module('common').directive('wcKompletteringReadOnlyPanelTab', [
    '$log', '$q', '$rootScope', '$state', '$stateParams', '$timeout', 'common.ObjectHelper', 'common.ErrorHelper', 'common.UserModel',
    'common.ArendeProxy', 'common.ArendeListViewStateService', 'common.ArendeHelper', 'common.statService',
    function($log, $q, $rootScope, $state, $stateParams, $timeout, ObjectHelper, ErrorHelper, UserModel,
        ArendeProxy, ArendeListViewState, ArendeHelper, statService) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        templateUrl: '/web/webjars/common/webcert/intyg/read-only-view/wcKompletteringReadOnlyPanel/wcKompletteringReadOnlyPanelTab.directive.html',
        controller: function($scope) {

            ArendeListViewState.reset();
            ArendeListViewState.setIntygType($state.current.data.intygType);
            ArendeListViewState.intyg = null;
            $scope.viewState = ArendeListViewState;
            $scope.unhandledKompletteringCount = 0;

            $scope.$on('$destroy', function() {
                //Since ArendeListViewState is a service that's used elsewhere we need to clean up
                //loaded state related to this instance
                ArendeListViewState.reset();
            });


            var abortFetchArenden;
            $scope.$on('$destroy', function() {
                if (abortFetchArenden) {
                    abortFetchArenden.resolve();
                    abortFetchArenden = null;
                }
            });

            function fetchArenden(intygId, intygTyp) {

                abortFetchArenden = $q.defer();

                ArendeProxy.getArenden(intygId, intygTyp, abortFetchArenden.promise, function(result) {
                    $log.debug('getArendeForCertificate:success data:' + result);
                    ArendeListViewState.activeErrorMessageKey = null;

                    ArendeListViewState.setArendeList(result);

                    angular.forEach(ArendeListViewState.arendeList, function(arendeListItem) {
                        if (arendeListItem.isOpen()) {
                            if (arendeListItem.isKomplettering()) {
                                $scope.unhandledKompletteringCount++;
                            }
                        }
                    });

                    $timeout(function(){
                        ArendeListViewState.doneLoading = true;
                    });
                }, function(errorData) {
                    // show error view
                    ArendeListViewState.doneLoading = true;

                    ArendeListViewState.activeErrorMessageKey = 'common.error.could_not_load_cert_qa';

                    abortFetchArenden = null;

                });
            }
            $scope.kompletteringarFilter = function(arendeListItem) {
                return arendeListItem.isKomplettering();
            };

            fetchArenden($stateParams.certificateId, $state.current.data.intygType);

    
        }
    };
} ]);
