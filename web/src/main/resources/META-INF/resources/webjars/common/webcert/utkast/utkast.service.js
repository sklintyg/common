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
        'common.ObjectHelper', 'common.IntygHelper', 'common.IntygProxy', 'common.PatientProxy', 'common.UtkastValidationService',
        function($rootScope, $document, $log, $location, $stateParams, $timeout, $window, $q, UtkastProxy,
            dialogService, messageService, statService, UserModel, CommonViewState, wcFocus, dynamicLabelService, ObjectHelper,
            IntygHelper, IntygProxy, PatientProxy, UtkastValidationService) {
            'use strict';

            // used to calculate save duration
            var saveStartTime;

            /**
             * Applicable only to NORMAL users, the PU service is used to load the Patient object and any name changes
             * are then applied to the utkast.grundData.patient.
             */
            function _applyNameChangeFromPuService(patient) {
                var deferred = $q.defer();

                CommonViewState.fetchingPatientData = true;
                PatientProxy.getPatient(patient.personId, function(patientResult) {
                    var dirty = false;
                    if (angular.isString(patientResult.fornamn) && patient.fornamn !== patientResult.fornamn) {
                        patient.fornamn = patientResult.fornamn;
                        dirty = true;
                    }
                    if (angular.isString(patientResult.mellannamn) && patient.mellannamn !== patientResult.mellannamn) {
                        patient.mellannamn = patientResult.mellannamn;
                        dirty = true;
                    }
                    if (angular.isString(patientResult.efternamn) && patient.efternamn !== patientResult.efternamn) {
                        patient.efternamn = patientResult.efternamn;
                        dirty = true;
                    }

                    // If there had been a change, build the 'fullstandigtNamn' property
                    if (dirty) {
                        patient.fullstandigtNamn = (patientResult.fornamn ? patientResult.fornamn : '');

                        if (angular.isString(patientResult.mellannamn)) {
                            patient.fullstandigtNamn += ' ' + patientResult.mellannamn;
                        }
                        if (angular.isString(patientResult.efternamn)) {
                            patient.fullstandigtNamn += ' ' + patientResult.efternamn;
                        }
                    }
                    CommonViewState.fetchingPatientData = false;
                    deferred.resolve(null);
                }, function() { // not found
                    CommonViewState.fetchingPatientData = false;
                    deferred.resolve(null);
                }, function() { // error
                    CommonViewState.fetchingPatientData = false;
                    deferred.resolve(null);
                });
                return deferred.promise;
            }

            /**
             * Performs the last loading steps, e.g. broadcasting and fullfilling the original promise.
             *
             * Handled in method so it can be chained to run after previous internal functions yielding promises has
             * executed.
             */
            function _finishLoadingUtkast(viewState, intygsTyp, def) {

                $timeout(function() {
                    wcFocus('focusFirstInput');
                    $rootScope.$broadcast('intyg.loaded', viewState.draftModel.content);
                    $rootScope.$broadcast(intygsTyp + '.loaded', viewState.draftModel.content);
                    CommonViewState.doneLoading = true;

                    UtkastValidationService.validate(viewState.draftModel.content);

                    _loadParentIntyg(viewState);
                    def.resolve(viewState.intygModel);
                }, 10);
            }


            /**
             * Load draft to webcert
             * @param viewState
             * @private
             */
            function _load(viewState) {
                var intygsTyp = viewState.common.intyg.type;
                CommonViewState.doneLoading = false;
                var def = $q.defer();

                UtkastProxy.getUtkast($stateParams.certificateId, intygsTyp, function(utkastData) {

                    // check that the certs status is not signed
                    if (utkastData.status === 'SIGNED') {
                        // just change straight to the intyg
                        $location.url('/intyg/' + intygsTyp + '/' + utkastData.content.id);
                    }
                    else {

                        // updateDynamicLabels will update draftModel.content with Tillaggsfragor
                        dynamicLabelService.updateDynamicLabels(intygsTyp, utkastData.latestTextVersion).then(
                            function(labels) {
                                viewState.relations = utkastData.relations;
                                viewState.klartForSigneringDatum = utkastData.klartForSigneringDatum;
                                viewState.common.intyg.isKomplettering = utkastData.content.grundData.relation !== undefined && utkastData.content.grundData.relation.relationKod === 'KOMPLT';

                                // update model here so controls dependent on correct models at startup has the right values first
                                viewState.common.update(viewState.draftModel, utkastData);

                                // add tilläggsfrågor to model when dynamic texts are used
                                if(ObjectHelper.isDefined(labels)) {
                                    dynamicLabelService.updateTillaggsfragorToModel(labels.tillaggsfragor, viewState.draftModel.content);
                                }

                                // Update patient name from PU-service if UserModel Origin is NORMAL
                                // INTYG-4086: Is this necessary since we're doing the patient name/address handling in the backend now?
                                if (!UserModel.isDjupintegration() && !UserModel.isUthopp()) {
                                    // Update, then finish loading using promise.
                                    _applyNameChangeFromPuService(viewState.draftModel.content.grundData.patient).then(function() {
                                        _finishLoadingUtkast(viewState, intygsTyp, def);
                                    });
                                } else {
                                    // No need to wait for PatientProxy to check patient, run finish method right away.
                                    _finishLoadingUtkast(viewState, intygsTyp, def);
                                }

                            }, function(error) {
                                CommonViewState.doneLoading = true;
                                CommonViewState.error.activeErrorMessageKey = checkSetError(error.errorCode);
                                def.reject(error);
                            });
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
                    IntygProxy.getIntyg(intygModel.grundData.relation.relationIntygsId, viewState.common.intyg.type,
                        function(result) {
                            if (result !== null && result !== '') {
                                var parentIntyg = result.contents;
                                var intygMeta = {
                                    isSent: IntygHelper.isSentToTarget(result.statuses, 'FKASSA'),
                                    isRevoked: IntygHelper.isRevoked(result.statuses),
                                    forceUseProvidedIntyg: true,
                                    meddelandeId: intygModel.grundData.relation.meddelandeId,
                                    type: viewState.common.intyg.type
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

                // We MAY have left the page (e.g. saving on back from utkast view.
                // In that case, we can just ignore everything...
                if (ObjectHelper.isEmpty(intygState.viewState.draftModel)) {
                    _saveFinally(extras);
                    return;
                }

                // Update draft version
                intygState.viewState.draftModel.version = data.version;

                if (data.status === 'DRAFT_COMPLETE') {
                    CommonViewState.intyg.isComplete = true;
                } else {
                    CommonViewState.intyg.isComplete = false;
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

                // If we have left the scope of the utkast view, just clean and return.
                if (ObjectHelper.isEmpty(intygState.viewState.draftModel)) {
                    _saveFinally(extras);
                    return;
                }

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
