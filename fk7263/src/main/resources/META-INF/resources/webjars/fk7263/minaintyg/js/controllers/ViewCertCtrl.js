/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

angular.module('fk7263').controller('fk7263.ViewCertCtrl',
    [ '$location', '$log', '$stateParams', '$scope', 'common.IntygService', 'fk7263.viewConfigFactory', 'fk7263.customizeViewstate',
        function($location, $log, $stateParams, $scope, certificateService, viewConfigFactory, customizeViewstate) {
            'use strict';

            $scope.cert = undefined;

            $scope.send = function() {
                $location.path('/send/fk7263/' + $stateParams.certificateId + '/FKASSA');
            };

            $scope.customizeCertificate = function() {
                customizeViewstate.resetModel();
                $location.path('/fk7263/customizepdf/' + $stateParams.certificateId + '/step1');
            };

            $scope.errorMessage = null;
            $scope.doneLoading = false;
            certificateService.getCertificate('fk7263', $stateParams.certificateId, function(result) {
                $scope.doneLoading = true;
                if (result !== null) {
                    $scope.cert = result.utlatande;
                    $scope.certMeta = result.meta;
                    $scope.errorMessage = null;
                } else {
                    // show error view
                    $scope.errorMessage = 'error.certnotfound';
                }
            }, function(error) {
                $scope.doneLoading = true;
                $log.debug('getCertificate got error' + error);
                $scope.errorMessage = 'error.certnotfound';
            });

            $scope.pagefocus = true;
            $scope.uvConfig = viewConfigFactory.getViewConfig();
        }]);
