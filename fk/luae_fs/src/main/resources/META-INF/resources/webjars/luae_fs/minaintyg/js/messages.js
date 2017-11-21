/* jshint maxlen: false */

angular.module('luae_fs').constant('luae_fs.messages', {
    'sv': {


        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.
        'luae_fs.inbox.complementaryinfo': 'Intygsperiod',
        'luae_fs.compact-header.complementaryinfo-prefix': 'Avser diagnos:',
        'luae_fs.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektronisk, ladda ned intyget som en PDF och arkivera intyget. För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',
        'luae_fs.label.pageingress.ersatt': 'Här visas hela ditt ersatta läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev ersatt med. Klicka på knappen Arkivera intyg för att flytta intyget till Arkiverade intyg. Du kan när som helst återställa intyget igen.'

    },
    'en': {
        'luae_fs.label.pagetitle': 'Show Certificate'
    }
});
