<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:lc="urn:riv:clinicalprocess:healthcond:certificate:ListCertificatesForCitizenResponder:2">

  <xsl:include href="transform/clinicalprocess-healthcond-certificate-2/general-transform.xslt"/>

  <xsl:template name="response">
     <lc:ListCertificatesForCitizenResponse>
       <lc:intygsLista />
       <lc:result>
         <xsl:call-template name="result"/>
       </lc:result>
     </lc:ListCertificatesForCitizenResponse>
   </xsl:template>

</xsl:stylesheet>
