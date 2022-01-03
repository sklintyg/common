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
angular.module('common').factory('common.AtticHelper', [function() {
    'use strict';

    function _updateToAtticImmediate(model, optionsKey) {
        model.updateToAttic(optionsKey);
        model.clear(optionsKey);
    }

    return {
        restoreFromAttic: function(model, optionsKey) {
            if (model.isInAttic(optionsKey)) {
                model.restoreFromAttic(optionsKey);
            }
        },

        updateToAttic: function($scope, model, optionsKey) {

            // This $destroy is invoked whenever a form element is removed from the DOM and we want to push the
            // change to the underlying model.
            $scope.$on('$destroy', function() {
                _updateToAtticImmediate(model, optionsKey);
            });
        },
        updateToAtticImmediate: _updateToAtticImmediate
    };
}]);