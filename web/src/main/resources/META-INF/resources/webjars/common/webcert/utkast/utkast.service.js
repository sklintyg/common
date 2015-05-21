/**
 * Common certificate management methods between certificate modules
 */
angular.module('common').factory('common.UtkastService',
    ['$rootScope', '$document', '$log', '$location', '$stateParams', '$timeout', '$window', '$q',
        'common.UtkastService', 'common.dialogService', 'common.messageService', 'common.statService',
        'common.UserModel', 'common.UtkastViewStateService', 'common.wcFocus',
        function($rootScope, $document, $log, $location, $stateParams, $timeout, $window, $q,
            UtkastService, dialogService, messageService, statService, UserModel, CommonViewState, wcFocus) {
            'use strict';

            /**
             * Load draft to webcert
             * @param viewState
             * @private
             */
            function _load(viewState) {
                var intygsTyp = viewState.common.intyg.type;
                CommonViewState.doneLoading = false;
                UtkastService.getUtkast($stateParams.certificateId, intygsTyp, function(data) {

                    viewState.common.update(viewState.draftModel, data);

                    // check that the certs status is not signed
                    if(viewState.draftModel.isSigned()){
                        // just change straight to the intyg
                        $location.url('/intyg/' + intygsTyp + '/' + viewState.draftModel.content.id);
                    }
                    else {
                        $timeout(function() {
                            wcFocus('firstInput');
                            $rootScope.$broadcast('intyg.loaded', viewState.draftModel.content);
                            $rootScope.$broadcast(intygsTyp + '.loaded', viewState.draftModel.content);
                            CommonViewState.doneLoading = true;
                        }, 10);
                    }

                }, function(error) {
                    CommonViewState.doneLoading = true;
                    CommonViewState.error.activeErrorMessageKey = checkSetError(error.errorCode);
                });
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
                if (UtkastService.isSaveDraftInProgress()) {
                    return false;
                }

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

                    saveCompletePromise.finally(function(){
                        if(extras && extras.destroy ){
                            extras.destroy();
                        }
                    });

                    UtkastService.saveDraft( intygState.viewState.intygModel.id, intygState.viewState.common.intyg.type,
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
