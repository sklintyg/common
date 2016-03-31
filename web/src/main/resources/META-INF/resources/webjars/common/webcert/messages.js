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

/* jshint maxlen: false, unused: false */
var commonMessages = {
    'sv': {

        'common.continue': 'Fortsätt',
        'common.yes': 'Ja',
        'common.no': 'Nej',
        'common.yes.caps': 'JA',
        'common.no.caps': 'NEJ',
        'common.nodontask': 'Nej, fråga inte igen',
        'common.ok': 'OK',
        'common.cancel': 'Avbryt',
        'common.goback': 'Tillbaka',
        'common.revoke': 'Intyget ska återtas',
        'common.sign': 'Signera',
        'common.send': 'Skicka',
        'common.copy': 'Kopiera',
        'common.delete': 'Radera',
        'common.sign.intyg': 'Signera intyget',

        'common.alert.newpersonid': 'Patienten har ett nytt personnummer: <b>${person}</b>',
        'common.alert.newreserveid': 'Patienten har samordningsnummer kopplat till reservnummer: <b>${reserve}</b>',
        'common.alert.sekretessmarkering.utkast': 'Patienten har en sekretessmarkering. Det innebär att patientens folkbokföringsuppgifter är skyddade. Var vänlig hantera dem varsamt.',
        'common.alert.sekretessmarkering.intyg': '<p>Patienten har en sekretessmarkering. Det innebär att patientens folkbokföringsuppgifter är skyddade. Var  vänlig hantera dem varsamt.</p>På grund av sekretessmarkeringen går det inte att kopiera intyget.',
        'common.alert.sekretessmarkering.error': 'Misslyckades att slå upp patienten i personuppgiftstjänsten. Observera att patienten kan ha skyddade folkbokföringsuppgifter. Var vänlig handskas varsamt med uppgifterna.',
        'common.alert.textversionupdated': 'Observera att frågetexterna har uppdaterats',

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

        // cert status messages
        'cert.status.draft_incomplete': 'Utkast, uppgifter saknas',
        'cert.status.draft_complete': 'Utkast, kan signeras',
        'cert.status.signed': 'Signerat',
        'cert.status.cancelled': 'Makulerat',
        'cert.status.unknown': 'Okänd',
        'cert.status.sent': 'Mottaget',
        'cert.status.received': 'Signerat',

        // common intyg view messages
        'common.label.ovanstaende-har-bekraftats': 'Ovanstående uppgifter och bedömningar bekräftas',
        'common.label.saving': 'Sparar',

        // draft form status messages
        'draft.status.draft_incomplete': '<strong>Status:</strong> Utkastet är sparat, men obligatoriska uppgifter saknas.',
        'draft.status.draft_complete': '<strong>Status:</strong> Utkastet är sparat och kan signeras.',
        'draft.status.signed': '<strong>Status:</strong> Intyget är signerat.',
        'draft.status.changed': '<strong>Status:</strong> Utkastet är ändrat sedan det senast sparades',

        // intyg forms
        'draft.saknar-uppgifter': 'Utkastet saknar uppgifter i följande avsnitt:',
        'draft.onlydoctorscansign': 'Endast läkare får signera intyget.',
        'draft.signingdoctor': 'Signerande läkare',
        'draft.helptext.signingdoctor': 'Den läkare som avses signera intyget anges här.',
        'draft.notifydoctor': 'Skicka ett mejl med en länk till utkastet till den läkare som ska signera.',

        // Revoke status messages (type agnostic)
        'cert.status.revoke.requested' : 'Begäran om makulering skickad till intygstjänsten',
        'cert.status.revoke.confirmed' : 'Intyget är makulerat',

        // wc-common-directives-resources
        'nav.label.loggedinas': 'Inloggad som:',

        // Fraga/svar resource - used both by webcert and module
        // modal messages
        'modal.title.markforward': 'Markera som vidarebefordrad?',

        'qa.status.pending_internal_action': 'Kräver svar',
        'qa.status.pending_external_action': 'Inväntar svar',
        'qa.status.answered': 'Besvarat',
        'qa.status.closed': 'Hanterat',
        'qa.help.handled': 'Tidigare meddelanden är sådana som redan har besvarats eller hanterats på något sätt.',
        'qa.help.kompletteringar': 'Kompletteringar visar den information som Försäkringskassan begärt ska korrigeras på intyget detta utkast ska komplettera.',

        'qa.fragestallare.fk': 'Försäkringskassan',
        'qa.fragestallare.wc': 'Vårdenheten',
        'qa.amne.paminnelse': 'Påminnelse',
        'qa.amne.arbetstidsforlaggning': 'Arbetstidsförläggning',
        'qa.amne.kontakt': 'Kontakt',
        'qa.amne.avstamningsmote': 'Avstämningsmöte',
        'qa.amne.komplettering_av_lakarintyg': 'Komplettering av läkarintyg',
        'qa.amne.makulering_av_lakarintyg': 'Makulering av läkarintyg',
        'qa.amne.ovrigt': 'Övrigt',

        'qa.measure.svarfranvarden': 'Svara',
        'qa.measure.svarfranfk': 'Invänta svar från Försäkringskassan',
        'qa.measure.komplettering': 'Komplettera',
        'qa.measure.markhandled': 'Markera som hanterad',
        'qa.measure.handled': 'Ingen',

        'arende.fraga.amne.komplt': 'Komplettering',
        'arende.fraga.amne.ovrigt': 'Övrigt',

        // Cert module messages. Used by several cert modules.
        'modules.label.field': 'Fält',
        'modules.label.blank': '- ej ifyllt',
        'modules.button.alt.archive': 'Arkivera intyget. Flyttar intyget till Arkiverade intyg',

        'info.loadingcertificate': 'Hämtar intyget...',

        'common.label.diagnoses.more_results': 'Det finns fler träffar än vad som kan visas i listan, förfina sökningen.',

        // Common errors
        'common.error.unknown': '<strong>Tekniskt fel.</strong>',
        'common.error.authorization_problem' : '<strong>Behörighet saknas.</strong><br>Du saknar behörighet att använda denna resurs.',
        'common.error.cantconnect': '<strong>Kunde inte kontakta servern.</strong>',
        'common.error.certificatenotfound': '<strong>Intyget finns inte.</strong>',
        'common.error.certificateinvalid': '<strong>Intyget är inte korrekt ifyllt.</strong>',
        'common.error.certificateinvalidstate': '<strong>Intyget är inte ett utkast.</strong>Inga operationer kan utföras på det längre.',
        'common.error.invalid_state': '<strong>Operation är inte möjlig.</strong><br>Förmodligen har en annan användare ändrat informationen medan du arbetat på samma utkast. Ladda om sidan och försök igen',
        'common.error.sign.general': '<strong>Intyget kunde inte signeras.</strong><br>Försök igen senare.',
        'common.error.sign.netid': '<strong>Intyget kunde inte signeras.</strong><br>Kunde inte kontakta Net iD-klienten. Försök igen senare eller kontakta din support.',
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
        'common.error.discard.concurrent_modification': '<strong>Kan inte ta bort utkastet. Utkastet har ändrats av en annan användare medan du arbetat på samma utkast.</strong><br>Ladda om sidan och försök igen. Utkastet ändrades av: ${name}',
        'common.error.save.unknown': '<strong>Okänt fel.</strong> Det går för tillfället inte att spara ändringar.',
        'common.error.save.module_problem': '<strong>Okänt fel.</strong> Det går för tillfället inte att spara ändringar.',
        'common.error.save.data_not_found': '<strong>Okänt fel.</strong> Det går för tillfället inte att spara ändringar.',
        'common.error.save.concurrent_modification': '<strong>Kan inte spara utkastet. Utkastet har ändrats av en annan användare medan du arbetat på samma utkast.</strong><br>Ladda om sidan och försök igen. Utkastet ändrades av: ${name}',
        'common.error.save.unknown_internal_problem': '<strong>Tappade anslutningen till servern.</strong><br>Det går för tillfället inte att spara ändringar.',
        'common.error.save.invalid_state': '<strong>Tekniskt fel.</strong><br>Intyget kunde inte laddas korrekt. (invalid_state).',

        // FMB texts
        'fmb.falt2_spb': 'Symtom, prognos och behandling',
        'fmb.falt2_general': 'Generell information',
        'fmb.falt4': '',
        'fmb.falt5': '',
        'fmb.falt8b': '',

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
        'common.fk.fragasvar.error.data_not_found': '<strong>Intyget kunde inte hittas i intygstjänsten.</strong><br>Intyget är borttaget eller så saknas behörighet.'
    },
    'en': {
        'common.ok': 'OK',
        'common.cancel': 'Cancel'
    }
};
