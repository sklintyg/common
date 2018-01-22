angular.module('common').directive('ueCheckboxDate', [ '$timeout', 'common.DateUtilsService', 'common.dynamicLabelService',
    'common.AtticHelper', 'common.UtkastValidationService', 'common.UtkastViewStateService',
    function($timeout, dateUtils, dynamicLabelService, AtticHelper, UtkastValidationService, UtkastViewState) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueCheckboxDate/ueCheckboxDate.directive.html',
            link: function($scope) {

                $scope.validation = UtkastViewState.validation;

                // Restore data model value form attic if exists
                AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

                // Clear attic model and destroy watch on scope destroy
                AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

                $scope.checkbox = {};

                $scope.$watch('model.' + $scope.config.modelProp, function(newVal, oldVal) {
                    if (newVal) {
                        $scope.checkbox.checked = true;
                    } else {
                        $scope.checkbox.checked = false;
                    }
                });

                $scope.$watch('checkbox.checked', function(newVal, oldVal) {
                    if (newVal) {
                        if (!$scope.model[$scope.config.modelProp]) {
                            $scope.model[$scope.config.modelProp] = dateUtils.todayAsYYYYMMDD();
                            $scope.validate();
                        }
                    } else if (oldVal !== undefined) {
                        // Clear date if check is unchecked
                        if ($scope.model[$scope.config.modelProp] !== undefined) {
                            $scope.model[$scope.config.modelProp] = undefined;
                            $scope.validate();
                        }
                    }
                });

                $scope.getDynamicText = function(key) {
                    return dynamicLabelService.getProperty(key);
                };

                $scope.validate = function() {
                    // When a date is selected from the date popup a blur event is sent.
                    // In the current version of Angular UI this blur event is sent before utkast model is updated
                    // This timeout ensures we get the new value in $scope.model
                    $timeout(function() {
                        UtkastValidationService.validate($scope.model);
                    });
                };
            }
        };
    }]);

