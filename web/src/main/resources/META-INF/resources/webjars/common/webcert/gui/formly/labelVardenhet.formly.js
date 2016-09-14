angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'label-vardenhet',
        templateUrl: '/web/webjars/common/webcert/gui/formly/labelVardenhet.formly.html',
        controller: function($scope) {
            $scope.$on('intyg.loaded', function() {

                // check if all info is available from HSA. If not, display the info message that someone needs to update it
                var vardenhetData = $scope.to.userModel.user.valdVardenhet;
                if (vardenhetData.mottagningar !== undefined) {
                    for (var enhetIndex = 0; enhetIndex < vardenhetData.mottagningar.length; enhetIndex++) {
                        if (vardenhetData.mottagningar[enhetIndex].id === $scope.model.grundData.skapadAv.vardenhet.enhetsid) {
                            vardenhetData = vardenhetData.mottagningar[enhetIndex];
                            break;
                        }
                    }
                }

                // check if all info is available from HSA. If not, display the info message that someone needs to update it
                $scope.hsaInfoMissing = false;
                if (vardenhetData.id !== $scope.model.grundData.skapadAv.vardenhet.enhetsid) {
                    var properties = ['postadress', 'postnummer', 'postort', 'telefonnummer'];
                    for(var i = 0; i < properties.length; i++) {
                        var field = vardenhetData[properties[i]];
                        if(field === undefined || field === '') {
                            $scope.hsaInfoMissing = true;
                            break;
                        }
                    }
                }
            });
        }
    });

});
