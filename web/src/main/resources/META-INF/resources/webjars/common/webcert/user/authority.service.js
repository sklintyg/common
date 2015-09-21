angular.module('common').factory('common.authorityService',
    [ 'common.UserModel', 'common.featureService' , function(userModel, featureService) {
        'use strict';

        function _isAuthorityActive(options) {
            var authority = options.authority;
            var feature = options.feature;
            var intygstyp = options.intygstyp;
            var role = options.role;


            return check(role, roleCheck) && check(authority, authorityCheck) && check(feature, featureCheck, intygstyp);
        }

        function check(toCheck, fn, intygstyp){
            var res = true;
            if(toCheck !== undefined && toCheck.length > 0){
                if(toCheck.indexOf(',') > 0){
                    var array = toCheck.split(',');
                    for(var i=0; i<array.length; i++){
                        res = fn(array[i].trim(), intygstyp);
                        if(!res){
                            // as soon as we get a false result return it
                            return false;
                        }
                    }
                } else {
                    res = fn(toCheck);
                }
            }
            return res;
        }

        function roleCheck(role){
            if (role !== undefined && role.length > 0) {

                if (role.indexOf('!') === 0) {
                    // we have a not
                    role = role.slice(1);
                    return !userModel.hasRole(role);
                } else {
                    return userModel.hasRole(role);
                }
            } else {
                return true;
            }
        };

        function authorityCheck(authority){
            if (authority !== undefined && authority.length > 0) {
                if (authority.indexOf('!') === 0) {
                    // we have a not
                    authority = authority.slice(1);
                    return !userModel.hasPrivilege(authority);
                } else {
                    return userModel.hasPrivilege(authority);
                }
            } else {
                return true;
            }
        };

        function featureCheck(feature, intygstyp){
            if (feature !== undefined && feature.length > 0) {
                if (feature.indexOf('!') === 0) {
                    // we have a not
                    feature = feature.slice(1);
                    return !featureService.isFeatureActive(feature, intygstyp);
                } else {
                    return featureService.isFeatureActive(feature, intygstyp);
                }
            } else {
                return true;
            }
        };

        return {
            isAuthorityActive: _isAuthorityActive
        };
    }]);
