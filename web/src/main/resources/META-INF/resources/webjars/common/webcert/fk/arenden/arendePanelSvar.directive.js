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
 * Created by BESA on 2015-03-05.
 */

/**
 * arendePanelSvar directive. Handles all komplettering and svar components.
 */
angular.module('common').directive('arendePanelSvar',
    [ '$window', '$log', '$state', '$stateParams', '$q',
        'common.ArendeProxy', 'common.ArendeHelper', 'common.statService', 'common.ObjectHelper', 'common.ErrorHelper',
        'common.IntygCopyRequestModel', 'common.ArendeSvarModel', 'common.pingService', 'common.FocusElementService',
        function($window, $log, $state, $stateParams, $q, ArendeProxy, ArendeHelper, statService, ObjectHelper,
            ErrorHelper, IntygCopyRequestModel, ArendeSvarModel, pingService, focusElement) {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                templateUrl: '/web/webjars/common/webcert/fk/arenden/arendePanelSvar.directive.html',
                scope: {
                    panelId: '@',
                    arendeListItem: '=',
                    parentViewState: '='
                },
                controller: function($scope, $element, $attrs) {

                    // For readability, keep a local struct with the values used from parent scope
                    var ArendeSvar = ArendeSvarModel.build($scope.parentViewState, $scope.arendeListItem);
                    $scope.arendeSvar = ArendeSvar;

                    $scope.$watch('arendeSvar', function() {
                        pingService.registerUserAction('edited-arende-details');
                    }, true);

                    $scope.showAnswerPanel = function() {
                        var hasMeddelandeIsClosed = ArendeSvar.meddelande && ArendeSvar.status === 'CLOSED';
                        var cannotKomplettera = ArendeSvar.answerKompletteringWithText || hasMeddelandeIsClosed;
                        return ArendeSvar.intygProperties.kompletteringOnly ||
                            (ArendeSvar.amne !== 'KOMPLT') ||
                            (ArendeSvar.amne === 'KOMPLT' && cannotKomplettera);
                    };

                    $scope.showAnswer = function() {
                        return (ArendeSvar.status === 'CLOSED' && ArendeSvar.meddelande) || (ArendeSvar.status === 'ANSWERED');
                    };

                    $scope.showKompletteringControls = function() {
                        return !ArendeSvar.intygProperties.kompletteringOnly &&
                            $scope.isAnswerAllowed() &&
                            ArendeSvar.amne === 'KOMPLT' && !ArendeSvar.answerKompletteringWithText;
                    };

                    $scope.showRegularAnswer = function() {
                        return $scope.isAnswerAllowed() && !ArendeSvar.answerKompletteringWithText;
                    };


                    $scope.showButtonBar = function() {
                        // VÄNTAR på svar från Vårdenheten och det är inte kompletteringsvy vi renderar
                        return !ArendeSvar.intygProperties.kompletteringOnly && ArendeSvar.status === 'PENDING_INTERNAL_ACTION';
                    };

                    $scope.isAnswerAllowed = function() {
                        return !ArendeSvar.answerDisabled && !ArendeSvar.intygProperties.isRevoked;
                    };

                    /**
                     * Svara på ärende från fk
                     */
                    $scope.sendAnswer = function sendAnswer() {
                        ArendeSvar.updateInProgress = true; // trigger local spinner

                        ArendeProxy.saveAnswer(ArendeSvar, ArendeSvar.intygProperties.type, function(result) {
                            $log.debug('Got saveAnswer result:' + result);
                            ArendeSvar.updateInProgress = false;
                            ArendeSvar.activeErrorMessageKey = null;
                            if (result !== null) {
                                // update real item
                                angular.copy(result, $scope.arendeListItem.arende);
                                $scope.arendeListItem.updateArendeListItem(result);
                                ArendeSvar.update($scope.parentViewState, $scope.arendeListItem);
                                statService.refreshStat();
                            }
                        }, function(errorData) {
                            // show error view
                            ArendeSvar.updateInProgress = false;
                            ArendeSvar.activeErrorMessageKey = ErrorHelper.safeGetError(errorData);
                        });
                    };

                    $scope.answerWithMessage = function() {
                        ArendeSvar.answerKompletteringWithText = true;
                        focusElement('answerText-' + ArendeSvar.fragaInternReferens);
                    };

                    $scope.abortTextAnswer = function() {
                        //Should we empty the svarstext input field?
                        ArendeSvar.answerKompletteringWithText = false;
                    };

                    $scope.updateAnsweredAsHandled = function(deferred, unhandledarendes) {
                        if (unhandledarendes === undefined || unhandledarendes.length === 0) {
                            return;
                        }
                        ArendeProxy.closeAllAsHandled(unhandledarendes,
                            function(arendes) {
                                if (arendes) {
                                    angular.forEach(arendes, function(arende) { //unused parameter , key
                                        ArendeHelper.decorateSingleItem(arende);
                                    });
                                    statService.refreshStat();
                                }
                                $window.doneLoading = true;
                                if (deferred) {
                                    deferred.resolve();
                                }
                            }, function() { // unused parameter: errorData
                                // show error view
                                $window.doneLoading = true;
                                if (deferred) {
                                    deferred.resolve();
                                }
                            });
                    };

                    // listeners - interscope communication
                    var unbindmarkAnsweredAsHandledEvent = $scope.$on('markAnsweredAsHandledEvent',
                        function($event, deferred, unhandledarendes) {
                            $scope.updateAnsweredAsHandled(deferred, unhandledarendes);
                        });
                    $scope.$on('$destroy', unbindmarkAnsweredAsHandledEvent);
                }
            };
        }]);
