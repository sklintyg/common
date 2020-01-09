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
angular.module('common').factory('common.Domain.RelationModel',
    function() {
        'use strict';

        /**
         * Constructor, with class name
         */
        function Relation() {
            this.relationIntygsId = undefined;
            this.relationKod = undefined;
            this.meddelandeId = undefined;
            this.sistaGiltighetsDatum = undefined;
            this.sistaSjukskrivningsgrad = undefined;
            this.referensId = undefined;
        }

        Relation.prototype.update = function(relation) {
            // refresh the model data
            if(relation === undefined){
                return;
            }
            this.relationIntygsId = relation.relationIntygsId;
            this.relationKod = relation.relationKod;
            this.meddelandeId = relation.meddelandeId;
            this.sistaGiltighetsDatum = relation.sistaGiltighetsDatum;
            this.sistaSjukskrivningsgrad = relation.sistaSjukskrivningsgrad;
            this.referensId = relation.referensId;
        };

        Relation.build = function() {
            return new Relation();
        };

        /**
         * Return the constructor function Relation
         */
        return Relation;

    });
