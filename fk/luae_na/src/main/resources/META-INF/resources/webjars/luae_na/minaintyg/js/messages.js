/* jshint maxlen: false */
angular.module('luae_na').constant('luae_na.messages', {
    'sv': {

        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.
        'luae_na.inbox.complementaryinfo': 'Intygsperiod',
        'luae_na.compact-header.complementaryinfo-prefix': 'Avser diagnos:',

        'luae_na.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_na.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_na.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
    }
});
