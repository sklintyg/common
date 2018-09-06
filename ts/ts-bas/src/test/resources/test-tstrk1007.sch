<?xml version="1.0" encoding="utf-8"?>
<iso:schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://purl.oclc.org/dsdl/schematron"
    queryBinding='xslt2' schemaVersion='ISO19757-3'>

  <iso:title>Schematron file for "Arbetsförmedlingens medicinska utlåtande".</iso:title>

  <iso:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
  <iso:ns prefix="rg" uri="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3"/>
  <iso:ns prefix="gn" uri="urn:riv:clinicalprocess:healthcond:certificate:3"/>
  <iso:ns prefix="tp" uri="urn:riv:clinicalprocess:healthcond:certificate:types:3"/>

  <iso:pattern id="intyg">
    <iso:rule context="//rg:intyg">
      <iso:assert test="count(gn:svar[@id='1']) ge 1 and count(gn:svar[@id='1']) le 10">
        Ett intyg måste ha ett Intyget avser behörighet (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='2']) eq 1">
        Ett intyg måste ha ett Identitet styrkt genom (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='3']) eq 1">
        Ett intyg måste ha ett Tecken på synfältsdefekter vid undersökning enligt Donder (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='4']) eq 1">
        Ett intyg måste ha ett Begränsning av seende vid nedsatt belysning (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='5']) eq 1">
        Ett intyg måste ha ett Progressiv ögonsjukdom (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='6']) eq 1">
        Ett intyg måste ha ett Dubbelseende (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='7']) eq 1">
        Ett intyg måste ha ett Nystagmus (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='8']) eq 1">
        Ett intyg måste ha ett Synskärpa (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='10']) eq 1">
        Ett intyg måste ha ett Överraskande anfall av balansrubbningar eller yrsel (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='12']) eq 1">
        Ett intyg måste ha ett Sjukdom eller funktionsnedsättning som påverkar rörligheten och som medför att fordon inte kan köras på ett trafiksäkert sätt (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='14']) eq 1">
        Ett intyg måste ha ett Hjärt- eller kärlsjukdom som kan medföra en påtaglig risk för att hjärnans funktioner akut försämras eller som i övrigt innebär en trafiksäkerhetsrisk (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='15']) eq 1">
        Ett intyg måste ha ett Tecken på hjärnskada efter trauma, stroke eller annan sjukdom i centrala nervsystemet (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='16']) eq 1">
        Ett intyg måste ha ett Riskfaktorer för stroke (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='17']) eq 1">
        Ett intyg måste ha ett Har diabetes (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='20']) eq 1">
        Ett intyg måste ha ett Tecken på neurologisk sjukdom (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='21']) eq 1">
        Ett intyg måste ha ett Epelepsi, epeleptiskt anfall eller annan medvetandestörning (svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='22']) eq 1">
        Ett intyg måste ha ett Nedsatt njurfunktion som kan innebära en trafiksäkerhetsrisk (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='23']) eq 1">
        Ett intyg måste ha ett Tecken på sviktande kognitiv funktion (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='24']) eq 1">
        Ett intyg måste ha ett Tecken på sömn- eller vakenhetsstörning (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='25']) eq 1">
        Ett intyg måste ha ett Missbruk eller beroende av alkohol, narkotika eller läkemedel (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='26']) eq 1">
        Ett intyg måste ha ett Regelbundet läkarordinerat bruk av läkemedel som kan innebära trafiksäkerhetsrisk (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='27']) eq 1">
        Ett intyg måste ha ett Psykisk sjukdom eller störning (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='28']) eq 1">
        Ett intyg måste ha ett Psykisk utvecklingsstörning (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='29']) eq 1">
        Ett intyg måste ha ett ADHD, ADD, DAMP, Aspergers syndrom eller Tourettes syndrom (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='30']) eq 1">
        Ett intyg måste ha ett Vård på sjukhus eller kontakt med läkare (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='31']) eq 1">
        Ett intyg måste ha ett Stadigvarande medicinering (Svar)
      </iso:assert>

      <iso:assert test="count(gn:svar[@id='33']) ge 1 and count(gn:svar[@id='33']) le 10">
        Ett intyg måste ha ett Uppfyller krav för behörighet (Svar)
      </iso:assert>

      <iso:let name="svarsIdExpr" value="'^[1-9]$|1[0-9]$|2[0-9]$|3[0-4]$'"/>
      <iso:assert test="count(gn:svar[not(matches(@id, $svarsIdExpr))]) = 0">
        Oväntat svars-id. Svars-id:n måste matcha "<value-of select="$svarsIdExpr"/>".
      </iso:assert>

      <!-- Meddelandet kan innehålla ett valfritt antal tilläggsfrågor -->
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1">
    <iso:rule context="//gn:svar[@id='1']">
      <iso:assert test="count(gn:delsvar[@id='1.1']) = 1">
        Ett Intyget avser behörighet (Svar) måste ha ett delsvar
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
      <iso:assert test="tp:cv/tp:codeSystem = '24c41b8d-258a-46bf-a08a-b90738b28770'">'codeSystem' måste vara '24c41b8d-258a-46bf-a08a-b90738b28770'.</iso:assert>

      <iso:assert test="matches(normalize-space(tp:cv/tp:code), $iavExpr)">
        'Intyget avser behörighet' kan ha ett av värdena "<value-of select="$iavExpr"/>'
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

  <iso:pattern id="q3">
    <iso:rule context="//gn:svar[@id='3']">
      <iso:assert test="count(gn:delsvar[@id='3.1']) = 1">
        Ett 'Tecken på synfältsdefekter vid undersökning enligt Donder (svar)' måste ha ett delsvar
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

  <iso:pattern id="q4">
    <iso:rule context="//gn:svar[@id='4']">
      <iso:assert test="count(gn:delsvar[@id='4.1']) = 1">
        Ett 'Begränsning av seende vid nedsatt belysning (svar)' måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^4\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q4.1">
    <iso:rule context="//gn:delsvar[@id='4.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q5">
    <iso:rule context="//gn:svar[@id='5']">
      <iso:assert test="count(gn:delsvar[@id='5.1']) = 1">
        Ett 'Progressiv ögonsjukdom (svar)' måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^5\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q5.1">
    <iso:rule context="//gn:delsvar[@id='5.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6">
    <iso:rule context="//gn:svar[@id='6']">
      <iso:assert test="count(gn:delsvar[@id='6.1']) = 1">
        Ett 'Dubbelseende (svar)' måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^6\.[1]$'"/>
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

  <iso:pattern id="q7">
    <iso:rule context="//gn:svar[@id='7']">
      <iso:assert test="count(gn:delsvar[@id='7.1']) = 1">
        Ett 'Nystagmus (svar)' måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^7\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q7.1">
    <iso:rule context="//gn:delsvar[@id='7.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8">
    <iso:rule context="//gn:svar[@id='8']">
      <iso:assert test="count(gn:delsvar[@id='8.1']) = 1">
        Ett 'Synskärpa (svar)' måste ha ett delsvar '8.1' (höger öga utan korrektion)
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='8.2']) = 1">
        Ett 'Synskärpa (svar)' måste ha ett delsvar '8.2' (vänster öga utan korrektion)
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='8.3']) = 1">
        Ett 'Synskärpa (svar)' måste ha ett delsvar '8.3' (binokulärt utan korrektion)
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^8\.[1-8]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8.1">
    <iso:rule context="//gn:delsvar[@id='8.1']">
      <iso:extends rule="decimal"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8.2">
    <iso:rule context="//gn:delsvar[@id='8.2']">
      <iso:extends rule="decimal"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8.3">
    <iso:rule context="//gn:delsvar[@id='8.3']">
      <iso:extends rule="decimal"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8.4">
    <iso:rule context="//gn:delsvar[@id='8.4']">
      <iso:extends rule="decimal"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8.5">
    <iso:rule context="//gn:delsvar[@id='8.5']">
      <iso:extends rule="decimal"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8.6">
    <iso:rule context="//gn:delsvar[@id='8.6']">
      <iso:extends rule="decimal"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8-R33">
    <iso:rule context="//gn:svar[@id='8']">
      <iso:assert test="(count(../gn:svar[@id='1']/gn:delsvar[@id='1.1']) gt 1)
      or
      (count(../gn:svar[@id='1']/gn:delsvar[@id='1.1']) = 1
      and matches(normalize-space(../gn:svar[@id='1']/gn:delsvar[@id='1.1']/tp:cv/tp:code), 'IAV10')
      and abs(gn:delsvar[@id='8.3']) lt 0.5
      and (count(gn:delsvar[@id='8.4']) = 1 and count(gn:delsvar[@id='8.5']) = 1 and count(gn:delsvar[@id='8.6']) = 1))">
        Om frågan 'Intyget avser behörighet (Delsvar 1.1)' besvarats med 'Annat'
        OCH frågan 'Binokulärt utan korrektion (Delsvar 8.3)' har besvarats med ett värde som är mindre än 0,5
        är (DFR 8.4), (DFR 8.5) och (DFR 8.6) obligatoriska att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q8-R34">
    <iso:rule context="//gn:svar[@id='8']">
      <iso:assert test="(count(../gn:svar[@id='1']/gn:delsvar[@id='1.1']) = 1
      and matches(normalize-space(../gn:svar[@id='1']/gn:delsvar[@id='1.1']/tp:cv/tp:code), 'IAV10'))
      or
      (count(../gn:svar[@id='1']/gn:delsvar[@id='1.1']) gt 1
      and abs(gn:delsvar[@id='8.1']) lt 0.8 and abs(gn:delsvar[@id='8.2']) lt 0.8
      and (count(gn:delsvar[@id='8.4']) = 1 and count(gn:delsvar[@id='8.5']) = 1 and count(gn:delsvar[@id='8.6']) = 1))">
        Om DFR 1.1 besvarats med något av valen 'C1, C1E, C, CE, D1, D1E, D, DE eller Taxi'
        OCH frågan DFR 8.1 har besvarats med ett värde som är mindre än 0,8
        OCH frågan DFR 8.2 har besvarats med ett värde som är mindre än 0,8
        är DFR 8.4, DFR 8.5 och DFR 8.6 obligatoriska att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q9">
    <iso:rule context="//gn:svar[@id='9']">
      <iso:assert test="count(gn:delsvar[@id='9.1']) = 1">
        Undersökning med > +8 dioptriers korrektionsgrad (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^9\.[1]$'"/>
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

  <iso:pattern id="q10">
    <iso:rule context="//gn:svar[@id='10']">
      <iso:assert test="count(gn:delsvar[@id='10.1']) = 1">
        Överraskande anfall av balansrubbningar eller yrsel (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^10\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q10.1">
    <iso:rule context="//gn:delsvar[@id='10.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q11">
    <iso:rule context="//gn:svar[@id='11']">
      <iso:assert test="count(gn:delsvar[@id='11.1']) = 1">
        Svårighet att uppfatta vanlig samtalsstämma på fyra meters avstånd (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^11\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q11.1">
    <iso:rule context="//gn:delsvar[@id='11.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q12">
    <iso:rule context="//gn:svar[@id='12']">
      <iso:assert test="count(gn:delsvar[@id='12.1']) = 1">
        Sjukdom eller funktionsnedsättning som påverkar rörligheten och som medför att fordon inte kan köras på ett trafiksäkert sätt (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^12\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q12.1">
    <iso:rule context="//gn:delsvar[@id='12.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q12.1-12.2-true">
    <iso:rule context="//gn:delsvar[@id='12.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='12.2']) = 1">
        Om är Förekomst av sjukdom eller funktionsnedsättning (Delsvar 12.1) besvaras med 'Ja' måste Typ av sjukdom eller funktionsnedsättning (Delsvar) besvaras
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q13">
    <iso:rule context="//gn:svar[@id='13']">
      <iso:assert test="count(gn:delsvar[@id='13.1']) = 1">
        Otillräcklig rörelseförmåga för att kunna hjälpa passagerare in och ut ur fordonet samt med bilbälte (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^13\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q13.1">
    <iso:rule context="//gn:delsvar[@id='13.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q14">
    <iso:rule context="//gn:svar[@id='14']">
      <iso:assert test="count(gn:delsvar[@id='14.1']) = 1">
        Hjärt- eller kärlsjukdom som kan medföra en påtaglig risk för att hjärnans funktioner akut försämras eller som i övrigt innebär en trafiksäkerhetsrisk (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^14\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q14.1">
    <iso:rule context="//gn:delsvar[@id='14.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q15">
    <iso:rule context="//gn:svar[@id='15']">
      <iso:assert test="count(gn:delsvar[@id='15.1']) = 1">
        Tecken på hjärnskada efter trauma, stroke eller annan sjukdom i centrala nervsystemet (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^15\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q15.1">
    <iso:rule context="//gn:delsvar[@id='15.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q16">
    <iso:rule context="//gn:svar[@id='16']">
      <iso:assert test="count(gn:delsvar[@id='16.1']) = 1">
        Riskfaktorer för stroke (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^16\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q16.1">
    <iso:rule context="//gn:delsvar[@id='16.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q16.1-16.2-true">
    <iso:rule context="//gn:delsvar[@id='16.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='16.2']) = 1">
        Förekomst av riskfaktorer för stroke (Delsvar) besvaras med 'Ja' måste Typ av sjukdom (Delsvar) besvaras
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q17">
    <iso:rule context="//gn:svar[@id='17']">
      <iso:assert test="count(gn:delsvar[@id='17.1']) = 1">
        Har diabetes (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^17\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q17.1">
    <iso:rule context="//gn:delsvar[@id='17.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q17.1-18.1-R10">
    <iso:rule context="//gn:delsvar[@id='17.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../../gn:svar[@id='18']/gn:delsvar[@id='18.1']) = 1">
        Om 'Har diabetes (Delsvar 17.1)' besvarats med 'Ja' är 'Typ av diabetes (Delsvar 18.1 obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q18.1">
    <iso:rule context="//gn:delsvar[@id='18.1']">
      <iso:extends rule="cv"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q18.1-19-R11">
    <iso:rule context="//gn:delsvar[@id='18.1' and matches(normalize-space(.), 'E11')]">
      <iso:assert test="count(../../gn:svar[@id='19']) = 1">
        Om 'Typ av diabetes (Delsvar 18.1)' besvarats med 'Typ 2' ska 'Behandling för diabetes (Svar 19)' vara obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q19">
    <iso:rule context="//gn:svar[@id='19']">
      <iso:let name="delsvarsIdExpr" value="'^19\.[123]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q20">
    <iso:rule context="//gn:svar[@id='20']">
      <iso:assert test="count(gn:delsvar[@id='20.1']) = 1">
        Tecken på neurologisk sjukdom (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^20\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q20.1">
    <iso:rule context="//gn:delsvar[@id='20.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q21">
    <iso:rule context="//gn:svar[@id='21']">
      <iso:assert test="count(gn:delsvar[@id='21.1']) = 1">
        Epelepsi, epeleptiskt anfall eller annan medvetandestörning (svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^21\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q21.1">
    <iso:rule context="//gn:delsvar[@id='21.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q21.1-21.2-R16">
    <iso:rule context="//gn:delsvar[@id='21.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='21.2']) = 1">
        Om 'Förekomst av epelepsi, epeleptiskt anfall eller annan medvetandestörning (Delsvar 21.1)' besvarats med  'Ja' är 'Tidpunkt och orsak för annan medvetandestörning (Delsvar 21.2)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q22">
    <iso:rule context="//gn:svar[@id='22']">
      <iso:assert test="count(gn:delsvar[@id='22.1']) = 1">
        Nedsatt njurfunktion som kan innebära en trafiksäkerhetsrisk (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^22\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q22.1">
    <iso:rule context="//gn:delsvar[@id='22.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q23">
    <iso:rule context="//gn:svar[@id='23']">
      <iso:assert test="count(gn:delsvar[@id='23.1']) = 1">
        Tecken på sviktande kognitiv funktion (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^23\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q23.1">
    <iso:rule context="//gn:delsvar[@id='23.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q24">
    <iso:rule context="//gn:svar[@id='24']">
      <iso:assert test="count(gn:delsvar[@id='24.1']) = 1">
        Tecken på sömn- eller vakenhetsstörning (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^24\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q24.1">
    <iso:rule context="//gn:delsvar[@id='24.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q25">
    <iso:rule context="//gn:svar[@id='25']">
      <iso:assert test="count(gn:delsvar[@id='25.1']) = 1">
        Missbruk eller beroende av alkohol, narkotika eller läkemedel (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^25\.[123]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q25.1">
    <iso:rule context="//gn:delsvar[@id='25.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q25.2">
    <iso:rule context="//gn:delsvar[@id='25.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q25.3">
    <iso:rule context="//gn:delsvar[@id='25.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q25.1-25.2-R17">
    <iso:rule context="//gn:delsvar[@id='25.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='25.3']) = 1">
        Om 'Tecken på missbruk eller beroende av alkohol, narkotika eller läkemedel (Delsvar 25.1)' besvarats med  'Ja' är 'Behövs provtagning avseende aktuellt bruk av alkohol eller narkotika (Delsvar 25.3)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q25.2-25.3-R18">
    <iso:rule context="//gn:delsvar[@id='25.2' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='25.3']) = 1">
        Om 'Vårdinsatser för missbruk eller beroende av alkohol, narkotika eller läkemedel (Delsvar DFR 25.2)' besvarats med 'Ja' är 'Behövs provtagning avseende aktuellt bruk av alkohol eller narkotika (Delsvar DFR 25.3)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q26">
    <iso:rule context="//gn:svar[@id='26']">
      <iso:assert test="count(gn:delsvar[@id='26.1']) = 1">
        Regelbundet läkarordinerat bruk av läkemedel som kan innebära trafiksäkerhetsrisk (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^26\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q26.1">
    <iso:rule context="//gn:delsvar[@id='26.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q26.2">
    <iso:rule context="//gn:delsvar[@id='26.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q26.1-26.2-R20">
    <iso:rule context="//gn:delsvar[@id='26.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='26.2']) = 1">
        Om 'Regelbundet läkarordinerat bruk av läkemedel som kan innebära trafiksäkerhetsrisk (Delsvar 26.1)' besvarats med  'Ja' är 'Läkemedel och ordinerad dos (Delsvar 26.2)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q27">
    <iso:rule context="//gn:svar[@id='27']">
      <iso:assert test="count(gn:delsvar[@id='27.1']) = 1">
        Psykisk sjukdom eller störning (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^27\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q27.1">
    <iso:rule context="//gn:delsvar[@id='27.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q28">
    <iso:rule context="//gn:svar[@id='28']">
      <iso:assert test="count(gn:delsvar[@id='28.1']) = 1">
        Psykisk utvecklingsstörning (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^28\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q28.1">
    <iso:rule context="//gn:delsvar[@id='28.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q29">
    <iso:rule context="//gn:svar[@id='29']">
      <iso:assert test="count(gn:delsvar[@id='29.1']) = 1">
        ADHD, ADD, DAMP, Aspergers syndrom eller Tourettes syndrom (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^29\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q29.1">
    <iso:rule context="//gn:delsvar[@id='29.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30">
    <iso:rule context="//gn:svar[@id='30']">
      <iso:assert test="count(gn:delsvar[@id='30.1']) = 1">
        Vård på sjukhus eller kontakt med läkare (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^30\.[1-4]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30.1">
    <iso:rule context="//gn:delsvar[@id='30.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30.2">
    <iso:rule context="//gn:delsvar[@id='30.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30.3">
    <iso:rule context="//gn:delsvar[@id='30.3']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30.4">
    <iso:rule context="//gn:delsvar[@id='30.4']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30.1-30.2-R21">
    <iso:rule context="//gn:delsvar[@id='30.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='30.2']) = 1">
        Om 'Förekomst av vård på sjukhus eller kontakt med läkare (Delsvar 30.1)' besvarats med  'Ja' är 'Tidpunkt för vård på sjukhus eller kontakt med läkare (Delsvar 30.2)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30.1-30.3-R22">
    <iso:rule context="//gn:delsvar[@id='30.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='30.3']) = 1">
        Om 'Förekomst av vård på sjukhus eller kontakt med läkare (Delsvar 30.1)' besvarats med  'Ja' är 'Plats för vård på sjukhus eller kontakt med läkare (Delsvar 30.3)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30.1-30.4-R23">
    <iso:rule context="//gn:delsvar[@id='30.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='30.4']) = 1">
        Om 'Förekomst av vård på sjukhus eller kontakt med läkare (Delsvar 30.1)' besvarats med  'Ja' är 'Orsak för vård på sjukhus eller kontakt med läkare (Delsvar 30.4)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q31">
    <iso:rule context="//gn:svar[@id='31']">
      <iso:assert test="count(gn:delsvar[@id='31.1']) = 1">
        Vård på sjukhus eller kontakt med läkare (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^31\.[12]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q31.1">
    <iso:rule context="//gn:delsvar[@id='31.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q31.2">
    <iso:rule context="//gn:delsvar[@id='31.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q31.1-31.2-R24">
    <iso:rule context="//gn:delsvar[@id='31.1' and matches(normalize-space(.), 'true|1')]">
      <iso:assert test="count(../gn:delsvar[@id='31.2']) = 1">
        Om 'Förekomst av stadigvarande medicinering (Delsvar 31.1)' besvarats med  'Ja' är 'Mediciner (Delsvar 31.2)' obligatorisk att besvara.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q32.1">
    <iso:rule context="//gn:delsvar[@id='32.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q33.1">
    <iso:rule context="//gn:delsvar[@id='33.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'e889fa20-1dee-4f79-8b37-03853e75a9f8'">'codeSystem' måste vara 'e889fa20-1dee-4f79-8b37-03853e75a9f8'.</iso:assert>

      <iso:assert test="matches(normalize-space(tp:cv/tp:code), $varExpr)">
        'Uppfyller krav för behörighet ' kan ha ett av värdena "<value-of select="$varExpr"/>'
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q34">
    <iso:rule context="//gn:svar[@id='34']">
      <iso:assert test="count(gn:delsvar[@id='34.1']) = 1">
        Bör undersökas av specialistläkare (Svar) måste ha ett delsvar
      </iso:assert>
      <iso:let name="delsvarsIdExpr" value="'^34\.[1]$'"/>
      <iso:assert test="count(gn:delsvar[not(matches(@id, $delsvarsIdExpr))]) = 0">
        Oväntat delsvars-id i delsvar till svar "<value-of select="@id"/>". Delsvars-id:n måste matcha "<value-of select="$delsvarsIdExpr"/>".
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q34.1">
    <iso:rule context="//gn:delsvar[@id='34.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>


  <!-- Generella regler -->
  <iso:pattern id="instans-pattern">
    <iso:rule context="//gn:instans">
      <iso:assert test="number(.) ge 1">
        'Instans' måste vara större än 0.
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

  <iso:pattern id="decimal-pattern">
    <iso:rule id="decimal" abstract="true">
      <iso:assert test="count(*) = 0">Decimal-värde får inte vara inbäddat i något element.</iso:assert>
      <iso:assert test=". castable as xs:decimal">Decimalvärde</iso:assert>
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

  <iso:let name="iavExpr" value="'^IAV([1-9]$|10$)'"/>
  <iso:let name="varExpr" value="'^VAR([1-9]$|1[01]$)'"/>

</iso:schema>
