# Intyg Common
Intyg Common tillhandahåller hjälpprojekt för de övriga intygsprojekten under [SKL Intyg](http://github.com/sklintyg).

## Kom igång
Här hittar du grundläggande instruktioner för hur man kommer igång med projektet. Mer detaljerade instruktioner för att sätta upp sin utvecklingsmiljö och liknande hittar du på projektets [Wiki för utveckling](https://github.com/sklintyg/common/wiki).

### Bygg projektet
Intyg Common innehåller flera olika underprojekt och byggs med hjälp av Gradle enligt följande:
```
$ git clone https://github.com/sklintyg/common.git

$ cd common
$ ./gradlew build install
```

## Kodstruktur i intygsmoduler
Här beskrivs några generella regler och en del förslag på hur koden skall struktureras med avseende på olika majorversioner av intygstyper.
Att studera strukturen på valfritt intyg ger också en bra bild av konventionerna.
### Backendkod
Gemensamma kodpaket för _alla_ versioner av intygstypen ligger direkt under ```se.inera.intyg.common.<intygtyp>```.

Versionsspecifik kod ligger sedan under ```v<majorversion>``` paket tex: 
```
se.inera.intyg.common.af00213
  .model
  .pdf
  .support
  .v1
  .v2
  .validator
```
 
En viss intygsägares alla intygstyper (FK, AF,TS osv) har ofta också gemensam kod och resurser som gäller för intygsägarens _samtliga_ intygstyper dvs oavsett intygstyp och versioner av dessa. 
Den koden läggs i ett eget (parent) projekt
tex ```af-parent```

###Namnkonventioner versionerade intygsfiler
En viss intygsversions ModuleApi-implementation _måste_ ha en namngiven Component annotering enligt mönstret 
  
`"moduleapi-<intygstyp>.v<majorversion>"`

Till exempel

```@Component(value = "moduleapi.af00213.v1")``` 

för att ModuleRegistry skall kunna slå upp rätt implementation.

Som utvecklare kan det bli ganska rörigt att navigera bland kod som har många av samma klass som heter exakt likadant, men som ligger i olika paket. Förslagsvis har tex respektive versions 
```Utlatande``` och ```ModuleApi``` implementationer även egna namn tex ```Af00213UtlatandeV1``` för att i en IDE lättare se vilken implementation man faktist jobbar med.

För att inte behöva deklarara alla ingående beans som en intygstyp använder i en spring xml bör ```src/main/resources/<intygtyp>-beans.xml``` använda component-scan ex:

```<context:component-scan base-package="se.inera.intyg.common.af00213"/>```

Alla Beans måste då annoteras med @Component (lämpligvis även med ett id)

Hur mycket kod som kan delas mellan parent-projekt, intygs-rot-klasser och versionsspecifika klasser beror helt på *hur* och *hur mycket* versionernas
 innehåll skiljer sig åt mellan två major-versioner av ett intyg - är det bara 1 valideringsregel som skiljer, eller är intygen väldigt olika? Det får man 
 lösa från fall till fall.

#### Bootstrapping av exempelintyg
Se namngivningsregler under readme.md under resp intyg /src/main/resources/module-bootstrap-certificate/README.md
 
#### Schematron
Schematronfilnamn pekas ut i resp versions moduleapiimplementations ```getSchematronFileName()```. I Schemas skall filerna ha med version enligt mönstret  ```"af00213.v1.sch"```

#### Generic PDF config file
Läggs i intygsmodulens ```src/main/resources/```  enligt namnkonventionen ```<intygstyp>-uv-viewmodel.v<majorversion>.js``` 

exempelvis: 
```
src/main/resources/af00213-uv-viewmodel.v1.js
src/main/resources/af00213-uv-viewmodel.v2.js
```
### Frontendkod
Versionsspecifika resurser slås upp dynamiskt i intygens routing (på likande sätt som i Spring) men mha 
common/web/src/main/resources/META-INF/resources/webjars/common/app-shared/factoryResolverHelper.service.js
De versionsspecifika komponenterna skall för konsekvensens skull följa namngivningskonventionen (både filnamn och komponentens namespace):
```<intygstyp>.<komponentnamn>.v<majorversion>```
exempelvis 
```.../webcert/view/utkast/af00213UtkastConfig.v1.factory.js``` som innehåller komponenten ```angular.module('af00213').factory('af00213.UtkastConfigFactory.v1', ...```

I  den mån det är vettigt att dela parentklasser/basconfig mellan version följer man lämpligtvis paket/directory strukturreglerna som finns för backend ovan.

## Licens
Copyright (C) 2014 Inera AB (http://www.inera.se)

Intyg Common is free software: you can redistribute it and/or modify it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

Intyg Common is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU LESSER GENERAL PUBLIC LICENSE for more details.

Se även [LICENSE.md](https://github.com/sklintyg/common/blob/master/LICENSE.md). 

-----
