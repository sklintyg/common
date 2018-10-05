/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('common').constant('common.icf', {
    'f322':
        {'funktion': [
            {
                namn: 'Temperament och personlighet',
                vald: undefined,
                beskrivning: 'Allmänna psykiska funktioner i en persons konstitutionella disposition att reagera på ett särskilt sätt i situationer och som innefattar den uppsättning av psykiska egenskaper som gör att personen klart skiljer sig från andra',
                innefattar: 'utåtvändhet, inåtvändhet, behaglighet, samvetsgrannhet, psykisk och emotionell stabilitet och öppenhet för upplevelser; optimism; att söka nyheter; självförtroende; trovärdighet'
            },{
                namn: 'Energi och driftfunktioner',
                vald: undefined,
                beskrivning: 'Allmänna psykiska funktioner av fysiologiska och psykologiska mekanismer som gör att personen envist strävar efter att tillfredsställa specifika behov och allmänna mål',
                innefattar: 'funktioner av energinivå, motivation, aptit; begär efter substanser, inklusive sådana som kan missbrukas; impulskontroll'
            },{
                namn: 'Sömn',
                vald: undefined,
                beskrivning: 'Allmänna psykiska funktioner av periodisk, reversibel och selektiv fysisk och psykisk avkoppling från sin egen omedelbara omgivning med samtidiga karakteristiska fysiologiska förändringar',
                innefattar: 'funktioner av sömnmängd, insomning, sömnunderhåll, sömnkvalitet, funktioner som rör sömncykeln såsom sömnlöshet, hypersömn, narkolepsi'
            },{
                namn: 'Uppmärksamhet',
                vald: undefined,
                beskrivning: 'Särskilda psykiska funktioner att under en erforderlig tidsperiod rikta in sig mot ett yttre stimulus eller inre erfarenhet',
                innefattar: 'funktioner att vidmakthålla uppmärksamhet, att skifta uppmärksamhet, att fördela uppmärksamhet, dela gemensam uppmärksamhet; koncentration; distraherbarhet'
            },{
                namn: 'Psykomotorik',
                vald: undefined,
                beskrivning: 'Specifika psykiska funktioner för kontroll över motoriska och psykologiska händelser på kroppsnivå',
                innefattar: 'funktioner av psykomotorisk kontroll, såsom psykomotorisk långsamhet, överretning och motorisk oro, avvikande kroppshållning, katatoni; negativism; ambitendens; ekopraxi och ekolali; psykomotoriska funktioners kvalitet'
            },{
                namn: 'Tankefunktioner',
                vald: undefined,
                beskrivning: 'Specifika psykiska funktioner som sammanhänger med förmågan att skapa tankemässiga representationer',
                innefattar: 'funktioner av tempo, form, kontroll och innehåll i tanke; målinriktade tankefunktioner, icke-målinriktade tankefunktioner; logiska tankefunktioner såsom vid tanketryck, idéflykt, tankeblockering, osammanhängande tankar, ovidkommande tankar, omständlighet, vanföreställningar och tvångstankar, tvångsföreställningar'
            },{
                namn: 'Minne',
                vald: undefined,
                beskrivning: 'Specifika psykiska funktioner som registrerar och lagrar information och återkallar den vid behov',
                innefattar: 'funktioner av kort- och långtidsminne, omedelbart, nyligt och avlägset minne; minnesomfång; att dra sig till minnes; att erinra sig; funktioner som används för att komma ihåg och att lära in, såsom vid allmän, selektiv och dissociativ minnesförlust'
            },{
                namn: 'Emotioner',
                vald: undefined,
                beskrivning: 'Specifika psykiska funktioner som hänför sig till känslo- och affektkomponenterna i tankeprocesserna',
                innefattar: 'funktioner av emotionens lämplighet, reglering och omfattning; affekt; sorgsenhet, lycka, kärlek, rädsla, ilska, hat, spänning, ångest, glädje, sorg; emotionslabilitet; utslätning av affekt'
            },{
                namn: 'Högre kognitiva funktioner',
                vald: undefined,
                beskrivning: 'Specifika psykiska funktioner särskilt relaterade till frontalloberna, inklusive komplext målinriktat beteende såsom beslutsfattande, abstrakt tänkande, planering och utförande av planer, mental flexibilitet och att fatta beslut om vilket beteende som är påkallat under vilka omständigheter, ofta benämnda exekutiva funktioner',
                innefattar: 'funktioner att göra abstraktioner och organisera tankar; tidsplanering, insikt och omdöme; begreppsbildning, kategorisering och kognitiv flexibilitet'
            },{
                namn: 'Avföringsfunktioner',
                vald: undefined,
                beskrivning: 'Funktioner att eliminera restprodukter och osmält föda som avföring och därmed sammanhängande funktioner',
                innefattar: 'avlägsningsfunktioner, avföringskonsistens, avföringsfrekvens, avföringskontinens, väderspänningar; funktionsnedsättningar såsom förstoppning, diarré, vattnig avföring, nedsatt förmåga i ändtarmens slutmuskel eller inkontinens'
            },{
                namn: 'Muskeluthållighet',
                vald: undefined,
                beskrivning: 'Funktioner vad avser att upprätthålla muskelsammandragning under erforderlig tid',
                innefattar: 'funktioner att upprätthålla muskelsammandragning i enskilda muskler och muskelgrupper och i kroppens alla muskler; funktionsnedsättningar som t.ex. myasthenia gravis'
            }],
        'aktivitet': [
            {
                namn: 'Att fokusera uppmärksamhet',
                vald: undefined,
                beskrivning: 'Att avsiktligt fokusera på specifika stimuli t.ex. genom att filtrera bort störande ljud',
                innefattar: ''
            },{
                namn: 'Att tänka',
                vald: undefined,
                beskrivning: 'Att formulera och hantera idéer, begrepp och bilder vare sig de är målinriktade eller ej, antingen ensam eller tillsammans med andra såsom att hitta på en historia, bevisa ett teorem, leka med idéer, komma på idéer, förmedla, fundera eller reflektera',
                innefattar: ''
            },{
                namn: 'Att lösa problem',
                vald: undefined,
                beskrivning: 'Att finna lösningar på problem eller situationer genom att identifiera och analysera frågor, utveckla möjliga lösningar, utvärdera tänkbara effekter av lösningar och genomföra en vald lösning såsom att lösa en konflikt mellan två personer',
                innefattar: 'att lösa enkla och sammansatta problem'
            },{
                namn: 'Att fatta beslut',
                vald: undefined,
                beskrivning: 'Att göra ett val mellan alternativ, att förverkliga valet och utvärdera effekterna av valet såsom att välja och köpa en specifik sak eller att besluta att göra och även genomföra en uppgift bland flera uppgifter som behöver genomföras',
                innefattar: ''
            },{
                namn: 'Att företa enstaka uppgifter',
                vald: undefined,
                beskrivning: 'Att genomföra enkla eller komplicerade och koordinerade handlingar som sammanhänger med de psykiska och fysiska komponenterna i en enstaka uppgift såsom att påbörja en uppgift, att organisera tid, rum och material till uppgiften, att planera uppgiften stegvis, genomföra, avsluta och upprätthålla en uppgift',
                innefattar: 'att företa en enkel eller en komplicerad uppgift; att företa en enstaka uppgift självständigt eller i grupp'
            },{
                namn: 'Att företa mångfaldiga uppgifter',
                vald: undefined,
                beskrivning: 'Att genomföra enkla eller komplicerade och koordinerade handlingar som komponenter i mångfaldiga, integrerade och komplicerade uppgifter i följd eller samtidigt',
                innefattar: 'att företa mångfaldiga uppgifter; fullfölja mångfaldiga uppgifter; att företa mångfaldiga uppgifter självständigt eller i grupp'
            },{
                namn: 'Att genomföra daglig rutin',
                vald: undefined,
                beskrivning: 'Att genomföra enkla eller sammansatta och samordnade handlingar för att planera, hantera och fullfölja vad de dagliga rutinerna kräver såsom att beräkna tid och göra upp planer för olika aktiviteter under dagen',
                innefattar: 'att hantera och fullfölja dagliga rutiner; att anpassa sin egen aktivitetsnivå'
            },{
                namn: 'Att hantera stress och andra psykologiska krav',
                vald: undefined,
                beskrivning: 'Att genomföra enkla eller sammansatta och samordnade handlingar för att klara och kontrollera de psykologiska krav som ställs för att genomföra uppgifter som kräver betydande ansvarstagande och innefattar stress, oro eller kris såsom när man kör ett fordon i stark trafik eller tar hand om många barn',
                innefattar: 'att hantera ansvarstagande; att hantera stress och kris'
            },{
                namn: 'Att sköta sin egen hälsa',
                vald: undefined,
                beskrivning: 'Att tillförsäkra sig fysisk bekvämlighet, hälsa och fysiskt och psykiskt välbefinnande såsom att upprätthålla en balanserad diet, lämplig nivå av fysisk aktivitet, hålla sig varm eller kall, undvika hälsorisker, ha säkra sexualvanor såsom att använda kondom, bli vaccinerad och genomgå regelbundna hälsokontroller',
                innefattar: 'att försäkra sig om fysisk bekvämlighet, att ta hand om diet och kondition, bibehålla sin egen hälsa'
            },{
                namn: 'Att förvärva färdigheter',
                vald: undefined,
                beskrivning: 'Att utveckla grundläggande och sammansatta förmågor att integrera handlingar eller uppgifter som att initiera och fullfölja förvärvandet av en färdighet såsom att hantera verktyg eller leksaker eller spela spel, t.ex. schack',
                innefattar: 'att förvärva grundläggande och sammansatta färdigheter'
            },{
                namn: 'Att uttrycka sig genom icke-verbala meddelanden',
                vald: undefined,
                beskrivning: 'Att använda gester, symboler och teckningar för att uttrycka meddelanden såsom att skaka på huvudet för att antyda bristande instämmande eller att teckna en bild eller diagram för att uttrycka ett faktum eller en komplex idé',
                innefattar: 'att framställa kroppsliga gester, tecken, symboler, teckningar och fotografier'
            },{
                namn: 'Att tala',
                vald: undefined,
                beskrivning: 'Att åstadkomma ord, fraser eller längre avsnitt i talade meddelanden med ordagrann och dold innebörd såsom att uttrycka ett faktum eller berätta en historia muntligt',
                innefattar: ''
            },{
                namn: 'Att konversera',
                vald: undefined,
                beskrivning: 'Att starta, hålla igång och slutföra ett utbyte av tankar och idéer genom talat, skrivet, tecknat eller annan form av språk med en eller flera personer som man känner eller som är främmande, i formella eller tillfälliga miljöer',
                innefattar: 'att starta, hålla igång och slutföra ett samtal; att samtala med en eller flera personer'
            },{
                namn: 'Att diskutera',
                vald: undefined,
                beskrivning: 'Att starta, hålla igång och slutföra en genomgång av ett ämne genom att ge argument för och emot eller att debattera med hjälp av talat, skrivet, tecknat eller annan form av språk, med en eller flera personer som man känner eller som är okända, i formella eller tillfälliga miljöer',
                innefattar: 'diskussion med en eller flera personer'
            },{
                namn: 'Att använda transportmedel',
                vald: undefined,
                beskrivning: 'Att använda transportmedel för att som passagerare förflytta sig såsom att bli körd i en bil eller buss, riksha, minibuss, på ett fordon draget av djur, i en privat eller offentlig taxi, buss, tåg, spårvagn, tunnelbana, båt eller flygplan och använda människor för transport',
                innefattar: 'att använda mänskligt drivna transportmedel, privata eller offentliga motoriserade transportmedel eller människor för transport'
            },{
                namn: 'Att vara förare',
                vald: undefined,
                beskrivning: 'Att ha kontroll över och köra ett fordon eller driva ett djur som drar det, att åka i egen regi eller till sitt förfogande ha något slag av transportmedel såsom en bil, cykel, båt eller djurdrivet fordon',
                innefattar: 'att föra ett mänskligt drivet transportmedel, motoriserade fordon, djurdrivna fordon'
            },{
                namn: 'Att bereda måltider',
                vald: undefined,
                beskrivning: 'Att planera, organisera, laga och servera enkla och sammansatta måltider till sig själv och andra såsom att göra upp en matsedel, välja ut ätlig mat och dryck och samla ihop ingredienser för att bereda måltider, laga varm mat och förbereda kall mat och dryck samt servera maten',
                innefattar: 'att bereda enkla och sammansatta måltider'
            },{
                namn: 'Att skaffa varor och tjänster',
                vald: undefined,
                beskrivning: 'Att välja, anskaffa och transportera alla varor och tjänster som krävs för det dagliga livet såsom att välja, anskaffa, transportera och förvara mat, dryck, kläder, rengöringsmaterial, bränsle, hushållsartiklar, husgeråd, kokkärl, hushållsredskap och verktyg; att anskaffa nyttoföremål och andra hushållstjänster',
                innefattar: 'att handla och samla ihop dagliga förnödenheter'
            },{
                namn: 'Hushållsarbete',
                vald: undefined,
                beskrivning: 'Att klara ett hushåll innefattande att hålla rent i hemmet, tvätta kläder, använda hushållsapparater, lagra mat och ta hand om avfall såsom att sopa, bona, tvätta bänkar, väggar och andra ytor, samla och kasta avfall, städa rum, garderober och lådor, samla ihop, tvätta, torka, vika och stryka kläder, putsa skor, använda kvastar, borstar och dammsugare, använda tvättmaskin, torkapparat och strykjärn',
                innefattar: 'att tvätta och torka kläder, att rengöra kök och köksredskap, städa bostaden, använda hushållsapparater, förvara dagliga förnödenheter och slänga avfall'
            },{
                namn: 'Socialt samspel',
                vald: undefined,
                beskrivning: 'Att samspela med människor på ett i sammanhanget socialt lämpligt sätt såsom att när det är lämpligt visa hänsynstagande och uppskattning eller att reagera på andras känslor',
                innefattar: 'att visa respekt, värme, tacksamhet och tolerans i förhållanden, att svara på kritik och sociala signaler och att använda lämplig fysisk kontakt'
            },{
                namn: 'Interaktioner med andra',
                vald: undefined,
                beskrivning: 'Att bibehålla och hantera interaktioner med andra människor på ett i sammanhanget och socialt lämpligt sätt såsom att reglera känslor och impulser, reglera verbal och fysisk aggression, handla oberoende i sociala interaktioner och att handla i överensstämmelse med sociala regler och konventioner med exempelvis att leka, studera eller arbeta med andra',
                innefattar: 'att bygga upp och avsluta relationer, interagera i överensstämmelse med sociala regler, reglera beteenden i interaktioner och behålla socialt avstånd'
            },{
                namn: 'Formella relationer',
                vald: undefined,
                beskrivning: 'Att skapa och bibehålla specifika relationer i formella sammanhang såsom med lärare, arbetsgivare, yrkesutövande eller servicegivare',
                innefattar: 'att ha kontakt med makthavare, underordnade och jämställda personer'
            }]
        },
    'm751':
        {'funktion': [
            {
                namn: 'Energi och driftfunktioner',
                vald: undefined,
                beskrivning: 'Allmänna psykiska funktioner av fysiologiska och psykologiska mekanismer som gör att personen envist strävar efter att tillfredsställa specifika behov och allmänna mål',
                innefattar: 'funktioner av energinivå, motivation, aptit; begär efter substanser, inklusive sådana som kan missbrukas; impulskontroll'
            },{
                namn: 'Sömn',
                vald: undefined,
                beskrivning: 'Allmänna psykiska funktioner av periodisk, reversibel och selektiv fysisk och psykisk avkoppling från sin egen omedelbara omgivning med samtidiga karakteristiska fysiologiska förändringar',
                innefattar: 'funktioner av sömnmängd, insomning, sömnunderhåll, sömnkvalitet, funktioner som rör sömncykeln såsom sömnlöshet, hypersömn, narkolepsi'
            },{
                namn: 'Uppmärksamhet',
                vald: undefined,
                begransning: 'Välj gradering',
                beskrivning: 'Särskilda psykiska funktioner att under en erforderlig tidsperiod rikta in sig mot ett yttre stimulus eller inre erfarenhet',
                innefattar: 'funktioner att vidmakthålla uppmärksamhet, att skifta uppmärksamhet, att fördela uppmärksamhet, dela gemensam uppmärksamhet; koncentration; distraherbarhet'
            },{
                namn: 'Smärta',
                vald: undefined,
                beskrivning: 'Förnimmelse av obehaglig känsla som tyder på möjlig eller faktisk skada i någon kroppsstruktur som känns antingen i en eller båda övre extremiteterna innefattande händer',
                innefattar: 'förnimmelser av generell eller lokal smärta i en eller flera kroppsdelar, smärta i ett dermatom (hudsegment), huggande, brännande, molande smärta och värk; funktionsnedsättningar såsom myalgi (muskelsmärta), analgesi (okänslighet för smärta) och hyperalgesi (överkänslighet för smärta)'
            },{
                namn: 'Ledrörlighet',
                vald: undefined,
                beskrivning: 'Funktioner för rörelseomfång och smidighet vad avser rörelse i en led',
                innefattar: 'funktioner att röra en enstaka eller flera leder, ryggrad, skuldra, armbåge, handled, höft, knä, fotled, småleder i hand och fot; allmän ledrörlighet; funktionsnedsättningar såsom överrörlighet i leder, stela leder, frusen skuldra, artrit'
            },{
                namn: 'Muskelkraft',
                vald: undefined,
                beskrivning: 'Funktioner vad avser den styrka som genereras genom sammandragning av muskler och muskelgrupper i arm eller ben',
                innefattar: 'funktioner som rör styrka i specifika muskler och muskelgrupper, muskler i en extremitet, i ena sidan av kroppen, i nedre kroppshalvan, i alla extremiteter, i bålen och i kroppens alla muskler; funktionsnedsättningar såsom pares, paralys, monoplegi, hemiplegi, paraplegi, tetraplegi och akinesi'
            },{
                namn: 'Muskeluthållighet',
                vald: undefined,
                beskrivning: 'Funktioner vad avser att upprätthålla muskelsammandragning under erforderlig tid',
                innefattar: 'funktioner att upprätthålla muskelsammandragning i enskilda muskler och muskelgrupper och i kroppens alla muskler; funktionsnedsättningar som t.ex. myasthenia gravis'
            }],
        'aktivitet': [{
                namn: 'Att hantera stress och andra psykologiska krav',
                vald: undefined,
                beskrivning: 'Att genomföra enkla eller sammansatta och samordnade handlingar för att klara och kontrollera de psykologiska krav som ställs för att genomföra uppgifter som kräver betydande ansvarstagande och innefattar stress, oro eller kris såsom när man kör ett fordon i stark trafik eller tar hand om många barn',
                innefattar: 'att hantera ansvarstagande; att hantera stress och kris'
            },{
                namn: 'Att lyfta och bära föremål',
                vald: undefined,
                beskrivning: 'Att lyfta upp ett föremål eller ta något från en plats till en annan såsom att lyfta en kopp eller leksak eller att bära en låda eller ett barn från ett rum till ett annat',
                innefattar: 'att lyfta, bära i händerna, i armarna, på skuldrorna, höften, ryggen eller huvudet och att sätta ner'
            },{
                namn: 'Handens finmotoriska användning',
                vald: undefined,
                beskrivning: 'Att genomföra koordinerade handlingar för att hantera föremål, plocka upp, behandla och släppa dem genom att använda hand, fingrar och tumme såsom krävs för att plocka upp ett mynt från ett bord, slå ett telefonnummer eller trycka på en knapp',
                innefattar: 'att plocka upp, gripa, hantera och släppa'
            },{
                namn: 'Att använda arm och hand',
                vald: undefined,
                beskrivning: 'Att genomföra koordinerade handlingar som krävs för att flytta föremål eller hantera dem genom att använda händer och armar såsom att vrida på dörrhandtag eller kasta eller fånga ett föremål',
                innefattar: 'att dra eller knuffa föremål, att räcka fram, vända eller vrida händer eller armar, att kasta, att fånga'
            },{
                namn: 'Att tvätta sig',
                vald: undefined,
                beskrivning: 'Att tvätta och torka hela kroppen eller delar av den genom att använda vatten och lämpliga material och metoder för att bli ren och torr såsom att bada, duscha, tvätta händer och fötter, ansikte och hår och att torka sig med handduk',
                innefattar: 'att tvätta delar av kroppen, hela kroppen och att torka sig'
            },{
                namn: 'Att sköta toalettbehov',
                vald: undefined,
                beskrivning: 'Att planera och genomföra uttömning av mänskliga restprodukter (menstruation, urinering och avföring) och att göra sig ren efteråt',
                innefattar: 'att kontrollera urinering, avföring och ta hand om menstruation'
            },{
                namn: 'Att klä sig',
                vald: undefined,
                beskrivning: 'Att genomföra samordnade handlingar och uppgifter att ta på och av kläder och skodon i ordning och i enlighet med klimat och sociala villkor såsom att sätta på sig, rätta till och ta av skjorta, kjol, blus, underkläder, sari, kimono, tights, hatt, handskar, kappa, skor, kängor, sandaler och tofflor',
                innefattar: 'att ta på och av kläder och skor och att välja lämplig klädsel'
            },{
                namn: 'Att sköta sin egen hälsa',
                vald: undefined,
                beskrivning: 'Att tillförsäkra sig fysisk bekvämlighet, hälsa och fysiskt och psykiskt välbefinnande såsom att upprätthålla en balanserad diet, lämplig nivå av fysisk aktivitet, hålla sig varm eller kall, undvika hälsorisker, ha säkra sexualvanor såsom att använda kondom, bli vaccinerad och genomgå regelbundna hälsokontroller',
                innefattar: 'att försäkra sig om fysisk bekvämlighet, att ta hand om diet och kondition, bibehålla sin egen hälsa'
            }]
        }
});
