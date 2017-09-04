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
 * Display SRS help texts
 */
angular.module('common').directive('wcSrsContent', ['$log', 'common.ObjectHelper', 'common.fmbService', 'common.srsProxy',
    function($log, ObjectHelper, fmbService, srsProxy) {
        'use strict';

        return {
            restrict: 'EA',
            scope: {
                fmb: '=',
                relatedFormId: '@',
                status: '=',
                fieldName: '='
            },
            link: function(scope, element, attrs) {
<<<<<<< HEAD
                scope.consentGiven = true;
=======
                srsProxy.getConsent().then(function(consent){
                    scope.consentGiven = consent === 'JA' ? true : false;
                })
                
>>>>>>> develop
                scope.inQuestionaireState = true;

                scope.typeOfVariable = function(variable){
                    var t = typeof variable;
                    return t;
                }
                $log.debug(scope.fmb);
<<<<<<< HEAD
                
                srsProxy.setConsent(true).then(function(consent){
=======

                scope.setConsent = function(consent){
                    scope.consentGiven = consent;
                    srsProxy.setConsent(consent);
                }

                srsProxy.getQuestions().then(function(questions){
                    scope.questions = questions;
                    console.log(questions);
                })
                
               /* srsProxy.setConsent(true).then(function(consent){
>>>>>>> develop
                    console.log("set consent: " + consent);
                    return srsProxy.getConsent()
                }).then(function(consent){
                    scope.consentGiven = consent === 'JA' ? true : false;
                    console.log("get consent: " + consent);
                    return srsProxy.getStatistik();
                }).then(function(statistik){
                    console.log("statistik: " + JSON.stringify(statistik));
                    scope.riskSignal = statistik.level;
                    scope.atgarder = statistik.atgarder
<<<<<<< HEAD
                })
=======
                })*/
>>>>>>> develop

            },
            templateUrl: '/web/webjars/common/webcert/srs/wcSrsContent.directive.html'
        };
    }]);
