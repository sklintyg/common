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
angular.module('fk7263').factory('fk7263.customizeViewstate', function() {
    'use strict';


    /**
     * Service that holds state and logic about which categories and fields that have values and their selection state.
     */

    var _fieldConfig = {

        'smittskydd': { mandatory: false, selected: true, fields: ['avstangningSmittskydd'], domId: 'smittskydd' },
        'diagnos': { mandatory: false, selected: true, fields: ['diagnosKod'], domId: 'diagnos'},
        'aktuelltSjukdomsforlopp': { mandatory: false, selected: true, fields: ['sjukdomsforlopp'], domId: 'aktuelltSjukdomsforlopp'},
        'funktionsnedsattning': { mandatory: false, selected: true, fields: ['funktionsnedsattning' ], domId: 'funktionsnedsattning'},
        'intygetBaserasPa': { mandatory: true, selected: true, fields: [
            'undersokningAvPatienten',
            'telefonkontaktMedPatienten',
            'journaluppgifter',
            'annanReferens'], domId: 'intygetBaserasPa'
        },

        'aktivitetsbegransning': { mandatory: false, selected: true, fields: ['aktivitetsbegransning'], warn: true , domId: 'aktivitetsbegransning'},

        'rekommendationerUtomForetagsHalsoVard': {
            mandatory: false,
            selected: true,
            fields: [ 'rekommendationKontaktArbetsformedlingen', 'rekommendationOvrigt' ],
            domId: 'rekommendationerUtomForetagsHalsoVard'
        },
        'rekommendationerForetagsHalsoVard': { mandatory: true, selected: true, fields: ['rekommendationKontaktForetagshalsovarden'], domId: 'rekommendationerForetagsHalsoVard'},
        'planeradBehandling': {mandatory: false, selected: true, fields: ['atgardInomSjukvarden', 'annanAtgard'] , domId: 'planeradBehandling'},
        'rehabilitering': { mandatory: false, selected: true, fields: ['rehabilitering'] , domId: 'rehabilitering'},
        'arbetsFormagaRelativtUtomNuvarandeArbete': { mandatory: false, selected: true, fields: ['arbetsloshet', 'foraldrarledighet'], domId: 'arbetsFormagaRelativtUtomNuvarandeArbete'},
        'arbetsFormagaRelativtNuvarandeArbete': { mandatory: true, selected: true, fields: ['nuvarandeArbetsuppgifter'], domId: 'arbetsFormagaRelativtNuvarandeArbete'},
        'bedomdArbetsFormaga': { mandatory: true, selected: true, fields: ['nedsattMed25', 'nedsattMed50', 'nedsattMed75', 'nedsattMed100'], domId: 'bedomdArbetsFormaga'},
        'arbetsFormaga': { mandatory: false, selected: true, fields: ['arbetsformagaPrognos'], domId: 'arbetsFormaga'},
        'prognos': { mandatory: false, selected: true, fields: ['prognosBedomning'], domId: 'prognos'},
        'ressatt': { mandatory: true, selected: true, fields: ['ressattTillArbeteAktuellt','ressattTillArbeteEjAktuellt'], domId: 'ressatt'},
        'fkKontakt': { mandatory: false, selected: true, fields: ['kontaktMedFk'], domId: 'fkKontakt'},
        'ovrigt': {mandatory: false, selected: true, fields: ['kommentar'], domId: 'ovrigt'}

    };


    //Create initial model
    var fieldConfig = angular.copy(_fieldConfig);

    function _getUnselected(withWarningOnly) {

        var unselectedFieldNames = [];
        angular.forEach(fieldConfig, function(fc, key) {
           if (!fc.selected && (!!withWarningOnly ? fc.warn: true)) {
                unselectedFieldNames.push(key);
            }
        });
        return unselectedFieldNames;
    }

    function _addOptional(field, fieldName, selectedOptionalFields) {
        if (!field.mandatory && field.selected) {
            if (field.id) {
                selectedOptionalFields.push(field.id);
            } else {
                selectedOptionalFields.push(fieldName);
            }

        }
    }

    function _getSelectedOptionalFields() {
        var selectedOptionalFields = [];
        angular.forEach(fieldConfig, function(fc, key) {
            if (angular.isArray(fc)) {
                angular.forEach(fc, function(nestedFc, key) {
                    _addOptional(nestedFc, key, selectedOptionalFields);
                });
            } else {
                _addOptional(fc, key, selectedOptionalFields);
            }
        });
        return selectedOptionalFields;
    }


    function _getSendModel() {
        var sendModel = _getSelectedOptionalFields();
        var unselected = _getUnselected();
        angular.forEach(unselected, function(field) {
            //! prefix indicates 'not'.. This is so that the backend can determine if any field (that was selectable) has been
            // deselected so that the correct watermarktext can be displayed
            sendModel.push('!' + field);
        });
        return sendModel;

    }
    //Expose public api for this service


    return {
        resetModel: function() {
            fieldConfig = angular.copy(_fieldConfig);
        },


        getModel: function() {
            return fieldConfig;
        },

        getUnselected: function(warningOnly) {
            return _getUnselected(warningOnly);
        },

        getSendModel: function() {
            return _getSendModel();
        }

    };
});
