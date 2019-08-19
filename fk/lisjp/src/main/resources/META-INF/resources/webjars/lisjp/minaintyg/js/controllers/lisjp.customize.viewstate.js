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
angular.module('lisjp').factory('lisjp.customizeViewstate', function() {
    'use strict';


    /**
     * Service that holds state and logic about which categories and fields that have values and their selection state.
     */

    var _fieldConfig = {

        'avstangningSmittskydd': { mandatory: false, selected: true, fields: ['avstangningSmittskydd'] },
        'grund': {
            mandatory: true, selected: true,
            fields: [
                'undersokningAvPatienten',
                'telefonkontaktMedPatienten',
                'journaluppgifter',
                'annatGrundForMU',
                'annatGrundForMUBeskrivning',
                'motiveringTillInteBaseratPaUndersokning'
            ]
        },
        'funktionsnedsattning': { mandatory: false, selected: true, fields: ['funktionsnedsattning'] },
        'aktivitetsbegransning': { mandatory: false, selected: true, fields: ['aktivitetsbegransning'], warn: true },
        'diagnoser': { mandatory: false, selected: true, fields: ['diagnoser'] },
        'pagaendeBehandling': { mandatory: false, selected: true, fields: ['pagaendeBehandling'] },
        'planeradBehandling': { mandatory: false, selected: true, fields: ['planeradBehandling']},
        'forsakringsmedicinsktBeslutsstod': {mandatory: false, selected: true, fields: ['forsakringsmedicinsktBeslutsstod'] },
        'arbetstidsforlaggningMotivering': { mandatory: false, selected: true, fields: ['arbetstidsforlaggningMotivering'], warn: true },
        'ovrigt': { mandatory: false, selected: true, fields: ['ovrigt']},
        'kontaktMedFk': { mandatory: false, selected: true, fields: ['kontaktMedFk']},
        'anledningTillKontakt': {mandatory: false, selected: true, fields: ['anledningTillKontakt']},
        'sysselsattningOptional': { id:'sysselsattningOptional', mandatory: false, selected: true,
            fields: [sysselsattningsTypResolver(['STUDIER','ARBETSSOKANDE', 'FORALDRALEDIG'])]
        },

        //Mandatory fields
        'sysselsattningMandatory': { id:'sysselsattningMandatory', mandatory: true, selected: true, fields: [sysselsattningsTypResolver(['NUVARANDE_ARBETE'])]},
        'sjukskrivningar': {  mandatory: true, selected: true, fields: ['sjukskrivningar'] },
        'arbetstidsforlaggning': {  mandatory: true, selected: true, fields: ['arbetstidsforlaggning'] },
        'arbetsresor': {  mandatory: true, selected: true, fields: ['arbetsresor'] },
        'prognos': {  mandatory: true, selected: true, fields: ['prognos'] },
        'arbetslivsinriktadeAtgarder': {  mandatory: true, selected: true, fields: ['arbetslivsinriktadeAtgarder'] },
        'arbetslivsinriktadeAtgarderBeskrivning': {  mandatory: true, selected: true, fields: ['arbetslivsinriktadeAtgarderBeskrivning'] },
        'tillaggsfragor': [] //is dynamically configured after cert has been loaded
    };

    // Return a function that given a certificate model, determines if it contains
    // any of the specified sysselsattnings types
    function sysselsattningsTypResolver(typer) {
        return function checkIfCertHasAnyOfTypes(cert) {
            var result = false;
            angular.forEach(cert.sysselsattning, function(s) {
                angular.forEach(typer, function(st) {
                    if (s.typ === st) {
                        result = true;
                    }
                });
            });
            return result;
        };

    }
    // Return a function that given a certificate model, determines if it contains
    // an answer to the specified tillaggsfraga
    function tillaggsfragaResolver(frageId) {
        return function checkIfCertHasValueForFragaWithId(cert) {
            var result = false;
            angular.forEach(cert.tillaggsfragor, function(fraga) {

                if (fraga.id === frageId && fraga.svar!=='') {
                    result = true;
                }
            });
            return result;
        };

    }
    //Create initial model
    var fieldConfig = angular.copy(_fieldConfig);

    function _getUnselected(withWarningOnly) {
        var unselectedFieldNames = [];
        angular.forEach(fieldConfig, function(fc, key) {
            if (angular.isArray(fc)) {
                angular.forEach(fc, function(nestedFc, key) {
                    if (!nestedFc.selected) {
                        if (nestedFc.id) {
                            unselectedFieldNames.push(nestedFc.id);
                        } else {
                            unselectedFieldNames.push(key);
                        }
                    }
                });

            } else if (!fc.selected && (!!withWarningOnly ? fc.warn: true)) {
                    unselectedFieldNames.push(key);
            }


        });
        return unselectedFieldNames;
    }

    function _addOptional(field, fieldName, selectedOptionalFields) {
        if (!field.mandatory && field.selected && field.hasValue) {
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

    //Tillaggsfragor are dynamically added to the model _after_ the certificate has been loaded
    //We need to add field config for these dynamically (and just once)
    function _addTillaggsFragor(tillaggsfragor) {
        if (tillaggsfragor && fieldConfig.tillaggsfragor.length === 0) {
            angular.forEach(tillaggsfragor, function(fraga, key) {
                fieldConfig.tillaggsfragor.push({
                    mandatory: false,
                    selected: true,
                    id: fraga.id,
                    fields: [ tillaggsfragaResolver(fraga.id) ]
                });
            });
        }
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

        addTillaggsFragor: function(fragor) {
            _addTillaggsFragor(fragor);
        },

        getModel: function() {
            return fieldConfig;
        },

        getUnselected: function(withWarningOnly) {
            return _getUnselected(withWarningOnly);
        },

        getSendModel: function() {
            return _getSendModel();
        }

    };
});
