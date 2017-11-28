angular.module('common').factory('common.ArendeDraftProxy', ['$http', '$log',
    function($http, $log) {
        'use strict';

        /*
         * Get the arende draft for a certificate. This is the draft for a new question to recipient.
         */
        function _getDraft(intygsId, onSuccess, onError) {
            $log.debug('_getDraft: intygsId:' + intygsId);

            var restPath = '/api/arende/draft/' + intygsId;

            $http.get(restPath).then(function(response) {
                $log.debug('got data: ' + response.data);
                onSuccess(response.data);
            }, function(response) {
                $log.debug('error: ' + response.data);
                onError(response.data);
            });
        }

        /*
         * Save new arende draft.
         */
        function _saveDraft(intygsId, questionId, text, amne, onSuccess, onError) {
            $log.debug('_saveDraft: intygsId:' + intygsId + ' questionId: ' + questionId);

            var restPath = '/api/arende/draft';

            var payload = {
                intygId: intygsId,
                text: text
            };

            if (questionId !== undefined) {
                payload.questionId = questionId;
            }
            if (amne !== undefined) {
                payload.amne = amne;
            }

            $http.put(restPath, payload).then(function(response) {
                $log.debug('got data: ' + response.data);
                onSuccess(response.data);
            }, function(response) {
                $log.debug('error: ' + response.data);
                onError(response.data);
            });
            return true;
        }

        /*
         * Delete a draft.
         */
        function _deleteDraft(intygsId, questionId, onSuccess, onError) {
            $log.debug('_deleteDraft: intygsId:' + intygsId + ' questionId: ' + questionId);

            var restPath = '/api/arende/draft/' + intygsId;
            if (questionId !== undefined) {
                restPath = restPath + '/' + questionId;
            }

            $http({
                method: 'DELETE',
                url: restPath
            }).then(function(response) {
                $log.debug('got data: ' + response.data);
                onSuccess(response.data);
            }, function(response) {
                $log.debug('error: ' + response.data);
                onError(response.data);
            });
        }

        /*
         * Delete a draft for a new question to recipient.
         */
        function _deleteQuestionDraft(intygsId, onSuccess, onError) {
            $log.debug('_deleteQuestionDraft: intygsId:' + intygsId);
            _deleteDraft(intygsId, undefined, onSuccess, onError);
        }

        // Return public API for the service
        return {
            getDraft: _getDraft,
            saveDraft: _saveDraft,
            deleteDraft: _deleteDraft,
            deleteQuestionDraft: _deleteQuestionDraft
        };
    }]);
