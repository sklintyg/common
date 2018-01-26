/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
 * Common certificate management methods between certificate modules
 */
angular.module('common').factory('common.UtkastSignService',
    ['$rootScope', '$document', '$log', '$location', '$stateParams', '$timeout', '$window', '$q',
        'common.UtkastProxy', 'common.dialogService', 'common.messageService', 'common.statService',
        'common.UserModel', '$uibModal',
        function($rootScope, $document, $log, $location, $stateParams, $timeout, $window, $q,
            UtkastProxy, dialogService, messageService, statService, UserModel, $uibModal) {
            'use strict';

            // Used for updating scope inside bankID modal(s) during signing.
            var ticketStatus = {
                status: ''
            };

            function _endsWith(str, suffix) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }

            /**
             * Uses NET_ID for auth methods NET_ID and SITHS. Use fake for :fake authScheme.
             * Uses BankID / GRP for everything else.
             */
            function _signera(intygsTyp, version) {
                var deferred = $q.defer();
                if (_endsWith(UserModel.user.authenticationScheme, ':fake')) {
                    _signeraServer(intygsTyp, $stateParams.certificateId, version, deferred);
                } else if (UserModel.user.authenticationMethod === 'NET_ID' || UserModel.user.authenticationMethod === 'SITHS') {
                    _signeraKlient(intygsTyp, $stateParams.certificateId, version, deferred);
                } else {
                    _signeraServerUsingGrp(intygsTyp, $stateParams.certificateId, version, deferred);
                }
                return deferred.promise;
            }

            function _signeraServer(intygsTyp, intygsId, version, deferred) {
                var signModel = {};
                _confirmSignera(signModel, intygsTyp, intygsId, version, deferred);
            }

            /**
             * Init point for signering using GRP (i.e. BankID and Mobilt BankID)
             */
            function _signeraServerUsingGrp(intygsTyp, intygsId, version, deferred) {
                var signModel = {};
                _confirmSigneraMedBankID(signModel, intygsTyp, intygsId, version, deferred);
            }

            /**
             * Resolves which templateUrl to use from the user AuthentcationMethod. After opening the (almost)
             * full-screen modal, the GRP signing request is submitted to the server and if successful, the
             * GUI goes into a polling state where the signing state is read from the backend once per second until
             * the ticket.status === 'SIGNED' or 'OKAND' where the latter just closes the modal. 'OKAND' typically
             * indicates that the signing was cancelled by the user from within the BankID / Mobilt BankID application.
             */
            function _confirmSigneraMedBankID(signModel, intygsTyp, intygsId, version, deferred) {

                var templates = {
                    'MOBILT_BANK_ID': '/app/views/signeraBankIdDialog/signera.mobiltbankid.dialog.html',
                    'BANK_ID': '/app/views/signeraBankIdDialog/signera.bankid.dialog.html'
                };

                // Anropa server, starta signering med GRP
                UtkastProxy.signeraUtkastWithGrp(intygsId, intygsTyp, version, function(ticket) {

                    // Resolve which modal template to use (BankID or Mobilt BankID differs somewhat)
                    var templateUrl = templates[UserModel.authenticationMethod()];
                    _handleBearbetar(signModel, intygsTyp, intygsId, ticket, deferred, _openBankIDSigningModal(templateUrl));

                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error);
                });
            }

            /**
             * Opens a custom (almost) full-screen modal for BankID signing. The biljettStatus() function in the internal
             * controller is used for updating texts within the modal as GRP state changes are propagated to the GUI.
             */
            function _openBankIDSigningModal(templateUrl) {

                return $uibModal.open({
                    templateUrl: templateUrl,
                    backdrop: 'static',
                    keyboard: false,
                    windowClass: 'bankid-signera-modal',
                    controller: function($scope, $uibModalInstance, ticketStatus) {

                        $scope.close = function() {
                            $uibModalInstance.close();
                        };

                        $scope.biljettStatus = function() {
                            return ticketStatus.status;
                        };
                    },
                    resolve: {
                        ticketStatus : function() {
                            return ticketStatus;
                        }
                    }
                });
            }


            // net id - telia och sits
            function _signeraKlient(intygsTyp, intygsId, version, deferred) {
                var signModel = {
                    signingWithSITHSInProgress : true
                };
                UtkastProxy.getSigneringshash(intygsId, intygsTyp, version, function(ticket) {
                    _openNetIdPlugin(ticket.hash, function(signatur) {
                        UtkastProxy.signeraUtkastWithSignatur(ticket.id, intygsTyp, signatur, function(ticket) {

                            if (ticket.status === 'SIGNERAD') {
                                deferred.resolve({newVersion: ticket.version});
                                _showIntygAfterSignering(signModel, intygsTyp, intygsId);
                            } else if (ticket.status === 'BEARBETAR') {
                                _handleBearbetar(signModel, intygsTyp, intygsId, ticket, deferred, undefined);
                            } else {
                                _handleFailedSignAttempt(signModel, ticket, deferred);
                            }

                        }, function(error) {
                            deferred.resolve({newVersion : ticket.version});
                            _showSigneringsError(signModel, error);
                        });
                    }, function(error) {
                        deferred.resolve({newVersion : ticket.version});
                        _showSigneringsError(signModel, error);
                    });
                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error);
                });
            }

            var knownSignStatuses = {'BEARBETAR':'', 'VANTA_SIGN':'', 'NO_CLIENT':'', 'SIGNERAD':'', 'OKAND': ''};

            // fake inlognings signering?
            function _confirmSignera(signModel, intygsTyp, intygsId, version, deferred) {
                UtkastProxy.signeraUtkast(intygsId, intygsTyp, version, function(ticket) {
                    if (ticket.status === 'SIGNERAD') {
                        deferred.resolve({newVersion: ticket.version});
                        _showIntygAfterSignering(signModel, intygsTyp, intygsId);
                    } else if (ticket.status === 'BEARBETAR') {

                        _handleBearbetar(signModel, intygsTyp, intygsId, ticket, deferred, undefined);

                    } else {
                        _handleFailedSignAttempt(signModel, ticket, deferred);
                    }
                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error);
                });
            }

            /**
             * Handles NetID and fake login BEARBETAR responses from the initial call, has no dialog handling.
             */
            function _handleBearbetar(signModel, intygsTyp, intygsId, ticket, deferred, dialogHandle) {

                // declare poll function
                function getSigneringsstatus() {
                    UtkastProxy.getSigneringsstatus(ticket.id, intygsTyp, function(ticket) {
                        
                        // Update the global ticketStatus
                        ticketStatus.status = ticket.status;

                        var signed = 'SIGNERAD' === ticket.status;

                        // If the status we received is a known non-error one we check if it's signed or not
                        if (knownSignStatuses.hasOwnProperty(ticket.status)) {

                            if (signed) {
                                if (dialogHandle) {
                                    dialogHandle.close();
                                }
                                deferred.resolve({newVersion: ticket.version});
                                _showIntygAfterSignering(signModel, intygsTyp, intygsId);
                            } else if (dialogHandle && 'OKAND' === ticket.status) {
                                if (dialogHandle) {
                                    dialogHandle.close();
                                }
                            } else {
                                signModel._timer = $timeout(getSigneringsstatus, 1000);
                            }
                        }
                        // Else, the status indicated an error, handle that!
                        else {
                            if (dialogHandle) {
                                dialogHandle.close();
                            }
                            _handleFailedSignAttempt(signModel, ticket, deferred);
                        }
                    }, function(error) {
                        if (dialogHandle) {
                            dialogHandle.close();
                        }
                        deferred.resolve({});
                        _showSigneringsError(signModel, error);
                    });
                }

                // Call to poll
                getSigneringsstatus();

            }

            function _openNetIdPlugin(hash, onSuccess, onError) {
                $timeout(function() {
                    iid_SetProperty('Base64', 'true'); // jshint ignore:line
                    iid_SetProperty('DataToBeSigned', hash); // jshint ignore:line
                    iid_SetProperty('URLEncode', 'false'); // jshint ignore:line
                    if (UserModel.user.authenticationMethod === 'NET_ID') {
                        iid_SetProperty('Subjects', 'SERIALNUMBER=' + UserModel.user.personId); // jshint ignore:line
                    } else if  (UserModel.user.authenticationMethod === 'SITHS') {
                        iid_SetProperty('Subjects', 'SERIALNUMBER=' + UserModel.user.hsaId); // jshint ignore:line
                    }
                    var resultCode = iid_Invoke('Sign'); // jshint ignore:line

                    if (resultCode === 0) {
                        onSuccess(iid_GetProperty('Signature')); // jshint ignore:line
                    } else {
                        var messageAbort = 'Signeringen avbröts med kod: ' + resultCode;
                        $log.info(messageAbort);
                        onError({ errorCode: 'SIGN_NETID_ERROR'});
                    }
                });
            }

            function _handleFailedSignAttempt(signModel, ticket, deferred) {
                deferred.resolve({newVersion : ticket.version});
                $timeout.cancel(signModel._timer);
                _showSigneringsError(signModel, {errorCode: 'SIGNERROR'});
            }

            function _showIntygAfterSignering(signModel, intygsTyp, intygsId) {
                signModel.signingWithSITHSInProgress = false;

                $location.replace();
                $location.path('/intyg/' + intygsTyp + '/' + intygsId + '/').search('signed', true);
                statService.refreshStat();
            }

            function _setErrorMessageId(error) {
                var messageId = '';

                var errorTable = {
                    'DATA_NOT_FOUND':          'common.error.certificatenotfound',
                    'INVALID_STATE':           'common.error.certificateinvalidstate',
                    'SIGN_NETID_ERROR':        'common.error.sign.netid',
                    'CONCURRENT_MODIFICATION': 'common.error.sign.concurrent_modification',
                    'AUTHORIZATION_PROBLEM':   'common.error.sign.authorization',
                    'INDETERMINATE_IDENTITY':  'common.error.sign.indeterminate.identity'
                };

                if (error === undefined) {
                    $log.debug('_setErrorMessageId: Error is not defined.');
                    messageId = 'common.error.sign.general';
                }
                else if (errorTable.hasOwnProperty(error.errorCode)) {
                    messageId = errorTable[error.errorCode];
                }
                else if (error.errorCode === 'GRP_PROBLEM') {
                    if (error.message === 'ALREADY_IN_PROGRESS') {
                        messageId = 'common.error.sign.grp.already_in_progress';
                    } else if (error.message === 'USER_CANCEL' || error.message === 'CANCELLED') {
                        messageId = 'common.error.sign.grp.cancel';
                    } else if (error.message === 'EXPIRED_TRANSACTION') {
                        messageId = 'common.error.sign.grp.expired_transaction';
                    } else {
                        messageId = 'common.error.sign.general';
                    }
                }
                else {
                    if (error === '') {
                        messageId = 'common.error.cantconnect';
                    } else {
                        messageId = 'common.error.sign.general';
                    }
                }
                return messageId;
            }

            function _showSigneringsError(signModel, error) {
                var sithssignerrormessageid = _setErrorMessageId(error);

                var errorMessage;
                var variables = null;
                if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                    // In the case of concurrent modification we should have the name of the user making trouble in the message.
                    variables = {name: error.message};
                }
                errorMessage = messageService.getProperty(sithssignerrormessageid, variables, sithssignerrormessageid);
                dialogService.showErrorMessageDialog(errorMessage);
                signModel.signingWithSITHSInProgress = false;
            }

            // Return public API for the service
            return {
                signera: _signera,

                __test__: {
                    confirmSignera: _confirmSignera,
                    setErrorMessageId: _setErrorMessageId,
                    showSigneringsError: _showSigneringsError
                }
            };
        }]);
