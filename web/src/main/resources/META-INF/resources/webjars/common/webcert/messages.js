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
        'common.revokeandreplace': 'Makulera och ersätt',
        'common.sign': 'Signera intyget',
        'common.signsend': 'Signera och skicka',
        'common.send': 'Skicka',
        'common.copy': 'Kopiera',
        'common.fornya': 'Förnya',
        'common.delete': 'Radera',
        'common.sign.intyg': 'Signera intyget',
        'common.button.save.as.pdf': 'Spara som PDF',
        'common.button.save.as.pdf.full': 'Fullständigt intyg',
        'common.button.save.as.pdf.mininmal': 'Minimalt intyg',
        'common.button.save.as.pdf.mininmal.title': 'Minimalt intyg enligt sjuklönelagens krav på intyg som kan lämnas till arbetsgivare',

        'common.alert.newpersonid': 'Patienten har ett nytt personnummer: <b>${person}</b>',
        'common.alert.newreserveid': 'Patienten har samordningsnummer kopplat till reservnummer: <b>${reserve}</b>',
        'common.alert.sekretessmarkering.utkast': 'Patienten har en sekretessmarkering. Det innebär att patientens folkbokföringsuppgifter är skyddade. Var vänlig hantera dem varsamt.',
        'common.alert.sekretessmarkering.intyg': '<p>Patienten har en sekretessmarkering. Det innebär att patientens folkbokföringsuppgifter är skyddade. Var  vänlig hantera dem varsamt.</p>På grund av sekretessmarkeringen går det inte att kopiera intyget.',
        'common.alert.sekretessmarkering.error': 'Misslyckades att slå upp patienten i personuppgiftstjänsten. Observera att patienten kan ha skyddade folkbokföringsuppgifter. Var vänlig handskas varsamt med uppgifterna.',
        'common.alert.textversionupdated': 'Observera att frågetexterna har uppdaterats',
        'common.warning.patientdataupdate.failed': '<b>Observera!</b> Misslyckades att slå upp patienten i personuppgiftstjänsten',


        'common.date': 'Datum',
        'common.when': 'När?',

        'common.notset': 'Ej angivet',

        'common.about.cookies': '<p>Så kallade kakor (cookies) används för att underlätta för besökaren på webbplatsen. En kaka är en textfil som lagras på din dator och som innehåller information. Denna webbplats använder så kallade sessionskakor. Sessionskakor lagras temporärt i din dators minne under tiden du är inne på en webbsida. Sessionskakor försvinner när du stänger din webbläsare. Ingen personlig information om dig sparas vid användning av sessionskakor.</p><p>Om du inte accepterar användandet av kakor kan du stänga av det via din webbläsares säkerhetsinställningar. Du kan även ställa in webbläsaren så att du får en varning varje gång webbplatsen försöker sätta en kaka på din dator.</p><p><strong>Observera!</strong> Om du stänger av kakor i din webbläsare kan du inte logga in i Webcert.</p><p>Allmän information om kakor (cookies) och lagen om elektronisk kommunikation finns på Post- och telestyrelsens webbplats.</p><p><a href="https://www.pts.se/sv/Privat/Internet/Integritet1/Fragor-och-svar-om-kakor-for-anvandare2/" target="_blank">Mer om kakor (cookies) på Post- och telestyrelsens webbplats</a></p>',

        // avtal/terms
        'avtal.title.text' : 'Godkännande av användarvillkor',
        'avtal.approve.label' : 'Jag godkänner villkoren',
        'avtal.print.label' : 'Skriv ut',
        'avtal.logout.label' : 'Logga ut',

        // labels for common modal dialogs
        'common.title.sign' : 'Signera intyget',

        'common.modal.label.discard_draft' : 'Ta bort utkast',
        'common.modal.label.confirm_sign': 'Signera intyget',
        'common.modal.bankid.heading' : 'Signera med BankID',
        'common.modal.bankid.open': 'Öppna programmet för BankID på din dator.',
        'common.modal.bankid.signing': 'Intyget signeras, vänligen skriv in din kod.',
        'common.modal.bankid.noclient': 'BankID-programmet svarar inte. Kontrollera att programmet är startat och att du har internetanslutning.  Försök sedan igen.',
        'common.modal.bankid.signed': 'Intyget är nu signerat.',

        'common.modal.mbankid.heading' : 'Signera med Mobilt BankID',
        'common.modal.mbankid.open': 'Öppna appen för Mobilt BankID på din telefon eller surfplatta.',
        'common.modal.mbankid.signing': 'Intyget signeras, vänligen skriv in din kod på din telefon eller surfplatta.',
        'common.modal.mbankid.noclient': 'Mobilt BankID-appen svarar inte. Kontrollera att appen är startad och att du har internetanslutning.  Försök sedan igen.',
        'common.modal.mbankid.signed': 'Intyget är nu signerat.',

        // common intyg view messages
        'common.label.ovanstaende-har-bekraftats': 'Ovanstående uppgifter och bedömningar bekräftas',

        // intyg status messages
        'intyg.status.draft_incomplete': 'Utkast, uppgifter saknas',
        'intyg.status.draft_complete': 'Utkast, kan signeras',
        'intyg.status.signed': 'Signerat',
        'intyg.status.cancelled': 'Makulerat',
        'intyg.status.unknown': 'Okänd',
        'intyg.status.sent': 'Mottaget',
        'intyg.status.received': 'Signerat',

        'intyg.status.patient.name.changed' : 'Observera att patientens namn har ändrats sedan det här intyget utfärdades. När du kopierar eller förnyar intyget kommer patientens aktuella namn att användas i det nya intygsutkastet.',
        'intyg.status.patient.address.changed' : 'Observera att patientens adress har ändrats sedan det här intyget utfärdades. När du kopierar eller förnyar intyget kommer patientens aktuella adress att användas i det nya intygsutkastet.',
        'intyg.status.patient.name.and.address.changed' : 'Observera att patientens namn och adress har ändrats sedan det här intyget utfärdades. När du kopierar eller förnyar intyget kommer patientens aktuella namn och adress att användas i det nya intygsutkastet.',

        // Revoke status messages (type agnostic)
        'intyg.status.revoke.requested' : '<strong>Status:</strong> Intyget är makulerat.',
        'intyg.status.revoke.confirmed' : '<strong>Status:</strong> Intyget är makulerat.',

        // draft utkast header form status messages
        'draft.status.incomplete': '<strong>Status:</strong> Utkastet är sparat, men obligatoriska uppgifter saknas.',
        'draft.status.complete': '<strong>Status:</strong> Utkastet är sparat och kan signeras.',
        'draft.status.signed': '<strong>Status:</strong> Intyget är signerat.',
        'draft.status.changed': '<strong>Status:</strong> Utkastet är ändrat sedan det senast sparades',

        // intyg forms
        'draft.saknar-uppgifter': 'Utkastet saknar uppgifter i följande avsnitt:',
        'draft.onlydoctorscansign': 'Endast läkare får signera intyget.',
        'draft.signingdoctor': 'Signerande läkare',
        'draft.helptext.signingdoctor': 'Den läkare som avses signera intyget anges här.',
        'draft.notifydoctor': 'Skicka ett mejl med en länk till utkastet till den läkare som ska signera.',
        'draft.completion.signinfo': 'Om du går vidare kommer det nya kompletterade intyget signeras och skickas direkt till Försäkringskassans system.',

        'intyg.makulera.help' : 'Orsak till makulering ska anges eftersom Försäkringskassans handläggare behöver den informationen för att bedöma patientens rätt till ersättning.',

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
        'qa.measure.handled': 'Ingen',

        // Ärendehantering
        'common.loading.existing.arenden': 'Laddar ärenden...',
        'common.arende.komplettering.answerwithmessage.help': 'Med förtydligande avses information som inte tillför ny medicinsk information, utan förtydligar, förklarar eller ytterligare specificerar medicinska begrepp, termer eller mått. Exempelvis vad avses med skrivningen ”synnerligt påverkad” (i just detta fall), vad menas med ”samsjuklighet” (i just detta fall), etc.',
        'common.arende.komplettering.disabled.svaramedintyg.uthopp': 'Om kompletteringen innebär att ny medicinsk information behöver tillföras ska informationen ges i ett nytt intyg. Kopiera/förnya det befintliga intyget i journalsystemet och komplettera med den nya informationen.',
        'common.arende.komplettering.disabled.onlydoctor': 'Kompletteringar kan endast besvaras av läkare.',
        'common.arende.komplettering.kompletteringsatgard.dialogtitle': 'Besvara kompletteringsbegäran',

        'common.arende.fragestallare.wc': 'Vårdenheten',
        'common.arende.fragestallare.fk': 'Försäkringskassan',

        'common.arende.atgard.svarfranvarden': 'Svara',
        'common.arende.atgard.svarfranfk': 'Invänta svar från Försäkringskassan',
        'common.arende.atgard.komplettering': 'Komplettera',
        'common.arende.atgard.markhandled': 'Markera som hanterad',
        'common.arende.atgard.handled': 'Ingen',

        'common.arende.fraga.amne.arbtid': 'Arbetstidsförläggning',
        'common.arende.fraga.amne.avstmn': 'Avstämningsmöte',
        'common.arende.fraga.amne.kontkt': 'Kontakt',
        'common.arende.fraga.amne.komplt': 'Komplettering',
        'common.arende.fraga.amne.ovrigt': 'Övrigt',
        'common.arende.fraga.amne.paminn': 'Påminnelse',

        // Används bara av fk7263 (ArendeLegacyProxy)
        'common.arende.fraga.amne.makulering_av_lakarintyg': 'Makulering av läkarintyg',

        'common.arende.help.kompletteringar': 'Kompletteringar visar den information som Försäkringskassan begärt ska korrigeras på intyget detta utkast ska komplettera.',
        'common.arende.help.handled': 'Tidigare frågor och svar är sådana som redan har besvarats eller hanterats på något sätt.',

        // intyg module messages. Used by several intyg modules.
        'modules.label.field': 'Fält',
        'modules.label.blank': '- ej ifyllt',
        'modules.button.alt.archive': 'Arkivera intyget. Flyttar intyget till Arkiverade intyg',

        'info.loadingcertificate': 'Hämtar intyget...',

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

        //common.sit.* messages are candidates for a fk-common messages.js
        'common.sit.label.valj-version-icd-10': 'Välj version av ICD-10-SE',
        'common.sit.label.diagnoskodverk.fullstandig': 'Fullständig version',
        'common.sit.label.diagnoskodverk.primarvard': 'Primärvårdsversion',
        'common.sit.client-validation.underlag.max-extra-underlag': 'Du kan inte lägga till fler utredningar, max antal är %0st',
        'common.label.patient': 'Patientens adressuppgifter',
        'common.label.vardenhet': 'Vårdenhetens adress',
        'common.validation.patient.postadress.missing': 'Postadress saknas.',
        'common.validation.patient.postnummer.missing': 'Postnummer saknas.',
        'common.validation.patient.postort.missing': 'Postort saknas.',

        // Common errors
        'common.error.unknown': '<strong>Tekniskt fel.</strong>',
        'common.error.authorization_problem' : '<strong>Behörighet saknas.</strong><br>Du saknar behörighet att använda denna resurs.',
        'common.error.cantconnect': '<strong>Kunde inte kontakta servern.</strong>',
        'common.error.certificatenotfound': '<strong>Intyget finns inte.</strong>',
        'common.error.certificateinvalid': '<strong>Intyget är inte korrekt ifyllt.</strong>',
        'common.error.certificateinvalidstate': '<strong>Intyget är inte ett utkast.</strong>Inga operationer kan utföras på det längre.',
        'common.error.invalid_state': '<strong>Operation är inte möjlig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma utkast. Ladda om sidan och försök igen',
        'common.error.sign.general': '<strong>Intyget kunde inte signeras.</strong><br>Försök igen senare.',
        'common.error.sign.netid': '<strong>Signering misslyckad.</strong><br>Intyget har inte signerats. Detta beror antingen på ett tekniskt fel eller att signeringen avbrutits. Försök igen senare eller kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'common.error.sign.bankid': '<strong>Intyget kunde inte signeras.</strong><br>Kunde inte kontakta Bank ID-klienten. Försök igen senare eller kontakta din support.',
        'common.error.sign.not_ready_yet': '<strong>Intyget är nu signerat.</strong><br>Tyvärr kan inte intyget visas än då det behandlas. Prova att ladda om sidan lite senare. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'common.error.sign.concurrent_modification': '<strong>Det går inte att signera utkastet.</strong><br/>Utkastet har ändrats av en annan användare sedan du började arbeta på det. Ladda om sidan, kontrollera att uppgifterna stämmer och försök signera igen.<br/>Utkastet ändrades av ${name}.',
        'common.error.sign.authorization': '<strong>Intyget kunde inte signeras.</strong><br/>Du saknar behörighet att signera detta intyg.',
        'common.error.sign.indeterminate.identity':'<strong>Intyget kunde inte signeras.</strong><br/>Det verkar som att du valt en annan identitet att signera med än den du loggade in med. Du måste identifiera dig på samma sätt som när du loggade in. Kontrollera om du har valt rätt och prova igen.',
        'common.error.sign.grp.already_in_progress':'<strong>Intyget kunde inte signeras.</strong><br/>En inloggning eller underskrift för det här personnumret är redan påbörjad, tryck avbryt i BankID säkerhetsapp och försök igen.',
        'common.error.sign.grp.cancel':'<strong>Intyget kunde inte signeras.</strong><br/>Åtgärden avbruten.',
        'common.error.sign.grp.expired_transaction':'<strong>Intyget kunde inte signeras.</strong><br/>Inget svar från klienten. Kontrollera att du har startat din BankID säkerhetsapp, följ instruktionerna och försök igen.',
        'common.error.unknown_internal_problem': '<strong>Tekniskt fel i Webcert.</strong><br>Försök igen senare.',
        'common.error.data_not_found': '<strong>Intyget kunde inte hittas.</strong><br>Intyget är borttaget eller så saknas behörighet.',
        'common.error.could_not_load_cert': '<strong>Intyget gick inte att läsa in.</strong><br>Prova att ladda om sidan. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
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

        // FMB texts
        'fmb.symptom_prognos_behandling': 'Symtom, prognos och behandling',
        'fmb.generell_info': 'Generell information',
        'fmb.funktionsnedsattning': '',
        'fmb.aktivitetsbegransning': '',
        'fmb.beslutsunderlag_textuellt': '',

        // Fragasvar för FK
        'common.fk.info.loading.existing.qa': 'Hämtar tidigare frågor och svar...',
        'common.fk.fragasvar.answer.is.sent': '<strong>Svaret har skickats till Försäkringskassan.</strong><br> Frågan är nu markerad som hanterad och visas nu under \'Hanterade frågor\' längre ner på sidan.',
        'common.fk.fragasvar.marked.as.hanterad': '<strong>Frågan-svaret är markerad som hanterad.</strong><br> Frågan-svaret visas under rubriken \'hanterade frågor och svar\' nedan.',
        'common.fk.fragasvar.marked.as.ohanterad': '<strong>Frågan-svaret är markerad som ej hanterad.</strong><br> Frågan-svaret visas under rubriken \'Ej hanterade frågor och svar\' ovan.',
        'common.fk.fragasvar.label.ovanstaende-har-bekraftats': '<strong>Ovanstående har bekräftats</strong>',

        // fragaSvar errors
        'common.fk.fragasvar.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
        'common.fk.fragasvar.error.internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'common.fk.fragasvar.error.invalid_state': '<strong>Funktionen är inte giltig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma post. Ladda om informationen och försök igen',
        'common.fk.fragasvar.error.unknown_internal_problem': '<strong>Ett tekniskt problem inträffade.</strong><br>Försök igen och kontakta supporten om problemet kvarstår.',
        'common.fk.fragasvar.error.authorization_problem': '<strong>Du har inte behörighet att utföra funktionen, kontakta supporten för mer information.</strong>',
        'common.fk.fragasvar.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.',

        // Ärendehantering errors
        'common.arende.error.external_system_problem': '<strong>Meddelandet har inte skickats till Försäkringskassan då Webcert saknar kontakt med Försäkringskassans datasystem.</strong><br>Prova att skicka om meddelandet. Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand Nationell kundservice på 0771-251010.',
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
