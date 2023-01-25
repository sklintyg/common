/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.fkparent.model.converter.certificate;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSES_LIST_ITEM_1_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSES_LIST_ITEM_2_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSES_LIST_ITEM_3_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_ICD_10_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_ICD_10_LABEL;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_KSH_97_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_KSH_97_LABEL;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.withCitation;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.diagnosisListValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.common.support.facade.model.config.DiagnosesListItem;
import se.inera.intyg.common.support.facade.model.config.DiagnosesTerminology;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosis;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

public abstract class AbstractQuestionDiagnoser {

    public static final short LIMIT_DIAGNOSIS_DESC = (short) 81;

    protected static CertificateDataElement toCertificate(List<Diagnos> diagnoser, String questionId, String parentId, String textId,
        String descriptionId, List<CertificateDataValidation> additionalValidations, int index,
        CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(questionId)
            .parent(parentId)
            .index(index)
            .config(
                CertificateDataConfigDiagnoses.builder()
                    .text(textProvider.get(textId))
                    .description(descriptionId != null ? textProvider.get(descriptionId) : null)
                    .terminology(
                        List.of(
                            DiagnosesTerminology.builder()
                                .id(DIAGNOS_ICD_10_ID)
                                .label(DIAGNOS_ICD_10_LABEL)
                                .build(),
                            DiagnosesTerminology.builder()
                                .id(DIAGNOS_KSH_97_ID)
                                .label(DIAGNOS_KSH_97_LABEL)
                                .build()
                        )
                    )
                    .list(
                        List.of(
                            DiagnosesListItem.builder()
                                .id(DIAGNOSES_LIST_ITEM_1_ID)
                                .build(),
                            DiagnosesListItem.builder()
                                .id(DIAGNOSES_LIST_ITEM_2_ID)
                                .build(),
                            DiagnosesListItem.builder()
                                .id(DIAGNOSES_LIST_ITEM_3_ID)
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueDiagnosisList.builder()
                    .list(createDiagnosValue(diagnoser))
                    .build()
            )
            .validation(
                getValidations(additionalValidations, questionId)
            )
            .build();
    }

    private static CertificateDataValidation[] getValidations(List<CertificateDataValidation> additionalValidations, String questionId) {
        final var certificateDataValidationsList = new ArrayList<CertificateDataValidation>();

        certificateDataValidationsList.add(
            CertificateDataValidationMandatory.builder()
                .questionId(questionId)
                .expression(withCitation(singleExpression(DIAGNOSES_LIST_ITEM_1_ID)))
                .build());
        certificateDataValidationsList.add(
            CertificateDataValidationText.builder()
                .limit(LIMIT_DIAGNOSIS_DESC)
                .build());

        if (additionalValidations != null) {
            certificateDataValidationsList.addAll(
                additionalValidations
            );
        }

        final var certificateDataValidationsArray = new CertificateDataValidation[certificateDataValidationsList.size()];
        return certificateDataValidationsList.toArray(certificateDataValidationsArray);
    }

    private static List<CertificateDataValueDiagnosis> createDiagnosValue(List<Diagnos> diagnoses) {
        if (diagnoses == null) {
            return Collections.emptyList();
        }

        final List<CertificateDataValueDiagnosis> newDiagnoses = new ArrayList<>();
        for (int i = 0; i < diagnoses.size(); i++) {
            final var diagnosis = diagnoses.get(i);
            if (diagnosis.getDiagnosKod() == null) {
                continue;
            }

            newDiagnoses.add(createDiagnosis(Integer.toString(i + 1), diagnosis));
        }

        return newDiagnoses;
    }

    private static CertificateDataValueDiagnosis createDiagnosis(String id, Diagnos diagnos) {
        return CertificateDataValueDiagnosis.builder()
            .id(convertIntToId(id))
            .terminology(diagnos.getDiagnosKodSystem())
            .code(diagnos.getDiagnosKod())
            .description(diagnos.getDiagnosBeskrivning())
            .build();
    }

    protected static List<Diagnos> toInternal(Certificate certificate, String questionId, WebcertModuleService moduleService) {
        var diagnosisList = diagnosisListValue(certificate.getData(), questionId);
        int maxDiagnosIndex = getMaxDiagnosIndex(diagnosisList);

        List<Diagnos> newDiagnosisList = new ArrayList<>();
        while (newDiagnosisList.size() < maxDiagnosIndex) {
            newDiagnosisList.add(Diagnos.create(null, null, null, null));
        }

        diagnosisList.forEach(diagnosis -> {
            var newDiagnosis = Diagnos.create(
                diagnosis.getCode(),
                diagnosis.getTerminology(),
                diagnosis.getDescription(),
                moduleService.getDescriptionFromDiagnosKod(diagnosis.getCode(), diagnosis.getTerminology()));
            newDiagnosisList.set(convertIdToInt(diagnosis.getId()), newDiagnosis);
        });
        return newDiagnosisList;
    }

    private static int getMaxDiagnosIndex(List<CertificateDataValueDiagnosis> diagnosisList) {
        int value = 0;
        for (CertificateDataValueDiagnosis diagnosis : diagnosisList) {
            int diagnosisIndex = convertIdToInt(diagnosis.getId()) + 1;
            if (diagnosisIndex > value) {
                value = diagnosisIndex;
            }
        }
        return value;
    }

    private static int convertIdToInt(String id) {
        switch (id) {
            case DIAGNOSES_LIST_ITEM_1_ID:
                return 0;
            case DIAGNOSES_LIST_ITEM_2_ID:
                return 1;
            case DIAGNOSES_LIST_ITEM_3_ID:
                return 2;
            default:
                return 0;
        }
    }

    private static String convertIntToId(String id) {
        switch (id) {
            case "1":
                return DIAGNOSES_LIST_ITEM_1_ID;
            case "2":
                return DIAGNOSES_LIST_ITEM_2_ID;
            case "3":
                return DIAGNOSES_LIST_ITEM_3_ID;
            default:
                return "";
        }
    }
}
