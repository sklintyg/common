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
/**
 * message directive for externalizing text resources.
 *
 * All resourcekeys are expected to be defined in lowercase and available in a
 * global js object named "messages"
 * Also supports dynamic key values such as key="status.{{scopedvalue}}"
 *
 * Usage: <dynamicLabel key="some.resource.key" [fallback="defaulttextifnokeyfound"]/>
 */
angular.module('common').factory('uvTableService',
    [ '$log', 'uvUtil',
        function($log, uvUtil) {
            'use strict';

            function _createRow(config, modelRow, colProp, rowIndex){
                var row = { values: [] };
                angular.forEach(config.valueProps, function(prop, colIndex) {
                    var value = uvUtil.resolveValue(prop, modelRow, colProp, rowIndex, colIndex);
                    value = uvUtil.getTextFromConfig(value);
                    row.values.push(value);
                }, row);
                return row;
            }

            return {
                createRow: _createRow,

                createRowsFromArrayModel: function(config, model) {
                    var rows = [];
                    angular.forEach(model, function(modelRow, rowIndex){
                        this.push(_createRow(config, modelRow, rowIndex));
                    }, rows);
                    return rows;
                },

                createRowsFromObjectModel: function(config, model){
                    var rows = [];
                    var colProps = config.colProps;

                    if(!colProps){
                        // Handle single object without colProps defined
                        rows.push(_createRow(config, model, null, 0));
                    } else {
                        angular.forEach(colProps, function(colProp, rowIndex){
                            var cellModel = model[colProp];
                            if(angular.isDefined(cellModel)){
                                this.push(_createRow(config, model[colProp], colProp, rowIndex));
                            }
                        }, rows);
                    }

                    return rows;
                }

            };
        }
    ]);
