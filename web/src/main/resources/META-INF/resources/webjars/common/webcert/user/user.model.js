angular.module('common').factory('common.UserModel',
    function() {
        'use strict';

        return {
            reset: function() {
                this.user = null;
            },

            getActiveFeatures: function() {
                if (this.user) {
                    return this.user.aktivaFunktioner;
                } else {
                    return null;
                }
            },

            setUser: function(user) {
                this.user = user;
                //setup lakare and privatLakare based on the new role enum
                this.user.lakare = this.isLakare();
                this.user.privatLakare = this.isPrivatLakare();
                this.user.isLakareOrPrivat = this.user.lakare || this.user.privatLakare;
            },

            // these enums are just copies from the backend an ment as a reference for the client.
            privileges: {
                PRIVILEGE_SIGNERA_INTYG: 'PRIVILEGE_SIGNERA_INTYG',
                PRIVILEGE_VIDAREBEFORDRA_UTKAST: 'PRIVILEGE_VIDAREBEFORDRA_UTKAST',
                PRIVILEGE_VIDAREBEFORDRA_FRAGASVAR: 'PRIVILEGE_VIDAREBEFORDRA_FRAGASVAR',
                PRIVILEGE_MAKULERA_INTYG: 'PRIVILEGE_MAKULERA_INTYG',
                PRIVILEGE_KOPIERA_INTYG: 'PRIVILEGE_KOPIERA_INTYG',
                PRIVILEGE_BESVARA_KOMPLETTERINGSFRAGA: 'PRIVILEGE_BESVARA_KOMPLETTERINGSFRAGA',
                PRIVILEGE_SKRIVA_INTYG: 'PRIVILEGE_SKRIVA_INTYG'
            },

            roles: {
                ROLE_VARDADMINISTRATOR: 'Vårdadministratör',
                ROLE_LAKARE: 'Läkare',
                ROLE_LAKARE_DJUPINTEGRERAD: 'Läkare - djupintegrerad',
                ROLE_LAKARE_UTHOPP: 'Läkare - uthopp',
                ROLE_PRIVATLAKARE: 'Privatläkare',
                ROLE_TANDLAKARE: 'Tandläkare'
            },

            hasRoles: function() {
                return this.user !== undefined && this.user.roles !== undefined;
            },


            hasAuthorities: function() {
                return this.user !== undefined && this.user.authorities !== undefined;
            },

            hasPrivilege: function _hasPrivilege(privilege) {
                return this.hasAuthorities() && this.user.authorities[privilege] !== undefined;
            },

            hasRole: function _hasRole(role) {
                return this.hasRoles() && this.user.roles[role] !== undefined;
            },

            isVardAdministrator: function _isVardAdministrator() {
                return this.hasRoles() && this.user.roles.ROLE_VARDADMINISTRATOR !== undefined;

            },

            isLakare: function _isLakare() {
                return this.hasRoles() && this.user.roles.ROLE_LAKARE !== undefined;

            },

            isLakareDjupIntegrerad: function _isLakareDjupIntegrerad() {
                return this.hasRoles() && this.user.roles.ROLE_LAKARE_DJUPINTEGRERAD !== undefined;
            },

            isLakareUthopp: function _isLakareUthopp() {
                return this.hasRoles() && this.user.roles.ROLE_LAKARE_UTHOPP !== undefined;

            },

            isPrivatLakare: function _isPrivatLakare() {
                return this.hasRoles() && this.user.roles.ROLE_PRIVATLAKARE !== undefined;

            },

            isTandlakare: function _isTandlakare() {
                return this.hasRoles() && this.user.roles.ROLE_TANDLAKARE == undefined;
            }
        };
    }
);