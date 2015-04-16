angular.module('common').factory('common.domain.AtticService',
    [ 'common.domain.ModelAttr', function( ModelAttr ) {
        'use strict';


        var AtticModel = Class._extend({

            init : function(model){
                this.atticModel = {};
                angular.copy(model, this.atticModel);
            },

            // attic functions
            isInAttic : function(properties) {
                var atticModel = this.atticModel;

                var cp = this.getProperties(atticModel, properties);

                var checkProp = function(prop) {
                    if(prop instanceof ModelAttr){
                        if(atticModel.hasOwnProperty(prop.property)){
                            prop = prop.property;
                        }
                    }
                    return atticModel.hasOwnProperty(prop) && atticModel[prop] !== undefined;
                }

                for(var i = 0; i<cp.props.length; i++){
                    var prop = cp.props[i];
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
                if(this.isString(properties)){
                    // work out the path
                    var cp = model._getPropertiesAndCurrent(properties);
                    var am = this.atticModel._getPropertiesAndCurrent(properties);
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

            update : function(model, properties) {
                var atticModel = this.atticModel;

                var cp = this.getProperties(model, properties);

                var thisUpdate = this._update;
                this._update(cp.current, cp.props, cp.atticModel, thisUpdate);
            },

            // private recursive method
            _update : function(model, properties, atticModel, thisUpdate) {

                angular.forEach(properties, function(prop, key){
                    //console.log('######## key : ' + key + ', prop ' + JSON.stringify(prop));
                    //console.log('## model : ' + JSON.stringify(model));
                    //console.log('## properties : ' + JSON.stringify(properties));
                    //console.log('## atticModel : ' + JSON.stringify(atticModel));

                    if(prop instanceof ModelAttr){
                        //console.log('-- ma');
                        if(atticModel.hasOwnProperty(prop.property) && this.hasOwnProperty(prop.property)){
                            atticModel[prop.property] = this[prop.property];
                        }
                    } else if( prop instanceof Array ){
                        thisUpdate(this, prop, atticModel, thisUpdate);
                    } else if(typeof prop === 'object'){
                        //console.log('-- object');
                        thisUpdate(model[key], properties[key], atticModel[key], thisUpdate);
                    } else {
                        //console.log('-- prop');
                        if (atticModel !== undefined && atticModel.hasOwnProperty(prop) && this.hasOwnProperty(prop)) {
                            atticModel[prop] = this[prop];
                        }
                    }
                }, model);
            },

            restore : function(model, properties) {

                var cp = this.getProperties(model, properties);

                var thisRestore = this._restore;
                this._restore(cp.current, cp.props, cp.atticModel, thisRestore);
            },

            _restore : function(model, properties, atticModel, thisRestore){
                //console.log('+++++++++++++++++++++++++++++');
                //console.log('++ model : ' + JSON.stringify(model));
                //console.log('++ properties : ' + JSON.stringify(properties));
                //console.log('++ atticModel : ' + JSON.stringify(atticModel));

                angular.forEach(properties, function(prop, key){
                    //console.log('++ key : ' + key + ', prop ' + JSON.stringify(prop));

                    if(prop instanceof ModelAttr){
                        if(atticModel.hasOwnProperty(prop.property) && model.hasOwnProperty(prop.property)){
                            model[prop.property] = atticModel[prop.property];
                        }
                    } else if( prop instanceof Array ){
                        thisRestore(this, prop, atticModel, thisRestore);
                    } else if(typeof prop === 'object'){
                        //console.log('-- object');
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