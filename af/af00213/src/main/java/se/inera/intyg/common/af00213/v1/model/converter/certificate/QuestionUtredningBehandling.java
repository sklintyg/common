package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;
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

public class QuestionUtredningBehandling {

    public static CertificateDataElement toCertificate(String utredningBehandling, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(UTREDNING_BEHANDLING_DELSVAR_ID_32)
            .index(index)
            .parent(UTREDNING_BEHANDLING_DELSVAR_ID_31)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(UTREDNING_BEHANDLING_DELSVAR_TEXT))
                    .description(texts.get(UTREDNING_BEHANDLING_DELSVAR_DESCRIPTION))
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_32)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_32)
                    .text(utredningBehandling)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(UTREDNING_BEHANDLING_DELSVAR_ID_32)
                        .expression(singleExpression(UTREDNING_BEHANDLING_SVAR_JSON_ID_32))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(UTREDNING_BEHANDLING_DELSVAR_ID_31)
                        .expression(singleExpression(UTREDNING_BEHANDLING_SVAR_JSON_ID_31))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), UTREDNING_BEHANDLING_DELSVAR_ID_32, UTREDNING_BEHANDLING_SVAR_JSON_ID_32);
    }
}
