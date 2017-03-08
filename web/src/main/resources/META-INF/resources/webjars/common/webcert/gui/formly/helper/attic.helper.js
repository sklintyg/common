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

            // This $destroy is invoked whenever a form element is removed from the DOM and we want to push the
            // change to the underlying model.
            $scope.$on('$destroy', function() {
                _updateToAtticImmediate(model, optionsKey);
            });
        },
        updateToAtticImmediate: _updateToAtticImmediate
    };
}]);