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

angular.module('common').directive('wcIntygRelations',
    ['$state', 'common.IntygViewStateService',
        function($state, IntygViewState) {
            'use strict';

            return {
                templateUrl: '/web/webjars/common/webcert/gui/wcIntygRelations.directive.html',
                link: function($scope) {

                    $scope.gotoRelatedIntyg = function(intyg) {
                        var intygType = IntygViewState.intygProperties.type || $scope.viewState.common.intyg.type;
                        if (intyg.status === 'DRAFT_INCOMPLETE' || intyg.status === 'DRAFT_COMPLETE')
                        {
                            $state.go(intygType + '-edit', {certificateId: intyg.intygsId});
                        }
                        else
                        {
                            $state.go('webcert.intyg.fk.' + intygType, {certificateId: intyg.intygsId});
                        }
                    };

                }
            };
        }]);
