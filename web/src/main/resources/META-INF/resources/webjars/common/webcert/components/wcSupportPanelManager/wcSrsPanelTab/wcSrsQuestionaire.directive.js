/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
 * Display SRS questionaire
 */
angular.module('common').directive('wcSrsQuestionaire', ['common.srsProxy', 'common.srsViewState',
    function(srsProxy, srsViewState) {
        'use strict';

        return {
            restrict: 'E',
            link: function(scope, element, attrs) {
                scope.change = function() {
                    if(!scope.prediction) {
                        scope.prediction = {};
                    }
                    srsViewState.prediction.description = '';
                    scope.allQuestionsAnswered = scope.questionsFilledForVisaButton();
                    if (scope.allQuestionsAnswered) {
                        scope.srs.showVisaKnapp = true;
                    }
                    else {
                        scope.srs.showVisaKnapp = false;
                    }

                };
            },
            templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsQuestionaire.directive.html'
        };
    }]);
