/**
 * Show patient has new id message if it differs from the one from the intyg.
 * Broadcast a intyg.loaded event on rootscope when the intyg is loaded to update the message.
 */
angular.module('common').directive('wcNewPersonIdMessage', [
    '$stateParams', 'common.PersonIdValidatorService', 'common.messageService',
    function($stateParams, personIdValidator, messageService) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: true,
            controller: function($scope) {

                $scope.show = false; // Flag to control visibility
                $scope.message = ''; // Text to be shown

                function showPersonnummerMessage(number) {
                    $scope.show = true;
                    var messageId = 'common.alert.newpersonid';
                    $scope.message = messageService.getProperty(messageId, {person: number}, messageId);
                }

                function showReservnummerMessage(number) {
                    $scope.show = true;
                    var messageId = 'common.alert.newreserveid';
                    $scope.message = messageService.getProperty(messageId, {reserve: number}, messageId);
                }

                var updateShowFlag = function() {
                    $scope.show = false;
                    if ($stateParams.patientId !== undefined && $stateParams.patientId !== '' &&
                        $scope.cert && $scope.cert.grundData && $scope.cert.grundData.patient) {

                        var intygPersonnummer = $scope.cert.grundData.patient.personId;
                        var alternatePatientSSn = $stateParams.patientId;

                        // 1. intygets personnummer validerar som personnummer
                        // = visa nuvarande skylt om nytt personnummer om alternatePatientSSn skiljer sig från detta.
                        var result = personIdValidator.validatePersonnummer(intygPersonnummer);
                        if(personIdValidator.validResult(result)){
                            if(intygPersonnummer !== alternatePatientSSn) {
                                showPersonnummerMessage(alternatePatientSSn);
                            }
                        } else {
                            //2 intygets personnummer är ett samordningsnummer (dagsiffra > 31)
                            result = personIdValidator.validateSamordningsnummer(intygPersonnummer);
                            if(personIdValidator.validResult(result)) {

                                //2.2 om alternatePatientSSn validerar som personnummer
                                //    = visa nuvarande meddelande om nytt personnummer.
                                result = personIdValidator.validatePersonnummer(alternatePatientSSn);
                                if(personIdValidator.validResult(result)) {
                                    showPersonnummerMessage(alternatePatientSSn);
                                } else {
                                    //2.1 om alternatePatientSSn inte validerar som personnummer
                                    //    = visa istället meddelande "Patienten har samordningsnummer kopplat till reservnummer: alternatePatientSSn"
                                    showReservnummerMessage(alternatePatientSSn);
                                }
                            }
                        }
                    }
                };

                // cert data may be loaded now, or it may be loaded later.
                updateShowFlag();
                $scope.$watch('cert.grundData.patient.personId', updateShowFlag);
            },
            templateUrl: '/web/webjars/common/webcert/intyg/wcNewPersonIdMessage.directive.html'
        };
    }]);
