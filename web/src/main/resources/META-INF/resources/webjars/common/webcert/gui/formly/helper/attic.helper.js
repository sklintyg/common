angular.module('common').factory('common.AtticHelper', [function() {
    'use strict';

    function _updateToAtticImmediate(model, optionsKey) {
        model.updateToAttic(optionsKey);
        model.clear(optionsKey);
    }

    return {
        restoreFromAttic: function(model, optionsKey) {
            if (model.isInAttic(optionsKey)) {
                model.restoreFromAttic(optionsKey);
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