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

angular.module('common').service('common.ArendeNewViewStateService',
    ['$log', function($log) {
        'use strict';

        this.reset = function() {
            this.arendeNewOpen = false; // Toggles if question form is open
            this.showSentMessage = false; // Shows info text that message has been sent to target
            this.focusQuestion = false; // True sets focus to question text via focus directive
            this.sendButtonToolTip = 'Skicka frågan';
            this.isIntygOnSendQueue = false;
            this.intygProperties = {
                isLoaded: false,
                isSent: false,
                isRevoked: false
            };
            return this;
        };

        this.reset();
    }]
);
