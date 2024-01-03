/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').directive('ueSjukskrivningar', ['$log', '$rootScope', 'common.ArendeListViewStateService', 'common.SjukskrivningarViewStateService',
    'common.UtkastValidationService', 'common.messageService', 'common.UtkastViewStateService', 'common.fmbProxy', 'common.ObjectHelper',
    function($log, $rootScope, ArendeListViewState, viewstate, UtkastValidationService, messageService, UtkastViewState, FMBProxy, ObjectHelper) {
    'use strict';
    return {
        restrict: 'E',
        scope: {
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueSjukskrivningar/ueSjukskrivningar.directive.html',
        link: function($scope) {

            //Support custom IFS text keys
            if ($scope.config.hoursPerWeek) {
                $scope.hoursPerWeek = $scope.config.hoursPerWeek;
            } else {
                $scope.hoursPerWeek = {
                    labelkey1: 'common.sit.label.sjukskrivning.hoursperweek.1',
                    labelkey2: 'common.sit.label.sjukskrivning.hoursperweek.2',
                    hlpKey: 'common.sit.help.sjukskrivning.hoursperweek'
                };
            }
            $scope.lightBulbTooltip = messageService.getProperty('common.lightbulb.tooltip');

            var validation = $scope.validation = UtkastViewState.validation;
            $scope.intygIsLocked = UtkastViewState.isLocked;

            $scope.$watch('validation.messagesByField', function() {
                $scope.validationsForPeriod = {};
                $scope.overlapValidations = [];

                if (!validation.messagesByField) {
                    return;
                }

                angular.forEach($scope.config.fields, function(field) {

                    var key = $scope.config.modelProp + '.period.' + field.toLowerCase();

                    var fromValidations = validation.messagesByField[key + '.from'];
                    var tomValidations = validation.messagesByField[key + '.tom'];
                    var periodValidations = validation.messagesByField[key];

                    $scope.validationsForPeriod[field] = [];
                    if (fromValidations) {
                        $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].concat(fromValidations);
                    }
                    if (tomValidations) {
                        $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].concat(tomValidations);
                    }
                    if (periodValidations) {
                        $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].concat(periodValidations);
                    }

                    // The validation message for PERIOD_OVERLAP should only be displayed once even if several periods overlaps
                    angular.forEach($scope.validationsForPeriod[field], function(validation) {
                        if (validation.type === 'PERIOD_OVERLAP' && $scope.overlapValidations.length === 0) {
                            $scope.overlapValidations.push(validation);
                        }
                    });

                    // The validation message for PERIOD_OVERLAP should not be displayed at each period
                    function noPeriodOverlaps(validation) {
                        return validation.type !== 'PERIOD_OVERLAP';
                    }

                    $scope.validationsForPeriod[field] = $scope.validationsForPeriod[field].filter(noPeriodOverlaps);
                });
            });

            $scope.hasValidationError = function(field, type) {
                var fieldKey = $scope.config.modelProp + '.period.' + field;
                var typeKey = fieldKey + '.' + type;
                return validation.messagesByField &&
                    (!!validation.messagesByField[fieldKey.toLowerCase()] ||
                        !!validation.messagesByField[typeKey.toLowerCase()]);
            };

            $scope.hasKompletteringar = function() {
                return ArendeListViewState.hasKompletteringar($scope.config.modelProp);
            };
            $scope.validate = function() {
                UtkastValidationService.validate($scope.model);
            };
            $scope.viewstate = viewstate.reset();

            function updatePeriods(){
                viewstate.updatePeriods();
                $rootScope.$broadcast('sjukskrivning.periodUpdated');
            }

            function setup() {
                viewstate.setModel($scope.model[$scope.config.modelProp]);
                updatePeriods();

                if ($scope.model.grundData.relation.sistaGiltighetsDatum) {
                    $scope.lastEffectiveDateNoticeText = messageService
                        .getProperty($scope.model.typ + '.help.sjukskrivningar.sista-giltighets-datum')
                        .replace('{{lastEffectiveDate}}', $scope.model.grundData.relation.sistaGiltighetsDatum)
                        .replace('{{sjukskrivningsgrad}}', $scope.model.grundData.relation.sistaSjukskrivningsgrad);
                }
            }

            setup();
            $scope.$on('intyg.loaded', function() {
                setup();
            });

            $scope.$watch('model.' + $scope.config.modelProp, function(newValue, oldValue) {
                updatePeriods();
            }, true);
        }
    };

}]);
