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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_HEADER_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_LABEL_FROM_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_LABEL_TOM_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_TEXT_ID;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigDateRangeTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueDateRangeTest;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;

@ExtendWith(MockitoExtension.class)
class QuestionSjukskrivningsperiodTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSjukskrivningsperiod.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return SJUKSKRIVNINGSGRAD_HEADER_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigDateRangeTests extends ConfigDateRangeTest {

        @Override
        protected String getJsonId() {
            return SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID;
        }

        @Override
        protected String getFromLabel() {
            return SJUKSKRIVNINGSGRAD_PERIOD_SVAR_LABEL_FROM_ID;
        }

        @Override
        protected String getToLabel() {
            return SJUKSKRIVNINGSGRAD_PERIOD_SVAR_LABEL_TOM_ID;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSjukskrivningsperiod.toCertificate(null, 0, texts);
        }

        @Override
        protected String getTextId() {
            return SJUKSKRIVNINGSGRAD_PERIOD_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    class IncludeValueDateRangeTests extends ValueDateRangeTest {

        private final InternalLocalDateInterval dateRange = new InternalLocalDateInterval();

        @Override
        protected CertificateDataElement getElement() {
            dateRange.setFrom(new InternalDate(LocalDate.now()));
            dateRange.setTom(new InternalDate(LocalDate.now()));
            return QuestionSjukskrivningsperiod.toCertificate(dateRange, 0, texts);
        }

        @Override
        protected String getJsonId() {
            return SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID;
        }

        @Override
        protected LocalDate getFromDate() {
            return dateRange.fromAsLocalDate();
        }

        @Override
        protected LocalDate getToDate() {
            return dateRange.tomAsLocalDate();
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSjukskrivningsperiod.toCertificate(null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalDateRangeFromValueTests extends InternalValueTest<InternalLocalDateInterval, LocalDate> {

        @Override
        protected CertificateDataElement getElement(InternalLocalDateInterval input) {
            return QuestionSjukskrivningsperiod.toCertificate(input, 0, texts);
        }

        @Override
        protected LocalDate toInternalValue(Certificate certificate) {
            final var internalLocalDateInterval = QuestionSjukskrivningsperiod.toInternal(certificate);
            return internalLocalDateInterval.fromAsLocalDate();
        }

        @Override
        protected List<InputExpectedValuePair<InternalLocalDateInterval, LocalDate>> inputExpectedValuePairList() {
            final var dateRange = new InternalLocalDateInterval();
            dateRange.setFrom(new InternalDate(LocalDate.now()));

            return List.of(
                new InputExpectedValuePair<>(null, LocalDate.now()),
                new InputExpectedValuePair<>(dateRange, dateRange.fromAsLocalDate())
            );
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalDateRangeToValueTests extends InternalValueTest<InternalLocalDateInterval, LocalDate> {

        @Override
        protected CertificateDataElement getElement(InternalLocalDateInterval input) {
            return QuestionSjukskrivningsperiod.toCertificate(input, 0, texts);
        }

        @Override
        protected LocalDate toInternalValue(Certificate certificate) {
            final var internalLocalDateInterval = QuestionSjukskrivningsperiod.toInternal(certificate);
            return internalLocalDateInterval.tomAsLocalDate();
        }

        @Override
        protected List<InputExpectedValuePair<InternalLocalDateInterval, LocalDate>> inputExpectedValuePairList() {
            final var dateRange = new InternalLocalDateInterval();
            dateRange.setFrom(new InternalDate(LocalDate.now()));

            return List.of(
                new InputExpectedValuePair<>(null, null),
                new InputExpectedValuePair<>(dateRange, dateRange.tomAsLocalDate())
            );
        }
    }
}
