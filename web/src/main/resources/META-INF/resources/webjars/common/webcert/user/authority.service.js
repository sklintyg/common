angular.module('common').factory('common.authorityService',
    [ 'common.UserModel', 'common.featureService' , function(userModel, featureService) {
        'use strict';

        function _isAuthorityActive(options) {
            var authority = options.authority;
            var feature = options.feature;
            var intygstyp = options.intygstyp;
            var role = options.role;
            var requestOrigin = options.requestOrigin;


            return  check(role, roleCheck) &&
                    check(authority, authorityCheck) &&
                    check(feature, featureCheck, intygstyp) &&
                    check(intygstyp, intygsTypCheck);
                    check(requestOrigin, requestOriginCheck);
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
            } else {
                return true;
            }
        }

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
            } else {
                return true;
            }
        }

        /**
         * Check if the current user's role has the global authorization for the specified intygstyp.
         *
         * If no intygstyp is specified, the check returns true.
         *
         * @param intygstyp
         */
        function intygsTypCheck(intygstyp) {
            if (intygstyp === undefined || intygstyp === '') {
                return true;
            }
            return userModel.hasIntygsTyp(intygstyp);
        }

        /**
         * Check where the current user has its origin.
         *
         * If no requestOrigin is specified, the check returns false.
         *
         * @param requestOrigin
         */
        function requestOriginCheck(requestOrigin){
            if (requestOrigin !== undefined && requestOrigin.length > 0) {
                if (requestOrigin.indexOf('!') === 0) {
                    // we have a not
                    requestOrigin = requestOrigin.slice(1);
                    return !userModel.hasRequestOrigin(requestOrigin);
                } else {
                    return userModel.hasRequestOrigin(requestOrigin);
                }
            } else {
                return false;
            }
        }

        return {
            isAuthorityActive: _isAuthorityActive
        };
    }]);
