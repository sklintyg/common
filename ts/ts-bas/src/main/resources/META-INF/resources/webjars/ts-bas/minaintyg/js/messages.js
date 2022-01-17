/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('ts-bas').constant('ts-bas.messages', {
    'sv': {

        // Labels
        'ts-bas.label.pageingress.inactive.major.version': '<p>Här visas hela ditt läkarintyg. Från den här sidan kan du ladda ner intyget som PDF och arkivera intyget. För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg eller den mottagning du besökte när du fick ditt intyg utfärdat.</p><p>Du har fått ett intyg från din läkare som inte går att skicka digitalt.</p>',
        'ts-bas.label.pageingress.active.major.version': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt, ladda ned intyget som PDF och arkivera intyget. För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',
        'ts-bas.label.pageingress.ersatt': 'Här visas hela ditt ersatta läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev ersatt med. Klicka på knappen Arkivera intyg för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',
        'ts-bas.label.pageingress.kompletterat': 'Här visas hela ditt kompletterade läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev kompletterat med. Klicka på knappen Arkivera intyg för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',

        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.
        'ts-bas.compact-header.complementaryinfo-prefix': 'Avser behörighet:',
        'ts-bas.inbox.complementaryinfo': 'Avser behörighet',

        'ts-bas.label.syn.binokulart': 'Binokulärt',
        'ts-bas.label.syn.hogeroga': 'Höger öga',
        'ts-bas.label.syn.vansteroga': 'Vänster öga',
        'ts-bas.label.syn.utankorrektion': 'Utan korrektion',
        'ts-bas.label.syn.medkorrektion': 'Med korrektion',
        'ts-bas.label.syn.kontaktlins': 'Kontaktlinser',
        'ts-bas.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',

        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',

        'ts-bas.label.true': 'JA',
        'ts-bas.label.false': 'NEJ'

    },
    'en': {
        'ts-bas.label.pagetitle': 'Show Certificate'
    }
});
