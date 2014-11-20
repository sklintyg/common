/**
 * Intyg Notify Service Module - Functions related to
 * forwarding notifications of intyg to a doctor via mail.
 */
angular.module('common').factory('common.intygNotifyService', ['$http', '$log', '$modal', '$window', '$timeout', 'common.dialogService',
    function($http, $log, $modal, $window, $timeout, dialogService) {
        'use strict';

        function _forwardIntyg(certMeta, widgetState) {
            $timeout(function() {
                _handleForwardedToggle(certMeta, function() {
                    _onForwardedChange(certMeta, widgetState)
                });
            }, 1000);
            // Launch mail client
            $window.location = _buildNotifyDoctorMailToLink(certMeta.intygId, certMeta.intygType);
        }

        function _onForwardedChange(certMeta, widgetState) {
            widgetState.vidarebefordraInProgress = true;
            _setForwardedState(certMeta.intygId, certMeta.intygType, certMeta.vidarebefordrad, function(result) {
                widgetState.vidarebefordraInProgress = false;

                if (result !== null) {
                    certMeta.vidarebefordrad = result.vidarebefordrad;
                } else {
                    certMeta.vidarebefordrad = !certMeta.vidarebefordrad;
                    dialogService.showErrorMessageDialog('Kunde inte markera/avmarkera intyget som ' +
                        'vidarebefordrat. Försök gärna igen för att se om felet är tillfälligt. Annars kan ' +
                        'du kontakta supporten. Läs mer under Om webcert | Support och kontaktinformation.');
                }
            });
        }

        /*
         * Toggle Forwarded state of a fragasvar entity with given id
         */
        function _setForwardedState(intygId, intygType, isForwarded, callback) {
            $log.debug('_setForwardedState');
            var restPath = '/api/intyg/' + intygType + '/' + intygId + '/vidarebefordra';
            $http.put(restPath, isForwarded.toString()).success(function(data) {
                $log.debug('_setForwardedState data:' + data);
                callback(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                callback(null);
            });
        }

        function _buildNotifyDoctorMailToLink(intygId, intygType) {
            var baseURL = $window.location.protocol + '//' + $window.location.hostname +
                ($window.location.port ? ':' + $window.location.port : '');
            var url = baseURL + '/web/dashboard#/' + intygType + '/edit/' + intygId;
            var recipient = '';
            var subject = 'Du har blivit tilldelad ett ej signerat intyg i Webcert';
            var body = 'Klicka pa lanken for att ga till intyget:\n' + url;
            var link = 'mailto:' + recipient + '?subject=' + encodeURIComponent(subject) + '&body=' +
                encodeURIComponent(body);
            $log.debug(link);
            return link;
        }

        function _setSkipForwardedCookie() {
            var secsDays = 12 * 30 * 24 * 3600 * 1000; // 1 year
            var now = new Date();
            var expires = new Date(now.getTime() + secsDays);
            document.cookie = 'WCDontAskForForwardedToggle=1; expires=' + expires.toUTCString();
        }

        function _isSkipForwardedCookieSet() {
            return document.cookie && document.cookie.indexOf('WCDontAskForForwardedToggle=1') !== -1;
        }

        // This handles forwarding of Ej signerade utkast only
        function _handleForwardedToggle(draft, onYesCallback) {
            // Only ask about toggle if not already set AND not skipFlag cookie is
            // set
            if (!draft.vidarebefordrad && !_isSkipForwardedCookieSet()) {
                _showForwardedPreferenceDialog('markforward',
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
                        _setSkipForwardedCookie();
                    }
                );
            }
        }

        function _showForwardedPreferenceDialog(title, bodyText, yesCallback, noCallback, noDontAskCallback,
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
            forwardIntyg: _forwardIntyg,
            onForwardedChange: _onForwardedChange,
            setForwardedState: _setForwardedState,
            showForwardedPreferenceDialog: _showForwardedPreferenceDialog,
            buildNotifyDoctorMailToLink: _buildNotifyDoctorMailToLink
        };
    }]);