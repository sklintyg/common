/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('fk7263').factory('fk7263.Domain.IntygModel',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel',
        'common.domain.BaseAtticModel',
        function(GrundData, DraftModel, BaseAtticModel) {
            'use strict';

            //We still need a basic model so that utkastservice can process loading of a fk7263 draft..
            //Also, some related components such as utkast-header expects grundData to exist for an utkast.
            var Fk7263Model = BaseAtticModel._extend({
                init: function init() {
                    init._super.call(this, 'Fk7263Model', {
                        id: undefined,
                        grundData: GrundData.build()
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
                    return new DraftModel(new Fk7263Model());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return Fk7263Model;

        }]);
