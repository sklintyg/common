angular.module('common').directive('wcDatePickerField',
    function($rootScope, $timeout) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                targetModel: '=',
                format: '@',
                domId: '@',
                invalid: '=',
                onChange: '&',
                maxDate: '@',
                overrideRender: '='
            },
            templateUrl: '/web/webjars/common/webcert/date/wcDatePickerField.directive.html',
            require:'wcDatePickerField',
            controller: function($scope) {
/*
                $scope.$watch('targetModel', function() {
                    if ($scope.onChange) { MUST RENAME IF USED
                        $scope.onChange();
                    }
                });
*/
                if($scope.format === undefined){
                    $scope.format = 'yyyy-MM-dd';
                }

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


                this.datepickerPopupScope = {};
                this.overrideRender = $scope.overrideRender;
            },
            link: function(scope, element, attrs, ctrl) {
                if(ctrl.overrideRender) {
                    var inputChild = element.find('input');
                    ctrl.datepickerPopupScope = inputChild.isolateScope();
                }
            }
        };
    })
    .directive('wcDatePickerFieldInput', ['$log', 'common.DateUtilsService',
    function($log, dateUtils) {
        'use strict';
        return {
            priority:1,
            restrict: 'A',
            require:['ngModel', '^wcDatePickerField'],
            link: function(scope, element, attrs, ctrls) {
                var ngModel = ctrls[0];
                var wcDatePickerField = ctrls[1];
                if(wcDatePickerField.overrideRender) {
                    ngModel.$render = function() {
                        $log.info('in render!!' + ngModel.$viewValue);
                        //var date = ngModel.$viewValue ? dateFilter(ngModel.$viewValue, dateFormat) : '';
                        element.val(ngModel.$viewValue);
                        if (wcDatePickerField && wcDatePickerField.datepickerPopupScope) {
                            // now then... we need to check if the date is the correct :
                            // YYYY-MM-DD format, if not then just set the datepicker-popups date
                            // to ... today ..
                            var ppdate;
                            if(ngModel.$viewValue instanceof Date){
                                ppdate = dateUtils.toMomentStrict(ngModel.$viewValue);
                            }
                            if(dateUtils.dateReg.test(ngModel.$viewValue)){
                                ppdate = dateUtils.toMomentStrict(ngModel.$viewValue);
                            } else {
                                ppdate = new Date();
                            }
                            wcDatePickerField.datepickerPopupScope.date = ppdate;
                        }
                    };
                }
            }
        };
    }]);
