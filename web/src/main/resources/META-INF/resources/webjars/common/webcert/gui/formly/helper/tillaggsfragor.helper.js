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
