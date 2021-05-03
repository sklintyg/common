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

package se.inera.intyg.common.af00213.v1.model.converter;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.builder.CertificateDataElementBuilder;
import se.inera.intyg.common.support.facade.builder.CertificateMetadataBuilder;
import se.inera.intyg.common.support.facade.builder.config.CertificateDataConfigBooleanBuilder;
import se.inera.intyg.common.support.facade.builder.config.CertificateDataConfigCategoryBuilder;
import se.inera.intyg.common.support.facade.builder.config.CertificateDataConfigTextAreaBuilder;
import se.inera.intyg.common.support.facade.builder.validation.CertificateDataValidationMandatoryBuilder;
import se.inera.intyg.common.support.facade.builder.validation.CertificateDataValidationShowBuilder;
import se.inera.intyg.common.support.facade.builder.value.CertificateDataTextValueBuilder;
import se.inera.intyg.common.support.facade.builder.value.CertificateDataValueBooleanBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.Unit;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;

public final class InternalToCertificate {

    private InternalToCertificate() {

    }

    public static Certificate convert(Af00213UtlatandeV1 internalCertificate) {
        int index = 0;
        return CertificateBuilder.create()
            .metadata(createMetadata(internalCertificate))
            .addElement(createFunktionsnedsattningsCategory(index++))
            .addElement(createHarFunktionsnedsattningsQuestion(internalCertificate.getHarFunktionsnedsattning(), index++))
            .addElement(createFunktionsnedsattningsQuestion(internalCertificate.getFunktionsnedsattning(), index++))
            .addElement(createAktivitetsbegransningsCategory(index++))
            .addElement(createHarAktivitetsbegransningsQuestion(internalCertificate.getHarAktivitetsbegransning(), index++))
            .addElement(createAktivitetsbegransningsQuestion(internalCertificate.getAktivitetsbegransning(), index++))
            .addElement(createUtredningBehandlingsCategory(index++))
            .addElement(createHarUtredningBehandlingsQuestion(internalCertificate.getHarUtredningBehandling(), index++))
            .addElement(createUtredningBehandlingsQuestion(internalCertificate.getUtredningBehandling(), index++))
            .addElement(createArbetspaverkansCategory(index++))
            .addElement(createHarArbetspaverkansQuestion(internalCertificate.getHarArbetetsPaverkan(), index++))
            .addElement(createArbetspaverkansQuestion(internalCertificate.getArbetetsPaverkan(), index++))
            .addElement(createOvrigtCategory(index++))
            .addElement(createOvrigtQuestion(internalCertificate.getOvrigt(), index))
            .build();
    }

    private static CertificateMetadata createMetadata(Af00213UtlatandeV1 internalCertificate) {
        final var unit = internalCertificate.getGrundData().getSkapadAv().getVardenhet();
        return CertificateMetadataBuilder.create()
            .id(internalCertificate.getId())
            .type(internalCertificate.getTyp())
            .typeVersion(internalCertificate.getTextVersion())
            .name("Arbetsförmedlingens medicinska utlåtande")
            .description(
                "Arbetsförmedlingen behöver ett medicinskt utlåtande för en arbetssökande som har ett behov av fördjupat stöd.\n"
                    + "\n"
                    + "Vi behöver ett utlåtande för att kunna:\n"
                    + "\n"
                    + "• utreda och bedöma om den arbetssökande har en funktionsnedsättning som medför nedsatt arbetsförmåga\n"
                    + "• bedöma om vi behöver göra anpassningar i program eller insatser\n"
                    + "• erbjuda lämpliga utredande, vägledande, rehabiliterande eller arbetsförberedande insatser.")
            .unit(
                Unit.create()
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

    private static CertificateDataElement createFunktionsnedsattningsCategory(int index) {
        return CertificateDataElementBuilder.create()
            .id(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategoryBuilder.create()
                    .text("Funktionsnedsättning")
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createHarFunktionsnedsattningsQuestion(Boolean harFunktionsnedsattning, int index) {
        return CertificateDataElementBuilder.create()
            .id(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigBooleanBuilder.create()
                    .text("Finns besvär på grund av sjukdom eller skada som medför funktionsnedsättning?")
                    .description("Med besvär avses sådant som påverkar psykiska, psykosociala eller kroppsliga funktioner.")
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11)
                    .selectedText("Ja")
                    .unselectedText("Nej")
                    .build()
            )
            .value(
                CertificateDataValueBooleanBuilder.create()
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11)
                    .selected(harFunktionsnedsattning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatoryBuilder.create()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createFunktionsnedsattningsQuestion(String funktionsnedsattning, int index) {
        return CertificateDataElementBuilder.create()
            .id(FUNKTIONSNEDSATTNING_DELSVAR_ID_12)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
            .config(
                CertificateDataConfigTextAreaBuilder.create()
                    .text(
                        "Beskriv de funktionsnedsättningar som har observerats (undersökningsfynd). Ange, om möjligt, varaktighet.")
                    .description(
                        "Ange de nedsättningar som har framkommit vid undersökning eller utredning.\n"
                            + "\n"
                            + "Till exempel:\n"
                            + "Medvetenhet, uppmärksamhet, orienteringsförmåga\n"
                            + "Social interaktion, agitation\n"
                            + "Kognitiva störningar som t ex minnessvårigheter\n"
                            + "Störningar på sinnesorganen som t ex syn- och hörselnedsättning, balansrubbningar\n"
                            + "Smärta i rörelseorganen\n"
                            + "Rörelseinskränkning, rörelseomfång, smidighet\n"
                            + "Uthållighet, koordination\n"
                            + "\n"
                            + "Med varaktighet menas permanent eller övergående. Ange i så fall tidsangivelse vid övergående.")
                    .id(FUNKTIONSNEDSATTNING_CATEGORY_ID)
                    .build()
            )
            .value(
                CertificateDataTextValueBuilder.create()
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12)
                    .text(funktionsnedsattning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatoryBuilder.create()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_12)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12))
                        .build(),
                    CertificateDataValidationShowBuilder.create()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createAktivitetsbegransningsCategory(int index) {
        return CertificateDataElementBuilder.create()
            .id(AKTIVITETSBEGRANSNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategoryBuilder.create()
                    .text("Aktivitetsbegränsning")
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShowBuilder.create()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createHarAktivitetsbegransningsQuestion(Boolean harAktivitetsbegransning, int index) {
        return CertificateDataElementBuilder.create()
            .id(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
            .parent(AKTIVITETSBEGRANSNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigBooleanBuilder.create()
                    .text("Leder funktionsnedsättningarna till aktivitetsbegränsningar i relation till arbete eller studier?")
                    .description(
                        "Aktivitet innebär personens möjlighet att genomföra en uppgift eller handling. Aktivitetsbegränsning ska bedömas"
                            + " utifrån de begränsningar personen har kopplat till att kunna söka arbete, genomföra en arbetsuppgift/"
                            + "arbetsuppgifter, kunna studera eller delta i aktivitet hos Arbetsförmedlingen.")
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21)
                    .selectedText("Ja")
                    .unselectedText("Nej")
                    .build()
            )
            .value(
                CertificateDataValueBooleanBuilder.create()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21)
                    .selected(harAktivitetsbegransning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatoryBuilder.create()
                        .questionId(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
                        .expression(singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21))
                        .build(),
                    CertificateDataValidationShowBuilder.create()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createAktivitetsbegransningsQuestion(String aktivitetsbegransning, int index) {
        return CertificateDataElementBuilder.create()
            .id(AKTIVITETSBEGRANSNING_DELSVAR_ID_22)
            .parent(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
            .index(index)
            .config(
                CertificateDataConfigTextAreaBuilder.create()
                    .text("Ange vilka aktivitetsbegränsningar? Ange hur och om möjligt varaktighet/prognos.")
                    .description(
                        "Ge konkreta exempel på aktivitetsbegränsningar utifrån personens planerade insatser hos Arbetsförmedlingen eller "
                            + "personens möjlighet att söka arbete, genomföra en arbetsuppgift/arbetsuppgifter eller studera."
                            + " Till exempel:\n"
                            + "\n"
                            + "att ta till sig en instruktion\n"
                            + "att ta reda på och förstå muntlig eller skriftlig information\n"
                            + "att kunna fokusera\n"
                            + "att kunna bära eller lyfta\n"
                            + "att kunna hantera statiskt arbete")
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22)
                    .build()
            )
            .value(
                CertificateDataTextValueBuilder.create()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22)
                    .text(aktivitetsbegransning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatoryBuilder.create()
                        .questionId(AKTIVITETSBEGRANSNING_DELSVAR_ID_22)
                        .expression(singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22))
                        .build(),
                    CertificateDataValidationShowBuilder.create()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build(),
                    CertificateDataValidationShowBuilder.create()
                        .questionId(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
                        .expression(singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createUtredningBehandlingsCategory(int index) {
        return CertificateDataElementBuilder.create()
            .id(UTREDNING_BEHANDLING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategoryBuilder.create()
                    .text("Utredning och behandling")
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createHarUtredningBehandlingsQuestion(Boolean harUtredningBehandling, int index) {
        return CertificateDataElementBuilder.create()
            .id(UTREDNING_BEHANDLING_DELSVAR_ID_31)
            .index(index)
            .parent(UTREDNING_BEHANDLING_CATEGORY_ID)
            .config(
                CertificateDataConfigBooleanBuilder.create()
                    .text(
                        "Finns pågående eller planerade utredningar/behandlingar som påverkar "
                            + "den planering som Arbetsförmedlingen har beskrivit i förfrågan?")
                    .description(
                        "Till exempel remiss för bedömning eller åtgärd inom annan vårdenhet eller aktiviteter inom egna verksamheten.")
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_31)
                    .selectedText("Ja")
                    .unselectedText("Nej")
                    .build()
            )
            .value(
                CertificateDataValueBooleanBuilder.create()
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_31)
                    .selected(harUtredningBehandling)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatoryBuilder.create()
                        .questionId(UTREDNING_BEHANDLING_DELSVAR_ID_31)
                        .expression(singleExpression(UTREDNING_BEHANDLING_SVAR_JSON_ID_31))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createUtredningBehandlingsQuestion(String utredningBehandling, int index) {
        return CertificateDataElementBuilder.create()
            .id(UTREDNING_BEHANDLING_DELSVAR_ID_32)
            .index(index)
            .parent(UTREDNING_BEHANDLING_DELSVAR_ID_31)
            .config(
                CertificateDataConfigTextAreaBuilder.create()
                    .text(
                        "Hur påverkar utredningarna/behandlingarna planeringen?"
                            + " När planeras utredningarna/behandlingarna att vara avslutade?\n")
                    .description(
                        "Utgå från den beskrivning Arbetsförmedlingen har gjort av personen och Arbetsförmedlingens"
                            + " planerade aktiviteter.\n"
                            + "\n"
                            + "Ange förväntat resultat för de utredningar eller behandlingar som ska genomföras i vården och när personen"
                            + " kan genomföra/delta i Arbetsförmedlingens planerade aktiviteter.")
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_32)
                    .build()
            )
            .value(
                CertificateDataTextValueBuilder.create()
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_32)
                    .text(utredningBehandling)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatoryBuilder.create()
                        .questionId(UTREDNING_BEHANDLING_DELSVAR_ID_32)
                        .expression(singleExpression(UTREDNING_BEHANDLING_SVAR_JSON_ID_32))
                        .build(),
                    CertificateDataValidationShowBuilder.create()
                        .questionId(UTREDNING_BEHANDLING_DELSVAR_ID_31)
                        .expression(singleExpression(UTREDNING_BEHANDLING_SVAR_JSON_ID_31))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createArbetspaverkansCategory(int index) {
        return CertificateDataElementBuilder.create()
            .id(ARBETETS_PAVERKAN_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategoryBuilder.create()
                    .text("Arbetspåverkan")
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createHarArbetspaverkansQuestion(Boolean harArbetspaverkan, int index) {
        return CertificateDataElementBuilder.create()
            .id(ARBETETS_PAVERKAN_DELSVAR_ID_41)
            .index(index)
            .parent(ARBETETS_PAVERKAN_CATEGORY_ID)
            .config(
                CertificateDataConfigBooleanBuilder.create()
                    .text("Kan sjukdomen/skadan förvärras av vissa arbetsuppgifter/arbetsmoment?")
                    .description(
                        "Utgå från den beskrivning Arbetsförmedlingen har gjort av personen och Arbetsförmedlingens planerade aktiviteter.")
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_41)
                    .selectedText("Ja")
                    .unselectedText("Nej")
                    .build()
            )
            .value(
                CertificateDataValueBooleanBuilder.create()
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_41)
                    .selected(harArbetspaverkan)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatoryBuilder.create()
                        .questionId(ARBETETS_PAVERKAN_DELSVAR_ID_41)
                        .expression(singleExpression(ARBETETS_PAVERKAN_SVAR_JSON_ID_41))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createArbetspaverkansQuestion(String arbetspaverkan, int index) {
        return CertificateDataElementBuilder.create()
            .id(ARBETETS_PAVERKAN_DELSVAR_ID_42)
            .index(index)
            .parent(ARBETETS_PAVERKAN_DELSVAR_ID_41)
            .config(
                CertificateDataConfigTextAreaBuilder.create()
                    .text("Vilken typ av arbetsuppgifter/arbetsmoment?")
                    .description(
                        "Utgå från den beskrivning Arbetsförmedlingen har gjort av personen och Arbetsförmedlingens planerade aktiviteter."
                            + "\n\n"
                            + "Till exempel:\n"
                            + "\n"
                            + "armarbete ovan axelhöjd\n"
                            + "arbete på höga höjder\n"
                            + "skärmarbete vid dator under längre tid\n"
                            + "statiskt arbete\n")
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_42)
                    .build()
            )
            .value(
                CertificateDataTextValueBuilder.create()
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_42)
                    .text(arbetspaverkan)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatoryBuilder.create()
                        .questionId(ARBETETS_PAVERKAN_DELSVAR_ID_42)
                        .expression(singleExpression(ARBETETS_PAVERKAN_SVAR_JSON_ID_42))
                        .build(),
                    CertificateDataValidationShowBuilder.create()
                        .questionId(ARBETETS_PAVERKAN_DELSVAR_ID_41)
                        .expression(singleExpression(ARBETETS_PAVERKAN_SVAR_JSON_ID_41))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createOvrigtCategory(int index) {
        return CertificateDataElementBuilder.create()
            .id(OVRIGT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategoryBuilder.create()
                    .text("Övrigt")
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createOvrigtQuestion(String ovrigt, int index) {
        return CertificateDataElementBuilder.create()
            .id(OVRIGT_DELSVAR_ID_5)
            .index(index)
            .parent(OVRIGT_CATEGORY_ID)
            .config(
                CertificateDataConfigTextAreaBuilder.create()
                    .id(OVRIGT_SVAR_JSON_ID_5)
                    .text("Övrigt som Arbetsförmedlingen bör känna till?")
                    .description(
                        "Som t ex risker för försämring vid andra aktiviteter än de som Arbetsförmedlingen har beskrivit i förfrågan.")
                    .build()
            )
            .value(
                CertificateDataTextValueBuilder.create()
                    .id(OVRIGT_SVAR_JSON_ID_5)
                    .text(ovrigt)
                    .build()
            )
            .build();
    }
}
