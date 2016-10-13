angular.module('common').factory('common.AtticHelper', ['common.ObjectHelper', function(ObjectHelper) {
    'use strict';

    function _updateToAtticImmediate(model, optionsKey) {
        model.updateToAttic(ObjectHelper.deepGet(model.properties, optionsKey));
        model.clear(model.properties[optionsKey.split('.')[0]]);
    }

    return {
        restoreFromAttic: function(model, optionsKey) {
            if (model.isInAttic(ObjectHelper.deepGet(model.properties, optionsKey))) {
                model.restoreFromAttic(ObjectHelper.deepGet(model.properties, optionsKey));
            }
        },

        updateToAttic: function($scope, model, optionsKey) {
            $scope.$on('$destroy', function() {
                _updateToAtticImmediate(model, optionsKey);
            });
        },
        updateToAtticImmediate: _updateToAtticImmediate
    };
}]);