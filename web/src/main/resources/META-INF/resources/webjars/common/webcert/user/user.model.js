angular.module('common').factory('common.UserModel',
    function() {
        'use strict';

        /**
         * Does flatMap+distinct to extract unique intygsTyper from the array of roles.
         * (Each role contains an array of authorizedIntygsTyper)
         *
         * @param roles
         * @returns {Array}
         * @private
         */
        function _getIntygsTyperFromRoles(roles) {
            var intygsTyper = [];

            for (var key in roles) {
                if (roles.hasOwnProperty(key)) {
                    var role = roles[key];
                    if (role.authorizedIntygsTyper !== undefined) {
                        for(var b = 0; b < role.authorizedIntygsTyper.length; b++) {
                            var intygsTyp = role.authorizedIntygsTyper[b];

                            if (intygsTyp !== undefined && intygsTyper.indexOf(intygsTyp) === -1) {
                                intygsTyper.push(intygsTyp);
                            }
                        }
                    }
                }

            }

            return intygsTyper;
        }

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
                if(user !== undefined && user !== null) {
                    this.user = user;
                    //setup lakare and privatLakare based on the new role enum
                    this.user.lakare = this.isLakare();
                    this.user.privatLakare = this.isPrivatLakare();
                    this.user.isLakareOrPrivat = this.user.lakare || this.user.privatLakare;
                    this.user.role = this.user.roles !== undefined ? this.roles.getRole(this.user.roles) : '';
                    this.user.intygsTyper = this.user.roles !== undefined ? _getIntygsTyperFromRoles(this.user.roles) : [];
                }
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
                ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD :'Vårdadministratör - djupintegrerad',
                ROLE_VARDADMINISTRATOR_UTHOPP :'Vårdadministratör - uthopp',
                ROLE_LAKARE: 'Läkare',
                ROLE_LAKARE_DJUPINTEGRERAD: 'Läkare - djupintegrerad',
                ROLE_LAKARE_UTHOPP: 'Läkare - uthopp', // franJournalsystemQAOnly
                ROLE_PRIVATLAKARE: 'Privatläkare',
                ROLE_TANDLAKARE: 'Tandläkare',
                getRole: function(roles) {
                    var rs = '';
                    if (roles.ROLE_VARDADMINISTRATOR !== undefined) {
                        rs += roles.ROLE_VARDADMINISTRATOR.name;
                    }

                    if (roles.ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD !== undefined) {
                        rs += roles.ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD.name;
                    }

                    if (roles.ROLE_VARDADMINISTRATOR_UTHOPP !== undefined) {
                        rs += roles.ROLE_VARDADMINISTRATOR_UTHOPP.name;
                    }

                    if (roles.ROLE_LAKARE !== undefined) {
                        rs += roles.ROLE_LAKARE.name;
                    }

                    if (roles.ROLE_LAKARE_DJUPINTEGRERAD !== undefined) {
                        rs += roles.ROLE_LAKARE_DJUPINTEGRERAD.name;
                    }

                    if (roles.ROLE_LAKARE_UTHOPP !== undefined) {
                        rs += roles.ROLE_LAKARE_UTHOPP.name;
                    }

                    if (roles.ROLE_PRIVATLAKARE !== undefined) {
                        rs += roles.ROLE_PRIVATLAKARE.name;
                    }

                    if (roles.ROLE_TANDLAKARE !== undefined) {
                        rs += roles.ROLE_TANDLAKARE.name;
                    }
                    return rs;
                }
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

            hasIntygsTyp: function _hasIntygsTyp(intygsTyp) {
                return this.user !== undefined && this.user.intygsTyper !== undefined ? this.user.intygsTyper.indexOf(intygsTyp) > -1 : false;
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
                return this.hasRoles() && this.user.roles.ROLE_TANDLAKARE !== undefined;
            },

            termsAccepted :false,
            transitioning : false

        };
    }
);