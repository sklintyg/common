/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.TillaggsfragorHelper', ['common.FactoryTemplatesHelper',
    function(FactoryTemplates) {
        'use strict';

        return {
            buildTillaggsFragor: function(formFields, model, insertIndex) {
                if (!model.tillaggsfragor) {
                    return;
                }

                var fields = [];
                for (var i = 0; i < model.tillaggsfragor.length; i++) {
                    var tillagsFraga = model.tillaggsfragor[i];
                    fields.push( FactoryTemplates.fraga(tillagsFraga.id, [{
                        key: 'tillaggsfragor[' + i + '].svar',
                        type: 'multi-text',
                        templateOptions: { label: 'DFR_' + tillagsFraga.id + '.1',
                            id: 'tillaggsfragor_' + tillagsFraga.id,
                            forceHeadingTypeLabel: true
                        }
                    }]));
                }

                if (fields.length > 0) {
                    var tillaggsFragorKategori = null;
                    for(var j = 0; j < formFields.length; j++) {
                        if (formFields[j].templateOptions && formFields[j].templateOptions.category === 9999) {
                            //find the category
                            tillaggsFragorKategori = formFields[j];
                        }
                    }
                    if (!tillaggsFragorKategori) {
                        tillaggsFragorKategori = FactoryTemplates.kategori(9999, name, fields, {hideExpression: 'model.avstangningSmittskydd'});
                        //insert tillaggsfragor last.
                        formFields.splice(insertIndex, 0, tillaggsFragorKategori);
                    }

                }
            }
        };
    }
]);
