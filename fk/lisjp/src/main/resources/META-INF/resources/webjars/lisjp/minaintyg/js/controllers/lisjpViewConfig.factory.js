angular.module('lisjp').factory('lisjp.viewConfigFactory', [ '$log', function($log) {
    'use strict';

    var viewConfig = [ {
        type: 'uv-kategori',
        labelKey: 'KAT_10.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'FRG_27.RBK',
            components: [ {
                type: 'uv-del-fraga',
                components: [ {
                    type: 'uv-boolean-value',
                    modelProp: 'avstangningSmittskydd'
                } ]
            } ]
        } ]
    }, {
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
                labelKey: 'KV_FKMU_0001.TELEFONKONTAKT.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'telefonkontaktMedPatienten'
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

            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_2.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_28.RBK',
                components: [ {
                    type: 'uv-list',
                    labelKey: 'KV_FKMU_0002.{var}.RBK', // {var} is a placeholder for sysselsattning.typ values
                    listKey: 'typ', // name of property on modelProp to use on each row
                    modelProp: 'sysselsattning'
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_29.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'nuvarandeArbete'
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_30.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'arbetsmarknadspolitisktProgram'
                } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_3.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'FRG_6.RBK',
            components: [ {
                type: 'uv-table',
                headers: ['DFR_6.2.RBK', 'DFR_6.1.RBK'], // labels for th cells
                valueProps: ['diagnosKod', 'diagnosBeskrivning'], // properties on diagnoser entries to use in each rows cells
                modelProp: 'diagnoser'
            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_4.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'DFR_35.1.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'funktionsnedsattning'
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'DFR_17.1.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'aktivitetsbegransning'
                } ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_5.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'DFR_19.1.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'pagaendeBehandling'
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'DFR_20.1.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'planeradBehandling'
                } ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_6.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_32.RBK',
                components: [
                    {
                        type: 'uv-table',
                        headers: ['Från och med', 'Till och med'],
                        valueProps: ['period.from', 'period.tom'], // TODO: needs to support dot reference
                        modelProp: 'sjukskrivningar'
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_37.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'forsakringsmedicinsktBeslutsstod'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'FRG_33.RBK',
                        components: [
                            {
                                type: 'uv-boolean-value',
                                modelProp: 'arbetstidsforlaggning'
                            },
                            {
                                type: 'uv-simple-value',
                                modelProp: 'arbetstidsforlaggningMotivering'
                            }
                        ]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_34.RBK',
                        components: [ {
                            type: 'uv-boolean-value',
                            modelProp: 'arbetsresor'
                        } ]
                    },
                    {
                        type: 'uv-fraga',
                        labelKey: 'FRG_39.RBK', // TODO: Needs custom component
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'prognos.typ'
                        } ]
                    }
                ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_7.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_40.RBK',
                components: [ {
                    type: 'uv-list',
                    labelKey: 'KV_FKMU_0004.{var}.RBK',
                    listKey: 'typ',
                    modelProp: 'arbetslivsinriktadeAtgarder'
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_44.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'arbetslivsinriktadeAtgarderBeskrivning'
                } ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_8.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_25.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'ovrigt'
                } ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_9.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_26.RBK',
                components: [
                    {
                        type: 'uv-boolean-value',
                        labelKey: 'DFR_26.1.RBK',
                        modelProp: 'kontaktMedFk'
                    },
                    {
                        type: 'uv-simple-value',
                        labelKey: 'DFR_26.2.RBK',
                        modelProp: 'anledningTillKontakt'
                    }
                ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_9999.RBK',
        components: [
            // TODO: needs custom component
        ]
    } ];

    return {
        getViewConfig: function() {
            return angular.copy(viewConfig);
        }
    };
} ]);
