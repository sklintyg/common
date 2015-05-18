// object.watch
'use strict';

var getProp = function(obj, prop) {
    if (obj === undefined || prop === undefined || prop.length === 0) {
        return -1;
    }
    var props = prop.split('.');
    for (var i = 0; i < props.length; i++) {
        var p = props[i];
        obj = obj[p];
        if (obj !== undefined && i === props.length - 1) {
            return obj;
        }
    }
};

var getThisAndProp = function(obj, prop) {
    if (obj === undefined || prop === undefined || prop.length === 0) {
        return -1;
    }
    var props = prop.split('.');
    for (var i = 0; i < props.length; i++) {
        var p = props[i];
        // o,a,b
        if (obj.hasOwnProperty(p) && i === props.length - 1) {
            return {
                self: obj,
                prop: p
            };
        }
        obj = obj[p];

    }
};

if (!Object.prototype.watch) {
    Object.defineProperty(Object.prototype, 'watch', {
        enumerable: false,
        configurable: true,
        writable: false,
        value: function(prop, handler) {

            var selfProp = getThisAndProp(this, prop);

            var oldVal = getProp(this, prop);

            var setter = function(newval) {
                if (oldVal !== newval) {
                    handler.call(selfProp.self, selfProp.prop, oldVal, newval);
                    oldVal = newval;
                } else {
                    return false;
                }
            };

            var getter = function() {
                return oldVal;
            };

            if (delete selfProp.self[selfProp.prop]) { // can't watch constants
                Object.defineProperty(selfProp.self, selfProp.prop, {
                    get: getter,
                    set: setter,
                    enumerable: true,
                    configurable: true
                });
            }
        }
    });
}

if (!Object.prototype.unwatch) {
    Object.defineProperty(Object.prototype, 'unwatch', {

        enumerable: false,
        configurable: true,
        writable: false,
        value: function(prop) {
            var selfProp = getThisAndProp(this, prop);
            if(selfProp.self) {
                var val = selfProp.self[selfProp.prop];
                delete selfProp.self[selfProp.prop]; // remove accessors
                selfProp.self[selfProp.prop] = val;
            }
        }
    });
}

/**
 *
 * Example usage:
 *
 *
 var o = {p: 1};

 o.watch("p", function (id, oldval, newval) {
	    console.log( "o." + id + " changed from " + oldval + " to " + newval );
	    return newval;
	});

 o.p = 2; // should log the change
 o.p = 2; // should do nothing
 *
 *
 */
