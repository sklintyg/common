/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
angular.module('common').service('common.receiverService', [
    '$q', '$log', '$uibModal', 'common.ReceiversProxy',
    function($q, $log, $uibModal, ReceiversProxy) {
        'use strict';
        var modalInstance;
        var state = {
            intygtyp: undefined,
            intygid: undefined,
            possibleReceivers: [],
            approvedReceivers: [],
            showApproveDialog: false
        };

        this.reset = function() {
            state.intygtyp = undefined;
            state.intygid = undefined;
            state.possibleReceivers = [];
            state.approvedReceivers = [];
            state.showApproveDialog = false;
        };

        this.updatePossibleReceivers = function(intygtyp) {
            var deferred = $q.defer();

            this.reset();
            state.intygtyp = intygtyp;
            ReceiversProxy.getPossibleReceivers(intygtyp, function success(response) {
                state.possibleReceivers = response;
                deferred.resolve(response);
            }, function err(data) {
                //cant do much here - let possible receivers be empty
                deferred.reject(false);
            });

            return deferred.promise;
        };

        this.getApprovedReceivers = function(intygtyp, intygid, useCache) {
            var deferred = $q.defer();

            if (useCache && intygid === state.intygid) {
                $log.debug('returning cached approvedList');
                deferred.resolve(state.approvedReceivers);
                return deferred.promise;
            }

            this.reset();
            state.intygtyp = intygtyp;
            state.intygid = intygid;


            ReceiversProxy.getApprovedReceivers(intygtyp, intygid).then(
                function success(response) {
                    //keep internal state for caching
                    state.approvedReceivers = response.data;
                    deferred.resolve(response.data);
            }, function err(data) {
                deferred.reject(false);
            });
            return deferred.promise;
        };

        this.getData = function() {
            return state;
        };

        this.openConfigDialogForIntyg = function(intygtyp, intygid, canCancel) {

            modalInstance = $uibModal.open({
                templateUrl: '/web/webjars/common/webcert/components/receiver/approveReceiversDialog.template.html',
                animation: canCancel,
                backdrop: canCancel ? true : 'static',
                keyboard: canCancel,
                size: 'md',
                controller: 'approveReceiversDialogController',
                resolve: {
                    dlgConfig: function() {
                        return {
                            intygtyp: intygtyp,
                            intygid: intygid,
                            canCancel: canCancel
                        };
                    }
                }
            });
            //angular > 1.5 warns if promise rejection is not handled (e.g backdrop-click == rejection)
            modalInstance.result.catch(function () {}); //jshint ignore:line

        };

        this.reset();
} ]);
