/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
angular.module('ts-bas').constant('ts-bas.messages', {
    'sv': {
        'ts-bas.label.patient': 'Patientens adressuppgifter',
        'ts-bas.label.patientadress': 'Patientens adressuppgifter',
        'ts-bas.label.intygavser': 'Intyget avser',
        'ts-bas.label.identitet': 'Identiteten är styrkt genom',
        'ts-bas.label.syn': '1. Synfunktioner',
        'ts-bas.label.horselbalans': '2. Hörsel och balanssinne',
        'ts-bas.label.funktionsnedsattning': '3. Rörelseorganens funktioner',
        'ts-bas.label.hjartkarl': '4. Hjärt- och kärlsjukdomar',
        'ts-bas.label.diabetes': '5. Diabetes',
        'ts-bas.label.neurologi': '6. Neurologiska sjukdomar',
        'ts-bas.label.medvetandestorning': '7. Epilepsi, epileptiskt anfall och annan medvetandestörning',
        'ts-bas.label.njurar': '8. Njursjukdomar',
        'ts-bas.label.kognitivt': '9. Demens och andra kognitiva störningar',
        'ts-bas.label.somnvakenhet': '10. Sömn- och vakenhetsstörningar',
        'ts-bas.label.narkotikalakemedel': '11. Alkohol, narkotika och läkemedel',
        'ts-bas.label.psykiskt': '12. Psykiska sjukdomar och störningar',
        'ts-bas.label.utvecklingsstorning': '13. ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning',
        'ts-bas.label.sjukhusvard': '14. Sjukhusvård',
        'ts-bas.label.medicinering': '15. Övrig medicinering',
        'ts-bas.label.ovrigkommentar': '16. Övrig kommentar',
        'ts-bas.label.bedomning': 'Bedömning',
        'ts-bas.label.vardenhet': 'Vårdenhet',
        
        'ts-bas.label.identitet.id_kort': 'ID-kort',
        'ts-bas.label.identitet.foretag_eller_tjanstekort': 'Företagskort eller tjänstekort',
        'ts-bas.label.identitet.korkort': 'Svenskt körkort',
        'ts-bas.label.identitet.pers_kannedom': 'Personlig kännedom',
        'ts-bas.label.identitet.forsakran_kap18': 'Försäkran enligt 18 kap. 4§',
        'ts-bas.label.identitet.pass': 'Pass',

        'ts-bas.helptext.intyg-avser': '<span style="text-align:left">C1 = medeltung lastbil och enbart ett lätt släpfordon<br/>C1E = medeltung lastbil och ett eller flera släpfordon oavsett vikt<br/>C = tung lastbil och enbart ett lätt släpfordon<br/>CE = tung lastbil och ett eller flera släpfordon oavsett vikt<br/>D1 = mellanstor buss och enbart ett lätt släpfordon<br/>D1E = mellanstor buss och ett eller flera släpfordon oavsett vikt<br/>D = buss och enbart ett lätt släpfordon<br/>DE = buss och ett eller flera släpfordon oavsett vikt<br/>Taxi = taxiförarlegitimation<br/>Annat = (AM,A1,A2,A,B,BE eller Traktor)</span>',

        'ts-bas.helptext.identitet': 'Identitet styrkt genom någon av nedanstående',

        'ts-bas.helptext.synfunktioner.utan-korrektion': 'Uppgiften är obligatorisk',
        'ts-bas.helptext.synfunktioner.med-korrektion': 'Uppgiften är obligatorisk om föreskriven synskärpa endast uppnås med korrektion. Definition av föreskriven synskärpa finns i Transportstyrelsens föreskrifter.',

        'ts-bas.helptext.diabetes.behandling': 'Vid tablett- eller insulinbehandlad diabetes krävs det att ett läkarintyg gällande sjukdomen skickas in.',
        'ts-bas.helptext.narkotika-lakemedel.provtagning': 'Om ja på ovanstående ska resultatet redovisas separat.',
        'ts-bas.helptext.bedomning': '<span style="text-align:left">C1 - medeltung lastbil och enbart ett lätt släpfordon<br/>C1E - medeltung lastbil och ett eller flera släpfordon oavsett vikt<br/>C - tung lastbil och enbart ett lätt släpfordon<br/>CE - tung lastbil och ett eller flera släpfordon oavsett vikt<br/>D1 - mellanstor buss och enbart ett lätt släpfordon<br/>D1E - mellanstor buss och ett eller flera släpfordon oavsett vikt<br/>D - buss och enbart ett lätt släpfordon<br/>DE - buss och ett eller flera släpfordon oavsett vikt<br/>Taxi = taxiförarlegitimation<br/>Taxi = taxiförarlegitimation<br/>Annat = AM,A1,A2,A,B,BE eller traktor<br/>',

        'ts-bas.form.postadress': 'Postadress',
        'ts-bas.form.postnummer': 'Postnummer',
        'ts-bas.form.postort': 'Postort',
        'ts-bas.form.telefonnummer': 'Telefonnummer',
        'ts-bas.form.epost': 'Epost',

        'ts-bas.label.specialkompetens': 'Specialistkompetens',
        'ts-bas.label.befattningar': 'Befattningar',
        'ts-bas.label.signera': 'Signera',

        'ts-bas.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',


        // Labels for showing signed intyg
        'ts-bas.label.syn.hogeroga': 'Höger öga',
        'ts-bas.label.syn.vansteroga': 'Vänster öga',
        'ts-bas.label.syn.utankorrektion': 'Utan korrektion',
        'ts-bas.label.syn.medkorrektion': 'Med korrektion',
        'ts-bas.label.syn.kontaktlins': 'Kontaktlinser',

        'ts-bas.label.syn.binokulart': 'Binokulärt',
        'ts-bas.label.syn.vanster-oga': 'Vänster öga',
        'ts-bas.label.syn.hoger-oga': 'Höger öga',
        'ts-bas.label.syn.kontaktlinster': 'Kontaktlinser',
        'ts-bas.label.syn.utan-korrektion': 'Utan korrektion',
        'ts-bas.label.syn.med-korrektion': 'Med korrektion',
        'ts-bas.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',

        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'ts-bas.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',
        'ts-bas.label.diabetes.diabetestyp.DIABETES_TYP_1': 'Typ 1',
        'ts-bas.label.diabetes.diabetestyp.DIABETES_TYP_2': 'Typ 2',

        'ts-bas.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',

        'ts-bas.label.bedomning-info-undersokas-med-specialkompetens': 'Patienten bör före ärendets avgörande undersökas av läkare med specialistkompetens i',
        'ts-bas.label.bedomning-info-ej-angivet': 'Ej angivet',
        'ts-bas.label.nagon-av-foljande-behorigheter': 'Någon av följande behörigheter',

        'ts-bas.label.kommentar-relevant-trafiksakerhet': 'Övriga kommentarer som är relevant ur trafiksäkerhetssynpunkt.',

        // Validation messages starting
        'ts-bas.validation.utvecklingsstorning.missing': 'ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning måste anges.',
        'ts-bas.validation.psykiskt.missing': 'Psykiska sjukdomar och störningar saknas.',
        'ts-bas.validation.somnvakenhet.missing': 'Sömn- och vakenhetsstörningar saknas.',
        'ts-bas.validation.njurar.missing': 'Njursjukdomar saknas.',
        'ts-bas.validation.neurologi.missing': 'Neurologiska sjukdomar saknas.',
        'ts-bas.validation.neurologi.neurologisksjukdom.missing': 'Välj ett alternativ.',
        'ts-bas.validation.sjukhusvard.missing': 'Objektet sjukhusvård saknas.',
        'ts-bas.validation.bedomning.missing': 'Bedömning saknas.',
        'ts-bas.validation.diabetes.missing': 'Diabetes saknas.',
        'ts-bas.validation.funktionsnedsattning.missing': 'Funktionsnedsättning saknas.',
        'ts-bas.validation.hjartkarl.missing': 'Hjärt- och kärlsjukdomar saknas.',
        'ts-bas.validation.horselbalans.missing': 'Hörsel och balanssinne saknas.',
        'ts-bas.validation.kognitivt.missing': 'Demens och kognitiva störningar saknas.',
        'ts-bas.validation.medicinering.missing': 'Övrig medicinering saknas.',
        'ts-bas.validation.narkotikalakemedel.missing': 'Alkohol, narkotika och läkemedel saknas.',
        'ts-bas.validation.medvetandestorning.missing': 'Välj ett alternativ.',
        'ts-bas.validation.syn.missing': 'Synfunktioner saknas.',
        'ts-bas.validation.syn.hogeroga.missing': 'Synfunktioner relaterade till höger öga saknas.',
        'ts-bas.validation.syn.vansteroga.missing': 'Synfunktioner relaterade till vänster öga saknas.',
        'ts-bas.validation.syn.binokulart.missing': 'Binokulära synfunktioner saknas.',
        'ts-bas.validation.syn.out-of-bounds': 'Ange synskärpa i intervallet 0,0 - 2,0.',
        'ts-bas.validation.syn.r33': 'Ange ett svar.',
        'ts-bas.validation.syn.r34': 'Ange ett svar.',
        'ts-bas.validation.syn.r35': 'Ange ett svar.',
        'ts-bas.validation.syn.r37': 'Synskärpa skickas senare är vald.'
    },
    'en': {
        'ts-bas.label.pagetitle': 'Show Certificate'
    }
});
