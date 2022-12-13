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
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DESCRIPTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_KRONISK;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_PLOTSLIG;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_CATEGORY_ID;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCauseOfDeathList;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.config.CodeItem;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeath;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeathList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionBidragandeSjukdomarTest {

    public static final List<String> EXPECTED_IDS = List.of("0", "1", "2", "3", "4", "5", "6", "7");
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
            assertEquals(TERMINAL_DODSORSAK_CATEGORY_ID, question.getParent());
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
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var config = (CertificateDataConfigCauseOfDeathList) question.getConfig();
            for (int i = 0; i < EXPECTED_IDS.size(); i++) {
                assertEquals(EXPECTED_IDS.get(i), config.getList().get(i).getId());
            }
        }


        @Test
        void shouldIncludeCorrectConfigDescriptionId() {
            var id = 0;
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var config = (CertificateDataConfigCauseOfDeathList) question.getConfig();
            for (CauseOfDeath causeOfDeath : config.getList()) {
                var expectedId = BIDRAGANDE_SJUKDOM_JSON_ID + "[" + id++ + "].beskrivning";
                assertEquals(expectedId, causeOfDeath.getDescriptionId());
            }
        }

        @Test
        void shouldIncludeCorrectConfigDebutId() {
            var id = 0;
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var config = (CertificateDataConfigCauseOfDeathList) question.getConfig();
            for (CauseOfDeath causeOfDeath : config.getList()) {
                var expectedId = BIDRAGANDE_SJUKDOM_JSON_ID + "[" + id++ + "].datum";
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
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            for (int i = 0; i < EXPECTED_IDS.size(); i++) {
                assertEquals(EXPECTED_IDS.get(i), causeOfDeathList.getList().get(i).getId());
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
            var id = 0;
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            for (CertificateDataValueCauseOfDeath causeOfDeath : causeOfDeathList.getList()) {
                var expectedId = BIDRAGANDE_SJUKDOM_JSON_ID + "[" + id++ + "].beskrivning";
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
            int id = 0;
            final var question = QuestionBidragandeSjukdomar.toCertificate(
                bidragandeSjukdomar, 0, texts);
            final var causeOfDeathList = (CertificateDataValueCauseOfDeathList) question.getValue();
            for (CertificateDataValueCauseOfDeath causeOfDeath : causeOfDeathList.getList()) {
                var expectedId = BIDRAGANDE_SJUKDOM_JSON_ID + "[" + id++ + "].datum";
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
                        .id(BIDRAGANDE_SJUKDOM_JSON_ID + "[0].beskrivning")
                        .text(bidragandeSjukdom.getBeskrivning())
                        .build()
                )
                .debut(
                    CertificateDataValueDate.builder()
                        .id(BIDRAGANDE_SJUKDOM_JSON_ID + "[0].datum")
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

        @Test
        void shouldIncludeValidationTextType() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(bidragandeSjukdomar, 0, texts);
            assertEquals(CertificateDataValidationType.TEXT_VALIDATION, question.getValidation()[1].getType());
        }

        @Test
        void shouldIncludeValidationTextId() {
            final var expectedIds = List.of("0", "1", "2", "3", "4", "5", "6", "7");
            final var question = QuestionBidragandeSjukdomar.toCertificate(bidragandeSjukdomar, 0, texts);
            for (int i = 0; i < expectedIds.size(); i++) {
                final var certificateDataValidationMaxDate = (CertificateDataValidationText) question.getValidation()[i];
                String expectedId = Integer.toString(i);
                assertEquals(expectedId, certificateDataValidationMaxDate.getId());
            }
        }

        @Test
        void shouldIncludeValidationTextLimit() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(bidragandeSjukdomar, 0, texts);
            final var ids = List.of("0", "1", "2", "3", "4", "5", "6", "7");
            for (int i = 0; i < ids.size(); i++) {
                final var certificateDataValidationMaxDate = (CertificateDataValidationText) question.getValidation()[i];
                assertEquals(55, certificateDataValidationMaxDate.getLimit());
            }
        }

        @Test
        void shouldIncludeValidationMaxDateType() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(bidragandeSjukdomar, 0, texts);
            final var ids = List.of("8", "9", "10", "11", "12", "13", "14", "15");
            for (int i = 8; i < ids.size() + 8; i++) {
                assertEquals(CertificateDataValidationType.MAX_DATE_VALIDATION, question.getValidation()[i].getType());
            }
        }

        @Test
        void shouldIncludeValidationMaxDateId() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(bidragandeSjukdomar, 0, texts);
            final var ids = List.of("8", "9", "10", "11", "12", "13", "14", "15");
            for (int i = 8; i < ids.size() + 8; i++) {
                final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[i];
                assertEquals(Integer.toString(i - 8), certificateDataValidationMaxDate.getId());
            }
        }

        @Test
        void shouldIncludeValidationMaxDateNumberOfDays() {
            final var question = QuestionBidragandeSjukdomar.toCertificate(bidragandeSjukdomar, 0, texts);
            final var ids = List.of("8", "9", "10", "11", "12", "13", "14", "15");
            for (int i = 8; i < ids.size() + 8; i++) {
                final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[i];
                assertEquals(0, certificateDataValidationMaxDate.getNumberOfDays());
            }
        }
    }

    @Nested
    class ToInternal {

        @Test
        void shouldReturnEmptyDodsorsakListIfNoneHasValue() {
            final var expectedDodsorsakList = Collections.emptyList();
            final var dodsorsakList = List.of(
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null)
            );

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionBidragandeSjukdomar.toCertificate(dodsorsakList, 0, texts))
                .build();

            final var actualValue = QuestionBidragandeSjukdomar.toInternal(certificate);
            assertEquals(expectedDodsorsakList, actualValue);
        }

        @Test
        void shouldReturnDodsorsakListWithItemsThatHasValues() {
            final var expectedDodsorsakList = List.of(
                Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.UPPGIFT_SAKNAS),
                Dodsorsak.create("beskrivning2", new InternalDate(LocalDate.now()), Specifikation.KRONISK),
                Dodsorsak.create("beskrivning3", new InternalDate(LocalDate.now()), Specifikation.PLOTSLIG)
            );

            final var dodsorsakList = List.of(
                expectedDodsorsakList.get(0),
                expectedDodsorsakList.get(1),
                expectedDodsorsakList.get(2),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null)
            );

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionBidragandeSjukdomar.toCertificate(dodsorsakList, 0, texts))
                .build();

            final var actualValue = QuestionBidragandeSjukdomar.toInternal(certificate);
            assertEquals(expectedDodsorsakList, actualValue);
        }

        @Test
        void shouldReturnDodsorsakListWithItemsThatHasValuesAndAnyEmptyBefore() {
            final var expectedDodsorsakList = List.of(
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.UPPGIFT_SAKNAS),
                Dodsorsak.create("beskrivning2", new InternalDate(LocalDate.now()), Specifikation.KRONISK)
            );

            final var dodsorsakList = List.of(
                expectedDodsorsakList.get(0),
                expectedDodsorsakList.get(1),
                expectedDodsorsakList.get(2),
                expectedDodsorsakList.get(3),
                expectedDodsorsakList.get(4),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null)
            );

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionBidragandeSjukdomar.toCertificate(dodsorsakList, 0, texts))
                .build();

            final var actualValue = QuestionBidragandeSjukdomar.toInternal(certificate);
            assertEquals(expectedDodsorsakList, actualValue);
        }

        @Test
        void shouldReturnDodsorsakListWithAllValues() {
            final var expectedDodsorsakList = List.of(
                Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.UPPGIFT_SAKNAS),
                Dodsorsak.create("beskrivning2", new InternalDate(LocalDate.now()), Specifikation.KRONISK),
                Dodsorsak.create("beskrivning3", new InternalDate(LocalDate.now()), Specifikation.PLOTSLIG),
                Dodsorsak.create("beskrivning4", new InternalDate(LocalDate.now()), Specifikation.UPPGIFT_SAKNAS),
                Dodsorsak.create("beskrivning5", new InternalDate(LocalDate.now()), Specifikation.KRONISK),
                Dodsorsak.create("beskrivning6", new InternalDate(LocalDate.now()), Specifikation.PLOTSLIG),
                Dodsorsak.create("beskrivning7", new InternalDate(LocalDate.now()), Specifikation.UPPGIFT_SAKNAS),
                Dodsorsak.create("beskrivning8", new InternalDate(LocalDate.now()), Specifikation.KRONISK)
            );

            final var dodsorsakList = List.of(
                expectedDodsorsakList.get(0),
                expectedDodsorsakList.get(1),
                expectedDodsorsakList.get(2),
                expectedDodsorsakList.get(3),
                expectedDodsorsakList.get(4),
                expectedDodsorsakList.get(5),
                expectedDodsorsakList.get(6),
                expectedDodsorsakList.get(7)
            );

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionBidragandeSjukdomar.toCertificate(dodsorsakList, 0, texts))
                .build();

            final var actualValue = QuestionBidragandeSjukdomar.toInternal(certificate);
            assertEquals(expectedDodsorsakList, actualValue);
        }

        @Test
        void shouldReturnEmptyDodsorsakListIfDodsorsakCodeAndLabelIsEmptyString() {
            final var expectedDodsorsakList = Collections.emptyList();
            final var dodsorsakList = List.of(
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null),
                Dodsorsak.create(null, null, null)
            );

            final var certificate = CertificateBuilder.create()
                .addElement(getElementWithEmptySpecification(dodsorsakList))
                .build();

            final var actualValue = QuestionBidragandeSjukdomar.toInternal(certificate);
            assertEquals(expectedDodsorsakList, actualValue);
        }

        private CertificateDataElement getElementWithEmptySpecification(List<Dodsorsak> dodsorsakList) {
            final var empty = "";
            final var certificateDataElement = QuestionBidragandeSjukdomar.toCertificate(dodsorsakList, 0, texts);
            final var list = ((CertificateDataValueCauseOfDeathList) certificateDataElement.getValue()).getList();
            final var questionBidragandeSjukdomar = CertificateDataElement.builder()
                .id(certificateDataElement.getId())
                .parent(certificateDataElement.getParent())
                .value(
                    CertificateDataValueCauseOfDeathList.builder()
                        .list(
                            list.stream().map(causeOfDeath -> CertificateDataValueCauseOfDeath.builder()
                                    .id(causeOfDeath.getId())
                                    .specification(
                                        CertificateDataValueCode.builder()
                                            .id(empty)
                                            .code(empty)
                                            .build()
                                    )
                                    .description(causeOfDeath.getDescription())
                                    .debut(causeOfDeath.getDebut())
                                    .build())
                                .collect(Collectors.toList())
                        )
                        .build()
                )
                .build();
            return questionBidragandeSjukdomar;
        }
    }
}