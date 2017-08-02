angular.module('fk7263').controller('fk7263.CustomizePdfCtrl',
    [ '$window', '$location', '$log', '$rootScope', '$state', '$stateParams', '$scope','fk7263.customizeViewstate', 'common.IntygListService',
        'common.IntygService', 'common.dialogService',
        function($window, $location, $log, $rootScope, $state, $stateParams, $scope, customizeViewstate, listCertService, certificateService, dialogService) {
            'use strict';


            $scope.customizeViewstate = customizeViewstate;
            $scope.downloadAsPdfLink = '/moduleapi/certificate/fk7263' + '/' + $stateParams.certificateId + '/pdf/arbetsgivarutskrift';

            certificateService.getCertificate('fk7263', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.certMeta = result.meta;
                } else {
                    // show error view
                    $location.path('/fk7263/visafel/certnotfound');
                }
            }, function() {
                $log.debug('got error');
                $location.path('/fk7263/visafel/certnotfound');
            });

            var dialogInstance;
            var leaveToState, leaveToParams = null;
            var leaveConfirmed = false;
            // Cleanup ---------------
            $scope.$on('$destroy', function() {
                if (dialogInstance) {
                    dialogInstance.close();
                    dialogInstance = undefined;
                }
            });
            function _showLeaveConfirmationDialog() {
                dialogInstance = dialogService.showDialog($scope, {
                    dialogId: 'confirm-leave-customize-pdf-dialog',
                    titleId: 'modules.customize.summary.leave-dialog.header',
                    bodyTextId: 'modules.customize.summary.leave-dialog.body',
                    button1click: function() {
                        //perform navigation
                        leaveConfirmed = true;
                        $state.go(leaveToState, leaveToParams);
                    },
                    button1id: 'leave-button',
                    button1text: 'modules.customize.summary.leave-dialog.button.confirm',
                    button1icon: 'icon-ok',
                    button2text: 'modules.customize.summary.leave-dialog.button.cancel',
                    button2icon: 'icon-cancel',
                    button2class: 'btn-secondary',
                    autoClose: false
                });
            }
            $scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams, options) {
                if (toState.name.indexOf('fk7263-customize')!==-1 || leaveConfirmed) {
                    return;
                }

                // defer this navigation until confirmed in dialog
                event.preventDefault();
                leaveToState = toState;
                leaveToParams = toParams;
                _showLeaveConfirmationDialog();
                //state history gets mixed when when preventing default. Avoid this by going to the current state.
                $state.go(fromState, fromParams);
            });


            $scope.backToViewCertificate = function() {
                customizeViewstate.resetModel();
                $location.path('/fk7263/view/' + $stateParams.certificateId);
            };




            //Fishbone state --------------------------------------
            $scope.stepModel =[
                {labelKey:'modules.customize.fishbone.step1', active: true},
                {labelKey:'modules.customize.fishbone.step2'},
                {labelKey:'modules.customize.fishbone.step3'}
            ];
            $scope.$on('$stateChangeSuccess', function(event, toState) {
                angular.forEach($scope.stepModel, function(step, index) {
                    step.active = (toState.data.index === index);
                });

            });


            //Download handling ------------------------

            function _addInput(name, item) {
                return '<input type="hidden" name="' + name + '" value="' + item + '" />';
            }

            $scope.submit = function() {
                var inputs = '';
                var fields = customizeViewstate.getSendModel();

                angular.forEach(fields, function(item) {
                    inputs += _addInput('selectedOptionalFields', item);
                });

                //send request via temporary added form and remove from dom directly
                $window.jQuery('<form action="' + $scope.downloadAsPdfLink + '" target="_blank" method="post">' + inputs + '</form>')
                    .appendTo('body').submit().remove();

                $scope.downloadSuccess = true;
            };

        }]);
