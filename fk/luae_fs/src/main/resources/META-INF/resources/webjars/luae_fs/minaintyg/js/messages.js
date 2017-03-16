/* jshint maxlen: false */

angular.module('luae_fs').constant('luae_fs.messages', {
    'sv': {
        'luae_fs.history.label.pagetitle': 'Intygets alla händelser',

        'luae_fs.status.sent': 'Mottaget av',

        'luae_fs.target.fk': 'Försäkringskassan',
        'luae_fs.target.afa': 'AFA Försäkring',
        'luae_fs.target.skandia': 'Skandia',

        'luae_fs.inbox.complementaryinfo': 'Intygsperiod',

        'luae_fs.button.send': 'Skicka',
        'luae_fs.button.send.certificate': 'Skicka',
        'luae_fs.button.send.certificate.title': 'Skicka detta intyg till en eller flera mottagare.',
        'luae_fs.button.cancel': 'Avbryt',
        'luae_fs.button.goback': 'Tillbaka',
        'luae_fs.button.next': 'Nästa',
        'luae_fs.button.previous': 'Föregående steg',

        'luae_fs.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',

        // Labels
        'luae_fs.label.latestevent': 'Senaste händelse',
        'luae_fs.label.latestevent.noevents': 'Inga händelser',
        'luae_fs.label.pagetitle': 'Granska och skicka intyg',
        'luae_fs.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Försäkringskassan, du kan även spara intyget som en PDF på din dator.<br>',
        'luae_fs.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'luae_fs.label.pagedescription.archive': 'För att arkivera intyget klickar du på knappen Arkivera.',
        'luae_fs.label.pagedescription.certificate.to.employer.header': '<h2>Intyg till arbetsgivare</h2>',
        'luae_fs.label.pagedescription.certificate.to.employer': 'Du har möjlighet att anpassa läkarintyget om du ska lämna läkarintyget till din arbetsgivare. Du anpassar intyget genom att välja om du vill visa alla uppgifter i intyget eller om du vill dölja vissa delar. Klicka på knappen Anpassa intyget för att välja vilken information du vill dela.',
        'luae_fs.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'luae_fs.label.pagedescription.error.in.certificate': 'För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',
        'luae_fs.label.pagedescription.save.or.print.certificate.header': '<h2>Spara eller skriva ut</h2>',
        'luae_fs.label.pagedescription.save.or.print.certificate': 'Klicka på knappen Spara som PDF för att spara eller skriva ut ditt intyg. Intyget öppnas eller laddas ner som en pdf-fil som du kan spara på din dator eller skriva ut.',
        'luae_fs.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'luae_fs.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Försäkringskassan eller en annan mottagare.<br>',
        'luae_fs.label.pagedescription.sickness.benefit.header': '<h2>Vill du ansöka om luae_fs?</h2>',
        'luae_fs.label.pagedescription.sickness.benefit': 'Det gör du enklast på <a href="http://www.forsakringskassan.se/sjuk" target="_blank">www.forsakringskassan.se/sjuk</a>. Där kan du läsa mer om luae_fs och hur du ansöker.<br>',

        'luae_fs.label.status.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'luae_fs.label.status.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',
        'luae_fs.label.showevents.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa färre händelser',
        'luae_fs.label.showevents.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa alla händelser',

        'luae_fs.label.patientname': 'Patientens namn',
        'luae_fs.label.issued': 'Inkom till Mina intyg',
        'luae_fs.label.civicnumber': 'Personnummer',
        'luae_fs.label.issuer': 'Vårdgivare',
        'luae_fs.label.period': 'Period',
        'luae_fs.label.unit': 'Enhet',
        'luae_fs.label.yes': 'Ja',
        'luae_fs.label.no': 'Nej',
        'luae_fs.label.otherinformation': 'Övriga upplysningar och förtydliganden',
        'luae_fs.label.confirmedby': 'Ovanstående uppgifter och bedömningar bekräftas',
        'luae_fs.label.date': 'Datum',
        'luae_fs.label.contactinfo': 'Namn och kontaktuppgifter till vårdenheten',

        'luae_fs.message.certificateloading': 'Hämtar intyg...',

        'luae_fs.common.cancel': 'Avbryt',

        'luae_fs.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_fs.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'luae_fs.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
        'luae_fs.label.pagetitle': 'Show Certificate'
    }
});
