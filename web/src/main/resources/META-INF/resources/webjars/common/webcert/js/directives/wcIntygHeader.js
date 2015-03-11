/* global document */
angular.module('common').directive('wcIntygHeader',
    ['$log', '$stateParams', 'common.messageService', 'webcert.ManageCertificate', 'common.ManageCertView',
     'common.IntygCopyRequestModel', 'common.User',
        function($log, $stateParams, messageService, ManageCertificate, ManageCertView, IntygCopyRequestModel, User) {
            'use strict';
            return {
                restrict: 'A',
                replace: true,
                scope: true,
                controller: function($scope) {
                    $scope.intygstyp = $stateParams.certificateType;

                    $scope.send = function(cert) {
                        cert.intygType = $stateParams.certificateType;
                        ManageCertificate.send($scope, cert, 'FK', $scope.intygstyp+'.label.send', function() {
                            $scope.$emit('loadCertificate');
                        });
                    };

                    $scope.makulera = function(cert) {
                        var confirmationMessage = messageService.getProperty($scope.intygstyp+'.label.makulera.confirmation', {
                            namn: cert.grundData.patient.fullstandigtNamn,
                            personnummer: cert.grundData.patient.personId
                        });
                        cert.intygType = $stateParams.certificateType;
                        ManageCertificate.makulera($scope, cert, confirmationMessage, function() {
                            $scope.$emit('loadCertificate');
                        });
                    };

                    $scope.copy = function(cert) {
                        if (cert === undefined || cert.grundData === undefined) {
                            $log.debug('cert or cert.grundData is undefined. Aborting copy.');
                            return;
                        }

                        var isOtherCareUnit = User.getValdVardenhet() !== cert.grundData.skapadAv.vardenhet.enhetsid;

                        ManageCertificate.copy($scope,
                            IntygCopyRequestModel.build({
                                intygId: cert.id,
                                intygType: $stateParams.certificateType,
                                patientPersonnummer: cert.grundData.patient.personId,
                                nyttPatientPersonnummer: $stateParams.patientId
                            }),
                            isOtherCareUnit);
                    };

                    $scope.print = function(cert) {
                        if ($scope.certProperties.isRevoked) {
                            ManageCertView.printDraft(cert.id, $stateParams.certificateType);
                        } else {
                            document.pdfForm.submit();
                        }
                    };
                },
                templateUrl: '/web/webjars/common/webcert/js/directives/wcIntygHeader.html'
            };
        }
    ]
);
