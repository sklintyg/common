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
/**
 * Recommendation directive
 */
angular.module('common').directive('wcSrsPrediction', [
    'common.srsProxy',
    function(srsProxy) {
        'use strict';

        return {
            restrict: 'EA',
            link: function(scope, element, attrs) {
                scope.srs.isQuestionsCollapsed = false;
                scope.questionsCollapserClicked = function() {
                    if (scope.srs.isQuestionsCollapsed) {
                        srsProxy.logSrsShowQuestionsClicked(scope.srs.userClientContext, scope.srs.intygId,
                            scope.srs.vardgivareHsaId, scope.srs.hsaId);
                    } else {
                        srsProxy.logSrsHideQuestionsClicked(scope.srs.userClientContext, scope.srs.intygId,
                            scope.srs.vardgivareHsaId, scope.srs.hsaId);
                    }
                    scope.srs.isQuestionsCollapsed = !scope.srs.isQuestionsCollapsed;
                };
                scope.calculateClicked = function() {
                    srsProxy.logSrsCalculateClicked(scope.srs.userClientContext, scope.srs.intygId,
                        scope.srs.vardgivareHsaId, scope.srs.hsaId);
                    scope.retrieveAndSetPrediction().then(function() {
                        scope.srs.showVisaKnapp = false;
                        scope.setPrediktionMessages();
                        scope.setPredictionRiskLevel();
                    });
                };
                scope.retrieveAndSetPrediction = function() {
                    var qaIds = scope.getSelectedAnswerOptions();
                    return srsProxy.getPrediction(scope.srs.intygId, scope.srs.personId, scope.srs.diagnosKod,
                        qaIds).then(function(prediction) {
                            scope.srs.prediction = prediction;
                    }, function(error) {
                        scope.srs.prediction = 'error';
                    });
                };


            },
            templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSrsPanelTab/wcSrsPrediction.directive.html'
        };
    }]);
