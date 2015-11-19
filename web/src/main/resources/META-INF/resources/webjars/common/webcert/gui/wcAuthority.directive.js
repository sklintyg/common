angular.module('common').directive('wcAuthority',
    ['common.authorityService',
        function(authorityService) {
            'use strict';
            return {
                restrict: 'A',
                link: function($scope, $element, $attr) {
                    var options = {
                        authority: $attr.wcAuthority,
                        feature: $attr.feature,
                        role: $attr.role,
                        intygstyp: $attr.intygstyp
                    };

                    if (!authorityService.isAuthorityActive(options)) {
                        $element.remove();
                    }
                }
            };
        }]);
