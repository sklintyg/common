angular.module('common').controller(
        'common.wcIntygReadOnlyViewController',
        [ '$stateParams', '$scope', 'common.IntygProxy', 'common.moduleService', 'common.dynamicLabelService', 'ViewConfigFactory', 'intygsType',
                function($stateParams, $scope, IntygProxy, moduleService, DynamicLabelService, viewConfigFactory, intygsType) {
                    'use strict';

                    // setup scope state object...
                    $scope.viewState = {
                        certName: moduleService.getModuleName(intygsType),
                        cert: undefined,
                        uvConfig: viewConfigFactory.getViewConfig(true),
                        doneLoading: false,
                        errorKey: ''
                    };

                    //..and load intyg data directly
                    IntygProxy.getIntyg($stateParams.certificateId, intygsType, function(result) {
                        $scope.viewState.doneLoading = true;
                        if (result !== null && result !== '') {

                            $scope.viewState.cert = result.contents;
                            //For intygstypes that have dynamic texts..
                            if ($scope.viewState.cert.textVersion) {
                                DynamicLabelService.updateDynamicLabels(intygsType, $scope.viewState.cert.textVersion).then(function(labels) {
                                    if (angular.isDefined(labels)) {
                                        DynamicLabelService.updateTillaggsfragorToModel(labels.tillaggsfragor, $scope.viewState.cert);
                                    }
                                });
                            }
                        } else {
                            $scope.viewState.errorKey = 'common.error.intyg.read-only.failed.load';
                        }
                    }, function(error) {
                        $scope.viewState.doneLoading = true;
                        $scope.viewState.errorKey = 'common.error.intyg.read-only.failed.load';
                    });

                } ]);
