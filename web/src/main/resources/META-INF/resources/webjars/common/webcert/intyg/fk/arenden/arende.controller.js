angular.module('common').controller('common.ArendeCtrl',
    [ '$log', '$rootScope', '$state', '$stateParams', '$scope', '$timeout', '$window', '$filter', 'common.dialogService',
        'common.ArendeProxy', 'common.ArendenViewStateService',
        function($log, $rootScope, $state, $stateParams, $scope, $timeout, $window, $filter, dialogService,
                 ArendeProxy, ArendenViewState) {
            'use strict';

            $scope.viewState = ArendenViewState;
            $scope.arendeList = [];
            var intygType = $state.current.data.intygType;

            // Request loading of arendes for this intyg
            ArendeProxy.getArenden($stateParams.certificateId, intygType, function(result) {
                $log.debug('getArendeForintygificate:success data:' + result);
                ArendenViewState.doneLoading = true;
                ArendenViewState.activeErrorMessageKey = null;
                $scope.arendeList = result;
            }, function(errorData) {
                // show error view
                ArendenViewState.doneLoading = true;
                ArendenViewState.activeErrorMessageKey = errorData.errorCode;
            });

            $scope.intyg = {};
            $scope.intygProperties = {
                isLoaded: false,
                isSent: false,
                isRevoked: false
            };

            var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', function(event, intyg, intygProperties) {
                $scope.intyg = intyg;
                $scope.intygProperties.isLoaded = true;
                $scope.intygProperties.isSent = intygProperties.isSent;
                $scope.intygProperties.isRevoked = intygProperties.isRevoked;
            });
            $scope.$on('$destroy', unbindFastEvent);
            
            $scope.openArendenFilter = function(arende) {
                return true;
            };
/*

            // init state

            $scope.dismissSentMessage = function() {
                $scope.widgetState.sentMessage = false;
            };

            var decorateWithGUIParameters = function(list) {
                // answerDisabled
                // answerButtonToolTip
                angular.forEach(list, function(qa) {
                    fragaSvarCommonService.decorateSingleItem(qa);
                });
            };

            $scope.openIssuesFilter = function(qa) {
                return (qa.proxyMessage === undefined && qa.status !== 'CLOSED') ||
                    (qa.proxyMessage !== undefined && qa.messageStatus !== 'CLOSED' && qa.messageStatus !== 'HIDDEN');

            };

            $scope.closedIssuesFilter = function(qa) {
                return (qa.proxyMessage === undefined && qa.status === 'CLOSED') ||
                    (qa.proxyMessage !== undefined && qa.messageStatus === 'CLOSED');
            };

            // listeners - interscope communication
            var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent', function($event, deferred, unhandledQas) {
                qaHelper.updateAnsweredAsHandled(deferred, unhandledQas, true);
            });

            $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);

            var unbindHasUnhandledQasEvent = $scope.$on('hasUnhandledQasEvent', function($event, deferred) {
                deferred.resolve(fragaSvarCommonService.getUnhandledQas($scope.qaList));
            });

            $scope.$on('$destroy', unbindHasUnhandledQasEvent);
*/
        }]);
