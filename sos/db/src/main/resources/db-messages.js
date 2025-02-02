/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

angular.module('db').constant('db.messages', {
    'sv': {
        //Validation messages
        'db.validation.explosivAvlagsnat.explosivImplantatFalse': 'Den valda kombinationen är ogiltig.',
        'db.validation.undersokningDatum.after.dodsdatum': 'Ange ett datum, samma som eller tidigare än "Dödsdatum".',
        'db.validation.undersokningDatum.after.antraffatDodDatum' : 'Ange ett datum, samma som eller tidigare än "Anträffad död".',
        'db.validation.undersokningDatum.before.beginningOflastYear' : 'Ange ett datum, samma som eller senare än 1 januari föregående året.',
        'db.validation.undersokningDatum.before.fourWeeks' : 'Ange ett datum, samma som eller tidigare än fyra veckor före döden.',

        'db.validation.datum.innanDodsdatum': 'Ange ett datum, samma som eller senare än "Dödsdatum".',

        'db.label.makulera.body.common-footer': '<b>Du ska endast makulera om intyget är utfärdat på fel person.</b> Om du behöver ändra innehållet i intyget ska du välja "Avbryt" och därefter välja "Ersätt".',
        'db.makulera.body.common-header': '<p>För att makulera ett dödsbevis som är skickat på fel person måste du genomföra följande två steg:</p><ol type="1"><li><b>Kontakta omedelbart Skatteverket</b> på telefon <b class="unbreakable">010-574 94 89</b> för att begära att intyget makuleras.<br><i>OBS! telefonnumret gäller endast för att makulera Dödsbevis, inga andra frågor.</i></li><li>Tryck på "Makulera" för att makulera dödsbeviset i Webcert.</li></ol>',
        'db.makulera.locked.body.common-header': 'Ett låst utkast kan makuleras om det är utfärdat på fel person. Genom att trycka på "Makulera" makulerar du det låsta utkastet i Webcert.',
        'db.modal.ersatt.text.info': 'Om dödsbeviset är utfärdat på fel patient ska du istället makulera dödsbeviset.',
        'db.modal.ersatt.text':
        '<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt skapas ett utkast, med samma information som i det ursprungliga intyget, som du kan redigera innan du signerar intyget.</p>' +
        '<p>Senast skapade dödsbevis är det som gäller. Om du ersätter det tidigare dödsbeviset och lämnar in det nya så blir det därför detta dödsbevis som gäller.</p>' +
        '<p>Det nya utkastet skapas på den enhet du är inloggad på.</p>',

        'db.label.status.recieved': '<p>Dödsbeviset är signerat och har nu skickats till Skatteverket.</p>'+
            '<p>Glöm inte att göra en journalanteckning att dödsbevis är inlämnat!</p>'+
            '<p>Du kan nu avsluta Webcert eller direkt skriva ett dödsorsaksintyg för samma person genom att trycka på knappen "Skriv dödsorsaksintyg" ovan.</p>',

        'db.status.revoke.requested': '<strong>Status:</strong> Intyget är makulerat.',
        'db.status.revoke.confirmed': '<strong>Status:</strong> Intyget är makulerat.',

        'db.createfromtemplate.doi.tooltip': 'Skapar ett dödsorsaksintyg utifrån dödsbeviset.',
        'db.gotocreatefromtemplate.doi.tooltip': 'Visa det dödsorsaksintyg som har skapats från dödsbeviset.',
        'db.createfromtemplate.doi.modal.header': 'Skapa dödsorsaksintyg utifrån dödsbevis',
        'db.createfromtemplate.doi.modal.text': 'Skapa ett dödsorsaksintyg utifrån ett dödsbevis innebär att informationsmängder som är gemensamma för båda intygen, automatiskt förifylls.',
        'db.createfromtemplate.doi.modal.text.info': 'Dödsorsaksintyg finns för detta personnummer hos annan vårdgivare. Senast skapade dödsorsaksintyg är det som gäller. Om du fortsätter och lämnar in dödsorsaksintyget så blir det därför detta dödsorsaksintyg som gäller.'
    },
    'en': {
        'db.label.pagetitle': 'Show Certificate'
    }
});
