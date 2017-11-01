/* jshint maxlen: false */

angular.module('db').constant('db.messages', {
    'sv': {
        //Validation messages
        'db.validation.explosivAvlagsnat.explosivImplantatFalse': 'Den valda kombinationen är ogiltig',
        'db.validation.undersokningDatum.after.dodsdatum': 'Undersökningsdatum får inte infalla efter dödsdatum',

        'db.makulera.body.common-header': '<p>Ett dödsbevis som är inskickat på fel person kan makuleras. Genom att trycka på "Makulera" makulerar du dödsbeviset i Webcert, med detta kommer inte återkalla dödsbeviset hos Skatteverket.</p>Förutom att trycka på "Makulera" måste du omedelbart ta kontakt med Skatteverket så att felet kan rättas fort. Du tar kontakt med Skatteverket genom att ringa till Skatteupplysningen på telefon 0771-567 567 och ange "folkbokföring - dödsfall".',
        'db.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient ska du istället makulera intyget.</p>',

        'db.info.barn.forced.true': 'Det angivna dödsdatumet infaller inom 28 dagar efter barnets födelsedatum. Fältet har därför förifyllts.',
        'db.info.barn.forced.false': 'Det angivna dödsdatumet infaller inte inom 28 dagar efter barnets födelsedatum. Fältet har därför förifyllts.',
        'db.info.polisanmalan': 'Skriv även ut dödsbeviset och skicka det till polisen per post/fax.',

        'db.label.signandsend': 'Om du går vidare kommer dödsbeviset signeras och skickas direkt till Skatteverkets system.',

        'db.label.status.recieved': '<p>Dödsbeviset är signerat och har nu skickats till Skatteverket.</p>'+
            '<p>Glöm inte att göra en journalanteckning att dödsbevis är inlämnat!</p>'+
            '<p>Du kan nu avsluta Webcert eller direkt skriva ett dödsorsaksintyg för samma person genom att trycka på knappen "Skriv dödsorsaksintyg" ovan.</p>',

        'db.warn.previouscertificate.samevg': 'Tidigare dödsbevis finns för detta personnummer. Senast skapade dödsbevis är det som gäller. Om du ersätter det tidigare dödsbeviset och lämnar in det nya så blir det därför detta dödsbevis som gäller.',
        'db.warn.previouscertificate.differentvg': 'Tidigare dödsbevis finns för detta personnummer. Senast skapade dödsbevis är det som gäller. Om du fortsätter och lämnar in dödsbeviset så blir det därför detta dödsbevis som gäller.'
    }
});
