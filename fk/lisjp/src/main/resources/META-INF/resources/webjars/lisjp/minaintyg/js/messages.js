/* jshint maxlen: false */
angular.module('lisjp').constant('lisjp.messages', {
    'sv': {
        'lisjp.button.customize.certificate': 'Anpassa intyget till arbetsgivare',
        'lisjp.button.customize.certificate.title': 'Anpassa detta intyg för att lämna till arbetsgivaren.',
        // Anpassat intyg
        'lisjp.customize.step.1.pagetitle': 'Anpassa intygsinformation till din arbetsgivare - steg 1 av 2',
        'lisjp.customize.step.1.pageingress.left': '<p><b>Här kan du skapa ett anpassat intyg till din arbetsgivare genom att välja vilken information du vill ta med och inte. Viss information måste lämnas till arbetsgivaren och kan inte väljas bort.</b></p><p>I de flesta fall är du inte skyldig att lämna mer information än den obligatoriska, men om ni har ett kollektivavtal kan det finnas andra regler för vilken information som måste tas med.</p>',
        'lisjp.customize.step.1.pageingress.right': '<p>Det finns ofta fördelar med att arbetsgivaren får ta del även av frivillig information. Det ger arbetsgivaren bättre möjlighet att anpassa din arbetsplats eller arbetssituation. Du kan alltid återvända till Mina intyg vid senare tillfälle för att skapa ett nytt anpassat intyg med mer information.</p><p>Du kan alltid återvända till Mina intyg vid senare tillfälle för att skapa ett nytt anpassat intyg med mer information.</p><p>För att välja bort information bockar du ur alternativet <i>Inkludera i anpassat intyg</i> i det fält du inte vill ska synas.</p>',
        'lisjp.customize.step.1.fishbone': 'Anpassa intyg',
        'lisjp.customize.step.1.next': 'Granska dina val',

        'lisjp.customize.step.2.pagetitle': 'Anpassa intygsinformation till din arbetsgivare - steg 2 av 2',
        'lisjp.customize.step.2.pageingress': '<p>Om du vill spara ditt anpassade intyg som PDF klickar du på knappen Spara som PDF.</p><p>Om du märker att du har valt bort ett fält som du vill ha med, eller tvärt om, klickar du på Ändra val.</p>',
        'lisjp.customize.step.2.fishbone': 'Sammanfattning',
        'lisjp.customize.step.2.info.fk': 'Tänk på att det anpassade intyget <u>inte</u> ska skickas till Försäkringskassan.',
        'lisjp.customize.step.2.info.pdf': 'När du sett över dina val klickar du på knappen ',

        'lisjp.customize.label.mandatory': 'Obligatoriskt fält',
        'lisjp.customize.label.optional': 'Inkludera i anpassat intyg',
        'lisjp.customize.message.warning': 'Informationen i fältet är frivillig, men informationen kan underlätta arbetsgivarens möjlighet att göra arbetsanpassningar.',
        'lisjp.button.customize.certificate.change': 'Ändra val',

        'lisjp.customize.summary.leave.header':'Vill du lämna anpassat intyg?',
        'lisjp.customize.summary.leave': 'Observera att ditt anpassade intyg inte sparas i Mina intyg efter att du navigerat till en annan sida. Se därför till att du har laddat ner det till din dator innan du lämnar sidan.',
        'lisjp.customize.summary.leave.yes': 'Ja, lämna anpassat intyg',
        'lisjp.customize.summary.leave.no': 'Nej, stanna kvar',


        'lisjp.label.latestevent': 'Senaste händelse',
        'lisjp.label.latestevent.noevents': 'Inga händelser',
        'lisjp.history.label.pagetitle': 'Intygets alla händelser',
        'lisjp.status.sent': 'Mottaget av',
        'lisjp.target.fk': 'Försäkringskassan',
        'lisjp.target.afa': 'AFA Försäkring',
        'lisjp.target.skandia': 'Skandia',

        'lisjp.inbox.complementaryinfo': 'Intygsperiod',

        'lisjp.button.send.certificate': 'Skicka',
        'lisjp.button.send.certificate.title': 'Skicka detta intyg till en eller flera mottagare.',

        // Labels
        'lisjp.label.pagetitle': 'Granska och skicka intyg',
        'lisjp.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget elektroniskt till Försäkringskassan, du kan även spara intyget som en PDF på din dator.<br>',
        'lisjp.label.pagedescription.sendcertificate.header': '<h2>Skicka intyg</h2>',
        'lisjp.label.pagedescription.sendcertificate': 'Klicka på knappen Skicka för att skicka intyget elektroniskt till Försäkringskassan eller en annan mottagare.<br>',
        'lisjp.label.pagedescription.certificate.to.employer.header': '<h2>Intyg till arbetsgivare</h2>',
        'lisjp.label.pagedescription.certificate.to.employer': 'Du har möjlighet att anpassa läkarintyget om du ska lämna läkarintyget till din arbetsgivare. Du anpassar intyget genom att välja om du vill visa alla uppgifter i intyget eller om du vill dölja vissa delar. Klicka på knappen Anpassa intyget för att välja vilken information du vill dela.',
        'lisjp.label.pagedescription.save.or.print.certificate.header': '<h2>Spara eller skriva ut</h2>',
        'lisjp.label.pagedescription.save.or.print.certificate': 'Klicka på knappen Spara som PDF för att spara eller skriva ut ditt intyg. Intyget öppnas eller laddas ner som en pdf-fil som du kan spara på din dator eller skriva ut.',
        'lisjp.label.pagedescription.sickness.benefit.header': '<h2>Vill du ansöka om sjukpenning?</h2>',
        'lisjp.label.pagedescription.sickness.benefit': 'Det gör du enklast på <a href="http://www.forsakringskassan.se/sjuk" target="_blank">www.forsakringskassan.se/sjuk</a>. Där kan du läsa mer om sjukpenning och hur du ansöker.<br>',
        'lisjp.label.pagedescription.error.in.certificate.header': '<h2>Fel i intyget?</h2>',
        'lisjp.label.pagedescription.error.in.certificate': 'För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',
        'lisjp.label.pagedescription.archive.header': '<h2>Arkivera</h2>',
        'lisjp.label.pagedescription.archive': 'För att arkivera intyget klickar du på knappen Arkivera.',

        'lisjp.label.status.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa mindre',
        'lisjp.label.status.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa mer',
        'lisjp.label.showevents.false': '<span class="glyphicon glyphicon-chevron-up"></span> Visa färre händelser',
        'lisjp.label.showevents.true': '<span class="glyphicon glyphicon-chevron-down"></span> Visa alla händelser',

        'lisjp.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',
        'lisjp.label.patientname': 'Patientens namn',
        'lisjp.label.issued': 'Inkom till Mina intyg',
        'lisjp.label.civicnumber': 'Personnummer',
        'lisjp.label.issuer': 'Utfärdare',
        'lisjp.label.period': 'Period',
        'lisjp.label.unit': 'Enhet',
        'lisjp.label.yes': 'Ja',
        'lisjp.label.no': 'Nej',

        'lisjp.label.confirmedby': 'Ovanstående uppgifter och bedömningar bekräftas',
        'lisjp.label.date': 'Datum',
        'lisjp.label.contactinfo': 'Namn och kontaktuppgifter till vårdenheten',

        'lisjp.message.certificateloading': 'Hämtar intyg...',
        'lisjp.button.send': 'Skicka',
        'lisjp.button.cancel': 'Avbryt',
        'lisjp.button.goback': 'Tillbaka',
        'lisjp.button.next': 'Nästa',
        'lisjp.button.previous': 'Föregående steg',

        'lisjp.vardkontakt.5880005': 'Min undersökning av patienten den %0',
        'lisjp.vardkontakt.undersokning': 'Min undersökning av patienten den ',
        'lisjp.vardkontakt.185317003': 'Min telefonkontakt med patienten den %0',
        'lisjp.vardkontakt.telefonkontakt': 'Min telefonkontakt med patienten den ',
        'lisjp.referens.419891008': 'Journaluppgifter, den %0',
        'lisjp.referens.journal': 'Journaluppgifter, den ',
        'lisjp.referens.kannedomompatient': 'Kännedom om patient, den ',
        'lisjp.referens.74964007': 'Annat, den %0',
        'lisjp.referens.annat': 'Annat, den ',
        'lisjp.common.cancel': 'Avbryt',

        'lisjp.error.generic': 'Intyget kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'lisjp.error.couldnotsend': 'Intyget kunde inte skickas till Försäkringskassan. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'lisjp.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>'
    },
    'en': {
        'lisjp.label.pagetitle': 'Show Certificate'
    }
});
