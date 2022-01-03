/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
/**
 * Helper class for resolving configfactories based on version
 */
angular.module('common').factory('factoryResolverHelper', [ '$injector', '$log', function($injector, $log) {
    'use strict';

    function _extractMajorVersion(version) {
        var majorVersion = '';
        if (angular.isDefined(version)) {
            majorVersion = version.split('.')[0];
        }
        //$log.debug('version "' + version + "' resolved majorversion '" + majorVersion + '"');
        return majorVersion;
    }

    function _resolve(baseName, $stateParams) {
            return $injector.get(baseName + '.v' + _extractMajorVersion($stateParams.intygTypeVersion));
    }

    /**
     * Dynamically looks up the intygstyp-specific PatientHelperService that contains the rules for when certain
     * info/warn messages should be displayed in the GUI when name- and/or address information doesn't match what's
     * stored in the utkast compared to djupintegrations-parameters or PU-service data.
     */
    function _resolvePatientHelper(intygsTyp, version) {

        var majorVersion = _extractMajorVersion(version);

        try {
            return $injector.get(intygsTyp + '.PatientHelperService.v' + majorVersion);
        } catch(e) {
            $log.error('Could not resolve PatientHelperService for intygstyp ' + intygsTyp + ' and version: ' + majorVersion + ', error: ' + e);
            throw e;
        }

    }

    // Return public API for the service
    return {
        resolve: _resolve,
        resolvePatientHelper: _resolvePatientHelper
    };
} ]);
