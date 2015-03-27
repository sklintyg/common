angular.module('common').directive('wcToUppercase', function() {
    'use strict';
    return{
        // ngModel controller, so we need this!
        require: 'ngModel',
        restrict: 'A',
        link: function($scope, $element, $attr, $ngModel){

            var dateFormat = 'yyyy-mm-dd';
            $ngModel.$parsers.push(function (viewValue)
            {
                //convert date into a string
                if(typeof viewValue == Date){
                    return $filter('date')(new Date(viewValue), dateFormat);
                } else {
                    var pDate = Date.parse(viewValue);
                    if (isNaN(pDate) === false) {
                        return $filter('date')(new Date(pDate), dateFormat);
                    }
                }

                return undefined;

            });
            $ngModel.$formatters.push(function (modelValue)
            {
                var pDate = Date.parse(modelValue);
                if (isNaN(pDate) === false) {
                    return $filter('date')(new Date(pDate), dateFormat);
                }
                return undefined;
            });
        }
    };
});
