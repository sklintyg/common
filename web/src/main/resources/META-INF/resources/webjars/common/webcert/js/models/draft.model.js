angular.module('common').factory('common.domain.DraftModel',
    [ function(  ) {
        'use strict';

        // the actual model
        var _draftModel;

        /**
         * Constructor, with class name
         */
        function DraftModel() {
            this.vidarebefordrad = undefined;
            this.status = undefined;
        }

        DraftModel.prototype.update = function (data) {
            // refresh the model data
            if(data === undefined) {
                return;
            }
            this.vidarebefordrad = data.vidarebefordrad;
            this.status = data.status;
            if(this.content){
                this.content.update(data.content);
            }
        };

        DraftModel.prototype.isSigned = function (){
            return !!(this.status && this.status === 'SIGNED');
        };

        DraftModel.prototype.isDraftComplete = function (){
            if(this.status && this.status === 'DRAFT_COMPLETE'){
                return true;
            } else {
                return false;
            }
        };

        DraftModel.build = function() {
            return new DraftModel();
        };

        _draftModel = DraftModel.build();

        return _draftModel;

    }]);