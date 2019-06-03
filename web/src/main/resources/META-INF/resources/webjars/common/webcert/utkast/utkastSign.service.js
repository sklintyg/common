/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
        'common.UserModel', '$uibModal', 'common.authorityService', 'common.receiverService',
        'common.MonitoringLogService',
        function($rootScope, $document, $log, $location, $stateParams, $timeout, $window, $q,
            UtkastProxy, dialogService, messageService, statService, UserModel, $uibModal,
            authorityService, receiverService, monitoringService) {
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
                if (_endsWith(UserModel.user.authenticationScheme, ':fake') && UserModel.user.authenticationMethod === 'FAKE') {
                    _signeraServerFake(intygsTyp, $stateParams.certificateId, version, deferred);
                } else if (UserModel.user.authenticationMethod === 'NET_ID' || UserModel.user.authenticationMethod === 'SITHS' || UserModel.user.authenticationMethod === 'EFOS') {

                    // Use iid_IsExplorer() to determine whether to use NetiD Plugin or NetiD Access
                    if (iid_IsExplorer()) { // jshint ignore:line
                        _signeraKlient(intygsTyp, $stateParams.certificateId, version, deferred);
                    } else {
                        _signeraServerUsingNias(intygsTyp, $stateParams.certificateId, version, deferred);
                    }
                } else {
                    _signeraServerUsingGrp(intygsTyp, $stateParams.certificateId, version, deferred);
                }
                return deferred.promise;
            }

            function _signeraServerFake(intygsTyp, intygsId, version, deferred) {
                var signModel = {};
                _confirmSigneraMedFake(signModel, intygsTyp, intygsId, version, deferred);
            }

            /**
             * Init point for signering using GRP (i.e. BankID and Mobilt BankID)
             */
            function _signeraServerUsingGrp(intygsTyp, intygsId, version, deferred) {
                var signModel = {};
                _confirmSigneraMedBankID(signModel, intygsTyp, intygsId, version, deferred);
            }

            /**
             * Init point for signering using NIAS (EFOS + NetiD Access Server)
             */
            function _signeraServerUsingNias(intygsTyp, intygsId, version, deferred) {
                var signModel = {};
                _confirmSigneraMedNias(signModel, intygsTyp, intygsId, version, deferred);
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
                UtkastProxy.startSigningProcess(intygsId, intygsTyp, version, 'GRP', function(ticket) {

                    // Resolve which modal template to use (BankID or Mobilt BankID differs somewhat)
                    var templateUrl = templates[UserModel.authenticationMethod()];
                    _handleBearbetar(signModel, intygsTyp, intygsId, ticket, deferred, _openBankIDSigningModal(templateUrl));

                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error, intygsTyp);
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


            // EFOS / NetiD Access Server
            function _confirmSigneraMedNias(signModel, intygsTyp, intygsId, version, deferred) {

                // Anropa server, starta signering med GRP
                UtkastProxy.startSigningProcess(intygsId, intygsTyp, version, 'NETID_ACCESS', function(ticket) {

                    var templateUrl = '/app/views/signeraNiasDialog/signera.nias.dialog.html';
                    _handleBearbetar(signModel, intygsTyp, intygsId, ticket, deferred, _openNiasSigningModal(templateUrl));

                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error, intygsTyp);
                });
            }

            /**
             * Opens a custom (almost) full-screen modal for NetiD Access Server signing. The biljettStatus() function
             * in the internal controller is used for updating texts within the modal as GRP state changes are propagated
             * to the GUI.
             */
            function _openNiasSigningModal(templateUrl) {

                return $uibModal.open({
                    templateUrl: templateUrl,
                    backdrop: 'static',
                    keyboard: false,
                    windowClass: 'nias-signera-modal',
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


            // net id - telia och siths
            function _signeraKlient(intygsTyp, intygsId, version, deferred) {
                var signModel = {
                    signingWithSITHSInProgress : true
                };
                UtkastProxy.startSigningProcess(intygsId, intygsTyp, version, 'NETID_PLUGIN', function(ticket) {
                    _openNetIdPlugin(ticket.hash, intygsId, function(signatur, certifikat) {
                        UtkastProxy.signeraUtkastWithSignatur(ticket.id, intygsTyp, signatur, certifikat, function(ticket) {

                            if (ticket.status === 'SIGNERAD') {
                                deferred.resolve({newVersion: ticket.version});
                                _showIntygAfterSignering(signModel, intygsTyp, intygsId);
                            } else if (ticket.status === 'BEARBETAR') {
                                _handleBearbetar(signModel, intygsTyp, intygsId, ticket, deferred, undefined);
                            } else {
                                _handleFailedSignAttempt(signModel, ticket, deferred, intygsTyp);
                            }

                        }, function(error) {
                            deferred.resolve({newVersion : ticket.version});
                            _showSigneringsError(signModel, error, intygsTyp);
                        });
                    }, function(error) {
                        deferred.resolve({newVersion : ticket.version});
                        _showSigneringsError(signModel, error, intygsTyp);
                    });
                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error, intygsTyp);
                });
            }

            var knownSignStatuses = {'BEARBETAR':'', 'VANTA_SIGN':'', 'NO_CLIENT':'', 'SIGNERAD':'', 'OKAND': ''};

            function _confirmSigneraMedFake(signModel, intygsTyp, intygsId, version, deferred) {

                // Anropa server, starta signering med GRP
                UtkastProxy.startSigningProcess(intygsId, intygsTyp, version, 'FAKE', function(ticket) {

                    // Kick off the poller
                    _handleBearbetar(signModel, intygsTyp, intygsId, ticket, deferred);

                    // Since this is fake, call the fejksignera endpoint right away. Only needs an error callback.
                    UtkastProxy.fejkSignera(intygsTyp, intygsId, version, ticket.id, function(error) {
                        deferred.resolve({});
                        _showSigneringsError(signModel, error, intygsTyp);
                    });

                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error, intygsTyp);
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
                                deferred.reject();
                            } else {
                                signModel._timer = $timeout(getSigneringsstatus, 1000);
                            }
                        }
                        // Else, the status indicated an error, handle that!
                        else {
                            if (dialogHandle) {
                                dialogHandle.close();
                            }
                            _handleFailedSignAttempt(signModel, ticket, deferred, intygsTyp);
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

            function _openNetIdPlugin(hash, intygsId, onSuccess, onError) {
                $timeout(function() {
                    iid_Invoke('Reset'); // jshint ignore:line
                    iid_SetProperty('Authentication', 'false'); // jshint ignore:line
                    iid_SetProperty('Algorithm', '1.2.840.113549.1.1.11'); // jshint ignore:line
                    iid_SetProperty('Base64', 'true'); // jshint ignore:line
                    iid_SetProperty('Raw', 'true'); // jshint ignore:line
                    iid_SetProperty('Data', hash); // jshint ignore:line
                    iid_SetProperty('URLEncode', 'false'); // jshint ignore:line

                    if (UserModel.user.authenticationMethod === 'NET_ID') {
                        iid_SetProperty('Subjects', 'SERIALNUMBER=' + UserModel.user.personId); // jshint ignore:line
                    } else if (UserModel.user.authenticationMethod === 'SITHS' || UserModel.user.authenticationMethod === 'EFOS') {
                        iid_SetProperty('Subjects', 'SERIALNUMBER=' + UserModel.user.hsaId); // jshint ignore:line
                    }

                    // Call sign
                    var resultCode = iid_Invoke('Sign'); // jshint ignore:line

                    if (resultCode === 0) {
                        var signatur = iid_GetProperty('Signature'); // jshint ignore:line
                        var certifikat = iid_GetProperty('Certificate'); // jshint ignore:line
                        onSuccess(signatur, certifikat); // jshint ignore:line
                    } else {
                        var messageAbort = 'Signeringen avbröts med kod: ' + resultCode + ', msg: ' +
                            iid_GetProperty('Error' + resultCode);                 // jshint ignore:line
                        $log.info(messageAbort);
                        monitoringService.signingFailed(messageAbort, intygsId);
                        onError({errorCode: 'SIGN_NETID_ERROR'});
                    }
                });
            }

            function CertificateInfoEx(slotId, realSlotId, keyId, label, issuer, subject, validFrom, validTo, isCA,
                                       credential, thumbprint, authorityIdentifier, keyUsage, expire, value) {
                this.slotId = slotId;
                this.realSlotId = realSlotId;
                this.keyId = keyId;
                this.label = label;
                this.issuer = issuer;
                this.subject = subject;
                this.validFrom = validFrom;
                this.validTo = validTo;
                this.isCA = isCA;
                this.credential = credential;
                this.thumbprint = thumbprint;
                this.authorityIdentifier = authorityIdentifier;
                this.keyUsage = keyUsage;
                this.expire = expire;
                this.value = value;
            }

            // GetPartBy comes from NetiD _utility.js script.
            function _parseCertificateExInfo(information) {                          // jshint ignore:line
                var certificateEx = null;                                            // jshint ignore:line
                if ((information != null) && (information.length > 14)) {            // jshint ignore:line
                    certificateEx = new CertificateInfoEx(                           // jshint ignore:line
                        GetPartBy(information, 0, ';'),                              // jshint ignore:line
                        GetPartBy(information, 1, ';'),                              // jshint ignore:line
                        GetPartBy(information, 2, ';'),                              // jshint ignore:line
                        GetPartBy(information, 3, ';'),                              // jshint ignore:line
                        URL.decode(GetPartBy(information, 4, ';')),                  // jshint ignore:line
                        URL.decode(GetPartBy(information, 5, ';')),                  // jshint ignore:line
                        GetPartBy(information, 6, ';'),                              // jshint ignore:line
                        GetPartBy(information, 7, ';'),                              // jshint ignore:line
                        GetPartBy(information, 8, ';'),                              // jshint ignore:line
                        GetPartBy(information, 9, ';'),                              // jshint ignore:line
                        GetPartBy(information, 10, ';'),                             // jshint ignore:line
                        GetPartBy(information, 11, ';'),                             // jshint ignore:line
                        GetPartBy(information, 12, ';'),                             // jshint ignore:line
                        GetPartBy(information, 13, ';'),                             // jshint ignore:line
                        GetPartBy(information, 14, ';'));                            // jshint ignore:line
                }
                return certificateEx;
            }

            function _handleFailedSignAttempt(signModel, ticket, deferred, intygsTyp) {
                deferred.resolve({newVersion : ticket.version});
                $timeout.cancel(signModel._timer);
                _showSigneringsError(signModel, {errorCode: 'SIGNERROR'}, intygsTyp);
            }

            function needsReceiverApproval(intygsTyp) {
                var hasbasicAuth = authorityService.isAuthorityActive({
                    authority: UserModel.privileges.GODKANNA_MOTTAGARE,
                    intygstyp: intygsTyp });

                return hasbasicAuth && receiverService.getData().possibleReceivers.length > 1;


            }
            function _showIntygAfterSignering(signModel, intygsTyp, intygsId) {
                signModel.signingWithSITHSInProgress = false;
                //TODO: either use stateParams for ALL context properties (intygstyp, intygTypeVersion and intygsId) instead of having to pass them
                // around in all these function signatures?
                var intygTypeVersion = $stateParams.intygTypeVersion;
                $location.replace();
                $location.path('/intyg/' + intygsTyp + '/' + intygTypeVersion + '/' + intygsId + '/').search('signed', true);
                receiverService.getData().showApproveDialog = needsReceiverApproval(intygsTyp);
                statService.refreshStat();
            }

            function _setErrorMessageId(error, intygsTyp) {
                var messageId = '';

                var errorTable = {
                    'PU_PROBLEM':                 'common.error.pu_problem',
                    'DATA_NOT_FOUND':             'common.error.certificatenotfound',
                    'INVALID_STATE':              'common.error.certificateinvalidstate',
                    'SIGN_NETID_ERROR':           'common.error.sign.netid',
                    'CONCURRENT_MODIFICATION':    'common.error.sign.concurrent_modification',
                    'AUTHORIZATION_PROBLEM':      'common.error.sign.authorization',
                    'INDETERMINATE_IDENTITY':     'common.error.sign.indeterminate.identity',
                    'INTYG_FROM_OTHER_VARDGIVARE_EXISTS': intygsTyp + '.error.sign.intyg_of_type_exists.other_vardgivare',
                    'INTYG_FROM_SAME_VARDGIVARE_EXISTS': intygsTyp + '.error.sign.intyg_of_type_exists.same_vardgivare'
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
                } else {
                    if (error === '') {
                        messageId = 'common.error.cantconnect';
                    } else {
                        messageId = 'common.error.sign.general';
                    }
                }
                return messageId;
            }

            function _setErrorModalTitle(error, intygsTyp) {
                var errorMap = {
                    'INTYG_FROM_OTHER_VARDGIVARE_EXISTS': intygsTyp + '.error.sign.intyg_of_type_exists.other_vardgivare.title',
                    'INTYG_FROM_SAME_VARDGIVARE_EXISTS': intygsTyp + '.error.sign.intyg_of_type_exists.same_vardgivare.title'
                };
                return errorMap.hasOwnProperty(error.errorCode) ? errorMap[error.errorCode] : 'common.modal.title.sign.error';
            }

            function _showSigneringsError(signModel, error, intygsTyp) {
                var errorMessage,
                    variables = null,
                    modalTitle = _setErrorModalTitle(error, intygsTyp),
                    sithssignerrormessageid = _setErrorMessageId(error, intygsTyp);

                if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                    // In the case of concurrent modification we should have the name of the user making trouble in the message.
                    variables = {name: error.message};
                }
                errorMessage = messageService.getProperty(sithssignerrormessageid, variables, sithssignerrormessageid);
                if (error.errorCode === 'PU_PROBLEM') {
                    dialogService.showMessageDialog('common.error.pu_problem.title', errorMessage);
                } else {
                    dialogService.showErrorMessageDialog(errorMessage, undefined, modalTitle);
                }
                signModel.signingWithSITHSInProgress = false;
            }

            // Return public API for the service
            return {
                signera: _signera,

                __test__: {
                    confirmSignera: _confirmSigneraMedFake,
                    setErrorMessageId: _setErrorMessageId,
                    showSigneringsError: _showSigneringsError
                }
            };
        }]);
