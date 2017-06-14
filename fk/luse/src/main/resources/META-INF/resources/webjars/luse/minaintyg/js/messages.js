/* jshint maxlen: false */
angular.module('luse').constant('luse.messages', {
    'sv': {

        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.
        'luse.inbox.complementaryinfo': 'Intygsperiod',
        'luse.compact-header.complementaryinfo-prefix': 'Avser diagnos:',

        'luse.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luse.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luse.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
    }
});
