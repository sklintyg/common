/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
        'common.srsService', 'common.srsViewState', '$timeout', '$window',
    function(ObjectHelper, srsProxy, authorityService, $stateParams,
             srsService, srsViewState, $timeout, $window) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsPanelTab.directive.html',
        link: function($scope, $element, $attrs) {

            $scope.srs = srsViewState;
            $scope.questionsFilledForVisaButton = function() {
                var answers = $scope.getSelectedAnswerOptions();
                for (var i = 0; i < answers.length; i++) {
                    if (!answers[i] || !answers[i].answerId) {
                        return false;
                    }
                }
                return true;
            };

            function getSelectedViewFromPredictions(predictions, isReadOnly) {
                var latestDaysInto = null;
                predictions.forEach(function(p, i) {
                    // if we found the first daysIntoSickLeave or a new highest value
                    if ((isReadOnly || (!isReadOnly && i !== 0)) && p.daysIntoSickLeave &&
                        (!latestDaysInto || latestDaysInto < p.daysIntoSickLeave)) {
                        latestDaysInto = p.daysIntoSickLeave;
                    }
                });
                if (latestDaysInto !== null) {
                    switch (latestDaysInto) {
                        case 15: return isReadOnly ? 'NEW' : 'EXT';
                        case 45: return isReadOnly ? 'EXT' : 'LATE_EXT';
                        case 75: return 'LATE_EXT';
                        default: return null;
                    }
                }
                return null;
            }

            $scope.setPredictionsOnScope = function(predictions) {
                $scope.srs.predictions = predictions || 'error';
                if ($scope.srs.predictions !== 'error') {
                    $scope.srs.currentPrediction = $scope.srs.predictions[0];
                    $scope.srs.previousPrediction = null;
                    $scope.srs.predictions.forEach(function(p) {
                        if ($scope.srs.previousPrediction === null && p !== $scope.srs.currentPrediction && p.probabilityOverLimit) {
                            $scope.srs.previousPrediction = p;
                        }
                    });
                    if (!$scope.srs.currentPrediction.probabilityOverLimit &&
                        $scope.srs.previousPrediction && $scope.srs.previousPrediction.opinion) {
                        $scope.srs.ownOpinion = $scope.srs.previousPrediction.opinion;
                    } else if ($scope.srs.currentPrediction && $scope.srs.currentPrediction.opinion) {
                        $scope.srs.ownOpinion = $scope.srs.currentPrediction.opinion;
                    } else {
                        $scope.srs.ownOpinion = null;
                    }
                }
            }

            $scope.retrieveAndSetAtgarderAndStatistikAndHistoricPrediction = function() {
                // var predictionIntygId = $scope.srs.userClientContext === 'SRS_FRL' ? $scope.srs.extensionFromIntygId : $scope.srs.intygId;
                var predictionIntygId = $scope.srs.intygId;
                return srsProxy.getAtgarderAndStatistikAndHistoricPredictionsForDiagnosis(predictionIntygId, $scope.srs.personId,
                    srsViewState.diagnosKod)
                    .then(function(data) {
                        $scope.srs.statistik = data.statistik || 'error';
                        $scope.srs.atgarder = data.atgarder || 'error';
                        $scope.srs.extensionChain = data.forlangningskedja || 'error';
                        $scope.setPredictionsOnScope(data.prediktioner);
                    }, function(error) {
                        $scope.srs.statistik = 'error';
                        $scope.srs.atgarder = 'error';
                        $scope.srs.predictions = 'error';
                    });
            };

            $scope.getQuestions = function(diagnosKod) {
                return srsProxy.getQuestions(diagnosKod).then(function(response) {
                    $scope.srs.selectedButtons = [];
                    if (response.status === 200) {
                        var questions = response.data;
                        var qas = questions;
                        for (var i = 0; i < questions.length; i++) {
                            for (var e = 0; e < questions[i].answerOptions.length; e++) {
                                if (questions[i].answerOptions[e].defaultValue) {
                                    qas[i].model = questions[i].answerOptions[e];
                                }
                            }
                        }
                        return qas;
                    } else {
                        $scope.srs.backendError = 'Tekniskt fel: ' +
                            'Stödet Risk och råd är inte tillgängligt eftersom tjänsten inte svarar just nu';
                        reset();
                    }
                });
            };

            $scope.setSelectedView = function(view) {
                $scope.srs.selectedView = view;
            };

            $scope.setOpinion = function(opinion) {
                // it is only possible to set own opinion while in draft mode, thus predictions[0]
                srsProxy.setOwnOpinion(opinion, $scope.srs.personId, $scope.srs.vardgivareHsaId, $scope.srs.hsaId,
                    $scope.srs.intygId, $scope.srs.predictions[0].diagnosisCode)
                .then(function(result) {
                        // Special procedure to trigger update (of the tooltip) in the risk diagram change listener
                        // I.e. this would not trigger the change listener: $scope.srs.predictions[0].opinion = opinion;
                        var copyOfCurrentPrediction = {};
                        Object.assign(copyOfCurrentPrediction, $scope.srs.predictions[0]);
                        copyOfCurrentPrediction.opinion = opinion;
                        $scope.srs.predictions[0] = copyOfCurrentPrediction;
                    },
                    function(error) {
                        $scope.srs.opinionError = 'Fel när egen bedömning skulle sparas';
                    });
            };

            $scope.logSrsPanelActivated = function() {
                if (!$scope.srs.activatedFirstTime) {
                    $scope.srs.activatedFirstTime = true;
                    srsProxy.logSrsPanelActivated($scope.srs.userClientContext, $scope.srs.intygId,
                        $scope.srs.vardgivareHsaId, $scope.srs.hsaId);
                }
            };

            $scope.logSrsMeasuresDisplayed = function() {
                if (!$scope.srs.measuresDisplayedFirstTime) {
                    $scope.srs.measuresDisplayedFirstTime = true;
                    srsProxy.logSrsMeasuresDisplayed($scope.srs.userClientContext, $scope.srs.intygId,
                        $scope.srs.vardgivareHsaId, $scope.srs.hsaId);
                }
            };

            /**
             * Starts the flow when the user enters the certificate editor
             * or the editor is reloaded.
             */
            $scope.$on('intyg.loaded', function(event, content) {
                reset();
                if (content.grundData.relation.relationKod === 'FRLANG') {
                    $scope.srs.isForlangning = true;
                    // $scope.srs.extensionFromIntygId = content.grundData.relation.relationIntygsId;
                    $scope.srs.userClientContext = 'SRS_FRL';
                    $scope.srs.extensionPredictionFetched = false;
                } else {
                    $scope.srs.isForlangning = false;
                    $scope.srs.userClientContext = 'SRS_UTK';
                }
                $scope.srs.intygId = $stateParams.certificateId;

                if(!$scope.srs.diagnosisListFetching) {
                    loadDiagCodes();
                }
                $scope.srs.userHasSrsFeature = checkIfUserHasSrsFeature();
                // INTYG-4543: Only use srs endpoints if user has srs-feature enabled.
                if ($scope.srs.userHasSrsFeature) {
                    $scope.$watch('srs.originalDiagnosKod', function(newVal, oldVal) {
                        if (newVal === null || newVal === '') {
                            reset();
                            resetMessages();
                        } else if (newVal !== oldVal) {
                            applySrsForDiagnosisCode();
                        }
                    });
                    $scope.$watch('srs.selectedView', function(newVal, oldVal) {
                        $scope.srs.showVisaKnapp = true;
                        $scope.srs.daysIntoSickLeave = getDaysIntoSickLeaveApproximationFromSelectedView(newVal);
                    });
                }
                applySrsForDiagnosisCode();
            });

            function getDaysIntoSickLeaveApproximationFromSelectedView(selectedView) {
                switch (selectedView) {
                    case 'NEW':
                        return 15; // No need for LISJP the first 14 days
                    case 'EXT':
                        return 45; // Middle of 30..60
                    case 'LATE_EXT':
                        return 75; // Not used at the moment
                    default:
                        break;
                }
                // We shouldn't end up here.
                // If we can't figure out the selected view, return 15 as default;
                return 15;
            }

            function getSelectedViewFromExtensionChain(extensionChain) {
                if (extensionChain && extensionChain.length > 0) {
                    switch (extensionChain.length) {
                        case 1:
                            return 'NEW';
                        case 2:
                            return 'EXT';
                        case 3:
                            return 'LATE_EXT';
                        default:
                            break;
                    }
                }
                // We shouldn't end up here.
                // If we can't figure out which view to show, use NEW
                return 'NEW';
            }

            /* jshint ignore:start */
            function debugLog() {
                var debugLogEnabled = false;
                if (debugLogEnabled) {
                    console.log.apply(null, arguments);
                }
            }
            /* jshint ignore:end */

            function isScrolledIntoView(element, fullyInView) {
                if (!$scope.srs.isLoaded) {
                    return false;
                }
                var pageTop = $window.document.documentElement.scrollTop;
                var pageBottom = pageTop + $window.innerHeight;
                var elementTop = element.getBoundingClientRect().top;
                var elementBottom = elementTop + element.getBoundingClientRect().height;
                if (fullyInView === true) {
                    return ((pageTop < elementTop) && (pageBottom > elementBottom));
                } else {
                    return ((elementTop <= pageBottom) && (elementBottom >= pageTop));
                }
            }

            function initViewportMonitoringEvents() {
                // First check if measures are already displayed in the viewport directly after loading
                // NB! This must be done after the thing above on SRS panel has been initialized to get an accurate snapshot of the
                // viewport
                if (isScrolledIntoView(angular.element('.recommendation-list')[0], true)) {
                    $scope.logSrsMeasuresDisplayed();
                }
                // Add listener on scroll events
                var $viewport = angular.element('#srs-panel-scrollable-body');
                $viewport.off('scroll'); // remove any old event handlers
                $viewport.on('scroll', function (e) {
                    $scope.logSrsPanelActivated();
                    if (isScrolledIntoView(angular.element('.recommendation-list')[0], true)) {
                        $scope.logSrsMeasuresDisplayed();
                    }
                });
            }

            function applySrsForDiagnosisCode() {
                resetMessages();
                reset();
                $scope.srs.diagnosKod = srsViewState.originalDiagnosKod;
                if ($scope.srs.diagnosisCodes === 'error') {
                    loadDiagCodes();
                }
                $scope.srs.diagnosisListFetching.then(function() {
                    $scope.srs.srsApplicable = isSrsApplicable($scope.srs.diagnosKod);
                    if ($scope.srs.srsApplicable) {
                        if (!$scope.srs.shownFirstTime) {
                            srsProxy.logSrsLoaded($scope.srs.userClientContext, $scope.srs.intygId,
                                $scope.srs.vardgivareHsaId, $scope.srs.hsaId, $scope.srs.diagnosKod);
                        }
                        $scope.srs.shownFirstTime = true; // TODO: can we make this survive a page reload?
                        loadSrs();
                    }
                });
            }

            function setAtgarderMessages() {
                $scope.srs.atgarderInfo = '';
                $scope.srs.atgarderError = '';
                if ($scope.srs.atgarder) {
                    if ($scope.srs.atgarder === 'error') {
                        $scope.srs.atgarderError = 'Tekniskt fel: Det gick inte att hämta Råd och åtgärder';
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
                }
            }

            function setStatistikMessages() {
                $scope.srs.statistikInfo = '';
                $scope.srs.statistikError = '';
                if ($scope.srs.statistik) {
                    if ($scope.srs.statistik === 'error') {
                        $scope.srs.statistikError =
                            'Tekniskt fel: Det gick inte att hämta Nationell statistik';
                    }
                    else if ($scope.srs.statistik.statistikStatusCode === 'STATISTIK_SAKNAS' ||
                        !$scope.srs.statistik.nationellStatistik) {
                        $scope.srs.statistikInfo = 'Observera! För ' + srsViewState.diagnosKod +
                            ' finns ingen SRS-information för detta fält.';
                    }
                }
            }

            $scope.getSelectedAnswerOptions = function() {
                var selectedOptions = [];
                for (var i = 0; i < $scope.srs.questions.length; i++) {
                    selectedOptions.push(
                        {questionId: $scope.srs.questions[i].questionId, answerId: $scope.srs.questions[i].model.id});
                }
                return selectedOptions;
            };

            function checkIfUserHasSrsFeature() {
                var options = {
                    feature: 'SRS',
                    intygstyp: $scope.srs.intygsTyp
                };
                return authorityService.isAuthorityActive(options);
            }

            function isSrsApplicable() {
                if (($scope.srs.intygsTyp.toLowerCase().indexOf('fk7263') > -1 || $scope.srs.intygsTyp.toLowerCase().indexOf('lisjp') > -1) &&
                    isSrsApplicableForCode(srsViewState.diagnosKod)) {
                    return true;
                } else {
                    return false;
                }

                function isSrsApplicableForCode(diagnosKod) {
                    if ($scope.srs.diagnosisCodes === 'error') {
                        $scope.srs.backendError = 'Tekniskt fel: ' +
                            'Stödet Risk och råd är inte tillgängligt eftersom tjänsten inte svarar just nu';
                        reset();
                        return false;
                    }
                    return diagnosKod !== undefined &&
                        diagnosKod !== null &&
                        $scope.srs.diagnosisCodes.indexOf(diagnosKod.substring(0, 3)) !== -1;
                }
            }

            function loadDiagCodes() {
                $scope.srs.diagnosisListFetching = srsProxy.getDiagnosisCodes().then(function(diagnosisCodes) {
                    if(diagnosisCodes.status === 200){
                        $scope.srs.diagnosisCodes = diagnosisCodes.data;
                    }
                    else{
                        $scope.srs.diagnosisCodes = 'error';
                        $scope.srs.backendError = 'Tekniskt fel: ' +
                            'Stödet Risk och råd är inte tillgängligt eftersom tjänsten inte svarar just nu';
                        reset();
                    }
                });
            }

            function loadSrs() {
                $scope.getQuestions($scope.srs.diagnosKod).then(function(questions) {
                    if (!$scope.srs.isForlangning || ($scope.srs.isForlangning && !$scope.srs.extensionPredictionFetched)) {
                        $scope.srs.questions = questions;
                        $scope.srs.allQuestionsAnswered = $scope.questionsFilledForVisaButton();
                        $scope.srs.showVisaKnapp = $scope.srs.allQuestionsAnswered;
                    }
                    $scope.retrieveAndSetAtgarderAndStatistikAndHistoricPrediction().then(function () {
                        if ($scope.srs.predictions !== 'error') {
                            // First try to set the selected view using the latest historic risk prediction, if any
                            var selectedViewFromPredictions = getSelectedViewFromPredictions($scope.srs.predictions, $scope.srs.isReadOnly);
                            if (selectedViewFromPredictions) {
                                $scope.setSelectedView(selectedViewFromPredictions);
                            }
                            // Update the selected answers to the received stored answer
                            if ($scope.srs.predictions[0].probabilityOverLimit && $scope.srs.predictions[0].modelVersion === '2.1' ||
                                (!$scope.srs.predictions[0].probabilityOverLimit && $scope.srs.predictions[1] &&
                                    $scope.srs.predictions[1].probabilityOverLimit && $scope.srs.predictions[1].modelVersion === '2.1')) {
                                $scope.srs.differingModelVersionInfo = 'Tidigare risk beräknades med annan version av prediktionsmodellen.\n ' +
                                    'Svaren nedan är inte därför inte patientens tidigare svar utan en grundinställning för respektive fråga.';
                            } else if ($scope.srs.predictions[0].questionsResponses || ($scope.srs.predictions[1] &&
                                $scope.srs.predictions[1].questionsResponses)) {
                                var qResponses = $scope.srs.predictions[0].questionsResponses ? $scope.srs.predictions[0].questionsResponses
                                    : $scope.srs.predictions[1].questionsResponses;
                                qResponses.forEach(function(qnr) {
                                    // Find correct question and answer option (in the scope) for received qnr
                                    var correspondingQuestion = $scope.srs.questions.filter(function(q) {
                                        return qnr.questionId === q.questionId;
                                    })[0];
                                    // Some prediction params like "Region" aren't reflected as questions in the gui so if we don't get a
                                    // match, ignore that one
                                    if (correspondingQuestion) {
                                        var storedAnswer = correspondingQuestion.answerOptions.filter(function(a) {
                                            return qnr.answerId === a.id;
                                        })[0];
                                        correspondingQuestion.model = storedAnswer;
                                    }
                                });
                            }
                        }
                        // If we don't have a value for selected view yet, make it default to the correct spot based on the extension chain
                        if (!$scope.srs.selectedView) {
                            var defaultSelectedView = getSelectedViewFromExtensionChain($scope.srs.extensionChain);
                            $scope.setSelectedView(defaultSelectedView);
                        }
                        setAtgarderMessages();
                        setStatistikMessages();
                        $scope.setPrediktionMessages();
                        $scope.setPredictionRiskLevel();
                        $scope.srs.isLoaded = true;
                    });
                    $scope.setPrediktionMessages(); // No prediction data as of yet, only used to ensure initial correct state.

                    // At the next "cycle" after the questions have been loaded and rendered
                    // init the monitoring events for the viewport (and check if measures are already shown)
                    $timeout(function(){
                        initViewportMonitoringEvents();
                    }, 0);
                });
            }

            $scope.setPrediktionMessages = function() {
                $scope.srs.prediktionInfo = '';
                $scope.srs.prediktionError = '';
                if ($scope.srs.predictions === 'error') {
                    $scope.srs.prediktionError =
                        'Tekniskt fel: Något gick fel när risk för sjukskrivning längre än 90 dagar skulle hämtas.';
                } else {
                    $scope.srs.predictions.forEach(function(prediction) {
                        if (prediction === 'error' || prediction.statusCode === 'NOT_OK') {
                            $scope.srs.prediktionError =
                                'Tekniskt fel: Något gick fel när risk för sjukskrivning längre än 90 dagar skulle hämtas.';
                        }
                    });
                }
            };

            $scope.setPredictionRiskLevel = function() {
                $scope.srs.predictions.forEach(function(prediction) {
                    if (prediction.probabilityOverLimit) {
                        var probability = prediction.probabilityOverLimit;
                        var riskLevel = null;
                        if (probability < 0.39) {
                            riskLevel = 'Måttlig risk';
                        } else if (probability >= 0.39 && probability <= 0.62) {
                            riskLevel = 'Hög risk';
                        } else if (probability > 0.62) {
                            riskLevel = 'Mycket hög risk';
                        }
                        prediction.riskLevel = riskLevel;
                    }
                });
            };

            function resetMessages() {
                $scope.srs.backendError = '';

                $scope.srs.atgarderInfo = '';
                $scope.srs.atgarderError = '';

                $scope.srs.statistikInfo = '';
                $scope.srs.statistikError = '';

                $scope.srs.prediktionInfo = '';
                $scope.srs.prediktionError = '';

                $scope.srs.differingModelVersionInfo = '';
            }

            function reset() {
                // originalDiagnosKod is normally updated by the Webcert framework when updating the main diagnosis
                // thus we don't alter that here.
                // In the read only mode (in rehabstöd) originalDiagnosKod is updated in panel.activated below
                // $scope.isLoaded = false;
                $scope.srs.currentPrediction = null;
                $scope.srs.previousPrediction = null;
                $scope.srs.ownOpinion = null;
                $scope.srs.opinionError = null;

                $scope.srs.selectedView = null;
                $scope.srs.questions = [];
                $scope.srs.statistik = {};
                $scope.srs.atgarder = {};
                $scope.srs.predictions = [];
                $scope.srs.shownFirstTime = false;
                $scope.srs.activatedFirstTime = false;
                $scope.srs.measuresDisplayedFirstTime = false;
                $scope.srs.srsApplicable = false;
                $scope.srs.errorMessage = '';
                $scope.srs.allQuestionsAnswered = false;
                $scope.srs.showVisaKnapp = false;
                $scope.srs.riskImage = '';
                $scope.srs.activeTab = 'atgarder';
            }

            $scope.$on('panel.activated', function(event, activatedPanelId) {
                if (activatedPanelId === 'wc-srs-panel-tab') {
                    if ($scope.srs.srsApplicable) {
                        $scope.logSrsPanelActivated();
                    }
                    if ($scope.config.isReadOnly) {
                        $scope.srs.isReadOnly = true;
                        $scope.srs.userClientContext = 'SRS_REH'; // Rehabstöd
                        $scope.srs.isForlangning = false;
                        $scope.srs.intygId = $scope.config.intygContext.id;

                        srsService.updatePersonnummer($scope.config.readOnlyIntyg.grundData.patient.personId);
                        srsService.updateHsaId($scope.config.readOnlyIntyg.grundData.skapadAv.vardenhet.enhetsid);
                        srsService.updateVardgivareHsaId($scope.config.readOnlyIntyg.grundData.skapadAv.vardenhet.vardgivare.vardgivarid);
                        srsService.updateIntygsTyp($scope.config.readOnlyIntyg.typ);
                        srsService.updateDiagnosKod($scope.config.diagnoseCode);

                        if(!$scope.srs.diagnosisListFetching) {
                            loadDiagCodes();
                        }
                        $scope.srs.userHasSrsFeature = checkIfUserHasSrsFeature();
                        if ($scope.srs.userHasSrsFeature) {
                            applySrsForDiagnosisCode();
                        }
                    }

                }
            });

        }
    };
} ]);
