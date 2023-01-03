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
angular.module('common').directive('latestEvents', ['$filter', 'common.messageService', 'common.recipientsFactory', '$uibModal', '$state',
    function($filter, messageService, recipientsFactory, $uibModal, $state) {
        'use strict';

        function _getEventText(msgProperty, params) {
            var text = messageService.getProperty(msgProperty, params);
            return text.length === 0 ? '' : text;
        }


        return {
            restrict: 'E',
            replace: true,
            scope: {
                certId: '@',
                events: '=',
                maxEvents: '@',
                tightRows: '@',
                showMoreButtonLocation: '@'
            },
            templateUrl: '/web/webjars/common/minaintyg/components/latestEvents/latestEvents.directive.html',
            link: function(scope) {

                if (!scope.showMoreButtonLocation) {
                    scope.showMoreButtonLocation = 'top';
                }

                function _updateEventModel() {
                    scope.filteredEvents = $filter('miRelevantEventFilter')(scope.events);
                }
                scope.$watch('events', function(){
                    _updateEventModel();
                });

                _updateEventModel();


                scope.messageService = messageService;
                scope.isCollapsedArchive = true;

                // Compile event event message info (date and text)
                scope.getEventInfo = function(event) {
                    var timestamp = event.timestamp ?
                        moment(event.timestamp).format('YYYY-MM-DD HH:mm') :
                        messageService.getProperty('certificates.events.unknowndatetime');

                    // Ugly knowledge of inner workings of event object. Perhaps move to backed?
                    var params = [];
                    if (event.eventType === 'STATUS') {
                        params.push(recipientsFactory.getNameForId(event.target));
                    }
                    if (event.eventType === 'RELATION') {

                        params.push(event.target);
                        params.push(event.intygsTyp);
                        params.push(event.intygsTypVersion);
                    }

                    var msgProperty = 'certificates.events.' + event.type.toLowerCase(); //received [sic] or sent
                    var text = _getEventText(msgProperty, params);
                    return {timestamp: timestamp, text: text};
                };

                scope.eventsShown = function(events, eventViewCollapsed) {
                    var nrOfStats = events ? events.length : 0;
                    var shown = Math.min(nrOfStats, scope.maxEventRows(eventViewCollapsed));
                    return messageService.getProperty('certificates.events.eventsshown', [shown, nrOfStats]);
                };

                scope.maxEventRows = function(isCollapsedArchive) {
                    return scope.maxEvents ? scope.maxEvents : (isCollapsedArchive ? 2 : 4);
                };

                scope.expandClicked = function() {
                    if (scope.filteredEvents.length > 4) {
                        openModal();
                    } else {
                        scope.isCollapsedArchive = !scope.isCollapsedArchive;
                    }
                };

                scope.viewCert = function(type, typeVersion, certId) {
                    $state.go(type.toLowerCase() + '-view', {certificateId: certId, intygTypeVersion: typeVersion});
                };

                function openModal() {
                    var modalCtrl = function($scope, $uibModalInstance) {
                        $scope.close = function() {
                            $uibModalInstance.close();
                        };

                        $scope.viewCert = function(type, certId) {
                            $scope.close();
                            scope.viewCert(type, certId);
                        };
                    };

                    $uibModal.open({
                        scope: scope,
                        windowClass: 'latest-events-modal',
                        templateUrl: '/web/webjars/common/minaintyg/components/latestEventsModal/latestEventsModal.html',
                        keyboard: true,
                        controller: modalCtrl
                    });
                }

            }

        };

    }]
);


