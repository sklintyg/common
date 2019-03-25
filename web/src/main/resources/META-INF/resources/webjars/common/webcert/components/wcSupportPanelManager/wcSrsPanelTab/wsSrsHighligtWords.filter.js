/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of statistik (https://github.com/sklintyg/statistik).
 *
 * statistik is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * statistik is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* This filter will tag words (or phrases) that will later be marked by the highlightWords directive.*/
angular.module('common').filter('common.wcSrsHighlightWords',
    /** @ngInject */
    function() {
        'use strict';

        var phrasesToHighlight = {
            'sjukfall': 'Ett sjukfall omfattar en patients alla elektroniska läkarintyg som följer på varandra med max fem dagars uppehåll. Intygen måste även vara utfärdade av samma vårdgivare. Om det är mer än fem dagar mellan två intyg eller om två intyg är utfärdade av olika vårdgivare räknas det som två sjukfall.',
            'meddelanden': 'Meddelanden som skickats elektroniskt från Försäkringskassan till hälso- och sjukvården. Ett meddelande rör alltid ett visst elektroniskt intyg som utfärdats av hälso- och sjukvården och som skickats till Försäkringskassan.',
            'utfärdade intyg': 'Elektroniska intyg som har utfärdats och signerats av hälso- och sjukvården.',
            'Okänd befattning': 'Innehåller sjukfall där läkaren inte går att slå upp i HSA-katalogen eller där läkaren inte har någon befattning angiven.',
            'Ej läkarbefattning': 'Innehåller sjukfall där läkaren inte har någon läkarbefattning angiven i HSA men däremot andra slags befattningar.',
            'Utan giltig ICD-10 kod': 'Innehåller sjukfall som inte har någon diagnoskod angiven eller där den angivna diagnoskoden inte finns i klassificeringssystemet för diagnoser, ICD-10-SE.',
            'Okänt län': 'Innehåller de sjukfall där enheten som utfärdat intygen inte har något län angivet i HSA-katalogen.'
        };
        var PHRASES_TO_HIGHLIGHT = phrasesToHighlight;

        return function(text) {
            var highlightedText = text;

            for (var key in PHRASES_TO_HIGHLIGHT) {
                if (PHRASES_TO_HIGHLIGHT.hasOwnProperty(key)) {
                    highlightedText = highlightedText.replace(new RegExp('(' + key + ')', 'gi'), '<span class="highlight-this-content">$1</span>');
                }
            }

            return highlightedText;
        };

    });
