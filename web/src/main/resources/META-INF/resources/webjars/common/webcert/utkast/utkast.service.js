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
        'common.UserModel', 'common.UtkastViewStateService', 'common.wcFocus', 'common.dynamicLabelService',
        'common.ObjectHelper', 'common.IntygService', 'common.IntygProxy',
        function($rootScope, $document, $log, $location, $stateParams, $timeout, $window, $q, UtkastProxy,
            dialogService, messageService, statService, UserModel, CommonViewState, wcFocus, dynamicLabelService, ObjectHelper,
            IntygService, IntygProxy) {
            'use strict';

            // used to calculate save duration
            var saveStartTime;

            /**
             * Load draft to webcert
             * @param viewState
             * @private
             */
            function _load(viewState) {
                var intygsTyp = viewState.common.intyg.type;
                CommonViewState.doneLoading = false;
                var def = $q.defer();
                var sjf = ObjectHelper.isDefined($stateParams.sjf) ? $stateParams.sjf : false;

                UtkastProxy.getUtkast($stateParams.certificateId, intygsTyp, sjf, function(data) {
                    viewState.relations = data.relations;
                    viewState.common.intyg.isKomplettering = data.content.grundData.relation !== undefined && data.content.grundData.relation.relationKod === 'KOMPLT';
                    viewState.common.update(viewState.draftModel, data);

                    // check that the certs status is not signed
                    if (viewState.draftModel.isSigned()) {
                        // just change straight to the intyg
                        $location.url('/intyg/' + intygsTyp + '/' + viewState.draftModel.content.id);
                    }
                    else {

                        // update patient data from integration if available
                        if(UserModel.isDjupintegration()) {
                            if(ObjectHelper.isDefined($stateParams.postadress)){
                                viewState.intygModel.grundData.patient.postadress = $stateParams.postadress;
                            }
                            if(ObjectHelper.isDefined($stateParams.postadress)) {
                                viewState.intygModel.grundData.patient.postnummer = $stateParams.postnummer;
                            }
                            if(ObjectHelper.isDefined($stateParams.postadress)) {
                                viewState.intygModel.grundData.patient.postort = $stateParams.postort;
                            }
                        }

                        // updateDynamicLabels will update draftModel.content with Tillaggsfragor
                        dynamicLabelService.updateDynamicLabels(intygsTyp, viewState.draftModel.content);
                        $timeout(function() {
                            wcFocus('focusFirstInput');
                            $rootScope.$broadcast('intyg.loaded', viewState.draftModel.content);
                            $rootScope.$broadcast(intygsTyp + '.loaded', viewState.draftModel.content);
                            CommonViewState.doneLoading = true;

                            _loadParentIntyg(viewState);
                            def.resolve(viewState.intygModel);
                        }, 10);
                    }

                }, function(error) {
                    CommonViewState.doneLoading = true;
                    CommonViewState.error.activeErrorMessageKey = checkSetError(error.errorCode);
                    def.reject(error);
                });
                return def.promise;
            }

            function _loadParentIntyg(viewState) {
                var intygModel = viewState.intygModel;
                // Load parentIntyg to feed fragasvar component with load event
                if (ObjectHelper.isDefined(intygModel.grundData.relation) &&
                    ObjectHelper.isDefined(intygModel.grundData.relation.relationIntygsId) &&
                    ObjectHelper.isDefined(intygModel.grundData.relation.meddelandeId)) {
                    IntygProxy.getIntyg(intygModel.grundData.relation.relationIntygsId, viewState.common.intyg.type, false,
                        function(result) {
                            if (result !== null && result !== '') {
                                var parentIntyg = result.contents;
                                var intygMeta = {
                                    isSent: IntygService.isSentToTarget(result.statuses, 'FK'),
                                    isRevoked: IntygService.isRevoked(result.statuses),
                                    forceUseProvidedIntyg: true,
                                    kompletteringOnly: true,
                                    meddelandeId: intygModel.grundData.relation.meddelandeId
                                };
                                $rootScope.$emit('ViewCertCtrl.load', parentIntyg, intygMeta);
                            } else {
                                $rootScope.$emit('ViewCertCtrl.load', null, null);
                            }
                        }, function(error) {
                            $rootScope.$emit('ViewCertCtrl.load', null, null);
                        });
                } else {
                    // Failed to load parent intyg. Tell frågasvar
                    $rootScope.$broadcast('ViewCertCtrl.load', null, {
                        isSent: false,
                        isRevoked: false
                    });
                }
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

                saveStartTime = moment();
                CommonViewState.saving = true;

                // Inform utkast controller about save
                var utkastControllerResponse = $q.defer();
                $rootScope.$broadcast('saveRequest', utkastControllerResponse);

                // utkast controller will return intygState specific to this intygstyp
                utkastControllerResponse.promise.then(function(intygState) {
                    UtkastProxy.saveUtkast(intygState.viewState.intygModel.id, intygState.viewState.common.intyg.type,
                        intygState.viewState.draftModel.version, intygState.viewState.intygModel.toSendModel(),
                        angular.bind(this, _saveSuccess, intygState, extras),
                        angular.bind(this, _saveFailed, intygState, extras));
                });
                return true;
            }

            // This function is called if save is successful
            function _saveSuccess(intygState, extras, data) {
                // Clear error messages
                intygState.viewState.common.error.saveErrorMessage = null;
                intygState.viewState.common.error.saveErrorCode = null;

                // Update draft version
                intygState.viewState.draftModel.version = data.version;

                // Update validation messages
                intygState.viewState.common.validationMessagesGrouped = {};
                intygState.viewState.common.validationMessages = [];
                intygState.viewState.common.validationSections = [];

                if (data.status === 'DRAFT_COMPLETE') {
                    CommonViewState.intyg.isComplete = true;
                } else {
                    CommonViewState.intyg.isComplete = false;

                    if (!CommonViewState.showComplete) {
                        intygState.viewState.common.validationMessages = data.messages.filter(function(message) {
                            return (message.type !== 'EMPTY');
                        });
                    }
                    else {
                        intygState.viewState.common.validationMessages = data.messages;
                    }

                    angular.forEach(intygState.viewState.common.validationMessages, function(message) {
                        var field = message.field;
                        var parts = field.split('.');
                        var section;
                        if (parts.length > 0) {
                            section = parts[0].toLowerCase();
                            if (intygState.viewState.common.validationSections.indexOf(section) === -1) {
                                intygState.viewState.common.validationSections.push(section);
                            }

                            if (intygState.viewState.common.validationMessagesGrouped[section]) {
                                intygState.viewState.common.validationMessagesGrouped[section].push(message);
                            } else {
                                intygState.viewState.common.validationMessagesGrouped[section] = [message];
                            }
                        }
                    });
                }

                // Update relation status on current utkast on save so relation table view is up to date
                angular.forEach(intygState.viewState.relations, function(relation) {
                    if(relation.intygsId === intygState.viewState.intygModel.id) {
                        relation.status = data.status;
                    }
                }, intygState.relations);

                _saveFinally(extras);
            }

            // This function is called if save fails
            function _saveFailed(intygState, extras, error) {
                // Show error message if save fails

                var errorCode;
                var errorMessage;
                var variables = null;
                if (error) {
                    if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                        // In the case of concurrent modification we should have the name of the user making trouble in the message.
                        variables = {name: error.message};
                    }

                    errorCode = error.errorCode;
                    if (typeof errorCode === 'undefined') {
                        errorCode = 'unknown';
                    }
                    var errorMessageId = checkSetErrorSave(errorCode);
                    errorMessage = messageService.getProperty(errorMessageId, variables, errorMessageId);
                } else {
                    // No error code from server. No contact at all
                    errorMessage = messageService.getProperty('common.error.save.noconnection', variables,
                        'common.error.save.noconnection');
                    errorCode = 'cantconnect';
                }

                intygState.formFail();
                intygState.viewState.common.error.saveErrorMessage = errorMessage;
                intygState.viewState.common.error.saveErrorCode = errorCode;

                _saveFinally(extras);
            }

            // This function will be executed if the save is successful or not
            function _saveFinally(extras) {
                if (extras && extras.destroy) {
                    extras.destroy();
                }

                // The save spinner should be visible at least 1 second
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
            }

            function checkSetError(errorCode) {
                var model = 'common.error.unknown';
                if (errorCode !== undefined && errorCode !== null) {
                    model = ('common.error.' + errorCode).toLowerCase();
                }

                return model;
            }

            function checkSetErrorSave(errorCode) {
                var model = 'common.error.save.unknown';
                if (errorCode !== undefined && errorCode !== null) {
                    model = ('common.error.save.' + errorCode).toLowerCase();
                }

                return model;
            }

            // Return public API for the service
            return {
                load: _load,
                save: _save
            };
        }]);
