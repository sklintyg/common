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

        // labels for common modal dialogs
        'common.modal.label.discard_draft' : 'Ta bort utkast',
        'common.modal.label.confirm_sign': 'Signera intyget',

        // cert status messages
        'cert.status.draft_incomplete': 'Utkast, ej komplett',
        'cert.status.draft_complete': 'Utkast, komplett',
        'cert.status.signed': 'Signerat',
        'cert.status.cancelled': 'Makulerat',
        'cert.status.unknown': 'Okänd',

        // unused statuses in gui
        'cert.status.sent': 'Skickat',
        'cert.status.received': 'Mottaget',

        // wc-common-directives-resources
        'nav.label.loggedinas': 'Inloggad som:',

        // Fraga/svar resource - used both by webcert and module
        // modal messages
        'modal.title.markforward': 'Markera som vidarebefordrad?',

        // Cert module messages. Used by several cert modules.
        'modules.label.field': 'Fält',
        'modules.label.blank': 'Ej ifyllt',

        // texts used by mi-certificate-action-buttons directive (would be nice to have resource keys per directive, like scss...)
        'modules.actionbar.button.send' : 'Välj mottagare och skicka intyget',
        'modules.actionbar.button.send.tooltip' : 'Skicka intyget elektroniskt, t.ex. till Försäkringskassan eller Transportstyrelsen.',
        'modules.actionbar.button.customizepdf' : 'Anpassa intyg till arbetsgivare',
        'modules.actionbar.button.customizepdf.tooltip' : 'Du kan välja bort intygsinformation du inte vill delge din arbetsgivare.',
        'modules.actionbar.button.print' : 'Ladda ner intyg som PDF',
        'modules.actionbar.button.print.tooltip' : 'Intyget sparas om PDF på din enhet.',
        'modules.actionbar.button.archive' : 'Arkivera intyg',
        'modules.actionbar.button.archive.tooltip' : 'Flytta intyget till Arkiverade intyg.',
        'modules.actionbar.archivedialog.title' : 'Arkivera intyg',
        'modules.actionbar.archivedialog.body' : 'När du väljer att arkivera intyget kommer det att flyttas till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',
        'modules.actionbar.archivedialog.archive.button' : 'Arkivera intyg',

        // Common texts used in page headers
        'modules.page-header.info.select-recipients-and-send': '<h3>Välj mottagare och skicka intyget</h3>Klicka på knappen för att komma till sidan där du väljer mottagare och skickar intyget. Detta intyg kan till exempel skickas till Försäkringskassan.',
        'modules.page-header.info.customize-pdf': '<h3>Anpassa intyg till arbetsgivare</h3>Klicka på knappen för att välja vilken information i intyget du vill delge din arbetsgivare. Viss information måste lämnas till arbetsgivaren och kan inte väljas bort. Dessa fält är markerade som obligatoriska.',
        'modules.page-header.info.download-pdf': '<h3>Ladda ner intyg som PDF</h3>Klicka på knappen för att ladda ner intyget som PDF. Du kan därefter välja hur du vill hantera intyget vidare, till exempel kan du skriva ut det. PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna PDF-filer behöver du en PDF läsare, exempelvis <LINK:adobeReader>.',
        'modules.page-header.info.archive': '<h3>Arkivera intyg</h3>Klicka på knappen för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',
        'modules.page-header.info.more.false':'<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'modules.page-header.info.more.true':'<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',
        'modules.view-intyg.end-of-intyg.text': 'Slut på intyget',



        //Common text in customized pdf wizard (lisjp and fk7263)
        'modules.customize.label.mandatory': 'Obligatoriskt fält',
        'modules.customize.label.optional': 'Inkludera i anpassat intyg',
        'modules.customize.message.warning': 'Dina val är frivilliga, men att dela med sig av information kan underlätta arbetsgivarens möjlighet att göra arbetsanpassningar för dig.',
        'modules.customize.remove-field.header': 'Vill du verkligen ta bort denna information?',
        'modules.customize.remove-field.body': 'Observera att informationen är frivillig, men att dina val kan försvåra arbetsgivarens möjligheter att göra arbetsanpassningar!',
        'modules.customize.remove-field.button.confirm': 'Ja, ta bort',
        'modules.customize.remove-field.button.cancel': 'Nej, ta inte bort',
        'modules.customize.fishbone.step1': 'Steg 1: Anpassa intyg',
        'modules.customize.fishbone.step2': 'Steg 2: Bekräfta anpassat intyg',
        'modules.customize.fishbone.step3': 'Steg 3: Ladda ner PDF',
        'modules.customize.summary.leave-dialog.header': 'Vill du lämna anpassat intyg?',
        'modules.customize.summary.leave-dialog.body': 'Observera att ditt anpassade intyg inte sparas i Mina intyg efter att du navigerat till en annan sida. Se därför till att du har laddat ner intyget, innan du lämnar sidan.',
        'modules.customize.summary.leave-dialog.button.confirm': 'Ja, lämna anpassat intyg. ',
        'modules.customize.summary.leave-dialog.button.cancel': 'Nej, stanna kvar.',

        'modules.button.alt.archive': 'Arkivera intyget. Flyttar intyget till Arkiverade intyg',
        'modules.button.save.as.pdf': 'Spara som PDF',
        'modules.link.label.save.as.pdf': 'Spara som PDF',

        'info.loadingcertificate': 'Hämtar intyget...',

        // Common errors
        'common.error.unknown': '<strong>Tekniskt fel</strong>',
        'common.error.cantconnect': 'Kunde inte kontakta servern',
        'common.error.certificatenotfound': 'Intyget finns inte',
        'common.error.certificateinvalid': 'Intyget är inte korrekt ifyllt',
        'common.error.data_not_found': '<strong>Intyget kunde inte hittas.</strong><br>Intyget är borttaget eller så saknas behörighet.'
    },
    'en': {
        'common.ok': 'OK',
        'common.cancel': 'Cancel'
    }
});
