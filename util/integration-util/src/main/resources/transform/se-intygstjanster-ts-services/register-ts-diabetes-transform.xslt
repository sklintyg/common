<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rmc="urn:local:se:intygstjanster:services:RegisterTSDiabetesResponder:1">

  <xsl:include href="transform/se-intygstjanster-ts-services/general-transform.xslt"/>

  <xsl:template name="response">
     <rmc:RegisterTSDiabetesResponse>
       <rmc:result>
         <xsl:call-template name="result"/>
       </rmc:result>
     </rmc:RegisterTSDiabetesResponse>
   </xsl:template>

</xsl:stylesheet>