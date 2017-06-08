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

angular.module('fk7263').factory('fk7263.viewConfigFactory', [ function() {
    'use strict';

    var viewConfig = [

        // TODO: Add sjukskrivningsgrad

        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.smittskydd',
            components: [ {
                type: 'uv-fraga',
                components: [ {
                    type: 'uv-boolean-value',
                    modelProp: 'avstangningSmittskydd'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.diagnosis',
            components: [{
                type: 'uv-fraga',
                labelKey: 'fk7263.label.diagnosisCode',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'diagnosKod'
                }]
            }, {
                // TODO: hide if empty
                type: 'uv-fraga',
                labelKey: 'fk7263.label.diagnosfortydligande',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'diagnosBeskrivning'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.progressofdesease',
            components: [ {
                type: 'uv-fraga',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'sjukdomsforlopp'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.disabilities',
            components: [ {
                type: 'uv-fraga',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'funktionsnedsattning'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.basedon',
            components: [ {
                // TODO: Add value
                type: 'uv-fraga',
                components: [
                 ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.limitation',
            components: [ {
                type: 'uv-fraga',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'aktivitetsbegransning'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.recommendations',
            components: [ {
                // TODO: Add value
                type: 'uv-fraga',
                components: []
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.plannedtreatment',
            components: [ {
                // TODO: Add value
                type: 'uv-fraga',
                components: []
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.workrehab',
            components: [ {
                type: 'uv-fraga',
                components: [{
                    type: 'uv-enum-value',
                    modelProp: 'rehabilitering',
                    values: {
                        'rehabiliteringAktuell' : 'fk7263.label.yes',
                        'rehabiliteringEjAktuell': 'fk7263.label.no',
                        'rehabiliteringGarInteAttBedoma':'fk7263.label.unjudgeable'
                    }
                }]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.patientworkcapacity',
            components: [ {
                // TODO: Add value
                type: 'uv-fraga',
                components: []
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.patientworkcapacityjudgement',
            components: [ {
                type: 'uv-fraga',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'arbetsformagaPrognos'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.prognosis',
            components: [ {
                type: 'uv-fraga',
                components: [{
                    type: 'uv-enum-value',
                    modelProp: 'prognosBedomning',
                    values: {
                        'arbetsformagaPrognosJa' : 'fk7263.label.yes',
                        'arbetsformagaPrognosJaDelvis': 'fk7263.label.partialyes',
                        'arbetsformagaPrognosNej':'fk7263.label.no',
                        'arbetsformagaPrognosGarInteAttBedoma':'fk7263.label.unjudgeable'
                    }
                }]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.othertransport',
            components: [ {
                type: 'uv-fraga',
                // TODO: Add value
                components: [ {
                    /*<span ng-show="cert.ressattTillArbeteAktuellt">
                     <span id="resaTillArbetet-yes" message key="fk7263.label.yes"></span>
                     </span>
                     <span ng-show="cert.ressattTillArbeteEjAktuellt">
                     <span id="resaTillArbetet-no" message key="fk7263.label.no"></span>
                     </span>*/
                } ]
            } ]
        },

        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.fkcontact',
            components: [ {
                type: 'uv-fraga',
                components: [ {
                    type: 'uv-boolean-value',
                    modelProp: 'kontaktMedFk'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.otherinformation',
            components: [ {
                type: 'uv-fraga',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'kommentar'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.workcodes',
            components: [ {
                type: 'uv-fraga',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'forskrivarkodOchArbetsplatskod'
                } ]
            } ]
        },
        {
            type: 'uv-skapad-av',
            modelProp: 'grundData.skapadAv'
        }
    ];

    return {
        getViewConfig: function() {
            return angular.copy(viewConfig);
        }
    };
} ]);
