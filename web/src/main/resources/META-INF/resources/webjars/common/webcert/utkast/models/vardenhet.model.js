angular.module('common').factory(
    'common.Domain.VardenhetModel',
    [ function() {
        'use strict';

        /**
         * Constructor, with class name
         */
        function VardenhetModel() {
            this.enhetsid = undefined;
            this.enhetsnamn = undefined;
            this.postadress = undefined;
            this.postnummer = undefined;
            this.postort = undefined;
            this.telefonnummer = undefined;
            this.epost = undefined;
            this.vardgivare = {
                vardgivarid: null,
                vardgivarnamn: null
            };
            this.arbetsplatsKod = undefined;
        }


        VardenhetModel.prototype.update = function(vardenhet) {
            // refresh the model data

            if(vardenhet === undefined) {
                return;
            }
            this.enhetsid = vardenhet.enhetsid;
            this.enhetsnamn = vardenhet.enhetsnamn;
            this.postadress = vardenhet.postadress;
            this.postnummer = vardenhet.postnummer;
            this.postort = vardenhet.postort;
            this.telefonnummer = vardenhet.telefonnummer;
            this.epost = vardenhet.epost;
            this.vardgivare = {
                vardgivarid: vardenhet.vardgivare.vardgivarid,
                vardgivarnamn: vardenhet.vardgivare.vardgivarnamn
            };
            this.arbetsplatsKod = vardenhet.arbetsplatsKod;
        };

        function isDefined(data) {
            return typeof data !== 'undefined' && data !== '';
        }

        VardenhetModel.prototype.isMissingInfo = function(){

            var props = [
                'postadress',
                'postnummer',
                'postort',
                'telefonnummer',
                'postadress',
                'postnummer',
                'postort',
                'telefonnummer'
            ];

            var missingInfo = false;
            angular.foreach(props, function(prop) {
                if(!isDefined(this[prop])) {
                    missingInfo = true;
                }
            }, this);

            return missingInfo;
        };

        VardenhetModel.build = function() {
            return new VardenhetModel();
        };

        /**
         * Return the constructor function VardenhetModel
         */
        return VardenhetModel;

    }]);