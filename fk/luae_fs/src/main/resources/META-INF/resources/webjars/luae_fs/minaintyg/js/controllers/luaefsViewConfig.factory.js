angular.module('luae_fs').factory('luae_fs.viewConfigFactory', [ '$log', function($log) {
    'use strict';

    var viewConfig = [
        {
        type: 'uv-kategori',
        labelKey: 'FRG_1.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'KV_FKMU_0001.UNDERSOKNING.RBK',
            components: [ {
                type: 'uvsimple-value',
                modelProp: ''
            } ]
        } ]
        },
        {
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
        } ];

    return {
        getViewConfig: function() {
            return angular.copy(viewConfig);
        }
    };
} ]);
