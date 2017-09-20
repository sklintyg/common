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

                    function setMessages(statistik) {

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

                    scope.$watch('srsStates.diagnoses["0"].diagnosKod', function(newVal, oldVal) {
                        if (newVal) {
                            scope.diagnosKod = newVal;
                            isSrsAvailable(scope.diagnosKod).then(function(srsAvailable) {
                                scope.srsAvailable = srsAvailable;
                                scope.getQuestions(newVal).then(function(questions) {
                                    scope.questions = questions;
                                    var qaIds = scope.getSelectedAnswerOptions();
                                    srsProxy.getSrs($stateParams.certificateId, scope.personId, scope.diagnosKod, qaIds,
                                        true, true, true).then(function(statistik) {
                                       // if (statistik && statistik !== 'error') {
                                            setMessages(statistik);
                                            scope.statistik = statistik;
                                            scope.atgarderRek = statistik.atgarderRek;
                                            scope.atgarderObs = statistik.atgarderObs;

                                            setAtgarderObs();
                                            scope.allQuestionsAnswered = scope.questionsFilledForVisaButton();
                                            if (scope.allQuestionsAnswered) {
                                                scope.showVisaKnapp = true;
                                            }
                                            else {
                                                scope.showVisaKnapp = false;
                                            }
                                        //}
                                        /*else{
                                            setMessages(statistik);
                                            scope.diagnosKod = "";
                                            scope.srsAvailable = false;
                                            scope.questions = [];
                                            var qaIds = []
                                            scope.statistik = [];
                                            scope.atgarderRek = []
                                            scope.atgarderObs = []
                                        }*/

                                    });

                                });
                            });

                        }else{
                            scope.diagnosKod = "";
                            scope.srsAvailable = false;
                            scope.questions = [];
                            var qaIds = []
                            scope.statistik = [];
                            scope.atgarderRek = []
                            scope.atgarderObs = []
                        }
                        
                    });

                    scope.closeFmb = function() {
                        if (scope.status.open) {
                            $rootScope.$broadcast('closeFmb');
                        }
                    };

                    scope.$on('closeSrs', function() {
                        scope.status.open = false;
                    });

                    scope.questionsFilledForVisaButton = function() {
                        var answers = scope.getSelectedAnswerOptions();
                        for (var i = 0; i < answers.length; i++) {
                            if (!answers[i] || !answers[i].answerId) {
                                return false;
                            }
                        }
                        return true;
                    };

                    scope.$watch('hsaId', function(newVal, oldVal) {
                        if (newVal) {
                            srsProxy.getConsent(scope.personId, scope.hsaId).then(function(consent) {
                                scope.consentGiven = consent === 'JA' ? true : false;
                            });
                        }
                    });

                    $('#testtooltip')
                        .tooltip({content: '<b style="color: red">Tooltip</b> <i>text</i>'});

                    scope.visaClicked = function() {
                        scope.setRiskSignal().then(function(riskSignal) {
                            scope.riskSignal = riskSignal;
                        });
                        scope.showVisaKnapp = false;
                    };

                    scope.editRiskSignal = function(riskSignal) {
                        scope.riskSignal = riskSignal;
                    };

                    scope.setRiskSignal = function() {
                        return $q(function(resolve, reject) {
                            var qaIds = scope.getSelectedAnswerOptions();
                            srsProxy.getRiskSignal($stateParams.certificateId, scope.personId, scope.diagnosKod, qaIds,
                                true, true, true).then(function(riskSignal) {
                                scope.riskSignal = riskSignal;
                                resolve(riskSignal);
                            });
                        });
                    };

                    scope.getSrs = function() {
                        var qaIds = scope.getSelectedAnswerOptions();
                        srsProxy.getSrs($stateParams.certificateId, scope.personId, scope.diagnosKod, qaIds, true, true,
                            true).then(function(statistik) {
                            setMessages(statistik);
                            if (statistik.diagnosisCode !== scope.diagnosKod) {
                                scope.higherDiagnosKod = statistik.diagnosisCode;
                            }
                            scope.atgarderErrorMessage = '';
                            if (statistik === 'error') {
                                scope.atgarderErrorMessage = 'Det gick inte att hämta information om åtgärder';
                            }
                            else {
                                scope.statistik = statistik;
                                scope.atgarderRek = statistik.atgarderRek;
                                scope.atgarderObs = statistik.atgarderObs;

                                scope.atgarderRek = scope.atgarderRek.length > 0 ? scope.atgarderRek :
                                    ['ÅtgärdRek1', 'ÅtgärdRek2', 'ÅtgärdRek3'];
                                scope.atgarderObs = scope.atgarderObs.length > 0 ? scope.atgarderObs :
                                    ['ÅtgärdObs1', 'ÅtgärdObs2', 'ÅtgärdObs3'];
                                scope.statistik.statistikBild =
                                    scope.statistik.statistikBild ? scope.statistik.statistikBild :
                                        'http://dxlfb468n8ekd.cloudfront.net/gsc/8RRLM2/e1/36/f7/e136f736a06747b3a1f18322df08f9fe/images/webcert_-_m75/u59.png?token=6b088289725e4e6e4f6ac8f19930873b';
                                setAtgarderObs();
                            }

                        });
                    };

                    function setAtgarderObs() {
                        var atgarderObs = scope.atgarderObs;
                        scope.statistik.atgardObs = '';
                        for (var i = 0; i < atgarderObs.length; i++) {
                            scope.statistik.atgardObs += atgarderObs[i];
                            scope.statistik.atgardObs += i < atgarderObs.length - 1 ? ', ' : '';
                        }
                    }

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

                    scope.getSelectedAnswerOptions = function() {
                        var selectedOptions = [];
                        if (!scope.questions) {
                            return [];
                        }
                        for (var i = 0; i < scope.questions.length; i++) {
                            selectedOptions.push(
                                {questionId: scope.questions[i].questionId, answerId: scope.questions[i].model.id});
                        }
                        return selectedOptions;
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

                    function isSrsAvailable() {
                        return $q(function(resolve, reject) {
                            if (scope.intygsTyp.toLowerCase().indexOf('fk7263') > -1) {
                                srsProxy.getDiagnosisCodes().then(function(diagnosisCodes) {
                                    //scope.diagnosisCodes = diagnosisCodes;
                                    //scope.higherDiagnosKod = getClosestMatchingDiagnosKod(scope.diagnosKod, scope.diagnosisCodes);
                                    if (scope.higherDiagnosKod) {
                                        scope.atgarderInfoMessage =
                                            'Det FMB-stöd som visas är för koden M79 - Andra sjukdomstillstånd i mjukvävnader som ej klassificeras annorstädes.';
                                    }
                                    for (var i = 0; i < diagnosisCodes.length; i++) {
                                        if (scope.diagnosKod === diagnosisCodes[i] || scope.higherDiagnosKod) {
                                            if (!scope.shownFirstTime) {
                                                srsProxy.logSrsShown();
                                            }
                                            scope.shownFirstTime = true;
                                            resolve(true);
                                            return;
                                        }
                                    }
                                    resolve(false);
                                });
                            } else {
                                resolve(false);
                            }
                        });
                    }

                },
                templateUrl: '/web/webjars/common/webcert/srs/wcSrsHelpDisplay.directive.html'
            };
        }]);
