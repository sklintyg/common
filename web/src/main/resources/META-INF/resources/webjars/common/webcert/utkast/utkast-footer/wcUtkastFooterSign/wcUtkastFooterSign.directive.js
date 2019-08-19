/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

angular
.module('common')
.directive('wcUtkastFooterSign',
    ['$q', 'common.dynamicLabelService', 'common.UtkastValidationViewState', 'common.UtkastSignService', 'common.UtkastProxy',
      'common.featureService', 'common.UtkastFooterService',
      'common.messageService', 'common.moduleService', 'common.dialogService',
      function($q, dynamicLabelService, utkastValidationViewState, UtkastSignService, commonUtkastProxy, featureService,
          UtkastFooterService,
          messageService, moduleService, dialogService) {
        'use strict';

        return {
          restrict: 'E',
          templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/wcUtkastFooterSign/wcUtkastFooterSign.directive.html',
          scope: {
            viewState: '=',
            certForm: '<'
          },
          controller: function($scope) {
            var viewState = $scope.viewState;

            var previousWarningMessage = null;
            var previousUtkastWarnings = {};
            var previousIntygWarnings = {};
            var waitingForSignCompletion = $q.resolve();
            var messageKey = 'common.';

            if (viewState.common.intyg.isKomplettering) {
              messageKey += 'signsend.completion';
            } else if (isSignAndSend()) {
              messageKey += 'signsend';
            } else {
              messageKey += 'sign';
            }

            $scope.signBtnText = dynamicLabelService.getProperty(messageKey);
            $scope.signBtnTooltip = messageService.getProperty(messageKey + '.tooltip', {
              'recipient': messageService.getProperty(
                  'common.recipient.' + moduleService.getModule(viewState.common.intyg.type).defaultRecipient.toLowerCase())
            });

            $scope.getPreviousIntygWarning = function() {
              return previousWarningMessage;
            };

            $scope.isSignAndSend = isSignAndSend;
            $scope.isSignAndSendOrKomp = isSignAndSendOrKomp;
            $scope.getCurrentSignStatus = getCurrentSignStatus;

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
              if (!UtkastFooterService.checkMissing(viewState, $scope.certForm, getCurrentSignStatus())) {
                return;
              }

              utkastValidationViewState.reset();
              if (viewState.common.intyg.type.toLowerCase() === 'doi') {
                signDoi();
              } else {
                doSignRequest();
              }
            };

            $scope.disableSign = function() {
              var previousIntyg = false;
              if (featureService.isFeatureActive(featureService.features.UNIKT_INTYG, viewState.common.intyg.type) &&
                  previousIntygWarnings !== undefined) {

                previousIntyg = previousIntygWarnings[viewState.common.intyg.type];

                if (previousIntyg && !previousIntyg.sameVardgivare) {
                  previousWarningMessage = viewState.common.intyg.type + '.warn.previouscertificate.differentvg';
                }
              }

              return previousIntyg;
            };

            function doSignRequest() {
              waitingForSignCompletion = UtkastSignService.signera(viewState.common.intyg.type, viewState.draftModel.version).then(
                  function(result) {
                    if (result.newVersion) {
                      viewState.draftModel.version = result.newVersion;
                    }
                  }
              );
            }

            function signDoi() {
              /*
              * Fetch previous intyg to see if there's an addiction to signed DOIs
              * */
              getPreviousIntyg(function() {

                // Display modal if we already have a signed doi
                if (previousIntygWarnings &&
                    typeof previousIntygWarnings.doi !== 'undefined' &&
                    !previousIntygWarnings.doi.sameVardgivare) {
                  showDoiForceSignModal();
                  return;
                }
                doSignRequest();
              });
            }

            function showDoiForceSignModal() {
              dialogService.showDialog({
                dialogId: 'doi-already-signed-dialog',
                titleText: 'doi.error.sign.intyg_of_type_exists.other_vardgivare.title',
                templateUrl: '/app/partials/doiInfo.dialog.html',
                button1click: function(modalInstance) {
                  modalInstance.close();
                  doSignRequest();
                },
                button2click: function(modalInstance) {
                  modalInstance.close();
                },
                button1text: 'doi.error.sign.intyg_of_type_exists.other_vardgivare.sign',
                button2text: 'common.createfromtemplate.cancel',
                bodyText: 'doi.error.sign.intyg_of_type_exists.other_vardgivare',
                autoClose: false
              });
            }

            function isSignAndSend() {
              return featureService.isFeatureActive(featureService.features.SIGNERA_SKICKA_DIREKT, viewState.common.intyg.type);
            }

            function isSignAndSendOrKomp() {
              return viewState.common.intyg.isKomplettering || isSignAndSend();
            }

            function getCurrentSignStatus() {
              // 0: pending, 1: resolved, 2: rejected. Works for later versions of AngularJS.
              switch (waitingForSignCompletion.$$state.status) {
              case 0:
                return 'pending';
              case 1:
                return 'resolved';
              case 2:
                return 'rejected';
              default:
                console.error('Unknown promise state. $q changed internal data representation?');
              }
            }

            function getPreviousIntyg(onComplete) {

              previousWarningMessage = {};

              commonUtkastProxy.getPrevious(viewState.intygModel.grundData.patient.personId, function(existing) {
                previousUtkastWarnings = existing.utkast;
                previousIntygWarnings = existing.intyg;
                if (typeof onComplete === 'function') {
                  onComplete();
                }
              });
            }

            $scope.$on('intyg.loaded', getPreviousIntyg);

          }
        };
      }]);
