angular.module('common').directive('wcOriginNormalWarning',
    ['$uibModal', '$window', 'common.UserModel', 'common.featureService',
      function($uibModal, $window, UserModel, featureService) {
        'use strict';

        return {
          restrict: 'E',
          scope: {},
          template: '',
          link: function() {
            var dialogInstance;

            var showOriginNormalWarningDialog = function() {
              $window.sessionStorage.originalNormalWarningDialogShown = true;

              var careProviderName = UserModel.user.valdVardgivare.namn

              dialogInstance = $uibModal.open({
                templateUrl:
                    '/web/webjars/common/webcert/components/headers/wcAppHeader/wcOriginNormalWarning/wcOriginNormalWarning.infodialog.html',
                size: 'md',
                id: 'OriginNormalWarningId',
                controller: function($scope, $uibModalInstance) {
                  $scope.careProviderName = careProviderName;
                }
              });
            };

            var shouldOriginNormalWarningDialogBeDisplayed = function() {
              return UserModel.user.origin === 'NORMAL'
                  && featureService.isFeatureActive(featureService.features.VARNING_FRISTAENDE)
                  && !$window.sessionStorage.originalNormalWarningDialogShown;
            }

            if (shouldOriginNormalWarningDialogBeDisplayed()) {
              showOriginNormalWarningDialog();
            }
          }
        };
      }]);