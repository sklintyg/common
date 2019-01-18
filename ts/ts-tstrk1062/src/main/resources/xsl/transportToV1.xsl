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

  <xsl:include href="xsl/transportToV1-common.xsl"/>

  <xsl:template match="ns3:intyg">
    <rc:RegisterCertificate>
      <rc:intyg>

        <xsl:apply-templates select="ns1:intygsId"/>

        <p1:typ>
          <p2:code>TSTRK1062</p2:code>
          <p2:codeSystem>b64ea353-e8f6-4832-b563-fc7d46f29548</p2:codeSystem>
          <p2:displayName>Läkarintyg avseende ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning</p2:displayName>
        </p1:typ>

        <xsl:apply-templates select="ns1:version"/>
        <xsl:apply-templates select="ns1:grundData"/>
        <xsl:apply-templates select="ns1:intygAvser"/>
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
          <xsl:if test="contains(ns2:extension, '-')">
            <xsl:call-template name="remove">
              <xsl:with-param name="haystack" select="ns2:extension"/>
              <xsl:with-param name="needle" select="'-'"/>
            </xsl:call-template>
          </xsl:if>
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

  <!-- Svar - END -->

</xsl:stylesheet>
