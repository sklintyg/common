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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.STADIGVARANDE_MEDICINERING_CATEGORY_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigRadioBooleanTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalBooleanValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueBooleanTest;
import se.inera.intyg.common.ts_bas.v7.model.internal.Medicinering;

@ExtendWith(MockitoExtension.class)
class QuestionStadigvarandeMedicineringTest {
    @Mock
    CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        when(textProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class ToCertificate {

        @Nested
        class IncludeCommonElementTests extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionStadigvarandeMedicinering.toCertificate(null, 0, textProvider);
            }

            @Override
            protected String getId() {
                return FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID;
            }

            @Override
            protected String getParent() {
                return STADIGVARANDE_MEDICINERING_CATEGORY_ID;
            }

            @Override
            protected int getIndex() {
                return 0;
            }
        }

        @Nested
        class IncludeConfigRadioBooleanTests extends ConfigRadioBooleanTest {

            @Override
            protected String getId() {
                return FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_JSON_ID;
            }

            @Override
            protected String getSelectedText() {
                return "Ja";
            }

            @Override
            protected String getUnselectedText() {
                return "Nej";
            }

            @Override
            protected CertificateTextProvider getTextProviderMock() {
                return textProvider;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionStadigvarandeMedicinering.toCertificate(null, 0, textProvider);
            }

            @Override
            protected String getTextId() {
                return FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_TEXT_ID;
            }

            @Override
            protected String getDescriptionId() {
                return null;
            }
        }

        @Nested
        class IncludeValueRadioBooleanTest extends ValueBooleanTest {

            @Override
            protected String getJsonId() {
                return FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_JSON_ID;
            }

            @Override
            protected Boolean getBoolean() {
                return true;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionStadigvarandeMedicinering.toCertificate(
                    Medicinering.builder().setStadigvarandeMedicinering(true).build(), 0, textProvider);
            }
        }

        @Nested
        class IncludeValidationMandatory extends ValidationMandatoryTest {

            @Override
            protected String getQuestionId() {
                return FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID;
            }

            @Override
            protected String getExpression() {
                return "exists(forekomststadigvarandemedicinering)";
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionStadigvarandeMedicinering.toCertificate(null, 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 0;
            }
        }
    }

    @Nested
    class ToInternal {
        @Nested
        @TestInstance(PER_CLASS)
        class IncludeInternalBooleanValueTest extends InternalBooleanValueTest {

            @Override
            protected CertificateDataElement getElement(Boolean expectedValue) {
                return QuestionStadigvarandeMedicinering.toCertificate(
                    Medicinering.builder().setStadigvarandeMedicinering(expectedValue).build(), 0, textProvider);
            }

            @Override
            protected Boolean toInternalBooleanValue(Certificate certificate) {
                return QuestionStadigvarandeMedicinering.toInternal(certificate);
            }
        }
    }
}