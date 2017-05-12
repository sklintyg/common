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
    },

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
        components: [ {
            type: 'uv-fraga',
            labelKey: 'FRG_28.RBK',
            components: [ {
                type: 'uv-list',
                labelKey: 'KV_FKMU_0002.{var}.RBK', // {var} is a placeholder for sysselsattning.typ values
                listKey: 'typ', // name of property on modelProp to use on each row
                modelProp: 'sysselsattning'
            }, {
                type: 'uv-fraga',
                labelKey: 'FRG_29.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'nuvarandeArbete'
                } ]
            },  {
                type: 'uv-fraga',
                labelKey: 'FRG_30.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'arbetsmarknadspolitisktProgram'
                } ]
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
    } ];

    return {
        getViewConfig: function() {
            return angular.copy(viewConfig);
        }
    };
} ]);
