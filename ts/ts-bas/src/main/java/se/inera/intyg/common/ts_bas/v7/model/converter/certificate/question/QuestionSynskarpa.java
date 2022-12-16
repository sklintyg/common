/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BINOKULART_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BINOKULART_MED_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BINOKULART_UTAN_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HOGER_OGA_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HOGER_OGA_MED_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HOGER_OGA_UTAN_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KONTAKTLINSER_HOGER_OGA_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KONTAKTLINSER_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KONTAKTLINSER_VANSTER_OGA_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.MED_KORREKTION_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFUNKTIONER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UTAN_KORREKTION_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VANSTER_OGA_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VANSTER_OGA_MED_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VANSTER_OGA_UTAN_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VARDEN_FOR_SYNSKARPA_ID;
import static se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa.VisualAcuityEnum.BINOCULAR;
import static se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa.VisualAcuityEnum.LEFT_EYE;
import static se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa.VisualAcuityEnum.RIGHT_EYE;

import java.util.Map;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigVisualAcuity;
import se.inera.intyg.common.support.facade.model.config.VisualAcuity;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDouble;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueVisualAcuities;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueVisualAcuity;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v7.model.internal.Synskarpevarden;

public class QuestionSynskarpa {

    public static CertificateDataElement toCertificate(Syn syn, int index, CertificateTextProvider textProvider) {
        final var hogerOga = syn != null && syn.getHogerOga() != null ? syn.getHogerOga() : null;
        final var vansterOga = syn != null && syn.getVansterOga() != null ? syn.getVansterOga() : null;
        final var binokulart = syn != null && syn.getBinokulart() != null ? syn.getBinokulart() : null;
        return CertificateDataElement.builder()
            .id(VARDEN_FOR_SYNSKARPA_ID)
            .parent(SYNFUNKTIONER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigVisualAcuity.builder()
                    .withoutCorrectionLabel(textProvider.get(UTAN_KORREKTION_TEXT_ID))
                    .withCorrectionLabel(textProvider.get(MED_KORREKTION_TEXT_ID))
                    .contactLensesLabel(textProvider.get(KONTAKTLINSER_TEXT_ID))
                    .rightEye(
                        getRightEyeConfig(textProvider)
                    )
                    .leftEye(
                        getLeftEyeConfig(textProvider)
                    )
                    .binocular(
                        getBinocularConfig(textProvider)
                    )
                    .build()
            )
            .value(
                CertificateDataValueVisualAcuities.builder()
                    .rightEye(
                        getRightEyeValue(hogerOga)
                    )
                    .leftEye(
                        getLeftEyeValue(vansterOga)
                    )
                    .binocular(
                        getBinocularValue(binokulart)
                    )
                    .build()
            )
            .build();
    }


    private static CertificateDataValueVisualAcuity getBinocularValue(Synskarpevarden binokulart) {
        return CertificateDataValueVisualAcuity.builder()
            .withoutCorrection(
                CertificateDataValueDouble.builder()
                    .id(BINOKULART_UTAN_KORREKTION_DELSVAR_ID_8)
                    .number(
                        binokulart != null && binokulart.getUtanKorrektion() != null ? binokulart.getUtanKorrektion()
                            : null)
                    .build()
            )
            .withCorrection(
                CertificateDataValueDouble.builder()
                    .id(BINOKULART_MED_KORREKTION_DELSVAR_ID_8)
                    .number(
                        binokulart != null && binokulart.getMedKorrektion() != null ? binokulart.getMedKorrektion() : null)
                    .build()
            )
            .build();
    }

    private static CertificateDataValueVisualAcuity getLeftEyeValue(Synskarpevarden vansterOga) {
        return CertificateDataValueVisualAcuity.builder()
            .withoutCorrection(
                CertificateDataValueDouble.builder()
                    .id(VANSTER_OGA_UTAN_KORREKTION_DELSVAR_ID_8)
                    .number(
                        vansterOga != null && vansterOga.getUtanKorrektion() != null ? vansterOga.getUtanKorrektion()
                            : null)
                    .build()
            )
            .withCorrection(
                CertificateDataValueDouble.builder()
                    .id(VANSTER_OGA_MED_KORREKTION_DELSVAR_ID_8)
                    .number(
                        vansterOga != null && vansterOga.getMedKorrektion() != null ? vansterOga.getMedKorrektion() : null)
                    .build()
            )
            .binocular(
                CertificateDataValueBoolean.builder()
                    .id(KONTAKTLINSER_VANSTER_OGA_DELSVAR_ID_8)
                    .selected(
                        vansterOga != null && vansterOga.getKontaktlins() != null ? vansterOga.getKontaktlins() : null)
                    .build()
            )
            .build();
    }

    private static CertificateDataValueVisualAcuity getRightEyeValue(Synskarpevarden hogerOga) {
        return CertificateDataValueVisualAcuity.builder()
            .withoutCorrection(
                CertificateDataValueDouble.builder()
                    .id(HOGER_OGA_UTAN_KORREKTION_DELSVAR_ID_8)
                    .number(
                        hogerOga != null && hogerOga.getUtanKorrektion() != null ? hogerOga.getUtanKorrektion() : null)
                    .build()
            )
            .withCorrection(
                CertificateDataValueDouble.builder()
                    .id(HOGER_OGA_MED_KORREKTION_DELSVAR_ID_8)
                    .number(
                        hogerOga != null && hogerOga.getMedKorrektion() != null ? hogerOga.getMedKorrektion() : null)
                    .build()
            )
            .binocular(
                CertificateDataValueBoolean.builder()
                    .id(KONTAKTLINSER_HOGER_OGA_DELSVAR_ID_8)
                    .selected(
                        hogerOga != null && hogerOga.getKontaktlins() != null ? hogerOga.getKontaktlins() : null)
                    .build()
            )
            .build();
    }

    private static VisualAcuity getBinocularConfig(CertificateTextProvider textProvider) {
        return VisualAcuity.builder()
            .label(textProvider.get(BINOKULART_LABEL_ID))
            .withoutCorrectionId(BINOKULART_UTAN_KORREKTION_DELSVAR_ID_8)
            .withCorrectionId(BINOKULART_MED_KORREKTION_DELSVAR_ID_8)
            .build();
    }

    private static VisualAcuity getLeftEyeConfig(CertificateTextProvider textProvider) {
        return VisualAcuity.builder()
            .label(textProvider.get(VANSTER_OGA_LABEL_ID))
            .withoutCorrectionId(VANSTER_OGA_UTAN_KORREKTION_DELSVAR_ID_8)
            .withCorrectionId(VANSTER_OGA_MED_KORREKTION_DELSVAR_ID_8)
            .contactLensesId(KONTAKTLINSER_VANSTER_OGA_DELSVAR_ID_8)
            .build();
    }

    private static VisualAcuity getRightEyeConfig(CertificateTextProvider textProvider) {
        return VisualAcuity.builder()
            .label(textProvider.get(HOGER_OGA_LABEL_ID))
            .withoutCorrectionId(HOGER_OGA_UTAN_KORREKTION_DELSVAR_ID_8)
            .withCorrectionId(HOGER_OGA_MED_KORREKTION_DELSVAR_ID_8)
            .contactLensesId(KONTAKTLINSER_HOGER_OGA_DELSVAR_ID_8)
            .build();
    }

    public static Synskarpevarden toInternal(Certificate certificate, VisualAcuityEnum type) {
        final var value = (CertificateDataValueVisualAcuities) certificate.getData().get(VARDEN_FOR_SYNSKARPA_ID).getValue();
        if (value == null) {
            return Synskarpevarden.builder().build();
        }
        final var synskarpaMap = Map.of(
            RIGHT_EYE, value.getRightEye(),
            LEFT_EYE, value.getLeftEye(),
            BINOCULAR, value.getBinocular());

        final var visualAcuityValue = synskarpaMap.get(type);

        return Synskarpevarden.builder()
            .setUtanKorrektion(
                visualAcuityValue.getWithoutCorrection().getNumber()
            )
            .setMedKorrektion(
                visualAcuityValue.getWithCorrection().getNumber()
            )
            .setKontaktlins(
                type != BINOCULAR ? visualAcuityValue.getBinocular().getSelected() : null
            )
            .build();
    }

    public enum VisualAcuityEnum {
        RIGHT_EYE, LEFT_EYE, BINOCULAR
    }
}
