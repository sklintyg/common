<?xml version="1.0" encoding="utf-8"?>
<iso:schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://purl.oclc.org/dsdl/schematron"
            queryBinding='xslt2' schemaVersion='ISO19757-3'>

  <iso:title>Schematron file for "Läkarintyg om arbetsförmåga – sjuklöneperiod AG1-14".</iso:title>

  <iso:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
  <iso:ns prefix="rg" uri="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3"/>
  <iso:ns prefix="gn" uri="urn:riv:clinicalprocess:healthcond:certificate:3"/>
  <iso:ns prefix="tp" uri="urn:riv:clinicalprocess:healthcond:certificate:types:3"/>

  <iso:pattern id="intyg">
    <iso:rule context="//rg:intyg">
      <iso:assert test="count(gn:svar[@id='1']) = 1">
        Ett 'AG1-14' måste innehålla 1 'Sysselsättning'.
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='2']) = 1">
        Ett 'AG1-14' måste innehålla 1 'Nuvarande arbete'.
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='3']) = 1">
        Ett 'AG1-14' måste innehålla 1 'Önskar förmedla diagnoser'.
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='4']) le 3">
        Ett 'AG1-14' får innehålla max 3 'Diagnos'.
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='5']) = 1">
        Ett 'AG1-14' måste innehålla 1 'På vilket sätt nedsatt arbetsförmåga'.
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='6']) = 1">
        Ett 'AG1-14' måste innehålla 1 'Arbetsförmåga trots sjukdom'.
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='7']) = 1">
        Ett 'AG1-14' måste innehålla 1 'Behov av sjukskrivning'.
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='8']) le 1">
        Ett 'AG1-14' får innehålla max 1 'Övrig'.
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='9']) le 1">
        Ett 'AG1-14' får innehålla max 1 'Kontakt med arbetsgivaren'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1">
    <iso:rule context="//gn:svar[@id='1']">
      <iso:assert test="count(gn:delsvar[@id='1.1']) = 1">
        'Sysselsättning' måste ha ett 'Typ av sysselsättning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^1\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.1">
    <iso:rule context="//gn:delsvar[@id='1.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'KV_FKMU_0002'">'codeSystem' måste vara 'KV_FKMU_0002'.</iso:assert>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^(NUVARANDE_ARBETE)$')">
        'Typ av sysselsättning' måste ha värdet NUVARANDE_ARBETE.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q2">
    <iso:rule context="//gn:svar[@id='2']">
      <iso:assert test="count(gn:delsvar[@id='2.1']) = 1">
        'Nuvarande arbete' måste ha ett 'Yrke och arbetsuppgifter'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^2\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q2.1">
    <iso:rule context="//gn:delsvar[@id='2.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q3">
    <iso:rule context="//gn:svar[@id='3']">
      <iso:assert test="count(gn:delsvar[@id='3.1']) = 1">
        'Sysselsättning' måste ha ett 'Typ av sysselsättning'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^3\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q3.1">
    <iso:rule context="//gn:delsvar[@id='3.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q4-R14">
    <iso:rule context="//gn:svar[@id='3']/gn:delsvar[@id='3.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../../gn:svar[@id='4']) ge 1">
        Om 'Önskar förmedla diagnos (FRG 3.1)' besvaras med 'Ja' är frågan 'Typ av diagnos (FRG 4)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q4.instans">
    <iso:rule context="//gn:svar[@id='4']">
      <iso:assert test="count(gn:instans) = 1">
        'Diagnos' måste ha ett instansnummer.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q4.1">
    <iso:rule context="//gn:svar[@id='4']/gn:delsvar[@id='4.1']">
      <iso:extends rule="non-empty-string"/>
      <iso:assert test="count(../gn:delsvar[@id='4.2']) = 1">
        Om 'Diagnostext (FRG 4.1)' besvarats är frågan 'Diagnoskod (FRG 4.2)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q4.2">
    <iso:rule context="//gn:svar[@id='4']/gn:delsvar[@id='4.2']">
      <iso:assert test="count(../gn:delsvar[@id='4.1']) = 1">
        Om 'Diagnoskod (FRG 4.2)' besvarats är frågan 'Diagnostext (FRG 4.1)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q4.2-R3-R4">
    <iso:rule context="//gn:delsvar[@id='4.2']"> 
      <iso:extends rule="cv"/>
      <iso:assert test="matches(normalize-space(tp:cv/tp:codeSystem), '(1.2.752.116.1.1.1.1.3|1.2.752.116.1.3.1.4.1)')">
        Diagnoskodsystem måste vara OID för ICD-10-SE eller KSH97-P.
      </iso:assert>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code),'^([A-EG-Ya-eg-y][0-9]{2}[A-Za-z0-9-]*|[Zz][0-689][0-9][A-Za-z0-9-]*|[Zz]7[0-24-9][A-Za-z0-9-]*|[Zz]73[A-Za-z0-9-]+|[Ff][0-9]{2}[A-Za-z0-9-]+)$')">
        Diagnoskod måste anges som bokstav följt av två siffror följt av noll eller flera bokstäver, siffror eller bindestreck, d.v.s. minst tre positioner
        måste anges. Om diagnoskoden börjar med F eller Z73 måste bokstav och två siffor följas av minst en bokstav, siffra eller bindestreck, d.v.s. minst fyra
        positioner måste anges.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q5">
    <iso:rule context="//gn:svar[@id='5']">
      <iso:assert test="count(gn:delsvar[@id='5.1']) = 1">
        'På vilket sätt nedsatt arbetsförmåga' måste ha ett 'På vilket sätt nedsatt arbetsförmåga (DFR 5.1)'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^5\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q5.1">
    <iso:rule context="//gn:delsvar[@id='5.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6">
    <iso:rule context="//gn:svar[@id='6']">
      <iso:assert test="count(gn:delsvar[@id='6.1']) = 1">
        'Arbetsförmåga trots sjukdom' måste ha ett 'Finns arbetsförmåga trots sjukdom'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^6\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6.1">
    <iso:rule context="//gn:delsvar[@id='6.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6.2-R1">
    <iso:rule context="//gn:delsvar[@id='6.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='6.2']) = 1">
        Om 'Finns arbetsförmåga trots sjukdom' besvarats med 'Ja' måste 'Beskriv arbetsförmåga trots sjukdom' besvaras.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6.2">
    <iso:rule context="//gn:delsvar[@id='6.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q7">
    <iso:rule context="//gn:svar[@id='7']">
      <iso:assert test="count(gn:delsvar[@id='7.1']) = 1">
        'Behov av sjukskrivning' måste ha ett 'Sjukskrivningsgrad'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='7.2']) = 1">
        'Behov av sjukskrivning' måste ha ett 'Sjukskrivningsperiod'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^7\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q7.1">
    <iso:rule context="//gn:delsvar[@id='7.1']">
      <iso:assert test="matches(normalize-space(.), '^0$|^[1-9]$|^[1-9][0-9]$|^100$')">
        'Sjukskrivningsgrad' måste besvaras med ett värde mellan 0 och 100%.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q7.2">
    <iso:rule context="//gn:delsvar[@id='7.2']">
      <iso:extends rule="period"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8">
    <iso:rule context="//gn:svar[@id='8']">
      <iso:assert test="count(gn:delsvar[@id='8.1']) = 1">
        'Övrig' måste ha ett 'Övriga upplysningar till arbetsgivaren'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^8\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8.1">
    <iso:rule context="//gn:delsvar[@id='8.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q9">
    <iso:rule context="//gn:svar[@id='9']">
      <iso:assert test="count(gn:delsvar[@id='9.1']) = 1">
        'Kontakt med arbetsgivaren' måste ha ett 'Kontakt med arbetsgivaren önskas'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='9.2']) le 1">
        'Kontakt med arbetsgivaren' får ha högst ett 'Kontakt vem och varför'.
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^9\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q9.1">
    <iso:rule context="//gn:delsvar[@id='9.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q9.2">
    <iso:rule context="//gn:delsvar[@id='9.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q9.2-R2">
    <iso:rule context="//gn:delsvar[@id='9.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='9.2']) = 1">
        Om 'Kontakt med arbetsgivaren önskas' besvarats med 'Ja' måste 'Kontakt vem och varför' besvaras.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="non-empty-string-pattern">
    <iso:rule id="non-empty-string" abstract="true">
      <iso:assert test="count(*) = 0">Värdet får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test="string-length(normalize-space(text())) > 0">Sträng kan inte vara tom.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="boolean-pattern">
    <iso:rule id="boolean" abstract="true">
      <iso:assert test="count(*) = 0">Booleskt värde får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test=". castable as xs:boolean">Kan bara vara 'true/1' eller 'false/0'</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="cv-pattern">
    <iso:rule id="cv" abstract="true">
      <iso:assert test="count(tp:cv) = 1">Ett värde av typen CV måste ha ett cv-element</iso:assert>
      <iso:assert test="count(tp:cv/tp:codeSystem) = 1">codeSystem är obligatoriskt</iso:assert>
      <iso:assert test="tp:cv/tp:codeSystem/count(*) = 0">'codeSystem' får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test="count(tp:cv/tp:code) = 1">code är obligatoriskt</iso:assert>
      <iso:assert test="tp:cv/tp:code/count(*) = 0">'code' får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test="count(tp:cv/tp:displayName) le 1">högst ett displayName kan anges</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="date-pattern">
    <iso:rule id="date" abstract="true">
      <iso:assert test="count(*) = 0">Datum får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test=". castable as xs:date">Värdet måste vara ett giltigt datum.</iso:assert>
      <iso:assert test="matches(., '^\d{4}-\d\d-\d\d')">Datumet måste uttryckas som YYYY-MM-DD.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="period-pattern">
    <iso:rule id="period" abstract="true">
      <iso:assert test="tp:datePeriod">En period måste inneslutas av ett 'datePeriod'-element</iso:assert>
      <iso:assert test="tp:datePeriod/tp:start/count(*) = 0">'from' får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test="tp:datePeriod/tp:start castable as xs:date">'from' måste vara ett giltigt datum.</iso:assert>
      <iso:assert test="matches(tp:datePeriod/tp:start, '^\d{4}-\d\d-\d\d')">'from' måste uttryckas som YYYY-MM-DD.</iso:assert>
      <iso:assert test="tp:datePeriod/tp:end/count(*) = 0">'tom' får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test="tp:datePeriod/tp:end castable as xs:date">'tom' måste vara ett giltigt datum.</iso:assert>
      <iso:assert test="matches(tp:datePeriod/tp:end, '^\d{4}-\d\d-\d\d')">'end' måste uttryckas som YYYY-MM-DD.</iso:assert>
      <iso:assert test="normalize-space(tp:datePeriod/tp:start) le normalize-space(tp:datePeriod/tp:end)">
        'from' måste vara mindre än eller lika med 'to'
      </iso:assert>
    </iso:rule>
  </iso:pattern>

</iso:schema>
