/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.lisjp.v1.model.converter;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SYSSELSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleAndExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.not;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.wrapWithParenthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.DiagnosesListItem;
import se.inera.intyg.common.support.facade.model.config.DiagnosesTerminology;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosis;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;

public final class InternalToCertificate {

    private InternalToCertificate() {

    }

    public static Certificate convert(LisjpUtlatandeV1 internalCertificate) {
        var index = 0;
        return CertificateBuilder.create()
            .metadata(createMetadata(internalCertificate))
            .addElement(createSmittbararpenningCategory(index++))
            .addElement(createAvstangningSmittskyddQuestion(internalCertificate, index++))
            .addElement(createGrundForMUCategory(index++))
            .addElement(createIntygetBaseratPa(internalCertificate, index++))
            .addElement(createAnnatGrundForBeskrivning(internalCertificate, index++))
            .addElement(createMotiveringEjUndersokning(internalCertificate, index++))
            .addElement(createSysselsattningCategory(index++))
            .addElement(createSysselsattningQuestion(internalCertificate, index++))
            .addElement(createSysselsattningYrkeQuestion(internalCertificate, index++))
            .addElement(createDiagnosCategory(index++))
            .addElement(createDiagnosQuestion(internalCertificate, index++))
            .build();
    }

    @SuppressWarnings("CheckStyle")
    private static CertificateMetadata createMetadata(LisjpUtlatandeV1 internalCertificate) {
        final var unit = internalCertificate.getGrundData().getSkapadAv().getVardenhet();
        return CertificateMetadata.builder()
            .id(internalCertificate.getId())
            .type(internalCertificate.getTyp())
            .typeVersion(internalCertificate.getTextVersion())
            .name(LisjpEntryPoint.MODULE_NAME)
            .description("Om du inte känner patienten ska hen styrka sin identitet genom legitimation med foto (HSLF-FS 2018:54).\n"
                + "\n"
                + "        Vad är sjukpenning?\n"
                + "        Sjukpenning är en ersättning för personer som har en nedsatt arbetsförmåga på grund av sjukdom. Beroende på hur mycket arbetsförmågan är nedsatt kan man få en fjärdedels, halv, tre fjärdedels eller hel sjukpenning.\n"
                + "\n"
                + "        Förutsättningar för att få sjukpenning\n"
                + "        Arbetsförmågan bedöms i förhållande till personens sysselsättning som kan vara nuvarande arbete eller föräldraledighet för vård av barn. För personer som är arbetslösa bedöms arbetsförmågan i förhållandet till att utföra sådant arbete som är normalt förekommande på arbetsmarknaden.\n"
                + "\n"
                + "        För att få sjukpenning ska man, förutom att ha nedsatt arbetsförmåga, tillhöra svensk socialförsäkring och ha inkomst från arbete. Försäkringskassan beslutar om och hur mycket sjukpenning personen kan få.\n"
                + "\n"
                + "        Från den åttonde dagen i en sjukperiod måste det finnas ett läkarintyg. Läkarintyget ska tydligt beskriva hur patientens sjukdom påverkar patientens förmåga att utföra sin sysselsättning.\n"
                + "\n"
                + "        Studerande\n"
                + "        En person kan få behålla sitt studiemedel om förmågan att studera är helt eller halvt nedsatt på grund av sjukdom. Försäkringskassan bedömer om förmågan är nedsatt och om studieperioden ska godkännas till CSN.\n"
                + "\n"
                + "        Den som ansöker om att få behålla sitt studiemedel ska från och med den femtonde dagen i sjukperioden styrka sin nedsatta studieförmåga med ett läkarintyg.\n"
                + "\n"
                + "        Smittbärarpenning\n"
                + "        En person kan få ersättning om hen måste avstå från sin sysselsättning på grund av läkarens beslut om avstängning enligt smittskyddslagen eller läkarundersökning alternativt hälsokontroll som syftar till att klarlägga sjukdom, smitta, sår eller annan skada som gör att hen inte får hantera livsmedel.\n"
                + "\n"
                + "        Den som ansöker om smittbärarpenning ska skicka med ett läkarintyg.\n"
                + "\n"
                + "        Mer om hur Försäkringskassan bedömer arbetsförmågan\n"
                + "        Försäkringskassan bedömer arbetsförmågan enligt rehabiliteringskedjan, som innebär följande:\n"
                + "        - Under de första 90 dagarna som personen är sjukskriven kan Försäkringskassan betala sjukpenning om personen inte kan utföra sitt vanliga arbete eller ett annat tillfälligt arbete hos sin arbetsgivare.\n"
                + "        - Efter 90 dagar kan Försäkringskassan betala sjukpenning om personen inte kan utföra något arbete alls hos sin arbetsgivare.\n"
                + "        - Efter 180 dagar kan Försäkringskassan betala ut sjukpenning om personen inte kan utföra sådant arbete som är normalt förekommande på arbetsmarknaden. Men detta gäller inte om Försäkringskassan bedömer att personen med stor sannolikhet kommer att kunna gå tillbaka till ett arbete hos sin arbetsgivare innan dag 366. I dessa fall bedöms arbetsförmågan i förhållande till ett arbete hos arbetsgivaren även efter dag 180. Regeln gäller inte heller om det kan anses oskäligt att bedöma personens arbetsförmåga i förhållande till arbete som är normalt förekommande på arbetsmarknaden.\n"
                + "        - Efter 365 dagar kan Försäkringskassan betala ut sjukpenning om personen inte kan utföra sådant arbete som är normalt förekommande på arbetsmarknaden. Undantag från detta kan göras om det kan anses oskäligt att bedöma personens arbetsförmåga i förhållande till sådant arbete som normalt förekommer på arbetsmarknaden.\n"
                + "\n"
                + "        Rehabiliteringskedjan gäller fullt ut bara för den som har en anställning.\n"
                + "\n"
                + "        Egna företagares arbetsförmåga bedöms i förhållande till de vanliga arbetsuppgifterna fram till och med dag 180. Sedan bedöms arbetsförmågan i förhållande till sådant arbete som normalt förekommer på arbetsmarknaden.\n"
                + "\n"
                + "        För arbetslösa bedöms arbetsförmågan i förhållande till arbeten som normalt förekommer på arbetsmarknaden redan från första dagen i sjukperioden.")
            .unit(
                Unit.builder()
                    .unitId(unit.getEnhetsid())
                    .unitName(unit.getEnhetsnamn())
                    .address(unit.getPostadress())
                    .zipCode(unit.getPostnummer())
                    .city(unit.getPostort())
                    .email(unit.getEpost())
                    .phoneNumber(unit.getTelefonnummer())
                    .build()
            )
            .build();
    }

    @SuppressWarnings("CheckStyle")
    private static CertificateDataElement createSmittbararpenningCategory(int index) {
        return CertificateDataElement.builder()
            .id(AVSTANGNING_SMITTSKYDD_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Smittbärarpenning")
                    .description("Fylls i om hon eller han måste avstå från sitt arbete på grund av:\n"
                        + "        - Intygsskrivande läkares beslut enligt smittskyddslagen\n"
                        + "        - Läkarundersökning eller hälsokontroll som syftar till att klarlägga om hon eller han är smittad av en allmänfarlig sjukdom eller om personen har en sjukdom, en smitta, ett sår eller annan skada som gör att hon eller han inte får hantera livsmedel.")
                    .build()
            )
            .build();
    }

    @SuppressWarnings("CheckStyle")
    private static CertificateDataElement createAvstangningSmittskyddQuestion(LisjpUtlatandeV1 internalCertificate, int index) {
        return CertificateDataElement.builder()
            .id(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
            .index(index)
            .parent(AVSTANGNING_SMITTSKYDD_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27)
                    .label(
                        "Avstängning enligt smittskyddslagen på grund av smitta. (Fortsätt till frågorna \"Diagnos\" och \"Nedsättning av arbetsförmåga\".")
                    .selectedText("Ja")
                    .unselectedText("Nej")
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27)
                    .selected(internalCertificate.getAvstangningSmittskydd())
                    .build()
            )
            .build();
    }

    @SuppressWarnings("CheckStyle")
    private static CertificateDataElement createGrundForMUCategory(int index) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMU_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Grund för medicinskt underlag")
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    @SuppressWarnings("CheckStyle")
    private static CertificateDataElement createIntygetBaseratPa(LisjpUtlatandeV1 internalCertificate, int index) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .index(index)
            .parent(GRUNDFORMU_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxMultipleDate.builder()
                    .text("Intyget är baserat på")
                    .description("Enligt Socialstyrelsens föreskrifter (HSLF-FS 2018:54) om att utfärda intyg i hälso- och\n"
                        + "        sjukvården ska ett läkarintyg innehålla uppgifter om vad som ligger till grund för din bedömning vid utfärdandet\n"
                        + "        av intyget. Ett intyg ska som huvudregel utfärdas efter en undersökning av patienten. Intyget ska innehålla\n"
                        + "        uppgift om kontaktsätt vid undersökningen. Om kontaktsättet är videosamtal anger du detta under fältet Övriga\n"
                        + "        upplysningar.")
                    .list(
                        Arrays.asList(
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                                .label("min undersökning av patienten")
                                .build(),
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                                .label("min telefonkontakt med patienten")
                                .build(),
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                                .label("journaluppgifter från den")
                                .build(),
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                                .label("annat")
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueDateList.builder()
                    .list(createIntygetBaseratPaValue(internalCertificate))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                        .expression(
                            multipleOrExpression(
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1),
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1),
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1),
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                            ))
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueDate> createIntygetBaseratPaValue(LisjpUtlatandeV1 internalCertificate) {
        final List<CertificateDataValueDate> values = new ArrayList<>();

        if (internalCertificate.getUndersokningAvPatienten() != null) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                    .date(internalCertificate.getUndersokningAvPatienten().asLocalDate())
                    .build()
            );
        }

        if (internalCertificate.getTelefonkontaktMedPatienten() != null) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                    .date(internalCertificate.getTelefonkontaktMedPatienten().asLocalDate())
                    .build()
            );
        }

        if (internalCertificate.getJournaluppgifter() != null) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                    .date(internalCertificate.getJournaluppgifter().asLocalDate())
                    .build()
            );
        }

        if (internalCertificate.getAnnatGrundForMU() != null) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                    .date(internalCertificate.getAnnatGrundForMU().asLocalDate())
                    .build()
            );
        }
        return values;
    }

    private static CertificateDataElement createAnnatGrundForBeskrivning(LisjpUtlatandeV1 internalCertificate, int index) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1)
            .index(index)
            .parent(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1)
                    .text("Ange vad annat är")
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1)
                    .text(internalCertificate.getAnnatGrundForMUBeskrivning())
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1)
                        .expression(singleExpression(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                        .expression(singleExpression(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1))
                        .build()
                }
            )
            .build();
    }

    @SuppressWarnings("CheckStyle")
    private static CertificateDataElement createMotiveringEjUndersokning(LisjpUtlatandeV1 internalCertificate, int index) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1)
            .index(index)
            .parent(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text("Motivering till varför det medicinska underlaget inte baseras på en undersökning av patienten")
                    .description(
                        "Observera att detta inte är en fråga från Försäkringskassan. Information om varför sjukskrivningen startar mer än en vecka före dagens datum kan vara till hjälp för Försäkringskassan i deras handläggning.\nInformationen du anger nedan, kommer att överföras till fältet \"Övriga upplysningar\" vid signering.")
                    .icon("lightbulb_outline")
                    .id(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1)
                    .text(internalCertificate.getMotiveringTillInteBaseratPaUndersokning())
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                        .expression(
                            multipleAndExpression(
                                not(
                                    singleExpression(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                                ),
                                wrapWithParenthesis(
                                    multipleOrExpression(
                                        singleExpression(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1),
                                        singleExpression(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1),
                                        singleExpression(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                                    )
                                )
                            )
                        )
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1)
                        .limit((short) 150)
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createSysselsattningCategory(int index) {
        return CertificateDataElement.builder()
            .id(SYSSELSATTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Sysselsättning")
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    @SuppressWarnings("CheckStyle")
    private static CertificateDataElement createSysselsattningQuestion(LisjpUtlatandeV1 internalCertificate, int index) {
        return CertificateDataElement.builder()
            .id(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
            .index(index)
            .parent(SYSSELSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text("I relation till vilken sysselsättning bedömer du arbetsförmågan?")
                    .description(
                        "Om du kryssar i flera val är det viktigt att du tydliggör under \"Övriga upplysningar\" om sjukskrivningens omfattning eller period skiljer sig åt mellan olika sysselsättningar.")
                    .list(
                        Arrays.asList(
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                                .label("Nuvarande arbete")
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.ARBETSSOKANDE.getId())
                                .label("Arbetssökande - att utföra sådant arbete som är normalt förekommande på arbetsmarknaden")
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.getId())
                                .label("Föräldraledighet för vård av barn")
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.STUDIER.getId())
                                .label("Studier")
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(createSysselsattningCodeList(internalCertificate))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
                        .expression(
                            multipleOrExpression(
                                singleExpression(SysselsattningsTyp.NUVARANDE_ARBETE.getId()),
                                singleExpression(SysselsattningsTyp.ARBETSSOKANDE.getId()),
                                singleExpression(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.getId()),
                                singleExpression(SysselsattningsTyp.STUDIER.getId())
                            )
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueCode> createSysselsattningCodeList(LisjpUtlatandeV1 internalCertificate) {
        if (internalCertificate.getSysselsattning() == null) {
            return Collections.emptyList();
        }

        return internalCertificate.getSysselsattning().stream()
            .map(sysselsattning -> CertificateDataValueCode.builder()
                .id(sysselsattning.getTyp().getId())
                .code(sysselsattning.getTyp().getId())
                .build())
            .collect(Collectors.toList());
    }

    private static CertificateDataElement createSysselsattningYrkeQuestion(LisjpUtlatandeV1 internalCertificate, int index) {
        return CertificateDataElement.builder()
            .id(NUVARANDE_ARBETE_SVAR_ID_29)
            .index(index)
            .parent(SYSSELSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text("Ange yrke och arbetsuppgifter")
                    .id(NUVARANDE_ARBETE_SVAR_JSON_ID_29)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(NUVARANDE_ARBETE_SVAR_JSON_ID_29)
                    .text(internalCertificate.getNuvarandeArbete())
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(NUVARANDE_ARBETE_SVAR_ID_29)
                        .expression(singleExpression(NUVARANDE_ARBETE_SVAR_JSON_ID_29))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
                        .expression(singleExpression(SysselsattningsTyp.NUVARANDE_ARBETE.getId()))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createDiagnosCategory(int index) {
        return CertificateDataElement.builder()
            .id(DIAGNOS_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Diagnos")
                    .build()
            )
            .build();
    }

    @SuppressWarnings("CheckStyle")
    private static CertificateDataElement createDiagnosQuestion(LisjpUtlatandeV1 internalCertificate, int index) {
        return CertificateDataElement.builder()
            .id(DIAGNOS_SVAR_ID_6)
            .index(index)
            .parent(DIAGNOS_CATEGORY_ID)
            .config(
                CertificateDataConfigDiagnoses.builder()
                    .text("Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga")
                    .description(
                        "Ange vilken eller vilka sjukdomar som orsakar nedsatt arbetsförmåga. Den sjukdom som påverkar arbetsförmågan mest anges först. Diagnoskoden anges alltid med så många positioner som möjligt.\n        Om patienten har fler än tre sjukdomar som påverkar arbetsförmågan anges dessa under \"övriga upplysningar\".")
                    .terminology(
                        Arrays.asList(
                            DiagnosesTerminology.builder()
                                .id("ICD_10_SE")
                                .label("ICD-10-SE")
                                .build(),
                            DiagnosesTerminology.builder()
                                .id("KSH_97_P")
                                .label("KSH97-P (Primärvård)")
                                .build()
                        )
                    )
                    .list(
                        Arrays.asList(
                            DiagnosesListItem.builder()
                                .id("1")
                                .build(),
                            DiagnosesListItem.builder()
                                .id("2")
                                .build(),
                            DiagnosesListItem.builder()
                                .id("3")
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueDiagnosisList.builder()
                    .list(createDiagnosValue(internalCertificate))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DIAGNOS_SVAR_ID_6)
                        .expression(singleExpression("1"))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueDiagnosis> createDiagnosValue(LisjpUtlatandeV1 internalCertificate) {
        if (isEmpty(internalCertificate)) {
            return Collections.emptyList();
        }

        final List<CertificateDataValueDiagnosis> diagnoses = new ArrayList<>();
        for (int i = 0; i < internalCertificate.getDiagnoser().size(); i++) {
            final var diagnos = internalCertificate.getDiagnoser().get(i);
            if (isInvalid(diagnos)) {
                continue;
            }

            diagnoses.add(createDiagnosis(Integer.toString(i + 1), diagnos));
        }

        return diagnoses;
    }

    private static boolean isInvalid(Diagnos diagnos) {
        return diagnos.getDiagnosKod() == null;
    }

    private static boolean isEmpty(LisjpUtlatandeV1 internalCertificate) {
        return internalCertificate.getDiagnoser() == null;
    }

    private static CertificateDataValueDiagnosis createDiagnosis(String id, Diagnos diagnos) {
        return CertificateDataValueDiagnosis.builder()
            .id(id)
            .terminology(diagnos.getDiagnosKodSystem())
            .code(diagnos.getDiagnosKod())
            .description(diagnos.getDiagnosDisplayName())
            .build();
    }
}
