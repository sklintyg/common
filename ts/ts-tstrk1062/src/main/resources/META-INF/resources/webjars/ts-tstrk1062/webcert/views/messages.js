/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
angular.module('ts-tstrk1062').constant('ts-tstrk1062.messages', {
    'sv': {
        'ts-tstrk1062.label.patient': 'Patientens adressuppgifter',
        'ts-tstrk1062.label.patientadress': 'Patientens adressuppgifter',
        'ts-tstrk1062.label.intygavser': 'Intyget avser',
        'ts-tstrk1062.label.identitet': 'Identiteten är styrkt genom',
        'ts-tstrk1062.label.syn': '1. Synfunktioner',
        'ts-tstrk1062.label.horselbalans': '2. Hörsel och balanssinne',
        'ts-tstrk1062.label.funktionsnedsattning': '3. Rörelseorganens funktioner',
        'ts-tstrk1062.label.hjartkarl': '4. Hjärt- och kärlsjukdomar',
        'ts-tstrk1062.label.diabetes': '5. Diabetes',
        'ts-tstrk1062.label.neurologi': '6. Neurologiska sjukdomar',
        'ts-tstrk1062.label.medvetandestorning': '7. Epilepsi, epileptiskt anfall och annan medvetandestörning',
        'ts-tstrk1062.label.njurar': '8. Njursjukdomar',
        'ts-tstrk1062.label.kognitivt': '9. Demens och andra kognitiva störningar',
        'ts-tstrk1062.label.somnvakenhet': '10. Sömn- och vakenhetsstörningar',
        'ts-tstrk1062.label.narkotikalakemedel': '11. Alkohol, narkotika och läkemedel',
        'ts-tstrk1062.label.psykiskt': '12. Psykiska sjukdomar och störningar',
        'ts-tstrk1062.label.utvecklingsstorning': '13. ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning',
        'ts-tstrk1062.label.sjukhusvard': '14. Sjukhusvård',
        'ts-tstrk1062.label.medicinering': '15. Övrig medicinering',
        'ts-tstrk1062.label.ovrigkommentar': '16. Övrig kommentar',
        'ts-tstrk1062.label.bedomning': 'Bedömning',
        'ts-tstrk1062.label.vardenhet': 'Vårdenhet',
        
        'ts-tstrk1062.label.identitet.id_kort': 'ID-kort',
        'ts-tstrk1062.label.identitet.foretag_eller_tjanstekort': 'Företagskort eller tjänstekort',
        'ts-tstrk1062.label.identitet.korkort': 'Svenskt körkort',
        'ts-tstrk1062.label.identitet.pers_kannedom': 'Personlig kännedom',
        'ts-tstrk1062.label.identitet.forsakran_kap18': 'Försäkran enligt 18 kap. 4§',
        'ts-tstrk1062.label.identitet.pass': 'Pass',

        'ts-tstrk1062.helptext.intyg-avser': '<span style="text-align:left">C1 = medeltung lastbil och enbart ett lätt släpfordon<br/>C1E = medeltung lastbil och ett eller flera släpfordon oavsett vikt<br/>C = tung lastbil och enbart ett lätt släpfordon<br/>CE = tung lastbil och ett eller flera släpfordon oavsett vikt<br/>D1 = mellanstor buss och enbart ett lätt släpfordon<br/>D1E = mellanstor buss och ett eller flera släpfordon oavsett vikt<br/>D = buss och enbart ett lätt släpfordon<br/>DE = buss och ett eller flera släpfordon oavsett vikt<br/>Taxi = taxiförarlegitimation<br/>Annat = (AM,A1,A2,A,B,BE eller Traktor)</span>',

        'ts-tstrk1062.helptext.identitet': 'Identitet styrkt genom någon av nedanstående',

        'ts-tstrk1062.helptext.synfunktioner.info-8-dioptrier': 'Intyg om korrektionsglasens styrka måste skickas in.',
        'ts-tstrk1062.helptext.synfunktioner.8-dioptrier-valt': 'Du har kryssat i frågan om 8 dioptrier – Glöm inte att skicka in intyg om korrektionsglasens styrka.',
        'ts-tstrk1062.helptext.synfunktioner.utan-korrektion': 'Uppgiften är obligatorisk',
        'ts-tstrk1062.helptext.synfunktioner.med-korrektion': 'Uppgiften är obligatorisk om föreskriven synskärpa endast uppnås med korrektion. Definition av föreskriven synskärpa finns i Transportstyrelsens föreskrifter.',

        'ts-tstrk1062.helptext.bedomning.info': 'Om någon av frågorna har besvarats med ja, ska de krav på ytterligare underlag som framgår av föreskrifterna beaktas.',

        'ts-tstrk1062.helptext.diabetes.behandling': 'Vid tablett- eller insulinbehandlad diabetes krävs det att ett läkarintyg gällande sjukdomen skickas in.',
        'ts-tstrk1062.helptext.narkotika-lakemedel.provtagning': 'Om ja på ovanstående ska resultatet redovisas separat.',
        'ts-tstrk1062.helptext.bedomning': '<span style="text-align:left">C1 - medeltung lastbil och enbart ett lätt släpfordon<br/>C1E - medeltung lastbil och ett eller flera släpfordon oavsett vikt<br/>C - tung lastbil och enbart ett lätt släpfordon<br/>CE - tung lastbil och ett eller flera släpfordon oavsett vikt<br/>D1 - mellanstor buss och enbart ett lätt släpfordon<br/>D1E - mellanstor buss och ett eller flera släpfordon oavsett vikt<br/>D - buss och enbart ett lätt släpfordon<br/>DE - buss och ett eller flera släpfordon oavsett vikt<br/>Taxi = taxiförarlegitimation<br/>Taxi = taxiförarlegitimation<br/>Annat = AM,A1,A2,A,B,BE eller traktor<br/>',

        'ts-tstrk1062.form.postadress': 'Postadress',
        'ts-tstrk1062.form.postnummer': 'Postnummer',
        'ts-tstrk1062.form.postort': 'Postort',
        'ts-tstrk1062.form.telefonnummer': 'Telefonnummer',
        'ts-tstrk1062.form.epost': 'Epost',

        'ts-tstrk1062.label.specialkompetens': 'Specialistkompetens',
        'ts-tstrk1062.label.befattningar': 'Befattningar',
        'ts-tstrk1062.label.signera': 'Signera',

        'ts-tstrk1062.modal.ersatt.text':'<p>Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. Uppgifterna i det nya utkastet går att ändra innan det signeras. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',


        // Labels for showing signed intyg
        'ts-tstrk1062.label.syn.hogeroga': 'Höger öga',
        'ts-tstrk1062.label.syn.vansteroga': 'Vänster öga',
        'ts-tstrk1062.label.syn.utankorrektion': 'Utan korrektion',
        'ts-tstrk1062.label.syn.medkorrektion': 'Med korrektion',
        'ts-tstrk1062.label.syn.kontaktlins': 'Kontaktlinser',

        'ts-tstrk1062.label.syn.binokulart': 'Binokulärt',
        'ts-tstrk1062.label.syn.vanster-oga': 'Vänster öga',
        'ts-tstrk1062.label.syn.hoger-oga': 'Höger öga',
        'ts-tstrk1062.label.syn.kontaktlinster': 'Kontaktlinser',
        'ts-tstrk1062.label.syn.utan-korrektion': 'Utan korrektion',
        'ts-tstrk1062.label.syn.med-korrektion': 'Med korrektion',
        'ts-tstrk1062.label.syn.korrektionsglasens-styrka': 'Korrektionsglasens styrka',

        'ts-tstrk1062.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
        'ts-tstrk1062.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',
        'ts-tstrk1062.label.diabetes.diabetestyp.DIABETES_TYP_1': 'Typ 1',
        'ts-tstrk1062.label.diabetes.diabetestyp.DIABETES_TYP_2': 'Typ 2',

        'ts-tstrk1062.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',

        'ts-tstrk1062.label.bedomning-info-undersokas-med-specialkompetens': 'Patienten bör före ärendets avgörande undersökas av läkare med specialistkompetens i',
        'ts-tstrk1062.label.bedomning-info-ej-angivet': 'Ej angivet',
        'ts-tstrk1062.label.nagon-av-foljande-behorigheter': 'Någon av följande behörigheter',

        'ts-tstrk1062.label.kommentar-relevant-trafiksakerhet': 'Övriga kommentarer som är relevant ur trafiksäkerhetssynpunkt.',

        // Validation messages starting
        'ts-tstrk1062.validation.utvecklingsstorning.missing': 'ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning måste anges.',
        'ts-tstrk1062.validation.psykiskt.missing': 'Psykiska sjukdomar och störningar saknas.',
        'ts-tstrk1062.validation.somnvakenhet.missing': 'Sömn- och vakenhetsstörningar saknas.',
        'ts-tstrk1062.validation.njurar.missing': 'Njursjukdomar saknas.',
        'ts-tstrk1062.validation.neurologi.missing': 'Neurologiska sjukdomar saknas.',
        'ts-tstrk1062.validation.neurologi.neurologisksjukdom.missing': 'Välj ett alternativ.',
        'ts-tstrk1062.validation.sjukhusvard.missing': 'Objektet sjukhusvård saknas.',
        'ts-tstrk1062.validation.bedomning.missing': 'Bedömning saknas.',
        'ts-tstrk1062.validation.diabetes.missing': 'Diabetes saknas.',
        'ts-tstrk1062.validation.funktionsnedsattning.missing': 'Funktionsnedsättning saknas.',
        'ts-tstrk1062.validation.hjartkarl.missing': 'Hjärt- och kärlsjukdomar saknas.',
        'ts-tstrk1062.validation.horselbalans.missing': 'Hörsel och balanssinne saknas.',
        'ts-tstrk1062.validation.kognitivt.missing': 'Demens och kognitiva störningar saknas.',
        'ts-tstrk1062.validation.medicinering.missing': 'Övrig medicinering saknas.',
        'ts-tstrk1062.validation.narkotikalakemedel.missing': 'Alkohol, narkotika och läkemedel saknas.',
        'ts-tstrk1062.validation.medvetandestorning.missing': 'Välj ett alternativ.',
        'ts-tstrk1062.validation.syn.missing': 'Synfunktioner saknas.',
        'ts-tstrk1062.validation.syn.hogeroga.missing': 'Synfunktioner relaterade till höger öga saknas.',
        'ts-tstrk1062.validation.syn.vansteroga.missing': 'Synfunktioner relaterade till vänster öga saknas.',
        'ts-tstrk1062.validation.syn.binokulart.missing': 'Binokulära synfunktioner saknas.',
        'ts-tstrk1062.validation.syn.out-of-bounds': 'Ange synskärpa i intervallet 0,0 - 2,0.',
        'ts-tstrk1062.validation.syn.r33': 'Ange ett svar.',
        'ts-tstrk1062.validation.syn.r34': 'Ange ett svar.',
        'ts-tstrk1062.validation.syn.r35': 'Ange ett svar.'
    },
    'en': {
        'ts-tstrk1062.label.pagetitle': 'Show Certificate'
    }
});
