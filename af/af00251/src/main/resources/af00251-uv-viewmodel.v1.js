/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
var viewConfig = [{
    type: 'uv-kategori',
    labelKey: 'KAT_1.RBK',
    components: [{
        type: 'uv-fraga',
        labelKey: 'FRG_1.RBK',
        components: [
            {
                type: 'uv-del-fraga',
                labelKey: 'KV_FKMU_0001.UNDERSOKNING.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'undersokningsDatum'
                }]
            },
            {
                type: 'uv-del-fraga',
                labelKey: 'KV_FKMU_0001.ANNAT.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'annatDatum'
                }]
            },
            {
                type: 'uv-del-fraga',
                labelKey: 'DFR_1.3.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'annatBeskrivning'
                }]
            }
        ]
    }]
}, {
    type: 'uv-kategori',
    labelKey: 'KAT_2.RBK',
    components: [{
        type: 'uv-fraga',
        labelKey: 'FRG_2.RBK',
        components: [{
            type: 'uv-del-fraga',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'arbetsmarknadspolitisktProgram.medicinskBedomning'
            }]
        }, {
            type: 'uv-del-fraga',
            labelKey: 'DFR_2.2.RBK',
            components: [{
                type: 'uv-kodverk-value',
                kvModelProps: ['arbetsmarknadspolitisktProgram.omfattning'],
                kvLabelKeys: ['OMFATTNING.PROGRAM_{var}.RBK']
            }]
        }, {
            type: 'uv-del-fraga',
            labelKey: 'DFR_2.3.RBK',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'arbetsmarknadspolitisktProgram.omfattningDeltid'
            }]
        }]
    }]
}, {
    type: 'uv-kategori',
    labelKey: 'KAT_3.RBK',
    components: [{
        type: 'uv-fraga',
        labelKey: 'FRG_3.RBK',
        components: [{
            type: 'uv-del-fraga',
            labelKey: 'DFR_3.1.RBK',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'funktionsnedsattning'
            }]
        }]
    }, {
        type: 'uv-fraga',
        labelKey: 'FRG_4.RBK',
        components: [{
            type: 'uv-del-fraga',
            labelKey: 'DFR_4.1.RBK',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'aktivitetsbegransning'
            }]
        }]
    }]
}, {
    type: 'uv-kategori',
    labelKey: 'KAT_4.RBK',
    components: [{
        type: 'uv-fraga',
        labelKey: 'FRG_5.RBK',
        components: [{
            type: 'uv-del-fraga',
            labelKey: 'DFR_5.1.RBK',
            components: [{
                type: 'uv-boolean-value',
                modelProp: 'harForhinder'
            }]
        }]
    }, {
        type: 'uv-fraga',
        labelKey: 'FRG_6.RBK',

        components: [{
            type: 'uv-del-fraga',
            labelKey: 'DFR_6.1.RBK',
            components: [{
                type: 'uv-table',
                headers: ['DFR_6.1.OMFATTNING.RBK', 'DFR_6.1.FROM.RBK', 'DFR_6.1.TOM.RBK'],
                valueProps: [
                    'niva',
                    'period.from',
                    'period.tom'
                ],
                modelProp: 'sjukfranvaro'
            }]
        }]
    }, {
        type: 'uv-fraga',
        labelKey: 'FRG_7.RBK',
        components: [{
            type: 'uv-del-fraga',
            labelKey: 'DFR_7.1.RBK',
            components: [{
                type: 'uv-boolean-value',
                modelProp: 'begransningSjukfranvaro.kanBegransas'
            }]
        }, {
            type: 'uv-del-fraga',
            labelKey: 'DFR_7.2.RBK',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'begransningSjukfranvaro.beskrivning'
            }]
        }]
    }, {
        type: 'uv-fraga',
        labelKey: 'FRG_8.RBK',
        components: [{
            type: 'uv-del-fraga',
            labelKey: 'DFR_8.1.RBK',
            components: [{
                type: 'uv-kodverk-value',
                kvModelProps: ['prognosAtergang.prognos'],
                kvLabelKeys: ['PROGNOS_ATERGANG.{var}.RBK']
            }]
        }, {
            type: 'uv-del-fraga',
            labelKey: 'DFR_8.2.RBK',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'prognosAtergang.anpassningar'
            }]
        }]
    }]
}, {
    type: 'uv-skapad-av',
    modelProp: 'grundData.skapadAv'
}];