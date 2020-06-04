/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
 * Recommendation directive
 */
angular.module('common').directive('wcSrsPrediction', [
    'common.srsProxy', '$window', '$timeout',
    function(srsProxy, $window, $timeout) {
        'use strict';

        return {
            restrict: 'EA',
            link: function(scope, element, attrs) {
                scope.srs.isQuestionsCollapsed = true;
                scope.questionsCollapserClicked = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    if (scope.srs.isQuestionsCollapsed) {
                        srsProxy.logSrsShowQuestionsClicked(scope.srs.userClientContext, scope.srs.intygId,
                            scope.srs.vardgivareHsaId, scope.srs.hsaId);
                        $timeout(function() {
                            document.getElementById('risk-calculation-input-container').scrollIntoView({ behavior: 'smooth' });
                        },2);

                    } else {
                        srsProxy.logSrsHideQuestionsClicked(scope.srs.userClientContext, scope.srs.intygId,
                            scope.srs.vardgivareHsaId, scope.srs.hsaId);
                        $timeout(function() {
                            document.getElementById('riskDiagram').scrollIntoView({ behavior: 'smooth'});
                        },2);
                    }
                    scope.srs.isQuestionsCollapsed = !scope.srs.isQuestionsCollapsed;
                };
                scope.calculateClicked = function() {
                    srsProxy.logSrsCalculateClicked(scope.srs.userClientContext, scope.srs.intygId,
                        scope.srs.vardgivareHsaId, scope.srs.hsaId);
                    scope.srs.isQuestionsCollapsed = !scope.srs.isQuestionsCollapsed;
                    scope.retrieveAndSetPrediction().then(function() {
                        scope.srs.showVisaKnapp = false;
                        $timeout(function() {
                            document.getElementById('riskDiagram').scrollIntoView({ behavior: 'smooth'});
                        },2);
                        scope.setPrediktionMessages();
                        scope.setPredictionRiskLevel();
                    });
                };
                scope.retrieveAndSetPrediction = function() {
                    var qaIds = scope.getSelectedAnswerOptions();
                    return srsProxy.getPredictions(scope.srs.intygId, scope.srs.personId, scope.srs.diagnosKod, qaIds, scope.srs.daysIntoSickLeave)
                        .then(
                            function(predictions) {
                                scope.srs.predictions = predictions;
                            }, function(error) {
                                scope.srs.predictions = 'error';
                            }
                        );
                };


            },
            templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsPrediction.directive.html'
        };
    }]);
