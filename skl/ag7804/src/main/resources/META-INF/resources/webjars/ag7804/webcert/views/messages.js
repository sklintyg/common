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
'use strict';

angular.module('ag7804').constant('ag7804.messages', {
    'sv': {
        //Validation messages
        'ag7804.validation.sysselsattning.nuvarandearbete.invalid_combination': 'Beskrivningen får inte fyllas i när inte nuvarande arbete valts.', // Should never happen because GUI should block this combination
        'ag7804.validation.sysselsattning.too-many': 'sysselsättning.toomany',  // Should never happen because GUI should block this combination
        'ag7804.validation.bedomning.fmb.empty' : 'Beskriv varför arbetsförmågan bedöms vara nedsatt längre tid än den som det försäkringsmedicinska beslutstödet anger kan inte fyllas i med endast blanksteg.',
        'ag7804.validation.bedomning.prognos.dagartillarbete.invalid_combination': 'Dagar till arbete kan bara fyllas i om prognosen "kan återgå helt i nuvarande sysselsättning efter x antal dagar valts".', // Should never happen because GUI should block this combination
        'ag7804.validation.bedomning.sjukskrivningar.missing': 'Välj minst ett alternativ.',
        'ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing': 'Välj ett alternativ.',
        'ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.missing': 'Ange motivering.',
        'ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect': '',
        'ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination': 'Ange motivering.', // Should never happen
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
        'ag7804.validation.atgarder.inte_aktuellt_no_combine': 'Inte aktuellt kan inte kombineras med andra val.', // Should never happen because GUI should block this combination
        'ag7804.validation.atgarder.invalid_combination': 'Beskrivning för arbetslivsinriktade åtgärder kan inte kombineras med åtgärd på detta sätt.', // Should never happen because GUI should block this combination
        'ag7804.validation.atgarder.too-many': 'För många val på åtgärder.', // Should never happen because GUI should block this combination
        'ag7804.validation.grund-for-mu.annat.beskrivning.invalid_combination': 'annat beskrivning invalid combination.',  // Should never happen because GUI should block this combination
        'ag7804.validation.kontakt.invalid_combination': 'Anledning får inte fyllas i när man svarat nej på kontakt.',  // Should never happen because GUI should block this combination
        'ag7804.validation.blanksteg.otillatet': 'Fältet får inte fyllas i med endast blanksteg.',  // Should never happen because GUI should block this combination
        'ag7804.validation.funktionsnedsattning.missing': 'Ange ett svar.',
        'ag7804.validation.aktivitetsbegransning.missing': 'Ange ett svar.',

        'ag7804.help.sjukskrivningar.sista-giltighets-datum': 'På det ursprungliga intyget var slutdatumet för den sista sjukskrivningsperioden {{lastEffectiveDate}} och sjukskrivningsgraden var {{sjukskrivningsgrad}}.',
        /// IS-14c / Modal-02c
        'ag7804.modalbody.intygstatus.is-008': '<p>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg></p>',
        'ag7804.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Invånaren kommer på Mina intyg, som nås via 1177.se, se att intyget är ersatt. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',

        'ag7804.fornya.tooltip': 'Om du vill förlänga en sjukskrivning kan du förnya intyget. Viss information från det befintliga intyget följer med till det nya utkastet.',
        'ag7804.modal.fornya.text': 'Förnya intyg kan användas vid förlängning av en sjukskrivning. När ett intyg förnyas skapas ett nytt intygsutkast med viss information från det ursprungliga intyget.<br><br>' +
            'Uppgifterna i det nya intygsutkastet går att ändra innan det signeras.<br><br>' +
            'De uppgifter som inte kommer med till det nya utkastet är:<br><br>' +
            '<ul>' +
            '<li>Valet om diagnos ska förmedlas till arbetsgivaren</li>' +
            '<li>Valet om funktionsnedsättning ska förmedlas till arbetsgivaren</li>' +
            '<li>Sjukskrivningsperiod och grad</li>' +
            '<li>Valet om man vill ha kontakt med arbetsgivaren</li>' +
            '<li>Referenser som intyget baseras på</li>' +
            '</ul>' +
            '<br>Det nya utkastet skapas på den enhet du är  inloggad på.',

        'ag7804.modal.copy-from-candidate.text': 'Det finns ett Läkarintyg för sjukpenning för denna patient som är utfärdat av dig ${createdDate}. Vill du kopiera de svar som givits i det intyget till detta intygsutkast?'
    },
    'en': {
        'ag7804.label.pagetitle': 'Show Certificate'
    }
});
