/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

                this.getPatientDataChanges = function(context, intyg, intygProperties) {

                    var patient = {
                        changedNamePuIntegration: false,
                        changedNamePu: false,
                        changedAddressPu: false
                    };

                    if(!context){
                        $log.debug('wcPatientInfoChangeMessage - context parameter missing.');
                    }
    
                    // INTYG views for TS intyg should not show name changes
                    var fkIntyg = !(intyg.typ === 'ts-bas' || intyg.typ === 'ts-diabetes');
                    var tsIntyg = !fkIntyg;
                    if(!(tsIntyg && context === 'INTYG')){
                        patient.changedNamePuIntegration = this.hasChangedNameInIntegration(intyg.grundData);
                    }
    
                    if(context === 'INTYG'){

                        // INTYG views for integrated FK intyg should not show name changes
                        if(!(fkIntyg && UserModel.isDjupintegration())){
                            patient.changedNamePu = intygProperties.patientNameChangedInPU;
                        }

                        // INTYG views for fristående TS intyg should show address changes
                        if(tsIntyg){
                            patient.changedAddressPu = intygProperties.patientAddressChangedInPU;
                        }

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
                this.isMissingRequiredAddressIntegrationParameter = function(context, intyg) {
                    if(!UserModel.isDjupintegration()) {
                        return false;
                    }

                    var eligibleIntygstyp = context === 'UTKAST' && (intyg.typ === 'ts-bas' || intyg.typ === 'ts-diabetes');
                    var missingAddressParameter = !ObjectHelper.isDefined(UserModel.getIntegrationParam('postort')) ||
                        !ObjectHelper.isDefined(UserModel.getIntegrationParam('postadress')) ||
                        !ObjectHelper.isDefined(UserModel.getIntegrationParam('postnummer'));
                    return eligibleIntygstyp && missingAddressParameter;
                };
        }]);
