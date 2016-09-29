/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
                minDate: '@',
                overrideRender: '=',
                addDateParser: '@',
                dateOptions: '@',
                onBlur: '&'
            },
            templateUrl: '/web/webjars/common/webcert/date/wcDatePickerField.directive.html',
            require:'wcDatePickerField',
            controller: function($scope) {

                $scope.dateOptions = {
                        minDate: undefined,
                        maxDate: undefined
                };

                if ($scope.minDate !== undefined) {
                    $scope.dateOptions.minDate = new Date($scope.minDate);
                }
                
                if ($scope.maxDate !== undefined) {
                    $scope.dateOptions.maxDate = new Date($scope.maxDate);
                }

                if($scope.format === undefined){
                    $scope.format = 'yyyy-MM-dd';
                }


                $scope.isOpen = false;
                $scope.toggleOpen = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $timeout(function() {
                        $scope.isOpen = !$scope.isOpen;
                    });
                };

                $scope.onDatepickerInputFieldBlur = function() {
                    $scope.onBlur();
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
        var checkDate = function(date1, date2){
        	if (!date1 || !date2) {
                // consider empty models to be valid
                return true;
            }

            var mdate1 = moment(date1);
            var mdate2 = moment(date2);

            if(!mdate1.isValid() || !mdate2.isValid()) {
                return true;
            }
            return !moment(mdate2).isAfter(mdate1);
        };
        return {
            priority:10,
            restrict: 'A',
            require:['ngModel', '^wcDatePickerField'],
            link: function(scope, element, attrs, ctrls) {

                var ngModel = ctrls[0];
                var wcDatePickerField = ctrls[1];
                if(wcDatePickerField.overrideRender) {
                    var getDate = function getDate(date){
                        // now then... we need to check if the date is the
						// correct :
                        // YYYY-MM-DD format, if not then just set the
						// datepicker-popups date
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
                        element.val(ngModel.$viewValue);
                        if (wcDatePickerField && wcDatePickerField.datepickerPopupScope) {
                            wcDatePickerField.datepickerPopupScope.date = getDate(ngModel.$viewValue);
                        }
                    };

                    // the bind event on the input text box is just setting the
					// date to the model value
                    // we need to intercept this and make sure :
                    // it's today, on an invalid date
                    // it's the date, if the date is valid
                    // but first we need to remove the event listeners for
					// ui-bootstrap-tpls.js datepicker
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
                        dateUtils.addLooseDateParser(ngModel);
                    }
                    else if (scope.addDateParser === 'strict') {
                        dateUtils.addStrictDateParser(ngModel);
                    }
                    else {
                        $log.error('unknown dateparser method ' + scope.addDateParser);
                    }
                }

                var maximumDate = '2099-12-12';
                var minimumDate = '1900-01-01';

                ngModel.$validators.maxDate = function() {
                    return checkDate(maximumDate, ngModel.$viewValue);
                };

                ngModel.$validators.minDate = function() {
                    return checkDate(ngModel.$viewValue, minimumDate);
                };

            }
        };
    }]);
