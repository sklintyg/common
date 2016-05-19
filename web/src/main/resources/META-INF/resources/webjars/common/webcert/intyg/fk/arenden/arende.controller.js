angular.module('common').controller('common.ArendeCtrl',
    ['$log', '$rootScope', '$state', '$stateParams', '$scope', '$timeout', '$window', '$filter',
        'common.dialogService', 'common.ObjectHelper',
        'common.ArendeProxy', 'common.ArendenViewStateService', 'common.ArendeHelper',
        function ($log, $rootScope, $state, $stateParams, $scope, $timeout, $window, $filter,
                  dialogService, ObjectHelper,
                  ArendeProxy, ArendenViewState, ArendeHelper) {
            'use strict';

            ArendenViewState.reset();
            ArendenViewState.setIntygType($state.current.data.intygType);
            $scope.viewState = ArendenViewState;
            $scope.arendeList = [];

            // Load

            function filterKompletteringar(arendeList, intygProperties) {

                var isAnyKompletteringarNotHandled = false;

                // Filter out the komplettering the utkast was based on and only that one.
                var filteredList = arendeList.filter(function(arendeListItem) {

                    var isKompletteringFraga = arendeListItem.amne === 'KOMPLETTERING_AV_LAKARINTYG' || arendeListItem.amne === 'KOMPLT';

                    // Check if this komplettering isn't handled. Used to show sign if there are no more unhandled kompletteringar
                    if(!isAnyKompletteringarNotHandled){
                        isAnyKompletteringarNotHandled = (isKompletteringFraga && arendeListItem.arende.fraga.status !== 'CLOSED');
                    }

                    // Filter out the komplettering the utkast was based on and only that one.
                    return isKompletteringFraga && Number(arendeListItem.internReferens) === Number(ArendenViewState.intygProperties.meddelandeId);
                });

                // If there aren't any kompletteringar that aren't handled already, we can show the sign that all kompletteringar are handled.
                ArendenViewState.showAllKompletteringarHandled = !isAnyKompletteringarNotHandled;
                return filteredList;
            }

            this.filterKompletteringar = filterKompletteringar;


            function fetchArenden(intygId, intygProperties) {
                ArendeProxy.getArenden(intygId, intygProperties.type, function (result) {
                    $log.debug('getArendeForintygificate:success data:' + result);
                    ArendenViewState.doneLoading = true;
                    ArendenViewState.activeErrorMessageKey = null;
                    ArendenViewState.showAllKompletteringarHandled = false;

                    if(ObjectHelper.isDefined(intygProperties)){
                        // If kompletteringsmode, only show kompletteringsissues
                        if (intygProperties.kompletteringOnly) {
                            result = filterKompletteringar(result, intygProperties);
                        }
                    }

                    $scope.arendeList = ArendeHelper.createListItemsFromArenden(result);

                    // Tell viewcertctrl about the intyg in case cert load fails
                    /*if (result.length > 0) {
                        $rootScope.$emit('ViewCertCtrl.load', result[0].intygsReferens);
                    }*/

                }, function (errorData) {
                    // show error view
                    ArendenViewState.doneLoading = true;
                    ArendenViewState.activeErrorMessageKey = errorData.errorCode;
                });
            }

            var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', function (event, intyg, intygProperties) {

                // IMPORTANT!! DON'T LET fetchArenden DEPEND ON THE INTYG LOAD EVENT (intyg) in this case!
                // Messages needs to be loaded separately from the intyg as user should be able to see messages even if intyg didn't load.
                // Used when coming from Intyg page.
                ArendenViewState.intyg = intyg;
                ArendenViewState.intygProperties.isLoaded = false;
                ArendenViewState.intygProperties.isSent = false;
                ArendenViewState.intygProperties.isRevoked = false;
                if (ObjectHelper.isDefined(ArendenViewState.intyg) && ObjectHelper.isDefined(ArendenViewState.intygProperties)) {

                    ArendenViewState.intygProperties = intygProperties;
                    ArendenViewState.intygProperties.isLoaded = true;
                    var intygId = $stateParams.certificateId;
                    if (intygProperties.forceUseProvidedIntyg) {
                        // Used for utkast page. In this case we must use the id from cert because $stateParams.certificateId is the id of the utkast, not the parentIntyg
                        intygId = intyg.id;
                    }
                    fetchArenden(intygId, ArendenViewState.intygProperties);

                } else if (ObjectHelper.isDefined($stateParams.certificateId)) {
                    fetchArenden($stateParams.certificateId, null);
                }
            });
            $scope.$on('$destroy', unbindFastEvent);

            // listeners - interscope communication
            var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent', function ($event, deferred, unhandledQas) {
                //qaHelper.updateAnsweredAsHandled(deferred, unhandledQas, true);
                deferred.resolve();
            });
            $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);

            var unbindHasUnhandledQasEvent = $scope.$on('hasUnhandledQasEvent', function ($event, deferred) {
                deferred.resolve([]);
                //deferred.resolve(fragaSvarCommonService.getUnhandledQas($scope.qaList));
            });
            $scope.$on('$destroy', unbindHasUnhandledQasEvent);


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
        }]);
