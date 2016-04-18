/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
        function ArendeNewModel() {

            // Topics are defined under RE-13
            this.topics = [
                {
                    label: 'Välj ämne',
                    value: null
                },
                {
                    label: 'Arbetstidsförläggning',
                    value: 'ARBETSTIDSFORLAGGNING'
                },
                {
                    label: 'Avstämningsmöte',
                    value: 'AVSTAMNINGSMOTE'
                },
                {
                    label: 'Kontakt',
                    value: 'KONTAKT'
                },
                {
                    label: 'Övrigt',
                    value: 'OVRIGT'
                }
            ];
            this.chosenTopic = this.topics[0]; // 'Välj ämne' is default
            this.frageText = '';
        }

        ArendeNewModel.build = function(data) {
            return new ArendeNewModel(data);
        };

        return ArendeNewModel;
    }
]);
