angular.module('common').directive('wcRole',['ngIfDirective', 'common.User',
    function(ngIfDirective, userService) {
        var ngIf = ngIfDirective[0];

        return {
            transclude: ngIf.transclude,
            priority: ngIf.priority,
            terminal: ngIf.terminal,
            restrict: ngIf.restrict,
            link: function($scope, $element, $attr) {
                var selfArguments = arguments;
                var value = $attr['wcRole'];
                var yourCustomValue = $scope.$eval(value);

                var userPromise = userService.initUser();

                userPromise.then(function(){
                    // find the initial ng-if attribute
                    var initialNgIf = $attr.ngIf, ifEvaluator;
                    // if it exists, evaluates ngIf && ifAuthenticated
                    if (initialNgIf) {
                        ifEvaluator = function () {
                            return $scope.$eval(initialNgIf) && yourCustomValue;
                        };
                    } else { // if there's no ng-if, process normally
                        ifEvaluator = function () {
                            return yourCustomValue;
                        };
                    }
                    $attr.ngIf = ifEvaluator;
                    ngIf.link.apply(ngIf, selfArguments);
                });
            }
        };
    }]);
