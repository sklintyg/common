<?xml version="1.0" encoding="utf-8"?>
<iso:schema
      xmlns="http://purl.oclc.org/dsdl/schematron"
      xmlns:iso="http://purl.oclc.org/dsdl/schematron"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://purl.oclc.org/dsdl/schematron"
      queryBinding='xslt2'
      schemaVersion='ISO19757-3'>

  <iso:title>Schematron file for "Läkarens anmälan"</iso:title>

  <iso:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
  <iso:ns prefix="rg" uri="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3"/>
  <iso:ns prefix="gn" uri="urn:riv:clinicalprocess:healthcond:certificate:3"/>
  <iso:ns prefix="tp" uri="urn:riv:clinicalprocess:healthcond:certificate:types:3"/>

  <iso:pattern id="intyg">
    <iso:rule context="//rg:intyg">
      <iso:assert test="count(gn:svar[@id='1']) ge 1 and count(gn:svar[@id='1']) le 16">
        Ett intyg måste ha ett, och som mest 16 'Behörigheter som avses'
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='2']) = 1">
        Ett 'TSTRK1009' måste ha en 'Identitet styrkt genom'.
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='46']) = 1">
        Ett 'TSTRK1009' måste ha ett 'Anmälan avser olämplighet eller sannolik olämplighet'.
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='47']) = 1">
        Ett 'TSTRK1009' måste ha ett 'Medicinska förhållanden som utgör grund för olämpligheten'.
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='48']) = 1">
        Ett 'TSTRK1009' måste ha ett 'Senaste undersökningsdatum'
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='49']) le 1">
        Ett 'TSTRK1009' får ha max ett 'Information om TS beslut önskas'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1">
    <iso:rule context="//gn:svar[@id='1']">
      <iso:assert test="count(gn:instans) = 1">
        'Behörigheter som avses' måste ha ett instansnummer.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='1.1']) = 1">
        Ett 'Behörigheter som avses' måste ha ett delsvar
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
      <iso:assert test="tp:cv/tp:codeSystem = 'e889fa20-1dee-4f79-8b37-03853e75a9f8'">'codeSystem' måste vara 'e889fa20-1dee-4f79-8b37-03853e75a9f8'.</iso:assert>

      <iso:assert test="matches(normalize-space(tp:cv/tp:code), $varExpr)">
        'Behörigheter som avses' kan ha ett av värdena "<value-of select="$varExpr"/>'
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q2">
    <iso:rule context="//gn:svar[@id='2']">
      <iso:assert test="count(gn:delsvar[@id='2.1']) = 1">
        Ett 'Identitet styrkt genom' måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^2\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q2.1">
    <iso:rule context="//gn:delsvar[@id='2.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'e7cc8f30-a353-4c42-b17a-a189b6876647'">'codeSystem' måste vara 'e7cc8f30-a353-4c42-b17a-a189b6876647'.</iso:assert>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^(IDK[1-6])$')">
        'Identitet styrkt genom' kan ha ett av värdena IDK1-IDK6
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q46">
    <iso:rule context="//gn:svar[@id='46']">
      <iso:assert test="count(gn:delsvar[@id='46.1']) = 1">
        Ett 'Anmälan avser olämplighet eller sannolik olämplighet' måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^46\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q46.1">
    <iso:rule context="//gn:delsvar[@id='46.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = '1.2.752.129.5.1.4'">'codeSystem' måste vara '1.2.752.129.5.1.4'.</iso:assert>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code), 'OLAMPLIGHET|SANNOLIK_OLAMPLIGHET')">
        'Anmälan avser olämplighet eller sannolik olämplighet' kan ha ett av värdena OLAMPLIGHET eller SANNOLIK_OLAMPLIGHET'
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q47">
    <iso:rule context="//gn:svar[@id='47']">
      <iso:assert test="count(gn:delsvar[@id='47.1']) = 1">
        Ett 'Medicinska förhållanden som utgör grund för olämpligheten' måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^47\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q47.1">
    <iso:rule context="//gn:delsvar[@id='47.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q48">
    <iso:rule context="//gn:svar[@id='48']">
      <iso:assert test="count(gn:delsvar[@id='48.1']) = 1">
        Ett 'Senaste undersökningsdatum' måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^48\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q48.1">
    <iso:rule context="//gn:delsvar[@id='48.1']">
      <iso:extends rule="date"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q49">
    <iso:rule context="//gn:svar[@id='49']">
      <iso:assert test="count(gn:delsvar[@id='49.1']) = 1">
        Ett 'Information om TS beslut önskas' måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^49\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q49.1">
    <iso:rule context="//gn:delsvar[@id='49.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="R1-48.1">
    <iso:rule context="//gn:delsvar[@id=48.1]">
      <iso:assert test="xs:date(.) le xs:date(current-date())">
        Datum som anges i delfråga 'Senaste undersökningsdatum (DFR 48.1)' får inte vara senare än dagens datum.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="R8-1.1">
    <iso:rule context="//gn:delsvar[@id='1.1' and matches(normalize-space(tp:cv/tp:code), 'VAR11')]">
      <iso:assert test="count(//gn:svar[@id='1']) = 1">
        Om 'Behörigheter som avses (Delsvar)' besvaras med 'Kan inte ta ställning (VAR11)' ska inga andra svarsalternativ för frågan kunna väljas samtidigt
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="R10-1.1">
    <iso:rule context="//gn:delsvar[@id='1.1' and not(matches(normalize-space(tp:cv/tp:code), 'VAR11'))]">
      <iso:assert test="count(//gn:svar[@id='1']/gn:delsvar[@id='1.1' and matches(normalize-space(./tp:cv/tp:code), 'VAR11')]) = 0">
        Om frågan 'Behörigheter som avses (Delsvar)' besvaras med något annat värde än 'Kan inte ta ställnin (VAR11)' kan värdet 'Kan inte ta ställning (VAR11)' inte också anges
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:let name="varExpr" value="'^VAR([1-9]$|1[1-8]$)'"/>

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
      <iso:assert test="matches(., '^\d{4}-\d{2}-\d{2}')">Datumet måste uttryckas som YYYY-MM-DD.</iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="partial-date-pattern">
    <iso:rule id="partial-date" abstract="true">
      <iso:assert test="matches(tp:partialDate/tp:value, '^\d{4}$')">Datumet måste uttryckas som YYYY.</iso:assert>
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
</iso:schema>
