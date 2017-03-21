angular.module('lisjp').controller('lisjp.CustomizePdfSummaryCtrl',
    [ '$window', '$location', '$log', '$rootScope', '$stateParams', '$scope', 'lisjp.customizeViewstate', 'common.IntygListService',
        'common.IntygService', 'common.dialogService', 'common.messageService', 'common.moduleService',
        function($window, $location, $log, $rootScope, $stateParams, $scope, customizeViewstate, listCertService, certificateService, dialogService,
            messageService, moduleService) {
            'use strict';

            $scope.customizeViewstate = customizeViewstate;
            $scope.downloadAsPdfLink = '/moduleapi/certificate/lisjp' + '/' + $stateParams.certificateId + '/pdf/arbetsgivarutskrift';
            $scope.downloadSuccess = false;

            certificateService.getCertificate('lisjp', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $rootScope.cert = $scope.cert;
                } else {
                    // show error view
                    $location.path('/lisjp/visafel/certnotfound');
                }
            }, function() {
                $log.debug('got error');
                $location.path('/lisjp/visafel/certnotfound');
            });

            $scope.backToCustomizeCertificate = function() {
                $location.path('/lisjp/customize/' + $stateParams.certificateId);
            };

            $scope.backToInbox = function() {
                $location.path('/web/start/#/');
            };


            //Handle leave wizard:
            $scope.leaveSummaryPage = null;
            $scope.toState = null;

            $scope.onLeaveSummaryPage = function() {
                $scope.leaveSummaryPage = true;
                $location.path($scope.toState.url);
            };

            function _onStateChangeStart(event, toState, toParams, fromState, fromParams, options) {
                // continue with event when leaveSummaryPage variable is set
                if ($scope.leaveSummaryPage) {
                    return;
                }

                // continue with event when navigation is back to step 1
                if (toParams.certificateId === fromParams.certificateId) {
                    return;
                }

                // prevent this event and display modal dialog
                event.preventDefault();
                $scope.toState = toState;
                _showDialog();
            }

            $scope.$on('$stateChangeStart', _onStateChangeStart);

            function _showDialog() {
                dialogService.showDialog($scope, {
                    dialogId: 'lisjp-customize-summary-dialog',
                    templateUrl: '/web/webjars/lisjp/minaintyg/views/customize-pdf-leave-dialog.html',
                    autoClose: true
                });
            }


            //Printing related
            // Submit user selected fields to PDF generator

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
