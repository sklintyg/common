/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

(function(){
    'use strict';

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
            'common.close': 'Stäng',
            'common.canceldontrevoke': 'Avbryt, makulera ej',
            'common.goback': 'Tillbaka',
            'common.createfromtemplate': 'Skapa ${intygName}',
            'common.createfromtemplate.tooltip': 'Skapar ett ${newIntygName} utifrån ${currentIntygName}.',
            'common.gotofromtemplate': 'Visa ${intygName}',
            'common.makulera': 'Makulera',
            'common.makulera.tooltip': 'Öppnar ett fönster där du kan välja att makulera intyget.',
            'common.makulera.locked.tooltip': 'Öppnar ett fönster där du kan välja att makulera det låsta utkastet.',
            'common.sign': 'Signera intyget',
            'common.shortsign': 'Signera',
            'common.sign.tooltip': 'Intyget signeras.',
            'common.signsend': 'Signera och skicka',
            'common.signsend.tooltip': 'Intyget skickas direkt till ${recipient}.',
            'common.signsend.completion': 'Signera och skicka',
            'common.signsend.completion.tooltip': 'Det kompletterade intyget skickas direkt till ${recipient}.',
            'common.send': 'Skicka',
            'common.send.tooltip': 'Öppnar ett fönster där du kan välja att skicka intyget till ${recipient}.',
            'common.copy': 'Kopiera',
            'common.copy.resume': 'Fortsätt på utkast',
            'common.copy.tooltip': 'Ett nytt utkast skapas på den vårdenhet du är inloggad på. All information i det befintliga intyget följer med till utkastet.',
            'common.copy.utkast.tooltip': 'Skapar en redigerbar kopia av intyget på den enheten du är inloggad på.',
            'common.fornya': 'Förnya',
            'common.fornya.tooltip': 'Skapar en redigerbar kopia av intyget på den enheten du är inloggad på.',
            'common.fornya.sjukskrivning.tooltip': 'Skapar ett nytt intygsutkast för förlängning av sjukskrivning, där en del information från detta intyg följer med.',
            'common.createfromtemplate.continue': 'Fortsätt',
            'common.createfromtemplate.cancel': 'Avbryt',
            'common.ersatt': 'Ersätt',
            'common.ersatt.resume': 'Fortsätt på utkast',
            'common.ersatt.cancel': 'Avbryt',
            'common.ersatt.tooltip': 'Skapar en kopia av detta intyg som du kan redigera.',
            'common.ersatt.unhandledkomplettering.tooltip': 'Intyget har minst en ohanterad kompletteringsbegäran och går inte att ersätta.',
            'common.open': 'Öppna',
            'common.show': 'Visa',
            'common.show.tooltip': 'Öppnar intyget/utkastet.',
            'common.delete': 'Radera',
            'common.delete.tooltip': 'Raderar intygsutkastet.',
            'common.sign.intyg': 'Signera intyget',
            'common.button.save.as.pdf': 'Skriv ut',
            'common.button.save.as.pdf.intyg.tooltip': 'Laddar ned intyget för utskrift.',
            'common.button.save.or.print.intyg.tooltip': 'Öppnar ett fönster där du kan välja att skriva ut eller spara intyget som PDF.',
            'common.button.save.as.pdf.intyg.select.tooltip': 'Välj att ladda ned ett fullständigt intyg, eller ett minimalt intyg som uppfyller sjuklönelagens krav på intyg som kan lämnas till arbetsgivaren.',
            'common.button.save.as.pdf.utkast.tooltip': 'Laddar ned intygsutkastet för utskrift.',
            'common.button.save.or.print.utkast.tooltip': 'Öppnar ett fönster där du kan välja att skriva ut eller spara intygsutkastet som PDF.',
            'common.button.save.as.pdf.full': 'Fullständigt intyg',
            'common.button.save.as.pdf.minimal': 'Minimalt intyg',

            'common.alert.newpersonid': 'Patientens personnummer har ändrats',
            'common.alert.newreserveid': 'Patienten har samordningsnummer kopplat till reservnummer: <b>${reserve}</b>.',
            'common.alert.sekretessmarkering.print': 'Patienten har skyddade personuppgifter. Hantera utskriften varsamt.',
            'common.alert.textversionupdated': 'Observera att frågetexterna har uppdaterats',
            'common.warning.patientdataupdate.failed': '<b>Observera!</b> Misslyckades att slå upp patienten i personuppgiftstjänsten',

            'common.date': 'Datum',
            'common.when': 'När?',

            'common.notset': 'Ej angivet',

            'common.favourite.add.tooltip': 'Markera intyget som favorit och fäst högst upp i listan.',
            'common.favourite.remove.tooltip': 'Ta bort som favoritmarkerat intyg.',
            'common.lightbulb.tooltip': 'Funktionen är ett stöd för ifyllnad och bedömning.',

            // makulera
            'label.makulera': 'Makulera intyg',
            'label.makulera.body.common-header': 'Ett intyg kan makuleras om det innehåller allvarliga fel. Exempel på ett allvarligt fel är om intyget är utfärdat på fel patient. Om intyget har skickats elektroniskt till en mottagare kommer denna att informeras om makuleringen. Invånaren kan inte se makulerade intyg på <LINK:minaintyg>.',
            'label.makulera.body.has-arenden.addition': 'Om du går vidare och makulerar intyget kommer dina ej hanterade ärenden markeras som hanterade.',
            'label.makulera.body.common-footer': 'Om du fått ny information om patienten eller av annan anledning behöver korrigera innehållet i intyget, bör du istället ersätta intyget med ett nytt intyg.',
            'db.makulera.body.common-header': '<p>För att makulera ett dödsbevis som är inskickat på fel person behöver du göra följande:</p><ol type="1"><li>Genom att trycka på ”Makulera” makulerar du dödsbeviset i Webcert, men detta kommer inte att återkalla dödsbeviset hos Skatteverket.</li><li><b>Förutom att trycka på ”Makulera” måste du omedelbart ta kontakt med Skatteverket så att felet kan åtgärdas.</b> Du tar kontakt med Skatteverket genom att ringa Skatteupplysningen på telefon <b>0771-567 567</b> och ange "folkbokföring - dödsfall".</li></ol>',
            'doi.makulera.body.common-header': '<p>För att makulera ett dödsorsaksintyg som är inskickat på fel person behöver du göra följande:</p><ol type="1"><li>Genom att trycka på ”Makulera” makulerar du dödsorsaksintyget i Webcert, men detta kommer inte att återkalla dödsorsaksintyget hos Socialstyrelsen.</li><li><b>Förutom att trycka på ”Makulera” måste du omedelbart ta kontakt med Socialstyrelsen så att felet kan åtgärdas.</b> Du tar kontakt med Socialstyrelsen genom att ringa Socialstyrelsens växel på telefon <b>075 247 30 00</b>.</li></ol>',
            'label.makulera.title.radio': 'Ange för ${recipient} varför du makulerar intyget:',
            'label.makulera.confirmation': 'Kvittens - Återtaget intyg',

            // makulera låst intyg
            'label.makulera.locked': 'Makulera låst utkast',
            'label.makulera.locked.body.common-footer': 'Om du fått ny information om patienten eller av annan anledning behöver korrigera innehållet i utkastet, bör du istället kopiera utkastet och skapa ett nytt intyg.',
            'label.makulera.locked.body.common-header': 'Ett låst utkast kan makuleras om det innehåller allvarliga fel. Exempel på ett allvarligt fel är om det är skapat för fel patient.',
            'label.makulera.locked.title.radio': 'Ange varför du makulerar det låsta utkastet:',
            'label.makulera.no.recipient.title.radio': 'Ange varför du makulerar intyget',

            // subscription
            'subscription.warning.title.text': 'Automatisk avtalskontroll sker för samtliga användare',
            'subscription.warning.info.check': 'Du ser detta meddelande för att din verksamhet saknar abonnemang för Webcert.',
            'subscription.warning.info.general': '<p>För att er verksamhet fortsatt ska kunna utfärda intyg efter <em>${requireSubscriptionStartDate}</em> kräver Inera att ni tecknar kundavtal samt beställer abonnemang för tjänsten Webcert.</p><p>Om abonnemang saknas efter <em>${requireSubscriptionStartDate}</em> kommer inga nya intyg att kunna utfärdas. Er verksamhet får då endast tillgång till Webcert för att läsa tidigare utfärdade intyg.</p>',
            'subscription.warning.info.eleg': 'Tidigare kommunicerat beslut att e-legitimationsinloggning endast är förbehållen enskild firma är omprövat. Ytterligare information finns på nedanstående länk.',
            'subscription.warning.info.link': 'Läs mer om Webcert och hur du beställer tjänsten <LINK:ineraIntygstjansterWebcert>',
            'error.auth-exception-subscription.text': '<h2>Abonnemang saknas för Webcert</h2><p/><p>För att kunna skapa konto behöver du först teckna kundavtal med Inera och beställa abonnemang för Webcert.</p><p>Läs mer om Webcert och hur du beställer abonnemang <LINK:ineraIntygstjansterWebcert>.</p><p>När du beställt abonnemang återvänder du till den här sidan för att skapa ditt konto i Webcert.</p>',

            'subscription.missing.title.text': 'Abonnemang för Webcert saknas',
            'subscription.missing.info.check': 'Du ser detta meddelande för att du eller din verksamhet saknar abonnemang.',
            'subscription.missing.info.general': '<p>För att kunna skriva intyg i Webcert behöver du eller din verksamhet teckna kundavtal med Inera samt beställa abonnemang för tjänsten.</p><p>Utan abonnemang kan du endast läsa, skriva ut och makulera eventuella tidigare utfärdade intyg.</p><p>Läs mer om Webcert och hur du beställer <LINK:ineraIntygstjansterWebcert>.</p>',
            'subscription.missing.info.link': 'Om du tidigare använt Webcert och inte längre vill nyttja tjänsten måste du eller din verksamhet inkomma med avslutsbegäran. Den reglerar din verksamhets skyldighet att omhänderta intygsinformation <LINK:ineraAvslutaTjansten>.',

            // avtal/terms
            'avtal.title.text': 'Godkännande av användarvillkor',
            'avtal.approve.label': 'Jag godkänner villkoren',
            'avtal.print.label': 'Skriv ut',
            'avtal.logout.label': 'Logga ut',

            // labels for common modal dialogs
            'common.title.sign': 'Signera intyget',
            'common.modal.title.sign.error': 'Signering misslyckad',
            'common.modal.title.error': 'Tekniskt fel',

            'common.modal.copy-from-candidate.title': 'Hjälp med ifyllnad?',

            'common.modal.copy.title': 'Kopiera låst utkast',
            'common.modal.copy.body_new': '<p>Genom att kopiera ett låst intygsutkast skapas ett nytt utkast med samma information som i det ursprungliga låsta utkastet. Du kan redigera utkastet innan du signerar det. Det ursprungliga låsta utkastet finns kvar.</p>' +
                                            '<p>Det nya utkastet skapas på den enhet du är inloggat på.</p>',
            'common.modal.copy.body_go': '<p>Genom att kopiera ett låst intygsutkast skapas ett nytt utkast med samma information som i det ursprungliga låsta utkastet. Du kan redigera utkastet innan du signerar det. Det ursprungliga låsta utkastet finns kvar.</p>',

            'common.modal.label.discard_draft': 'Radera utkast',
            'common.modal.label.confirm_sign': 'Signera intyget',

            'common.modal.label.signing': 'Ditt intyg signeras.',
            'common.modal.label.signing.seconds': 'Detta kan ta några sekunder.',
            'common.modal.label.signing.open.bankid': 'Om ditt BankID säkerhetsprogram inte öppnas automatiskt kan du behöva starta det själv.',
            'common.modal.label.signing.open.mobilt.bankid': 'Starta mobilt BankID på den enhet du har det installerat.',
            'common.modal.label.signing.mobilt.noclient': 'Mobilt BankID-servern får ej kontakt med din enhet. Kontrollera att du har startat Mobilt BankID på din enhet.',
            'common.modal.label.signing.bankid.noclient': 'BankID-servern får ej kontakt med ditt BankID säkerhetsprogram. Kontrollera att du har startat BankID säkerhetsprogram på din dator.',


            'common.modal.label.print.sekretessmarkerad.title': 'Skriv ut intyg',
            'common.modal.label.print.sekretessmarkerad.title.draft': 'Skriv ut utkast',
            'common.modal.label.print.sekretessmarkerad.yes':  'Skriv ut',
            'common.modal.label.employee.title': 'Skriv ut minimalt intyg',
            'common.modal.label.employee.yes': 'Skriv ut',
            'common.modal.label.employee.body': 'Ett minimalt intyg innehåller endast de uppgifter som enligt sjuklönelagen är obligatoriska att delge arbetsgivaren. Detta kan ge arbetsgivaren sämre förutsättningar att stödja patientens rehabilitering och göra nödvändiga arbetsanpassningar.<br><br>Sveriges kommuner och landsting rekommenderar att patienten skickar in ett fullständigt läkarintyg till arbetsgivaren, alternativt skapar ett anpassat intyg på <LINK:minaintyg>.',

            'common.modal.confirm_handled.text': 'När ett intyg markeras som hanterad kan detta inte ångras senare.',
            'common.arende.confirm_handled.notification': 'Kompletteringsbegäran är markerad som hanterad.',


            // Text in copy and fornya (renew) modal dialogs
            'common.modal.copy.text': 'Vid kopiering skapas ett nytt intygsutkast med samma information som i det ursprungliga intyget. Uppgifterna i ett kopierat intygsutkast går att ändra innan det signeras. Om intyget är utfärdat på en annan enhet kommer det nya utkastet utfärdas på den enhet du är inloggad på. I de fall patienten har ändrat namn eller adress så uppdateras den informationen.',

            'common.modal.fornya.text': 'Förnya intyg kan användas vid förlängning av en sjukskrivning. När ett intyg förnyas skapas ett nytt intygsutkast med viss information från det ursprungliga intyget. Uppgifterna i det nya intygsutkastet går att ändra innan det signeras. Om intyget är utfärdat på en annan enhet kommer det nya utkastet utfärdas på den enhet du är inloggad på.<br><br>' +
                'De uppgifter som inte kommer med till det nya utkastet är:<br><br>' +
                '<ul><li>Information om sjukskrivningsperiod.</li>' +
                '<li>Valet om man vill ha kontakt med Försäkringskassan.</li>' +
                '<li>Referenser som intyget baseras på.</li></ul>',

            'common.modal.marked.ready.notification.sent': 'Observera att utkastet saknar obligatoriska uppgifter. Om du inte kan fylla i mer information kan du ändå markera intyget som klart för signering. Läkaren kommer då behöva komplettera intyget med de saknade uppgifterna innan det går att signera.',

            // Messages for user survey
            'wc.modal.survey.title': 'Användarundersökning',
            'wc.modal.survey.text': 'Vi genomför en användarundersökning för intygsapplikationen Webcert och vore mycket tacksamma om du kan tänka dig att dela dina synpunkter med oss. Undersökningen tar några minuter att fylla i och är helt anonym. Du kan närsomhelst avbryta undersökningen om så önskas. \n' +
                'Vill du hjälpa oss att förbättra tjänsten genom ditt deltagande?',

            // common intyg view messages
            'common.label.ovanstaende-har-bekraftats': 'Ovanstående uppgifter och bedömningar bekräftas',
            'common.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',

            // intyg status messages
            'intyg.status.has.komplettering': 'Försäkringskassan har begärt kompletteringar på intyget.',
            'intyg.status.draft_incomplete': 'Utkast, uppgifter saknas',
            'intyg.status.draft_complete': 'Utkast, kan signeras',
            'intyg.status.draft_locked': 'Utkast, låst',
            'intyg.status.draft_locked_cancelled': 'Makulerat',
            'intyg.status.signed': 'Signerat',
            'intyg.status.cancelled': 'Makulerat',
            'intyg.status.unknown': 'Okänd',
            'intyg.status.sent': 'Skickat',
            'intyg.status.notsent': 'Ej skickat',
            'intyg.status.received': 'Signerat',
            'intyg.status.kompletterat_av_intyg': 'Kompletterat',
            'intyg.status.ersatt_av_intyg': 'Ersatt',

            'intyg.status.patient.name.pu-intyg.changed': 'Patientens namn har ändrats',
            'intyg.status.patient.address.pu-intyg.changed': 'Patientens adress har ändrats',
            'intyg.status.patient.name.pu-integration.changed': 'Patientens namn skiljer sig från det i journalsystemet',

            'intyg.status.patient.name.pu-intyg.changed.modalheader': 'Patientens namn har ändrats',
            'intyg.status.patient.name.pu-intyg.changed.modalbody': '<p>Patientens namn har ändrats hos Personuppgiftstjänsten sedan det här intyget utfärdades.</p>',
            'intyg.status.patient.address.pu-intyg.changed.modalheader': 'Patientens adress har ändrats',
            'intyg.status.patient.address.pu-intyg.changed.modalbody': '<p>Patientens adress har ändrats hos Personuppgiftstjänsten sedan det här intyget utfärdades.</p>',
            'intyg.status.patient.name.pu-integration.changed.modalheader': 'Patientens namn skiljer sig',
            'intyg.status.patient.name.pu-integration.changed.modalbody': '<p>Patientens namn som visas i intyget har hämtats från Personuppgiftstjänsten och skiljer sig från det som är lagrat i journalsystemet.</p>',

            'intyg.status.patient.ps-007.modalheader': 'Patientens samordningsnummer',
            'intyg.status.patient.ps-007.modalbody': 'Om ett intyg skapas utifrån detta intyg kommer det nya intyget skrivas på samordningsnumret.',

            'intyg.status.patient.ps-008' : 'Adress från nationella personuppgiftstjänsten',
            'intyg.status.patient.ps-008.modalheader': 'Ingen adress angavs av journalsystemet',
            'intyg.status.patient.ps-008.modalbody': 'Journalsystemet angav inga adressuppgifter för patienten, därför har en slagning i den nationella personuppgiftstjänsten genomförts. Det är adressen som finns registrerad där som nu visas i intyget.',

            // common intyg replacement text
            'intyg.modal.ersatt.utkast.finns.text.info': 'Om intyget innehåller ett allvarligt fel, till exempel om det är utfärdat på fel patient, bör du istället makulera intyget.',
            'intyg.modal.ersatt.utkast.finns.text': 'Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt skapas ett utkast, med samma information som i det ursprungliga intyget, som du kan redigera innan du signerar intyget.',

            // Revoke status messages (type agnostic)
            'intyg.status.revoke.requested': '<strong>Status:</strong> Intyget är makulerat. <br><br>Intyget är inte längre tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>.',
            'intyg.status.revoke.confirmed': '<strong>Status:</strong> Intyget är makulerat. <br><br>Intyget är inte längre tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>.',

            // draft utkast header form status messages
            'draft.status.incomplete': '<strong>Status:</strong> Utkastet är sparat, men obligatoriska uppgifter saknas.',
            'draft.status.complete': '<strong>Status:</strong> Utkastet är sparat och kan signeras.',
            'draft.status.signed': '<strong>Status:</strong> Intyget är signerat.',
            'draft.status.changed': '<strong>Status:</strong> Utkastet är ändrat sedan det senast sparades',

            // intyg forms
            'draft.saknar-uppgifter': 'Utkastet saknar uppgifter i följande avsnitt:',
            'draft.signingdoctor': 'Signerande läkare',
            'draft.helptext.signingdoctor': 'Den läkare som avses signera intyget anges här.',
            'draft.notifydoctor': 'Skicka ett mejl med en länk till utkastet till den läkare som ska signera.',

            'draft.notify.check-missing': 'Visa vad som saknas',
            'draft.notify.button': 'Vidarebefordra utkast',
            'draft.notify.button.tooltip': 'Skapar ett e-postmeddelande i din e-postklient med en direktlänk till utkastet.',
            'draft.notify.hide-missing.button': 'Dölj information som saknas',

            'draft.notifyready.button': 'Markera klart för signering ',
            'draft.notifyready.button.tooltip': 'Utkastet markeras som klar för signering.',
            'draft.notifyready.text': 'Utkastet är sparat och markerat klart för signering.',
            'modal.title.notifyjournalsystem': 'Markera utkast klart för signering',


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

            'qa.amne.paminnelse': 'Påminnelse',
            'qa.amne.arbetstidsforlaggning': 'Arbetstidsförläggning',
            'qa.amne.kontakt': 'Kontakt',
            'qa.amne.avstamningsmote': 'Avstämningsmöte',
            'qa.amne.komplettering_av_lakarintyg': 'Komplettering av läkarintyg',
            'qa.amne.komplt': 'Komplettering av läkarintyg',
            'qa.amne.makulering_av_lakarintyg': 'Makulering av läkarintyg',
            'qa.amne.ovrigt': 'Övrigt',

            // Ärendehantering
            'common.loading.existing.arenden': 'Laddar ärenden...',
            'common.arende.komplettering.disabled.onlydoctor': 'Kompletteringar kan endast besvaras av läkare.',
            'common.arende.komplettering.kompletteringsatgard.dialogtitle': 'Kan ej komplettera',

            'common.arende.komplettering.uthopp.link': 'Hur kompletterar jag ett intyg?',
            'common.arende.komplettering.uthopp.modal.header': 'Hur kompletterar jag ett intyg?',
            'common.arende.komplettering.uthopp.modal.body': '<p>Kopiera/förnya det befintliga intyget i journalsystemet och komplettera med den nya informationen.</p><p>Kan ingen ytterligare information anges så ska det motiveras i fältet "Övriga upplysningar"</p>',

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

            'common.arende.svar.besvaradmedintyg': 'Kompletteringsbegäran besvarades med ett nytt intyg.',
            'common.arende.svar.go': 'Öppna intyget',
            'common.arende.svar.komplettering.help': 'Beskriv varför kompletteringen inte kan genomföras. Här får ingen medicinsk information anges.',

            // Används bara av fk7263 (ArendeLegacyProxy)
            'common.arende.fraga.amne.makulering_av_lakarintyg': 'Makulering av läkarintyg',
            'common.arende.fraga.amne.arbetstidsforlaggning': 'Arbetstidsförläggning',

            'common.arende.help.kompletteringar': 'Kompletteringar visar den information som Försäkringskassan begärt ska korrigeras på intyget detta utkast ska komplettera.',
            'common.arende.help.handled': 'Tidigare frågor och svar är sådana som redan har besvarats eller hanterats på något sätt.',

            'common.arende.draft.normal': '',
            'common.arende.draft.dirty': '',
            'common.arende.draft.saving': '<i class="icon-wc-ikon-02 animate-spin"></i> Utkast sparas',
            'common.arende.draft.saved': '<i class="material-icons">check</i> Utkast sparat',
            'common.arende.draft.failed': 'Utkastet kunde inte sparas på grund av ett tekniskt fel.',
            'common.arende.draft.delete.answer.title': 'Radera påbörjat svar',
            'common.arende.draft.delete.answer.body': 'Är du säker på att du vill radera ditt påbörjade svar?',
            'common.arende.draft.delete.question.title': 'Radera påbörjad fråga',
            'common.arende.draft.delete.question.body': 'Är du säker på att du vill radera din påbörjade fråga?',
            'common.arende.draft.delete.yes': 'Ja, radera',
            'common.arende.draft.error.chosenTopic': 'Ange en rubrik för att kunna skicka frågan.',
            'common.arende.draft.error.frageText': 'Skriv ett meddelande för att kunna skicka frågan.',

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
            'common.postnummer': 'Postnummer',
            'common.postort': 'Postort',
            'common.telefonnummer': 'Telefonnummer',

            //////////////////////////////////////////////////////////////////////////////////////////////////
            // Recipients
            //////////////////////////////////////////////////////////////////////////////////////////////////

            'common.recipient.fkassa': 'Försäkringskassan',
            'common.recipient.transp': 'Transportstyrelsen',
            'common.recipient.skv': 'Skatteverket',
            'common.recipient.sos': 'Socialstyrelsen',
            'common.recipient.af': 'Arbetsförmedlingen',
            'common.recipient.ag': 'Intygstjänsten',
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
            'common.load.signed-certificates-list': 'Hämtar signerade intyg ..',

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
            'tstrk1062.button.send': 'Skicka till Transportstyrelsen',
            'tstrk1062.label.send': 'Skicka intyg till Transportstyrelsen',

            'af00213.button.send': 'Skicka till Arbetsförmedlingen',
            'af00213.label.send': 'Skicka intyg till Arbetsförmedlingen',

            'af00251.button.send': 'Skicka till Arbetsförmedlingen',
            'af00251.label.send': 'Skicka intyg till Arbetsförmedlingen',

            // Send fk
            'luse.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Försäkringskassans system vilket ska göras i samråd med patienten.',
            'luae_na.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Försäkringskassans system vilket ska göras i samråd med patienten.',
            'luae_fs.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Försäkringskassans system vilket ska göras i samråd med patienten.',

            // send ts
            'ts-bas.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Transportstyrelsens system vilket ska göras i samråd med patienten.',
            'ts-diabetes.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Transportstyrelsens system vilket ska göras i samråd med patienten.',
            'tstrk1062.label.send.body': 'Om du går vidare kommer intyget skickas direkt till Transportstyrelsens system vilket ska göras i samråd med patienten.',

            // Send w sjukpenning
            'common.label.send.body': '<p>Intyget ska skickas till Försäkringskassan från dag 8 i sjukperioden om patienten är:</p>' +
                '<ul>' +
                '<li>Egenföretagare</li>' +
                '<li>Arbetssökande</li>' +
                '<li>Anställd men arbetsgivaren betalar inte ut sjuklön</li>' +
                '<li>Studerande och arbetar med rätt till sjukpenning (tjänar mer än 10 700 per år)</li>' +
                '<li>Ledig med föräldrapenning</li>' +
                '<li>Ledig med graviditetspenning</li>' +
                '</ul>',
            'lisjp.label.send.body': '<p>Om du går vidare kommer intyget skickas direkt till Försäkringskassans system vilket ska göras i samråd med patienten.</p>Upplys patienten om att även göra en ansökan om sjukpenning hos Försäkringskassan.',
            'fk7263.label.send.body': '<p>Om du går vidare kommer intyget skickas direkt till Försäkringskassans system vilket ska göras i samråd med patienten.</p>Upplys patienten om att även göra en ansökan om sjukpenning hos Försäkringskassan.',

             // Send AF
            'af00213.label.send.body': 'Om du går vidare kommer intyget att skickas till Arbetsförmedlingens system, vilket ska göras i samråd med patienten.',
            'af00251.label.send.body': 'Om du går vidare kommer intyget att skickas till Arbetsförmedlingens system, vilket ska göras i samråd med patienten.',

            'sjukpenning.label.send.obs.short.duration': 'Om sjukperioden är kortare än 15 dagar ska intyget inte skickas till Försäkringskassan utom i vissa undantagsfall.',

            'common.label.utkastcreated': 'Utkastet skapades',
            'common.label.showallstatuses': 'Visa alla händelser',

            'common.label.intygstatus.is-001': 'Intyget är signerat',
            'common.label.intygstatus.is-002': 'Intyget är skickat till ${recipient}',
            'common.label.intygstatus.is-003': 'Intyget har ersatts av <a href="#/intyg/${intygstyp}/${intygTypeVersion}/${intygsid}/?forcelink">detta intyg</a>',
            'common.label.intygstatus.is-004': 'Intyget är makulerat',
            'common.label.intygstatus.is-005': 'Intyget har kompletterats med ett annat intyg. <a href="#/intyg/${intygstyp}/${intygTypeVersion}/${intygsid}/?forcelink">Öppna intyget</a>',
            'common.label.intygstatus.is-006': 'Försäkringkassan har begärt komplettering',
            'common.label.intygstatus.is-007': 'Intyget ersatte ett tidigare intyg som också kan behöva makuleras. <a href="#/intyg/${intygstyp}/${intygTypeVersion}/${intygsid}/?forcelink">Öppna intyget</a>',
            'common.label.intygstatus.is-008': 'Intyget är tillgängligt för patienten',
            'common.label.intygstatus.is-009': 'Det finns redan ett påbörjat utkast som ska ersätta detta intyg. <a href="#/${intygstyp}/${intygTypeVersion}/edit/${intygsid}/?forcelink">Öppna utkastet</a>',
            'common.label.intygstatus.is-010': 'Intyget är förnyat utifrån ett tidigare intyg som också kan behöva makuleras. <a href="#/intyg/${intygstyp}/${intygTypeVersion}/${intygsid}/?forcelink">Öppna intyget</a>',
            'common.label.intygstatus.is-011': 'Intyget är en komplettering av ett tidigare intyg som också kan behöva makuleras. <a href="#/intyg/${intygstyp}/${intygTypeVersion}/${intygsid}/?forcelink">Öppna intyget</a>',
            'common.label.intygstatus.is-012': 'Det finns redan en påbörjad komplettering. <a href="#/${intygstyp}/${intygTypeVersion}/edit/${intygsid}/?forcelink">Öppna utkastet</a>',
            // Utkast status
            'common.label.intygstatus.is-013': 'Utkastet sparas',
            'common.label.intygstatus.is-014': 'Utkastet är sparat',
            'common.label.intygstatus.is-015': 'Obligatoriska uppgifter saknas',
            'common.label.intygstatus.is-016': 'Klart att signera',

            'common.label.intygstatus.lus-01': 'Utkastet är låst',
            'common.label.intygstatus.lus-02': 'Utkastet är makulerat',

            'common.label.intygstatus.us-006': 'Utkastet är skapat',
            'common.label.intygstatus.us-007': 'Utkastet är skapat för att komplettera ett tidigare intyg. <a href="#/intyg/${intygstyp}/${intygTypeVersion}/${intygsid}/?forcelink">Öppna intyget</a>',
            'common.label.intygstatus.us-008': 'Utkastet är skapat för att förnya ett tidigare intyg. <a href="#/intyg/${intygstyp}/${intygTypeVersion}/${intygsid}/?forcelink">Öppna intyget</a>',
            'common.label.intygstatus.us-009': 'Utkastet är skapat för att ersätta ett tidigare intyg. <a href="#/intyg/${intygstyp}/${intygTypeVersion}/${intygsid}/?forcelink">Öppna intyget</a>',
            'common.label.intygstatus.us-010': 'Utkastet är skapat som en kopia på ett låst utkast. <a href="#/${intygstyp}/${intygTypeVersion}/edit/${intygsid}/?forcelink">Öppna utkastet</a>',

            'common.modalheader.intygstatus.is-004': 'Intyget är makulerat',
            'common.modalbody.intygstatus.is-004': 'Intyget är inte längre tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>.',
            'common.modalheader.intygstatus.lus-01': 'Utkastet är låst',
            'common.modalbody.intygstatus.lus-01': '<p>Det har gått fler än fjorton dagar sedan det här utkastet skapades. Det har därför låsts.</p><p>Intyg, inklusive utkast, betraktas som journalhandlingar vilket innebär att Patiendatalagen och Socialstyrelsens föreskrifter om journalföring gäller för alla intyg och utkast som hanteras av Webcert.</p>',

            'db.modalbody.intygstatus.is-004': '',
            'doi.modalbody.intygstatus.is-004': '',

            'common.modalheader.intygstatus.is-008': 'Intyget är tillgängligt för patienten',
            'common.modalbody.intygstatus.is-008': '<p>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg></p><p>Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten.</p>',
            'fk7263.modalbody.intygstatus.is-008': '<p>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg></p><p>Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om sjukpenning.</p><p>Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten.</p>',
            'lisjp.modalbody.intygstatus.is-008': '<p>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg></p><p>Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om sjukpenning.</p><p>Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten.</p>',

            // Status - signed but not sent
            'common.label.status.signed.patient-dead': 'Intyget är signerat.<br>',
            // Same status for fk7263 & lisjp
            'fk7263.label.status.signed.patient-alive': 'Intyget är signerat.<br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>. Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om sjukpenning. Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten.',
            'lisjp.label.status.signed.patient-alive': 'Intyget är signerat.<br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>. Intyget går även att nå via Försäkringskassans e-tjänst för ansökan om sjukpenning. Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten.',
            // Same status for luse, luae_na, luae_fs and both ts
            'luse.label.status.signed.patient-alive': 'Intyget är signerat.<br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>. Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten. ',
            'luae_na.label.status.signed.patient-alive':  'Intyget är signerat.<br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>. Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten. ',
            'luae_fs.label.status.signed.patient-alive':  'Intyget är signerat.<br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>. Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten. ',
            'ts-bas.label.status.signed.patient-alive':  'Intyget är signerat.<br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>. Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten. ',
            'ts-diabetes.label.status.signed.patient-alive':  'Intyget är signerat.<br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>. Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten. ',
            'tstrk1062.label.status.signed.patient-alive':  'Intyget är signerat.<br><br>Intyget är tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>. Om patienten inte har möjlighet att skicka intyget elektroniskt till ${recipient} kan du skicka intyget direkt till ${recipient} åt patienten. ',

            // Status - signed and sent
            'common.label.status.sent.patient-dead': 'Intyget är signerat och har skickats till ${recipient}s system.',
            'common.label.status.sent.patient-alive': 'Intyget är signerat och har skickats till ${recipient}s system.<br><br>Intyget är nu tillgängligt för patienten i Mina intyg, som nås via <LINK:minaintyg>.',
            'db.label.status.sent': 'Dödsbeviset är signerat och har nu skickats till Skatteverket.<br><br>Glöm inte att göra en journalanteckning att dödsbevis är inlämnat!<br><br>Du kan nu avsluta Webcert eller direkt skriva ett dödsorsaksintyg för samma person genom att trycka på knappen "Skriv dödsorsaksintyg" ovan.',
            'doi.label.status.sent': 'Dödsorsaksintyget är signerat och har nu skickats till Socialstyrelsen.<br><br>Glöm inte att göra en journalanteckning att dödsorsaksintyg är inlämnat!',


            // Makulera
            'luse.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
            'luae_na.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
            'luae_fs.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
            'lisjp.label.makulera.confirmation': 'Läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
            'ts-bas.label.makulera.confirmation': 'Transportstyrelsens läkarintyg utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
            'ts-diabetes.label.makulera.confirmation': 'Transportstyrelsens läkarintyg, diabetes, utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',
            'tstrk1062.label.makulera.confirmation': 'Transportstyrelsens läkarintyg, diabetes, utställd på ${namn} – ${personnummer} är återtaget. Mottagare av intyget är notifierade om detta.',


            // Validation messages
            'luse.validation.grund-for-mu.incorrect_combination_annat_beskrivning': 'Fritextfältet som hör till alternativet Annat under \'Uppgifterna i intyget baseras på\' får endast fyllas i om \'Annan referens valts\'.',
            'luse.validation.grund-for-mu.kannedom.after': 'Ange ett datum, tidigare än eller samma som för "{0}".',
            'luse.validation.underlagfinns.incorrect_combination': 'Om frågan \'Finns det andra medicinska utredningar eller underlag\' besvarats med nej får underlag inte anges.',
            'luse.validation.underlag.too_many': 'För många underlag. Detta fel kan ej åtgärdas av användaren. Påbörja ett nytt utkast och rapportera felet till <LINK:ineraKundserviceAnmalFel>.',
            'luse.validation.underlag.missing': 'Välj ett alternativ.',
            'luse.validation.underlag.incorrect_format': 'Fel typ av underlag.',
            'luse.validation.underlag.date.missing': 'Ange ett datum.',
            'luse.validation.underlag.hamtas-fran.missing': 'Ange ett svar.',
            'luse.validation.diagnosfornybedomning.incorrect_combination': 'Om \'Finns det skäl att göra en ny bedömning av diagnosen?\' besvarats med \'Nej\' får inte \'Diagnos för ny bedömning\' fyllas i.',
            'luse.validation.kontakt.incorrect_combination': 'Anledning till kontakt kan endast fyllas i om \'kontakt med FK önskas\' är vald.',

            'luae_na.validation.grund-for-mu.incorrect_combination_annat_beskrivning': 'Fritextfältet som hör till alternativet Annat under \'Uppgifterna i intyget baseras på\' får endast fyllas i om \'Annan referens\' valts.',
            'luae_na.validation.grund-for-mu.kannedom.after': 'Ange ett datum, tidigare än eller samma som för "{0}".',
            //'luae_na.validation.grund-for-mu.kannedom.after.undersokning': '\'Kännedom om patienten\' får inte vara senare än datum för \'Min undersökning av patienten\'.',
            //'luae_na.validation.grund-for-mu.kannedom.after.anhorigsbeskrivning': '\'Kännedom om patienten\' får inte vara senare än datum för \'Anhörigs beskrivning av patienten\'.',
            'luae_na.validation.underlagfinns.incorrect_combination': 'Om frågan \'Finns det andra medicinska utredningar eller underlag\' besvarats med nej får underlag inte anges.',
            'luae_na.validation.underlag.missing': 'Välj ett alternativ.',
            'luae_na.validation.underlag.incorrect_format': 'Fel typ av underlag.',
            'luae_na.validation.underlag.date.missing': 'Ange ett datum.',
            'luae_na.validation.underlag.hamtas-fran.missing': 'Ange ett svar.',
            'luae_na.validation.diagnosfornybedomning.incorrect_combination': 'Om \'Finns det skäl att göra en ny bedömning av diagnosen?\' besvarats med \'Nej\' får inte \'Diagnos för ny bedömning\' fyllas i.',
            'luae_na.validation.kontakt.incorrect_combination': 'Anledning till kontakt kan endast fyllas i om \'kontakt med FK önskas\' är vald.',

            'luae_fs.validation.grund-for-mu.annat.beskrivning.invalid_combination': 'Om inte alternativet Annat är angett får inte beskrivningsfältet anges.',
            'luae_fs.validation.underlagfinns.incorrect_combination': 'Om frågan \'Finns det andra medicinska utredningar eller underlag\' besvarats med nej får underlag inte anges.',
            'luae_fs.validation.grund-for-mu.kannedom.after': 'Ange ett datum, tidigare än eller samma som för "{0}".',
            'luae_fs.validation.underlag.missing': 'Välj ett alternativ.',
            'luae_fs.validation.underlag.incorrect_format': 'Fel typ av underlag.',
            'luae_fs.validation.underlagfinns.missing': 'Frågan \'Finns det andra medicinska utredningar eller underlag\' måste besvaras.',
            'luae_fs.validation.underlag.date.missing': 'Ange ett datum.',
            'luae_fs.validation.underlag.hamtas-fran.missing': 'Ange ett svar.',
            'luae_fs.validation.kontakt.invalid_combination': 'Anledning får inte fyllas i när man svarat nej på kontakt.',
            'luae_fs.validation.diagnos.max-diagnoser': 'Du kan endast ange upp till tre st diagnoser.',
            'luae_fs.validation.funktionsnedsattning.debut.missing': 'Ange ett svar.',
            'luae_fs.validation.funktionsnedsattning.paverkan.missing': 'Ange ett svar.',
            'luae_fs.validation.blanksteg.otillatet': 'Fältet får inte fyllas i med endast blanksteg.',

            'ts-diabetes.validation.blanksteg.otillatet': 'Fältet får inte fyllas i med endast blanksteg.',

            'lisjp.validation.grund-for-mu.annat.beskrivning.invalid_combination': 'annat beskrivning invalid combination.',  // Should never happen because GUI should block this combination
            'lisjp.validation.kontakt.invalid_combination': 'Anledning får inte fyllas i när man svarat nej på kontakt.',  // Should never happen because GUI should block this combination
            'lisjp.validation.blanksteg.otillatet': 'Fältet får inte fyllas i med endast blanksteg.',  // Should never happen because GUI should block this combination
            'lisjp.validation.funktionsnedsattning.missing': 'Ange ett svar.',
            'lisjp.validation.aktivitetsbegransning.missing': 'Ange ett svar.',

            // errors
            'sjukersattning.error.could_not_load_cert': '<strong>Intyget kunde inte visas</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'sjukersattning.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
            'sjukersattning.error.could_not_load_cert_qa': '<strong>Ärendekommunikation för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.</strong>',
            'luae_na.error.could_not_load_cert': '<strong>Intyget kunde inte visas</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'luae_na.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
            'luae_na.error.could_not_load_cert_qa': '<strong>Ärendekommunikation för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.</strong>',
            'luae_fs.error.could_not_load_cert': '<strong>Intyget kunde inte visas</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'luae_fs.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
            'luae_fs.error.could_not_load_cert_qa': '<strong>Ärendekommunikation för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.</strong>',
            'lisjp.error.could_not_load_cert': '<strong>Intyget kunde inte visas</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'lisjp.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
            'lisjp.error.could_not_load_cert_qa': '<strong>Ärendekommunikation för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.</strong>',
            'fk7263.error.could_not_load_cert': '<strong>Intyget kunde inte visas</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'fk7263.error.could_not_load_cert_not_auth': '<strong>Kunde inte hämta intyget eftersom du saknar behörighet.</strong>',
            'fk7263.error.could_not_load_cert_qa': '<strong>Ärendekommunikation för detta intyg gick inte att läsa in. Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.</strong>',

            // PU down, cannot print cert
            'fk7263.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'lisjp.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'luse.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'luae_na.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'luae_fs.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'ts-bas.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'ts-diabetes.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'tstrk1062.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'db.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'doi.error_could_not_print_cert_no_pu': '<strong>Det går inte att skriva ut intyget!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut intyget. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',

            // Could not sign
            'db.error.sign.intyg_of_type_exists.same_vardgivare': 'Dödsbevis för detta personnummer har utfärdats av någon annan under tiden du har arbetat med detta utkast. Du kan inte signera detta dödsbevis men kan däremot välja att ersätta det befintliga dödsbeviset.',
            'db.error.sign.intyg_of_type_exists.same_vardgivare.title': 'Dödsbevis har utfärdats under tiden',
            'db.error.sign.intyg_of_type_exists.other_vardgivare': 'Dödsbevis för detta personnummer har utfärdats av någon annan hos annan vårdgivare under tiden du har arbetat med detta utkast. Det är inte möjligt att signera detta dödsbevis.',
            'db.error.sign.intyg_of_type_exists.other_vardgivare.title': 'Dödsbevis har utfärdats under tiden hos annan vårdgivare',
            'doi.error.sign.intyg_of_type_exists.same_vardgivare': 'Dödsorsaksintyg för detta personnummer har utfärdats av någon annan under tiden du har arbetat med detta utkast. Du kan inte signera detta dödsorsaksintyg men kan däremot välja att ersätta det befintliga dödsorsaksintyget.',
            'doi.error.sign.intyg_of_type_exists.same_vardgivare.title': 'Dödsorsaksintyg har utfärdats under tiden',
            'doi.error.sign.intyg_of_type_exists.other_vardgivare': 'Dödsorsaksintyg för detta personnummer har utfärdats av någon annan hos annan vårdgivare under tiden du har arbetat med detta utkast. Senast skapade dödsorsaksintyg är det som gäller. Om du fortsätter och lämnar in dödsorsaksintyget så blir det därför detta dödsorsaksintyg som gäller.',
            'doi.error.sign.intyg_of_type_exists.other_vardgivare.title': 'Dödsorsaksintyg har utfärdats under tiden hos annan vårdgivare',

            // PU down, cannot print draft
            'common.error_could_not_print_draft_no_pu': '<strong>Det går inte att skriva ut utkastet!</strong><br><br>På grund av tekniskt fel går det inte att skriva ut utkastet. Försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            // Generic PU-error.
            'common.error.pu_problem.title': '<strong>Personuppgiftstjänsten svarar inte</strong>',
            'common.error.pu_problem': 'Personuppgiftstjänsten svarar inte. Åtgärden kan inte genomföras eftersom den kräver att personuppgifter kan hämtas från personuppgiftsregistret. Prova igen om en stund.<br><br>Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',

            // PU down, cannot send
            'fk7263.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'lisjp.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'luse.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'luae_na.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'luae_fs.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'ts-bas.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'ts-diabetes.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'tstrk1062.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.<br><br>Patienten kan själv gå in på minaintyg.se för att skriva ut och skicka intyget elektroniskt.',
            'db.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'doi.error_could_not_send_cert_no_pu': '<strong>Det går inte att skicka intyget!</strong><br><br>På grund av tekniskt fel kan du för tillfället inte skicka intyget, försök igen om en liten stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',


            // fragaSvar errors
            'luae_na.error.external_system_problem': '<strong>Meddelandet har inte skickats</strong><br>Webcert saknar kontakt med Försäkringskassans datasystem.<br><br>Prova igen om en stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'luae_na.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'luae_na.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
            'luae_na.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'luae_na.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
            'luae_na.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',
            'sjukersattning.error.external_system_problem': '<strong>Meddelandet har inte skickats</strong><br>Webcert saknar kontakt med Försäkringskassans datasystem.<br><br>Prova igen om en stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'sjukersattning.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'sjukersattning.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
            'sjukersattning.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'sjukersattning.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
            'sjukersattning.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',
            'luae_fs.error.external_system_problem': '<strong>Meddelandet har inte skickats</strong><br>Webcert saknar kontakt med Försäkringskassans datasystem.<br><br>Prova igen om en stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'luae_fs.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'luae_fs.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
            'luae_fs.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'luae_fs.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
            'luae_fs.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',
            'lisjp.error.external_system_problem': '<strong>Meddelandet har inte skickats</strong><br>Webcert saknar kontakt med Försäkringskassans datasystem.<br><br>Prova igen om en stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'lisjp.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'lisjp.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
            'lisjp.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'lisjp.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
            'lisjp.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',

            //////////////////////////////////////////////////////////////////////////////////////////////////
            // END WIP module specific strings
            //////////////////////////////////////////////////////////////////////////////////////////////////

            'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning': 'Motivering till varför det medicinska underlaget inte baseras på en undersökning av patienten',
            'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning.help': 'Observera att detta inte är en fråga från Försäkringskassan. Information om varför sjukskrivningen startar mer än en vecka före dagens datum kan vara till hjälp för Försäkringskassan i deras handläggning.\nInformationen du anger nedan, kommer att överföras till fältet "{0}" vid signering.',

            //common.sit.* messages are candidates for a fk-common messages.js
            'common.sit.label.sjukskrivning.hoursperweek.1': 'Patienten arbetar i snitt',
            'common.sit.label.sjukskrivning.hoursperweek.2': 'timmar/vecka',
            'common.sit.help.sjukskrivning.hoursperweek': 'Ange hur många timmar patienten arbetar i snitt per vecka. Observera att denna funktion endast är ett stöd för att tydliggöra hur många timmar per vecka patienten bedöms kunna arbeta när en viss nedsättning av arbetsförmåga har angivits. Uppgiften lagras inte som en del av intyget då Försäkringskassan inhämtar information från annat håll.',
            'common.sit.label.valj-version-icd-10': 'Välj kodverk:',
            'common.sit.label.diagnoskodverk.fullstandig': 'ICD-10-SE',
            'common.sit.label.diagnoskodverk.primarvard': 'KSH97-P (Primärvård)',
            'common.sit.client-validation.underlag.max-extra-underlag': 'Du kan inte lägga till fler utredningar, max antal är %0st',
            'common.label.patient': 'Patientens adressuppgifter',
            'common.label.vardenhet': 'Vårdenhetens adress',
            'common.validation.patient.postadress.missing': 'Ange postadress.',
            'common.validation.patient.postnummer.missing': 'Ange postnummer.',
            'common.validation.patient.postort.missing': 'Ange postort.',
            'common.validation.patient.telefon.missing': 'Ange telefonnummer.',
            'common.validation.postnummer.incorrect-format': 'Ange postnummer med fem siffor.',
            'common.validation.funktionsnedsattning.empty': 'Beskriv minst en funktionsnedsättning.',

            'common.label.year': 'År',
            'common.label.month': 'Månad',
            'common.label.day': 'Dag',
            'common.label.date': 'Datum',
            'common.label.choose': 'Välj...',

            // Standard valideringsmeddelande UE komponenter

            'common.validation.ue-checkbox-date.empty': 'Ange ett svar.',
            'common.validation.ue-year-picker.empty': 'Ange ett år.',
            'common.validation.ue-year-picker.invalid_format': 'Ange ett år på formatet åååå.',
            'common.validation.ue-checkbox-date.invalid_format': 'Ange datum i formatet åååå-mm-dd.',
            'common.validation.ue-checkgroup.empty': 'Välj minst ett alternativ.',
            'common.validation.ue-checkgroup-disabled.empty': 'Välj minst ett alternativ.',
            'common.validation.ue-checkgroup.incorrect_combination': 'Välj ett alternativ.',
            'common.validation.ue-radio.empty': 'Välj ett alternativ.',
            'common.validation.ue-radio.incorrect_combination': 'Välj ett alternativ.',
            'common.validation.ue-radiogroup.empty': 'Välj ett alternativ.',
            'common.validation.ue-textarea.empty': 'Ange ett svar.',
            'common.validation.ue-textfield.empty': 'Ange ett svar.',
            'common.validation.ue-typeahead.empty': 'Ange ett svar.',
            'common.validation.ue-year.empty': 'Ange ett svar.',
            'common.validation.ue-dropdown.empty': 'Välj ett alternativ.',
            'common.validation.ue-fraga.empty': 'Frågan är obligatorisk att besvara.',
            'common.validation.ue-underlag.empty': 'Välj ett alternativ.',
            'common.validation.ue-date.empty': 'Ange ett datum.',
            'common.validation.ue-date.invalid_format': 'Ange datum i formatet åååå-mm-dd.',
            'common.validation.ue-prognos.empty': 'Välj ett alternativ.',
            'common.validation.ue-checkgroup-ts.empty': 'Välj minst ett alternativ.',
            'common.validation.ue-synskarpa.empty': 'Ange ett svar.',
            'common.validation.ue-synskarpa.invalid_format': 'Ange synskärpa i intervallet 0,0 - 2,0.',
            'common.validation.ue-syn.empty': 'Välj ett alternativ.',
            'common.validation.ue-korkort-bedomning.empty': 'Välj ett alternativ.',
            'common.validation.ue-vaguedate.empty.year': 'Ange år.',
            'common.validation.ue-vaguedate.empty.month': 'Ange månad.',
            'common.validation.ue-vaguedate.invalid_format': 'åååå måste anges, månad och dag kan anges som 00.',
            'common.validation.ue-dodsorsak-foljd.empty': '', // this message is handled by ueDodsorsakRow
            'common.validation.ue-percent-field.empty': 'Ange ett svar.',
            'common.validation.ue-sjukfranvaro.niva.empty': 'Ange ett svar.',
            'common.validation.ue-sjukfranvaro.niva.invalid_format': 'Ange omfattning i intervallet 1-100',
            'common.validation.ue-sjukfranvaro.period.invalid': 'Ange en datumperiod.',
            'common.validation.ue-sjukfranvaro.period.from.empty': 'Ange ett datum.',
            'common.validation.ue-sjukfranvaro.period.tom.empty': 'Ange ett datum.',
            'common.validation.ue-sjukfranvaro.period.from.invalid_format': 'Ange datum i formatet åååå-mm-dd.',
            'common.validation.ue-sjukfranvaro.period.tom.invalid_format': 'Ange datum i formatet åååå-mm-dd.',
            'common.validation.ue-sjukfranvaro.period.from.period_overlap': 'Frånvaroperioder med överlappande datum har angetts.',
            'common.validation.ue-sjukfranvaro.period.tom.period_overlap': 'Frånvaroperioder med överlappande datum har angetts.',
            'common.validation.ue-sjukfranvaro.period_overlap': 'Ange frånvaroperioder som inte överlappar varandra.',
            'common.validation.ue-sjukfranvaro.period.incorrect_combination': 'Ange ett slutdatum som infaller efter startdatumet.',
            'common.validation.ue-grid.empty': 'Ange ett svar.',

            // Specifika valideringsmeddelande från backend
            'common.validation.date.year.not_selected': 'Ange år och månad.',
            'common.validation.date.month.not_selected': 'Ange månad.',
            'common.validation.date-period.invalid_format': 'Felaktigt datumformat.',
            'common.validation.date-period.invalid_order': 'Ange ett slutdatum som infaller efter startdatumet.',
            'common.validation.date-period.period_overlap': 'Datumintervall överlappar.',
            'common.validation.date_out_of_range': 'Ange ett datum som inte ligger för långt fram eller tillbaka i tiden.',
            'common.validation.date_out_of_range_no_future': 'Ange ett datum som inte ligger för långt tillbaka i tiden.',
            'common.validation.date_invalid': 'Ange ett giltigt datum.',
            'common.validation.future.datum': 'Ange ett giltigt datum. Framtida datum ger inga resultat.',
            'common.validation.previous.datum': 'Ange ett giltigt datum. Datumet är för långt bak i tiden.',
            'common.validation.date.today.or.earlier': 'Ange dagens eller ett tidigare datum.',
            'common.validation.date.beforeLastYear': 'Ange ett datum, samma som eller senare än 1 januari föregående året.',
            'common.validation.diagnos.missing': 'Ange minst en diagnos.',
            'common.validation.diagnos.codemissing': 'Ange diagnoskod.',
            'common.validation.diagnos.invalid': 'Diagnoskod är ej giltig.',
            'common.validation.diagnos.length-3': 'Ange diagnoskod med så många positioner som möjligt, men minst tre positioner.',
            'common.validation.diagnos.psykisk.length-4': 'Ange diagnosrad med minst fyra positioner på en psykisk diagnos.',
            'common.validation.diagnos.description.missing': 'Ange diagnostext.',
            'common.validation.sjukskrivning_period.empty': 'Ange ett datum.',
            'common.validation.sjukskrivning_period.invalid_format': 'Ange datum i formatet åååå-mm-dd.',
            'common.validation.sjukskrivning_period.incorrect_combination': 'Ange ett slutdatum som infaller efter startdatumet.',
            'common.validation.sjukskrivning_period.period_overlap': 'Ange sjukskrivningsperioder som inte överlappar varandra.',
            'common.validation.sjukskrivning_period.en_fjardedel.invalid_format': 'Datum för nedsatt arbetsförmåga med 25% har angetts på felaktigt format.',
            'common.validation.sjukskrivning_period.halften.invalid_format': 'Datum för nedsatt arbetsförmåga med 50% har angetts på felaktigt format.',
            'common.validation.sjukskrivning_period.tre_fjardedel.invalid_format': 'Datum för nedsatt arbetsförmåga med 75% har angetts på felaktigt format.',
            'common.validation.sjukskrivning_period.helt_nedsatt.invalid_format': 'Datum för nedsatt arbetsförmåga med 100% har angetts på felaktigt format.',
            'common.validation.d-01': 'Ange vilket år behandling med insulin påbörjades.',
            'common.validation.d-02': 'Ange ett årtal mellan patientens födelsedatum och dagens datum.',
            'common.validation.d-03': 'Ange synskärpa i intervallet 0,0 - 2,0.',
            'common.validation.d-04': 'Ange ett datum inom det senaste året.',
            'common.validation.d-05': 'Ange ett år, senare än patientens födelseår.',
            'common.validation.d-06': 'Ange ett år, årets eller tidigare.',
            'common.validation.d-08': 'Ange dagens eller ett tidigare datum.',
            'common.validation.d-09': 'Ange ett datum som infaller under de senaste tolv månaderna',
            'common.validation.d-10': 'Ange ett datum som infaller under de senaste tre månaderna',
            'common.validation.d-11': 'Ange ett datum som infaller efter patientens födelsedatum.',
            'common.validation.d-12': 'Vald behörighet överensstämmer inte med vilken behörighet intyget avser.',
            'common.validation.c-05': 'Fyll i den översta raden först.',
            'common.validation.c-06': 'Ange ett giltigt datum. Framtida datum får inte anges.',
            'common.validation.c-13a': 'Ange diagnos på översta raden först.',
            'common.validation.b-04': 'Välj minst ett alternativ.',
            'common.validation.b-02b': 'Ange ett giltigt årtal.',
            'common.validation.b-03a': 'Ange ett svar.',

            // Common errors
            'common.error.unknown': '<strong>Tekniskt fel.</strong>',
            'common.error.authorization_problem': '<strong>Behörighet saknas.</strong><br>Du saknar behörighet att använda denna resurs.',
            'common.error.authorization_problem_sekretessmarkering': '<strong>Behörighet saknas</strong><br>För att hantera intyg för patienter med skyddade personuppgifter krävs att du har befattningen läkare eller tandläkare. Vissa intygstyper får inte hanteras alls för patienter med skyddade personuppgifter, även om du har befattningen som krävs.',
            'common.error.authorization_problem_sekretessmarkering_enhet': '<strong>Behörighet saknas</strong><br>Du saknar behörighet att hantera detta intyg. För att hantera ett intyg för en patient med skyddade personuppgifter måste du vara inloggad på den vårdenhet intyget skrevs.',
            'common.error.cantconnect': '<strong>Kunde inte kontakta servern.</strong>',
            'common.error.certificatenotfound': '<strong>Intyget finns inte.</strong>',
            'common.error.certificateinvalid': '<strong>Intyget är inte korrekt ifyllt.</strong>',
            'common.error.certificateinvalidstate': 'Signeringen kunde inte slutföras. Intyget har inte signerats. Prova att ladda om sidan och försök igen.<br>Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.error.certificate_revoked': '<strong>Operation är inte möjlig.</strong><br>Förmodligen har en annan användare makulerat intyget medan du arbetat på samma post. Ladda om sidan och försök igen',
            'common.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Ladda om sidan och försök igen. Kontakta supporten om problemet kvarstår.',
            'common.error.invalid_state': '<strong>Operation är inte möjlig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma utkast. Ladda om sidan och försök igen',

            'common.error.sign.general': 'Intyget har inte signerats. Detta beror antingen på ett tekniskt fel eller att signeringen avbrutits.<br><br>Prova igen om en stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.error.sign.only_possible_in_ie': 'Intyget har inte signerats.<br><br>Det kan bero på att du inte har förutsättningar för att signera i annan webbläsare än Internet Explorer. Kontakta din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.error.sign.netid': 'Intyget har inte signerats. Detta beror antingen på ett tekniskt fel eller att signeringen avbrutits.<br><br>Prova igen om en stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.error.sign.bankid': 'Intyget har inte signerats. Detta beror antingen på ett tekniskt fel eller att signeringen avbrutits.<br><br>Prova igen om en stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.error.sign.not_ready_yet': '<strong>Intyget kunde inte visas</strong><br>Intyget är signerat, men kunde inte visas, då det behandlas. Prova att ladda om sidan.<br><br>Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.error.sign.concurrent_modification': '<strong>Det går inte att signera utkastet.</strong><br/>Utkastet har ändrats av en annan användare sedan du började arbeta på det. Ladda om sidan, kontrollera att uppgifterna stämmer och försök signera igen.<br/>Utkastet ändrades av ${name}.',
            'common.error.sign.authorization': '<strong>Intyget kunde inte signeras.</strong><br/>Du saknar behörighet att signera detta intyg.',
            'common.error.sign.indeterminate.identity': '<strong>Intyget kunde inte signeras.</strong><br/>Det verkar som att du valt en annan identitet att signera med än den du loggade in med. Du måste identifiera dig på samma sätt som när du loggade in. Kontrollera om du har valt rätt och prova igen.',
            'common.error.sign.grp.already_in_progress': '<strong>Intyget kunde inte signeras.</strong><br/>En inloggning eller underskrift för det här personnumret är redan påbörjad, tryck avbryt i BankID säkerhetsapp och försök igen.',
            'common.error.sign.grp.cancel': '<strong>Intyget kunde inte signeras.</strong><br/>Åtgärden avbruten.',
            'common.error.sign.grp.expired_transaction': '<strong>Intyget kunde inte signeras.</strong><br/>Inget svar från klienten. Kontrollera att du har startat din BankID säkerhetsapp, följ instruktionerna och försök igen.',
            'common.error.unknown_internal_problem': '<strong>Tekniskt fel i Webcert.</strong><br>Försök igen senare.',
            'common.error.data_not_found': '<strong>Intyget kunde inte hittas.</strong><br>Intyget är borttaget eller så saknas behörighet.',
            'common.error.could_not_load_cert': '<strong>Intyget kunde inte visas</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.error.could_not_load_draft_not_auth': '<strong>Behörighet saknas</strong><br>Du saknar behörighet för att komma åt utkastet. För att få hjälp, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.error.could_not_load_cert_not_auth': '<strong>Behörighet saknas</strong><br>Du saknar behörighet för att komma åt intyget. För att få hjälp, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.error.could_not_load_cert_qa': '<strong>Ärenden kunde inte visas</strong>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.error.module_problem': '<strong>Tekniskt fel i Webcert.</strong><br>Problem att kontakta intygsmodulen.',
            'common.error.discard.concurrent_modification': '<strong><br>Kan inte ta bort utkastet. Utkastet har ändrats av en annan användare medan du arbetat på samma utkast.</strong><br>Ladda om sidan och försök igen. Utkastet ändrades av: ${name}',
            'common.error.save.unknown': 'Tekniskt fel. Utkastet kunde inte sparas. Om problemet kvarstår, kontakta din lokala IT-administratör.',
            'common.error.save.module_problem': 'Tekniskt fel. Utkastet kunde inte sparas. Om problemet kvarstår, kontakta din lokala IT-administratör.',
            'common.error.save.data_not_found': 'Tekniskt fel. Utkastet kunde inte sparas. Om problemet kvarstår, kontakta din lokala IT-administratör.',
            'common.error.save.invalid_state': 'Tekniskt fel. Utkastet kunde inte sparas. Om problemet kvarstår, kontakta din lokala IT-administratör.',
            'common.error.save.noconnection': 'Inget nätverk. Utkastet kunde inte sparas. Kontrollera att du har en fungerande internetanslutning.',
            'common.error.save.concurrent_modification': 'Utkastet har samtidigt ändrats av en annan användare och kunde därför inte sparas. Ladda om sidan och försök igen. Utkastet ändrades av: ${name}',
            'common.error.save.unknown_internal_problem': 'Tappade anslutningen till Webcerts server. Utkastet kunde inte sparas. Om problemet kvarstår, kontakta din lokala IT-administratör.',
            'common.error.certificate_type_sekretessmarkering_unsupported': '<strong>Intyg kan ej hanteras.</strong><br>Det intyg du valt att skriva går tyvärr inte att skapa i Webcert för en patient med skyddade personuppgifter.',
            'common.error.intyg.status.failed.load': 'Intygsstatus kunde inte hämtas.',

            // FMB texts
            'fmb.symptom_prognos_behandling': 'Symtom, prognos, behandling',
            'fmb.generell_info': 'Försäkringsmedicinsk information',
            'fmb.funktionsnedsattning': 'Funktionsnedsättning',
            'fmb.aktivitetsbegransning': 'Aktivitetsbegränsning',
            'fmb.informationomrehabilitering': 'Information om rehabilitering',
            'fmb.beslutsunderlag_textuellt': 'Vägledning för sjukskrivning',
            'fmb.warn.no-diagnose-set': 'Ange minst en diagnos för att få FMB-stöd.',
            'fmb.info.single-diagnose-no-data': 'För den angivna diagnosen finns för tillfället inget FMB-stöd.',
            'fmb.info.multiple-diagnose-no-data': 'För de angivna diagnoserna finns för tillfället inget FMB-stöd.',
            'fmb.incorrectKodverk': 'FMB-stödet finns enbart för koder som ingår i ICD-10-SE.',
            'fmb.varning.error': 'På grund av ett tekniskt fel kan vi just nu inte räkna ut om patienten överskrider FMB:s rekommenderade sjukskrivningslängd.',

            // SRS texts
            'srs.warn.no-diagnosis-set': 'Ange minst en diagnos för att få stöd för sjukskrivning.',
            // Old SRS
            'srs.srsfordiagnose.load.message': 'Laddar Stöd för bedömning vid sjukskrivning...',
            'srs.srsfordiagnose.load.error': 'Tekniskt fel, stöd för bedömning kan inte visas.',
            'srs.srsfordiagnose.load.nodata': 'Det finns ingen information från bedömningsstödet SRS',
            'srs.srsfordiagnose.atgarder.missing': 'Det finns inga åtgärdsrekommendationer från bedömningsstödet SRS',
            'srs.srsfordiagnose.atgarder.highercode': 'Åtgärdsrekommendationer från bedömningsstödet SRS visas för den överordnade diagnoskoden ${code}',
            'srs.srsfordiagnose.statistik.missing': 'Det finns ingen statistik från bedömningsstödet SRS',
            'srs.srsfordiagnose.statistik.highercode': 'Statistik från bedömningsstödet SRS visas för den överordnade diagnoskoden ${code}',

            // doubling this since some messages use common.error instead of common.arende.error to use other errors, we should unentangle this mess later
            'common.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel> på 0771-251010.',

            // Ärendehantering errors
            'common.arende.error.send.title': 'Meddelandet har inte skickats',
            'common.arende.error.external_system_problem': '<strong>Meddelandet har inte skickats</strong><br>Webcert saknar kontakt med Försäkringskassans datasystem. Prova igen om en stund. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.',
            'common.arende.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'common.arende.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
            'common.arende.error.unknown': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'common.arende.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
            'common.arende.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
            'common.arende.error.data_not_found': '<strong>Personen kunde inte hittas i personuppgiftstjänsten.</strong>',
            'common.arende.error.komplettera-no-intyg': '<strong>Kan inte svara med nytt intyg</strong><br>Intyget kunde inte laddas och är därför inte tillgängligt att användas som grund för komplettering.',
            'common.arende.error.komplettera-no-utkast': '<strong>Kan inte gå till existerande intygsutkast</strong><br>Intygsutkastet kunde inte laddas.',
            'common.arende.error.pu_problem.modalheader': 'Personuppgiftstjänsten svarar inte',
            'common.arende.error.pu_problem': '<p>Personuppgiftstjänsten svarar inte. Åtgärden kan inte genomföras eftersom den kräver att personuppgifter kan hämtas från personuppgiftsregistret. Prova igen om en stund.</p><p>Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand <LINK:ineraKundserviceAnmalFel>.</p>',


            // Högerflikar
            'common.supportpanel.srs.title': 'Risk och råd',
            'common.supportpanel.srs.tooltip': 'Risk och råd',
            'common.supportpanel.srs.title.minimized': 'Risk och råd',
            'common.supportpanel.srs.tooltip.minimized': 'Öppnar fliken med Risk och råd.',

            'common.supportpanel.fmb.title': 'FMB',
            'common.supportpanel.fmb.tooltip': 'Läs FMB - ett stöd för ifyllnad och bedömning.',
            'common.supportpanel.fmb.title.minimized': 'FMB',
            'common.supportpanel.fmb.tooltip.minimized': 'Öppnar fliken med det försäkringsmedicinska beslutsstödet.',
            'common.supportpanel.arende.title': 'Ärendekommunikation',
            'common.supportpanel.arende.tooltip': 'Hantera kompletteringsbegäran, frågor och svar.',
            'common.supportpanel.arende.title.minimized': 'Ärenden',
            'common.supportpanel.arende.tooltip.minimized': 'Öppnar fliken med ärendekommunikationen.',
            'common.supportpanel.help.title': 'Om intyget',
            'common.supportpanel.help.tooltip': 'Läs om intyget.',
            'common.supportpanel.help.title.minimized': 'Om intyget',
            'common.supportpanel.help.tooltip.minimized': 'Öppnar fliken med information om intyget.',
            'common.supportpanel.minimize.tooltip': 'Döljer högerfätet',
            'common.supportpanel.ro-kompletteringar.title': 'Ärendekommunikation',
            'common.supportpanel.ro-kompletteringar.tooltip': 'Visar obesvarade kompletteringar för intyget.',
            'common.supportpanel.ro.no-kompl': 'Det finns inga obesvarade kompletteringar',
            'common.supportpanel.ro.no-qa': 'Det finns inga obesvarade administrativa frågor eller svar',

            // Approve receivers related resources
            'common.receivers.save.btn': 'Spara',
            'common.receivers.loading.text': 'Hämtar intygsmottagare...',
            'common.receivers.save.error': 'Kunde inte spara mottagare på grund av ett tekniskt fel. Försök igen och kontakta supporten om problemet kvarstår.',

            'db.warn.previouscertificate.samevg': 'Det finns ett signerat dödsbevis för detta personnummer. Du kan inte skapa ett nytt dödsbevis men kan däremot välja att ersätta det befintliga dödsbeviset.',
            'db.warn.previouscertificate.samevg.differentenhet': 'Det finns ett signerat dödsbevis för detta personnummer på annan vårdenhet. Du kan inte skapa ett nytt dödsbevis men kan däremot välja att ersätta det befintliga dödsbeviset.',
            'db.warn.previouscertificate.differentvg': 'Det finns ett signerat dödsbevis för detta personnummer hos annan vårdgivare. Det är inte möjligt att skapa ett nytt dödsbevis.',
            'db.warn.previouscertificate.differentvg.sign': 'Det finns ett signerat dödsbevis för detta personnummer hos annan vårdgivare. Det är därför inte möjligt att signera detta dödsbevis.',
            'db.warn.previouscertificate.title': 'Signerat dödsbevis finns',
            'db.warn.previousdraft.samevg': 'Det finns ett utkast på dödsbevis för detta personnummer. Du kan inte skapa ett nytt utkast men kan däremot välja att fortsätta med det befintliga utkastet.',
            'db.warn.previousdraft.samevg.differentenhet': 'Det finns ett utkast på dödsbevis för detta personnummer på annan vårdenhet. Du kan inte skapa ett nytt utkast men kan däremot välja att fortsätta med det befintliga utkastet.',
            'db.warn.previousdraft.differentvg': 'Det finns ett utkast på dödsbevis för detta personnummer hos annan vårdgivare. Senast skapade dödsbevis är det som gäller. Om du fortsätter och lämnar in dödsbeviset så blir det därför detta dödsbevis som gäller.',
            'db.warn.previousdraft.title': 'Utkast på dödsbevis finns',


            'doi.warn.previouscertificate.samevg': 'Det finns ett signerat dödsorsaksintyg för detta personnummer. Du kan inte skapa ett nytt dödsorsaksintyg men kan däremot välja att ersätta det befintliga dödsorsaksintyget.',
            'doi.warn.previouscertificate.samevg.differentenhet': 'Det finns ett signerat dödsorsaksintyg för detta personnummer på annan vårdenhet. Du kan inte skapa ett nytt dödsorsaksintyg men kan däremot välja att ersätta det befintliga dödsorsaksintyget.',
            'doi.warn.previouscertificate.differentvg': 'Det finns ett signerat dödsorsaksintyg för detta personnummer hos annan vårdgivare. Senast skapade dödsorsaksintyg är det som gäller. Om du fortsätter och lämnar in dödsorsaksintyget så blir det därför detta dödsorsaksintyg som gäller.',
            'doi.warn.previouscertificate.title': 'Signerat dödsorsaksintyg finns',
            'doi.warn.previousdraft.samevg': 'Det finns ett utkast på dödsorsaksintyg för detta personnummer. Du kan inte skapa ett nytt utkast men kan däremot välja att fortsätta med det befintliga utkastet.',
            'doi.warn.previousdraft.samevg.differentenhet': 'Det finns ett utkast på dödsorsaksintyg för detta personnummer på annan vårdenhet. Du kan inte skapa ett nytt utkast men kan däremot välja att fortsätta med det befintliga utkastet.',
            'doi.warn.previousdraft.differentvg': 'Det finns ett utkast på dödsorsaksintyg för detta personnummer hos annan vårdgivare. Senast skapade dödsorsaksintyg är det som gäller. Om du fortsätter och lämnar in dödsorsaksintyget så blir det därför detta dödsorsaksintyg som gäller.',
            'doi.warn.previousdraft.title': 'Utkast på dödsorsaksintyg finns',
            
            // launchId
            'error.launchId.text': '<h2>Intyget kan inte visas</h2><br><p>Detta intyg kan inte visas eftersom du har öppnat ett annat intyg.</p>'

        },
        'en': {
            'common.ok': 'OK',
            'common.cancel': 'Cancel'
        }
    });

})();