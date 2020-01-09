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
 * Created by BESA on 2015-02-09.
 */

angular.module('common').factory('common.ArendeNewModel',
    [function() {
        'use strict';

        /**
         * Constructor
         */
        function ArendeNewModel(data) {

            // Topics are defined under RE-13
            this.topics = [
                {
                    label: 'Välj typ av fråga',
                    id: null
                }
            ];

            if (data === 'fk7263') {
                this.topics.push({
                    label: 'Arbetstidsförläggning',
                    id: 'ARBETSTIDSFORLAGGNING'
                });
            }

            this.topics.push(
                {
                    label: 'Avstämningsmöte',
                    id: 'AVSTMN'
                },
                {
                    label: 'Kontakt',
                    id: 'KONTKT'
                },
                {
                    label: 'Övrigt',
                    id: 'OVRIGT'
                }
            );

            this.saveState = 'normal';

            var model = this;
            this.reset = function() {
                model.chosenTopic = this.topics[0].id; // 'Välj ämne' is default
                model.frageText = '';
                model.saveState = 'normal';
            };
            this.reset();
        }

        ArendeNewModel.build = function(data) {
            return new ArendeNewModel(data);
        };

        return ArendeNewModel;
    }
]);
