<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ns1="urn:local:se:intygstjanster:services:1"
    xmlns:ns2="urn:local:se:intygstjanster:services:types:1"
    xmlns:ns3="urn:local:se:intygstjanster:services:RegisterTSBasResponder:1"
    xmlns:p1="urn:riv:clinicalprocess:healthcond:certificate:3"
    xmlns:p2="urn:riv:clinicalprocess:healthcond:certificate:types:3"
    xmlns:rc="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3"
    xmlns:ext="http://exslt.org/common"
>

  <xsl:output method="xml" indent="yes"/>

  <xsl:include href="xsl/transportToV3-common.xsl"/>

  <xsl:template match="ns3:intyg">
    <rc:RegisterCertificate>
      <rc:intyg>

        <xsl:apply-templates select="ns1:intygsId"/>

        <p1:typ>
          <p2:code>TSTRK1007</p2:code>
          <p2:codeSystem>f6fb361a-e31d-48b8-8657-99b63912dd9b</p2:codeSystem>
          <p2:displayName>Transportstyrelsens läkarintyg</p2:displayName>
        </p1:typ>

        <xsl:apply-templates select="ns1:version"/>
        <xsl:apply-templates select="ns1:grundData"/>
        <xsl:apply-templates select="ns1:intygAvser"/>
        <xsl:apply-templates select="ns1:identitetStyrkt"/>
        <xsl:apply-templates select="ns1:synfunktion"/>
        <xsl:apply-templates select="ns1:horselBalanssinne"/>
        <xsl:apply-templates select="ns1:rorelseorganensFunktioner"/>
        <xsl:apply-templates select="ns1:hjartKarlSjukdomar"/>
        <xsl:apply-templates select="ns1:diabetes"/>
        <xsl:apply-templates select="ns1:neurologiskaSjukdomar"/>
        <xsl:apply-templates select="ns1:medvetandestorning"/>
        <xsl:apply-templates select="ns1:harNjurSjukdom"/>
        <xsl:apply-templates select="ns1:harKognitivStorning"/>
        <xsl:apply-templates select="ns1:harSomnVakenhetStorning"/>
        <xsl:apply-templates select="ns1:alkoholNarkotikaLakemedel"/>
        <xsl:apply-templates select="ns1:harPsykiskStorning"/>
        <xsl:apply-templates select="ns1:utvecklingsstorning"/>
        <xsl:apply-templates select="ns1:sjukhusvard"/>
        <xsl:apply-templates select="ns1:ovrigMedicinering"/>
        <xsl:apply-templates select="ns1:ovrigKommentar"/>
        <xsl:apply-templates select="ns1:bedomning"/>
      </rc:intyg>
    </rc:RegisterCertificate>
  </xsl:template>

  <!-- GrundData -->

  <xsl:template match="ns1:grundData">
    <p1:signeringstidpunkt>
      <xsl:value-of select="ns1:signeringsTidstampel"/>
    </p1:signeringstidpunkt>

    <xsl:apply-templates select="ns1:patient"/>
    <xsl:apply-templates select="ns1:skapadAv"/>
  </xsl:template>

  <xsl:template match="ns1:intygsId">
    <xsl:call-template name="id">
      <xsl:with-param name="elemName" select="'p1:intygs-id'"/>
      <xsl:with-param name="root" select="../ns1:grundData/ns1:skapadAv/ns1:vardenhet/ns1:enhetsId/ns2:extension"/>
      <xsl:with-param name="extension" select="."/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:version">
    <p1:version>
      <xsl:if test="string(.)">
        <xsl:value-of select="substring(.,2)"/>.<xsl:value-of select="substring(../ns1:utgava,2)"/>
      </xsl:if>
      <xsl:if test="not(string(.))">
        <xsl:value-of select="$default-version"/>
      </xsl:if>
    </p1:version>
  </xsl:template>

  <xsl:template match="ns1:patient">
    <p1:patient>
      <xsl:apply-templates select="ns1:personId"/>

      <p1:fornamn>
        <xsl:value-of select="ns1:fornamn"/>
      </p1:fornamn>
      <p1:efternamn>
        <xsl:value-of select="ns1:efternamn"/>
      </p1:efternamn>
      <p1:postadress>
        <xsl:value-of select="ns1:postadress"/>
      </p1:postadress>
      <p1:postnummer>
        <xsl:value-of select="ns1:postnummer"/>
      </p1:postnummer>
      <p1:postort>
        <xsl:value-of select="ns1:postort"/>
      </p1:postort>
    </p1:patient>
  </xsl:template>

  <xsl:template match="ns1:skapadAv">
    <p1:skapadAv>
      <xsl:apply-templates select="ns1:personId"/>

      <p1:fullstandigtNamn>
        <xsl:value-of select="ns1:fullstandigtNamn"/>
      </p1:fullstandigtNamn>

      <xsl:apply-templates select="ns1:befattningar"/>
      <xsl:apply-templates select="ns1:vardenhet"/>
      <xsl:apply-templates select="ns1:specialiteter"/>

    </p1:skapadAv>
  </xsl:template>

  <xsl:template match="ns1:personId">
    <xsl:variable name="parentName" select="name(parent::node())"/>
    <xsl:if test="contains($parentName, 'patient')">
      <xsl:call-template name="id">
        <xsl:with-param name="elemName" select="'p1:person-id'"/>
        <xsl:with-param name="root" select="ns2:root"/>
        <xsl:with-param name="extension">
          <xsl:choose>
            <xsl:when test="contains(ns2:extension, '-')">
              <xsl:call-template name="remove">
                <xsl:with-param name="haystack" select="ns2:extension"/>
                <xsl:with-param name="needle" select="'-'"/>
              </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="ns2:extension"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="contains($parentName, 'skapadAv')">
      <xsl:call-template name="id">
        <xsl:with-param name="elemName" select="'p1:personal-id'"/>
        <xsl:with-param name="root" select="ns2:root"/>
        <xsl:with-param name="extension" select="ns2:extension"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <xsl:template name="remove">
    <xsl:param name="haystack"/>
    <xsl:param name="needle"/>
    <xsl:value-of select="concat(substring-before($haystack, $needle), substring-after($haystack, $needle))"/>
  </xsl:template>

  <xsl:template match="ns1:vardenhet">
    <p1:enhet>
      <xsl:apply-templates select="ns1:enhetsId"/>

      <p1:arbetsplatskod>
        <p2:root>1.2.752.29.4.71</p2:root>
        <p2:extension>N/A</p2:extension>
      </p1:arbetsplatskod>
      <p1:enhetsnamn>
        <xsl:value-of select="ns1:enhetsnamn"/>
      </p1:enhetsnamn>
      <p1:postadress>
        <xsl:value-of select="ns1:postadress"/>
      </p1:postadress>
      <p1:postnummer>
        <xsl:value-of select="ns1:postnummer"/>
      </p1:postnummer>
      <p1:postort>
        <xsl:value-of select="ns1:postort"/>
      </p1:postort>
      <p1:telefonnummer>
        <xsl:value-of select="ns1:telefonnummer"/>
      </p1:telefonnummer>

      <xsl:apply-templates select="ns1:vardgivare"/>
    </p1:enhet>
  </xsl:template>

  <xsl:template match="ns1:enhetsId">
    <xsl:call-template name="id">
      <xsl:with-param name="elemName" select="'p1:enhets-id'"/>
      <xsl:with-param name="root" select="ns2:root"/>
      <xsl:with-param name="extension" select="ns2:extension"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:specialiteter">
    <xsl:if test="string(.)">
      <p1:specialistkompetens>
        <p2:code>N/A</p2:code>
        <p2:displayName>
          <xsl:value-of select="."/>
        </p2:displayName>
      </p1:specialistkompetens>
    </xsl:if>
  </xsl:template>

  <xsl:template match="ns1:befattningar">
    <xsl:if test="string(.)">
      <p1:befattning>
        <p2:code>
          <xsl:value-of select="."/>
        </p2:code>
        <p2:codeSystem>1.2.752.129.2.2.1.4</p2:codeSystem>
      </p1:befattning>
    </xsl:if>
  </xsl:template>

  <xsl:template match="ns1:vardgivare">
    <p1:vardgivare>
      <xsl:call-template name="id">
        <xsl:with-param name="elemName" select="'p1:vardgivare-id'"/>
        <xsl:with-param name="root" select="ns1:vardgivarid/ns2:root"/>
        <xsl:with-param name="extension" select="ns1:vardgivarid/ns2:extension"/>
      </xsl:call-template>
      <p1:vardgivarnamn>
        <xsl:value-of select="ns1:vardgivarnamn"/>
      </p1:vardgivarnamn>
    </p1:vardgivare>
  </xsl:template>

  <!-- GrundData - END -->

  <!-- Svar -->

  <xsl:template match="ns1:intygAvser">
    <xsl:apply-templates select="ns1:korkortstyp">
      <xsl:with-param name="svarsId" select="'1'"/>
      <xsl:with-param name="codeSystem" select="$id_kv_intyget_avser"/>
    </xsl:apply-templates>
  </xsl:template>

  <xsl:template match="ns1:identitetStyrkt">
    <p1:svar id="2">
      <p1:delsvar id="2.1">
        <xsl:call-template name="cv">
          <xsl:with-param name="code" select="ns1:idkontroll"/>
          <xsl:with-param name="codeSystem" select="$id_kv_id_kontroll"/>
          <xsl:with-param name="displayName" select="ext:node-set($idKontrollKod)/mapping[@key = current()/ns1:idkontroll]/@value"/>
        </xsl:call-template>
      </p1:delsvar>
    </p1:svar>
  </xsl:template>

  <xsl:template match="ns1:synfunktion">
    <xsl:apply-templates select="ns1:harSynfaltsdefekt"/>
    <xsl:apply-templates select="ns1:harNattblindhet"/>
    <xsl:apply-templates select="ns1:harProgressivOgonsjukdom"/>
    <xsl:apply-templates select="ns1:harDiplopi"/>
    <xsl:apply-templates select="ns1:harNystagmus"/>

    <p1:svar id="8">
      <xsl:apply-templates select="ns1:synskarpaUtanKorrektion"/>
      <xsl:apply-templates select="ns1:synskarpaMedKorrektion"/>
    </p1:svar>

    <xsl:apply-templates select="ns1:harGlasStyrkaOver8Dioptrier"/>
  </xsl:template>

  <xsl:template match="ns1:harSynfaltsdefekt">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'3'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:harNattblindhet">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'4'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:harProgressivOgonsjukdom">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'5'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:harDiplopi">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'6'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:harNystagmus">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'7'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:synskarpaUtanKorrektion">
    <xsl:call-template name="endastDelsvar">
      <xsl:with-param name="delsvarsId" select="'8.1'"/>
      <xsl:with-param name="value" select="format-number(ns1:hogerOga, '0.0')"/>
    </xsl:call-template>
    <xsl:call-template name="endastDelsvar">
      <xsl:with-param name="delsvarsId" select="'8.2'"/>
      <xsl:with-param name="value" select="format-number(ns1:vansterOga, '0.0')"/>
    </xsl:call-template>
    <xsl:call-template name="endastDelsvar">
      <xsl:with-param name="delsvarsId" select="'8.3'"/>
      <xsl:with-param name="value" select="format-number(ns1:binokulart, '0.0')"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:synskarpaMedKorrektion">
    <xsl:if test="ns1:hogerOga">
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'8.4'"/>
        <xsl:with-param name="value" select="format-number(ns1:hogerOga, '0.0')"/>
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="ns1:vansterOga">
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'8.5'"/>
        <xsl:with-param name="value" select="format-number(ns1:vansterOga, '0.0')"/>
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="ns1:binokulart">
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'8.6'"/>
        <xsl:with-param name="value" select="format-number(ns1:binokulart, '0.0')"/>
      </xsl:call-template>
    </xsl:if>
    <xsl:call-template name="endastDelsvar">
      <xsl:with-param name="delsvarsId" select="'8.7'"/>
      <xsl:with-param name="value" select="ns1:harKontaktlinsHogerOga"/>
    </xsl:call-template>
    <xsl:call-template name="endastDelsvar">
      <xsl:with-param name="delsvarsId" select="'8.8'"/>
      <xsl:with-param name="value" select="ns1:harKontaktlinsVansterOga"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:harGlasStyrkaOver8Dioptrier">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'9'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:horselBalanssinne">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'10'"/>
      <xsl:with-param name="value" select="ns1:harBalansrubbningYrsel"/>
    </xsl:call-template>
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'11'"/>
      <xsl:with-param name="value" select="ns1:harSvartUppfattaSamtal4Meter"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:rorelseorganensFunktioner">
    <p1:svar id="12">
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'12.1'"/>
        <xsl:with-param name="value" select="ns1:harRorelsebegransning"/>
      </xsl:call-template>
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'12.2'"/>
        <xsl:with-param name="value" select="ns1:rorelsebegransningBeskrivning"/>
      </xsl:call-template>
    </p1:svar>
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'13'"/>
      <xsl:with-param name="value" select="ns1:harOtillrackligRorelseformagaPassagerare"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:hjartKarlSjukdomar">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'14'"/>
      <xsl:with-param name="value" select="ns1:harRiskForsamradHjarnFunktion"/>
    </xsl:call-template>
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'15'"/>
      <xsl:with-param name="value" select="ns1:harHjarnskadaICNS"/>
    </xsl:call-template>
    <xsl:if test="string(ns1:harRiskfaktorerStroke)">
      <p1:svar id="16">
        <xsl:call-template name="endastDelsvar">
          <xsl:with-param name="delsvarsId" select="'16.1'"/>
          <xsl:with-param name="value" select="ns1:harRiskfaktorerStroke"/>
        </xsl:call-template>
        <xsl:call-template name="endastDelsvar">
          <xsl:with-param name="delsvarsId" select="'16.2'"/>
          <xsl:with-param name="value" select="ns1:riskfaktorerStrokeBeskrivning"/>
        </xsl:call-template>
      </p1:svar>
    </xsl:if>
  </xsl:template>

  <xsl:template match="ns1:diabetes">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'17'"/>
      <xsl:with-param name="value" select="ns1:harDiabetes"/>
    </xsl:call-template>

    <xsl:apply-templates select="ns1:diabetesTyp">
      <xsl:with-param name="svarsId" select="'18'"/>
    </xsl:apply-templates>

    <xsl:if test="ns1:harBehandlingKost or ns1:harBehandlingTabletter or ns1:harBehandlingInsulin">
      <p1:svar id="19">
        <xsl:call-template name="endastDelsvar">
          <xsl:with-param name="delsvarsId" select="'19.1'"/>
          <xsl:with-param name="value" select="ns1:harBehandlingKost"/>
        </xsl:call-template>
        <xsl:call-template name="endastDelsvar">
          <xsl:with-param name="delsvarsId" select="'19.2'"/>
          <xsl:with-param name="value" select="ns1:harBehandlingTabletter"/>
        </xsl:call-template>
        <xsl:call-template name="endastDelsvar">
          <xsl:with-param name="delsvarsId" select="'19.3'"/>
          <xsl:with-param name="value" select="ns1:harBehandlingInsulin"/>
        </xsl:call-template>
      </p1:svar>
    </xsl:if>
  </xsl:template>

  <xsl:template match="ns1:diabetesTyp">
    <xsl:param name="svarsId"/>
    <p1:svar id="{$svarsId}">
      <p1:delsvar id="{concat($svarsId, '.1')}">
        <xsl:call-template name="cv">
          <xsl:with-param name="code" select="ext:node-set($diabetesTyp)/mapping[@key = current()]/@value"/>
          <xsl:with-param name="codeSystem" select="$id_icd-10"/>
          <xsl:with-param name="displayName" select="ext:node-set($diabetesTyp)/mapping[@key = current()]/@description"/>
        </xsl:call-template>
      </p1:delsvar>
    </p1:svar>
  </xsl:template>

  <xsl:template match="ns1:neurologiskaSjukdomar">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'20'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:medvetandestorning">
    <p1:svar id="21">
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'21.1'"/>
        <xsl:with-param name="value" select="ns1:harMedvetandestorning"/>
      </xsl:call-template>
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'21.2'"/>
        <xsl:with-param name="value" select="ns1:medvetandestorningBeskrivning"/>
      </xsl:call-template>
    </p1:svar>
  </xsl:template>

  <xsl:template match="ns1:harNjurSjukdom">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'22'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:harKognitivStorning">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'23'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:harSomnVakenhetStorning">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'24'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:alkoholNarkotikaLakemedel">
    <p1:svar id="25">
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'25.1'"/>
        <xsl:with-param name="value" select="ns1:harTeckenMissbruk"/>
      </xsl:call-template>
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'25.2'"/>
        <xsl:with-param name="value" select="ns1:harVardinsats"/>
      </xsl:call-template>
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'25.3'"/>
        <xsl:with-param name="value" select="ns1:harVardinsatsProvtagningBehov"/>
      </xsl:call-template>
    </p1:svar>

    <xsl:if test="ns1:harLakarordineratLakemedelsbruk or ns1:lakarordineratLakemedelOchDos">
      <p1:svar id="26">
        <xsl:call-template name="endastDelsvar">
          <xsl:with-param name="delsvarsId" select="'26.1'"/>
          <xsl:with-param name="value" select="ns1:harLakarordineratLakemedelsbruk"/>
        </xsl:call-template>
        <xsl:call-template name="endastDelsvar">
          <xsl:with-param name="delsvarsId" select="'26.2'"/>
          <xsl:with-param name="value" select="ns1:lakarordineratLakemedelOchDos"/>
        </xsl:call-template>
      </p1:svar>
    </xsl:if>
  </xsl:template>

  <xsl:template match="ns1:harPsykiskStorning">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'27'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:utvecklingsstorning">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'28'"/>
      <xsl:with-param name="value" select="ns1:harPsykiskUtvecklingsstorning"/>
    </xsl:call-template>
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'29'"/>
      <xsl:with-param name="value" select="ns1:harAndrayndrom"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:sjukhusvard">
    <p1:svar id="30">
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'30.1'"/>
        <xsl:with-param name="value" select="ns1:harSjukhusvardEllerLakarkontakt"/>
      </xsl:call-template>
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'30.2'"/>
        <xsl:with-param name="value" select="ns1:sjukhusvardEllerLakarkontaktDatum"/>
      </xsl:call-template>
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'30.3'"/>
        <xsl:with-param name="value" select="ns1:sjukhusvardEllerLakarkontaktVardinrattning"/>
      </xsl:call-template>
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'30.4'"/>
        <xsl:with-param name="value" select="ns1:sjukhusvardEllerLakarkontaktAnledning"/>
      </xsl:call-template>
    </p1:svar>
  </xsl:template>

  <xsl:template match="ns1:ovrigMedicinering">
    <p1:svar id="31">
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'31.1'"/>
        <xsl:with-param name="value" select="ns1:harStadigvarandeMedicinering"/>
      </xsl:call-template>
      <xsl:call-template name="endastDelsvar">
        <xsl:with-param name="delsvarsId" select="'31.2'"/>
        <xsl:with-param name="value" select="ns1:stadigvarandeMedicineringBeskrivning"/>
      </xsl:call-template>
    </p1:svar>
  </xsl:template>

  <xsl:template match="ns1:ovrigKommentar">
    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'32'"/>
      <xsl:with-param name="value" select="current()"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:bedomning">
    <xsl:variable name="instans" select="count(ns1:korkortstyp)"/>

    <xsl:apply-templates select="ns1:korkortstyp">
      <xsl:with-param name="svarsId" select="'33'"/>
      <xsl:with-param name="codeSystem" select="$id_kv_korkortsbehorighet"/>
    </xsl:apply-templates>

    <xsl:apply-templates select="ns1:kanInteTaStallning">
      <xsl:with-param name="svarsId" select="'33'"/>
      <xsl:with-param name="instans" select="$instans + 1"/>
      <xsl:with-param name="codeSystem" select="$id_kv_korkortsbehorighet"/>
    </xsl:apply-templates>

    <xsl:call-template name="svarMedDelsvar">
      <xsl:with-param name="svarsId" select="'34'"/>
      <xsl:with-param name="value" select="ns1:behovAvLakareSpecialistKompetens"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="ns1:korkortstyp">
    <xsl:param name="svarsId"/>
    <xsl:param name="codeSystem"/>

    <xsl:variable name="parentName" select="name(parent::node())"/>
    <xsl:variable name="mappingNode">
      <xsl:if test="contains($parentName, 'intygAvser')">
        <xsl:copy-of select="$intygAvser"/>
      </xsl:if>
      <xsl:if test="contains($parentName, 'bedomning')">
        <xsl:copy-of select="$korkortsTyp"/>
      </xsl:if>
    </xsl:variable>

    <p1:svar id="{$svarsId}">
      <p1:instans>
        <xsl:value-of select="position()"/>
      </p1:instans>
      <p1:delsvar id="{concat($svarsId, '.1')}">
        <xsl:call-template name="cv">
          <xsl:with-param name="code" select="ext:node-set($mappingNode)/mapping[@key = current()]/@value"/>
          <xsl:with-param name="codeSystem" select="$codeSystem"/>
          <xsl:with-param name="displayName" select="ext:node-set($mappingNode)/mapping[@key = current()]/@displayname"/>
        </xsl:call-template>
      </p1:delsvar>
    </p1:svar>
  </xsl:template>

  <xsl:template match="ns1:kanInteTaStallning">
    <xsl:param name="svarsId"/>
    <xsl:param name="instans"/>
    <xsl:param name="codeSystem"/>
    <xsl:if test="current() = 'true'">
      <p1:svar id="{$svarsId}">
        <p1:instans>
          <xsl:value-of select="$instans"/>
        </p1:instans>
        <p1:delsvar id="{concat($svarsId, '.1')}">
          <xsl:call-template name="cv">
            <xsl:with-param name="code" select="'VAR11'"/>
            <xsl:with-param name="codeSystem" select="$codeSystem"/>
            <xsl:with-param name="displayName" select="'Kan inte ta ställning'"/>
          </xsl:call-template>
        </p1:delsvar>
      </p1:svar>
    </xsl:if>
  </xsl:template>

  <!-- Svar - END -->

</xsl:stylesheet>
