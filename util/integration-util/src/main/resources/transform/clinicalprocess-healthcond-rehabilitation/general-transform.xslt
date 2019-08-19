<?xml version="1.0"?>
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

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:c="urn:riv:clinicalprocess:healthcond:certificate:1">

  <!-- Copy all XML nodes, if no more specific template matches. -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <!-- Transform <soap:Fault> element into a proper WS response. -->
  <xsl:template match="soap:Fault">
    <!--
      Here we call the 'abstract' template named response. Has to be provided by
      all XSLTs which include this one.
      -->
    <xsl:call-template name="response"/>
  </xsl:template>

  <!-- Transform <faultcode> and <faultstring> elements to <resultCode> and <resultText> -->
  <xsl:template name="result">
    <c:resultCode>ERROR</c:resultCode>
    <xsl:choose>
      <xsl:when test="contains(faultstring/text(), 'Unmarshalling Error')">
        <!-- Schema validation errors are transformed to VALIDATION_ERROR -->
        <c:errorId>VALIDATION_ERROR</c:errorId>
      </xsl:when>
      <xsl:otherwise>
        <!-- 'soap:Server' is transformed to APPLICATION_ERROR -->
        <c:errorId>APPLICATION_ERROR</c:errorId>
      </xsl:otherwise>
    </xsl:choose>
    <c:resultText>
      <xsl:value-of select="faultstring/text()"/>
    </c:resultText>
  </xsl:template>

</xsl:stylesheet>
