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
                        $location.path('/lisjp/customize/' + $stateParams.certificateId + '/step1');
                    };

                    $scope.errorMessage = null;
                    $scope.doneLoading = false;
                    certificateService.getCertificate('lisjp', $stateParams.certificateId, function(result) {
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
