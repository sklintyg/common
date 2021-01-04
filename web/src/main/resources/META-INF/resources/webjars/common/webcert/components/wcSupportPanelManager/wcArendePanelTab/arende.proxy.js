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
angular.module('common').factory('common.ArendeProxy', ['$http', '$log', 'common.ArendeLegacyProxy',
    function($http, $log, ArendeLegacyProxy) {
        'use strict';

        /*
         * Load questions and answers data for a certificate
         */
        function _getArenden(intygsId, intygsTyp, timeout, onSuccess, onError) {
            $log.debug('_getArenden: intygsId:' + intygsId + ' intygsTyp: ' + intygsTyp);
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.getArenden.apply(null, arguments);
            }

            var restPath = '/moduleapi/arende/' + intygsId;
            $http.get(restPath, { timeout:timeout }).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(response.data);
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
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
                amne: arende.chosenTopic,
                meddelande: arende.frageText
            };

            var restPath = '/moduleapi/arende/' + intygsTyp + '/' + intygsId;
            $http.post(restPath, payload).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(response.data);
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
            $log.debug('_saveAnswer: arendeId:' + ArendeSvar.fragaInternReferens + ' intygsTyp: ' + intygsTyp);
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.saveAnswer.apply(null, arguments);
            }

            var restPath = '/moduleapi/arende/' + intygsTyp + '/' + ArendeSvar.fragaInternReferens + '/besvara';
            $http.put(restPath, ArendeSvar.meddelande).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(response.data);
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * save new administrativ fråga answer to a komplettering question
         */
        function _saveKompletteringAnswer(meddelande, intygsTyp, intygsId, onSuccess, onError) {
            $log.debug('_saveAnswer: intygsId: ' + intygsId);
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.saveKompletteringAnswer.apply(null, arguments);
            }

            var restPath = '/moduleapi/arende/' + intygsId + '/besvara';
            $http.put(restPath, meddelande).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(response.data);
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
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

            $http.put(restPath).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(response.data);
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
        function _openAsUnhandled(arendeId, intygsTyp, onSuccess, onError) {
            $log.debug('_openAsUnhandled: arendeId:' + arendeId + ' intygsTyp: ' + intygsTyp);
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.openAsUnhandled.apply(null, arguments);
            }

            var restPath = '/moduleapi/arende/' + intygsTyp + '/' + arendeId + '/oppna';
            $http.put(restPath).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(response.data);
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
            var restPath = '/moduleapi/arende/stang';
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.closeAllAsHandled.apply(null, arguments);
            }

            var fs = [];
            angular.forEach(arenden, function(arendeListItem, key) {
                this.push({ intygsTyp: arendeListItem.arende.fraga.intygTyp, arendeId: arendeListItem.arende.internReferens });
            }, fs);

            $http.put(restPath, fs).then(function(response) {
                $log.debug(restPath + ' response:' + response.data);
                onSuccess(response.data);
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                onError(response.data);
            });
        }

        /*
         * Toggle vidarebefordrad state of a arende entity with given id
         */
        function _setVidarebefordradState(intygId, intygsTyp, callback) {
            $log.debug('_setVidareBefordradState');
            if (intygsTyp === 'fk7263') {
                return ArendeLegacyProxy.setVidarebefordradState(intygId, callback);
            }

            var restPath = '/moduleapi/arende/' + intygId + '/vidarebefordrad';
            $http.post(restPath).then(function(response) {
                $log.debug('_setVidareBefordradState data:' + response.data);
                callback(response.data);
            }, function(response) {
                $log.error('error ' + response.status);
                // Let calling code handle the error of no data response
                callback(null);
            });
        }

        // Return public API for the service
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
