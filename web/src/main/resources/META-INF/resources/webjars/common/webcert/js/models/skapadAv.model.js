angular.module('common').factory('common.Domain.SkapadAvModel',
    ['common.Domain.VardenhetModel',
        function(VardenhetModel) {
            'use strict';

            /**
             * Constructor, with class name
             */
            function SkapadAv() {
                this.personId = undefined;
                this.fullstandigtNamn = undefined;
                this.forskrivarKod = undefined;
                this.vardenhet = VardenhetModel.build();
            }

            SkapadAv.prototype.update = function(skapadAv) {
                // refresh the model data
                if(skapadAv === undefined){
                    return;
                }
                this.personId = skapadAv.personId;
                this.fullstandigtNamn = skapadAv.fullstandigtNamn;
                this.forskrivarKod = skapadAv.forskrivarkod;
                this.vardenhet.update(skapadAv.vardenhet);
            };

            SkapadAv.build = function() {
                return new SkapadAv();
            };

            /**
             * Return the constructor function SkapadAv
             */
            return SkapadAv;

        }]);