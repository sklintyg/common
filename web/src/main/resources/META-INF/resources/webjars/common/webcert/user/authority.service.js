angular.module('common').factory('common.authorityService',
    [ 'common.UserModel', 'common.featureService' , function(userModel, featureService) {
        'use strict';

        function _isAuthorityActive(options) {
            var authority = options.authority;
            var feature = options.feature;
            var intygstyp = options.intygstyp;
            var role = options.role;

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
                    fres = !featureService.isFeatureActive(feature, intygstyp);
                } else {
                    fres = featureService.isFeatureActive(feature, intygstyp);
                }
            }

            return rres && pres && fres;
        }

        return {
            isAuthorityActive: _isAuthorityActive
        };
    }]);
