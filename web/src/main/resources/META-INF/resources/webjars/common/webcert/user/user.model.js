angular.module('common').factory('common.UserModel',
    function() {
        'use strict';

        return {
            reset: function() {
                this.userContext = null;
                this.user = null;
            },

            getActiveFeatures: function() {
                if (this.userContext) {
                    return this.userContext.aktivaFunktioner;
                } else {
                    return null;
                }
            },

            /**
             * Set user context from api
             * @param userContext
             */
            setUserContext: function(userContext) {
                this.userContext = userContext;
            },

            setUser : function(user){
                this.user = user;
            }
        };
    }
);