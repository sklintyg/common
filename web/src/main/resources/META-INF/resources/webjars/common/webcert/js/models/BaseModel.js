angular.module('common').factory('common.domain.BaseModel',
    ['common.domain.ModelAttr', function( ModelAttr ) {
        'use strict';

        var BaseModel = Class.extend({
            init : function(name, properties){
                this.name = name;
                this.properties = properties;
                angular.forEach(properties, function(prop) {
                    var initProp = function(self, prop){
                        if (prop instanceof ModelAttr) {
                            self[prop.property] = prop.defaultValue;
                        } else {
                            self[prop] = undefined;
                        }
                    };
                    if (prop instanceof Array) {
                        angular.forEach(prop, function(aprop) {
                            initProp(this, aprop);
                        }, this );
                    } else {
                        initProp(this, prop);
                    }
                }, this);
                this.updateCount = 0;
            },

            update : function(content, properties) {
                // refresh the model data
                if(properties === undefined){
                    properties = this.properties;
                }
                if (content !== undefined) {
                    var updateProp = function(self, prop){
                        if (prop instanceof ModelAttr) {
                            if(content.hasOwnProperty(prop.property) && self.hasOwnProperty(prop.property)){
                                if(prop.fromTransform !== undefined){
                                    self[prop.property] = prop.fromTransform(content[prop.property]);
                                } else if( self[prop.property].update !== undefined ){
                                    self[prop.property].update( content[prop.property] );
                                } else {
                                    self[prop.property] = content[prop.property];
                                }
                            }
                        } else {
                            if (content.hasOwnProperty(prop) && self.hasOwnProperty(prop)) {
                                self[prop] = content[prop];
                            }
                        }
                    };
                    angular.forEach(properties, function(prop) {
                        if (prop instanceof Array) {
                            angular.forEach(prop, function(aprop) {
                                updateProp(this, aprop);
                            }, this );
                        } else {
                            updateProp(this, prop);
                        }
                    }, this);

                }

                this.updateCount ++;
            },

            clear : function(properties) {
                if(properties === undefined){
                    properties = this.properties;
                }
                this._clear(properties, this);
            },

            _clear : function(properties, self){
                angular.forEach(properties, function(prop){
                    if(prop instanceof ModelAttr){
                        if(this.hasOwnProperty(prop.property)){
                            if(prop.defaultValue !== undefined){
                                this[prop.property] = prop.defaultValue;
                            } else {
                                this[prop.property] = undefined;
                            }

                        }
                    } else if (prop instanceof Array) {
                        this._clear(prop, self);
                    } else {
                        if(this.hasOwnProperty(prop)){
                            this[prop] = undefined;
                        }
                    }

                }, this);
            },

            toSendModel : function(properties) {
                var toModel = {};

                if(properties === undefined){
                    properties = this.properties;
                }
                var self = this;
                var setModelVal = function(prop){
                    if(prop instanceof ModelAttr){
                        if(self.hasOwnProperty(prop.property) && !prop.trans && self[prop.property] !== undefined){
                            toModel[prop.property] = self[prop.property];
                        }
                    } else {
                        if(self.hasOwnProperty(prop)){
                            if(self[prop] !== undefined) {
                                toModel[prop] = self[prop];
                            }
                        }
                    }
                }

                angular.forEach(properties, function(prop){
                    if (prop instanceof Array) {
                        angular.forEach(prop, function(aprop) {
                            setModelVal(aprop);
                        }, this );
                    } else {
                        setModelVal(prop);
                    }

                }, this);
                return toModel;
            }

        });

        return BaseModel;
    }]);
