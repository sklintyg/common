/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('fk7263').constant('fk7263.messages', {
    'sv': {
        // Labels
        'fk7263.label.pageingress.active.major.version': '<p>Här visas hela ditt läkarintyg. Från den här sidan kan du ladda ner intyget som PDF och arkivera intyget. Om du vill ansöka om sjukpenning, gör du det enklast på <LINK:forsakringskassan-sjuk>. För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.</p><p>Du har fått ett intyg från din läkare som inte går att skicka digitalt. Om du vill ansöka om sjukpenning behöver du skicka intyget med post till Försäkringskassan.</p>',
        'fk7263.label.pageingress.ersatt': 'Här visas hela ditt ersatta läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev ersatt med. Klicka på knappen Arkivera intyg för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',
        'fk7263.label.pageingress.kompletterat': 'Här visas hela ditt kompletterade läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev kompletterat med. Klicka på knappen Arkivera intyg för att flytta intyget till <i>Arkiverade intyg</i>. Du kan när som helst återställa intyget igen.',

        'fk7263.label.nedsattning.falt8b': 'Jag bedömer att arbetsförmåga är (fält 8b)',
        'fk7263.label.patientname': 'Patientens namn',
        'fk7263.label.issued': 'Inkom till Mina intyg',
        'fk7263.label.civicnumber': 'Personnummer',
        'fk7263.label.issuer': 'Vårdgivare',
        'fk7263.label.period': 'Period',
        'fk7263.label.unit': 'Enhet',
        'fk7263.label.smittskydd.kategori': 'Avstängning enligt smittskyddslagen på grund av smitta (fält 1)',
        'fk7263.label.yes': 'Ja',
        'fk7263.label.no': 'Nej',
        'fk7263.label.partialyes': 'Ja, delvis',
        'fk7263.label.diagnosis': 'Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga (fält 2)',
        'fk7263.label.diagnosiscode': 'Diagnoskod enligt ICD-10 (huvuddiagnos)',
        'fk7263.label.ytterligarediagnoser': 'Ytterligare diagnoser',
        'fk7263.label.diagnosfortydligande': 'Förtydligande av diagnos/diagnoser',
        'fk7263.label.samsjuklighet': 'Samsjuklighet föreligger',
        'fk7263.label.progressofdesease': 'Aktuellt sjukdomförlopp (fält 3)',
        'fk7263.label.disabilities': 'Funktionsnedsättning - observationer, undersökningsfynd och utredningsresultat (fält 4)',
        'fk7263.label.basedon': 'Intyget baseras på (fält 4b)',
        'fk7263.label.comment': 'Kommentar:',
        'fk7263.label.limitation': 'Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning (fält 5)',
        'fk7263.label.recommendations': 'Rekommendationer (fält 6a)',
        'fk7263.label.recommendations.anpassatintyg.1': 'Rekommendationer (fält 6a)',
        'fk7263.label.recommendations.anpassatintyg.2': 'Rekommendationer (fält 6a) - kontakt med företagshälsovård',
        'fk7263.label.plannedtreatment': 'Planerad eller pågående behandling eller åtgärd (fält 6b)',
        'fk7263.label.plannedtreatment.healthcare': 'Inom sjukvården',
        'fk7263.label.plannedtreatment.other': 'Annan åtgärd',
        'fk7263.label.workrehab': 'Är arbetslivsinriktad rehabilitering aktuell? (fält 7)',
        'fk7263.label.unjudgeable': 'Går inte att bedöma',
        'fk7263.label.patientworkcapacity': 'Patientens arbetsförmåga bedöms i förhållande till (fält 8a)',
        'fk7263.label.patientworkcapacity.currentwork': 'Nuvarande arbete',
        'fk7263.label.patientworkcapacity.unemployed': 'Arbetslöshet - att utföra sådant arbete som är normalt förekommande på arbetsmarknaden',
        'fk7263.label.patientworkcapacity.parentalleave': 'Föräldraledighet med föräldrapenning - att vårda sitt barn',
        'fk7263.label.patientworkcapacityjudgement': 'Patientens arbetsförmåga bedöms nedsatt längre tid än den som det försäkringsmedicinska beslutsstödet anger, därför att:  (fält 9)',
        'fk7263.label.prognosis': 'Prognos - kommer patienten att få tillbaka sin arbetsförmåga i nuvarande arbete? (Gäller inte arbetslösa) (fält 10)',
        'fk7263.label.othertransport': 'Kan resor till och från arbetet med annat färdsätt än normalt göra det möjligt för patienten att återgå i arbete? (fält 11)',
        'fk7263.label.fkcontact': 'Kontakt önskas med Försäkringskassan (fält 12)',
        'fk7263.label.workcodes': 'Förskrivarkod och arbetsplatskod (fält 17)',
        'fk7263.label.recommendations.contact.jobcenter': 'Kontakt med Arbetsförmedlingen',
        'fk7263.label.recommendations.contact.healthdepartment': 'Kontakt med företagshälsovården',
        'fk7263.label.recommendations.contact.other': 'Övrigt',
        'fk7263.label.reference.to.field13': 'Se under Övriga upplysningar och förtydliganden (fält 13)',
        'fk7263.label.otherinformation': 'Övriga upplysningar och förtydliganden (fält 13)',

        'fk7263.button.goback': 'Tillbaka',

        // Misc
        'fk7263.common.cancel': 'Avbryt',
        'fk7263.helptext.readpdfhelp': '<b>Läsa en pdf-fil</b><br/>PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna pdf-filer behöver du en pdf-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank">Adobe Reader</a>.',
        'fk7263.history.label.pagetitle': 'Intygets alla händelser',

        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.

        'fk7263.nedsattningsgrad.helt_nedsatt': 'Helt nedsatt',
        'fk7263.nedsattningsgrad.nedsatt_med_3_4': 'Nedsatt med 3/4',
        'fk7263.nedsattningsgrad.nedsatt_med_1_2': 'Nedsatt med hälften',
        'fk7263.nedsattningsgrad.nedsatt_med_1_4': 'Nedsatt med 1/4',

        'fk7263.vardkontakt.5880005': 'Min undersökning av patienten den %0',
        'fk7263.vardkontakt.undersokning': 'Min undersökning av patienten ',
        'fk7263.vardkontakt.185317003': 'Min telefonkontakt med patienten den %0',
        'fk7263.vardkontakt.telefonkontakt': 'Min telefonkontakt med patienten ',

        'fk7263.referens.419891008': 'Journaluppgifter, den %0',
        'fk7263.referens.journal': 'Journaluppgifter',
        'fk7263.referens.74964007': 'Annat, den %0',
        'fk7263.referens.annat': 'Annat',


        'fk7263.inbox.complementaryinfo': 'Intygsperiod',
        'fk7263.compact-header.complementaryinfo-prefix': 'Gäller intygsperiod:'

    },
    'en': {
        'fk7263.label.pagetitle': 'Show Certificate'
    }
});
