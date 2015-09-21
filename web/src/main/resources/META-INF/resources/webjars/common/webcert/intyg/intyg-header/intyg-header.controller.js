/* global document */
angular.module('common').controller('common.IntygHeader',
    ['$scope', '$log', '$stateParams', 'common.messageService', 'common.PrintService',
    'common.IntygCopyRequestModel', 'common.User', 'common.UserModel', 'common.IntygService',
    'common.IntygViewStateService', 'common.statService',
        function($scope, $log, $stateParams, messageService, PrintService, IntygCopyRequestModel,
            User, UserModel, IntygService, CommonViewState, statService) {
            'use strict';

            $scope.user = UserModel;
            $scope.intygstyp = $stateParams.certificateType;
            $scope.copyBtnTooltipText = messageService.getProperty($scope.intygstyp+'.label.kopiera.text');


            $scope.send = function() {
                IntygService.send($scope.viewState.intygModel.id, $stateParams.certificateType, CommonViewState.defaultRecipient,
                         $scope.intygstyp+'.label.send', $scope.intygstyp+'.label.send.body', function() {
                        // TODO After a send request we shouldn't reload right away due to async reasons.
                        // TODO We show an info message stating 'Intyget has skickats till mottagaren'
                        //$scope.$emit('loadCertificate');
                        $scope.viewState.common.isIntygOnSendQueue = true;
                });
            };

            $scope.makulera = function(cert) {
                var confirmationMessage = messageService.getProperty($scope.intygstyp+'.label.makulera.confirmation', {
                    namn: cert.grundData.patient.fullstandigtNamn,
                    personnummer: cert.grundData.patient.personId
                });
                cert.intygType = $stateParams.certificateType;
                IntygService.makulera( cert, confirmationMessage, function() {
                    //$scope.$emit('loadCertificate');
                    $scope.viewState.common.isIntygOnRevokeQueue = true;
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
                    //PrintService.printWebPage(cert.id, $stateParams.certificateType);
                    var customHeader = cert.grundData.patient.fullstandigtNamn + ' - ' + cert.grundData.patient.personId;
                    PrintService.printWebPageWithCustomTitle(cert.id, $stateParams.certificateType, customHeader);
                } else {
                    document.pdfForm.submit();
                }
            };
        }
    ]
);
