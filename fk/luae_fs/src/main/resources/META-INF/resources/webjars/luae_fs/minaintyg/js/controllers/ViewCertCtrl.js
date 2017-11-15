angular.module('luae_fs').controller('luae_fs.ViewCertCtrl',
        [ '$location', '$log', '$stateParams', '$scope', 'common.IntygService', 'luae_fs.viewConfigFactory',

        function($location, $log, $stateParams, $scope, certificateService, viewConfigFactory) {
            'use strict';

            $scope.cert = undefined;

            $scope.send = function() {
                $location.path('/send/luae_fs/' + $stateParams.certificateId + '/FKASSA');
            };

            $scope.errorMessage = null;
            $scope.doneLoading = false;
            certificateService.getCertificate('luae_fs', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.certMeta = result.meta;
                    $scope.errorMessage = null;
                } else {
                    $scope.errorMessage = 'error.certnotfound';
                }
            }, function(error) {
                $scope.doneLoading = true;
                $log.debug('getCertificate got error' + error);
                $scope.errorMessage = 'error.certnotfound';
            });

            $scope.pagefocus = true;
            $scope.uvConfig = viewConfigFactory.getViewConfig();
        } ]);
