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

package se.inera.intyg.common.fk7263.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_FOM_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_FOM_TEXT;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_100_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_25_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_50_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_75_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_TEXT;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_TOM_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_TOM_TEXT;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.ViewColumn;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewTable;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigViewTableTest;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;

@ExtendWith(MockitoExtension.class)
class QuestionArbetsformogaBedomningTest {

    @Mock
    CertificateMessagesProvider messagesProvider;

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionArbetsformogaBedomning.toCertificate(null, null, null, null,
                0, messagesProvider);
        }

        @Override
        protected String getId() {
            return ARBETSFORMAGA_BEDOMNING_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return ARBETSFORMAGA_BEDOMNING_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigViewTableTests extends ConfigViewTableTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionArbetsformogaBedomning.toCertificate(null, null, null,
                null, 0, messagesProvider);
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected CertificateMessagesProvider getMessageProviderMock() {
            return messagesProvider;
        }

        @Override
        protected String getMessageId() {
            return null;
        }

        @Override
        protected List<ViewColumn> getColumns() {
            return List.of(
                ViewColumn.builder()
                    .id(ARBETSFORMAGA_BEDOMNING_NEDSATT_ID)
                    .text(ARBETSFORMAGA_BEDOMNING_NEDSATT_TEXT)
                    .build(),
                ViewColumn.builder()
                    .id(ARBETSFORMAGA_BEDOMNING_FOM_ID)
                    .text(ARBETSFORMAGA_BEDOMNING_FOM_TEXT)
                    .build(),
                ViewColumn.builder()
                    .id(ARBETSFORMAGA_BEDOMNING_TOM_ID)
                    .text(ARBETSFORMAGA_BEDOMNING_TOM_TEXT)
                    .build()
            );
        }
    }

    @Nested
    class IncludeValueViewTableTests {

        @Test
        void shouldIncludeTypeValueViewTable() {
            final var element = QuestionArbetsformogaBedomning.toCertificate(null, null, null,
                null, 0, messagesProvider);
            assertEquals(CertificateDataValueType.VIEW_TABLE, element.getValue().getType());
        }

        @Test
        void shouldIncludeRows() {
            final var element = QuestionArbetsformogaBedomning.toCertificate(null, null, null,
                null, 0, messagesProvider);
            final var value = (CertificateDataValueViewTable) element.getValue();

            assertNotNull(value.getRows());
        }

        @Test
        void shouldIncludeRowText() {
            final var expectedInternalLocalDateInterval = new InternalLocalDateInterval();
            expectedInternalLocalDateInterval.setFrom(new InternalDate(LocalDate.now()));
            expectedInternalLocalDateInterval.setTom(new InternalDate(LocalDate.now()));
            final var element = QuestionArbetsformogaBedomning.toCertificate(expectedInternalLocalDateInterval,
                expectedInternalLocalDateInterval, expectedInternalLocalDateInterval,
                expectedInternalLocalDateInterval, 0, messagesProvider);

            assertAll(
                () -> verify(messagesProvider, atLeastOnce()).get(ARBETSFORMAGA_BEDOMNING_NEDSATT_25_TEXT_ID),
                () -> verify(messagesProvider, atLeastOnce()).get(ARBETSFORMAGA_BEDOMNING_NEDSATT_50_TEXT_ID),
                () -> verify(messagesProvider, atLeastOnce()).get(ARBETSFORMAGA_BEDOMNING_NEDSATT_75_TEXT_ID),
                () -> verify(messagesProvider, atLeastOnce()).get(ARBETSFORMAGA_BEDOMNING_NEDSATT_100_TEXT_ID)
            );
        }

        @Test
        void shouldIncludeRowId() {
            final var expectedInternalLocalDateInterval = new InternalLocalDateInterval();
            expectedInternalLocalDateInterval.setFrom(new InternalDate(LocalDate.now()));
            expectedInternalLocalDateInterval.setTom(new InternalDate(LocalDate.now()));
            final var element = QuestionArbetsformogaBedomning.toCertificate(expectedInternalLocalDateInterval,
                null, null,
                null, 0, messagesProvider);

            final var value = (CertificateDataValueViewTable) element.getValue();

            assertAll(
                () -> assertEquals(ARBETSFORMAGA_BEDOMNING_NEDSATT_ID, value.getRows().get(0).getColumns().get(0).getId()),
                () -> assertEquals(ARBETSFORMAGA_BEDOMNING_FOM_ID, value.getRows().get(0).getColumns().get(1).getId()),
                () -> assertEquals(ARBETSFORMAGA_BEDOMNING_TOM_ID, value.getRows().get(0).getColumns().get(2).getId())
            );
        }

        @Test
        void shouldIncludeColumns() {
            final var expectedInternalLocalDateInterval = new InternalLocalDateInterval();
            expectedInternalLocalDateInterval.setFrom(new InternalDate(LocalDate.now()));
            expectedInternalLocalDateInterval.setTom(new InternalDate(LocalDate.now()));
            final var element = QuestionArbetsformogaBedomning.toCertificate(expectedInternalLocalDateInterval,
                expectedInternalLocalDateInterval, expectedInternalLocalDateInterval,
                expectedInternalLocalDateInterval, 0, messagesProvider);
            final var value = (CertificateDataValueViewTable) element.getValue();

            assertAll(
                () -> assertNotNull(value.getRows().get(0).getColumns()),
                () -> assertNotNull(value.getRows().get(1).getColumns()),
                () -> assertNotNull(value.getRows().get(2).getColumns()),
                () -> assertNotNull(value.getRows().get(3).getColumns())
            );
        }

        @Test
        void shouldIncludeColumnsWithValues() {
            final var expectedInternalLocalDateInterval = new InternalLocalDateInterval();
            expectedInternalLocalDateInterval.setFrom(new InternalDate(LocalDate.now()));
            expectedInternalLocalDateInterval.setTom(new InternalDate(LocalDate.now()));
            final var element = QuestionArbetsformogaBedomning.toCertificate(expectedInternalLocalDateInterval,
                expectedInternalLocalDateInterval, expectedInternalLocalDateInterval,
                null, 0, messagesProvider);
            final var value = (CertificateDataValueViewTable) element.getValue();

            assertAll(
                () -> assertEquals(expectedInternalLocalDateInterval.getFrom().toString(),
                    value.getRows().get(0).getColumns().get(1).getText()),
                () -> assertEquals(expectedInternalLocalDateInterval.getTom().toString(),
                    value.getRows().get(0).getColumns().get(2).getText()),
                () -> assertEquals(expectedInternalLocalDateInterval.getFrom().toString(),
                    value.getRows().get(1).getColumns().get(1).getText()),
                () -> assertEquals(expectedInternalLocalDateInterval.getTom().toString(),
                    value.getRows().get(1).getColumns().get(2).getText()),
                () -> assertEquals(expectedInternalLocalDateInterval.getFrom().toString(),
                    value.getRows().get(2).getColumns().get(1).getText()),
                () -> assertEquals(expectedInternalLocalDateInterval.getTom().toString(),
                    value.getRows().get(2).getColumns().get(2).getText())
            );
        }

        @Test
        void shouldNotIncludeColumnsWithNoValues() {
            final var element = QuestionArbetsformogaBedomning.toCertificate(null, null, null,
                null, 0, messagesProvider);
            final var value = (CertificateDataValueViewTable) element.getValue();

            assertEquals(0, value.getRows().size());
        }
    }
}
