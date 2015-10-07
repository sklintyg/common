angular.module('common').controller('common.UtkastHeader',
    ['$rootScope', '$scope', '$log', '$state', '$stateParams', '$location', '$q', '$timeout', '$window',
        'common.messageService', 'common.PrintService', 'common.UtkastService', 'common.UtkastProxy', 'common.statService',
        'common.featureService', 'common.UserModel', 'common.dialogService', 'common.UtkastViewStateService', 'common.anchorScrollService',
        'common.PatientProxy', 'common.authorityService',
        function($rootScope, $scope, $log, $state, $stateParams, $location, $q, $timeout, $window, messageService,
            PrintService, UtkastService, UtkastProxy, statService, featureService, UserModel,  dialogService, CommonViewState,
            anchorScrollService, PatientProxy, authorityService) {
            'use strict';

            $scope.updatePatientData = function() {

                if(!(angular.isObject($scope.viewState.intygModel) &&
                     angular.isObject($scope.viewState.intygModel.grundData) &&
                     angular.isObject($scope.viewState.intygModel.grundData.patient) &&
                     angular.isString($scope.viewState.intygModel.grundData.patient.personId))) {
                    $log.debug('Intygdata or patient data missing for lookup.');
                    return;
                }

                CommonViewState.fetchingPatientData = true;
                $timeout(function() { // delay operation just a bit to make sure the animation is visible to the user
                    PatientProxy.getPatient($scope.viewState.intygModel.grundData.patient.personId, function(patientResult) {
                        CommonViewState.fetchingPatientData = false;
                        $scope.viewState.intygModel.grundData.patient.fullstandigtNamn = (patientResult.fornamn ? patientResult.fornamn : '');

                        if (angular.isString(patientResult.mellannamn)) {
                            $scope.viewState.intygModel.grundData.patient.fullstandigtNamn += ' ' + patientResult.mellannamn;
                        }

                        if (angular.isString(patientResult.efternamn)) {
                            $scope.viewState.intygModel.grundData.patient.fullstandigtNamn += ' ' + patientResult.efternamn;
                        }

                        $scope.viewState.intygModel.grundData.patient.postadress = patientResult.postadress;
                        $scope.viewState.intygModel.grundData.patient.postnummer = patientResult.postnummer;
                        $scope.viewState.intygModel.grundData.patient.postort = patientResult.postort;

                    }, function() { // not found
                        CommonViewState.fetchingPatientData = false;
                    }, function() { // error
                        CommonViewState.fetchingPatientData = false;
                    });

                }, 500);
            };

            /**
             * Toggle header part ('Dölj meny'-knapp)
             */
            $scope.toggleHeader = function() {
                CommonViewState.toggleCollapsedHeader();
            };

            /**
             * Toggle 'Visa vad som behöver kompletteras'
             */
            $scope.toggleShowComplete = function() {
                CommonViewState.toggleShowComplete();
                UtkastService.save();
                anchorScrollService.scrollTo('top');
            };

            /**
             * Action to discard the certificate draft and return to WebCert again.
             */
            $scope.discard = function() {
                var bodyText = 'När du raderar utkastet tas det bort från Webcert.';
                var dialogModel = {
                    acceptprogressdone: false,
                    errormessageid: 'Error',
                    showerror: false
                };

                var draftDeleteDialog = {};
                draftDeleteDialog = dialogService.showDialog({
                    dialogId: 'confirm-draft-delete',
                    titleId: 'common.modal.label.discard_draft',
                    bodyText: bodyText,
                    button1id: 'confirm-draft-delete-button',
                    model: dialogModel,

                    button1click: function() {
                        $log.debug('delete draft ');
                        dialogModel.acceptprogressdone = false;
                        var back = function() {
                            $window.doneLoading = true;
                            // IE9 infinite digest workaround
                            $timeout(function() {
                                $window.history.back();
                            });
                        };
                        UtkastProxy.discardUtkast($stateParams.certificateId, CommonViewState.intyg.type, $scope.viewState.draftModel.version, function() {
                            dialogModel.acceptprogressdone = true;
                            statService.refreshStat(); // Update statistics to reflect change

                            if (!authorityService.isAuthorityActive({authority: 'PRIVILEGE_NAVIGERING'})) {
                                CommonViewState.deleted = true;
                                CommonViewState.error.activeErrorMessageKey = 'error';
                                draftDeleteDialog.close();
                            } else {
                                draftDeleteDialog.close({direct:back});
                            }
                        }, function(error) {
                            dialogModel.acceptprogressdone = true;
                            if (error.errorCode === 'DATA_NOT_FOUND') { // Godtagbart, intyget var redan borta.
                                statService.refreshStat(); // Update statistics to reflect change
                                draftDeleteDialog.close({direct:back});
                            } else if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                                dialogModel.showerror = true;
                                var errorMessageId = 'common.error.discard.concurrent_modification';
                                // In the case of concurrent modification we should have the name of the user making trouble in the message.
                                dialogModel.errormessage = messageService.getProperty(errorMessageId, {name: error.message}, errorMessageId);
                                dialogModel.errormessageid = '';
                            } else {
                                dialogModel.showerror = true;
                                dialogModel.errormessage = '';
                                if (error === '') {
                                    dialogModel.errormessageid = 'common.error.cantconnect';
                                } else {
                                    dialogModel.errormessageid =
                                        ('common.error.' + error.errorCode).toLowerCase();
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
             * Print draft. Supplies the PrintService with patient name and id as a customHeader string.
             */
            $scope.print = function() {
                var customHeader = $scope.viewState.intygModel.grundData.patient.fullstandigtNamn + ' - ' +
                    $scope.viewState.intygModel.grundData.patient.personId;

                PrintService.printWebPageWithCustomTitle($scope.viewState.intygModel.id, CommonViewState.intyg.type, customHeader);
            };

            $window.onbeforeunload = function(event) {
                if ($scope.certForm.$dirty) {
                    // Trigger a save now. If the user responds with "Leave the page" we may not have time to save
                    // before the page is closed. We could use an ajax request with async:false this will force the
                    // browser to "hang" the page until the request is complete. But using async:false is deprecated
                    // and will be removed in future browsers.
                    UtkastService.save();
                    var message = 'Om du väljer "Lämna sidan" kan ändringar försvinna. Om du väljer "Stanna kvar på sidan" autosparas ändringarna.';
                    if (typeof event === 'undefined') {
                        event = $window.event;
                    }
                    if (event) {
                        event.returnValue = message;
                    }
                    return message;
                }
            };

            $scope.$on('$destroy', function() {
                $window.onbeforeunload = null;
            });
        }
    ]
);
