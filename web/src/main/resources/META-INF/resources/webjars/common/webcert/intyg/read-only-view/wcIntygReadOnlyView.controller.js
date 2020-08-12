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
angular.module('common').controller(
    'common.wcIntygReadOnlyViewController',
    ['$stateParams', '$scope', 'common.IntygProxy', 'common.moduleService', 'common.dynamicLabelService',
        'common.authorityService', 'ViewConfigFactory', 'ViewState', 'DiagnosExtractor',
        function($stateParams, $scope, IntygProxy, moduleService, DynamicLabelService,
            authorityService, viewConfigFactory, ViewState, DiagnosExtractor) {
            'use strict';

            ViewState.reset();
            $scope.viewState = ViewState;

            $scope.config = {
                uvConfig: viewConfigFactory.getViewConfig(true),
                certName: moduleService.getModuleName(ViewState.common.intygProperties.type),
                errorKey: ''
            };

            //..and load intyg data directly
            IntygProxy.getIntyg($stateParams.certificateId, ViewState.common.intygProperties.type, function(result) {
                ViewState.doneLoading = true;
                if (result !== null && result !== '') {

                    ViewState.cert = result.contents;
                    //For intygstypes that have dynamic texts..
                    if (ViewState.cert.textVersion) {
                        DynamicLabelService.updateDynamicLabels(ViewState.common.intygProperties.type, ViewState.cert.textVersion).then(
                            function(labels) {
                                if (angular.isDefined(labels)) {
                                    DynamicLabelService.updateTillaggsfragorToModel(labels.tillaggsfragor, ViewState.cert);
                                }
                            });
                    }

                    if(ViewState.intygModel) {
                      ViewState.common.updateIntygProperties(result, ViewState.intygModel.id);
                    } else {
                      ViewState.common.updateIntygProperties(result, ViewState.cert.id);
                    }


                    // Construct panel config
                    var panelConfig = {
                        disableMinimizeMode: true,
                        tabs: [],
                        intygContext: {
                            type: ViewState.common.intygProperties.type,
                            id: $stateParams.certificateId,
                            intygTypeVersion: $scope.viewState.cert.textVersion
                        }
                    };
                    //Kompletteringpanel is always enabled
                    panelConfig.tabs.push({
                        id: 'wc-komplettering-read-only-panel-tab',
                        title: 'common.supportpanel.ro-kompletteringar.title',
                        tooltip: 'common.supportpanel.ro-kompletteringar.tooltip',
                        config: {
                            intygContext: panelConfig.intygContext
                        },
                        active: true
                    });
                    // SRS read only view
                    if (authorityService.isAuthorityActive(
                        {feature: 'SRS', intygstyp: ViewState.common.intygProperties.type})) {
                        panelConfig.tabs.push({
                            id: 'wc-srs-panel-tab',
                            title: 'common.supportpanel.srs.title',
                            icon: 'lightbulb_outline',
                            tooltip: 'common.supportpanel.srs.tooltip',
                            config: {
                                intygContext: panelConfig.intygContext,
                                isReadOnly: true,
                                readOnlyIntyg: ViewState.cert,
                                diagnoseCode: DiagnosExtractor.call(null, ViewState.cert)
                            },
                            active: false
                        });
                    }
                    $scope.supportPanelConfig = panelConfig;


                } else {
                    ViewState.errorKey = 'common.error.could_not_load_cert';
                }
            }, function(error) {
                ViewState.doneLoading = true;
                ViewState.errorKey = 'common.error.could_not_load_cert';
            });

        }]);
