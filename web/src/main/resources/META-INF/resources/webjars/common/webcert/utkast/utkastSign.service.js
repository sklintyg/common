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

/**
 * Common certificate management methods between certificate modules
 */
angular.module('common').factory('common.UtkastSignService',
    ['$rootScope', '$document', '$log', '$location', '$stateParams', '$timeout', '$window', '$q',
        'common.UtkastProxy', 'common.dialogService', 'common.messageService', 'common.statService',
        'common.UserModel',
        function($rootScope, $document, $log, $location, $stateParams, $timeout, $window, $q,
            UtkastProxy, dialogService, messageService, statService, UserModel) {
            'use strict';

            function _endsWith(str, suffix) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }

            /**
             * Uses NET_ID for auth methods NET_ID and SITHS. Use fake for :fake authScheme.
             * Uses BankID / GRP for everything else.
             */
            function signera(intygsTyp, version) {
                var deferred = $q.defer();
                if (_endsWith(UserModel.user.authenticationScheme, ':fake')) {
                    return _signeraServer(intygsTyp, $stateParams.certificateId, version, deferred);
                } else if (UserModel.user.authenticationMethod === 'NET_ID' || UserModel.user.authenticationMethod === 'SITHS') {
                    return _signeraKlient(intygsTyp, $stateParams.certificateId, version, deferred);
                } else {
                    return _signeraServerUsingGrp(intygsTyp, $stateParams.certificateId, version, deferred);
                }
            }

            function _signeraServer(intygsTyp, intygsId, version, deferred) {
                var signModel = {};
                _confirmSignera(signModel, intygsTyp, intygsId, version, deferred);
                return deferred.promise;
            }

            function _signeraServerUsingGrp(intygsTyp, intygsId, version, deferred) {
                var signModel = {};
                _confirmSigneraWithGrp(signModel, intygsTyp, intygsId, version, deferred);
                return deferred.promise;
            }

            // backid och mobilt id
            function _confirmSigneraWithGrp(signModel, intygsTyp, intygsId, version, deferred) {
                UtkastProxy.signeraUtkastWithGrp(intygsId, intygsTyp, version, function(ticket) {

                    var dialogSignModel ={
                        acceptprogressdone: true,
                        focus: false,
                        errormessageid: 'common.error.sign.bankid',
                        showerror: false,
                        label : {signing:'common.title.sign'},
                        signState : ''
                    };


                    if(UserModel.hasAuthenticationMethod('MOBILT_BANK_ID')){
                        dialogSignModel.bodyTextId ='common.modal.mbankid.open';
                        dialogSignModel.heading = 'common.modal.mbankid.heading';
                    } else {
                        dialogSignModel.bodyTextId ='common.modal.bankid.open';
                        dialogSignModel.heading = 'common.modal.bankid.heading';
                    }

                    var bankIdSignDialog = dialogService.showDialog({
                        dialogId: 'signera-bankid-dialog',
                        templateUrl: '/app/partials/signera-bankid-dialog.html',
                        model: dialogSignModel,
                        title: 'sign',
                        autoClose: false
                    });

                    bankIdSignDialog.opened.then(function() {
                        bankIdSignDialog.isOpen = true;
                    }, function() {
                        bankIdSignDialog.isOpen = false;
                    });

                    bankIdSignDialog.closed.then(function() {
                        bankIdSignDialog.isOpen = false;
                        if(signModel._timer !== undefined){
                            $timeout.cancel(signModel._timer);
                        }
                    }, function(error) {
                        $log.debug('Could not close Bank ID dialog for ticket request');
                        $log.debug(error);
                    });

                    _waitForSigneringsstatusSigneradAndClose(signModel, intygsTyp, intygsId, ticket, deferred, bankIdSignDialog);
                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error);
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
                                deferred.resolve({newVersion : ticket.version});
                                _showIntygAfterSignering(signModel, intygsTyp, intygsId);
                            } else {
                                _waitForSigneringsstatusSigneradAndClose(signModel, intygsTyp, intygsId, ticket, deferred);
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
                return deferred.promise;
            }

            // fake inlognings signering?
            function _confirmSignera(signModel, intygsTyp, intygsId, version, deferred) {
                UtkastProxy.signeraUtkast(intygsId, intygsTyp, version, function(ticket) {
                    _waitForSigneringsstatusSigneradAndClose(signModel, intygsTyp, intygsId, ticket, deferred);
                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error);
                });
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
                        var message = 'Signeringen avbröts med kod: ' + resultCode;
                        $log.info(message);
                        onError({ errorCode: 'SIGN_NETID_ERROR'});
                    }
                });
            }

            function _waitForSigneringsstatusSigneradAndClose(signModel, intygsTyp, intygsId, ticket, deferred, dialogHandle) {

                function getSigneringsstatus() {
                    //Define the signing statuses and their text values
                    var statuses = {'BEARBETAR': {
                            'mbankid': 'common.modal.mbankid.open',
                            'bankid': 'common.modal.bankid.open'},
                        'VANTA_SIGN': {
                            'mbankid': 'common.modal.mbankid.signing',
                            'bankid': 'common.modal.bankid.signing'},
                        'NO_CLIENT': {
                            'mbankid': 'common.modal.mbankid.noclient',
                            'bankid': 'common.modal.bankid.noclient'},
                        'SIGNERAD': {
                            'mbankid': 'common.modal.mbankid.signed',
                            'bankid': 'common.modal.bankid.signed'}
                    };

                    UtkastProxy.getSigneringsstatus(ticket.id, intygsTyp, function(ticket) {
                        var hasDialogHandle = dialogHandle !== undefined,
                            signed = 'SIGNERAD' === ticket.status,
                            status;

                        if(statuses.hasOwnProperty(ticket.status)) {
                            status = statuses[ticket.status];

                            if(signed) {
                                deferred.resolve({newVersion : ticket.version});
                            } else {
                                signModel._timer = $timeout(getSigneringsstatus, 1000);
                            }

                            if(hasDialogHandle){
                                // change the status
                                dialogHandle.model.bodyTextId = UserModel.hasAuthenticationMethod('MOBILT_BANK_ID') ? status.mbankid : status.bankid;
                                dialogHandle.model.signState = ticket.status;
                                if(signed) {
                                    dialogHandle.close();
                                }
                                deferred.resolve({});
                            }

                            if(signed) {
                                _showIntygAfterSignering(signModel, intygsTyp, intygsId);
                                $timeout.cancel(signModel._timer);
                            }
                        } else {
                            deferred.resolve({newVersion : ticket.version});
                            if (hasDialogHandle) {
                                dialogHandle.close();
                            }
                            $timeout.cancel(signModel._timer);
                            _showSigneringsError(signModel, {errorCode: 'SIGNERROR'});
                        }
                    });
                }

                getSigneringsstatus();
            }

            function _closeSigningDialog(dialogHandle, signModel){
                signModel._timer = $timeout(getSigneringsstatus, 1000);
                dialogHandle.close();

            }

            function _showIntygAfterSignering(signModel, intygsTyp, intygsId) {
                signModel.signingWithSITHSInProgress = false;

                $location.replace();
                $location.path('/intyg/' + intygsTyp + '/' + intygsId).search('signed', true);
                statService.refreshStat();
            }

            function _setErrorMessageId(error) {
                var messageId = '';

                if (error === undefined) {
                    $log.debug('_setErrorMessageId: Error is not defined.');
                    messageId = 'common.error.sign.general';
                }
                else {
                    if (error.errorCode === 'DATA_NOT_FOUND') {
                        messageId = 'common.error.certificatenotfound';
                    } else if (error.errorCode === 'INVALID_STATE') {
                        messageId = 'common.error.certificateinvalidstate';
                    } else if (error.errorCode === 'SIGN_NETID_ERROR') {
                        messageId = 'common.error.sign.netid';
                    } else if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                        messageId = 'common.error.sign.concurrent_modification';
                    } else if (error.errorCode === 'AUTHORIZATION_PROBLEM') {
                        messageId = 'common.error.sign.authorization';
                    } else if (error.errorCode === 'INDETERMINATE_IDENTITY') {
                        messageId = 'common.error.sign.indeterminate.identity';
                    } else if (error.errorCode === 'GRP_PROBLEM') {
                        if (error.message === 'ALREADY_IN_PROGRESS') {
                            messageId = 'common.error.sign.grp.already_in_progress';
                        } else if (error.message === 'USER_CANCEL' || error.message === 'CANCELLED') {
                            messageId = 'common.error.sign.grp.cancel';
                        } else if (error.message === 'EXPIRED_TRANSACTION') {
                            messageId = 'common.error.sign.grp.expired_transaction';
                        } else {
                            messageId = 'common.error.sign.general';
                        }
                    } else if (error === '') {
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
                signera: signera,

                __test__: {
                    confirmSignera: _confirmSignera,
                    setErrorMessageId: _setErrorMessageId,
                    showSigneringsError: _showSigneringsError
                }
            };
        }]);
