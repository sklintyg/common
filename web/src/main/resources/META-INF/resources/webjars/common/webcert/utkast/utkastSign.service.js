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

            function signera(intygsTyp, version) {
                var deferred = $q.defer();
                if (_endsWith(UserModel.userContext.authenticationScheme, ':fake')) {
                    return _signeraServer(intygsTyp, $stateParams.certificateId, version, deferred);
                } else if (UserModel.userContext.authenticationMethod === 'NET_ID') {
                    return _signeraKlient(intygsTyp, $stateParams.certificateId, version, deferred);
                } else {
                    // TODO BANK ID
                    $log.info('Starting Bank ID signing...');
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

            function _confirmSigneraWithGrp(signModel, intygsTyp, intygsId, version, deferred) {
                UtkastProxy.signeraUtkastWithGrp(intygsId, intygsTyp, version, function(ticket) {

                    var dialogSignModel ={
                        acceptprogressdone: true,
                        focus: false,
                        errormessageid: 'common.error.sign.bankid',
                        showerror: false
                    };

                    var bankIdSignDialog = dialogService.showDialog({
                        dialogId: 'signera-bankid-dialog',
                        title: 'sign',
                        bodyTextId: 'common.modal.bankid.sign',
                        templateUrl: '/app/partials/signera-bankid-dialog.html',
                        model: dialogSignModel,
                        autoClose: false
                    });

                    bankIdSignDialog.opened.then(function() {
                        bankIdSignDialog.isOpen = true;
                    }, function() {
                        bankIdSignDialog.isOpen = false;
                    });

                    _waitForSigneringsstatusSigneradAndClose(signModel, intygsTyp, intygsId, ticket, deferred, bankIdSignDialog);
                }, function(error) {
                    deferred.resolve({});
                    _showSigneringsError(signModel, error);
                });
            }


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
                    iid_SetProperty('Base64', 'true');
                    iid_SetProperty('DataToBeSigned', hash);
                    iid_SetProperty('URLEncode', 'false');
                    var resultCode = iid_Invoke('Sign');

                    if (resultCode === 0) {
                        onSuccess(iid_GetProperty('Signature'));
                    } else {
                        var message = 'Signeringen avbröts med kod: ' + resultCode;
                        $log.info(message);
                        onError({ errorCode: 'SIGN_NETID_ERROR'});
                    }
                });
            }

            function _waitForSigneringsstatusSigneradAndClose(signModel, intygsTyp, intygsId, ticket, deferred, dialogHandle) {

                function getSigneringsstatus() {
                    UtkastProxy.getSigneringsstatus(ticket.id, intygsTyp, function(ticket) {
                        if ('BEARBETAR' === ticket.status) {
                            // TODO: We _should_ change the text of the dialog (if active) once we have signed in BankID
                            // TODO: and are waiting for the GRP collect to finish.
                            signModel._timer = $timeout(getSigneringsstatus, 1000);
                        } else if ('SIGNERAD' === ticket.status) {
                            deferred.resolve({newVersion : ticket.version});
                            if (dialogHandle) dialogHandle.close();        // TODO this is a hack, fix.
                            _showIntygAfterSignering(signModel, intygsTyp, intygsId);
                        } else {
                            deferred.resolve({newVersion : ticket.version});
                            if (dialogHandle) dialogHandle.close(); // TODO this is a hack, fix.
                            _showSigneringsError(signModel, {errorCode: 'SIGNERROR'});
                        }
                    });
                }

                getSigneringsstatus();
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
