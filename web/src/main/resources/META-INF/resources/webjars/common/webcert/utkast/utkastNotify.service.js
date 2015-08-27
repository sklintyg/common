/**
 * Utkast Notify Service Module - Functions related to
 * sending notifications of utkast to a doctor via mail.
 */
angular.module('common').factory('common.UtkastNotifyService',
    ['$http', '$log', '$modal', '$window', '$timeout', '$q', 'common.UtkastNotifyProxy', 'common.messageService',
        'common.dialogService',
        function($http, $log, $modal, $window, $timeout, $q, utkastNotifyProxy, messageService, dialogService) {
            'use strict';

            function _notifyUtkast(intygId, intygType, utkast, updateState) {
                var utkastNotifyRequest = {
                    intygId: intygId,
                    intygType: intygType,
                    intygVersion: utkast.version,
                    vidarebefordradContainer: utkast,
                    enhetsNamn : utkast.enhetsNamn,
                    vardgivareNamn : utkast.vardgivareNamn
                };
                notifyUtkast(utkastNotifyRequest, updateState).then(function(vidarebefordradResult) {
                    onNotifyChangeSuccess(utkast, updateState, vidarebefordradResult);
                }, function(error) {
                    onNotifyChangeFail(utkast, updateState, error);
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
                        'Det verkar som att du har informerat den som ska signera utkastet. Vill du markera utkastet som vidarebefordrad?',
                        function() { // yes
                            $log.debug('yes');
                            draft.vidarebefordradContainer.vidarebefordrad = true;
                            if (onYesCallback) {
                                // let calling scope handle yes answer
                                onYesCallback(draft);
                            }
                        },
                        function() { // no
                            $log.debug('no');
                            // Do nothing
                            onRejectCallback();
                        },
                        function() {
                            $log.debug('no and dont ask');
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

                var DialogInstanceCtrl = function($scope, $modalInstance, title, bodyText, yesCallback, noCallback,
                    noDontAskCallback) {
                    $scope.title = title;
                    $scope.bodyText = bodyText;
                    $scope.noDontAskVisible = noDontAskCallback !== undefined;
                    $scope.yes = function(result) {
                        yesCallback();
                        $modalInstance.close(result);
                    };
                    $scope.no = function() {
                        noCallback();
                        $modalInstance.close('cancel');
                    };
                    $scope.noDontAsk = function() {
                        noDontAskCallback();
                        $modalInstance.close('cancel_dont_ask_again');
                    };
                };

                var msgbox = $modal.open({
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
                var url = baseURL + '/web/dashboard#/' + intygType + '/edit/' + intygId;
                var recipient = '';
                var subject = 'Du har blivit tilldelad ett ej signerat utkast i Webcert på enhet ' + enhetsNamn + ' for vardgivare ' + vardgivareNamn; //'Du har blivit tilldelad ett ej signerat utkast i Webcert på enhet ' + enhetsNamn;
                var body = 'Klicka pa lanken for att ga till utkastet:\n' + url;
                var link = 'mailto:' + recipient + '?subject=' + encodeURIComponent(subject) + '&body=' +
                    encodeURIComponent(body);
                $log.debug(link);
                return link;
            }

            // Return public API for the service
            return {
                notifyUtkast: _notifyUtkast,
                onNotifyChange: _onNotifyChange,
                showNotifyPreferenceDialog: _showNotifyPreferenceDialog,
                buildNotifyDoctorMailToLink: _buildNotifyDoctorMailToLink
            };
        }]);