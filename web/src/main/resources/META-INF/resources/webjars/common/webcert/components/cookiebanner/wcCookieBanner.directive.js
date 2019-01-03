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
angular.module('common').directive('wcCookieBanner',

    ['$window', 'common.dialogService', function($window, dialogService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {},
            templateUrl: '/web/webjars/common/webcert/components/cookiebanner/wcCookieBanner.directive.html',
            controller: function($scope, $timeout) {
                $scope.isOpen = false;
                $scope.showDetails = false;

                var dialogInstance;

                $scope.showModal = function() {
                    dialogInstance = dialogService.showDialog({
                        dialogId: 'cookie-banner-modal',
                        templateUrl: '/web/webjars/common/webcert/components/cookiebanner/wcCookieBanner.modal.html',
                        button1click: function() {
                            dialogInstance.close();
                        },
                        button2click: function() {
                            dialogInstance.close();
                            $scope.onCookieConsentClick();
                        },
                        autoClose: false,
                        size: 'lg'
                    });
                };

                function cookieConsentGiven() {
                    return $window.localStorage && $window.localStorage.getItem('wc-cookie-consent-given') === '1';
                }

                $timeout(function() {
                    if (!cookieConsentGiven()) {
                        $scope.isOpen = true;
                    }
                }, 500);


                $scope.onCookieConsentClick = function() {
                    $scope.isOpen = false;
                    if ($window.localStorage) {
                        $window.localStorage.setItem('wc-cookie-consent-given', '1');
                    }

                };
            }
        };
    }]);
