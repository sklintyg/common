angular.module('common').controller('common.ArendeCtrl',
    [ '$log', '$rootScope', '$state', '$stateParams', '$scope', '$timeout', '$window', '$filter', 'common.dialogService',
        'common.ArendeProxy',/*, 'common.fragaSvarCommonService', 'common.statService',
        'common.UserModel', 'common.fragaSvarHelper'*/ 'common.IntygViewStateService',
        function($log, $rootScope, $state, $stateParams, $scope, $timeout, $window, $filter, dialogService, ArendeProxy,
            /* fragaSvarCommonService, statService, UserModel, qaHelper*/ CommonViewState) {
            'use strict';

            // Injecting the CommonViewState service so client-side only changes on the cert page (such as a send/revoke)
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
                $log.debug('getArendeForCertificate:success data:' + result);
                $scope.viewState.doneLoading = true;
                $scope.viewState.activeErrorMessageKey = null;
                $scope.arendeList = result;

                // Tell viewcertctrl about the intyg in case cert load fails
/*                if (result.length > 0) {
                    // Verkar inte finnas någon lyssnare på detta event
                    $rootScope.$emit('ArendeCtrl.load.complete', result[0].intygsReferens);
                }*/
            }, function(errorData) {
                // show error view
                $scope.viewState.doneLoading = true;
                $scope.viewState.activeErrorMessageKey = errorData.errorCode;
            });

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

            $scope.cert = {};
            $scope.certProperties = {
                isLoaded: false,
                isSent: false,
                isRevoked: false
            };

            var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', function(event, cert, certProperties) {
                $scope.cert = cert;
                $scope.certProperties.isLoaded = true;
                $scope.certProperties.isSent = certProperties.isSent;
                $scope.certProperties.isRevoked = certProperties.isRevoked;
            });
            $scope.$on('$destroy', unbindFastEvent);

            $scope.openIssuesFilter = function(qa) {
                return (qa.proxyMessage === undefined && qa.status !== 'CLOSED') ||
                    (qa.proxyMessage !== undefined && qa.messageStatus !== 'CLOSED' && qa.messageStatus !== 'HIDDEN');

            };

            $scope.closedIssuesFilter = function(qa) {
                return (qa.proxyMessage === undefined && qa.status === 'CLOSED') ||
                    (qa.proxyMessage !== undefined && qa.messageStatus === 'CLOSED');
            };

            $scope.toggleQuestionForm = function() {
                $scope.widgetState.newQuestionOpen = !$scope.widgetState.newQuestionOpen;
                $scope.initQuestionForm();
                // hide sent message
                $scope.widgetState.sentMessage = false;
                $scope.widgetState.focusQuestion = true;
            };

            $scope.sendQuestion = function (newQuestion) {
                $log.debug('sendQuestion:' + newQuestion);
                newQuestion.updateInProgress = true; // trigger local spinner
                $scope.widgetState.sentMessage = false;

                fragaSvarService.saveNewQuestion($stateParams.certificateId, intygType, newQuestion,
                    function(result) {
                        $log.debug('Got saveNewQuestion result:' + result);
                        newQuestion.updateInProgress = false;
                        newQuestion.activeErrorMessageKey = null;
                        if (result !== null) {
                            fragaSvarCommonService.decorateSingleItem(result);
                            // result is a new FragaSvar Instance: add it to our local repo
                            $scope.qaList.push(result);
                            //$scope.activeQA = result.internReferens;
                            // close question form
                            $scope.toggleQuestionForm();
                            // show sent message
                            $scope.widgetState.sentMessage = true;
                            statService.refreshStat();
                        }
                    }, function(errorData) {
                        // show error view
                        newQuestion.updateInProgress = false;
                        newQuestion.activeErrorMessageKey = errorData.errorCode;
                    });
            };

            $scope.questionValidForSubmit = function(newQuestion) {
                var validToSend = newQuestion.chosenTopic.value && newQuestion.frageText &&
                    !newQuestion.updateInProgress;
                if (!validToSend) {
                    newQuestion.sendButtonToolTip =
                        'Du måste välja ett ämne och skriva en frågetext innan du kan skicka frågan';
                } else {
                    newQuestion.sendButtonToolTip = 'Skicka frågan';
                }
                return validToSend;
            };

            $scope.initQuestionForm = function() {
                // Topics are defined under RE-13
                $scope.newQuestion = {
                    topics: [
                        {
                            label: 'Välj ämne',
                            value: null
                        },
                        {
                            label: 'Arbetstidsförläggning',
                            value: 'ARBETSTIDSFORLAGGNING'
                        },
                        {
                            label: 'Avstämningsmöte',
                            value: 'AVSTAMNINGSMOTE'
                        },
                        {
                            label: 'Kontakt',
                            value: 'KONTAKT'
                        },
                        {
                            label: 'Övrigt',
                            value: 'OVRIGT'
                        }
                    ],
                    frageText: ''
                };
                $scope.newQuestion.chosenTopic = $scope.newQuestion.topics[0]; // 'Välj ämne' is default
            };
            $scope.initQuestionForm();

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
