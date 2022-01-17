/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').controller(
    'common.ViewCertCtrl',
    [ '$location', '$log', '$stateParams', '$scope', 'common.IntygService', 'viewConfigFactory', 'viewFactory',
        function($location, $log, $stateParams, $scope, certificateService, viewConfigFactory, viewFactory) {
            'use strict';
            $scope.certificateId = $stateParams.certificateId;
            $scope.cert = undefined;

            $scope.intygsTyp = viewFactory.intygsTyp;
            $scope.enableSend = false;
            $scope.selectRecipientKey = viewFactory.selectRecipientKey;
            $scope.activeMajorVersionSuffix = '.active.major.version';

            $scope.possibleToCustomize = function() {
                return angular.isFunction(viewFactory.enableCustomizeCertificate);
            };

            $scope.enableCustomize = function() {
                return angular.isFunction(viewFactory.enableCustomizeCertificate) && viewFactory.enableCustomizeCertificate($scope.cert);
            };

            $scope.send = function() {
                $location.path(viewFactory.getSendUrl());
            };

            $scope.customizeCertificate = function() {
                viewFactory.customizeCertificate();
            };

            $scope.errorMessage = null;
            $scope.doneLoading = false;
            certificateService.getCertificate(viewFactory.intygsTyp, $stateParams.intygTypeVersion, $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.certMeta = result.meta;
                    $scope.enableSend = !!viewFactory.getSendUrl() && $scope.certMeta.majorVersionActive;
                    $scope.activeMajorVersionSuffix = $scope.certMeta.majorVersionActive ?
                        '.active.major.version' : '.inactive.major.version';
                    $scope.errorMessage = null;
                } else {
                    $scope.errorMessage = 'error.certnotfound';
                }
            }, function(errorMsgKey) {
                $scope.doneLoading = true;
                $log.debug('getCertificate got error ' + errorMsgKey);
                $scope.errorMessage = errorMsgKey;
            });

            $scope.pagefocus = true;
            $scope.uvConfig = viewConfigFactory.getViewConfig();
        } ]);
