/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

            this.buildMailToLink = function(arende) {
                var baseURL = $window.location.protocol + '//' + $window.location.hostname +
                    ($window.location.port ? ':' + $window.location.port : '');
                var certificateUrlPart = UserModel.isUthopp() ? 'certificate/' : 'basic-certificate/';

                if(typeof arende.fraga.intygsReferens === 'undefined' || typeof arende.fraga.intygsReferens.intygsId === 'undefined') {
                    $log.error('Invalid intyg id. Cannot create vidarebefordra link');
                    return 'error';
                }
                var url = baseURL + '/webcert/web/user/' + certificateUrlPart + arende.fraga.intygsReferens.intygsId + '/questions';

                var recipient = '';
                var subject = 'En fraga-svar ska besvaras i Webcert';
                if (arende.fraga.enhetsnamn !== undefined) {
                    subject += ' pa enhet ' + arende.fraga.enhetsnamn;
                    if (arende.fraga.vardgivarnamn !== undefined) {
                        subject += ' for vardgivare ' + arende.fraga.vardgivarnamn;
                    }
                }

                var body = 'Klicka pa lanktexten for att besvara fraga-svar:\n' + url;
                var link = 'mailto:' + recipient + '?subject=' +
                    encodeURIComponent(UtilsService.replaceAccentedCharacters(subject)) + '&body=' +
                    encodeURIComponent(body);
                $log.debug(link);
                return link;
            };

            this.handleVidareBefodradToggle = function (arende, onYesCallback) {
                // Only ask about toggle if not already set AND not skipFlag cookie is
                // set
                if (!arende.fraga.vidarebefordrad && !_isSkipVidareBefodradCookieSet()) {
                    this.showVidarebefordradPreferenceDialog(
                        'markforward',
                        'Det verkar som att du har informerat den som ska hantera ärendet. Vill du markera ärendet som vidarebefordrat?',
                        function() { // yes
                            $log.debug('yes');
                            arende.fraga.vidarebefordrad = true;
                            if (onYesCallback) {
                                // let calling scope handle yes answer
                                onYesCallback(arende);
                            }
                        },
                        function() { // no
                            $log.debug('no');
                            // Do nothing
                        },
                        function() {
                            $log.debug('no and dont ask');
                            // How can user reset this?
                            _setSkipVidareBefodradCookie();
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

            function _setSkipVidareBefodradCookie() {
                var secsDays = 12 * 30 * 24 * 3600 * 1000; // 1 year
                var now = new Date();
                var expires = new Date(now.getTime() + secsDays);
                document.cookie = 'WCDontAskForVidareBefordradToggle=1; expires=' + expires.toUTCString();

            }

            function _isSkipVidareBefodradCookieSet() {
                return (document.cookie && document.cookie.indexOf('WCDontAskForVidareBefordradToggle=1') !== -1);
            }

        }]);
