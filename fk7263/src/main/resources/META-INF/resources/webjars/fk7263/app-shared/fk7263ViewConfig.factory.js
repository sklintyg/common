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
angular.module('fk7263').factory('fk7263.viewConfigFactory', ['uvUtil', function(uvUtil) {
    'use strict';

    var viewConfig = [
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.smittskydd.kategori',
            components: [ {
                type: 'uv-fraga',
                components: [ {
                    type: 'uv-boolean-statement',
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
                },
                {
                    type: 'uv-del-fraga',
                    labelKey: 'fk7263.label.diagnosfortydligande',
                    components: [ {
                        type: 'uv-simple-value',
                        modelProp: 'diagnosBeskrivning'
                    } ]
                }]
            }]
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
                type: 'uv-fraga',
                components: [
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.vardkontakt.undersokning',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'undersokningAvPatienten'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.vardkontakt.telefonkontakt',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'telefonkontaktMedPatienten'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.referens.journal',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'journaluppgifter'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.referens.annat',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'annanReferens'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        hideExpression: '!annanReferensBeskrivning',
                        components: [{
                            type: 'uv-simple-value',
                            modelProp: 'annanReferensBeskrivning'
                        }]
                    }
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
                type: 'uv-fraga',
                components: [
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.label.recommendations.contact.jobcenter',
                        components: [ {
                            type: 'uv-boolean-statement',
                            modelProp: 'rekommendationKontaktArbetsformedlingen'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.label.recommendations.contact.healthdepartment',
                        components: [ {
                            type: 'uv-boolean-statement',
                            modelProp: 'rekommendationKontaktForetagshalsovarden'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.label.recommendations.contact.other',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'rekommendationOvrigt'
                        } ]
                    }
                ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.plannedtreatment',
            components: [ {
                type: 'uv-fraga',
                components: [
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.label.plannedtreatment.healthcare',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'atgardInomSjukvarden'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.label.plannedtreatment.other',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'annanAtgard'
                        } ]
                    }
                ]
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
                type: 'uv-fraga',
                components: [
                    {
                        type: 'fk7263-list',
                        noValueId: 'patientworkcapacity-no-value',
                        modelProps: [{
                            modelProp: 'nuvarandeArbetsuppgifter',
                            label: 'fk7263.label.patientworkcapacity.currentwork',
                            showValue: true
                        },{
                            modelProp: 'arbetsloshet',
                            label: 'fk7263.label.patientworkcapacity.unemployed'
                        },{
                            modelProp: 'foraldrarledighet',
                            label: 'fk7263.label.patientworkcapacity.parentalleave'
                        }]
                    }
                ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.nedsattning.falt8b',
            components: [ {
                type: 'uv-fraga',
                components: [
                    {
                        type: 'fk-uv-table',
                        headers: ['Nedsatt med', 'Från och med', 'Längst till och med'],
                        rows: [{
                                label: 'fk7263.nedsattningsgrad.nedsatt_med_1_4',
                                key: 'nedsattMed25'
                            },
                            {
                                label: 'fk7263.nedsattningsgrad.nedsatt_med_1_2',
                                key: 'nedsattMed50'
                            },
                            {
                                label: 'fk7263.nedsattningsgrad.nedsatt_med_3_4',
                                key: 'nedsattMed75'
                            },
                            {
                                label: 'fk7263.nedsattningsgrad.helt_nedsatt',
                                key: 'nedsattMed100'
                            }]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.nedsattningsgrad.nedsatt_med_1_4.arbetstidsforlaggning',
                        hideExpression: '!nedsattMed25Beskrivning',
                        components: [{
                            type: 'uv-simple-value',
                            modelProp: 'nedsattMed25Beskrivning'
                        }]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.nedsattningsgrad.nedsatt_med_1_2.arbetstidsforlaggning',
                        hideExpression: '!nedsattMed50Beskrivning',
                        components: [{
                            type: 'uv-simple-value',
                            modelProp: 'nedsattMed50Beskrivning'
                        }]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'fk7263.nedsattningsgrad.nedsatt_med_3_4.arbetstidsforlaggning',
                        hideExpression: '!nedsattMed75Beskrivning',
                        components: [{
                            type: 'uv-simple-value',
                            modelProp: 'nedsattMed75Beskrivning'
                        }]
                    }
                ]
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
                },
                {
                    type: 'uv-del-fraga',
                    labelKey: 'fk7263.label.clarifying',
                    hideExpression: '!arbetsformagaPrognosGarInteAttBedomaBeskrivning',
                    components: [{
                        type: 'uv-simple-value',
                        modelProp: 'arbetsformagaPrognosGarInteAttBedomaBeskrivning'
                    }]
                }]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.othertransport',
            components: [ {
                type: 'uv-fraga',
                components: [
                    {
                        type: 'fk-uv-boolean-value',
                        modelProp: 'resaTillArbetet',
                        value: function(cert) {
                            if (cert.ressattTillArbeteAktuellt) {
                                return true;
                            }

                            if (cert.ressattTillArbeteEjAktuellt) {
                                return false;
                            }

                            return null;
                        }
                    }
                ]
            } ]
        },

        {
            type: 'uv-kategori',
            labelKey: 'fk7263.label.fkcontact',
            components: [ {
                type: 'uv-fraga',
                components: [ {
                    type: 'uv-boolean-statement',
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
        getViewConfig: function(webcert, isDraft) {
            var config = angular.copy(viewConfig);

            if (webcert) {
                config = uvUtil.convertToWebcert(config, true, isDraft);
            }

            return config;
        }
    };
} ]);
