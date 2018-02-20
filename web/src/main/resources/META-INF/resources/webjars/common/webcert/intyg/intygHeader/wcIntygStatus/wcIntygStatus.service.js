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

angular.module('common').service('common.IntygStatusService',
    ['common.messageService', 'common.IntygHeaderViewState',
        function(messageService, IntygHeaderViewState) {
            'use strict';

            var service = this;

            this.getMessageKeyForIntyg = function(partialKey) {
                var key = IntygHeaderViewState.intygType + partialKey;
                if (!messageService.propertyExists(key)) {
                    key = 'common' + partialKey;
                }
                return key;
            };

            this.getMessageForIntygStatus = function(status, vars) {
                if (!status) {
                    return '';
                }
                return messageService.getProperty(service.getMessageKeyForIntyg('.label.intygstatus.' + status), vars);
            };

            this.intygStatusHasModal = function(intygStatus) {
                return Boolean(messageService.propertyExists('common.modalbody.intygstatus.' + intygStatus) ||
                    messageService.propertyExists(IntygHeaderViewState.intygType + '.modalbody.intygstatus.' + intygStatus));
            };

        }]
);

