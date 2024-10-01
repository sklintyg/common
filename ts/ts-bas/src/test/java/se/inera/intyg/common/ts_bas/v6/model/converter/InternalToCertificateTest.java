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

package se.inera.intyg.common.ts_bas.v6.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.BALANSRUBBNINGAR_YRSEL_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.BALANSRUBBNINGAR_YRSEL_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.BEHANDLING_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.BEHORIGHET_LAKARE_SPECIALKOMPETENS_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.DEMENS_KOGNITIV_FUNKTION_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.DUBBELSEENDE_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.FOREKOMST_MEDVETANDESTORNING_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.HAR_DIABETES_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.HAR_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.HJART_ELLER_KARLSJUKDOM_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.HJART_ELLER_KARLSJUKDOM_RISKFAKTORER_STROKE_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.HJART_ELLER_KARLSJUKDOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.HJART_ELLER_KARLSJUKDOM_TECKEN_PA_HJARNSKADA_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.HJART_ELLER_KARLSJUKDOM_TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.IDENTITET_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.IDENTITET_STYRKT_GENOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.INTYG_AVSER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.LAKARINTYG_AV_OGONSPECIALIST_MESSAGE_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.LAKEMEDEL_ORDINERAD_DOS_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.MEDICINER_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.MEDVETANDESTORNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.MEDVETANDESTORNING_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.MISSBRUK_BEROENDE_LAKEMEDEL_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.NEDSATT_NJURFUNKTION_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.NEUROLOGISK_SJUKDOM_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.NJURFUNKTION_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.NYSTAGMUS_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.OTILLRACKLIG_RORELSEFORMAGA_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.OVRIGA_KOMMENTARER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.OVRIGA_KOMMENTARER_DELSVARSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PLATS_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PROGRESSIV_OGONSJUKDOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_MESSAGE_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PSYKISK_SJUKDOM_STORNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PSYKISK_SJUKDOM_STORNING_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PSYKISK_SYNDROM_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PSYKISK_UTVECKLINGSSTORNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SEENDE_NEDSATT_BELYSNING_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SJUKDOM_FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SJUKDOM_FUNKTIONSNEDSATTNING_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.STADIGVARANDE_MEDICINERING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNFALTSDEFEKTER_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNFUNKTIONER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.TECKEN_MISSBRUK_BEROENDE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.TECKEN_NEUROLOGISK_SJUKDOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.TIDPUNKT_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.TYP_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_MESSAGE_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.UPPFATTA_SAMTALSTAMMA_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.VARDEN_FOR_SYNSKARPA_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.VARDINSATSER_MISSBRUK_BEROENDE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.VARD_SJUKHUS_KONTAKT_LAKARE_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULIN_ELLER_TABLETT_MESSAGE_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.ts_bas.v6.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v6.model.internal.Synskarpevarden;
import se.inera.intyg.common.ts_bas.v6.model.internal.TsBasUtlatandeV6;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class InternalToCertificateTest {

    @Mock
    CertificateTextProvider textProvider;

    private InternalToCertificate internalToCertificate;

    private TsBasUtlatandeV6 internalCertificate;

    @BeforeEach
    void setUp() {
        internalToCertificate = new InternalToCertificate();
        internalCertificate = TsBasUtlatandeV6.builder()
            .setId("certificateId")
            .setTextVersion("1.0")
            .setGrundData(getGrundData())
            .setSyn(
                Syn.builder()
                    .setVansterOga(
                        Synskarpevarden.builder().build()
                    )
                    .setHogerOga(
                        Synskarpevarden.builder().build()
                    )
                    .setBinokulart(
                        Synskarpevarden.builder().build()
                    )
                    .build()
            )
            .build();
    }

    private static GrundData getGrundData() {
        final var unit = new Vardenhet();
        final var skapadAv = new HoSPersonal();
        final var patient = new Patient();
        final var grundData = new GrundData();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        skapadAv.setVardenhet(unit);
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        return grundData;
    }

    @Test
    void shallIncludeMetadata() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertNotNull(actualCertificate.getMetadata(), "Shall contain metadata");
    }

    @Test
    void shallIncludeCategoryIntygetAvser() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(0, actualCertificate.getData().get(INTYG_AVSER_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIntygetAvser() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(1, actualCertificate.getData().get(INTYG_AVSER_SVAR_ID_1).getIndex());
    }

    @Test
    void shallIncludeCategoryIdentitet() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(2, actualCertificate.getData().get(IDENTITET_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIdentitet() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(3, actualCertificate.getData().get(IDENTITET_STYRKT_GENOM_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategorySynfunktioner() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(4, actualCertificate.getData().get(SYNFUNKTIONER_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSynfaltsdefekter() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(5, actualCertificate.getData().get(SYNFALTSDEFEKTER_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionNattblindhet() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(6, actualCertificate.getData().get(SEENDE_NEDSATT_BELYSNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionProgressivOgonsjukdom() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(7, actualCertificate.getData().get(PROGRESSIV_OGONSJUKDOM_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeLakarintygMessage() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(8, actualCertificate.getData().get(LAKARINTYG_AV_OGONSPECIALIST_MESSAGE_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDubbelseende() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(9, actualCertificate.getData().get(DUBBELSEENDE_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionNystagmus() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(10, actualCertificate.getData().get(NYSTAGMUS_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSynskarpa() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(11, actualCertificate.getData().get(VARDEN_FOR_SYNSKARPA_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionKorrektionsglasensStyrka() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(12, actualCertificate.getData().get(UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeMessageKorrektionsglasensStyrka() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(13, actualCertificate.getData().get(UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_MESSAGE_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryHorselOchBalanssinne() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(14, actualCertificate.getData().get(BALANSRUBBNINGAR_YRSEL_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionBalansrubbningar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(15, actualCertificate.getData().get(BALANSRUBBNINGAR_YRSEL_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionUppfattaSamtal4Meter() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(16, actualCertificate.getData().get(UPPFATTA_SAMTALSTAMMA_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryFunktionsnedsattningar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(17, actualCertificate.getData().get(SJUKDOM_FUNKTIONSNEDSATTNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(18, actualCertificate.getData().get(SJUKDOM_FUNKTIONSNEDSATTNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningBeskrivning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(19, actualCertificate.getData().get(TYP_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOtillrackligRorelseformoga() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(20, actualCertificate.getData().get(OTILLRACKLIG_RORELSEFORMAGA_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryHjartOchKarlsjukdom() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(21, actualCertificate.getData().get(HJART_ELLER_KARLSJUKDOM_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHjartOchKarlsjukdom() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(22, actualCertificate.getData().get(HJART_ELLER_KARLSJUKDOM_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHjarnskadaEfterTrauama() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(23, actualCertificate.getData().get(HJART_ELLER_KARLSJUKDOM_TECKEN_PA_HJARNSKADA_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionRiskfaktorerEfterStroke() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(24, actualCertificate.getData().get(HJART_ELLER_KARLSJUKDOM_RISKFAKTORER_STROKE_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionBeskrivningRiskfaktorer() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(25, actualCertificate.getData().get(HJART_ELLER_KARLSJUKDOM_TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryDiabetes() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(26, actualCertificate.getData().get(HAR_DIABETES_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHarDiabetes() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(27, actualCertificate.getData().get(HAR_DIABETES_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiabetesTyp() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(28, actualCertificate.getData().get(TYP_AV_DIABETES_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiabetesBehandling() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(29, actualCertificate.getData().get(BEHANDLING_DIABETES_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionTablettEllerInsulinMessage() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(30, actualCertificate.getData().get(INSULIN_ELLER_TABLETT_MESSAGE_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryNeurologiskaSjukdomar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(31, actualCertificate.getData().get(NEUROLOGISK_SJUKDOM_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionTeckenNeurologiskaSjukdomar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(32, actualCertificate.getData().get(TECKEN_NEUROLOGISK_SJUKDOM_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryMedvetandestorning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(33, actualCertificate.getData().get(MEDVETANDESTORNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionMedvetandestorning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(34, actualCertificate.getData().get(MEDVETANDESTORNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionMedvetandestorningBeskrivning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(35, actualCertificate.getData().get(FOREKOMST_MEDVETANDESTORNING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludCategoryNjursjukdomar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(36, actualCertificate.getData().get(NJURFUNKTION_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludQuestionNedsattNjurfunktion() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(37, actualCertificate.getData().get(NEDSATT_NJURFUNKTION_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludCategoryDemensOchAndraKognitivaStorningar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(38, actualCertificate.getData().get(DEMENS_KOGNITIV_FUNKTION_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionKognitivaFormoga() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(39, actualCertificate.getData().get(TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategorySomnOchVakenhetsstorningar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(40, actualCertificate.getData().get(TECKEN_SOMN_ELLER_VAKENHETSSTORNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSomnOchVakenhetsstorningar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(41, actualCertificate.getData().get(TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryAlkoholNarkotikaOchLakemedel() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(42, actualCertificate.getData().get(MISSBRUK_BEROENDE_LAKEMEDEL_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAlkoholNarkotikaTeckenMissbruk() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(43, actualCertificate.getData().get(TECKEN_MISSBRUK_BEROENDE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAlkoholNarkotikaVardinsatser() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(44, actualCertificate.getData().get(VARDINSATSER_MISSBRUK_BEROENDE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAlkoholNarkotikaProvtagning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(45, actualCertificate.getData().get(PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAlkoholNarkotikaProvtagningMessage() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(46, actualCertificate.getData().get(PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_MESSAGE_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAlkoholNarkotikaLakarordineratLakemedelsbruk() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(47, actualCertificate.getData().get(REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAlkoholNarkotikaLakarordineratDos() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(48, actualCertificate.getData().get(LAKEMEDEL_ORDINERAD_DOS_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryPsykiskSjukdom() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(49, actualCertificate.getData().get(PSYKISK_SJUKDOM_STORNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionPsykiskSjukdom() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(50, actualCertificate.getData().get(PSYKISK_SJUKDOM_STORNING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryPsykiskUtvecklingsstorning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(51, actualCertificate.getData().get(PSYKISK_UTVECKLINGSSTORNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionPsykiskUtvecklingsstorning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(52, actualCertificate.getData().get(PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionPsykiskSyndrom() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(53, actualCertificate.getData().get(PSYKISK_SYNDROM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategorySjukhusvard() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(54, actualCertificate.getData().get(VARD_SJUKHUS_KONTAKT_LAKARE_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionVardatsPaSjukhus() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(55, actualCertificate.getData().get(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionTidpunktVardPaSjukhus() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(56, actualCertificate.getData().get(TIDPUNKT_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionVardinrattningensNamn() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(57, actualCertificate.getData().get(PLATS_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionVardatsPaSjukhusOrsak() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(58, actualCertificate.getData().get(ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryOvrigMedicinering() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(59, actualCertificate.getData().get(STADIGVARANDE_MEDICINERING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionStadigvarandeMedicinering() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(60, actualCertificate.getData().get(FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionStadigvarandeMedicineringBeskrivning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(61, actualCertificate.getData().get(MEDICINER_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryOvrigKommentar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(62, actualCertificate.getData().get(OVRIGA_KOMMENTARER_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOvrigKommentar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(63, actualCertificate.getData().get(OVRIGA_KOMMENTARER_DELSVARSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryBedomning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(64, actualCertificate.getData().get(BEDOMNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionBedomningKorkortsTyp() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(65, actualCertificate.getData().get(LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionBedomningLakareSpecialkompetens() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(66, actualCertificate.getData().get(BEHORIGHET_LAKARE_SPECIALKOMPETENS_SVAR_ID).getIndex());
    }
}
