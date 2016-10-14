angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'underlag',
        templateUrl: '/web/webjars/common/webcert/gui/formly/underlag.formly.html',
        controller: ['$scope', 'common.dynamicLabelService', 'common.ObjectHelper', '$timeout',
        function($scope, dynamicLabelService, objectHelper, $timeout) {

            var chooseOption = {
                id: null,
                label: 'Välj...'
            };

            $scope.underlagOptions = [];
            var underlag = $scope.model[$scope.options.key];

            $scope.previousUnderlagIncomplete = function() {
                var prev = underlag[underlag.length - 1];
                return objectHelper.isEmpty(prev.typ) || objectHelper.isEmpty(prev.datum) || objectHelper.isEmpty(prev.hamtasFran);
            };

            function updateUnderlag() {
                $scope.underlagOptions = [chooseOption];

                if ($scope.to.underlagsTyper) {
                    $scope.to.underlagsTyper.forEach(function (underlagsTyp) {
                        $scope.underlagOptions.push({
                            'id': underlagsTyp,
                            'label': dynamicLabelService.getProperty('KV_FKMU_0005.' + underlagsTyp + '.RBK')
                        });
                    });
                }
            }

            $scope.$on('dynamicLabels.updated', function() {
                updateUnderlag();
            });

            updateUnderlag();
        }]
    });

});
