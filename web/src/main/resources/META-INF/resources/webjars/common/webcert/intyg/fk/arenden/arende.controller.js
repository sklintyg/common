angular.module('common').controller('common.ArendeCtrl',
    ['$log', '$rootScope', '$state', '$stateParams', '$scope', '$timeout', '$window', '$filter',
        'common.dialogService', 'common.ObjectHelper',
        'common.ArendeProxy', 'common.ArendenViewStateService', 'common.ArendeHelper',
        function ($log, $rootScope, $state, $stateParams, $scope, $timeout, $window, $filter,
                  dialogService, ObjectHelper,
                  ArendeProxy, ArendenViewState, ArendeHelper) {
            'use strict';

            $scope.viewState = ArendenViewState;
            $scope.arendeList = [];
            $scope.intyg = {};
            $scope.intygProperties = {
                isLoaded: false,
                isSent: false,
                isRevoked: false,
                intygType: $state.current.data.intygType
            };

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
                $scope.intyg = intyg;
                $scope.intygProperties.isLoaded = false;
                $scope.intygProperties.isSent = false;
                $scope.intygProperties.isRevoked = false;

                if (ObjectHelper.isDefined(intyg) && ObjectHelper.isDefined(intygProperties)) {

                    $scope.intygProperties = intygProperties;
                    $scope.intygProperties.isLoaded = true;
                    var intygId = $stateParams.certificateId;
                    if (intygProperties.forceUseProvidedIntyg) {
                        // Used for utkast page. In this case we must use the id from cert because $stateParams.certificateId is the id of the utkast, not the parentIntyg
                        intygId = cert.id;
                    }
                    fetchArenden(intygId, $scope.intygProperties);

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
                qaHelper.updateAnsweredAsHandled(deferred, unhandledQas, true);
            });

            $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);

            var unbindHasUnhandledQasEvent = $scope.$on('hasUnhandledQasEvent', function ($event, deferred) {
                deferred.resolve(fragaSvarCommonService.getUnhandledQas($scope.qaList));
            });

            $scope.$on('$destroy', unbindHasUnhandledQasEvent);
        }]);
