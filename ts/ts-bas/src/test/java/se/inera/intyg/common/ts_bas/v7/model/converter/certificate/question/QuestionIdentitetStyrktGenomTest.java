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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.facade.model.config.Layout.COLUMNS;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_STYRKT_GENOM_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_STYRKT_GENOM_FORETAG_ELLER_TJANSTEKORT_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_STYRKT_GENOM_FORSAKRAN_KAP18_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_STYRKT_GENOM_ID_KORT_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_STYRKT_GENOM_KORKORT_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_STYRKT_GENOM_PASS_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_STYRKT_GENOM_PERS_KANNEDOM_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_STYRKT_GENOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_STYRKT_GENOM_TEXT_ID;

import java.util.Arrays;
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
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigRadioMultipleCodeTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueCodeTest;
import se.inera.intyg.common.ts_bas.v7.model.internal.Vardkontakt;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;

@ExtendWith(MockitoExtension.class)
class QuestionIdentitetStyrktGenomTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setup() {
        when(textProvider.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        @Nested
        class IncludeCommonElementTest extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionIdentitetStyrktGenom.toCertificate(null, 0, textProvider);
            }

            @Override
            protected String getId() {
                return IDENTITET_STYRKT_GENOM_SVAR_ID;
            }

            @Override
            protected String getParent() {
                return IDENTITET_CATEGORY_ID;
            }

            @Override
            protected int getIndex() {
                return 0;
            }
        }

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class IncludeConfigRadioMultipleCodeTests extends ConfigRadioMultipleCodeTest {

            @Override
            protected CertificateTextProvider getTextProviderMock() {
                return textProvider;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionIdentitetStyrktGenom.toCertificate(null, 0, textProvider);
            }

            @Override
            protected String getTextId() {
                return IDENTITET_STYRKT_GENOM_TEXT_ID;
            }

            @Override
            protected String getDescriptionId() {
                return IDENTITET_STYRKT_GENOM_DESCRIPTION_ID;
            }

            @Override
            protected List<RadioMultipleCode> getExpectedRadioMultipleCodes() {
                return Arrays.asList(
                    RadioMultipleCode.builder()
                        .id(IdKontrollKod.ID_KORT.getCode())
                        .label(textProvider.get(IDENTITET_STYRKT_GENOM_ID_KORT_TEXT_ID))
                        .build(),
                    RadioMultipleCode.builder()
                        .id(IdKontrollKod.KORKORT.getCode())
                        .label(textProvider.get(IDENTITET_STYRKT_GENOM_KORKORT_TEXT_ID))
                        .build(),
                    RadioMultipleCode.builder()
                        .id(IdKontrollKod.FORSAKRAN_KAP18.getCode())
                        .label(textProvider.get(IDENTITET_STYRKT_GENOM_FORSAKRAN_KAP18_TEXT_ID))
                        .build(),
                    RadioMultipleCode.builder()
                        .id(IdKontrollKod.FORETAG_ELLER_TJANSTEKORT.getCode())
                        .label(textProvider.get(IDENTITET_STYRKT_GENOM_FORETAG_ELLER_TJANSTEKORT_TEXT_ID))
                        .build(),
                    RadioMultipleCode.builder()
                        .id(IdKontrollKod.PERS_KANNEDOM.getCode())
                        .label(textProvider.get(IDENTITET_STYRKT_GENOM_PERS_KANNEDOM_TEXT_ID))
                        .build(),
                    RadioMultipleCode.builder()
                        .id(IdKontrollKod.PASS.getCode())
                        .label(textProvider.get(IDENTITET_STYRKT_GENOM_PASS_TEXT_ID))
                        .build()
                );
            }

            @Override
            protected Layout getExpectedLayout() {
                return COLUMNS;
            }
        }

        @Nested
        class IncludeValueCodeTest extends ValueCodeTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionIdentitetStyrktGenom.toCertificate(
                    Vardkontakt.create(null, IdKontrollKod.FORETAG_ELLER_TJANSTEKORT.getCode()), 0, textProvider);
            }

            @Override
            protected String getCodeId() {
                return IdKontrollKod.FORETAG_ELLER_TJANSTEKORT.getCode();
            }

            @Override
            protected String getCode() {
                return IdKontrollKod.FORETAG_ELLER_TJANSTEKORT.getCode();
            }
        }

        @Nested
        class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

            @Override
            protected String getQuestionId() {
                return IDENTITET_STYRKT_GENOM_SVAR_ID;
            }

            @Override
            protected String getExpression() {
                return "exists(" + IdKontrollKod.ID_KORT.getCode()
                    + ") || exists(" + IdKontrollKod.FORETAG_ELLER_TJANSTEKORT.getCode()
                    + ") || exists(" + IdKontrollKod.KORKORT.getCode()
                    + ") || exists(" + IdKontrollKod.PERS_KANNEDOM.getCode()
                    + ") || exists(" + IdKontrollKod.FORSAKRAN_KAP18.getCode()
                    + ") || exists(" + IdKontrollKod.PASS.getCode() + ")";
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionIdentitetStyrktGenom.toCertificate(null, 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 0;
            }
        }

        @Nested
        @TestInstance(Lifecycle.PER_CLASS)
        class ToInternal extends InternalValueTest<Vardkontakt, Vardkontakt> {

            @Override
            protected CertificateDataElement getElement(Vardkontakt input) {
                return QuestionIdentitetStyrktGenom.toCertificate(input, 0, textProvider);
            }

            @Override
            protected Vardkontakt toInternalValue(Certificate certificate) {
                return QuestionIdentitetStyrktGenom.toInternal(certificate);
            }

            @Override
            protected List<InputExpectedValuePair<Vardkontakt, Vardkontakt>> inputExpectedValuePairList() {
                return List.of(
                    new InputExpectedValuePair<>(
                        null, Vardkontakt.create(null, null)),
                    new InputExpectedValuePair<>(
                        Vardkontakt.create(null, IdKontrollKod.PERS_KANNEDOM.getCode()),
                        Vardkontakt.create(null, IdKontrollKod.PERS_KANNEDOM.getCode())),
                    new InputExpectedValuePair<>(
                        Vardkontakt.create(null, ""), Vardkontakt.create(null, null))
                );
            }
        }
    }
}
