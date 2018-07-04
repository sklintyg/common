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

angular.module('afmu').constant('afmu.messages', {
    'sv': {
        //Validation messages
        'afmu.validation.atgarder.inte_aktuellt_no_combine': 'Inte aktuellt kan inte kombineras med andra val.', // Should never happen because GUI should block this combination
        'afmu.validation.atgarder.invalid_combination': 'Beskrivning för arbetslivsinriktade åtgärder kan inte kombineras med åtgärd på detta sätt.', // Should never happen because GUI should block this combination
        'afmu.validation.atgarder.too-many': 'För många val på åtgärder.', // Should never happen because GUI should block this combination
        'afmu.validation.atgarder.mastevaljas': 'Åtgärder måste väljas eller Inte aktuellt.',

        'afmu.help.sjukskrivningar.sista-giltighets-datum': 'På det ursprungliga intyget var slutdatumet för den sista sjukskrivningsperioden {{lastEffectiveDate}} och sjukskrivningsgraden var {{sjukskrivningsgrad}}.',
        'afmu.label.sjukskrivningar.tidigtstartdatum.motivering': 'Ange orsak för att starta perioden mer än 7 dagar bakåt i tiden.',
        'afmu.label.sjukskrivningar.tidigtstartdatum.motivering.help': 'Observera att detta inte är en fråga från Försäkringskassan. Information om varför sjukskrivningen startar mer än en vecka före dagens datum kan vara till hjälp för Försäkringskassan i deras handläggning.',
        'afmu.label.sjukskrivningar.tidigtstartdatum.motivering.info': 'Informationen överförs till fältet \'{0}\' vid signering.',

        'afmu.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Invånaren kommer på Mina intyg, som nås via 1177.se, se att intyget är ersatt. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',

        'afmu.fornya.tooltip': 'Om du vill förlänga en sjukskrivning kan du förnya intyget. Viss information från det befintliga intyget följer med till det nya utkastet.',
        'afmu.modal.fornya.text': 'Förnya intyg kan användas vid förlängning av en sjukskrivning. När ett intyg förnyas skapas ett nytt intygsutkast med viss information från det ursprungliga intyget. Uppgifterna i det nya intygsutkastet går att ändra innan det signeras. Om intyget är utfärdat på en annan enhet kommer det nya utkastet utfärdas på den enhet du är inloggad på. <br><br>' +
        'De uppgifter som inte kommer med till det nya utkastet är:<br><br>' +
            '<ul><li>Sjukskrivningsperiod och grad.</li>' +
            '<li>Valet om man vill ha kontakt med försäkringskassan.</li>' +
            '<li>Referenser som intyget baseras på.</li></ul><br>' +
            'Ärenden som ännu inte är hanterade kommer markeras som hanterade och kommer inte visas i det förnyade intyget.'
    },
    'en': {
        'afmu.label.pagetitle': 'Show Certificate'
    }
});
