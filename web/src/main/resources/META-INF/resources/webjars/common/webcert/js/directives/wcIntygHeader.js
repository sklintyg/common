/* global document */
angular.module('common').directive('wcIntygHeader',
    ['$rootScope', '$log', '$routeParams', 'common.messageService', 'webcert.ManageCertificate', 'common.ManageCertView',
     'common.IntygCopyRequestModel', 'common.User',
        function($rootScope, $log, $routeParams, messageService, ManageCertificate, ManageCertView, IntygCopyRequestModel, User) {
            'use strict';
            return {
                restrict: 'A',
                replace: true,
                scope: true,
                controller: function($scope) {
                    $scope.intygstyp = $routeParams.certificateType;

                    ManageCertificate.initSend($scope);
                    $scope.send = function(cert) {
                        cert.intygType = $routeParams.certificateType;
                        ManageCertificate.send($scope, cert, 'FK', $scope.intygstyp+'.label.send', function(status) {
                            if (status === '"RESCHEDULED"') {
                                // Intygstjansten is down, webcert will reschedule the send request later
                                // We can not call loadCertificate since it will display an error
                                $scope.certProperties.isSent = true;
                                $rootScope.$emit('fk7263.ViewCertCtrl.load', $scope.cert, $scope.certProperties);
                            }
                            else {
                                $scope.$emit('loadCertificate');
                            }
                        });
                    };

                    ManageCertificate.initMakulera($scope);
                    $scope.makulera = function(cert) {
                        var confirmationMessage = messageService.getProperty($scope.intygstyp+'.label.makulera.confirmation', {
                            namn: cert.grundData.patient.fullstandigtNamn,
                            personnummer: cert.grundData.patient.personId
                        });
                        cert.intygType = $routeParams.certificateType;
                        ManageCertificate.makulera($scope, cert, confirmationMessage, function() {
                            $scope.$emit('loadCertificate');
                        });
                    };

                    ManageCertificate.initCopyDialog($scope);
                    $scope.copy = function(cert) {
                        if (cert === undefined || cert.grundData === undefined) {
                            $log.debug('cert or cert.grundData is undefined. Aborting copy.');
                            return;
                        }

                        var isOtherCareUnit = User.getValdVardenhet() !== cert.grundData.skapadAv.vardenhet.enhetsid;

                        ManageCertificate.copy($scope,
                            IntygCopyRequestModel.build({
                                intygId: cert.id,
                                intygType: $routeParams.certificateType,
                                patientPersonnummer: cert.grundData.patient.personId,
                                nyttPatientPersonnummer: $routeParams.patientId
                            }),
                            isOtherCareUnit);
                    };

                    $scope.print = function(cert) {
                        if ($scope.certProperties.isRevoked) {
                            ManageCertView.printDraft(cert.id, $routeParams.certificateType);
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
