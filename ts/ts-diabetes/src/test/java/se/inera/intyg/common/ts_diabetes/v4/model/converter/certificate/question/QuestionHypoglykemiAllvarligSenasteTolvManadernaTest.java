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
package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.SVAR_JA_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.SVAR_NEJ_TEXT_ID;

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
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigRadioBooleanTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalBooleanValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueBooleanTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;

@ExtendWith(MockitoExtension.class)
class QuestionHypoglykemiAllvarligSenasteTolvManadernaTest {

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
            return QuestionHypoglykemiAllvarligSenasteTolvManaderna.toCertificate(null, getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return HYPOGLYKEMI_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 3;
        }
    }

    @Nested
    class IncludeConfigRadioBooleanTest extends ConfigRadioBooleanTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            doReturn("Ja").when(textProvider).get(SVAR_JA_TEXT_ID);
            doReturn("Nej").when(textProvider).get(SVAR_NEJ_TEXT_ID);
            return QuestionHypoglykemiAllvarligSenasteTolvManaderna.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getId() {
            return HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID;
        }

        @Override
        protected String getTextId() {
            return HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected String getSelectedText() {
            return "Ja";
        }

        @Override
        protected String getUnselectedText() {
            return "Nej";
        }
    }

    @Nested
    class IncludeValueBooleanTest extends ValueBooleanTest {

        @Override
        protected CertificateDataElement getElement() {
            final var hypoglykemi = Hypoglykemi.builder().setAllvarligSenasteTolvManaderna(true).build();
            return QuestionHypoglykemiAllvarligSenasteTolvManaderna.toCertificate(hypoglykemi, 0, textProvider);
        }

        @Override
        protected String getJsonId() {
            return HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID;
        }

        @Override
        protected Boolean getBoolean() {
            return true;
        }
    }

    @Nested
    class IncludeValidationShowTest extends ValidationShowTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionHypoglykemiAllvarligSenasteTolvManaderna.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }

        @Override
        protected String getQuestionId() {
            return INTYG_AVSER_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "exists(" + IntygAvserKategori.VAR1.name()
                + ") || exists(" + IntygAvserKategori.VAR2.name() + ") || exists(" + IntygAvserKategori.VAR3.name()
                + ") || exists(" + IntygAvserKategori.VAR4.name() + ") || exists(" + IntygAvserKategori.VAR5.name()
                + ") || exists(" + IntygAvserKategori.VAR6.name() + ") || exists(" + IntygAvserKategori.VAR7.name()
                + ") || exists(" + IntygAvserKategori.VAR8.name() + ") || exists(" + IntygAvserKategori.VAR9.name() + ")";
        }
    }

    @Nested
    class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionHypoglykemiAllvarligSenasteTolvManaderna.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }

        @Override
        protected String getQuestionId() {
            return HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "exists(" + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID + ")";
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalBooleanValueTest extends InternalBooleanValueTest {

        @Override
        protected CertificateDataElement getElement(Boolean expectedValue) {
            final var hypoglykemi = Hypoglykemi.builder().setAllvarligSenasteTolvManaderna(expectedValue).build();
            return QuestionHypoglykemiAllvarligSenasteTolvManaderna.toCertificate(hypoglykemi, 0, textProvider);
        }

        @Override
        protected Boolean toInternalBooleanValue(Certificate certificate) {
            return QuestionHypoglykemiAllvarligSenasteTolvManaderna.toInternal(certificate);
        }
    }
}
