# Generiskt utskriftsramverk för intyg

I webcert 6.1 införs ett nytt utskriftsramverk som givet följande:

* UV-ramverksfil som bekriver intygets komponenter
* En JSON-representation av intyget
* Texter
* samt metadata

Kan generera en PDF-utskrift mha iText 7 enligt vad UV-filen stipulerar.

Ramverket ersätter inte existerande AcroForms för FK7263/TS-bas/TS-diabetes eller iText5-ritade PDF:er för SMI. 

Används för:

* AF00213
* ....

### UV-ramverksfilen
Detta är alltså den Javascript-representation av hur ett intyg skall renderas i webbsida som inkluderas i t.ex. 

    /common/af/af00213/src/main/resources/META-INF/resources/webjars/af00213/app-shared/af00213ViewConfig.factory.js
    
Ovanstående fil skall för ett nytt intyg kopieras till /src/main/resources och döpas enligt namnkonventionen:

    [intygskod]-uv-viewmodel.js
    
Efter kopieringen SKALL den endast innehålla själva js-objektet med viewConfig, dvs:

**FÖRE:**

    angular.module('af00213').factory('af00213.viewConfigFactory', ['uvUtil', function (uvUtil) {
        'use strict';
    
        var viewConfig = [{
            type: 'uv-kategori',
            labelKey: 'KAT_1.RBK',
            components: [{
                type: 'uv-fraga',
             ....
             ....
         }, {
                 type: 'uv-skapad-av',
                 modelProp: 'grundData.skapadAv'
             }];
         
             return {
                 getViewConfig: function (webcert) {
                     var config = angular.copy(viewConfig);
         
                     if (webcert) {
                         config = uvUtil.convertToWebcert(config, true);
                     }
         
                     return config;
                 }
             };
         }]);
    

**EFTER:**

    var viewConfig = [{
        type: 'uv-kategori',
        labelKey: 'KAT_1.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_1.RBK',
            components: [{
        ...
        ...
          ]
        }, {
            type: 'uv-skapad-av',
            modelProp: 'grundData.skapadAv'
        }];

Orsaken till detta är att vår rendering sker mha Nashorn som inte kan läsa in Angularjs, därav tvättar vi bort angular-delarna och har enbart kvar "viewConfig"-objektet.

**VIKTIGT: Man skall ej under några som helst omständigheter ändra i viewConfig för PDF-utskriften jämfört med Webb-förlagan.**

### Komponenttyper

Om UV-ramverket utökas med nya komponenttyper (för webb) så behöver PDF-ramverket i så fall uppdateras. Se _/Users/eriklupander/intyg/common/pdf/src/main/java/se/inera/intyg/common/pdf/model_ för existerande komponentrenderare:

- UVAlertValue
- UVBooleanValue
- UVComponent (basklass)
- UVDelfraga
- UVFraga
- UVKategori
- UVKodverkValue
- UVList
- UVSimpleValue
- UVSkapadAv
- UVTable
- UVTillaggsFragor

Framförallt komponenterna för UVList och UVTable är väldigt komplexa för att kunna hantera olika varianter av hur värden plockas ut ur modell och i vissa fall slås upp mot IntygTexts.

### Show/Hide expressions
UV-ramverket stödjer både sträng- och funktionsbaserade predikat för huruvida en komponent skall visas eller ej, s.k. _showExpression_ respektive _hideExpression_.

PDF-ramverket har också stöd för detta (via Nashorn), men kan behöva mer handpåläggning ifall mer komplicerade predikat eller funktioner används.

### Kompabilitets-script för JS
Vissa intyg som använder UV-ramverket använder functions även i renderingen av t.ex. tabellceller. I några fall har då Angular-funktioner använts som t.ex _$filter_. Då vi inte kan läsa in AngularJS i Nashorn så behöver vi då implementera egna varianter av sådana funktioner på $ namespacet.

Detta sker i _customfilter.js_:

    // Deklarerar funktion som har samma namespace+namn som den riktiga funktionen i angularjs.
    $filter = function(type) {
    
        // Implementera hur man vill, de viktiga är att output från funktionen är lämplig för ändamålet.
        if (type === 'number') {
            return function(number, fractionSize) {
    
                // if null or undefined pass it through
                return (number == null)
                    ? number
                    : number;
            };
        }
    
        if (type === 'uvBoolFilter') {
            return function(value) {
                return value == null || typeof value === 'undefined' ||value === '' || value === 'false' || value === false
                    ? 'Nej' : 'Ja'
            };
        }
    
        // default.
        return function(value) {
            return value;
        }
    }

När detta skrivs finns endast ovanstående $filter-funktion definierad. Om fler custom-funktioner används så får man utöka _customfilter.js_ med dessa. (Samt överväga att byta namn på js-filen)

### PdfGenerator.java

Ovanstående klass skall skapas för varje intygstyp som använder detta utskriftsramverk. Det är den komponent som binder samman vårt ModuleApi med PDF-ramverket. I denna klass specificeras intygsspecifik information som parameteriseras in i utskriftsmotorn.
 
Följande skall finnas:

- **intygJsonModel**: Intygets JSON-representation. OBS! Denna måste vara tvättad från radbrytningar, annars får Nashorn spader.
- **upJsModel**: Intygets UV-modell (var viewConfig) uppläst från classpath 
- **personnummer**: Patients person- eller samordningsnummer.
- **utfardarLogotyp**: Logotypen laddad från fil till byte-array.
- **intygsNamn**: Intygets namn, typiskt sett hämtat från ModuleEntryPoint.MODULE_NAME.
- **intygsKod**: Intygets kod (intygstyp), typiskt sett hämtat från ModuleEntryPoint.ISSUER_TYPE_ID
- **infoText**: Texten som hamnar i "röda rutan". Dynamisk utifrån om intyget skickats eller ej etc.
- **intygsId**: Intygs-id (UUID), skrivs ut stående i högerkanten.
- **leftMarginTypText**: Texten som skrivs ut stående i vänsterkanten. Typiskt sett "[kod] - Fastställd av [intygsutfärdare]"
- **summaryHeader**: Rubrik på eventuellt infoblad (sista sidan) 
- **summaryText**: Text på eventuellt infoblad (sista sidan). Bör vara hämtad från IntygTexts och nyckel FRM_1.RBK
- **isUtkast**: True om utskriften rör är ett utkast (dvs ej signerat)
- **isLockedUtkast**: True om utskriften rör ett **låst** utkast.
- **isMakulerad**: True of utskriften rör ett **makulerat** intyg.
- **applicationOrigin**: Anger vilken applikation som försöker skriva ut, typiskt WEBCERT eller MINA_INTYG.



### Checklist nytt intyg:

1. Kopiera över UV-ramverksfilens "var viewConfig" till egen fil i intygets _/src/main/resources_ enligt namngivningskonventionen.
2. Kopiera in utfärdarens logotyp (.png) in i _/src/main/resources_
3. Skapa en PdfGenerator.java i intyget på motsv _se.inera.intyg.common.[intygskod].pdf_ enligt ovan.
4. Länka in ModuleApi-implementationen till din nya PdfGenerator.
5. Kontrollera om UV-ramverksfilen använder några custom-funktioner eller liknande från angular, implementera isf dessa i _customfilter.js_.
6. För tester, skapa med fördel en mapp under /common/pdf/src/test/resources/[intygskod] och kopiera sedan dit filer enligt samma mönster som existerande tester. Lägg därefter till testfall i UVRendererTest.java eller annan testklass.
