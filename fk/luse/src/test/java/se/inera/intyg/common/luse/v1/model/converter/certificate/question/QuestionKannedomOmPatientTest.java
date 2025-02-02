/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.luse.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.KANNEDOM_SVAR_ID_2;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.KANNEDOM_SVAR_JSON_ID_2;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.KANNEDOM_SVAR_TEXT_ID;

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
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigDateTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueDateTest;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionKannedomOmPatientTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        when(textProvider.get(anyString())).thenReturn("test string");
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKannedomOmPatient.toCertificate(null, getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return KANNEDOM_SVAR_ID_2;
        }

        @Override
        protected String getParent() {
            return GRUNDFORMU_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 3;
        }
    }

    @Nested
    class IncludeConfigDateTest extends ConfigDateTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKannedomOmPatient.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getJsonId() {
            return KANNEDOM_SVAR_JSON_ID_2;
        }

        @Override
        protected String getTextId() {
            return KANNEDOM_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected LocalDate getMaxDate() {
            return LocalDate.now();
        }

        @Override
        protected LocalDate getMinDate() {
            return null;
        }

    }

    @Nested
    class IncludeValueDateTest extends ValueDateTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKannedomOmPatient.toCertificate(new InternalDate(LocalDate.parse("2022-12-15")), 0, textProvider);
        }

        @Override
        protected String getJsonId() {
            return KANNEDOM_SVAR_JSON_ID_2;
        }

        @Override
        protected LocalDate getDate() {
            return LocalDate.parse("2022-12-15");
        }
    }

    @Nested
    class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKannedomOmPatient.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }

        @Override
        protected String getQuestionId() {
            return KANNEDOM_SVAR_ID_2;
        }

        @Override
        protected String getExpression() {
            return "$" + KANNEDOM_SVAR_JSON_ID_2;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalDateValueTest extends InternalValueTest<InternalDate, InternalDate> {

        @Override
        protected CertificateDataElement getElement(InternalDate input) {
            return QuestionKannedomOmPatient.toCertificate(input, 0, textProvider);
        }

        @Override
        protected InternalDate toInternalValue(Certificate certificate) {
            return QuestionKannedomOmPatient.toInternal(certificate);
        }

        @Override
        protected List<InputExpectedValuePair<InternalDate, InternalDate>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(new InternalDate("2022-12-15"), new InternalDate("2022-12-15")),
                new InputExpectedValuePair<>(new InternalDate(""), null),
                new InputExpectedValuePair<>(null, null)
            );
        }
    }
}
