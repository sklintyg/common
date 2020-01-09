/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
angular.module('common').service('common.ArendeHelper',
    ['$log', '$timeout', '$window', 'common.statService', 'common.ArendeListItemModel', 'common.ArendeListViewStateService', 'common.UserModel',
        function ($log, $timeout, $window, statService, ArendeListItemModel, ArendeListViewState, UserModel) {
            'use strict';

            this.getUnhandledArenden = function(arendeList) {
                if(!arendeList || arendeList.length === 0){
                    return false;
                }
                var arendeListfiltered = [];
                for (var i = 0, len = arendeList.length; i < len; i++) {
                    var arendeListItem = arendeList[i];
                    var isUnhandled = arendeListItem.isUnhandled();
                    var fromFk = arendeListItem.fromFk();
                    if (arendeListItem.arende.fraga.status === 'ANSWERED' || (isUnhandled && fromFk) ){
                        arendeListfiltered.push(arendeListItem);
                    }
                }
                return arendeListfiltered;
            };

        }]);
