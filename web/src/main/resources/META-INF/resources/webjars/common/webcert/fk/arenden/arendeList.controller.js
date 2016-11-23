angular.module('common').controller('common.ArendeListCtrl',
    ['$log', '$rootScope', '$state', '$stateParams', '$scope', '$timeout', '$window', '$filter',
        'common.dialogService', 'common.ObjectHelper', 'common.ErrorHelper',
        'common.ArendeProxy', 'common.ArendeListViewStateService', 'common.ArendeHelper', 'common.statService',
        'common.dynamicLabelService',
        function ($log, $rootScope, $state, $stateParams, $scope, $timeout, $window, $filter,
                  dialogService, ObjectHelper, ErrorHelper,
                  ArendeProxy, ArendeListViewState, ArendeHelper, statService, dynamicLabelService) {
            'use strict';

            ArendeListViewState.reset();
            ArendeListViewState.setIntygType($state.current.data.intygType);
            $scope.viewState = ArendeListViewState;
            $scope.arendeList = [];

            // Load

            function fetchArenden(intygId, intygProperties) {
                ArendeProxy.getArenden(intygId, intygProperties.type, function (result) {
                    $log.debug('getArendeForCertificate:success data:' + result);
                    ArendeListViewState.doneLoading = true;
                    ArendeListViewState.activeErrorMessageKey = null;

                    $scope.arendeList = ArendeHelper.createListItemsFromArenden(result);

                    // Merge all kompletteringar and set in ArendeListViewState
                    var kompletteringar = {};
                    angular.forEach(result, function(arende) {
                        if (arende.fraga.status === 'PENDING_INTERNAL_ACTION') {
                            angular.forEach(arende.fraga.kompletteringar, function (komplettering) {
                                komplettering.amne = arende.fraga.amne;
                                komplettering.status = arende.fraga.status;

                                var key = komplettering.jsonPropertyHandle;
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
                            });
                        }
                    });
                    ArendeListViewState.setKompletteringar(kompletteringar);

                }, function (errorData) {
                    // show error view
                    ArendeListViewState.doneLoading = true;

                    ArendeListViewState.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                });
            }

            var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', function (event, intyg, intygProperties) {

                // IMPORTANT!! DON'T LET fetchArenden DEPEND ON THE INTYG LOAD EVENT (intyg) in this case!
                // Messages needs to be loaded separately from the intyg as user should be able to see messages even if intyg didn't load.
                // Used when coming from Intyg page.
                ArendeListViewState.intyg = intyg;
                if (ObjectHelper.isDefined(ArendeListViewState.intyg) && ObjectHelper.isDefined(ArendeListViewState.intygProperties)) {

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
                        type:$state.current.data.intygType
                    });
                }
            });
            $scope.$on('$destroy', unbindFastEvent);

            // listeners - interscope communication
            var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent', function ($event, deferred, unhandledQas) {
                _updateAnsweredAsHandled(deferred, unhandledQas);
                deferred.resolve();
            });
            $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);

            var unbindHasUnhandledQasEvent = $scope.$on('hasUnhandledQasEvent', function ($event, deferred) {
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
                        $window.doneLoading = true;
                        if (deferred) {
                            deferred.resolve();
                        }
                    }, function() { // unused parameter: errorData
                        // show error view
                        $window.doneLoading = true;
                        if (deferred) {
                            deferred.resolve();
                        }
                    }
                );
            }

            // Scope interactions

            $scope.dismissSentMessage = function() {
                $scope.viewState.sentMessage = false;
            };

            $scope.openArendenFilter = function (arendeListItem) {
                return arendeListItem.arende.fraga.status !== 'CLOSED';
            };

            $scope.closedArendenFilter = function (arendeListItem) {
                return arendeListItem.arende.fraga.status === 'CLOSED';
            };

            $scope.kompletteringarFilter = function(arendeListItem) {
                return arendeListItem.arende.fraga.amne === 'KOMPLETTERING_AV_LAKARINTYG' || arendeListItem.arende.fraga.amne === 'KOMPLT';
            };

        }]);
