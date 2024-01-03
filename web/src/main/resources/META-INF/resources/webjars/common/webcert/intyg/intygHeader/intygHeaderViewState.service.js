/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').service('common.IntygHeaderViewState', [
    'common.featureService', 'common.moduleService', 'common.messageService',
        function(featureService, moduleService, messageService) {
            'use strict';

            this.reset = function() {
                /*
                // Current intyg view state
                */
                this.intygViewState = {};
                this.intygType = null;
                this.intygLoaded = false;
                this.arendenLoaded = false;
            };

            this.reset();

            this.utkastCreatedFrom = null;

            /*
            // Create from template state
            */
            var createFromTemplateConfig = {
                db: {
                    moduleId: 'doi',
                    name: 'dödsorsaksintyg',
                    buttonText: '', // generated from name and message string
                    tooltip: '', // generated from name and message string
                    features: [
                        featureService.features.UNIKT_INTYG_INOM_VG,
                        featureService.features.UNIKT_UTKAST_INOM_VG
                    ],
                    origins: ['NORMAL']
                },
                lisjp: {
                    moduleId: 'ag7804',
                    name: 'AG7804',
                    buttonText: '', // generated from name and message string
                    tooltip: '', // generated from name and message string
                    features: [],
                    origins: ['NORMAL']
                }
            };

            this.warningForCreateTemplate = {};
            this.currentCreateFromTemplateConfig = undefined;

            this.setIntygViewState = function(intygViewState, intygType) {
                this.intygViewState = intygViewState;
                this.intygType = intygType;
                this.currentCreateFromTemplateConfig = createFromTemplateConfig[intygType];
                if(this.currentCreateFromTemplateConfig){
                    this.currentCreateFromTemplateConfig.buttonText = messageService.getProperty('common.createfromtemplate', {intygName: this.currentCreateFromTemplateConfig.name});
                    this.currentCreateFromTemplateConfig.gotoButtonText = messageService.getProperty('common.gotofromtemplate', {intygName: this.currentCreateFromTemplateConfig.name});
                    var intygSpecificTooltipKey = intygType + '.createfromtemplate.' + this.currentCreateFromTemplateConfig.moduleId + '.tooltip';
                    if (messageService.propertyExists(intygSpecificTooltipKey)) {
                        this.currentCreateFromTemplateConfig.tooltip = messageService.getProperty(intygSpecificTooltipKey);
                    }
                    else {
                        this.currentCreateFromTemplateConfig.tooltip =
                            messageService.getProperty('common.createfromtemplate.tooltip',
                                {
                                    newIntygName: this.currentCreateFromTemplateConfig.name,
                                    currentIntygName: moduleService.getModuleName(intygType)
                                });
                    }
                    this.currentCreateFromTemplateConfig.gotoButtonTooltip = messageService.getProperty('db.gotocreatefromtemplate.doi.tooltip');
                }

                this.recipientId = moduleService.getModule(intygType).defaultRecipient;
                this.recipientText = messageService.getProperty('common.recipient.' + this.recipientId.toLowerCase());
            };

            /*
            // Previous intyg/utkast state
            */
            var previousIntyg = {};
            var previousUtkast = {};

            this.setPreviousIntygUtkast = function(prevIntyg, prevUtkast){
                previousIntyg = prevIntyg;
                previousUtkast = prevUtkast;
            };

            this.checkIfShowDoiEnabledForIntyg = function(moduleId){
                return previousIntyg !== undefined && previousIntyg[moduleId] && previousIntyg[moduleId].enableShowDoiButton;
            };

            this.checkIfShowDoiEnabledForUtkast = function(moduleId){
                return previousUtkast !== undefined && previousUtkast[moduleId] && previousUtkast[moduleId].enableShowDoiButton;
            };

            this.checkIntygModuleId = function(moduleId){
                return previousIntyg !== undefined && previousIntyg[moduleId] && previousIntyg[moduleId].sameVardgivare;
            };

            this.checkUtkastModuleId = function(moduleId){
                return previousUtkast !== undefined && previousUtkast[moduleId] && previousUtkast[moduleId].sameVardgivare;
            };

            this.checkUtkastModuleIdDifferent = function(moduleId){
                return previousUtkast !== undefined && previousUtkast[moduleId] && !previousUtkast[moduleId].sameVardgivare;
            };

            this.checkIntygModuleId = function(moduleId){
                return previousIntyg !== undefined && previousIntyg[moduleId] && previousIntyg[moduleId].sameVardgivare;
            };

            this.getPreviousIntyg = function(){
                return previousIntyg;
            };

            this.getUtkastIntygsIdForModuleId = function(moduleId){
                if (previousUtkast && previousUtkast[moduleId]) {
                    return previousUtkast[moduleId].latestIntygsId;
                }
            };

            this.getIntygIntygsIdForModuleId = function(moduleId){
                if (previousIntyg && previousIntyg[moduleId]) {
                    return previousIntyg[moduleId].latestIntygsId;
                }
            };

        }
    ]
);
