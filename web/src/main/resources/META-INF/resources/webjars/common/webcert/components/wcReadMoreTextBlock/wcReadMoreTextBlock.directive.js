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
angular.module('common').directive('wcReadMoreTextBlock',
        [ 'common.messageService', function(messageService) {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    text: '=',
                    textKey: '@',
                    maxChars: '@'
                },
                templateUrl: '/web/webjars/common/webcert/components/wcReadMoreTextBlock/wcReadMoreTextBlock.directive.html',
                link: function($scope) {

                    $scope.vm = {
                        showControls: false,
                        showsAll: false,
                        text: ''
                    };

                    var _updateEffectiveText = function() {

                        var _maxLength = parseInt($scope.maxChars, 10);
                        var _originalText = $scope.text;

                        if ($scope.textKey) {
                            _originalText = messageService.getProperty(angular.lowercase($scope.textKey));
                        }

                        if ((_originalText.length > _maxLength) && !$scope.vm.showsAll) {
                            $scope.vm.text = _originalText.substring(0, _maxLength) + '...';
                        } else {
                            $scope.vm.text = _originalText;
                        }

                        $scope.vm.showControls = _originalText.length > _maxLength;


                    };

                    $scope.toggle = function() {
                        $scope.vm.showsAll = !$scope.vm.showsAll;
                        _updateEffectiveText();
                    };

                    _updateEffectiveText();

                }
            };
        } ]);
