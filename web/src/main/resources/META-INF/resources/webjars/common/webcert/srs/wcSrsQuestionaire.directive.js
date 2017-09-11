
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

/**
 * Display SRS questionaire
 */
angular.module('common').directive('wcSrsQuestionaire', ['common.srsProxy', '$stateParams',
    function (srsProxy, $stateParams) {
        'use strict';

        return {
            restrict: 'E',
            link: function (scope, element, attrs) {

                scope.getCurrentDiagnosKod = function() {
                    return scope.fmb.diagnosKod;
                }

                srsProxy.getQuestions(scope.getCurrentDiagnosKod()).then(function (questions) {
                    scope.selectedButtons = [];
                    scope.questions = questions;
                    for (var i = 0; i < scope.questions.length; i++) {
                        for (var e = 0; e < scope.questions[i].answerOptions.length; e++) {
                            if (scope.questions[i].answerOptions[e].defaultValue) {
                                scope.questions[i].model = scope.questions[i].answerOptions[e];
                            }
                        }
                    }
                })

                scope.visaClicked = function () {
                    scope.inQuestionaireState = false;
                    scope.getSrs();
                }

            },
            templateUrl: '/web/webjars/common/webcert/srs/wcSrsQuestionaire.directive.html'
        };
    }]);
