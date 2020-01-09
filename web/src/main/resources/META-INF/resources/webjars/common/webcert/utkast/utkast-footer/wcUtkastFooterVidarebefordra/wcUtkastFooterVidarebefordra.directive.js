/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

angular
    .module('common')
    .directive('wcUtkastFooterVidarebefordra',
        ['common.dynamicLabelService', 'common.UtkastNotifyService', 'common.UtkastViewStateService', 'common.UtkastFooterService', '$rootScope', '$timeout',
            function(dynamicLabelService, UtkastNotifyService, CommonViewState, UtkastFooterService, $rootScope, $timeout) {
                'use strict';

                return {
                    restrict: 'E',
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/wcUtkastFooterVidarebefordra/wcUtkastFooterVidarebefordra.directive.html',
                    scope: {
                        viewState: '=',
                        certForm: '<'
                    },
                    controller: function($scope) {
                        var viewState = $scope.viewState;

                        $scope.forwardBtnText = dynamicLabelService.getProperty('draft.notify.button');
                        $scope.forwardBtnTooltip = dynamicLabelService.getProperty('draft.notify.button.tooltip');
                        $scope.checkMissingLabel = dynamicLabelService.getProperty('draft.notify.check-missing');

                        /**
                         * Handle vidarebefordra dialog
                         */
                        $scope.vidarebefordraUtkast = function() {
                            UtkastNotifyService.notifyUtkast(viewState.intygModel.id, viewState.common.intyg.type,
                                viewState.draftModel, viewState.common);
                        };

                        $scope.onVidarebefordradChange = function() {
                            UtkastNotifyService.onNotifyChange(viewState.intygModel.id, viewState.common.intyg.type,
                                viewState.draftModel, viewState.common);
                        };

                        $scope.showMissing = function(value) {
                            UtkastFooterService.toggleMissing(value, viewState, $scope.certForm);

                            $timeout(function() {
                                $rootScope.$emit('validation.content-updated');
                            }, 200);

                        };
                    }
                };
            } ]);
