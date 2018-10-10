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
angular.module('lisjp').controller(
        'lisjp.ViewCertCtrl',
        [ '$location', '$log', '$stateParams', '$scope', 'common.IntygService', 'lisjp.customizeViewstate', 'viewConfigFactory',
                function($location, $log, $stateParams, $scope, certificateService, customizeViewstate, viewConfigFactory) {
                    'use strict';
                    $scope.certificateId = $stateParams.certificateId;
                    $scope.cert = undefined;

                    $scope.send = function() {
                        $location.path('/send/lisjp/' + $stateParams.intygTypeVersion + '/' + $stateParams.certificateId + '/FKASSA');
                    };

                    $scope.customizeCertificate = function() {
                        customizeViewstate.resetModel();
                        $location.path('/lisjp/' + $stateParams.intygTypeVersion + '/customize/' + $stateParams.certificateId + '/step1');
                    };

                    $scope.errorMessage = null;
                    $scope.doneLoading = false;
                    certificateService.getCertificate('lisjp', $stateParams.intygTypeVersion, $stateParams.certificateId, function(result) {
                        $scope.doneLoading = true;
                        if (result !== null) {
                            $scope.cert = result.utlatande;
                            $scope.certMeta = result.meta;
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
