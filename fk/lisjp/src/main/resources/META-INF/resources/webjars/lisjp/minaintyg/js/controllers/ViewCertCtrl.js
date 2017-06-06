angular.module('lisjp').controller(
        'lisjp.ViewCertCtrl',
        [ '$location', '$log', '$stateParams', '$scope', 'common.IntygService', 'lisjp.customizeViewstate', 'lisjp.viewConfigFactory',
                function($location, $log, $stateParams, $scope, certificateService, customizeViewstate, viewConfigFactory) {
                    'use strict';

                    $scope.cert = undefined;

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
                } ]);
