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
angular.module('common').directive('wcSrsHelpDisplay',
    ['$q', 'common.srsProxy', 'common.fmbViewState', 'common.srsViewState', 'common.srsLinkCreator', '$stateParams',
        '$rootScope',
        function($q, srsProxy, fmbViewState, srsViewState, srsLinkCreator, $stateParams, $rootScope) {
            'use strict';

            return {
                restrict: 'E',
                transclude: true,
                scope: {
                    personId: '=',
                    hsaId: '=',
                    intygsTyp: '='
                },
                link: function(scope, element, attrs) {
                    scope.status = {
                        open: false
                    };

                    scope.shownFirstTime = false;
                    scope.clickedFirstTime = false;
                    scope.diagnosKod = srsViewState.diagnosKod;
                    scope.srsStates = fmbViewState;
                    scope.srsAvailable = false;
                    scope.errorMessage = '';
                    scope.riskSignal = '';
                    scope.allQuestionsAnswered = false;
                    scope.higherDiagnosKod = '';
                    scope.showVisaKnapp = false;
                    scope.srsButtonVisible = true; // SRS window should not start in fixed position immediately.

                    scope.atgarderInfo = '';
                    scope.atgarderError = '';

                    scope.statistikInfo = '';
                    scope.statistikError = '';

                    scope.prediktionInfo = '';
                    scope.prediktionError = '';

                    scope.getAtgardLink = srsLinkCreator.createAtgardsrekommendationLink;
                    scope.getPrediktionsModellLink = srsLinkCreator.createPrediktionsModellLink;

                    scope.activeTab = 'atgarder';

                    scope.closeFmb = function() {
                        if (scope.status.open) {
                            $rootScope.$broadcast('closeFmb');
                        }
                    };

                    scope.questionsFilledForVisaButton = function() {
                        var answers = getSelectedAnswerOptions();
                        for (var i = 0; i < answers.length; i++) {
                            if (!answers[i] || !answers[i].answerId) {
                                return false;
                            }
                        }
                        return true;
                    };

                    scope.visaClicked = function() {
                        scope.setRiskSignal().then(function(riskSignal) {
                            scope.showVisaKnapp = false;
                        });
                    };

                    scope.editRiskSignal = function(riskSignal) {
                        scope.riskSignal = riskSignal;
                    };

                    scope.testVisaSvar = function(){
                        var qaIds = getSelectedAnswerOptions();
                        console.log(JSON.stringify(qaIds));
                    };

                    scope.setRiskSignal = function() {
                        return $q(function(resolve, reject) {
                            var qaIds = getSelectedAnswerOptions();
                            srsProxy.getRiskSignal($stateParams.certificateId, scope.personId, scope.diagnosKod, qaIds,
                                true, true, true).then(function(riskSignal) {
                                scope.riskSignal = riskSignal;
                                resolve(riskSignal);
                            });
                        });
                    };

                    scope.getSrs = function() {
                        return $q(function(resolve, reject){
                            var qaIds = getSelectedAnswerOptions();
                            srsProxy.getSrs($stateParams.certificateId, scope.personId, scope.diagnosKod, qaIds, true, true,
                                true).then(function(statistik) {
                                    scope.statistik = statistik || createEmptyStatistik();
                                    resolve(statistik);
                            }, function(err){
                                console.log(err);
                                resolve();
                            });
                        });
                    };

                    scope.getQuestions = function(diagnosKod) {
                        return srsProxy.getQuestions(diagnosKod).then(function(questions) {
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
                        });
                    };

                    scope.setConsent = function(consent) {
                        scope.consentGiven = consent;
                        srsProxy.setConsent(scope.personId, scope.hsaId, consent);
                    };

                    scope.logSrsButtonClicked = function() {
                        if (scope.status.open && !scope.clickedFirstTime) {
                            scope.clickedFirstTime = true;
                            srsProxy.logSrsClicked();
                        }
                    };

                    scope.logAtgarderLasMerButtonClicked = function() {
                        srsProxy.log();
                    };

                    scope.logAtgarderRekClicked = function(){
                        srsProxy.logSrsAtgardClicked();
                    };

                    scope.logStatistikClicked = function(){
                        srsProxy.logSrsAtgardClicked();
                    };

                    scope.setActiveTab = function(tabname){
                        scope.activeTab = tabname;
                    };

                    function setMessages(statistik) {
                        setAtgarderMessages();
                        setStatistikMessages();
                        setPrediktionMessages();

                        function setAtgarderMessages() {
                            scope.atgarderInfo = '';
                            scope.atgarderError = '';
                            if (statistik.atgarderStatusCode !== 'OK') {
                                if (statistik.atgarderStatusCode === 'INFORMATION_SAKNAS') {
                                    scope.atgarderInfo = 'Observera! För ' + scope.diagnosKod +
                                        ' finns ingen SRS-information för detta fält.';
                                }
                                else if (statistik.atgarderStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                                    scope.atgarderInfo = 'Det SRS-stöd som visas är för koden ' + scope.diagnosKod;
                                }
                                else {
                                    scope.atgarderError = 'Tekniskt fel.\nDet gick inte att hämta information om åtgärder';
                                }
                            }
                        }

                        function setStatistikMessages() {
                            scope.statistikInfo = '';
                            scope.statistikError = '';
                            if (statistik.statistikStatusCode !== 'OK') {
                                if (statistik.statistikStatusCode === 'STATISTIK_SAKNAS') {
                                    scope.statistikInfo = 'Observera! För ' + scope.diagnosKod +
                                        ' finns ingen SRS-information för detta fält.';
                                }
                                else if (statistik.statistikStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                                    scope.statistikInfo = 'Det SRS-stöd som visas är för koden ' + scope.diagnosKod;
                                }
                                else {
                                    scope.statistikError =
                                        'Tekniskt fel.\nDet gick inte att hämta information om statistik ' + scope.diagnosKod;
                                }
                            }
                        }

                        function setPrediktionMessages() {
                            scope.prediktionInfo = '';
                            scope.prediktionError = '';
                            if (statistik.predictionStatusCode !== 'OK') {
                                if (statistik.predictionStatusCode === 'PREDIKTION_SAKNAS') {
                                    scope.prediktionInfo = 'Observera! För ' + scope.diagnosKod +
                                        ' finns ingen SRS-information för detta fält.';
                                }
                                else if(statistik.predictionStatusCode === 'NOT_OK'){
                                    scope.predictionError = 'Tekniskt fel. \nDet gick inte att hämta information om risk för lång sjukskrivning';
                                }
                                else if (statistik.predictionStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                                    scope.prediktionInfo = 'Det SRS-stöd som visas är för koden ' + scope.diagnosKod;
                                }
                                else {
                                    scope.prediktionError =
                                        'Det gick inte att hämta information om risk för lång sjukskrivning';
                                }
                            }
                        }

                    }

                    function createEmptyStatistik(){
                        var statistik = {atgarderRek: [], atgarderObs: []};
                        return statistik;
                    }

                    function getSelectedAnswerOptions() {
                        var selectedOptions = [];
                        if (!scope.questions) {
                            return [];
                        }
                        for (var i = 0; i < scope.questions.length; i++) {
                            selectedOptions.push(
                                {questionId: scope.questions[i].questionId, answerId: scope.questions[i].model.id});
                        }
                        return selectedOptions;
                    }

                    function isSrsAvailable() {
                        return $q(function(resolve, reject) {
                            if (scope.intygsTyp.toLowerCase().indexOf('fk7263') > -1) {
                                srsProxy.getDiagnosisCodes().then(function(diagnosisCodes) {
                                    if(currentDiagnosKodInDiagnosisCodes(scope.diagnosKod, diagnosisCodes)){
                                        if (!scope.shownFirstTime) {
                                            srsProxy.logSrsShown();
                                        }
                                        scope.shownFirstTime = true;
                                        resolve(true);
                                        return;
                                    }
                                    else{
                                        resolve(false);
                                        return;
                                    }
                                });
                            } else {
                                resolve(false);
                                return;
                            }
                        });

                        function currentDiagnosKodInDiagnosisCodes(diagnosKod, diagnosisCodes){
                            for(var i = 0; i < diagnosisCodes.length; i++){
                                var diagnosisCode = diagnosisCodes[i];
                                if(diagnosisCode === diagnosKod){
                                    return true;
                                }
                            }
                            return false;
                        }

                    }

                    function loadSrs(){
                        scope.getQuestions(scope.diagnosKod).then(function(questions) {
                            scope.questions = questions;
                            scope.allQuestionsAnswered = scope.questionsFilledForVisaButton();
                            scope.getSrs().then(function(statistik) {
                                setMessages(statistik);
                                scope.statistik = statistik;
                                scope.atgarderErrorMessage = '';
                                if (scope.allQuestionsAnswered) {
                                    scope.showVisaKnapp = true;
                                }
                                else {
                                    scope.showVisaKnapp = false;
                                }
                            });

                        });
                    }

                    /*function getHigherLevelCode(diagnosKod, diagnosisCodes){
                        if(diagnosKod && diagnosisCodes){
                            for(var i = 0; i < diagnosisCodes.length; i++){
                                var diagnosisCode = diagnosisCode[i];
                                if(diagnosKod.indexOf(diagnosisCode) > -1){
                                    return diagnosisCode;
                                }
                            }
                        }
                        return '';
                    }*/

                    scope.$watch('srsStates.diagnoses["0"].diagnosKod', function(newVal, oldVal) {
                        if (newVal) {
                            scope.diagnosKod = newVal;
                            isSrsAvailable(scope.diagnosKod).then(function(srsAvailable) {
                                scope.srsAvailable = srsAvailable;
                                if(srsAvailable){
                                    loadSrs();
                                }
                                else{
                                    scope.srsAvailable = false;
                                    scope.questions = [];
                                    scope.statistik = [];
                                }
                            });

                        }else{
                            scope.diagnosKod = '';
                            scope.srsAvailable = false;
                            scope.questions = [];
                            scope.statistik = [];
                        }
                    });

                    scope.$on('closeSrs', function() {
                        scope.status.open = false;
                    });

                    scope.$watch('hsaId', function(newVal, oldVal) {
                        if (newVal) {
                            srsProxy.getConsent(scope.personId, scope.hsaId).then(function(consent) {
                                scope.consentGiven = consent === 'JA' ? true : false;
                            });
                        }
                    });

                    $('#testtooltip')
                        .tooltip({content: '<b style="color: red">Tooltip</b> <i>text</i>'});

                },
                templateUrl: '/web/webjars/common/webcert/srs/wcSrsHelpDisplay.directive.html'
            };
        }]);
