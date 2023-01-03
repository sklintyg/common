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
angular.module('tstrk1009').factory('tstrk1009.Domain.IntygModel.v1',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.UtilsService', 'common.tsBaseHelper',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, UtilsService, tsBaseHelper) {
            'use strict';

            var intygetAvserBehorigheterFromTransform = function(backendBedomning) {
                return {
                    typer: tsBaseHelper.setupKorkortstypChoices(backendBedomning.typer, ['ALLA', 'KANINTETASTALLNING'])
                };
            };
    
            var intygetAvserBehorigheterToTransform = function(frontendObject) {
                return frontendObject;
            };
    

            var V1Model = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'IntygModel', {

                        id: undefined,
                        typ: undefined,
                        textVersion: undefined,
                        grundData: grundData,

                        signature: undefined,
                        identitetStyrktGenom: undefined,
                        anmalanAvser: undefined,
                        medicinskaForhallanden: undefined,
                        senasteUndersokningsdatum: undefined,
                        intygetAvserBehorigheter: new ModelAttr('intygetAvserBehorigheter', {
                            toTransform: intygetAvserBehorigheterToTransform,
                            fromTransform: intygetAvserBehorigheterFromTransform
                        }),
                        informationOmTsBeslutOnskas: undefined
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
                    return new DraftModel(new V1Model());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return V1Model;

        }]);
