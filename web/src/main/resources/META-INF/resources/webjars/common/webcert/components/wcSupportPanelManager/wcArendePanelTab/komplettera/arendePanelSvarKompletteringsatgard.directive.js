/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
/**
 * Created by marced on 2016-08-31.
 */

/**
 * arendePanelSvarKompletteringsatgard directive. Handles kompletteringsatgard choice in a modal dialog.
 * If user selects to reply with a new Intyg, this component handles that, if user choose to reply with a message, the calling component
 * get's a callback through 'onAnswerWithMessage' and must handle it there
 */
angular.module('common').directive('arendePanelSvarKompletteringsatgard',
    ['$window', '$log', '$state', '$stateParams', '$q',
        'common.ArendeProxy', 'common.statService', 'common.ObjectHelper',
        'common.IntygCopyRequestModel', 'common.ArendeSvarModel', 'common.dialogService',
        'common.ArendeListViewStateService', 'common.IntygProxy', 'common.anchorScrollService',
        function($window, $log, $state, $stateParams, $q, ArendeProxy, statService, ObjectHelper,
            IntygCopyRequestModel, ArendeSvarModel, dialogService, ArendeListViewStateService, IntygProxy,
            anchorScrollService) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/komplettera/arendePanelSvarKompletteringsatgard.directive.html',
                scope: {
                    arendeListItem: '=',
                    parentViewState: '=',
                    onAnswerWithMessage: '&'
                },
                controller: function($scope, $element, $attrs) {

                    // For readability, keep a local struct with the values used from parent scope
                    var ArendeSvar = ArendeSvarModel.build($scope.parentViewState, $scope.arendeListItem);

                    $scope.arendeSvar = ArendeSvar;

                }
            };
        }]);
