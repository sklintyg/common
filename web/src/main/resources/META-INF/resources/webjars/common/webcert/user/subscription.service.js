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
    [ 'common.UserModel', 'common.featureService' , function(UserModel, featureService) {
      'use strict';

      var acknowledgedCareUnits = [];

      function _hasAcknowledgedSubscriptionInfoForCareUnit() {
          return acknowledgedCareUnits.includes(UserModel.user.valdVardenhet.id);
      }

      function _acknowledgeSubscriptionInfoForCareUnit() {
          acknowledgedCareUnits.push(UserModel.user.valdVardenhet.id);
      }

      function _isDuringAdjustmentPeriod() {
          return featureService.isFeatureActive('SUBSCRIPTION_DURING_ADJUSTMENT_PERIOD') &&
              !featureService.isFeatureActive('SUBSCRIPTION_PAST_ADJUSTMENT_PERIOD');
      }

      function _isPastAdjustmentPeriod() {
          return featureService.isFeatureActive('SUBSCRIPTION_PAST_ADJUSTMENT_PERIOD');
      }

      function _isAnySubscriptionFeatureActive() {
          return featureService.isFeatureActive('SUBSCRIPTION_DURING_ADJUSTMENT_PERIOD') ||
              featureService.isFeatureActive('SUBSCRIPTION_PAST_ADJUSTMENT_PERIOD');
      }

      function _hasCareProviderMissingSubscription() {
          return UserModel.user.subscriptionInfo.unitHsaIdList.length > 0;
      }

      function _isCareProviderMissingSubscription(careProviderHsaId) {
          return UserModel.user.subscriptionInfo.unitHsaIdList.indexOf(careProviderHsaId) === -1;
      }

      function _isElegUser() {
          var authScheme = UserModel.user.authenticationScheme;
          return authScheme === 'urn:inera:webcert:eleg:fake' ||
              authScheme === 'urn:oasis:names:tc:SAML:2.0:ac:classes:SoftwarePKI' ||
              authScheme === 'urn:oasis:names:tc:SAML:2.0:ac:classes:SmartcardPKI' ||
              authScheme === 'urn:oasis:names:tc:SAML:2.0:ac:classes:MobileTwofactorContract';
        }

      return {
          hasAcknowledgedSubscriptionInfoForCareUnit: _hasAcknowledgedSubscriptionInfoForCareUnit,
          acknowledgeSubscriptionInfoForCareUnit: _acknowledgeSubscriptionInfoForCareUnit,
          isElegUser: _isElegUser,
          isDuringAdjustmentPeriod: _isDuringAdjustmentPeriod,
          isPastAdjustmentPeriod: _isPastAdjustmentPeriod,
          isAnySubscriptionFeatureActive: _isAnySubscriptionFeatureActive,
          hasCareProviderMissingSubscription: _hasCareProviderMissingSubscription,
          isCareProviderMissingSubscription: _isCareProviderMissingSubscription
      };
    }]);
