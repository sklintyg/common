angular.module('common').directive('wcAuthority',
    ['common.UserModel', 'common.featureService',
        function(userModel, featureService) {
            'use strict';
            return {
                restrict: 'A',
                link: function($scope, $element, $attr) {
                    var authority = $attr.wcAuthority;
                    var feature = $attr.feature

                    console.log('feature : ' + feature + ', authority : ' + authority);

                    var pres = true;
                    if (authority !== undefined && authority.length > 0) {
                        pres = userModel.hasPrivilege(authority);
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

                    console.log('fres : ' + fres + ', pres : ' + pres + ', =' + !(pres && fres));

                    if(!(pres && fres)){
                        console.log('remove');
                        $element.remove();
                    }
                }
            };
        }]);
