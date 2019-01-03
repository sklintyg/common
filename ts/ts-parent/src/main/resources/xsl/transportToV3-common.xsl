<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright (C) 2019 Inera AB (http://www.inera.se)
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
    xmlns:p1="urn:riv:clinicalprocess:healthcond:certificate:3"
    xmlns:p2="urn:riv:clinicalprocess:healthcond:certificate:types:3"
>

  <xsl:output method="xml" indent="yes"/>

  <xsl:variable name="intygAvser">
    <mapping key="C1" value="IAV1" displayname="C1"/>
    <mapping key="C1E" value="IAV2" displayname="C1E"/>
    <mapping key="C" value="IAV3" displayname="C"/>
    <mapping key="CE" value="IAV4" displayname="CE"/>
    <mapping key="D1" value="IAV5" displayname="D1"/>
    <mapping key="D1E" value="IAV6" displayname="D1E"/>
    <mapping key="D" value="IAV7" displayname="D"/>
    <mapping key="DE" value="IAV8" displayname="DE"/>
    <mapping key="TAXI" value="IAV9" displayname="Taxi"/>
    <mapping key="ANNAT" value="IAV10" displayname="Annat"/>
    <mapping key="AM" value="IAV11" displayname="AM"/>
    <mapping key="A1" value="IAV12" displayname="A1"/>
    <mapping key="A2" value="IAV13" displayname="A2"/>
    <mapping key="A" value="IAV14" displayname="A"/>
    <mapping key="B" value="IAV15" displayname="B"/>
    <mapping key="BE" value="IAV16" displayname="BE"/>
    <mapping key="TRAKTOR" value="IAV17" displayname="Traktor"/>
  </xsl:variable>

  <xsl:variable name="korkortsTyp">
    <mapping key="C1" value="VAR1" displayname="C1"/>
    <mapping key="C1E" value="VAR2" displayname="C1E"/>
    <mapping key="C" value="VAR3" displayname="C"/>
    <mapping key="CE" value="VAR4" displayname="CE"/>
    <mapping key="D1" value="VAR5" displayname="D1"/>
    <mapping key="D1E" value="VAR6" displayname="D1E"/>
    <mapping key="D" value="VAR7" displayname="D"/>
    <mapping key="DE" value="VAR8" displayname="DE"/>
    <mapping key="TAXI" value="VAR9" displayname="Taxi"/>
    <mapping key="ANNAT" value="VAR10" displayname="Annat (AM, A1, A2, A, B, BE eller Traktor)"/>
    <mapping key="AM" value="VAR12" displayname="AM"/>
    <mapping key="A1" value="VAR13" displayname="A1"/>
    <mapping key="A2" value="VAR14" displayname="A2"/>
    <mapping key="A" value="VAR15" displayname="A"/>
    <mapping key="B" value="VAR16" displayname="B"/>
    <mapping key="BE" value="VAR17" displayname="BE"/>
    <mapping key="TRAKTOR" value="VAR18" displayname="Traktor"/>
  </xsl:variable>

  <xsl:variable name="idKontrollKod">
    <mapping key="IDK1" value="ID-kort"/>
    <mapping key="IDK2" value="Företagskort eller tjänstekort"/>
    <mapping key="IDK3" value="Svenskt körkort"/>
    <mapping key="IDK4" value="Personlig kännedom"/>
    <mapping key="IDK5" value="Försäkran enligt 18 kap. 4 §"/>
    <mapping key="IDK6" value="Pass"/>
  </xsl:variable>

  <xsl:variable name="diabetesTyp">
    <mapping key="TYP1" value="E10" description="Diabetes mellitus typ 1"/>
    <mapping key="TYP2" value="E11" description="Diabetes mellitus typ 2"/>
  </xsl:variable>

  <xsl:variable name="id_kv_intyget_avser" select="'24c41b8d-258a-46bf-a08a-b90738b28770'"/>
  <xsl:variable name="id_kv_id_kontroll" select="'e7cc8f30-a353-4c42-b17a-a189b6876647'"/>
  <xsl:variable name="id_kv_korkortsbehorighet" select="'e889fa20-1dee-4f79-8b37-03853e75a9f8'"/>
  <xsl:variable name="id_snomed-ct" select="'1.2.752.116.2.1.1.1'"/>
  <xsl:variable name="id_icd-10" select="'1.2.752.116.1.1.1.1.3'"/>

  <xsl:variable name="default-version" select="'6.7'"/>

  <!-- Common variables - END -->

  <!-- Common templates V3 -->

  <xsl:template name="id">
    <xsl:param name="elemName"/>
    <xsl:param name="root"/>
    <xsl:param name="extension"/>
    <xsl:element name="{$elemName}">
      <xsl:call-template name="ii">
        <xsl:with-param name="root" select="$root"/>
        <xsl:with-param name="extension" select="$extension"/>
      </xsl:call-template>
    </xsl:element>
  </xsl:template>

  <xsl:template name="cv">
    <xsl:param name="code"/>
    <xsl:param name="codeSystem"/>
    <xsl:param name="displayName"/>
    <p2:cv>
      <p2:code>
        <xsl:value-of select="$code"/>
      </p2:code>
      <p2:codeSystem>
        <xsl:value-of select="$codeSystem"/>
      </p2:codeSystem>
      <p2:displayName>
        <xsl:value-of select="$displayName"/>
      </p2:displayName>
    </p2:cv>
  </xsl:template>

  <xsl:template name="ii">
    <xsl:param name="root"/>
    <xsl:param name="extension"/>
    <p2:root>
      <xsl:value-of select="$root"/>
    </p2:root>
    <p2:extension>
      <xsl:value-of select="$extension"/>
    </p2:extension>
  </xsl:template>

  <xsl:template name="svarMedDelsvar">
    <xsl:param name="svarsId"/>
    <xsl:param name="value"/>
    <xsl:if test="string($value)">
      <p1:svar id="{$svarsId}">
        <p1:delsvar id="{concat($svarsId, '.1')}">
          <xsl:value-of select="$value"/>
        </p1:delsvar>
      </p1:svar>
    </xsl:if>
  </xsl:template>

  <xsl:template name="endastDelsvar">
    <xsl:param name="delsvarsId"/>
    <xsl:param name="value"/>
    <xsl:if test="string($value)">
      <p1:delsvar id="{$delsvarsId}">
        <xsl:value-of select="$value"/>
      </p1:delsvar>
    </xsl:if>
  </xsl:template>

  <!-- Common templates V3 - END -->

</xsl:stylesheet>
