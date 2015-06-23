/**
 * Common utkast proxy. All utkast-related REST-functions goes here.
 */
angular.module('common').factory('common.UtkastProxy',
    function($http, $log, $window) {
        'use strict';

        var saveDraftInProgress = false;

        function _handleError(callback, error) {
            if (callback) {
                callback(error);
            } else {
                $log.error(error);
            }
        }

        /**
         * Get a utkast with the specified id from the server.
         */
        function _getUtkast(intygsId, intygsTyp, onSuccess, onError) {
            $log.debug('_getDraft intygsId: ' + intygsId + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + intygsId;
            $http.get(restPath).
                success(function(data) {
                    $log.debug('_getDraft data: ' + data);
                    onSuccess(data);
                }).
                error(function(data, status) {
                    $log.error('error ' + status);
                    onError(data);
                });
        }

        /**
         * Saves a utkast to the server.
         */
        function _saveUtkast(intygsId, intygsTyp, version, cert, onSuccess, onError) {
            $log.debug('_saveDraft id: ' + intygsId + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + intygsId + '/' + version;

            $window.saving = true;
            saveDraftInProgress = true;
            $http.put(restPath, cert).
                success(function(data) {
                    $log.debug('_saveDraft data: ' + data);
                    onSuccess(data);
                    saveDraftInProgress = false;
                }).
                error(function(data, status) {
                    $log.error('error ' + status);
                    onError(data);
                    saveDraftInProgress = false;
                }).
                finally(function(){
                    saveDraftInProgress = false;
                });
        }

        function _isSaveUtkastInProgress() {
            return saveDraftInProgress;
        }

        /**
         * Discards a utkast and removes it from the server.
         */
        function _discardUtkast(intygsId, intygsTyp, version, onSuccess, onError) {
            $log.debug('_discardDraft id: ' + intygsId + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + intygsId + '/' + version;
            $http['delete'](restPath).
                success(function(data) {
                    $log.debug('_discardDraft data: ' + data);
                    onSuccess(data);
                }).
                error(function(data, status) {
                    $log.error('error ' + status);
                    onError(data);
                });
        }

        function _getSigneringshash(intygsId, intygsTyp, version, onSuccess, onError) {
            $log.debug('_getSigneringshash, intygsId: ' + intygsId + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + intygsId + '/' + version + '/signeringshash';
            $http.post(restPath).
                success(function(data) {
                    onSuccess(data);
                }).
                error(function(error) {
                    _handleError(onError, error);
                });
        }

        function _getSigneringsstatus(ticketId, intygsTyp, onSuccess, onError) {
            $log.debug('_getSigneringsstatus, ticketId: ' + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + ticketId + '/signeringsstatus';
            $http.get(restPath).
                success(function(data) {
                    onSuccess(data);
                }).
                error(function(error) {
                    _handleError(onError, error);
                });
        }

        function _signeraUtkast(intygsId, intygsTyp, version, onSuccess, onError) {
            $log.debug('_signeraUtkast, intygsId:' + intygsId + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + intygsId + '/' + version + '/signeraserver';
            $http.post(restPath).
                success(function(data) {
                    onSuccess(data);
                }).
                error(function(error) {
                    _handleError(onError, error);
                });
        }

        function _signeraUtkastWithSignatur(ticketId, intygsTyp, signatur, onSuccess, onError) {
            $log.debug('_signeraUtkastWithSignatur, ticketId: ' + ticketId + ' intygsTyp: ' + intygsTyp + ' sign:' + signatur);
            var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + ticketId + '/signeraklient';
            $http.post(restPath, {
                'signatur': signatur
            }).
                success(function(ticket) {
                    onSuccess(ticket);
                }).
                error(function(error) {
                    _handleError(onError, error);
                });
        }

        // Return public API for the service
        return {
            getUtkast: _getUtkast,
            saveUtkast: _saveUtkast,
            isSaveUtkastInProgress: _isSaveUtkastInProgress,
            discardUtkast: _discardUtkast,
            getSigneringshash: _getSigneringshash,
            getSigneringsstatus: _getSigneringsstatus,
            signeraUtkast: _signeraUtkast,
            signeraUtkastWithSignatur: _signeraUtkastWithSignatur
        };
    });
