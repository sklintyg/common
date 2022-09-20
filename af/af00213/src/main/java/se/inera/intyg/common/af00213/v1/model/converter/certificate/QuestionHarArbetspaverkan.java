package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ANSWER_NO;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_QUESTION_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_QUESTION_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_41;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public class QuestionHarArbetspaverkan {

    public static CertificateDataElement toCertificate(Boolean harArbetspaverkan, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETETS_PAVERKAN_DELSVAR_ID_41)
            .index(index)
            .parent(ARBETETS_PAVERKAN_CATEGORY_ID)
            .config(
                CertificateDataConfigBoolean.builder()
                    .text(texts.get(ARBETETS_PAVERKAN_QUESTION_TEXT))
                    .description(texts.get(ARBETETS_PAVERKAN_QUESTION_DESCRIPTION))
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_41)
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_41)
                    .selected(harArbetspaverkan)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETETS_PAVERKAN_DELSVAR_ID_41)
                        .expression(singleExpression(ARBETETS_PAVERKAN_SVAR_JSON_ID_41))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), ARBETETS_PAVERKAN_DELSVAR_ID_41, ARBETETS_PAVERKAN_SVAR_JSON_ID_41);
    }
}
