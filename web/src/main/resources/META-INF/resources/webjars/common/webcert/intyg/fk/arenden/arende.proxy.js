angular.module('common').factory('common.ArendeProxy',
    function($http, $log) {
        'use strict';

        /*
         * Load questions and answers data for a certificate
         */
        function _getArenden(intygsId, intygsTyp, onSuccess, onError) {
            $log.debug('_getArenden: intygsId:' + intygsId + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/arende/' + intygsId;
            $http.get(restPath).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * save new question
         */
        function _sendNewArende(intygsId, intygsTyp, arende, onSuccess, onError) {
            $log.debug('_saveNewQuestion: intygsId:' + intygsId + ' intygsTyp: ' + intygsTyp);
            var payload = {
                amne: arende.chosenTopic.value,
                meddelande: arende.frageText
            };

            var restPath = '/moduleapi/arende/' + intygsTyp + '/' + intygsId;
            $http.post(restPath, payload).success(function(data) {
                $log.debug('got callback data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * answer komplettering with a new intyg (basically do a copy with a 'komplettering' relation to this intyg)
         */
        function _answerWithIntyg(arende, intygsTyp, intygCopyRequest, onSuccess, onError) {
            $log.debug('_answerWithIntyg: arendeId:' + arende.fraga.internReferens + ' intygsTyp: ' + intygsTyp);

            var restPath = '/api/intyg/' + intygsTyp + '/' + intygCopyRequest.intygId + '/' + arende.fraga.internReferens + '/komplettera';
            var payload = {};
            payload.patientPersonnummer = intygCopyRequest.patientPersonnummer;
            if (intygCopyRequest.nyttPatientPersonnummer) {
                payload.nyttPatientPersonnummer = intygCopyRequest.nyttPatientPersonnummer;
            }

            $http.post(restPath, payload).success(function(data) {
                $log.debug('got data:' + data.intygsUtkastId);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }
        
        /*
         * save new answer to a question
         */
/*        function _saveAnswer(arende, intygsTyp, onSuccess, onError) {
            $log.debug('_saveAnswer: arendeId:' + arende.internReferens + ' intygsTyp: ' + intygsTyp);

            var restPath = '/moduleapi/arenden/' + intygsTyp + '/' + arende.internReferens + '/besvara';
            $http.put(restPath, arende.svarsText).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * update the handled status to handled ('Closed') of a QuestionAnswer
         */
/*        function _closeAsHandled(arendeId, intygsTyp, onSuccess, onError) {
            var restPath = '/moduleapi/arenden/' + intygsTyp + '/' + arendeId + '/stang';
            $http.get(restPath).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * update the handled status to handled ('Closed') of a QuestionAnswer
         */
  /*      function _closeAllAsHandled(qas, onSuccess, onError) {
            var restPath = '/moduleapi/arenden/stang';
            var fs = [];
            angular.forEach(qas, function(qa, key) {
                this.push({ intygsTyp : qa.intygsReferens.intygsTyp, arendeId:qa.internReferens });
            }, fs);

            $http.put(restPath, fs).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * update the handled status to unhandled ('ANSWERED or PENDING_EXTERNAL_ACTION depending if the question has an
         * answer set or not') of a QuestionAnswer
         */
    /*    function _openAsUnhandled(arendeId, intygsTyp, onSuccess, onError) {
            $log.debug('_openAsUnhandled: arendeId:' + arendeId + ' intygsTyp: ' + intygsTyp);

            var restPath = '/moduleapi/arenden/' + intygsTyp + '/' + arendeId + '/oppna';
            $http.get(restPath).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }*/

        // Return public API for the service
        return {
            getArenden: _getArenden,
            answerWithIntyg: _answerWithIntyg,
            sendNewArende: _sendNewArende
            /*            saveAnswer: _saveAnswer,
            closeAsHandled: _closeAsHandled,
            closeAllAsHandled: _closeAllAsHandled,
            openAsUnhandled: _openAsUnhandled*/
        };
    });
