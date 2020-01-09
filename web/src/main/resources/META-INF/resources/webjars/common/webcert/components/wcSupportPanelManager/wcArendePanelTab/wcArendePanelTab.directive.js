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

angular.module('common').directive('wcArendePanelTab', [
    '$log', '$q', '$rootScope', '$state', '$stateParams', '$timeout', 'common.ObjectHelper', 'common.ErrorHelper',
    'common.UserModel', 'common.ArendeProxy', 'common.ArendeListViewStateService', 'common.ArendeHelper',
    'common.statService', 'common.ResourceLinkService',
    function($log, $q, $rootScope, $state, $stateParams, $timeout, ObjectHelper, ErrorHelper, UserModel,
        ArendeProxy, ArendeListViewState, ArendeHelper, statService, ResourceLinkService) {
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

                $scope.isAdministrativeQuestionViewReadOnly = function() {
                    // If user has no access to create or answer administrative questions, the view is considered read only.
                    return !ResourceLinkService.isLinkTypeExists(ArendeListViewState.intygProperties.links,
                        'SKAPA_FRAGA') &&
                        !ResourceLinkService.isLinkTypeExists(ArendeListViewState.intygProperties.links,
                            'BESVARA_FRAGA');
                };

                $scope.isComplementQuestionViewReadOnly = function() {
                    // If user has no access to answer complement questions, the view is considered read only.
                    return !ResourceLinkService.isLinkTypeExists(ArendeListViewState.intygProperties.links,
                        'BESVARA_KOMPLETTERING') &&
                        !ResourceLinkService.isLinkTypeExists(ArendeListViewState.intygProperties.links,
                            'BESVARA_FRAGA');
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
                            } else {
                                $scope.unhandledAdministrativaFragorCount++;
                            }
                        }
                    });
                    $rootScope.$emit('totalArendenCount.updated', {
                        count: $scope.unhandledAdministrativaFragorCount + $scope.unhandledKompletteringCount
                    });
                }

                $scope.$on('arenden.updated', function() {
                    updateCounts();
                });

                function updateInteractionEnabled() {
                    ArendeListViewState.intygProperties.isInteractionEnabled =
                        ArendeListViewState.intygProperties.isSent &&
                        $scope.config.intygContext.isSigned &&
                        !ArendeListViewState.intygProperties.isRevoked &&
                        !ObjectHelper.isDefined(
                            ArendeListViewState.intygProperties.latestChildRelations.complementedByUtkast);
                }

                $scope.$on('intygstatus.updated', function() {
                    updateInteractionEnabled();
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

                        // Select default state for isFilterKomplettering
                        $scope.isFilterKomplettering =
                            !($scope.unhandledKompletteringCount === 0 && $scope.unhandledAdministrativaFragorCount >
                                0);

                        abortFetchArenden = null;

                        $timeout(function() {
                            ArendeListViewState.doneLoading = true;
                        });
                    }, function(errorData) {
                        // show error view
                        ArendeListViewState.doneLoading = true;

                        ArendeListViewState.activeErrorMessageKey = 'could_not_load_cert_qa';

                        abortFetchArenden = null;

                        // intygHeader.controller.js waits for this event
                        $rootScope.$broadcast('arenden.updated');
                    });
                }

                // If this is a signed intyg we cant start fetching ärenden with this certificateId
                if ($scope.config.intygContext.isSigned) {
                    fetchArenden($stateParams.certificateId, $state.current.data.intygType);
                } else {
                    // If this is a not signed intyg we need the id from the parentIntyg. $stateParams.certificateId is the id of the utkast, not the parentIntyg
                    $scope.$on('arenden.loadForIntygId', function(event, intygId) {
                        fetchArenden(intygId, $state.current.data.intygType);
                    });
                }

                var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', function(event, intyg, intygProperties) {

                    ArendeListViewState.intyg = intyg;

                    if (ObjectHelper.isDefined(ArendeListViewState.intyg) &&
                        ObjectHelper.isDefined(ArendeListViewState.intygProperties)) {
                        ArendeListViewState.intygProperties = intygProperties;
                        ArendeListViewState.intygProperties.isLoaded = true;
                    }

                    updateInteractionEnabled();
                    ArendeListViewState.intygLoaded = true;
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
                                        if (arende.fraga.internReferens ===
                                            arendeListItem.arende.fraga.internReferens) {
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

                $scope.paminnelseFilter = function(arendeListItem) {
                    return arendeListItem.isPaminnelse();
                };

                $scope.administrativaFragorFilter = function(arendeListItem) {
                    return !$scope.kompletteringarFilter(arendeListItem);
                };

            }
        };
    }]);
