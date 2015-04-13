angular.module('common').factory('common.domain.BaseAtticModel',
    ['common.domain.ModelAttr', 'common.domain.BaseModel', 'common.domain.AtticService', function( ModelAttr, BaseModel, atticService) {
        'use strict';

        var BaseAtticModel = BaseModel.extend({
            init : function(name, properties){
                this._super(name, properties);
                atticService.addAtticModel(this);
            },
            updateToAttic : function(properties){
                atticService.update(this, properties);
            },
            restoreFromAttic : function(properties){
                atticService.restore(this, properties);
            },
            isInAttic : function(properties){
                return atticService.isInAttic(this, properties);
            },
            update : function(content, properties){
                this._super(content, properties);
                if(this.updateCount === 1){
                    // update the attic
                    this.updateToAttic();
                }
            }

        });

        return BaseAtticModel;
    }]);
