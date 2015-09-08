angular.module('common').factory('common.User',
    [ '$http', '$log', 'common.UserModel', '$q', function($http, $log, userModel, $q) {
        'use strict';

        return {

            getUserContext: function() {
                return userModel.userContext;
            },

            /**
             * returns a list of selectable vardenheter and mottagningar from user context
             * @returns valdVardenhet
             */
            getVardenhetSelectionList: function() {

                var ucVardgivare = angular.copy(userModel.userContext.vardgivare);

                var vardgivareList = [];

                angular.forEach(ucVardgivare, function(vardgivare, key1) {

                    this.push({ 'id': vardgivare.id, 'namn': vardgivare.namn, 'vardenheter': [] });

                    angular.forEach(vardgivare.vardenheter, function(vardenhet) {

                        this.push({ 'id': vardenhet.id, 'namn': vardenhet.namn });

                        angular.forEach(vardenhet.mottagningar, function(mottagning) {

                            mottagning.namn = vardenhet.namn + ' - ' + mottagning.namn;
                            this.push(mottagning);

                        }, vardgivareList[key1].vardenheter);

                    }, vardgivareList[key1].vardenheter);

                }, vardgivareList);

                return vardgivareList;
            },

            /**
             * Returns a list of the selected vardenhet and all its mottagningar
             * @returns {*}
             */
            getVardenhetFilterList: function(vardenhet) {
                if (!vardenhet) {

                    if (userModel.userContext.valdVardenhet) {
                        $log.debug('getVardenhetFilterList: using valdVardenhet');
                        vardenhet = userModel.userContext.valdVardenhet;
                    } else {
                        $log.debug('getVardenhetFilterList: parameter vardenhet was omitted');
                        return [];
                    }
                }

                var vardenhetCopy = angular.copy(vardenhet); // Don't modify the original!
                var units = [];
                units.push(vardenhetCopy);

                angular.forEach(vardenhetCopy.mottagningar, function(mottagning) {
                    this.push(mottagning);
                }, units);

                return units;
            },

            /**
             * returns valdVardgivare from user context
             * @returns valdVardgivare
             */
            getValdVardgivare: function() {
                return userModel.userContext.valdVardgivare;
            },

            /**
             * returns valdVardenhet from user context
             * @returns valdVardenhet
             */
            getValdVardenhet: function() {
                return userModel.userContext.valdVardenhet;
            },

            /**
             * setValdVardenhet. Tell server which vardenhet is active in user context
             * @param vardenhet - complete vardenhet object to send
             * @param onSuccess - success callback on successful call
             * @param onError - error callback on connection failure
             */
            setValdVardenhet: function(vardenhet, onSuccess, onError) {
                $log.debug('setValdVardenhet' + vardenhet.namn);

                var payload = vardenhet;

                var restPath = '/api/anvandare/andraenhet';
                $http.post(restPath, payload).success(function(data) {
                    $log.debug('got callback data: ' + data);

                    // Update user context
                    userModel.setUserContext(data);

                    onSuccess(data);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    onError(data);
                });
            },

            /**
             * Fetches the current user object from the current session.
             */
            userDef : undefined,
            initUser : function(onSuccess, onError){
                var self = this;
                if(self.userDef !== undefined){
                    // just return the promise
                    return self.userDef.promise;
                }
                // else create a new def
                self.userDef = $q.defer();
                var restPath = '/api/anvandare';
                $http.get(restPath).success(function(data) {
                    $log.debug('got callback data: ' + data);
                    // Update user context
                    userModel.setUser(data);
                    if(onSuccess){
                        onSuccess(data);
                    }
                    self.userDef.resolve();
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    if(onError) {
                        onError(data);
                    }
                    self.userDef.resolve();
                });
                return self.userDef.promise;
            }


        };
    }]);
