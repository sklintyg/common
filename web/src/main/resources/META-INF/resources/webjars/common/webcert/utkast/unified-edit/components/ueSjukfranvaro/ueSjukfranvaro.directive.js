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

angular.module('common').directive('ueSjukfranvaro', ['common.ArendeListViewStateService', 'common.SjukfranvaroViewStateService',
    'common.UtkastValidationService', 'common.messageService', 'common.UtkastViewStateService', 'common.AtticHelper',
    function (ArendeListViewState, viewstate, UtkastValidationService, messageService, UtkastViewState, AtticHelper) {
        'use strict';
        return {
            restrict: 'E',
            scope: {
                form: '=',
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueSjukfranvaro/ueSjukfranvaro.directive.html',
            link: function ($scope) {

                AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

                // Clear attic model and destroy watch on scope destroy
                AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

                var sjukfranvaroArray = $scope.model[$scope.config.modelProp];


                if (sjukfranvaroArray === undefined) {
                    sjukfranvaroArray = [];
                }

                if (sjukfranvaroArray.length === 0) {
                    sjukfranvaroArray.push({
                        niva: 100,
                        checked: false,
                        period: {
                            from: '',
                            tom: ''
                        }
                    });
                    sjukfranvaroArray.push({
                        niva: undefined,
                        checked: false,
                        period: {
                            from: '',
                            tom: ''
                        }
                    });
                }

                var validation = $scope.validation = UtkastViewState.validation;

                $scope.$watch('validation.messages', function () {
                    $scope.validationMessages = [];

                    if (!validation.messages) {
                        return;
                    }

                    angular.forEach(validation.messages, function (message) {
                        if (message.field.startsWith($scope.config.modelProp)) {
                            $scope.validationMessages.push(message);
                        }
                    });
                });

                $scope.hasValidationError = function (index, type) {
                    var key = $scope.config.modelProp + '[' + index + '].' + type;

                    return validation.messagesByField &&
                        validation.messagesByField[key];
                };

                $scope.hasKompletteringar = function () {
                    return ArendeListViewState.hasKompletteringar($scope.config.modelProp);
                };
                $scope.validate = function () {
                    UtkastValidationService.validate($scope.model);
                };
                $scope.viewstate = viewstate.reset();

                function setup() {

                    viewstate.setup($scope.model[$scope.config.modelProp], $scope.config.maxRows, function () {
                        $scope.form.$setDirty();
                    });

                    viewstate.updatePeriods();
                }

                setup();
                $scope.$on('intyg.loaded', function () {
                    setup();
                });

                $scope.$watch('model.' + $scope.config.modelProp, function (newValue, oldValue) {
                    viewstate.updatePeriods();
                }, true);
            }
        };

    }]);
