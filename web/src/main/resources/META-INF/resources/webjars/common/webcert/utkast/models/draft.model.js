/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.Domain.DraftModel',
    [function() {
        'use strict';

        // the actual model

        /**
         * Constructor, with class name
         */
        function DraftModel(contentModel) {
            this.version = undefined;
            this.vidarebefordrad = undefined;
            this.status = undefined;
            this.patientResolved = undefined;
            this.sekretessmarkering = undefined;
            this.avliden = undefined;
            this.created = undefined;
            this.links = undefined;
            if (contentModel) {
                this.content = contentModel;
            }
        }

        DraftModel.prototype.update = function(data) {
            // refresh the model data
            if (data === undefined) {
                return;
            }
            this.version = data.version;
            this.relations = data.relations;
            this.vidarebefordrad = data.vidarebefordrad;
            this.status = data.status;
            this.patientResolved = data.patientResolved;
            this.sekretessmarkering = data.sekretessmarkering;
            this.avliden = data.avliden;
            this.created = data.created;
            this.revokedAt = data.revokedAt;
            this.links = data.links;
            if (this.content) {
                this.content.update(data.content);
            }
        };

        DraftModel.prototype.isSigned = function() {
            return (this.status && this.status === 'SIGNED');
        };

        DraftModel.prototype.isDraftComplete = function() {
            return (this.status && this.status === 'DRAFT_COMPLETE');
        };

        DraftModel.prototype.isLocked = function() {
            return (this.status && this.status === 'DRAFT_LOCKED');
        };

        DraftModel.prototype.isRevoked = function() {
            return !!this.revokedAt;
        };

        DraftModel.build = function() {
            return new DraftModel();
        };

        return DraftModel;

    }]);
