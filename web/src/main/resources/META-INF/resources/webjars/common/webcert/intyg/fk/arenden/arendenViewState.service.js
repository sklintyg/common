/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

angular.module('common').service('common.ArendenViewStateService',
    ['common.IntygViewStateService', function(CommonViewState) {
        'use strict';

        this.reset = function() {
            this.doneLoading = false;
            this.activeErrorMessageKey = null;
            this.showTemplate = true;

            // Injecting the CommonViewState service so client-side only changes on the intyg page (such as a send/revoke)
            // can trigger GUI updates in the Q&A view.
            this.common = CommonViewState;
            return this;
        };

        this.reset();
    }]
);
