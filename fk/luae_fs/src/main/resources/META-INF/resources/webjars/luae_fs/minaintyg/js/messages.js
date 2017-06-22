/* jshint maxlen: false */

angular.module('luae_fs').constant('luae_fs.messages', {
    'sv': {


        // Labels
        'luae_fs.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektronisk, ladda ned intyget som en PDF och arkivera intyget. För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',

        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.
        'luae_fs.inbox.complementaryinfo': 'Intygsperiod',
        'luae_fs.compact-header.complementaryinfo-prefix': 'Avser diagnos:',

        'luae_fs.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/omminaintyg/help-info">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_fs.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_fs.error.certnotfound': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/omminaintyg/help-info">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
        'luae_fs.label.pagetitle': 'Show Certificate'
    }
});
