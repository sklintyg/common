angular.module('lisjp').controller('lisjp.ViewCertCtrl',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygListService',
        'common.IntygService', 'common.dialogService', 'common.messageService', 'lisjp.customizeViewstate', 'lisjp.viewConfigFactory',
        function($location, $log, $rootScope, $stateParams, $scope, listCertService, certificateService, dialogService,
            messageService, customizeViewstate, viewConfigFactory) {
            'use strict';

            $scope.cert = undefined;
            $scope.messageService = messageService;

            $scope.send = function() {
                $location.path('/send/lisjp/' + $stateParams.certificateId + '/FKASSA');
            };

            $scope.customizeCertificate = function() {
                customizeViewstate.resetModel();
                $location.path('/lisjp/customize/' + $stateParams.certificateId);
            };

            $scope.doneLoading = false;
            certificateService.getCertificate('lisjp', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.certMeta = result.meta;
                } else {
                    // show error view
                    $location.path('/lisjp/visafel/certnotfound');
                }
            }, function() {
                $log.debug('got error');
                $location.path('/lisjp/visafel/certnotfound');
            });

            $scope.pagefocus = true;
            $scope.uvConfig = viewConfigFactory.getViewConfig();
        }]);
