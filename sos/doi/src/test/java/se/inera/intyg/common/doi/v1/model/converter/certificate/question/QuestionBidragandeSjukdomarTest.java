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

package se.inera.intyg.common.doi.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DESCRIPTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_KRONISK;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_PLOTSLIG;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCauseOfDeathList;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.config.CodeItem;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeath;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeathList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionBidragandeSjukdomarTest {

    @Mock
    private CertificateTextProvider texts;

    private List<Dodsorsak> bidragandeSjukdomar;
    private List<CodeItem> allSpecifications;

    @BeforeEach
    void setup() {
        bidragandeSjukdomar = Collections.emptyList();
        allSpecifications = List.of(
            CodeItem.builder()
                .id(Specifikation.PLOTSLIG.name())
                .label(FOLJD_OM_DELSVAR_PLOTSLIG)
                .code(Specifikation.PLOTSLIG.name())
                .build(),
            CodeItem.builder()
                .id(Specifikation.KRONISK.name())
                .label(FOLJD_OM_DELSVAR_KRONISK)
                .code(Specifikation.KRONISK.name())
                .build(),
            CodeItem.builder()
                .id(Specifikation.UPPGIFT_SAKNAS.name())
                .label(FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS)
                .code(Specifikation.UPPGIFT_SAKNAS.name())
                .build()
        );
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        @Test
        void shouldIncludeId() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(bidragandeSjukdomar, 0, texts);
            assertEquals(BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID, question.getId());
        }


        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            assertEquals(DODSORSAK_SVAR_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(BIDRAGANDE_SJUKDOM_OM_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeDescription() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(bidragandeSjukdomar, 0, texts);
            assertTrue(question.getConfig().getDescription().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(BIDRAGANDE_SJUKDOM_OM_DESCRIPTION_TEXT_ID);
        }


        @Test
        void shouldIncludeTerminalCauseOfDeathConfigType() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_CAUSE_OF_DEATH_LIST, question.getConfig().getType());
        }

        @Test
        void shouldIncludeTerminalCauseOfDeathList() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var config = (CertificateDataConfigCauseOfDeathList) question.getConfig();
            assertEquals(8, config.getList().size());
        }

        @Test
        void shouldIncludeCorrectConfigCauseOfDeathId() {
            final var expectedIds = List.of("0", "1", "2", "3", "4", "5", "6", "7");
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var config = (CertificateDataConfigCauseOfDeathList) question.getConfig();
            for (int i = 0; i < expectedIds.size(); i++) {
                assertEquals(expectedIds.get(i), config.getList().get(i).getId());
            }
        }


        @Test
        void shouldIncludeCorrectConfigDescriptionId() {
            final var expectedId = "description";
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var config = (CertificateDataConfigCauseOfDeathList) question.getConfig();
            for (CauseOfDeath causeOfDeath : config.getList()) {
                assertEquals(expectedId, causeOfDeath.getDescriptionId());
            }
        }

        @Test
        void shouldIncludeCorrectConfigDebutId() {
            final var expectedId = "debut";
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var config = (CertificateDataConfigCauseOfDeathList) question.getConfig();
            for (CauseOfDeath causeOfDeath : config.getList()) {
                assertEquals(expectedId, causeOfDeath.getDebutId());
            }
        }


        @Test
        void shouldIncludeCorrectConfigSpecifications() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var config = (CertificateDataConfigCauseOfDeathList) question.getConfig();
            for (CauseOfDeath causeOfDeath : config.getList()) {
                assertEquals(allSpecifications, causeOfDeath.getSpecifications());
            }
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeathList() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            assertEquals(CertificateDataValueType.CAUSE_OF_DEATH_LIST, question.getValue().getType());
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeathListEqualSizeOfConfig() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            final var config = (CertificateDataConfigCauseOfDeathList) question.getConfig();
            assertEquals(config.getList().size(), causeOfDeathList.getList().size());
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeathListId() {
            final var expectedIds = List.of("0", "1", "2", "3", "4", "5", "6", "7");
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            for (int i = 0; i < expectedIds.size(); i++) {
                assertEquals(expectedIds.get(i), causeOfDeathList.getList().get(i).getId());
            }
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeathListDescriptionTextValue() {
            final var expectedValueType = CertificateDataValueType.TEXT;
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            for (CertificateDataValueCauseOfDeath causeOfDeath : causeOfDeathList.getList()) {
                assertEquals(expectedValueType, causeOfDeath.getDescription().getType());
            }
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeathListDescriptionTextValueId() {
            final var expectedId = "description";
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            for (CertificateDataValueCauseOfDeath causeOfDeath : causeOfDeathList.getList()) {
                assertEquals(expectedId, causeOfDeath.getDescription().getId());
            }
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeathListDebutDateValue() {
            final var expectedValueType = CertificateDataValueType.DATE;
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            for (CertificateDataValueCauseOfDeath causeOfDeath : causeOfDeathList.getList()) {
                assertEquals(expectedValueType, causeOfDeath.getDebut().getType());
            }
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeathListDebutDateValueId() {
            final var expectedId = "debut";
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            for (CertificateDataValueCauseOfDeath causeOfDeath : causeOfDeathList.getList()) {
                assertEquals(expectedId, causeOfDeath.getDebut().getId());
            }
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeathListSpecificationCodeValue() {
            final var expectedValueType = CertificateDataValueType.CODE;
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            for (CertificateDataValueCauseOfDeath causeOfDeath : causeOfDeathList.getList()) {
                assertEquals(expectedValueType, causeOfDeath.getSpecification().getType());
            }
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeathOneValue() {
            final var bidragandeSjukdom = Dodsorsak.create("Description", new InternalDate(LocalDate.now()), Specifikation.KRONISK);
            final var expectedValue = CertificateDataValueCauseOfDeath.builder()
                .id("0")
                .description(
                    CertificateDataTextValue.builder()
                        .id("description")
                        .text(bidragandeSjukdom.getBeskrivning())
                        .build()
                )
                .debut(
                    CertificateDataValueDate.builder()
                        .id("debut")
                        .date(bidragandeSjukdom.getDatum().asLocalDate())
                        .build()
                )
                .specification(
                    se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode.builder()
                        .id(bidragandeSjukdom.getSpecifikation().name())
                        .code(bidragandeSjukdom.getSpecifikation().name())
                        .build()
                )
                .build();

            final var question = QuestionBidragandeSjukdomar.toCertificate(
                List.of(bidragandeSjukdom), 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            assertEquals(expectedValue, causeOfDeathList.getList().get(0));
        }
    }

    @Nested
    class ToInternal {

        List dodsorsakList = List.of(
            Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.PLOTSLIG),
            Dodsorsak.create(null, null, null),
            Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.KRONISK),
            Dodsorsak.create(null, null, null),
            Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.UPPGIFT_SAKNAS),
            Dodsorsak.create(null, null, null),
            Dodsorsak.create(null, null, null),
            Dodsorsak.create(null, null, null));

        @Test
        void shouldIncludeAllDodsorsak() {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionBidragandeSjukdomar.toCertificate(dodsorsakList, 0, texts))
                .build();

            final var actualValue = QuestionBidragandeSjukdomar.toInternal(certificate);

            assertEquals(dodsorsakList.size(), actualValue.size());
            for (int i = 0; i < dodsorsakList.size(); i++) {
                assertEquals(dodsorsakList.get(i), actualValue.get(i));
            }
        }
    }
}