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

angular.module('common').service('common.IntygViewStateService',
    ['$log',
        'common.IntygHelper', 'common.ObjectHelper', 'common.UserModel',
        function($log,
            IntygHelper, ObjectHelper, UserModel) {
            'use strict';

            this.reset = function() {
                this.doneLoading = false;
                this.activeErrorMessageKey = null;
                this.inlineErrorMessageKey = null;
                this.showTemplate = true;
                this.isIntygOnSendQueue = false;
                this.isIntygOnRevokeQueue = false;
                this.deleted = false;

                this.intygProperties = {
                    type: undefined,
                    defaultRecipient: undefined,
                    isSent: false,
                    isRevoked: false,
                    isPatientDeceased: false,
                    newPatientId: false // FK only for now. Consider making specific viewState services for each intyg as with utkast
                };
            };

            this.updateIntygProperties = function(result) {
                var targetName;
                if (this.intygProperties.type === 'ts-bas' || this.intygProperties.type === 'ts-diabetes') {
                    targetName = 'TRANSP';
                } else {
                    targetName = 'FKASSA';
                }

                this.intygProperties.isSent = IntygHelper.isSentToTarget(result.statuses, targetName);
                this.intygProperties.isRevoked = IntygHelper.isRevoked(result.statuses);
                this.intygProperties.isPatientDeceased = result.deceased;

                if (result.relations && result.relations.parent) {
                    this.intygProperties.parent = result.relations.parent;
                }

                if (result.relations && result.relations.latestChildRelations) {
                    this.intygProperties.latestChildRelations = result.relations.latestChildRelations;
                }
            };

            this.updateActiveError = function(error, signed) {
                $log.debug('Loading intyg - got error: ' + error.message);
                if (error.errorCode === 'DATA_NOT_FOUND') {
                    this.activeErrorMessageKey = 'common.error.data_not_found';
                } else if (error.errorCode === 'AUTHORIZATION_PROBLEM') {
                    this.activeErrorMessageKey = 'common.error.could_not_load_cert_not_auth';
                } else if (error.errorCode === 'AUTHORIZATION_PROBLEM_SEKRETESSMARKERING_ENHET') {
                    this.activeErrorMessageKey = 'common.error.authorization_problem_sekretessmarkering_enhet';
                } else if (error.errorCode === 'AUTHORIZATION_PROBLEM_SEKRETESSMARKERING') {
                    this.activeErrorMessageKey = 'common.error.authorization_problem_sekretessmarkering';
                } else {
                    if (signed) {
                        this.activeErrorMessageKey = 'common.error.sign.not_ready_yet';
                    } else {
                        this.activeErrorMessageKey = 'common.error.could_not_load_cert';
                    }
                }
            };

            this.patient = {
                /**
                 * When a deep-integration user requests an intyg, the original request may contain name and address.
                 * This method matches the supplied parameters (if applicable) with the patient address on the requested
                 * certificate and returns true if the name has changed.
                 * */
                hasChangedName: function(intygModel) {
                    if (ObjectHelper.isDefined(intygModel) &&
                        ObjectHelper.isDefined(intygModel.grundData) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('fornamn')) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('efternamn'))) {

                        return intygModel.grundData.patient.fornamn !== UserModel.getIntegrationParam('fornamn') ||
                            intygModel.grundData.patient.efternamn !== UserModel.getIntegrationParam('efternamn');
                    }
                    return false;
                },

                /**
                 * When a deep-integration user requests an intyg, the original request  may contain name and address.
                 * This method matches the supplied parameters (if applicable) with the patient address on the requested
                 * certificate and returns true if the address has changed.
                 */
                hasChangedAddress: function(intygModel) {
                    if (ObjectHelper.isDefined(intygModel) &&
                        ObjectHelper.isDefined(intygModel.grundData) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('postort')) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('postadress')) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('postnummer'))) {

                        return intygModel.grundData.patient.postort !== UserModel.getIntegrationParam('postort') ||
                            intygModel.grundData.patient.postadress !== UserModel.getIntegrationParam('postadress') ||
                            intygModel.grundData.patient.postnummer !== UserModel.getIntegrationParam('postnummer');
                    }
                    return false;
                }
            };

            this.reset();
        }]
);
