angular.module('common').controller('common.ArendeCtrl',
    [ '$log', '$rootScope', '$state', '$stateParams', '$scope', '$timeout', '$window', '$filter', 'common.dialogService',
        'common.ArendeProxy',/*, 'common.fragaSvarCommonService', 'common.statService',
        'common.UserModel', 'common.fragaSvarHelper'*/ 'common.IntygViewStateService',
        function($log, $rootScope, $state, $stateParams, $scope, $timeout, $window, $filter, dialogService, ArendeProxy,
            /* fragaSvarCommonService, statService, UserModel, qaHelper*/ CommonViewState) {
            'use strict';

            // Injecting the CommonViewState service so client-side only changes on the intyg page (such as a send/revoke)
            // can trigger GUI updates in the Q&A view.
            $scope.viewState = {
                common: CommonViewState,
                doneLoading: false,
                activeErrorMessageKey: null,
                newQuestionOpen: false,
                sentMessage: false,
                focusQuestion: false,
                showTemplate: true
            };

            $scope.arendeList = [];
            var intygType = $state.current.data.intygType;

            // Request loading of arendes for this intyg
            ArendeProxy.getArenden($stateParams.certificateId, intygType, function(result) {
                $log.debug('getArendeForintygificate:success data:' + result);
                $scope.viewState.doneLoading = true;
                $scope.viewState.activeErrorMessageKey = null;
                $scope.arendeList = result;

                // Tell viewintygctrl about the intyg in case intyg load fails
/*                if (result.length > 0) {
                    // Verkar inte finnas någon lyssnare på detta event
                    $rootScope.$emit('ArendeCtrl.load.complete', result[0].intygsReferens);
                }*/
            }, function(errorData) {
                // show error view
                $scope.viewState.doneLoading = true;
                $scope.viewState.activeErrorMessageKey = errorData.errorCode;
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
