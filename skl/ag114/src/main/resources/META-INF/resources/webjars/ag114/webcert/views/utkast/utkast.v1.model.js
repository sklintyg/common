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
angular.module('ag114').factory('ag114.Domain.IntygModel.v1',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.domain.ModelTransformService',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, ModelTransform) {
            'use strict';

            var Ag114Model = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'Ag114Model', {

                        'id': undefined,
                        'typ': undefined,
                        'textVersion': undefined,
                        'grundData': grundData,

                        // Kategori 1 sysselsättning
                        'sysselsattning': new ModelAttr('sysselsattning', {
                            toTransform: ModelTransform.enumToTransform,
                            fromTransform: ModelTransform.enumFromTransform,
                            defaultValue: {
                                "typ": "NUVARANDE_ARBETE"
                            }
                        }),
                        'nuvarandeArbete' : undefined,
                        'onskarFormedlaDiagnos': undefined,
                        'nedsattArbetsformaga': undefined,
                        'arbertsformagaTrotsSjukdom': undefined,
                        'arbertsformagaTrotsSjukdomBeskrivning': undefined
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
                    return new DraftModel(new Ag114Model());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return Ag114Model;

        }]);
