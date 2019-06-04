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
angular.module('common').directive('wcSrsPanelTab',
    [ 'common.ObjectHelper', 'common.srsProxy', 'common.authorityService', '$stateParams',
        /*from FMB with tweaks--->*/ 'common.anchorScrollService', 'common.srsService',
        'common.srsViewState', '$log','$timeout',
    function(ObjectHelper, srsProxy, authorityService, $stateParams,
             anchorScrollService, srsService,
             srsViewState, $log, $timeout) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsPanelTab.directive.html',
        link: function($scope, $element, $attrs) {
            $scope.srs = srsViewState;
            $scope.isQuestionsCollapsed = false;

            $scope.questionsFilledForVisaButton = function() {
                debugLog('$scope.questionsFilledForVisaButton()');
                var answers = getSelectedAnswerOptions();
                for (var i = 0; i < answers.length; i++) {
                    if (!answers[i] || !answers[i].answerId) {
                        return false;
                    }
                }
                return true;
            };

            $scope.visaClicked = function() {
                debugLog('$scope.visaClicked()');
                $scope.retrieveAndSetPrediction().then(function() {
                    $scope.srs.showVisaKnapp = false;
                    setPredictionImage();
                    setPrediktionMessages();
                    setPredictionRiskLevel();
                });
            };

            $scope.retrieveAndSetPrediction = function() {
                debugLog('$scope.retrieveAndSetPrediction()');
                var qaIds = getSelectedAnswerOptions();
                return srsProxy.getPrediction($scope.srs.intygId, $scope.srs.personId, srsViewState.diagnosKod,
                    qaIds, true, true, true).then(function(prediction) {
                        debugLog('new prediction', prediction);
                        $scope.srs.prediction = prediction;
                }, function(error) {
                    $scope.srs.prediction = 'error';
                });
            };

            /**
             * Because IE don't support Array.find
             * @param arr
             * @param matcherFn Function to find matching elements e.g. function(entry){return entry.name==='test'}
             * @returns {*}
             */
            function altFind(arr, matcherFn) {
                if (arr.find) {
                    return arr.find(matcherFn);
                } else {
                    for (var i = 0; i < arr.length; i++) {
                        var match = matcherFn(arr[i]);
                        if (match) {
                            return arr[i];
                        }
                    }
                    return null;
                }
            }

            $scope.retrieveAndSetAtgarderAndStatistikAndHistoricPrediction = function() {
                debugLog('$scope.retrieveAndSetAtgarderAndStatistikAndHistoricPrediction()');
                return srsProxy.getAtgarderAndStatistikAndHistoricPredictionForDiagnosis($scope.srs.intygId, $scope.srs.personId,
                    srsViewState.diagnosKod)
                    .then(function(data) {
                        debugLog('got atgarder and statistik', data);
                        debugLog('atgarder', data.atgarder);
                        $scope.srs.statistik = data.statistik || 'error';
                        $scope.srs.atgarder = data.atgarder || 'error';
                        $scope.srs.prediction = data.prediktion || 'error';
                        if($scope.srs.atgarder !== 'error') {
                            $scope.srs.atgarder.atgarderObs.forEach(function(a){
                                a.collapsed = true;
                            });
                            $scope.srs.atgarder.atgarderRek.forEach(function(a){
                                a.collapsed = true;
                            });
                            // Slice åtgärder and rekommendationer into 4 + the rest
                            $scope.srs.atgarder.firstAtgarderObs = $scope.srs.atgarder.atgarderObs.slice(0,4);
                            $scope.srs.atgarder.moreAtgarderObs =
                                $scope.srs.atgarder.atgarderObs.slice(4).length > 0 ? $scope.srs.atgarder.atgarderObs.slice(4):null;
                            $scope.srs.atgarder.firstAtgarderRek = $scope.srs.atgarder.atgarderRek.slice(0,4);
                            $scope.srs.atgarder.moreAtgarderRek =
                                $scope.srs.atgarder.atgarderRek.slice(4).length > 0 ? $scope.srs.atgarder.atgarderRek.slice(4):null;
                        }

                        // Update the selected answers to the received stored answer
                        if ($scope.srs.prediction.predictionQuestionsResponses) {
                            debugLog('questions', $scope.srs.questions);
                            $scope.srs.prediction.predictionQuestionsResponses.forEach(function(qnr) {
                                // find correct question and answer option (in the scope) for received qnr
                                debugLog('finding question for received qnr', qnr);
                                // var correspondingQuestion = $scope.srs.questions.find(function(q){return qnr.questionId===q.questionId;});
                                var correspondingQuestion =
                                    altFind($scope.srs.questions, function(q){return qnr.questionId===q.questionId;});
                                // some prediction params like "Region" aren't reflected as questions in the gui so if we don't get a
                                // match, ignore that one
                                if (correspondingQuestion) {
                                    debugLog('finding answer option for question', correspondingQuestion);
                                    var storedAnswer = altFind(correspondingQuestion.answerOptions, function (a) {
                                        return qnr.answerId === a.id;
                                    });
                                    correspondingQuestion.model = storedAnswer;
                                }
                            });
                        }
                    }, function(error) {
                        $scope.srs.statistik = 'error';
                        $scope.srs.atgarder = 'error';
                        $scope.srs.prediction = 'error';
                    });
            };

            $scope.getQuestions = function(diagnosKod) {
                debugLog('$scope.getQuestions()');
                return srsProxy.getQuestions(diagnosKod).then(function(questions) {
                    debugLog('got questions', questions);
                    $scope.srs.selectedButtons = [];
                    var qas = questions;
                    debugLog('printing models before');
                    qas.forEach(function(qa){debugLog(qa.model);});
                    for (var i = 0; i < questions.length; i++) {
                        for (var e = 0; e < questions[i].answerOptions.length; e++) {
                            if (questions[i].answerOptions[e].defaultValue) {
                                qas[i].model = questions[i].answerOptions[e];
                            }
                        }
                    }
                    debugLog('printing models after');
                    qas.forEach(function(qa){debugLog(qa.model);});
                    return qas;
                });
            };

            $scope.setConsent = function(consent) {
                debugLog('setConsent', consent);
                $scope.srs.consent = consent ? 'JA' : 'NEJ';
                debugLog('$scope.srs.consent', $scope.srs.consent);
                srsProxy.setConsent($scope.srs.personId, $scope.srs.hsaId, consent);
                if ($scope.srs.consent === 'NEJ') {
                    // $scope.retrieveAndSetPrediction()
                    debugLog('Removed consent, cleaning prediction', $scope.srs.prediction);
                    var cleanPrediction = angular.copy($scope.srs.prediction);
                    cleanPrediction.probabilityOverLimit = null;
                    debugLog('clean prediction', cleanPrediction);
                    $scope.srs.prediction = cleanPrediction;
                }
            };

            $scope.setOpinion = function(opinion) {
                debugLog('$scope.setOwnOpinion()', opinion, $scope.srs.intygId, $scope.srs.hsaId, $scope.srs.prediction.predictionDiagnosisCode);
                srsProxy.setOwnOpinion(opinion, $scope.srs.vardgivareHsaId, $scope.srs.hsaId,
                    $scope.srs.intygId, $scope.srs.prediction.predictionDiagnosisCode).then(function(result) {
                    debugLog('opinion set on server', result);
                    $scope.srs.prediction.opinion = opinion;
                }, function(error) {
                    debugLog('Error setting opinion on server', error);
                    $scope.srs.prediction.opinionError = 'Fel när egen bedömning skulle sparas';
                });
            };

            $scope.logSrsButtonClicked = function() {
                debugLog('$scope.logSrsButtonClicked()');
                if (!$scope.srs.clickedFirstTime) {
                    $scope.srs.clickedFirstTime = true;
                    srsProxy.logSrsClicked();
                }
            };

            $scope.logAtgarderLasMerButtonClicked = function() {
                debugLog('$scope.logAtgarderLasMerButtonClicked()');
                srsProxy.logSrsAtgardClicked();
            };

            $scope.logStatistikLasMerButtonClicked = function() {
                debugLog('$scope.logStatistikClicked()');
                srsProxy.logSrsStatistikClicked();
            };

            $scope.setActiveTab = function(tabname) {
                debugLog('$scope.setActiveTab()');
                $scope.srs.activeTab = tabname;
            };

            /**
             * Starts the flow when the user enters the certificate editor
             * or the editor is reloaded.
             */
            $scope.$on('intyg.loaded', function(event, content) {
                    debugLog('EVENT: "intyg.loaded"', event, content);
                    if (content.grundData.relation.relationKod === 'FRLANG') {
                        $scope.srs.isForlangning = true;
                        $scope.srs.intygId = content.grundData.relation.relationIntygsId;
                    } else {
                        $scope.srs.isForlangning = false;
                        $scope.srs.intygId = $stateParams.certificateId;
                    }

                    if(!$scope.srs.diagnosisListFetching) {
                        loadDiagCodes();
                    }
                    $scope.srs.userHasSrsFeature = checkIfUserHasSrsFeature();
                    debugLog('$scope.srs.userHasSrsFeature', $scope.srs.userHasSrsFeature);
                    // INTYG-4543: Only use srs endpoints if user has srs-feature enabled.
                    if ($scope.srs.userHasSrsFeature) {
                        // TODO: Ta förmodligen bort det här när slutligt beslut om UI för diagnoskod på högre nivå tagits
                        // $scope.srs.consentInfo = '';
                        $scope.srs.consentError = '';
                        $scope.srs.consent = '';
                        srsProxy.getConsent($scope.srs.personId, $scope.srs.hsaId).then(function(consent) {
                            if(consent.status === 200){
                                $scope.srs.consent = consent.data;
                            }
                            else{
                                $scope.srs.consent = 'error';
                            }
                            setConsentMessages();
                        });

                        applySrsForDiagnosCode();
                        $scope.$watch('srs.originalDiagnosKod', function(newVal, oldVal) {
                            if (newVal === null) {
                                reset();
                            } else if (newVal !== oldVal) {
                                applySrsForDiagnosCode();
                            }
                        });

                        debugLog('setting watch on scope param srs.consent');
                        $scope.$watch('srs.consent', function(newVal, oldVal) {
                            debugLog('$scope.srs.consent from, to', oldVal, newVal);
                            debugLog('srsViewState.diagnosKod', srsViewState.diagnosKod);
                            if (newVal === 'JA' && srsViewState.diagnosKod) {
                                srsViewState.diagnosisListFetching.then(function() {
                                    loadSrs();
                                });
                            } else {
                                if (newVal === false && oldVal === true) {
                                    reset();
                                    $scope.srs.srsApplicable = isSrsApplicable($scope.srs.diagnosKod);

                                }
                            }
                        });

                        $('#testtooltip')
                            .tooltip({content: '<b style="color: red">Tooltip</b> <i>text</i>'});
                    }
                }
            );

            function debugLog() {
                var debugLogEnabled = false;
                if (debugLogEnabled) {
                    console.log.apply(null, arguments);
                }
            }

            function applySrsForDiagnosCode() {
                debugLog('applySrsForDiagnosisCode()');
                resetMessages();
                reset();
                $scope.srs.diagnosKod = srsViewState.originalDiagnosKod;
                $scope.srs.diagnosisListFetching.then(function() {
                    $scope.srs.srsApplicable = isSrsApplicable($scope.srs.diagnosKod);
                    // TODO: Check if we remove patient data already otherwise replace this with
                    //  removing patient data if the consent is removed
                    // if ($scope.srs.srsApplicable && $scope.srs.consent === 'JA') {
                    if ($scope.srs.srsApplicable) {
                        if (!$scope.srs.shownFirstTime) {
                            srsProxy.logSrsShown();
                        }
                        $scope.srs.shownFirstTime = true;
                        loadSrs();
                    }
                });
            }

            function setConsentMessages() {
                debugLog('setConsentMessages()');
                // TODO: Ta förmodligen bort det här när slutligt beslut om UI för diagnoskod på högre nivå tagits
                // $scope.srs.consentInfo = '';
                // Assumption: only codes of exactly length 3 will be supported by SRS predictions.
                // if ($scope.srs.diagnosKod !== undefined && $scope.srs.diagnosKod.length > 3) {
                //     $scope.srs.consentInfo = 'Det SRS-stöd som visas är för koden ' + $scope.srs.diagnosKod.substring(0, 3);
                // }
                if ($scope.srs.consent === 'error') {
                    $scope.srs.consentError = 'Tekniskt fel. Det gick inte att hämta information om samtycket.';
                }
            }

            function setAtgarderMessages() {
                debugLog('setAtgarderMessages()', $scope.srs.atgarder);
                $scope.srs.atgarderInfo = '';
                $scope.srs.atgarderError = '';
                if ($scope.srs.atgarder) {
                    if ($scope.srs.atgarder === 'error') {
                        $scope.srs.atgarderError = 'Tekniskt fel.\nDet gick inte att hämta information om åtgärder.';
                    }
                    else if ($scope.srs.atgarder.atgarderStatusCode === 'INFORMATION_SAKNAS') {
                        $scope.srs.atgarderInfo = 'Observera! För ' + $scope.srs.diagnosKod +
                            ' - ' + $scope.srs.diagnosBeskrivning + ' finns ingen SRS-information för detta fält.';
                    }
                    else if (!$scope.srs.atgarder.atgarderRek ||
                        ($scope.srs.atgarder.atgarderRek && $scope.srs.atgarder.atgarderRek.length < 1)) {
                        $scope.srs.atgarderInfo = 'Observera! För ' + srsViewState.diagnosKod +
                            ' finns ingen SRS-information för detta fält.';
                    }

                    // TODO: Ta förmodligen bort det här när slutligt beslut om UI för diagnoskod på högre nivå tagits
                    // else if ($scope.srs.atgarder.atgarderStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                    //     $scope.srs.atgarderInfo = 'Det SRS-stöd som visas är för koden ' + $scope.srs.atgarder.atgarderDiagnosisCode +
                    //         ' - ' + $scope.srs.atgarder.atgarderDiagnosisDescription;
                    // }
                }
            }

            function setStatistikMessages() {
                debugLog('setStatistikMessages()', $scope.srs.statistik);
                $scope.srs.statistikInfo = '';
                $scope.srs.statistikError = '';
                if ($scope.srs.statistik) {
                    if ($scope.srs.statistik === 'error') {
                        $scope.srs.statistikError =
                            'Tekniskt fel.\nDet gick inte att hämta information om statistik';
                    }
                    else if ($scope.srs.statistik.statistikStatusCode === 'STATISTIK_SAKNAS' ||
                        !$scope.srs.statistik.nationellStatistik) {
                        $scope.srs.statistikInfo = 'Observera! För ' + srsViewState.diagnosKod +
                            ' finns ingen SRS-information för detta fält.';
                    }
                    // TODO: Ta förmodligen bort det här när slutligt beslut om UI för diagnoskod på högre nivå tagits
                    // else if ($scope.srs.statistik.statistikStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                    //     $scope.srs.statistikInfo = 'Det SRS-stöd som visas är för koden ' + $scope.srs.statistik.statistikDiagnosisCode;
                    // }
                }
            }

            function setPrediktionMessages() {
                debugLog('setPrediktionMessages()');
                $scope.srs.prediktionInfo = '';
                $scope.srs.prediktionError = '';
                if ($scope.srs.prediction === 'error') {
                    $scope.srs.prediktionError =
                        'Tekniskt fel. \nDet gick inte att hämta information om risk för lång sjukskrivning';
                } else {
                    if ($scope.srs.prediction.statusCode === 'PREDIKTIONSMODELL_SAKNAS') {
                        $scope.srs.prediktionInfo = 'Observera! För ' + $scope.srs.diagnosKod +
                            ' - ' + $scope.srs.diagnosBeskrivning + ' finns ingen SRS-information för detta fält.';
                    }
                    else if ($scope.srs.prediction.statusCode === 'NOT_OK') {
                        $scope.srs.prediktionError =
                            'Tekniskt fel. \nDet gick inte att hämta information om risk för lång sjukskrivning';
                    }
                    else if ($scope.srs.prediction.statusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                        $scope.srs.prediktionInfo = 'Det SRS-stöd som visas är för koden ' + $scope.srs.prediction.predictionDiagnosisCode +
                            ' - ' + $scope.srs.prediction.predictionDiagnosisDescription;
                    }
                }
            }

            function setPredictionRiskLevel() {
                debugLog('setPredictionRiskLevel()');
                if ($scope.srs.prediction.probabilityOverLimit && $scope.srs.prediction.probabilityOverLimit !== null) {
                    var probability = $scope.srs.prediction.probabilityOverLimit;
                    var riskLevel = null;
                    if (probability < 0.39) {
                        riskLevel = 'Måttlig risk';
                    } else if (probability >= 0.39 && probability <= 0.62) {
                        riskLevel = 'Hög risk';
                    } else if (probability > 0.62) {
                        riskLevel = 'Mycket hög risk';
                    }
                    debugLog('probability: ' + probability + ' -> riskLevel: ' + riskLevel, typeof probability);
                    $scope.srs.prediction.riskLevel = riskLevel;
                }
            }

            function setPredictionImage() {
                debugLog('setPredicitionImage()');
                if ($scope.srs.prediction.description === 'Prediktion saknas.') {
                    $scope.srs.riskImage = '';
                }
                else if ($scope.srs.prediction.description === 'Lätt förhöjd risk') {
                    $scope.srs.riskImage = '/web/webjars/common/webcert/utkast/srs/img/latt_forhojd.png';
                }
                else if ($scope.srs.prediction.description === 'Måttligt förhöjd risk') {
                    $scope.srs.riskImage = '/web/webjars/common/webcert/utkast/srs/img/mattligt_forhojd.png';
                }
                else if ($scope.srs.prediction.description === 'Starkt förhöjd risk') {
                    $scope.srs.riskImage = '/web/webjars/common/webcert/utkast/srs/img/starkt_forhojd.png';
                }
            }

            function getSelectedAnswerOptions() {
                debugLog('getSelectedAnswerOptions()');
                var selectedOptions = [];
                for (var i = 0; i < $scope.srs.questions.length; i++) {
                    selectedOptions.push(
                        {questionId: $scope.srs.questions[i].questionId, answerId: $scope.srs.questions[i].model.id});
                }
                return selectedOptions;
            }

            function checkIfUserHasSrsFeature() {
                debugLog('checkIfUserHasSrsFeature()');
                var options = {
                    feature: 'SRS',
                    intygstyp: $scope.srs.intygsTyp
                };
                return authorityService.isAuthorityActive(options);
            }

            function isSrsApplicable() {
                debugLog('isSrsApplicable intygstyp:', $scope.srs.intygsTyp);
                if (($scope.srs.intygsTyp.toLowerCase().indexOf('fk7263') > -1 || $scope.srs.intygsTyp.toLowerCase().indexOf('lisjp') > -1) &&
                    isSrsApplicableForCode(srsViewState.diagnosKod)) {
                    return true;
                } else {
                    return false;
                }

                function isSrsApplicableForCode(diagnosKod) {
                    return diagnosKod !== undefined &&
                        diagnosKod !== null &&
                        $scope.srs.diagnosisCodes.indexOf(diagnosKod.substring(0, 3)) !== -1;
                }
            }

            function loadDiagCodes() {
                debugLog('loadDiagCodes()');
                $scope.srs.diagnosisListFetching = srsProxy.getDiagnosisCodes().then(function(diagnosisCodes) {
                    debugLog('got diagnosis codes', diagnosisCodes);
                    $scope.srs.diagnosisCodes = diagnosisCodes;
                });
            }

            function loadSrs() {
                debugLog('loadSrs()');
                $scope.getQuestions($scope.srs.diagnosKod).then(function(questions) {
                    debugLog('GOT THE QUESTIONS QAS', questions);
                    setConsentMessages();
                    $scope.srs.questions = questions;
                    $scope.srs.allQuestionsAnswered = $scope.questionsFilledForVisaButton();
                    $scope.srs.showVisaKnapp = $scope.srs.allQuestionsAnswered;
                    $scope.retrieveAndSetAtgarderAndStatistikAndHistoricPrediction().then(function() {
                        setAtgarderMessages();
                        setStatistikMessages();
                        setPrediktionMessages();
                        setPredictionRiskLevel();
                    });
                    setPrediktionMessages(); // No prediction data as of yet, only used to ensure initial correct state.
                });
            }

            function resetMessages(){
                debugLog('resetMessages()');
                // $scope.srs.consentInfo = '';
                $scope.srs.consentError = '';

                $scope.srs.atgarderInfo = '';
                $scope.srs.atgarderError = '';

                $scope.srs.statistikInfo = '';
                $scope.srs.statistikError = '';

                $scope.srs.prediktionInfo = '';
                $scope.srs.prediktionError = '';
            }

            function reset() {
                debugLog('reset()');
                // $scope.srs.intygId = null;
                // $scope.srs.isForlangning = false;
                $scope.srs.questions = [];
                $scope.srs.statistik = {};
                $scope.srs.atgarder = {};
                $scope.srs.prediction = {};
                $scope.srs.prediction.description = '';

                // TODO: what value should consent have on reset, do we need to/should we really reset consent here?
                //$scope.consent = false;
                $scope.srs.shownFirstTime = false;
                $scope.srs.clickedFirstTime = false;
                // TODO: What value should diagnosKod have on reset?
                //$scope.srs.diagnosKod = ($scope.srsStates.diagnoses['0'] && $scope.srsStates.diagnoses['0'].diagnosKod) || '';
                $scope.srs.srsApplicable = false;
                $scope.srs.errorMessage = '';
                $scope.srs.allQuestionsAnswered = false;
                $scope.srs.showVisaKnapp = false;
                $scope.srs.srsButtonVisible = true; // SRS window should not start in fixed position immediately.
                $scope.srs.riskImage = '';


                $scope.srs.activeTab = 'atgarder';
            }

            $scope.$watch('srs.consent', function(newVal,oldVal) {
                debugLog('caught a change of consent from: ' + oldVal + ' to: ' + newVal);
            });

            $scope.$on('panel.activated', function(event, activatedPanelId) {
                if (activatedPanelId === 'wc-srs-panel-tab') {
                    debugLog('Activated SRS panel');
                }
            });

        }
    };
} ]);
