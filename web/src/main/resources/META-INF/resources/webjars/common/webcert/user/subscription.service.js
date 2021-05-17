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
          return UserModel.user.subscriptionInfo.acknowledgedWarnings.includes(UserModel.user.valdVardenhet.id);
      }

      function _setAcknowledgedWarnings(acknowledgedWarnings) {
          UserModel.user.subscriptionInfo.acknowledgedWarnings = acknowledgedWarnings;
      }

      function _addAcknowledgedWarning() {
          UserModel.user.subscriptionInfo.acknowledgedWarnings.push(UserModel.user.valdVardenhet.id);
      }

      function _shouldDisplaySubscriptionWarning() {
          if (UserModel.user.hasOwnProperty('valdVardgivare')) {
              return UserModel.isNormalOrigin() && _isDuringAdjustmentPeriod() && _isCareProviderMissingSubscription();
          }
          return false;
      }

      function _isDuringAdjustmentPeriod() {
          return UserModel.user.subscriptionInfo.subscriptionAction === 'MISSING_SUBSCRIPTION_WARN';
      }

      function _isPastAdjustmentPeriod() {
          return UserModel.user.subscriptionInfo.subscriptionAction === 'MISSING_SUBSCRIPTION_BLOCK';
      }

      function _isAnySubscriptionFeatureActive() {
          return _isDuringAdjustmentPeriod() || _isPastAdjustmentPeriod();
      }

      function _isCareProviderMissingSubscription() {
          return UserModel.user.subscriptionInfo.unitHsaIdList.includes(UserModel.user.valdVardgivare.id);
      }

      function _getSubscriptionBlockStartDate() {
          return UserModel.user.subscriptionInfo.subscriptionBlockStartDate;
      }

      function _isElegUser() {
          return UserModel.user.subscriptionInfo.authenticationMethod === 'ELEG';
      }

      return {
          setAcknowledgedWarnings: _setAcknowledgedWarnings,
          hasAcknowledgedSubscriptionWarning: _hasAcknowledgedSubscriptionWarning,
          addAcknowledgedWarning: _addAcknowledgedWarning,
          isElegUser: _isElegUser,
          isDuringAdjustmentPeriod: _isDuringAdjustmentPeriod,
          isPastAdjustmentPeriod: _isPastAdjustmentPeriod,
          isAnySubscriptionFeatureActive: _isAnySubscriptionFeatureActive,
          shouldDisplaySubscriptionWarning: _shouldDisplaySubscriptionWarning,
          getSubscriptionBlockStartDate: _getSubscriptionBlockStartDate
      };
    }]);
