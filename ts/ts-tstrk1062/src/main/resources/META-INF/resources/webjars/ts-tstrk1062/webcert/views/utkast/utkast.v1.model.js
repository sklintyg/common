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
angular.module('ts-tstrk1062').factory('ts-tstrk1062.Domain.IntygModel.v1',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.ModelTransformService', 'common.domain.BaseAtticModel', 'common.UtilsService',
        'common.tsBaseHelper',
        function(GrundData, DraftModel, ModelAttr, ModelTransform, BaseAtticModel, u, tsBaseHelper) {
            'use strict';

            var uppfyllerBehorighetskravFromTransform = function(backendValue) {
                return tsBaseHelper.setupKorkortstypChoices(backendValue, 'KANINTETASTALLNING');
            };

            /**
             * Constructor, with class name
             */
            var TsTstrk1062Model = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'TsTstrk1062Model', {
                        id: undefined,
                        typ: undefined,
                        kommentar: undefined,
                        grundData: grundData,
                        textVersion: undefined,
                        vardkontakt: {
                            typ: undefined,
                            idkontroll: undefined
                        },
                        intygAvser: {
                            'korkortstyp': new ModelAttr('korkortstyp',
                                {
                                    defaultValue: [
                                        {'type': 'IAV11', 'selected': false},
                                        {'type': 'IAV12', 'selected': false},
                                        {'type': 'IAV13', 'selected': false},
                                        {'type': 'IAV14', 'selected': false},
                                        {'type': 'IAV15', 'selected': false},
                                        {'type': 'IAV16', 'selected': false},
                                        {'type': 'IAV17', 'selected': false},
                                        {'type': 'IAV1', 'selected': false},
                                        {'type': 'IAV2', 'selected': false},
                                        {'type': 'IAV3', 'selected': false},
                                        {'type': 'IAV4', 'selected': false},
                                        {'type': 'IAV5', 'selected': false},
                                        {'type': 'IAV6', 'selected': false},
                                        {'type': 'IAV7', 'selected': false},
                                        {'type': 'IAV8', 'selected': false},
                                        {'type': 'IAV9', 'selected': false}
                                    ]
                                })
                        },
                        idKontroll: undefined,
                        diagnosRegistrering: undefined,
                        // Kategori 3 diagnos
                        'diagnosKodad': new ModelAttr('diagnosKodad', {
                            fromTransform: ModelTransform.diagnosFromTransform,
                            toTransform: ModelTransform.diagnosToTransform
                        }),
                        diagnosFritext: undefined,
                        lakemedelsbehandling: undefined,
                        bedomningAvSymptom: undefined,
                        prognosTillstand: undefined,
                        ovrigaKommentarer: undefined,

                        // Kategori 7
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
                    return new DraftModel(new TsTstrk1062Model());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return TsTstrk1062Model;
        }]);
