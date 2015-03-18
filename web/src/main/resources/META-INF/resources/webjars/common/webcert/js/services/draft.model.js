angular.module('common').factory('common.domain.DraftModel',
    [ function(  ) {
        'use strict';

        // the actual model
        var _draftModel;

        /**
         * Constructor, with class name
         */
        function DraftModel() {
            this.vidarebefordrad;
            this.status;
        }

        DraftModel.prototype.update = function (data) {
            // refresh the model data
            if(data === undefined) return;
            this.vidarebefordrad = data.vidarebefordrad;
            this.status = data.status;
            if(this.content){
                this.content.update(data.content);
            }
        }

        DraftModel.build = function() {
            return new DraftModel();
        };

        _draftModel = DraftModel.build();

        return _draftModel;

    }]);