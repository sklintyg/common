angular.module('luse').controller('luse.ViewCertCtrl',
    [ '$location', '$log', '$stateParams', '$scope', 'common.IntygService',
        'common.dialogService', 'common.messageService', 'luse.viewConfigFactory',
        function($location, $log, $stateParams, $scope, certificateService,
            dialogService, messageService, viewConfigFactory) {
            'use strict';

            $scope.cert = undefined;
            $scope.messageService = messageService;

            $scope.send = function() {
                $location.path('/send/luse/' + $stateParams.certificateId + '/FKASSA');
            };

            $scope.errorMessage = null;
            $scope.doneLoading = false;
            certificateService.getCertificate('luse', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.certMeta = result.meta;
                    $scope.errorMessage = null;
                } else {
                    $scope.errorMessage = 'error.certnotfound';
                }
            }, function(errorMsgKey) {
                $scope.doneLoading = true;
                $log.debug('getCertificate got error ' + status);
                $scope.errorMessage = errorMsgKey;
            });

            $scope.pagefocus = true;
            $scope.uvConfig = viewConfigFactory.getViewConfig();
        }]);
