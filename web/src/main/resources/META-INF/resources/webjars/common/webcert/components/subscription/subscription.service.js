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

      function _getSubscriptionAdaptationStartDate() {
        return UserModel.user.subscriptionInfo.subscriptionAdaptationStartDate;
      }

      function _getRequireSubscriptionStartDate() {
        return UserModel.user.subscriptionInfo.requireSubscriptionStartDate;
      }

      function _missingSubscriptionBlock(careProviderId) {
        return UserModel.user.subscriptionInfo.subscriptionAction === 'BLOCK' && missingSubscription(careProviderId);
      }

      function _acknowledgeWarning() {
        if (UserModel.user.valdVardgivare) {
          var index = UserModel.user.subscriptionInfo.careProvidersMissingSubscription.indexOf(UserModel.user.valdVardgivare.id);
          if (index > -1) {
            UserModel.user.subscriptionInfo.careProvidersMissingSubscription.splice(index, 1);
          }
        }
      }

      function _shouldDisplayWarning() {
        return UserModel.user.hasOwnProperty('valdVardgivare') && UserModel.user.subscriptionInfo.subscriptionAction === 'WARN' &&
            missingSubscription(UserModel.user.valdVardgivare.id);
      }

      function missingSubscription(careProviderId) {
        return UserModel.user.subscriptionInfo.careProvidersMissingSubscription.includes(careProviderId);
      }

      return {
        getSubscriptionAdaptationStartDate: _getSubscriptionAdaptationStartDate,
        getRequireSubscriptionStartDate: _getRequireSubscriptionStartDate,
        missingSubscriptionBlock: _missingSubscriptionBlock,
        acknowledgeWarning: _acknowledgeWarning,
        shouldDisplayWarning: _shouldDisplayWarning
      };
    }]);
