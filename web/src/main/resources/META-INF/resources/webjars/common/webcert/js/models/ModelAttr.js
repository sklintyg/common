angular.module('common').factory('common.domain.ModelAttr',
    function( ) {
        'use strict';
        var ModelAttr = function(property, options){
            this.property = property;
            if(options && options.defaultValue !== undefined){
                this.defaultValue = options.defaultValue;
            } else {
                this.defaultValue = undefined;
            }
            if(options) {
                this.trans = options.trans;
                this.toTransform = options.toTransform;
                this.fromTransform = options.fromTransform;
                this.linkedProperty = options.linkedProperty;
            }
        };

        return ModelAttr;
    });
