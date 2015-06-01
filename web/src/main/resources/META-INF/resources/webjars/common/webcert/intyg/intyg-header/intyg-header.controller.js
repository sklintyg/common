/* global document */
angular.module('common').controller('common.IntygHeader',
    ['$scope', '$log', '$stateParams', 'common.messageService', 'common.PrintService',
    'common.IntygCopyRequestModel', 'common.User', 'common.IntygService',
    'common.IntygViewStateService', 'common.statService', 'common.PatientProxy', 'common.PatientModel',
        function($scope, $log, $stateParams, messageService, PrintService, IntygCopyRequestModel,
            User, IntygService, CommonViewState, statService, PatientProxy, PatientModel) {
            'use strict';

            $scope.intygstyp = $stateParams.certificateType;
            $scope.viewState = CommonViewState;
            $scope.copyBtnTooltipText = messageService.getProperty($scope.intygstyp+'.label.kopiera.text');


            $scope.send = function() {
                IntygService.send($scope.cert.id, $stateParams.certificateType, CommonViewState.defaultRecipient,
                         $scope.intygstyp+'.label.send', $scope.intygstyp+'.label.send.body', function() {
                    $scope.$emit('loadCertificate');
                });
            };

            $scope.makulera = function(cert) {
                var confirmationMessage = messageService.getProperty($scope.intygstyp+'.label.makulera.confirmation', {
                    namn: cert.grundData.patient.fullstandigtNamn,
                    personnummer: cert.grundData.patient.personId
                });
                cert.intygType = $stateParams.certificateType;
                IntygService.makulera( cert, confirmationMessage, function() {
                    $scope.$emit('loadCertificate');
                    statService.refreshStat();
                });
            };

            $scope.copy = function(cert) {
                if (cert === undefined || cert.grundData === undefined) {
                    $log.debug('cert or cert.grundData is undefined. Aborting copy.');
                    return;
                }

                var isOtherCareUnit = User.getValdVardenhet() !== cert.grundData.skapadAv.vardenhet.enhetsid;
                IntygService.copy($scope.viewState,
                    IntygCopyRequestModel.build({
                        intygId: cert.id,
                        intygType: $stateParams.certificateType,
                        patientPersonnummer: cert.grundData.patient.personId,
                        nyttPatientPersonnummer: $stateParams.patientId
                    }),
                    isOtherCareUnit);
            };

            $scope.print = function(cert) {
                if (CommonViewState.intyg.isRevoked) {
                    PrintService.printWebPage(cert.id, $stateParams.certificateType);
                } else {
                    document.pdfForm.submit();
                }
            };

            /*
             * Lookup patient to check for sekretessmarkering
             */
            $scope.$on('intyg.loaded', function(event, content) {

                $scope.sekretessmarkering = false;
                $scope.sekretessmarkeringError = false;

                var onSuccess = function() {
                    $scope.sekretessmarkering = PatientModel.sekretessmarkering;
                };

                var onNotFound = function() {
                    $scope.sekretessmarkeringError = true;
                };

                var onError = function() {
                    $scope.sekretessmarkeringError = true;
                };

                PatientProxy.getPatient(content.grundData.patient.personId, onSuccess, onNotFound, onError);
            });
        }
    ]
);
