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

angular.module('common').directive('wcIntygField', ['$log', '$rootScope', 'common.ObjectHelper', 'common.IntygViewStateService',
    function($log, $rootScope, ObjectHelper, IntygViewStateService) {
        'use strict';

        return {
            restrict: 'AE',
            replace: true,
            scope: {
                categoryKey: '@',
                prevField: '=',
                field: '=',
                nextField: '=',
                intygModel: '='
            },
            templateUrl: '/web/webjars/common/webcert/intyg/fk/wcIntygField.directive.html',
            link: function(scope, element, attrs) {

                // Update structure holding field and category mapping
                scope.viewState = IntygViewStateService;
                scope.viewState.setCategoryField(scope.categoryKey, scope.field && (scope.field.key || (scope.field.templateOptions && scope.field.templateOptions.kompletteringGroup)));

                scope.showField = function(field){

                    if(scope.intygModel.avstangningSmittskydd)
                    {
                        switch(field.key){
                        case 'avstangningSmittskydd':
                        case 'diagnoser':
                        case 'sjukskrivningar':
                        case 'ovrigt':
                            return true;
                        }

                        return false;
                    }

                    // Edge cases
                    var edge = true;
                    switch (field.key)
                    {
                    case 'underlag':
                        if(scope.intygModel[field.key] && scope.intygModel[field.key].length === 0){
                            edge = false;
                        }
                        break;
                    }

                    return edge && field.templateOptions && !field.templateOptions.hideFromSigned && (!field.templateOptions.hideWhenEmpty || scope.intygModel[field.key]);
                };

                scope.showFieldLine = function(prevField, field, nextField) {

                    function analyzeNextField(nextField){
                        if (nextField) {
                            var exp = /DFR_[0-9]+\.([2-9]|[1-9][0-9]+)/;
                            if (exp.exec(nextField.templateOptions.label)) {
                                return false;
                            }
                            if (field.key === 'underlagFinns' && nextField.key === 'underlag' &&
                                ObjectHelper.isDefined(scope.intygModel.underlag) &&
                                scope.intygModel.underlag.length === 0) {
                                return false;
                            }
                            if(field.key === 'sysselsattning' && (!scope.intygModel.nuvarandeArbete && !scope.intygModel.arbetsmarknadspolitisktProgram)) {
                                return false;
                            }
                        }
                        else {
                            return false;
                        }

                        return true;
                    }

                    // Check that we have even loaded a intyg
                    if(!scope.intygModel.id){
                        return false;
                    }

                    //$log.log(field.key + ':' + field.templateOptions.forceNoDividerAfter);

                    var showField = scope.showField(field);

                    if(showField){

                        // No lines after these fields
                        if (field.type === 'info' || field.type === 'headline') {
                            return false;
                        }
                        if (field.templateOptions && field.templateOptions.label && field.templateOptions.label.indexOf('KV_') === 0) {
                            return false;
                        }

                        if(scope.intygModel.avstangningSmittskydd) {
                            if (field.type === 'sjukskrivningar') {
                                return false;
                            }
                        }

                        // Check overrides
                        if(field.templateOptions) {
                            if(field.templateOptions.forceDividerAfter){
                                return true;
                            } else if(field.templateOptions.forceNoDividerAfter) {
                                return false;
                            }
                        }

                        return analyzeNextField(nextField);
                    }

                    if(!scope.intygModel.avstangningSmittskydd) {
                        if (field.key === 'arbetstidsforlaggningMotivering') {
                            return true;
                        }
                    }

                    // Check overrides
                    if(field.templateOptions) {
                        if(field.templateOptions.forceDividerAfter){
                            return true;
                        } else if(field.templateOptions.forceNoDividerAfter) {
                            return false;
                        }
                    }

                    return false;
                };
            }
        };
    }
]);
