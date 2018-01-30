/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcIntygStatus', [ function() {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            viewState: '=',
        },
        templateUrl: '/web/webjars/common/webcert/intyg/intygHeader/wcPatientStatus/wcPatientStatus.directive.html',
        link: function($scope) {

            $scope.statusFieldId = function() {
                if(!$scope.viewState.common.intygProperties.isSent && !$scope.viewState.common.isIntygOnSendQueue) {
                    return 'certificate-is-sent-to-it-message-text';
                } else if(!$scope.viewState.common.intygProperties.isSent && $scope.viewState.common.isIntygOnSendQueue) {
                    return 'certificate-is-on-sendqueue-to-it-message-text';
                } else {
                    return 'certificate-is-sent-to-recipient-message-text';
                }
            };

            $scope.generateSentText = function () {
                if($scope.isRevoked()) {
                    // Case is handled by wcIntygRelatedRevokedMessage directive.
                    return '';
                }

                var patientDeceased = $scope.isPatientDeceased();
                if(intygType === 'doi' || intygType === 'db') {
                    // db or doi with patient still alive is impossible, and thus not an option.
                    patientDeceased = true;
                }
                var recipientId = moduleService.getModule(intygType).defaultRecipient;
                var recipient = messageService.getProperty('common.recipient.' + recipientId.toLowerCase());
                var vars = {'recipient': recipient};

                if($scope.isSentIntyg()) {
                    if(intygType === 'db' || intygType === 'doi') {
                        return messageService.getProperty((intygType + '.label.status.sent'), vars);
                    } else {
                        return messageService.getProperty(('common.label.status.sent.patient-' + (patientDeceased ? 'dead' : 'alive')), vars);
                    }
                } else {
                    if (patientDeceased) {
                        return messageService.getProperty('common.label.status.signed.patient-dead');
                    } else {
                        return messageService.getProperty((intygType + '.label.status.signed.patient-alive'), vars);
                    }
                }
            };
        }
    };
} ]);
