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

angular.module('common').factory('ueUtil', ['$parse', '$timeout', 'common.AtticHelper', 'common.UtkastValidationService',
    'common.UtkastValidationViewState',
    function($parse, $timeout, AtticHelper, UtkastValidationService, UtkastValidationViewState) {
        'use strict';

        return {
            updateValidation: function _updateValidation(form, model) {
                form.$commitViewValue();
                // $timeout is needed to allow for the attic functionality to clear the model value for hidden fields
                $timeout(function() {
                    UtkastValidationService.validate(model);
                });
            },

            setupWatchers: function _setupWatchers(scope, config) {

                function setupWatcher(watcher) {
                    if(angular.isArray(watcher.expression)) {
                        scope.$watchGroup(watcher.expression, watcher.listener, watcher.watchDeep);
                    } else {
                        scope.$watch(watcher.expression, watcher.listener, watcher.watchDeep);
                    }
                }

                if (config.watcher) {
                    if (angular.isArray(config.watcher)) {
                        config.watcher.forEach(setupWatcher);
                    }
                    else {
                        setupWatcher(config.watcher);
                    }
                }
            },

            standardSetup: function _standardSetup(scope) {
                // Expose validation status on scope
                scope.validation = UtkastValidationViewState;

                // Expose function to update validation status
                scope.updateValidation = angular.bind(this, this.updateValidation, scope.form, scope.model);

                // Setup watchers from utkastConfig
                this.setupWatchers(scope, scope.config);

                // This is a ngModel getter/setter function
                // needed for more complex modelProp like tillaggsfragor[0].svar
                var getterSetter = $parse(scope.config.modelProp);
                scope.modelGetterSetter = function(newValue) {
                    return arguments.length ? (getterSetter.assign(scope.model, newValue)) : getterSetter(scope.model);
                };

                // The conditional skipAttic flag originates from the fact that the attic framework
                // doesn't fullt handle array indexed modelProps, but the actual array property level itself.
                // (see base.model.js:298). If / when the attic handling is refactored we can skip this flag.
                if (!scope.config.skipAttic) {
                    // Restore data model value form attic if exists
                    AtticHelper.restoreFromAttic(scope.model, scope.config.modelProp);

                    // Clear attic model and destroy watch on scope destroy
                    AtticHelper.updateToAttic(scope, scope.model, scope.config.modelProp);
                }

                // One time bindings will stop watching when the result is no longer undefined.
                if (!scope.config.label) {
                    // To stop the watch we need to set the value to something that is not undefined.
                    scope.config.label = null;
                }
                // Same as above
                if (!scope.config.placeholder) {
                    scope.config.placeholder = null;
                }
                if (!scope.config.enableHelp) {
                    scope.config.enableHelp = false;
                }
            }
        };
    } ]);
