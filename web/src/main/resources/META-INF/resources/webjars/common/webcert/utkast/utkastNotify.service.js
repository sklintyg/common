/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
/**
 * Utkast Notify Service Module - Functions related to
 * sending notifications of utkast to a doctor via mail.
 */
angular.module('common').factory('common.UtkastNotifyService',
    ['$http', '$log', '$uibModal', '$window', '$timeout', '$q', 'common.UtkastNotifyProxy', 'common.messageService',
        'common.dialogService', 'common.UtkastProxy', 'common.UtilsService',
        function($http, $log, $uibModal, $window, $timeout, $q, utkastNotifyProxy, messageService, dialogService,
            utkastProxy, utilsService) {
            'use strict';

            /**
             * Performs an extra REST call to fetch the Utkast so we get hold of the enhets- and vardgivare names.
             */
            function _notifyUtkast(intygId, intygType, utkast, updateState) {
                // Fetch DraftModel to get hold of enhetsNamn and vardgivareNamn
                utkastProxy.getUtkast(intygId, intygType, function(draft) {
                        var utkastNotifyRequest = {
                            intygId: intygId,
                            intygType: intygType,
                            intygVersion: utkast.version,
                            vidarebefordradContainer: utkast,
                            enhetsNamn : draft.enhetsNamn,
                            vardgivareNamn : draft.vardgivareNamn
                        };
                        notifyUtkast(utkastNotifyRequest, updateState).then(function(vidarebefordradResult) {
                            onNotifyChangeSuccess(utkast, updateState, vidarebefordradResult);
                        }, function(error) {
                            // Update vidarebefordrad to true failed, reset gui to previous value
                            utkast.vidarebefordrad = draft.vidarebefordrad;
                            onNotifyChangeFail(utkast, updateState, error);
                        });
                },
                function() {
                    var errorMessage = 'Kunde inte öppna e-postprogram för vidarebefordran av Utkast för signering. ' +
                        'Utkastet kunde inte läsas upp. Försök gärna igen för att se om felet är tillfälligt. Annars kan ' +
                        'du kontakta supporten. Läs mer under Om webcert | Support och kontaktinformation.';
                    dialogService.showErrorMessageDialog(errorMessage);
                });
            }

            function _onNotifyChange(intygId, intygType, utkast, updateState) {
                updateState.vidarebefordraInProgress = true;

                var utkastNotifyRequest = {
                    intygId: intygId,
                    intygType: intygType,
                    intygVersion: utkast.version,
                    vidarebefordradContainer: utkast
                };

                var deferred = $q.defer();
                onNotifyChange(utkastNotifyRequest, deferred).then(function(vidarebefordradResult) {
                    onNotifyChangeSuccess(utkast, updateState, vidarebefordradResult);
                }, function(error) {
                    utkast.vidarebefordrad = !utkast.vidarebefordrad;
                    onNotifyChangeFail(utkast, updateState, error);
                });
            }

            function onNotifyChangeSuccess(utkast, updateState, vidarebefordradResult) {
                updateState.vidarebefordraInProgress = false;
                if(vidarebefordradResult !== null) {
                    utkast.vidarebefordrad = vidarebefordradResult.vidarebefordrad;
                    utkast.version = vidarebefordradResult.version;
                }
            }

            function onNotifyChangeFail(utkast, updateState, error) {
                updateState.vidarebefordraInProgress = false;
                var errorMessage = 'Kunde inte markera/avmarkera intyget som ' +
                    'vidarebefordrat. Försök gärna igen för att se om felet är tillfälligt. Annars kan ' +
                    'du kontakta supporten. Läs mer under Om webcert | Support och kontaktinformation.';

                if (error && error.errorCode === 'CONCURRENT_MODIFICATION') {
                    var errorMessageId = 'common.error.save.concurrent_modification';
                    errorMessage = messageService.getProperty(errorMessageId, {name: error.message}, errorMessageId);
                }

                dialogService.showErrorMessageDialog(errorMessage);
            }

            function notifyUtkast(notifyRequest, updateState) {
                var deferred = $q.defer();
                $timeout(function() {
                    _handleNotifyToggle(notifyRequest, function() {
                        updateState.vidarebefordraInProgress = true;
                        onNotifyChange(notifyRequest, deferred);
                    }, function() {
                        deferred.resolve(null); // User didn't want to do anything or dialog wasn't even shown
                    });
                }, 1000);
                // Launch mail client
                $window.location = _buildNotifyDoctorMailToLink(
                    notifyRequest.intygId, notifyRequest.intygType,
                    notifyRequest.enhetsNamn, notifyRequest.vardgivareNamn);
                return deferred.promise;
            }

            function onNotifyChange(notifyRequest, deferred) {
                utkastNotifyProxy.setNotifyState(notifyRequest.intygId, notifyRequest.intygType, notifyRequest.intygVersion,
                    notifyRequest.vidarebefordradContainer.vidarebefordrad, function(result) {

                    if (result !== null) {
                        deferred.resolve({
                            vidarebefordrad: result.vidarebefordrad,
                            version: result.version
                        });
                    } else {
                        deferred.reject();
                    }
                }, function(error) {
                    deferred.reject(error);
                });

                return deferred.promise;
            }

            function _setSkipNotifyCookie() {
                var secsDays = 12 * 30 * 24 * 3600 * 1000; // 1 year
                var now = new Date();
                var expires = new Date(now.getTime() + secsDays);
                document.cookie = 'WCDontAskForNotifyToggle=1; expires=' + expires.toUTCString();
            }

            function _isSkipNotifyCookieSet() {
                return document.cookie && document.cookie.indexOf('WCDontAskForNotifyToggle=1') !== -1;
            }

            // This handles forwarding of utkast only
            function _handleNotifyToggle(draft, onYesCallback, onRejectCallback) {
                // Only ask about toggle if not already set AND not skipFlag cookie is
                // set
                if (!draft.vidarebefordradContainer.vidarebefordrad && !_isSkipNotifyCookieSet()) {
                    _showNotifyPreferenceDialog('markforward',
                        '\tVill du markera utkastet som vidarebefordrat?',
                        function(dontShowAgain) { // yes
                            $log.debug('yes');
                            draft.vidarebefordradContainer.vidarebefordrad = true;
                            if (onYesCallback) {
                                // let calling scope handle yes answer
                                onYesCallback(draft);
                            }
                            if (dontShowAgain) {
                                _setSkipNotifyCookie();
                            }
                        },
                        function(dontShowAgain) { // no
                            $log.debug('no');
                            // Do nothing
                            onRejectCallback();
                            if (dontShowAgain) {
                                _setSkipNotifyCookie();
                            }
                        },
                        function() {
                            $log.debug('dont ask again');
                            // How can user reset this?
                            _setSkipNotifyCookie();
                            onRejectCallback();
                        }
                    );
                } else {
                    onRejectCallback();
                }
            }

            function _showNotifyPreferenceDialog(title, bodyText, yesCallback, noCallback, noDontAskCallback,
                callback) {

                var DialogInstanceCtrl = function($scope, $uibModalInstance, title, bodyText, yesCallback, noCallback,
                    noDontAskCallback) {
                    $scope.title = title;
                    $scope.bodyText = bodyText;
                    $scope.noDontAskVisible = noDontAskCallback !== undefined;
                    $scope.model = {
                        dontShowAgain: false
                    };
                    $scope.yes = function(dontShowAgain) {
                        yesCallback(dontShowAgain);
                        $uibModalInstance.close(dontShowAgain);
                    };
                    $scope.no = function(dontShowAgain) {
                        noCallback(dontShowAgain);
                        $uibModalInstance.close('cancel');
                    };
                    $scope.noDontAsk = function() {
                        noDontAskCallback();
                        $uibModalInstance.close('cancel_dont_ask_again');
                    };
                };

                var msgbox = $uibModal.open({
                    templateUrl: '/app/partials/preference-dialog.html',
                    controller: DialogInstanceCtrl,
                    resolve: {
                        title: function() {
                            return angular.copy(title);
                        },
                        bodyText: function() {
                            return angular.copy(bodyText);
                        },
                        yesCallback: function() {
                            return yesCallback;
                        },
                        noCallback: function() {
                            return noCallback;
                        },
                        noDontAskCallback: function() {
                            return noDontAskCallback;
                        }
                    }
                });

                msgbox.result.then(function(result) {
                    if (callback) {
                        callback(result);
                    }
                }, function() {
                });
            }

            function _buildNotifyDoctorMailToLink(intygId, intygType, enhetsNamn, vardgivareNamn) {
                var baseURL = $window.location.protocol + '//' + $window.location.hostname +
                    ($window.location.port ? ':' + $window.location.port : '');
                var url = baseURL + '/web/maillink/intyg/' + intygType + '/' + intygId;
                var recipient = '';
                var subject = 'Du har blivit tilldelad ett ej signerat utkast i Webcert på enhet ' +
                    enhetsNamn + ' för vårdgivare ' + vardgivareNamn;
                var body = 'Klicka på länken för att gå till utkastet:\n' + url + '\n\nOBS! Sätt i ditt SITHS-kort innan du klickar på länken.';
                var link = 'mailto:' + recipient + '?subject=' + encodeURIComponent(subject) + '&body=' +
                    encodeURIComponent(body);
                $log.debug(link);
                return link;
            }

            // Notifiering till journalsystem hanteras i backend
            function _notifyJournalsystem(intygId, intygType, utkast, updateState, successCallback, errorCallback) {

                if (!updateState.intyg.isComplete) {
                    _showNotifyJournalsystemDialog('notifyjournalsystem',
                        messageService.getProperty('common.modal.marked.ready.notification.sent'),
                        function() { // Send notification, e.g. "yes"
                            utkastNotifyProxy.sendNotificationStatusUpdate(intygId, intygType, utkast,
                                function() {
                                    // The callback should update the viewstate.
                                    successCallback();
                                }, function() {
                                    $log.error('Send notification failed!');

                                    if (angular.isFunction(errorCallback)) {
                                        errorCallback();
                                    }
                                });
                        },
                        function() { // no
                            $log.debug('no');
                            if (angular.isFunction(errorCallback)) {
                                errorCallback();
                            }
                        }
                    );
                } else {
                     utkastNotifyProxy.sendNotificationStatusUpdate(intygId, intygType, utkast,
                         function() {
                             $log.debug('Send notification success!');
                             successCallback();
                         }, function(err) {
                             $log.debug('Send notification failed!');

                             if (angular.isFunction(errorCallback)) {
                                 errorCallback();
                             }
                         });
                }
            }


            function _showNotifyJournalsystemDialog(title, bodyText, yesCallback, noCallback,
                callback) {
                var DialogInstanceCtrl = function($scope, $uibModalInstance, title, bodyText, yesCallback, noCallback
                    ) {
                    $scope.title = title;
                    $scope.bodyText = bodyText;
                    $scope.yes = function(result) {
                        yesCallback();
                        $uibModalInstance.close(result);
                    };
                    $scope.no = function() {
                        noCallback();
                        $uibModalInstance.close('cancel');
                    };
                };

                var msgbox = $uibModal.open({
                    templateUrl: '/app/partials/notifiering-journalsystem-dialog.html',
                    controller: DialogInstanceCtrl,
                    resolve: {
                        title: function() {
                            return angular.copy(title);
                        },
                        bodyText: function() {
                            return angular.copy(bodyText);
                        },
                        yesCallback: function() {
                            return yesCallback;
                        },
                        noCallback: function() {
                            return noCallback;
                        }
                    }
                });

                msgbox.result.then(function(result) {
                    if (callback) {
                        callback(result);
                    }
                }, function() {
                });
            }



            // Return public API for the service
            return {
                notifyUtkast: _notifyUtkast,
                onNotifyChange: _onNotifyChange,
                showNotifyPreferenceDialog: _showNotifyPreferenceDialog,
                buildNotifyDoctorMailToLink: _buildNotifyDoctorMailToLink,
                notifyJournalsystem: _notifyJournalsystem
            };
        }]);
