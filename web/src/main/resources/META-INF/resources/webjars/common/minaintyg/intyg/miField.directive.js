angular.module('common').directive('miField',
    function() {
        'use strict';

        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            scope: {
                fieldLabel: '@',
                filled: '=?'
            },
            templateUrl: '/web/webjars/common/minaintyg/intyg/miField.directive.html'
        };
    });
