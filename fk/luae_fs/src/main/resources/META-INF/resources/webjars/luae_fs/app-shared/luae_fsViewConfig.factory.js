angular.module('luae_fs').factory('luae_fs.viewConfigFactory', ['uvUtil', function (uvUtil) {
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
                        headers: ['', '', 'DFR_4.3.RBK'], // labels for th cells
                        valueProps: ['KV_FKMU_0005.{typ}.RBK', 'datum', 'hamtasFran'], // {typ} refers to underlag.typ values
                        modelProp: 'underlag'
                    } ]
                } ]
            },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_3.RBK',
            components: [{
                type: 'uv-fraga',
                labelKey: 'FRG_6.RBK',
                components: [{
                    type: 'uv-table',
                    headers: ['DFR_6.2.RBK', ''], // labels for th cells
                    valueProps: ['diagnosKod', 'diagnosBeskrivning'], // properties on diagnoser entries to use in each rows cells
                    modelProp: 'diagnoser'
                }]
            }]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_4.RBK',
            components: [ {
                type: 'uv-fraga',
                labelKey: 'FRG_15.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'funktionsnedsattningDebut'
                } ]
            }, {
                type: 'uv-fraga',
                labelKey: 'FRG_16.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'funktionsnedsattningPaverkan'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_5.RBK',
            components: [{
                type: 'uv-fraga',
                labelKey: 'FRG_25.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'ovrigt'
                }]
            }]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_6.RBK',
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
        },
        {
            type: 'uv-skapad-av',
            modelProp: 'grundData.skapadAv'
        }
    ];

    return {
        getViewConfig: function(webcert) {

            var config = angular.copy(viewConfig);

            if (webcert) {
                config = uvUtil.convertToWebcert(config);
            }

            return config;
        }
    };
}]);
