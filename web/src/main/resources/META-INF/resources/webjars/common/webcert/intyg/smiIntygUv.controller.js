angular.module('common').controller('smi.ViewCertCtrlUv',
    [ '$log', '$rootScope', '$stateParams', '$scope', '$state', 'common.IntygProxy',
        'common.UserModel', 'ViewState',
        'ViewConfigFactory', 'common.dynamicLabelService', 'common.IntygViewStateService', 'uvUtil',
        function($log, $rootScope, $stateParams, $scope, $state, IntygProxy,
            UserModel, ViewState, viewConfigFactory, DynamicLabelService, IntygViewStateService, uvUtil) {
            'use strict';

            ViewState.reset();
            $scope.viewState = ViewState;

            // Check if the user used the special qa-link to get here.
            if ($stateParams.qaOnly) {
                $scope.isQaOnly = true;
            }

            // Page setup
            $scope.user = UserModel;

            ViewState.intygModel = {};
            ViewState.intygModel.filledAlways = true;
            $scope.cert = undefined;

            $scope.uvConfig = viewConfigFactory.getViewConfig(true);

            angular.forEach($scope.uvConfig, function(category) {
                if (category.labelKey) {
                    var fields = uvUtil.getModelProps(category);
                    angular.forEach(fields, function(field) {
                        IntygViewStateService.setCategoryField(category.labelKey, field);
                    });
                }
            });

            /**
             * Private
             */
            function loadIntyg() {
                $log.debug('Loading intyg ' + $stateParams.certificateId);
                IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intygProperties.type, function(result) {
                    ViewState.common.doneLoading = true;
                    if (result !== null && result !== '') {
                        ViewState.intygModel = result.contents;
                        ViewState.relations = result.relations;

                        DynamicLabelService.updateDynamicLabels(ViewState.common.intygProperties.type, ViewState.intygModel.textVersion);

                        if(ViewState.intygModel !== undefined && ViewState.intygModel.grundData !== undefined){
                            ViewState.enhetsId = ViewState.intygModel.grundData.skapadAv.vardenhet.enhetsid;
                        }

                        ViewState.common.updateIntygProperties(result);

                        $scope.pdfUrl = '/moduleapi/intyg/'+ ViewState.common.intygProperties.type +'/' + ViewState.intygModel.id + '/pdf';

                        $scope.cert = result.contents;
                        $rootScope.$emit('ViewCertCtrl.load', ViewState.intygModel, ViewState.common.intygProperties);
                        $rootScope.$broadcast('intyg.loaded', ViewState.intygModel);

                    } else {
                        $rootScope.$emit('ViewCertCtrl.load', null, null);

                        if ($stateParams.signed) {
                            ViewState.common.activeErrorMessageKey = 'common.error.sign.not_ready_yet';
                        } else {
                            ViewState.common.activeErrorMessageKey = 'common.error.could_not_load_cert';
                        }
                    }
                    $scope.intygBackup.showBackupInfo = false;
                }, function(error) {
                    $rootScope.$emit('ViewCertCtrl.load', null, null);
                    ViewState.common.doneLoading = true;
                    ViewState.common.updateActiveError(error, $stateParams.signed);
                    $scope.intygBackup.showBackupInfo = true;
                });
            }
            loadIntyg();

            /**
             * Event response from QACtrl which sends its intyg-info here in case intyg couldn't be loaded but QA info could.
             * @type {{}}
             */
            $scope.intygBackup = {intyg: null, showBackupInfo: false};
            var unbindFastEventFail = $rootScope.$on($state.current.data.intygType+'.ViewCertCtrl.load.failed', function(event, intyg) {
                $scope.intygBackup.intyg = intyg;
            });
            $scope.$on('$destroy', unbindFastEventFail);

            $scope.$on('loadCertificate', loadIntyg);

		}]);
