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
    [ '$window', '$log', 'common.statService', 'common.ErrorHelper', 'common.ArendeProxy', 'common.ArendeHelper', 'common.dynamicLabelService',
        function($window, $log, statService, ErrorHelper, ArendeProxy, ArendeHelper, dynamicLabelService) {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                templateUrl: '/web/webjars/common/webcert/fk/arenden/hantera/arendeHantera.directive.html',
                scope: {
                    arendeList: '=',
                    arendeListItem: '=',
                    parentViewState: '='
                },
                controller: function($scope, $element, $attrs) {

                    $scope.showHandleToggle = function() {
                        var arendeModel = $scope.arendeListItem;
                        var isRevoked = $scope.parentViewState.intygProperties.isRevoked;

                        //Special case - fraga from FK on revoked intyg and not handled already: allow to mark as handled (see INTYG-3617)
                        if (isRevoked && arendeModel.arende.fraga.frageStallare === 'FK' && arendeModel.arende.fraga.status !== 'CLOSED') {
                            return true;
                        }

                        //Rule 1: Revoked intyg can't be toggled at all
                        if (isRevoked) {
                            return false;
                        }

                        //Rule 2: Handled komplettering answered with new intyg can not be toggled back to unhandled (See INTYG-3792)
                        if (arendeModel.isKomplettering() && arendeModel.arende.fraga.status === 'CLOSED' &&
                                angular.isObject(arendeModel.arende.answeredWithIntyg)) {
                            return false;
                        }

                        // Enforce default business rule FS-011, from FK + answer should remain closed
                        return arendeModel.arende.fraga.frageStallare === 'WC' || !arendeModel.arende.svar.meddelande;
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

                                $scope.parentViewState.updateKompletteringarArende(arendeListItem.arende);

                                statService.refreshStat();
                                ArendeHelper.splitToSingleItem(arendeListItem, $scope.arendeList);
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

                                $scope.parentViewState.updateKompletteringarArende(arendeListItem.arende);

                                statService.refreshStat();
                                ArendeHelper.checkMergeToKompletteringItem(arendeListItem, $scope.arendeList);
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
