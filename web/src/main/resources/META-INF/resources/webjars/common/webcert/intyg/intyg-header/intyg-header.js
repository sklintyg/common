/* global document */
angular.module('common').controller('common.IntygHeader',
    ['$scope', '$log', '$stateParams', 'common.messageService', 'webcert.ManageCertificate', 'common.ManageCertView',
        'common.IntygCopyRequestModel', 'common.User', 'common.IntygViewStateService',
        function($scope, $log, $stateParams,
            messageService, ManageCertificate, ManageCertView, IntygCopyRequestModel, User, CommonViewState) {
            'use strict';

            $scope.intygstyp = $stateParams.certificateType;
            $scope.viewState = CommonViewState;
            $scope.copyBtnTooltipText = messageService.getProperty($scope.intygstyp+'.label.kopiera.text');


            $scope.send = function() {
                ManageCertificate.send($scope.cert.id, $stateParams.certificateType, CommonViewState.defaultRecipient,
                         $scope.intygstyp+'.label.send', $scope.intygstyp+'.label.send.body', function() {
                        // TODO After a send request we shouldn't reload right away due to async reasons.
                        // TODO We should consider just showing an info message stating 'Intyg has skickats till mottagaren'
                    //$scope.$emit('loadCertificate');
                        $scope.viewState.isIntygOnSendQueue = true;
                });
            };

            $scope.makulera = function(cert) {
                var confirmationMessage = messageService.getProperty($scope.intygstyp+'.label.makulera.confirmation', {
                    namn: cert.grundData.patient.fullstandigtNamn,
                    personnummer: cert.grundData.patient.personId
                });
                cert.intygType = $stateParams.certificateType;
                ManageCertificate.makulera( cert, confirmationMessage, function() {
                    //$scope.$emit('loadCertificate');
                    $scope.viewState.isIntygOnRevokeQueue = true;
                });
            };

            $scope.copy = function(cert) {
                if (cert === undefined || cert.grundData === undefined) {
                    $log.debug('cert or cert.grundData is undefined. Aborting copy.');
                    return;
                }

                var isOtherCareUnit = User.getValdVardenhet() !== cert.grundData.skapadAv.vardenhet.enhetsid;
                ManageCertificate.copy($scope.viewState,
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
                    ManageCertView.printDraft(cert.id, $stateParams.certificateType);
                } else {
                    document.pdfForm.submit();
                }
            };
        }
    ]
);
