/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
                $log.debug(restPath + ' response: ' + response.data);
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
                $log.debug(restPath + ' response: ' + response.data);
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
                $log.debug(restPath + ' response: ' + response.data);
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
