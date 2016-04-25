angular.module('common').factory('common.AtticHelper',
    ['common.ObjectHelper', function(ObjectHelper) {
        'use strict';

        return {
            restoreFromAttic: function(model, optionsKey) {
                if (model.isInAttic(ObjectHelper.deepGet(model.properties, optionsKey))) {
                    model.restoreFromAttic(ObjectHelper.deepGet(model.properties, optionsKey));
                }
            },

            updateToAttic: function($scope, model, optionsKey) {
                var atticWatch = $scope.$watch(
                        function () {
                            return ObjectHelper.deepGet(model, optionsKey);
                        },
                        function (newVal, oldVal) {
                            if (newVal === oldVal) { return; }
                            if (!ObjectHelper.isEmpty(newVal)) {
                                model.updateToAttic(ObjectHelper.deepGet(model.properties, optionsKey));
                            } else {
                                model.clear(model.properties[optionsKey.split('.')[0]]);
                            }
                        }
                );

                $scope.$on('$destroy', function () {
                    model.updateToAttic(ObjectHelper.deepGet(model.properties, optionsKey));
                    model.clear(model.properties[optionsKey.split('.')[0]]);
                    atticWatch();
                });
            }
        };
    }]);
