angular.module('common').factory('common.PrivilegeService',
    [ 'common.UserModel', function(UserModel) {
        'use strict';
        var _privileges = {
            PRIVILEGE_SIGNERA_INTYG : 'PRIVILEGE_SIGNERA_INTYG',
            PRIVILEGE_VIDAREBEFORDRA_UTKAST : 'PRIVILEGE_VIDAREBEFORDRA_UTKAST',
            PRIVILEGE_VIDAREBEFORDRA_FRAGASVAR : 'PRIVILEGE_VIDAREBEFORDRA_FRAGASVAR',
            PRIVILEGE_MAKULERA_INTYG : 'PRIVILEGE_MAKULERA_INTYG',
            PRIVILEGE_KOPIERA_INTYG : 'PRIVILEGE_KOPIERA_INTYG',
            PRIVILEGE_BESVARA_KOMPLETTERINGSFRAGA : 'PRIVILEGE_BESVARA_KOMPLETTERINGSFRAGA',
            PRIVILEGE_SKRIVA_INTYG : 'PRIVILEGE_SKRIVA_INTYG'
        };

        function _hasPrivilege(privilege) {
            if(UserModel.user && UserModel.user.authorities){
                return UserModel.user.authorities.indexOf(privilege) > -1;
            } else {
                return false;
            }
        }

        return {
            hasPrivilege : _hasPrivilege
        };
    }]);
