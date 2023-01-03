/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('ag7804').factory('ag7804.customizeViewstate', function() {
    'use strict';


    /**
     * Service that holds state and logic about which categories and fields that have values and their selection state.
     */

    var _fieldConfig = {
        'avstangningSmittskydd': {mandatory: false, selected: true, fields: ['avstangningSmittskydd']},
        'grund': {
            mandatory: true, selected: true,
            fields: [
                'undersokningAvPatienten',
                'telefonkontaktMedPatienten',
                'journaluppgifter',
                'annatGrundForMU',
                'annatGrundForMUBeskrivning'
            ]
        },
        'funktionsnedsattning': {
            mandatory: true,
            selected: true,
            fields: ['funktionsnedsattning']
        },
        'aktivitetsbegransning': {mandatory: true, selected: true, fields: ['aktivitetsbegransning']},
        'sysselsattning': {mandatory: true, selected: true, fields: ['sysselsattning', 'nuvarandeArbete']},
        'diagnosGrupp': {mandatory: false, selected: true, fields: ['onskarFormedlaDiagnos', 'diagnoser']},
        'pagaendeBehandling': {mandatory: true, selected: true, fields: ['pagaendeBehandling']},
        'planeradBehandling': {mandatory: true, selected: true, fields: ['planeradBehandling']},
        'sjukskrivningar': {mandatory: true, selected: true, fields: ['sjukskrivningar']},
        'arbetstidsforlaggning': {mandatory: true, selected: true, fields: ['arbetstidsforlaggning']},
        'arbetsresor': {mandatory: true, selected: true, fields: ['arbetsresor']},
        'prognos': {mandatory: true, selected: true, fields: ['prognos']},
        'arbetslivsinriktadeAtgarder': {mandatory: true, selected: true, fields: ['arbetslivsinriktadeAtgarder']},
        'arbetslivsinriktadeAtgarderBeskrivning': {
            mandatory: true,
            selected: true,
            fields: ['arbetslivsinriktadeAtgarderBeskrivning']
        },

        'forsakringsmedicinsktBeslutsstod': {
            mandatory: true,
            selected: true,
            fields: ['forsakringsmedicinsktBeslutsstod']
        },
        'arbetstidsforlaggningMotivering': {
            mandatory: true,
            selected: true,
            fields: ['arbetstidsforlaggningMotivering']
        },
        'ovrigt': {mandatory: true, selected: true, fields: ['ovrigt']},
        'kontaktMedAg': {mandatory: true, selected: true, fields: ['kontaktMedAg']},
        'anledningTillKontakt': {mandatory: true, selected: true, fields: ['anledningTillKontakt']}
    };



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
                angular.forEach(fc.fields, function(field) {
                    unselectedFieldNames.push(field);
                });

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

        getUnselected: function(withWarningOnly) {
            return _getUnselected(withWarningOnly);
        },

        getSendModel: function() {
            return _getSendModel();
        }

    };
});
