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
angular.module('common').factory('common.srsProxy', ['common.ObjectHelper', '$http', '$q', '$log',
    function(ObjectHelper, $http, $q, $log) {
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
            if(data === 'error'){
                return data;
            }
            return {
                predictionDiagnosisDescription: data.predictionDiagnosisDescription,
                predictionDiagnosisCode: data.predictionDiagnosisCode,
                description: data.predictionDescription,
                level: data.predictionLevel,
                statusCode: data.predictionStatusCode,
                probabilityOverLimit: data.predictionProbabilityOverLimit,
                prevalence: data.predictionPrevalence,
                opinion: data.predictionPhysiciansOwnOpinionRisk,
                predictionQuestionsResponses: data.predictionQuestionsResponses,
                predictionTimestamp: data.predictionTimestamp,
                predictionDate: data.predictionTimestamp?moment(Date.parse(data.predictionTimestamp)).format('YYYY-MM-DD'):undefined
            };
        }

        function _getAtgarderFromResponseData(data) {
            var atgarder = {};
            /* jshint ignore:start */
            atgarder.atgarderDiagnosisDescription = data.atgarderDiagnosisDescription;
            atgarder.atgarderDiagnosisCode = data.atgarderDiagnosisCode;
            atgarder.atgarderStatusCode = data.atgarderStatusCode;
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
            var statistik = {
                statistikDiagnosisDescription: data.statistikDiagnosisDescription,
                statistikDiagnosisCode: data.statistikDiagnosisCode,
                statistikStatusCode: data.statistikStatusCode,
                statistikBild: data.statistikBild,
                nationellStatistik: data.statistikNationellStatistik
            };
            if(angular.equals({}, statistik)) {
                statistik = null;
            }
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
            return $http.get('/api/srs/codes').then(
                function(response) {
                    return response;
                }, function(err) {
                    return err;
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
            return $http.get('/api/srs/questions/' + diagnosKod).then(
                function(response) {
                    return response;
                }, function(err){
                    return err;
                });
        }

        /**
         * Monitor logging for SRS
         * @param eventType 'SRS_LOADED', 'SRS_PANEL_ACTIVATED', 'SRS_CONSENT_ANSWERED', 'SRS_QUESTION_ANSWERED'
         *  'SRS_CALCULATE_CLICKED', 'SRS_HIDE_QUESTIONS_CLICKED', 'SRS_SHOW_QUESTIONS_CLICKED',
         *  'SRS_MEASURES_SHOW_MORE_CLICKED', 'SRS_MEASURES_LINK_CLICKED', 'SRS_STATISTICS_ACTIVATED',
         *  'SRS_STATISTICS_LINK_CLICKED'
         * @param userClientContext The client context UTK=utkast, REH
         * @param intygsId
         * @param caregiverId
         * @param careUnitId
         * @param diagnosisCode only used on 'SRS_LOADED'
         * @private
         */
        function _logSrsMonitor(eventType, userClientContext, intygsId, caregiverId, careUnitId, diagnosisCode) {
            if (ObjectHelper.isDefined(eventType) && ObjectHelper.isDefined(userClientContext) &&
                ObjectHelper.isDefined(intygsId) && ObjectHelper.isDefined(caregiverId) &&
                ObjectHelper.isDefined(careUnitId) &&
                (eventType!=='SRS_LOADED' || eventType==='SRS_LOADED' && ObjectHelper.isDefined(diagnosisCode))) {
                $http.post('/api/jslog/srs', {
                    'event': eventType,
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
            logSrsLoaded: function(userClientContext, intygsId, caregiverId, careUnitId, diagnosisCode) {
                _logSrsMonitor('SRS_LOADED', userClientContext, intygsId, caregiverId, careUnitId, diagnosisCode);},
            logSrsPanelActivated: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_PANEL_ACTIVATED', userClientContext, intygsId, caregiverId, careUnitId);},
            logSrsConsentAnswered: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_CONSENT_ANSWERED', userClientContext, intygsId, caregiverId, careUnitId);},
            logSrsQuestionAnswered: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_QUESTION_ANSWERED', userClientContext, intygsId, caregiverId, careUnitId);},
            logSrsCalculateClicked: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_CALCULATE_CLICKED', userClientContext, intygsId, caregiverId, careUnitId);},
            logSrsHideQuestionsClicked: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_HIDE_QUESTIONS_CLICKED', userClientContext, intygsId, caregiverId, careUnitId);},
            logSrsShowQuestionsClicked: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_SHOW_QUESTIONS_CLICKED', userClientContext, intygsId, caregiverId, careUnitId);},
            logSrsMeasuresShowMoreClicked: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_MEASURES_SHOW_MORE_CLICKED', userClientContext, intygsId, caregiverId, careUnitId);},
            logSrsMeasuresExpandOneClicked: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_MEASURES_EXPAND_ONE_CLICKED', userClientContext, intygsId, caregiverId, careUnitId);},
            logSrsMeasuresLinkClicked: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_MEASURES_LINK_CLICKED', userClientContext, intygsId, caregiverId, careUnitId);},
            logSrsStatisticsActivated: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_STATISTICS_ACTIVATED', userClientContext, intygsId, caregiverId, careUnitId);},
            logSrsStatisticsLinkClicked: function(userClientContext, intygsId, caregiverId, careUnitId) {
                _logSrsMonitor('SRS_STATISTICS_LINK_CLICKED', userClientContext, intygsId, caregiverId, careUnitId);},

            __test__: {
                getPredictionFromResponseData: _getPredictionFromResponseData,
                getStatistikFromResponseData: _getStatistikFromResponseData,
                logSrsMonitoring: _logSrsMonitor
            }
        };
    }]);

