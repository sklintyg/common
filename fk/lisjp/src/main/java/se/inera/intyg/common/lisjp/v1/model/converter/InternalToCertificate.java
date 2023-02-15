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
package se.inera.intyg.common.lisjp.v1.model.converter;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_BESKRIVNING;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_COLLECTION;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_INFO;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_PLACEHOLDER;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANSWER_NO;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_JSON_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_BESKRIVNING;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ATGARDER_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ATGARDER_CATEGORY_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEDOMNING_CATEGORY_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_EN_FJARDEDEL;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_HALFTEN;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_HELT_NEDSATT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_BESKRIVNING;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_TRE_FJARDEDEL;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_BESKRIVNING;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_BESKRIVNING;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ICF_COLLECTION;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ICF_INFO;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ICF_PLACEHOLDER;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_CATEGORY_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_BESKRIVNING;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_CATEGORY_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_DAGAR_180;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_DAGAR_30;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_DAGAR_365;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_DAGAR_60;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_DAGAR_90;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ATER_X_ANTAL_DAGAR;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_BESKRIVNING;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_PROGNOS_OKLAR;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_SANNOLIKT_INTE;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_STOR_SANNOLIKHET;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_TEXT;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.appendAttribute;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.lessThanOrEqual;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.wrapWithParenthesis;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategoryDiagnos;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategorySmittbararpenning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategorySysselsattning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionAnnatGrundForMUBeskrivning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionAvstangningSmittskydd;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionIntygetBaseratPa;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionMotiveringEjUndersokning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionSysselsattning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionSysselsattningYrke;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.CertificateDataElementStyleEnum;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDropdown;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigIcf;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigSickLeavePeriod;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CheckboxDateRange;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.DropdownItem;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCodeOptionalDropdown;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisableSubElement;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationEnable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataIcfValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.model.common.internal.Relation;

public final class InternalToCertificate {

    private static final short LIMIT_MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING = (short) 150;
    private static final short LIMIT_OVRIGT = (short) 2700;

    private static final String VALIDATION_DAYS_TIDIGT_START_DATUM = "-7";


    private InternalToCertificate() {

    }

    public static Certificate convert(LisjpUtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        var index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, texts))
            .addElement(CategorySmittbararpenning.toCertificate(index++, texts))
            .addElement(QuestionAvstangningSmittskydd.toCertificate(internalCertificate.getAvstangningSmittskydd(), index++, texts))
            .addElement(CategorySmittbararpenning.toCertificate(index++, texts))
            .addElement(QuestionIntygetBaseratPa.toCertificate(internalCertificate, index++, texts))
            .addElement(
                QuestionAnnatGrundForMUBeskrivning.toCertificate(internalCertificate.getAnnatGrundForMUBeskrivning(), index++, texts))
            .addElement(
                QuestionMotiveringEjUndersokning.toCertificate(internalCertificate.getMotiveringTillInteBaseratPaUndersokning(), index++))
            .addElement(CategorySysselsattning.toCertificate(index++, texts))
            .addElement(QuestionSysselsattning.toCertificate(internalCertificate.getSysselsattning(), index++, texts))
            .addElement(QuestionSysselsattningYrke.toCertificate(internalCertificate.getNuvarandeArbete(), index++, texts))
            .addElement(CategoryDiagnos.toCertificate(index++, texts))
            .addElement(createDiagnosQuestion(internalCertificate.getDiagnoser(), index++, texts))
            .addElement(createFunktionsnedsattningCategory(index++, texts))
            .addElement(createFunktionsnedsattningQuestion(internalCertificate.getFunktionsnedsattning(),
                internalCertificate.getFunktionsKategorier(), index++, texts))
            .addElement(createAktivitetsbegransningQuestion(internalCertificate.getAktivitetsbegransning(),
                internalCertificate.getAktivitetsKategorier(), index++, texts))
            .addElement(createMedicinskaBehandlingarCategory(index++, texts))
            .addElement(createPagaendeBehandlingQuestion(internalCertificate.getPagaendeBehandling(), index++, texts))
            .addElement(createPlaneradBehandlingQuestion(internalCertificate.getPlaneradBehandling(), index++, texts))
            .addElement(createBedomningCategory(index++, texts))
            .addElement(createBehovAvSjukskrivningQuestion(internalCertificate.getSjukskrivningar(), index++, texts,
                internalCertificate.getGrundData().getRelation()))
            .addElement(createMotiveringTidigtStartdatumQuestion(internalCertificate.getMotiveringTillTidigtStartdatumForSjukskrivning(),
                index++, texts))
            .addElement(
                createForsakringsmedicinsktBeslutsstodQuestion(internalCertificate.getForsakringsmedicinsktBeslutsstod(), index++, texts))
            .addElement(createArbetstidsforlaggningQuestion(internalCertificate.getArbetstidsforlaggning(), index++, texts))
            .addElement(
                createMotiveringArbetstidsforlaggningQuestion(internalCertificate.getArbetstidsforlaggningMotivering(), index++, texts))
            .addElement(createArbetsresorQuestion(internalCertificate.getArbetsresor(), index++, texts))
            .addElement(createPrognosQuestion(internalCertificate.getPrognos(), index++, texts))
            .addElement(createPrognosTimeperiodQuestion(internalCertificate.getPrognos(), index++, texts))
            .addElement(createAtgarderCategory(index++, texts))
            .addElement(createAtgarderQuestion(internalCertificate.getArbetslivsinriktadeAtgarder(), index++, texts))
            .addElement(createAtgarderBeskrivning(internalCertificate.getArbetslivsinriktadeAtgarderBeskrivning(), index++, texts))
            .addElement(createOvrigtCategory(index++, texts))
            .addElement(createOvrigtQuestion(internalCertificate.getOvrigt(), index++, texts))
            .addElement(createKontaktCategory(index++, texts))
            .addElement(createKontaktQuestion(internalCertificate.getKontaktMedFk(), index++, texts))
            .addElement(createKontaktBeskrivning(internalCertificate.getAnledningTillKontakt(), index, texts))
            .build();
    }

    public static CertificateDataElement createDiagnosQuestion(List<Diagnos> value, int index, CertificateTextProvider texts) {
        return QuestionDiagnoser.toCertificate(value, index, texts);
    }

    private static CertificateDataElement createFunktionsnedsattningCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID))
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

    public static CertificateDataElement createFunktionsnedsattningQuestion(String value,
        List<String> disabilityCategories, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_SVAR_ID_35)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigIcf.builder()
                    .header(texts.get(FUNKTIONSNEDSATTNING_SVAR_TEXT))
                    .text(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_TEXT))
                    .description(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_BESKRIVNING))
                    .modalLabel(FUNKTIONSNEDSATTNING_ICF_INFO)
                    .collectionsLabel(FUNKTIONSNEDSATTNING_ICF_COLLECTION)
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                    .placeholder(FUNKTIONSNEDSATTNING_ICF_PLACEHOLDER)
                    .build()
            )
            .value(
                CertificateDataIcfValue.builder()
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                    .text(value)
                    .icfCodes(disabilityCategories)
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

    public static CertificateDataElement createAktivitetsbegransningQuestion(String value, List<String> activityLimitationCategories,
        int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_SVAR_ID_17)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigIcf.builder()
                    .header(texts.get(AKTIVITETSBEGRANSNING_SVAR_TEXT))
                    .text(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_TEXT))
                    .description(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_BESKRIVNING))
                    .modalLabel(AKTIVITETSBEGRANSNING_ICF_INFO)
                    .collectionsLabel(AKTIVITETSBEGRANSNING_ICF_COLLECTION)
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                    .placeholder(AKTIVITETSBEGRANSNING_ICF_PLACEHOLDER)
                    .build()
            )
            .value(
                CertificateDataIcfValue.builder()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                    .text(value)
                    .icfCodes(activityLimitationCategories)
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

    private static CertificateDataElement createMedicinskaBehandlingarCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(MEDICINSKABEHANDLINGAR_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(MEDICINSKABEHANDLINGAR_CATEGORY_TEXT))
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

    public static CertificateDataElement createPagaendeBehandlingQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PAGAENDEBEHANDLING_SVAR_ID_19)
            .index(index)
            .parent(MEDICINSKABEHANDLINGAR_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(PAGAENDEBEHANDLING_SVAR_TEXT))
                    .text(texts.get(PAGAENDEBEHANDLING_DELSVAR_TEXT))
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

    public static CertificateDataElement createPlaneradBehandlingQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PLANERADBEHANDLING_SVAR_ID_20)
            .index(index)
            .parent(MEDICINSKABEHANDLINGAR_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(PLANERADBEHANDLING_SVAR_TEXT))
                    .text(texts.get(PLANERADBEHANDLING_DELSVAR_TEXT))
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

    private static CertificateDataElement createBedomningCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(BEDOMNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(BEDOMNING_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{}
            )
            .build();
    }

    public static CertificateDataElement createBehovAvSjukskrivningQuestion(List<Sjukskrivning> list, int index,
        CertificateTextProvider texts, Relation relation) {
        return CertificateDataElement.builder()
            .id(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigSickLeavePeriod.builder()
                    .text(texts.get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_TEXT))
                    .description(texts.get(BEHOV_AV_SJUKSKRIVNING_SVAR_BESKRIVNING))
                    .list(
                        Arrays.asList(
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_1_4.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_EN_FJARDEDEL))
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_HALFTEN.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_HALFTEN))
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.NEDSATT_3_4.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_TRE_FJARDEDEL))
                                .build(),
                            CheckboxDateRange.builder()
                                .id(SjukskrivningsGrad.HELT_NEDSATT.getId())
                                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_HELT_NEDSATT))
                                .build()
                        )
                    )
                    .previousSickLeavePeriod(getPreviousSickLeavePeriod(relation))
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

    private static String getPreviousSickLeavePeriod(Relation relation) {
        return hasRenewalRelation(relation) ? getPreviousSickLeavePeriodText(relation) : null;
    }

    private static String getPreviousSickLeavePeriodText(Relation relation) {
        return String.format(
            "På det ursprungliga intyget var slutdatumet för den sista sjukskrivningsperioden %s och sjukskrivningsgraden var %s.",
            DateTimeFormatter.ofPattern("yyyy-MM-dd").format(relation.getSistaGiltighetsDatum()),
            relation.getSistaSjukskrivningsgrad()
        );
    }

    private static boolean hasRenewalRelation(Relation relation) {
        return relation != null && relation.getRelationKod() == RelationKod.FRLANG;
    }

    private static List<CertificateDataValueDateRange> createSjukskrivningValue(List<Sjukskrivning> sickLeaves) {
        if (sickLeaves == null) {
            return Collections.emptyList();
        }
        return sickLeaves.stream()
            .filter(item -> item.getPeriod() != null && item.getPeriod().isValid())
            .map(item -> CertificateDataValueDateRange.builder()
                .id(Objects.requireNonNull(item.getSjukskrivningsgrad()).getId())
                .to(Objects.requireNonNull(item.getPeriod()).getTom().asLocalDate())
                .from(item.getPeriod().getFrom().asLocalDate())
                .build()
            ).collect(Collectors.toList());
    }

    public static CertificateDataElement createMotiveringTidigtStartdatumQuestion(String value, int index,
        CertificateTextProvider texts) {
        var attribute = "from";
        return CertificateDataElement.builder()
            .id(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text("Ange orsak för att starta perioden mer än 7 dagar bakåt i tiden.")
                    .description("Observera att detta inte är en fråga från Försäkringskassan."
                        + "Information om varför sjukskrivningen startar mer än en vecka före"
                        + " dagens datum kan vara till hjälp för Försäkringskassan "
                        + "i deras handläggning.</br></br>"
                        + "Informationen du anger nedan, kommer att överföras till fältet \"Övriga upplysningar\" vid signering.")
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
                                        lessThanOrEqual(
                                            appendAttribute(
                                                SjukskrivningsGrad.NEDSATT_1_4.getId(), attribute), VALIDATION_DAYS_TIDIGT_START_DATUM))),
                                wrapWithParenthesis(
                                    singleExpression(
                                        lessThanOrEqual(
                                            appendAttribute(
                                                SjukskrivningsGrad.NEDSATT_HALFTEN.getId(), attribute),
                                            VALIDATION_DAYS_TIDIGT_START_DATUM))),
                                wrapWithParenthesis(
                                    singleExpression(
                                        lessThanOrEqual(
                                            appendAttribute(
                                                SjukskrivningsGrad.NEDSATT_3_4.getId(), attribute), VALIDATION_DAYS_TIDIGT_START_DATUM))),
                                wrapWithParenthesis(
                                    singleExpression(
                                        lessThanOrEqual(
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

    public static CertificateDataElement createForsakringsmedicinsktBeslutsstodQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_TEXT))
                    .description(texts.get(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_BESKRIVNING))
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

    public static CertificateDataElement createArbetstidsforlaggningQuestion(Boolean value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSTIDSFORLAGGNING_SVAR_ID_33)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33)
                    .text(texts.get(ARBETSTIDSFORLAGGNING_SVAR_TEXT))
                    .description(texts.get(ARBETSTIDSFORLAGGNING_SVAR_BESKRIVNING))
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
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
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETSTIDSFORLAGGNING_SVAR_ID_33)
                        .expression(singleExpression(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build(),
                }
            )
            .build();
    }

    public static CertificateDataElement createMotiveringArbetstidsforlaggningQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33)
            .index(index)
            .parent(ARBETSTIDSFORLAGGNING_SVAR_ID_33)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(ARBETSTIDSFORLAGGNING_MOTIVERING_TEXT))
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
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33)
                        .expression(singleExpression(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createArbetsresorQuestion(Boolean value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSRESOR_SVAR_ID_34)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(ARBETSRESOR_SVAR_JSON_ID_34)
                    .label(texts.get(ARBETSRESOR_SVAR_TEXT))
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
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

    public static CertificateDataElement createPrognosQuestion(Prognos prognos, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PROGNOS_SVAR_ID_39)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioMultipleCodeOptionalDropdown.builder()
                    .text(texts.get(PROGNOS_SVAR_TEXT))
                    .description(texts.get(PROGNOS_SVAR_BESKRIVNING))
                    .list(
                        Arrays.asList(
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.MED_STOR_SANNOLIKHET.getId())
                                .label(texts.get(PROGNOS_SVAR_STOR_SANNOLIKHET))
                                .build(),
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.ATER_X_ANTAL_DGR.getId())
                                .label(texts.get(PROGNOS_SVAR_ATER_X_ANTAL_DAGAR))
                                .dropdownQuestionId(PROGNOS_BESKRIVNING_DELSVAR_ID_39)
                                .build(),
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId())
                                .label(texts.get(PROGNOS_SVAR_SANNOLIKT_INTE))
                                .build(),
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.PROGNOS_OKLAR.getId())
                                .label(texts.get(PROGNOS_SVAR_PROGNOS_OKLAR))
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

    public static CertificateDataElement createPrognosTimeperiodQuestion(Prognos prognos, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PROGNOS_BESKRIVNING_DELSVAR_ID_39)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigDropdown.builder()
                    .list(
                        Arrays.asList(
                            DropdownItem.builder()
                                .id("")
                                .label("Välj tidsperiod")
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_30.getId())
                                .label(texts.get(PROGNOS_DAGAR_30))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_60.getId())
                                .label(texts.get(PROGNOS_DAGAR_60))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_90.getId())
                                .label(texts.get(PROGNOS_DAGAR_90))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_180.getId())
                                .label(texts.get(PROGNOS_DAGAR_180))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_365.getId())
                                .label(texts.get(PROGNOS_DAGAR_365))
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
            .style(CertificateDataElementStyleEnum.HIDDEN)
            .build();
    }

    private static String getPrognosDagarTillArbeteValue(Prognos prognos) {
        return prognos != null && prognos.getDagarTillArbete() != null ? prognos.getDagarTillArbete().getId() : null;
    }

    private static CertificateDataElement createAtgarderCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ATGARDER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(ATGARDER_CATEGORY_TEXT))
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


    public static CertificateDataElement createAtgarderQuestion(List<ArbetslivsinriktadeAtgarder> atgarder, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
            .index(index)
            .parent(ATGARDER_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(ARBETSLIVSINRIKTADE_ATGARDER_TEXT))
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
                    CertificateDataValidationDisableSubElement.builder()
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
                        .id(Collections.singletonList(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId()))
                        .build(),
                    CertificateDataValidationDisableSubElement.builder()
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

    public static CertificateDataElement createAtgarderBeskrivning(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44)
            .index(index)
            .parent(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_TEXT))
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
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createOvrigtCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(OVRIGT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(OVRIGT_CATEGORY_TEXT))
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createOvrigtQuestion(String value, int index,
        CertificateTextProvider texts) {
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

    private static CertificateDataElement createKontaktCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(KONTAKT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(KONTAKT_CATEGORY_TEXT))
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

    public static CertificateDataElement createKontaktQuestion(Boolean value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(KONTAKT_ONSKAS_SVAR_ID_26)
            .index(index)
            .parent(KONTAKT_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(KONTAKT_ONSKAS_SVAR_JSON_ID_26)
                    .text(texts.get(KONTAKT_ONSKAS_SVAR_TEXT))
                    .description(texts.get(KONTAKT_ONSKAS_SVAR_BESKRIVNING))
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .label(texts.get(KONTAKT_ONSKAS_DELSVAR_TEXT))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(KONTAKT_ONSKAS_SVAR_JSON_ID_26)
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

    public static CertificateDataElement createKontaktBeskrivning(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26)
            .index(index)
            .parent(KONTAKT_ONSKAS_SVAR_ID_26)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26)
                    .text(texts.get(ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT))
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
