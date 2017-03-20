/* jshint maxlen: false */
angular.module('luae_na').constant('luae_na.messages', {
    'sv': {
        'luae_na.sent.button.backtocertificate': 'Tillbaka till intyget',
        'luae_na.label.latestevent': 'Senaste händelse',
        'luae_na.label.latestevent.noevents': 'Inga händelser',
        'luae_na.history.label.pagetitle': 'Intygets alla händelser',
        'luae_na.status.sent': 'Mottaget av',
        'luae_na.target.fk': 'Försäkringskassan',
        'luae_na.target.afa': 'AFA Försäkring',
        'luae_na.target.skandia': 'Skandia',

        'luae_na.inbox.complementaryinfo': 'Intygsperiod',
        'luae_na.compact-header.complementaryinfo-prefix': 'Avser diagnos:',

        'luae_na.button.send.certificate': 'Skicka',
        'luae_na.button.send.certificate.title': 'Skicka detta intyg till en eller flera mottagare.',

        // Labels
        'luae_na.label.pagetitle': 'Granska och skicka intyg',
        'luae_na.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Försäkringskassan, du kan även spara intyget som en PDF på din dator.<br>',
        'luae_na.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'luae_na.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Försäkringskassan eller en annan mottagare.<br>',
        'luae_na.label.pagedescription.certificate.to.employer.header': '<h2>Intyg till arbetsgivare</h2>',
        'luae_na.label.pagedescription.certificate.to.employer': 'Du har möjlighet att anpassa läkarintyget om du ska lämna läkarintyget till din arbetsgivare. Du anpassar intyget genom att välja om du vill visa alla uppgifter i intyget eller om du vill dölja vissa delar. Klicka på knappen Anpassa intyget för att välja vilken information du vill dela.',
        'luae_na.label.pagedescription.save.or.print.certificate.header': '<h2>Spara eller skriva ut</h2>',
        'luae_na.label.pagedescription.save.or.print.certificate': 'Klicka på knappen Spara som PDF för att spara eller skriva ut ditt intyg. Intyget öppnas eller laddas ner som en pdf-fil som du kan spara på din dator eller skriva ut.',
        'luae_na.label.pagedescription.sickness.benefit.header': '<h2>Vill du ansöka om luae_na?</h2>',
        'luae_na.label.pagedescription.sickness.benefit': 'Det gör du enklast på <a href="http://www.forsakringskassan.se/sjuk" target="_blank">www.forsakringskassan.se/sjuk</a>. Där kan du läsa mer om luae_na och hur du ansöker.<br>',
        'luae_na.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'luae_na.label.pagedescription.error.in.certificate': 'För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',
        'luae_na.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'luae_na.label.pagedescription.archive': 'För att arkivera intyget klickar du på knappen Arkivera.',

        'luae_na.label.status.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'luae_na.label.status.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',
        'luae_na.label.showevents.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa färre händelser',
        'luae_na.label.showevents.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa alla händelser',

        'luae_na.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',

        'luae_na.label.patientname': 'Patientens namn',
        'luae_na.label.issued': 'Inkom till Mina intyg',
        'luae_na.label.civicnumber': 'Personnummer',
        'luae_na.label.issuer': 'Vårdgivare',
        'luae_na.label.period': 'Period',
        'luae_na.label.unit': 'Enhet',
        'luae_na.label.yes': 'Ja',
        'luae_na.label.no': 'Nej',

        'luae_na.label.confirmedby': 'Ovanstående uppgifter och bedömningar bekräftas',
        'luae_na.label.date': 'Datum',
        'luae_na.label.contactinfo': 'Namn och kontaktuppgifter till vårdenheten',

        'luae_na.message.certificateloading': 'Hämtar intyg...',
        'luae_na.button.send': 'Skicka',
        'luae_na.button.cancel': 'Avbryt',
        'luae_na.button.goback': 'Tillbaka',
        'luae_na.button.next': 'Nästa',
        'luae_na.button.previous': 'Föregående steg',

        'luae_na.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_na.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_na.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
        'luae_na.label.pagetitle': 'Show Certificate'
    }
});
