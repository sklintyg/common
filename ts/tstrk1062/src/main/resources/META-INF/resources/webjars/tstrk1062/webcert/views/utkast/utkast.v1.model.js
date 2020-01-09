/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
angular.module('tstrk1062').factory('tstrk1062.Domain.IntygModel.v1',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.ModelTransformService', 'common.domain.BaseAtticModel', 'common.UtilsService',
        'common.tsBaseHelper', 'common.ObjectHelper',
        function(GrundData, DraftModel, ModelAttr, ModelTransform, BaseAtticModel, u, tsBaseHelper, ObjectHelper) {
            'use strict';

            var uppfyllerBehorighetskravFromTransform = function(backendValue) {
                return tsBaseHelper.setupKorkortstypChoices(backendValue, ['VAR11']);
            };

            var diagnosFromTransform = function(diagnosArray) {

                // We now always have a specific amount of underlag so add that number of empty elements
                for (var i = 0; diagnosArray.length < 4; i++) {
                    diagnosArray.push({
                        diagnosKodSystem: 'ICD_10_SE',
                        diagnosKod: undefined,
                        diagnosBeskrivning: undefined,
                        diagnosArtal: undefined
                    });
                }

                return diagnosArray;
            };

            var diagnosToTransform = function(diagnosArray) {
                var diagnosCopy = angular.copy(diagnosArray);

                // delete all rows with no values at all so as to not confuse backend with non-errors
                var i = diagnosCopy.length - 1;
                while (i >= 0) {
                    if (ObjectHelper.isEmpty(diagnosCopy[i].diagnosKod) &&
                        ObjectHelper.isEmpty(diagnosCopy[i].diagnosBeskrivning) &&
                        ObjectHelper.isEmpty(diagnosCopy[i].diagnosArtal)) {
                        diagnosCopy.splice(i, 1);
                    } else {
                        break;
                    }
                    i--;
                }

                return diagnosCopy;
            };

            /**
             * Constructor, with class name
             */
            var TsTrk1062Model = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'TsTrk1062Model', {
                        id: undefined,
                        typ: undefined,
                        kommentar: undefined,
                        grundData: grundData,
                        textVersion: undefined,
                        vardkontakt: {
                            typ: undefined,
                            idkontroll: undefined
                        },

                        // Kategori 1 - Intyget avses
                        intygAvser: {
                            behorigheter: undefined
                        },

                        // Kategori 2 - ID kontroll
                        idKontroll: undefined,

                        // Kategori 3 - Diagnos
                        diagnosRegistrering: {
                            typ: undefined
                        },
                        diagnosKodad: new ModelAttr('diagnosKodad', {
                            fromTransform: diagnosFromTransform,
                            toTransform: diagnosToTransform
                        }),
                        diagnosFritext: {
                            diagnosFritext: undefined,
                            diagnosArtal: undefined
                        },

                        // Kategori 4 - Läkemedelsbehandling
                        lakemedelsbehandling: {
                            harHaft: undefined,
                            pagar: undefined,
                            aktuell: undefined,
                            pagatt: undefined,
                            effekt: undefined,
                            foljsamhet: undefined,
                            avslutadTidpunkt: undefined,
                            avslutadOrsak: undefined
                        },

                        // Kategori 5 - Symptom
                        bedomningAvSymptom: undefined,
                        prognosTillstand: {
                            typ: undefined
                        },

                        // Kategori 6 - Övrigt
                        ovrigaKommentarer: undefined,

                        // Kategori 7 - Bedömning
                        bedomning: {
                            uppfyllerBehorighetskrav: new ModelAttr('uppfyllerBehorighetskrav', {
                                fromTransform: uppfyllerBehorighetskravFromTransform
                            })
                        }
                    })
                    ;
                },
                update: function update(content, parent) {
                    if (parent) {
                        parent.content = this;
                    }
                    update._super.call(this, content);
                }
            }, {
                build: function() {
                    return new DraftModel(new TsTrk1062Model());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return TsTrk1062Model;
        }]);
