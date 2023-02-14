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
package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_TEXT_ID;

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
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueDateTest;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunktTest {

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
            return QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toCertificate(null, null, getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID;
        }

        @Override
        protected String getParent() {
            return HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID;
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
            final var personId = Personnummer.createPersonnummer("191212121212").orElseThrow();
            return QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toCertificate(null, personId, 0, textProvider);
        }

        @Override
        protected String getJsonId() {
            return HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID;
        }

        @Override
        protected String getTextId() {
            return HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected LocalDate getMinDate() {
            return LocalDate.parse("1912-12-12");
        }

        @Override
        protected  LocalDate getMaxDate() {
            return LocalDate.now();
        }
    }

    @Nested
    class IncludeValueDateTest extends ValueDateTest {

        @Override
        protected CertificateDataElement getElement() {
            final var hypoglykemi = Hypoglykemi.builder().setAterkommandeVaketSenasteTreTidpunkt(new InternalDate(getDate()))
                .build();
            return QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toCertificate(hypoglykemi, null, 0, textProvider);
        }

        @Override
        protected String getJsonId() {
            return HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID;
        }

        @Override
        protected LocalDate getDate() {
            return LocalDate.parse("2023-01-13");
        }
    }

    @Nested
    class IncludeValidationShowTest extends ValidationShowTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toCertificate(null, null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }

        @Override
        protected String getQuestionId() {
            return HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID;
        }
    }

    @Nested
    class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toCertificate(null, null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }

        @Override
        protected String getQuestionId() {
            return HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalDateValueTest extends InternalValueTest<Hypoglykemi, InternalDate> {

        @Override
        protected CertificateDataElement getElement(Hypoglykemi input) {
            final var personId = Personnummer.createPersonnummer("191212121212").orElseThrow();
            return QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toCertificate(input, personId, 0, textProvider);
        }

        @Override
        protected InternalDate toInternalValue(Certificate certificate) {
            return QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toInternal(certificate);
        }

        @Override
        protected List<InputExpectedValuePair<Hypoglykemi, InternalDate>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(Hypoglykemi.builder()
                    .setAterkommandeVaketSenasteTreTidpunkt(new InternalDate("2022-12-15")).build(), new InternalDate("2022-12-15")),
                new InputExpectedValuePair<>(Hypoglykemi.builder()
                    .setAterkommandeVaketSenasteTreTidpunkt(new InternalDate("")).build(), null),
                new InputExpectedValuePair<>(null, null)
            );
        }
    }
}
