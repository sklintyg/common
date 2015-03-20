<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gtb="urn:local:se:intygstjanster:services:GetTSBasResponder:1">

  <xsl:include href="transform/se-intygstjanster-ts-services/general-transform.xslt"/>

  <xsl:template name="response">
     <gtb:GetTSBasResponse>
       <gtb:result>
         <xsl:call-template name="result"/>
       </gtb:result>
     </gtb:GetTSBasResponse>
   </xsl:template>

</xsl:stylesheet>