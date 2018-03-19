/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

angular.module('lisjp').factory('lisjp.UtkastConfigFactory',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ueFactoryTemplatesHelper',
        function($log, $timeout, DateUtils, ueFactoryTemplates) {
            'use strict';

            var categoryIds = {
                1: 'grundformu',
                2: 'sysselsattning',
                3: 'diagnos',
                4: 'funktionsnedsattning',
                5: 'medicinskaBehandlingar',
                6: 'bedomning',
                7: 'atgarder',
                8: 'ovrigt',
                9: 'kontakt',
                10: 'smittbararpenning'
            };

            var kategori = ueFactoryTemplates.kategori;
            var fraga = ueFactoryTemplates.fraga;

            var config = [

                kategori(categoryIds[10], 'KAT_10.RBK', 'KAT_10.HLP', {}, [
                    fraga(27, '', '', {}, [{
                        type: 'ue-checkbox',
                        modelProp: 'avstangningSmittskydd',
                        label: {
                            key: 'FRG_27.RBK',
                            helpKey: 'FRG_27.HLP'
                        }
                    }])
                ]),

                kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                    fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', { validationContext: {key: 'baseratPa', type: 'ue-checkgroup'}, required: true }, [{
                        label: {
                            key: 'KV_FKMU_0001.UNDERSOKNING.RBK',
                            helpKey: 'KV_FKMU_0001.UNDERSOKNING.HLP'
                        },
                        type: 'ue-checkbox-date',
                        modelProp: 'undersokningAvPatienten',
                        paddingBottom: true
                    }, {
                        label: {
                            key: 'KV_FKMU_0001.TELEFONKONTAKT.RBK',
                            helpKey: 'KV_FKMU_0001.TELEFONKONTAKT.HLP'
                        },
                        type: 'ue-checkbox-date',
                        modelProp: 'telefonkontaktMedPatienten',
                        paddingBottom: true
                    }, {
                        label: {
                            key: 'KV_FKMU_0001.JOURNALUPPGIFTER.RBK',
                            helpKey: 'KV_FKMU_0001.JOURNALUPPGIFTER.HLP'
                        },
                        type: 'ue-checkbox-date',
                        modelProp: 'journaluppgifter',
                        paddingBottom: true
                    }, {
                        label: {
                            key: 'KV_FKMU_0001.ANNAT.RBK',
                            helpKey: 'KV_FKMU_0001.ANNAT.HLP'
                        },
                        type: 'ue-checkbox-date',
                        modelProp: 'annatGrundForMU'
                    }, {
                        label: {
                            key: 'DFR_1.3.RBK',
                            helpKey: 'DFR_1.3.HLP',
                            required: true
                        },
                        type: 'ue-textfield',
                        hideExpression: '!model.annatGrundForMU',
                        modelProp: 'annatGrundForMUBeskrivning'
                    }]), fraga(1, '', '', { hideExpression: 'model.undersokningAvPatienten || !(model.telefonkontaktMedPatienten || model.journaluppgifter || model.annatGrundForMU)' }, [{
                        type: 'ue-textarea',
                        label: {
                            bold: 'bold',
                            key: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning',
                            required: true,
                            type: 'label'
                        },
                        modelProp: 'motiveringTillInteBaseratPaUndersokning'
                    }, {
                        type: 'ue-text',
                        label: {
                            htmlClass: 'info-transfer',
                            key: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning.info',
                            variableLabelKey: 'FRG_25.RBK'
                        }
                    }])
                ]),

                kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                    fraga(28, 'FRG_28.RBK', 'FRG_28.HLP', { required:true }, [{
                        type: 'ue-checkgroup',
                        modelProp: 'sysselsattning',
                        code: 'KV_FKMU_0002',
                        choices: ['NUVARANDE_ARBETE',
                            'ARBETSSOKANDE',
                            'FORALDRALEDIG',
                            'STUDIER'
                        ]
                    }]), fraga(29, 'FRG_29.RBK', 'FRG_29.HLP', { required: true, hideExpression: '!model.sysselsattning["NUVARANDE_ARBETE"]' }, [{
                        type: 'ue-textarea',
                        modelProp: 'nuvarandeArbete'
                    }])
                ]),

                kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', {}, [
                    fraga(6, 'FRG_6.RBK', 'FRG_6.HLP', { required: true}, [{
                        type: 'ue-diagnos',
                        modelProp: 'diagnoser',
                        diagnosBeskrivningLabel: 'DFR_6.1.RBK',
                        diagnosBeskrivningHelp: 'DFR_6.1.HLP',
                        diagnosKodLabel: 'DFR_6.2.RBK',
                        diagnosKodHelp: 'DFR_6.2.HLP'
                    }])
                ]),

                kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                    fraga(35, 'FRG_35.RBK', 'FRG_35.HLP', {}, [{
                        type: 'ue-textarea',
                        modelProp: 'funktionsnedsattning',
                        label: {
                            key: 'DFR_35.1.RBK',
                            helpKey: 'DFR_35.1.HLP',
                            required: true,
                            modelProp: 'funktionsnedsattning'
                        }
                    }]),
                    fraga(17, 'FRG_17.RBK', 'FRG_17.HLP', {}, [{
                        type: 'ue-textarea',
                        modelProp: 'aktivitetsbegransning',
                        label: {
                            key: 'DFR_17.1.RBK',
                            helpKey: 'DFR_17.1.HLP',
                            required: true,
                            modelProp: 'aktivitetsbegransning'
                        }
                    }])
                ]),

                kategori(categoryIds[5], 'KAT_5.RBK', 'KAT_5.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                    fraga(19, 'FRG_19.RBK', 'FRG_19.HLP', {}, [{
                        type: 'ue-textarea',
                        modelProp: 'pagaendeBehandling',
                        label: {
                            key: 'DFR_19.1.RBK',
                            helpKey: 'DFR_19.1.HLP'
                        }
                    }]),
                    fraga(20, 'FRG_20.RBK', 'FRG_20.HLP', {}, [{
                        type: 'ue-textarea',
                        modelProp: 'planeradBehandling',
                        label: {
                            key: 'DFR_20.1.RBK',
                            helpKey: 'DFR_20.1.HLP'
                        }
                    }])
                ]),

                kategori(categoryIds[6], 'KAT_6.RBK', 'KAT_6.HLP', {}, [
                    fraga(32, 'FRG_32.RBK', 'FRG_32.HLP', { required: true }, [{
                        type: 'ue-sjukskrivningar',
                        modelProp: 'sjukskrivningar',
                        code: 'KV_FKMU_0003',
                        fields: [
                            'EN_FJARDEDEL',
                            'HALFTEN',
                            'TRE_FJARDEDEL',
                            'HELT_NEDSATT'
                        ]
                    }]),
                    fraga(null, '', '', { hideExpression: function(scope) {
                        var hide = true;
                        var warnings = scope.validation.warningMessagesByField;
                        if (warnings) {
                            angular.forEach(warnings.sjukskrivningar, function(w) {
                                if (w.message ===
                                    'lisjp.validation.bedomning.sjukskrivningar.tidigtstartdatum') {
                                    hide = false;
                                }
                            });
                        }
                        return hide;
                    } }, [ {
                        type: 'ue-textarea',
                        label: {
                            bold: 'bold',
                            key: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering',
                            helpKey: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering.help',
                            required: true,
                            type: 'label'
                        },
                        modelProp: 'motiveringTillTidigtStartdatumForSjukskrivning'
                    }, {
                        type: 'ue-text',
                        label : {
                            htmlClass: 'info-transfer',
                            key: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering.info',
                            variableLabelKey: 'FRG_25.RBK'
                        }
                    }]),
                    fraga(37, 'FRG_37.RBK', 'FRG_37.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [{
                        type: 'ue-textarea',
                        modelProp: 'forsakringsmedicinsktBeslutsstod'
                    }]),
                    fraga(33, '', '', { }, [{
                        type: 'ue-radio',
                        modelProp: 'arbetstidsforlaggning',
                        label: {
                            key: 'FRG_33.RBK',
                            helpKey: 'FRG_33.HLP',
                            labelType: 'h4'
                        },
                        hideExpression: function(scope) {

                            if (scope.model.avstangningSmittskydd) {
                                return true;
                            }

                            var sjukskrivningar = scope.model.sjukskrivningar;

                            var nedsatt75under = false;
                            angular.forEach(sjukskrivningar, function(item, key) {
                                if (!nedsatt75under && key !== 'HELT_NEDSATT') {
                                    if (item.period && DateUtils.isDate(item.period.from) &&
                                        DateUtils.isDate(item.period.tom)) {
                                        nedsatt75under = true;
                                    }
                                }
                            });

                            return !nedsatt75under;
                        }
                    }, {
                        type: 'ue-textarea',
                        modelProp: 'arbetstidsforlaggningMotivering',
                        label: {
                            key: 'DFR_33.2.RBK',
                            helpKey: 'DFR_33.2.HLP',
                            labelType: 'h4',
                            required: true
                        },
                        hideExpression: function(scope) {
                            if (scope.model.avstangningSmittskydd) {
                                return true;
                            }
                            return scope.model.arbetstidsforlaggning !== true;
                        }
                    }]),
                    fraga(34, '', '', { hideExpression: 'model.avstangningSmittskydd' }, [{
                        type: 'ue-checkbox',
                        modelProp: 'arbetsresor',
                        label: {
                            key: 'FRG_34.RBK',
                            helpKey: 'FRG_34.HLP'
                        }
                    }]),
                    fraga(39, 'FRG_39.RBK', 'FRG_39.HLP', { required: true, hideExpression: 'model.avstangningSmittskydd' }, [{
                        type: 'ue-prognos',
                        modelProp: 'prognos',
                        code: 'KV_FKMU_0006',
                        choices: [{id: 'STOR_SANNOLIKHET', showDropDown: false},
                            {id: 'ATER_X_ANTAL_DGR', showDropDown: true},
                            {id: 'PROGNOS_OKLAR', showDropDown: false},
                            {id: 'SANNOLIKT_INTE', showDropDown: false}
                        ],
                        prognosDagarTillArbeteCode: 'KV_FKMU_0007',
                        prognosDagarTillArbeteTyper: ['TRETTIO_DGR',
                            'SEXTIO_DGR',
                            'NITTIO_DGR',
                            'HUNDRAATTIO_DAGAR',
                            'TREHUNDRASEXTIOFEM_DAGAR'
                        ]
                    }])
                ]),

                kategori(categoryIds[7], 'KAT_7.RBK', 'KAT_7.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                    fraga(40, 'FRG_40.RBK', 'FRG_40.HLP', { required: true }, [{
                        type: 'ue-checkgroup',
                        modelProp: 'arbetslivsinriktadeAtgarder',
                        code: 'KV_FKMU_0004',
                        choices: ['EJ_AKTUELLT',
                            'ARBETSTRANING',
                            'ARBETSANPASSNING',
                            'SOKA_NYTT_ARBETE',
                            'BESOK_ARBETSPLATS',
                            'ERGONOMISK',
                            'HJALPMEDEL',
                            'KONFLIKTHANTERING',
                            'KONTAKT_FHV',
                            'OMFORDELNING',
                            'OVRIGA_ATGARDER'
                        ],
                        watcher: [{
                            type: '$watch',
                            watchDeep: true,
                            expression: 'model.arbetslivsinriktadeAtgarder',
                            listener: function(newValue, oldValue, scope) {
                                if (oldValue && newValue !== oldValue) {
                                    if (newValue.EJ_AKTUELLT !== oldValue.EJ_AKTUELLT) {
                                        if (newValue.EJ_AKTUELLT) {
                                            angular.forEach(scope.model.arbetslivsinriktadeAtgarder,
                                                function(atgard, key) {
                                                    if (key !== 'EJ_AKTUELLT') {
                                                        scope.model.arbetslivsinriktadeAtgarder[key] = undefined;
                                                    }
                                                });
                                        }
                                    } else {
                                        angular.forEach(scope.model.arbetslivsinriktadeAtgarder,
                                            function(atgard, key) {
                                                if (key !== 'EJ_AKTUELLT' && atgard) {
                                                    scope.model.arbetslivsinriktadeAtgarder.EJ_AKTUELLT = undefined;
                                                }
                                            });
                                    }
                                }
                            }
                        }]
                    }]),
                    fraga(44, 'FRG_44.RBK', 'FRG_44.HLP', { hideExpression: function(scope) {
                        var hide = true;
                        angular.forEach(scope.model.arbetslivsinriktadeAtgarder, function(atgard, key) {
                            if (atgard === true && key !== 'EJ_AKTUELLT') {
                                hide = false;
                                return;
                            }
                        });
                        return hide;
                    }}, [{
                        modelProp: 'arbetslivsinriktadeAtgarderBeskrivning',
                        type: 'ue-textarea'
                    }])
                ]),

                kategori(categoryIds[8], 'KAT_8.RBK', 'KAT_8.HLP', {}, [
                    fraga(25, '', '', { }, [{
                        modelProp: 'ovrigt',
                        type: 'ue-textarea'
                    }])
                ]),

                kategori(categoryIds[9], 'KAT_9.RBK', 'KAT_9.HLP', { hideExpression: 'model.avstangningSmittskydd' }, [
                    fraga(26, 'FRG_26.RBK', 'FRG_26.HLP', { }, [{
                        type: 'ue-checkbox',
                        modelProp: 'kontaktMedFk',
                        label: {
                            key: 'DFR_26.1.RBK',
                            helpKey: 'DFR_26.1.HLP'
                        }
                    },{
                        type: 'ue-textarea',
                        modelProp: 'anledningTillKontakt',
                        hideExpression: '!model.kontaktMedFk',
                        label: {
                            labelType: 'h5',
                            key: 'DFR_26.2.RBK',
                            helpKey: 'DFR_26.2.HLP'
                        }
                    }])
                ]),

                {
                    type: 'ue-tillaggsfragor'
                },

                ueFactoryTemplates.vardenhet
            ];

            return {
                getConfig: function() {
                    return angular.copy(config);
                },
                getCategoryIds: function() {
                    return angular.copy(categoryIds);
                }
            };
        }]);
