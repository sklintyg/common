/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

angular.module('common').factory('common.domain.AtticService',
    [ 'common.domain.ModelAttr', 'common.ObjectHelper', function( ModelAttr, ObjectHelper ) {
        'use strict';


        var AtticModel = Class._extend({

            init : function(model){
                this.atticModel = angular.copy(model);
            },

            // attic functions
            isInAttic : function(properties) {
                var atticModel = this.atticModel;
                var cp = this.getProperties(atticModel, properties);

                var checkProp = function(prop) {
                    if(prop instanceof ModelAttr && atticModel.hasOwnProperty(prop.property)){
                        prop = prop.property;
                    }
                    return atticModel.hasOwnProperty(prop) && atticModel[prop] !== undefined;
                };

                function hasProp(prop) {
                    if(prop instanceof Array){
                        for(var j = 0; j<prop.length; j++){
                            if(checkProp(prop[j])){
                                return true;
                            }
                        }
                    } else if(checkProp(prop)){
                        return true;
                    }

                    return false;
                }

                if(cp.props instanceof ModelAttr) {
                    return hasProp(cp.props);
                } else {
                    // Assuming array for now, add cases as needed.
                    for(var i = 0; i<cp.props.length; i++){
                        var prop = cp.props[i];
                        if(hasProp(prop)) {
                            return true;
                        }
                    }
                }

                return false;
            },

            getProperties: function(model, properties, fromRestore) {
                if(this.isString(properties)){
                    // work out the path
                    var cp = model._getPropertiesAndCurrent(properties);
                    var am;
                    if(fromRestore){
                        am = {current:this._getAtticProp( properties)};
                    } else {
                        am = this.atticModel._getPropertiesAndCurrent(properties);
                    }

                    cp.atticModel = am.current;
                    return cp;
                } else if( properties !== undefined ){
                    return {props:properties,current:model, atticModel:this.atticModel};
                } else if(typeof model.properties === 'function' ){
                    properties = model.properties();
                } else  {
                    properties = model.properties;
                }
                return {props:properties,current:model, atticModel:this.atticModel};
            },

            _getAtticProp: function( propertyPath){

                var atticProp = this.atticModel;
                var i = propertyPath.lastIndexOf('.');
                var nc;
                var ps = this.atticModel.properties;

                function findAtticProp(atticProp, props) {
                    for(var j = 0; j < props.length; j++){
                        var prop = props[j];
                        ps = ps[prop];
                        if(ps.property === undefined) {
                            nc = atticProp[prop];
                            if(nc){
                                atticProp = nc;
                            }
                        }
                    }

                    return atticProp;
                }

                if(i > -1 ){
                    var props = propertyPath.split('.');
                    atticProp = findAtticProp(atticProp, props);
                } else {
                    nc = atticProp[propertyPath];
                    if(nc){
                        atticProp = nc;
                    }
                }

                return atticProp;
            },

            update : function(model, properties) {
                var cp = this.getProperties(model, properties);
                var thisUpdate = this._update;
                this._update(cp.current, cp.props, cp.atticModel, thisUpdate);
            },

            // private recursive method
            _update : function(model, properties, atticModel, thisUpdate) {

                angular.forEach(properties, function(prop, key){

                    if(prop instanceof ModelAttr){
                        if(ObjectHelper.isDefined(atticModel) && atticModel.hasOwnProperty(prop.property) && this.hasOwnProperty(prop.property)){
                            atticModel[prop.property] = this[prop.property];
                        }
                    } else if( prop instanceof Array ){
                        thisUpdate(this, prop, atticModel, thisUpdate);
                    } else if(typeof prop === 'object'){
                        thisUpdate(model[key], properties[key], atticModel[key], thisUpdate);
                    } else {
                        if (ObjectHelper.isDefined(atticModel) && atticModel.hasOwnProperty(prop) && this.hasOwnProperty(prop)) {
                            atticModel[prop] = this[prop];
                        }
                    }
                }, model);
            },

            restore : function(model, properties) {

                var cp = this.getProperties(model, properties, true);

                var thisRestore = this._restore;
                this._restore(cp.current, cp.props, cp.atticModel, thisRestore);
            },

            _restore : function(model, properties, atticModel, thisRestore){
                if(properties instanceof ModelAttr){
                    properties = [properties];
                }
                angular.forEach(properties, function(prop, key){

                    if(prop instanceof ModelAttr){
                        if(atticModel.hasOwnProperty(prop.property) && model.hasOwnProperty(prop.property)){
                            model[prop.property] = atticModel[prop.property];
                        }
                    } else if( prop instanceof Array ){
                        thisRestore(this, prop, atticModel, thisRestore);
                    } else if(typeof prop === 'object'){
                        thisRestore(model[key], properties[key], atticModel[key], thisRestore);
                    } else {
                        if (atticModel.hasOwnProperty(prop) && model.hasOwnProperty(prop)) {
                            model[prop] = atticModel[prop];
                        }
                    }
                }, model);
            },

            isString : function(val){
                return val !== undefined && typeof val === 'string';
            }

        },
        // class methods
        {

        });

        var AtticService = Class._extend({
            init : function(){
                this.atticModels = {};
            },

            addAtticModel : function(model){
                if(this.atticModels[model.name] === undefined){
                    this.atticModels[model.name] = new AtticModel(model);
                }
                return this.atticModels[model.name];
            },

            getAtticModel : function(modelName){
                return this.atticModels[modelName];
            },

            update : function(model, properties){
                var atticModel = this.getAtticModel(model.name);
                if(atticModel){
                    atticModel.update(model, properties);
                }
            },

            restore : function(model, properties){
                var atticModel = this.getAtticModel(model.name);
                if(atticModel){
                    atticModel.restore(model, properties);
                }
            },

            isInAttic : function(model, properties){
                var atticModel = this.getAtticModel(model.name);
                if(atticModel){
                    return atticModel.isInAttic(properties);
                } else {
                    return false;
                }

            }

        });

        var _atticService = new AtticService();

        return _atticService;

    }]);
