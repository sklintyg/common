/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

/**
 * Created by BESA on 2015-05-09.
 */

/**
 * arendeHantera directive. Common directive for Hanterad checkbox
 */
angular.module('common').directive('arendeHantera',
    [ '$window', '$log', 'common.ArendeProxy', 'common.statService', 'common.ErrorHelper',
        function($window, $log, ArendeProxy, statService, ErrorHelper) {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                templateUrl: '/web/webjars/common/webcert/fk/arenden/arendeHantera.directive.html',
                scope: {
                    arendeListItem: '=',
                    parentViewState: '='
                },
                controller: function($scope, $element, $attrs) {

                    $scope.showHandleToggle = function() {
                        return !$scope.parentViewState.intygProperties.isRevoked &&
                            !$scope.parentViewState.intygProperties.kompletteringOnly &&
                            // Enforce business rule FS-011, from FK + answer should remain closed
                            ($scope.arendeListItem.arende.fraga.frageStallare === 'WC' ||
                            $scope.arendeListItem.arende.fraga.status !== 'CLOSED');
                    };

                    $scope.handledFunction = function(newState) {
                        if (arguments.length) {
                            if (newState) {
                                $scope.updateAsHandled($scope.arendeListItem);
                            }
                            else {
                                $scope.updateAsUnhandled($scope.arendeListItem);
                            }
                        }
                        else {
                            return $scope.arendeListItem.arende.fraga.status === 'CLOSED';
                        }
                    };

                    $scope.updateAsHandled = function(arendeListItem, deferred) {
                        $log.debug('updateAsHandled:' + arendeListItem.arende);
                        arendeListItem.updateHandledStateInProgress = true;

                        ArendeProxy.closeAsHandled(arendeListItem.arende.fraga.internReferens, $scope.parentViewState.intygProperties.type, function(result) {
                            arendeListItem.activeErrorMessageKey = null;
                            arendeListItem.updateHandledStateInProgress = false;
                            if (result !== null) {
                                angular.copy(result, arendeListItem.arende);
                                arendeListItem.updateArendeListItem();
                                statService.refreshStat();
                            }
                            $window.doneLoading = true;
                            if(deferred) {
                                deferred.resolve();
                            }
                        }, function(errorData) {
                            // show error view
                            arendeListItem.updateHandledStateInProgress = false;
                            arendeListItem.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                            $window.doneLoading = true;
                            if(deferred) {
                                deferred.resolve();
                            }
                        });
                    };

                    $scope.updateAsUnhandled = function(arendeListItem) {
                        $log.debug('updateAsUnHandled:' + arendeListItem);
                        arendeListItem.updateHandledStateInProgress = true; // trigger local

                        ArendeProxy.openAsUnhandled(arendeListItem.arende.fraga.internReferens, $scope.parentViewState.intygProperties.type, function(result) {
                            $log.debug('Got openAsUnhandled result:' + result);
                            arendeListItem.activeErrorMessageKey = null;
                            arendeListItem.updateHandledStateInProgress = false;

                            if (result !== null) {
                                angular.copy(result, arendeListItem.arende);
                                arendeListItem.updateArendeListItem();
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            arendeListItem.updateHandledStateInProgress = false;
                            arendeListItem.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                        });
                    };

                }
            };
        }]);
