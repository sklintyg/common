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
/**
 * Common Fragasvar Module - Common services and controllers related to
 * FragaSvar functionality to be used in webcert and modules handling Fraga/svar
 * related to certificates. (As of this time, only fk7263 module)
 */
angular.module('common').factory('common.enhetArendenCommonService',
    ['$http', '$log', '$uibModal', '$window', 'common.dialogService', 'common.LocationUtilsService',
        'common.featureService', 'common.UserModel', 'common.UtilsService',
        function($http, $log, $uibModal, $window, dialogService, LocationUtilsService, featureService, UserModel,
            utilsService) {
            'use strict';

            /*
             * Toggle vidarebefordrad state of a fragasvar entity with given id
             */
            function _setVidareBefordradState(intygId, intygsTyp, isVidareBefordrad, callback) {
                $log.debug('_setVidareBefordradState');
                var restPath = '/moduleapi/arende/' + intygId + '/vidarebefordrad';
                $http.post(restPath).then(function(response) {
                    $log.debug('_setVidareBefordradState data:' + response.data);
                    callback(response.data);
                }, function(response) {
                    $log.error('error ' + response.status);
                    // Let calling code handle the error of no data response
                    callback(null);
                });
            }

            function _setSkipVidareBefordradCookie() {
                var secsDays = 12 * 30 * 24 * 3600 * 1000; // 1 year
                var now = new Date();
                var expires = new Date(now.getTime() + secsDays);
                document.cookie = 'WCDontAskForVidareBefordradToggle=1; expires=' + expires.toUTCString();

            }

            function _isSkipVidareBefordradCookieSet() {
                return (document.cookie && document.cookie.indexOf('WCDontAskForVidareBefordradToggle=1') !== -1);
            }

            function _isKomplettering(value) {
                return value === 'KOMPLETTERING_AV_LAKARINTYG' || value === 'KOMPLT';
            }

            function _isPaminnelse(value) {
                return value === 'PAMINNELSE' || value === 'PAMINN';
            }

            function _decorateSingleItem(qa) {

                var allowedToKomplettera = UserModel.hasPrivilege(UserModel.privileges.BESVARA_KOMPLETTERINGSFRAGA, undefined, false);

                if (_isPaminnelse(qa.amne)) {
                    // RE-020 Påminnelser is never
                    // answerable
                    qa.answerDisabled = true;
                    qa.answerDisabledReason = undefined; // Påminnelser kan inte besvaras men det behöver vi inte säga
                } else if (_isKomplettering(qa.amne) && !allowedToKomplettera) {
                    // If svaramednyttintygdisabled = true already then we aren't allowed to answer regardless of privilege

                    // RE-005, RE-006
                    qa.answerDisabled = true;
                    qa.answerDisabledReason = 'Kompletteringar kan endast besvaras av läkare.';
                } else {
                    qa.answerDisabled = false;
                    qa.answerDisabledReason = undefined;
                }

                _decorateSingleItemMeasure(qa);
            }

            function _decorateSingleItemMeasure(qa) {
                if (qa.status === 'CLOSED') {
                    qa.measureResKey = 'handled';
                } else if (_isUnhandledForDecoration(qa)) {
                    qa.measureResKey = 'markhandled.' + (_fromFk(qa) ? 'fk' : 'wc'); 
                } else if (_isKomplettering(qa.amne)) {
                    qa.measureResKey = 'komplettering';
                } else {
                    if (qa.status === 'PENDING_INTERNAL_ACTION') {
                        qa.measureResKey = 'svarfranvarden';
                    } else if (qa.status === 'PENDING_EXTERNAL_ACTION') {
                        qa.measureResKey = 'svarfranfk';
                    } else {
                        qa.measureResKey = '';
                        $log.debug('warning: undefined status');
                    }
                }
            }

            function _isUnhandledForDecoration(qa){
                if(!qa){
                    return false;
                }
                return qa.status === 'ANSWERED' || qa.amne === 'MAKULERING' || _isPaminnelse(qa.amne);
            }

            function _isUnhandled(qa){
                if(!qa){
                    return false;
                }
                return (qa.status === 'PENDING_INTERNAL_ACTION' && _isPaminnelse(qa.amne)) || qa.status === 'ANSWERED';
            }

            function _getUnhandledQas(qas){
                if(!qas || qas.length === 0){
                    return false;
                }
                var qasfiltered = [];
                for (var i = 0, len = qas.length; i < len; i++) {
                    var qa = qas[i];
                    var isUnhandled = _isUnhandled(qa);
                    var fromFk = _fromFk(qa);
                    if(qa.status === 'ANSWERED' || (isUnhandled && fromFk) ){
                        qasfiltered.push(qa);
                    }
                }
                return qasfiltered;
            }

            function _fromFk(qa){
                if(qa.frageStallare === 'FK'){
                    return true;
                }
                return false;
            }

            function _showVidarebefordradPreferenceDialog(title, bodyText, yesCallback, noCallback, noDontAskCallback,
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
            }

            function _handleVidareBefordradToggle(qa, onYesCallback) {
                // Only ask about toggle if not already set AND not skipFlag cookie is
                // set
                if (!qa.vidarebefordrad && !_isSkipVidareBefordradCookieSet()) {
                    _showVidarebefordradPreferenceDialog(
                        'markforward',
                        'Det verkar som att du har informerat den som ska hantera ärendet. Vill du markera ärendet som vidarebefordrat?',
                        function() { // yes
                            $log.debug('yes');
                            qa.vidarebefordrad = true;
                            if (onYesCallback) {
                                // let calling scope handle yes answer
                                onYesCallback(qa);
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
            }

            function isUthoppUser() {
                var uthoppUser = UserModel.isUthopp();
                $log.debug('Is uthopp user: ' + uthoppUser);
                return uthoppUser;
            }

            function isNavigatingAway(newUrl) {

                // The following urls are where the uthopp users are supposed to be
                var allowedNewUrls = [
                    '#/fragasvar/',
                    '#/enhet-arenden',
                    '#/webcert/about',
                    '#/support/about',
                    '#/certificates/about',
                    '#/faq/about',
                    '#/cookies/about'
                ];

                // If its none of them we are navigating outside of where we should
                var navigatingAway = true;
                angular.forEach(allowedNewUrls, function(url) {
                    if(newUrl.indexOf(url) !== -1) { // found
                        navigatingAway = false;
                    }
                });

                $log.debug('Navigating away: ' + navigatingAway);
                return navigatingAway;
            }

            function isUthoppUserNavigatingAway(newUrl) {
                return isUthoppUser() &&
                    !isUthoppDialogOpen() &&
                    isNavigatingAway(newUrl);
            }

            var QAdialog = null;
            var QAdialogConfirmed = false;
            function isUthoppDialogOpen() {
                $log.debug('Uthopp dialog already open. standard: ' + QAdialog + ', confirmed:' + QAdialogConfirmed);
                return QAdialog || QAdialogConfirmed;
            }

            function _checkQAonlyDialog($scope, $event, newUrl, currentUrl, unbindEvent) {
                // Check if the user used the special qa-link to get here.
                if (isUthoppUserNavigatingAway(newUrl)) {

                    $event.preventDefault();

                    QAdialog = dialogService.showDialog({
                        dialogId: 'qa-only-warning-dialog',
                        titleId: 'label.qaonlywarning',
                        bodyTextId: 'label.qaonlywarning.body',
                        templateUrl: '/app/partials/qa-only-warning-dialog.html',
                        button1click: function() {
                            QAdialogConfirmed = true;
                            // unbind the location change listener
                            unbindEvent();
                            LocationUtilsService.changeUrl(currentUrl, newUrl);
                        },
                        button1text: 'common.continue',
                        button1id: 'button1continue-dialog',
                        button2text: 'common.cancel',
                        autoClose: true
                    }).result.then(function() {
                        QAdialog = null; // Dialog closed
                    }, function() {
                        QAdialog = null; // Dialog dismissed
                    });

                }
                else {
                    // unbind the location change listener
                    unbindEvent();
                    if ($event.defaultPrevented) {
                        LocationUtilsService.changeUrl(currentUrl, newUrl);
                    }
                }
            }

            // Return public API for the service
            return {
                setVidareBefordradState: _setVidareBefordradState,
                handleVidareBefordradToggle: _handleVidareBefordradToggle,
                decorateSingleItemMeasure: _decorateSingleItemMeasure,
                decorateSingleItem: _decorateSingleItem,
                isUnhandled: _isUnhandled,
                fromFk : _fromFk,
                checkQAonlyDialog: _checkQAonlyDialog,
                getUnhandledQas : _getUnhandledQas
            };
        }]);
