package se.inera.intyg.common.support.facade.builder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.builder.config.CertificateDataConfigCategoryBuilder;
import se.inera.intyg.common.support.facade.builder.config.CertificateDataConfigTextAreaBuilder;
import se.inera.intyg.common.support.facade.builder.validation.CertificateDataValidationShowBuilder;
import se.inera.intyg.common.support.facade.builder.validation.CertificateDataValidationTextBuilder;
import se.inera.intyg.common.support.facade.builder.value.CertificateDataTextValueBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

public class CertificateDataElementBuilderTest {

    @Test
    public void shallCreateElementWithCategoryConfig() throws Exception {
        final var expectedId = "ID1";
        final var expectedParent = "ParentID1";
        final var expectedIndex = 1;
        final var expectedHeader = "HeaderText";
        final var expectedIcon = "IconName";
        final var expectedText = "Text";
        final var expectedDescription = "Description";

        final var actualCertificateDataElement =
            CertificateDataElementBuilder.create()
                .id(expectedId)
                .parent(expectedParent)
                .index(expectedIndex)
                .config(
                    CertificateDataConfigCategoryBuilder.create()
                        .header(expectedHeader)
                        .icon(expectedIcon)
                        .text(expectedText)
                        .description(expectedDescription)
                        .build()
                )
                .build();

        assertEquals(expectedId, actualCertificateDataElement.getId());
        assertEquals(expectedParent, actualCertificateDataElement.getParent());
        assertEquals(expectedIndex, actualCertificateDataElement.getIndex());

        assertNotNull(actualCertificateDataElement.getConfig(), () -> "Config should not be null!");
        assertEquals(expectedHeader, actualCertificateDataElement.getConfig().getHeader());
        assertEquals(expectedIcon, actualCertificateDataElement.getConfig().getIcon());
        assertEquals(expectedText, actualCertificateDataElement.getConfig().getText());
        assertEquals(expectedDescription, actualCertificateDataElement.getConfig().getDescription());
        assertEquals(CertificateDataConfigTypes.CATEGORY, actualCertificateDataElement.getConfig().getType());
    }

    @Test
    public void shallCreateElementWithTextAreaConfig() throws Exception {
        final var expectedId = "ID1";
        final var expectedTextAreaId = "TextAreaID1";
        final var expectedParent = "ParentID1";
        final var expectedIndex = 1;
        final var expectedHeader = "HeaderText";
        final var expectedIcon = "IconName";
        final var expectedText = "Text";
        final var expectedDescription = "Description";
        final var expectedTextValueId = "TextValueID1";
        final var expectedTextValue = "This is the actual text";
        final var expectedTextValidationId = "TextValidationID1";
        final var expectedLimit = (short) 500;

        final var expectedQuestionId = "ID2";
        final var expectedExpression = "Expression1";

        final var actualCertificateDataElement =
            CertificateDataElementBuilder.create()
                .id(expectedId)
                .parent(expectedParent)
                .index(expectedIndex)
                .config(
                    CertificateDataConfigTextAreaBuilder.create()
                        .header(expectedHeader)
                        .icon(expectedIcon)
                        .text(expectedText)
                        .description(expectedDescription)
                        .id(expectedTextAreaId)
                        .build()
                )
                .value(
                    CertificateDataTextValueBuilder.create()
                        .id(expectedTextValueId)
                        .text(expectedTextValue)
                        .build()
                )
                .validation(
                    new CertificateDataValidation[]{
                        CertificateDataValidationTextBuilder.create()
                            .id(expectedTextValidationId)
                            .limit(expectedLimit)
                            .build()
                        ,
                        CertificateDataValidationShowBuilder.create()
                            .questionId(expectedQuestionId)
                            .expression(expectedExpression)
                            .build()
                    }
                )
                .build();

        assertEquals(expectedId, actualCertificateDataElement.getId());
        assertEquals(expectedParent, actualCertificateDataElement.getParent());
        assertEquals(expectedIndex, actualCertificateDataElement.getIndex());

        assertNotNull(actualCertificateDataElement.getConfig(), () -> "Config should not be null!");
        assertEquals(expectedHeader, actualCertificateDataElement.getConfig().getHeader());
        assertEquals(expectedIcon, actualCertificateDataElement.getConfig().getIcon());
        assertEquals(expectedText, actualCertificateDataElement.getConfig().getText());
        assertEquals(expectedDescription, actualCertificateDataElement.getConfig().getDescription());
        assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, actualCertificateDataElement.getConfig().getType());
        assertEquals(expectedTextAreaId, ((CertificateDataConfigTextArea)actualCertificateDataElement.getConfig()).getId());

        assertEquals(CertificateDataValueType.TEXT, actualCertificateDataElement.getValue().getType());
        assertEquals(expectedTextValueId, ((CertificateDataTextValue)actualCertificateDataElement.getValue()).getId());
        assertEquals(expectedTextValue, ((CertificateDataTextValue)actualCertificateDataElement.getValue()).getText());

        System.out.println(new ObjectMapper().writeValueAsString(actualCertificateDataElement));
    }
}