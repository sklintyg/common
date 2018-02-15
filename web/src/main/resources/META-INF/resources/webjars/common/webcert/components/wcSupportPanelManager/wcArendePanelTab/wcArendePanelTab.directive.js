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
//TODO: Innehållet i detta direktiv är kopierat rakt av från META-INF/resources/webjars/common/webcert/fk/arenden/arendeList.controller.js och templaten och rakt av från

angular.module('common').directive('wcArendePanelTab', [
    '$log', '$rootScope', '$state', '$stateParams', '$timeout', '$filter',
    'common.dialogService', 'common.ObjectHelper', 'common.ErrorHelper',
    'common.ArendeProxy', 'common.ArendeListViewStateService', 'common.ArendeHelper', 'common.statService',
    'common.dynamicLabelService',
    function($log, $rootScope, $state, $stateParams, $timeout, $filter,
        dialogService, ObjectHelper, ErrorHelper,
        ArendeProxy, ArendeListViewState, ArendeHelper, statService, dynamicLabelService) {
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
            $scope.arendeList = [];

            $scope.$on('$destroy', function() {
                //Since ArendeListViewState is a service that's used elsewhere we need to clean up
                //loaded state related to this instance
                ArendeListViewState.reset();
            });

            function fetchArenden(intygId, intygProperties) {

                ArendeProxy.getArenden(intygId, intygProperties.type, function(result) {
                    $log.debug('getArendeForCertificate:success data:' + result);
                    ArendeListViewState.doneLoading = true;
                    ArendeListViewState.activeErrorMessageKey = null;

                    $scope.arendeList = ArendeHelper.createListItemsFromArenden(result);

                    ArendeListViewState.setArendeList($scope.arendeList);
                    // Merge all kompletteringar and set in ArendeListViewState
                    var kompletteringar = {};
                    angular.forEach(result, function(arende) {
                        if (arende.fraga.status === 'PENDING_INTERNAL_ACTION') {
                            angular.forEach(arende.fraga.kompletteringar, function(komplettering) {
                                var key = komplettering.jsonPropertyHandle;
                                if (key) {
                                    // Uppdatera ämne och status
                                    komplettering.amne = arende.fraga.amne;
                                    komplettering.status = arende.fraga.status;

                                    if (key === 'tillaggsfragor') {
                                        var tillaggsfragor = dynamicLabelService.getTillaggsFragor();
                                        if (tillaggsfragor) {
                                            for (var i = 0; i < tillaggsfragor.length; i++) {
                                                if (tillaggsfragor[i].id === komplettering.frageId) {
                                                    key += '[' + i + '].svar';
                                                }
                                            }
                                        }
                                    }
                                    if (!kompletteringar[key]) {
                                        kompletteringar[key] = [];
                                    }

                                    kompletteringar[key].push(komplettering);
                                }
                            });
                        }
                    });
                    ArendeListViewState.setKompletteringar(kompletteringar);

                    $rootScope.$broadcast('arenden.loaded');

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
                deferred.resolve(ArendeHelper.getUnhandledArenden($scope.arendeList));
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
                                    angular.forEach($scope.arendeList, function(arendeListItem) {
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

            $scope.dismissSentMessage = function() {
                $scope.viewState.sentMessage = false;
            };

            $scope.openArendenFilter = function(arendeListItem) {
                return arendeListItem.arende.fraga.status !== 'CLOSED';
            };

            $scope.closedArendenFilter = function(arendeListItem) {
                return arendeListItem.arende.fraga.status === 'CLOSED';
            };

            $scope.kompletteringarFilter = function(arendeListItem) {
                return arendeListItem.arende.fraga.amne === 'KOMPLETTERING_AV_LAKARINTYG' ||
                        arendeListItem.arende.fraga.amne === 'KOMPLT';
            };

        }
    };
} ]);
