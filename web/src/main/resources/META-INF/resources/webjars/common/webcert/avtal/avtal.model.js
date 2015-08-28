/**
 * Created by BESA on 2015-02-09.
 */

angular.module('common').factory('common.AvtalModel',
    [function() {
        'use strict';

        /**
         * Constructor
         */
        function AvtalModel(avtal) {
            this.avtalText = avtal.avtalText;
            this.avtalVersion = avtal.avtalVersion;
            this.versionDatum = moment(avtal.versionDatum).format('YYYY-MM-DD');
        }

        AvtalModel.build = function(avtal) {
            return new AvtalModel(avtal);
        };

        return AvtalModel;
    }
]);