angular.module('common').directive('wcSpinner',
    ['$timeout','$window', function($timeout, $window) {
        'use strict';

        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            scope: {
                label: '@',
                showSpinner: '=',
                showContent: '=',
                isHeader: '='
            },
            templateUrl: '/web/webjars/common/webcert/gui/wcSpinner.directive.html',
            link: {
                pre : function (scope, element){
                    $window.rendered = false;
                },
                post : function (scope, element){
                    scope.$watch('showContent', function(newVal, oldVal){
                        if (newVal !== oldVal && newVal && !scope.isHeader) {
                            $timeout(function(){
                                $window.rendered = true;
                                //console.log('#saknas-lista is visible:' + $('#visa-vad-som-saknas-lista').is(':visible'));
                            });
                        } else if(!newVal){
                            $window.rendered = false;
                        }
                    });
                }
            }
        };
    }]);
