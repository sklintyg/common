angular.module('common').factory('common.TillaggsfragorHelper',
    function() {
        'use strict';

        return {
            buildTillaggsFragor: function(formFields, model, insertIndex) {
                if (!model.tillaggsfragor) {
                    return;
                }

                var fields = [];
                for (var i = 0; i < model.tillaggsfragor.length; i++) {
                    var tillagsFraga = model.tillaggsfragor[i];
                    fields.push({
                        key: 'tillaggsfragor[' + i + '].svar',
                        type: 'multi-text',
                        templateOptions: { label: 'DFR_' + tillagsFraga.id + '.1' }
                    });
                }

                if (fields.length > 0) {
                    var tillaggsFragor = null;
                    for(var j = 0; j < formFields.length; j++) {
                        if (formFields[j].templateOptions && formFields[j].templateOptions.category === 9999) {
                            tillaggsFragor = formFields[j];
                        }
                    }
                    if (!tillaggsFragor) {
                        tillaggsFragor = {
                            wrapper: 'wc-field',
                            templateOptions: { category: 9999 }
                        };
                        formFields.splice(insertIndex, 0, tillaggsFragor);
                    }
                    tillaggsFragor.fieldGroup = fields;
                }
            }
        };
    }
);
