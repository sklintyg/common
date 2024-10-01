/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DESCRIPTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_KRONISK;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_PLOTSLIG;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_CATEGORY_ID;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCauseOfDeathList;
import se.inera.intyg.common.support.facade.model.config.CodeItem;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeath;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeathList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.model.InternalDate;

public class QuestionBidragandeSjukdomar {

    public static final short LIMIT = (short) 55;

    public static CertificateDataElement toCertificate(List<Dodsorsak> bidragandeSjukdomar, int index, CertificateTextProvider texts) {

        return CertificateDataElement.builder()
            .id(BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID)
            .index(index)
            .parent(TERMINAL_DODSORSAK_CATEGORY_ID)
            .config(
                CertificateDataConfigCauseOfDeathList.builder()
                    .text(texts.get(BIDRAGANDE_SJUKDOM_OM_QUESTION_TEXT_ID))
                    .description(texts.get(BIDRAGANDE_SJUKDOM_OM_DESCRIPTION_TEXT_ID))
                    .list(
                        List.of(
                            getCauseOfDeathConfig("0"),
                            getCauseOfDeathConfig("1"),
                            getCauseOfDeathConfig("2"),
                            getCauseOfDeathConfig("3"),
                            getCauseOfDeathConfig("4"),
                            getCauseOfDeathConfig("5"),
                            getCauseOfDeathConfig("6"),
                            getCauseOfDeathConfig("7")
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCauseOfDeathList.builder()
                    .list(
                        List.of(
                            getCaseOfDeathValue("0", getBidragandeSjukdom(bidragandeSjukdomar, 0)),
                            getCaseOfDeathValue("1", getBidragandeSjukdom(bidragandeSjukdomar, 1)),
                            getCaseOfDeathValue("2", getBidragandeSjukdom(bidragandeSjukdomar, 2)),
                            getCaseOfDeathValue("3", getBidragandeSjukdom(bidragandeSjukdomar, 3)),
                            getCaseOfDeathValue("4", getBidragandeSjukdom(bidragandeSjukdomar, 4)),
                            getCaseOfDeathValue("5", getBidragandeSjukdom(bidragandeSjukdomar, 5)),
                            getCaseOfDeathValue("6", getBidragandeSjukdom(bidragandeSjukdomar, 6)),
                            getCaseOfDeathValue("7", getBidragandeSjukdom(bidragandeSjukdomar, 7))
                        )
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    textLimit("0"),
                    textLimit("1"),
                    textLimit("2"),
                    textLimit("3"),
                    textLimit("4"),
                    textLimit("5"),
                    textLimit("6"),
                    textLimit("7"),
                }
            )
            .build();
    }

    private static CauseOfDeath getCauseOfDeathConfig(String id) {
        return CauseOfDeath.builder()
            .id(id)
            .descriptionId(BIDRAGANDE_SJUKDOM_JSON_ID + "[" + id + "].beskrivning")
            .debutId(BIDRAGANDE_SJUKDOM_JSON_ID + "[" + id + "].datum")
            .specifications(getSpecifications())
            .maxDate(LocalDate.now())
            .build();
    }

    private static CertificateDataValidationText textLimit(String id) {
        return CertificateDataValidationText.builder().id(id).limit(LIMIT).build();
    }

    private static Dodsorsak getBidragandeSjukdom(List<Dodsorsak> bidragandeSjukdomar, int index) {
        return bidragandeSjukdomar.size() > index ? bidragandeSjukdomar.get(index) : null;
    }

    private static CertificateDataValueCauseOfDeath getCaseOfDeathValue(String id, Dodsorsak bidragandeSjukdom) {
        return CertificateDataValueCauseOfDeath.builder()
            .id(id)
            .description(
                getDescription(bidragandeSjukdom, id)
            )
            .debut(
                getDebut(bidragandeSjukdom, id)
            )
            .specification(
                getSpecification(bidragandeSjukdom)
            )
            .build();
    }

    private static CertificateDataValueText getDescription(Dodsorsak bidragandeSjukdom, String id) {
        return CertificateDataValueText.builder()
            .id(BIDRAGANDE_SJUKDOM_JSON_ID + "[" + id + "].beskrivning")
            .text(bidragandeSjukdom != null ? bidragandeSjukdom.getBeskrivning() : null)
            .build();
    }

    private static CertificateDataValueDate getDebut(Dodsorsak bidragandeSjukdom, String id) {
        return CertificateDataValueDate.builder()
            .id(BIDRAGANDE_SJUKDOM_JSON_ID + "[" + id + "].datum")
            .date(bidragandeSjukdom != null && bidragandeSjukdom.getDatum() != null ? bidragandeSjukdom.getDatum().asLocalDate() : null)
            .build();
    }

    private static CertificateDataValueCode getSpecification(Dodsorsak bidragandeSjukdom) {
        final var name = bidragandeSjukdom != null && bidragandeSjukdom.getSpecifikation() != null
            ? bidragandeSjukdom.getSpecifikation().name() : null;
        return CertificateDataValueCode.builder()
            .id(name)
            .code(name)
            .build();
    }

    private static List<CodeItem> getSpecifications() {
        return List.of(
            CodeItem.builder()
                .id(Specifikation.PLOTSLIG.name())
                .label(FOLJD_OM_DELSVAR_PLOTSLIG)
                .code(Specifikation.PLOTSLIG.name())
                .build(),
            CodeItem.builder()
                .id(Specifikation.KRONISK.name())
                .label(FOLJD_OM_DELSVAR_KRONISK)
                .code(Specifikation.KRONISK.name())
                .build(),
            CodeItem.builder()
                .id(Specifikation.UPPGIFT_SAKNAS.name())
                .label(FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS)
                .code(Specifikation.UPPGIFT_SAKNAS.name())
                .build());
    }

    public static List<Dodsorsak> toInternal(Certificate certificate) {
        if (certificate.getData().get(BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID) == null) {
            return null;
        }

        final var causeOfDeathListValue = (CertificateDataValueCauseOfDeathList) certificate.getData().get(BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID)
            .getValue();
        final var dodsorsakList = causeOfDeathListValue.getList().stream()
            .map(causeOfDeath ->
                Dodsorsak.create(
                    causeOfDeath.getDescription().getText(),
                    getDebut(causeOfDeath),
                    getSpecification(causeOfDeath)
                )
            )
            .collect(Collectors.toList());

        removeEmptyValuesIfAtEndOfList(dodsorsakList);

        return dodsorsakList;
    }

    private static Specifikation getSpecification(CertificateDataValueCauseOfDeath causeOfDeath) {
        return causeOfDeath.getSpecification().getCode() != null && !causeOfDeath.getSpecification().getCode().isEmpty()
            ? Specifikation.fromValue(causeOfDeath.getSpecification().getCode()) : null;
    }

    private static InternalDate getDebut(CertificateDataValueCauseOfDeath causeOfDeath) {
        return causeOfDeath.getDebut().getDate() != null
            ? new InternalDate(causeOfDeath.getDebut().getDate()) : null;
    }

    private static void removeEmptyValuesIfAtEndOfList(List<Dodsorsak> dodsorsakList) {
        for (int i = dodsorsakList.size() - 1; i >= 0; i--) {
            if (hasValue(dodsorsakList.get(i))) {
                break;
            } else {
                dodsorsakList.remove(i);
            }
        }
    }

    private static boolean hasValue(Dodsorsak dodsorsak) {
        return (dodsorsak.getBeskrivning() != null && !dodsorsak.getBeskrivning().isEmpty())
            || dodsorsak.getDatum() != null || dodsorsak.getSpecifikation() != null;
    }
}
