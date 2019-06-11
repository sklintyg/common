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
angular.module('common').factory('common.srsProxy', ['$http', '$q', '$log',
    function($http, $q, $log) {
        'use strict';

        function _createGarbageQuestionAnswer() {
            return [{questionId: 'garbagedata', answerId: 'garbagedata'}];
        }

        function _getSrs(intygsId, patientId, diagnosKod, qaIds, prediktion, atgard, statistik) {
            var url = '/api/srs/' + intygsId + '/' + patientId + '/' + diagnosKod + '?prediktion=' +
                prediktion + '&atgard=' + atgard + '&statistik=' + statistik;
            return $http.post(url, qaIds).then(function(response) {
                return response.data;
            }, function(err) {
                return 'error';
            });
        }

        function _getPredictionFromResponseData(data) {
            /*jshint maxcomplexity:12 */
            var prediction = {};
            if(data === 'error'){
                return data;
            }
            if(data.predictionDiagnosisDescription) {
                prediction.predictionDiagnosisDescription = data.predictionDiagnosisDescription;
            }
            if(data.predictionDiagnosisCode) {
                prediction.predictionDiagnosisCode = data.predictionDiagnosisCode;
            }
            if(data.predictionDescription) {
                prediction.description = data.predictionDescription;
            }
            if(data.predictionLevel) {
                prediction.level = data.predictionLevel;
            }
            if(data.predictionStatusCode) {
                prediction.statusCode = data.predictionStatusCode;
            }
            if (data.predictionProbabilityOverLimit) {
                prediction.probabilityOverLimit = data.predictionProbabilityOverLimit;
            }
            if (data.predictionPrevalence) {
                prediction.prevalence = data.predictionPrevalence;
            }
            if (data.predictionPhysiciansOwnOpinionRisk) {
                prediction.opinion = data.predictionPhysiciansOwnOpinionRisk;
            }
            if (data.predictionQuestionsResponses) {
                prediction.predictionQuestionsResponses = data.predictionQuestionsResponses;
            }
            if (data.predictionTimestamp) {
                prediction.predictionTimestamp = data.predictionTimestamp;
                prediction.predictionDate = moment(Date.parse(data.predictionTimestamp)).format('YYYY-MM-DD');
            }
            return prediction;
        }

        function _getAtgarderFromResponseData(data) {
            var atgarder = {};
            /* jshint ignore:start */
            if(data.atgarderDiagnosisDescription) {
                atgarder.atgarderDiagnosisDescription = data.atgarderDiagnosisDescription;
            }
            if(data.atgarderDiagnosisCode) {
                atgarder.atgarderDiagnosisCode = data.atgarderDiagnosisCode;
            }
            if (data.atgarderStatusCode) {
                atgarder.atgarderStatusCode = data.atgarderStatusCode;
            }
            if (data.atgarderRek) {
                atgarder.atgarderRek = {
                    diagnosisCode: data.atgarderDiagnosisCode,
                    diagnosisDescription: data.atgarderDiagnosisDescription,
                    atgarder: data.atgarderRek
                };
            }
            if (data.atgarderObs) {
                atgarder.atgarderObs = {
                    diagnosisCode: data.atgarderDiagnosisCode,
                    diagnosisDescription: data.atgarderDiagnosisDescription,
                    atgarder: data.atgarderObs
                };
            }
            if (data.atgarderFrl) {
                atgarder.atgarderExt = {
                    diagnosisCode: data.atgarderDiagnosisCode,
                    diagnosisDescription: data.atgarderDiagnosisDescription,
                    atgarder: data.atgarderFrl
                };
            }
            if (data.atgarderReh) {
                atgarder.atgarderReh = {
                    diagnosisCode: data.atgarderDiagnosisCode,
                    diagnosisDescription: data.atgarderDiagnosisDescription,
                    atgarder: data.atgarderReh
                };
            }
            if(angular.equals({}, atgarder)) {
                atgarder = null;
            }
            /* jshint ignore:end */
            return atgarder;
        }

        function _getStatistikFromResponseData(data) {
            var statistik = {};
            /* jshint ignore:start */
            if(data.statistikDiagnosisDescription) {
                statistik.statistikDiagnosisDescription = data.statistikDiagnosisDescription;
            }
            if(data.statistikDiagnosisCode) {
                statistik.statistikDiagnosisCode = data.statistikDiagnosisCode;
            }
            if (data.statistikStatusCode) {
                statistik.statistikStatusCode = data.statistikStatusCode;
            }
            if (data.statistikBild) {
                statistik.statistikBild = data.statistikBild;
            }
            if (data.statistikNationellStatistik) {
                statistik.nationellStatistik = data.statistikNationellStatistik;
            }
            if(angular.equals({}, statistik)) {
                statistik = null;
            }
            /* jshint ignore:end */
            return statistik;
        }

        function _getPrediction(intygsId, patientId, diagnosKod, qaIds) {
            return _getSrs(intygsId, patientId, diagnosKod, qaIds, true, false, false).then(function(data) {
                return _getPredictionFromResponseData(data);
            });
        }

        function _getHistoricPredictionForDiagnosis(intygsId, patientId, diagnosKod) {
            return _getSrs(intygsId, patientId, diagnosKod, _createGarbageQuestionAnswer(), false, false, false).then(
                function(data) {
                    return {
                        'prediktion': _getPredictionFromResponseData(data)
                    };
                });
        }

        function _getAtgarderAndStatistikAndHistoricPredictionForDiagnosis(intygsId, patientId, diagnosKod) {
            return _getSrs(intygsId, patientId, diagnosKod, _createGarbageQuestionAnswer(), false, true, true).then(
                function(data) {
                    return {
                        'atgarder': _getAtgarderFromResponseData(data),
                        'statistik': _getStatistikFromResponseData(data),
                        'prediktion': _getPredictionFromResponseData(data)
                    };
                });
        }

        function _getDiagnosisCodes() {
            return $http.get('/api/srs/codes').then(function(response) {
                return response.data;
            });
        }

        function _setConsent(patientId, careUnitHsaId, consentGiven) {
            return $http.put('/api/srs/consent/' + patientId + '/' + careUnitHsaId, consentGiven).then(function(response) {
                return response.data;
            });
        }

        function _getConsent(personId, careUnitHsaId) {
            return $http.get('/api/srs/consent/' + personId + '/' + careUnitHsaId).then(
                function(response) {
                    return response;
                }, function(err){
                    return err;
                });
        }

        function _getQuestions(diagnosKod) {
            return $http.get('/api/srs/questions/' + diagnosKod).then(function(response) {
                return response.data;
            });
        }

        function isDefined(input) {
            return input !== undefined && input !== '';
        }

        // SRS_LOADED,
        function _logSrsLoaded(userClientContext, intygsId, caregiverId, careUnitId, diagnosisCode) {
            if (isDefined(userClientContext) && isDefined(intygsId) && isDefined(caregiverId) &&
                isDefined(careUnitId) && isDefined(diagnosisCode)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_LOADED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId,
                        'mainDiagnosisCode': diagnosisCode
                    }
                });
            }
        }
        // SRS_PANEL_ACTIVATED,
        function _logSrsPanelActivated(userClientContext, intygsId, caregiverId, careUnitId) {
            if (isDefined(userClientContext) && isDefined(intygsId) && isDefined(caregiverId) && isDefined(careUnitId)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_PANEL_ACTIVATED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId
                    }
                });
            }
        }
        // SRS_CONSENT_ANSWERED,
        function _logSrsConsentAnswered(userClientContext, intygsId, caregiverId, careUnitId) {
            if (isDefined(userClientContext) && isDefined(intygsId) && isDefined(caregiverId) && isDefined(careUnitId)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_CONSENT_ANSWERED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId
                    }
                });
            }
        }
        // SRS_QUESTION_ANSWERED,
        function _logSrsQuestionAnswered(userClientContext, intygsId, caregiverId, careUnitId) {
            if (isDefined(userClientContext) && isDefined(intygsId) && isDefined(caregiverId) && isDefined(careUnitId)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_QUESTION_ANSWERED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId
                    }
                });
            }
        }
        // SRS_CALCULATE_CLICKED,
        function _logSrsCalculateClicked(userClientContext, intygsId, caregiverId, careUnitId) {
            if (isDefined(userClientContext) && isDefined(intygsId) && isDefined(caregiverId) && isDefined(careUnitId)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_CALCULATE_CLICKED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId
                    }
                });
            }
        }
        // SRS_HIDE_QUESTIONS_CLICKED,
        function _logSrsHideQuestionsClicked(userClientContext, intygsId, caregiverId, careUnitId) {
            if (isDefined(intygsId) && isDefined(caregiverId) && isDefined(careUnitId)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_HIDE_QUESTIONS_CLICKED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId
                    }
                });
            }
        }
        // SRS_SHOW_QUESTIONS_CLICKED,
        function _logSrsShowQuestionsClicked(userClientContext, intygsId, caregiverId, careUnitId) {
            if (isDefined(userClientContext) && isDefined(intygsId) && isDefined(caregiverId) && isDefined(careUnitId)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_SHOW_QUESTIONS_CLICKED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId
                    }
                });
            }
        }
        // SRS_MEASURES_SHOW_MORE_CLICKED,
        function _logSrsMeasuresShowMoreClicked(userClientContext, intygsId, caregiverId, careUnitId) {
            if (isDefined(userClientContext) && isDefined(intygsId) && isDefined(caregiverId) && isDefined(careUnitId)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_MEASURES_SHOW_MORE_CLICKED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId
                    }
                });
            }
        }
        // SRS_MEASURES_LINK_CLICKED,
        function _logSrsMeasuresLinkClicked(userClientContext, intygsId, caregiverId, careUnitId) {
            if (isDefined(userClientContext) && isDefined(intygsId) && isDefined(caregiverId) && isDefined(careUnitId)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_MEASURES_LINK_CLICKED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId
                    }
                });
            }
        }
        // SRS_STATISTICS_ACTIVATED,
        function _logSrsStatisticsActivated(userClientContext, intygsId, caregiverId, careUnitId) {
            if (isDefined(userClientContext) && isDefined(intygsId) && isDefined(caregiverId) && isDefined(careUnitId)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_STATISTICS_ACTIVATED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId
                    }
                });
            }
        }
        // SRS_STATISTICS_LINK_CLICKED
        function _logSrsStatisticsLinkClicked(userClientContext, intygsId, caregiverId, careUnitId) {
            if (isDefined(userClientContext) && isDefined(intygsId) && isDefined(caregiverId) && isDefined(careUnitId)) {
                $http.post('/api/jslog/srs', {
                    'event': 'SRS_STATISTICS_LINK_CLICKED',
                    'info': {
                        'userClientContext': userClientContext,
                        'intygId': intygsId,
                        'caregiverId': caregiverId,
                        'careUnitId': careUnitId
                    }
                });
            }
        }

        function _getSrsForDiagnoseOnly(diagnoseCode) {
            return $http.get('/api/srs/atgarder/' + diagnoseCode);
        }

        function _setOwnOpinion(opinion, personnummer, careGiverId, careUnitId, certificateId, diagnosisCode) {
            return $http.put('/api/srs/opinion/' + personnummer + '/' + careGiverId + '/' + careUnitId + '/' +
                certificateId + '/' + diagnosisCode, opinion).then(function(response) {
                return response.data;
            });
        }

        // Return public API for the service
        return {
            getConsent: _getConsent,
            getDiagnosisCodes: _getDiagnosisCodes,
            getQuestions: _getQuestions,
            setOwnOpinion: _setOwnOpinion,
            getPrediction: _getPrediction,
            getAtgarderAndStatistikAndHistoricPredictionForDiagnosis: _getAtgarderAndStatistikAndHistoricPredictionForDiagnosis,
            getHistoricPredictionForDiagnosis: _getHistoricPredictionForDiagnosis,
            setConsent: _setConsent,
            getSrsForDiagnoseOnly: _getSrsForDiagnoseOnly,
            logSrsLoaded: _logSrsLoaded,
            logSrsPanelActivated: _logSrsPanelActivated,
            logSrsConsentAnswered: _logSrsConsentAnswered,
            logSrsQuestionAnswered: _logSrsQuestionAnswered,
            logSrsCalculateClicked: _logSrsCalculateClicked,
            logSrsHideQuestionsClicked: _logSrsHideQuestionsClicked,
            logSrsShowQuestionsClicked: _logSrsShowQuestionsClicked,
            logSrsMeasuresShowMoreClicked: _logSrsMeasuresShowMoreClicked,
            logSrsMeasuresLinkClicked: _logSrsMeasuresLinkClicked,
            logSrsStatisticsActivated: _logSrsStatisticsActivated,
            logSrsStatisticsLinkClicked: _logSrsStatisticsLinkClicked
        };
    }]);

