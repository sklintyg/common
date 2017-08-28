angular.module('db').service('db.EditCertCtrl.ViewStateService',
    ['$log', '$state', 'common.UtkastViewStateService', 'db.Domain.IntygModel',
        function($log, $state, CommonViewState, IntygModel) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.setDraftModel = function(draftModel){
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
            };

            this.inputLimits = {
                identitetStyrkt: 100,
                dodsplatsKommun: 100
            };

            this.reset = function() {
                CommonViewState.reset();
                CommonViewState.intyg.type = 'db';
                this.setDraftModel(IntygModel._members.build());
                return this;
            };

            this.clearModel = function(){
                this.intygModel = undefined;
                this.draftModel = undefined;
            };

            this.reset();
        }]);
