angular.module('common').factory('common.UserModel',
    function() {
        'use strict';

        return {
            reset: function() {
                this.user = null;
            },

            getActiveFeatures: function() {
                if (this.user) {
                    return this.user.aktivaFunktioner;
                } else {
                    return null;
                }
            },

            setUser : function(user){
                this.user = user;
            }
        };
    }
);