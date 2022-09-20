package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ANSWER_NO;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_QUESTION_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_QUESTION_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public class QuestionHarUtredningBehandling {

    public static CertificateDataElement toCertificate(Boolean harUtredningBehandling, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(UTREDNING_BEHANDLING_DELSVAR_ID_31)
            .index(index)
            .parent(UTREDNING_BEHANDLING_CATEGORY_ID)
            .config(
                CertificateDataConfigBoolean.builder()
                    .text(texts.get(UTREDNING_BEHANDLING_QUESTION_TEXT))
                    .description(texts.get(UTREDNING_BEHANDLING_QUESTION_DESCRIPTION))
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_31)
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_31)
                    .selected(harUtredningBehandling)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(UTREDNING_BEHANDLING_DELSVAR_ID_31)
                        .expression(singleExpression(UTREDNING_BEHANDLING_SVAR_JSON_ID_31))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), UTREDNING_BEHANDLING_DELSVAR_ID_31, UTREDNING_BEHANDLING_SVAR_JSON_ID_31);
    }
}
