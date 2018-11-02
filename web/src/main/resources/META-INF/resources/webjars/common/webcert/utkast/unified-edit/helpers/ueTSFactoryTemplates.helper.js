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
angular.module('common').factory('common.ueTSFactoryTemplatesHelper', [
    '$log', 'common.ObjectHelper', 'common.UserModel', 'common.ueFactoryTemplatesHelper', 'common.UtilsService',
    function($log, ObjectHelper, UserModel, ueFactoryTemplates, UtilsService) {
        'use strict';

        function _patient(viewState) {
            function _shouldDisableAddressInputWhen(model) {
                return UserModel.isDjupintegration() && viewState.common.validPatientAddressAquiredFromPU;
            }
            return ueFactoryTemplates.patient(_shouldDisableAddressInputWhen, true, true);
        }

        function _getBedomningListenerConfig(modelProp, kanInteTaStallningType) {
            return [{
                type: '$watch',
                watchDeep: true,
                expression: 'model.bedomning.' + modelProp,
                /*jshint maxcomplexity:11*/
                listener: function(newValue, oldValue, scope) {

                    if (oldValue && newValue !== oldValue) {

                        var kanInteTaStallningIndex = UtilsService.findIndexWithPropertyValue(newValue, 'type', kanInteTaStallningType);
                        if (oldValue[kanInteTaStallningIndex].selected === newValue[kanInteTaStallningIndex].selected) {
                            return;
                        }

                        var kanInteTaStallningSelected = newValue[kanInteTaStallningIndex].selected;
                        for(var i = 0; i < scope.model.bedomning[modelProp].length; i++) {
                            if(kanInteTaStallningIndex === i) {
                                continue;
                            }
                            scope.model.bedomning[modelProp][i].disabled = kanInteTaStallningSelected;
                            if(kanInteTaStallningSelected) {
                                scope.model.bedomning[modelProp][i].selected = false;
                            }
                        }
                    }
                }
            }];
        }


        return {
            patient: _patient,
            getBedomningListenerConfig: _getBedomningListenerConfig
        };
    }]);
