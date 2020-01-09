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
/**
 * message directive for externalizing text resources.
 *
 * All resourcekeys are expected to be defined in lowercase and available in a
 * global js object named "messages"
 * Also supports dynamic key values such as key="status.{{scopedvalue}}"
 *
 * Usage: <dynamicLabel key="some.resource.key" [fallback="defaulttextifnokeyfound"]/>
 */
angular.module('common').factory('common.dynamicLabelService',
    [ '$log', '$rootScope', 'common.DynamicLabelProxy', 'common.messageService',
        function($log, $rootScope, DynamicLabelProxy, messageService) {
            'use strict';

            var _labelResources = null;
            var tillaggsFragor = null;

            function _hasProperty(key) {
                var text = _labelResources[key];
                if (typeof text === 'undefined' || text === '') {
                    return false;
                }
                return true;
            }

            function _getProperty(key) {
                var value = getRequiredTextByPropKey(key); // get required text
                return value;
            }

            // get prop req
            function getRequiredTextByPropKey(key) {
                if (_labelResources === null) {
                    return '';
                }

                var text = _labelResources[key];

                if(angular.isDefined(text) && text !== ''){
                    return text;
                } else {
                    if(messageService.propertyExists(key)){
                        var staticLabel = messageService.getProperty(key);
                        return staticLabel;
                    }
                }

                if (typeof text === 'undefined') {
                    $log.debug('[MISSING TEXT ERROR - missing required id: "' + key + '"]');
                } else if (text === '') {
                    $log.debug('[MISSING TEXT ERROR - have ID but not text for required id: "' + key + '"]');
                }

                return text;
            }

            function _clearLabels() {
                _labelResources = {};
            }

            function _addLabels(labels) {
                _checkLabels();
                angular.extend(_labelResources, labels.texter);

                tillaggsFragor = labels.tillaggsfragor;
            }

            function _getTillaggsFragor() {
                return tillaggsFragor;
            }

            function _updateTillaggsfragorToModel(tillaggsfragor, model) {
                var modelFrageList = model.tillaggsfragor;

                if (!modelFrageList) {
                    return;
                }

                for (var i = 0; i < tillaggsfragor.length; i++) {
                    var tillaggsfraga = {
                        'id': tillaggsfragor[i].id,
                        'svar': ''
                    };
                    if (i >= modelFrageList.length) {
                        modelFrageList.push(tillaggsfraga);
                    } else {
                        if (modelFrageList[i].id !== tillaggsfraga.id) {
                            modelFrageList.splice(i, 0, tillaggsfraga);
                        }
                    }
                }

                var textHasFraga = function(id) {
                    for (var i = 0; i < tillaggsfragor.length; i++) {
                        if (tillaggsfragor[i].id === id) {
                            return true;
                        }
                    }
                    return false;
                };
                for(var j = 0; j < modelFrageList.length; j++) {
                    if (!textHasFraga(modelFrageList[j].id)) {
                        modelFrageList.splice(j, 1);
                        j--;
                    }
                }
            }

            function _checkLabels() {
                if (_labelResources === null) { // extend to local storage here
                    _labelResources = {};
                }
            }

            function _updateDynamicLabels(intygsTyp, model) {
                DynamicLabelProxy.getDynamicLabels(intygsTyp, model.textVersion).then(
                    function(dynamicLabelJson) {
                        if (dynamicLabelJson !== null && typeof dynamicLabelJson !== 'undefined') {
                            $log.debug(dynamicLabelJson);
                            _clearLabels();
                            _addLabels(dynamicLabelJson);
                            _updateTillaggsfragorToModel(dynamicLabelJson.tillaggsfragor, model);
                        } else {
                            $log.debug('No dynamic text for intygType: ' + intygsTyp);
                        }
                        $rootScope.$broadcast('dynamicLabels.updated');
                    },
                    function(error) {
                        $log.debug('error:' + error);
                    });
            }
            return {
                checkLabels: _checkLabels,
                getProperty: _getProperty,
                getTillaggsFragor: _getTillaggsFragor,
                hasProperty: _hasProperty,
                updateDynamicLabels: _updateDynamicLabels
            };
        }
    ]);
