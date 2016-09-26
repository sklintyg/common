angular.module('common').factory('common.ArendeLegacyProxy', ['$http', '$log', 'common.ArendeLegacyService',
    function($http, $log, ArendeLegacyService) {
        'use strict';

        /*
         * Load questions and answers data for a certificate
         */
        function _getArenden(intygsId, intygsTyp, onSuccess, onError) {

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + intygsId;
            $http.get(restPath).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(ArendeLegacyService.convertFragasvarListToArendeList(data));
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * save new question
         */
        function _sendNewArende(intygsId, intygsTyp, question, onSuccess, onError) {
            var payload = {};
            payload.amne = ArendeLegacyService.convertAmneArendeToFragasvar(question.chosenTopic.value);
            payload.frageText = question.frageText;

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + intygsId;
            $http.post(restPath, payload).success(function(data) {
                $log.debug('got callback data:' + data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(data));
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
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + ArendeSvar.internReferens + '/besvara';
            $http.put(restPath, ArendeSvar.meddelande).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(data));
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }


        /*
         * update the handled status to handled ('Closed') of a QuestionAnswer
         */
        function _closeAsHandled(fragaSvarId, intygsTyp, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/stang';
            $http.get(restPath).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(data));
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * update the handled status to handled ('Closed') of a QuestionAnswer
         */
        function _closeAllAsHandled(qas, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/stang';
            var fs = [];
            angular.forEach(qas, function(qa/*, key*/) {
                this.push({ intygsTyp : qa.intygsReferens.intygsTyp, fragaSvarId:qa.internReferens });
            }, fs);

            $http.put(restPath, fs).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(ArendeLegacyService.convertFragasvarListToArendeList(data));
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
        function _openAsUnhandled(fragaSvarId, intygsTyp, onSuccess, onError) {
            $log.debug('_openAsUnhandled: fragaSvarId:' + fragaSvarId + ' intygsTyp: ' + intygsTyp);

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/oppna';
            $http.get(restPath).success(function(data) {
                $log.debug('got data:' + data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(data));
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        /*
         * Toggle vidarebefordrad state of a fragasvar entity with given id
         */
        function _setVidarebefordradState(fragaSvarId, intygsTyp, isVidareBefordrad, callback) {
            $log.debug('_setVidareBefordradState');
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/hanterad';
            $http.put(restPath, {'dispatched' : isVidareBefordrad}).success(function(data) {
                $log.debug('_setVidareBefordradState data:' + data);
                callback(ArendeLegacyService.convertFragasvarToArende(data));
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                callback(null);
            });
        }

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