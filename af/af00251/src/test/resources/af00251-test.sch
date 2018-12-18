<?xml version="1.0" encoding="utf-8"?>
<iso:schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://purl.oclc.org/dsdl/schematron"
    queryBinding='xslt2' schemaVersion='ISO19757-3'>

  <iso:title>Schematron file for "Läkarintyg för deltagare i arbetsmarknadspolitiska program".</iso:title>

  <iso:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
  <iso:ns prefix="rg" uri="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3"/>
  <iso:ns prefix="gn" uri="urn:riv:clinicalprocess:healthcond:certificate:3"/>
  <iso:ns prefix="tp" uri="urn:riv:clinicalprocess:healthcond:certificate:types:3"/>

    <!-- Kategori 1 - Grund för medicinskt underlag -->
      <iso:pattern id="q1">
        <iso:rule context="//rg:intyg">
        <iso:let name="countValue" value="count(gn:svar[@id='1'])"/>

          <iso:assert test="count(gn:svar[@id='1']) ge 1">
            'Grund för medicinskt underlag' måste besvaras. <value-of select="$countValue"/>
          </iso:assert>

          <iso:assert test="$countValue le 2">
            'Grund för medicinskt underlag' får vara max 2 "<value-of select="$countValue"/>"
          </iso:assert>

          <iso:assert test="count(gn:svar[@id='2']) ge 1">
             Ett 'MU' måste ha ett 'Arbetsmarknadspolitiskt program'
          </iso:assert>

              <!-- Kategori 3 - Funktionsnedsättning -->
          <iso:assert test="count(gn:svar[@id='3']) ge 1">
               Ett 'MU' måste ha en 'Funktionsnedsättning'
            </iso:assert>

             <!-- Kategori 4 - Aktivitetsbegränsning -->
           <iso:assert test="count(gn:svar[@id='4']) ge 1">
               Ett 'MU' måst ha en 'Aktivitetsbegränsning'
            </iso:assert>

             <!-- Kategori 5 - Förhinder -->
           <iso:assert test="count(gn:svar[@id='5']) ge 1">
               Ett 'MU' måst ha ett 'Förhinder'
            </iso:assert>

            <!-- Kategori 7 - Begränsning sjukfrånvaro -->
            <iso:assert test="count(gn:svar[@id='7']) ge 1">
               Ett 'MU' måste ha en 'Begränsning av sjukfrånvaro'
             </iso:assert>

            <!-- Kategori 8 - Prognos återgång -->
              <iso:assert test="count(gn:svar[@id='8']) ge 1">
                Ett 'MU' måste ha en 'Prognos återgång'
              </iso:assert>
        </iso:rule>
      </iso:pattern>

     <iso:pattern id="q1.1">
        <iso:rule context="//gn:delsvar[@id='1.1']">
        <iso:extends rule="cv"/>
          <iso:assert test="tp:cv/tp:codeSystem = 'FKMU_0001'">'codeSystem' måste vara 'FKMU_0001'.</iso:assert>
          <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^(UNDERSOKNING|ANNAT)$')">
            Tillåtna värden är UNDERSOKNING, ANNAT.
          </iso:assert>
        </iso:rule>
    </iso:pattern>

      <iso:pattern id="q1.2">
        <iso:rule context="//gn:delsvar[@id='1.2']">
          <iso:extends rule="date"/>
        </iso:rule>
      </iso:pattern>

     <iso:pattern id="q1.3">
     <iso:rule context="//gn:delsvar[@id='1.1' and matches(normalize-space(.), '3|ANNAT')]">
     <iso:let name="countValue" value="count(../gn:delsvar[@id='1.3'])"/>
        <iso:assert test="count(../gn:delsvar[@id='1.3']) = 1">
            Om "Typ av grund för medicinskt underlag" har besvarats med "Annat" måste "Annan grund finns för medicinskt underlag" fyllas i.
        </iso:assert>
      </iso:rule>
    </iso:pattern>

    <!-- Kategori 2 - Arbetsmarknadspolitiskt program -->
    <iso:pattern id="q2.2">
        <iso:rule context="//gn:delsvar[@id='2.2']">
          <iso:assert test="tp:cv/tp:codeSystem = 'kv-omfattning-program'">'codeSystem' måste vara 'kv-omfattning-program'.</iso:assert>
          <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^(FT|PT|UNK)$')">
            'Typ av grund för MU' kan ha ett av värdena Heltid (40 timmar/vecka), Deltid, Okänd.
          </iso:assert>
        </iso:rule>
      </iso:pattern>

      <iso:pattern id="q2.3">
              <iso:rule context="//gn:delsvar[@id='2.3']">
                <iso:assert test="../gn:delsvar[@id='2.2']/tp:cv/tp:code = 'PT'" />
                <iso:assert test="number(.) ge 1 and number(.) le 39" />
        </iso:rule>
      </iso:pattern>

    <!-- Kategori 5 - Förhinder -->
    <iso:pattern id="q5.1">
      <iso:rule context="//gn:delsvar[@id='5.1']">
         <iso:extends rule="boolean"/>
      </iso:rule>
     </iso:pattern>


     <!-- Kategori 6 - Sjukfrånvaro -->
     <iso:pattern id="q51-6">
      <iso:rule context="//gn:delsvar[@id='5.1' and matches(normalize-space(.), '1|true')]">
        <iso:let name="countValue" value="count(../../gn:svar[@id='6'])"/>
           <iso:assert test="$countValue le 4">
            "Sjukfrånvaro från arbetsmarknadspolitiskt program" får finnas max 4 gånger. "<value-of select="$countValue"/>".
           </iso:assert>
    </iso:rule>
      <iso:rule context="//gn:delsvar[@id='5.1' and matches(normalize-space(.), '0|false')]">
        <iso:let name="countValue" value="count(../../gn:svar[@id='6'])"/>
           <iso:assert test="$countValue le 1">
             Sjukfrånvaro från arbetsmarknadspolitiskt program får ej fyllas i. "<value-of select="$countValue"/>".
           </iso:assert>
       </iso:rule>

     </iso:pattern>


     <iso:pattern id="q6">
     <iso:rule context="//gn:svar[@id='6']">
        <iso:assert test="number(gn:delsvar[@id='6.1']) ge 1 and number(gn:delsvar[@id='6.1']) le 100">
            Sjukfrånvaro måste anges i % mellan 1 och 100.
        </iso:assert>

        <iso:assert test="count(gn:delsvar[@id='6.2']) = 1">'Sjukskrivning' måste ha en 'period'.</iso:assert>
          <iso:let name="cstart" value="normalize-space(gn:delsvar[@id='6.2']/tp:datePeriod/tp:start)"/>
          <iso:let name="cend" value="normalize-space(gn:delsvar[@id='6.2']/tp:datePeriod/tp:end)"/>
          <iso:assert test="not(preceding-sibling::gn:svar[@id='6']/gn:delsvar[@id='6.2']/tp:datePeriod/tp:start[normalize-space(.) lt $cend and normalize-space(../tp:end) gt $cstart])">
            Två 'perioder' kan inte vara överlappande.
        </iso:assert>
    </iso:rule>
   </iso:pattern>


    <!-- Kategori 7 - Begränsning sjukfrånvaro -->
     <iso:pattern id="q7">
        <iso:rule context="//gn:delsvar[@id='7.1']">
            <iso:extends rule="boolean"/>
        </iso:rule>
     </iso:pattern>

   <iso:pattern id="q71-q72">
    <iso:rule context="//gn:delsvar[@id='7.1' and matches(normalize-space(.), '1|true')]">
     <iso:let name="countValue" value="count(../gn:delsvar[@id='7.2'])"/>
        <iso:assert test="count(../gn:delsvar[@id='7.2']) = 1">
            Om "Kan sjukfrånvaron begränsas genom anpassning" besvarats med "Ja" är "Anpassningar som kan begränsa sjukfrånvaro" obligatorisk.
        </iso:assert>
      </iso:rule>
    </iso:pattern>


 <!-- Kategori 8 - Prognos återgång -->
    <iso:pattern id="q8.1">
      <iso:rule context="//gn:delsvar[@id='8.1']">
        <iso:extends rule="cv"/>
        <iso:assert test="tp:cv/tp:codeSystem = 'kv-prognos-återgång'">'codeSystem' måste vara 'kv-prognos-återgång'.</iso:assert>
        <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^(ATERGANG.ATERGA_UTAN_ANPASSNING.RBK|ATERGANG.ATERGA_MED_ANPASSNING.RBK|ATERGANG.KAN_EJ_ATERGA.RBK|ATERGANG.EJ_MOJLIGT_AVGORA.RBK)$')">
          Svaret måste vara ett av värdena ATERGA_UTAN_ANPASSNING, ATERGA_MED_ANPASSNING, KAN_EJ_ATERGA, EJ_MOJLIGT_AVGORA.
        </iso:assert>
      </iso:rule>
    </iso:pattern>

    <iso:pattern id="q8.2-81">
        <iso:rule context="//gn:delsvar[@id='8.1']/tp:cv/tp:code[matches(normalize-space(.), '1|ATERGANG.ATERGA_MED_ANPASSNING.RBK')]">
         <iso:let name="countValue" value="count(../../../gn:delsvar[@id='8.2'])"/>
            <iso:assert test="$countValue = 1">
                Denna fråga kan inte vara tom om föregeående svarats med ATERGANG.ATERGA_MED_ANPASSNING. "<value-of select="$countValue"/>".
            </iso:assert>
        </iso:rule>
     </iso:pattern>



  <iso:pattern id="instans-pattern">
    <iso:rule context="//gn:instans">
      <iso:assert test="number(.) ge 1">
        'Instans' måste vara större än 0.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

    <iso:pattern id="instans-pattern-bigger-100">
      <iso:rule context="//gn:instans-100">
        <iso:assert test="number(.) le 101">
          'Instans' får inte vara större än 100.
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

  <iso:pattern id="date-pattern">
    <iso:rule id="date" abstract="true">
     <iso:assert test="count(*) = 0">Datum får inte vara inbäddat i något element.</iso:assert>
     <iso:assert test=". castable as xs:date">Värdet måste vara ett giltigt datum.</iso:assert>
     <iso:assert test="matches(., '^\d{4}-\d\d-\d\d')">Datumet måste uttryckas som YYYY-MM-DD.</iso:assert>
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

</iso:schema>
