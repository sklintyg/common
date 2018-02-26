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

angular.module('doi').constant('doi.messages', {
    'sv': {
        //Validation messages
        'common.validation.dodsorsak.empty': 'Beskrivning får inte vara tom.',
        'common.validation.date.incorrect_combination': 'Datumet får inte vara efter efterkommande följd eller terminal dödsorsak.',

        'doi.validation.forgiftning.orsak.incorrect_combination': 'Orsak får ej anges om "Dödsfall i samband med skada/förgiftning" besvarats med "Nej"',
        'doi.validation.forgiftning.datum.incorrect_combination': 'Datum får ej anges om "Dödsfall i samband med skada/förgiftning" besvarats med "Nej"',
        'doi.validation.forgiftning.uppkommelse.incorrect_combination': 'Uppkommelse får ej anges om "Dödsfall i samband med skada/förgiftning" besvarats med "Nej"',

        'operation.operationAnledning.operationNejUppgiftSaknas': 'Beskrivning om tillstånd får inte anges om val anges som Nej eller Uppgift Saknas.',
        'operation.operationDatum.efterDodsdatum': 'Datumet får inte vara senare än "Dödsdatum".',
        'operation.operationDatum.efterAntraffatDodDatum': 'Datumet får inte vara senare än datumet för "Anträffad död".',
        'forgiftning.forgiftningDatum.efterDodsdatum': 'Datumet får inte vara senare än "Dödsdatum".',
        'forgiftning.forgiftningDatum.efterAntraffatDodDatum': 'Datumet får inte vara senare än datumet för "Anträffad död".',
        'doi.validation.terminalDodsorsak.datum.efterDodsdatum': 'Datum för terminal dödsorsak får inte vara senare än "Dödsdatum".',
        'doi.validation.terminalDodsorsak.datum.efterAntraffatDodsdatum': 'Datum för terminal dödsorsak får inte vara senare än "Anträffat död".',

        'doi.info.barn.forced.true': 'Det angivna dödsdatumet infaller inom 28 dagar efter barnets födelsedatum. Fältet har därför förifyllts.',
        'doi.info.barn.forced.false': 'Det angivna dödsdatumet infaller inte inom 28 dagar efter barnets födelsedatum. Fältet har därför förifyllts.',

        'doi.label.signandsend': 'Om du går vidare kommer dödsorsaksintyget signeras och skickas direkt till Socialstyrelsens system.',
        'doi.label.status.recieved': 'Dödsorsaksintyget är signerat och har nu skickats till Socialstyrelsen.<br><br>Glöm inte att göra en journalanteckning att dödsorsaksintyg är inlämnat!',

        'doi.makulera.body.common-header': 'Intyg som innehåller allvarliga fel kan makuleras. Exempel på ett allvarligt fel är om intyget är utfärdat på fel patient. Om intyget har skickats elektroniskt till en mottagare kommer mottagaren att informeras om makuleringen.',
        'doi.modal.ersatt.text':'<p>Senast skapade dödsorsaksintyg är det som gäller. Om du ersätter det tidigare dödsorsaksintyget och lämnar in det nya så blir det därför detta dödsorsaksintyg som gäller.</p>' +
        '<p>Ett dödsorsaksintyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att dödsorsaksintyget utfärdades. När ett dödsorsaksintyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga dödsorsaksintyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Ett ersatt dödsorsaksintyg är endast tillgängligt att läsa och skriva ut. På det ersatta dödsorsaksintyget kommer en länk finnas till det nya dödsorsaksintyget.</p>' +
        '<p><b>Notera</b>: Om dödsorsaksintyget innehåller ett allvarligt fel, till exempel om dödsorsaksintyget är utfärdat på fel patient ska du istället makulera dödsorsaksintyget.</p>',

        'doi.fornya.tooltip': 'Om du vill förlänga en sjukskrivning kan du förnya intyget. Viss information från det befintliga intyget följer med till det nya utkastet.',
        'doi.modal.fornya.text': 'Förnya intyg kan användas vid förlängning av en sjukskrivning. När ett intyg förnyas skapas ett nytt intygsutkast med viss information från det ursprungliga intyget. Uppgifterna i det nya intygsutkastet går att ändra innan det signeras. Om intyget är utfärdat på en annan enhet kommer det nya utkastet utfärdas på den enhet du är inloggad på. I de fall patienten har ändrat namn eller adress så uppdateras den informationen.<br><br>' +
        'De uppgifter som inte kommer med till det nya utkastet är:<br><br>' +
        '<ul><li>Information om sjukskrivningsperiod.</li>' +
        '<li>Valet om man vill ha kontakt med försäkringskassan.</li>' +
        '<li>Referenser som intyget baseras på.</li></ul>',

        'doi.warn.previouscertificate.samevg': 'Dödsorsaksintyg finns för detta personnummer. Du kan inte skapa ett nytt dödsorsaksintyg men kan däremot välja att ersätta det befintliga dödsorsaksintyget.',
        'doi.warn.previouscertificate.differentvg': 'Tidigare dödsorsaksintyg finns för detta personnummer hos annan vårdgivare. Senast skapade dödsorsaksintyg är det som gäller. Om du fortsätter och lämnar in dödsorsaksintyget så blir det därför detta dödsorsaksintyg som gäller.',
        'doi.warn.previousdraft.samevg': 'Utkast på dödsorsaksintyg finns för detta personnummer. Du kan inte skapa ett nytt utkast men kan däremot välja att fortsätta med det befintliga utkastet.',
        'doi.warn.previousdraft.differentvg': 'Utkast på dödsorsaksintyg finns för detta personnummer hos annan vårdgivare. Senast skapade dödsorsaksintyg är det som gäller. Om du fortsätter och lämnar in dödsorsaksintyget så blir det därför detta dödsorsaksintyg som gäller.',

        'doi.status.revoke.requested': '<strong>Status:</strong> Intyget är makulerat.',
        'doi.status.revoke.confirmed': '<strong>Status:</strong> Intyget är makulerat.'
    },
    'en': {
        'doi.label.pagetitle': 'Show Certificate'
    }
});
