package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_42;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionArbetspaverkan {

    public static CertificateDataElement toCertificate(String arbetspaverkan, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETETS_PAVERKAN_DELSVAR_ID_42)
            .index(index)
            .parent(ARBETETS_PAVERKAN_DELSVAR_ID_41)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(ARBETETS_PAVERKAN_DELSVAR_TEXT))
                    .description(texts.get(ARBETETS_PAVERKAN_DELSVAR_DESCRIPTION))
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_42)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_42)
                    .text(arbetspaverkan)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETETS_PAVERKAN_DELSVAR_ID_42)
                        .expression(singleExpression(ARBETETS_PAVERKAN_SVAR_JSON_ID_42))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(ARBETETS_PAVERKAN_DELSVAR_ID_41)
                        .expression(singleExpression(ARBETETS_PAVERKAN_SVAR_JSON_ID_41))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), ARBETETS_PAVERKAN_DELSVAR_ID_42, ARBETETS_PAVERKAN_SVAR_JSON_ID_42);
    }
}
