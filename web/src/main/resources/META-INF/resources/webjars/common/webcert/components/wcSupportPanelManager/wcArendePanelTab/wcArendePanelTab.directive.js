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

angular.module('common').directive('wcArendePanelTab', [
    '$log', '$rootScope', '$state', '$stateParams', '$timeout', 'common.ObjectHelper', 'common.ErrorHelper', 'common.UserModel',
    'common.ArendeProxy', 'common.ArendeListViewStateService', 'common.ArendeHelper', 'common.statService',
    function($log, $rootScope, $state, $stateParams, $timeout, ObjectHelper, ErrorHelper, UserModel,
        ArendeProxy, ArendeListViewState, ArendeHelper, statService) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/wcArendePanelTab.directive.html',
        controller: function($scope) {

            ArendeListViewState.reset();
            ArendeListViewState.setIntygType($state.current.data.intygType);
            $scope.viewState = ArendeListViewState;
            $scope.unhandledKompletteringCount = 0;
            $scope.unhandledAdministrativaFragorCount = 0;

            $scope.setFilterKomplettering = function(value) {
                $scope.isFilterKomplettering = value;
            };

            $scope.$on('$destroy', function() {
                //Since ArendeListViewState is a service that's used elsewhere we need to clean up
                //loaded state related to this instance
                ArendeListViewState.reset();
            });

            function updateCounts() {
                $scope.unhandledKompletteringCount = 0;
                $scope.unhandledAdministrativaFragorCount = 0;
                angular.forEach(ArendeListViewState.arendeList, function(arendeListItem) {
                    if (arendeListItem.isOpen()) {
                        if (arendeListItem.isKomplettering()) {
                            $scope.unhandledKompletteringCount++;
                        }
                        else {
                            $scope.unhandledAdministrativaFragorCount++;
                        }
                    }
                });
            }

            $scope.$on('arenden.updated', function(){
                updateCounts();
            });

            function fetchArenden(intygId, intygProperties) {

                ArendeProxy.getArenden(intygId, intygProperties.type, function(result) {
                    $log.debug('getArendeForCertificate:success data:' + result);
                    ArendeListViewState.activeErrorMessageKey = null;

                    ArendeListViewState.setArendeList(result);

                    // Select default state for isFilterKomplettering
                    $scope.isFilterKomplettering =
                        !($scope.unhandledKompletteringCount === 0 && $scope.unhandledAdministrativaFragorCount > 0);

                    $timeout(function(){
                        ArendeListViewState.doneLoading = true;
                    });

                }, function(errorData) {
                    // show error view
                    ArendeListViewState.doneLoading = true;

                    ArendeListViewState.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                });
            }

            var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', function(event, intyg, intygProperties) {

                // IMPORTANT!! DON'T LET fetchArenden DEPEND ON THE INTYG LOAD EVENT (intyg) in this case!
                // Messages needs to be loaded separately from the intyg as user should be able to see messages even if intyg didn't load.
                // Used when coming from Intyg page.
                ArendeListViewState.intyg = intyg;
                if (ObjectHelper.isDefined(ArendeListViewState.intyg) &&
                        ObjectHelper.isDefined(ArendeListViewState.intygProperties)) {

                    ArendeListViewState.intygProperties = intygProperties;
                    ArendeListViewState.intygProperties.isLoaded = true;
                    var intygId = $stateParams.certificateId;
                    if (intygProperties.forceUseProvidedIntyg) {
                        // Used for utkast page. In this case we must use the id from intyg because $stateParams.certificateId is the id of the utkast, not the parentIntyg
                        intygId = intyg.id;
                    }
                    fetchArenden(intygId, ArendeListViewState.intygProperties);

                } else if (ObjectHelper.isDefined($stateParams.certificateId)) {
                    fetchArenden($stateParams.certificateId, {
                        type: $state.current.data.intygType
                    });
                }

                ArendeListViewState.intygProperties.isInteractionEnabled = $scope.config.intygContext.isSigned && !ArendeListViewState.intygProperties.isRevoked;
            });
            $scope.$on('$destroy', unbindFastEvent);

            // listeners - interscope communication
            var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent',
                    function($event, deferred, unhandledQas) {
                        _updateAnsweredAsHandled(deferred, unhandledQas);
                        deferred.resolve();
                    });
            $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);

            var unbindHasUnhandledQasEvent = $scope.$on('hasUnhandledQasEvent', function($event, deferred) {
                deferred.resolve(ArendeHelper.getUnhandledArenden(ArendeListViewState.arendeList));
            });
            $scope.$on('$destroy', unbindHasUnhandledQasEvent);

            function _updateAnsweredAsHandled(deferred, unhandledarendes) {
                if (unhandledarendes === undefined || unhandledarendes.length === 0) {
                    return;
                }
                ArendeProxy.closeAllAsHandled(unhandledarendes, ArendeListViewState.common.intygProperties.type,
                        function(arendes) {
                            if (arendes) {
                                angular.forEach(arendes, function(arende) {
                                    angular.forEach(ArendeListViewState.arendeList, function(arendeListItem) {
                                        if (arende.fraga.internReferens === arendeListItem.arende.fraga.internReferens) {
                                            angular.copy(arende, arendeListItem.arende);
                                            arendeListItem.updateArendeListItem();
                                        }
                                    });
                                });
                                statService.refreshStat();
                            }
                            if (deferred) {
                                deferred.resolve();
                            }
                        }, function() { // unused parameter: errorData
                            // show error view
                            if (deferred) {
                                deferred.resolve();
                            }
                        });
            }

            // Scope interactions

            $scope.openArendenFilter = function(arendeListItem) {
                return arendeListItem.isOpen();
            };

            $scope.kompletteringarFilter = function(arendeListItem) {
                return arendeListItem.isKomplettering();
            };

            $scope.administrativaFragorFilter = function(arendeListItem) {
                return !$scope.kompletteringarFilter(arendeListItem);
            };

        }
    };
} ]);
