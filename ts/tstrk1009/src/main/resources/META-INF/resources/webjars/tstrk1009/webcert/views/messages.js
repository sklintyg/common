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
angular.module('tstrk1009').constant('tstrk1009.messages', {
  'sv': {
    'tstrk1009.form.postadress': 'Postadress',
    'tstrk1009.form.postnummer': 'Postnummer',
    'tstrk1009.form.postort': 'Postort',

    'tstrk1009.label.empty': '',
    'tstrk1009.label.patientadress': 'Patientens adressuppgifter',
    'tstrk1009.label.intygavser': 'Intyget avser',
    'tstrk1009.label.identitet': 'Identiteten är styrkt genom',
    'tstrk1009.label.diabetes': '1. Allmänt',
    'tstrk1009.label.hypoglykemier': '2. Hypoglykemier (lågt blodsocker)',
    'tstrk1009.label.syn': '3. Synintyg',
    'tstrk1009.label.bedomning': '4. Bedömning',
    'tstrk1009.label.vardenhet': 'Vårdenhet',
    'tstrk1009.label.diabets.typ1': 'Typ 1',
    'tstrk1009.label.diabets.typ2': 'Typ 2',

    'tstrk1009.label.identitet.id_kort': 'ID-kort ¹',
    'tstrk1009.label.identitet.foretag_eller_tjanstekort': 'Företagskort eller tjänstekort ²',
    'tstrk1009.label.identitet.korkort': 'Svenskt körkort',
    'tstrk1009.label.identitet.pers_kannedom': 'Personlig kännedom',
    'tstrk1009.label.identitet.forsakran_kap18': 'Försäkran enligt 18 kap. 4§ ³',
    'tstrk1009.label.identitet.pass': 'Pass ⁴',

    // Visa tstrk1009
    'tstrk1009.label.patient': 'Patientens adressuppgifter',

    'tstrk1009.label.syn.binokulart': 'Binokulärt',
    'tstrk1009.label.syn.hogeroga': 'Höger öga',
    'tstrk1009.label.syn.vansteroga': 'Vänster öga',
    'tstrk1009.label.syn.utankorrektion': 'Utan korrektion',
    'tstrk1009.label.syn.medkorrektion': 'Med korrektion',
    'tstrk1009.label.syn.kontaktlins': 'Kontaktlinser',

    'tstrk1009.label.diabetes.diabetestyp.diabetes_typ_1': 'Typ 1',
    'tstrk1009.label.diabetes.diabetestyp.diabetes_typ_2': 'Typ 2',
    'tstrk1009.label.kontakt-info': 'Namn och kontaktuppgifter till vårdenheten',

    'tstrk1009.label.bedomning-info-alt-1': 'Patienten uppfyller kraven enligt Transportstyrelsens föreskrifter och allmänna råd om medicinska krav för innehav av körkort m.m. (TSFS 2010:125, senast ändrade genom TSFS 2013:2) för:',
    'tstrk1009.label.bedomning.kan-inte-ta-stallning': 'Kan inte ta ställning',
    'tstrk1009.label.bedomning-info-ej-angivet': 'Ej angivet',

    // Help texts
    'tstrk1009.helptext.identitet-styrkt-genom': 'ID-kort = SIS-märkt ID-kort, svenskt nationellt ID-kort eller ID-kort utfärdat av Skatteverket.<br/> Företagskort eller tjänstekort = SIS-märkt företagskort eller tjänstekort.<br/> Försäkran enligt 18 kap. 4 § = Försäkran enligt 18 kap 4 § i Transportstyrelsens föreskrifter (TSFS 2010:125, senast ändrade genom TSFS 2013:2): Identiteten får fastställas genom att en förälder, annan vårdnadshavare, make, maka eller sambo, registrerad partner eller myndigt barn skriftligen försäkrar att lämnade uppgifter om sökandens identitet är riktiga. Den som lämnar en sådan försäkran ska vara närvarande vid identitetskontrollen och kunna styrka sin egen identitet.<br/> Pass = Svenskt EU-pass, annat EU-pass utfärdade från och med den 1 september 2006, pass utfärdat av Island, Liechtenstein, Norge eller Schweiz från och med den 1 september 200<br/>',

    'tstrk1009.helptext.hypoglykemier.korkortd': 'För innehav av behörigheterna C1, C1E, C, CE, D1, D1E, D, DE och taxiförarlegitimation ska även fråga f-g besvaras',
    'tstrk1009.helptext.hypoglykemier.date': '(Fyll i åååå-mm-dd)',

    'tstrk1009.helptext.syn.alt1.heading': 'Alternativ 1',
    'tstrk1009.helptext.syn.alt1.text': 'Vid synnedsättningar av betydelse för innehavet krävs ögonläkarintyg. Detta gäller vid proliferativ retinopati, genomgången laserbehandling av retinopati, signifikant makulaödem eller vid annan ögonsjukdom samt om ögonbottenfoto saknas.',
    'tstrk1009.helptext.syn.alt2.heading': 'Alternativ 2: (frågorna b-d besvaras)',
    'tstrk1009.helptext.syn.alt2.text': 'Om ögonläkarintyg inte krävs kan behandlande specialistkompetent läkare med god kännedom om patientens sjukdom här avge intyg om synfunktionen.',

    'tstrk1009.helptext.synfunktioner.synskarpa': 'Synskärpa (alla bokstäver ska kunna läsas på den rad som anger synskärpan. Är synskärpan sämre än 0,1 ska den anges som 0,0).<br/><br/> Uppgifterna om synskärpa med och utan korrektion kan grundas på aktuellt intyg av bl.a. legitimerad optiker eller den som är anställd hos optiker. Alternativt kan kopia av sådant intyg bifogas. Uppgifter från ögonbottenfoto kan också användas.',
    'tstrk1009.helptext.synfunktioner.utan-korrektion': 'Uppgiften är obligatorisk',
    'tstrk1009.helptext.synfunktioner.med-korrektion': 'Uppgiften är obligatorisk om föreskriven synskärpa endast uppnås med korrektion. Definition av föreskriven synskärpa finns i Transportstyrelsens föreskrifter.',

    'tstrk1009.modal.ersatt.text': '<p>' +
        'Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades. ' +
        'När ett intyg ersätts med ett nytt, skapas ett utkast med samma information som i det ursprungliga intyget. ' +
        'Uppgifterna i det nya utkastet går att ändra innan det signeras. Ett ersatt intyg är endast tillgängligt att läsa och skriva ut. ' +
        'På det ersatta intyget kommer en länk finnas till det nya intyget.</p>' +
        '<p><b>Notera</b>: Om intyget innehåller ett allvarligt fel, till exempel om intyget är utfärdat på fel patient bör du istället makulera intyget. Det kommer då inte vara tillgängligt för invånaren via Mina intyg.</p>',

    // Validation messages
    'tstrk1009.validation.hypoglykemier.missing': 'Hypoglykemier saknas.',
    'tstrk1009.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.incorrect-date': 'Tidpunkt för allvarlig hypoglykemi under vaken tid måste anges som åååå-mm-dd, och får inte vara tidigare än ett år tillbaka eller senare än dagens datum.',
    'tstrk1009.validation.diabetes.missing': 'Diabetes saknas.',
    'tstrk1009.validation.diabetes.observationsperiod.incorrect-format': 'År måste anges enligt formatet åååå. Det går inte att ange årtal som är senare än innevarande år eller tidigare än patientens födelseår.',
    'tstrk1009.validation.diabetes.behandling.missing': 'Välj minst ett alternativ.',
    'tstrk1009.validation.diabetes.insulin.behandlingsperiod.missing': 'Ange vilket år behandling med insulin påbörjades.',
    'tstrk1009.validation.diabetes.insulin.behandlingsperiod.incorrect-format': 'År måste anges enligt formatet åååå. Det går inte att ange årtal som är senare än innevarande år eller tidigare än år 1900.',
    'tstrk1009.validation.syn.out-of-bounds': 'Ange synskärpa i intervallet 0,0 - 2,0.'
  },
  'en': {
    'view.label.pagetitle': 'Show Certificate'
  }
});
