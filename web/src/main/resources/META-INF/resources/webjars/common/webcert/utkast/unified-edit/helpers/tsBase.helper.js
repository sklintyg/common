/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.tsBaseHelper', [
    '$log', 'common.ObjectHelper', 'common.UserModel', 'common.ueFactoryTemplatesHelper', 'common.UtilsService',
    function($log, ObjectHelper, UserModel, ueFactoryTemplates, UtilsService) {
        'use strict';

        function _setupKorkortstypChoices(choices, exclusiveChoicesArray) {

            function processExclusiveChoice(choices, choiceType) {
                var somethingElseSelected = false,
                index = UtilsService.findIndexWithPropertyValue(choices, 'type', choiceType),
                choiceSelected = (index > -1 && choices[index].selected);

                if(!choiceSelected) {
                    // check if something else is selected
                    somethingElseSelected = choices.filter(function(choice) {
                        return choice.type !== choice && choice.selected;
                    }).length > 0;
                }

                return choices.map(function(choice) {
                    if ((choiceSelected && choice.type !== choiceType) || (somethingElseSelected && choice.type === choiceType)) {
                        choice.disabled = true;
                        choice.selected = false;
                    }
                    return choice;
                });
            }

            exclusiveChoicesArray.forEach(function (choice) {
                choices = processExclusiveChoice(choices, choice);
            });

            return choices;
        }

        return {
            setupKorkortstypChoices: _setupKorkortstypChoices
        };
    }]);
