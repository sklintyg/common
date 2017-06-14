/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
angular.module('ts-bas').constant('ts-bas.messages', {
    'sv': {
        // Labels
        'ts-bas.label.pagetitle': 'Granska och skicka intyg',
        'ts-bas.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Transportstyrelsen, du kan även spara intyget som en PDF på din dator.<br>',
        'ts-bas.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'ts-bas.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Transportstyrelsen.',
        'ts-bas.label.pagedescription.save.or.print.certificate.header': '<h2>Spara eller skriva ut</h2>',
        'ts-bas.label.pagedescription.save.or.print.certificate': 'Klicka på knappen Spara som PDF för att spara eller skriva ut ditt intyg. Intyget öppnas eller laddas ner som en pdf-fil som du kan spara på din dator eller skriva ut.',
        'ts-bas.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'ts-bas.label.pagedescription.error.in.certificate': 'Om någon uppgift är fel i ditt intyg ska du kontakta den som utfärdat ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',
        'ts-bas.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'ts-bas.label.pagedescription.archive': 'För att arkivera intyget klickar du på knappen Arkivera.',

        //Helptexts
        'ts-bas.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',

        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.
        'ts-bas.compact-header.complementaryinfo-prefix': 'Avser behörighet:',
        'ts-bas.inbox.complementaryinfo': 'Avser behörighet',

        'ts-bas.label.syn.binokulart': 'Binokulärt',
        'ts-bas.label.syn.hogeroga': 'Höger öga',
        'ts-bas.label.syn.vansteroga': 'Vänster öga',
        'ts-bas.label.syn.utankorrektion': 'Utan korrektion',
        'ts-bas.label.syn.medkorrektion': 'Med korrektion',
        'ts-bas.label.syn.kontaktlins': 'Kontaktlinser',
        'ts-bas.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',

        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',

        'ts-bas.label.true': 'JA',
        'ts-bas.label.false': 'NEJ',

        'ts-bas.error.generic': 'Kunde inte visa intyget',
        'ts-bas.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'



    },
    'en': {
        'ts-bas.label.pagetitle': 'Show Certificate'
    }
});
