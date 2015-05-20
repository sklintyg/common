angular.module('common').directive('wcDatePickerField',
    function($rootScope, $timeout) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                targetModel: '=',
                domId: '@',
                invalid: '=',
                onChange: '&',
                maxDate: '@'
            },
            templateUrl: '/web/webjars/common/webcert/date/wcDatePickerField.directive.html',
            controller: function($scope) {
/*
                $scope.$watch('targetModel', function() {
                    if ($scope.onChange) { MUST RENAME IF USED
                        $scope.onChange();
                    }
                });
*/
                if($scope.maxDate === undefined){
                    $scope.maxDate = null;
                } else {
                    $scope.maxDate = '\'' + $scope.maxDate + '\'';
                }

                $scope.isOpen = false;
                $scope.toggleOpen = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $timeout(function() {
                        $scope.isOpen = !$scope.isOpen;
                    });
                };
            }
        };
    });
