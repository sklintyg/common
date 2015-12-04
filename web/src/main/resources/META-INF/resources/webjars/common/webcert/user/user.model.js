angular.module('common').factory('common.UserModel',
    function() {
        'use strict';

        function _fillIntygsTyperFromRole(role, intygsTyper) {
            if (role.authorizedIntygsTyper !== undefined) {
                for (var b = 0; b < role.authorizedIntygsTyper.length; b++) {
                    var intygsTyp = role.authorizedIntygsTyper[b];

                    if (intygsTyp !== undefined && intygsTyper.indexOf(intygsTyp) === -1) {
                        intygsTyper.push(intygsTyp);
                    }
                }
            }
        }

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
                    _fillIntygsTyperFromRole(role, intygsTyper);
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
                    this.user.lakare = this.isLakare();
                    this.user.privatLakare = this.isPrivatLakare();
                    this.user.tandLakare = this.isTandlakare();
                    this.user.isLakareOrPrivat = this.user.lakare || this.user.privatLakare || this.user.tandLakare;
                    this.user.role = this.user.roles !== undefined ? this.roles.getRole(this.user.roles) : '';
                    this.user.intygsTyper = this.user.roles !== undefined ? _getIntygsTyperFromRoles(this.user.roles) : [];
                }
            },

            // these enums are just copies from the backend an ment as a reference for the client.
            requestOrigins: {
                NORMAL: 'NORMAL',
                DJUPINTEGRATION: 'DJUPINTEGRATION',
                UTHOPP: 'UTHOPP'
            },

            privileges: {
                SIGNERA_INTYG: 'SIGNERA_INTYG',
                VIDAREBEFORDRA_UTKAST: 'VIDAREBEFORDRA_UTKAST',
                VIDAREBEFORDRA_FRAGASVAR: 'VIDAREBEFORDRA_FRAGASVAR',
                MAKULERA_INTYG: 'MAKULERA_INTYG',
                KOPIERA_INTYG: 'KOPIERA_INTYG',
                BESVARA_KOMPLETTERINGSFRAGA: 'BESVARA_KOMPLETTERINGSFRAGA',
                FILTRERA_PA_LAKARE: 'FILTRERA_PA_LAKARE',
                ATKOMST_ANDRA_ENHETER: 'ATKOMST_ANDRA_ENHETER',
                HANTERA_PERSONUPPGIFTER: 'HANTERA_PERSONUPPGIFTER',
                HANTERA_MAILSVAR: 'HANTERA_MAILSVAR',
                NAVIGERING: 'NAVIGERING'
            },

            roles: {
                VARDADMINISTRATOR: 'Vårdadministratör',
                //ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD :'Vårdadministratör',
                //ROLE_VARDADMINISTRATOR_UTHOPP :'Vårdadministratör',
                LAKARE: 'Läkare',
                //ROLE_LAKARE_DJUPINTEGRERAD: 'Läkare',
                //ROLE_LAKARE_UTHOPP: 'Läkare',
                PRIVATLAKARE: 'Privatläkare',
                TANDLAKARE: 'Tandläkare',
                //ROLE_TANDLAKARE_DJUPINTEGRERAD: 'Tandläkare',
                //ROLE_TANDLAKARE_UTHOPP: 'Tandläkare',
                getRole: function(roles) {
                    var rs = '';
                    if (roles.VARDADMINISTRATOR !== undefined) {
                        rs += roles.VARDADMINISTRATOR.name;
                    }

                    //if (roles.ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD !== undefined) {
                    //    rs += roles.ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD.name;
                    //}

                    //if (roles.ROLE_VARDADMINISTRATOR_UTHOPP !== undefined) {
                    //    rs += roles.ROLE_VARDADMINISTRATOR_UTHOPP.name;
                    //}

                    if (roles.LAKARE !== undefined) {
                        rs += roles.LAKARE.name;
                    }

                    //if (roles.ROLE_LAKARE_DJUPINTEGRERAD !== undefined) {
                    //    rs += roles.ROLE_LAKARE_DJUPINTEGRERAD.name;
                    //}

                    //if (roles.ROLE_LAKARE_UTHOPP !== undefined) {
                    //    rs += roles.ROLE_LAKARE_UTHOPP.name;
                    //}

                    if (roles.PRIVATLAKARE !== undefined) {
                        rs += roles.PRIVATLAKARE.name;
                    }

                    if (roles.TANDLAKARE !== undefined) {
                        rs += roles.TANDLAKARE.name;
                    }

                    //if (roles.ROLE_TANDLAKARE_DJUPINTEGRERAD !== undefined) {
                    //    rs += roles.ROLE_TANDLAKARE_DJUPINTEGRERAD.name;
                    //}

                    //if (roles.ROLE_TANDLAKARE_UTHOPP !== undefined) {
                    //    rs += roles.ROLE_TANDLAKARE_UTHOPP.name;
                    //}

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

            hasRequestOrigin: function _hasRequestOrigin(requestOrigin) {
                return this.user !== undefined && this.user.requestOrigin !== undefined ? this.user.requestOrigin === requestOrigin : false;
            },

            //isVardAdministratorUthopp: function _isVardAdministratorUthopp() {
            //    return this.hasRoles() && this.user.roles.ROLE_VARDADMINISTRATOR_UTHOPP !== undefined;
            //},

            isVardAdministrator: function _isVardAdministrator() {
                return this.hasRoles() && this.user.roles.VARDADMINISTRATOR !== undefined;
            },

            isLakare: function _isLakare() {
                return this.hasRoles() && this.user.roles.LAKARE !== undefined;
            },

            //isLakareDjupIntegrerad: function _isLakareDjupIntegrerad() {
            //    return this.hasRoles() && this.user.roles.ROLE_LAKARE_DJUPINTEGRERAD !== undefined;
            //},

            //isVardadministratorDjupIntegrerad: function _isVardadministratorDjupIntegrerad() {
            //    return this.hasRoles() && this.user.roles.ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD !== undefined;
            //},

            //isLakareUthopp: function _isLakareUthopp() {
            //    return this.hasRoles() && this.user.roles.ROLE_LAKARE_UTHOPP !== undefined;
            //},

            //isVardadministratorUthopp: function _isVardadministratorUthopp() {
            //    return this.hasRoles() && this.user.roles.ROLE_VARDADMINISTRATOR_UTHOPP !== undefined;
            //},

            isPrivatLakare: function _isPrivatLakare() {
                return this.hasRoles() && this.user.roles.PRIVATLAKARE !== undefined;
            },

            isTandlakare: function _isTandlakare() {
                return this.hasRoles() && this.user.roles.TANDLAKARE !== undefined;
            },

            //isTandlakareDjupIntegrerad: function _isTandlakareDjupIntegrerad() {
            //    return this.hasRoles() && this.user.roles.ROLE_TANDLAKARE_DJUPINTEGRERAD !== undefined;
            //},

            //isTandlakareUthopp: function _isTandlakareUthopp() {
            //    return this.hasRoles() && this.user.roles.ROLE_TANDLAKARE_UTHOPP !== undefined;
            //},

            authenticationMethod: function _authenticationMethod(authenticationMethod){
              return this.user !== undefined && this.user.authenticationMethod !== undefined && this.user.authenticationMethod === authenticationMethod;
            },

            termsAccepted :false,
            transitioning : false

        };
    }
);