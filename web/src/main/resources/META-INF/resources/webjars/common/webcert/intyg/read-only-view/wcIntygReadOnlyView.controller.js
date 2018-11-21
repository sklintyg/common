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
angular
        .module('common')
        .controller(
                'common.wcIntygReadOnlyViewController',
                [
                        '$stateParams',
                        '$scope',
                        'common.IntygProxy',
                        'common.moduleService',
                        'common.dynamicLabelService',
                        'common.authorityService',
                        'ViewConfigFactory',
                        'intygsType',
                        'DiagnosExtractor',
                        function($stateParams, $scope, IntygProxy, moduleService, DynamicLabelService, authorityService, viewConfigFactory, intygsType, DiagnosExtractor) {
                            'use strict';

                            // setup scope state object...
                            $scope.viewState = {
                                certName: moduleService.getModuleName(intygsType),
                                cert: undefined,
                                uvConfig: viewConfigFactory.getViewConfig(true),
                                doneLoading: false,
                                errorKey: '',
                                srs: {
                                    showSrs: false,
                                    code: null
                                }
                            };

                            //..and load intyg data directly
                            IntygProxy.getIntyg($stateParams.certificateId, intygsType, function(result) {
                                $scope.viewState.doneLoading = true;
                                if (result !== null && result !== '') {

                                    $scope.viewState.cert = result.contents;
                                    //For intygstypes that have dynamic texts..
                                    if ($scope.viewState.cert.textVersion) {
                                        DynamicLabelService.updateDynamicLabels(intygsType, $scope.viewState.cert.textVersion).then(function(labels) {
                                            if (angular.isDefined(labels)) {
                                                DynamicLabelService.updateTillaggsfragorToModel(labels.tillaggsfragor, $scope.viewState.cert);
                                            }
                                        });
                                    }
                                    //Show srs also?
                                    if (authorityService.isAuthorityActive({
                                        feature: 'SRS',
                                        intygstyp: intygsType
                                    })) {
                                        $scope.viewState.srs.showSrs = true;
                                        $scope.viewState.srs.code = DiagnosExtractor.call(null, $scope.viewState.cert);
                                    }

                                } else {
                                    $scope.viewState.errorKey = 'common.error.intyg.read-only.failed.load';
                                }
                            }, function(error) {
                                $scope.viewState.doneLoading = true;
                                $scope.viewState.errorKey = 'common.error.intyg.read-only.failed.load';
                            });

                        } ]);
