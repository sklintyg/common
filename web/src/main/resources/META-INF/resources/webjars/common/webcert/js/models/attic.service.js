angular.module('common').factory('common.domain.AtticService',
    [ 'common.domain.ModelAttr', function( ModelAttr ) {
        'use strict';


        var AtticModel = Class.extend({

            init : function(model){
                this.atticModel = {};
                angular.copy(model, this.atticModel);
            },

            // attic functions
            isInAttic : function(properties) {
                var atticModel = this.atticModel;
                if(properties === undefined){
                    properties = this.getProperties(atticModel, properties);
                }

                var checkProp = function(prop) {
                    if(prop instanceof ModelAttr){
                        if(atticModel.hasOwnProperty(prop.property)){
                            prop = prop.property;
                        }
                    }
                    return atticModel.hasOwnProperty(prop) && atticModel[prop] !== undefined;
                }

                for(var i = 0; i<properties.length; i++){
                    var prop = properties[i];
                    if(prop instanceof Array){
                        for(var j = 0; j<prop.length; j++){
                            if(checkProp(prop[j])){
                                return true;
                            }
                        }
                    } else if(checkProp(prop)){
                        return true;
                    }
                }
                return false;
            },

            getProperties: function(model, properties) {
                if(typeof model.properties === 'function' ){
                    properties = model.properties();
                } else {
                    properties = model.properties;
                }
                return properties;
            },

            update : function(model, properties) {
                var atticModel = this.atticModel;
                if(properties === undefined){
                    properties = this.getProperties(model, properties);
                }
                var thisUpdate = this._update;
                this._update(model, properties, atticModel, thisUpdate);
            },

            // private recursive method
            _update : function(model, properties, atticModel, thisUpdate) {
                angular.forEach(properties, function(prop){
                    if(prop instanceof ModelAttr){
                        if(atticModel.hasOwnProperty(prop.property) && this.hasOwnProperty(prop.property)){
                            atticModel[prop.property] = this[prop.property];
                        }
                    } else if( prop instanceof Array ){
                        thisUpdate(this, prop, atticModel, thisUpdate);
                    } else {
                        if (atticModel.hasOwnProperty(prop) && this.hasOwnProperty(prop)) {
                            atticModel[prop] = this[prop];
                        }
                    }
                }, model);
            },

            restore : function(model, properties) {
                var atticModel = this.atticModel;
                if(properties === undefined){
                    properties = this.getProperties(model, properties);
                }
                var thisRestore = this._restore;
                this._restore(model, properties, atticModel, thisRestore);
            },

            _restore : function(model, properties, atticModel, thisRestore){
                angular.forEach(properties, function(prop){
                    if(prop instanceof ModelAttr){
                        if(atticModel.hasOwnProperty(prop.property) && model.hasOwnProperty(prop.property)){
                            model[prop.property] = atticModel[prop.property];
                        }
                    } else if( prop instanceof Array ){
                        thisRestore(this, prop, atticModel, thisRestore);
                    } else {
                        if (atticModel.hasOwnProperty(prop) && model.hasOwnProperty(prop)) {
                            model[prop] = atticModel[prop];
                        }
                    }
                }, model);
            }
        });

        var AtticService = Class.extend({
            init : function(){
                this.atticModels = {};
            },

            addAtticModel : function(model){
                if(this.atticModels[model.name] === undefined){
                    this.atticModels[model.name] = new AtticModel(model);
                }
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