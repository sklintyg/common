angular.module('lisjp').controller('lisjp.CustomizePdfCtrl',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope','lisjp.customizeViewstate', 'common.IntygListService',
        'common.IntygService', 'common.dialogService', 'common.messageService', 'common.moduleService',
        function($location, $log, $rootScope, $stateParams, $scope, customizeViewstate, listCertService, certificateService, dialogService,
            messageService, moduleService) {
            'use strict';

            $scope.moduleService = moduleService;

            $scope.customizeViewstate = customizeViewstate;

            certificateService.getCertificate('lisjp', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $rootScope.cert = $scope.cert;
                    //Tillaggsfragor are dynamic, so we create field config for any such things after cert has been loaded.
                    customizeViewstate.addTillaggsFragor($scope.cert.tillaggsfragor);
                } else {

                    // show error view
                    $location.path('/lisjp/visafel/certnotfound');
                }
            }, function() {
                $log.debug('got error');
                $location.path('/lisjp/visafel/certnotfound');
            });


            $scope.backToViewCertificate = function() {
                customizeViewstate.resetModel();
                $location.path('/lisjp/view/' + $stateParams.certificateId);
            };

            $scope.confirmCertificateCustomization = function() {
                $location.path('/lisjp/customize/' + $stateParams.certificateId + '/summary');
            };

        }]);
