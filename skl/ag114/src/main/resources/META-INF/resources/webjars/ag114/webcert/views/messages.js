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
angular.module('ag114').constant('ag114.messages', {
    'sv': {
        //Note : these are compound message keys, overriding common messages,

        /// IS-14c / Modal-02c
        'ag114.modalbody.intygstatus.is-008': '<p>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg></p>',

        'ag114.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Invånaren kommer på Mina intyg, som nås via 1177.se, se att intyget är ersatt. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
            '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',

        'ag114.fornya.tooltip': 'En kopia av det tidigare intyget skapas. Samtlig information följer med till det nya intygsutkastet. Informationen kan sedan ändras.',
        'ag114.modal.fornya.text': 'Förnya intyg innebär att ett nytt intygsutkast skapas med samma information som i det ursprungliga intyget. Uppgifterna i det nya intygsutkastet går att ändra innan det signeras.<br><br>' +
        'Om intyget är utfärdat på en annan enhet kommer det nya utkastet utfärdas på den enhet du är inloggad på. I de fall patienten har ändrat namn eller adress så uppdateras den informationen.<br><br>' +
        'Ärenden som ännu inte är hanterade kommer markeras som hanterade och kommer inte visas i det förnyade intyget.',
        'ag114.validation.sjukskrivningsgrad.invalid.percent': 'Ange ett värde mellan 0 och 100 %',
        'ag114.validation.grund-for-mu.annat.beskrivning.invalid_combination': 'annat beskrivning invalid combination.'
    },
    'en': {
        'ag114.label.pagetitle': 'Show Certificate'
    }
});
