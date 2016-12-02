/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.moduleService',
    ['$http', '$log', function($http, $log) {
        'use strict';

        var moduleArray = null;

        function _findModule(moduleId) {
            var found = null;
            if (Array.isArray(moduleArray)) {
                found = moduleArray.find(function(module) {
                    return module.id === moduleId;
                });
            }
            return found;
        }

        function _lookupName(moduleId) {
            var module = _findModule(moduleId);
            return !module ? '' : module.label;
        }

        function _setModules(modules) {
            moduleArray = modules;
        }

        function _getModules(onSuccess, onError) {
            if (Array.isArray(moduleArray)) {
                onSuccess(moduleArray);
            } else {
                var restPath = '/api/modules/map';
                $http.get(restPath).success(function(data) {
                    moduleArray = data;
                    onSuccess(data);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    if (onError) {
                        onError();
                    }
                });
            }
        }

        return {
            getModule: _findModule,
            getModuleName: _lookupName,
            getModules: _getModules,
            setModules: _setModules
        };
    }]
);

