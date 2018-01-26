/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('luae_fs').factory('luae_fs.FormFactoryHelper', ['common.ObjectHelper', 'common.UtilsService', function(ObjectHelper, UtilsService) {
    'use strict';

    function _underlagListener(field, newValue, oldValue, scope, stopWatching) {
        var model = scope.model;
        if (newValue) {
            if (model.isInAttic(model.properties.underlag)) {
                model.restoreFromAttic(model.properties.underlag);
            }
            if (!model.underlag || model.underlag.length === 0) {
                model.underlag.push({typ: null, datum: null, hamtasFran: null});
            }
        } else {
            model.updateToAttic(model.properties.underlag);
            model.clear(model.properties.underlag);
            scope.model.underlag = [];
        }
    }

    return {
        underlagListener: _underlagListener
    };
}]);
