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
angular.module('common').factory('common.UtkastValidationService',
    ['$log', '$rootScope', 'common.UtkastValidationViewState', 'common.UtkastValidationProxy', 'common.UtkastViewStateService',
        function($log, $rootScope, ValidationViewState, UtkastValidationProxy, CommonViewState) {
            'use strict';

            function _validate(utkastModel) {
                UtkastValidationProxy.validateUtkast(
                    utkastModel.id,
                    CommonViewState.intyg.type,
                    utkastModel.toSendModel(),
                    _processResult,
                    function() {
                        $log.error('Validation call failed');
                    });
            }

            var rawMessages = [];

            function _processResult(data) {

                // Warn messages
                ValidationViewState.warningMessages = typeof data.warnings !== 'undefined' ? data.warnings : [];
                ValidationViewState.warningMessagesByField = {};

                // Process warning messages. We want to show these regardless if the draft is complete/valid or not.
                angular.forEach(ValidationViewState.warningMessages, function (message) {
                    var field = message.field.toLowerCase();
                    if (!ValidationViewState.warningMessagesByField[field]) {
                        ValidationViewState.warningMessagesByField[field] = [];
                    }
                    ValidationViewState.warningMessagesByField[field].push(message);
                });

                rawMessages = data.messages;

                _filterValidationMessages();
            }

            function _filterValidationMessages() {

                ValidationViewState.messagesGrouped = {};
                ValidationViewState.messages = [];
                ValidationViewState.sections = [];
                ValidationViewState.messagesByField = {};

                if (!CommonViewState.showComplete) {
                    ValidationViewState.messages = rawMessages.filter(function(message) {
                        return (message.type !== 'EMPTY');
                    });
                }
                else {
                    ValidationViewState.messages = rawMessages;
                }

                // Iterate over and process validation errors
                angular.forEach(ValidationViewState.messages, function(message) {

                    var section = message.category.toLowerCase();
                    var field = message.field.toLowerCase();

                    if (ValidationViewState.sections.indexOf(section) === -1) {
                        ValidationViewState.sections.push(section);
                    }

                    if (ValidationViewState.messagesGrouped[section]) {
                        ValidationViewState.messagesGrouped[section].push(message);
                    } else {
                        ValidationViewState.messagesGrouped[section] = [message];
                    }

                    if (!ValidationViewState.messagesByField[field]) {
                        ValidationViewState.messagesByField[field] = [];
                    }
                    ValidationViewState.messagesByField[field].push(message);
                });
                $rootScope.$emit('validation.content-updated');
            }

            return {
                validate: _validate,
                filterValidationMessages: _filterValidationMessages
            };
        }
    ]);