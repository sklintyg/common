/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
angular.module('ag7804').factory('ag7804.viewFactory.v1', [
    '$stateParams', '$location', 'ag7804.customizeViewstate',
    function($stateParams, $location, customizeViewstate) {
        'use strict';

        var intygsTyp = 'ag7804';
        
        var _sendUrl = function() {
            return null;
        };

        var _customizeCertificate = function() {
            customizeViewstate.resetModel();
            $location.path('/' + intygsTyp + '/' + $stateParams.intygTypeVersion + '/customize-ag7804/' + $stateParams.certificateId + '/step1');
        };

        var _enableCustomizeCertificate = function(cert) {
            return !cert.avstangningSmittskydd && cert.onskarFormedlaDiagnos;
        };

        return {
            intygsTyp: intygsTyp,
            getSendUrl: _sendUrl,
            customizeCertificate: _customizeCertificate,
            enableCustomizeCertificate: _enableCustomizeCertificate
        };
    }]);
