angular.module('common').directive('wcToUppercase', function() {
    'use strict';
    return{
        // ngModel controller, so we need this!
        require: 'ngModel',
        restrict: 'A',
        link: function(scope, element, attr, ngModel){

            ngModel.$parsers.unshift(function(viewValue)
            {
                var val = (viewValue || '').toUpperCase();
                element.val(val);
                return val;
            });
            ngModel.$formatters.unshift(function()
            {
                if(!ngModel.$modelValue) {
                    return;
                }
                return ngModel.$modelValue.toUpperCase();
            });

        }
    };
});
