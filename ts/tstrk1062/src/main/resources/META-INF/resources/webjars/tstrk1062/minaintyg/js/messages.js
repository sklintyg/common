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
/* jshint maxlen: false */
angular.module('tstrk1062').constant('tstrk1062.messages', {
    'sv': {

        // Labels
        'tstrk1062.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektronisk, ladda ned intyget som PDF och arkivera intyget. För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',
        'tstrk1062.label.pageingress.ersatt': 'Här visas hela ditt ersatta läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev ersatt med. Klicka på knappen Arkivera intyg för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',
        'tstrk1062.label.pageingress.kompletterat': 'Här visas hela ditt kompletterade läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev kompletterat med. Klicka på knappen Arkivera intyg för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',

        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.
        'tstrk1062.compact-header.complementaryinfo-prefix': 'Avser behörighet:',
        'tstrk1062.inbox.complementaryinfo': 'Avser behörighet',

        'tstrk1062.label.syn.binokulart': 'Binokulärt',
        'tstrk1062.label.syn.hogeroga': 'Höger öga',
        'tstrk1062.label.syn.vansteroga': 'Vänster öga',
        'tstrk1062.label.syn.utankorrektion': 'Utan korrektion',
        'tstrk1062.label.syn.medkorrektion': 'Med korrektion',
        'tstrk1062.label.syn.kontaktlins': 'Kontaktlinser',
        'tstrk1062.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',

        'tstrk1062.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'tstrk1062.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',

        'tstrk1062.label.true': 'JA',
        'tstrk1062.label.false': 'NEJ',

        'tstrk1062.error.generic': 'Kunde inte visa intyget',
        'tstrk1062.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'



    },
    'en': {
        'tstrk1062.label.pagetitle': 'Show Certificate'
    }
});
