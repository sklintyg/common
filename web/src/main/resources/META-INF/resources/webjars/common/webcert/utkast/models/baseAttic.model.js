angular.module('common').factory('common.domain.BaseAtticModel',
    ['common.domain.ModelAttr', 'common.domain.BaseModel', 'common.domain.AtticService', function( ModelAttr, BaseModel, atticService) {
        'use strict';

        var BaseAtticModel = BaseModel._extend({
            init : function init (name, properties){
                init._super.call(this, name, properties);
                this.atticModel = atticService.addAtticModel(this);
            },
            updateToAttic : function updateToAttic(properties){
                atticService.update(this, properties);
            },
            isInAttic : function isInAttic(properties){
                return atticService.isInAttic(this, properties);
            },
            restoreFromAttic : function restoreFromAttic(properties){
                atticService.restore(this, properties);
            },
            update : function update(content, properties){
                update._super.call(this, content, properties);
                if(this.updateCount === 1){
                    // update the attic
                    this.updateToAttic();
                }
            }

        });

        return BaseAtticModel;
    }]);
