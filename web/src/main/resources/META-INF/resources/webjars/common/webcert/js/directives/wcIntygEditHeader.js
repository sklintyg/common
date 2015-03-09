angular.module('common').directive('wcIntygEditHeader',
    ['$rootScope', '$log', '$anchorScroll', '$routeParams', '$location', '$q', 'common.messageService',
     'common.ManageCertView', 'common.CertificateService', 'common.statService',
     'common.featureService', 'common.dialogService', 'common.CertViewState',
        function($rootScope, $log, $anchorScroll, $routeParams, $location, $q, messageService, ManageCertView,
                 CertificateService, statService, featureService, dialogService, CertViewState) {
            'use strict';
            return {
                restrict: 'A',
                replace: true,
                scope: true,
                controller: function($scope) {

                    /**
                     * Toggle header part ('Dölj meny'-knapp)
                     */
                    $scope.toggleHeader = function() {
                        CertViewState.toggleCollapsedHeader()
                    };

                    /**
                     * Toggle 'Visa vad som behöver kompletteras'
                     */
                    $scope.toggleShowComplete = function() {
                        if (CertViewState.toggleShowComplete()) {
                            $scope.save();

                            var old = $location.hash();
                            $location.hash('top');
                            $anchorScroll();
                            // reset to old to keep any additional routing logic from kicking in
                            $location.hash(old);
                        }
                    };

                    /**
                     * Action to discard the certificate draft and return to WebCert again.
                     */
                    $scope.discard = function() {
                        var bodyText = 'När du raderar utkastet tas det bort från Webcert.';
                        var dialogRequestModel = {
                            acceptprogressdone: false,
                            errormessageid: 'Error',
                            showerror: false
                        };

                        var draftDeleteDialog = {};
                        draftDeleteDialog = dialogService.showDialog($scope, {
                            dialogId: 'confirm-draft-delete',
                            titleId: 'common.modal.label.discard_draft',
                            bodyText: bodyText,
                            button1id: 'confirm-draft-delete-button',
                            dialogRequestModel: dialogRequestModel,

                            button1click: function() {
                                $log.debug('delete draft ');
                                dialogRequestModel.acceptprogressdone = false;
                                CertificateService.discardDraft($routeParams.certificateId, $scope.intygstyp, function() {
                                    dialogRequestModel.acceptprogressdone = true;
                                    statService.refreshStat(); // Update statistics to reflect change

                                    if (featureService.isFeatureActive('franJournalsystem')) {
                                        $rootScope.$broadcast('intyg.deleted', $routeParams.certificateId);
                                    } else {
                                        $location.path('/unsigned');
                                    }
                                    draftDeleteDialog.close();
                                }, function(error) {
                                    dialogRequestModel.acceptprogressdone = true;
                                    if (error.errorCode === 'DATA_NOT_FOUND') { // Godtagbart, intyget var redan borta.
                                        statService.refreshStat(); // Update statistics to reflect change
                                        draftDeleteDialog.close();
                                        $location.path('/unsigned');
                                    } else {
                                        dialogRequestModel.showerror = true;
                                        if (error === '') {
                                            dialogRequestModel.errormessageid = 'common.error.cantconnect';
                                        } else {
                                            dialogRequestModel.errormessageid =
                                                ('error.message.' + error.errorCode).toLowerCase();
                                        }
                                    }
                                });
                            },
                            button1text: 'common.delete',
                            button2text: 'common.cancel',
                            autoClose: false
                        });
                    };

                    /**
                     * Action to sign the certificate draft and return to Webcert again.
                     */
                    $scope.sign = function() {
                        ManageCertView.signera($scope, $scope.certMeta.intygType);
                    };

                    /**
                     * Print draft
                     */
                    $scope.print = function() {
                        ManageCertView.printDraft( $scope.cert.id, $scope.certMeta.intygType );
                    };
                },
                templateUrl: '/web/webjars/common/webcert/js/directives/wcIntygEditHeader.html'
            };
        }
    ]
);
