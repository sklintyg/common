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
angular
    .module('common')
    .factory('common.UtkastFooterService',
    ['common.UtkastViewStateService', 'common.UtkastService', 'common.UtkastValidationService', 'common.UtkastValidationViewState',
        function(CommonViewState, UtkastService, UtkastValidationService, utkastValidationViewState) {
            'use strict';

             function checkMissing(viewState, certForm, signStatus) {
                if(signStatus === 'pending') {
                    return false;
                }

                if(!viewState.common.intyg.isComplete || certForm.$dirty){

                    CommonViewState.setShowComplete(true);
                    UtkastService.save();
                    UtkastValidationService.filterValidationMessages();

                    return false;
                }

                return true;
            }

            function toggleMissing(value, viewState, certForm) {
                if (value) {
                    checkMissing(viewState, certForm);
                } else {
                    CommonViewState.setShowComplete(false);
                    utkastValidationViewState.reset();
                }
            }

            return {
                checkMissing: checkMissing,
                toggleMissing: toggleMissing
            };
        }]);
