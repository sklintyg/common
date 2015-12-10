angular.module('common').factory('common.authorityService',
    [ 'common.UserModel', 'common.featureService' , function(userModel, featureService) {
        'use strict';

        function _isAuthorityActive(options) {
            var feature = options.feature;
            var role = options.role;
            var authority = options.authority;
            var requestOrigin = options.requestOrigin;
            var intygstyp = options.intygstyp;

            var a = check(feature, featureCheck, intygstyp);
            var b = check(role, roleCheck);
            var c = check(authority, privilegeCheck, intygstyp);
            var d = check(requestOrigin, requestOriginCheck, intygstyp);

            return a && b && c && d;
/*
            return  check(feature, featureCheck, intygstyp) &&
                    check(role, roleCheck) &&
                    check(authority, privilegeCheck, intygstyp) &&
                    check(requestOrigin, requestOriginCheck, intygstyp);
*/
        }

        function checkEach(toCheck, fn, intygstyp) {
            var result = true;
            var array = toCheck.split(',');
            for(var i = 0; i < array.length; i++){
                result = fn(array[i].trim(), intygstyp);
                if(!result){
                    // as soon as we get a false result return it
                    return false;
                }
            }

            return true;
        }

        function check(toCheck, fn, intygstyp){
            var res = true;
            if(toCheck !== undefined && toCheck.length > 0) {
                if(toCheck.indexOf(',') > 0){
                    res = checkEach(toCheck, fn, intygstyp);
                } else {
                    res = fn(toCheck, intygstyp);
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
            }

            return true;
        }

        function privilegeCheck(privilege, intygstyp) {
            if (privilege !== undefined && privilege.length > 0) {
                if (privilege.indexOf('!') === 0) {
                    // we have a not
                    privilege = privilege.slice(1);
                    return !userModel.hasPrivilege(privilege, intygstyp);
                } else {
                    return userModel.hasPrivilege(privilege, intygstyp);
                }
            }

            return true;
        }

        function featureCheck(feature, intygstyp){
            if (feature !== undefined && feature.length > 0) {
                if (feature.indexOf('!') === 0) {
                    // we have a not
                    feature = feature.slice(1);
                    return !featureService.isFeatureActive(feature, intygstyp);
                } else {
                    return featureService.isFeatureActive(feature, intygstyp);
                }
            }

            return true;
        }

        /**
         * Check if the current user has authorization for the specified intygstyp.
         *
         * If no intygstyp is specified, the check returns true.
         *
         * @param intygstyp
         */
/*
        function intygsTypCheck(intygstyp) {
            if (intygstyp === undefined || intygstyp === '') {
                return true;
            }

            return userModel.hasIntygsTyp(intygstyp);
            //return userModel.hasIntygsTyp(intygstyp);
        }
*/

        /**
         * Check the current user's origin.
         *
         * 1. Om requestOrigin finns måste användaren ha den
         * 2. Om intygstyp finns måste användaren's request origin stödja den
         *    såvida inte användarens request origin har några begränsningar
         *
         * @param requestOrigin
         * @param intygsTyp
         */
        function requestOriginCheck(requestOrigin, intygsTyp){
            if (requestOrigin !== undefined && requestOrigin.length > 0) {
                if (requestOrigin.indexOf('!') === 0) {
                    // we have a not
                    requestOrigin = requestOrigin.slice(1);
                    return !userModel.hasRequestOrigin(requestOrigin, intygsTyp);
                } else {
                    return userModel.hasRequestOrigin(requestOrigin, intygsTyp);
                }
            }

            return true;
        }

        return {
            isAuthorityActive: _isAuthorityActive
        };
    }]);
