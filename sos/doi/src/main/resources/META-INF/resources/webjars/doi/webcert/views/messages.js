/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

angular.module('doi').constant('doi.messages', {
    'sv': {
        //Validation messages
        'doi.validation.sysselsattning.nuvarandearbete.invalid_combination': 'Beskrivningen får inte fyllas i när inte nuvarande arbete valts.', // Should never happen because GUI should block this combination
        'doi.validation.sysselsattning.too-many': 'sysselsättning.toomany',  // Should never happen because GUI should block this combination
        'doi.validation.bedomning.fmb.empty' : 'Beskriv varför arbetsförmågan bedöms vara nedsatt längre tid än den som det försäkringsmedicinska beslutstödet anger kan inte fyllas i med endast blanksteg',
        'doi.validation.bedomning.prognos.dagartillarbete.invalid_combination': 'Dagar till arbete kan bara fyllas i om prognosen "kan återgå helt i nuvarande sysselsättning efter x antal dagar valts"', // Should never happen because GUI should block this combination
        'doi.validation.bedomning.sjukskrivningar.missing': 'Minst en sjukskrivningsperiod måste anges.',
        'doi.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'doi.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.missing': 'Arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.',
        'doi.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect': '',
        'doi.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination': 'Motivering till arbetstidsförläggning måste fyllas i om period 75%, 50% eller 25% har valts.', // Should never happen
        'doi.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing': 'Tekniskt fel. Sjukskrivningsgrad saknas.', // Should never happen because GUI should block this combination
        'doi.validation.bedomning.sjukskrivningar.periodhelt_nedsatt.missing': 'Helt nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'doi.validation.bedomning.sjukskrivningar.periodhelt_nedsatt.invalid_format': 'Helt nedsatt: Felaktigt datumformat.',
        'doi.validation.bedomning.sjukskrivningar.periodhelt_nedsatt.overlap': 'Helt nedsatt: Datumintervall överlappar.',
        'doi.validation.bedomning.sjukskrivningar.periodtre_fjardedel.missing': '75% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'doi.validation.bedomning.sjukskrivningar.periodtre_fjardedel.invalid_format': '75% nedsatt: Felaktigt datumformat.',
        'doi.validation.bedomning.sjukskrivningar.periodtre_fjardedel.overlap': '75% nedsatt: Datumintervall överlappar.',
        'doi.validation.bedomning.sjukskrivningar.periodhalften.missing': '50% nedsatt: Tekniskt fel. Period saknas.', // Should never happen because GUI should block this combination
        'doi.validation.bedomning.sjukskrivningar.periodhalften.invalid_format': '50% nedsatt: Felaktigt datumformat.',
        'doi.validation.bedomning.sjukskrivningar.periodhalften.overlap': '50% nedsatt: Datumintervall överlappar.',
        'doi.validation.bedomning.sjukskrivningar.perioden_fjardedel.missing': '25% nedsatt: Period saknas.', // Should never happen because GUI should block this combination
        'doi.validation.bedomning.sjukskrivningar.perioden_fjardedel.invalid_format': '25% nedsatt: Felaktigt datumformat.',
        'doi.validation.bedomning.sjukskrivningar.perioden_fjardedel.overlap': '25% nedsatt: Datumintervall överlappar.',
        'doi.validation.bedomning.sjukskrivningar.tidigtstartdatum': 'Det startdatum du angett är mer än <strong>en vecka före dagens datum</strong>. Du bör kontrollera att tidsperioderna är korrekta.',
        'doi.validation.bedomning.sjukskrivningar.sentslutdatum': 'Det datum du angett innebär <strong>en period på mer än 6 månader</strong>. Du bör kontrollera att tidsperioderna är korrekta.',
        'doi.validation.atgarder.inte_aktuellt_no_combine': 'Inte aktuellt kan inte kombineras med andra val.', // Should never happen because GUI should block this combination
        'doi.validation.atgarder.invalid_combination': 'Beskrivning för arbetslivsinriktade åtgärder kan inte kombineras med åtgärd på detta sätt.', // Should never happen because GUI should block this combination
        'doi.validation.atgarder.too-many': 'För många val på åtgärder.', // Should never happen because GUI should block this combination

        'doi.label.sjukskrivningar.tidigtstartdatum.motivering': 'Ange orsak för att starta perioden mer än 7 dagar bakåt i tiden.',
        'doi.label.sjukskrivningar.tidigtstartdatum.motivering.help': 'Observera att detta inte är en fråga från Försäkringskassan. Information om varför sjukskrivningen startar mer än en vecka före dagens datum kan vara till hjälp för Försäkringskassan i deras handläggning.',
        'doi.label.sjukskrivningar.tidigtstartdatum.motivering.info': 'Informationen överförs till fältet \'{0}\' vid signering.',
        'doi.label.sjukskrivningar.sentslutdatum.motivering': 'Ange orsak för att sjukskrivningen är längre än 6 månader.',
        'doi.label.sjukskrivningar.sentslutdatum.motivering.help': 'Observera att detta inte är en fråga från Försäkringskassan. Information om varför sjukskrivningen är längre än 6 månader kan vara till hjälp för Försäkringskassan i deras handläggning.',
        'doi.label.sjukskrivningar.sentslutdatum.motivering.info': 'Informationen överförs till fältet \'{0}\' vid signering.',

        'doi.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Invånaren kommer på Mina intyg, som nås via 1177.se, se att intyget är ersatt. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',

        'doi.fornya.tooltip': 'Om du vill förlänga en sjukskrivning kan du förnya intyget. Viss information från det befintliga intyget följer med till det nya utkastet.',
        'doi.modal.fornya.text': 'Förnya intyg kan användas vid förlängning av en sjukskrivning. När ett intyg förnyas skapas ett nytt intygsutkast med viss information från det ursprungliga intyget. Uppgifterna i det nya intygsutkastet går att ändra innan det signeras. Om intyget är utfärdat på en annan vårdenhet kommer det nya utkastet utfärdas på den enhet du är inloggad på. I de fall patienten har ändrat namn eller adress så uppdateras den informationen.<br><br>' +
        'De uppgifter som inte kommer med till det nya utkastet är:<br><br>' +
        '<ul><li>Information om sjukskrivningsperiod.</li>' +
        '<li>Valet om man vill ha kontakt med försäkringskassan.</li>' +
        '<li>Referenser som intyget baseras på.</li></ul>'
    },
    'en': {
        'doi.label.pagetitle': 'Show Certificate'
    }
});
