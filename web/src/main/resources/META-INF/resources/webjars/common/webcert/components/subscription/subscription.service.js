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

angular.module('common').factory('common.subscriptionService', [ 'common.UserModel', function(UserModel) {
      'use strict';

      function _getRequireSubscriptionStartDate() {
        return UserModel.user.requireSubscriptionStartDate;
      }

      function _hasSubscription(subscriptionAction) {
        return subscriptionAction !== 'BLOCK';
      }

      function _acknowledgeWarning() {
        UserModel.user.valdVardgivare.subscriptionAction = 'NONE';
      }

      function _shouldDisplayWarning() {
        return UserModel.user.hasOwnProperty('valdVardgivare') && UserModel.user.valdVardgivare.subscriptionAction === 'WARN';
      }

      return {
        getRequireSubscriptionStartDate: _getRequireSubscriptionStartDate,
        hasSubscription: _hasSubscription,
        acknowledgeWarning: _acknowledgeWarning,
        shouldDisplayWarning: _shouldDisplayWarning
      };
    }]);
