/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.moduleService', ['common.dynamicLinkService', function(dynamicLinkService) {
    'use strict';

    var _moduleArray = null;

    function _findModuleById(moduleId) {
        var filterArray = [];
        if (Array.isArray(_moduleArray)) {
            filterArray = _moduleArray.filter(function(module) {
                return module.id === moduleId;
            });
        }

        // The filter() method creates a new array with all elements
        // that pass the test implemented by the provided function.
        // If there were an element that passed the test, the filterArray
        // only have one item since _moduleArray only keep unique entries.
        return filterArray.length > 0 ? filterArray[0] : null;
    }

    function _getModuleName(moduleId) {
        var module = _findModuleById(moduleId);
        return !module ? '' : module.label;
    }

    function _setModules(modules) {
        _moduleArray = modules;
        if(_moduleArray){
            _moduleArray.forEach(function(intygModule) {
                if(intygModule.detailedDescription){
                    intygModule.detailedDescription = dynamicLinkService.processLinkTags(intygModule.detailedDescription);
                }
            });
        }
    }

    function _getModules(modules) {
        return _moduleArray;
    }

    return {
        getModule: _findModuleById,
        getModuleName: _getModuleName,
        setModules: _setModules,
        getModules: _getModules
    };
}]);

