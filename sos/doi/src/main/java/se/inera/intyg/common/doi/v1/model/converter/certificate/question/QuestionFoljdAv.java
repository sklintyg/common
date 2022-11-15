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

package se.inera.intyg.common.doi.v1.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_AV_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_ID;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeath;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.model.InternalDate;

public class QuestionFoljdAv {

    public static final short LIMIT = (short) 80;
    public static final short NUMBER_OF_DAYS_IN_FUTURE = (short) 0;
    private static final String KRONISK = "Kronisk";
    private static final String PLOTSLIG = "Akut";
    private static final String UPPGIFT_SAKNAS = "Uppgift Saknas";
    private static final String LABEL_B = "-B";
    private static final String LABEL_C = "-C";
    private static final String LABEL_D = "-D";
    private static final Dodsorsak EMPTY_CAUSE_OF_DEATH = Dodsorsak.create("", null, null);

    //TODO remove?
    public static CertificateDataElement toCertificate(List<Dodsorsak> foljder, int index, CertificateTextProvider texts, String label) {

        final var foljd = getFoljd(foljder, label);

        return CertificateDataElement.builder()
            .id(FOLJD_OM_DELSVAR_ID + label)
            .index(index)
            .parent(DODSORSAK_DELSVAR_ID)
            .config(
                CertificateDataConfigCauseOfDeath.builder()
                    .text(texts.get(FOLJD_AV_QUESTION_TEXT_ID))
                    .label(label)
                    .causeOfDeath(
                        CauseOfDeath.builder()
                            .id(FOLJD_JSON_ID + label)
                            .descriptionId(FOLJD_OM_DELSVAR_ID + label)
                            .debutId(FOLJD_DATUM_DELSVAR_ID + label)
                            .specifications(List.of(
                                CertificateDataConfigCode.builder()
                                    .id(Specifikation.PLOTSLIG.name() + label)
                                    .label(PLOTSLIG)
                                    .code(Specifikation.PLOTSLIG.name())
                                    .build(),
                                CertificateDataConfigCode.builder()
                                    .id(Specifikation.KRONISK.name() + label)
                                    .label(KRONISK)
                                    .code(Specifikation.KRONISK.name())
                                    .build(),
                                CertificateDataConfigCode.builder()
                                    .id(Specifikation.UPPGIFT_SAKNAS.name() + label)
                                    .label(UPPGIFT_SAKNAS)
                                    .code(Specifikation.UPPGIFT_SAKNAS.name())
                                    .build()
                                ))
                            .build()
                    )
                    .build()
            )
            .value(
                CertificateDataValueCauseOfDeath.builder()
                    .id(FOLJD_JSON_ID + label)
                    .description(
                        CertificateDataTextValue.builder()
                            .id(FOLJD_OM_DELSVAR_ID + label)
                            .text(foljd.getBeskrivning())
                            .build()
                    )
                    .debut(
                        CertificateDataValueDate.builder()
                            .id(FOLJD_DATUM_DELSVAR_ID + label)
                            .date(toLocalDate(foljd.getDatum()))
                            .build()
                    )
                    .specification(
                        CertificateDataValueCode.builder()
                            .id(foljd.getSpecifikation() != null ? foljd.getSpecifikation().name() + label : null)
                            .code(foljd.getSpecifikation() != null ? foljd.getSpecifikation().name() : null)
                            .build()
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(FOLJD_OM_DELSVAR_ID + label)
                        .limit(LIMIT)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(FOLJD_DATUM_DELSVAR_ID + label)
                        .numberOfDays(NUMBER_OF_DAYS_IN_FUTURE)
                        .build()
                }
            )
            .build();
    }

    public static List<Dodsorsak> toInternal(Certificate certificate) {
        final var foljder = List.of(
            certificate.getData().get(FOLJD_OM_DELSVAR_ID + LABEL_B),
            certificate.getData().get(FOLJD_OM_DELSVAR_ID + LABEL_C),
            certificate.getData().get(FOLJD_OM_DELSVAR_ID + LABEL_D)
        );

        return foljder.stream()
            .filter(foljd -> !isEmpty((CertificateDataValueCauseOfDeath) foljd.getValue()))
            .map(foljd -> createDodsorsak((CertificateDataValueCauseOfDeath) foljd.getValue()))
            .collect(Collectors.toList());
    }

    private static Dodsorsak createDodsorsak(CertificateDataValueCauseOfDeath causeOfDeath) {
        final var description = causeOfDeath.getDescription().getText();
        final var debut = causeOfDeath.getDebut().getDate() != null ? new InternalDate(causeOfDeath.getDebut().getDate()) : null;
        final var specifikation = causeOfDeath.getSpecification().getCode() != null
            ? Specifikation.fromValue(causeOfDeath.getSpecification().getCode()) : null;

        return Dodsorsak.create(description, debut, specifikation);
    }

    private static boolean isEmpty(CertificateDataValueCauseOfDeath foljd) {
        return (foljd.getDescription().getText() == null || foljd.getDescription().getText().equals(""))
            && (foljd.getDebut().getDate() == null || foljd.getDebut().getDate().toString().equals(""))
            && (foljd.getSpecification().getCode() == null || foljd.getSpecification().getCode().equals(""));
    }

    private static LocalDate toLocalDate(InternalDate internalDate) {
        return (internalDate != null && internalDate.isValidDate()) ? internalDate.asLocalDate() : null;
    }

    private static Dodsorsak getFoljd(List<Dodsorsak> causeOfDeathList, String label) {
        switch (label) {
            case LABEL_B:
                return causeOfDeathList.size() > 0 ? causeOfDeathList.get(0) : EMPTY_CAUSE_OF_DEATH;
            case  LABEL_C:
                return causeOfDeathList.size() > 1 ? causeOfDeathList.get(1) : EMPTY_CAUSE_OF_DEATH;
            case  LABEL_D:
                return causeOfDeathList.size() > 2 ? causeOfDeathList.get(2) : EMPTY_CAUSE_OF_DEATH;
            default:
                return EMPTY_CAUSE_OF_DEATH;
        }
    }
}
