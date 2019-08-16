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
package se.inera.intyg.common.fk7263.pdf;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.util.List;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

/**
 * Created by marced on 18/08/16.
 */
public class PdfEmployeeGenerator extends PdfAbstractGenerator {

    private static final int MARK_AS_EMPLOYER_WC_HEIGTH = 68;
    private static final int MARK_AS_EMPLOYER_WC_WIDTH = 478;
    private static final int MARK_AS_EMPLOYER_MI_HEIGHT = 48;
    private static final int MARK_AS_EMPLOYER_MI_WIDTH = 478;
    private static final int MARK_AS_EMPLOYER_START_X = 50;
    private static final int MARK_AS_EMPLOYER_START_Y = 670;

    // CHECKSTYLE:OFF LineLength
    private static final String WATERMARK_TEXT_WC_EMPLOYER_MINIMAL_COPY = "Detta är en utskrift av ett elektroniskt intyg med minimalt innehåll. Det uppfyller sjuklönelagens krav, om inget annat regleras i kollektivavtal. Det minimala intyget kan ge arbetsgivaren sämre möjligheter att bedöma behovet av rehabilitering än ett fullständigt intyg. Intyget har signerats elektroniskt av intygsutfärdaren.";
    private static final String WATERMARK_TEXT_CONTENT_IS_CUSTOMIZED = "Detta är en anpassad utskrift av ett elektroniskt intyg. Viss information i intyget har valts bort. Intyget har signerats elektroniskt av intygsutfärdaren.";
    // CHECKSTYLE:ON LineLength

    private List<String> selectedOptionalFields;

    public PdfEmployeeGenerator(Fk7263Utlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus)
        throws PdfGeneratorException {
        this(intyg, statuses, applicationOrigin, optionalFields, utkastStatus, true);
    }

    protected PdfEmployeeGenerator(Fk7263Utlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> selectedOptionalFields,
        UtkastStatus utkastStatus, boolean flatten)
        throws PdfGeneratorException {
        try {
            this.selectedOptionalFields = selectedOptionalFields;
            this.intyg = intyg;

            outputStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader(PDF_TEMPLATE);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, this.outputStream);
            fields = pdfStamper.getAcroFields();
            boolean isUtkast = UtkastStatus.getDraftStatuses().contains(utkastStatus);
            boolean isLocked = UtkastStatus.DRAFT_LOCKED == utkastStatus;

            switch (applicationOrigin) {
                case MINA_INTYG:

                    //In MI mode, we dont care about isUtkast or not - since only signed intyg are available
                    generateMIPdfWithOptionalFields(selectedOptionalFields);
                    // perform additional decoration for MI originated pdf
                    maskSendToFkInformation(pdfStamper);
                    if (isCustomized()) {
                        mark(pdfStamper, WATERMARK_TEXT_CONTENT_IS_CUSTOMIZED, MARK_AS_EMPLOYER_START_X, MARK_AS_EMPLOYER_START_Y,
                            MARK_AS_EMPLOYER_MI_HEIGHT, MARK_AS_EMPLOYER_MI_WIDTH);
                    } else {
                        markAsElectronicCopy(pdfStamper);
                    }

                    createRightMarginText(pdfStamper, pdfReader.getNumberOfPages(), intyg.getId(), MINA_INTYG_MARGIN_TEXT);
                    break;
                case WEBCERT:
                    generateMinimalPdf();

                    // perform additional decoration for WC originated pdf
                    maskSendToFkInformation(pdfStamper);
                    mark(pdfStamper, WATERMARK_TEXT_WC_EMPLOYER_MINIMAL_COPY, MARK_AS_EMPLOYER_START_X, MARK_AS_EMPLOYER_START_Y,
                        MARK_AS_EMPLOYER_WC_HEIGTH, MARK_AS_EMPLOYER_WC_WIDTH);

                    if (!isUtkast) {
                        createRightMarginText(pdfStamper, pdfReader.getNumberOfPages(), intyg.getId(), WEBCERT_MARGIN_TEXT);
                    }

                    break;
                default:
                    break;
            }

            // Add applicable watermarks
            addIntygStateWatermark(pdfStamper, pdfReader.getNumberOfPages(), isUtkast, isMakulerad(statuses), isLocked);

            pdfStamper.setFormFlattening(flatten);
            pdfStamper.close();

        } catch (Exception e) {
            throw new PdfGeneratorException(e);
        }
    }

    public boolean isCustomized() {
        return !EmployeeOptionalFields.containsAllValues(selectedOptionalFields);
    }

    private void generateMinimalPdf() {
        // Mandatory fields
        fillPatientDetails();
        fillBasedOn();
        fillRecommendationsKontaktMedForetagshalsovarden();
        fillCapacityRelativeToNuvarandeArbete();
        fillCapacity();
        fillTravel();
        fillSignerCodes();
        fillSignerNameAndAddress();

    }

    private void generateMIPdfWithOptionalFields(List<String> optionalFields) {
        // Mandatory fields
        generateMinimalPdf();

        // EmployerCopy pdf's could have customized optionalfields
        if (EmployeeOptionalFields.SMITTSKYDD.isPresent(optionalFields)) {
            fillIsSuspenseDueToInfection(); // Fält 1
        }
        if (EmployeeOptionalFields.DIAGNOS.isPresent(optionalFields)) {
            fillDiagnose(); // Fält 2
        }
        if (EmployeeOptionalFields.AKTUELLT_SJUKDOMS_FORLOPP.isPresent(optionalFields)) {
            fillDiseaseCause(); // Fält 3
        }
        if (EmployeeOptionalFields.FUNKTIONSNEDSATTNING.isPresent(optionalFields)) {
            fillDisability(); // Fält 4a
        }
        if (EmployeeOptionalFields.AKTIVITETSBEGRANSNING.isPresent(optionalFields)) {
            fillActivityLimitation(); // Fält 5
        }
        if (EmployeeOptionalFields.REKOMMENDATIONER_UTOM_FORETAGSHALSOVARD.isPresent(optionalFields)) {
            fillRecommendationsOther(); // Fält 6a (utom Kontakt med företagshälsovården som är obligatorisk)
        }
        if (EmployeeOptionalFields.BEHANDLING_ATGARD.isPresent(optionalFields)) {
            fillMeasures(); // Fält 6b
        }
        if (EmployeeOptionalFields.REHABILITERING.isPresent(optionalFields)) {
            fillRehabilitation(); // Fält 7
        }

        if (EmployeeOptionalFields.ARBETSFORMAGA_RELATIVT_TILL_UTOM_NUVARANDE_ARBETE.isPresent(optionalFields)) {
            fillCapacityRelativeToOtherThanNuvarandeArbete(); // Fält 8a (arbetslöshet / föräldraledighet)
        }
        if (EmployeeOptionalFields.ARBETSFORMAGA.isPresent(optionalFields)) {
            fillArbetsformaga(); // Fält 9
        }
        if (EmployeeOptionalFields.PROGNOS.isPresent(optionalFields)) {
            fillPrognose(); // Fält 10
        }
        if (EmployeeOptionalFields.KONTAKT_MED_FK.isPresent(optionalFields)) {
            fillFkContact(); // Fält 12
        }
        if (EmployeeOptionalFields.OVRIGT.isPresent(optionalFields)) {
            fillOther(); // Fält 13
        }
    }
}
