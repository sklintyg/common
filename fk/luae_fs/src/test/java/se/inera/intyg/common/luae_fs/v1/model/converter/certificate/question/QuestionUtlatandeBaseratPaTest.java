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

package se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMU_ANNAT_LABEL;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMU_JOURNALUPPGIFTER_LABEL;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMU_UNDERSOKNING_LABEL;

import java.time.LocalDate;
import java.util.Collections;
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
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCheckboxMultipleDateTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMaxDateTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalDateListValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueDateListTest;
import se.inera.intyg.common.support.model.InternalDate;


@ExtendWith(MockitoExtension.class)
class QuestionUtlatandeBaseratPaTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        doReturn("Text!").when(textProvider).get(anyString());
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUtlatandeBaseratPa.toCertificate(null,null,null, null, getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
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
    class IncludeConfigCheckboxMultipleDateTest extends ConfigCheckboxMultipleDateTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUtlatandeBaseratPa.toCertificate(null, null, null, null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected List<CheckboxMultipleDate> getCheckboxMultipleDates() {
            return List.of(
                CheckboxMultipleDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                    .label(GRUNDFORMU_UNDERSOKNING_LABEL)
                    .build(),
                CheckboxMultipleDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                    .label(GRUNDFORMU_JOURNALUPPGIFTER_LABEL)
                    .build(),
                CheckboxMultipleDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1)
                    .label(GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL)
                    .build(),
                CheckboxMultipleDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                    .label(GRUNDFORMU_ANNAT_LABEL)
                    .build()
            );
        }
    }

    @Nested
    class IncludeValueDateListTest {

        @Nested
        class ValidDatesTest extends ValueDateListTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionUtlatandeBaseratPa.toCertificate(new InternalDate("2022-11-13"), new InternalDate("2022-12-14"),
                    new InternalDate("2022-12-15"), new InternalDate("2022-12-16"), 0, textProvider);
            }

            @Override
            protected List<CertificateDataValueDate> getDateValues() {
                return List.of(
                    CertificateDataValueDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                        .date(LocalDate.parse("2022-11-13"))
                        .build(),
                    CertificateDataValueDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                        .date(LocalDate.parse("2022-12-14"))
                        .build(),
                    CertificateDataValueDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1)
                        .date(LocalDate.parse("2022-12-15"))
                        .build(),
                    CertificateDataValueDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                        .date(LocalDate.parse("2022-12-16"))
                        .build()
                );
            }
        }

        @Nested
        class InvalidDatesTest extends ValueDateListTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionUtlatandeBaseratPa.toCertificate(null, new InternalDate(""), new InternalDate("2022-12-146"),
                    new InternalDate("20223-12-14"), 0, textProvider);
            }

            @Override
            protected List<CertificateDataValueDate> getDateValues() {
                return Collections.emptyList();
            }
        }
    }

    @Nested
    class IncludeValidationMaxDateTestIndex0 extends ValidationMaxDateTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUtlatandeBaseratPa.toCertificate(null, null, null, null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }

        @Override
        protected String getId() {
            return GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
        }

        @Override
        protected short getDaysInFuture() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationMaxDateTestIndex1 extends ValidationMaxDateTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUtlatandeBaseratPa.toCertificate(null, null, null, null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }

        @Override
        protected String getId() {
            return GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
        }

        @Override
        protected short getDaysInFuture() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationMaxDateTestIndex2 extends ValidationMaxDateTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUtlatandeBaseratPa.toCertificate(null, null, null, null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 2;
        }

        @Override
        protected String getId() {
            return GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
        }

        @Override
        protected short getDaysInFuture() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationMaxDateTestIndex3 extends ValidationMaxDateTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUtlatandeBaseratPa.toCertificate(null, null, null, null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 3;
        }

        @Override
        protected String getId() {
            return GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
        }

        @Override
        protected short getDaysInFuture() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUtlatandeBaseratPa.toCertificate(null, null, null, null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 4;
        }

        @Override
        protected String getQuestionId() {
            return GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
        }

        @Override
        protected String getExpression() {
            return GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1
                + " || " + GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1
                + " || " + GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1
                + " || " + GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalDateListValueTest extends InternalDateListValueTest {

        @Override
        protected CertificateDataElement getElement(InternalDate expectedValue) {
            return QuestionUtlatandeBaseratPa.toCertificate(new InternalDate("2022-12-13"), null, null, expectedValue, 0, textProvider);
        }

        @Override
        protected InternalDate toInternalDateListValue(Certificate certificate) {
            return QuestionUtlatandeBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1);
        }
    }

}