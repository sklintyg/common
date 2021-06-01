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

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_JSON_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ATGARDER_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SYSSELSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.appendAttribute;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleAndExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.not;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.wrapWithParenthesis;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.lessThan;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDropdown;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigSickLeavePeriod;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CheckboxDateRange;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.DiagnosesListItem;
import se.inera.intyg.common.support.facade.model.config.DiagnosesTerminology;
import se.inera.intyg.common.support.facade.model.config.DropdownItem;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationEnable;
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
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosis;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;

public final class InternalToCertificate {

    private static final short LIMIT_MOTIVERING_INTE_BASERAT_PA_UNDERLAG = (short) 150;
    private static final short LIMIT_MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING = (short) 150;
    private static final short LIMIT_OVRIGT = (short) 3296;

    private static final String VALIDATION_DAYS_TIDIGT_START_DATUM = "-7";


    private InternalToCertificate() {

    }

    public static Certificate convert(LisjpUtlatandeV1 internalCertificate) {
        var index = 0;
        return CertificateBuilder.create()
            .metadata(createMetadata(internalCertificate))
            .addElement(createSmittbararpenningCategory(index++))
            .addElement(createAvstangningSmittskyddQuestion(internalCertificate.getAvstangningSmittskydd(), index++))
            .addElement(createGrundForMUCategory(index++))
            .addElement(createIntygetBaseratPa(internalCertificate, index++))
            .addElement(createAnnatGrundForMUBeskrivning(internalCertificate.getAnnatGrundForMUBeskrivning(), index++))
            .addElement(createMotiveringEjUndersokning(internalCertificate.getMotiveringTillInteBaseratPaUndersokning(), index++))
            .addElement(createSysselsattningCategory(index++))
            .addElement(createSysselsattningQuestion(internalCertificate.getSysselsattning(), index++))
            .addElement(createSysselsattningYrkeQuestion(internalCertificate, index++))
            .addElement(createDiagnosCategory(index++))
            .addElement(createDiagnosQuestion(internalCertificate.getDiagnoser(), index++))
            .addElement(createFunktionsnedsattningCategory(index++))
            .addElement(createFunktionsnedsattningQuestion(internalCertificate.getFunktionsnedsattning(), index++))
            .addElement(createAktivitetsbegransningQuestion(internalCertificate.getAktivitetsbegransning(), index++))
            .addElement(createMedicinskaBehandlingarCategory(index++))
            .addElement(createPagaendeBehandlingQuestion(internalCertificate.getPagaendeBehandling(), index++))
            .addElement(createPlaneradBehandlingQuestion(internalCertificate.getPlaneradBehandling(), index++))
            .addElement(createBedomningCategory(index++))
            .addElement(createBehovAvSjukskrivningQuestion(internalCertificate.getSjukskrivningar(), index++))
            .addElement(createMotiveringTidigtStartdatumQuestion(internalCertificate.getMotiveringTillTidigtStartdatumForSjukskrivning(),
                index++))
            .addElement(createForsakringsmedicinsktBeslutsstodQuestion(internalCertificate.getForsakringsmedicinsktBeslutsstod(), index++))
            .addElement(createArbetstidsforlaggningQuestion(internalCertificate.getArbetstidsforlaggning(), index++))
            .addElement(createMotiveringArbetstidsforlaggningQuestion(internalCertificate.getArbetstidsforlaggningMotivering(), index++))
            .addElement(createArbetsresorQuestion(internalCertificate.getArbetsresor(), index++))
            .addElement(createPrognosQuestion(internalCertificate.getPrognos(), index++))
            .addElement(createPrognosTimeperiodQuestion(internalCertificate.getPrognos(), index++))
            .addElement(createAtgarderCategory(index++))
            .addElement(createAtgarderQuestion(internalCertificate.getArbetslivsinriktadeAtgarder(), index++))
            .addElement(createAtgarderBeskrivning(internalCertificate.getArbetslivsinriktadeAtgarderBeskrivning(), index++))
            .addElement(createOvrigtCategory(index++))
            .addElement(createOvrigtQuestion(internalCertificate.getOvrigt(), index++))
            .addElement(createKontaktCategory(index++))
            .addElement(createKontaktQuestion(internalCertificate.getKontaktMedFk(), index++))
            .addElement(createKontaktBeskrivning(internalCertificate.getAnledningTillKontakt(), index++))
            .build();
    }


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
                + "        Sjukpenning är en ersättning för personer som har en nedsatt arbetsförmåga på grund av sjukdom. Beroende på "
                + "hur mycket arbetsförmågan är nedsatt kan man få en fjärdedels, halv, tre fjärdedels eller hel sjukpenning.\n"
                + "\n"
                + "        Förutsättningar för att få sjukpenning\n"
                + "        Arbetsförmågan bedöms i förhållande till personens sysselsättning som kan vara nuvarande arbete eller "
                + "föräldraledighet för vård av barn. För personer som är arbetslösa bedöms arbetsförmågan i förhållandet till att utföra "
                + "sådant arbete som är normalt förekommande på arbetsmarknaden.\n"
                + "\n"
                + "        För att få sjukpenning ska man, förutom att ha nedsatt arbetsförmåga, tillhöra svensk socialförsäkring och ha"
                + " inkomst från arbete. Försäkringskassan beslutar om och hur mycket sjukpenning personen kan få.\n"
                + "\n"
                + "        Från den åttonde dagen i en sjukperiod måste det finnas ett läkarintyg. Läkarintyget ska tydligt beskriva hur"
                + " patientens sjukdom påverkar patientens förmåga att utföra sin sysselsättning.\n"
                + "\n"
                + "        Studerande\n"
                + "        En person kan få behålla sitt studiemedel om förmågan att studera är helt eller halvt nedsatt på grund av "
                + "sjukdom. Försäkringskassan bedömer om förmågan är nedsatt och om studieperioden ska godkännas till CSN.\n"
                + "\n"
                + "        Den som ansöker om att få behålla sitt studiemedel ska från och med den femtonde dagen i sjukperioden styrka"
                + " sin nedsatta studieförmåga med ett läkarintyg.\n"
                + "\n"
                + "        Smittbärarpenning\n"
                + "        En person kan få ersättning om hen måste avstå från sin sysselsättning på grund av läkarens beslut om "
                + "avstängning enligt smittskyddslagen eller läkarundersökning alternativt hälsokontroll som syftar till att klarlägga "
                + "sjukdom, smitta, sår eller annan skada som gör att hen inte får hantera livsmedel.\n"
                + "\n"
                + "        Den som ansöker om smittbärarpenning ska skicka med ett läkarintyg.\n"
                + "\n"
                + "        Mer om hur Försäkringskassan bedömer arbetsförmågan\n"
                + "        Försäkringskassan bedömer arbetsförmågan enligt rehabiliteringskedjan, som innebär följande:\n"
                + "        - Under de första 90 dagarna som personen är sjukskriven kan Försäkringskassan betala sjukpenning om personen "
                + "inte kan utföra sitt vanliga arbete eller ett annat tillfälligt arbete hos sin arbetsgivare.\n"
                + "        - Efter 90 dagar kan Försäkringskassan betala sjukpenning om personen inte kan utföra något arbete alls "
                + "hos sin arbetsgivare.\n"
                + "        - Efter 180 dagar kan Försäkringskassan betala ut sjukpenning om personen inte kan utföra sådant arbete som är"
                + " normalt förekommande på arbetsmarknaden. Men detta gäller inte om Försäkringskassan bedömer att personen med stor "
                + "sannolikhet kommer att kunna gå tillbaka till ett arbete hos sin arbetsgivare innan dag 366. I dessa fall bedöms "
                + "arbetsförmågan i förhållande till ett arbete hos arbetsgivaren även efter dag 180. Regeln gäller inte heller om det "
                + "kan anses oskäligt att bedöma personens arbetsförmåga i förhållande till arbete som är normalt förekommande på "
                + "arbetsmarknaden.\n"
                + "        - Efter 365 dagar kan Försäkringskassan betala ut sjukpenning om personen inte kan utföra sådant arbete som "
                + "är normalt förekommande på arbetsmarknaden. Undantag från detta kan göras om det kan anses oskäligt att bedöma personens"
                + " arbetsförmåga i förhållande till sådant arbete som normalt förekommer på arbetsmarknaden.\n"
                + "\n"
                + "        Rehabiliteringskedjan gäller fullt ut bara för den som har en anställning.\n"
                + "\n"
                + "        Egna företagares arbetsförmåga bedöms i förhållande till de vanliga arbetsuppgifterna fram till och med dag "
                + "180. Sedan bedöms arbetsförmågan i förhållande till sådant arbete som normalt förekommer på arbetsmarknaden.\n"
                + "\n"
                + "        För arbetslösa bedöms arbetsförmågan i förhållande till arbeten som normalt förekommer på arbetsmarknaden "
                + "redan från första dagen i sjukperioden.")
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


    private static CertificateDataElement createSmittbararpenningCategory(int index) {
        return CertificateDataElement.builder()
            .id(AVSTANGNING_SMITTSKYDD_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Smittbärarpenning")
                    .description("Fylls i om hon eller han måste avstå från sitt arbete på grund av:\n"
                        + "        - Intygsskrivande läkares beslut enligt smittskyddslagen\n"
                        + "        - Läkarundersökning eller hälsokontroll som syftar till att klarlägga om hon eller han är smittad av "
                        + "en allmänfarlig sjukdom eller om personen har en sjukdom, en smitta, ett sår eller annan skada som gör att hon"
                        + " eller han inte får hantera livsmedel.")
                    .build()
            )
            .build();
    }


    public static CertificateDataElement createAvstangningSmittskyddQuestion(Boolean value, int index) {
        return CertificateDataElement.builder()
            .id(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
            .index(index)
            .parent(AVSTANGNING_SMITTSKYDD_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27)
                    .label(
                        "Avstängning enligt smittskyddslagen på grund av smitta. (Fortsätt till frågorna \"Diagnos\" och"
                            + " \"Nedsättning av arbetsförmåga\".")
                    .selectedText("Ja")
                    .unselectedText("Nej")
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27)
                    .selected(value)
                    .build()
            )
            .build();
    }


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


    public static CertificateDataElement createIntygetBaseratPa(LisjpUtlatandeV1 internalCertificate, int index) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .index(index)
            .parent(GRUNDFORMU_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxMultipleDate.builder()
                    .text("Intyget är baserat på")
                    .description("Enligt Socialstyrelsens föreskrifter (HSLF-FS 2018:54) om att utfärda intyg i hälso- och\n"
                        + "        sjukvården ska ett läkarintyg innehålla uppgifter om vad som ligger till grund för din bedömning "
                        + "vid utfärdandet\n"
                        + "        av intyget. Ett intyg ska som huvudregel utfärdas efter en undersökning av patienten. Intyget ska "
                        + "innehålla\n"
                        + "        uppgift om kontaktsätt vid undersökningen. Om kontaktsättet är videosamtal anger du detta under "
                        + "fältet Övriga\n"
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
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
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

    public static CertificateDataElement createAnnatGrundForMUBeskrivning(String value, int index) {
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
                    .text(value)
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
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }


    public static CertificateDataElement createMotiveringEjUndersokning(String value, int index) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1)
            .index(index)
            .parent(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text("Motivering till varför det medicinska underlaget inte baseras på en undersökning av patienten")
                    .description(
                        "Observera att detta inte är en fråga från Försäkringskassan. Information om varför sjukskrivningen startar "
                            + "mer än en vecka före dagens datum kan vara till hjälp för Försäkringskassan i deras handläggning.\n"
                            + "Informationen du anger nedan, kommer att överföras till fältet \"Övriga upplysningar\" vid signering.")
                    .icon("lightbulb_outline")
                    .id(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1)
                    .text(value)
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
                        .limit(LIMIT_MOTIVERING_INTE_BASERAT_PA_UNDERLAG)
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
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


    public static CertificateDataElement createSysselsattningQuestion(List<Sysselsattning> value, int index) {
        return CertificateDataElement.builder()
            .id(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
            .index(index)
            .parent(SYSSELSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text("I relation till vilken sysselsättning bedömer du arbetsförmågan?")
                    .description(
                        "Om du kryssar i flera val är det viktigt att du tydliggör under \"Övriga upplysningar\" om sjukskrivningens "
                            + "omfattning eller period skiljer sig åt mellan olika sysselsättningar.")
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
                    .list(createSysselsattningCodeList(value))
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

    private static List<CertificateDataValueCode> createSysselsattningCodeList(List<Sysselsattning> value) {
        if (value == null) {
            return Collections.emptyList();
        }

        return value.stream()
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

    public static CertificateDataElement createDiagnosQuestion(List<Diagnos> value, int index) {
        return CertificateDataElement.builder()
            .id(DIAGNOS_SVAR_ID_6)
            .index(index)
            .parent(DIAGNOS_CATEGORY_ID)
            .config(
                CertificateDataConfigDiagnoses.builder()
                    .text("Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga")
                    .description(
                        "Ange vilken eller vilka sjukdomar som orsakar nedsatt arbetsförmåga. Den sjukdom som påverkar arbetsförmågan mest"
                            + " anges först. Diagnoskoden anges alltid med så många positioner som möjligt.\n        Om patienten har fler "
                            + "än tre sjukdomar som påverkar arbetsförmågan anges dessa under \"övriga upplysningar\".")
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
                    .list(createDiagnosValue(value))
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

    private static List<CertificateDataValueDiagnosis> createDiagnosValue(List<Diagnos> diagnoses) {
        if (diagnoses == null) {
            return Collections.emptyList();
        }

        final List<CertificateDataValueDiagnosis> newDiagnoses = new ArrayList<>();
        for (int i = 0; i < diagnoses.size(); i++) {
            final var diagnosis = diagnoses.get(i);
            if (isInvalid(diagnosis)) {
                continue;
            }

            newDiagnoses.add(createDiagnosis(Integer.toString(i + 1), diagnosis));
        }

        return newDiagnoses;
    }

    private static boolean isInvalid(Diagnos diagnos) {
        return diagnos.getDiagnosKod() == null;
    }

    private static CertificateDataValueDiagnosis createDiagnosis(String id, Diagnos diagnos) {
        return CertificateDataValueDiagnosis.builder()
            .id(id)
            .terminology(diagnos.getDiagnosKodSystem())
            .code(diagnos.getDiagnosKod())
            .description(diagnos.getDiagnosDisplayName())
            .build();
    }

    private static CertificateDataElement createFunktionsnedsattningCategory(int index) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Sjukdomens konsekvenser")
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

    public static CertificateDataElement createFunktionsnedsattningQuestion(String value, int index) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_SVAR_ID_35)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header("Funktionsnedsättning")
                    .text("Beskriv undersökningsfynd, testresultat och observationer")
                    .description(
                        "Ange observerade nedsättningar (direkt och indirekt), exempelvis\n"
                            + "- avvikelser i somatiskt och psykiskt status\n        - röntgen- och laboratoriefynd\n")
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FUNKTIONSNEDSATTNING_SVAR_ID_35)
                        .expression(
                            singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
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

    public static CertificateDataElement createAktivitetsbegransningQuestion(String value, int index) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_SVAR_ID_17)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header("Aktivitetsbegränsning")
                    .text("Beskriv vad patienten inte kan göra på grund av sin sjukdom. Ange vad uppgiften grundas på.")
                    .description(
                        "Ge konkreta exempel på situationer och aktiviteter i patientens sysselsättning där svårigheter uppstår."
                            + " Beskriv hur ofta dessa situationer uppstår och graden av svårigheterna.\\n\\n"
                            + "Beskriv hur du själv har observerat begränsningarna. "
                            + "Om uppgiften baseras på anamnes/intervju eller någon annans observationer anger du det.\\n\\n"
                            + "Ange även om det finns någon typ av aktivitet som patienten bör undvika"
                            + " på grund av betydande medicinsk risk eller allvarlig olycksrisk.\",\n")
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(AKTIVITETSBEGRANSNING_SVAR_ID_17)
                        .expression(
                            singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
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

    private static CertificateDataElement createMedicinskaBehandlingarCategory(int index) {
        return CertificateDataElement.builder()
            .id(MEDICINSKABEHANDLINGAR_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Medicinsk behandling")
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

    public static CertificateDataElement createPagaendeBehandlingQuestion(String value, int index) {
        return CertificateDataElement.builder()
            .id(PAGAENDEBEHANDLING_SVAR_ID_19)
            .index(index)
            .parent(MEDICINSKABEHANDLINGAR_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header("Pågående medicinska behandlingar/åtgärder")
                    .text("Ange vad syftet är och om möjligt tidplan samt ansvarig vårdenhet.")
                    .id(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
                    .text(value)
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

    public static CertificateDataElement createPlaneradBehandlingQuestion(String value, int index) {
        return CertificateDataElement.builder()
            .id(PLANERADBEHANDLING_SVAR_ID_20)
            .index(index)
            .parent(MEDICINSKABEHANDLINGAR_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header("Planerade medicinska behandlingar/åtgärder.")
                    .text("Ange vad syftet är och om möjligt tidplan samt ansvarig vårdenhet.")
                    .id(PLANERADBEHANDLING_SVAR_JSON_ID_20)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(PLANERADBEHANDLING_SVAR_JSON_ID_20)
                    .text(value)
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

    private static CertificateDataElement createBedomningCategory(int index) {
        return CertificateDataElement.builder()
            .id(BEDOMNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Bedömning")
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{}
            )
            .build();
    }

    public static CertificateDataElement createBehovAvSjukskrivningQuestion(List<Sjukskrivning> list, int index) {
        return CertificateDataElement.builder()
            .id(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigSickLeavePeriod.builder()
                    .text("Min bedömning av patientens nedsättning av arbetsförmågan")
                    .description("Utgångspunkten är att patientens arbetsförmåga ska bedömas i förhållande till hens normala arbetstid.")
                    .list(
                        Arrays.asList(
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_1_4.getId())
                                .label(SjukskrivningsGrad.NEDSATT_1_4.getLabel())
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_HALFTEN.getId())
                                .label(SjukskrivningsGrad.NEDSATT_HALFTEN.getLabel())
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_3_4.getId())
                                .label(SjukskrivningsGrad.NEDSATT_3_4.getLabel())
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.HELT_NEDSATT.getId())
                                .label(SjukskrivningsGrad.HELT_NEDSATT.getLabel())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueDateRangeList.builder()
                    .list(createSjukskrivningValue(list))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
                        .expression(multipleOrExpression(
                            singleExpression(SjukskrivningsGrad.NEDSATT_1_4.getId()),
                            singleExpression(SjukskrivningsGrad.NEDSATT_HALFTEN.getId()),
                            singleExpression(SjukskrivningsGrad.NEDSATT_3_4.getId()),
                            singleExpression(SjukskrivningsGrad.HELT_NEDSATT.getId())
                        ))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueDateRange> createSjukskrivningValue(List<Sjukskrivning> sickLeaves) {
        if (sickLeaves == null) {
            return Collections.emptyList();
        }
        return sickLeaves.stream()
            .map(item -> CertificateDataValueDateRange.builder()
                .id(item.getSjukskrivningsgrad().getId())
                .to(item.getPeriod().getTom().asLocalDate())
                .from(item.getPeriod().getFrom().asLocalDate())
                .build()
            ).collect(Collectors.toList());
    }

    public static CertificateDataElement createMotiveringTidigtStartdatumQuestion(String value, int index) {
        var attribute = "from";
        return CertificateDataElement.builder()
            .id(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text("Ange orsak för att starta perioden mer än 7 dagar bakåt i tiden.")
                    .description("Observera att detta inte är en fråga från Försäkringskassan. "
                            + "Information om varför sjukskrivningen startar mer än en vecka före"
                            + " dagens datum kan vara till hjälp för Försäkringskassan"
                            + "i deras handläggning.\\n' +\n            '\\n' +\n            "
                            + "'Informationen du anger nedan, kommer att överföras till fältet \"{0}\" vid signering.")
                    .icon("lightbulb_outline")
                    .id(MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
                        .expression(
                            multipleOrExpression(
                                wrapWithParenthesis(
                                    singleExpression(
                                        lessThan(
                                            appendAttribute(
                                                SjukskrivningsGrad.NEDSATT_1_4.getId(), attribute), VALIDATION_DAYS_TIDIGT_START_DATUM))),
                                wrapWithParenthesis(
                                    singleExpression(
                                        lessThan(
                                            appendAttribute(
                                                SjukskrivningsGrad.NEDSATT_HALFTEN.getId(), attribute),
                                                VALIDATION_DAYS_TIDIGT_START_DATUM))),
                                wrapWithParenthesis(
                                    singleExpression(
                                        lessThan(
                                            appendAttribute(
                                                SjukskrivningsGrad.NEDSATT_3_4.getId(), attribute), VALIDATION_DAYS_TIDIGT_START_DATUM))),
                                wrapWithParenthesis(
                                    singleExpression(
                                        lessThan(
                                            appendAttribute(
                                                SjukskrivningsGrad.HELT_NEDSATT.getId(), attribute), VALIDATION_DAYS_TIDIGT_START_DATUM)))
                            )
                        )
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID)
                        .limit(LIMIT_MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING)
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createForsakringsmedicinsktBeslutsstodQuestion(String value, int index) {
        return CertificateDataElement.builder()
            .id(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text("Patientens arbetsförmåga bedöms nedsatt längre tid än den som Socialstyrelsens"
                        + " försäkringsmedicinska beslutsstöd anger, därför att")
                    .description("- Om sjukdomen inte följer förväntat förlopp ska det framgå på vilket sätt.\n"
                            + "        - Om det inträffar komplikationer som gör att det tar längre tid att återfå arbetsförmåga ska"
                            + " du beskriva komplikationerna eller sjukdomstillstånden och skriva en förklaring"
                            + " till varför dessa fördröjer tillfrisknandet.\n"
                            + "        - Om sjukskrivningstidens längd påverkas av flera sjukdomar som orsakar en längre period med"
                            + " aktivitetsbegränsning än varje sjukdom för sig, samsjuklighet, ska du beskriva och förklara detta."
                    )
                    .id(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37)
                    .text(value)
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

    public static CertificateDataElement createArbetstidsforlaggningQuestion(Boolean value, int index) {
        return CertificateDataElement.builder()
            .id(ARBETSTIDSFORLAGGNING_SVAR_ID_33)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33)
                    .text("Finns det medicinska skäl att förlägga arbetstiden på något"
                        + " annat sätt än att minska arbetstiden lika mycket varje dag?")
                    .description("Frågorna besvaras endast vid partiellt nedsatt arbetsförmåga.")
                    .selectedText("Ja")
                    .unselectedText("Nej")
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33)
                    .selected(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
                        .expression(multipleOrExpression(
                            singleExpression(SjukskrivningsGrad.NEDSATT_1_4.getId()),
                            singleExpression(SjukskrivningsGrad.NEDSATT_HALFTEN.getId()),
                            singleExpression(SjukskrivningsGrad.NEDSATT_3_4.getId())
                        ))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createMotiveringArbetstidsforlaggningQuestion(String value, int index) {
        return CertificateDataElement.builder()
            .id(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text("Beskriv medicinska skäl till annan förläggning av arbetstiden")
                    .id(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(ARBETSTIDSFORLAGGNING_SVAR_ID_33)
                        .expression(
                            singleExpression(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33)
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

    public static CertificateDataElement createArbetsresorQuestion(Boolean value, int index) {
        return CertificateDataElement.builder()
            .id(ARBETSRESOR_SVAR_ID_34)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(ARBETSRESOR_SVAR_JSON_ID_34)
                    .label("Resor till och från arbetet med annat färdmedel än normalt kan göra det möjligt för"
                        + " patienten att återgå till arbetet under sjukskrivningsperioden.")
                    .selectedText("Ja")
                    .unselectedText("Nej")
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(ARBETSRESOR_SVAR_JSON_ID_34)
                    .selected(value)
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

    public static CertificateDataElement createPrognosQuestion(Prognos prognos, int index) {
        return CertificateDataElement.builder()
            .id(PROGNOS_SVAR_ID_39)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text("Prognos för arbetsförmåga utifrån aktuellt undersökningstillfälle")
                    .description("En viktig information för att underlätta planeringen.")
                    .list(
                        Arrays.asList(
                            RadioMultipleCode.builder()
                                .id(PrognosTyp.MED_STOR_SANNOLIKHET.getId())
                                .label(PrognosTyp.MED_STOR_SANNOLIKHET.getLabel())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(PrognosTyp.ATER_X_ANTAL_DGR.getId())
                                .label(PrognosTyp.ATER_X_ANTAL_DGR.getLabel())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId())
                                .label(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getLabel())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(PrognosTyp.PROGNOS_OKLAR.getId())
                                .label(PrognosTyp.PROGNOS_OKLAR.getLabel())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCode.builder()
                    .id(getPrognosValue(prognos))
                    .code(getPrognosValue(prognos))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(PROGNOS_SVAR_ID_39)
                        .expression(
                            multipleOrExpression(
                                singleExpression(PrognosTyp.MED_STOR_SANNOLIKHET.getId()),
                                singleExpression(PrognosTyp.ATER_X_ANTAL_DGR.getId()),
                                singleExpression(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId()),
                                singleExpression(PrognosTyp.PROGNOS_OKLAR.getId())
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

    private static String getPrognosValue(Prognos prognos) {
        return (prognos != null && prognos.getTyp() != null) ? prognos.getTyp().getId() : null;
    }

    public static CertificateDataElement createPrognosTimeperiodQuestion(Prognos prognos, int index) {
        return CertificateDataElement.builder()
            .id(PROGNOS_BESKRIVNING_DELSVAR_ID_39)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigDropdown.builder()
                    .list(
                        Arrays.asList(
                            DropdownItem.builder()
                                .id("VALJ_TIDSPERIOD")
                                .label("Välj tidsperiod")
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_30.getId())
                                .label(PrognosDagarTillArbeteTyp.DAGAR_30.getLabel())
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_60.getId())
                                .label(PrognosDagarTillArbeteTyp.DAGAR_60.getLabel())
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_90.getId())
                                .label(PrognosDagarTillArbeteTyp.DAGAR_90.getLabel())
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_180.getId())
                                .label(PrognosDagarTillArbeteTyp.DAGAR_180.getLabel())
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_365.getId())
                                .label(PrognosDagarTillArbeteTyp.DAGAR_365.getLabel())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCode.builder()
                    .id(getPrognosDagarTillArbeteValue(prognos))
                    .code(getPrognosDagarTillArbeteValue(prognos))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(PROGNOS_BESKRIVNING_DELSVAR_ID_39)
                        .expression(
                            multipleOrExpression(
                                singleExpression(PrognosDagarTillArbeteTyp.DAGAR_30.getId()),
                                singleExpression(PrognosDagarTillArbeteTyp.DAGAR_60.getId()),
                                singleExpression(PrognosDagarTillArbeteTyp.DAGAR_90.getId()),
                                singleExpression(PrognosDagarTillArbeteTyp.DAGAR_180.getId()),
                                singleExpression(PrognosDagarTillArbeteTyp.DAGAR_365.getId())
                            )
                        )
                        .build(),
                    CertificateDataValidationEnable.builder()
                        .questionId(PROGNOS_SVAR_ID_39)
                        .expression(singleExpression(PrognosTyp.ATER_X_ANTAL_DGR.getId()))
                        .build()
                }
            )
            .build();
    }

    private static String getPrognosDagarTillArbeteValue(Prognos prognos) {
        return prognos != null && prognos.getDagarTillArbete() != null ? prognos.getDagarTillArbete().getId() : null;
    }

    private static CertificateDataElement createAtgarderCategory(int index) {
        return CertificateDataElement.builder()
            .id(ATGARDER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Åtgärder")
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


    public static CertificateDataElement createAtgarderQuestion(List<ArbetslivsinriktadeAtgarder> atgarder, int index) {
        return CertificateDataElement.builder()
            .id(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
            .index(index)
            .parent(ATGARDER_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text("Här kan du ange åtgärder som du tror skulle göra det lättare för patienten att återgå i arbete")
                    .list(
                        Arrays.asList(
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.OVRIGT.getLabel())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(createAtgarderCodeList(atgarder))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            multipleOrExpression(
                                singleExpression(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                                )
                        )
                        .build(),
                    CertificateDataValidationDisable.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            multipleOrExpression(
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                            )
                        )
                        .id(Arrays.asList(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId()))
                        .build(),
                    CertificateDataValidationDisable.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            singleExpression(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId())
                            )
                        .id(
                            Arrays.asList(
                                ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId(),
                                ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId(),
                                ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.getId(),
                                ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId(),
                                ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId(),
                                ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId(),
                                ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.getId(),
                                ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId(),
                                ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId(),
                                ArbetslivsinriktadeAtgarderVal.OVRIGT.getId()
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

    private static List<CertificateDataValueCode> createAtgarderCodeList(List<ArbetslivsinriktadeAtgarder> atgarder) {
        if (atgarder == null) {
            return Collections.emptyList();
        }

        return atgarder.stream()
            .map(atgard -> CertificateDataValueCode.builder()
                .id(atgard.getTyp().getId())
                .code(atgard.getTyp().getId())
                .build())
            .collect(Collectors.toList());
    }

    public static CertificateDataElement createAtgarderBeskrivning(String value, int index) {
        return CertificateDataElement.builder()
            .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44)
            .index(index)
            .parent(ATGARDER_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text("Här kan du ange fler åtgärder. Du kan också beskriva hur åtgärderna kan underlätta återgång i arbete.")
                    .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            multipleOrExpression(
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
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

    private static CertificateDataElement createOvrigtCategory(int index) {
        return CertificateDataElement.builder()
            .id(OVRIGT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Övriga upplysningar")
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createOvrigtQuestion(String value, int index) {
        return CertificateDataElement.builder()
            .id(OVRIGT_SVAR_ID_25)
            .index(index)
            .parent(OVRIGT_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(OVRIGT_SVAR_JSON_ID_25)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(OVRIGT_SVAR_JSON_ID_25)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(OVRIGT_SVAR_JSON_ID_25)
                        .limit(LIMIT_OVRIGT)
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createKontaktCategory(int index) {
        return CertificateDataElement.builder()
            .id(KONTAKT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text("Kontakt")
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createKontaktQuestion(Boolean value, int index) {
        return CertificateDataElement.builder()
            .id(KONTAKT_ONSKAS_SVAR_ID_26)
            .index(index)
            .parent(KONTAKT_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(KONTAKT_ONSKAS_SVAR_JSON_ID_26)
                    .text("Kontakt med Försäkringskassan")
                    .description("Försäkringskassans handläggare tar kontakt med dig när"
                        + " underlaget har kommit in och handläggningen kan påbörjas.")
                    .selectedText("Ja")
                    .unselectedText("Nej")
                    .label("Jag önskar att Försäkringskassan kontaktar mig.")
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(KONTAKT_ONSKAS_SVAR_JSON_ID_26)
                    .selected(value)
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createKontaktBeskrivning(String value, int index) {
        return CertificateDataElement.builder()
            .id(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26)
            .index(index)
            .parent(KONTAKT_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26)
                    .text("Ange gärna varför du vill ha kontakt.")
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(KONTAKT_ONSKAS_SVAR_ID_26)
                        .expression(
                            singleExpression(KONTAKT_ONSKAS_SVAR_JSON_ID_26)
                        )
                        .build()
                }
            )
            .build();
    }
}
