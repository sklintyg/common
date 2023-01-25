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
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_GRUNDFORMU_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_DESCRIPTION_TEXT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_ANNAT_LABEL_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_JOURNALUPPGIFTER_LABEL_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_TELEFONKONTAKT_LABEL_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_UNDERSOKNING_LABEL_ID;

import java.time.LocalDate;
import java.util.Arrays;
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
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCheckboxMultipleDateTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMaxDateTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueDateListTest;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionIntygetBaseratPaTest {

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
            return QuestionIntygetBaseratPa.toCertificate(null, null, null, null, 0, texts);
        }

        @Override
        protected String getId() {
            return GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return CATEGORY_GRUNDFORMU_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigCheckboxMultipleDateTests extends ConfigCheckboxMultipleDateTest {

        @Override
        protected List<CheckboxMultipleDate> getCheckboxMultipleDates() {
            return Arrays.asList(
                CheckboxMultipleDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                    .label(GRUNDFORMU_UNDERSOKNING_LABEL_ID)
                    .build(),
                CheckboxMultipleDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                    .label(GRUNDFORMU_TELEFONKONTAKT_LABEL_ID)
                    .build(),
                CheckboxMultipleDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                    .label(GRUNDFORMU_JOURNALUPPGIFTER_LABEL_ID)
                    .build(),
                CheckboxMultipleDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                    .label(GRUNDFORMU_ANNAT_LABEL_ID)
                    .build()
            );
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionIntygetBaseratPa.toCertificate(null, null, null, null, 0, texts);
        }

        @Override
        protected String getTextId() {
            return GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return GRUNDFORMEDICINSKTUNDERLAG_SVAR_DESCRIPTION_TEXT_ID;
        }
    }

    @Nested
    class IncludeValueDateListTest extends ValueDateListTest<List<InternalDate>> {

        @Override
        protected CertificateDataElement getElement() {
            return getElement(Arrays.asList(null, null, null, null));
        }

        @Override
        protected CertificateDataElement getElement(List<InternalDate> expectedValue) {
            return QuestionIntygetBaseratPa.toCertificate(expectedValue.get(0), expectedValue.get(1), expectedValue.get(2),
                expectedValue.get(3), 0, texts);
        }

        @Override
        protected List<InputExpectedValuePair<List<InternalDate>, CertificateDataValueDateList>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(List.of(new InternalDate("2022-11-13"), new InternalDate("2022-12-14"),
                    new InternalDate("2022-12-15"), new InternalDate("2022-12-16")),
                    CertificateDataValueDateList.builder()
                        .list(List.of(
                            CertificateDataValueDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                                .date(LocalDate.parse("2022-11-13"))
                                .build(),
                            CertificateDataValueDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                                .date(LocalDate.parse("2022-12-14"))
                                .build(),
                            CertificateDataValueDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                                .date(LocalDate.parse("2022-12-15"))
                                .build(),
                            CertificateDataValueDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                                .date(LocalDate.parse("2022-12-16"))
                                .build()
                        ))
                        .build()
                ),
                new InputExpectedValuePair<>(Arrays.asList(null, new InternalDate(""), new InternalDate("2022-12-146"),
                    new InternalDate("20223-12-14")), CertificateDataValueDateList.builder().list(Collections.emptyList()).build()),
                new InputExpectedValuePair<>(Arrays.asList(null, null, null, null), CertificateDataValueDateList.builder()
                    .list(Collections.emptyList()).build())
            );
        }
    }

    @Nested
    class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionIntygetBaseratPa.toCertificate(null, null, null, null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }

        @Override
        protected String getQuestionId() {
            return GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1
                + " || " + GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1
                + " || " + GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1
                + " || " + GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
        }

    }

    @Nested
    class IncludeValidationMaxDateTestUndersokning extends ValidationMaxDateTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionIntygetBaseratPa.toCertificate(null, null, null, null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
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
    class IncludeValidationMaxDateTestAnhorigsBeskrivning extends ValidationMaxDateTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionIntygetBaseratPa.toCertificate(null, null, null, null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 2;
        }

        @Override
        protected String getId() {
            return GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
        }

        @Override
        protected short getDaysInFuture() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationMaxDateTestJournalUppgifter extends ValidationMaxDateTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionIntygetBaseratPa.toCertificate(null, null, null, null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 3;
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
    class IncludeValidationMaxDateTestAnnat extends ValidationMaxDateTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionIntygetBaseratPa.toCertificate(null, null, null, null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 4;
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
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalDateListValueTest extends InternalValueTest<List<InternalDate>, InternalDate> {

        @Override
        protected CertificateDataElement getElement(List<InternalDate> input) {
            return QuestionIntygetBaseratPa.toCertificate(input.get(0), input.get(1), input.get(2), input.get(3), 0, texts);
        }

        @Override
        protected InternalDate toInternalValue(Certificate certificate) {
            return QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1);
        }

        @Override
        protected List<InputExpectedValuePair<List<InternalDate>, InternalDate>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(Arrays.asList(null, null, null, new InternalDate("2022-12-13")),
                    new InternalDate("2022-12-13")),
                new InputExpectedValuePair<>(Arrays.asList(null, null, null, new InternalDate("")),
                    null),
                new InputExpectedValuePair<>(Arrays.asList(null, null, null, null), null)
            );
        }
    }
}
