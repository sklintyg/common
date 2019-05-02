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
        /*from FMB with tweaks--->*/ 'common.anchorScrollService', 'common.srsService', 'common.srsViewState', '$log','$timeout',
    function(ObjectHelper, srsProxy, authorityService, $stateParams, anchorScrollService, srsService, srsViewState, $log, $timeout) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsPanelTab.directive.html',
        link: function($scope, $element, $attrs) {
            $scope.srs = srsViewState;
            // $scope.id = $attrs.id;  // id is not provided by wcSupportPanelComponentRenderer
            // $scope.status = {
            //     open: false,
            //     riskInfoOpen: false
            // };

            // make sure component isn't visible if no id is supplied
            // if(!ObjectHelper.isDefined($scope.id)){
            //     $scope.srs.srsApplicable = false;
            // }
            // console.log('link $scope.srs.srsApplicable', $scope.srs.srsApplicable)

            $scope.questionsFilledForVisaButton = function() {
                console.log('$scope.questionsFilledForVisaButton()')
                var answers = getSelectedAnswerOptions();
                for (var i = 0; i < answers.length; i++) {
                    if (!answers[i] || !answers[i].answerId) {
                        return false;
                    }
                }
                return true;
            };

            $scope.visaClicked = function() {
                console.log('$scope.visaClicked()')
                $scope.retrieveAndSetPrediction().then(function() {
                    $scope.srs.showVisaKnapp = false;
                    setPredictionImage();
                    setPrediktionMessages();
                });
            };


            $scope.retrieveAndSetPrediction = function() {
                console.log('$scope.retrieveAndSetPrediction()')
                var qaIds = getSelectedAnswerOptions();
                return srsProxy.getPrediction($stateParams.certificateId, $scope.srs.personId, srsViewState.diagnosKod,
                    qaIds, true, true, true).then(function(prediction) {
                        console.log("new prediction", prediction)
                        $scope.srs.prediction = prediction;
                }, function(error) {
                    $scope.srs.prediction = 'error';
                });
            };

            $scope.retrieveAndSetAtgarderAndStatistikAndHistoricPrediction = function() {
                console.log('$scope.retrieveAndSetAtgarderAndStatistikAndHistoricPrediction()')
                return srsProxy.getAtgarderAndStatistikAndHistoricPredictionForDiagnosis($stateParams.certificateId, $scope.srs.personId,
                    srsViewState.diagnosKod)
                    .then(function(data) {
                        console.log('got atgarder and statistik', data);
                        console.log('atgarder', data.atgarder);
                        $scope.srs.statistik = data.statistik || 'error';
                        $scope.srs.atgarder = data.atgarder || 'error';
                        $scope.srs.prediction = data.prediktion || 'error';
                        if($scope.srs.atgarder !== 'error') {
                            $scope.srs.atgarder.atgarderObs.forEach(function(a){
                                a.recommendationText = '• ' + a.recommendationText
                            })
                            $scope.srs.atgarder.atgarderRek.forEach(function(a){
                                a.recommendationText = '• ' + a.recommendationText
                            })
                            //$scope.srs.atgarder.atgarderObs = stringifyAtgarderObs($scope.srs.atgarder.atgarderObs);
                        }

                        // Update the selected answers to the received stored answer
                        if ($scope.srs.prediction.predictionQuestionsResponses) {
                            console.log("questions", $scope.srs.questions)
                            $scope.srs.prediction.predictionQuestionsResponses.forEach(function(qnr) {
                                // find correct question and answer option (in the scope) for received qnr
                                console.log("finding question for received qnr", qnr)
                                var correspondingQuestion = $scope.srs.questions.find(function(q){return qnr.questionId===q.questionId})
                                // some prediction params like "Region" aren't reflected as questions in the gui so if we don't get a
                                // match, ignore that one
                                if (correspondingQuestion) {
                                    console.log("finding answer option for question", correspondingQuestion)
                                    var storedAnswer = correspondingQuestion.answerOptions.find(function (a) {
                                        return qnr.answerId === a.id
                                    })
                                    correspondingQuestion.model = storedAnswer;
                                }
                            })
                        }
                    }, function(error) {
                        $scope.srs.statistik = 'error';
                        $scope.srs.atgarder = 'error';
                        $scope.srs.prediction = 'error';
                    });
            };

            $scope.getQuestions = function(diagnosKod) {
                console.log('$scope.getQuestions()')
                return srsProxy.getQuestions(diagnosKod).then(function(questions) {
                    console.log('got questions', questions)
                    $scope.srs.selectedButtons = [];
                    var qas = questions;
                    console.log('printing models before')
                    qas.forEach(function(qa){console.log(qa.model)});
                    for (var i = 0; i < questions.length; i++) {
                        for (var e = 0; e < questions[i].answerOptions.length; e++) {
                            if (questions[i].answerOptions[e].defaultValue) {
                                qas[i].model = questions[i].answerOptions[e];
                            }
                        }
                    }
                    console.log('printing models after')
                    qas.forEach(function(qa){console.log(qa.model)});
                    return qas;
                });
            };

            $scope.setConsent = function(consent) {
                console.log('setConsent', consent)
                $scope.srs.consent = consent ? 'JA' : 'NEJ';
                console.log('$scope.srs.consent', $scope.srs.consent)
                srsProxy.setConsent($scope.srs.personId, $scope.srs.hsaId, consent)
            };

            $scope.setOpinion = function(opinion) {
                console.log("$scope.setOwnOpinion()", opinion, $stateParams.certificateId, $scope.srs.hsaId)
                srsProxy.setOwnOpinion(opinion, $scope.srs.vardgivareHsaId, $scope.srs.hsaId, $stateParams.certificateId).then(function(result) {
                    console.log("opinion set on server", result)
                    $scope.srs.prediction.opinion = opinion;
                }, function(error) {
                    console.log("Error setting opinion on server", error)
                    $scope.srs.prediction.opinionError = 'Fel när egen bedömning skulle sparas';
                })
            }

            $scope.logSrsButtonClicked = function() {
                console.log('$scope.logSrsButtonClicked()')
                // if ($scope.status.open && !$scope.srs.clickedFirstTime) {
                if (!$scope.srs.clickedFirstTime) {
                    $scope.srs.clickedFirstTime = true;
                    srsProxy.logSrsClicked();
                }
            };

            $scope.logAtgarderLasMerButtonClicked = function() {
                console.log('$scope.logAtgarderLasMerButtonClicked()')
                srsProxy.log();
            };

            $scope.logAtgarderRekClicked = function() {
                console.log('$scope.logAtgarderRekClicked()')
                srsProxy.logSrsAtgardClicked();
            };

            $scope.logStatistikClicked = function() {
                console.log('$scope.logStatistikClicked()')
                srsProxy.logSrsAtgardClicked();
            };

            $scope.setActiveTab = function(tabname) {
                console.log('$scope.setActiveTab()')
                $scope.srs.activeTab = tabname;
            };

            /**
             * Starts the flow when the user enters the certificate editor
             * or the editor is reloaded.
             */
            $scope.$on('intyg.loaded', function(event, content) {
                    console.log('EVENT: "intyg.loaded"', event, content)
                    if(!$scope.srs.diagnosisListFetching) {
                        loadDiagCodes();
                    }
                    $scope.srs.userHasSrsFeature = checkIfUserHasSrsFeature();
                    console.log('$scope.srs.userHasSrsFeature', $scope.srs.userHasSrsFeature)
                    // console.log('$scope.id', $scope.id)
                    // INTYG-4543: Only use srs endpoints if user has srs-feature enabled.
                    // if ($scope.srs.userHasSrsFeature && $scope.id === '2') {
                    if ($scope.srs.userHasSrsFeature) { // TODO: why scope id 2? was that used for identifying the "window" as the srs window?
                        $scope.srs.consentInfo = '';
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
                            if (newVal !== oldVal) {
                                applySrsForDiagnosCode();
                            }
                        });

                        console.log('setting watch on scope param srs.consent')
                        $scope.$watch('srs.consent', function(newVal, oldVal) {
                            console.log('$scope.srs.consent from, to', oldVal, newVal)
                            console.log('srsViewState.diagnosKod', srsViewState.diagnosKod)
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

            function applySrsForDiagnosCode() {
                console.log('applySrsForDiagnosisCode()')
                resetMessages();
                reset();
                $scope.srs.diagnosKod = srsViewState.originalDiagnosKod;
                $scope.srs.diagnosisListFetching.then(function() {
                    $scope.srs.srsApplicable = isSrsApplicable($scope.srs.diagnosKod);
                    // if ($scope.srs.srsApplicable && $scope.srs.consent === 'JA') { // TODO: Replace this with removing patient data if the consent is removed
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
                console.log('setConsentMessages()')
                $scope.srs.consentInfo = '';
                // Assumption: only codes of exactly length 3 will be supported by SRS predictions.
                if ($scope.srs.diagnosKod !== undefined && $scope.srs.diagnosKod.length > 3) {
                    $scope.srs.consentInfo = 'Det SRS-stöd som visas är för koden ' + $scope.srs.diagnosKod.substring(0, 3);
                }
                if ($scope.srs.consent === 'error') {
                    $scope.srs.consentError = 'Tekniskt fel. Det gick inte att hämta information om samtycket.';
                }
                console.log('$scope.srs.consent message', $scope.srs.consentInfo, $scope.srs.consentError)
            }

            function setAtgarderMessages() {
                console.log('setAtgarderMessages()', $scope.srs.atgarder)
                // console.log('$scope.status', $scope.status)
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
                    else if ($scope.srs.atgarder.atgarderStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                        $scope.srs.atgarderInfo = 'Det SRS-stöd som visas är för koden ' + $scope.srs.atgarder.atgarderDiagnosisCode +
                            ' - ' + $scope.srs.atgarder.atgarderDiagnosisDescription;
                    }
                }
            }

            function setStatistikMessages() {
                console.log('setStatistikMessages()')
                $scope.srs.statistikInfo = '';
                $scope.srs.statistikError = '';
                if ($scope.srs.statistik) {
                    if ($scope.srs.statistik === 'error') {
                        $scope.srs.statistikError =
                            'Tekniskt fel.\nDet gick inte att hämta information om statistik';
                    }
                    else if ($scope.srs.statistik.statistikStatusCode === 'STATISTIK_SAKNAS' ||
                        !$scope.srs.statistik.statistikBild) {
                        $scope.srs.statistikInfo = 'Observera! För ' + srsViewState.diagnosKod +
                            ' finns ingen SRS-information för detta fält.';
                    }
                    else if ($scope.srs.statistik.statistikStatusCode === 'DIAGNOSKOD_PA_HOGRE_NIVA') {
                        $scope.srs.statistikInfo = 'Det SRS-stöd som visas är för koden ' + $scope.srs.statistik.statistikDiagnosisCode;
                    }
                }
            }

            function setPrediktionMessages() {
                console.log('setPrediktionMessages()')
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

            function setPredictionImage() {
                console.log('setPredicitionImage()')
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
                console.log('getSelectedAnswerOptions()')
                var selectedOptions = [];
                for (var i = 0; i < $scope.srs.questions.length; i++) {
                    selectedOptions.push(
                        {questionId: $scope.srs.questions[i].questionId, answerId: $scope.srs.questions[i].model.id});
                }
                return selectedOptions;
            }

            function checkIfUserHasSrsFeature() {
                console.log('checkIfUserHasSrsFeature()')
                var options = {
                    feature: 'SRS',
                    intygstyp: $scope.srs.intygsTyp
                };
                return authorityService.isAuthorityActive(options);
            }

            function isSrsApplicable() {
                console.log('isSrsApplicable intygstyp:', $scope.srs.intygsTyp)
                if (($scope.srs.intygsTyp.toLowerCase().indexOf('fk7263') > -1 || $scope.srs.intygsTyp.toLowerCase().indexOf('lisjp') > -1) &&
                    isSrsApplicableForCode(srsViewState.diagnosKod)) {
                    return true;
                } else {
                    return false;
                }

                function isSrsApplicableForCode(diagnosKod) {
                    return diagnosKod !== undefined && $scope.srs.diagnosisCodes.indexOf(diagnosKod.substring(0, 3)) !== -1;
                }
            }

            function loadDiagCodes() {
                console.log('loadDiagCodes()')
                $scope.srs.diagnosisListFetching = srsProxy.getDiagnosisCodes().then(function(diagnosisCodes) {
                    console.log('got diagnosis codes', diagnosisCodes)
                    $scope.srs.diagnosisCodes = diagnosisCodes;
                });
            }

            function loadSrs() {
                console.log('loadSrs()')
                $scope.getQuestions($scope.srs.diagnosKod).then(function(questions) {
                    console.log("GOT THE QUESTIONS QAS", questions)
                    setConsentMessages();
                    $scope.srs.questions = questions;
                    $scope.srs.allQuestionsAnswered = $scope.questionsFilledForVisaButton();
                    $scope.srs.showVisaKnapp = $scope.srs.allQuestionsAnswered;
                    $scope.retrieveAndSetAtgarderAndStatistikAndHistoricPrediction().then(function() {
                        setAtgarderMessages();
                        setStatistikMessages();
                    });
                    setPrediktionMessages(); // No prediction data as of yet, only used to ensure initial correct state.
                });
            }

            function resetMessages(){
                console.log('resetMessages()')
                $scope.srs.consentInfo = '';
                $scope.srs.consentError = '';

                $scope.srs.atgarderInfo = '';
                $scope.srs.atgarderError = '';

                $scope.srs.statistikInfo = '';
                $scope.srs.statistikError = '';

                $scope.srs.prediktionInfo = '';
                $scope.srs.prediktionError = '';
            }

            function reset() {
                console.log('reset()')
                $scope.srs.questions = [];
                $scope.srs.statistik = {};
                $scope.srs.atgarder = {};
                $scope.srs.prediction = {};
                $scope.srs.prediction.description = '';

                //$scope.consent = false;
                $scope.srs.shownFirstTime = false;
                $scope.srs.clickedFirstTime = false;
                //$scope.srs.diagnosKod = ($scope.srsStates.diagnoses['0'] && $scope.srsStates.diagnoses['0'].diagnosKod) || '';
                $scope.srs.srsApplicable = false;
                $scope.srs.errorMessage = '';
                $scope.srs.allQuestionsAnswered = false;
                $scope.srs.showVisaKnapp = false;
                $scope.srs.srsButtonVisible = true; // SRS window should not start in fixed position immediately.
                $scope.srs.riskImage = '';


                $scope.srs.activeTab = 'atgarder';
            }

            function _updateState() {
                var oldActive = $scope.vm.activeDiagnose;
                var oldActiveValid = false;

                $scope.vm.activeDiagnose = null;
                $scope.vm.noDataMessage = null;

                var diagnosesEnteredCount = 0;
                var diagnosesWithDataCount = 0;

                // angular.forEach($scope.srs.diagnoses, function(d) {
                //     diagnosesEnteredCount++;
                //     if (srsService.checkDiagnos(d)) {
                //         diagnosesWithDataCount++;
                //         if (oldActive === d) {
                //             oldActiveValid = true;
                //         }
                //         // Default is to select first diagnose that has data data
                //         if (!$scope.vm.activeDiagnose) {
                //             $scope.vm.activeDiagnose = d;
                //         }
                //     }
                // });

                //But if the previously selected diagnose still exist and has data - reselect the old one.
                // if (oldActiveValid) {
                //     $scope.vm.activeDiagnose = oldActive;
                // }

                //Update data to display
                // _updateSectionDataForDiagnose($scope.vm.activeDiagnose);

                //Update message to display
                $scope.vm.noDataMessage = getNoDataReasonMessage(diagnosesEnteredCount, diagnosesWithDataCount, $scope.fmb.isIcdKodVerk);

                $log.debug($scope.vm.noDataMessage);
            }

            $scope.$watch('srs.consent', function(newVal,oldVal) {
                console.log('caught a change of consent from: ' + oldVal + ' to: ' + newVal);
            })

            $scope.$on('panel.activated', function(event, activatedPanelId) {
                if (activatedPanelId === 'wc-srs-panel-tab') {
                    console.log('Activated SRS panel')
                }
            })

            // $timeout(function(){
            //     console.log('BROADCASTING EVENT!')
            //     $scope.$broadcast('panel.activated', 'wc-srs-panel-tab');
            // });

            // $scope.$watch('vm.activeDiagnose.diagnosKod', function(newVal) {
            //     _updateState();
            //     if (newVal) {
            //         angular.element('#srs-panel-scrollable-body').scrollTop(0);
            //     }
            // });
            //
            // //Diagnoses were added/removed/changed
            // $scope.$watchCollection('srs.diagnoses', function() {
            //     _updateState();
            // });



        // --------- Below is copied from new FMB

            //  /**
            //  * Determine info message to show
            //  */
            // function getNoDataReasonMessage(diagnoseCount, hasDataCount, enabled) {
            //     //Case 1: No diagnose set at all
            //     if (enabled === false) {
            //         return {
            //             incorrectKodverk: 'fmb.incorrectKodverk'
            //         };
            //     } else if (diagnoseCount === 0) {
            //         return {
            //             warning: 'srs.warn.no-diagnose-set'
            //         };
            //         //Case 2: 1 diagnose set, but no data for it
            //     } else if (diagnoseCount === 1 && hasDataCount === 0) {
            //         return {
            //             info: 'fmb.info.single-diagnose-no-data'
            //         };
            //         //Case 3: 2+ diagnose set, but no data for any of them
            //     } else if (diagnoseCount > 1 && hasDataCount === 0) {
            //         return {
            //             info: 'fmb.info.multiple-diagnose-no-data'
            //         };
            //     } else {
            //         return null;
            //     }
            //
            // }
            //
            // function _updateSectionDataForDiagnose(activeDiagnose) {
            //     //Init sections
            //     $scope.vm.sections = [ {
            //         formId: 'ARBETSFORMAGA',
            //         heading: 'BESLUTSUNDERLAG_TEXTUELLT',
            //         data: null
            //     }, {
            //         formId: 'FUNKTIONSNEDSATTNING',
            //         heading: 'FUNKTIONSNEDSATTNING',
            //         data: null
            //     }, {
            //         formId: 'AKTIVITETSBEGRANSNING',
            //         heading: 'AKTIVITETSBEGRANSNING',
            //         data: null
            //     }, {
            //         formId: 'DIAGNOS',
            //         heading: 'GENERELL_INFO',
            //         data: null
            //     }, {
            //         formId: 'DIAGNOS',
            //         heading: 'SYMPTOM_PROGNOS_BEHANDLING',
            //         data: null
            //     } ];
            //
            //     angular.forEach($scope.vm.sections, function(section) {
            //         section.data = activeDiagnose ? activeDiagnose.getFormData(section.formId, section.heading) : null;
            //         var reference = activeDiagnose ? activeDiagnose.getReference() : null;
            //         if (reference) {
            //             $scope.vm.referensDescr = reference.desc;
            //             $scope.vm.referensLink = reference.link;
            //         }
            //     });
            // }
            //
            // function _updateState() {
            //     var oldActive = $scope.vm.activeDiagnose;
            //     var oldActiveValid = false;
            //
            //     $scope.vm.activeDiagnose = null;
            //     $scope.vm.noDataMessage = null;
            //
            //     var diagnosesEnteredCount = 0;
            //     var diagnosesWithDataCount = 0;
            //
            //     angular.forEach($scope.fmb.diagnoses, function(d) {
            //         diagnosesEnteredCount++;
            //         if (fmbService.checkDiagnos(d)) {
            //             diagnosesWithDataCount++;
            //             if (oldActive === d) {
            //                 oldActiveValid = true;
            //             }
            //             // Default is to select first diagnose that has data data
            //             if (!$scope.vm.activeDiagnose) {
            //                 $scope.vm.activeDiagnose = d;
            //             }
            //         }
            //     });
            //
            //     //But if the previously selected diagnose still exist and has data - reselect the old one.
            //     if (oldActiveValid) {
            //         $scope.vm.activeDiagnose = oldActive;
            //     }
            //
            //     //Update data to display
            //     _updateSectionDataForDiagnose($scope.vm.activeDiagnose);
            //
            //     //Update message to display
            //     $scope.vm.noDataMessage = getNoDataReasonMessage(diagnosesEnteredCount, diagnosesWithDataCount, $scope.fmb.isIcdKodVerk);
            //
            //     $log.debug($scope.vm.noDataMessage);
            // }
            //
            // $scope.$watch('srs.isIcdKodVerk', function(newVal, oldVal) {
            //     $scope.srs.isIcdKodVerk = newVal;
            //     $log.debug($scope.fmb.isIcdKodVerk);
            //     _updateState();
            // }, true);
            //
            // //Diagnoses were added/removed/changed
            // $scope.$watchCollection('srs.diagnoses', function() {
            //     _updateState();
            // });
            // $scope.$watch('vm.activeDiagnose.diagnosKod', function(newVal) {
            //     _updateState();
            //     if (newVal) {
            //         angular.element('#srs-panel-scrollable-body').scrollTop(0);
            //     }
            // });
            //
            // $scope.getDiagnoses = function() {
            //     var diagnoser = [];
            //     var unikaDiagnoser = [];
            //     for (var d in srsViewState.diagnoses) {
            //         if (angular.isDefined(d) &&
            //             unikaDiagnoser.indexOf(srsViewState.diagnoses[d].diagnosKod) === -1) {
            //             unikaDiagnoser.push(srsViewState.diagnoses[d].diagnosKod);
            //             diagnoser.push(srsViewState.diagnoses[d]);
            //         }
            //     }
            //
            //     return diagnoser;
            // };
        }
    };
} ]);
