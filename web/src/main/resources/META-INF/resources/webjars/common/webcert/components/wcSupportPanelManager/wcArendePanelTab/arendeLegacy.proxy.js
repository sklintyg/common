/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.ArendeLegacyProxy', ['$http', '$log', 'common.ArendeLegacyService',
    function($http, $log, ArendeLegacyService) {
        'use strict';

        /*
         * Load questions and answers data for a certificate
         */
        function _getArenden(intygsId, intygsTyp, timeout, onSuccess, onError) {

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + intygsId;
            $http.get(restPath, { timeout:timeout }).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarViewListToArendeList(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * save new question
         */
        function _sendNewArende(intygsId, intygsTyp, question, onSuccess, onError) {
            var payload = {};
            payload.amne = ArendeLegacyService.convertAmneArendeToFragasvar(question.chosenTopic);
            payload.frageText = question.frageText;

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + intygsId;
            $http.post(restPath, payload).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * save new answer to a question
         */
        function _saveAnswer(ArendeSvar, intygsTyp, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + ArendeSvar.internReferens + '/besvara';
            $http.put(restPath, ArendeSvar.meddelande).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * save new answer to a komplettering question
         */
        function _saveKompletteringAnswer(meddelande, intygsTyp, intygsId, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/' + intygsId + '/besvara';
            $http.put(restPath, meddelande).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarViewListToArendeList(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * update the handled status to handled ('Closed') of a QuestionAnswer
         */
        function _closeAsHandled(fragaSvarId, intygsTyp, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/stang';
            $http.get(restPath).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * update the handled status to handled ('Closed') of a QuestionAnswer
         */
        function _closeAllAsHandled(arenden, intygsTyp, onSuccess, onError) {
            var restPath = '/moduleapi/fragasvar/stang';
            var fs = [];
            angular.forEach(arenden, function(arendeListItem) {
                this.push({ intygsTyp : intygsTyp, fragaSvarId:arendeListItem.arende.fraga.internReferens });
            }, fs);

            $http.put(restPath, fs).then(function(response) {
                $log.debug(restPath + ' response:' + angular.toJSON(response.data));
                onSuccess(ArendeLegacyService.convertFragasvarListToArendeList(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * update the handled status to unhandled ('ANSWERED or PENDING_EXTERNAL_ACTION depending if the question has an
         * answer set or not') of a QuestionAnswer
         */
        function _openAsUnhandled(fragaSvarId, intygsTyp, onSuccess, onError) {
            $log.debug('_openAsUnhandled: fragaSvarId:' + fragaSvarId + ' intygsTyp: ' + intygsTyp);

            var restPath = '/moduleapi/fragasvar/' + intygsTyp + '/' + fragaSvarId + '/oppna';
            $http.get(restPath).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(ArendeLegacyService.convertFragasvarToArende(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * Toggle vidarebefordrad state of a fragasvar entity with given id
         */
        function _setVidarebefordradState(intygsId, callback) {
            $log.debug('_setVidareBefordradState');
            var restPath = '/moduleapi/fragasvar/' + intygsId + '/vidarebefordrad';
            $http.post(restPath).then(function(response) {
                $log.debug('_setVidareBefordradState data:' + response.data);
                callback(ArendeLegacyService.convertFragasvarListToArendeList(response.data));
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                callback(null);
            });
        }

        return {
            getArenden: _getArenden,
            sendNewArende: _sendNewArende,
            saveAnswer: _saveAnswer,
            saveKompletteringAnswer: _saveKompletteringAnswer,
            closeAsHandled: _closeAsHandled,
            openAsUnhandled: _openAsUnhandled,
            closeAllAsHandled: _closeAllAsHandled,
            setVidarebefordradState: _setVidarebefordradState
        };

    }]);
