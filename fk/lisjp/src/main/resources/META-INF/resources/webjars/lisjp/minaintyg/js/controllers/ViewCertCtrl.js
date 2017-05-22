angular.module('lisjp').controller('lisjp.ViewCertCtrl',
    [ '$location', '$log', '$rootScope', '$stateParams', '$scope', 'common.IntygListService',
        'common.IntygService', 'common.dialogService', 'common.messageService', 'lisjp.customizeViewstate', 'lisjp.viewConfigFactory',
        function($location, $log, $rootScope, $stateParams, $scope, listCertService, certificateService, dialogService,
            messageService, customizeViewstate, viewConfigFactory) {
            'use strict';

            $scope.cert = {};

            $scope.messageService = messageService;



            $scope.send = function() {
                $location.path('/send/lisjp/' + $stateParams.certificateId + '/FKASSA');
            };


            $scope.visibleStatuses = [ 'SENT' ];
            $scope.filterStatuses = function(statuses) {
                var result = [];
                if (!angular.isObject(statuses)) {
                    return result;
                }
                for (var i = 0; i < statuses.length; i++) {
                    if ($scope.userVisibleStatusFilter(statuses[i])) {
                        result.push(statuses[i]);
                    }
                }
                return result;
            };

            $scope.userVisibleStatusFilter = function(status) {
                for (var i = 0; i < $scope.visibleStatuses.length; i++) {
                    if (status.type === $scope.visibleStatuses[i]) {
                        return true;
                    }
                }
                return false;
            };

            $scope.showStatusHistory = function() {
                $location.path('/lisjp/statushistory');
            };

            $scope.backToViewCertificate = function() {
                $location.path('/lisjp/view/' + $stateParams.certificateId);
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
                    $scope.viewData = result.utlatande;
                    $scope.certMeta = result.meta;
                    $scope.cert.filteredStatuses = $scope.filterStatuses(result.meta.statuses);
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
