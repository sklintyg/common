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
        'common.notset': 'Ej ifyllt',
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
