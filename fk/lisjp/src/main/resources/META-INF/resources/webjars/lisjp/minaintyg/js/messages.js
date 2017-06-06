/* jshint maxlen: false */
angular.module('lisjp').constant('lisjp.messages', {
    'sv': {
        'lisjp.button.customize.certificate': 'Anpassa intyget till arbetsgivare',
        'lisjp.button.customize.certificate.title': 'Anpassa detta intyg för att lämna till arbetsgivaren.',
        // Anpassat intyg
        'lisjp.customize.step.1.pagetitle': 'Anpassa intygsinformation till din arbetsgivare - steg 1 av 3',
        'lisjp.customize.step.1.pageingress.left': 'Här kan du skapa ett anpassat intyg till din arbetsgivare genom att välja vilken information du vill ta med och inte. Viss information måste lämnas till arbetsgivaren och kan inte väljas bort. I de flesta fall är du inte skyldig att lämna mer information än den obligatoriska, men om ni har ett kollektivavtal kan det finnas andra regler för vilken information som måste tas med.',
        'lisjp.customize.step.1.pageingress.right': ' Det finns ofta fördelar med att arbetsgivaren får ta del även av frivillig information. Det ger arbetsgivaren bättre möjlighet att anpassa din arbetsplats eller arbetssituation. Du kan alltid återvända till Mina intyg vid senare tillfälle för att skapa ett nytt anpassat intyg med mer information.<br>För att välja bort information bockar du ur alternativet <i>Inkludera i anpassat intyg</i> i det fält du inte vill ska synas.',
        'lisjp.customize.step.1.next': 'Gå till steg 2: Granska dina val',

        'lisjp.customize.step.2.pagetitle': 'Anpassa intygsinformation till din arbetsgivare - steg 2 av 3',
        'lisjp.customize.step.2.pageingress.left': 'Var god kontrollera dina val. Informationen som visas nedan är den information ditt intyg till din arbetsgivare kommer att innehålla. Bekräfta sedan dina val.',
        'lisjp.customize.step.2.pageingress.right': 'Du kan alltid återvända till Mina intyg vid ett senare tillfälle för att skapa ett nytt anpassat intyg.',
        'lisjp.customize.step.2.next': 'Gå till steg 3: Ladda ner PDF',

        'lisjp.customize.step.3.pagetitle': 'Ladda ner PDF - steg 3 av 3',
        'lisjp.customize.step.3.pageingress.left': 'Här kan du ladda ner ditt anpassade intyg som en PDF, för att sedan kunna skriva ut det eller mejla till din arbetsgivare. ',
        'lisjp.customize.step.3.pageingress.right': '',
        'lisjp.customize.step.3.info.fk': 'Intyget <u>skall inte skickas</u> till Försäkringskassan!',
        'lisjp.customize.step.3.download': 'Ladda ner som PDF',


        'lisjp.customize.change': 'Ändra dina val',

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
        'lisjp.compact-header.complementaryinfo-prefix': 'Gäller intygsperiod:',

        'lisjp.button.send.certificate': 'Skicka',
        'lisjp.button.send.certificate.title': 'Skicka detta intyg till en eller flera mottagare.',

        // Labels
        'lisjp.label.pagetitle': 'Granska och skicka intyg',

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
        'lisjp.error.certnotfound': 'Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>. Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>',
        'lisjp.error.couldnotarchivecert': 'Intyget kunde inte arkiveras'
    },
    'en': {
        'lisjp.label.pagetitle': 'Show Certificate'
    }
});
