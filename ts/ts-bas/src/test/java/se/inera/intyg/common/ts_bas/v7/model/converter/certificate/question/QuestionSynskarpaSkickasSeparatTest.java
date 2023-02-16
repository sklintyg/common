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
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BINOKULART_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BINOKULART_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HOGER_OGA_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HOGER_OGA_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KONTAKTLINSER_HOGER_OGA_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KONTAKTLINSER_VANSTER_OGA_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_JA_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_NEJ_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_HEADER_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_SVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VANSTER_OGA_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VANSTER_OGA_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VARDEN_FOR_SYNSKARPA_ID;

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
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCheckboxBooleanTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationDisableTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalBooleanValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueBooleanTest;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;

@ExtendWith(MockitoExtension.class)
class QuestionSynskarpaSkickasSeparatTest {

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
                return QuestionSynskarpaSkickasSeparat.toCertificate(null, 0, textProvider);
            }

            @Override
            protected String getId() {
                return SYNKARPA_SKICKAS_SEPARAT_DELSVAR_ID;
            }

            @Override
            protected String getParent() {
                return SYNKARPA_SKICKAS_SEPARAT_HEADER_ID;
            }

            @Override
            protected int getIndex() {
                return 0;
            }
        }

        @Nested
        class IncludeConfigCheckboxBooleanTests extends ConfigCheckboxBooleanTest {

            @Override
            protected String getJsonId() {
                return SYNKARPA_SKICKAS_SEPARAT_JSON_ID;
            }

            @Override
            protected CertificateTextProvider getTextProviderMock() {
                return textProvider;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionSynskarpaSkickasSeparat.toCertificate(null, 0, textProvider);
            }

            @Override
            protected String getLabelId() {
                return SYNKARPA_SKICKAS_SEPARAT_SVAR_TEXT_ID;
            }

            @Override
            protected String getTextId() {
                return null;
            }

            @Override
            protected String getDescriptionId() {
                return null;
            }

            @Override
            protected String getSelectedTextId() {
                return SVAR_JA_TEXT;
            }

            @Override
            protected String getUnselectedTextId() {
                return SVAR_NEJ_TEXT;
            }
        }

        @Nested
        class IncludeValueRadioBooleanTest extends ValueBooleanTest {

            @Override
            protected CertificateDataElement getElement() {
                final var syn = Syn.builder().setSynskarpaSkickasSeparat(true).build();
                return QuestionSynskarpaSkickasSeparat.toCertificate(syn, 0, textProvider);
            }

            @Override
            protected String getJsonId() {
                return SYNKARPA_SKICKAS_SEPARAT_JSON_ID;
            }

            @Override
            protected Boolean getBoolean() {
                return true;
            }
        }

        @Nested
        class IncludeValidationDisableTests extends ValidationDisableTest {

            @Override
            protected String getQuestionId() {
                return VARDEN_FOR_SYNSKARPA_ID;
            }

            @Override
            protected String getExpression() {
                return "exists(" + VANSTER_OGA_UTAN_KORREKTION_JSON_ID
                    + ") || exists(" + VANSTER_OGA_MED_KORREKTION_JSON_ID
                    + ") || exists(" + KONTAKTLINSER_VANSTER_OGA_JSON_ID
                    + ") || exists(" + HOGER_OGA_UTAN_KORREKTION_JSON_ID
                    + ") || exists(" + HOGER_OGA_MED_KORREKTION_JSON_ID
                    + ") || exists(" + KONTAKTLINSER_HOGER_OGA_DELSVAR_JSON_ID
                    + ") || exists(" + BINOKULART_UTAN_KORREKTION_JSON_ID
                    + ") || exists(" + BINOKULART_MED_KORREKTION_JSON_ID + ")";
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionSynskarpaSkickasSeparat.toCertificate(null, 0, textProvider);
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
                final var syn = Syn.builder().setSynskarpaSkickasSeparat(expectedValue).build();
                return QuestionSynskarpaSkickasSeparat.toCertificate(syn, 0, textProvider);
            }

            @Override
            protected Boolean toInternalBooleanValue(Certificate certificate) {
                return QuestionSynskarpaSkickasSeparat.toInternal(certificate);
            }
        }
    }
}
