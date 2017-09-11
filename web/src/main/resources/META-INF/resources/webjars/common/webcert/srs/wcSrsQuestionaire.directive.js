
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
angular.module('common').directive('wcSrsQuestionaire', ['common.ObjectHelper', 'common.fmbViewState', 'common.srsProxy', '$stateParams',
    function (ObjectHelper, fmbViewState, srsProxy, $stateParams) {
        'use strict';

        return {
            restrict: 'E',
            link: function (scope, element, attrs) {

                //scope.riskData = ["1 - Prediktion saknas", "2 - Låg", "3 - mellan", "4 - hög"];

                srsProxy.getQuestions(getCurrentDiagnosKod()).then(function (questions) {
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
                    //var opt = [{questionId: 1, answerId: 1}];
                    var qaIds = getSelectedAnswerOptions();
                    srsProxy.getSrs($stateParams.certificateId, scope.personId, getCurrentDiagnosKod(), qaIds, true, true, true).then(function (statistik) {
                        scope.statistik = statistik;
                        console.log(statistik);
                        setAtgarderObs();
                        scope.inQuestionaireState = false;
                    })
                }

                function getCurrentDiagnosKod() {
                    return scope.fmb.diagnosKod;
                }

                function getSelectedAnswerOptions() {
                    var selectedOptions = [];
                    for (var i = 0; i < scope.questions.length; i++) {
                        selectedOptions.push({ questionId: scope.questions[i].questionId, answerId: scope.questions[i].model.id });
                    }
                    return selectedOptions;
                }

                function setAtgarderObs() {
                    var atgarderObs = scope.statistik.atgarderObs;
                    scope.statistik.atgardObs = "";
                    for (var i = 0; i < atgarderObs.length; i++) {
                        scope.statistik.atgardObs += atgarderObs[i];
                        scope.statistik.atgardObs += i < atgarderObs.length - 1 ? ", " : "";
                    }
                }

            },
            templateUrl: '/web/webjars/common/webcert/srs/wcSrsQuestionaire.directive.html'
        };
    }]);
