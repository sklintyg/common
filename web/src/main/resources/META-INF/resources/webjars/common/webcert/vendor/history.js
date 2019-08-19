/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

angular.module('ui.router.history', [
    'ui.router'
]).service('$history', function($state) {
    'use strict';
    var history = [];
    var goingBack = false;

    angular.extend(this, {
        push: function(state, params) {
            if(goingBack) {
                goingBack = false;
                return;
            }
            history.push({ state: state, params: params });
        },
        all: function() {
            return history;
        },
        go: function(step) {
            step = Math.abs(step || 1);
            var prev = this.previous(step || -1);
            history = history.splice(0, history.length - step);
            goingBack = true;
            return $state.go(prev.state, prev.params);
        },
        previous: function(step) {
            return history[history.length - step];
        },
        back: function() {
            return this.go(-1);
        }
    });

}).run(function($history, $state, $rootScope) {
    'use strict';
    $rootScope.$on('$stateChangeSuccess', function(event, to, toParams, from, fromParams) {
        if (!from.abstract) { // jshint ignore:line

            if(angular.isDefined(from.data) && angular.isDefined(to.data)) {

                if(!!from.data.intygType && !!to.data.intygType &&
                    from.name === from.data.intygType + '.utkast' && to.name === 'webcert.intyg.' + to.data.intygType) {
                    // We're probably saving a draft, skip adding history
                    return;
                }
            }
            $history.push(from, fromParams);
        }
    });

    $history.push($state.current, $state.params);
});