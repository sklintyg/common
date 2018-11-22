<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright (C) 2016 Inera AB (http://www.inera.se)
  ~
  ~ This file is part of sklintyg (https://github.com/sklintyg).
  ~
  ~ sklintyg is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ sklintyg is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program.  If not, see <http://www.gnu.org/licenses/>.
  -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ns1="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3"
    xmlns:ns2="urn:riv:clinicalprocess:healthcond:certificate:3"
    xmlns:ns3="urn:riv:clinicalprocess:healthcond:certificate:types:3"

    xmlns:p="urn:riv:clinicalprocess:healthcond:certificate:1"
    xmlns:p2="urn:riv:clinicalprocess:healthcond:certificate:ts-bas:1"
    xmlns:reg="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1">

  <xsl:output method="xml" indent="yes"/>

  <xsl:include href="xsl/V3toV1-common.xsl"/>

  <xsl:variable name="ts-bas-ns" select="'urn:riv:clinicalprocess:healthcond:certificate:ts-bas:1'"/>
  <xsl:variable name="ts-bas-prefix" select="'p2'"/>

  <xsl:template match="ns1:intyg">
    <reg:RegisterCertificate>
      <reg:utlatande>

        <xsl:call-template name="utlatandeHeader">
          <xsl:with-param name="displayName" select="'TSTRK1007'"/>
        </xsl:call-template>

        <xsl:call-template name="grundData">
          <xsl:with-param name="ns-namespace" select="$ts-bas-ns"/>
          <xsl:with-param name="ns-prefix" select="$ts-bas-prefix"/>
        </xsl:call-template>

        <xsl:call-template name="vardKontakt"/>

        <!-- Synfältsprövning (Donders konfrontationsmetod) -->
        <xsl:call-template name="ogatsSynfaltAktivitet">
          <xsl:with-param name="ns-namespace" select="$ts-bas-ns"/>
          <xsl:with-param name="ns-prefix" select="$ts-bas-prefix"/>
        </xsl:call-template>

        <xsl:call-template name="ogatsRorlighetAktivitet"/>

        <!-- Undersökning med 8+ dioptrier -->
        <xsl:if test="matches(normalize-space(ns2:svar[@id='9']/ns2:delsvar[@id='9.1']), '1|true')">
          <p:aktivitet>
            <p:aktivitetskod code="AKT17" codeSystem="{$id_kv_aktiviteter_intyg}" codeSystemName="kv_aktiviteter_intyg"/>
            <p:forekomst>true</p:forekomst>
          </p:aktivitet>
        </xsl:if>

        <!-- Vårdinsats för missbruk eller beroende av alkohol, narkotika eller läkemedel (11.2) -->
        <p:aktivitet>
          <p:aktivitetskod code="AKT15" codeSystem="{$id_kv_aktiviteter_intyg}" codeSystemName="kv_aktiviteter_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='25']/ns2:delsvar[@id='25.2']"/>
          </p:forekomst>
        </p:aktivitet>

        <!-- Provtagning avseende narkotika eller alkohol -->
        <xsl:if test="matches(normalize-space(ns2:svar[@id='25']/ns2:delsvar[@id='25.2']), '1|true')">
          <p:aktivitet>
            <p:aktivitetskod code="AKT14" codeSystem="{$id_kv_aktiviteter_intyg}" codeSystemName="kv_aktiviteter_intyg"/>
            <p:forekomst>
              <xsl:value-of select="ns2:svar[@id='25']/ns2:delsvar[@id='25.3']"/>
            </p:forekomst>
          </p:aktivitet>
        </xsl:if>

        <!-- Vård på sjukhus (16.1) -->
        <xsl:if test="ns2:svar[@id='30']">
          <p:aktivitet>
            <p:aktivitetskod code="AKT19" codeSystem="{$id_kv_aktiviteter_intyg}" codeSystemName="kv_aktiviteter_intyg"/>
            <xsl:if test="ns2:svar[@id='30']/ns2:delsvar[@id='30.1' and matches(normalize-space(.), '1|true')]">
              <p:beskrivning>
                <xsl:value-of select="ns2:svar[@id='30']/ns2:delsvar[@id='30.4']"/>
              </p:beskrivning>
            </xsl:if>
            <p:forekomst>
              <xsl:value-of select="ns2:svar[@id='30']/ns2:delsvar[@id='30.1']"/>
            </p:forekomst>
            <xsl:if test="ns2:svar[@id='30']/ns2:delsvar[@id='30.3']">
              <p2:plats>
                <xsl:value-of select="ns2:svar[@id='30']/ns2:delsvar[@id='30.3']"/>
              </p2:plats>
            </xsl:if>
            <xsl:if test="ns2:svar[@id='30']/ns2:delsvar[@id='30.2']">
              <p2:ostruktureradtid>
                <xsl:value-of select="ns2:svar[@id='30']/ns2:delsvar[@id='30.2']"/>
              </p2:ostruktureradtid>
            </xsl:if>
          </p:aktivitet>
        </xsl:if>

        <p:rekommendation>
          <p:rekommendationskod code="REK8" codeSystem="{$id_kv_rekommendation_intyg}" codeSystemName="kv_rekommendation_intyg"/>
          <xsl:choose>
            <xsl:when test="ns2:svar[@id='33']/ns2:delsvar[@id='33.1' and matches(normalize-space(./ns3:cv/ns3:code), 'VAR10')] = '1|true'">
              <p2:varde code="VAR11" codeSystem="{$id_kv_korkortsbehorighet}" codeSystemName="kv_körkortsbehörighet"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:for-each select="ns2:svar[@id='33']/ns2:delsvar[@id='33.1']">
                <p2:varde codeSystem="{$id_kv_korkortsbehorighet}" codeSystemName="kv_körkortsbehörighet" code="{./ns3:cv/ns3:code}"/>
              </xsl:for-each>
            </xsl:otherwise>
          </xsl:choose>
        </p:rekommendation>

        <xsl:if test="ns2:svar[@id='34']/ns2:delsvar[@id='34.1']">
          <p:rekommendation>
            <p:rekommendationskod code="REK9" codeSystem="{$id_kv_rekommendation_intyg}" codeSystemName="kv_rekommendation_intyg"/>
            <p:beskrivning>
              <xsl:value-of select="ns2:svar[@id='34']/ns2:delsvar[@id='34.1']"/>
            </p:beskrivning>
          </p:rekommendation>
        </xsl:if>

        <!-- Synfältsprövning -->
        <p:observation>
          <p:observations-id root="1.2.752.129.2.1.2.1" extension="{$observations-id1}"/>
          <p:observationskod code="H53.4" codeSystem="1.2.752.116.1.1.1.1.1" codeSystemName="ICD-10"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='3']/ns2:delsvar[@id='3.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Begränsning av seende vid nedsatt belysning -->
        <p:observation>
          <p:observationskod code="H53.6" codeSystem="{$id_icd-10}" codeSystemName="ICD-10"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='4']/ns2:delsvar[@id='4.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Progressiv ögonsjukdom -->
        <p:observation>
          <p:observationskod code="OBS1" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='5']/ns2:delsvar[@id='5.1']"/>
          </p:forekomst>
        </p:observation>

        <xsl:call-template name="ogatsRorlighetObservation"/>

        <!-- Nystagmus -->
        <p:observation>
          <p:observationskod code="H55.9" codeSystem="{$id_icd-10}" codeSystemName="ICD-10"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='7']/ns2:delsvar[@id='7.1']"/>
          </p:forekomst>
        </p:observation>

        <xsl:call-template name="synfunktionObservation">
          <xsl:with-param name="ns-namespace" select="$ts-bas-ns"/>
          <xsl:with-param name="ns-prefix" select="$ts-bas-prefix"/>
        </xsl:call-template>

        <!-- Kontaktlinser -->
        <xsl:if test="ns2:svar[@id='8']/ns2:delsvar[@id='8.7']">
          <p:observation>
            <p:observationskod code="285049007" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
            <p:forekomst>
              <xsl:value-of select="ns2:svar[@id='8']/ns2:delsvar[@id='8.7']"/>
            </p:forekomst>
            <p2:lateralitet code="24028007" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
          </p:observation>
        </xsl:if>

        <xsl:if test="ns2:svar[@id='8']/ns2:delsvar[@id='8.8']">
          <p:observation>
            <p:observationskod code="285049007" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
            <p:forekomst>
              <xsl:value-of select="ns2:svar[@id='8']/ns2:delsvar[@id='8.8']"/>
            </p:forekomst>
            <p2:lateralitet code="7771000" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
          </p:observation>
        </xsl:if>

        <!-- Anfall av balansrubbningar eller yrsel -->
        <p:observation>
          <p:observationskod code="OBS2" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='10']/ns2:delsvar[@id='10.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Uppfatta samtal 4 meter -->
        <xsl:if test="ns2:svar[@id='11']/ns2:delsvar[@id='11.1']">
          <p:observation>
            <p:observationskod code="OBS3" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
            <p:forekomst>
              <xsl:value-of select="ns2:svar[@id='11']/ns2:delsvar[@id='11.1']"/>
            </p:forekomst>
          </p:observation>
        </xsl:if>

        <!-- Rörelseorganens funktioner -->
        <p:observation>
          <p:observationskod code="OBS4" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <xsl:if test="ns2:svar[@id='12']/ns2:delsvar[@id='12.1' and matches(normalize-space(.), '1|true')]">
            <p:beskrivning>
              <xsl:value-of select="ns2:svar[@id='12']/ns2:delsvar[@id='12.2']"/>
            </p:beskrivning>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='12']/ns2:delsvar[@id='12.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Oförmåga hjälpa passagerare -->
        <xsl:if test="ns2:svar[@id='13']">
          <p:observation>
            <p:observationskod code="OBS5" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
            <p:forekomst>
              <xsl:value-of select="ns2:svar[@id='13']/ns2:delsvar[@id='13.1']"/>
            </p:forekomst>
          </p:observation>
        </xsl:if>

        <!-- Hjärt och kärlsjukdomar (6.1 - 6.3) -->

        <!-- Hjärt- och kärlsjukdom som innebär en trafiksäkerhetsrisk -->
        <p:observation>
          <p:observationskod code="OBS6" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='14']/ns2:delsvar[@id='14.1']"/>
          </p:forekomst>
        </p:observation>

        <p:observation>
          <p:observationskod code="OBS8" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='15']/ns2:delsvar[@id='15.1']"/>
          </p:forekomst>
        </p:observation>

        <p:observation>
          <p:observationskod code="OBS7" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <xsl:if test="ns2:svar[@id='16']/ns2:delsvar[@id='16.1' and matches(normalize-space(.), '1|true')]">
            <p:beskrivning>
              <xsl:value-of select="ns2:svar[@id='16']/ns2:delsvar[@id='16.2']"/>
            </p:beskrivning>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='16']/ns2:delsvar[@id='16.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Diabetes (7.2) -->
        <p:observation>
          <p:observationskod code="73211009" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='17']/ns2:delsvar[@id='17.1']"/>
          </p:forekomst>
        </p:observation>

        <xsl:if test="ns2:svar[@id='17']/ns2:delsvar[@id='17.1' and matches(normalize-space(.),'1|true')]">
          <xsl:choose>

            <xsl:when test="ns2:svar[@id='18']/ns2:delsvar[@id='18.1' and matches(normalize-space(./ns3:cv/ns3:code), 'E10')]">
              <!-- typ1 -->
              <p:observation>
                <p:observationskod code="E10" codeSystem="{$id_icd-10}" codeSystemName="ICD-10"/>
                <p:forekomst>true</p:forekomst>
              </p:observation>
            </xsl:when>

            <xsl:otherwise>
              <!-- typ2 -->
              <p:observation>
                <p:observationskod code="E11" codeSystem="{$id_icd-10}" codeSystemName="ICD-10"/>
                <p:forekomst>true</p:forekomst>
              </p:observation>

              <!-- Kostbehandling -->
              <xsl:if test="ns2:svar[@id='19']/ns2:delsvar[@id='19.1' and matches(normalize-space(.), '1|true')]">
                <p:observation>
                  <p:observationskod code="OBS9" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
                  <p:forekomst>true</p:forekomst>
                </p:observation>
              </xsl:if>

              <!-- Tablettbehandling -->
              <xsl:if test="ns2:svar[@id='19']/ns2:delsvar[@id='19.2' and matches(normalize-space(.), '1|true')]">
                <p:observation>
                  <p:observationskod code="170746002" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
                  <p:forekomst>true</p:forekomst>
                </p:observation>
              </xsl:if>

              <!-- Insulinbehandling -->
              <xsl:if test="ns2:svar[@id='19']/ns2:delsvar[@id='19.3' and matches(normalize-space(.), '1|true')]">
                <p:observation>
                  <p:observationskod code="170747006" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
                  <p:forekomst>true</p:forekomst>
                </p:observation>
              </xsl:if>
            </xsl:otherwise>

          </xsl:choose>
        </xsl:if>

        <!-- Neurologiska sjukdomar (8) -->
        <p:observation>
          <p:observationskod code="407624006" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='20']/ns2:delsvar[@id='20.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Epilepsi etc (9.1) -->
        <p:observation>
          <p:observationskod code="G40.9" codeSystem="{$id_icd-10}" codeSystemName="ICD-10"/>
          <xsl:if test="ns2:svar[@id='21']/ns2:delsvar[@id='21.1' and matches(normalize-space(.), '1|true')]">
            <p:beskrivning>
              <xsl:value-of select="ns2:svar[@id='21']/ns2:delsvar[@id='21.2']"/>
            </p:beskrivning>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='21']/ns2:delsvar[@id='21.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Njursjukdomar -->
        <p:observation>
          <p:observationskod code="OBS11" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='22']/ns2:delsvar[@id='22.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Demens och kognitiva störningar -->
        <p:observation>
          <p:observationskod code="OBS12" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='23']/ns2:delsvar[@id='23.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Sömn- och vakenhetsstörningar -->
        <p:observation>
          <p:observationskod code="OBS13" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='24']/ns2:delsvar[@id='24.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Alkohol, narkotika och läkemedel (13.1 - 13.2) -->
        <p:observation>
          <p:observationskod code="OBS14" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='25']/ns2:delsvar[@id='25.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Regelbundet läkarordinerat bruk av läkemedel som kan innebära en trafiksäkerhetsrisk -->
        <p:observation>
          <p:observationskod code="OBS15" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <xsl:if test="ns2:svar[@id='26']/ns2:delsvar[@id='26.1' and matches(normalize-space(.), '1|true')]">
            <p:beskrivning>
              <xsl:value-of select="ns2:svar[@id='26']/ns2:delsvar[@id='26.2']"/>
            </p:beskrivning>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='26']/ns2:delsvar[@id='26.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Psykisk sjukdom eller störning (14) -->
        <p:observation>
          <p:observationskod code="OBS16" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='27']/ns2:delsvar[@id='27.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Psykisk utvecklingsstörning (15.1) -->
        <p:observation>
          <p:observationskod code="129104009" codeSystem="{$id_snomed-ct}" codeSystemName="SNOMED-CT"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='28']/ns2:delsvar[@id='28.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- ADHD DAMP (15.2) -->
        <p:observation>
          <p:observationskod code="OBS17" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='29']/ns2:delsvar[@id='29.1']"/>
          </p:forekomst>
        </p:observation>

        <!-- Stadigvarande medicinering (17.1) -->
        <p:observation>
          <p:observationskod code="OBS18" codeSystem="{$id_kv_observationer_intyg}" codeSystemName="kv_observationer_intyg"/>
          <xsl:if test="ns2:svar[@id='31']/ns2:delsvar[@id='31.1' and matches(normalize-space(.), '1|true')]">
            <p:beskrivning>
              <xsl:value-of select="ns2:svar[@id='31']/ns2:delsvar[@id='31.2']"/>
            </p:beskrivning>
          </xsl:if>
          <p:forekomst>
            <xsl:value-of select="ns2:svar[@id='31']/ns2:delsvar[@id='31.1']"/>
          </p:forekomst>
        </p:observation>

        <p2:observationAktivitetRelation>
          <p2:observationsid root="1.2.752.129.2.1.2.1" extension="{$observations-id1}"/>
          <p2:aktivitetsid root="1.2.752.129.2.1.2.1" extension="{$aktivitets-id1}"/>
        </p2:observationAktivitetRelation>

        <p2:observationAktivitetRelation>
          <p2:observationsid root="1.2.752.129.2.1.2.1" extension="{$observations-id2}"/>
          <p2:aktivitetsid root="1.2.752.129.2.1.2.1" extension="{$aktivitets-id2}"/>
        </p2:observationAktivitetRelation>

        <xsl:for-each select="ns2:svar[@id='1']/ns2:delsvar[@id='1.1']">
          <p2:intygAvser codeSystem="{$id_kv_intyget_avser}" codeSystemName="kv_intyget_avser" code="{./ns3:cv/ns3:code}"/>
        </xsl:for-each>

        <p2:utgava>
          <xsl:value-of select="concat('0', substring(ns2:version, 3,3))"/>
        </p2:utgava>
        <p2:version>
          <xsl:value-of select="concat('0', substring(ns2:version, 1,1))"/>
        </p2:version>

      </reg:utlatande>
    </reg:RegisterCertificate>
  </xsl:template>

  <!-- Dont output text nodes we dont transform  -->
  <xsl:template match="ns2:typ"/>

</xsl:stylesheet>
