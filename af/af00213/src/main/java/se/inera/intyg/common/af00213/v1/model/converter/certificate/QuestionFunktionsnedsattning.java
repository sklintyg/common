package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12;
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

public class QuestionFunktionsnedsattning {

    public static CertificateDataElement toCertificate(String funktionsnedsattning, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_DELSVAR_ID_12)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_TEXT))
                    .description(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_DESCRIPTION))
                    .id(FUNKTIONSNEDSATTNING_CATEGORY_ID)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12)
                    .text(funktionsnedsattning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_12)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), FUNKTIONSNEDSATTNING_DELSVAR_ID_12, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12);
    }
}
