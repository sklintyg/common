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
    [ '$log', '$rootScope', 'common.DynamicLabelProxy',
        function($log, $rootScope, DynamicLabelProxy) {
            'use strict';

            var _labelResources = null;
            var structureTypesEnum = { kategori: 'KAT', fraga: 'FRG', delFraga: 'DFR' };
            var structureTextTypesEnum = { rubrik: 'RBK', hjalp: 'HLP' };
            var tillaggsFragor = null;

            function _getProperty(key) {
                var value = getRequiredTextByPropKey(key); // get required text
                return value;
            }

            // get prop req
            function getRequiredTextByPropKey(key) {
                if (_labelResources === null) {
                    return '';
                }

                var textFound = true;
                var text = _labelResources[key];
                if (typeof text === 'undefined') {
                    $log.debug('[MISSING TEXT ERROR - missing required id: "' + key + '"]');
                    textFound = false;
                } else if (text === '') {
                    $log.debug('[MISSING TEXT ERROR - have ID but not text for required id: "' + key + '"]');
                    textFound = false;
                }

                if(!textFound && typeof tillaggsFragor !== 'undefined')
                {
                    // Check if its a tillaggsfraga
                    for (var i = 0; i < tillaggsFragor.length; i++) {
                        if (tillaggsFragor[i].id === _convertTextIdToModelId(key)) {
                            text = tillaggsFragor[i].text;
                            break;
                        }
                    }
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

            function _convertTextIdToModelId(textObject) {
                if(typeof textObject !== 'undefined' && textObject !== null) {
                    return textObject.substr(4, 4);
                } else {
                    $log.debug('invalid textobject:' + textObject);
                }

                return '';
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
                updateDynamicLabels: _updateDynamicLabels
            };
        }
    ]);
