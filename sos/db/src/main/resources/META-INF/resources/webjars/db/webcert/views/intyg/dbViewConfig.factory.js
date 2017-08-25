angular.module('db').factory('db.viewConfigFactory', [ 'uvUtil', function(uvUtil) {
    'use strict';

    var viewConfig = [ {
        type: 'uv-kategori',
        labelKey: 'KAT_1.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'DFR_1.1.RBK',
            components: [ {
                type: 'uv-del-fraga',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'identitetStyrkt'
                } ]
            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_2.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_2.RBK',
            components: [{
/*                type: 'uv-boolean-value',
                yesLabel: 'DFR_2.1.SVA_1',
                noLabel: 'DFR_2.1.SVA_2',*/
                type: 'uv-enum-value',
                values: {
                    true : 'DFR_2.1.SVA_1',
                    false: 'DFR_2.1.SVA_2'
                },
                modelProp: 'dodsdatumSakert'
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_2.2.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'dodsdatum'
                }]
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_2.3.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'antraffatDodDatum'
                }]
            }]
        }, {
            type: 'uv-fraga',
            labelKey: 'FRG_3.RBK',
            components: [ {
                type: 'uv-del-fraga',
                labelKey: 'DFR_3.1.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'dodsplatsKommun'
                } ]

            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_3.2.RBK',
                components: [ {
                    type: 'uv-kodverk-value',
                    kvModelProps: ['dodsplatsBoende'],
                    kvLabelKeys: ['KV_DODSPLATS_BOENDE.{var}.RBK']
                } ]

            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_3.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'DFR_4.1.RBK',
            components: [ {
                type: 'uv-boolean-value',
                modelProp: 'barn'
            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_4.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'DFR_5.1.RBK',
            components: [ {
                type: 'uv-boolean-value',
                modelProp: 'explosivImplantat'
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_5.2.RBK',
                components: [ {
                    type: 'uv-boolean-value',
                    modelProp: 'explosivAvlagsnat'
                } ]
            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_5.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'DFR_6.1.RBK',
            components: [ {
                type: 'uv-kodverk-value',
                kvModelProps: ['undersokningYttre'],
                kvLabelKeys: ['KV_DETALJER_UNDERSOKNING.{var}.RBK']
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_6.3.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'undersokningDatum'
                } ]
            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_6.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'DFR_7.1.RBK',
            components: [ {
                type: 'uv-boolean-value',
                modelProp: 'polisanmalan'
            } ]
        } ]
    }, {
        type: 'uv-skapad-av',
        modelProp: 'grundData.skapadAv'
    } ];

    return {
        getViewConfig: function(webcert) {
            var config = angular.copy(viewConfig);

            if (webcert) {
                config = uvUtil.convertToWebcert(config);
            }

            return config;
        }
    };
} ]);
