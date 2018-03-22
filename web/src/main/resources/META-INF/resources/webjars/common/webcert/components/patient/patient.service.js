/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
    ['$log', 'common.ObjectHelper', 'common.UserModel',
        function($log, ObjectHelper, UserModel) {
            'use strict';

                this.getPatientDataChanges = function(isIntyg, intyg, intygProperties) {

                    var patient = {
                        changedNamePuIntegration: false, // PS-004
                        changedNamePu: false,            // PS-005
                        changedAddressPu: false          // PS-006
                    };

                    // Do we have enough info to determine messages?
                    // Must have at least intygsdata, and if it's not a draft - we also need to have intygProperties
                    if(!intyg || (isIntyg && !intygProperties)){
                        return;
                    }
    
                    // TODO: We should not have knowledge about intygstyper in the common codebase...
                    // Maybe we should implement a concept of "family" or "issuer" or delegate this logic to a
                    // intygstyp-specific component?
                    var tsIntyg = (intyg.typ === 'ts-bas' || intyg.typ === 'ts-diabetes');
                    var fkIntyg = !tsIntyg && !(intyg.typ === 'db' || intyg.typ === 'doi');

                    // PS-004 -----------------------------------------------------------------------------
                    // 1. Should only displayed for djupintegrerade
                    if (UserModel.isDjupintegration()) {
                        //Show for ts-utkast and all fk
                        if ((tsIntyg && !isIntyg) || fkIntyg) {
                            patient.changedNamePuIntegration = this.hasChangedNameInIntegration(intyg.grundData);
                        }
                    }


                    //PS-005 -----------------------------------------------------------------------------
                    // INTYG + TS - > Potentially show PS-005
                    if(tsIntyg && isIntyg){
                        patient.changedNamePu = intygProperties.patientNameChangedInPU;
                    }


                    // PS-006 -----------------------------------------------------------------------------
                    // Should only be displayed for TS / INTYG / Fristående
                    if(tsIntyg && isIntyg && UserModel.isNormalOrigin()){
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
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('fornamn')) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('efternamn'))) {

                        return grundData.patient.fornamn !== UserModel.getIntegrationParam('fornamn') ||
                            grundData.patient.efternamn !== UserModel.getIntegrationParam('efternamn');
                    }
                    return false;
                };

                /**
                 * When a deep-integration user requests an intyg, the original request  may contain name and address.
                 * This method matches the supplied parameters (if applicable) with the patient address on the requested
                 * certificate and returns true if the address has changed.
                 */
                this.hasChangedAddressInIntegration = function(grundData) {
                    if (ObjectHelper.isDefined(grundData) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('postort')) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('postadress')) &&
                        ObjectHelper.isDefined(UserModel.getIntegrationParam('postnummer'))) {

                        return grundData.patient.postort !== UserModel.getIntegrationParam('postort') ||
                            grundData.patient.postadress !== UserModel.getIntegrationParam('postadress') ||
                            grundData.patient.postnummer !== UserModel.getIntegrationParam('postnummer');
                    }
                    return false;
                };

                // TS intyg needs to showwarning if any address integration parameters are missing (INTYG-5146).
                this.isMissingRequiredAddressIntegrationParameter = function(isIntyg, intyg) {
                    if(!UserModel.isDjupintegration()) {
                        return false;
                    }

                    var eligibleIntygstyp = !isIntyg && (intyg.typ === 'ts-bas' || intyg.typ === 'ts-diabetes');
                    var missingAddressParameter = !ObjectHelper.isDefined(UserModel.getIntegrationParam('postort')) ||
                        !ObjectHelper.isDefined(UserModel.getIntegrationParam('postadress')) ||
                        !ObjectHelper.isDefined(UserModel.getIntegrationParam('postnummer'));
                    return eligibleIntygstyp && missingAddressParameter;
                };
        }]);
