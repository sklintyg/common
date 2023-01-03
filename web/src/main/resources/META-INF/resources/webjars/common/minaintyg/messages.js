/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('common').constant('common.messages', {
    'sv': {
        'common.yes': 'Ja',
        'common.no': 'Nej',
        'common.notset': 'Ej angivet',
        'common.yes.caps': 'JA',
        'common.no.caps': 'NEJ',
        'common.nodontask': 'Nej, fråga inte igen',
        'common.ok': 'OK',
        'common.cancel': 'Avbryt',
        'common.goback': 'Tillbaka',
        'common.sign': 'Signera',
        'common.send': 'Skicka',
        'common.copy': 'Kopiera',
        'common.delete': 'Ta bort',

        'common.date': 'Datum',

        // wc-common-directives-resources
        'nav.label.loggedinas': 'Inloggad som:',

        // modal messages

        // Cert module messages. Used by several cert modules.
        'modules.label.field': 'Fält',
        'modules.label.blank': 'Ej ifyllt',
        'modules.message.certificateloading': 'Hämtar intyg...',

        // texts used by mi-certificate-action-buttons directive (would be nice to have resource keys per directive, like scss...)
        'modules.actionbar.button.send' : 'Välj mottagare och skicka intyget',
        'modules.actionbar.button.send.tooltip' : 'Skicka intyget elektroniskt, t.ex. till Försäkringskassan eller Transportstyrelsen.',
        'modules.actionbar.button.customizepdf' : 'Anpassa intyg till arbetsgivare',
        'modules.actionbar.button.customizepdf.tooltip' : 'Du kan välja bort intygsinformation du inte vill delge din arbetsgivare.',
        'modules.actionbar.button.print' : 'Ladda ner intyg som PDF',
        'modules.actionbar.button.print.tooltip' : 'Intyget sparas som PDF på din enhet.',
        'modules.actionbar.button.archive' : 'Arkivera intyg',
        'modules.actionbar.button.archive.tooltip' : 'Flytta intyget till Arkiverade intyg.',
        'modules.actionbar.archivedialog.title' : 'Arkivera intyg',
        'modules.actionbar.archivedialog.body' : 'När du väljer att arkivera intyget kommer det att flyttas till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',
        'modules.actionbar.archivedialog.archive.button' : 'Arkivera intyg',

        // Common texts used in page headers
        'modules.page-header.view-intyg.page-title': 'Läs och hantera ditt intyg',
        'modules.page-header.info.select-recipients-and-send': '<h3>Välj mottagare och skicka intyget</h3>Klicka på knappen för att komma till sidan där du väljer mottagare och skickar intyget. Detta intyg kan till exempel skickas till Försäkringskassan.',
        'modules.page-header.info.select-recipients-and-send.ts': '<h3>Välj mottagare och skicka intyget</h3>Klicka på knappen för att komma till sidan där du väljer mottagare och skickar intyget. Detta intyg kan till exempel skickas till Transportstyrelsen.',
        'modules.page-header.info.select-recipients-and-send.af': '<h3>Välj mottagare och skicka intyget</h3>Klicka på knappen för att komma till sidan där du väljer mottagare och skickar intyget. Detta intyg kan till exempel skickas till Arbetsförmedlingen.',
        'modules.page-header.info.customize-pdf': '<h3>Anpassa intyg till arbetsgivare</h3>Klicka på knappen om du inte vill visa din diagnos för arbetsgivare. Information i alla andra fält lämnas till arbetsgivaren och de är därför markerade som obligatoriska.',
        'modules.page-header.info.download-pdf': '<h3>Ladda ner intyg som PDF</h3>' +
                                                 '<p>Klicka på knappen för att ladda ner intyget som PDF. Du kan därefter välja hur du vill hantera intyget vidare, till exempel kan du skriva ut det. PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna PDF-filer behöver du en PDF läsare, exempelvis <LINK:adobeReader>.<p>' +
                                                 '<p>När du laddar ner ditt intyg som PDF kommer det att sparas till den dator/enhet du använder. Om du till exempel använder en offentlig dator kan det vara bra att radera det nerladdade intyget innan du lämnar datorn/enheten.</p>',
        'modules.page-header.info.archive': '<h3>Arkivera intyg</h3>Klicka på knappen för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',
        'modules.page-header.info.more.false':'<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'modules.page-header.info.more.true':'<span class="glyphicon glyphicon-chevron-down"></span> Läs mer om knappfunktioner',
        'modules.view-intyg.end-of-intyg.text': 'Slut på intyget',

        // Certificates events
        'certificates.events.received': 'Mottagits av {0}',
        'certificates.events.sent': 'Skickat till {0}',
        'certificates.events.noevents': 'Inga händelser',
        'certificates.events.unknowndatetime': 'Okänd tid',
        'certificates.events.eventsshown': '(visar {0} av {1} händelser)',
        'certificates.events.ersatt': '<a data-ng-click="viewCert({type: \'{1}\', typeVersion: \'{2}\', id: \'{0}\'})">Ersattes av vården med ett nytt intyg</a>',
        'certificates.events.ersatter': '<a data-ng-click="viewCert({type: \'{1}\', typeVersion: \'{2}\', id: \'{0}\'})">Ersätter ett intyg som inte längre är aktuellt</a>',
        'certificates.events.ersatt.warn.header': 'OBS! Intyget har ersatts av vården med <a href="/web/start#/{1}/{2}/view/{0}">detta intyg</a>.',
        'certificates.events.ersatt.warn.text': 'Läkaren kan ersätta ett intyg om till exempel intyget innehåller felaktig information eller ny information tillkommit.',
        'certificates.events.kompletterat': '<a data-ng-click="viewCert({type: \'{1}\', typeVersion: \'{2}\', id: \'{0}\'})">Kompletterades av vården med ett nytt intyg</a>',
        'certificates.events.kompletterar': '<a data-ng-click="viewCert({type: \'{1}\', typeVersion: \'{2}\', id: \'{0}\'})">Kompletterar ett intyg som inte längre är aktuellt</a>',
        'certificates.events.kompletterat.warn.header': 'OBS! Intyget har kompletterats av vården med <a href="/web/start#/{1}/{2}/view/{0}">detta intyg</a>.',
        'certificates.events.kompletterat.warn.text': 'Läkaren kan svara med ett nytt intyg, innehållande de kompletterade uppgifterna, om Försäkringskassan begärt en komplettering av vården.',

        //Common text in customized pdf wizard (ag7804)
        'modules.customize.message.customize.disabled': 'Intyg som avser avstängning enligt smittskyddslagen kan inte anpassas till din arbetsgivare. Om du vill hämta det fullständiga intyget så klicka på knappen <i>Ladda ner intyg som PDF</i>.',
        'modules.customize.label.mandatory': 'Obligatorisk info',
        'modules.customize.label.optional': 'Inkludera i anpassat intyg',
        'modules.customize.message.warning': 'Dina val är frivilliga, men att dela med sig av information kan underlätta arbetsgivarens möjlighet att göra arbetsanpassningar för dig.',
        'modules.customize.remove-field.header': 'Vill du verkligen ta bort denna information?',
        'modules.customize.remove-field.body': 'Informationen är frivillig, men informationen kan underlätta arbetsgivarens möjlighet att göra arbetsanpassningar.',
        'modules.customize.remove-field.button.confirm': 'Ja, ta bort',
        'modules.customize.remove-field.button.cancel': 'Nej, ta inte bort',
        'modules.customize.fishbone.step1': '1. Anpassa intyg',
        'modules.customize.fishbone.step2': '2. Granska dina val',
        'modules.customize.fishbone.step3': '3. Ladda ner PDF',
        'modules.customize.summary.leave-dialog.header': 'Vill du lämna anpassat intyg?',
        'modules.customize.summary.leave-dialog.body': 'Observera att ditt anpassade intyg inte sparas i Mina intyg efter att du navigerat till en annan sida. Se därför till att du har laddat ner intyget, innan du lämnar sidan.',
        'modules.customize.summary.leave-dialog.button.confirm': 'Ja, lämna anpassat intyg',
        'modules.customize.summary.leave-dialog.button.cancel': 'Nej, stanna kvar',

        'modules.button.alt.archive': 'Arkivera intyget. Flyttar intyget till Arkiverade intyg',
        'modules.button.save.as.pdf': 'Spara som PDF',
        'modules.link.label.save.as.pdf': 'Spara som PDF',

        'info.loadingcertificate': 'Hämtar intyget...',

        // Common errors
        'common.error.unknown': '<strong>Tekniskt fel</strong>'
    },
    'en': {
        'common.ok': 'OK',
        'common.cancel': 'Cancel'
    }
});
