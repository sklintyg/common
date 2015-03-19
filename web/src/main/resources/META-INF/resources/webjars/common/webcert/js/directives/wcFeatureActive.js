angular.module('common').directive('wcFeatureActive',
    [ 'common.User', 'common.featureService', function(User, featureService) {
        'use strict';

        return {
            restrict: 'A',
            link: function(scope, element, attrs) {

                // Make sure that the attribute 'feature' is set.
                if (attrs.feature === undefined) {
                    element.remove();
                    throw new Error('\'wcFeatureActive\' kräver att du har angett attributet \'feature\' för att fungera.');
                }

                if (!featureService.isFeatureActive(attrs.feature, attrs.intygstyp)) {
                    element.remove();
                }
            }
        };
    }]);
