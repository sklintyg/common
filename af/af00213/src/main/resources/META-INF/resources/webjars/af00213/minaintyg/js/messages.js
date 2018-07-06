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
angular.module('af00213').constant('af00213.messages', {
    'sv': {
        // Composite keys - used with dynamic prefix such as <intygstype> or suffixes <true/false> etc.
        // Be careful to investigate if they are used before removing them.

        'af00213.label.pageingress': 'Här visas hela ditt läkarintyg. Från den här sidan kan du skicka intyget, göra en anpassad version till din arbetsgivare, ladda ner intyget som PDF och arkivera intyget. Om du vill ansöka om sjukpenning, gör du det enklast på <LINK:forsakringskassan-sjuk>. För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.',
        'af00213.label.pageingress.ersatt': 'Här visas hela ditt ersatta läkarintyg. Klicka på länken i den gula inforutan för att komma till det nya intyget som detta blev ersatt med. Klicka på knappen Arkivera intyg för att flytta intyget till Arkiverade intyg. Du kan när som helst återställa intyget igen.',

        // Anpassat intyg
        'af00213.customize.step.1.pagetitle': 'Anpassa intygsinformation till din arbetsgivare - steg 1 av 3',
        'af00213.customize.step.1.pageingress.left': 'Här kan du skapa ett anpassat intyg till din arbetsgivare genom att välja vilken information du vill ta med och inte. Viss information måste lämnas till arbetsgivaren och kan inte väljas bort. I de flesta fall är du inte skyldig att lämna mer information än den obligatoriska, men om ni har ett kollektivavtal kan det finnas andra regler för vilken information som måste tas med.<br><br>',
        'af00213.customize.step.1.pageingress.right': ' Det finns ofta fördelar med att arbetsgivaren får ta del även av frivillig information. Det ger arbetsgivaren bättre möjlighet att anpassa din arbetsplats eller arbetssituation. Du kan alltid återvända till Mina intyg vid senare tillfälle för att skapa ett nytt anpassat intyg med mer information.<br>För att välja bort information bockar du ur alternativet <i>Inkludera i anpassat intyg</i> i det fält du inte vill ska synas.',
        'af00213.customize.step.1.next': 'Gå till steg 2: Granska dina val',

        'af00213.customize.step.2.pagetitle': 'Anpassa intygsinformation till din arbetsgivare - steg 2 av 3',
        'af00213.customize.step.2.pageingress.left': 'Var god kontrollera dina val. Informationen som visas nedan är den information ditt intyg till din arbetsgivare kommer att innehålla. Bekräfta sedan dina val.<br><br>',
        'af00213.customize.step.2.pageingress.right': 'Du kan alltid återvända till Mina intyg vid ett senare tillfälle för att skapa ett nytt anpassat intyg.',
        'af00213.customize.step.2.next': 'Gå till steg 3: Ladda ner PDF',

        'af00213.customize.step.3.pagetitle': 'Ladda ner PDF - steg 3 av 3',
        'af00213.customize.step.3.pageingress.left': 'Här kan du ladda ner ditt anpassade intyg som en PDF, för att sedan kunna skriva ut det eller mejla till din arbetsgivare. ',
        'af00213.customize.step.3.pageingress.right': '',
        'af00213.customize.step.3.info.fk': 'Intyget <u>ska inte skickas</u> till Försäkringskassan!',
        'af00213.customize.step.3.download': 'Ladda ner som PDF',
        'af00213.customize.step.3.download.tooltip': 'Intyget sparas som PDF på din enhet.',
        'af00213.customize.change': 'Ändra dina val',


        // Labels
        'af00213.label.yes': 'Ja',
        'af00213.label.no': 'Nej',

        'af00213.button.cancel': 'Avbryt',

        // Composite keys - used with dynamic prefix such as <intygstype>.
        // Be careful to investigate if they are used before removing them.
        'af00213.inbox.complementaryinfo': 'Intygsperiod',
        'af00213.compact-header.complementaryinfo-prefix': 'Gäller intygsperiod:'
    },
    'en': {
        'af00213.label.pagetitle': 'Show Certificate'
    }
});
