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
package se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.OVRIGT;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRANFORETAGSHALSOVARD;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRANPSYKOLOG;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRANSKOLHALSOVARD;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRAN_FYSIOTERAPEUT;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRAN_HABILITERINGEN;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRAN_LOGOPED;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_DATUM_TEXT;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_INFORMATION_SOURCE_DESCRIPTION_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_INFORMATION_SOURCE_TEXT_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_SVAR_ID_4;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_SVAR_JSON_ID_4;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_TYPE_TEXT_ID;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CodeItem;
import se.inera.intyg.common.support.facade.model.config.MedicalInvestigation;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueMedicalInvestigation;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueMedicalInvestigationList;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigMedicalInvestigationTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueMedicalInvestigationTest;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionUnderlagTest {

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
            return QuestionUnderlag.toCertificate(Collections.emptyList(), getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return UNDERLAG_SVAR_ID_4;
        }

        @Override
        protected String getParent() {
            return GRUNDFORMU_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 3;
        }
    }

    @Nested
    class IncludeConfigMedicalInvestigationTest extends ConfigMedicalInvestigationTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUnderlag.toCertificate(Collections.emptyList(), 0, textProvider);
        }

        @Override
        protected String getTypeText() {
            return UNDERLAG_TYPE_TEXT_ID;
        }

        @Override
        protected String getDateText() {
            return UNDERLAG_DATUM_TEXT;
        }

        @Override
        protected String getInformationSourceText() {
            return UNDERLAG_INFORMATION_SOURCE_TEXT_ID;
        }

        @Override
        protected String getInformationSourceDescription() {
            return UNDERLAG_INFORMATION_SOURCE_DESCRIPTION_ID;
        }

        @Override
        protected List<MedicalInvestigation> getMedicalInvestigations() {
            return List.of(
                MedicalInvestigation.builder()
                    .investigationTypeId(UNDERLAG_SVAR_JSON_ID_4 + "[0].typ")
                    .dateId(UNDERLAG_SVAR_JSON_ID_4 + "[0].datum")
                    .informationSourceId(UNDERLAG_SVAR_JSON_ID_4 + "[0].hamtasFran")
                    .typeOptions(getTypeOptions())
                    .build(),
                MedicalInvestigation.builder()
                    .investigationTypeId(UNDERLAG_SVAR_JSON_ID_4 + "[1].typ")
                    .dateId(UNDERLAG_SVAR_JSON_ID_4 + "[1].datum")
                    .informationSourceId(UNDERLAG_SVAR_JSON_ID_4 + "[1].hamtasFran")
                    .typeOptions(getTypeOptions())
                    .build(),
                MedicalInvestigation.builder()
                    .investigationTypeId(UNDERLAG_SVAR_JSON_ID_4 + "[2].typ")
                    .dateId(UNDERLAG_SVAR_JSON_ID_4 + "[2].datum")
                    .informationSourceId(UNDERLAG_SVAR_JSON_ID_4 + "[2].hamtasFran")
                    .typeOptions(getTypeOptions())
                    .build()
            );
        }

        @Override
        protected List<LocalDate> getMaxDates() {
            return Collections.nCopies(3, LocalDate.now());
        }

        @Override
        protected List<LocalDate> getMinDates() {
            return Collections.nCopies(3, null);
        }

        @Override
        protected String getTextId() {
            return null;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        private List<CodeItem> getTypeOptions() {
            return List.of(
                getCodeItem(NEUROPSYKIATRISKT_UTLATANDE),
                getCodeItem(UNDERLAG_FRAN_HABILITERINGEN),
                getCodeItem(UNDERLAG_FRAN_ARBETSTERAPEUT),
                getCodeItem(UNDERLAG_FRAN_FYSIOTERAPEUT),
                getCodeItem(UNDERLAG_FRAN_LOGOPED),
                getCodeItem(UNDERLAG_FRANPSYKOLOG),
                getCodeItem(UNDERLAG_FRANFORETAGSHALSOVARD),
                getCodeItem(UNDERLAG_FRANSKOLHALSOVARD),
                getCodeItem(UTREDNING_AV_ANNAN_SPECIALISTKLINIK),
                getCodeItem(UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS),
                getCodeItem(OVRIGT));
        }

        private CodeItem getCodeItem(UnderlagsTyp underlagsTyp) {
            return CodeItem.builder()
                .id(underlagsTyp.getId())
                .code(underlagsTyp.getId())
                .label(underlagsTyp.getLabel())
                .build();
        }
    }

    @Nested
    class IncludeValueMedicalInvestigationTest extends ValueMedicalInvestigationTest<List<Underlag>> {

        @Override
        protected CertificateDataElement getElement(List<Underlag> input) {
            return QuestionUnderlag.toCertificate(input, 0, textProvider);
        }

        @Override
        protected CertificateDataElement getElement() {
            return getElement(Collections.emptyList());
        }

        @Override
        protected List<InputExpectedValuePair<List<Underlag>, CertificateDataValueMedicalInvestigationList>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(
                    List.of(),
                    CertificateDataValueMedicalInvestigationList.builder()
                        .list(List.of(
                                getDefaultInvestigation(0),
                                getDefaultInvestigation(1),
                                getDefaultInvestigation(2)
                            )
                        ).build()
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        getUnderlag(UNDERLAG_FRANPSYKOLOG, new InternalDate("2022-12-16"), "fran psykolog")
                    ),
                    CertificateDataValueMedicalInvestigationList.builder()
                        .list(List.of(
                                getMedicalInvestigation(0, "2022-12-16", "fran psykolog", UNDERLAG_FRANPSYKOLOG),
                                getDefaultInvestigation(1),
                                getDefaultInvestigation(2)
                            )
                        ).build()
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        getUnderlag(UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate("2022-11-14"), "fran arbetsterapeut"),
                        getUnderlag(UNDERLAG_FRANSKOLHALSOVARD, new InternalDate("2022-11-15"), "fran skolhalsovard")
                    ),
                    CertificateDataValueMedicalInvestigationList.builder()
                        .list(List.of(
                                getMedicalInvestigation(0, "2022-11-14", "fran arbetsterapeut", UNDERLAG_FRAN_ARBETSTERAPEUT),
                                getMedicalInvestigation(1, "2022-11-15", "fran skolhalsovard", UNDERLAG_FRANSKOLHALSOVARD),
                                getDefaultInvestigation(2)
                            )
                        ).build()
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        getUnderlag(UNDERLAG_FRANPSYKOLOG, new InternalDate("2022-12-14"), "fran psykolog"),
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, new InternalDate("2022-12-15"), "fran neuro"),
                        getUnderlag(UNDERLAG_FRAN_LOGOPED, new InternalDate("2022-12-16"), "fran logoped")
                    ),
                    CertificateDataValueMedicalInvestigationList.builder()
                        .list(List.of(
                                getMedicalInvestigation(0, "2022-12-14", "fran psykolog", UNDERLAG_FRANPSYKOLOG),
                                getMedicalInvestigation(1, "2022-12-15", "fran neuro", NEUROPSYKIATRISKT_UTLATANDE),
                                getMedicalInvestigation(2, "2022-12-16", "fran logoped", UNDERLAG_FRAN_LOGOPED)
                            )
                        ).build()
                )
            );
        }

        private Underlag getUnderlag(UnderlagsTyp type, InternalDate date, String hamtasFran) {
            return Underlag.create(type, date, hamtasFran);
        }

        private CertificateDataValueMedicalInvestigation getMedicalInvestigation(int id, String date, String text, UnderlagsTyp code) {
            return CertificateDataValueMedicalInvestigation.builder()
                .date(CertificateDataValueDate.builder()
                    .id(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].datum")
                    .date(LocalDate.parse(date)).build())
                .informationSource(CertificateDataTextValue.builder()
                    .id(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].hamtasFran")
                    .text(text).build())
                .investigationType(CertificateDataValueCode.builder()
                    .id(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].typ")
                    .code(code.getId()).build())
                .build();
        }

        private CertificateDataValueMedicalInvestigation getDefaultInvestigation(int id) {
            return CertificateDataValueMedicalInvestigation.builder()
                .date(CertificateDataValueDate.builder()
                    .id(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].datum").build())
                .informationSource(CertificateDataTextValue.builder()
                    .id(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].hamtasFran").build())
                .investigationType(CertificateDataValueCode.builder()
                    .id(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].typ").build())
                .build();
        }
    }

    @Nested
    class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUnderlag.toCertificate(Collections.emptyList(), 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }

        @Override
        protected String getQuestionId() {
            return UNDERLAG_SVAR_ID_4;
        }

        @Override
        protected String getExpression() {
            return "!empty('" + UNDERLAG_SVAR_JSON_ID_4 + "[0].typ')" + " && " + "!empty('" + UNDERLAG_SVAR_JSON_ID_4 + "[0].datum')"
                + " && " + "!empty('" + UNDERLAG_SVAR_JSON_ID_4 + "[0].hamtasFran')";
        }
    }

    @Nested
    class IncludeValidationShowTest extends ValidationShowTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUnderlag.toCertificate(Collections.emptyList(), 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }

        @Override
        protected String getQuestionId() {
            return UNDERLAGFINNS_SVAR_ID_3;
        }

        @Override
        protected String getExpression() {
            return "$" + UNDERLAGFINNS_SVAR_JSON_ID_3;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalMedicalInvestigationTest extends InternalValueTest<List<Underlag>, List<Underlag>> {

        @Override
        protected CertificateDataElement getElement(List<Underlag> input) {
            return QuestionUnderlag.toCertificate(input, 0, textProvider);
        }

        @Override
        protected List<Underlag> toInternalValue(Certificate certificate) {
            return QuestionUnderlag.toInternal(certificate);
        }

        @Override
        protected List<InputExpectedValuePair<List<Underlag>, List<Underlag>>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(
                    List.of(
                        getUnderlag(UNDERLAG_FRANPSYKOLOG, new InternalDate("2022-12-14"), "fran psykolog"),
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, new InternalDate("2022-12-15"), "fran neuro"),
                        getUnderlag(UNDERLAG_FRAN_LOGOPED, new InternalDate("2022-12-16"), "fran logoped")
                    ),
                    List.of(
                        getUnderlag(UNDERLAG_FRANPSYKOLOG, new InternalDate("2022-12-14"), "fran psykolog"),
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, new InternalDate("2022-12-15"), "fran neuro"),
                        getUnderlag(UNDERLAG_FRAN_LOGOPED, new InternalDate("2022-12-16"), "fran logoped")
                    )
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        getUnderlag(UNDERLAG_FRANPSYKOLOG, new InternalDate("2022-12-14"), "fran psykolog")
                    ),
                    List.of(
                        getUnderlag(UNDERLAG_FRANPSYKOLOG, new InternalDate("2022-12-14"), "fran psykolog")
                    )
                ),
                new InputExpectedValuePair<>(
                    List.of(),
                    List.of()
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        getUnderlag(UNDERLAG_FRANPSYKOLOG, new InternalDate("2022-12-14"), "fran psykolog"),
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, new InternalDate("2022-12-15"), "fran neuro"),
                        getUnderlag(null, null, null)
                    ),
                    List.of(
                        getUnderlag(UNDERLAG_FRANPSYKOLOG, new InternalDate("2022-12-14"), "fran psykolog"),
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, new InternalDate("2022-12-15"), "fran neuro")
                    )
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, null, null),
                        getUnderlag(null, null, null),
                        getUnderlag(null, null, null)
                    ),
                    List.of(
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, null, null)
                    )
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        getUnderlag(null, null, null),
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, null, null),
                        getUnderlag(null, null, null)
                    ),
                    List.of(
                        getUnderlag(null, null, null),
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, null, null)
                    )
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        getUnderlag(null, null, null),
                        getUnderlag(null, null, null),
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, null, null)
                    ),
                    List.of(
                        getUnderlag(null, null, null),
                        getUnderlag(null, null, null),
                        getUnderlag(NEUROPSYKIATRISKT_UTLATANDE, null, null)
                    )
                )
            );
        }

        private Underlag getUnderlag(UnderlagsTyp type, InternalDate date, String hamtasFran) {
            return Underlag.create(type, date, hamtasFran);
        }
    }
}
