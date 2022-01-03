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
/**
 * Service for getting meta state regarding patient data (name, address): specifically when that data comes from a
 * certain source and replaces another source.
 */
angular.module('common').service('common.PatientService',
    ['$injector','$log', 'common.ObjectHelper', 'common.StringHelper', 'common.UserModel', 'factoryResolverHelper',
        function($injector, $log, ObjectHelper, StringHelper, UserModel, factoryResolverHelper) {
            'use strict';

                this.getPatientDataChanges = function(isIntyg, intyg, intygProperties) {

                    var patient = {
                        changedNamePuIntegration: false, // PS-004
                        changedNamePu: false,            // PS-005
                        changedAddressPu: false          // PS-006
                    };

                    // Do we have enough info to determine messages?
                    // Must have at least intygsdata, and if it's not a draft - we also need to have intygProperties
                    if(!intyg || (isIntyg && !intygProperties) || !intyg.typ){
                        return;
                    }

                    // Dynamically inject the patient helper with intygstyp specific rules.
                    var patientHelper = factoryResolverHelper.resolvePatientHelper(intyg.typ, intyg.textVersion);

                    // PS-003 -----------------------------------------------------------------------------
                    // 1. Should only displayed for djupintegrerade
                    if (UserModel.isDjupintegration()) {
                        // Show for ts-utkast and all fk
                        if (patientHelper.showPatientNameChangedIntegration(isIntyg)) {
                            patient.changedNamePuIntegration = this.hasChangedNameInIntegration(intyg.grundData);
                        }
                    }

                    //PS-004 -----------------------------------------------------------------------------
                    // INTYG + TS - > Potentially show PS-004
                    if (patientHelper.showPatientNameChangedPU(isIntyg)) {
                        patient.changedNamePu = intygProperties.patientNameChangedInPU;
                    }


                    // PS-005 -----------------------------------------------------------------------------
                    // Should only be displayed for TS / INTYG / Fristående
                    if (patientHelper.showPatientAddressChangedPU(isIntyg) && UserModel.isNormalOrigin()) {
                        patient.changedAddressPu = intygProperties.patientAddressChangedInPU;
                    }

                    return patient;
                };

                /**
                 * When a deep-integration user requests an intyg, the original request may contain name and address.
                 * This method matches the supplied parameters (if applicable) with the patient address on the requested
                 * certificate and returns true if the name has changed.
                 * */
                this.hasChangedNameInIntegration = function(grundData) {
                    if (ObjectHelper.isDefined(grundData) &&
                        ObjectHelper.isDefined(grundData.patient.fornamn) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('fornamn')) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('efternamn'))) {

                        var mellannamn = false;

                        if (ObjectHelper.isDefined(UserModel.getIntegrationParam('mellannamn'))) {
                            mellannamn = StringHelper.toLowerCase(grundData.patient.mellannamn) !== UserModel.getIntegrationParam('mellannamn').toLowerCase();
                        }


                        return grundData.patient.fornamn.toLowerCase() !== UserModel.getIntegrationParam('fornamn').toLowerCase() ||
                            grundData.patient.efternamn.toLowerCase() !== UserModel.getIntegrationParam('efternamn').toLowerCase() ||
                            mellannamn;
                    }
                    return false;
                };

                // TS intyg needs to showwarning if any address integration parameters are missing (INTYG-5146).
                this.isMissingRequiredAddressIntegrationParameter = function(isIntyg, intyg) {
                    if(!UserModel.isDjupintegration()) {
                        return false;
                    }

                    var patientHelper = factoryResolverHelper.resolvePatientHelper(intyg.typ, intyg.textVersion);

                    var eligibleIntygstyp = patientHelper.showMissingAddressParameter(isIntyg);
                    var missingAddressParameter = !ObjectHelper.isDefined(UserModel.getIntegrationParam('postort')) ||
                        !ObjectHelper.isDefined(UserModel.getIntegrationParam('postadress')) ||
                        !ObjectHelper.isDefined(UserModel.getIntegrationParam('postnummer'));
                    return eligibleIntygstyp && missingAddressParameter;
                };
        }]);
