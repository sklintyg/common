package se.inera.intyg.common.af00213.v1.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import org.junit.Test;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDataBooleanValueDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDataConfigDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDataElementDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDataTextValueDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDataValidationDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateMetaDataDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateStatusDTO;

public class SerializerTest {

    @Test
    public void testSerializationAndDeserializationCommon() throws IOException {
        final var originalCertificate = createDummyCertificate();
        final var objectMapper = new ObjectMapper();
        final var serializedCertificate = objectMapper.writeValueAsString(originalCertificate);
        System.out.println(serializedCertificate);
        final var jsonParser = new JsonFactory().createParser(serializedCertificate);
        final var deserializedCertificate = objectMapper.readValue(jsonParser, CertificateDTO.class);
        System.out.println(deserializedCertificate.getData());
    }

    private CertificateDTO createDummyCertificate() {
        final CertificateMetaDataDTO metaData = new CertificateMetaDataDTO();
        metaData.setCertificateId("bed26d3e-7112-4f08-98bf-01be40e26c80");
        metaData.setCertificateType("af00213");
        metaData.setCertificateTypeVersion("1.0");
        metaData.setCertificateName("Arbetsförmedlingens medicinska utlåtande");
        metaData.setCertificateDescription("Här är intygstypens beskrivning...");
        metaData.setCertificateStatus(CertificateStatusDTO.UNSIGNED);

        final CertificateDataElementDTO funktionsnedsattningCategoryDataElement = new CertificateDataElementDTO();
        funktionsnedsattningCategoryDataElement.setId("funktionsnedsattning");
        funktionsnedsattningCategoryDataElement.setParent("");
        funktionsnedsattningCategoryDataElement.setIndex(0);
        funktionsnedsattningCategoryDataElement.setVisible(true);
        funktionsnedsattningCategoryDataElement.setReadOnly(false);

        final CertificateDataConfigDTO funktionsnedsattningCategoryDataConfig = new CertificateDataConfigDTO();
        funktionsnedsattningCategoryDataConfig.setText("Funktionsnedsättning");
        funktionsnedsattningCategoryDataConfig.setComponent("category");

        funktionsnedsattningCategoryDataElement.setConfig(funktionsnedsattningCategoryDataConfig);

        final CertificateDataElementDTO harFunktionsnedsattningDataElement = new CertificateDataElementDTO();
        harFunktionsnedsattningDataElement.setId("1");
        harFunktionsnedsattningDataElement.setParent("funktionsnedsattning");
        harFunktionsnedsattningDataElement.setIndex(1);
        harFunktionsnedsattningDataElement.setVisible(true);
        harFunktionsnedsattningDataElement.setReadOnly(false);
        harFunktionsnedsattningDataElement.setMandatory(true);

        final CertificateDataConfigDTO harFunktionsnedsattningDataConfig = new CertificateDataConfigDTO();
        harFunktionsnedsattningDataConfig.setText("Finns besvär på grund av sjukdom eller skada som medför funktionsnedsättning?");
        harFunktionsnedsattningDataConfig
            .setDescription("Med besvär avses sådant som påverkar psykiska, psykosociala eller kroppsliga funktioner.");
        harFunktionsnedsattningDataConfig.setComponent("ue-radio");

        final CertificateDataBooleanValueDTO harFunktionsnedsattningValue = new CertificateDataBooleanValueDTO();
        harFunktionsnedsattningValue.setSelectedText("Ja");
        harFunktionsnedsattningValue.setUnselectedText("Nej");

        final CertificateDataValidationDTO harFunktionsnedsattningValidation = new CertificateDataValidationDTO();
        harFunktionsnedsattningValidation.setRequired(true);
        harFunktionsnedsattningValidation.setRequiredProp("harFunktionsnedsattning");

        harFunktionsnedsattningDataElement.setConfig(harFunktionsnedsattningDataConfig);
        harFunktionsnedsattningDataElement.setValue(harFunktionsnedsattningValue);
        harFunktionsnedsattningDataElement.setValidation(harFunktionsnedsattningValidation);

        final CertificateDataElementDTO funktionsnedsattningDataElement = new CertificateDataElementDTO();
        funktionsnedsattningDataElement.setId("1.1");
        funktionsnedsattningDataElement.setParent("1");
        funktionsnedsattningDataElement.setIndex(2);
        funktionsnedsattningDataElement.setVisible(false);
        funktionsnedsattningDataElement.setReadOnly(false);
        funktionsnedsattningDataElement.setMandatory(true);

        final CertificateDataConfigDTO funktionsnedsattningDataConfig = new CertificateDataConfigDTO();
        funktionsnedsattningDataConfig
            .setText("Beskriv de funktionsnedsättningar som har observerats (undersökningsfynd). Ange, om möjligt, varaktighet.");
        funktionsnedsattningDataConfig
            .setDescription(
                "Ange de nedsättningar som har framkommit vid undersökning eller utredning.\n\nTill exempel:\nMedvetenhet, uppmärksamhet, orienteringsförmåga\nSocial interaktion, agitation\nKognitiva störningar som t ex minnessvårigheter\nStörningar på sinnesorganen som t ex syn- och hörselnedsättning, balansrubbningar\nSmärta i rörelseorganen\nRörelseinskränkning, rörelseomfång, smidighet\nUthållighet, koordination\n\nMed varaktighet menas permanent eller övergående. Ange i så fall tidsangivelse vid övergående.");
        funktionsnedsattningDataConfig.setComponent("ue-textarea");

        final CertificateDataTextValueDTO funktionsnedsattningValue = new CertificateDataTextValueDTO();
        funktionsnedsattningValue.setLimit(50);

        final CertificateDataValidationDTO funktionsnedsattningValidation = new CertificateDataValidationDTO();
        funktionsnedsattningValidation.setRequired(true);
        funktionsnedsattningValidation.setRequiredProp("harFunktionsnedsattning");
        funktionsnedsattningValidation.setHideExpression("!harFunktionsnedsattning");

        funktionsnedsattningDataElement.setConfig(funktionsnedsattningDataConfig);
        funktionsnedsattningDataElement.setValue(funktionsnedsattningValue);
        funktionsnedsattningDataElement.setValidation(funktionsnedsattningValidation);

        final CertificateDTO certificate = new CertificateDTO();
        certificate.setMetadata(metaData);
        certificate.setData(new HashMap<>());
        certificate.getData().put(funktionsnedsattningCategoryDataElement.getId(), funktionsnedsattningCategoryDataElement);
        certificate.getData().put(harFunktionsnedsattningDataElement.getId(), harFunktionsnedsattningDataElement);
        certificate.getData().put(funktionsnedsattningDataElement.getId(), funktionsnedsattningDataElement);

        return certificate;
    }
}
