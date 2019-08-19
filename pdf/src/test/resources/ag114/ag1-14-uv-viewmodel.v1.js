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
var viewConfig = [
    {
        type: 'uv-kategori',
        labelKey: 'KAT_1.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_1.RBK',
            components: [{
                type: 'uv-del-fraga',
                labelKey: '',
                components: [{
                    type: 'uv-list',
                    labelKey: 'KV_FKMU_0002.{var}.RBK', // {var} is a placeholder for sysselsattning.typ values
                    listKey: 'typ', // name of property on modelProp to use on each row
                    modelProp: 'sysselsattning'
                }]
            }, {
                type: 'uv-del-fraga',
                labelKey: 'FRG_2.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'nuvarandeArbete'
                }]
            }]
        }]
    },
    {
        type: 'uv-kategori',
        labelKey: 'KAT_2.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_3.RBK',
            components: [{
                type: 'uv-boolean-value',
                modelProp: 'onskarFormedlaDiagnos'
            }]
        },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_4.RBK',
                components: [{
                    type: 'uv-table',
                    headers: ['DFR_4.1.RBK', ''], // labels for th cells
                    valueProps: ['diagnosKod', 'diagnosBeskrivning'], // properties on diagnoser entries to use in each rows cells
                    modelProp: 'diagnoser'
                }]
            }]
    },
    {
        type: 'uv-kategori',
        labelKey: 'KAT_3.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_5.RBK',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'nedsattArbetsformaga'
            }]
        },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_6.RBK',
                components: [{
                    type: 'uv-del-fraga',
                    components: [{
                        type: 'uv-boolean-value',
                        modelProp: 'arbetsformagaTrotsSjukdom'
                    }]
                }, {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_6.2.RBK',
                    components: [{
                        type: 'uv-simple-value',
                        modelProp: 'arbetsformagaTrotsSjukdomBeskrivning'
                    }]
                }]
            }]
    },
    {
        type: 'uv-kategori',
        labelKey: 'KAT_4.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_7.RBK',
            components: [{
                type: 'uv-del-fraga',
                labelKey: 'DFR_7.1.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'sjukskrivningsgrad',
                    unit: '%'
                }]
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_7.2.RBK',
                components: [{
                    type: 'uv-template-string',
                    template: 'Fr.o.m {0} t.o.m {1}',
                    variables: ['from', 'tom'],
                    modelProp: 'sjukskrivningsperiod'
                }, {
                    type: 'uv-alert-value',
                    showExpression: function(model) {
                        // Pure javascript function to determine if message shold be displayed.
                        // This is because we want to use the same expression in pdf uv-config.
                        if (model.sjukskrivningsperiod && model.sjukskrivningsperiod.from && model.sjukskrivningsperiod.tom) {

                            var msPerDay = 86400000;

                            // Convert both dates to milliseconds
                            var fromMs = new Date(model.sjukskrivningsperiod.from).getTime();
                            var toMs = new Date(model.sjukskrivningsperiod.tom).getTime();

                            // Calculate the difference in milliseconds
                            var differenceMs = toMs - fromMs;
                            // Convert back to days
                            var daysBetween = Math.round(differenceMs / msPerDay) + 1;

                            return daysBetween > 14;
                        }
                        return false;
                    },
                    labelKey: 'SKL-001.ALERT',
                    alertLevel: 'warning'
                }]
            }]
        }]
    },
    {
        type: 'uv-kategori',
        labelKey: 'KAT_5.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_8.RBK',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'ovrigaUpplysningar'
            }]
        }]
    },
    {
        type: 'uv-kategori',
        labelKey: 'KAT_6.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'DFR_9.1.RBK',
            components: [{
                type: 'uv-boolean-value',
                modelProp: 'kontaktMedArbetsgivaren'
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_9.2.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'anledningTillKontakt'
                }]
            }]
        }]
    },
    {
        type: 'uv-skapad-av',
        modelProp: 'grundData.skapadAv'
    }
];