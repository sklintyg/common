/* jshint maxlen: false */
angular.module('luae_fs').constant('luae_fs.messages', {
    'sv': {
        'luae_fs.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Invånaren kommer på Mina intyg, som nås via 1177.se, se att intyget är ersatt. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
            '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',

        'luae_fs.fornya.tooltip': 'En kopia av det tidigare intyget skapas. Samtlig information följer med till det nya intygsutkastet. Informationen kan sedan ändras.',
        'luae_fs.modal.fornya.text': 'Förnya intyg innebär att ett nytt intygsutkast skapas med samma information som i det ursprungliga intyget. Uppgifterna i ett intygsutkastet går att ändra innan det signeras. Om intyget är utfärdat på en annan vårdenhet kommer det nya utkastet utfärdas på den enhet du är inloggad på. I de fall patienten har ändrat namn eller adress så uppdateras den informationen.'
    },
    'en': {
        'luae_fs.label.pagetitle': 'Show Certificate'
    }
});
