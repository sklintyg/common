angular.module('common').controller('common.ArendeCtrl',
    ['$log', '$rootScope', '$state', '$stateParams', '$scope', '$timeout', '$window', '$filter',
        'common.dialogService', 'common.ObjectHelper',
        'common.ArendeProxy', 'common.ArendenViewStateService', 'common.ArendeHelper',
        function ($log, $rootScope, $state, $stateParams, $scope, $timeout, $window, $filter,
                  dialogService, ObjectHelper,
                  ArendeProxy, ArendenViewState, ArendeHelper) {
            'use strict';

            ArendenViewState.setIntygType($state.current.data.intygType);
            $scope.viewState = ArendenViewState;
            $scope.arendeList = [];

            function fetchArenden(intygId, intygProperties) {

                // Request loading of arenden for this intyg
                ArendeProxy.getArenden(intygId, intygProperties.intygType, function (result) {
                    $log.debug('getArendeForintygificate:success data:' + result);
                    ArendenViewState.doneLoading = true;
                    ArendenViewState.activeErrorMessageKey = null;
                    $scope.arendeList = ArendeHelper.createListItemsFromArenden(result);
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
                        intygId = cert.id;
                    }
                    fetchArenden(intygId, ArendenViewState.intygProperties);

                } else if (ObjectHelper.isDefined($stateParams.certificateId)) {
                    fetchArenden($stateParams.certificateId, null);
                }
            });
            $scope.$on('$destroy', unbindFastEvent);


            $scope.openArendenFilter = function (arende) {
                return true;
            };

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
        }]);
