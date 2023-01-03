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
angular.module('common').factory('common.domain.ModelAttr',
    function( ) {
        'use strict';
        var ModelAttr = function(property, options){
            this.property = property;
            if(options && options.defaultValue !== undefined){
                this.defaultValue = options.defaultValue;
            } else {
                this.defaultValue = undefined;
            }
            if(options) {
                this.trans = options.trans;
                this.toTransform = options.toTransform;
                this.fromTransform = options.fromTransform;
                this.linkedProperty = options.linkedProperty;
            }
        };

        return ModelAttr;
    });
