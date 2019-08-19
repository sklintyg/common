/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
    ['$log', '$stateParams', 'common.featureService', 'common.IntygViewStateService', 'common.IntygHeaderViewState',
      'common.UserModel', 'common.User', 'common.IntygCopyActions', 'common.UtkastProxy', 'common.IntygFornyaRequestModel',
      'common.IntygHelper',
      function($log, $stateParams, featureService, CommonIntygViewState, IntygHeaderViewState,
          UserModel, User, IntygCopyActions, UtkastProxy, IntygFornyaRequestModel, IntygHelper) {
        'use strict';

        var _intygActionDialog = null;

        this.closeDialogs = function() {
          if (_intygActionDialog) {
            _intygActionDialog.close();
            _intygActionDialog = undefined;
          }
        };

        this.updatePreviousIntygUtkast = function(intyg) {
          if (IntygHeaderViewState.currentCreateFromTemplateConfig && intyg) {
            UtkastProxy.getPrevious(intyg.grundData.patient.personId, function(existing) {
              IntygHeaderViewState.setPreviousIntygUtkast(existing.intyg, existing.utkast);
            });
          }
        };

        this.intygCopyAction = function(intyg, intygServiceMethod, buildIntygRequestModel, newIntygType) {
          if (intyg === undefined || intyg.grundData === undefined) {
            $log.debug('intyg or intyg.grundData is undefined. Aborting fornya.');
            return;
          }
          IntygHeaderViewState.utkastCreatedFrom = intyg.id;
          var isOtherCareUnit = User.getValdVardenhet().id !== intyg.grundData.skapadAv.vardenhet.enhetsid;
          _intygActionDialog = intygServiceMethod(IntygHeaderViewState.intygViewState,
              buildIntygRequestModel({
                intygId: intyg.id,
                intygType: IntygHeaderViewState.intygType,
                newIntygType: newIntygType || IntygHeaderViewState.intygType,
                patientPersonnummer: intyg.grundData.patient.personId
              }),
              isOtherCareUnit,
              IntygHeaderViewState.getPreviousIntyg()
          );
        };

        this.showCreateFromTemplate = function() {
          return IntygHeaderViewState.currentCreateFromTemplateConfig !== undefined &&
              !CommonIntygViewState.isRevoked() &&
              !CommonIntygViewState.isReplaced() &&
              !CommonIntygViewState.isComplementedByIntyg() &&
              !UserModel.getIntegrationParam('inactiveUnit') &&
              !this.showGotoCreatedFromTemplate() &&
              this.passesOriginCheck(IntygHeaderViewState.currentCreateFromTemplateConfig.origins, User.getUser().origin) &&
              featureService.isFeatureActive(featureService.features.HANTERA_INTYGSUTKAST,
                  IntygHeaderViewState.currentCreateFromTemplateConfig.moduleId);
        };

        this.passesOriginCheck = function(validOriginsContraint, actualOrigin) {
          if (!validOriginsContraint) {
            return true;
          }
          return validOriginsContraint.indexOf(actualOrigin) > -1;
        };

        this.showGotoCreatedFromTemplate = function() {
          var intygTemplateConfig = IntygHeaderViewState.currentCreateFromTemplateConfig;
          if (intygTemplateConfig && intygTemplateConfig.features.indexOf(featureService.features.UNIKT_INTYG_INOM_VG) !== -1 &&
              IntygHeaderViewState.checkIntygModuleId(intygTemplateConfig.moduleId)) {
            return true;
          }
          if (intygTemplateConfig && intygTemplateConfig.features.indexOf(featureService.features.UNIKT_UTKAST_INOM_VG) !== -1 &&
              IntygHeaderViewState.checkUtkastModuleId(intygTemplateConfig.moduleId)) {
            return true;
          }
          return false;
        };

        this.createFromTemplate = function(intyg) {
          return this.intygCopyAction(intyg, IntygCopyActions.createFromTemplate, IntygFornyaRequestModel.build,
              IntygHeaderViewState.currentCreateFromTemplateConfig.moduleId);
        };

        this.gotoRelatedFromTemplate = function() {
          var intygTemplateConfig = IntygHeaderViewState.currentCreateFromTemplateConfig;
          if (intygTemplateConfig.features.indexOf(featureService.features.UNIKT_INTYG_INOM_VG) !== -1 &&
              IntygHeaderViewState.checkUtkastModuleId(intygTemplateConfig.moduleId)) {
            var utkastId = IntygHeaderViewState.getUtkastIntygsIdForModuleId(intygTemplateConfig.moduleId);
            IntygHelper.goToDraft(intygTemplateConfig.moduleId, $stateParams.intygTypeVersion, utkastId);
          } else if (intygTemplateConfig.features.indexOf(featureService.features.UNIKT_UTKAST_INOM_VG) !== -1 &&
              IntygHeaderViewState.checkIntygModuleId(intygTemplateConfig.moduleId)) {
            var intygsId = IntygHeaderViewState.getIntygIntygsIdForModuleId(intygTemplateConfig.moduleId);
            IntygHelper.goToIntyg(intygTemplateConfig.moduleId, $stateParams.intygTypeVersion, intygsId);
          }
        };

        this.getCurrentCreateFromTemplate = function() {
          return IntygHeaderViewState.currentCreateFromTemplateConfig;
        };

        this.enableCreateFromTemplate = function() {
          var intygTemplateConfig = IntygHeaderViewState.currentCreateFromTemplateConfig;
          if (intygTemplateConfig.features.indexOf(featureService.features.UNIKT_INTYG_INOM_VG) !== -1 &&
              IntygHeaderViewState.checkIntygModuleId(intygTemplateConfig.moduleId)) {
            IntygHeaderViewState.warningForCreateTemplate = intygTemplateConfig.moduleId + '.warn.previouscertificate.samevg';
            return false;
          }

          if (intygTemplateConfig.features.indexOf(featureService.features.UNIKT_UTKAST_INOM_VG) !== -1) {
            if (IntygHeaderViewState.checkUtkastModuleId(intygTemplateConfig.moduleId)) {
              IntygHeaderViewState.warningForCreateTemplate = intygTemplateConfig.moduleId + '.warn.previousdraft.samevg';
              return false;
            } else if (IntygHeaderViewState.checkUtkastModuleIdDifferent(intygTemplateConfig.moduleId)) {
              IntygHeaderViewState.warningForCreateTemplate = intygTemplateConfig.moduleId + '.warn.previousdraft.differentvg';
              return true;
            }
          }
          return true;
        };

      }]
);
