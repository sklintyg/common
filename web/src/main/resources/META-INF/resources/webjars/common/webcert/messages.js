/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* jshint maxlen: false */
angular.module('common').constant('common.messages', {
    'sv': {

        'common.continue': 'Fortsätt',
        'common.yes': 'Ja',
        'common.no': 'Nej',
        'common.yes.caps': 'JA',
        'common.no.caps': 'NEJ',
        'common.nodontask': 'Nej, fråga inte igen',
        'common.ok': 'OK',
        'common.cancel': 'Avbryt',
        'common.canceldontrevoke': 'Avbryt, makulera ej',
        'common.goback': 'Tillbaka',
        'common.revoke': 'Makulera',
        'common.sign': 'Signera intyget',
        'common.signsend': 'Signera och skicka',
        'common.send': 'Skicka',
        'common.copy': 'Kopiera',
        'common.copy.tooltip': 'Ett nytt utkast skapas på den vårdenhet du är inloggad på. All information i det befintliga intyget följer med till utkastet.',
        'common.fornya': 'Förnya',
        'common.fornya.tooltip': 'Om du vill förlänga en sjukskrivning kan du förnya intyget. Viss information från det befintliga intyget följer med till det nya utkastet.',
        'common.ersatt': 'Ersätt',
        'common.ersatt.resume': 'Fortsätt på utkast',
        'common.ersatt.cancel': 'Avbryt, ersätt ej',
        'common.ersatt.tooltip': 'Om intyget innehåller felaktiga uppgifter, eller om ny information inkommit, kan du ersätta ett intyg. All information i det befintliga intyget följer med till utkastet.',
        'common.show': 'Visa',
        'common.show.tooltip': 'Visa intyget.',
        'common.delete': 'Radera',
        'common.sign.intyg': 'Signera intyget',
        'common.button.save.as.pdf': 'Skriv ut',
        'common.button.save.as.pdf.full': 'Fullständigt intyg',
        'common.button.save.as.pdf.mininmal': 'Minimalt intyg',
        'common.button.save.as.pdf.mininmal.title': 'Minimalt intyg enligt sjuklönelagens krav på intyg som kan lämnas till arbetsgivare.',

        'common.alert.newpersonid': 'Patienten har ett nytt personnummer: <b>${person}</b>',
        'common.alert.newreserveid': 'Patienten har samordningsnummer kopplat till reservnummer: <b>${reserve}</b>. Om ett intyg skapas utifrån detta intyg kommer det nya intyget skrivas på samordningsnumret.',
        'common.alert.sekretessmarkering.print': '<strong>OBS!</strong> Patienten har en sekretessmarkering. Hantera utskriften varsamt.',
        'common.alert.textversionupdated': 'Observera att frågetexterna har uppdaterats',
        'common.warning.patientdataupdate.failed': '<b>Observera!</b> Misslyckades att slå upp patienten i personuppgiftstjänsten',

        'common.date': 'Datum',
        'common.when': 'När?',

        'common.notset': 'Ej angivet',

        // makulera
        'label.makulera': 'Makulera intyg',
        'label.makulera.body.common-header': 'Intyg som innehåller allvarliga fel kan makuleras. Exempel på ett allvarligt fel är om intyget är utfärdat på fel patient. Om intyget har skickats elektroniskt till en mottagare kommer mottagaren att informeras om makuleringen. Invånaren kommer på Mina intyg, som nås via minaintyg.se, inte längre se intyget.',
        'label.makulera.body.has-arenden.addition': 'Om du går vidare och makulerar intyget kommer dina ej hanterade frågor och svar markeras som hanterade.',
        'label.makulera.body.common-footer': '<b>Notera</b>: Om du fått ny information om patienten eller av annan anledning behöver korrigera innehållet i intyget, ska det inte makuleras. Du bör då ersätta intyget med ett nytt genom att använda dig av funktionen "Ersätt" och korrigera eller lägga till de uppgifter du önskar.',

        'label.makulera.confirmation': 'Kvittens - Återtaget intyg',

        // avtal/terms
        'avtal.title.text': 'Godkännande av användarvillkor',
        'avtal.approve.label': 'Jag godkänner villkoren',
        'avtal.print.label': 'Skriv ut',
        'avtal.logout.label': 'Logga ut',

        // labels for common modal dialogs
        'common.title.sign': 'Signera intyget',

        'common.modal.label.discard_draft': 'Ta bort utkast',
        'common.modal.label.confirm_sign': 'Signera intyget',

        'common.modal.label.signing': 'Ditt intyg signeras.',
        'common.modal.label.signing.seconds': 'Detta kan ta några sekunder.',
        'common.modal.label.signing.open.bankid': 'Om ditt BankID säkerhetsprogram inte öppnas automatiskt kan du behöva starta det själv.',
        'common.modal.label.signing.open.mobilt.bankid': 'Starta mobilt BankID på den enhet du har det installerat.',
        'common.modal.label.signing.mobilt.noclient': 'Mobilt BankID-servern får ej kontakt med din enhet. Kontrollera att du har startat Mobilt BankID på din enhet.',
        'common.modal.label.signing.bankid.noclient': 'BankID-servern får ej kontakt med ditt BankID säkerhetsprogram. Kontrollera att du har startat BankID säkerhetsprogram på din dator.',

        'common.modal.label.print.sekretessmarkerad.title': 'Skriv ut intyg',
        'common.modal.label.print.sekretessmarkerad.yes':  'Skriv ut',
        'common.modal.label.employee.title': 'Skriv ut minimalt intyg',
        'common.modal.label.employee.yes': 'Skriv ut minimalt intyg',
        'common.modal.label.employee.body': 'Ett minimalt intyg innehåller endast de uppgifter som enligt sjuklönelagen är obligatoriska att delge arbetsgivaren. Observera att det minimala intyget kan ge arbetsgivaren sämre förutsättningar att stödja patientens rehabilitering och göra nödvändiga arbetsanpassningar.<br><br>Sveriges kommuner och landsting rekommenderar att patienten skickar in ett fullständigt läkarintyg till arbetsgivaren, alternativt skapar ett anpassat intyg på <LINK:minaintyg>',

        // Text in copy and fornya (renew) modal dialogs
        'common.modal.copy.text': 'Vid kopiering skapas ett nytt intygsutkast med samma information som i det ursprungliga intyget. Uppgifterna i ett kopierat intygsutkast går att ändra innan det signeras. Om intyget är utfärdat på en annan vårdenhet kommer det nya utkastet utfärdas på den enhet du är inloggad på. I de fall patienten har ändrat namn eller adress så uppdateras den informationen.',

        'common.modal.fornya.text': 'Förnya intyg kan användas vid förlängning av en sjukskrivning. När ett intyg förnyas skapas ett nytt intygsutkast med viss information från det ursprungliga intyget. Uppgifterna i det nya intygsutkastet går att ändra innan det signeras. Om intyget är utfärdat på en annan vårdenhet kommer det nya utkastet utfärdas på den enhet du är inloggad på.<br><br>' +
            'De uppgifter som inte kommer med till det nya utkastet är:<br><br>' +
            '<ul><li>Information om sjukskrivningsperiod.</li>' +
            '<li>Valet om man vill ha kontakt med försäkringskassan.</li>' +
            '<li>Referenser som intyget baseras på.</li></ul>',

        'common.modal.marked.ready.notification.sent': 'Observera att utkastet saknar obligatoriska uppgifter. Om du inte kan fylla i mer information kan du ändå markera intyget som klart för signering. Läkaren kommer då behöva komplettera intyget med de saknade uppgifterna innan det går att signera.',

        // common intyg view messages
        'common.label.ovanstaende-har-bekraftats': 'Ovanstående uppgifter och bedömningar bekräftas',
        'common.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',

        // intyg status messages
        'intyg.status.has.komplettering': 'Försäkringskassan har begärt kompletteringar på intyget.',
        'intyg.status.draft_incomplete': 'Utkast, uppgifter saknas',
        'intyg.status.draft_complete': 'Utkast, kan signeras',
        'intyg.status.signed': 'Signerat',
        'intyg.status.cancelled': 'Makulerat',
        'intyg.status.unknown': 'Okänd',
        'intyg.status.sent': 'Skickat',
        'intyg.status.received': 'Signerat',

        'intyg.status.patient.name.pu-intyg.changed': 'Observera att patientens namn har ändrats hos den nationella personuppgiftstjänsten sedan det här intyget utfärdades. Intyg kommer fortsättningsvis innehålla patientens nya namn.',
        'intyg.status.patient.address.pu-intyg.changed': 'Observera att en slagning i nationella personuppgiftstjänsten har genomförts och visar att patientens adress har ändrats sedan det här intyget utfärdades. Intyg kommer fortsättningsvis visa patientens nya adress.',
        'intyg.status.patient.name.pu-integration.changed': 'Observera att namnet som visas i intyget har hämtats från den nationella personuppgiftstjänsten och skiljer sig åt från det som är lagrat i journalsystemet.',

        'intyg.status.replacement.utkast.exists': '<strong>OBS!</strong> Det finns redan ett intygsutkast påbörjat som skall ersätta detta intyg.',

        // Revoke status messages (type agnostic)
        'intyg.status.revoke.requested': '<strong>Status:</strong> Intyget är makulerat.',
        'intyg.status.revoke.confirmed': '<strong>Status:</strong> Intyget är makulerat.',

        // draft utkast header form status messages
        'draft.status.incomplete': '<strong>Status:</strong> Utkastet är sparat, men obligatoriska uppgifter saknas.',
        'draft.status.complete': '<strong>Status:</strong> Utkastet är sparat och kan signeras.',
        'draft.status.signed': '<strong>Status:</strong> Intyget är signerat.',
        'draft.status.changed': '<strong>Status:</strong> Utkastet är ändrat sedan det senast sparades',

        // intyg forms
        'draft.saknar-uppgifter': 'Utkastet saknar uppgifter i följande avsnitt',
        'draft.onlydoctorscansign': 'Endast läkare får signera intyget.',
        'draft.signingdoctor': 'Signerande läkare',
        'draft.helptext.signingdoctor': 'Den läkare som avses signera intyget anges här.',
        'draft.notifydoctor': 'Skicka ett mejl med en länk till utkastet till den läkare som ska signera.',
        'draft.completion.signinfo': 'Om du går vidare kommer det nya kompletterade intyget signeras och skickas direkt till Försäkringskassans system.',

        'draft.notify.check-missing': 'Visa om någon information saknas',
        'draft.notify.check-missing.help': 'Visa om och vilken information utkastet saknar.',
        'draft.notify.button': 'Vidarebefordra utkast',
        'draft.notify.help': 'Skapar ett e-mail i din mailklient innehållande direktlänk till utkastet.',
        'draft.notify.hide-missing.button': 'Dölj information som saknas',

        'draft.notifyready.button': 'Markera som klart för signering',
        'draft.notifyready.text': 'Utkastet är sparat och markerat som klart för signering.',
        'modal.title.notifyjournalsystem': 'Markera utkast som klart för signering',


        'intyg.makulera.help': 'Orsak till makulering ska anges eftersom Försäkringskassans handläggare behöver den informationen för att bedöma patientens rätt till ersättning.',

        // wc-common-directives-resources
        'nav.label.loggedinas': 'Inloggad som:',

        // Fraga/svar resource - used both by webcert and module modal messages
        'modal.title.markforward': 'Markera som vidarebefordrad?',

        'qa.status.pending_internal_action': 'Kräver svar',
        'qa.status.pending_external_action': 'Inväntar svar',
        'qa.status.answered': 'Besvarat',
        'qa.status.closed': 'Hanterat',
        'qa.help.handled': 'Tidigare frågor och svar är sådana som redan har besvarats eller hanterats på något sätt.',
        'qa.help.kompletteringar': 'Kompletteringar visar den information som Försäkringskassan begärt ska korrigeras på intyget detta utkast ska komplettera.',

        'qa.fragestallare.fk': 'Försäkringskassan',
        'qa.fragestallare.wc': 'Vårdenheten',
        'qa.amne.paminnelse': 'Påminnelse',
        'qa.amne.arbetstidsforlaggning': 'Arbetstidsförläggning',
        'qa.amne.kontakt': 'Kontakt',
        'qa.amne.avstamningsmote': 'Avstämningsmöte',
        'qa.amne.komplettering_av_lakarintyg': 'Komplettering av läkarintyg',
        'qa.amne.komplt': 'Komplettering av läkarintyg',
        'qa.amne.makulering_av_lakarintyg': 'Makulering av läkarintyg',
        'qa.amne.ovrigt': 'Övrigt',

        'qa.measure.svarfranvarden': 'Svara',
        'qa.measure.svarfranfk': 'Invänta svar från Försäkringskassan',
        'qa.measure.komplettering': 'Komplettera',
        'qa.measure.markhandled': 'Markera som hanterad',
        'qa.measure.handled': 'Inget',

        // Ärendehantering
        'common.loading.existing.arenden': 'Laddar ärenden...',
        'common.arende.komplettering.disabled.onlydoctor': 'Kompletteringar kan endast besvaras av läkare.',
        'common.arende.komplettering.kompletteringsatgard.dialogtitle': 'Besvara kompletteringsbegäran',
        'common.arende.komplettering.kompletteringsatgard.dialogtitlevardadmin': 'Kompletteringsbegäran kan inte genomföras',

        'common.arende.fragestallare.wc': 'Vårdenheten',
        'common.arende.fragestallare.fk': 'Försäkringskassan',

        'common.arende.atgard.svarfranvarden': 'Svara',
        'common.arende.atgard.svarfranfk': 'Invänta svar från Försäkringskassan',
        'common.arende.atgard.komplettering': 'Komplettera',
        'common.arende.atgard.markhandled': 'Markera som hanterad',
        'common.arende.atgard.handled': 'Inget',

        'common.arende.fraga.amne.avstmn': 'Avstämningsmöte',
        'common.arende.fraga.amne.kontkt': 'Kontakt',
        'common.arende.fraga.amne.komplt': 'Komplettering',
        'common.arende.fraga.amne.ovrigt': 'Övrigt',
        'common.arende.fraga.amne.paminn': 'Påminnelse',

        'common.arende.svar.besvaradmedintyg': 'Frågan är besvarad med ett nytt intyg.',
        'common.arende.svar.go': 'Gå vidare till intyget',
        'common.arende.svar.komplettering.help': 'Beskriv varför kompletteringen inte kan genomföras. Här får ingen medicinsk information anges.',

        // Används bara av fk7263 (ArendeLegacyProxy)
        'common.arende.fraga.amne.makulering_av_lakarintyg': 'Makulering av läkarintyg',
        'common.arende.fraga.amne.arbetstidsforlaggning': 'Arbetstidsförläggning',

        'common.arende.help.kompletteringar': 'Kompletteringar visar den information som Försäkringskassan begärt ska korrigeras på intyget detta utkast ska komplettera.',
        'common.arende.help.handled': 'Tidigare frågor och svar är sådana som redan har besvarats eller hanterats på något sätt.',

        'common.arende.draft.normal': '',
        'common.arende.draft.dirty': '',
        'common.arende.draft.saved': 'Sparat <span class="glyphicon glyphicon glyphicon-ok-sign"></span>',
        'common.arende.draft.failed': 'Kunde inte spara, på grund av ett tekniskt fel. Försök igen senare. <span class="glyphicon glyphicon glyphicon-exclamation-sign"></span>',
        'common.arende.draft.delete.answer.title': 'Radera påbörjat svar',
        'common.arende.draft.delete.answer.body': 'Är du säker på att du vill radera ditt påbörjade svar?',
        'common.arende.draft.delete.question.title': 'Radera påbörjad fråga',
        'common.arende.draft.delete.question.body': 'Är du säker på att du vill radera din påbörjade fråga?',
        'common.arende.draft.delete.yes': 'Ja, radera',

        // intyg module messages. Used by several intyg modules.
        'modules.label.field': 'Fält',
        'modules.label.blank': '- ej ifyllt',
        'modules.button.alt.archive': 'Arkivera intyget. Flyttar intyget till Arkiverade intyg',

        'common.label.diagnoses.more_results': 'Det finns fler träffar än vad som kan visas i listan, förfina sökningen.',

        'common.intyg.relation.showrelated': 'Visa relaterade intyg <span class="glyphicon glyphicon-chevron-down"></span>',
        'common.intyg.relation.hiderelated': 'Dölj relaterade intyg <span class="glyphicon glyphicon-chevron-up"></span>',
        'common.intyg.relation.currentlyviewing': 'Visas nu',
        'common.intyg.relation.show': 'Visa',
        'common.intyg.relation': 'Relation',
        'common.intyg.relation.status': 'Status',
        'common.intyg.relation.date': 'Datum',
        'common.intyg.relation.code.komplt': 'Kompletterar',
        'common.intyg.relation.code.ersatt': 'Ersätter',
        'common.intyg.relation.code.frlang': 'Förlänger',

        'common.intyg.patientadress': 'Patientens adressuppgifter',
        'common.postadress': 'Postadress',
        'common.postnummer': 'Postadress',
        'common.postort': 'Postort',

        //////////////////////////////////////////////////////////////////////////////////////////////////
        // WIP. module specific strings that should be made common. keep working on these!
        //////////////////////////////////////////////////////////////////////////////////////////////////

        // Basics
        'common.error.generic.show': 'Kunde inte visa intyget',
        'luse.error.generic': 'Kunde inte visa intyget',
        'luae_na.error.generic': 'Kunde inte visa intyget',
        'luae_fs.error.generic': 'Kunde inte visa intyget',
        'lisjp.error.generic': 'Kunde inte visa intyget',
        'fk7263.error.generic': 'Kunde inte visa intyget',

        'common.load.certificate': 'Hämtar intyget..',

        'luse.message.certificateloading': 'Hämtar intyg...',
        'luae_na.message.certificateloading': 'Hämtar intyg...',
        'luae_fs.message.certificateloading': 'Hämtar intyg...',
        'lisjp.message.certificateloading': 'Hämtar intyg...',
        'fk7263.message.certificateloading': 'Hämtar intyg...',

        'luse.label.send': 'Skicka intyg till Försäkringskassan',
        'luae_na.label.send': 'Skicka intyg till Försäkringskassan',
        'luae_fs.label.send': 'Skicka intyg till Försäkringskassan',
        'lisjp.label.send': 'Skicka intyg till Försäkringskassan',
        'fk7263.label.send': 'Skicka intyg till Försäkringskassan',

        'luse.button.send': 'Skicka till Försäkringskassan',
        'luae_na.button.send': 'Skicka till Försäkringskassan',
        'luae_fs.button.send': 'Skicka till Försäkringskassan',
        'lisjp.button.send': 'Skicka till Försäkringskassan',
        'fk7263.button.send': 'Skicka till Försäkringskassan',

        'ts-bas.button.send': 'Skicka till Transportstyrelsen',
        'ts-bas.label.send': 'Skicka intyg till Transportstyrelsen',
        'ts-diabetes.button.send': 'Skicka till Transportstyrelsen',
        'ts-diabetes.label.send': 'Skicka intyg till Transportstyrelsen',

        // Send fk
        'luse.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Försäkringskassans system vilket ska göras i samråd med patienten.',
        'luae_na.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Försäkringskassans system vilket ska göras i samråd med patienten.',
        'luae_fs.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Försäkringskassans system vilket ska göras i samråd med patienten.',

        // send ts
        'ts-bas.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Transportstyrelsens system vilket ska göras i samråd med patienten.',
        'ts-diabetes.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Transportstyrelsens system vilket ska göras i samråd med patienten.',

        // Send w sjukpenning
        'lisjp.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Försäkringskassans system vilket ska göras i samråd med patienten.<br><br>Upplys patienten om att även göra en ansökan om sjukpenning hos Försäkringskassan.',
        'fk7263.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Försäkringskassans system vilket ska göras i samråd med patienten.<br><br>Upplys patienten om att även göra en ansökan om sjukpenning hos Försäkringskassan.',

        // Status
        'luse.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'luse.label.status.recieved': 'Intyget är signerat, skickat och mottaget av Försäkringskassans system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'luse.label.status.signed': 'Intyget är signerat. <br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via minaintyg.se. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten.',
        'luse.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',

        'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning': 'Motivering till varför det medicinska underlaget inte baseras på en undersökning av patienten',
        'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning.info': 'Information överförs till fältet {0} vid signering.',

        'luae_na.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'luae_na.label.status.recieved': 'Intyget är signerat, skickat och mottaget av Försäkringskassans system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'luae_na.label.status.signed': 'Intyget är signerat. <br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via minaintyg.se. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten.',
        'luae_na.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',

        'luae_fs.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'luae_fs.label.status.recieved': 'Intyget är signerat, skickat och mottaget av Försäkringskassans system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'luae_fs.label.status.signed': 'Intyget är signerat. <br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via minaintyg.se. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten.',
        'luae_fs.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',

        'lisjp.label.status.sent': 'Intyget är signerat och har skickats till Försäkringskassans system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'lisjp.label.status.recieved': 'Intyget är signerat, skickat och mottaget av Försäkringskassans system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'lisjp.label.status.signed': 'Intyget är signerat. <br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via minaintyg.se. Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om sjukpenning. Om patienten inte har möjlighet att skicka intyget elektroniskt till Försäkringskassan kan du skicka intyget direkt till Försäkringskassan åt patienten.',
        'lisjp.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',

        'ts-bas.label.status.sent': 'Intyget är signerat och har skickats till Transportstyrelsens system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'ts-bas.label.status.recieved': 'Intyget är signerat, skickat och mottaget av Transportstyrelsens system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'ts-bas.label.status.signed': 'Intyget är signerat. <br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via minaintyg.se. Om patienten inte har möjlighet att skicka intyget elektroniskt till Transportstyrelsen kan du skicka intyget direkt till Transportstyrelsen åt patienten.',
        'ts-bas.label.makulera.confirmation': 'Transportstyrelsens läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',

        'ts-diabetes.label.makulera.confirmation': 'Transportstyrelsens läkarintyg, diabetes, utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
        'ts-diabetes.label.status.sent': 'Intyget är signerat och har skickats till Transportstyrelsens system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'ts-diabetes.label.status.recieved': 'Intyget är signerat, skickat och mottaget av Transportstyrelsens system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via minaintyg.se.',
        'ts-diabetes.label.status.signed': 'Intyget är signerat. <br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via minaintyg.se. Om patienten inte har möjlighet att skicka intyget elektroniskt till Transportstyrelsen kan du skicka intyget direkt till Transportstyrelsen åt patienten.',

        // Validation messages
        'luse.validation.grund-for-mu.incorrect_combination_annat_beskrivning': 'Fritextfältet som hör till alternativet Annat under \'Uppgifterna i intyget baseras på\' får endast fyllas i om \'Annan referens valts\'.',
        'luse.validation.grund-for-mu.kannedom.after': 'Får inte vara senare än \'{0}\'',
        'luse.validation.underlagfinns.incorrect_combination': 'Om frågan \'Finns det andra medicinska utredningar eller underlag\' besvarats med nej får underlag inte anges.',
        'luse.validation.underlag.too_many': 'För många underlag. Detta fel kan ej åtgärdas av användaren. Påbörja ett nytt utkast och rapportera felet till <LINK:ineraKundserviceAnmalFel>.',
        'luse.validation.underlag.missing': 'Du måste ange ett underlag.',
        'luse.validation.underlag.incorrect_format': 'Fel typ av underlag',
        'luse.validation.underlag.date.missing': 'Du måste ange datum för underlaget.',
        'luse.validation.underlag.hamtas-fran.missing': 'Du måste ange var Försäkringskassan kan få information om utredningen.',
        'luse.validation.diagnosfornybedomning.incorrect_combination': 'Om \'Finns det skäl att göra en ny bedömning av diagnosen?\' besvarats med \'Nej\' får inte \'Diagnos för ny bedömning\' fyllas i',
        'luse.validation.kontakt.incorrect_combination': 'Anledning till kontakt kan endast fyllas i om \'kontakt med FK önskas\' är vald.',

        'luae_na.validation.grund-for-mu.incorrect_combination_annat_beskrivning': 'Fritextfältet som hör till alternativet Annat under \'Uppgifterna i intyget baseras på\' får endast fyllas i om \'Annan referens\' valts.',
        'luae_na.validation.grund-for-mu.kannedom.after': 'Får inte vara senare än \'{0}\'',
        //'luae_na.validation.grund-for-mu.kannedom.after.undersokning': '\'Kännedom om patienten\' får inte vara senare än datum för \'Min undersökning av patienten\'',
        //'luae_na.validation.grund-for-mu.kannedom.after.anhorigsbeskrivning': '\'Kännedom om patienten\' får inte vara senare än datum för \'Anhörigs beskrivning av patienten\'',
        'luae_na.validation.underlagfinns.incorrect_combination': 'Om frågan \'Finns det andra medicinska utredningar eller underlag\' besvarats med nej får underlag inte anges.',
        'luae_na.validation.underlag.missing': 'Du måste ange ett underlag.',
        'luae_na.validation.underlag.incorrect_format': 'Fel typ av underlag',
        'luae_na.validation.underlag.date.missing': 'Du måste ange datum för underlaget.',
        'luae_na.validation.underlag.hamtas-fran.missing': 'Du måste ange var Försäkringskassan kan få information om utredningen.',
        'luae_na.validation.diagnosfornybedomning.incorrect_combination': 'Om \'Finns det skäl att göra en ny bedömning av diagnosen?\' besvarats med \'Nej\' får inte \'Diagnos för ny bedömning\' fyllas i',
        'luae_na.validation.kontakt.incorrect_combination': 'Anledning till kontakt kan endast fyllas i om \'kontakt med FK önskas\' är vald.',

        'luae_fs.validation.grund-for-mu.annat.beskrivning.invalid_combination': 'Om inte alternativet Annat är angett får inte beskrivningsfältet anges',
        'luae_fs.validation.underlagfinns.incorrect_combination': 'Om frågan \'Finns det andra medicinska utredningar eller underlag\' besvarats med nej får underlag inte anges.',
        'luae_fs.validation.grund-for-mu.kannedom.after': 'Får inte vara senare än \'{0}\'',
        'luae_fs.validation.underlag.missing': 'Du måste ange ett underlag.',
        'luae_fs.validation.underlag.incorrect_format': 'Fel typ av underlag',
        'luae_fs.validation.underlagfinns.missing': 'Frågan \'Finns det andra medicinska utredningar eller underlag\' måste besvaras',
        'luae_fs.validation.underlag.date.missing': 'Du måste ange datum för underlaget.',
        'luae_fs.validation.underlag.hamtas-fran.missing': 'Du måste ange var Försäkringskassan kan få information om utredningen.',
        'luae_fs.validation.kontakt.invalid_combination': 'Anledning får inte fyllas i när man svarat nej på kontakt.',
        'luae_fs.validation.diagnos.max-diagnoser': 'Du kan endast ange upp till tre st diagnoser.',
        'luae_fs.validation.funktionsnedsattning.debut.missing': 'Funktionsnedsättningens debut och utveckling måste fyllas i.',
        'luae_fs.validation.funktionsnedsattning.paverkan.missing': 'Funktionsnedsättningens påverkan måste fyllas i.',
        'luae_fs.validation.blanksteg.otillatet': 'Fältet får inte fyllas i med endast blanksteg',

        'lisjp.validation.grund-for-mu.annat.beskrivning.invalid_combination': 'annat beskrivning invalid combination',  // Should never happen because GUI should block this combination
        'lisjp.validation.kontakt.invalid_combination': 'Anledning får inte fyllas i när man svarat nej på kontakt.',  // Should never happen because GUI should block this combination
        'lisjp.validation.blanksteg.otillatet': 'blanksteg',  // Should never happen because GUI should block this combination

        // errors
        'sjukersattning.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'sjukersattning.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'sjukersattning.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.</strong>',
        'luae_na.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'luae_na.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'luae_na.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.</strong>',
        'luae_fs.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'luae_fs.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'luae_fs.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.</strong>',
        'lisjp.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'lisjp.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'lisjp.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.</strong>',
        'fk7263.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'fk7263.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'fk7263.error.could_not_load_cert_qa': '<strong>Frågor och svar för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.</strong>',

        // PU down, cannot print cert
        'fk7263.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'lisjp.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'luse.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'luae_na.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'luae_fs.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'ts-bas.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'ts-diabetes.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'db.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
        'doi.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',

        // PU down, cannot print draft
        'common.error_could_not_print_draft_no_pu': '<strong>Det går inte att skriva ut utkastet!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut utkastet. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
        // Generic PU-error.
        'common.error.pu_problem': 'Den nationella personuppgiftstjänsten svarar inte. Åtgärden kan inte genomföras eftersom den kräver att personuppgifter kan hämtas från personuppgiftsregistret.<br>Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',

        // PU down, cannot send
        'fk7263.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'lisjp.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'luse.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'luae_na.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'luae_fs.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'ts-bas.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'ts-diabetes.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
        'db.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
        'doi.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',


    // fragaSvar errors
        'luae_na.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'luae_na.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'luae_na.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'luae_na.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'luae_na.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'luae_na.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',
        'sjukersattning.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'sjukersattning.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'sjukersattning.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'sjukersattning.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'sjukersattning.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'sjukersattning.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',
        'luae_fs.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'luae_fs.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'luae_fs.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'luae_fs.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'luae_fs.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'luae_fs.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',
        'lisjp.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'lisjp.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'lisjp.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'lisjp.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'lisjp.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'lisjp.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',

        //////////////////////////////////////////////////////////////////////////////////////////////////
        // END WIP module specific strings
        //////////////////////////////////////////////////////////////////////////////////////////////////

        //common.sit.* messages are candidates for a fk-common messages.js
        'common.sit.label.sjukskrivning.hoursperweek': 'Patienten arbetar i snitt',
        'common.sit.help.sjukskrivning.hoursperweek': 'Ange hur många timmar patienten arbetar i snitt per vecka. Observera att denna funktion endast är ett stöd för att tydliggöra hur många timmar per vecka patienten bedöms kunna arbeta när en viss nedsättning av arbetsförmåga har angivits. Uppgiften lagras inte som en del av intyget då Försäkringskassan inhämtar information från annat håll.',
        'common.sit.label.valj-version-icd-10': 'Välj kodverk:',
        'common.sit.label.diagnoskodverk.fullstandig': 'ICD-10-SE',
        'common.sit.label.diagnoskodverk.primarvard': 'KSH97-P (Primärvård)',
        'common.sit.client-validation.underlag.max-extra-underlag': 'Du kan inte lägga till fler utredningar, max antal är %0st',
        'common.label.patient': 'Patientens adressuppgifter',
        'common.label.vardenhet': 'Vårdenhetens adress',
        'common.validation.patient.postadress.missing': 'Postadress saknas.',
        'common.validation.patient.postnummer.missing': 'Postnummer saknas.',
        'common.validation.patient.postort.missing': 'Postort saknas.',
        'common.validation.postnummer.incorrect-format': 'Postnummer måste anges med fem siffror',

        'common.validation.boolean.empty': 'Du måste välja ett alternativ.',
        'common.validation.check-group.empty': 'Du måste välja minst ett alternativ.',
        'common.validation.multi-text.empty': 'Fältet får inte vara tomt.',
        'common.validation.text-group.empty': 'Minst ett fält måste fyllas i',
        'common.validation.radio-group.empty': 'Du måste välja ett alternativ.',
        'common.validation.select.empty': 'Du måste välja ett alternativ.',
        'common.validation.singledate.empty': 'Du måste välja datum.',
        'common.validation.singledate.invalid_format': 'Datum behöver skrivas på formatet ÅÅÅÅ-MM-DD',
        'common.validation.single-text.empty': 'Fältet får inte vara tomt.',
        'common.validation.single-text-vertical.empty': 'Fältet får inte vara tomt.',
        'common.validation.typeahead.empty': 'Fältet får inte vara tomt.',
        'common.validation.underlag.empty': 'Minst en rad måste fyllas i',
        'common.validation.date.empty': 'Fältet får inte vara tomt.',
        'common.validation.date.invalid_format': 'Datum behöver skrivas på formatet ÅÅÅÅ-MM-DD',
        'common.validation.date-period.invalid_format': 'Felaktigt datumformat.',
        'common.validation.date-period.invalid_order': 'Startdatum får inte vara efter slutdatum.',
        'common.validation.date-period.period_overlap': 'Datumintervall överlappar.',
        'common.validation.date_out_of_range': 'Datum får inte ligga för långt fram eller tillbaka i tiden.',
        'common.validation.date_invalid': 'Ogiltigt datum',
        'common.validation.future.datum': 'Observera att du valt ett datum framåt i tiden.',
        'common.validation.date.today.or.earlier': 'Datumet måste vara dagens datum eller tidigare.',
        'common.validation.diagnos.missing': 'Huvuddiagnos måste anges',
        'common.validation.diagnos0.missing': 'ICD-10 kod saknas på huvuddiagnosen.',
        'common.validation.diagnos0.invalid': 'ICD-10 kod på huvuddiagnosen är ej giltig',
        'common.validation.diagnos0.length-3': 'Diagnoskod på huvuddiagnosen ska anges med så många positioner som möjligt, men minst tre positioner.',
        'common.validation.diagnos0.psykisk.length-4': 'Diagnoskod på huvuddiagnosen ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'common.validation.diagnos0.description.missing': 'Diagnostext saknas på huvuddiagnosen',
        'common.validation.diagnos1.missing': 'ICD-10 kod saknas på diagnosrad 2.',
        'common.validation.diagnos1.invalid': 'ICD-10 kod på diagnosrad 2 är ej giltig',
        'common.validation.diagnos1.length-3': 'Diagnoskod på diagnosrad 2 ska anges med så många positioner som möjligt, men minst tre positioner.',
        'common.validation.diagnos1.psykisk.length-4': 'Diagnoskod på diagnosrad 2 ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'common.validation.diagnos1.description.missing': 'Diagnostext saknas på diagnosrad 2',
        'common.validation.diagnos2.missing': 'ICD-10 kod saknas på diagnosrad 3.',
        'common.validation.diagnos2.invalid': 'ICD-10 kod på diagnosrad 3 är ej giltig',
        'common.validation.diagnos2.length-3': 'Diagnoskod på diagnosrad 3 ska anges med så många positioner som möjligt, men minst tre positioner.',
        'common.validation.diagnos2.psykisk.length-4': 'Diagnoskod på diagnosrad 3 ska anges med minst fyra positioner då en psykisk diagnos anges.',
        'common.validation.diagnos2.description.missing': 'Diagnostext saknas på diagnosrad 3',
        'common.validation.sjukskrivning_period.empty': 'Fältet får inte vara tomt.',
        'common.validation.sjukskrivning_period.invalid_format': 'Datum behöver skrivas på formatet ÅÅÅÅ-MM-DD',
        'common.validation.sjukskrivning_period.incorrect_combination': 'Startdatum får inte vara efter slutdatum.',
        'common.validation.sjukskrivning_period.period_overlap': 'Sjukskrivningsperioder med överlappande datum har angetts.',
        'common.validation.sjukskrivning_period.en_fjardedel.invalid_format': 'Datum för nedsatt arbetsförmåga med 25% har angetts på felaktigt format.',
        'common.validation.sjukskrivning_period.halften.invalid_format': 'Datum för nedsatt arbetsförmåga med 50% har angetts på felaktigt format.',
        'common.validation.sjukskrivning_period.tre_fjardedel.invalid_format': 'Datum för nedsatt arbetsförmåga med 75% har angetts på felaktigt format.',
        'common.validation.sjukskrivning_period.helt_nedsatt.invalid_format': 'Datum för nedsatt arbetsförmåga med 100% har angetts på felaktigt format.',
        'common.validation.underlag.invalid_format': 'Datum behöver skrivas på formatet ÅÅÅÅ-MM-DD',
        'common.validation.vaguedate.invalid_format': 'ÅÅÅÅ måste anges, månad och dag kan anges som 00',

        // Common errors
        'common.error.unknown': '<strong>Tekniskt fel.</strong>',
        'common.error.authorization_problem': '<strong>Behörighet saknas.</strong><br>Du saknar behörighet att använda denna resurs.',
        'common.error.authorization_problem_sekretessmarkering': '<strong>Behörighet saknas.</strong><br>Du saknar behörighet. För att hantera intyg för patienter med sekretessmarkering krävs att du har befattningen läkare eller tandläkare. Vissa intygstyper får inte hanteras alls för patienter med sekretessmarkering, även om du har befattningen som krävs.',
        'common.error.authorization_problem_sekretessmarkering_enhet': '<strong>Behörighet saknas.</strong><br>Du saknar behörighet att hantera detta intyg. För att hantera ett intyg för en patient med sekretessmarkering måste du vara inloggad på den vårdenhet intyget skrevs.',
        'common.error.cantconnect': '<strong>Kunde inte kontakta servern.</strong>',
        'common.error.certificatenotfound': '<strong>Intyget finns inte.</strong>',
        'common.error.certificateinvalid': '<strong>Intyget är inte korrekt ifyllt.</strong>',
        'common.error.certificateinvalidstate': '<strong>Intyget är inte ett utkast.</strong>Inga operationer kan utföras på det längre.',
        'common.error.certificate_revoked': '<strong>Operation är inte möjlig.</strong><br>Förmodligen har en annan användare makulerat intyget medan du arbetat på samma post. Ladda om sidan och försök igen',
        'common.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Ladda om sidan och försök igen. Kontakta supporten om problemet kvarstår.',
        'common.error.invalid_state': '<strong>Operation är inte möjlig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma utkast. Ladda om sidan och försök igen',
        'common.error.sign.general': '<strong>Intyget kunde inte signeras.</strong><br>Försök igen senare.',
        'common.error.sign.netid': '<strong>Signering misslyckad.</strong><br>Intyget har inte signerats. Detta beror antingen på ett tekniskt fel eller att signeringen avbrutits. Försök igen senare eller kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'common.error.sign.bankid': '<strong>Intyget kunde inte signeras.</strong><br>Kunde inte kontakta Bank ID-klienten. Försök igen senare eller kontakta din support.',
        'common.error.sign.not_ready_yet': '<strong>Intyget är nu signerat.</strong><br>Tyvärr kan inte intyget visas än då det behandlas. Prova att ladda om sidan lite senare. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'common.error.sign.concurrent_modification': '<strong>Det går inte att signera utkastet.</strong><br/>Utkastet har ändrats av en annan användare sedan du började arbeta på det. Ladda om sidan, kontrollera att uppgifterna stämmer och försök signera igen.<br/>Utkastet ändrades av ${name}.',
        'common.error.sign.authorization': '<strong>Intyget kunde inte signeras.</strong><br/>Du saknar behörighet att signera detta intyg.',
        'common.error.sign.indeterminate.identity': '<strong>Intyget kunde inte signeras.</strong><br/>Det verkar som att du valt en annan identitet att signera med än den du loggade in med. Du måste identifiera dig på samma sätt som när du loggade in. Kontrollera om du har valt rätt och prova igen.',
        'common.error.sign.grp.already_in_progress': '<strong>Intyget kunde inte signeras.</strong><br/>En inloggning eller underskrift för det här personnumret är redan påbörjad, tryck avbryt i BankID säkerhetsapp och försök igen.',
        'common.error.sign.grp.cancel': '<strong>Intyget kunde inte signeras.</strong><br/>Åtgärden avbruten.',
        'common.error.sign.grp.expired_transaction': '<strong>Intyget kunde inte signeras.</strong><br/>Inget svar från klienten. Kontrollera att du har startat din BankID säkerhetsapp, följ instruktionerna och försök igen.',
        'common.error.unknown_internal_problem': '<strong>Tekniskt fel i Webcert.</strong><br>Försök igen senare.',
        'common.error.data_not_found': '<strong>Intyget kunde inte hittas.</strong><br>Intyget är borttaget eller så saknas behörighet.',
        'common.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'common.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
        'common.error.module_problem': '<strong>Tekniskt fel i Webcert.</strong><br>Problem att kontakta intygsmodulen.',
        'common.error.discard.concurrent_modification': '<strong><br>Kan inte ta bort utkastet. Utkastet har ändrats av en annan användare medan du arbetat på samma utkast.</strong><br>Ladda om sidan och försök igen. Utkastet ändrades av: ${name}',
        'common.error.save.unknown': '<strong>Okänt fel.</strong><br>Det går för tillfället inte att spara ändringar.',
        'common.error.save.noconnection': '<strong>Inget nätverk</strong><br>Det går för tillfället inte att spara ändringar eftersom servern inte kunde kontaktas.',
        'common.error.save.module_problem': '<strong>Okänt fel.</strong><br>Det går för tillfället inte att spara ändringar.',
        'common.error.save.data_not_found': '<strong>Okänt fel.</strong><br>Det går för tillfället inte att spara ändringar.',
        'common.error.save.concurrent_modification': '<strong>Kan inte spara utkastet. Utkastet har ändrats av en annan användare medan du arbetat på samma utkast.</strong><br>Ladda om sidan och försök igen. Utkastet ändrades av: ${name}',
        'common.error.save.unknown_internal_problem': '<strong>Tappade anslutningen till servern.</strong><br>Det går för tillfället inte att spara ändringar.',
        'common.error.save.invalid_state': '<strong>Tekniskt fel.</strong><br>Intyget kunde inte laddas korrekt. (invalid_state).',
        'common.error.certificate_type_sekretessmarkering_unsupported': '<strong>Intyg kan ej hanteras.</strong><br>Det intyg du valt att skriva går tyvärr inte att skapa i Webcert för en patient med sekretessmarkering.',

        // FMB texts
        'fmb.symptom_prognos_behandling': 'Symtom, prognos och behandling',
        'fmb.generell_info': 'Generell information',
        'fmb.funktionsnedsattning': 'Om funktionsnedsättning',
        'fmb.aktivitetsbegransning': 'Om aktivitetsbegränsning',
        'fmb.beslutsunderlag_textuellt': 'Sjukskrivningsrekommendation',

        // Fragasvar för FK
        'common.fk.info.loading.existing.qa': 'Hämtar tidigare frågor och svar...',
        'common.fk.fragasvar.answer.is.sent': '<strong>Svaret har skickats till Försäkringskassan.</strong><br> Frågan är nu markerad som hanterad och visas nu under \'Hanterade frågor\' längre ner på sidan.',
        'common.fk.fragasvar.marked.as.hanterad': '<strong>Frågan-svaret är markerad som hanterad.</strong><br> Frågan-svaret visas under rubriken \'hanterade frågor och svar\' nedan.',
        'common.fk.fragasvar.marked.as.ohanterad': '<strong>Frågan-svaret är markerad som ej hanterad.</strong><br> Frågan-svaret visas under rubriken \'Ej hanterade frågor och svar\' ovan.',
        'common.fk.fragasvar.label.ovanstaende-har-bekraftats': '<strong>Ovanstående har bekräftats</strong>',

        // fragaSvar errors
        'common.fk.fragasvar.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'common.fk.fragasvar.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'common.fk.fragasvar.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'common.fk.fragasvar.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'common.fk.fragasvar.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'common.fk.fragasvar.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',

        // Ärendehantering errors
        'common.arende.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',
        'common.arende.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'common.arende.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'common.arende.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'common.arende.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'common.arende.error.data_not_found': '<strong>Personen kunde inte hittas i personuppgiftstjänsten.</strong>',
        'common.arende.error.komplettera-no-intyg': '<strong>Kan inte svara med nytt intyg</strong><br>Intyget kunde inte laddas och är därför inte tillgängligt att användas som grund för komplettering.',
        'common.arende.error.komplettera-no-utkast': '<strong>Kan inte gå till existerande intygsutkast</strong><br>Intygsutkastet kunde inte laddas.'
    },
    'en': {
        'common.ok': 'OK',
        'common.cancel': 'Cancel'
    }
});
