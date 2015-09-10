angular.module('common').directive('wcAuthority',
    ['ngIfDirective', 'common.User', 'common.PrivilegeService', 'common.featureService',
        function(ngIfDirective, userService, privilegeService, featureService) {
            'use strict';
            var ngIf = ngIfDirective[0];

            return {
                transclude: ngIf.transclude,
                priority: ngIf.priority,
                terminal: ngIf.terminal,
                restrict: ngIf.restrict,
                link: function($scope, $element, $attr) {
                    var selfArguments = arguments;
                    var authority = $attr.wcAuthority;
                    var feature = $attr.feature
                    //var authority = $scope.$eval(value);


                    // find the initial ng-if attribute
                    var initialNgIf = $attr.ngIf, ifEvaluator;

                    var pres = true;
                    if (authority !== undefined && authority.length > 0) {
                        pres = privilegeService.hasPrivilege(authority);
                    }
                    var fres = true;
                    if (feature !== undefined && feature.length > 0) {
                        if (feature.indexOf('!') === 0) {
                            // we have a not
                            feature = feature.slice(1);
                            fres = !featureService.isFeatureActive(feature, $attr.intygstyp);
                        } else {
                            fres = featureService.isFeatureActive(feature, $attr.intygstyp);
                        }
                    }

                    // if it exists, evaluates ngIf && ifAuthenticated
                    if (initialNgIf) {
                        ifEvaluator = function() {
                            return $scope.$eval(initialNgIf) && pres && fres;
                        };
                    } else { // if there's no ng-if, process normally
                        ifEvaluator = function() {
                            return pres && fres;
                        };
                    }
                    $attr.ngIf = ifEvaluator;
                    ngIf.link.apply(ngIf, selfArguments);

                }
            };
        }]);
