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
angular.module('common').service('common.IntygHeaderViewState', [
    'common.featureService',
        function(featureService) {
            'use strict';

            /*
            // Current intyg view state
            */
            this.intygViewState = {};
            this.intygType = null;

            this.setIntygViewState = function(intygViewState, intygType) {
                intygViewState = intygViewState;
                this.intygType = intygType;
                this.currentCreateFromTemplateConfig = createFromTemplateConfig[intygViewState.type];
            }

            /*
            // Previous intyg/utkast state
            */
            var previousIntyg = {};
            var previousUtkast = {};

            this.setPreviousIntygUtkast = function(prevIntyg, prevUtkast){
                this.previousIntyg = prevIntyg;
                this.previousUtkast = prevUtkast;
            }

            this.checkIntygModuleId = function(moduleId){
                return previousIntyg !== undefined && previousIntyg[moduleId] === true;
            }

            this.checkUtkastModuleId = function(moduleId){
                return previousUtkast !== undefined && previousUtkast[moduleId] === true;
            }

            this.checkUtkastModuleIdDifferent = function(moduleId){
                return previousUtkast !== undefined && previousUtkast[moduleId] === false;
            }

            /*
            // Create from template state
            */
            var createFromTemplateConfig = {
                db: {
                    moduleId: 'doi',
                    name: 'dödsorsaksintyg',
                    features: [
                        featureService.features.UNIKT_INTYG_INOM_VG,
                        featureService.features.UNIKT_UTKAST_INOM_VG
                    ]
                }
            };

            this.warningForCreateTemplate = {};
            this.currentCreateFromTemplateConfig = null;
        }
    ]
);
