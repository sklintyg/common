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
 * Display SRS help texts
 */
angular.module('common').directive('wcSrsHelpDisplay', ['common.srsProxy', 'common.fmbViewState', 'common.srsViewState', '$stateParams',
    function (srsProxy, fmbViewState, srsViewState, $stateParams) {
        'use strict';

        return {
            restrict: 'E',
            transclude: true,
            scope: {
                personId: '=',
                hsaId: '=',
                intygsTyp: '='
            },
            link: function (scope, element, attrs) {
                scope.status = {
                    open: true
                };

                scope.shownFirstTime = false;
                scope.clickedFirstTime = false;
                scope.diagnosKod = srsViewState.diagnosKod;
                scope.srsStates = fmbViewState;
                scope.srsAvailable = false;
                scope.diagnosKod = "";
                scope.getConsentErrorMessage = "";
                scope.riskSignal = "";

                scope.$watch('diagnosKod', function(newVal, oldVal){
                    if(newVal){
                        scope.getQuestions(newVal).then(function(questions){
                            scope.questions = questions;
                            scope.getSrs();
                        });
                    }
                });
                
                scope.$watch('hsaId', function (newVal, oldVal) {
                    if (newVal) {
                        srsProxy.getConsent(scope.personId, scope.hsaId).then(function (consent) {
                            scope.consentGiven = consent === 'JA' ? true : false;
                            //scope.getConsentErrorMessage = "";
                            //scope.getConsentErrorMessage = "Tekniskt fel. Det gick inte att hämta information om samtycket.";
                        });
                    }
                });

                scope.visaClicked = function () {
                    var qaIds = scope.getSelectedAnswerOptions();
                    srsProxy.getRiskSignal($stateParams.certificateId, scope.personId, scope.diagnosKod, qaIds || [], true, true, true).then(function (riskSignal) {
                        scope.riskSignal = riskSignal;
                    })
                }

                scope.getSrs = function(){
                    var qaIds = scope.getSelectedAnswerOptions();
                    console.log(JSON.stringify(qaIds));
                    srsProxy.getSrs($stateParams.certificateId, scope.personId, scope.diagnosKod, qaIds || [], true, true, true).then(function (statistik) {
                        scope.atgarderErrorMessage = "";
                        if(statistik == 'error'){
                            scope.atgarderErrorMessage = "Det gick inte att hämta information om åtgärder";
                        }
                        scope.statistik = statistik;
                        console.log(statistik)
                        scope.atgarderRek = statistik.atgarderRek;
                        scope.atgarderObs = statistik.atgarderObs;
                        setAtgarderObs();
                    })
                }

                function setAtgarderObs() {
                    var atgarderObs = scope.statistik.atgarderObs;
                    scope.statistik.atgardObs = "";
                    for (var i = 0; i < atgarderObs.length; i++) {
                        scope.statistik.atgardObs += atgarderObs[i];
                        scope.statistik.atgardObs += i < atgarderObs.length - 1 ? ", " : "";
                    }
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

                scope.getSelectedAnswerOptions = function() {
                    var selectedOptions = [];
                    if(!scope.questions)
                        return [];
                    for (var i = 0; i < scope.questions.length; i++) {
                        selectedOptions.push({ questionId: scope.questions[i].questionId, answerId: scope.questions[i].model.id });
                    }
                    return selectedOptions;
                }

                scope.setConsent = function (consent) {
                    scope.consentGiven = consent;
                    srsProxy.setConsent(scope.personId, scope.hsaId, consent);
                }

                
                scope.$watch('srsStates.diagnoses["0"].diagnosKod', function (newVal, oldVal) {
                    if (newVal) {
                        scope.diagnosKod = newVal;
                        isSrsAvailable(scope.diagnosKod).then(function (srsAvailable) {
                            scope.srsAvailable = srsAvailable;
                        })
                    }
                })

                scope.logSrsButtonClicked = function () {
                    if (scope.status.open && !scope.clickedFirstTime) {
                        scope.clickedFirstTime = true;
                        srsProxy.logSrsShown();
                    }
                }

                scope.logAtgarderLasMerButtonClicked = function () {
                    srsProxy.log();
                }

                function isSrsAvailable() {
                    return new Promise(function (resolve, reject) {
                        if (scope.intygsTyp.toLowerCase().indexOf('fk7263') > -1) {
                            srsProxy.getDiagnosisCodes().then(function (diagnosisCodes) {
                                scope.diagnosisCodes = diagnosisCodes;
                                for (var i = 0; i < diagnosisCodes.length; i++) {
                                    if (scope.diagnosKod === diagnosisCodes[i]) {
                                        if(!scope.shownFirstTime){
                                            srsProxy.logSrsShown();
                                        }
                                        scope.shownFirstTime = true;
                                        resolve(true);
                                        break;
                                    }
                                }
                                resolve(false);
                            })
                        }
                    })
                }

                /*
                 for (var i = 0; i < diagnosisCodes.length; i++) {
                                    if (scope.diagnosKod === diagnosisCodes[i]) {
                                        if(!scope.shownFirstTime){
                                            srsProxy.logSrsShown();
                                        }
                                        scope.shownFirstTime = true;
                                        resolve(true);
                                        break;
                                    }
                                    else{
                                        for(var i = 1; i < scope.diagnosKod.length; i++){
                                            var higherLevelCode = scope.diagnosKod.substring(0, scope.diagnosKod.length-i);
                                            if(diagnosisCodes[i] === higherLevelCode){
                                                higherLevelCodeExists = true;
                                            }
                                        }
                                        
                                    }
                                }
                */

            },
            templateUrl: '/web/webjars/common/webcert/srs/wcSrsHelpDisplay.directive.html'
        };
    }]);
