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
angular.module('ts-diabetes').factory('ts-diabetes.Domain.IntygModel.v3',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.UtilsService', 'common.tsBaseHelper',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, UtilsService, tsBaseHelper) {
            'use strict';

            var uppfyllerBehorighetskravFromTransform = function(backendValue) {
                return tsBaseHelper.setupKorkortstypChoices(backendValue, ['VAR11']);
            };


            var TsDiabetesV3Model = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'IntygModel', {

                        id: undefined,
                        typ: undefined,
                        textVersion: undefined,
                        grundData: grundData,

                        // Kategori 1
                        intygAvser: {
                            kategorier: undefined
                        },

                        // Kategori 2
                        identitetStyrktGenom: undefined,

                        // Kategori 3
                        allmant: {
                            diabetesDiagnosAr: undefined,
                            typAvDiabetes: undefined,
                            beskrivningAnnanTypAvDiabetes: undefined,
                            behandling: {
                                endastKost: undefined,
                                tabletter: undefined,
                                insulin: undefined,
                                insulinSedanAr: undefined,
                                annanBehandling: undefined,
                                annanBehandlingBeskrivning: undefined,
                                riskHypoglykemi: undefined
                            }
                        },

                        // Kategori 4
                        hypoglykemier: {
                            egenkontrollBlodsocker: undefined,
                            nedsattHjarnfunktion: undefined,
                            sjukdomenUnderKontroll: undefined,
                            formagaVarningstecken: undefined,
                            aterkommandeSenasteAret: undefined,
                            aterkommandeSenasteTidpunkt: undefined,
                            aterkommandeSenasteKvartalet: undefined,
                            senasteTidpunktVaken: undefined,
                            forekomstTrafik: undefined,
                            forekomstTrafikTidpunkt: undefined
                        },

                        // Kategori 5
                        synfunktion: {
                            misstankeOgonsjukdom: undefined,
                            skickasSeparat: false,
                            hoger: {
                                utanKorrektion: undefined,
                                medKorrektion: undefined
                            },
                            vanster: {
                                utanKorrektion: undefined,
                                medKorrektion: undefined
                                },
                            binokulart: {
                                utanKorrektion: undefined,
                                medKorrektion: undefined
                                }
                        },
                        // Kategori 6
                        ovrigt: undefined,

                        // Kategori 7
                        bedomning: {
                            uppfyllerBehorighetskrav: new ModelAttr('uppfyllerBehorighetskrav', {
                                fromTransform: uppfyllerBehorighetskravFromTransform
                            }),
                            lampligtInnehav: undefined,
                            borUndersokasBeskrivning: undefined
                        }
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
                    return new DraftModel(new TsDiabetesV3Model());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return TsDiabetesV3Model;

        }]);
