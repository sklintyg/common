/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.UserModel', [
  '$log', 'common.ObjectHelper',
  function($log, ObjectHelper) {
    'use strict';

    function _checkRequestOrigin(user, privilegeConfig, intygsTypContext) {
      if (privilegeConfig.requestOrigins !== undefined && privilegeConfig.requestOrigins.length > 0) {

        //requestOrigin constraint exist - we must match that
        var originToMatch = user.origin;
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
        // INTYG-7389: Hanterar nu även intygsTyp som tom sträng i och med att wc-authority alltid kräver intygstyp.
        if (intygsTypContext !== undefined && intygsTypContext !== '') {

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
      return true;
    }

    return {
      reset: function() {
        this.user = null;
      },

      getActiveFeatures: function() {
        if (this.user) {
          return this.user.features;
        } else {
          return null;
        }
      },

      getIntegrationParam: function(paramName) {

        if (!ObjectHelper.isDefined(this.user)) {
          $log.error('Invalid user object');
          return null;
        }

        if (!ObjectHelper.isDefined(this.user.parameters)) {
          return null; // User is probably not integrated, fail silently
        }

        var paramValue = this.user.parameters[paramName];
        if (!ObjectHelper.isDefined(paramValue)) {
          $log.debug('Undefined value for paramName ' + paramName);
          return null;
        }

        return paramValue;
      },

      setUser: function(user) {
        if (user !== undefined && user !== null) {
          this.user = user;
          this.user.lakare = this.isLakare();
          this.user.privatLakare = this.isPrivatLakare();
          this.user.tandLakare = this.isTandlakare();
          this.user.isLakareOrPrivat = this.user.lakare || this.user.privatLakare || this.user.tandLakare;
          this.user.role = this.user.roles !== undefined ? this.roles.getRole(this.user.roles) : '';
        }
      },

      // these enums are just copies from the backend an ment as a reference for the client.
      requestOrigins: {
        NORMAL: 'NORMAL',
        DJUPINTEGRATION: 'DJUPINTEGRATION',
        UTHOPP: 'UTHOPP',
        READONLY: 'READONLY'
      },

      privileges: {
        VISA_INTYG: 'VISA_INTYG',
        SKRIVA_INTYG: 'SKRIVA_INTYG',
        SIGNERA_INTYG: 'SIGNERA_INTYG',
        VIDAREBEFORDRA_UTKAST: 'VIDAREBEFORDRA_UTKAST',
        VIDAREBEFORDRA_FRAGASVAR: 'VIDAREBEFORDRA_FRAGASVAR',
        MAKULERA_INTYG: 'MAKULERA_INTYG',
        FORNYA_INTYG: 'FORNYA_INTYG',
        ERSATTA_INTYG: 'ERSATTA_INTYG',
        BESVARA_KOMPLETTERINGSFRAGA: 'BESVARA_KOMPLETTERINGSFRAGA',
        SVARA_MED_NYTT_INTYG: 'SVARA_MED_NYTT_INTYG',
        ATKOMST_ANDRA_ENHETER: 'ATKOMST_ANDRA_ENHETER',
        HANTERA_PERSONUPPGIFTER: 'HANTERA_PERSONUPPGIFTER',
        HANTERA_MAILSVAR: 'HANTERA_MAILSVAR',
        NAVIGERING: 'NAVIGERING',
        HANTERA_SEKRETESSMARKERAD_PATIENT: 'HANTERA_SEKRETESSMARKERAD_PATIENT',
        GODKANNA_MOTTAGARE: 'GODKANNA_MOTTAGARE'
      },

      roles: {
        VARDADMINISTRATOR: 'Vårdadministratör',
        LAKARE: 'Läkare',
        PRIVATLAKARE: 'Privatläkare',
        TANDLAKARE: 'Tandläkare',

        getRole: function(roles) {
          var rs = '';
          if (roles.VARDADMINISTRATOR !== undefined) {
            rs += roles.VARDADMINISTRATOR.desc;
          }

          if (roles.LAKARE !== undefined) {
            rs += roles.LAKARE.desc;
          }

          if (roles.PRIVATLAKARE !== undefined) {
            rs += roles.PRIVATLAKARE.desc;
          }

          if (roles.TANDLAKARE !== undefined) {
            rs += roles.TANDLAKARE.desc;
          }

          return rs;
        }
      },

      hasAuthenticationMethod: function _hasAuthenticationMethod(authenticationMethod) {
        return this.user !== undefined && this.user.authenticationMethod !== undefined &&
            this.user.authenticationMethod === authenticationMethod;
      },

      authenticationMethod: function _authenticationMethod() {
        return this.user.authenticationMethod;
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
        // INTYG-7389: Hanterar nu även intygsTyp som tom sträng i och med att wc-authority alltid kräver intygstyp.
        if (intygsTypContext !== undefined && intygsTypContext !== '') {
          var intygsTyper = privilegeConfig.intygstyper;
          if (intygsTyper !== undefined && intygsTyper.length > 0 &&
              intygsTyper.indexOf(intygsTypContext) === -1) {
            return false;
          }
        }

        //..and also, if the privilege has requestOrigin constraints, the users current origin must match that..
        if (!_checkRequestOrigin(this.user, privilegeConfig, intygsTypContext)) {
          return false;
        }

        //If we get this far - the user is considered to have the privilege
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

      isReadOnly: function _isReadOnly() {
        return this.hasRequestOrigin(this.requestOrigins.READONLY);
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

      isNormalOrigin: function _isNormalOrigin() {
        return this.hasRequestOrigin(this.requestOrigins.NORMAL);
      },

      isVardAdministrator: function _isVardAdministrator() {
        return this.hasRoles() && this.user.roles.VARDADMINISTRATOR !== undefined;
      },

      getAnvandarPreference: function _getAnvandarPreference(prefKey) {
        return this.user.anvandarPreference[prefKey];
      },

      setAnvandarPreference: function _setAnvandarPreference(prefKey, prefValue) {
        this.user.anvandarPreference[prefKey] = prefValue;
      },

      termsAccepted: false,
      transitioning: false,
      idpConnectivityChecked: false

    };
  }
]);
