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
        'common.domain.BaseAtticModel', 'common.UtilsService', 'common.tsBaseHelper',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, u, tsBaseHelper) {
        'use strict';

        var bedomningFromTransform = function(backendBedomning) {

            var korkortstyp = backendBedomning.korkortstyp;

            korkortstyp.push({
                type:'KAN_INTE_TA_STALLNING',
                selected: angular.isDefined(backendBedomning.kanInteTaStallning) && backendBedomning.kanInteTaStallning
            });

            return {
                korkortstyp: tsBaseHelper.setupKorkortstypChoices(korkortstyp, 'KAN_INTE_TA_STALLNING'),
                lakareSpecialKompetens: backendBedomning.lakareSpecialKompetens
            };
        };

        var bedomningToTransform = function(frontendObject) {

            var transformedFrontendObject = angular.copy(frontendObject);
            var index = u.findIndexWithPropertyValue(transformedFrontendObject.korkortstyp, 'type', 'KAN_INTE_TA_STALLNING');
            var kanInteTaStallning;
            if(index !== -1) {
                kanInteTaStallning = transformedFrontendObject.korkortstyp[index].selected;
                transformedFrontendObject.korkortstyp.splice(index);
            }

            var backendBedomning = {
                korkortstyp: transformedFrontendObject.korkortstyp,
                kanInteTaStallning: kanInteTaStallning,
                lakareSpecialKompetens: transformedFrontendObject.lakareSpecialKompetens
            };
            return backendBedomning;
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
                            {defaultValue:[
                                {'type': 'C1', 'selected': false},
                                {'type': 'C1E', 'selected': false},
                                {'type': 'C', 'selected': false},
                                {'type': 'CE', 'selected': false},
                                {'type': 'D1', 'selected': false},
                                {'type': 'D1E', 'selected': false},
                                {'type': 'D', 'selected': false},
                                {'type': 'DE', 'selected': false},
                                {'type': 'TAXI', 'selected': false},
                                {'type': 'ANNAT', 'selected': false}
                        ]})
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
                return new DraftModel(new TsTstrk1062Model());
            }
        });

        /**
         * Return the constructor function IntygModel
         */
        return TsTstrk1062Model;
    }]);
