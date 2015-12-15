/**
 * label directive for fetching texts associated with a cert a and version of the same
 *
 * Usage: <intygLabel key="some.resource.key" [fallback="defaulttextifnokeyfound"]/>
 */
angular.module('common').factory( 'common.intygLabelService',
    function($http, $log) {
        'use strict';

        var cachedLabels = null;

        function _emptyCache() {
            $log.debug('Clearing cache labels');
            cachedLabels = null;
        }

        function _getLabels(callback) {
                if (cachedLabels !== null) {
                    $log.debug('returning cached response lables');
                    callback(cachedLabels);
                    return;
                }
                $http.get('/api/utkast/questions/sjukersattning').success(function(data) {
                    $log.debug('populating cache labels');
                    //$log.info('from api: ' + JSON.stringify(data));
                    cachedLabels = data;
                    callback(cachedLabels);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    //give calling code a chance to handle errorHAr
                    callback(null);
                });
        }

        function _getLabel() {

        }

        // Return public API for our service
        return {
            getLabels: _getLabels,
            getLabel: _getLabel,
            emptyCache: _emptyCache()
        };
    }
);