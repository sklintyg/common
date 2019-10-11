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

angular.module('common').directive('fmbVarning', ['$log', '$filter', 'common.fmbProxy', 'common.ObjectHelper',
    function($log, $filter, FMBProxy, ObjectHelper) {
    'use strict';
    return {
        restrict: 'E',
        scope: {
            model: '=',
            viewstate: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueSjukskrivningar/fmbVarning.directive.html',
        link: function($scope) {

            function requestFMBVarningUpdate(){
                if($scope.model && $scope.model.diagnoser && $scope.model.diagnoser.length > 0 &&
                    ObjectHelper.isDefined($scope.model.diagnoser[0].diagnosKod) &&
                    $scope.viewstate.totalDays && $scope.viewstate.totalDays > 0) {
                    $log.debug('fmbvarning - diagnoses available and period entered - requesting fmbvarning info');
                    FMBProxy.getValidateSjukskrivningstid({
                        icd10Kod1: $scope.model.diagnoser[0].diagnosKod,
                        icd10Kod2: $scope.model.diagnoser[1].diagnosKod,
                        icd10Kod3: $scope.model.diagnoser[2].diagnosKod,
                        foreslagenSjukskrivningstid: $scope.viewstate.totalDays,
                        personnummer: $scope.model.grundData.patient.personId
                    }).then(function(data){
                        $log.debug('fmbvarning - updated from server');
                        var fmbVarning = data;
                        $scope.fmbVarning = fmbVarning;

                        if (fmbVarning.overskriderRekommenderadSjukskrivningstid) {
                            if (fmbVarning.totalTidigareSjukskrivningstid < 1) {
                                $scope.fmbVarning.text = 'Den föreslagna sjukskrivningsperioden är längre än FMBs rekommendation på ' +
                                    fmbVarning.maximaltRekommenderadSjukskrivningstid + ' dagar (' +
                                    fmbVarning.maximaltRekommenderadSjukskrivningstidSource + ') för diagnosen ' +
                                    fmbVarning.aktuellIcd10Kod + '. Ange en motivering för att underlätta Försäkringskassans handläggning.';
                            } else {

                                $scope.fmbVarning.text = 'Den totala sjukskrivningsperioden är ' +
                                    fmbVarning.totalSjukskrivningstidInklusiveForeslagen +
                                    ' dagar och därmed längre än FMBs rekommendation på ' +
                                    fmbVarning.maximaltRekommenderadSjukskrivningstid +
                                    ' dagar (' + fmbVarning.maximaltRekommenderadSjukskrivningstidSource + ') för diagnosen ' +
                                    fmbVarning.aktuellIcd10Kod +
                                    '. Ange en motivering för att underlätta Försäkringskassans handläggning. Sjukskrivningsperioden är baserad på patientens sammanhängande intyg på denna vårdenhet.';
                            }
                        }

                    }, function(error) {
                        $scope.fmbVarning = { overskriderRekommenderadSjukskrivningstid: false, error: true };
                        $log.debug('fmbvarning error: ' + error);
                    });
                } else {
                    $scope.fmbVarning = { overskriderRekommenderadSjukskrivningstid: false };
                    $log.debug('fmbvarning - diagnoses or period not entered yet');
                }
            }

            $scope.$on('diagnos.changed', function(event, data){
                requestFMBVarningUpdate();
            });

            $scope.$on('sjukskrivning.periodUpdated', function(event, data){
                requestFMBVarningUpdate();
            });

        }
    };

}]);
