angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'single-text',
        templateUrl: '/web/webjars/common/webcert/gui/formly/singleText.formly.html',
        controller: ['$scope', 'common.ObjectHelper', function($scope, ObjectHelper) {
            var model = $scope.model,
                optionsKey = $scope.options.key,
                get = ObjectHelper.deepGet;

            if(!$scope.to.labelColSize) {
                $scope.to.labelColSize = 5;
            }
            if(!$scope.to.indent) {
                $scope.to.indent = false;
            }

            // Restore data model value form attic if exists
            if (model.isInAttic(get(model.properties, optionsKey))) {
                model.restoreFromAttic(get(model.properties, optionsKey));
            }

            // Setup watch on model value and update attic model accordingly
            var atticWatch = $scope.$watch(
                function () {
                    return get(model, optionsKey);
                },
                function (newVal, oldVal) {
                    if (newVal === oldVal) { return; }
                    if (!ObjectHelper.isEmpty(newVal)) {
                        model.updateToAttic(get(model.properties, optionsKey));
                    } else {
                        model.clear(model.properties[optionsKey.split('.')[0]]);
                    }
                }
            );

            // Clear attic model and destroy watch on scope destroy
            $scope.$on('$destroy', function () {
                model.updateToAttic(get(model.properties, optionsKey));
                model.clear(model.properties[optionsKey.split('.')[0]]);
                atticWatch();
            });
        }]
    });

});
