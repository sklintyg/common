/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.pdf;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Strings;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPCell;

import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.model.internal.Tillaggsfraga;
import se.inera.intyg.common.fkparent.pdf.FkBasePdfDefinitionBuilder;
import se.inera.intyg.common.fkparent.pdf.PdfConstants;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkDynamicPageDecoratorEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkFormIdentityEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkLogoEventHandler;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkOverflowPagePersonnummerEventHandlerImpl;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.FkPrintedByEventHandler;
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
import se.inera.intyg.common.lisjp.model.internal.LisjpUtlatande;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.pdf.eventhandlers.LisjpFormPagePersonnummerEventHandlerImpl;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

// CHECKSTYLE:OFF MagicNumber
// CHECKSTYLE:OFF MethodLength
public class LisjpPdfDefinitionBuilder extends FkBasePdfDefinitionBuilder {

    private static final float KATEGORI_FULL_WIDTH = 170f;
    private static final float KATEGORI_OFFSET_X = 18.5f;

    private static final float CHECKBOXROW_DEFAULT_HEIGHT = 8.5f;
    private static final float CHECKBOX_DEFAULT_WIDTH = 72.2f;

    public FkPdfDefinition buildPdfDefinition(LisjpUtlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin, IntygTexts intygTexts)
            throws PdfGeneratorException {
        this.intygTexts = intygTexts;

        try {
            FkPdfDefinition def = new FkPdfDefinition();

            // Add page envent handlers
            def.addPageEvent(new PageNumberingEventHandler(173f, 10f));
            def.addPageEvent(new FkFormIdentityEventHandler(intygTexts.getProperties().getProperty(PROPERTY_KEY_FORMID),
                    intygTexts.getProperties().getProperty(PROPERTY_KEY_BLANKETT_ID),
                    intygTexts.getProperties().getProperty(PROPERTY_KEY_BLANKETT_VERSION),
                    16f, 21f, 16f, 124f));
            def.addPageEvent(new LisjpFormPagePersonnummerEventHandlerImpl(intyg.getGrundData().getPatient().getPersonId().getPersonnummer(),
                    PdfConstants.FONT_FORM_ID_LABEL));
            def.addPageEvent(new FkOverflowPagePersonnummerEventHandlerImpl(intyg.getGrundData().getPatient().getPersonId().getPersonnummer()));
            def.addPageEvent(new FkPrintedByEventHandler(intyg.getId(), getPrintedByText(applicationOrigin), 192f, 75f));
            def.addPageEvent(new FkLogoEventHandler(1, 1, 22.3f, 18f, 23f));
            def.addPageEvent(new FkLogoEventHandler(5, 99));
            def.addPageEvent(new FkDynamicPageDecoratorEventHandler(5,
                    new float[] {
                            Utilities.millimetersToPoints(18f),
                            Utilities.millimetersToPoints(22f),
                            Utilities.millimetersToPoints(40f),
                            Utilities.millimetersToPoints(10f)
                    },
                    "Läkarintyg", "för sjukpenning"));

            def.addChild(createPage1(intyg, statuses, applicationOrigin));
            def.addChild(createPage2(intyg));
            def.addChild(createPage3(intyg));
            def.addChild(createPage4(intyg));

            // Only add tillaggsfragor page if there are some
            if (intyg.getTillaggsfragor().size() > 0) {
                def.addChild(createPage5(intyg));
            }

            // Always add the overflow page last, as it will scan the model for overflowing content and must therefore
            // also be renderad last.
            def.addChild(new FkOverflowPage("Fortsättningsblad, svar med hänvisning fortsätter nedan", def, 5f, 8f));

            return def;
        } catch (IOException | DocumentException e) {
            throw new PdfGeneratorException("Failed to create FkPdfDefinition", e);
        }

    }

    private FkPage createPage1(LisjpUtlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin) throws IOException, DocumentException {
        List<PdfComponent> allElements = new ArrayList<>();

        boolean showFkAddress;
        if (applicationOrigin.equals(ApplicationOrigin.MINA_INTYG)) {
            // we never include FK address in MI prints..
            showFkAddress = false;
        } else {
            showFkAddress = !isSentToFk(statuses);
        }
        addPage1MiscFields(intyg, showFkAddress, allElements);

        // Smittskydd
        FkFieldGroup fraga1 = new FkFieldGroup("1. " + getText("KAT_10.RBK"))
                .offset(KATEGORI_OFFSET_X, 77.5f)
                .size(KATEGORI_FULL_WIDTH, 13f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);
        fraga1.addChild(new FkCheckbox(getText("FRG_27.RBK"), (intyg.getAvstangningSmittskydd() == null) ? false : intyg.getAvstangningSmittskydd())
                .offset(0f, 2f)
                .size(KATEGORI_FULL_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        allElements.add(fraga1);

        // START KATEGORI 1. (Utlåtandet är baserat på....)
        FkFieldGroup fraga2 = new FkFieldGroup("2. " + getText("FRG_1.RBK"))
                .offset(KATEGORI_OFFSET_X, 103f)
                .size(KATEGORI_FULL_WIDTH, 40f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);

        // Use a variable for yOffset to minimize use of magic numbers for offsets
        float checkBoxYOffset = 2;

        // Special case of first topLabel
        fraga2.addChild(new FkValueField("")
                .offset(CHECKBOX_DEFAULT_WIDTH, 0f)
                .size(CHECKBOX_DEFAULT_WIDTH, 4f)
                .withTopLabel("datum (år, månad, dag)"));

        fraga2.addChild(new FkCheckbox(getText("KV_FKMU_0001.UNDERSOKNING.RBK"), intyg.getUndersokningAvPatienten() != null)
                .offset(0f, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga2.addChild(new FkValueField(nullSafeString(intyg.getUndersokningAvPatienten()))
                .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        checkBoxYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga2.addChild(new FkCheckbox(getText("KV_FKMU_0001.JOURNALUPPGIFTER.RBK"), intyg.getJournaluppgifter() != null)
                .offset(0f, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga2.addChild(new FkValueField(nullSafeString(intyg.getJournaluppgifter()))
                .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        checkBoxYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga2.addChild(new FkCheckbox(getText("KV_FKMU_0001.TELEFONKONTAKT.RBK"), intyg.getTelefonkontaktMedPatienten() != null)
                .offset(0f, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga2.addChild(new FkValueField(nullSafeString(intyg.getTelefonkontaktMedPatienten()))
                .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        checkBoxYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga2.addChild(new FkCheckbox(getText("KV_FKMU_0001.ANNAT.RBK"), intyg.getAnnatGrundForMU() != null)
                .offset(0f, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga2.addChild(new FkValueField(nullSafeString(intyg.getAnnatGrundForMU()))
                .offset(CHECKBOX_DEFAULT_WIDTH, checkBoxYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)

                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        fraga2.addChild(new FkLabel(getText("DFR_1.3.RBK"))
                .offset(8f, 36f)
                .size(58f, 4f)
                .withVerticalAlignment(Element.ALIGN_MIDDLE));
        fraga2.addChild(new FkOverflowableValueField(nullSafeString(intyg.getAnnatGrundForMUBeskrivning()), getText("DFR_1.3.RBK"))
                .offset(CHECKBOX_DEFAULT_WIDTH, 34f)
                .size(KATEGORI_FULL_WIDTH - CHECKBOX_DEFAULT_WIDTH, 3f));

        allElements.add(fraga2);

        FkFieldGroup fraga3 = new FkFieldGroup("3. " + getText("FRG_28.RBK"))
                .offset(KATEGORI_OFFSET_X, 156f)
                .size(KATEGORI_FULL_WIDTH, 63.5f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);

        fraga3.addChild(new FkCheckbox(getText("KV_FKMU_0002.NUVARANDE_ARBETE.RBK"),
                intyg.getSysselsattning().stream().map(Sysselsattning::getTyp)
                        .anyMatch(typ -> Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE.equals(typ)))
                                .offset(0f, 0f)
                                .size(26f, 21f)
                                .withBorders(Rectangle.BOTTOM + Rectangle.RIGHT)
                                .withVerticalAlignment(Element.ALIGN_TOP)
                                .withTopPadding(2f));
        fraga3.addChild(new FkOverflowableValueField(intyg.getNuvarandeArbete(), getText("FRG_29.RBK"))
                .offset(26f, 0f)
                .size(KATEGORI_FULL_WIDTH - 26f, 21f)
                .showLabelOnTop()
                .withBorders(Rectangle.BOTTOM));

        fraga3.addChild(new FkCheckbox(getText("KV_FKMU_0002.ARBETSSOKANDE.RBK"),
                intyg.getSysselsattning().stream().map(Sysselsattning::getTyp)
                        .anyMatch(typ -> Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE.equals(typ)))
                                .offset(0f, 22f)
                                .size(KATEGORI_FULL_WIDTH, 6.5f));
        fraga3.addChild(new FkCheckbox(getText("KV_FKMU_0002.FORALDRALEDIG.RBK"),
                intyg.getSysselsattning().stream().map(Sysselsattning::getTyp)
                        .anyMatch(typ -> Sysselsattning.SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.equals(typ)))
                                .offset(0f, 28.5f)
                                .size(KATEGORI_FULL_WIDTH, 6.5f));
        fraga3.addChild(new FkCheckbox(getText("KV_FKMU_0002.STUDIER.RBK"),
                intyg.getSysselsattning().stream().map(Sysselsattning::getTyp)
                        .anyMatch(typ -> Sysselsattning.SysselsattningsTyp.STUDIER.equals(typ)))
                                .offset(0f, 35f)
                                .size(KATEGORI_FULL_WIDTH, 7.5f)
                                .withBorders(Rectangle.BOTTOM));

        fraga3.addChild(new FkCheckbox(getText("KV_FKMU_0002.PROGRAM.RBK"),
                intyg.getSysselsattning().stream().map(Sysselsattning::getTyp)
                        .anyMatch(typ -> Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM.equals(typ)))
                                .offset(0f, 42.5f)
                                .size(26f, 21f)
                                .withBorders(Rectangle.BOTTOM + Rectangle.RIGHT)
                                .withVerticalAlignment(Element.ALIGN_TOP)
                                .withTopPadding(2f));
        fraga3.addChild(new FkOverflowableValueField(intyg.getArbetsmarknadspolitisktProgram(), getText("FRG_30.RBK"))
                .offset(26f, 42.5f)
                .size(KATEGORI_FULL_WIDTH - 26f, 21f)
                .showLabelOnTop()
                .withBorders(Rectangle.BOTTOM));

        allElements.add(fraga3);

        // Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga
        FkFieldGroup fraga4 = new FkFieldGroup("4. " + getText("FRG_6.RBK"))
                .offset(KATEGORI_OFFSET_X, 232.5f)
                .size(KATEGORI_FULL_WIDTH, 27.5f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);
        Diagnos currentDiagnos = safeGetDiagnos(intyg.getDiagnoser(), 0);
        FkValueField diagnos1 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
                .size(132f, 10.5f)
                .withTopPadding(2f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withBorders(Rectangle.BOX);

        // Diagnoskod enligt ICD-10 SE
        FkDiagnosKodField diagnosKodField1 = new FkDiagnosKodField(currentDiagnos.getDiagnosKod())
                .withTopLabel(getText("DFR_6.2.RBK"))
                .size(38f, 10.5f)
                .offset(132f, 0f)
                .withPartWidth(7f)
                .withBorders(Rectangle.BOTTOM);
        fraga4.addChild(diagnos1);
        fraga4.addChild(diagnosKodField1);

        currentDiagnos = safeGetDiagnos(intyg.getDiagnoser(), 1);
        FkValueField diagnos2 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
                .size(132f, 8.5f)
                .offset(0f, 10.5f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withBorders(Rectangle.BOX);
        FkDiagnosKodField diagnosKodField2 = new FkDiagnosKodField(currentDiagnos.getDiagnosKod())
                .size(38f, 8.5f)
                .offset(132f, 10.5f)
                .withPartWidth(7f)
                .withBorders(Rectangle.BOTTOM);
        fraga4.addChild(diagnos2);
        fraga4.addChild(diagnosKodField2);

        currentDiagnos = safeGetDiagnos(intyg.getDiagnoser(), 2);
        FkValueField diagnos3 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
                .size(132f, 8.5f)
                .offset(0f, 19f)
                .withBorders(Rectangle.BOX)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP);
        FkDiagnosKodField diagnosKodField3 = new FkDiagnosKodField(currentDiagnos.getDiagnosKod())
                .size(38f, 8.5f)
                .offset(132f, 19f)
                .withPartWidth(7f)
                .withBorders(Rectangle.BOTTOM);
        fraga4.addChild(diagnos3);
        fraga4.addChild(diagnosKodField3);
        allElements.add(fraga4);

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private void addPage1MiscFields(LisjpUtlatande intyg, boolean showFkAddress, List<PdfComponent> allElements) throws IOException {
        // Meta information text(s) etc.

        FkLabel elektroniskKopia = new FkLabel(PdfConstants.ELECTRONIC_COPY_WATERMARK_TEXT)
                .offset(20f, 50f)
                .withHorizontalAlignment(PdfPCell.ALIGN_CENTER)
                .withVerticalAlignment(Element.ALIGN_MIDDLE)
                .size(70f, 12f)
                .withFont(PdfConstants.FONT_BOLD_9)
                .withBorders(Rectangle.BOX, BaseColor.RED);
        allElements.add(elektroniskKopia);

        FkLabel fortsBladText = new FkLabel("Använd fortsättningsbladet som finns i slutet av blanketten om utrymmet i fälten inte räcker till.")
                .offset(20f, 24f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .size(75f, 10f)
                .withLeading(.0f, 1.2f)
                .withFont(PdfConstants.FONT_INLINE_FIELD_LABEL_LARGE);
        allElements.add(fortsBladText);

        FkLabel inteKannerPatientenText = new FkLabel("Om du inte känner patienten ska hen styrka\nsin identitet genom legitimation med foto\n(SOSFS 2005:29).")
                .offset(20f, 35.5f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .size(70f, 15f)
                .withLeading(.0f, 1.2f)
                .withFont(PdfConstants.FONT_INLINE_FIELD_LABEL_LARGE);
        allElements.add(inteKannerPatientenText);

        FkLabel mainHeader = new FkLabel("Läkarintyg")
                .offset(103f, 13f)
                .size(40f, 12f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_BOLD_10);
        FkLabel subHeader = new FkLabel("för sjukpenning")
                .offset(103f, 17f)
                .size(40f, 15f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_BOLD_8);
        allElements.add(mainHeader);
        allElements.add(subHeader);

        FkLabel patientNamnLbl = new FkLabel("Patientens namn")
                .offset(103f, 21f)
                .size(62.5f, 15f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_STAMPER_LABEL);
        FkLabel patientPnrLbl = new FkLabel("Personnummer")
                .offset(162f, 21f)
                .size(35f, 15f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_STAMPER_LABEL);
        allElements.add(patientNamnLbl);
        allElements.add(patientPnrLbl);

        FkLabel patientNamn = new FkLabel(intyg.getGrundData().getPatient().getFullstandigtNamn())
                .offset(103f, 25f)
                .size(62.5f, 15)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_VALUE_TEXT);
        FkLabel patientPnr = new FkLabel(intyg.getGrundData().getPatient().getPersonId().getPersonnummer())
                .offset(162f, 25f)
                .size(35f, 15f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_VALUE_TEXT);
        allElements.add(patientNamn);
        allElements.add(patientPnr);

        if (showFkAddress) {
            FkLabel skickaBlankettenTillLbl = new FkLabel("Skicka blanketten till")
                    .offset(109.2f, 38.5f)
                    .size(35f, 5f)
                    .withVerticalAlignment(Element.ALIGN_TOP)
                    .withFont(PdfConstants.FONT_INLINE_FIELD_LABEL_SMALL);
            allElements.add(skickaBlankettenTillLbl);

            FkLabel inlasningsCentralRad1 = new FkLabel("Försäkringskassans inläsningscentral")
                    .offset(109.2f, 43f)
                    .size(60f, 6f)
                    .withVerticalAlignment(Element.ALIGN_TOP)
                    .withFont(PdfConstants.FONT_INLINE_FIELD_LABEL_LARGE);
            FkLabel inlasningsCentralRad2 = new FkLabel("839 88 Östersund")
                    .offset(109.2f, 49.75f)
                    .size(60f, 6f)
                    .withVerticalAlignment(Element.ALIGN_TOP)
                    .withFont(PdfConstants.FONT_INLINE_FIELD_LABEL_LARGE);

            allElements.add(inlasningsCentralRad1);
            allElements.add(inlasningsCentralRad2);
        }
    }

    private FkPage createPage2(LisjpUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        // Fraga 6.Funktionsnedsättningar
        FkFieldGroup fraga5 = new FkFieldGroup("6. " + getText("FRG_35.RBK"))
                .offset(KATEGORI_OFFSET_X, 28.5f)
                .size(KATEGORI_FULL_WIDTH, 40f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);

        fraga5.addChild(new FkOverflowableValueField(intyg.getFunktionsnedsattning(), getText("DFR_35.1.RBK"))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 40f)
                .showLabelOnTop());

        allElements.add(fraga5);

        // Fraga 6.Aktivitetsbegränsningar --------------------------------------------------------------------------
        FkFieldGroup fraga6 = new FkFieldGroup("6. " + getText("FRG_17.RBK"))
                .offset(KATEGORI_OFFSET_X, 80f)
                .size(KATEGORI_FULL_WIDTH, 40f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);

        fraga6.addChild(new FkOverflowableValueField(intyg.getAktivitetsbegransning(), getText("DFR_17.1.RBK"))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 40f)
                .showLabelOnTop());

        allElements.add(fraga6);

        // Fraga 7.Medicinsk behandling
        FkFieldGroup fraga7 = new FkFieldGroup("7. " + getText("KAT_5.RBK"))
                .offset(KATEGORI_OFFSET_X, 133f)
                .size(KATEGORI_FULL_WIDTH, 68f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);

        fraga7.addChild(new FkOverflowableValueField(intyg.getPagaendeBehandling(), getText("FRG_19.RBK") + " " + getText("DFR_19.1.RBK"))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 34f)
                .withBorders(Rectangle.BOTTOM)
                .showLabelOnTop());

        fraga7.addChild(new FkOverflowableValueField(intyg.getPlaneradBehandling(), getText("FRG_20.RBK") + " " + getText("DFR_20.1.RBK"))
                .offset(0f, 34f)
                .size(KATEGORI_FULL_WIDTH, 34f)
                .showLabelOnTop());

        allElements.add(fraga7);

        // Fraga 8.Mina bedömning avpatientens nedsättning av arbetsförmågan
        FkFieldGroup fraga8 = new FkFieldGroup("8. " + getText("FRG_32.RBK"))
                .offset(KATEGORI_OFFSET_X, 213f)
                .size(KATEGORI_FULL_WIDTH, 34f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);

        Optional<Sjukskrivning> sjuk100 = intyg.getSjukskrivningar().stream()
                .filter(s -> s.getSjukskrivningsgrad().equals(Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT)).findAny();
        fraga8.addChild(new FkCheckbox(getText("KV_FKMU_0003.HELT_NEDSATT.RBK"), sjuk100.isPresent())
                .offset(0f, 0f)
                .size(77.5f, 8.5f)
                .withBorders(Rectangle.BOTTOM));
        fraga8.addChild(new FkValueField(sjuk100.isPresent() ? nullSafeString(sjuk100.get().getPeriod().getFrom()) : "")
                .offset(77.5f, 0f)
                .size(42.5f, 8.5f)
                .withTopLabel("från och med (år, månad, dag)")
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));
        fraga8.addChild(new FkValueField(sjuk100.isPresent() ? nullSafeString(sjuk100.get().getPeriod().getTom()) : "")
                .offset(120f, 0f)
                .size(50f, 8.5f)
                .withTopLabel("till och med (år, månad, dag)")
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        Optional<Sjukskrivning> sjuk75 = intyg.getSjukskrivningar().stream()
                .filter(s -> s.getSjukskrivningsgrad().equals(Sjukskrivning.SjukskrivningsGrad.NEDSATT_3_4)).findAny();
        fraga8.addChild(new FkCheckbox(getText("KV_FKMU_0003.TRE_FJARDEDEL.RBK"), sjuk75.isPresent())
                .offset(0f, 8.5f)
                .size(77.5f, 8.5f)
                .withBorders(Rectangle.BOTTOM));
        fraga8.addChild(new FkValueField(sjuk75.isPresent() ? nullSafeString(sjuk75.get().getPeriod().getFrom()) : "")
                .offset(77.5f, 8.5f)
                .size(42.5f, 8.5f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));
        fraga8.addChild(new FkValueField(sjuk75.isPresent() ? nullSafeString(sjuk75.get().getPeriod().getTom()) : "")
                .offset(120f, 8.5f)
                .size(50f, 8.5f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        Optional<Sjukskrivning> sjuk50 = intyg.getSjukskrivningar().stream()
                .filter(s -> s.getSjukskrivningsgrad().equals(Sjukskrivning.SjukskrivningsGrad.NEDSATT_HALFTEN)).findAny();
        fraga8.addChild(new FkCheckbox(getText("KV_FKMU_0003.HALFTEN.RBK"), sjuk50.isPresent())
                .offset(0f, 17f)
                .size(77.5f, 8.5f)
                .withBorders(Rectangle.BOTTOM));
        fraga8.addChild(new FkValueField(sjuk50.isPresent() ? nullSafeString(sjuk50.get().getPeriod().getFrom()) : "")
                .offset(77.5f, 17f)
                .size(42.5f, 8.5f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));
        fraga8.addChild(new FkValueField(sjuk50.isPresent() ? nullSafeString(sjuk50.get().getPeriod().getTom()) : "")
                .offset(120f, 17f)
                .size(50f, 8.5f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        Optional<Sjukskrivning> sjuk25 = intyg.getSjukskrivningar().stream()
                .filter(s -> s.getSjukskrivningsgrad().equals(Sjukskrivning.SjukskrivningsGrad.NEDSATT_1_4)).findAny();
        fraga8.addChild(new FkCheckbox(getText("KV_FKMU_0003.EN_FJARDEDEL.RBK"), sjuk25.isPresent())
                .offset(0f, 25.5f)
                .size(77.5f, 8.5f)
                .withBorders(Rectangle.BOTTOM));
        fraga8.addChild(new FkValueField(sjuk25.isPresent() ? nullSafeString(sjuk25.get().getPeriod().getFrom()) : "")
                .offset(77.5f, 25.5f)
                .size(42.5f, 8.5f)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));
        fraga8.addChild(new FkValueField(sjuk25.isPresent() ? nullSafeString(sjuk25.get().getPeriod().getTom()) : "")
                .offset(120f, 25.5f)
                .size(50f, 8.5f)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        allElements.add(fraga8);

        FkOverflowableValueField fraga8p2 = new FkOverflowableValueField(intyg.getForsakringsmedicinsktBeslutsstod(), getText("FRG_37.RBK"))
                .offset(KATEGORI_OFFSET_X, 250f)
                .size(KATEGORI_FULL_WIDTH, 21f)
                .withBorders(Rectangle.BOX)
                .showLabelOnTop();
        allElements.add(fraga8p2);

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage3(LisjpUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        FkCheckbox fraga8p3 = new FkCheckbox(getText("FRG_34.RBK"), intyg.getArbetsresor() != null ? intyg.getArbetsresor() : false)
                .offset(KATEGORI_OFFSET_X, 26.5f)
                .size(KATEGORI_FULL_WIDTH, 12.5f)
                .withBorders(Rectangle.BOX);
        allElements.add(fraga8p3);

        FkValueField fraga8p4 = new FkValueField("")
                .offset(KATEGORI_OFFSET_X, 42f)
                .size(KATEGORI_FULL_WIDTH, 32f)
                .withTopLabel(getText("FRG_33.RBK"))
                .withBorders(Rectangle.BOX);

        fraga8p4.addChild(new FkCheckbox("Nej", intyg.getArbetstidsforlaggning() != null ? !intyg.getArbetstidsforlaggning() : false)
                .offset(0f, 2f)
                .size(21f, 9f)
                .withBorders(Rectangle.BOTTOM));

        fraga8p4.addChild(new FkCheckbox("Ja. Fyll i nedan.", intyg.getArbetstidsforlaggning() != null ? intyg.getArbetstidsforlaggning() : false)
                .offset(21f, 2f)
                .size(KATEGORI_FULL_WIDTH - 21f, 9f)
                .withBorders(Rectangle.BOTTOM));

        fraga8p4.addChild(new FkOverflowableValueField(intyg.getArbetstidsforlaggningMotivering(), getText("DFR_33.2.RBK"))
                .offset(0, 11f)
                .size(KATEGORI_FULL_WIDTH, 21f)
                .showLabelOnTop());

        allElements.add(fraga8p4);

        FkFieldGroup fraga9 = new FkFieldGroup("9. " + getText("FRG_39.RBK"))
                .offset(KATEGORI_OFFSET_X, 86f)
                .size(KATEGORI_FULL_WIDTH, 28f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);

        fraga9.addChild(new FkCheckbox(getText("KV_FKMU_0006.STOR_SANNOLIKHET.RBK"), PrognosTyp.MED_STOR_SANNOLIKHET.equals(intyg.getPrognos().getTyp()))
                .offset(0f, 1f)
                .size(KATEGORI_FULL_WIDTH, 6.5f));

        fraga9.addChild(new FkCheckbox(getText("KV_FKMU_0006.ATER_X_ANTAL_DGR.RBK"), PrognosTyp.ATER_X_ANTAL_DGR.equals(intyg.getPrognos().getTyp()))
                .offset(0f, 7.5f)
                .size(135f, 6.5f));

        fraga9.addChild(
                new FkValueField(PrognosTyp.ATER_X_ANTAL_DGR.equals(intyg.getPrognos().getTyp()) ? intyg.getPrognos().getDagarTillArbete().getLabel() : "")
                        .offset(135f, 7.5f)
                        .size(KATEGORI_FULL_WIDTH - 135f, 6.5f));

        fraga9.addChild(new FkCheckbox(getText("KV_FKMU_0006.SANNOLIKT_INTE.RBK"),
                PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.equals(intyg.getPrognos().getTyp()))
                        .offset(0f, 14f)
                        .size(KATEGORI_FULL_WIDTH, 6.5f));

        fraga9.addChild(new FkCheckbox(getText("KV_FKMU_0006.PROGNOS_OKLAR.RBK"), PrognosTyp.PROGNOS_OKLAR.equals(intyg.getPrognos().getTyp()))
                .offset(0f, 20.5f)
                .size(KATEGORI_FULL_WIDTH, 6.5f));

        allElements.add(fraga9);

        FkFieldGroup fraga10 = new FkFieldGroup("10. " + getText("FRG_40.RBK"))
                .offset(KATEGORI_OFFSET_X, 126.5f)
                .size(KATEGORI_FULL_WIDTH, 72f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);

        // row 0
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.ARBETSTRANING.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.equals(atgard)))
                                .offset(0f, 0f)
                                .size(43f, 8.5f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.ERGONOMISK.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.equals(atgard)))
                                .offset(43f, 0f)
                                .size(63.5f, 8.5f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.OMFORDELNING.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.equals(atgard)))
                                .offset(106.5f, 0f)
                                .size(63.5f, 8.5f));

        // row 1
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.ARBETSANPASSNING.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.equals(atgard)))
                                .offset(0f, 8.5f)
                                .size(43f, 8.5f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.HJALPMEDEL.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.equals(atgard)))
                                .offset(43f, 8.5f)
                                .size(63.5f, 8.5f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.OVRIGA_ATGARDER.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.OVRIGT.equals(atgard)))
                                .offset(106.5f, 8.5f)
                                .size(63.5f, 8.5f));

        // row 2
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.SOKA_NYTT_ARBETE.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.equals(atgard)))
                                .offset(0f, 17f)
                                .size(43f, 8.5f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.KONFLIKTHANTERING.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.equals(atgard)))
                                .offset(43f, 17f)
                                .size(63.5f, 8.5f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.EJ_AKTUELLT.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.equals(atgard)))
                                .offset(106.5f, 17f)
                                .size(63.5f, 8.5f));

        // row 3
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.BESOK_ARBETSPLATS.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.equals(atgard)))
                                .offset(0f, 25.5f)
                                .size(43f, 8.5f));
        fraga10.addChild(new FkCheckbox(getText("KV_FKMU_0004.KONTAKT_FHV.RBK"),
                intyg.getArbetslivsinriktadeAtgarder().stream().map(ArbetslivsinriktadeAtgarder::getTyp)
                        .anyMatch(atgard -> ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.equals(atgard)))
                                .offset(43f, 25.5f)
                                .size(KATEGORI_FULL_WIDTH - 43f, 8.5f));

        fraga10.addChild(new FkOverflowableValueField(intyg.getArbetslivsinriktadeAtgarderBeskrivning(), getText("FRG_44.RBK"))
                .offset(0f, 34f)
                .size(KATEGORI_FULL_WIDTH, 38f)
                .showLabelOnTop()
                .withBorders(Rectangle.TOP));
        allElements.add(fraga10);

        FkFieldGroup fraga11 = new FkFieldGroup("11. " + getText("FRG_25.RBK"))
                .offset(KATEGORI_OFFSET_X, 211f)
                .size(KATEGORI_FULL_WIDTH, 28f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);

        StringBuilder ovrigt = new StringBuilder();

        if (!Strings.nullToEmpty(intyg.getMotiveringTillInteBaseratPaUndersokning()).trim().isEmpty()) {
            ovrigt.append("Motivering till varför utlåtandet inte baseras på undersökning av patienten: ")
                    .append(intyg.getMotiveringTillInteBaseratPaUndersokning())
                    .append("\n");
        }

        if (!Strings.nullToEmpty(intyg.getOvrigt()).trim().isEmpty()) {
            ovrigt.append(intyg.getOvrigt());
        }

        // OBS: Övrigt fältet skall behålla radbytformattering eftersom detta kan vara sammanslaget med motiveringstext
        fraga11.addChild(new FkOverflowableValueField(ovrigt.toString(), getText("FRG_25.RBK"))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 28f)
                .keepNewLines());

        allElements.add(fraga11);

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;

    }

    private FkPage createPage4(LisjpUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        // Fraga 12. Kontakt med FK
        FkFieldGroup fraga12 = new FkFieldGroup("12. " + getText("FRG_26.RBK"))
                .offset(KATEGORI_OFFSET_X, 28.5f)
                .size(KATEGORI_FULL_WIDTH, 21f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);
        // Jag önskar att Försäkringskassan kontaktar mig
        fraga12.addChild(new FkCheckbox(getText("DFR_26.1.RBK"), safeBoolean(intyg.getKontaktMedFk()))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 8.5f)
                .withBorders(Rectangle.BOTTOM));
        fraga12.addChild(new FkOverflowableValueField(intyg.getAnledningTillKontakt(), getText("DFR_26.2.RBK"))
                .offset(0f, 8.5f)
                .size(KATEGORI_FULL_WIDTH, 12.5f)
                .showLabelOnTop());

        allElements.add(fraga12);

        // Fraga 13. Underskrift --------------------------------------------------------------------------
        FkFieldGroup fraga13 = new FkFieldGroup("13. Underskrift")
                .offset(KATEGORI_OFFSET_X, 62.5f)
                .size(KATEGORI_FULL_WIDTH, 81f)
                .withFont(PdfConstants.FONT_FRAGERUBRIK_SMALL)
                .withBorders(Rectangle.BOX);

        fraga13.addChild(new FkValueField(intyg.getGrundData().getSigneringsdatum().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .offset(0f, 0f)
                .size(42f, 10.5f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.RIGHT + Rectangle.BOTTOM)
                .withTopLabel("Datum"));
        fraga13.addChild(new FkValueField("")
                .offset(42f, 0f)
                .size(KATEGORI_FULL_WIDTH - 42f, 10.5f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withTopLabel("Läkarens namnteckning"));

        fraga13.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getFullstandigtNamn())
                .offset(0f, 10.5f)
                .size(KATEGORI_FULL_WIDTH, 10.5f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withTopLabel("Namnförtydligande"));

        fraga13.addChild(new FkValueField(concatStringList(intyg.getGrundData().getSkapadAv().getBefattningar()))
                .offset(0f, 21f)
                .size(85f, 13f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withBorders(Rectangle.RIGHT + Rectangle.BOTTOM)
                .withTopLabel("Befattning"));
        fraga13.addChild(new FkValueField(concatStringList(intyg.getGrundData().getSkapadAv().getSpecialiteter()))
                .offset(85f, 21f)
                .size(KATEGORI_FULL_WIDTH - 85f, 13f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopLabel("Eventuell specialistkompetens"));
        // skapadAv.personId is always a hsa-id
        fraga13.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getPersonId())
                .offset(0f, 34f)
                .size(85f, 8.5f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.BOTTOM + Rectangle.RIGHT)
                .withTopLabel("Läkarens HSA-id"));
        fraga13.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getVardenhet().getArbetsplatsKod())
                .offset(85f, 34f)
                .size(KATEGORI_FULL_WIDTH - 85f, 8.5f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.BOTTOM)
                .withTopLabel("Arbetsplatskod"));
        // We only have an hsa-Id - so we never fill this field
        fraga13.addChild(new FkValueField("")
                .offset(0f, 42.5f)
                .size(KATEGORI_FULL_WIDTH, 8.5f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.BOTTOM)
                .withTopLabel("Läkarens personnummer. Anges endast om du som läkare saknar HSA-id."));

        fraga13.addChild(new FkValueField(buildVardEnhetAdress(intyg.getGrundData().getSkapadAv().getVardenhet()))
                .offset(0f, 51f)
                .size(KATEGORI_FULL_WIDTH, 30f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopLabel("Vårdenhetens namn, adress och telefon."));

        // Somewhat hacky, add a label outside the category box.
        fraga13.addChild(new FkLabel("Underskriften omfattar samtliga uppgifter i intyget")
                .offset(3f, 87.5f)
                .size(KATEGORI_FULL_WIDTH - 6f, 10f)
                .withVerticalAlignment(PdfPCell.ALIGN_TOP)
                .backgroundColor(245, 245, 245)
                .backgroundRounded(true));

        allElements.add(fraga13);

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage5(LisjpUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        // Sida 5 ar en extrasida, har lagger vi ev tillaggsfragor
        for (int i = 0; i < intyg.getTillaggsfragor().size(); i++) {
            Tillaggsfraga tillaggsfraga = intyg.getTillaggsfragor().get(i);
            allElements.add(new FkTillaggsFraga((i + 1) + ". " + getText("DFR_" + tillaggsfraga.getId() + ".1.RBK"),
                    tillaggsfraga.getSvar(), 5f, 8f));
        }

        FkPage thisPage = new FkPage("Tilläggsfrågor", 5f);
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

}
// CHECKSTYLE:ON MagicNumber
// CHECKSTYLE:ON MethodLength
