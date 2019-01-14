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
angular.module('luae_fs').constant('luae_fs.messages', {
    'sv': {
        'luae_fs.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Invånaren kommer på Mina intyg, som nås via 1177.se, se att intyget är ersatt. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
            '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',

        'luae_fs.fornya.tooltip': 'En kopia av det tidigare intyget skapas. Samtlig information följer med till det nya intygsutkastet. Informationen kan sedan ändras.',
        'luae_fs.modal.fornya.text': 'Förnya intyg innebär att ett nytt intygsutkast skapas med samma information som i det ursprungliga intyget.<br><br>' +
        'Uppgifterna i det nya intygsutkastet går att ändra innan det signeras.<br><br>' +
        'I de fall patienten har ändrat namn eller adress så uppdateras den informationen.<br><br>' +
        'Eventuell kompletteringsbegäran kommer att klarmarkeras.<br><br>' +
        'Det nya utkastet skapas på den enhet du är inloggad på.'
    },
    'en': {
        'luae_fs.label.pagetitle': 'Show Certificate'
    }
});
