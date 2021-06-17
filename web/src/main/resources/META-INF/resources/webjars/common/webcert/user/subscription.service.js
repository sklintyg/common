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

angular.module('common').factory('common.subscriptionService',
    [ 'common.UserModel', function(UserModel) {
      'use strict';

      function _hasAcknowledgedSubscriptionWarning() {
        return UserModel.user.subscriptionInfo.acknowledgedWarnings.includes(UserModel.user.valdVardgivare.id);
      }

      function _setAcknowledgedWarnings(acknowledgedWarnings) {
        UserModel.user.subscriptionInfo.acknowledgedWarnings = acknowledgedWarnings;
      }

      function _addAcknowledgedWarning() {
        UserModel.user.subscriptionInfo.acknowledgedWarnings.push(UserModel.user.valdVardgivare.id);
      }

      function _shouldDisplaySubscriptionWarning() {
        if (UserModel.user.hasOwnProperty('valdVardgivare')) {
          return UserModel.isNormalOrigin() && _isSubscriptionAdaptation() && _isCareProviderMissingSubscription();
        }
        return false;
      }

      function _isSubscriptionAdaptation() {
        return UserModel.user.subscriptionInfo.subscriptionState === 'SUBSCRIPTION_ADAPTATION';
      }

      function _isSubscriptionRequired() {
        return UserModel.user.subscriptionInfo.subscriptionState === 'SUBSCRIPTION_REQUIRED';
      }

      function _isAnySubscriptionFeatureActive() {
        return _isSubscriptionAdaptation() || _isSubscriptionRequired();
      }

      function _isCareProviderMissingSubscription() {
        return UserModel.user.subscriptionInfo.careProviderHsaIdList.includes(UserModel.user.valdVardgivare.id);
      }

      function _getRequireSubscriptionStartDate() {
        return UserModel.user.subscriptionInfo.requireSubscriptionStartDate;
      }

      function _hasSubscription(careProviderHsaID) {
        return !UserModel.user.subscriptionInfo.careProviderHsaIdList.includes(careProviderHsaID);
      }

      return {
        hasAcknowledgedSubscriptionWarning: _hasAcknowledgedSubscriptionWarning,
        setAcknowledgedWarnings: _setAcknowledgedWarnings,
        addAcknowledgedWarning: _addAcknowledgedWarning,
        shouldDisplaySubscriptionWarning: _shouldDisplaySubscriptionWarning,
        isSubscriptionAdaptation: _isSubscriptionAdaptation,
        isSubscriptionRequired: _isSubscriptionRequired,
        isAnySubscriptionFeatureActive: _isAnySubscriptionFeatureActive,
        isCareProviderMissingSubscription: _isCareProviderMissingSubscription,
        getRequireSubscriptionStartDate: _getRequireSubscriptionStartDate,
        hasSubscription: _hasSubscription
      };
    }]);
