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

angular.module('common').service('common.PatientService',
    ['$log', 'common.ObjectHelper', 'common.UserModel',
        function($log, ObjectHelper, UserModel) {
            'use strict';

                /**
                 * When a deep-integration user requests an intyg, the original request may contain name and address.
                 * This method matches the supplied parameters (if applicable) with the patient address on the requested
                 * certificate and returns true if the name has changed.
                 * */
                this.hasChangedName = function(grundData) {
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
                this.hasChangedAddress = function(grundData) {
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
        }]);
