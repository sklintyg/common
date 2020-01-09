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
/* jshint maxlen: false */
angular.module('ts-diabetes').constant('ts-diabetes.messages', {
    'sv': {
        // Labels

        'ts-diabetes.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt, ladda ned intyget som PDF och arkivera intyget. För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',
        'ts-diabetes.label.pageingress.ersatt': 'Här visas hela ditt ersatta läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev ersatt med. Klicka på knappen Arkivera intyg för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',
        'ts-diabetes.label.pageingress.kompletterat': 'Här visas hela ditt kompletterade läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev kompletterat med. Klicka på knappen Arkivera intyg för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',

        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.
        'ts-diabetes.compact-header.complementaryinfo-prefix': 'Avser behörighet:',
        'ts-diabetes.inbox.complementaryinfo': 'Avser behörighet',

        'ts-diabetes.label.syn.binokulart': 'Binokulärt',
        'ts-diabetes.label.syn.hogeroga': 'Höger öga',
        'ts-diabetes.label.syn.vansteroga': 'Vänster öga',
        'ts-diabetes.label.syn.utankorrektion': 'Utan korrektion',
        'ts-diabetes.label.syn.medkorrektion': 'Med korrektion',
        'ts-diabetes.label.syn.kontaktlins': 'Kontaktlinser',
        'ts-diabetes.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',

        'ts-diabetes.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'ts-diabetes.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',

        'ts-diabetes.label.true': 'JA',
        'ts-diabetes.label.false': 'NEJ'
    },
    'en': {
        'ts-diabetes.label.pagetitle': 'Show Certificate'
    }
});
