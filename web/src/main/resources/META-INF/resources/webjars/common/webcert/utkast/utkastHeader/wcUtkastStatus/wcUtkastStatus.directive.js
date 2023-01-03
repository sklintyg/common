/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcUtkastStatus', [
    '$rootScope', '$uibModal',
    'common.moduleService', 'common.messageService', 'common.UtkastViewStateService', 'common.UtkastHeaderViewState',
    'common.IntygStatusService', 'common.certificateEventProxy',
    function($rootScope, $uibModal,
        moduleService, messageService, CommonViewState, HeaderViewState, IntygStatusService, CertificateEventProxy) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                utkastViewState: '=',
                certForm: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/utkastHeader/wcUtkastStatus/wcUtkastStatus.directive.html',
            link: function($scope) {

                $scope.intygstatus1 = {};
                $scope.intygstatus2 = {};

                $scope.statusEvents = [];

                $scope.$on('intyg.loaded', getAllStatuses);
                $scope.$on('intygstatus.updated', getAllStatuses);

                function addToStatusEvents(statusCode, timestamp, vars) {
                    $scope.statusEvents.push({
                        code: statusCode,
                        text: IntygStatusService.getMessageForIntygStatus(statusCode, vars),
                        timestamp: dateToString(timestamp),
                        modal: true
                    });
                }

                function getAllStatuses() {
                    $scope.statusEvents = [];
                    var id = CommonViewState.intyg.certificateId;

                    CertificateEventProxy.getCertificateEvents(id, function(response) {
                        if (response !== null) {
                            angular.forEach(response, function(event) {
                                switch (event.eventCode) {
                                    case 'SKAPAT':
                                    case 'SKAPATFRAN':
                                        addToStatusEvents('us-006', event.timestamp);
                                        break;
                                    case 'LAST':
                                        addToStatusEvents('lus-01', event.timestamp);
                                        break;
                                    case 'MAKULERAT':
                                        addToStatusEvents('lus-02', event.timestamp);
                                        break;
                                    case 'KOMPLETTERAR':
                                    case 'FORLANGER':
                                    case 'ERSATTER':
                                    case 'KOPIERATFRAN':
                                        addRelationalStatus(event);
                                        break;
                                    default:
                                        break;
                                }
                            });
                            IntygStatusService.sortByStatusAndTimestamp($scope.statusEvents);
                        }
                    }, function(error) {
                        CommonViewState.inlineErrorMessageKey = 'common.error.intyg.status.failed.load';
                    });
                }

                function addRelationalStatus(event) {
                    var vars;
                    if (event.extendedMessage) {
                        vars = getMessageVars(event.extendedMessage);

                        if (event.eventCode === 'KOMPLETTERAR') {
                            addToStatusEvents('us-007', event.timestamp, vars);
                        }
                        if (event.eventCode === 'FORLANGER') {
                            addToStatusEvents('us-008', event.timestamp, vars);
                        }
                        if (event.eventCode === 'ERSATTER') {
                            addToStatusEvents('us-009', event.timestamp, vars);
                        }
                        if (event.eventCode === 'KOPIERATFRAN') {
                            addToStatusEvents('us-010', event.timestamp, vars);
                        }
                    }
                }

                function dateToString(timestamp) {
                    // timestamp is sometimes JS date objects, convert those to a comparable string
                    return typeof timestamp === 'object' ? moment(timestamp).format('YYYY-MM-DD[T]HH:mm:ss.SSS') : timestamp;
                }

                function getMessageVars(message) {
                    return {
                        intygsid: message.originalCertificateId,
                        intygstyp: message.originalCertificateType,
                        intygTypeVersion: message.originalCertificateTypeVersion
                    };
                }

                $scope.getIntygStatus1 = function() {
                    // Just in case (should never be here for a signed utkast)
                    if (CommonViewState.isSigned) {
                        return setIntygStatus($scope.intygstatus1, 'is-001');
                    }
                    else if(CommonViewState.isLocked) {
                        if (CommonViewState.isRevoked()) {
                            return setIntygStatus($scope.intygstatus1, 'lus-02');
                        }
                        return setIntygStatus($scope.intygstatus1, 'lus-01');
                    }
                    else if (CommonViewState.intyg.isComplete) {
                        return setIntygStatus($scope.intygstatus1, 'is-016');
                    }
                    return setIntygStatus($scope.intygstatus1, 'is-015');
                };

                $scope.getIntygStatus2 = function() {
                    if (CommonViewState.isLocked){
                        return;
                    }  else if (CommonViewState.saving) {
                        return setIntygStatus($scope.intygstatus2, 'is-013');
                    }
                    else if ($scope.certForm && $scope.certForm.$pristine) {
                        return setIntygStatus($scope.intygstatus2, 'is-014');
                    }
                };

                function setIntygStatus(scopeObj, intygStatus, vars) {
                    scopeObj.code = intygStatus;
                    scopeObj.text = IntygStatusService.getMessageForIntygStatus(intygStatus, vars);
                    scopeObj.modal =  IntygStatusService.intygStatusHasModal(intygStatus);
                    return scopeObj;
                }

                $scope.openAllStatusesModal = function() {
                    var allStatuses = $scope.statusEvents;
                    var allStatusesModalInstance = $uibModal.open({
                        templateUrl: '/web/webjars/common/webcert/intyg/intygHeader/wcIntygStatus/wcIntygStatusModal.template.html',
                        size: 'md',
                        controller: function($scope) {
                            $scope.statuses = allStatuses;
                        }
                    }).result.then(function() {
                        allStatusesModalInstance = undefined;
                    },function() {
                        allStatusesModalInstance = undefined;
                    });
                };
            }
        };
    }
]);
