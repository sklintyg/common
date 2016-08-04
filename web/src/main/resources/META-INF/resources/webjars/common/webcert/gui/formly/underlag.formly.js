angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'underlag',
        templateUrl: '/web/webjars/common/webcert/gui/formly/underlag.formly.html',
        controller: ['$scope', 'common.dynamicLabelService', 'common.ObjectHelper', function($scope, dynamicLabelService, objectHelper) {

            if (!$scope.to.maxUnderlag) {
                $scope.to.maxUnderlag = 10;
            }

            $scope.underlagOptions = [{
                id: null,
                label: 'Ange utredning eller underlag'
            }];

            function updateUnderlag() {
                $scope.underlagOptions = [{
                    id: null,
                    label: 'Ange utredning eller underlag'
                }];
                if ($scope.to.underlagsTyper) {
                    $scope.to.underlagsTyper.forEach(function (underlagsTyp) {
                        $scope.underlagOptions.push({
                            'id': underlagsTyp,
                            'label': dynamicLabelService.getProperty('KV_FKMU_0005.' + underlagsTyp + '.RBK')
                        });
                    });
                }

                $scope.createUnderlag = function() {
                    $scope.model[$scope.options.key].push({ typ: null, datum: null, hamtasFran: null });
                    $scope.form.$setDirty();
                };

                $scope.removeUnderlag = function(index) {
                    $scope.model[$scope.options.key].splice(index, 1);
                    $scope.form.$setDirty();
                };

                $scope.previousUnderlagIncomplete = function() {
                    var prev = $scope.model.underlag[$scope.model.underlag.length - 1];
                    return objectHelper.isEmpty(prev.typ) || objectHelper.isEmpty(prev.datum) || objectHelper.isEmpty(prev.hamtasFran);
                }
            }

            $scope.$on('dynamicLabels.updated', function() {
                updateUnderlag();
            });

            updateUnderlag();
        }]
    });

});
