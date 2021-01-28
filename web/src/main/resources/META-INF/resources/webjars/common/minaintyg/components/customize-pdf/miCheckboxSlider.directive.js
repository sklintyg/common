/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('common').directive('miCheckboxSlider', ['common.dialogService', function(dialogService) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            model: '=',
            showConfirm: '=',
            id: '@',
            checkedText: '@',
            uncheckedText: '@'
        },
        templateUrl: '/web/webjars/common/minaintyg/components/customize-pdf/miCheckboxSlider.directive.html',
        controller: function($scope) {
            var dialogInstance;
            // Cleanup ---------------
            $scope.$on('$destroy', function() {
                if (dialogInstance) {
                    dialogInstance.close();
                    dialogInstance = undefined;
                }
            });

            $scope.onToggleRequested = function() {

                if ($scope.showConfirm && $scope.model) {
                    dialogInstance = dialogService.showDialog( $scope, {
                        dialogId: 'confirm-field-deselection-dialog',
                        titleId: 'modules.customize.remove-field.header',
                        bodyTextId: 'modules.customize.remove-field.body',
                        button1click: function() {
                            //perform toggle
                            $scope.model = !$scope.model;
                            dialogInstance.close();
                        },
                        button1id: 'ok-to-deselect-button',
                        button1text: 'modules.customize.remove-field.button.confirm',
                        button1icon: 'icon-ok',
                        button2text: 'modules.customize.remove-field.button.cancel',
                        button2icon: 'icon-cancel',
                        button2class: 'btn-secondary',
                        autoClose: false
                    });

                } else {
                    $scope.model = !$scope.model;
                }
            };
        }

    };
}]);
