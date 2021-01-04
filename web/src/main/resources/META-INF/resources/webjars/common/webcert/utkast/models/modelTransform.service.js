/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.domain.ModelTransformService',
    ['common.ObjectHelper', function(ObjectHelper) {
        'use strict';

        return {
            toStringFilter: function(fromFrontend){
                if(typeof fromFrontend === 'string'){
                    fromFrontend = fromFrontend.trim();
                }

                if(fromFrontend === ''){
                    fromFrontend = undefined;
                }

                return fromFrontend;
            },
            toTypeTransform: function(fromApp) {

                var transportModel = [];

                angular.forEach(fromApp, function(value, key) {
                    if(value === true) {
                        transportModel.push({val: parseInt(key, 10)});
                    }
                }, fromApp);

                return transportModel;
            },
            fromTypeTransform: function(fromBackend) {

                var modelInternal = {};

                for(var backendPropIndex = 0; backendPropIndex < fromBackend.length; backendPropIndex++) {
                    modelInternal[fromBackend[backendPropIndex].val] = true;
                }
                return modelInternal;
            },
            enumToTransform: function(fromApp, propName) {

                if(!propName) {
                    propName = 'typ';
                }

                var transportModel = [];

                angular.forEach(fromApp, function(value, key) {
                    if(value === true) {
                        var propObject = {};
                        propObject[propName] = key;
                        transportModel.push(propObject);
                    }
                }, fromApp);

                return transportModel;
            },
            enumFromTransform: function(fromBackend, propName) {

                if(!propName) {
                    propName = 'typ';
                }

                var modelInternal = {};

                for(var backendPropIndex = 0; backendPropIndex < fromBackend.length; backendPropIndex++) {
                    modelInternal[fromBackend[backendPropIndex][propName]] = true;
                }
                return modelInternal;
            },
            underlagFromTransform: function(underlagArray) {

                // We now always have a specific amount of underlag so add that number of empty elements
                for(var i = 0; underlagArray.length < 3; i++){
                    underlagArray.push({
                        typ: null,
                        datum: null,
                        hamtasFran: null
                    });
                }

                if (underlagArray) {
                    underlagArray.forEach(function(underlag) {
                        if (!underlag.typ) {
                            underlag.typ = null;
                        }
                        if (!underlag.datum) {
                            underlag.datum = null;
                        }
                        if (!underlag.hamtasFran) {
                            underlag.hamtasFran = null;
                        }
                    });
                }
                return underlagArray;
            },
            underlagToTransform: function(underlagArray) {

                var underlagCopy = angular.copy(underlagArray);

                // delete all rows with no values at all so as to not confuse backend with non-errors
                var i = underlagCopy.length - 1;
                while(i >= 0) {
                    if(ObjectHelper.isEmpty(underlagCopy[i].typ) &&
                        ObjectHelper.isEmpty(underlagCopy[i].datum) &&
                        ObjectHelper.isEmpty(underlagCopy[i].hamtasFran)){
                        underlagCopy.splice(i, 1);
                    } else {
                        break;
                    }
                    i--;
                }

                return underlagCopy;
            },
            diagnosFromTransform: function(diagnosArray) {

                // We now always have a specific amount of underlag so add that number of empty elements
                for(var i = 0; diagnosArray.length < 3; i++){
                    diagnosArray.push({
                        diagnosKodSystem: 'ICD_10_SE',
                        diagnosKod : undefined,
                        diagnosBeskrivning : undefined
                    });
                }

                return diagnosArray;
            },
            diagnosToTransform: function(diagnosArray) {
                var diagnosCopy = angular.copy(diagnosArray);

                // delete all rows with no values at all so as to not confuse backend with non-errors
                var i = diagnosCopy.length - 1;
                while(i >= 0) {
                    if(ObjectHelper.isEmpty(diagnosCopy[i].diagnosKod) &&
                        ObjectHelper.isEmpty(diagnosCopy[i].diagnosBeskrivning)){
                        diagnosCopy.splice(i, 1);
                    } else {
                        break;
                    }
                    i--;
                }

                return diagnosCopy;
            }
        };
    }]);
