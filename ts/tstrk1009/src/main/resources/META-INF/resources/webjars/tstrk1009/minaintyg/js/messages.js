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
angular.module('tstrk1009').constant('tstrk1009.messages', {
    'sv': {
        // Labels

        'tstrk1009.label.pageingress': 'Här visas hela ditt intyg. Från den här sidan kan du ladda ned intyget som PDF och arkivera intyget. För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat. Intyget har redan skickats till Transportstyrelsen via vården.',
        'tstrk1009.label.pageingress.ersatt': 'Här visas hela ditt ersatta läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev ersatt med. Klicka på knappen Arkivera intyg för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',
        'tstrk1009.label.pageingress.kompletterat': 'Här visas hela ditt kompletterade läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev kompletterat med. Klicka på knappen Arkivera intyg för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',

        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.
        'tstrk1009.compact-header.complementaryinfo-prefix': 'Avser behörighet:',
        'tstrk1009.inbox.complementaryinfo': 'Avser behörighet',

        'tstrk1009.label.syn.binokulart': 'Binokulärt',
        'tstrk1009.label.syn.hogeroga': 'Höger öga',
        'tstrk1009.label.syn.vansteroga': 'Vänster öga',
        'tstrk1009.label.syn.utankorrektion': 'Utan korrektion',
        'tstrk1009.label.syn.medkorrektion': 'Med korrektion',
        'tstrk1009.label.syn.kontaktlins': 'Kontaktlinser',
        'tstrk1009.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',

        'tstrk1009.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'tstrk1009.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',

        'tstrk1009.label.true': 'JA',
        'tstrk1009.label.false': 'NEJ'

    },
    'en': {
        'tstrk1009.label.pagetitle': 'Show Certificate'
    }
});
