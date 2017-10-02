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
    ['$q', 'common.srsProxy', 'common.fmbViewState', 'common.srsViewState', 'common.srsLinkCreator', 'common.authorityService',
        '$stateParams', '$rootScope',
        function($q, srsProxy, fmbViewState, srsViewState, srsLinkCreator, authorityService, $stateParams, $rootScope) {
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
                    scope.userHasSrsFeature = checkIfUserHasSrsFeature();
                    scope.status = {
                        open: false
                    };

                    scope.consent = false;
                    scope.shownFirstTime = false;
                    scope.clickedFirstTime = false;
                    scope.originalDiagnosKod = '';
                    scope.diagnosKod = srsViewState.diagnosKod;
                    scope.diagnosisCodes = null;
                    scope.srsStates = fmbViewState;
                    scope.srsApplicable = false;
                    scope.errorMessage = '';
                    scope.riskSignal = '';
                    scope.allQuestionsAnswered = false;
                    scope.higherDiagnosKod = '';
                    scope.showVisaKnapp = false;
                    scope.srsButtonVisible = true; // SRS window should not start in fixed position immediately.
                    scope.riskImage = '';

                    scope.consentInfo = '';
                    scope.consentError = '';

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
                            scope.riskSignal = riskSignal;
                            setPredictionImage(scope.riskSignal);
                            setMessages(scope.statistik);
                        });
                    };

                    scope.editRiskSignal = function(riskSignal) {
                        scope.riskSignal = riskSignal;
                    };

                    scope.testVisaSvar = function(){
                        var qaIds = getSelectedAnswerOptions();
                        console.log(qaIds);
                    };

                    scope.setRiskSignal = function() {
                        return $q(function(resolve, reject) {
                            var qaIds = getSelectedAnswerOptions();
                            srsProxy.getRiskSignal($stateParams.certificateId, scope.personId, scope.diagnosKod, qaIds,
                                true, true, true).then(function(riskSignal) {
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

                    // INTYG-4543: Only use srs endpoints if user has srs-feature enabled.
                    if(scope.userHasSrsFeature) {
                        // Do the first retrieval of which diagnosiscodes srs is applicable for.
                        scope.diagnosisCodesPromise = srsProxy.getDiagnosisCodes().then(function(diagnosisCodes) {
                            scope.diagnosisCodes = diagnosisCodes;
                        });

                        // When applicable code is entered, show srs button
                        scope.$watch('srsStates.diagnoses["0"].diagnosKod', function(newVal, oldVal) {
                            if (newVal != oldVal) {
                                reset();
                                scope.diagnosKod = newVal;
                                scope.diagnosisCodesPromise.then(function() {
                                    scope.srsApplicable = isSrsApplicable(scope.diagnosKod);
                                    if(scope.srsApplicable) {
                                        if (!scope.shownFirstTime) {
                                            srsProxy.logSrsShown();
                                        }
                                        scope.shownFirstTime = true;
                                    }
                                });
                            }
                        });

                        scope.$on('closeSrs', function() {
                            scope.status.open = false;
                        });

                        scope.$watch('hsaId', function(newVal, oldVal) {
                            if (newVal) {
                                scope.consentInfo = '';
                                scope.consentError = '';
                                srsProxy.getConsent(scope.personId, scope.hsaId).then(function(consent) {
                                    scope.consent = consent;
                                    scope.consentGiven = consent === 'JA';
                                });
                            }
                        });

                        // When consent has been given, load everything which requires consent from user
                        scope.$watch('consentGiven', function(newVal, oldVal) {
                            if(newVal === true && scope.diagnosKod) {
                                scope.diagnosisCodesPromise.then(function() {
                                    scope.higherDiagnosKod = getHigherLevelCode(scope.diagnosKod, scope.diagnosisCodes);
                                    if(scope.higherDiagnosKod){
                                        activateHigherCodeMode(scope.diagnosKod, scope.higherDiagnosKod);
                                    }

                                    loadSrs();
                                });
                            }
                        });

                        $('#testtooltip')
                            .tooltip({content: '<b style="color: red">Tooltip</b> <i>text</i>'});
                    }

                    function setMessages(statistik) {
                        setConsentMessages();
                        setAtgarderMessages();
                        setStatistikMessages();
                        setPrediktionMessages();

                        function setConsentMessages(){
                            scope.consentInfo = '';
                            if(scope.higherDiagnosKod){
                                scope.consentInfo = 'Det SRS-stöd som visas är för koden ' + scope.higherDiagnosKod;
                            }
                            if(scope.consent === 'error'){
                                scope.consentError = 'Tekniskt fel. Det gick inte att hämta information om samtycket.';
                            }
                        }

                        function setAtgarderMessages() {
                            scope.atgarderInfo = '';
                            scope.atgarderError = '';
                            if(statistik){
                                if(statistik === 'error'){
                                    scope.atgarderError = 'Tekniskt fel.\nDet gick inte att hämta information om åtgärder.';
                                }
                                else if (statistik.atgarderStatusCode === 'INFORMATION_SAKNAS') {
                                    scope.atgarderInfo = 'Observera! För ' + scope.diagnosKod +
                                        ' finns ingen SRS-information för detta fält.';
                                }
                                else if (statistik.atgarderStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                                    scope.atgarderInfo = 'Det SRS-stöd som visas är för koden ' + scope.higherDiagnosKod;
                                }
                                else if (!statistik.atgarderRek || (statistik.atgarderRek && statistik.atgarderRek.length < 1)) {
                                    scope.atgarderInfo = 'Observera! För ' + scope.diagnosKod +
                                        ' finns ingen SRS-information för detta fält.';
                                }
                            }
                            if(scope.higherDiagnosKod){
                                scope.atgarderInfo = 'Det SRS-stöd som visas är för koden ' + scope.higherDiagnosKod;
                            }
                        }

                        function setStatistikMessages() {
                            scope.statistikInfo = '';
                            scope.statistikError = '';
                            if(statistik){
                                if(statistik === 'error'){
                                    scope.statistikError =
                                    'Tekniskt fel.\nDet gick inte att hämta information om statistik ' + scope.diagnosKod;
                                }
                                if (statistik.statistikStatusCode === 'STATISTIK_SAKNAS' || !statistik.statistikBild) {
                                    scope.statistikInfo = 'Observera! För ' + scope.diagnosKod +
                                        ' finns ingen SRS-information för detta fält.';
                                }
                                else if (statistik.statistikStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                                    scope.statistikInfo = 'Det SRS-stöd som visas är för koden ' + scope.higherDiagnosKod;
                                }
                                else {
                                    scope.statistikError = 'Tekniskt fel.\nDet gick inte att hämta information om statistik';
                                }
                            }
                            if(scope.higherDiagnosKod){
                                scope.statistikInfo = 'Det SRS-stöd som visas är för koden ' + scope.higherDiagnosKod;
                            }
                        }

                        function setPrediktionMessages() {
                            scope.prediktionInfo = '';
                            scope.prediktionError = '';
                            if(statistik){
                                if(statistik === 'error' || scope.riskSignal === 'error'){
                                    //scope.riskSignal = '';
                                    scope.prediktionError = 'Tekniskt fel. \nDet gick inte att hämta information om risk för lång sjukskrivning';
                                }
                                if (statistik.predictionStatusCode === 'PREDIKTION_SAKNAS' || !scope.riskSignal) {
                                    scope.prediktionInfo = 'Observera! För ' + scope.diagnosKod +
                                        ' finns ingen SRS-information för detta fält.';
                                }
                                else if(statistik.predictionStatusCode === 'NOT_OK'){
                                    scope.prediktionError = 'Tekniskt fel. \nDet gick inte att hämta information om risk för lång sjukskrivning';
                                }
                                else if (statistik.predictionStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                                    scope.prediktionInfo = 'Det SRS-stöd som visas är för koden ' + scope.higherDiagnosKod;
                                }
                            }
                            if(scope.higherDiagnosKod){
                                scope.prediktionInfo = 'Det SRS-stöd som visas är för koden ' + scope.higherDiagnosKod;
                            }
                        }

                    }

                    function setPredictionImage(riskSignal){
                        if(riskSignal === 'Prediktion saknas.'){
                            scope.riskImage = '';
                        }
                        else if(riskSignal === 'Ingen förhöjd risk detekterad.'){
                            //scope.riskImage = '/web/webjars/common/webcert/srs/img/Lätt förhöjd.png';
                            scope.riskImage = '';
                        }
                        else if(riskSignal === 'Förhöjd risk detekterad.'){
                            scope.riskImage = '/web/webjars/common/webcert/srs/img/Måttligt förhöjd.png';
                        }
                        else if(riskSignal === 'Starkt förhöjd risk detekterad.'){
                            scope.riskImage = '/web/webjars/common/webcert/srs/img/Starkt förhöjd.png';
                        }
                    }

                    function activateHigherCodeMode(currentDiagnosKod, higherCode){
                        scope.higherCode = higherCode;
                        scope.originalDiagnosKod = currentDiagnosKod;
                        scope.diagnosKod = scope.higherCode;
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

                    function checkIfUserHasSrsFeature() {
                        var options = {
                            feature: 'srs'
                        };
                        return authorityService.isAuthorityActive(options);
                    }

                    function isSrsApplicable() {
                        if (scope.intygsTyp.toLowerCase().indexOf('fk7263') > -1 && isSrsApplicableForCode(scope.diagnosKod)) {
                            return true;
                        } else {
                            return false;
                        }

                        function isSrsApplicableForCode(diagnosKod) {
                            return scope.higherDiagnosKod || scope.diagnosisCodes.find(function(el){return el === diagnosKod;});
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

                    function getHigherLevelCode(diagnosKod, diagnosisCodes){
                        if(diagnosKod && diagnosisCodes){
                            for(var i = 0; i < diagnosisCodes.length; i++){
                                var diagnosisCode = diagnosisCodes[i];
                                if(diagnosKod.indexOf(diagnosisCode) > -1 && diagnosKod !== diagnosisCode){
                                    return diagnosisCode;
                                }
                            }
                        }
                        return '';
                    }

                    function reset() {
                        scope.questions = [];
                        scope.statistik = [];

                        scope.consent = false;
                        scope.shownFirstTime = false;
                        scope.clickedFirstTime = false;
                        scope.originalDiagnosKod = '';
                        scope.diagnosKod = srsViewState.diagnosKod;
                        scope.diagnosisCodesPromise = srsProxy.getDiagnosisCodes().then(function(diagnosisCodes) {
                            scope.diagnosisCodes = diagnosisCodes;
                        });
                        scope.srsStates = fmbViewState;
                        scope.srsApplicable = false;
                        scope.errorMessage = '';
                        scope.riskSignal = '';
                        scope.allQuestionsAnswered = false;
                        scope.higherDiagnosKod = '';
                        scope.showVisaKnapp = false;
                        scope.srsButtonVisible = true; // SRS window should not start in fixed position immediately.
                        scope.riskImage = '';

                        scope.consentInfo = '';
                        scope.consentError = '';

                        scope.atgarderInfo = '';
                        scope.atgarderError = '';

                        scope.statistikInfo = '';
                        scope.statistikError = '';

                        scope.prediktionInfo = '';
                        scope.prediktionError = '';

                        scope.activeTab = 'atgarder';
                    }


                },
                templateUrl: '/web/webjars/common/webcert/srs/wcSrsHelpDisplay.directive.html'
            };
        }]);
