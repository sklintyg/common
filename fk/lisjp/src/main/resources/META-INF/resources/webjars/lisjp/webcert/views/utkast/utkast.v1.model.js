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
angular.module('lisjp').factory('lisjp.Domain.IntygModel.v1',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.domain.ModelTransformService', 'common.ObjectHelper',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, ModelTransform, ObjectHelper) {
            'use strict';

            var sjukskrivningFromTransform = function(sjukskrivningArray) {

                var resultObject = {
                    HELT_NEDSATT: {
                        period: {
                            from: '',
                            tom: ''
                        }
                    },
                    TRE_FJARDEDEL: {
                        period: {
                            from: '',
                            tom: ''
                        }
                    },
                    HALFTEN: {
                        period: {
                            from: '',
                            tom: ''
                        }
                    },
                    EN_FJARDEDEL: {
                        period: {
                            from: '',
                            tom: ''
                        }
                    }
                };

                for(var i = 0; i < sjukskrivningArray.length; i++){
                    resultObject[sjukskrivningArray[i].sjukskrivningsgrad] = {
                        period: sjukskrivningArray[i].period
                    };
                }

                return resultObject;
            };

            var sjukskrivningToTransform = function(sjukskrivningObject) {

                var resultArray = [];
                angular.forEach(sjukskrivningObject, function(value, key) {
                    if(!ObjectHelper.isEmpty(value.period.from) || !ObjectHelper.isEmpty(value.period.tom)) {
                        resultArray.push({
                            sjukskrivningsgrad: key,
                            period: {
                                from: value.period.from,
                                tom: value.period.tom
                            }
                        });
                    }
                }, sjukskrivningObject);

                return resultArray;
            };

            var LisjpModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'lisjpModel', {

                        'id': undefined,
                        'typ': undefined,
                        'textVersion': undefined,
                        'grundData': grundData,

                        // Kategori 10 Smittbärarpenning
                        'avstangningSmittskydd': new ModelAttr( 'avstangningSmittskydd', { defaultValue : false }),

                        // Kategori 1 Grund för medicinskt underlag
                        'undersokningAvPatienten': undefined,
                        'telefonkontaktMedPatienten': undefined,
                        'journaluppgifter': undefined,
                        'annatGrundForMU': undefined,
                        'annatGrundForMUBeskrivning': undefined,
                        'motiveringTillInteBaseratPaUndersokning':undefined,

                        // Kategori 2 sysselsättning
                        'sysselsattning': new ModelAttr('sysselsattning', {
                            toTransform: ModelTransform.enumToTransform,
                            fromTransform: ModelTransform.enumFromTransform,
                            defaultValue: {}
                        }),
                        'nuvarandeArbete' : undefined,

                        // Kategori 3 diagnos
                        'diagnoser':new ModelAttr('diagnoser', {
                            fromTransform: ModelTransform.diagnosFromTransform,
                            toTransform: ModelTransform.diagnosToTransform
                        }),

                        // Kategori 4 Sjukdomens konsekvenser
                        'funktionsnedsattning': undefined,
                        'funktionsKategorier': undefined,
                        'aktivitetsbegransning': undefined,
                        'aktivitetsKategorier': undefined,

                        // Kategori 5 Medicinska behandlingar / åtgärder
                        'pagaendeBehandling': undefined,
                        'planeradBehandling': undefined,

                        // Kategory 6 Bedömning
                        'sjukskrivningar': new ModelAttr('sjukskrivningar', {
                            defaultValue : {
                                HELT_NEDSATT: {
                                    period: {
                                        from: '',
                                        tom: ''
                                    }
                                },
                                TRE_FJARDEDEL: {
                                    period: {
                                        from: '',
                                        tom: ''
                                    }
                                },
                                HALFTEN: {
                                    period: {
                                        from: '',
                                        tom: ''
                                    }
                                },
                                EN_FJARDEDEL: {
                                    period: {
                                        from: '',
                                        tom: ''
                                    }
                                }
                            },
                            fromTransform: sjukskrivningFromTransform,
                            toTransform: sjukskrivningToTransform
                        }),
                        'motiveringTillTidigtStartdatumForSjukskrivning': undefined,
                        'forsakringsmedicinsktBeslutsstod': undefined,
                        'arbetstidsforlaggning': undefined,
                        'arbetstidsforlaggningMotivering': undefined,
                        'arbetsresor': undefined,
                        'prognos': {
                            'typ': undefined,
                            'dagarTillArbete': undefined
                        },

                        // Kategori 7 Åtgärder
                        'arbetslivsinriktadeAtgarder': new ModelAttr('arbetslivsinriktadeAtgarder', {
                            toTransform: ModelTransform.enumToTransform,
                            fromTransform: ModelTransform.enumFromTransform,
                            defaultValue: {}
                        }),
                        'arbetslivsinriktadeAtgarderBeskrivning': undefined,

                        // Kategori 8 Övrigt
                        'ovrigt': undefined,

                        // Kategori 9 Kontakt
                        'kontaktMedFk': new ModelAttr( 'kontaktMedFk', { defaultValue : false }),
                        'anledningTillKontakt': undefined,

                        // Kategori 9999 Tilläggsfrågor
                        'tillaggsfragor': new ModelAttr( 'tillaggsfragor', { defaultValue : [] })
                    });
                },
                update: function update(content, parent) {
                    if (parent) {
                        parent.content = this;
                    }
                    update._super.call(this, content);
                }

            }, {
                build : function(){
                    return new DraftModel(new LisjpModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return LisjpModel;

        }]);
