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
    ['$q', 'common.ObjectHelper', 'common.srsProxy', 'common.srsViewState', 'common.authorityService', '$stateParams',
        '$rootScope',
        function($q, ObjectHelper, srsProxy, srsViewState, authorityService, $stateParams, $rootScope) {
            'use strict';

            return {
                restrict: 'E',
                transclude: true,
                link: function(scope, element, attrs) {
                    scope.srsViewState = srsViewState;
                    scope.srsViewState.userHasSrsFeature = checkIfUserHasSrsFeature();
                    scope.status = {
                        open: false
                    };

                    scope.id = attrs.id;

                    // make sure component isn't visible if no id is supplied
                    if(!ObjectHelper.isDefined(scope.id)){
                        scope.srsViewState.srsApplicable = false;
                    }
                    
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
                        scope.retrieveAndSetPrediction().then(function() {
                            scope.srsViewState.showVisaKnapp = false;
                            setPredictionImage();
                            setPrediktionMessages();
                        });
                    };

                    scope.retrieveAndSetPrediction = function() {
                        var qaIds = getSelectedAnswerOptions();
                        return srsProxy.getPrediction($stateParams.certificateId, scope.srsViewState.personId, srsViewState.diagnosKod,
                            qaIds, true, true, true).then(function(prediction) {
                            scope.srsViewState.prediction = prediction;
                        }, function(error) {
                            scope.srsViewState.prediction = 'error';
                        });
                    };

                    scope.retrieveAndSetAtgarderAndStatistik = function() {
                        return srsProxy.getAtgarderAndStatistikForDiagnosis($stateParams.certificateId, scope.srsViewState.personId,
                            srsViewState.diagnosKod)
                            .then(function(data) {
                                scope.srsViewState.statistik = data.statistik || 'error';
                                scope.srsViewState.atgarder = data.atgarder ||'error';
                                if(scope.srsViewState.atgarder !== 'error') {
                                    scope.srsViewState.atgarder.atgarderObs = stringifyAtgarderObs(scope.srsViewState.atgarder.atgarderObs);
                                }
                            }, function(error) {
                                scope.srsViewState.statistik = 'error';
                                scope.srsViewState.atgarder = 'error';
                            });

                        function stringifyAtgarderObs(atgarderObs){
                            var tempAtgarderObs = atgarderObs && atgarderObs.length > 0 ? '<b>Tänk på att</b> ' : '';
                            for(var i = 0; i < atgarderObs.length; i++){
                                tempAtgarderObs += atgarderObs[i];
                                tempAtgarderObs += '. ';
                            }
                            return tempAtgarderObs;
                        }
                    };

                    scope.getQuestions = function(diagnosKod) {
                        return srsProxy.getQuestions(diagnosKod).then(function(questions) {
                            scope.srsViewState.selectedButtons = [];
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
                        scope.srsViewState.consent = consent ? 'JA' : '';
                        srsProxy.setConsent(scope.srsViewState.personId, scope.srsViewState.hsaId, consent);
                    };

                    scope.logSrsButtonClicked = function() {
                        if (scope.status.open && !scope.srsViewState.clickedFirstTime) {
                            scope.srsViewState.clickedFirstTime = true;
                            srsProxy.logSrsClicked();
                        }
                    };

                    scope.logAtgarderLasMerButtonClicked = function() {
                        srsProxy.log();
                    };

                    scope.logAtgarderRekClicked = function() {
                        srsProxy.logSrsAtgardClicked();
                    };

                    scope.logStatistikClicked = function() {
                        srsProxy.logSrsAtgardClicked();
                    };

                    scope.setActiveTab = function(tabname) {
                        scope.srsViewState.activeTab = tabname;
                    };

                    // INTYG-4543: Only use srs endpoints if user has srs-feature enabled.
                    if (scope.srsViewState.userHasSrsFeature && scope.id === '2') {
                        var diagnosisListFetching = loadDiagCodes();
                        // When applicable code is entered, show srs button
                        scope.$watch('srsViewState.diagnosKod', function(newVal, oldVal) {
                            if (newVal !== oldVal) {
                                reset();
                                srsViewState.diagnosKod = newVal;
                                diagnosisListFetching = loadDiagCodes().then(function() {
                                    scope.srsViewState.srsApplicable = isSrsApplicable(scope.srsViewState.diagnosKod);
                                    if (scope.srsViewState.srsApplicable && scope.srsViewState.consent) {
                                        if (!scope.srsViewState.shownFirstTime) {
                                            srsProxy.logSrsShown();
                                        }
                                        scope.srsViewState.shownFirstTime = true;
                                        loadSrs();
                                    }
                                });
                            }
                        });

                        scope.$on('closeSrs', function() {
                            scope.status.open = false;
                        });

                        scope.$watch('srsViewState.hsaId', function(newVal, oldVal) {
                            if (newVal) {
                                scope.srsViewState.consentInfo = '';
                                scope.srsViewState.consentError = '';
                                srsProxy.getConsent(scope.srsViewState.personId, scope.srsViewState.hsaId).then(function(consent) {
                                    if(consent.status === 200){
                                        scope.srsViewState.consent = consent.data;
                                    }
                                    else{
                                        scope.srsViewState.consent = 'error';
                                    }
                                    setConsentMessages();
                                });
                                
                            }
                        });

                        scope.$watch('srsViewState.consent', function(newVal, oldVal) {
                            if (newVal === 'JA' && srsViewState.diagnosKod) {
                                diagnosisListFetching.then(function() {
                                    loadSrs();
                                });
                            } else {
                                if (newVal === false && oldVal === true) {
                                    reset();
                                    scope.srsViewState.srsApplicable = isSrsApplicable(scope.srsViewState.diagnosKod);
                                }
                            }
                        });

                        $('#testtooltip')
                            .tooltip({content: '<b style="color: red">Tooltip</b> <i>text</i>'});
                    }

                    function setConsentMessages() {
                        scope.srsViewState.consentInfo = '';
                        if (scope.srsViewState.diagnosKod !== undefined && scope.srsViewState.diagnosKod.length > 3) {
                            scope.srsViewState.consentInfo = 'Det SRS-stöd som visas är för koden ' + scope.srsViewState.diagnosKod.substring(0, 3);
                        }
                        if (scope.srsViewState.consent === 'error') {
                            scope.srsViewState.consentError = 'Tekniskt fel. Det gick inte att hämta information om samtycket.';
                        }
                    }

                    function setAtgarderMessages(atgarder) {
                        scope.srsViewState.atgarderInfo = '';
                        scope.srsViewState.atgarderError = '';
                        if (scope.srsViewState.atgarder) {
                            if (scope.srsViewState.atgarder === 'error') {
                                scope.srsViewState.atgarderError = 'Tekniskt fel.\nDet gick inte att hämta information om åtgärder.';
                            }
                            else if (scope.srsViewState.atgarder.atgarderStatusCode === 'INFORMATION_SAKNAS') {
                                scope.srsViewState.atgarderInfo = 'Observera! För ' + srsViewState.diagnosKod +
                                    ' finns ingen SRS-information för detta fält.';
                            }
                            else if (!scope.srsViewState.atgarder.atgarderRek ||
                                (scope.srsViewState.atgarder.atgarderRek && scope.srsViewState.atgarder.atgarderRek.length < 1)) {
                                scope.srsViewState.atgarderInfo = 'Observera! För ' + srsViewState.diagnosKod +
                                    ' finns ingen SRS-information för detta fält.';
                            }
                            else if (scope.srsViewState.atgarder.atgarderStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                                scope.srsViewState.atgarderInfo = 'Det SRS-stöd som visas är för koden ' + scope.srsViewState.atgarder.atgarderDiagnosisCode;
                            }
                        }
                    }

                    function setStatistikMessages() {
                        scope.srsViewState.statistikInfo = '';
                        scope.srsViewState.statistikError = '';
                        if (scope.srsViewState.statistik) {
                            if (scope.srsViewState.statistik === 'error') {
                                scope.srsViewState.statistikError =
                                    'Tekniskt fel.\nDet gick inte att hämta information om statistik ' +
                                    srsViewState.diagnosKod;
                            }
                            if (scope.srsViewState.statistik.statistikStatusCode === 'STATISTIK_SAKNAS' ||
                                !scope.srsViewState.statistik.statistikBild) {
                                scope.srsViewState.statistikInfo = 'Observera! För ' + srsViewState.diagnosKod +
                                    ' finns ingen SRS-information för detta fält.';
                            }
                            else if (scope.srsViewState.statistik.statistikStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                                scope.srsViewState.statistikInfo = 'Det SRS-stöd som visas är för koden ' + scope.srsViewState.statistik.statistikDiagnosisCode;
                            }
                        }
                    }

                    function setPrediktionMessages() {
                        scope.srsViewState.prediktionInfo = '';
                        scope.srsViewState.prediktionError = '';
                        if (scope.srsViewState.prediction === 'error') {
                            scope.srsViewState.prediktionError =
                                'Tekniskt fel. \nDet gick inte att hämta information om risk för lång sjukskrivning';
                        } else {
                            if (scope.srsViewState.prediction.statusCode === 'PREDIKTIONSMODELL_SAKNAS') {
                                scope.srsViewState.prediktionInfo = 'Observera! För ' + srsViewState.diagnosKod +
                                    ' finns ingen SRS-information för detta fält.';
                            }
                            else if (scope.srsViewState.prediction.statusCode === 'NOT_OK') {
                                scope.srsViewState.prediktionError =
                                    'Tekniskt fel. \nDet gick inte att hämta information om risk för lång sjukskrivning';
                            }
                            else if (scope.srsViewState.prediction.statusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                                scope.srsViewState.prediktionInfo = 'Det SRS-stöd som visas är för koden ' + scope.srsViewState.prediction.predictionDiagnosisCode;
                            }
                        }
                    }

                    function setPredictionImage() {
                        if (scope.srsViewState.prediction.description === 'Prediktion saknas.') {
                            scope.srsViewState.riskImage = '';
                        }
                        else if (scope.srsViewState.prediction.description === 'Ingen förhöjd risk detekterad.') {
                            scope.srsViewState.riskImage = '/web/webjars/common/webcert/srs/img/latt_forhojd.png';
                            //scope.srsViewState.riskImage = '';
                        }
                        else if (scope.srsViewState.prediction.description === 'Förhöjd risk detekterad.') {
                            scope.srsViewState.riskImage = '/web/webjars/common/webcert/srs/img/mattligt_forhojd.png';
                        }
                        else if (scope.srsViewState.prediction.description === 'Starkt förhöjd risk detekterad.') {
                            scope.srsViewState.riskImage = '/web/webjars/common/webcert/srs/img/starkt_forhojd.png';
                        }
                    }

                    function getSelectedAnswerOptions() {
                        var selectedOptions = [];
                        for (var i = 0; i < scope.srsViewState.questions.length; i++) {
                            selectedOptions.push(
                                {questionId: scope.srsViewState.questions[i].questionId, answerId: scope.srsViewState.questions[i].model.id});
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
                        if (scope.srsViewState.intygsTyp.toLowerCase().indexOf('fk7263') > -1 &&
                            isSrsApplicableForCode(srsViewState.diagnosKod)) {
                            return true;
                        } else {
                            return false;
                        }

                        function isSrsApplicableForCode(diagnosKod) {
                            return diagnosKod !== undefined && scope.srsViewState.diagnosisCodes.indexOf(diagnosKod.substring(0, 3)) !== -1;
                        }
                    }

                    function loadDiagCodes() {
                        return srsProxy.getDiagnosisCodes().then(function(diagnosisCodes) {
                            scope.srsViewState.diagnosisCodes = diagnosisCodes;
                        });
                    }

                    function loadSrs() {
                        scope.getQuestions(scope.srsViewState.diagnosKod).then(function(questions) {
                            setConsentMessages();
                            scope.srsViewState.questions = questions;
                            scope.srsViewState.allQuestionsAnswered = scope.questionsFilledForVisaButton();
                            scope.srsViewState.showVisaKnapp = scope.srsViewState.allQuestionsAnswered;
                            scope.retrieveAndSetAtgarderAndStatistik().then(function() {
                                setAtgarderMessages();
                                setStatistikMessages();
                            });
                            setPrediktionMessages(); // No prediction data as of yet, only used to ensure initial correct state.

                        });
                    }

                    function reset() {
                        scope.srsViewState.questions = [];
                        scope.srsViewState.statistik = {};
                        scope.srsViewState.atgarder = {};
                        scope.srsViewState.prediction = {};
                        scope.srsViewState.prediction.description = '';

                        //scope.consent = false;
                        scope.srsViewState.shownFirstTime = false;
                        scope.srsViewState.clickedFirstTime = false;
                        //scope.srsViewState.diagnosKod = (scope.srsStates.diagnoses['0'] && scope.srsStates.diagnoses['0'].diagnosKod) || '';
                        scope.srsViewState.srsApplicable = false;
                        scope.srsViewState.errorMessage = '';
                        scope.srsViewState.allQuestionsAnswered = false;
                        scope.srsViewState.showVisaKnapp = false;
                        scope.srsViewState.srsButtonVisible = true; // SRS window should not start in fixed position immediately.
                        scope.srsViewState.riskImage = '';

                        //scope.consentInfo = '';
                        //scope.consentError = '';

                        scope.srsViewState.atgarderInfo = '';
                        scope.srsViewState.atgarderError = '';

                        scope.srsViewState.statistikInfo = '';
                        scope.srsViewState.statistikError = '';

                        scope.srsViewState.prediktionInfo = '';
                        scope.srsViewState.prediktionError = '';

                        scope.srsViewState.activeTab = 'atgarder';
                    }


                },
                templateUrl: '/web/webjars/common/webcert/srs/wcSrsHelpDisplay.directive.html'
            };
        }]);
