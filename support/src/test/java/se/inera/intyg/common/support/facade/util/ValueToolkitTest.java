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
package se.inera.intyg.common.support.facade.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.grundData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.Patient;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.facade.model.value.CertificateDataIcfValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataUncertainDateValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosis;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;

class ValueToolkitTest {

    private static final String QUESTION_ID_1 = "1.1";
    private static final String QUESTION_ID_2 = "1.2";
    private static final String VALUE_ID_1 = "2.1";
    private static final String VALUE_ID_2 = "2.2";
    private static final String WRONG_VALUE_ID = "Wrong value id";
    public static final String CURRENT_ZIP_CODE = "Current zip code";
    public static final String CURRENT_CITY = "Current city";
    public static final String CURRENT_STREET_ADDRESS = "Current street address";

    private Map<String, CertificateDataElement> data;
    private CertificateDataElement certificateDataElement1;
    private CertificateDataElement certificateDataElement2;

    @BeforeEach
    void setUp() {
        data = new HashMap<>();
    }

    @Nested
    class BooleanTest {

        @Test
        void booleanValueTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataValueBoolean.builder().id(VALUE_ID_1).selected(true).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataValueBoolean.builder().id(VALUE_ID_2).selected(false).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.booleanValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertTrue(response);
        }

        @Test
        void booleanValueNotInstanceofTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataTextValue.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataValueBoolean.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.booleanValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void booleanValueWrongValueIdTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataValueBoolean.builder().id(WRONG_VALUE_ID).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataValueBoolean.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.booleanValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }
    }

    @Nested
    class Text {

        @Test
        void textValueTest() {
            String text = "text 1";
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataTextValue.builder().id(VALUE_ID_1).text(text).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataTextValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.textValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertEquals(text, response);
        }

        @Test
        void textValueNotInstanceofTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataValueBoolean.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataTextValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.textValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void textValueWrongValueIdTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataTextValue.builder().id(WRONG_VALUE_ID).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataTextValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.textValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void shouldReturnNullIfGivenEmptyString() {
            CertificateDataElement certificateDataElement = CertificateDataElement.builder()
                .id(QUESTION_ID_1)
                .value(
                    CertificateDataTextValue.builder()
                        .id(VALUE_ID_1)
                        .text("")
                        .build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement);
            var response = ValueToolkit.textValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void shouldReturnNullIfGivenStringIsNull() {
            CertificateDataElement certificateDataElement = CertificateDataElement.builder()
                .id(QUESTION_ID_1)
                .value(
                    CertificateDataTextValue.builder()
                        .id(VALUE_ID_1)
                        .build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement);
            var response = ValueToolkit.textValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }
    }

    @Nested
    class UncertainDate {

        @Test
        void uncertainDateValueTest() {
            String text = "text 1";
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataUncertainDateValue.builder().id(VALUE_ID_1).value(text).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataUncertainDateValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.uncertainDateValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertEquals(text, response);
        }

        @Test
        void uncertainDateValueNotInstanceofTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataTextValue.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataUncertainDateValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.uncertainDateValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void uncertainDateValueWrongValueIdTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataUncertainDateValue.builder().id(WRONG_VALUE_ID).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataUncertainDateValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.uncertainDateValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }
    }

    @Nested
    class IcfText {

        @Test
        void icfTextValueTest() {
            String text = "text 1";
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_1).text(text).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.icfTextValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertEquals(text, response);
        }

        @Test
        void icfTextValueNotInstanceofTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataUncertainDateValue.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.icfTextValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void icfTextValueWrongValueIdTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataIcfValue.builder().id(WRONG_VALUE_ID).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.icfTextValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }
    }

    @Nested
    class IcfCode {

        @Test
        void icfCodeValueTest() {
            final var text = "text 1";
            final var list = List.of(text);
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_1).icfCodes(list).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.icfCodeValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertEquals(list, response);
        }

        @Test
        void icfCodeValueNotInstanceofTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataUncertainDateValue.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.icfCodeValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void icfCodeValueWrongValueIdTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataIcfValue.builder().id(WRONG_VALUE_ID).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.icfCodeValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }
    }

    @Nested
    class DateList {

        @Test
        void dateListValueTest() {
            CertificateDataValueDate certificateDataValueDate1 =
                CertificateDataValueDate.builder().id(VALUE_ID_1).date(LocalDate.now()).build();
            CertificateDataValueDate certificateDataValueDate2 =
                CertificateDataValueDate.builder().id(VALUE_ID_2).date(LocalDate.now().plusDays(1)).build();
            final var list1 = List.of(certificateDataValueDate1);
            final var list2 = List.of(certificateDataValueDate2);

            certificateDataElement1 = CertificateDataElement.builder().id(VALUE_ID_1)
                .value(CertificateDataValueDateList.builder().list(list1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(VALUE_ID_2)
                .value(CertificateDataValueDateList.builder().list(list2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.dateListValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertEquals(certificateDataValueDate1.getDate(), response);
        }

        @Test
        void dateListValueNotInstanceofTest() {
            CertificateDataValueDate certificateDataValueDate2 =
                CertificateDataValueDate.builder().id(VALUE_ID_2).date(LocalDate.now().plusDays(1)).build();
            final var list2 = List.of(certificateDataValueDate2);

            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(VALUE_ID_2)
                .value(CertificateDataValueDateList.builder().list(list2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.dateListValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void dateListValueWrongValueIdTest() {
            CertificateDataValueDate certificateDataValueDate1 =
                CertificateDataValueDate.builder().id(WRONG_VALUE_ID).date(LocalDate.now()).build();
            CertificateDataValueDate certificateDataValueDate2 =
                CertificateDataValueDate.builder().id(VALUE_ID_2).date(LocalDate.now().plusDays(1)).build();
            final var list1 = List.of(certificateDataValueDate1);
            final var list2 = List.of(certificateDataValueDate2);

            certificateDataElement1 = CertificateDataElement.builder().id(VALUE_ID_1)
                .value(CertificateDataValueDateList.builder().list(list1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(VALUE_ID_2)
                .value(CertificateDataValueDateList.builder().list(list2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.dateListValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }
    }

    @Nested
    class Date {

        @Test
        void dateValueTest() {
            LocalDate date = LocalDate.now();
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataValueDate.builder().id(VALUE_ID_1).date(date).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataValueDate.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.dateValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertEquals(date, response);
        }

        @Test
        void dateValueNotInstanceofTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataUncertainDateValue.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataValueDate.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.dateValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void dateMissingCertificateDataElementTest() {
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataValueDate.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.dateValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void dateValueIdNullTest() {
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataValueDate.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.dateValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

        @Test
        void dateValueWrongValueIdTest() {
            LocalDate date = LocalDate.now();
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataValueDate.builder().id(WRONG_VALUE_ID).date(date).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataValueDate.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.dateValue(data, QUESTION_ID_1, VALUE_ID_1);
            assertNull(response);
        }

    }

    @Nested
    class Code {

        @Test
        void codeValueTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataValueCode.builder().id(VALUE_ID_1).code("ok 1").build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataValueCode.builder().id(VALUE_ID_2).code("ok 2").build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.codeValue(data, QUESTION_ID_1);
            assertEquals("ok 1", response);
        }

        @Test
        void codeValueNotInstanceofTest() {
            certificateDataElement1 = CertificateDataElement.builder().id(QUESTION_ID_1)
                .value(CertificateDataTextValue.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder().id(QUESTION_ID_2)
                .value(CertificateDataValueCode.builder().id(VALUE_ID_2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.codeValue(data, QUESTION_ID_1);
            assertNull(response);
        }
    }

    @Nested
    class CodeList {

        @Test
        void codeListValueTest() {
            CertificateDataValueCode certificateDataValueDate1 =
                CertificateDataValueCode.builder().id(VALUE_ID_1).code("code 1").build();
            CertificateDataValueCode certificateDataValueDate2 =
                CertificateDataValueCode.builder().id(VALUE_ID_2).code("code 2").build();
            final var list1 = List.of(certificateDataValueDate1);
            final var list2 = List.of(certificateDataValueDate2);

            certificateDataElement1 = CertificateDataElement.builder()
                .value(CertificateDataValueCodeList.builder().list(list1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder()
                .value(CertificateDataValueCodeList.builder().list(list2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.codeListValue(data, QUESTION_ID_1);
            assertEquals(list1, response);
        }

        @Test
        void codeListValueNotInstanceofTest() {
            CertificateDataValueCode certificateDataValueDate2 =
                CertificateDataValueCode.builder().id(VALUE_ID_2).code("code 2").build();
            final var list2 = List.of(certificateDataValueDate2);

            certificateDataElement1 = CertificateDataElement.builder()
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder()
                .value(CertificateDataValueCodeList.builder().list(list2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.codeListValue(data, QUESTION_ID_1);
            assertTrue(response.isEmpty());
        }
    }

    @Nested
    class DiagnosisList {

        @Test
        void diagnosisListValueTest() {
            CertificateDataValueDiagnosis certificateDataValueDate1 =
                CertificateDataValueDiagnosis.builder().id(VALUE_ID_1).code("code 1").build();
            CertificateDataValueDiagnosis certificateDataValueDate2 =
                CertificateDataValueDiagnosis.builder().id(VALUE_ID_2).code("code 2").build();
            final var list1 = List.of(certificateDataValueDate1);
            final var list2 = List.of(certificateDataValueDate2);

            certificateDataElement1 = CertificateDataElement.builder()
                .value(CertificateDataValueDiagnosisList.builder().list(list1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder()
                .value(CertificateDataValueDiagnosisList.builder().list(list2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.diagnosisListValue(data, QUESTION_ID_1);
            assertEquals(list1, response);
        }

        @Test
        void diagnosisListValueNotInstanceofTest() {
            CertificateDataValueDiagnosis certificateDataValueDate2 =
                CertificateDataValueDiagnosis.builder().id(VALUE_ID_2).code("code 2").build();
            final var list2 = List.of(certificateDataValueDate2);

            certificateDataElement1 = CertificateDataElement.builder()
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder()
                .value(CertificateDataValueDiagnosisList.builder().list(list2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.diagnosisListValue(data, QUESTION_ID_1);
            assertTrue(response.isEmpty());
        }
    }

    @Nested
    class DateRangeList {

        @Test
        void dateRangeListValueTest() {
            CertificateDataValueDateRange certificateDataValueDate1 =
                CertificateDataValueDateRange.builder().id(VALUE_ID_1).build();
            CertificateDataValueDateRange certificateDataValueDate2 =
                CertificateDataValueDateRange.builder().id(VALUE_ID_2).build();
            final var list1 = List.of(certificateDataValueDate1);
            final var list2 = List.of(certificateDataValueDate2);

            certificateDataElement1 = CertificateDataElement.builder()
                .value(CertificateDataValueDateRangeList.builder().list(list1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder()
                .value(CertificateDataValueDateRangeList.builder().list(list2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.dateRangeListValue(data, QUESTION_ID_1);
            assertEquals(list1, response);
        }

        @Test
        void dateRangeListValueNotInstanceofTest() {
            CertificateDataValueDateRange certificateDataValueDate2 =
                CertificateDataValueDateRange.builder().id(VALUE_ID_2).build();
            final var list2 = List.of(certificateDataValueDate2);

            certificateDataElement1 = CertificateDataElement.builder()
                .value(CertificateDataIcfValue.builder().id(VALUE_ID_1).build())
                .build();
            certificateDataElement2 = CertificateDataElement.builder()
                .value(CertificateDataValueDateRangeList.builder().list(list2).build())
                .build();
            data.put(QUESTION_ID_1, certificateDataElement1);
            data.put(QUESTION_ID_2, certificateDataElement2);

            var response = ValueToolkit.dateRangeListValue(data, QUESTION_ID_1);
            assertTrue(response.isEmpty());
        }
    }

    @Nested
    class GrundDataVardenhet {

        private se.inera.intyg.common.support.model.common.internal.GrundData grundData;
        private CertificateMetadata metadata;
        private HoSPersonal hoSPersonal;
        private Vardenhet vardenhet = new Vardenhet();

        @BeforeEach
        public void setup() {
            grundData = spy(new GrundData());
            grundData.setPatient(new se.inera.intyg.common.support.model.common.internal.Patient());
            hoSPersonal = spy(new HoSPersonal());
            vardenhet = new Vardenhet();
        }

        @Test
        void grundDataTest() {
            Unit unit = Unit.builder().address("adress").city("stad").zipCode("12345").phoneNumber("123456789").build();
            hoSPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hoSPersonal);
            metadata = CertificateMetadata.builder()
                .unit(unit)
                .patient(Patient.builder().build())
                .build();

            var result = grundData(metadata, grundData);

            verify(grundData, times(2)).getSkapadAv();
            verify(hoSPersonal, times(2)).getVardenhet();

            assertEquals(result.getSkapadAv(), grundData.getSkapadAv());
            assertEquals(result.getSkapadAv().getVardenhet(), grundData.getSkapadAv().getVardenhet());
            assertEquals(result.getSkapadAv().getVardenhet().getPostadress(), grundData.getSkapadAv().getVardenhet().getPostadress());
            assertEquals(result.getSkapadAv().getVardenhet().getPostort(), grundData.getSkapadAv().getVardenhet().getPostort());
            assertEquals(result.getSkapadAv().getVardenhet().getPostnummer(), grundData.getSkapadAv().getVardenhet().getPostnummer());
            assertEquals(result.getSkapadAv().getVardenhet().getPostnummer(), grundData.getSkapadAv().getVardenhet().getPostnummer());
        }

        @Test
        void grundDataNullTest() {
            hoSPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hoSPersonal);
            var result = grundData(null, grundData);

            verify(grundData, never()).getSkapadAv();
            verify(hoSPersonal, never()).getVardenhet();

            assertEquals(result, grundData);
        }

        @Test
        void grundDataSkapadAvNullTest() {
            Unit unit = Unit.builder().address("adress").city("stad").zipCode("12345").phoneNumber("123456789").build();
            hoSPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(null);
            metadata = CertificateMetadata.builder()
                .unit(unit)
                .patient(Patient.builder().build())
                .build();

            grundData(metadata, grundData);

            verify(grundData, times(1)).getSkapadAv();
            verify(hoSPersonal, never()).getVardenhet();
        }

        @Test
        void grundDataVardenhetNullTest() {
            Unit unit = Unit.builder().address("adress").city("stad").zipCode("12345").phoneNumber("123456789").build();
            hoSPersonal.setVardenhet(null);
            grundData.setSkapadAv(hoSPersonal);
            metadata = CertificateMetadata.builder().unit(unit)
                .patient(Patient.builder().build())
                .build();

            grundData(metadata, grundData);

            verify(grundData, times(2)).getSkapadAv();
            verify(hoSPersonal, times(1)).getVardenhet();
        }
    }

    @Nested
    class GrundDataPatient {

        private GrundData grundData;

        @BeforeEach
        void setUp() {
            grundData = new GrundData();
            final var patient = new se.inera.intyg.common.support.model.common.internal.Patient();
            patient.setPostadress(CURRENT_STREET_ADDRESS);
            patient.setPostort(CURRENT_CITY);
            patient.setPostnummer(CURRENT_ZIP_CODE);
            grundData.setPatient(patient);
        }

        @Test
        void shallUpdatePatientStreetIfNotFromPU() {
            final var expectedStreet = "New street address";
            final var metadata = CertificateMetadata.builder().patient(
                    Patient.builder()
                        .street(expectedStreet)
                        .addressFromPU(false)
                        .build()
                )
                .unit(Unit.builder().build())
                .build();

            final var actualGrundData = grundData(metadata, grundData);

            assertEquals(expectedStreet, actualGrundData.getPatient().getPostadress());
        }

        @Test
        void shallNotUpdatePatientStreetIfFromPU() {
            final var metadata = CertificateMetadata.builder().patient(
                    Patient.builder()
                        .street("New street address")
                        .addressFromPU(true)
                        .build()
                )
                .unit(Unit.builder().build())
                .build();

            final var actualGrundData = grundData(metadata, grundData);

            assertEquals(CURRENT_STREET_ADDRESS, actualGrundData.getPatient().getPostadress());
        }

        @Test
        void shallUpdatePatientCityIfNotFromPU() {
            final var expectedCity = "New city";
            final var metadata = CertificateMetadata.builder().patient(
                    Patient.builder()
                        .city(expectedCity)
                        .addressFromPU(false)
                        .build()
                )
                .unit(Unit.builder().build())
                .build();

            final var actualGrundData = grundData(metadata, grundData);

            assertEquals(expectedCity, actualGrundData.getPatient().getPostort());
        }

        @Test
        void shallNotUpdatePatientCityIfFromPU() {
            final var metadata = CertificateMetadata.builder().patient(
                    Patient.builder()
                        .city("New city")
                        .addressFromPU(true)
                        .build()
                )
                .unit(Unit.builder().build())
                .build();

            final var actualGrundData = grundData(metadata, grundData);

            assertEquals(CURRENT_CITY, actualGrundData.getPatient().getPostort());
        }

        @Test
        void shallUpdatePatientZipCodeIfNotFromPU() {
            final var expectedZipCode = "New zip code";
            final var metadata = CertificateMetadata.builder().patient(
                    Patient.builder()
                        .zipCode(expectedZipCode)
                        .addressFromPU(false)
                        .build()
                )
                .unit(Unit.builder().build())
                .build();

            final var actualGrundData = grundData(metadata, grundData);

            assertEquals(expectedZipCode, actualGrundData.getPatient().getPostnummer());
        }

        @Test
        void shallNotUpdatePatientZipCodeIfFromPU() {
            final var metadata = CertificateMetadata.builder().patient(
                    Patient.builder()
                        .zipCode("New zip code")
                        .addressFromPU(true)
                        .build()
                )
                .unit(Unit.builder().build())
                .build();

            final var actualGrundData = grundData(metadata, grundData);

            assertEquals(CURRENT_ZIP_CODE, actualGrundData.getPatient().getPostnummer());
        }
    }
}