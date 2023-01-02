/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('luae_fs').factory('luae_fs.Domain.IntygModel.v1',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.domain.ModelTransformService',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, ModelTransform) {
            'use strict';

            var LuaeFsModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'LuaeFsModel', {

                        'id': undefined,
                        'typ': undefined,
                        'textVersion': undefined,
                        'grundData': grundData,

                        // Kategori 1 Grund för medicinskt underlag
                        'undersokningAvPatienten': undefined,           //KV_FKMU_0001_1
                        'journaluppgifter': undefined,                  //KV_FKMU_0001_3
                        'anhorigsBeskrivningAvPatienten': undefined,    //KV_FKMU_0001_4
                        'annatGrundForMU': undefined,                   //KV_FKMU_0001_5
                        'annatGrundForMUBeskrivning': undefined,
                        'motiveringTillInteBaseratPaUndersokning': undefined,
                        'kannedomOmPatient': undefined,

                        // Kategori 2 Andra medicinska utredningar och underlag
                        'underlagFinns': undefined,
                        'underlag': new ModelAttr('underlag', { defaultValue: [],
                            fromTransform: ModelTransform.underlagFromTransform,
                            toTransform: ModelTransform.underlagToTransform
                        }),


                        // Kategori 3 diagnos
                        'diagnoser': new ModelAttr('diagnoser', {
                            fromTransform: ModelTransform.diagnosFromTransform,
                            toTransform: ModelTransform.diagnosToTransform
                        }),


                        // Kategori 4 Funktionsnedsättning
                        'funktionsnedsattningDebut': undefined,  //15.1
                        'funktionsnedsattningPaverkan': undefined,  //16.1

                        // Kategori 5 Övrigt
                        'ovrigt': undefined,

                        // Kategori 6 Kontakt
                        'kontaktMedFk': new ModelAttr('kontaktMedFk', {defaultValue: false}), // 26.1
                        'anledningTillKontakt': undefined, //26.2

                        // Kategori 9999 Tilläggsfrågor
                        'tillaggsfragor': new ModelAttr('tillaggsfragor', {defaultValue: []})
                    });
                },
                update: function update(content, parent) {
                    if (parent) {
                        parent.content = this;
                    }
                    update._super.call(this, content);
                }

            }, {
                build: function() {
                    return new DraftModel(new LuaeFsModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return LuaeFsModel;

        }]);
