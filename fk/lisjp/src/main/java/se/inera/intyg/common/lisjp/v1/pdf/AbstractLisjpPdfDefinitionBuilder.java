/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.v1.pdf;

import com.google.common.base.Strings;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.util.StringUtils;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.pdf.FkBasePdfDefinitionBuilder;
import se.inera.intyg.common.fkparent.pdf.PdfConstants;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkDynamicPageDecoratorEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkFormIdentityEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkFormPagePersonnummerEventHandlerImpl;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkLogoEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkOverflowPagePersonnummerEventHandlerImpl;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkPrintedByEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.IntygStateWatermarker;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.PageNumberingEventHandler;
import se.inera.intyg.common.fkparent.pdf.model.FkCheckbox;
import se.inera.intyg.common.fkparent.pdf.model.FkDiagnosKodField;
import se.inera.intyg.common.fkparent.pdf.model.FkFieldGroup;
import se.inera.intyg.common.fkparent.pdf.model.FkLabel;
import se.inera.intyg.common.fkparent.pdf.model.FkOverflowPage;
import se.inera.intyg.common.fkparent.pdf.model.FkOverflowableValueField;
import se.inera.intyg.common.fkparent.pdf.model.FkPage;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.pdf.model.FkTillaggsFraga;
import se.inera.intyg.common.fkparent.pdf.model.FkValueField;
import se.inera.intyg.common.fkparent.pdf.model.PdfComponent;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

// CHECKSTYLE:OFF MagicNumber
// CHECKSTYLE:OFF MethodLength
public abstract class AbstractLisjpPdfDefinitionBuilder extends FkBasePdfDefinitionBuilder {

    // Tillaggsfragor cant have static names, so by convention they are sent as "<frageId>" from gui , e.g "9001"

    private static final float KATEGORI_FULL_WIDTH = 180f;
    private static final float KATEGORI_OFFSET_X = 14.5f;

    private static final float CHECKBOXROW_DEFAULT_HEIGHT = 9f;
    private static final float CHECKBOX_DEFAULT_WIDTH = 60f;
    private static final String PROPERTY_KEY_BLANKETT_LABEL_DATUM = "label.datum";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_DATUM_FROM = "label.from";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_DATUM_TOM = "label.tom";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_JA_FYLL_I = "label.fylli";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_RUBRIK = "label.underskrift.rubrik";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_LAKARE = "label.underskrift.lakare";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_NAMNFORTYDLIGANDE = "label.underskrift.namnfortydligande";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_BEFATTNING = "label.underskrift.befattning";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_SPECIALISTKOMP = "label.underskrift.specialistkomp";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_LAKARE_HSAID = "label.underskrift.lakare.hsaid";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_ARBETSPLATSKOD = "label.underskrift.arbetsplatskod";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_LAKARE_PERSONID = "label.underskrift.lakare.personid";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_VARDENHET_ADRESS = "label.underskrift.vardenhet.adress";
    private static final String PROPERTY_KEY_BLANKETT_UNDERSKRIFT_OMFATTNING = "label.underskrift.omfattning";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_GDPR_INFO = "label.gdpr.info";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_INTE_KANNER_PATIENT = "label.inte.kanner.patient";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_PATIENT_NAMN = "label.patientnamn";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_PERSONNR = "label.personnr";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_SKICKA_TILL = "label.skicka.till";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_FK_INLASNING = "label.fk.inlasning";
    private static final String PROPERTY_KEY_BLANKETT_LABEL_FK_ADRESS = "label.fk.adress";
    private static final String PROPERTY_KEY_BLANKETT_FORTSATTNINGSBLAD = "label.fk.fortsattningsblad";

    abstract void fillIntyg(FkPdfDefinition pdfDefinition, LisjpUtlatandeV1 intyg, boolean isUtkast, boolean isLockedUtkast,
        List<Status> statuses, ApplicationOrigin applicationOrigin) throws IOException, DocumentException;

    public FkPdfDefinition buildPdfDefinition(LisjpUtlatandeV1 intyg, List<Status> statuses, ApplicationOrigin applicationOrigin,
        IntygTexts intygTexts, UtkastStatus utkastStatus)
        throws PdfGeneratorException {
        this.intygTexts = intygTexts;

        try {
            FkPdfDefinition def = new FkPdfDefinition();
            boolean isUtkast = UtkastStatus.getDraftStatuses().contains(utkastStatus);
            boolean isLockedUtkast = UtkastStatus.DRAFT_LOCKED == utkastStatus;

            if (isUtkast) {
                clearSkapadAvForUtkast(intyg.getGrundData());
            }

            // Add page envent handlers
            def.addPageEvent(new PageNumberingEventHandler());

            def.addPageEvent(new FkFormIdentityEventHandler(
                getPropertyValue(PROPERTY_KEY_FORMID),
                getPropertyValue(PROPERTY_KEY_FORMID_ROW2),
                getPropertyValue(PROPERTY_KEY_BLANKETT_ID),
                getPropertyValue(PROPERTY_KEY_BLANKETT_VERSION)));
            def.addPageEvent(new FkFormPagePersonnummerEventHandlerImpl(intyg.getGrundData().getPatient().getPersonId().getPersonnummer()));
            def.addPageEvent(new FkOverflowPagePersonnummerEventHandlerImpl(
                intyg.getGrundData().getPatient().getPersonId().getPersonnummer()));
            if (!isUtkast && !isLockedUtkast) {
                def.addPageEvent(new FkPrintedByEventHandler(intyg.getId(), getPrintedByText(applicationOrigin)));
            }

            def.addPageEvent(new IntygStateWatermarker(isUtkast, isMakulerad(statuses), isLockedUtkast));
            def.addPageEvent(new FkLogoEventHandler(1, 1, 0.233f * 100f, 13f, 22.5f));
            def.addPageEvent(new FkLogoEventHandler(5, 99));
            def.addPageEvent(new FkDynamicPageDecoratorEventHandler(5, def.getPageMargins(), "Läkarintyg", "för sjukpenning"));

            fillIntyg(def, intyg, isUtkast, isLockedUtkast, statuses, applicationOrigin);

            // Always add the overflow page last, as it will scan the model for overflowing content and must therefore
            // also be renderad last.
            def.addChild(new FkOverflowPage("Fortsättningsblad, svar med hänvisning fortsätter nedan", def));

            return def;
        } catch (IOException | DocumentException e) {
            throw new PdfGeneratorException("Failed to create FkPdfDefinition", e);
        }

    }

    FkFieldGroup fraga1(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga1 = new FkFieldGroup("1. " + getText("KAT_10.RBK"))
            .offset(KATEGORI_OFFSET_X, 81f)
            .size(KATEGORI_FULL_WIDTH, 13.5f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);
        fraga1.addChild(new FkCheckbox(getText("FRG_27.RBK"),
            intyg.getAvstangningSmittskydd() != null ? intyg.getAvstangningSmittskydd() : false)
            .offset(0f, 0f)
            .size(KATEGORI_FULL_WIDTH, 13.5f));
        return fraga1;
    }

    FkFieldGroup fraga2(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga2 = new FkFieldGroup("2. " + getText("FRG_1.RBK"))
            .offset(KATEGORI_OFFSET_X, 108f)
            .size(KATEGORI_FULL_WIDTH, 43f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);

        // Use a variable for yOffset to minimize use of magic numbers for offsets
        float checkBoxYOffset = 2f;

        // Special case of first topLabel
        fraga2.addChild(new FkValueField("")
            .offset(CHECKBOX_DEFAULT_WIDTH, 0f)
            .size(CHECKBOX_DEFAULT_WIDTH, 4f)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_DATUM)));

        fraga2.addChild(new FkCheckbox(getText("KV_FKMU_0001.UNDERSOKNING.RBK"),
            intyg.getUndersokningAvPatienten() != null)
            .offset(0f, checkBoxYOffset)
            .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga2.addChild(
            new FkValueField(nullSafeString(intyg.getUndersokningAvPatienten()))
                .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        checkBoxYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga2.addChild(new FkCheckbox(getText("KV_FKMU_0001.TELEFONKONTAKT.RBK"),
            intyg.getTelefonkontaktMedPatienten() != null)
            .offset(0f, checkBoxYOffset)
            .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga2.addChild(new FkValueField(
            nullSafeString(intyg.getTelefonkontaktMedPatienten()))
            .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
            .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        checkBoxYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga2.addChild(new FkCheckbox(getText("KV_FKMU_0001.JOURNALUPPGIFTER.RBK"),
            intyg.getJournaluppgifter() != null)
            .offset(0f, checkBoxYOffset)
            .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga2.addChild(new FkValueField(nullSafeString(intyg.getJournaluppgifter()))
            .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
            .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        checkBoxYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga2.addChild(new FkCheckbox(getText("KV_FKMU_0001.ANNAT.RBK"),
            intyg.getAnnatGrundForMU() != null)
            .offset(0f, checkBoxYOffset)
            .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga2.addChild(new FkValueField(nullSafeString(intyg.getAnnatGrundForMU()))
            .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
            .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        fraga2.addChild(new FkLabel(getText("DFR_1.3.RBK"))
            .offset(8f, 39f)
            .size(CHECKBOX_DEFAULT_WIDTH, 4f)
            .withVerticalAlignment(Element.ALIGN_MIDDLE));
        fraga2.addChild(new FkOverflowableValueField(
            nullSafeString(intyg.getAnnatGrundForMUBeskrivning()),
            getText("DFR_1.3.RBK"))
            .offset(CHECKBOX_DEFAULT_WIDTH, 38f)
            .size(KATEGORI_FULL_WIDTH - CHECKBOX_DEFAULT_WIDTH, 5f));
        return fraga2;
    }

    FkFieldGroup fraga3(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga3 = new FkFieldGroup("3. " + getText("FRG_28.RBK"))
            .offset(KATEGORI_OFFSET_X, 164.5f)
            .size(KATEGORI_FULL_WIDTH, 45f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);
        // Sysselsattning = nuvarande arbete & arbetsbeskrivning = obligatoriskt
        fraga3.addChild(new FkCheckbox(getText("KV_FKMU_0002.NUVARANDE_ARBETE.RBK"),
            intyg.getSysselsattning() != null
                && intyg.getSysselsattning().stream()
                .filter(Objects::nonNull)
                .map(Sysselsattning::getTyp)
                .anyMatch(typ -> Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE.equals(typ)))
            .offset(0f, 0f)
            .size(27.5f, 22.5f)
            .withBorders(Rectangle.BOTTOM + Rectangle.RIGHT)
            .withVerticalAlignment(Element.ALIGN_TOP)
            .withTopPadding(2f));
        fraga3.addChild(new FkOverflowableValueField(intyg.getNuvarandeArbete(), getText("FRG_29.RBK"))
            .offset(27.5f, 0f)
            .size(KATEGORI_FULL_WIDTH - 27.5f, 22.5f)
            .showLabelOnTop()
            .withBorders(Rectangle.BOTTOM));

        fraga3.addChild(new FkCheckbox(getText("KV_FKMU_0002.ARBETSSOKANDE.RBK"),
            intyg.getSysselsattning().stream().map(Sysselsattning::getTyp)
                .anyMatch(typ -> Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE.equals(typ)))
            .offset(0f, 22.5f)
            .size(KATEGORI_FULL_WIDTH, 7.5f));
        fraga3.addChild(new FkCheckbox(getText("KV_FKMU_0002.FORALDRALEDIG.RBK"),
            intyg.getSysselsattning().stream().map(Sysselsattning::getTyp)
                .anyMatch(typ -> Sysselsattning.SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.equals(typ)))
            .offset(0f, 30f)
            .size(KATEGORI_FULL_WIDTH, 7.5f));
        fraga3.addChild(new FkCheckbox(getText("KV_FKMU_0002.STUDIER.RBK"),
                intyg.getSysselsattning().stream().map(Sysselsattning::getTyp)
                .anyMatch(typ -> Sysselsattning.SysselsattningsTyp.STUDIER.equals(typ)))
            .offset(0f, 37.5f)
            .size(KATEGORI_FULL_WIDTH, 7.5f)
            .withBorders(Rectangle.BOTTOM + Rectangle.RIGHT));
        return fraga3;
    }

    FkFieldGroup fraga4(LisjpUtlatandeV1 intyg) {
        // Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga
        FkFieldGroup fraga4 = new FkFieldGroup("4. " + getText("FRG_6.RBK"))
            .offset(KATEGORI_OFFSET_X, 223.5f)
            .size(KATEGORI_FULL_WIDTH, 29f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);
        Diagnos currentDiagnos = safeGetDiagnos(intyg.getDiagnoser(), 0);
        FkValueField diagnos1 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
            .size(140, 11f)
            .withTopPadding(2f)
            .withValueTextAlignment(PdfPCell.ALIGN_TOP)
            .withBorders(Rectangle.BOX);

        // Diagnoskod enligt ICD-10 SE
        FkDiagnosKodField diagnosKodField1 = new FkDiagnosKodField(
            currentDiagnos.getDiagnosKod())
            .withTopLabel(getText("DFR_6.2.RBK"))
            .size(40f, 11f)
            .offset(140f, 0f)
            .withPartWidth(7.5f)
            .withBorders(Rectangle.BOTTOM);
        fraga4.addChild(diagnos1);
        fraga4.addChild(diagnosKodField1);

        currentDiagnos = safeGetDiagnos(intyg.getDiagnoser(), 1);
        FkValueField diagnos2 = new FkValueField(
            currentDiagnos.getDiagnosBeskrivning())
            .size(140f, 9f)
            .offset(0f, 11f)
            .withValueTextAlignment(PdfPCell.ALIGN_TOP)
            .withBorders(Rectangle.BOX);
        FkDiagnosKodField diagnosKodField2 = new FkDiagnosKodField(
            currentDiagnos.getDiagnosKod())
            .size(40f, 9f)
            .offset(140f, 11f)
            .withPartWidth(7.5f)
            .withBorders(Rectangle.BOTTOM);
        fraga4.addChild(diagnos2);
        fraga4.addChild(diagnosKodField2);

        currentDiagnos = safeGetDiagnos(intyg.getDiagnoser(), 2);
        FkValueField diagnos3 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
            .size(140f, 9f)
            .offset(0f, 20f)
            .withBorders(Rectangle.BOX)
            .withValueTextAlignment(PdfPCell.ALIGN_TOP);
        FkDiagnosKodField diagnosKodField3 = new FkDiagnosKodField(
            currentDiagnos.getDiagnosKod())
            .size(40f, 9f)
            .offset(140f, 20f)
            .withPartWidth(7.5f)
            .withBorders(Rectangle.BOTTOM);
        fraga4.addChild(diagnos3);
        fraga4.addChild(diagnosKodField3);
        return fraga4;
    }

    FkFieldGroup fraga5(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga5 = new FkFieldGroup("5. " + getText("FRG_35.RBK"))
            .offset(KATEGORI_OFFSET_X, 28.5f)
            .size(KATEGORI_FULL_WIDTH, 43f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);

        String content = intyg.getFunktionsnedsattning();
        if (intyg.getFunktionsKategorier() != null && intyg.getFunktionsKategorier().size() > 0) {
            StringBuilder icfContent = new StringBuilder();
            icfContent.append("Problem som påverkar patientens möjlighet att utföra sin sysselsättning:\n");
            for (String aktivitet : intyg.getFunktionsKategorier()) {
                icfContent.append(aktivitet);
                if (intyg.getFunktionsKategorier().indexOf(aktivitet) != intyg.getFunktionsKategorier().size() - 1) {
                    icfContent.append(" - ");
                }
            }
            icfContent.append("\n\n" + content);
            content = icfContent.toString();
        }
        fraga5.addChild(
            new FkOverflowableValueField(content,
                getText("DFR_35.1.RBK"))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 43f)
                .showLabelOnTop()
                .keepNewLines());
        return fraga5;
    }

    FkFieldGroup fraga6(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga6 = new FkFieldGroup("6. " + getText("FRG_17.RBK"))
            .offset(KATEGORI_OFFSET_X, 84f)
            .size(KATEGORI_FULL_WIDTH, 43f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);

        String content = intyg.getAktivitetsbegransning();
        if (intyg.getAktivitetsKategorier() != null && intyg.getAktivitetsKategorier().size() > 0) {
            StringBuilder icfContent = new StringBuilder();
            icfContent.append("Svårigheter som påverkar patientens sysselsättning:\n");
            for (String aktivitet : intyg.getAktivitetsKategorier()) {
                icfContent.append(aktivitet);
                if (intyg.getAktivitetsKategorier().indexOf(aktivitet) != intyg.getAktivitetsKategorier().size() - 1) {
                    icfContent.append(" - ");
                }
            }
            icfContent.append("\n\n" + content);
            content = icfContent.toString();
        }

        fraga6.addChild(
            new FkOverflowableValueField(content,
                getText("DFR_17.1.RBK"))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 43f)
                .showLabelOnTop()
                .keepNewLines());
        return fraga6;
    }

    FkFieldGroup fraga7(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga7 = new FkFieldGroup("7. " + getText("KAT_5.RBK"))
            .offset(KATEGORI_OFFSET_X, 140f)
            .size(KATEGORI_FULL_WIDTH, 72f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);

        fraga7.addChild(
            new FkOverflowableValueField(intyg.getPagaendeBehandling(),
                getText("FRG_19.RBK") + " " + getText("DFR_19.1.RBK"))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 36f)
                .withBorders(Rectangle.BOTTOM)
                .showLabelOnTop());

        fraga7.addChild(
            new FkOverflowableValueField(intyg.getPlaneradBehandling(),
                getText("FRG_20.RBK") + " " + getText("DFR_20.1.RBK"))
                .offset(0f, 36f)
                .size(KATEGORI_FULL_WIDTH, 36f)
                .showLabelOnTop());
        return fraga7;
    }

    FkFieldGroup fraga8p1(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga8 = new FkFieldGroup("8. " + getText("FRG_32.RBK"))
            .offset(KATEGORI_OFFSET_X, 225f)
            .size(KATEGORI_FULL_WIDTH, 36f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);

        Optional<Sjukskrivning> sjuk100 = intyg.getSjukskrivningar().stream()
            .filter(s -> Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT.equals(s.getSjukskrivningsgrad())).findAny();
        fraga8.addChild(new FkCheckbox(getText("KV_FKMU_0003.HELT_NEDSATT.RBK"), sjuk100.isPresent())
            .offset(0f, 0f)
            .size(82.5f, 9f)
            .withBorders(Rectangle.BOTTOM));
        fraga8.addChild(new FkValueField(sjuk100.map(Sjukskrivning::getPeriod)
            .map(InternalLocalDateInterval::getFrom)
            .map(InternalDate::getDate)
            .orElse(""))
            .offset(82.5f, 0f)
            .size(45f, 9f)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_DATUM_FROM))
            .withBorders(Rectangle.BOTTOM)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));
        fraga8.addChild(new FkValueField(sjuk100.map(Sjukskrivning::getPeriod)
            .map(InternalLocalDateInterval::getTom)
            .map(InternalDate::getDate)
            .orElse(""))
            .offset(127.5f, 0f)
            .size(52.5f, 9f)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_DATUM_TOM))
            .withBorders(Rectangle.BOTTOM)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        Optional<Sjukskrivning> sjuk75 = intyg.getSjukskrivningar().stream()
            .filter(s -> s.getSjukskrivningsgrad().equals(Sjukskrivning.SjukskrivningsGrad.NEDSATT_3_4)).findAny();
        fraga8.addChild(new FkCheckbox(getText("KV_FKMU_0003.TRE_FJARDEDEL.RBK"), sjuk75.isPresent())
            .offset(0f, 9f)
            .size(82.5f, 9f)
            .withBorders(Rectangle.BOTTOM));
        fraga8.addChild(new FkValueField(sjuk75.map(Sjukskrivning::getPeriod)
            .map(InternalLocalDateInterval::getFrom)
            .map(InternalDate::getDate)
            .orElse(""))
            .offset(82.5f, 9f)
            .size(45f, 9f)
            .withBorders(Rectangle.BOTTOM)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));
        fraga8.addChild(new FkValueField(sjuk75.map(Sjukskrivning::getPeriod)
            .map(InternalLocalDateInterval::getTom)
            .map(InternalDate::getDate)
            .orElse(""))
            .offset(127.5f, 9f)
            .size(52.5f, 9f)
            .withBorders(Rectangle.BOTTOM)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        Optional<Sjukskrivning> sjuk50 = intyg.getSjukskrivningar().stream()
            .filter(s -> s.getSjukskrivningsgrad().equals(Sjukskrivning.SjukskrivningsGrad.NEDSATT_HALFTEN)).findAny();
        fraga8.addChild(new FkCheckbox(getText("KV_FKMU_0003.HALFTEN.RBK"), sjuk50.isPresent())
            .offset(0f, 18f)
            .size(82.5f, 9f)
            .withBorders(Rectangle.BOTTOM));
        fraga8.addChild(new FkValueField(sjuk50.map(Sjukskrivning::getPeriod)
            .map(InternalLocalDateInterval::getFrom)
            .map(InternalDate::getDate)
            .orElse(""))
            .offset(82.5f, 18f)
            .size(45f, 9f)
            .withBorders(Rectangle.BOTTOM)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));
        fraga8.addChild(new FkValueField(sjuk50.map(Sjukskrivning::getPeriod)
            .map(InternalLocalDateInterval::getTom)
            .map(InternalDate::getDate)
            .orElse(""))
            .offset(127.5f, 18f)
            .size(52.5f, 9f)
            .withBorders(Rectangle.BOTTOM)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        Optional<Sjukskrivning> sjuk25 = intyg.getSjukskrivningar().stream()
            .filter(s -> s.getSjukskrivningsgrad().equals(Sjukskrivning.SjukskrivningsGrad.NEDSATT_1_4)).findAny();
        fraga8.addChild(new FkCheckbox(getText("KV_FKMU_0003.EN_FJARDEDEL.RBK"), sjuk25.isPresent())
            .offset(0f, 27f)
            .size(82.5f, 9f)
            .withBorders(Rectangle.BOTTOM));
        fraga8.addChild(new FkValueField(sjuk25.map(Sjukskrivning::getPeriod)
            .map(InternalLocalDateInterval::getFrom)
            .map(InternalDate::getDate)
            .orElse(""))
            .offset(82.5f, 27f)
            .size(45f, 9f)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));
        fraga8.addChild(new FkValueField(sjuk25.map(Sjukskrivning::getPeriod)
            .map(InternalLocalDateInterval::getTom)
            .map(InternalDate::getDate)
            .orElse(""))
            .offset(127.5f, 27f)
            .size(52.5f, 9f)
            .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));
        return fraga8;
    }

    FkOverflowableValueField fraga8p2(LisjpUtlatandeV1 intyg) {
        return new FkOverflowableValueField(
            intyg.getForsakringsmedicinsktBeslutsstod(),
            getText("FRG_37.RBK"))
            .offset(KATEGORI_OFFSET_X, 264f)
            .size(KATEGORI_FULL_WIDTH, 22.5f)
            .withBorders(Rectangle.BOX)
            .showLabelOnTop();
    }

    FkCheckbox fraga8p3(LisjpUtlatandeV1 intyg) {
        return new FkCheckbox(getText("FRG_34.RBK"), intyg.getArbetsresor() != null ? intyg.getArbetsresor() : false)
            .offset(KATEGORI_OFFSET_X, 27f)
            .size(KATEGORI_FULL_WIDTH, 13.5f)
            .withBorders(Rectangle.BOX);
    }

    FkValueField fraga8p4(LisjpUtlatandeV1 intyg) {
        // Enligt krav skall ja/nej alltid skrivas ut. Däremot är motiveringsfälet valbart
        FkValueField fraga8p4 = new FkValueField("")
            .offset(KATEGORI_OFFSET_X, 43f)
            .size(KATEGORI_FULL_WIDTH, 34f)
            .withTopLabel(getText("FRG_33.RBK"))
            .withBorders(Rectangle.BOX);

        fraga8p4.addChild(new FkCheckbox("Nej",
            intyg.getArbetstidsforlaggning() != null ? !intyg.getArbetstidsforlaggning() : false)
            .offset(0f, 2f)
            .size(22f, 9f)
            .withBorders(Rectangle.BOTTOM));

        fraga8p4.addChild(new FkCheckbox(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_JA_FYLL_I),
            intyg.getArbetstidsforlaggning() != null ? intyg.getArbetstidsforlaggning()
                : false)
            .offset(22f, 2f)
            .size(KATEGORI_FULL_WIDTH - 22f, 9f)
            .withBorders(Rectangle.BOTTOM));

        fraga8p4.addChild(new FkOverflowableValueField(
            intyg.getArbetstidsforlaggningMotivering(),
            getText("DFR_33.2.RBK"))
            .offset(0, 11f)
            .size(KATEGORI_FULL_WIDTH, 23f)
            .showLabelOnTop());
        return fraga8p4;
    }

    FkFieldGroup fraga9(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga9 = new FkFieldGroup("9. " + getText("FRG_39.RBK"))
            .offset(KATEGORI_OFFSET_X, 90f)
            .size(KATEGORI_FULL_WIDTH, 29.5f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);
        final PrognosTyp prognosTyp = intyg.getPrognos() == null ? null : intyg.getPrognos().getTyp();
        fraga9.addChild(new FkCheckbox(getText("KV_FKMU_0006.STOR_SANNOLIKHET.RBK"),
            PrognosTyp.MED_STOR_SANNOLIKHET.equals(prognosTyp))
            .offset(0f, 1.2f)
            .size(KATEGORI_FULL_WIDTH, 6.8f));

        fraga9.addChild(new FkCheckbox(getText("KV_FKMU_0006.ATER_X_ANTAL_DGR.RBK"),
            PrognosTyp.ATER_X_ANTAL_DGR.equals(prognosTyp))
            .offset(0f, 8f)
            .size(135f, 6.9f));

        fraga9.addChild(
            new FkValueField(PrognosTyp.ATER_X_ANTAL_DGR.equals(prognosTyp) && intyg.getPrognos().getDagarTillArbete() != null
                ? intyg.getPrognos().getDagarTillArbete().getLabel() : "")
                .offset(137f, 6f)
                .size(KATEGORI_FULL_WIDTH - 135f, 6.9f));

        fraga9.addChild(new FkCheckbox(getText("KV_FKMU_0006.SANNOLIKT_INTE.RBK"),
            PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.equals(prognosTyp))
            .offset(0f, 14.8f)
            .size(KATEGORI_FULL_WIDTH, 6.9f));

        fraga9.addChild(new FkCheckbox(getText("KV_FKMU_0006.PROGNOS_OKLAR.RBK"),
            PrognosTyp.PROGNOS_OKLAR.equals(prognosTyp))
            .offset(0f, 21.8f)
            .size(KATEGORI_FULL_WIDTH, 6.9f));
        return fraga9;
    }

    FkFieldGroup fraga10(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga10 = new FkFieldGroup("10. " + getText("FRG_40.RBK"))
            .offset(KATEGORI_OFFSET_X, 133f)
            .size(KATEGORI_FULL_WIDTH, 76.5f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);

        // row 0
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.ARBETSTRANING.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.equals(atgard)))
            .offset(0f, 0f)
            .size(44.5f, 9f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.ERGONOMISK.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.equals(atgard)))
            .offset(44.5f, 0f)
            .size(67.5f, 9f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.OMFORDELNING.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER
                    .equals(atgard)))
            .offset(111.5f, 0f)
            .size(68.5f, 9f));

        // row 1
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.ARBETSANPASSNING.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.equals(atgard)))
            .offset(0f, 9f)
            .size(44.5f, 9f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.HJALPMEDEL.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.equals(atgard)))
            .offset(44.5f, 9f)
            .size(67.5f, 9f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.OVRIGA_ATGARDER.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.OVRIGT.equals(atgard)))
            .offset(111.5f, 9f)
            .size(68.5f, 9f));

        // row 2
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.SOKA_NYTT_ARBETE.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.equals(atgard)))
            .offset(0f, 18f)
            .size(44.5f, 9f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.KONFLIKTHANTERING.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.equals(atgard)))
            .offset(44.5f, 18f)
            .size(67.5f, 9f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.EJ_AKTUELLT.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.equals(atgard)))
            .offset(111.5f, 18f)
            .size(68.5f, 9f));

        // row 3
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.BESOK_ARBETSPLATS.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(
                    atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.equals(atgard)))
            .offset(0f, 27f)
            .size(44.5f, 9f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.KONTAKT_FHV.RBK"),
            intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD
                    .equals(atgard)))
            .offset(44.5f, 27f)
            .size(KATEGORI_FULL_WIDTH - 44.5f, 9f));

        fraga10.addChild(new FkOverflowableValueField(intyg.getArbetslivsinriktadeAtgarderBeskrivning(), getText("FRG_44.RBK"))
            .offset(0f, 36f)
            .size(KATEGORI_FULL_WIDTH, 40.5f)
            .showLabelOnTop()
            .withBorders(Rectangle.TOP));
        return fraga10;
    }

    FkFieldGroup fraga11(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga11 = new FkFieldGroup("11. " + getText("FRG_25.RBK"))
            .offset(KATEGORI_OFFSET_X, 223f)
            .size(KATEGORI_FULL_WIDTH, 40.5f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);

        StringBuilder ovrigt = new StringBuilder();

        if (!Strings.nullToEmpty(intyg.getOvrigt()).trim().isEmpty()) {
            ovrigt
                .append(intyg.getOvrigt())
                .append("\n\n");
        }

        if (!Strings.nullToEmpty(intyg.getMotiveringTillInteBaseratPaUndersokning()).trim().isEmpty()) {
            ovrigt.append("Motivering till varför utlåtandet inte baseras på undersökning av patienten: ")
                .append(intyg.getMotiveringTillInteBaseratPaUndersokning());
        }

        // OBS: Övrigt fältet skall behålla radbytformattering eftersom detta kan vara sammanslaget med motiveringstext
        fraga11.addChild(new FkOverflowableValueField(ovrigt.toString(),
            getText("FRG_25.RBK"))
            .offset(0f, 0f)
            .size(KATEGORI_FULL_WIDTH, 40.5f)
            .keepNewLines());
        return fraga11;
    }

    FkFieldGroup fraga12(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga12 = new FkFieldGroup("12. " + getText("FRG_26.RBK"))
            .offset(KATEGORI_OFFSET_X, 29.5f)
            .size(KATEGORI_FULL_WIDTH, 22.5f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);
        // Jag önskar att Försäkringskassan kontaktar mig
        fraga12.addChild(new FkCheckbox(getText("DFR_26.1.RBK"),
            safeBoolean(intyg.getKontaktMedFk()))
            .offset(0f, 0f)
            .size(KATEGORI_FULL_WIDTH, 9f)
            .withBorders(Rectangle.BOTTOM));
        fraga12.addChild(new FkOverflowableValueField(
            intyg.getAnledningTillKontakt(),
            getText("DFR_26.2.RBK"))
            .offset(0f, 9f)
            .size(KATEGORI_FULL_WIDTH, 13.5f)
            .showLabelOnTop());
        return fraga12;
    }

    FkFieldGroup fraga13(LisjpUtlatandeV1 intyg) {
        FkFieldGroup fraga13 = new FkFieldGroup(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_RUBRIK))
            .offset(KATEGORI_OFFSET_X, 65.5f)
            .size(KATEGORI_FULL_WIDTH, 85.5f)
            .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
            .withBorders(Rectangle.BOX);

        fraga13.addChild(new FkValueField(intyg.getGrundData().getSigneringsdatum() != null
            ? intyg.getGrundData().getSigneringsdatum().format(DateTimeFormatter.ofPattern(DATE_PATTERN)) : "")
            .offset(0f, 0f)
            .size(45f, 11f)
            .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
            .withBorders(Rectangle.RIGHT + Rectangle.BOTTOM)
            .withTopLabel("Datum"));
        fraga13.addChild(new FkValueField("")
            .offset(45f, 0f)
            .size(KATEGORI_FULL_WIDTH - 45f, 11f)
            .withBorders(Rectangle.BOTTOM)
            .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_LAKARE)));

        fraga13.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getFullstandigtNamn())
            .offset(0f, 11f)
            .size(KATEGORI_FULL_WIDTH, 11f)
            .withBorders(Rectangle.BOTTOM)
            .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_NAMNFORTYDLIGANDE)));

        fraga13.addChild(new FkValueField(concatStringList(intyg.getGrundData().getSkapadAv().getBefattningar()))
            .offset(0f, 22f)
            .size(90f, 14f)
            .withValueTextAlignment(PdfPCell.ALIGN_TOP)
            .withBorders(Rectangle.RIGHT + Rectangle.BOTTOM)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_BEFATTNING)));
        fraga13.addChild(new FkValueField(concatStringList(intyg.getGrundData().getSkapadAv().getSpecialiteter()))
            .offset(90f, 22f)
            .size(KATEGORI_FULL_WIDTH - 90f, 14f)
            .withBorders(Rectangle.BOTTOM)
            .withValueTextAlignment(PdfPCell.ALIGN_TOP)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_SPECIALISTKOMP)));
        // skapadAv.personId is always a hsa-id
        fraga13.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getPersonId())
            .offset(0f, 36f)
            .size(90f, 9f)
            .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
            .withBorders(Rectangle.BOTTOM + Rectangle.RIGHT)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_LAKARE_HSAID)));
        fraga13.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getVardenhet().getArbetsplatsKod())
            .offset(90f, 36f)
            .size(KATEGORI_FULL_WIDTH - 90f, 9f)
            .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
            .withBorders(Rectangle.BOTTOM)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_ARBETSPLATSKOD)));
        // We only have an hsa-Id - so we never fill this field
        fraga13.addChild(new FkValueField("")
            .offset(0f, 45f)
            .size(KATEGORI_FULL_WIDTH, 9f)
            .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
            .withBorders(Rectangle.BOTTOM)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_LAKARE_PERSONID)));

        fraga13.addChild(new FkValueField(buildVardEnhetAdress(intyg.getGrundData().getSkapadAv().getVardenhet()))
            .offset(0f, 54f)
            .size(KATEGORI_FULL_WIDTH, 31.5f)
            .withValueTextAlignment(PdfPCell.ALIGN_TOP)
            .withTopLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_UNDERSKRIFT_VARDENHET_ADRESS)));

        //GDPRInfo (Optional)
        String gdprInfoText = getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_GDPR_INFO);
        float gdprOffset = 0;
        if (!StringUtils.isEmpty(gdprInfoText)) {
            gdprOffset = 5f;
            fraga13.addChild(new FkLabel(gdprInfoText)
                .offset(2f, 86f)
                .size(KATEGORI_FULL_WIDTH - 6f, 10f)
                .withVerticalAlignment(PdfPCell.ALIGN_TOP));
        }
        // Somewhat hacky, add a label outside the category box.
        fraga13.addChild(new FkLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_UNDERSKRIFT_OMFATTNING))
            .offset(3f, 91f + gdprOffset)
            .size(KATEGORI_FULL_WIDTH - 6f, 10f)
            .withVerticalAlignment(PdfPCell.ALIGN_TOP)
            .backgroundColor(245, 245, 245)
            .backgroundRounded(true));
        return fraga13;
    }

    void addPage1MiscFields(LisjpUtlatandeV1 intyg, boolean showFkAddress, List<PdfComponent<?>> allElements)
        throws IOException {
        FkLabel fortsBladText = new FkLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_FORTSATTNINGSBLAD))
            .offset(14f, 24f)
            .withVerticalAlignment(Element.ALIGN_TOP)
            .size(80f, 10f)
            .withLeading(0.0f, 1.2f)
            .withFont(PdfConstants.FONT_INLINE_FIELD_LABEL_LARGE);
        allElements.add(fortsBladText);

        FkLabel inteKannerPatientenText = new FkLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_INTE_KANNER_PATIENT))
            .offset(14f, 35.5f)
            .withVerticalAlignment(Element.ALIGN_TOP)
            .size(70f, 15f)
            .withLeading(.0f, 1.2f)
            .withFont(PdfConstants.FONT_INLINE_FIELD_LABEL_LARGE);
        allElements.add(inteKannerPatientenText);

        FkLabel mainHeader = new FkLabel("Läkarintyg")
            .offset(104f, 13f)
            .size(40f, 12f)
            .withVerticalAlignment(Element.ALIGN_TOP)
            .withFont(PdfConstants.FONT_FRAGERUBRIK);
        FkLabel subHeader = new FkLabel("för sjukpenning")
            .offset(104f, 17f)
            .size(40f, 15f)
            .withVerticalAlignment(Element.ALIGN_TOP)
            .withFont(PdfConstants.FONT_BOLD_9);
        allElements.add(mainHeader);
        allElements.add(subHeader);

        FkLabel patientNamnLbl = new FkLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_PATIENT_NAMN))
            .offset(104f, 21f)
            .size(62.5f, 15f)
            .withVerticalAlignment(Element.ALIGN_TOP)
            .withFont(PdfConstants.FONT_STAMPER_LABEL);
        FkLabel patientPnrLbl = new FkLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_PERSONNR))
            .offset(166f, 21f)
            .size(35f, 15f)
            .withVerticalAlignment(Element.ALIGN_TOP)
            .withFont(PdfConstants.FONT_STAMPER_LABEL);
        allElements.add(patientNamnLbl);
        allElements.add(patientPnrLbl);

        FkLabel patientPnr = new FkLabel(intyg.getGrundData().getPatient().getPersonId().getPersonnummer())
            .offset(166f, 25f)
            .size(35f, 15f)
            .withVerticalAlignment(Element.ALIGN_TOP)
            .withFont(PdfConstants.FONT_VALUE_TEXT);
        allElements.add(patientPnr);

        if (showFkAddress) {
            FkLabel skickaBlankettenTillLbl = new FkLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_SKICKA_TILL))
                .offset(110.2f, 40f)
                .size(35f, 5f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_INLINE_FIELD_LABEL_SMALL);
            allElements.add(skickaBlankettenTillLbl);

            FkLabel inlasningsCentralRad1 = new FkLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_FK_INLASNING))
                .offset(110.2f, 44.5f)
                .size(70f, 6f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_INLINE_FIELD_LABEL_LARGE);
            FkLabel inlasningsCentralRad2 = new FkLabel(getPropertyValue(PROPERTY_KEY_BLANKETT_LABEL_FK_ADRESS))
                .offset(110.2f, 51.25f)
                .size(60f, 6f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_INLINE_FIELD_LABEL_LARGE);

            allElements.add(inlasningsCentralRad1);
            allElements.add(inlasningsCentralRad2);
        }
    }

    void printElectronicCopy(List<PdfComponent<?>> allElements) {
        FkLabel elektroniskKopia = new FkLabel(PdfConstants.ELECTRONIC_COPY_WATERMARK_TEXT)
            .offset(14f, 50f)
            .withHorizontalAlignment(PdfPCell.ALIGN_CENTER)
            .withVerticalAlignment(Element.ALIGN_MIDDLE)
            .size(70f, 12f)
            .withFont(PdfConstants.FONT_BOLD_9)
            .withBorders(Rectangle.BOX, BaseColor.RED);
        allElements.add(elektroniskKopia);
    }

    FkPage tillaggsfragorPage(LisjpUtlatandeV1 intyg) throws IOException, DocumentException {

        List<PdfComponent<?>> allElements = new ArrayList<>();

        // Sida 5 ar en extrasida, har lagger vi ev tillaggsfragor
        for (int i = 0; i < intyg.getTillaggsfragor().size(); i++) {
            Tillaggsfraga tillaggsfraga = intyg.getTillaggsfragor().get(i);
            allElements.add(new FkTillaggsFraga((i + 1) + ". " + getText("DFR_" + tillaggsfraga.getId() + ".1.RBK"),
                tillaggsfraga.getSvar()));
        }

        if (allElements.isEmpty()) {
            return null;
        }

        FkPage thisPage = new FkPage("Tilläggsfrågor");
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }
}
// CHECKSTYLE:ON MagicNumber
// CHECKSTYLE:ON MethodLength
