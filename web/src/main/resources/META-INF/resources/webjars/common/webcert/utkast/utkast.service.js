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
angular.module('common').factory('common.UtkastService',
    ['$rootScope', '$document', '$log', '$location', '$stateParams', '$timeout', '$window', '$q',
        'common.UtkastProxy', 'common.dialogService', 'common.messageService', 'common.statService',
        'common.UserModel', 'common.UtkastViewStateService', 'common.wcFocus', 'common.dynamicLabelService', 'common.DynamicLabelProxy',
        function($rootScope, $document, $log, $location, $stateParams, $timeout, $window, $q,
            UtkastProxy, dialogService, messageService, statService, UserModel, CommonViewState, wcFocus, dynamicLabelService, DynamicLabelProxy) {
            'use strict';

            /**
             * Load draft to webcert
             * @param viewState
             * @private
             */
            function _load(viewState) {
                var intygsTyp = viewState.common.intyg.type;
                CommonViewState.doneLoading = false;
                var def = $q.defer();
                UtkastProxy.getUtkast($stateParams.certificateId, intygsTyp, function(data) {

                    viewState.common.update(viewState.draftModel, data);

                    // check that the certs status is not signed
                    if(viewState.draftModel.isSigned()){
                        // just change straight to the intyg
                        $location.url('/intyg/' + intygsTyp + '/' + viewState.draftModel.content.id);
                    }
                    else {
                        $timeout(function() {
                            wcFocus('focusFirstInput');
                            $rootScope.$broadcast('intyg.loaded', viewState.draftModel.content);
                            $rootScope.$broadcast(intygsTyp + '.loaded', viewState.draftModel.content);
                            CommonViewState.doneLoading = true;
                        }, 10);
                    }

                    // TODOOOOO TAKE FROM UTKAST MODEL
                    var version = "1.0";
                    DynamicLabelProxy.getDynamicLabels(intygsTyp, version).then(
                        function(data) {
                            dynamicLabelService.addLabels(data);
                            $log.debug(data);
                        },
                        function(error) {
                            $log.debug("error:" + error);
                        });

                    def.resolve(viewState.intygModel);

                }, function(error) {
                    CommonViewState.doneLoading = true;
                    CommonViewState.error.activeErrorMessageKey = checkSetError(error.errorCode);
                });
                return def.promise;
            }

            function checkSetErrorSave(errorCode) {
                var model = 'common.error.save.unknown';
                if (errorCode !== undefined && errorCode !== null) {
                    model = ('common.error.save.' + errorCode).toLowerCase();
                }

                return model;
            }

            /**
             * Save draft to webcert
             * @param autoSave
             * @private
             */
            function _save(extras) {
                if (UtkastProxy.isSaveUtkastInProgress()) {
                    return false;
                }

                var saveStartTime = moment();
                CommonViewState.saving = true;

                var deferred = $q.defer();
                $rootScope.$broadcast('saveRequest', deferred);

                deferred.promise.then(function(intygState) {

                    var saveComplete = $q.defer();

                    var saveCompletePromise = saveComplete.promise.then(function(result) {
                        // save success
                        intygState.viewState.common.validationSections = result.validationSections;
                        intygState.viewState.common.validationMessages = result.validationMessages;
                        intygState.viewState.common.validationMessagesGrouped = result.validationMessagesGrouped;
                        intygState.viewState.common.error.saveErrorMessage = null;
                        intygState.viewState.common.error.saveErrorCode = null;
                        intygState.viewState.draftModel.version = result.version;

                    }, function(result) {
                        // save failed
                        intygState.formFail();
                        intygState.viewState.common.error.saveErrorMessage = result.errorMessage;
                        intygState.viewState.common.error.saveErrorCode = result.errorCode;
                    });

                    saveCompletePromise.finally(function(){ // jshint ignore:line
                        if(extras && extras.destroy ){
                            extras.destroy();
                        }
                        var saveRequestDuration = moment().diff(saveStartTime);
                        if (saveRequestDuration > 1000) {
                            CommonViewState.saving = false;
                            $window.saving = false;
                        } else {
                            $timeout(function() {
                                CommonViewState.saving = false;
                                $window.saving = false;
                            }, 1000 - saveRequestDuration);
                        }
                    });

                    UtkastProxy.saveUtkast( intygState.viewState.intygModel.id, intygState.viewState.common.intyg.type,
                            intygState.viewState.draftModel.version, intygState.viewState.intygModel.toSendModel(),
                        function(data) {

                                var result = {};
                                result.validationMessagesGrouped = {};
                                result.validationMessages = [];
                                result.validationSections = [];
                                result.version = data.version;

                                if (data.status === 'COMPLETE') {
                                    CommonViewState.intyg.isComplete = true;
                                    saveComplete.resolve(result);
                                } else {
                                    CommonViewState.intyg.isComplete = false;

                                    if (!CommonViewState.showComplete) {
                                        result.validationMessages = data.messages.filter(function(message) {
                                            return (message.type !== 'EMPTY');
                                        });
                                    }
                                    else {
                                        result.validationMessages = data.messages;
                                    }

                                    angular.forEach(result.validationMessages, function(message) {
                                        var field = message.field;
                                        var parts = field.split('.');
                                        var section;
                                        if (parts.length > 0) {
                                            section = parts[0].toLowerCase();
                                            if(result.validationSections.indexOf(section) === -1){
                                                result.validationSections.push(section);
                                            }

                                            if (result.validationMessagesGrouped[section]) {
                                                result.validationMessagesGrouped[section].push(message);
                                            } else {
                                                result.validationMessagesGrouped[section] = [message];
                                            }
                                        }
                                    });
                                    saveComplete.resolve(result);
                                }
                            }, function(error) {
                                // Show error message if save fails

                                var errorMessage;
                                var variables = null;
                                if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                                    // In the case of concurrent modification we should have the name of the user making trouble in the message.
                                    variables = {name: error.message};
                                }

                                var errorCode = error.errorCode;
                                if (typeof errorCode === 'undefined') {
                                    errorCode = 'unknown';
                                }
                                var errorMessageId = checkSetErrorSave(errorCode);
                                errorMessage = messageService.getProperty(errorMessageId, variables, errorMessageId);

                                var result = {
                                    errorMessage: errorMessage,
                                    errorCode: errorCode
                                };
                                saveComplete.reject(result);
                            }
                        );
                });
                return true;
            }

            function checkSetError(errorCode) {
                var model = 'common.error.unknown';
                if (errorCode !== undefined && errorCode !== null) {
                    model = ('common.error.' + errorCode).toLowerCase();
                }

                return model;
            }

            // Return public API for the service
            return {
                load: _load,
                save: _save
            };
        }]);
