/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
        'common.IntygHelper', 'common.moduleService', 'common.IntygHeaderViewState',
        function($log,
            IntygHelper, moduleService, IntygHeaderViewState) {
            'use strict';

            // Create persistent object. Never overwrite this. Makes it possible to one-time-bind and reference this object everywhere for faster rendering.
            this.intygProperties = {};

            this.reset = function() {
                this.doneLoading = false;
                this.activeErrorMessageKey = null;
                this.inlineErrorMessageKey = null;
                this.showTemplate = true;
                this.isIntygOnRevokeQueue = false;
                this.deleted = false;

                // IMPORTANT NOTE: needs to be this way so intygProperties object reference is not overwritten. intygProperties = {} will decouple reference in wcIntygRelatedRevokedMessage
                this.intygProperties.type = undefined;
                this.intygProperties.intygTypeVersion = undefined;
                this.intygProperties.isSent = false;
                this.intygProperties.sentTimestamp = undefined;
                this.intygProperties.isRevoked = false;
                this.intygProperties.revokedTimestamp = undefined;
                this.intygProperties.isPatientDeceased = false;
                this.intygProperties.newPatientId = false; // FK only for now. Consider making specific viewState services for each intyg as with utkast
                this.intygProperties.patientAddressChangedInPU = false;
                this.intygProperties.patientNameChangedInPU = false;
                this.intygProperties.parent = undefined;
                this.intygProperties.created = undefined;
                this.intygProperties.links = undefined;

                IntygHeaderViewState.reset();
            };

            this.isRevoked = function() {
                return this.intygProperties.isRevoked || this.isIntygOnRevokeQueue;
            };
            this.isReplaced = function() {
                return angular.isObject(this.intygProperties.latestChildRelations) &&
                    angular.isObject(this.intygProperties.latestChildRelations.replacedByIntyg);
            };
            this.isReplacedByUtkast = function() {
                return angular.isObject(this.intygProperties.latestChildRelations) &&
                    angular.isObject(this.intygProperties.latestChildRelations.replacedByUtkast);
            };
            this.isComplementedByIntyg = function() {
                return angular.isObject(this.intygProperties.latestChildRelations) &&
                    angular.isObject(this.intygProperties.latestChildRelations.complementedByIntyg) &&
                    this.intygProperties.latestChildRelations.complementedByIntyg.makulerat === false;
            };

            this.isComplementedByUtkast = function() {
                return angular.isObject(this.intygProperties.latestChildRelations) &&
                    angular.isObject(this.intygProperties.latestChildRelations.complementedByUtkast);
            };
            this.isReplacing = function() {
                return angular.isObject(this.intygProperties.parent) &&
                    this.intygProperties.parent.relationKod === 'ERSATT';
            };
            this.isComplementing = function() {
                return angular.isObject(this.intygProperties.parent) &&
                    this.intygProperties.parent.relationKod === 'KOMPLT';
            };
            this.isRenewing = function() {
                return angular.isObject(this.intygProperties.parent) &&
                    this.intygProperties.parent.relationKod === 'FRLANG';
            };

            this.isPatientDeceased = function() {
                return this.intygProperties.isPatientDeceased;
            };

            this.isSentIntyg = function() {
                return this.intygProperties.isSent;
            };

            this.getLinks = function() {
                return this.intygProperties.links;
            };

            this.updateIntygProperties = function(result, intygId) {

                var targetName = moduleService.getModule(this.intygProperties.type).defaultRecipient;

                this.intygProperties.intygTypeVersion = result.contents.textVersion;
                this.intygProperties.pdfUrl = '/moduleapi/intyg/' + this.intygProperties.type + '/' + intygId + '/pdf';

                this.intygProperties.signeringsdatum = result.contents.grundData.signeringsdatum;

                this.intygProperties.isSent = false;
                this.intygProperties.sentTimestamp = IntygHelper.sentToTargetTimestamp(result.statuses, targetName);
                if (this.intygProperties.sentTimestamp) {
                    this.intygProperties.isSent = true;
                }
                this.intygProperties.isRevoked = false;
                this.intygProperties.revokedTimestamp = IntygHelper.revokedTimestamp(result.statuses);
                if (this.intygProperties.revokedTimestamp) {
                    this.intygProperties.isRevoked = true;
                }
                this.intygProperties.isPatientDeceased = result.deceased;
                this.intygProperties.patientAddressChangedInPU = result.patientAddressChangedInPU;
                this.intygProperties.patientNameChangedInPU = result.patientNameChangedInPU;

                if (result.relations && result.relations.parent) {
                    this.intygProperties.parent = result.relations.parent;
                }

                if (result.relations && result.relations.latestChildRelations) {
                    this.intygProperties.latestChildRelations = result.relations.latestChildRelations;
                }
                this.intygProperties.created = result.created;

                this.intygProperties.links = result.links;
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
                } else if (error.errorCode === 'PU_PROBLEM') {
                    this.activeErrorMessageKey = 'common.error.pu_problem';
                } else {
                    if (signed) {
                        this.activeErrorMessageKey = 'common.error.sign.not_ready_yet';
                    } else {
                        this.activeErrorMessageKey = 'common.error.could_not_load_cert';
                    }
                }
            };

            this.reset();
        }]
);
