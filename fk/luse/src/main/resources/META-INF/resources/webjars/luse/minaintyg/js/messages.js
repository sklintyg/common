/* jshint maxlen: false */
angular.module('luse').constant('luse.messages', {
    'sv': {
        'luse.sent.button.backtocertificate': 'Tillbaka till intyget',

        'luse.history.label.pagetitle': 'Intygets alla händelser',

        'luse.status.sent': 'Mottaget av',

        'luse.target.fk': 'Försäkringskassan',
        'luse.target.afa': 'AFA Försäkring',
        'luse.target.skandia': 'Skandia',

        'luse.inbox.complementaryinfo': 'Intygsperiod',
        'luse.compact-header.complementaryinfo-prefix': 'Avser diagnos:',

        'luse.button.send': 'Skicka',
        'luse.button.cancel': 'Avbryt',
        'luse.button.goback': 'Tillbaka',
        'luse.button.next': 'Nästa',
        'luse.button.previous': 'Föregående steg',
        'luse.button.send.certificate': 'Skicka',
        'luse.button.send.certificate.title': 'Skicka detta intyg till en eller flera mottagare.',

        // Labels
        'luse.label.latestevent': 'Senaste händelse',
        'luse.label.latestevent.noevents': 'Inga händelser',

        'luse.label.pagetitle': 'Granska och skicka intyg',
        'luse.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Försäkringskassan, du kan även spara intyget som en PDF på din dator.<br>',
        'luse.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'luse.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Försäkringskassan eller en annan mottagare.<br>',
        'luse.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'luse.label.pagedescription.error.in.certificate': 'För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',
        'luse.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'luse.label.pagedescription.archive': 'För att arkivera intyget klickar du på knappen Arkivera.',

        'luse.label.status.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'luse.label.status.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',
        'luse.label.showevents.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa färre händelser',
        'luse.label.showevents.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa alla händelser',

        'luse.label.patientname': 'Patientens namn',
        'luse.label.issued': 'Inkom till Mina intyg',
        'luse.label.civicnumber': 'Personnummer',
        'luse.label.issuer': 'Vårdgivare',
        'luse.label.period': 'Period',
        'luse.label.unit': 'Enhet',
        'luse.label.yes': 'Ja',
        'luse.label.no': 'Nej',

        'luse.label.confirmedby': 'Ovanstående uppgifter och bedömningar bekräftas',
        'luse.label.date': 'Datum',
        'luse.label.contactinfo': 'Namn och kontaktuppgifter till vårdenheten',

        'luse.message.certificateloading': 'Hämtar intyg...',

        'luse.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',

        'luse.common.cancel': 'Avbryt',

        'luse.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luse.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luse.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
        'luse.label.pagetitle': 'Show Certificate'
    }
});
