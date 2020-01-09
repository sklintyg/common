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
angular.module('common').factory('common.domain.BaseAtticModel',
    ['common.domain.ModelAttr', 'common.domain.BaseModel', 'common.domain.AtticService', function( ModelAttr, BaseModel, atticService) {
        'use strict';

        var BaseAtticModel = BaseModel._extend({
            init : function init (name, properties){
                init._super.call(this, name, properties);
                this.atticModel = atticService.addNewAtticModel(this);
            },
            updateToAttic : function updateToAttic(properties){
                atticService.update(this, properties);
            },
            isInAttic : function isInAttic(properties){
                return atticService.isInAttic(this, properties);
            },
            restoreFromAttic : function restoreFromAttic(properties){
                atticService.restore(this, properties);
            },
            update : function update(content, properties){
                update._super.call(this, content, properties);
                if(this.updateCount === 1){
                    // update the attic
                    this.updateToAttic();
                }
            }

        });

        return BaseAtticModel;
    }]);
