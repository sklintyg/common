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
                    type: 'uv-simple-value',
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
                    type: 'uv-simple-value'
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
                type: 'sysselsattning',
                modelProp: 'sysselsattning'
            }, {
                type: 'uv-fraga',
                labelKey: 'FRG_29.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'nuvarandeArbete'
                } ]
            }, {
                type: 'uv-simple-value',
                labelKey: 'DFR_30.1.RBK',
                modelProp: 'arbetsmarknadspolitisktProgram'
            } ]
        } ]
    } ];

    return {
        getViewConfig: function() {
            return angular.copy(viewConfig);
        }
    };
} ]);
