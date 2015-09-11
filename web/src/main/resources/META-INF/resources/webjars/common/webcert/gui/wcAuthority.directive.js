angular.module('common').directive('wcAuthority',
    ['common.UserModel', 'common.featureService',
        function(userModel, featureService) {
            'use strict';
            return {
                restrict: 'A',
                link: function($scope, $element, $attr) {
                    var authority = $attr.wcAuthority;
                    var feature = $attr.feature;
                    var role = $attr.role;

                    //console.log('feature : ' + feature + ', authority : ' + authority);

                    var rres = true;
                    if(role !== undefined && role.length > 0){
                        if (role.indexOf('!') === 0) {
                            // we have a not
                            role = role.slice(1);
                            rres = !userModel.hasRole(role);
                        } else {
                            rres = userModel.hasRole(role);
                        }
                    }

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

                    //console.log('fres : ' + fres + ', pres : ' + pres + ', =' + !(pres && fres));

                    if(!(rres && pres && fres)){
                        //console.log('remove');
                        $element.remove();
                    }
                }
            };
        }]);
