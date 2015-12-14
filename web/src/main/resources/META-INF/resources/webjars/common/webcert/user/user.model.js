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
                if (user !== undefined && user !== null) {
                    this.user = user;
                    this.user.lakare = this.isLakare();
                    this.user.privatLakare = this.isPrivatLakare();
                    this.user.tandLakare = this.isTandlakare();
                    this.user.isLakareOrPrivat = this.user.lakare || this.user.privatLakare || this.user.tandLakare;
                    this.user.role = this.user.roles !== undefined ? this.roles.getRole(this.user.roles) : '';
                    this.user.origin = this.user.requestOrigin !== undefined ? this.user.requestOrigin.name : '';
                }
            },

            // these enums are just copies from the backend an ment as a reference for the client.
            requestOrigins: {
                NORMAL: 'NORMAL',
                DJUPINTEGRATION: 'DJUPINTEGRATION',
                UTHOPP: 'UTHOPP'
            },

            privileges: {
                VISA_INTYG: 'VISA_INTYG',
                SKRIVA_INTYG: 'SKRIVA_INTYG',
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
                LAKARE: 'Läkare',
                PRIVATLAKARE: 'Privatläkare',
                TANDLAKARE: 'Tandläkare',

                getRole: function(roles) {
                    var rs = '';
                    if (roles.VARDADMINISTRATOR !== undefined) {
                        //rs += roles.VARDADMINISTRATOR.name;
                        rs += roles.VARDADMINISTRATOR.desc;
                    }

                    if (roles.LAKARE !== undefined) {
                        //rs += roles.LAKARE.name;
                        rs += roles.LAKARE.desc;
                    }

                    if (roles.PRIVATLAKARE !== undefined) {
                        //rs += roles.PRIVATLAKARE.name;
                        rs += roles.PRIVATLAKARE.desc;
                    }

                    if (roles.TANDLAKARE !== undefined) {
                        //rs += roles.TANDLAKARE.name;
                        rs += roles.TANDLAKARE.desc;
                    }

                    return rs;
                }
            },

            hasAuthenticationMethod: function _hasAuthenticationMethod(authenticationMethod) {
                return this.user !== undefined && this.user.authenticationMethod !== undefined && this.user.authenticationMethod === authenticationMethod;
            },

            hasAuthorities: function() {
                return this.user !== undefined && this.user.authorities !== undefined;
            },

            hasPrivilege: function _hasPrivilege(privilege, intygsTypContext) {

                //Basic first check - User must at least have the base privilege
                if (!(this.hasAuthorities() && this.user.authorities[privilege] !== undefined)) {
                    return false;
                }

                var privilegeConfig = this.user.authorities[privilege];

                //.. and if intygstyp context is given, must also have a matching privilege<->intygstyp constraint if
                // such a constraint exist.
                if (intygsTypContext !== undefined) {
                    var intygsTyper = privilegeConfig.intygstyper;
                    if (intygsTyper !== undefined && intygsTyper.length > 0 &&
                        intygsTyper.indexOf(intygsTypContext) === -1) {
                        return false;
                    }
                }

                //..and also, if the privilege has requestOrigin constraints, the users current origin must match that..
                if (privilegeConfig.requestOrigins !== undefined && privilegeConfig.requestOrigins.length > 0) {

                    //requestOrigin constraint exist - we must match that
                    var originToMatch = this.user.origin;
                    var matchingOriginConfig;
                    for (var i = 0; i < privilegeConfig.requestOrigins.length; i++) {
                        if (privilegeConfig.requestOrigins[i].name === originToMatch) {
                            matchingOriginConfig = privilegeConfig.requestOrigins[i];
                            break;
                        }

                    }

                    if (matchingOriginConfig === undefined) {
                        return false;
                    }

                    //..secondly, if intygstyp context is given, must also have a matching privilege.requestOrigin.intygstyper<->intygstyp constraint if
                    // such a constraint exist.
                    if (intygsTypContext !== undefined) {
                        //does the originConfig have a intygstyp constraint?
                        if (matchingOriginConfig.intygstyper !== undefined &&
                            matchingOriginConfig.intygstyper.length > 0) {
                            //.. do we have a match with the given intygstyp context?
                            if (matchingOriginConfig.intygstyper.indexOf(intygsTypContext) === -1) {
                                return false;
                            }
                        }
                    }
                }

                //If we get this far - the user is considered to have the previlegie
                return true;
            },

            hasRequestOrigin: function _hasRequestOrigin(requestOrigin) {
                if (requestOrigin === undefined) {
                    return true;
                }

                if (this.user !== undefined && this.user.origin !== undefined) {
                    return requestOrigin === this.user.origin;
                }

                return false;
            },

            hasRole: function _hasRole(role) {
                return this.hasRoles() && this.user.roles[role] !== undefined;
            },

            hasRoles: function() {
                return this.user !== undefined && this.user.roles !== undefined;
            },

            isDjupintegration: function _isDjupintegration() {
                return this.hasRequestOrigin(this.requestOrigins.DJUPINTEGRATION);
            },

            isLakare: function _isLakare() {
                return this.hasRoles() && this.user.roles.LAKARE !== undefined;
            },

            isPrivatLakare: function _isPrivatLakare() {
                return this.hasRoles() && this.user.roles.PRIVATLAKARE !== undefined;
            },

            isTandlakare: function _isTandlakare() {
                return this.hasRoles() && this.user.roles.TANDLAKARE !== undefined;
            },

            isUthopp: function _isUthopp() {
                return this.hasRequestOrigin(this.requestOrigins.UTHOPP);
            },

            isVardAdministrator: function _isVardAdministrator() {
                return this.hasRoles() && this.user.roles.VARDADMINISTRATOR !== undefined;
            },

            termsAccepted: false,
            transitioning: false

        };
    }
);