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

angular.module('doi').constant('doi.messages', {
    'sv': {
        //Validation messages
        'common.validation.dodsorsak.empty': 'Ange ett svar.',
        'common.validation.date.incorrect_combination': 'Datumet får inte vara efter efterkommande följd eller terminal dödsorsak.',
        'common.validation.date.e-06': 'Ange ett datum, samma som eller tidigare än fyra veckor före döden.',

        'doi.validation.datum.innanDodsdatum': 'Ange ett datum, samma som eller senare än "Dödsdatum".',

        'doi.validation.forgiftning.orsak.incorrect_combination': 'Ange endast om "Dödsfall i samband med skada/förgiftning" besvarats med "Ja".',
        'doi.validation.forgiftning.datum.incorrect_combination': 'Ange endast datum om "Dödsfall i samband med skada/förgiftning" besvarats med "Ja".',
        'doi.validation.forgiftning.uppkommelse.incorrect_combination': 'Ange endast om "Dödsfall i samband med skada/förgiftning" besvarats med "Ja".',

        'operation.operationAnledning.operationNejUppgiftSaknas': 'Ange endast om val anges som "Ja"',
        'operation.operationDatum.efterDodsdatum': 'Ange ett datum, samma som eller tidigare än "Dödsdatum".',
        'operation.operationDatum.efterAntraffatDodDatum': 'Ange ett datum, samma som eller tidigare än "Anträffad död".',
        'forgiftning.forgiftningDatum.efterDodsdatum': 'Ange ett datum, samma som eller tidigare än  "Dödsdatum".',
        'forgiftning.forgiftningDatum.efterAntraffatDodDatum': 'Ange ett datum, samma som eller tidigare än "Anträffad död".',
        'doi.validation.terminalDodsorsak.datum.efterDodsdatum': 'Ange ett datum, samma som eller tidigare än "Dödsdatum".',
        'doi.validation.terminalDodsorsak.datum.efterAntraffatDodsdatum': 'Ange ett datum, samma som eller tidigare än "Anträffad död".',
        'doi.validation.foljd.datum.f.val-050': 'Ange ett datum, samma som eller tidigare än den terminala dödsorsaken.',
        'doi.validation.foljd.datum.f.val-051': 'Ange ett datum, samma som eller tidigare än datumet för sjukdomen eller skadan som angavs i föregående "Som var en följd av".',

        'doi.info.barn.forced.true': 'Det angivna dödsdatumet infaller inom 28 dagar efter barnets födelsedatum. Fältet har därför förifyllts.',
        'doi.info.barn.forced.false': 'Det angivna dödsdatumet infaller inte inom 28 dagar efter barnets födelsedatum. Fältet har därför förifyllts.',

        'doi.label.status.recieved': 'Dödsorsaksintyget är signerat och har nu skickats till Socialstyrelsen.<br><br>Glöm inte att göra en journalanteckning att dödsorsaksintyg är inlämnat!',

        'doi.label.makulera.body.common-footer': '<b>Du ska endast makulera om intyget är utfärdat på fel person.</b> Om du behöver ändra innehållet i intyget ska du välja "Avbryt" och därefter välja "Ersätt".',
        'doi.makulera.body.common-header': '<p>För att makulera ett dödsorsaksintyg som är skickat på fel person måste du genomföra följande två steg:</p><ol type="1"><li>Kontakta omedelbart Socialstyrelsen (Dödsorsaksproduktionen) på telefon <b class="unbreakable">075-247 39 00</b> för att begära att intyget makuleras.</li><li>Tryck på "Makulera" för att makulera dödsorsaksintyget i Webcert.</li></ol>',
        'doi.makulera.locked.body.common-header': 'Ett låst utkast kan makuleras om det är utfärdat på fel person. Genom att trycka på "Makulera" makulerar du det låsta utkastet i Webcert.',
        'doi.modal.ersatt.text.info': 'Om dödsorsaksintyget är utfärdat på fel patient ska du istället makulera dödsorsaksintyget.',
        'doi.modal.ersatt.text':
        '<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt skapas ett utkast, med samma information som i det ursprungliga intyget, som du kan redigera innan du signerar intyget.</p>' +
        '<p>Senast skapade dödsorsaksintyg är det som gäller. Om du ersätter det tidigare dödsorsaksintyget och lämnar in det nya så blir det därför detta dödsorsaksintyg som gäller.</p>' +
        '<p>Det nya utkastet skapas på den enhet du är inloggad på.</p>',

        'doi.fornya.tooltip': 'Om du vill förlänga en sjukskrivning kan du förnya intyget. Viss information från det befintliga intyget följer med till det nya utkastet.',
        'doi.modal.fornya.text': 'Förnya intyg kan användas vid förlängning av en sjukskrivning. När ett intyg förnyas skapas ett nytt intygsutkast med viss information från det ursprungliga intyget. Uppgifterna i det nya intygsutkastet går att ändra innan det signeras. Om intyget är utfärdat på en annan enhet kommer det nya utkastet utfärdas på den enhet du är inloggad på. I de fall patienten har ändrat namn eller adress så uppdateras den informationen.<br><br>' +
        'De uppgifter som inte kommer med till det nya utkastet är:<br><br>' +
        '<ul><li>Information om sjukskrivningsperiod.</li>' +
        '<li>Valet om man vill ha kontakt med Försäkringskassan.</li>' +
        '<li>Referenser som intyget baseras på.</li></ul>',

        'doi.status.revoke.requested': '<strong>Status:</strong> Intyget är makulerat.',
        'doi.status.revoke.confirmed': '<strong>Status:</strong> Intyget är makulerat.',

        'doi.modal.copy-from-candidate.text': 'Det finns ett signerat dödsbevis (från den <strong>${createdDate}</strong>) för detta personnummer på samma vårdenhet som du är inloggad. Vill du kopiera de svar som givits i det intyget till detta intygsutkast?',
        'doi.modal.show-candidate-vardenhet-info.title': 'Information om dödsbevis',
        'doi.modal.show-candidate-vardenhet-details.title': 'Information om vårdenhet',
        'doi.modal.show-candidate-vardenhet-info.text': 'Det finns ett signerat dödsbevis för detta personnummer på annan vårdenhet än den du är inloggad på. Vill du se information om vilken vårdenhet det handlar om?',
        'doi.modal.show-candidate-vardenhet-details.text': 'Det finns ett signerat dödsbevis för detta personnummer på <strong>${candidateUnitName}</strong>. Det är tyvärr inte möjligt att kopiera de svar som givits i det intyget till detta intygsutkast.'
    },
    'en': {
        'doi.label.pagetitle': 'Show Certificate'
    }
});
