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

angular.module('af00251').constant('af00251.messages', {
    'sv': {
        //Validation messages
        'af00251.validation.undersokning.future-date': 'Ange ett giltigt datum. Framtida datum får inte anges.',
        'af00251.validation.harForhinder.forbidden-sjukfranvaro': 'Om patienten inte har förhinder får sjukfrånvaro ej anges.',
        'af00251.validation.arbetsmarknadspolitisktProgram.omfattningDeltid.invalid-range': 'Antal timmar måste vara mellan 1 - 39',
        'af00251.validation.sjukfranvaro.too-many': 'För många sjukfrånvaroperioder.', // Should never happen because GUI should block this combination
        'af00251.validation.sjukfranvaro.missing': 'Välj minst ett alternativ.',
        'af00251.validation.prognosAtergang.prognos-no-anpassning': 'Om anpassning krävs måste detta beskrivas.',
        'af00251.help.sjukskrivningar.sista-giltighets-datum': 'På det ursprungliga intyget var slutdatumet för den sista frånvaroperioden {{lastEffectiveDate}} och omfattningen var {{sjukskrivningsgrad}}%.',

        'af00251.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Invånaren kommer på Mina intyg, som nås via 1177.se, se att intyget är ersatt. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',

        'af00251.fornya.tooltip': '',
        'af00251.modal.fornya.text': 'Förnya intyg kan användas vid förlängning av en sjukskrivning. När ett intyg förnyas skapas ett nytt intygsutkast med viss information från det ursprungliga intyget. <br><br>Uppgifterna i det nya intygsutkastet går att ändra innan det signeras. <br><br>Om intyget är utfärdat på en annan enhet kommer det nya utkastet utfärdas på den enhet du är inloggad på.<br><br>De uppgifter som inte kommer med till det nya utkastet är:<br><br><ul><li>Sjukfrånvaroperiod och nivå.</li><li>Referenser som intyget baseras på.</li></ul><br>Det nya utkastet skapas på den enhet du är inloggad på.'
    },
    'en': {
        'af00251.label.pagetitle': 'Show Certificate'
    }
});
