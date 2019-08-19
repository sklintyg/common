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
angular.module('common').directive('miCertificateActionButtons',
    ['$log', '$state', 'common.messageService', 'common.IntygListService', 'common.dialogService', 'MIUser',
      function($log, $state, messageService, listCertService, dialogService, MIUser) {
        'use strict';

        return {
          restrict: 'E',
          scope: {
            certModel: '=', // certificate model
            onSend: '&?', // handler for send button.
            enableArchive: '=', // handler for send button. if attribute not present = not enabled
            enablePrint: '=', // true/false to show print as pdf button
            enableSend: '=', // true/false to show send button
            enableCustomize: '=', // boolean, if customize button should be enabled or not
            onCustomizePdf: '&?',  // handler for customize pdf  button.
            isReplaced: '=' // boolean for telling us whether the cert is replaced or not.
          },
          templateUrl: '/web/webjars/common/minaintyg/components/miCertificateActionButtons/miCertificateActionButtons.directive.html',
          controller: function($scope) {
            //expose messageservice so template can present messages
            $scope.messageService = messageService;

            //pdf download link requires the certificate to be present
            function buildPdfLink() {
              if ($scope.certModel) {
                return '/moduleapi/certificate/' + $scope.certModel.typ + '/' + $scope.certModel.textVersion + '/' +
                    $scope.certModel.id + '/pdf';
              } else {
                return undefined;
              }
            }

            var _dialogInstance;

            $scope.archiveSelected = function() {
              var item = $scope.certModel;

              $log.debug('requesting archiving of certificate ' + item.id);
              $scope.dialog.acceptprogressdone = false;
              listCertService.archiveCertificate(item, function(fromServer, oldItem) {
                $log.debug('archive request callback:' + fromServer);
                if (fromServer !== null) {
                  $scope.dialog.acceptprogressdone = true;
                  _dialogInstance.close();
                  //Goto inkorgen
                  $state.go('inkorg');
                } else {
                  _dialogInstance.close();
                  dialogService.showDialog($scope, {
                    dialogId: 'archive-error-dialog',
                    titleId: 'error.generictechproblem.title',
                    bodyTextId: 'error.modal.couldnotarchivecert',
                    button1text: 'common.close',
                    templateUrl: '/app/partials/error-dialog.html',
                    autoClose: true
                  });
                }
              });
            };

            // Archive dialog
            $scope.onArchiveClicked = function() {
              _dialogInstance = dialogService.showDialog($scope, {
                dialogId: 'archive-confirmation-dialog',
                titleId: 'modules.actionbar.archivedialog.title',
                bodyTextId: 'modules.actionbar.archivedialog.body',
                button1click: function() {
                  $log.debug('archive confirmed by user');
                  $scope.archiveSelected();
                },
                button1id: 'archive-button',
                button1text: 'modules.actionbar.archivedialog.archive.button',
                button1icon: 'icon-box',
                autoClose: false
              });
            };
            $scope.onDownloadClicked = function() {

              function downloadPdf() {
                window.open(buildPdfLink(), '_blank');
              }

              if (MIUser.sekretessmarkering) {
                dialogService.showDialog($scope, {
                  dialogId: 'mi-downloadpdf-sekretess-dialog',
                  titleId: 'pdf.sekretessmarkeringmodal.header',
                  bodyTextId: 'pdf.sekretessmarkeringmodal.body',
                  button1click: downloadPdf,
                  button2click: function() {
                  },
                  button1id: 'close-fkdialog-logout-button',
                  button1text: 'pdf.sekretessmarkeringmodal.button1',
                  button2text: 'pdf.sekretessmarkeringmodal.button2',
                  button2visible: true,
                  autoClose: true
                });
              } else {
                downloadPdf();
              }
            };
          }
        };
      }]);
