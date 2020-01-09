/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
angular.module('common').service('common.ArendeVidarebefordraHelper',
    ['$log', '$window', '$uibModal', 'common.UserModel', 'common.UtilsService',
        function ($log, $window, $uibModal, UserModel, UtilsService) {
            'use strict';

            this.buildMailToLink = function(arendeMailModel) {
                var baseURL = $window.location.protocol + '//' + $window.location.hostname +
                    ($window.location.port ? ':' + $window.location.port : '');
                var certificateUrlPart = UserModel.isUthopp() ? 'certificate' : 'basic-certificate';

                if(typeof arendeMailModel.intygId === 'undefined') {
                    $log.error('Invalid intyg id. Cannot create vidarebefordra link');
                    return 'error';
                }
                var url = baseURL + '/webcert/web/user/' + certificateUrlPart + '/' + arendeMailModel.intygType + '/' + arendeMailModel.intygId + '/questions';

                var recipient = '';
                var subject = 'Ett ärende ska hanteras i Webcert';
                var vardenhet = UserModel.user.valdVardenhet;
                var vardgivare = UserModel.user.valdVardgivare;
                if (vardenhet.namn !== undefined) {
                    subject += ' på enhet ' + vardenhet.namn;
                    if (vardgivare.namn !== undefined) {
                        subject += ' för vårdgivare ' + vardgivare.namn;
                    }
                }

                var body = 'Klicka på länken för att hantera ärendet:\n' + url + '\n\nOBS! Sätt i ditt SITHS-kort innan du klickar på länken.';
                var link = 'mailto:' + recipient + '?subject=' +
                    encodeURIComponent(subject) + '&body=' +
                    encodeURIComponent(body);
                $log.debug(link);
                return link;
            };

            this.handleVidareBefordradToggle = function (onYesCallback) {
                // Only ask about toggle if not already set AND not skipFlag cookie is
                // set
                if (!_isSkipVidareBefordradCookieSet()) {
                    this.showVidarebefordradPreferenceDialog(
                        'markforward',
                        'Vill du markera ärendet som vidarebefordrat?',
                        function() { // yes
                            $log.debug('yes');
                            if (onYesCallback) {
                                // let calling scope handle yes answer
                                onYesCallback();
                            }
                        },
                        function() { // no
                            $log.debug('no');
                            // Do nothing
                        },
                        function() {
                            $log.debug('no and dont ask');
                            // How can user reset this?
                            _setSkipVidareBefordradCookie();
                        }
                    );
                }
            };

            this.showVidarebefordradPreferenceDialog = function(title, bodyText, yesCallback, noCallback, noDontAskCallback,
                callback) {

                var DialogInstanceCtrl = function($scope, $uibModalInstance, title, bodyText, yesCallback, noCallback,
                    noDontAskCallback) {
                    $scope.title = title;
                    $scope.bodyText = bodyText;
                    $scope.noDontAskVisible = noDontAskCallback !== undefined;
                    $scope.yes = function(result) {
                        yesCallback();
                        $uibModalInstance.close(result);
                    };
                    $scope.no = function() {
                        noCallback();
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
            };

            function _setSkipVidareBefordradCookie() {
                var secsDays = 12 * 30 * 24 * 3600 * 1000; // 1 year
                var now = new Date();
                var expires = new Date(now.getTime() + secsDays);
                document.cookie = 'WCDontAskForVidareBefordradToggle=1; expires=' + expires.toUTCString();

            }

            function _isSkipVidareBefordradCookieSet() {
                return (document.cookie && document.cookie.indexOf('WCDontAskForVidareBefordradToggle=1') !== -1);
            }

        }]);
