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
angular.module('doi').factory('doi.Domain.IntygModel.v1',
    ['common.Domain.GrundDataModel', 'common.Domain.DraftModel', 'common.domain.ModelAttr',
        'common.domain.BaseAtticModel', 'common.ObjectHelper',
        function(GrundData, DraftModel, ModelAttr, BaseAtticModel, ObjectHelper) {
            'use strict';

            var DoiModel = BaseAtticModel._extend({
                init: function init() {
                    var grundData = GrundData.build();
                    init._super.call(this, 'doiModel', {

                        'id': undefined,
                        'typ': undefined,
                        'textVersion': undefined,
                        'grundData': grundData,

                        'identitetStyrkt': undefined,
                        'dodsdatumSakert': undefined,
                        'dodsdatum': new ModelAttr('dodsdatum', {
                            defaultValue: '',
                            fromTransform: function(fromBackend) {
                                var toFrontend = fromBackend;
                                return toFrontend;
                            },
                            toTransform: function(fromFrontend) {
                                var toBackend = angular.copy(fromFrontend);
                                return toBackend;
                            }
                        }),
                        'antraffatDodDatum': undefined,
                        'dodsplatsKommun': undefined,
                        'dodsplatsBoende': undefined,
                        'barn': undefined,
                        'land': undefined,

                        'terminalDodsorsak': new ModelAttr('terminalDodsorsak', {
                            // for some reason null doesn't work as default value for specifikation for this component due to ui-select using undefined and null as reference internally
                            // (default value sometimes won't be matched with the place holder item in the control)
                            defaultValue : { beskrivning: null, datum: null, specifikation: ''},
                            fromTransform: function(fromBackend) {

                                // Terminal is always an object. convert to array so it works in the same system as everything else
                                var modelTerminal = fromBackend;
                                if(!modelTerminal || (!modelTerminal.beskrivning && !modelTerminal.datum && !modelTerminal.specifikation)){
                                    modelTerminal = this.defaultValue;
                                }

                                if(!modelTerminal.specifikation){
                                    modelTerminal.specifikation = '';
                                }

                                return modelTerminal;
                            },
                            toTransform: function(fromFrontend) {

                                var toBackend = angular.copy(fromFrontend);
                                if(Array.isArray(toBackend)){

                                    if(toBackend[0].specifikation === ''){
                                        toBackend[0].specifikation = null;
                                    }

                                    return toBackend[0];
                                } else {
                                    if(toBackend.specifikation === ''){
                                        toBackend.specifikation = null;
                                    }
                                }

                                return toBackend;
                            }
                        }),
                        'foljd': new ModelAttr('foljd', {
                            fromTransform: function(fromBackend) {

                                // for some reason null doesn't work as default value for specifikation for this component due to ui-select using undefined and null as reference internally
                                // (default value sometimes won't be matched with the place holder item in the control)
                                var modelFoljdArray = fromBackend;
                                if(!modelFoljdArray){
                                    modelFoljdArray = [
                                        { beskrivning: null, datum: null, specifikation: ''},
                                        { beskrivning: null, datum: null, specifikation: ''},
                                        { beskrivning: null, datum: null, specifikation: ''}
                                    ];
                                }

                                if(modelFoljdArray.length < 3){
                                    var missingCount = 3 - modelFoljdArray.length;
                                    for(var i = 0; i < missingCount; i++){
                                        modelFoljdArray.push({ beskrivning: null, datum: null, specifikation: ''});
                                    }
                                }

                                modelFoljdArray.forEach(function(item){
                                    if(!item.specifikation){
                                        item.specifikation = '';
                                    }
                                });

                                return modelFoljdArray;
                            },
                            toTransform: function(fromFrontend) {

                                var toBackend = angular.copy(fromFrontend);
                                if(Array.isArray(toBackend)){

                                    toBackend.forEach(function(item){
                                        if(item.specifikation === ''){
                                            item.specifikation = null;
                                        }
                                    });

                                    // Remove empty rows from the end of array until non-empty row is reached
                                    for(var i=toBackend.length-1; i>=0; i--) {
                                        var e1 = ObjectHelper.isEmpty(toBackend[i].beskrivning);
                                        var e2 = ObjectHelper.isEmpty(toBackend[i].datum);
                                        var e3 = ObjectHelper.isEmpty(toBackend[i].specifikation);
                                        if (e1 && e2 && e3) {
                                            toBackend.splice(i, 1);
                                        }
                                        else {
                                            break;
                                        }
                                    }

                                    return toBackend;
                                }

                                return toBackend;
                            }
                        }),
                        'bidragandeSjukdomar': new ModelAttr('bidragandeSjukdomar', {
                            // for some reason null doesn't work as default value for specifikation for this component due to ui-select using undefined and null as reference internally
                            // (default value sometimes won't be matched with the place holder item in the control)
                            defaultValue : [{ beskrivning: null, datum: null, specifikation: ''}],
                            fromTransform: function(fromBackend) {

                                var modelSjukdomsArray = fromBackend;
                                if(!modelSjukdomsArray || modelSjukdomsArray.length === 0){
                                    modelSjukdomsArray = this.defaultValue;
                                }

                                modelSjukdomsArray.forEach(function(item){
                                    if(!item.specifikation){
                                        item.specifikation = '';
                                    }
                                });

                                return modelSjukdomsArray;
                            },
                            toTransform: function(fromFrontend) {

                                var toBackend = angular.copy(fromFrontend);
                                if(Array.isArray(toBackend)){

                                    toBackend.forEach(function(item){
                                        if(item.specifikation === ''){
                                            item.specifikation = null;
                                        }
                                    });

                                    for(var i=toBackend.length-1; i>=0; i--) {
                                        var e1 = ObjectHelper.isEmpty(toBackend[i].beskrivning);
                                        var e2 = ObjectHelper.isEmpty(toBackend[i].datum);
                                        var e3 = ObjectHelper.isEmpty(toBackend[i].specifikation);
                                        if (e1 && e2 && e3) {
                                            toBackend.splice(i, 1);
                                        }
                                    }

                                    return toBackend;
                                }

                                return toBackend;
                            }
                        }),

                        'operation': undefined,
                        'operationDatum': undefined,
                        'operationAnledning': undefined,
                        'forgiftning': undefined,
                        'forgiftningOrsak': undefined,
                        'forgiftningDatum': undefined,
                        'forgiftningUppkommelse': undefined,
                        'grunder': new ModelAttr('grunder', {
                            defaultValue : [],
                            toTransform: function(fromApp) {

                                var transportModel = [];

                                angular.forEach(fromApp, function(value, key) {
                                    if(value === true) {
                                        transportModel.push(key);
                                    }
                                }, fromApp);

                                return transportModel;
                            },
                            fromTransform: function(fromBackend) {

                                var modelInternal = {};

                                angular.forEach(fromBackend, function(value, key) {
                                    modelInternal[value] = true;
                                });

                                return modelInternal;
                            }
                        })
                    });
                },
                update: function update(content, parent) {
                    if (parent) {
                        parent.content = this;
                    }
                    update._super.call(this, content);
                }
            }, {
                build : function(){
                    return new DraftModel(new DoiModel());
                }
            });

            /**
             * Return the constructor function IntygModel
             */
            return DoiModel;

        }]);
