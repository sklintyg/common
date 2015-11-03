/**
 * wcFieldSingle directive. Used to abstract common layout for single-line form fields in cert modules
 */
angular.module('common').directive('wcFieldSingle', ['common.messageService',
    function(messageService) {
        'use strict';

        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            scope: {
                fieldNumber: '@',
                fieldHelpText: '@',
                fieldTooltipPlacement: '@'
            },
            controller: function($scope) {

                if ($scope.fieldNumber === null) {
                    $scope.fieldNumber = undefined;
                }

                if ($scope.fieldTooltipPlacement === undefined) {
                    $scope.placement = 'right';
                } else {
                    $scope.placement = $scope.fieldTooltipPlacement;
                }

                $scope.getMessage = function(key) {
                    return messageService.getProperty(key);
                };
            },
            template: '<div class="body-row body-row-single clearfix">' +
                '<h4 class="cert-field-number" ng-if="fieldNumber != undefined">' +
                '<span message key="modules.label.field"></span> {{fieldNumber}}</h4>' +
                '<span ng-transclude></span>' +
                '<span ng-if="fieldHelpText != undefined" class="glyphicon glyphicon-question-sign" tooltip-trigger="mouseenter"' +
                'tooltip-html-unsafe="{{getMessage(fieldHelpText)}}" tooltip-placement="{{placement}}"></span>' +
                '</div>'
        };
    }]);
