/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
angular.module('common').directive('wcUtkastHeader',
    ['$window', '$state', 'common.moduleService', 'common.UtkastHeaderViewState', 'common.UserModel',
      '$log', 'common.messageService', 'common.dialogService', '$timeout', 'common.statService',
      'common.IntygViewStateService', 'common.authorityService', 'common.UtkastViewStateService',
      'common.UtkastProxy', '$stateParams','common.ResourceLinkService',
      function($window, $state, moduleService, UtkastHeaderViewState, UserModel, $log, messageService,
          dialogService, $timeout, statService, IntygViewState, authorityService, CommonViewState,
          UtkastProxy, $stateParams, ResourceLinkService) {
        'use strict';

        return {
          restrict: 'E',
          scope: {
            utkastViewState: '=',
            certForm: '='
          },
          templateUrl: '/web/webjars/common/webcert/utkast/utkastHeader/wcUtkastHeader/wcUtkastHeader.directive.html',
          link: function($scope) {

            $scope.certificateName = moduleService.getModuleName(UtkastHeaderViewState.intygType);
            $scope.backState = 'webcert.create-choose-certtype-index';

            // $scope.oldPersonId = UserModel.getIntegrationParam('beforeAlternateSsn');

            /**
             * First have to make sure if grundData has been loaded to the IntygModel.
             *
             * If beforeAlternateSsn has a patient id - It means that the backend has updated it based on passed alternateSsn
             * and beforeAlternateSsn has the old patient id.
             *
             * If beforeAlternateSsn is missing patient id but alternateSsn has one - It means that the EHR-system have called
             * webcert with a new patient id but the backend haven't stored it (based on user authorization). Then display
             * alternateSsn as the new and the patient id on the draft as old.
             *
             * If both beforeAlternateSsn and alternateSsn are missing value, then give just use the patient id on the draft.
             */
            function updatePersonId() {
              if ($scope.utkastViewState.intygModel.grundData) {
                var beforeAlternateSsn = UserModel.getIntegrationParam('beforeAlternateSsn');
                var alternateSsn = UserModel.getIntegrationParam('alternateSsn');

                if (beforeAlternateSsn) {
                  $scope.oldPersonId = beforeAlternateSsn;
                  $scope.personId = alternateSsn;
                } else if (alternateSsn) {
                  $scope.oldPersonId = $scope.utkastViewState.intygModel.grundData.patient.personId;
                  $scope.personId = alternateSsn;
                } else {
                  $scope.personId = $scope.utkastViewState.intygModel.grundData.patient.personId;
                }
              }
            }

            function discardModal() {
              if(UserModel.user.origin === 'DJUPINTEGRATION' &&
                  $scope.utkastViewState.draftModel.content.name ==='dbModel' &&
                  ResourceLinkService.isLinkTypeExists($scope.utkastViewState.draftModel.links, 'REDIGERA_UTKAST')) {
                var patient = $scope.utkastViewState.intygModel.grundData.patient;
                var namn = patient.fornamn;
                if(patient.mellannamn !== undefined){
                  namn = namn + ' ' + patient.mellannamn;
                }
                namn = namn + ' ' + patient.efternamn;

                var dialogModel = {
                  acceptprogressdone: false,
                  errormessageid: 'Error',
                  showerror: false,
                  namn: namn,
                  personnummer: patient.personId.slice(0, 8) + '-' + patient.personId.slice(8),
                  toggleProceed: function() {
                    document.getElementById('button1id').disabled =
                        !document.getElementById('checkboxId').checked;
                  },
                  checkboxId: 'checkboxId',
                  checkboxText: 'db.label.checkbox.text'
                };

                var draftDeleteDialog = {};
                draftDeleteDialog = dialogService.showDialog({
                  dialogId: 'db-info-dialog',
                  titleText: 'db.label.titleText',
                  templateUrl: '/app/partials/dbInfo.dialog.html',
                  bodyText: 'db.draft.label.bodyText',
                  model: dialogModel,

                  button2click: function() {
                    dialogModel.acceptprogressdone = false;
                    var back = function() {
                      // IE9 infinite digest workaround
                      $timeout(function() {
                        $window.history.back();
                      });
                    };

                    function afterDelete() {
                      statService.refreshStat(); // Update statistics to reflect change
                      IntygViewState.deletedDraft = true;
                      if (!authorityService.isAuthorityActive({authority: 'NAVIGERING'})) {
                        if (CommonViewState.isCreatedFromIntygInSession()) {
                          CommonViewState.clearUtkastCreatedFrom();
                          draftDeleteDialog.close({direct: back});
                        } else {
                          CommonViewState.deleted = true;
                          CommonViewState.error.activeErrorMessageKey = 'error';
                          draftDeleteDialog.close();
                        }
                      } else {
                        draftDeleteDialog.close({direct: back});
                      }
                    }
                    UtkastProxy.discardUtkast($stateParams.certificateId, CommonViewState.intyg.type,
                        $scope.utkastViewState.draftModel.version, function() {
                          dialogModel.acceptprogressdone = true;

                          afterDelete();
                        }, function(error) {
                          dialogModel.acceptprogressdone = true;
                          if (error.errorCode === 'DATA_NOT_FOUND') { // Godtagbart, intyget var redan borta.
                            afterDelete();
                          } else if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                            dialogModel.showerror = true;
                            var errorMessageId = 'common.error.discard.concurrent_modification';
                            // In the case of concurrent modification we should have the name of the user making trouble in the message.
                            dialogModel.errormessage =
                                messageService.getProperty(errorMessageId, {name: error.message},
                                    errorMessageId);
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
                  button1click: function(modalInstance) {
                    modalInstance.close();
                  },
                  button1id: 'button1id',
                  button1text: 'db.label.button1text',
                  button2text: 'common.delete',
                  autoClose: false,
                  disableClose: true
                });
              }
            }

            updatePersonId();
            $scope.$on('intyg.loaded', updatePersonId);
            $scope.$on('intyg.loaded', discardModal);
          }
        };
      }]);
