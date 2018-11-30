/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
'use strict';

angular.module('ag7804').constant('ag7804.messages', {
    'sv': {
        //Validation messages
        'ag7804.validation.sysselsattning.nuvarandearbete.invalid_combination': 'Beskrivningen får inte fyllas i när inte nuvarande arbete valts.', // Should never happen because GUI should block this combination
        'ag7804.validation.sysselsattning.too-many': 'sysselsättning.toomany',  // Should never happen because GUI should block this combination
        'ag7804.validation.bedomning.fmb.empty' : 'Beskriv varför arbetsförmågan bedöms vara nedsatt längre tid än den som det försäkringsmedicinska beslutstödet anger kan inte fyllas i med endast blanksteg.',
        'ag7804.validation.bedomning.prognos.dagartillarbete.invalid_combination': 'Dagar till arbete kan bara fyllas i om prognosen "kan återgå helt i nuvarande sysselsättning efter x antal dagar valts".', // Should never happen because GUI should block this combination
        'ag7804.validation.bedomning.sjukskrivningar.missing': 'Minst en sjukskrivningsperiod måste anges.',
        'ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect': '',
        'ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination': 'Motivering till arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.', // Should never happen
        'ag7804.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing': 'Tekniskt fel. Sjukskrivningsgrad saknas.', // Should never happen because GUI should block this combination
        'ag7804.validation.bedomning.sjukskrivningar.periodhelt_nedsatt.missing': 'Helt nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'ag7804.validation.bedomning.sjukskrivningar.periodhelt_nedsatt.invalid_format': 'Helt nedsatt: Felaktigt datumformat.',
        'ag7804.validation.bedomning.sjukskrivningar.periodhelt_nedsatt.overlap': 'Helt nedsatt: Datumintervall överlappar.',
        'ag7804.validation.bedomning.sjukskrivningar.periodtre_fjardedel.missing': '75% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'ag7804.validation.bedomning.sjukskrivningar.periodtre_fjardedel.invalid_format': '75% nedsatt: Felaktigt datumformat.',
        'ag7804.validation.bedomning.sjukskrivningar.periodtre_fjardedel.overlap': '75% nedsatt: Datumintervall överlappar.',
        'ag7804.validation.bedomning.sjukskrivningar.periodhalften.missing': '50% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'ag7804.validation.bedomning.sjukskrivningar.periodhalften.invalid_format': '50% nedsatt: Felaktigt datumformat.',
        'ag7804.validation.bedomning.sjukskrivningar.periodhalften.overlap': '50% nedsatt: Datumintervall överlappar.',
        'ag7804.validation.bedomning.sjukskrivningar.perioden_fjardedel.missing': '25% nedsatt: Period saknas.', // Should never happen because GUI should block this combination
        'ag7804.validation.bedomning.sjukskrivningar.perioden_fjardedel.invalid_format': '25% nedsatt: Felaktigt datumformat.',
        'ag7804.validation.bedomning.sjukskrivningar.perioden_fjardedel.overlap': '25% nedsatt: Datumintervall överlappar.',
        'ag7804.validation.bedomning.sjukskrivningar.tidigtstartdatum': 'Det startdatum du angett är mer än <strong>en vecka före dagens datum</strong>. Du bör kontrollera att tidsperioderna är korrekta.',
        'ag7804.validation.bedomning.sjukskrivningar.sentslutdatum': 'Det datum du angett innebär <strong>en period på mer än 6 månader</strong>. Du bör kontrollera att tidsperioderna är korrekta.',
        'ag7804.validation.atgarder.inte_aktuellt_no_combine': 'Inte aktuellt kan inte kombineras med andra val.', // Should never happen because GUI should block this combination
        'ag7804.validation.atgarder.invalid_combination': 'Beskrivning för arbetslivsinriktade åtgärder kan inte kombineras med åtgärd på detta sätt.', // Should never happen because GUI should block this combination
        'ag7804.validation.atgarder.too-many': 'För många val på åtgärder.', // Should never happen because GUI should block this combination
        'ag7804.validation.atgarder.mastevaljas': 'Åtgärder måste väljas eller Inte aktuellt.',

        'ag7804.help.sjukskrivningar.sista-giltighets-datum': 'På det ursprungliga intyget var slutdatumet för den sista sjukskrivningsperioden {{lastEffectiveDate}} och sjukskrivningsgraden var {{sjukskrivningsgrad}}.',
        'ag7804.label.sjukskrivningar.tidigtstartdatum.motivering': 'Ange orsak för att starta perioden mer än 7 dagar bakåt i tiden.',
        'ag7804.label.sjukskrivningar.tidigtstartdatum.motivering.help': 'Observera att detta inte är en fråga från Försäkringskassan. Information om varför sjukskrivningen startar mer än en vecka före dagens datum kan vara till hjälp för Försäkringskassan i deras handläggning.',
        'ag7804.label.sjukskrivningar.tidigtstartdatum.motivering.info': 'Informationen överförs till fältet \'{0}\' vid signering.',

        'ag7804.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Invånaren kommer på Mina intyg, som nås via 1177.se, se att intyget är ersatt. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',

        'ag7804.fornya.tooltip': 'Om du vill förlänga en sjukskrivning kan du förnya intyget. Viss information från det befintliga intyget följer med till det nya utkastet.',
        'ag7804.modal.fornya.text': 'Förnya intyg kan användas vid förlängning av en sjukskrivning. När ett intyg förnyas skapas ett nytt intygsutkast med viss information från det ursprungliga intyget.<br><br>' +
            'Uppgifterna i det nya intygsutkastet går att ändra innan det signeras.<br><br>' +
            'De uppgifter som inte kommer med till det nya utkastet är:<br><br>' +
            '<ul>' +
            '<li>Sjukskrivningsperiod och grad.</li>' +
            '<li>Valet om man vill ha kontakt med försäkringskassan.</li>' +
            '<li>Referenser som intyget baseras på.</li>' +
            '</ul>' +
            'Eventuell kompletteringsbegäran kommer att klarmarkeras.<br><br>' +
            'Det nya utkastet skapas på den enhet du är  inloggad på.'
    },
    'en': {
        'ag7804.label.pagetitle': 'Show Certificate'
    }
});
