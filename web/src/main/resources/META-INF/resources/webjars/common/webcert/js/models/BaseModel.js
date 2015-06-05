angular.module('common').factory('common.domain.BaseModel',
    ['common.domain.ModelAttr', function( ModelAttr ) {
        'use strict';

        var BaseModel = Class._extend(
            // member methods
            {
            init : function(name, properties){
                this.name = name;
                this.properties = properties;

                // this gets executed once the property has been recursed
                var initProp = function(current, prop, extras){
                    //console.log('------------------------ initProp');
                    //console.log('-- ec : ' + JSON.stringify(extras.ec));
                    //console.log('-- ep : ' + JSON.stringify(extras.ep));
                    //console.log('-- prop '+ extras.key +' : ' + JSON.stringify(prop));
                    if (extras.self.isModel(prop)) {
                        if(extras.key !== undefined){
                            current[extras.key] = prop;
                        } else {
                            current[prop.property] = prop;
                        }
                    } else if (prop instanceof ModelAttr) {
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
                    } else if(extras && extras.key !== undefined && !extras.self.isNumber(extras.key)){
                        if(extras.self.isObject(prop)){
                            //console.log('-- object prop: ' + extras.key);
                            // set a new object on the current this then becomes the new current
                            current[extras.key] = {};
                            return current[extras.key];
                        } else {
                            //console.log('setting prop: ' + extras.key);
                            if(extras.self.isFunction(prop)){
                                current[extras.key] = prop(extras.key);
                            } else {
                                current[extras.key] = prop; // prop is the default value, or an object
                            }

                            //console.log('-- MA('+extras.key+') default value : ' + prop);
                            extras.ep[extras.key] = new ModelAttr(extras.key, {defaultValue:prop});

                        }

                    } else {
                        //console.log('-- simple ' + prop + ':undefined, current: ' + JSON.stringify(current));
                        current[prop] = undefined;
                        var ma = new ModelAttr(prop, {defaultValue:undefined});
                        //console.log('-- MA('+prop+') default value : undefined');
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

            isModelAttr : function(val){
                return val instanceof ModelAttr;
            },

            isNumber : function(val){
                return val !== undefined && typeof val === 'number';
            },

            isModel : function(val){
              return val != undefined && val.update !== undefined;
            },

            _recurse : function _recurse(currentSelf, props, propFn, extras){

                if(extras.self.isModelAttr(props)){
                    propFn(currentSelf, props, extras);
                } else {
                    angular.forEach(props, function(val, key) {
                        //console.log('******************************************* recurs');

                        var epb = props;
                        var ecb = currentSelf;
                        var cb = extras.content;
                        var tm = extras.tm;

                        extras.ep = props;
                        extras.ec = currentSelf;

                        if (extras.self.isString(key) && !(extras.self.isArray(val))) {
                            //console.log('********** key value');
                            //console.log('** ec   ' + key + ', :' + JSON.stringify(extras.ec));
                            //console.log('** ep   ' + key + ', :' + JSON.stringify(extras.ep));
                            //console.log('** prop ' + key + ', :' + JSON.stringify(val));
                            // case 1 property is an object the key is it's name ....
                            if (extras.self.isObject(val) && !extras.self.isString(val)) {
                                // recurse on the objects properties
                                //console.log('** case1 isObject');

                                extras.key = key;

                                extras.ec = propFn(currentSelf, val, extras);

                                extras.key = undefined;

                                if (!extras.self.isModelAttr(val) && !extras.self.isModel(val)) {
                                    // only recurse if it's a standard object or type
                                    // not if its a ModelAttr or another Model
                                    //console.log('wasnt ma about to recurse ...');
                                    _recurse(extras.ec, val, propFn, extras);
                                }


                            } else {
                                // case 2 property is a simple property
                                // with a defaultValue ( undefined, boolean, string etc ..
                                //console.log('** case2 isSimple prop');
                                extras.key = key;
                                propFn(currentSelf, val, extras);
                                extras.key = undefined;
                            }
                        } else if (extras.self.isArray(val)) {
                            //console.log('********** array');
                            //console.log('** array : ' + JSON.stringify(val) + ', key:' + key);

                            _recurse(currentSelf, val, propFn, extras);
                        } else if (extras.self.isObject(val) && !extras.self.isModelAttr(val)) {
                            //console.log('********** object');
                            //console.log('** object : ' + JSON.stringify(val));

                            _recurse(currentSelf, val, propFn, extras);
                        } else {
                            //console.log('********** other');
                            //console.log('** other : ' + JSON.stringify(val));
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

            clear : function(properties,current) {
                if(properties === undefined){
                    properties = this.properties;
                }
                if(current === undefined){
                    current = this;
                }

                if(this.isString(properties)){
                    // work out the path
                    var propsCurrent = this._getPropertiesAndCurrent(properties);
                    current = propsCurrent.current;
                    properties = propsCurrent.props;

                } else if(this.isArray(properties)){
                    // it's a simple array which will work on the main model
                    current = this;
                }

                var clearFn = function(current, prop, extras){
                    //console.log('------------------------ clear');
                    //console.log('-- ec : ' + JSON.stringify(extras.ec));
                    //console.log('-- ep : ' + JSON.stringify(extras.ep));
                    //console.log('-- prop '+ extras.key +' : ' + JSON.stringify(prop));
                    if(prop instanceof ModelAttr){
                        //console.log('-- ma');
                        if(current.hasOwnProperty(prop.property)){
                            if(prop.defaultValue !== undefined){
                                //console.log('-- defaultValue : ' + prop.defaultValue);
                                current[prop.property] = prop.defaultValue;
                            } else {
                                current[prop.property] = undefined;
                            }

                        }
                    } if(extras.self.isObject(prop)){
                        //console.log('-- object');
                        return current[extras.key];
                    } else {
                        if(current.hasOwnProperty(prop)){
                            //console.log('-- undefined');
                            current[prop] = undefined;
                        }
                    }
                };
                this._recurse(current, properties, clearFn, {self:this, ec:current, ep:properties});
            },

            update : function(content, properties) {
                //console.log('update bm');
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

                    var updateProp = function updateProp(current, prop, extras){

                        //console.log('------------------------ update');
                        //console.log('-- current : ' + JSON.stringify(current));
                        //console.log('-- content : ' + JSON.stringify(extras.content));
                        //console.log('-- ec : ' + JSON.stringify(extras.ec));
                        //console.log('-- ep : ' + JSON.stringify(extras.ep));
                        //console.log('-- prop '+ extras.key +' : ' + JSON.stringify(prop));
                        //console.log('-- isMA ' + prop instanceof ModelAttr );
                        if (prop instanceof ModelAttr) {
                            //console.log('--- ma');
                            if(extras.content && extras.content.hasOwnProperty(prop.property) && current.hasOwnProperty(prop.property)) {
                                if (prop.fromTransform !== undefined) {
                                    //console.log('---- update transform');
                                    current[prop.property] = prop.fromTransform(extras.content[prop.property]);
                                } else if (extras.self.isModel(current[prop.property])) {
                                    //console.log('---- update child model');
                                    current[prop.property].update(extras.content[prop.property]);
                                } else {
                                    //console.log('---- update prop');
                                    current[prop.property] = extras.content[prop.property];
                                }
                            } else if(extras.content && propsSent ){ // single property just set it on the property
                                //console.log('---- update single prop with : ' + extras.content);
                                current[prop.property] = extras.content;
                            }
                        } else if(extras.self.isModel(prop)){
                            //console.log('---- update child model');
                            current[extras.key].update(extras.content[extras.key]);
                        } else if(extras.self.isObject(prop)){
                            //console.log('-- object');
                            extras.content = extras.content[extras.key];
                            return current[extras.key];

                        } else {
                            //console.log('--- prop');
                            if (extras.content.hasOwnProperty(prop) && current.hasOwnProperty(prop)) {
                                current[prop] = extras.content[prop];
                            } else if(extras.content && propsSent ){
                                current[prop] = extras.content;
                            }
                        }
                    };

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
                    //console.log('------------------------ to model');
                    //console.log('-- current : ' + JSON.stringify(current));
                    //console.log('-- tm : ' + JSON.stringify(extras.tm));
                    //console.log('-- ec : ' + JSON.stringify(extras.ec));
                    //console.log('-- ep : ' + JSON.stringify(extras.ep));
                    //console.log('-- prop '+ extras.key +' : ' + JSON.stringify(prop));

                    if(extras.self.isModelAttr(prop)){
                        if(current.hasOwnProperty(prop.property) && !prop.trans && current[prop.property] !== undefined){
                            extras.tm[prop.property] = current[prop.property];
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
                        //console.log('-- object prop: ' + extras.key);
                        // set a new object on the current this then becomes the new current
                        extras.tm[extras.key] = {};
                        extras.tm = extras.tm[extras.key];
                        return current[extras.key];

                    } else {
                        if(current.hasOwnProperty(prop)){
                            if(current[prop] !== undefined) {
                                extras.tm[prop] = current[prop];
                            }
                        }
                    }
                }

                this._recurse(current, properties, toModelFn, {self:this, tm:toModel, ec:current, ep:properties});

                return toModel;
            },

            _getPropertiesAndCurrent: function(propertyPath){
                var propsCurrent = {props:this.properties, current:this};
                var i = propertyPath.lastIndexOf('.');
                var nc;
                if(i > -1 ){
                    var props = propertyPath.split('.');
                    for(var j = 0; j<props.length; j++){
                        var prop = props[j];
                        if(!this.isModelAttr(propsCurrent.props[prop])) {
                            nc = propsCurrent.current[prop];
                            if(nc){
                                propsCurrent.current = nc;
                            }
                        }
                        propsCurrent.props = propsCurrent.props[prop];
                    }
                } else {
                    nc = this[propertyPath];
                    if(nc){
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

        return BaseModel;
    }]);
