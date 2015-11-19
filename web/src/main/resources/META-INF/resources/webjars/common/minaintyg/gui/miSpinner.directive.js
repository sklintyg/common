angular.module('common').directive('miSpinner',
    function() {
        'use strict';

        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            scope: {
                label: '@',
                showSpinner: '=',
                showContent: '='
            },
            templateUrl: '/web/webjars/common/minaintyg/gui/miSpinner.directive.html'
        };
    });
