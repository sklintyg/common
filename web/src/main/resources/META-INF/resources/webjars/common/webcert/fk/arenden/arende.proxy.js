angular.module('common').factory('common.ArendeProxy', ['$http', '$log', 'common.ArendeLegacyProxy',
    function($http, $log, ArendeLegacyProxy) {
        'use strict';

        /*
         * Load questions and answers data for a certificate
         */
        function _getArenden(intygsId, intygsTyp, onSuccess, onError) {
            $log.debug('_getArenden: intygsId:' + intygsId + ' intygsTyp: ' + intygsTyp);
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.getArenden.apply(null, arguments);
            }

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
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.sendNewArende.apply(null, arguments);
            }

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
         * save new answer to a question
         */
        function _saveAnswer(ArendeSvar, intygsTyp, onSuccess, onError) {
            $log.debug('_saveAnswer: arendeId:' + ArendeSvar.fragaInternReferens + ' intygsTyp: ' + intygsTyp);
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.saveAnswer.apply(null, arguments);
            }

            var restPath = '/moduleapi/arende/' + intygsTyp + '/' + ArendeSvar.fragaInternReferens + '/besvara';
            $http.put(restPath, ArendeSvar.meddelande).success(function(data) {
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
        function _closeAsHandled(arendeId, intygsTyp, onSuccess, onError) {
            var restPath = '/moduleapi/arende/' + intygsTyp + '/' + arendeId + '/stang';
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.closeAsHandled.apply(null, arguments);
            }

            $http.put(restPath).success(function(data) {
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
        function _openAsUnhandled(arendeId, intygsTyp, onSuccess, onError) {
            $log.debug('_openAsUnhandled: arendeId:' + arendeId + ' intygsTyp: ' + intygsTyp);
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.openAsUnhandled.apply(null, arguments);
            }

            var restPath = '/moduleapi/arende/' + intygsTyp + '/' + arendeId + '/oppna';
            $http.put(restPath).success(function(data) {
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
        function _closeAllAsHandled(arenden, onSuccess, onError) {
            var restPath = '/moduleapi/arende/stang';
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.closeAllAsHandled.apply(null, arguments);
            }

            var fs = [];
            angular.forEach(arenden, function(arende, key) {
                this.push({ intygsTyp: arende.fraga.intygTyp, arendeId: arende.internReferens });
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
         * Toggle vidarebefordrad state of a arende entity with given id
         */
        function _setVidarebefordradState(arendeReferens, intygsTyp, isVidareBefordrad, callback) {
            $log.debug('_setVidareBefordradState');
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.setVidarebefordradState.apply(null, arguments);
            }

            var restPath = '/moduleapi/arende/' + intygsTyp + '/' + arendeReferens + '/vidarebefordrad';
            $http.put(restPath, isVidareBefordrad).success(function(data) {
                $log.debug('_setVidareBefordradState data:' + data);
                callback(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                callback(null);
            });
        }

        // Return public API for the service
        return {
            getArenden: _getArenden,
            sendNewArende: _sendNewArende,
            saveAnswer: _saveAnswer,
            closeAsHandled: _closeAsHandled,
            openAsUnhandled: _openAsUnhandled,
            closeAllAsHandled: _closeAllAsHandled,
            setVidarebefordradState: _setVidarebefordradState
        };
    }]);
