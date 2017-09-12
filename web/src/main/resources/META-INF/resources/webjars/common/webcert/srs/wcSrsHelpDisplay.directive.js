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
angular.module('common').directive('wcSrsHelpDisplay', ['common.srsProxy', 'common.fmbViewState',
    function(srsProxy, fmbViewState) {
        'use strict';

        return {
            restrict: 'E',
            transclude: true,
            scope: {
                personId: '=',
                hsaId: '=',
                intygsTyp: '='
            },
            link: function(scope, element, attrs) {
                scope.status = {
                    open: true
                };

                scope.srsStates = fmbViewState;
                scope.srsAvailable = false;
                scope.diagnosKod = "";

                srsProxy.logSrsShown();

                scope.$watch('hsaId', function(newVal, oldVal){
                    if(newVal){
                        srsProxy.getConsent(scope.personId, scope.hsaId).then(function(consent){
                            scope.consentGiven = consent === 'JA' ? true : false;
                        })
                        scope.setConsent = function(consent){
                            scope.consentGiven = consent;
                            srsProxy.setConsent(scope.personId, scope.hsaId, consent);
                        }
                    }
                })

                scope.$watch('srsStates.diagnoses["0"].diagnosKod', function(newVal, oldVal){
                    if(newVal){
                        scope.diagnosKod = newVal;
                        isSrsAvailable(scope.diagnosKod).then(function(srsAvailable){
                            scope.srsAvailable = srsAvailable;    
                        })
                    }
                })

                function isSrsAvailable(){
                    return new Promise(function(resolve, reject){
                        if(scope.intygsTyp.toLowerCase().indexOf('fk7263') > -1 || scope.intygsTyp.toLowerCase().indexOf('lisjp') > -1){
                            srsProxy.getDiagnosisCodes().then(function(diagnosisCodes){
                                scope.diagnosisCodes = diagnosisCodes;
                                for(var i = 0; i < diagnosisCodes.length; i++){
                                    if(scope.diagnosKod === diagnosisCodes[i]){
                                        resolve(true);
                                        break;
                                    }
                                }
                                resolve(false);
                            })
                        }
                    })
                }

            },
            templateUrl: '/web/webjars/common/webcert/srs/wcSrsHelpDisplay.directive.html'
        };
    }]);
