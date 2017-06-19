angular.module('luae_fs').controller('luae_fs.ViewCertCtrl',
        [ '$location', '$log', '$stateParams', '$scope', 'common.IntygService', 'luae_fs.viewConfigFactory',

        function($location, $log, $stateParams, $scope, certificateService, viewConfigFactory) {
            'use strict';

            $scope.cert = undefined;

            $scope.send = function() {
                $location.path('/send/luae_fs/' + $stateParams.certificateId + '/FKASSA');
            };

            $scope.doneLoading = false;
            certificateService.getCertificate('luae_fs', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.certMeta = result.meta;
                } else {
                    // show error view
                    $location.path('/luae_fs/visafel/certnotfound');
                }
            }, function() {
                $log.debug('got error');
                $location.path('/luae_fs/visafel/certnotfound');
            });

            $scope.pagefocus = true;
            $scope.uvConfig = viewConfigFactory.getViewConfig();
        } ]);
