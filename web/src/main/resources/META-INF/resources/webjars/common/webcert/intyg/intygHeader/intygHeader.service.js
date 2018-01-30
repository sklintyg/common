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
angular.module('common').service('common.IntygHeaderService',
    ['$log', 'common.IntygViewStateService',
        'common.authorityService', 'common.featureService', 'common.messageService', 'common.moduleService',
        'common.IntygCopyRequestModel', 'common.IntygFornyaRequestModel', 'common.IntygErsattRequestModel', 'common.User', 'common.UserModel',
        'common.IntygSend', 'common.IntygCopyActions', 'common.IntygMakulera',
        'common.IntygViewStateService', 'common.dialogService', 'common.PatientProxy', 'common.UtkastProxy', 'common.ObjectHelper',
        function($log, featureService, IntygViewStateService, UserModel) {
            'use strict';

            var previousIntyg = {};
            var previousUtkast = {};
            var warningForCreateTemplate = {};
            this.createFromTemplateConfig = {
                db: {
                    moduleId: 'doi',
                    name: 'dödsorsaksintyg',
                    features: [
                        featureService.features.UNIKT_INTYG_INOM_VG,
                        featureService.features.UNIKT_UTKAST_INOM_VG
                    ]
                }
            };

            this.getWarningForCreateFromTemplate = function() {
                return warningForCreateTemplate;
            };

            this.showCreateFromTemplate = function() {
                return this.createFromTemplateConfig[$scope.intygstyp] !== undefined && !IntygViewStateService.isRevoked() && !IntygViewStateService.isReplaced() &&
                    !IntygViewStateService.isComplemented() && !UserModel.getIntegrationParam('inactiveUnit');
            };

            this.enableCreateFromTemplate = function() {
                var intygTemplateConfig = this.createFromTemplateConfig[$scope.intygstyp];
                if (intygTemplateConfig.features.indexOf(featureService.features.UNIKT_INTYG_INOM_VG) !== -1 &&
                    previousIntyg !== undefined && previousIntyg[intygTemplateConfig.moduleId] === true) {
                    warningForCreateTemplate = intygTemplateConfig.moduleId + '.warn.previouscertificate.samevg';
                    return false;
                }

                if (intygTemplateConfig.features.indexOf(featureService.features.UNIKT_UTKAST_INOM_VG) !== -1 &&
                    previousUtkast !== undefined) {
                    if (previousUtkast[intygTemplateConfig.moduleId] === true) {
                        warningForCreateTemplate = intygTemplateConfig.moduleId + '.warn.previousdraft.samevg';
                        return false;
                    } else if (previousUtkast[intygTemplateConfig.moduleId] === false){
                        warningForCreateTemplate = intygTemplateConfig.moduleId + '.warn.previousdraft.differentvg';
                        return true;
                    }
                }
                return true;
            };

            function intygCopyAction (intyg, intygServiceMethod, buildIntygRequestModel, newIntygType) {
                if (intyg === undefined || intyg.grundData === undefined) {
                    $log.debug('intyg or intyg.grundData is undefined. Aborting fornya.');
                    return;
                }
                var isOtherCareUnit = User.getValdVardenhet().id !== intyg.grundData.skapadAv.vardenhet.enhetsid;
                _intygActionDialog = intygServiceMethod($scope.viewState,
                    buildIntygRequestModel({
                        intygId: intyg.id,
                        intygType: intygType,
                        newIntygType: newIntygType || intygType,
                        patientPersonnummer: intyg.grundData.patient.personId
                    }),
                    isOtherCareUnit
                );
            }

            this.createFromTemplate = function(intyg, newIntygType) {
                return intygCopyAction(intyg, IntygCopyActions.createFromTemplate, IntygFornyaRequestModel.build, this.createFromTemplateConfig[$scope.intygstyp].moduleId);
            };

        }]
);
