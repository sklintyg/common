/* jshint maxlen: false */

angular.module('db').constant('db.messages', {
    'sv': {
        //Validation messages
        'db.validation.explosivAvlagsnat.explosivImplantatFalse': 'Den valda kombinationen är ogiltig',

        'db.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Invånaren kommer på Mina intyg, som nås via 1177.se, se att intyget är ersatt. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',

        'db.fornya.tooltip': 'Om du vill förlänga en sjukskrivning kan du förnya intyget. Viss information från det befintliga intyget följer med till det nya utkastet.',
        'db.modal.fornya.text': 'Förnya intyg kan användas vid förlängning av en sjukskrivning. När ett intyg förnyas skapas ett nytt intygsutkast med viss information från det ursprungliga intyget. Uppgifterna i det nya intygsutkastet går att ändra innan det signeras. Om intyget är utfärdat på en annan vårdenhet kommer det nya utkastet utfärdas på den enhet du är inloggad på. I de fall patienten har ändrat namn eller adress så uppdateras den informationen.<br><br>' +
        'De uppgifter som inte kommer med till det nya utkastet är:<br><br>' +
        '<ul><li>Information om sjukskrivningsperiod.</li>' +
        '<li>Valet om man vill ha kontakt med försäkringskassan.</li>' +
        '<li>Referenser som intyget baseras på.</li></ul>',

        'db.info.polisanmalan': 'Skriv även ut dödsbeviset och skicka det till polisen per post/fax.',

        'db.label.status.signed': '<p>Dödsbeviset är signerat och har nu skickats till Skatteverket.</p>'+
            '<p>Glöm inte att göra en journalanteckning att dödsbevis är inlämnat!</p>'+
            '<p>Du kan nu avsluta intygstjänsten eller direkt skriva ett dödsorsaksintyg för samma person &lt;Knapp&gt;</p>'
    }
});
