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
angular.module('luse').factory('luse.viewConfigFactory.v1', [
    'uvUtil',
    function(uvUtil) {
        'use strict';

        var viewConfig = [
            {
                type: 'uv-kategori',
                labelKey: 'KAT_1.RBK',
                components: [ {
                    type: 'uv-fraga',
                    labelKey: 'FRG_1.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'KV_FKMU_0001.UNDERSOKNING.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'undersokningAvPatienten'
                        } ]
                    }, {
                        type: 'uv-del-fraga',
                        labelKey: 'KV_FKMU_0001.JOURNALUPPGIFTER.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'journaluppgifter'
                        } ]
                    }, {
                        type: 'uv-del-fraga',
                        labelKey: 'KV_FKMU_0001.ANHORIG.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'anhorigsBeskrivningAvPatienten'
                        } ]
                    }, {
                        type: 'uv-del-fraga',
                        labelKey: 'KV_FKMU_0001.ANNAT.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'annatGrundForMU'
                        } ]
                    }, {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_1.3.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'annatGrundForMUBeskrivning'
                        } ]
                    }, {
                        type: 'uv-del-fraga',
                        labelKey: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning',
                        hideExpression: '!motiveringTillInteBaseratPaUndersokning',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'motiveringTillInteBaseratPaUndersokning'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_2.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'kannedomOmPatient'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_3.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        components: [ {
                            type: 'uv-boolean-value',
                            modelProp: 'underlagFinns'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_4.RBK',
                    components: [ {
                        type: 'uv-table',
                        contentUrl: 'utlatande',
                        headers: ['FRG_4.RBK', 'Datum', 'DFR_4.3.RBK'], // labels for th cells
                        valueProps: ['KV_FKMU_0005.{typ}.RBK', 'datum', 'hamtasFran'], // {typ} refers to underlag.typ values
                        modelProp: 'underlag'
                    } ]
                } ]
            }, {
                type: 'uv-kategori',
                labelKey: 'KAT_4.RBK',
                components: [ {
                    type: 'uv-fraga',
                    labelKey: 'FRG_6.RBK',
                    components: [ {
                        type: 'uv-table',
                        headers: ['DFR_6.2.RBK', ''], // labels for th cells
                        valueProps: ['diagnosKod', 'diagnosBeskrivning'], // properties on diagnoser entries to use in each rows cells
                        modelProp: 'diagnoser'
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_7.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'diagnosgrund'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_45.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        components: [ {
                            type: 'uv-boolean-value',
                            modelProp: 'nyBedomningDiagnosgrund'
                        } ]
                    }, {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_45.2.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'diagnosForNyBedomning'
                        } ]
                    } ]
                } ]
            }, {
                type: 'uv-kategori',
                labelKey: 'KAT_3.RBK',
                components: [ {
                    type: 'uv-fraga',
                    labelKey: 'FRG_5.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'sjukdomsforlopp'
                        } ]
                    } ]
                } ]
            }, {
                type: 'uv-kategori',
                labelKey: 'KAT_5.RBK',
                components: [ {
                    type: 'uv-fraga',
                    labelKey: 'FRG_8.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_8.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'funktionsnedsattningIntellektuell'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_9.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_9.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'funktionsnedsattningKommunikation'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_10.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_10.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'funktionsnedsattningKoncentration'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_11.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_11.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'funktionsnedsattningPsykisk'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_12.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_12.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'funktionsnedsattningSynHorselTal'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_13.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_13.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'funktionsnedsattningBalansKoordination'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_14.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_14.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'funktionsnedsattningAnnan'
                        } ]
                    } ]
                } ]
            }, {
                type: 'uv-kategori',
                labelKey: 'KAT_6.RBK',
                components: [ {
                    type: 'uv-fraga',
                    labelKey: 'FRG_17.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_17.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'aktivitetsbegransning'
                        } ]
                    } ]
                } ]
            }, {
                type: 'uv-kategori',
                labelKey: 'KAT_7.RBK',
                components: [ {
                    type: 'uv-fraga',
                    labelKey: 'FRG_18.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_18.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'avslutadBehandling'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_19.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_19.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'pagaendeBehandling'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_20.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_20.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'planeradBehandling'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_21.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_21.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'substansintag'
                        } ]
                    } ]
                } ]
            }, {
                type: 'uv-kategori',
                labelKey: 'KAT_8.RBK',
                components: [ {
                    type: 'uv-fraga',
                    labelKey: 'FRG_22.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_22.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'medicinskaForutsattningarForArbete'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_23.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_23.1.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'formagaTrotsBegransning'
                        } ]
                    } ]
                } ]
            }, {
                type: 'uv-kategori',
                labelKey: 'KAT_9.RBK',
                components: [ {
                    type: 'uv-fraga',
                    labelKey: 'FRG_25.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'ovrigt'
                        } ]
                    } ]
                } ]
            }, {
                type: 'uv-kategori',
                labelKey: 'KAT_10.RBK',
                components: [ {
                    type: 'uv-fraga',
                    labelKey: 'FRG_26.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_26.1.RBK',
                        components: [ {
                            type: 'uv-boolean-statement',
                            modelProp: 'kontaktMedFk'
                        } ]
                    }, {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_26.2.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'anledningTillKontakt'
                        } ]
                    } ]
                } ]
            }, {
                type: 'uv-tillaggsfragor',
                labelKey: 'KAT_9999.RBK',
                modelProp: 'tillaggsfragor'
            }, {
                type: 'uv-skapad-av',
                modelProp: 'grundData.skapadAv'
            }
        ];

        return {
            getViewConfig: function(webcert) {

                var config = angular.copy(viewConfig);

                if (webcert) {
                    config = uvUtil.convertToWebcert(config, true);
                }

                return config;
            }
        };
    }

]);
