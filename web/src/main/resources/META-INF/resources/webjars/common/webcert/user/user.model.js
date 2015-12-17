/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
                    //setup lakare and privatLakare based on the new role enum
                    this.user.lakare = this.isLakare();
                    this.user.privatLakare = this.isPrivatLakare();
                    this.user.tandLakare = this.isTandlakare();
                    this.user.isLakareOrPrivat = this.user.lakare || this.user.privatLakare || this.user.tandLakare;
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
                PRIVILEGE_FILTRERA_PA_LAKARE: 'PRIVILEGE_FILTRERA_PA_LAKARE',
                PRIVILEGE_ATKOMST_ANDRA_ENHETER: 'PRIVILEGE_ATKOMST_ANDRA_ENHETER',
                PRIVILEGE_HANTERA_PERSONUPPGIFTER: 'PRIVILEGE_HANTERA_PERSONUPPGIFTER',
                PRIVILEGE_HANTERA_MAILSVAR: 'PRIVILEGE_HANTERA_MAILSVAR',
                PRIVILEGE_NAVIGERING: 'PRIVILEGE_NAVIGERING'
            },

            roles: {
                ROLE_VARDADMINISTRATOR: 'Vårdadministratör',
                ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD :'Vårdadministratör',
                ROLE_VARDADMINISTRATOR_UTHOPP :'Vårdadministratör',
                ROLE_LAKARE: 'Läkare',
                ROLE_LAKARE_DJUPINTEGRERAD: 'Läkare',
                ROLE_LAKARE_UTHOPP: 'Läkare',
                ROLE_PRIVATLAKARE: 'Privatläkare',
                ROLE_TANDLAKARE: 'Tandläkare',
                ROLE_TANDLAKARE_DJUPINTEGRERAD: 'Tandläkare',
                ROLE_TANDLAKARE_UTHOPP: 'Tandläkare',
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

                    if (roles.ROLE_TANDLAKARE_DJUPINTEGRERAD !== undefined) {
                        rs += roles.ROLE_TANDLAKARE_DJUPINTEGRERAD.name;
                    }

                    if (roles.ROLE_TANDLAKARE_UTHOPP !== undefined) {
                        rs += roles.ROLE_TANDLAKARE_UTHOPP.name;
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

            isVardAdministratorUthopp: function _isVardAdministratorUthopp() {
                return this.hasRoles() && this.user.roles.ROLE_VARDADMINISTRATOR_UTHOPP !== undefined;
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

            isVardadministratorDjupIntegrerad: function _isVardadministratorDjupIntegrerad() {
                return this.hasRoles() && this.user.roles.ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD !== undefined;
            },

            isLakareUthopp: function _isLakareUthopp() {
                return this.hasRoles() && this.user.roles.ROLE_LAKARE_UTHOPP !== undefined;

            },

            isVardadministratorUthopp: function _isVardadministratorUthopp() {
                return this.hasRoles() && this.user.roles.ROLE_VARDADMINISTRATOR_UTHOPP !== undefined;

            },

            isPrivatLakare: function _isPrivatLakare() {
                return this.hasRoles() && this.user.roles.ROLE_PRIVATLAKARE !== undefined;

            },

            isTandlakare: function _isTandlakare() {
                return this.hasRoles() && this.user.roles.ROLE_TANDLAKARE !== undefined;
            },

            isTandlakareDjupIntegrerad: function _isTandlakareDjupIntegrerad() {
                return this.hasRoles() && this.user.roles.ROLE_TANDLAKARE_DJUPINTEGRERAD !== undefined;
            },

            isTandlakareUthopp: function _isTandlakareUthopp() {
                return this.hasRoles() && this.user.roles.ROLE_TANDLAKARE_UTHOPP !== undefined;
            },

            authenticationMethod: function _authenticationMethod(authenticationMethod){
              return this.user !== undefined && this.user.authenticationMethod !== undefined &&
                  this.user.authenticationMethod === authenticationMethod;
            },

            termsAccepted :false,
            transitioning : false

        };
    }
);
