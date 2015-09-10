angular.module('common').factory('common.PrivilegeService',
    [ 'common.UserModel', function(UserModel) {
        'use strict';
        // these enums are just copies from the backend an ment as a reference for the client.
        var _privileges = {
            PRIVILEGE_SIGNERA_INTYG : 'PRIVILEGE_SIGNERA_INTYG',
            PRIVILEGE_VIDAREBEFORDRA_UTKAST : 'PRIVILEGE_VIDAREBEFORDRA_UTKAST',
            PRIVILEGE_VIDAREBEFORDRA_FRAGASVAR : 'PRIVILEGE_VIDAREBEFORDRA_FRAGASVAR',
            PRIVILEGE_MAKULERA_INTYG : 'PRIVILEGE_MAKULERA_INTYG',
            PRIVILEGE_KOPIERA_INTYG : 'PRIVILEGE_KOPIERA_INTYG',
            PRIVILEGE_BESVARA_KOMPLETTERINGSFRAGA : 'PRIVILEGE_BESVARA_KOMPLETTERINGSFRAGA',
            PRIVILEGE_SKRIVA_INTYG : 'PRIVILEGE_SKRIVA_INTYG'
        };

        var _roles = {
            ROLE_VARDADMINISTRATOR : 'Vårdadministratör',
            ROLE_LAKARE :'Läkare',
            ROLE_LAKARE_DJUPINTEGRERAD :'Läkare - djupintegrerad',
            ROLE_LAKARE_UTHOPP :'Läkare - uthopp',
            ROLE_PRIVATLAKARE :'Privatläkare',
            ROLE_TANDLAKARE :'Tandläkare'
        };
        
        function _hasPrivilege(privilege) {
            if(UserModel.user && UserModel.user.authorities){
                return UserModel.user.authorities[privilege] !== undefined;
            } else {
                return false;
            }
        }

        function _hasRole(role) {
            if(UserModel.user && UserModel.user.roles){
                return UserModel.user.roles[role] !== undefined;
            } else {
                return false;
            }
        }

        function _isVardAdministrator(){
            if(UserModel.user && UserModel.user.roles){
                return UserModel.user.roles.ROLE_VARDADMINISTRATOR !== undefined;
            }
            return false;
        }

        function _isLakare(){
            if(UserModel.user && UserModel.user.roles){
                return UserModel.user.roles.ROLE_LAKARE !== undefined;
            }
            return false;
        }

        function _isLakareDjupIntegrerad(){
            if(UserModel.user && UserModel.user.roles){
                return UserModel.user.roles.ROLE_LAKARE_DJUPINTEGRERAD !== undefined;
            }
            return false;
        }

        function _isLakareUthopp(){
            if(UserModel.user && UserModel.user.roles){
                return UserModel.user.roles.ROLE_LAKARE_UTHOPP !== undefined;
            }
            return false;
        }

        function _isPrivatLakare(){
            if(UserModel.user && UserModel.user.roles){
                return UserModel.user.roles.ROLE_PRIVATLAKARE !== undefined;
            }
            return false;
        }

        function _isTandlakare(){
            if(UserModel.user && UserModel.user.roles){
                return UserModel.user.roles.ROLE_TANDLAKARE== undefined;
            }
            return false;
        }

        return {
            hasPrivilege : _hasPrivilege,
            hasRole : _hasRole,
            isVardAdministrator : _isVardAdministrator,
            isLakare : _isLakare,
            isLakareDjupIntegrerad: _isLakareDjupIntegrerad,
            isLakareUthopp : _isLakareUthopp,
            isPrivatLakare : _isPrivatLakare,
            isTandlakare : _isTandlakare
        };
    }]);
