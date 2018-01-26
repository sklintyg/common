/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('fk7263').controller('fk7263.EditCert.Form8bCtrl',
    ['$scope', '$log', '$timeout', 'fk7263.EditCertCtrl.ViewStateService',
        'common.DateUtilsService', 'common.DateRangeService', 'common.fmbViewState', 'common.messageService',
        'common.UtkastValidationService',
        function($scope, $log, $timeout, viewState, dateUtils, DateRangeService, fmbViewState, messageService,
            UtkastValidationService) {
            'use strict';
            // private vars

            // scope
            $scope.model = viewState.intygModel;
            $scope.viewState = viewState;
            $scope.fmbViewState = fmbViewState;
            $scope.lastEffectiveDateNoticeText = '';

            // 1. onload
            // 2. on check change
            // 3. on manual change.

            var _dateRangeService = DateRangeService.FromTos.build(['nedsattMed25','nedsattMed50','nedsattMed75','nedsattMed100']);

            $scope.onToFieldBlur = function(fromTo) {
                //If from-field has a valid date, and the to-field contains a parsable day code, calculate a new to-field dateString
                if (fromTo && fromTo.from.moment && fromTo.from.valid) {
                    var toDate;
                    var days = dateUtils.parseDayCodes(fromTo.to.dateString);
                    if (days !== null) {
                        //Take away 1 day, because the dayCode defines the total length of the interval we should get.
                        toDate = moment(fromTo.from.moment).add(days - 1 , 'days').format('YYYY-MM-DD');
                    } else {
                        var months = dateUtils.parseMonthCode(fromTo.to.dateString);
                        if (months !== null) {
                            toDate = moment(fromTo.from.moment).add(months, 'months').format('YYYY-MM-DD');
                        }
                    }
                    if (toDate) {
                        fromTo.to.update(toDate);
                        fromTo.to.form.$setDirty();
                    }
                }

            };
            // 8b. Arbetsförmåga date management
            $scope.field8b = {
                nedsattMed25 : _dateRangeService.nedsattMed25,
                nedsattMed50 : _dateRangeService.nedsattMed50,
                nedsattMed75 : _dateRangeService.nedsattMed75,
                nedsattMed100 : _dateRangeService.nedsattMed100,
                onChangeWorkStateCheck : function(nedsattModelName) {
                    $log.debug('------------------------ onChangeWorkStateCheck');

                    _dateRangeService[nedsattModelName].check();

                    var descriptionModelName = nedsattModelName + 'Beskrivning';
                    if (!$scope.field8b[nedsattModelName].workState) {
                        viewState.intygModel.updateToAttic(descriptionModelName);
                        viewState.intygModel.clear('form8b.'+descriptionModelName, viewState.intygModel.form8b);
                    } else {
                        viewState.intygModel.restoreFromAttic(descriptionModelName);
                    }
                },
                info : _dateRangeService
            };

            $scope.info = _dateRangeService;


            $scope.$watch('field8b.nedsattMed25.workState', function(newVal, oldVal) {
                $log.debug('workstate : newVal:' + newVal + ', oldVal:' + oldVal);
            });

            var doneLoading = false;
            $scope.$watch(function() {

                return !!(viewState.common.doneLoading &&
                    angular.isObject($scope.form8b.nedsattMed25from) &&
                    angular.isObject($scope.form8b.nedsattMed25tom) &&
                    angular.isObject($scope.form8b.nedsattMed50from) &&
                    angular.isObject($scope.form8b.nedsattMed50tom) &&
                    angular.isObject($scope.form8b.nedsattMed75from) &&
                    angular.isObject($scope.form8b.nedsattMed75tom) &&
                    angular.isObject($scope.form8b.nedsattMed100from) &&
                    angular.isObject($scope.form8b.nedsattMed100tom));

            }, function(newVal, oldVal) {
                if (doneLoading) {
                    return;
                }
                if (newVal) {
                    _dateRangeService.linkFormAndModel($scope.form8b, viewState.intygModel, $scope);
                    if (viewState.intygModel.grundData.relation.sistaGiltighetsDatum){
                        var percent = {
                            'HELT_NEDSATT': 100,
                            'NEDSATT_MED_3_4': 75,
                            'NEDSATT_MED_1_2': 50,
                            'NEDSATT_MED_1_4': 25
                        }[viewState.intygModel.grundData.relation.sistaSjukskrivningsgrad];
                        if (!percent) {
                            percent = 'okänt';
                        }
                        $scope.lastEffectiveDateNoticeText = messageService
                            .getProperty('fk7263.helptext.sista-giltighets-datum')
                            .replace('{{lastEffectiveDate}}', viewState.intygModel.grundData.relation.sistaGiltighetsDatum)
                            .replace('{{sjukskrivningsgrad}}', percent);
                    }
                    doneLoading = true;
                }
            });

            $scope.validate = function() {
                // When a date is selected from a date popup a blur event is sent.
                // In the current version of Angular UI this blur event is sent before utkast model is updated
                // This timeout ensures we get the new value in $scope.model
                $timeout(function() {
                    UtkastValidationService.validate($scope.model);
                });
            };
        }]);
