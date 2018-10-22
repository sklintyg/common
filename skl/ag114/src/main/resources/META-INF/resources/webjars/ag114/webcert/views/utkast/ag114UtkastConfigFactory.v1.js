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

angular.module('ag114').factory('ag114.UtkastConfigFactory.v1',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ueFactoryTemplatesHelper',
        function($log, $timeout, DateUtils, ueFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'sysselsattning',
                    2: 'diagnos',
                    3: 'arbetsformaga',
                    4: 'bedomning',
                    5: 'ovrigt',
                    6: 'kontakt'
                };
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();

                var underlagChoices = [
                    {label: 'common.label.choose', id: null},
                    {label: 'KV_FKMU_0005.NEUROPSYKIATRISKT.RBK', id: 'NEUROPSYKIATRISKT'},
                    {label: 'KV_FKMU_0005.HABILITERING.RBK', id: 'HABILITERING'},
                    {label: 'KV_FKMU_0005.ARBETSTERAPEUT.RBK', id: 'ARBETSTERAPEUT'},
                    {label: 'KV_FKMU_0005.FYSIOTERAPEUT.RBK', id: 'FYSIOTERAPEUT'},
                    {label: 'KV_FKMU_0005.LOGOPED.RBK', id: 'LOGOPED'},
                    {label: 'KV_FKMU_0005.PSYKOLOG.RBK', id: 'PSYKOLOG'},
                    {label: 'KV_FKMU_0005.SKOLHALSOVARD.RBK', id: 'SKOLHALSOVARD'},
                    {label: 'KV_FKMU_0005.SPECIALISTKLINIK.RBK', id: 'SPECIALISTKLINIK'},
                    {label: 'KV_FKMU_0005.VARD_UTOMLANDS.RBK', id: 'VARD_UTOMLANDS'},
                    {label: 'KV_FKMU_0005.OVRIGT_UTLATANDE.RBK', id: 'OVRIGT_UTLATANDE'}
                ];

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var today = moment().format('YYYY-MM-DD');

                var buildUnderlagConfigRow = function(row) {
                    return [ {
                        type: 'ue-dropdown',
                        modelProp: 'underlag[' + row + '].typ',
                        choices: underlagChoices,
                        skipAttic: (row > 0)

                    }, {
                        type: 'ue-date',
                        maxDate: today,
                        modelProp: 'underlag[' + row + '].datum',
                        skipAttic: true
                    }, {
                        type: 'ue-textfield',
                        modelProp: 'underlag[' + row + '].hamtasFran',
                        size: 'full-width',
                        htmlMaxlength: 53,
                        skipAttic: true
                    } ];

                };

                var config = [

                    // Sysselsättning
                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', {signingDoctor: true}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', { required: true, requiredProp: ['sysselsattning["NUVARANDE_ARBETE"]']}, [{
                            type: 'ue-checkgroup',
                            modelProp: 'sysselsattning',
                            code: 'KV_FKMU_0002',
                            choices: ['NUVARANDE_ARBETE']
                        }]), fraga(2, 'FRG_2.RBK', 'FRG_2.HLP', { required: true, requiredProp: 'nuvarandeArbete'}, [{
                            type: 'ue-textarea',
                            modelProp: 'nuvarandeArbete'
                        }])
                    ]),

                    // Diagnos
                    //  - Önskar förmedla 3.1
                    //  - Typ av diagnos
                    //     - Diagnostext 4.1
                    //     - Diagnoskod 4.2
                    kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', {signingDoctor: true}, [
                        fraga(3, 'FRG_3.RBK', 'FRG_3.HLP', { required:true, requiredProp: 'onskarFormedlaDiagnos' }, [{
                            type: 'ue-radio',
                            modelProp: 'onskarFormedlaDiagnos'
                        }]),
                        fraga(4, 'FRG_4.RBK', 'FRG_4.HLP', { required: true, requiredProp: 'diagnoser[0].diagnosKod'
                            , hideExpression: '!model.onskarFormedlaDiagnos'}, [{
                            type: 'ue-diagnos',
                            modelProp: 'diagnoser',
                            diagnosBeskrivningLabel: 'DFR_4.1.RBK',
                            diagnosBeskrivningHelp: 'DFR_4.1.HLP',
                            diagnosKodLabel: 'DFR_4.2.RBK',
                            diagnosKodHelp: 'DFR_4.2.HLP'
                        }])
                    ]),

                    // Arbetsförmåga
                    //  - nedsättning 5.1
                    //  - förmåga trots sjukdom 6.1
                    kategori(categoryIds[3], 'KAT_3.RBK', '', {signingDoctor: true}, [
                        fraga(5, 'FRG_5.RBK', 'FRG_5.HLP', { required: true }, [{
                            type: 'ue-textarea',
                            modelProp: 'nedsattArbetsformaga'
                        }]),
                        fraga(6, 'FRG_6.RBK', 'FRG_6.HLP', { required: true, requiredProp: 'arbetsformagaTrotsSjukdom' }, [{
                            type: 'ue-radio',
                            modelProp: 'arbetsformagaTrotsSjukdom'
                        }, {
                            type: 'ue-textarea',
                            modelProp: 'arbetsformagaTrotsSjukdomBeskrivning',
                            hideExpression: '!model.arbetsformagaTrotsSjukdom',
                            label: {
                                key: 'DFR_6.2.RBK'
                            }
                        }])
                    ]),
                    kategori(categoryIds[6], 'KAT_6.RBK', 'KAT_6.HLP', { }, [
                        fraga(26, undefined, 'FRG_9.HLP', { }, [{
                            type: 'ue-checkbox',
                            modelProp: 'kontaktMedArbetsgivaren',
                            label: {
                                key: 'DFR_9.1.RBK',
                                helpKey: 'DFR_9.1.HLP'
                            }
                        }, {
                            type: 'ue-textarea',
                            modelProp: 'anledningTillKontakt',
                            hideExpression: '!model.kontaktMedArbetsgivaren',
                            label: {
                                labelType: 'h5',
                                key: 'DFR_9.2.RBK',
                                helpKey: 'DFR_9.2.HLP'
                            }
                        }])
                    ]),
                    // Kategori vårdenhet
                    ueFactoryTemplates.vardenhet
                ];
                return config;
            }


            return {
                getConfig: _getConfig,
                getCategoryIds: _getCategoryIds
            };
        }]);
