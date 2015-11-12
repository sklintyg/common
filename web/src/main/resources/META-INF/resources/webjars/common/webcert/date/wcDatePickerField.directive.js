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
                overrideRender: '=',
                addDateParser: '@'
            },
            templateUrl: '/web/webjars/common/webcert/date/wcDatePickerField.directive.html',
            require:'wcDatePickerField',
            controller: function($scope) {

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
    function($log, dateUtils ) {
        'use strict';
        return {
            priority:10,
            restrict: 'A',
            require:['ngModel', '^wcDatePickerField'],
            link: function(scope, element, attrs, ctrls) {
                var ngModel = ctrls[0];
                var wcDatePickerField = ctrls[1];
                if(wcDatePickerField.overrideRender) {
                    var getDate = function getDate(date){
                        // now then... we need to check if the date is the correct :
                        // YYYY-MM-DD format, if not then just set the datepicker-popups date
                        // to ... today ..
                        var ppdate;
                        if(date instanceof Date){
                            ppdate = dateUtils.toMomentStrict(date);
                        }
                        if(dateUtils.dateReg.test(date)){
                            ppdate = dateUtils.toMomentStrict(date);
                        } else {
                            ppdate = new Date();
                        }
                        return ppdate;
                    };
                    ngModel.$render = function() {
                        //var date = ngModel.$viewValue ? dateFilter(ngModel.$viewValue, dateFormat) : '';
                        element.val(ngModel.$viewValue);
                        if (wcDatePickerField && wcDatePickerField.datepickerPopupScope) {
                            wcDatePickerField.datepickerPopupScope.date = getDate(ngModel.$viewValue);
                        }
                    };

                    // the bind event on the input text box is just setting the date to the model value
                    // we need to intercept this and make sure :
                    // it's today, on an invalid date
                    // it's the date, if the date is valid
                    // but first we need to remove the event listeners for ui-bootstrap-tpls.js datepicker
                    // this little monster listening can be found at ln 1519
                    //
                    // UPDATED for UI Bootstrap 0.14.3 now at row 2234
                    ngModel.$viewChangeListeners.pop();
                    ngModel.$viewChangeListeners.push(function() {
                        wcDatePickerField.datepickerPopupScope.date = getDate(ngModel.$viewValue);
                    });
                }

                if (scope.addDateParser) {
                    if (scope.addDateParser === 'loose') {
                        dateUtils.addDateParserFormatter(ngModel);
                    }
                    else {
                        $log.error('unknown dateparser method ' + scope.addDateParser);
                    }
                }
            }
        };
    }]);
