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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_EN_FJARDEDEL;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_HALFTEN;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_HELT_NEDSATT;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_BESKRIVNING;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_TEXT;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_TRE_FJARDEDEL;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.dateRangeListValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.getInternalLocalDateInterval;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxDateRangeList;
import se.inera.intyg.common.support.facade.model.config.CheckboxDateRange;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;

public abstract class AbstractQuestionBehovAvSjukskrivning {

    public static CertificateDataElement toCertificate(QuestionBehovAvSjukskrivningConfigProvider configProvider, String questionId,
        String parent, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(questionId)
            .index(index)
            .parent(parent)
            .config(
                CertificateDataConfigCheckboxDateRangeList.builder()
                    .text(texts.get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_TEXT))
                    .description(texts.get(BEHOV_AV_SJUKSKRIVNING_SVAR_BESKRIVNING))
                    .list(
                        List.of(
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_1_4.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_EN_FJARDEDEL))
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_HALFTEN.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_HALFTEN))
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_3_4.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_TRE_FJARDEDEL))
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.HELT_NEDSATT.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_HELT_NEDSATT))
                                .build()
                        )
                    )
                    .previousDateRangeText(
                        hasRenewalRelation(configProvider.getRenewalRelation()) ? getPreviousDateRangeText(
                            configProvider.getSickLeaveText(),
                            configProvider.getExpirationalDate()
                        ) : null
                    )
                    .build()
            )
            .value(
                CertificateDataValueDateRangeList.builder()
                    .list(createSjukskrivningValue(configProvider.getValues()))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(questionId)
                        .expression(multipleOrExpression(
                            singleExpression(SjukskrivningsGrad.NEDSATT_1_4.getId()),
                            singleExpression(SjukskrivningsGrad.NEDSATT_HALFTEN.getId()),
                            singleExpression(SjukskrivningsGrad.NEDSATT_3_4.getId()),
                            singleExpression(SjukskrivningsGrad.HELT_NEDSATT.getId())
                        ))
                        .build()
                }
            )
            .build();
    }

    private static String getPreviousDateRangeText(String sickLeaveText, LocalDate expirationalDate) {
        if (expirationalDate == null) {
            return null;
        }

        return String.format(
            "På det ursprungliga intyget var slutdatumet för den sista sjukskrivningsperioden %s och sjukskrivningsgraden var %s.",
            DateTimeFormatter.ofPattern("yyyy-MM-dd").format(expirationalDate),
            sickLeaveText
        );
    }

    private static boolean hasRenewalRelation(RelationKod relation) {
        return relation == RelationKod.FRLANG;
    }

    private static List<CertificateDataValueDateRange> createSjukskrivningValue(List<SjukskrivningValue> sickLeaves) {
        if (sickLeaves == null) {
            return Collections.emptyList();
        }
        return sickLeaves.stream()
            .filter(item -> item.getPeriod() != null && item.getPeriod().isValid())
            .map(item -> CertificateDataValueDateRange.builder()
                .id(Objects.requireNonNull(item.getId()))
                .to(Objects.requireNonNull(item.getPeriod()).getTom().asLocalDate())
                .from(item.getPeriod().getFrom().asLocalDate())
                .build()
            ).collect(Collectors.toList());
    }

    public static List<Sjukskrivning> toInternal(Certificate certificate, String questionId) {
        var list = dateRangeListValue(certificate.getData(), questionId);

        return list.stream().map(
            AbstractQuestionBehovAvSjukskrivning::getSjukskrivning).collect(Collectors.toList());
    }

    private static Sjukskrivning getSjukskrivning(CertificateDataValueDateRange item) {
        return Sjukskrivning.create(SjukskrivningsGrad.fromId(item.getId()), getInternalLocalDateInterval(item));
    }

    public static class QuestionBehovAvSjukskrivningConfigProvider {

        private final RelationKod renewalRelation;
        private final LocalDate expirationalDate;
        private final String sickLeaveText;
        private final List<SjukskrivningValue> values;

        public QuestionBehovAvSjukskrivningConfigProvider(RelationKod renewalRelation,
            LocalDate expirationalDate, String sickLeaveText, List<SjukskrivningValue> values) {
            this.renewalRelation = renewalRelation;
            this.expirationalDate = expirationalDate;
            this.sickLeaveText = sickLeaveText;
            this.values = values;
        }

        public RelationKod getRenewalRelation() {
            return renewalRelation;
        }

        public LocalDate getExpirationalDate() {
            return expirationalDate;
        }

        public String getSickLeaveText() {
            return sickLeaveText;
        }

        public List<SjukskrivningValue> getValues() {
            return values;
        }
    }

    public static class SjukskrivningValue {

        private final String id;
        private final InternalLocalDateInterval period;

        public SjukskrivningValue(InternalLocalDateInterval period, String id) {
            this.period = period;
            this.id = id;
        }

        public String getId() {
            return id;
        }


        public InternalLocalDateInterval getPeriod() {
            return period;
        }
    }
}
