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
angular.module('common').factory('common.domain.BaseModel',
    ['$log', 'common.domain.ModelAttr', function( $log, ModelAttr ) {
        'use strict';

        var BaseModel = Class._extend(
            // member methods
            {
            init : function(name, properties){
                this.name = name;
                this.properties = properties;

                // NOTE: for modelAttr models key and value in object must be named the same ex: 'a': new ModelAttr('a' ...)

                // this gets executed once the property has been recursed
                var initProp = function(current, prop, extras){
                    if (extras.self.isModel(prop)) {
                        if(extras.key !== undefined){
                            current[extras.key] = prop;
                        } else {
                            current[prop.property] = prop;
                        }
                    } else if (prop instanceof ModelAttr) {
                        _initModelAttrProp( extras, prop, current );
                    } else if(extras && extras.key !== undefined && !extras.self.isNumber(extras.key)){
                        if(extras.self.isObject(prop)){
                            // set a new object on the current this then becomes the new current
                            current[extras.key] = {};
                            return current[extras.key];
                        } else {
                            if(extras.self.isFunction(prop)){
                                current[extras.key] = prop(extras.key);
                            } else {
                                current[extras.key] = prop; // prop is the default value, or an object
                            }

                            extras.ep[extras.key] = new ModelAttr(extras.key, {defaultValue:prop});
                        }

                    } else {
                        current[prop] = undefined;
                        var ma = new ModelAttr(prop, {defaultValue:undefined});
                        extras.ep[prop] = ma;
                    }
                    return current;
                };

                this._recurse(this, properties, initProp, {self:this, ec:this, ep:properties});

                this.updateCount = 0;

            },

            // utils
            isArray : function(val){
                return val !== undefined && val instanceof Array;
            },

            isString : function(val){
                return val !== undefined && typeof val === 'string';
            },

            isFunction : function(val){
                return val !== undefined && typeof val === 'function';
            },

            isObject : function(val){
                return val !== undefined && typeof val === 'object' && !(val instanceof Date);
            },

            isModelAttr : function isModelAttr(val){
                return val instanceof ModelAttr;
            },

            isNumber : function(val){
                return val !== undefined && typeof val === 'number';
            },

            isModel : function(val){
              return val !== undefined && val !== null && val.update !== undefined;
            },

            _recurse : function _recurse(currentSelf, props, propFn, extras){

                function keyIsString(key, val) {
                    // case 1 property is an object the key is it's name ....
                    if (extras.self.isObject(val) && !extras.self.isString(val)) {
                        // recurse on the objects properties

                        extras.key = key;
                        extras.ec = propFn(currentSelf, val, extras);
                        extras.key = undefined;

                        if (!extras.self.isModelAttr(val) && !extras.self.isModel(val)) {
                            // only recurse if it's a standard object or type
                            // not if its a ModelAttr or another Model
                            _recurse(extras.ec, val, propFn, extras);
                        }

                    } else {
                        // case 2 property is a simple property
                        // with a defaultValue ( undefined, boolean, string etc ..
                        extras.key = key;
                        propFn(currentSelf, val, extras);
                        extras.key = undefined;
                    }
                }

                if(extras.self.isModelAttr(props) || !(extras.self.isArray(props) || extras.self.isObject(props))){
                    propFn(currentSelf, props, extras);
                } else {
                    angular.forEach(props, function(val, key) {

                        var epb = props;
                        var ecb = currentSelf;
                        var cb = extras.content;
                        var tm = extras.tm;

                        extras.ep = props;
                        extras.ec = currentSelf;

                        if (extras.self.isString(key) && !(extras.self.isArray(val))) {
                            keyIsString(key, val);
                        } else if (extras.self.isArray(val) ||
                            (extras.self.isObject(val) && !extras.self.isModelAttr(val))) {

                            _recurse(currentSelf, val, propFn, extras);
                        } else {
                            propFn(currentSelf, val, extras);

                        }
                        // after recursion reset the enclosing props and this
                        extras.ep = epb;
                        extras.ec = ecb;
                        extras.content = cb;
                        extras.tm = tm;
                    }, currentSelf);
                }

            },

            clear : function(clearProperty, model) {
                // clear property clearProperty on model
                if(clearProperty === undefined){
                    clearProperty = this.properties;
                }
                if(model === undefined){
                    model = this;
                }

                if(this.isString(clearProperty)){
                    // work out the path
                    var propsCurrent = this._getPropertiesAndCurrent(clearProperty);
                    model = propsCurrent.current;
                    clearProperty = propsCurrent.props;

                } else if(this.isArray(clearProperty)){
                    // it's a simple array which will work on the main model
                    model = this;
                }

                var clearFn = function(model, prop, extras){
                    if(prop instanceof ModelAttr && model.hasOwnProperty(prop.property)){
                        if(prop.defaultValue !== undefined){
                            model[prop.property] = angular.copy(prop.defaultValue);
                        } else {
                            model[prop.property] = undefined;
                        }
                    }
                    if(extras.self.isObject(prop)){
                        if(!extras.key && prop.property){
                            return model[prop.property];
                        } else {
                            return model[extras.key];
                        }
                    } else {
                        if(model.hasOwnProperty(prop)){
                            model[prop] = undefined;
                        }
                    }
                };
                this._recurse(model, clearProperty, clearFn, {self:this, ec:model, ep:clearProperty});
            },

            update : function(content, properties) {
                var propsSent = properties !== undefined ? true : false;
                // refresh the model data
                var current = this;
                if(properties === undefined){
                    properties = this.properties;
                }
                if(this.isString(properties)){
                    // work out the path
                    var pc = this._getPropertiesAndCurrent(properties);
                    properties = pc.props;
                    current = pc.current;
                }

                if (content !== undefined) {
                    var updateProp = _updateProp( propsSent );
                    this._recurse(current, properties, updateProp, {self:this,content:content, ec:current, ep:properties} );
                }

                this.updateCount ++;
            },

            toSendModel : function(properties) {
                var toModel = {};
                var current = this;
                if(properties === undefined){
                    properties = this.properties;
                }
                if(this.isString(properties)){
                    // work out the path
                    var pc = this._getPropertiesAndCurrent(properties);
                    properties = pc.props;
                    current = pc.current;
                }
                var toModelFn = function(current, prop, extras){
                    if(extras.self.isModelAttr(prop)){
                        if(current.hasOwnProperty(prop.property) && !prop.trans && current[prop.property] !== undefined){
                            if(typeof prop.toTransform !== 'undefined') {
                                extras.tm[prop.property] = prop.toTransform(current[prop.property]);
                            }
                            else {
                                extras.tm[prop.property] = current[prop.property];
                            }
                        }
                    } else if(extras.self.isModel(prop)){
                        var child = current[extras.key];
                        if(current.hasOwnProperty(extras.key) && child !== undefined ){
                            if(child.toSendModel !== undefined){
                                extras.tm[extras.key] = child.toSendModel();
                            } else {
                                extras.tm[extras.key] = child;
                            }
                        }
                    } else if(extras.self.isObject(prop)){
                        // set a new object on the current this then becomes the new current
                        extras.tm[extras.key] = {};
                        extras.tm = extras.tm[extras.key];
                        return current[extras.key];

                    } else {
                        if(current.hasOwnProperty(prop) && current[prop] !== undefined) {
                            extras.tm[prop] = current[prop];
                        }
                    }
                };

                this._recurse(current, properties, toModelFn, {self:this, tm:toModel, ec:current, ep:properties});

                return toModel;
            },

            _getPropertiesAndCurrent: function(propertyPath){

                var nc;

                function findPropsCurrent(baseModel, props, propsCurrent) {
                    for(var j = 0; j < props.length; j++){
                        var prop = props[j];
                        if (!baseModel.isModelAttr(propsCurrent.props[prop]) &&
                            (baseModel.isObject(propsCurrent.props[prop]) || baseModel.isArray(propsCurrent.props[prop]))) {
                            nc = propsCurrent.current[prop];
                            if(nc){
                                propsCurrent.current = nc;
                            }
                        }
                        if (baseModel.isObject(propsCurrent.props[prop])) {
                            propsCurrent.props = propsCurrent.props[prop];
                        }
                        else {
                            propsCurrent.props = prop;
                        }
                    }
                }

                var propsCurrent = {props:this.properties, current:this};

                var arrayIndex = propertyPath.lastIndexOf('[0]');
                if (arrayIndex > -1) {
                    propertyPath = propertyPath.substring(0, arrayIndex);
                }

                var i = propertyPath.lastIndexOf('.');
                if(i > -1 ){
                    var props = propertyPath.split('.');
                    findPropsCurrent(this, props, propsCurrent);
                } else {
                    nc = this[propertyPath];
                    // Added check for isModelAttr
                    // propsCurrent.current can not be set to nc when clearing a field with a single ModelAttr
                    // propsCurrent.current needs to be set to nc when clearing a field with child properties
                    if (nc && !this.isModelAttr(this.properties[propertyPath])) {
                        propsCurrent.current = nc;
                    }
                    propsCurrent.props = this.properties[propertyPath];
                }
                return propsCurrent;
            }

        },
        // class methods
        {
            isArray : function(val){
                return val !== undefined && val instanceof Array;
            },

            isString : function(val){
                return val !== undefined && typeof val === 'string';
            },
            isObject : function(val){
                return val !== undefined && typeof val === 'object';
            }
        });

        function _updateLinkedProperty(extras, prop, current) {
            current['set' + prop.property] = prop.linkedProperty.set;

            var lps = {};

            function updateProp(self, lps, id, oldval, newval) {
                lps[id] = newval;
                self[prop.property] = prop.linkedProperty.update(self, lps);
                return newval;
            }

            for (var i = 0; i < prop.linkedProperty.props.length; i++) {
                var lp = prop.linkedProperty.props[i];

                var self;
                if (lp.indexOf('.') > 0) {
                    self = extras.self;
                } else {
                    self = current;
                }

                lps[lp] = undefined;

                self.watch(lp, angular.bind(self, updateProp, self, lps));
            }
        }

        /* Initialize ModelAttr props, extracted from initModel due cyclomatic comlexity*/
        function _initModelAttrProp( extras, prop, current ) {
            if(extras.key !== undefined){
                if(extras.self.isObject(prop.defaultValue)){
                    current[extras.key] = angular.copy(prop.defaultValue);
                } else {
                    current[extras.key] = prop.defaultValue;
                }
            } else {
                if(extras.self.isObject(prop.defaultValue)){
                    current[prop.property] = angular.copy(prop.defaultValue);
                } else {
                    current[prop.property] = prop.defaultValue;
                }
            }

            if(prop.linkedProperty){
                _updateLinkedProperty(extras, prop, current);
            }
        }

        /* Update props, extracted due to cyclomatic comlexity,
        * returns a function that is a closure over propsSent*/
        function _updateProp( propsSent ){
            return function( current, prop, extras ) {
                    if (prop instanceof ModelAttr) {
                        _updateModelAttrProp( current, prop, extras, propsSent);
                    } else if(extras.self.isModel(prop)){
                        current[extras.key].update(extras.content[extras.key]);
                    } else if(extras.self.isObject(prop)){
                        extras.content = extras.content[extras.key];
                        return current[extras.key];
                    } else {
                        if (extras.content.hasOwnProperty(prop) && current.hasOwnProperty(prop)) {
                            current[prop] = extras.content[prop];
                        } else if(extras.content && propsSent ){
                            current[prop] = extras.content;
                        }
                    }
                };
        }

        function _updateModelAttrProp( current, prop, extras, propsSent ) {
            if(extras.content &&
                (prop.linkedProperty ||
                    (extras.content.hasOwnProperty(prop.property) && current.hasOwnProperty(prop.property)))) {
                if (prop.fromTransform !== undefined) {
                    current[prop.property] = prop.fromTransform(extras.content[prop.property]);
                } else if (extras.self.isModel(current[prop.property])) {
                    current[prop.property].update(extras.content[prop.property]);
                } else if(!prop.linkedProperty) { // jshint ignore:line
                    current[prop.property] = extras.content[prop.property];
                }
            } else if(extras.content && propsSent ){ // single property just set it on the property
                current[prop.property] = extras.content;
            }
        }

        return BaseModel;
    }]);
