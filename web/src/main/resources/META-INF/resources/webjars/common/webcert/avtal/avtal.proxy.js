angular.module('common').factory('common.AvtalProxy',
    [ '$http', '$log', 'common.AvtalModel',
        function($http, $log, AvtalModel) {
            'use strict';

            /**
             * getLatestAvtal
             */
            function _getLatestAvtal(onSuccess, onError) {
                var restPath = '/api/anvandare/latestavtal';
                $http.get(restPath).success(function(data) {
                    $log.debug(data);
                    onSuccess(AvtalModel.build(data));
                }).error(function() {
                    $log.warn('Avtal-tjänsten kunde inte kontaktas.');
                    onError();
                });
            }

            function _approve(onSuccess, onError){
                var restPath = '/api/anvandare/godkannavtal';
                $http.put(restPath).success(function() {
                    onSuccess();
                }).error(function() {
                    $log.warn('Avtal-tjänsten kunde inte kontaktas.');
                    onError();
                });
            }

             // Return public API for the service
            return {
                getLatestAvtal: _getLatestAvtal,
                approve: _approve
            };
        }]);
