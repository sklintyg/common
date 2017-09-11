
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
angular.module('common').directive('wcSrsQuestionaire', ['common.srsProxy',
    function (srsProxy) {
        'use strict';

        return {
            restrict: 'E',
            link: function (scope, element, attrs) {

                scope.$watch('diagnosKod', function(newVal, oldVal){
                    if(newVal){
                        console.log(newVal);
                        scope.getQuestions(newVal).then(function(data){
                            scope.questions = data;
                        })
                    }
                })

                scope.visaClicked = function () {
                    scope.inQuestionaireState = false;
                    scope.getSrs();
                }

                scope.getQuestions = function(diagnosKod){
                    return srsProxy.getQuestions(diagnosKod).then(function (questions) {
                        scope.selectedButtons = [];
                        var qas = questions;
                        for (var i = 0; i < questions.length; i++) {
                            for (var e = 0; e < questions[i].answerOptions.length; e++) {
                                if (questions[i].answerOptions[e].defaultValue) {
                                    qas[i].model = questions[i].answerOptions[e];
                                }
                            }
                        }
                        return qas;
                    })
                }

            },
            templateUrl: '/web/webjars/common/webcert/srs/wcSrsQuestionaire.directive.html'
        };
    }]);
