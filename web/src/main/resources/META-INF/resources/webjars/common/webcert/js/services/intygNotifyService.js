/**
 * Utkast Notify Service Module - Functions related to
 * sending notifications of utkast to a doctor via mail.
 * TODO: rename this to utkastNotifyService. It has nothing to do with intyg.
 */
angular.module('common').factory('common.intygNotifyService',
    ['$http', '$log', '$modal', '$window', '$timeout', 'common.dialogService', 'common.messageService', 'common.UtkastViewStateService',
        function($http, $log, $modal, $window, $timeout, dialogService, messageService, CommonViewState) {
            'use strict';

            function _notifyUtkast(notifyRequest) {
                $timeout(function() {
                    _handleNotifyToggle(notifyRequest, function() {
                        _onNotifyChange(notifyRequest);
                    });
                }, 1000);
                // Launch mail client
                $window.location = _buildNotifyDoctorMailToLink(notifyRequest.intygId, notifyRequest.intygType);
            }

            function _onNotifyChange(notifyRequest) {
                CommonViewState.vidarebefordraInProgress = true;
                _setNotifyState(notifyRequest.intygId, notifyRequest.intygType, notifyRequest.intygVersion,
                    notifyRequest.vidarebefordrad, function(result) {
                    CommonViewState.vidarebefordraInProgress = false;

                    if (result !== null) {
                        if (notifyRequest.updateFunc) {
                            notifyRequest.updateFunc(result.vidarebefordrad, result.version);
                        }
                    } else {
                        if (notifyRequest.updateFunc) {
                            notifyRequest.updateFunc(!notifyRequest.vidarebefordrad);
                        }
                        dialogService.showErrorMessageDialog('Kunde inte markera/avmarkera intyget som ' +
                        'vidarebefordrat. Försök gärna igen för att se om felet är tillfälligt. Annars kan ' +
                        'du kontakta supporten. Läs mer under Om webcert | Support och kontaktinformation.');
                    }
                },
                function(error) {
                    if (notifyRequest.updateFunc) {
                        notifyRequest.updateFunc(!notifyRequest.vidarebefordrad);
                    }
                    var errorMessage = 'Kunde inte markera/avmarkera intyget som ' +
                        'vidarebefordrat. Försök gärna igen för att se om felet är tillfälligt. Annars kan ' +
                        'du kontakta supporten. Läs mer under Om webcert | Support och kontaktinformation.';

                    if (error && error.errorCode === 'CONCURRENT_MODIFICATION') {
                        var errorMessageId = 'common.error.save.concurrent_modification';
                        errorMessage = messageService.getProperty(errorMessageId, {name: error.message}, errorMessageId);
                    }

                    dialogService.showErrorMessageDialog(errorMessage);
                });
            }

            /*
             * Toggle Notify state of a fragasvar entity with given id
             */
            function _setNotifyState(intygId, intygType, intygVersion, isNotified, callback, errorCallback) {
                $log.debug('_setNotifyState');
                var restPath = '/api/intyg/' + intygType + '/' + intygId + '/' + intygVersion + '/vidarebefordra';
                $http.put(restPath, isNotified.toString()).success(function(data) {
                    $log.debug('_setNotifyState data:' + data);
                    callback(data);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    errorCallback(data);
                });
            }

            function _buildNotifyDoctorMailToLink(intygId, intygType) {
                var baseURL = $window.location.protocol + '//' + $window.location.hostname +
                    ($window.location.port ? ':' + $window.location.port : '');
                var url = baseURL + '/web/dashboard#/' + intygType + '/edit/' + intygId;
                var recipient = '';
                var subject = 'Du har blivit tilldelad ett ej signerat utkast i Webcert';
                var body = 'Klicka pa lanken for att ga till utkastet:\n' + url;
                var link = 'mailto:' + recipient + '?subject=' + encodeURIComponent(subject) + '&body=' +
                    encodeURIComponent(body);
                $log.debug(link);
                return link;
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
            function _handleNotifyToggle(draft, onYesCallback) {
                // Only ask about toggle if not already set AND not skipFlag cookie is
                // set
                if (!draft.vidarebefordrad && !_isSkipNotifyCookieSet()) {
                    _showNotifyPreferenceDialog('markforward',
                        'Det verkar som att du har informerat den som ska signera utkastet. Vill du markera utkastet som vidarebefordrad?',
                        function() { // yes
                            $log.debug('yes');
                            draft.vidarebefordrad = true;
                            if (onYesCallback) {
                                // let calling scope handle yes answer
                                onYesCallback(draft);
                            }
                        },
                        function() { // no
                            $log.debug('no');
                            // Do nothing
                        },
                        function() {
                            $log.debug('no and dont ask');
                            // How can user reset this?
                            _setSkipNotifyCookie();
                        }
                    );
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
                    templateUrl: '/views/partials/preference-dialog.html',
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

            // Return public API for the service
            return {
                forwardIntyg: _notifyUtkast,
                onForwardedChange: _onNotifyChange,
                setForwardedState: _setNotifyState,
                showForwardedPreferenceDialog: _showNotifyPreferenceDialog,
                buildNotifyDoctorMailToLink: _buildNotifyDoctorMailToLink
            };
        }]);