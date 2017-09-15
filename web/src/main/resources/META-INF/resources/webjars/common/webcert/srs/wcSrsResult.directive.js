
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
 * Display SRS questionaire
 */
angular.module('common').directive('wcSrsResult', ['common.ObjectHelper', 'common.srsProxy', '$stateParams',
    function(ObjectHelper, srsProxy, $stateParams) {
        'use strict';

        return {
            restrict: 'E',
            link: function (scope, element, attrs) {

                scope.atgarderErrorMessage="";
                scope.atgarderInfoMessage="";

                scope.getSrs = function(){
                    var qaIds = scope.getSelectedAnswerOptions();
                    console.log(JSON.stringify(qaIds));
                    srsProxy.getSrs($stateParams.certificateId, scope.personId, scope.diagnosKod, qaIds, true, true, true).then(function (statistik) {
                        scope.atgarderErrorMessage = "";
                        if(statistik == 'error'){
                            scope.atgarderErrorMessage = "Det gick inte att hämta information om åtgärder";
                        }
                        scope.statistik = statistik;
                        setAtgarderObs();
                    })
                }
                
                scope.getSelectedAnswerOptions = function() {
                    var selectedOptions = [];
                    for (var i = 0; i < scope.questions.length; i++) {
                        selectedOptions.push({ questionId: scope.questions[i].questionId, answerId: scope.questions[i].model.id });
                    }
                    return selectedOptions;
                }

                function setAtgarderObs() {
                    var atgarderObs = scope.statistik.atgarderObs;
                    scope.statistik.atgardObs = "";
                    for (var i = 0; i < atgarderObs.length; i++) {
                        scope.statistik.atgardObs += atgarderObs[i];
                        scope.statistik.atgardObs += i < atgarderObs.length - 1 ? ", " : "";
                    }
                }
            },
            templateUrl: '/web/webjars/common/webcert/srs/wcSrsResult.directive.html'
        };
    }]);
