/**
 * Created by BESA on 2015-02-09.
 */

angular.module('common').factory('common.IntygCopyRequestModel',
    [function() {
        'use strict';

        /**
         * Constructor
         */
        function IntygCopyRequestModel() {

        }

        IntygCopyRequestModel.build = function() {
            return new IntygCopyRequestModel();
        };

        return IntygCopyRequestModel;
    }
]);