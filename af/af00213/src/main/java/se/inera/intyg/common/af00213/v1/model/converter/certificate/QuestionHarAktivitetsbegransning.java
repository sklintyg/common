package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_QUESTION_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_QUESTION_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ANSWER_NO;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public class QuestionHarAktivitetsbegransning {
    public static CertificateDataElement toCertificate(Boolean harAktivitetsbegransning, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
            .parent(AKTIVITETSBEGRANSNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigBoolean.builder()
                    .text(texts.get(AKTIVITETSBEGRANSNING_QUESTION_TEXT))
                    .description(texts.get(AKTIVITETSBEGRANSNING_QUESTION_DESCRIPTION))
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21)
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21)
                    .selected(harAktivitetsbegransning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
                        .expression(singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), AKTIVITETSBEGRANSNING_DELSVAR_ID_21, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21);
    }
}
