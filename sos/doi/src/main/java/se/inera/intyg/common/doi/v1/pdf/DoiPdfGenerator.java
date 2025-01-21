/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.doi.v1.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.doi.model.internal.OmOperation;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.sos_parent.pdf.AbstractSoSPdfGenerator;
import se.inera.intyg.common.sos_parent.pdf.SoSPdfGeneratorException;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;

/**
 * Created by marced on 2017-10-18.
 */
public class DoiPdfGenerator extends AbstractSoSPdfGenerator {

    public static final String DEFAULT_PDF_TEMPLATE = "pdf/Blankett-Dodsorsaksintyg-2018-54.pdf";

    /*
     * Acrofield names and values (extracted from pdf template file using util class
     * se.inera.intyg.common.doi.pdf.ListAcrofields)
     *
     * Maybe in the future, the issuer of the template (Socialstyrelsen) will provide a more programmer-friendly
     * version where values/names are more logical. But for now - this is what we have to work with.
     */

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_ERSATTER_TIDIGARE_UTFARDAT_INTYG = "Ersätter tidigare";
    // Type TEXT
    private static final String FIELD_PERSONNUMMER = "Personnummer";
    // Type TEXT
    private static final String FIELD_EFTERNAMN = "Efternamn";
    // Type TEXT
    private static final String FIELD_FORNAMN = "Förnamn";
    // Type TEXT
    private static final String FIELD_BOSTADSADRESS = "Bostadsadress";
    // Type TEXT
    private static final String FIELD_POSTNUMMER = "Postnummer";
    // Type TEXT
    private static final String FIELD_POSTORT = "Postort";
    // Type TEXT
    private static final String FIELD_LAND_OM_EJ_STADIGVARANDE_BOSATT_I_SVERIGE = "Land om ej stadigvarande bosatt i Sverige";
    // Type TEXT
    private static final String FIELD_IDENTITETEN_STYRKT_GENOM = "Identiteten styrkt genom";


    // Type TEXT
    private static final String FIELD_DODSDATUM = "Dödsdatum år mån dag";
    // Type CHECKBOX - values [On]
    private static final String FIELD_DODSDATUM_SAKERT = "Säkert";
    // Type CHECKBOX - values [On]
    private static final String FIELD_DODSDATUM_EJ_SAKERT = "Ej Säkert";
    // Type TEXT
    private static final String FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD = "Om dödsdatum ej säkert År mån dag anträffad död";


    // Type TEXT
    private static final String FIELD_KOMMUN_OM_OKAND_DODSPLATS_KOMMUNEN_DAR_KROPPEN_PATRAFFADES = "Kommun om okänd "
        + "dödsplats kommunen där kroppen påträffades";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_DODSPLATS_SJUKUS = "Sjukhus";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_DODSPLATS_ORDINART_BOENDE = "Ordinärt boende";
    // Type CHECKBOX - values [On]
    private static final String FIELD_DODSPLATS_SARSKILT_BOENDE = "Särskilt boende";
    // Type CHECKBOX - values [On]
    private static final String FIELD_DODSPLATS_ANNAN_OKAND = "Annan/okänd";


    // Type CHECKBOX - values [On]
    private static final String FIELD_AVLIDET_INOM_28_DYGN = "Avlidet inom 28 dygn";


    // Dynamic fieldnames (1, 2, 3 etc)
    private static final String FIELD_DODSORSAK_FIRSTROW_BESKRIVNING = "Den terminala dödsorsaken var_%s";
    private static final String FIELD_DODSORSAK_ROW_BESKRIVNING = "Den terminala dödsorsaken var_%s";
    private static final String FIELD_DODSORSAK_ROW_UNGEFARLIG_DEBUT = "Ungefärlig debut år mån dag_%s";
    private static final String FIELD_DODSORSAK_ROW_AKUT = "Akut_%s";
    private static final String FIELD_DODSORSAK_ROW_KRONISK = "Kronisk_%s";
    private static final String FIELD_DODSORSAK_ROW_INGEN_UPPGIFT = "Uppgift saknas_%s";
    private static final String FIELD_BIDRAGANDE_DODSORSAK_ROW_BESKRIVNING = "Andra sjukdomar eller skador som bidragit till"
        + " dödsfalletRow%d";
    private static final String FIELD_BIDRAGANDE_DODSORSAK_ROW_UNGEFARLIG_DEBUT = "Ungefärlig debut år mån dag_%d";


    // Type CHECKBOX - values [On]
    private static final String FIELD_OPERERAD_JA = "Opererad ja";
    // Type CHECKBOX - values [On]
    private static final String FIELD_OPERERAD_NEJ = "Opererad nej";
    // Type CHECKBOX - values [On]
    private static final String FIELD_OPERERAD_UPPGIFT_SAKNAS = "Uppgift om operation saknas";
    // Type TEXT
    private static final String FIELD_OPERATIONSDATUM_AR_MAN_DAG = "Operationsdatum år mån dag";
    // Type TEXT
    private static final String FIELD_TILLSTAND_SOM_FORANLEDDE_INGREPPET = "Tillstånd som föranledde ingreppet";


    // Type CHECKBOX - values [Ja]
    private static final String FIELD_OLYCKSFALL = "Olycksfall";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_AVSIKTLIGT_VALLAD_AV_ANNAN = "Avsiktligt vållad av annan";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_SJALVMORD = "Självmord";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_OKLART_OM_AVSIKT_FORELEGAT = "Oklart om avsikt förelegat";
    // Type TEXT
    private static final String FIELD_DATUM_FOR_SKADAFORGIFTNING_AR_MAN_DAG = "Datum för skadaförgiftning år mån dag";
    // Type TEXT
    private static final String FIELD_KORT_BESKRIVNING_AV_HUR_SKADANFORGIFTNINGEN_UPPKOM = "Kort beskrivning av hur skadanförgiftningen"
        + " uppkom utan att röja eventuellt andra inblandades identiteter";


    // Type CHECKBOX - values [Ja]
    private static final String FIELD_UNDERSOKNING_FORE_DODEN = "Undersökning före döden";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_RATTSMEDICINSK_OBDUKTION = "Rättsmedicinsk obduktion";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_YTTRE_UNDERSOKNING_EFTER_DODEN = "Yttre efter döden";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_RATTSMEDICINSK_LIKBESIKTNING = "Rättsmedicinsk likbesiktning";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_KLINISK_OBDUKTION = "Klinisk obduktion";


    // Type TEXT
    private static final String FIELD_ORT_OCH_DATUM = "Ort och datum";
    // Type TEXT
    private static final String FIELD_LAKARENS_EFTERNAMN_OCH_FORNAMN = "Läkarens efternamn och förnamn";
    // Type TEXT
    private static final String FIELD_BEFATTNING = "Befattning";
    // Type TEXT
    private static final String FIELD_TJANSTESTALLE = "Tjänsteställe";
    // Type TEXT
    private static final String FIELD_UTDELNINGSADRESS = "Utdelningsadress";
    // Type TEXT
    private static final String FIELD_POSTNUMMER_2 = "Postnummer_2";
    // Type TEXT
    private static final String FIELD_POSTORT_2 = "Postort_2";
    // Type TEXT
    private static final String FIELD_TELEFON_INKL_RIKTNUMMER = "Telefon inkl riktnummer";
    // Type TEXT
    private static final String FIELD_EPOST = "Epost";

    private static final double ADJUST_BY_1 = 1.0;
    private static final double ADJUST_BY_2 = 2.0;
    private static final double ADJUST_BY_3 = 3.0;
    private static final double ADJUST_BY_4 = 4.0;
    private static final double ADJUST_BY_5 = 5.0;
    private static final double ADJUST_BY_6 = 6.0;
    private static final double ADJUST_BY_9 = 9.0;

    private static final int FIELD_INDEX_3 = 3;
    private static final int FIELD_INDEX_4 = 4;

    private static final float MAX_FONT_SIZE = 9f;
    private static final float MIN_FONT_SIZE = 5f;

    private final DoiUtlatandeV1 doiUtlatandeV1;

    public DoiPdfGenerator(DoiUtlatandeV1 intyg, IntygTexts intygTexts, List<Status> statuses, UtkastStatus utkastStatus)
        throws SoSPdfGeneratorException {
        this(intyg, intygTexts, statuses, utkastStatus, true);
    }

    protected DoiPdfGenerator(DoiUtlatandeV1 utlatande, IntygTexts intygTexts, List<Status> statuses,
        UtkastStatus utkastStatus, boolean flatten) throws SoSPdfGeneratorException {
        try {
            this.doiUtlatandeV1 = utlatande;
            outputStream = new ByteArrayOutputStream();
            PdfReader pdfReader = getPdfReader(utlatande, intygTexts, DEFAULT_PDF_TEMPLATE);

            // If not removing Adobe Reader Usage rights signature, you get a warning when opening i Adobe Reader.
            pdfReader.removeUsageRights();
            PdfStamper pdfStamper = new PdfStamper(pdfReader, this.outputStream);
            fields = pdfStamper.getAcroFields();
            boolean isUtkast = UtkastStatus.getDraftStatuses().contains(utkastStatus);
            boolean isLocked = UtkastStatus.DRAFT_LOCKED == utkastStatus;

            fillAcroformFields();

            if (!isUtkast && !isLocked) {
                // Only signed dbUtlatande prints should have this text
                markAsElectronicCopy(pdfStamper);
                // Only signed doiUtlatande prints should have these decorations
                createRightMarginText(pdfStamper, pdfReader.getNumberOfPages(), utlatande.getId(), WEBCERT_MARGIN_TEXT);
            }

            // Add applicable watermarks
            addIntygStateWatermark(pdfStamper, pdfReader.getNumberOfPages(), isUtkast, isMakulerad(statuses), isLocked);

            pdfStamper.setFormFlattening(flatten);
            pdfStamper.close();

        } catch (Exception e) {
            throw new SoSPdfGeneratorException(e);
        }
    }

    private void fillAcroformFields() throws IOException, DocumentException {
        fillRelation();
        fillPatientDetails();
        fillDateOfDeath();
        fillPlaceOfDeath();
        fillDeathOfChild();
        fillCauseOfDeath();
        fillContributingCauses();
        fillSurgery();
        fillInjuryPoisoning();
        fillBasisForCauseOfDeath();
        fillSignature(doiUtlatandeV1.getGrundData().getSigneringsdatum(), doiUtlatandeV1.getGrundData().getSkapadAv());
    }

    private void fillRelation() {
        if (ersatterTidigareIntyg(doiUtlatandeV1.getGrundData().getRelation())) {
            checkCheckboxField(FIELD_ERSATTER_TIDIGARE_UTFARDAT_INTYG, "On");
        }
    }

    protected void fillPatientDetails() throws IOException, DocumentException {
        fillText(FIELD_PERSONNUMMER,
            doiUtlatandeV1.getGrundData().getPatient().getPersonId().getPersonnummer());
        fillText(FIELD_EFTERNAMN, doiUtlatandeV1.getGrundData().getPatient().getEfternamn());
        fillText(FIELD_FORNAMN, doiUtlatandeV1.getGrundData().getPatient().getFornamn());
        fillText(FIELD_BOSTADSADRESS,
            truncateTextIfNeeded(doiUtlatandeV1.getGrundData().getPatient().getPostadress(), FIELD_BOSTADSADRESS));
        fillText(FIELD_POSTNUMMER, doiUtlatandeV1.getGrundData().getPatient().getPostnummer());
        fillText(FIELD_POSTORT, truncateTextIfNeeded(doiUtlatandeV1.getGrundData().getPatient().getPostort(), FIELD_POSTORT));

        fillText(FIELD_IDENTITETEN_STYRKT_GENOM, doiUtlatandeV1.getIdentitetStyrkt());
        fillText(FIELD_LAND_OM_EJ_STADIGVARANDE_BOSATT_I_SVERIGE, doiUtlatandeV1.getLand());
    }

    private void fillDateOfDeath() {
        Boolean certainDateOfDeath = doiUtlatandeV1.getDodsdatumSakert();
        if (certainDateOfDeath != null) {
            if (certainDateOfDeath) {
                checkCheckboxField(FIELD_DODSDATUM_SAKERT, "On");
            } else {
                checkCheckboxField(FIELD_DODSDATUM_EJ_SAKERT, "On");
            }
        }
        adjustAndFill(FIELD_DODSDATUM, doiUtlatandeV1.getDodsdatum(), ADJUST_BY_5);
        adjustAndFill(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, doiUtlatandeV1.getAntraffatDodDatum(), ADJUST_BY_5);
    }

    private void fillPlaceOfDeath() {
        DodsplatsBoende accomodation = doiUtlatandeV1.getDodsplatsBoende();
        if (accomodation != null) {
            String fieldToCheckForAccomodation = getFieldToCheckForAccomodation(accomodation);
            if (fieldToCheckForAccomodation.equals(FIELD_DODSPLATS_SJUKUS)
                || fieldToCheckForAccomodation.equals(FIELD_DODSPLATS_ORDINART_BOENDE)) {
                checkCheckboxField(fieldToCheckForAccomodation, "Ja");
            } else {
                checkCheckboxField(fieldToCheckForAccomodation, "On");
            }
        }

        adjustAndFill(FIELD_KOMMUN_OM_OKAND_DODSPLATS_KOMMUNEN_DAR_KROPPEN_PATRAFFADES, doiUtlatandeV1.getDodsplatsKommun(), ADJUST_BY_9);
    }

    private void fillDeathOfChild() {
        // NOTE: The doi form option 'dödfött' is not possible in certificates issued in Webcert.
        Boolean childDeadWithin28Days = doiUtlatandeV1.getBarn();
        if (childDeadWithin28Days != null && childDeadWithin28Days) {
            checkCheckboxField(FIELD_AVLIDET_INOM_28_DYGN, "On");
        }
    }

    private void fillCauseOfDeath() throws DocumentException, IOException {
        // Terminal cause of death (1)
        final var fieldDescriptionMainCause = String.format(FIELD_DODSORSAK_FIRSTROW_BESKRIVNING, 1);
        fillCauseOfDeathRow(1, fieldDescriptionMainCause, doiUtlatandeV1.getTerminalDodsorsak());

        // Contributing factors (2,3,4)
        if (doiUtlatandeV1.getFoljd() != null) {
            final var drDictionary = fields.getFieldItem(fieldDescriptionMainCause).getMerged(0).getAsDict(PdfName.DR);
            for (int i = 0; i < doiUtlatandeV1.getFoljd().size(); i++) {
                final var fieldDescriptionSecondaryCause = String.format(FIELD_DODSORSAK_ROW_BESKRIVNING, 2 + i);
                setFieldFont(drDictionary, fieldDescriptionSecondaryCause);
                fillCauseOfDeathRow(2 + i, fieldDescriptionSecondaryCause, doiUtlatandeV1.getFoljd().get(i));
            }
        }
    }

    private void fillCauseOfDeathRow(int index, String fieldDescription, Dodsorsak orsak) throws DocumentException, IOException {
        final var fieldDebut = String.format(FIELD_DODSORSAK_ROW_UNGEFARLIG_DEBUT, index);
        final var fieldAcute = String.format(FIELD_DODSORSAK_ROW_AKUT, index);
        final var fieldChronic = String.format(FIELD_DODSORSAK_ROW_KRONISK, index);
        final var fieldNotAvailable = String.format(FIELD_DODSORSAK_ROW_INGEN_UPPGIFT, index);

        setCauseOfDeathFields(orsak, fieldDescription, ADJUST_BY_1, fieldDebut, fieldAcute, fieldChronic, fieldNotAvailable);
    }

    private void fillContributingCauses() throws DocumentException, IOException {

        // Other contributing factors (1..8)
        if (doiUtlatandeV1.getBidragandeSjukdomar() != null) {
            for (int i = 0; i < doiUtlatandeV1.getBidragandeSjukdomar().size(); i++) {
                fillContributingCauseRow(i + 1, doiUtlatandeV1.getBidragandeSjukdomar().get(i));
            }
        }
    }

    private void fillContributingCauseRow(int index, Dodsorsak cause) throws DocumentException, IOException {
        final var fieldDescription = String.format(FIELD_BIDRAGANDE_DODSORSAK_ROW_BESKRIVNING, index);
        final var fieldDebut = String.format(FIELD_BIDRAGANDE_DODSORSAK_ROW_UNGEFARLIG_DEBUT, FIELD_INDEX_4 + index);
        final var fieldAcute = String.format(FIELD_DODSORSAK_ROW_AKUT, FIELD_INDEX_4 + index);
        final var fieldChronic = String.format(FIELD_DODSORSAK_ROW_KRONISK, FIELD_INDEX_4 + index);
        final var fieldNotAvailable = String.format(FIELD_DODSORSAK_ROW_INGEN_UPPGIFT, FIELD_INDEX_4 + index);

        setCauseOfDeathFields(cause, fieldDescription, ADJUST_BY_2, fieldDebut, fieldAcute, fieldChronic, fieldNotAvailable);
    }

    private void setCauseOfDeathFields(Dodsorsak cause, String fieldDescription, double adjustment, String fieldDebut, String fieldAcute,
        String fieldKronisk, String fieldNotAvailable) throws DocumentException, IOException {
        if (cause != null) {
            setCauseOfDeathFieldFontSize(fieldDescription, cause.getBeskrivning());
            adjustAndFill(fieldDescription, cause.getBeskrivning(), adjustment);
            adjustAndFill(fieldDebut, cause.getDatum(), ADJUST_BY_5);
            checkCheckboxField(fieldAcute, Specifikation.PLOTSLIG == cause.getSpecifikation() ? "Ja" : "");
            checkCheckboxField(fieldKronisk, Specifikation.KRONISK == cause.getSpecifikation() ? "Ja" : "");
            checkCheckboxField(fieldNotAvailable, Specifikation.UPPGIFT_SAKNAS == cause.getSpecifikation() ? "Ja" : "");
        }
    }

    private void fillSurgery() {
        OmOperation operation = doiUtlatandeV1.getOperation();
        if (operation != null) {
            String fieldToCheckForCauseOfInjury = getFieldToCheckForSurgery(operation);
            checkCheckboxField(fieldToCheckForCauseOfInjury, "On");
        }

        fillText(FIELD_OPERATIONSDATUM_AR_MAN_DAG, doiUtlatandeV1.getOperationDatum());
        adjustAndFill(FIELD_TILLSTAND_SOM_FORANLEDDE_INGREPPET, doiUtlatandeV1.getOperationAnledning(), ADJUST_BY_4);
    }

    private void fillInjuryPoisoning() {
        ForgiftningOrsak causeOfInjury = doiUtlatandeV1.getForgiftningOrsak();
        if (causeOfInjury != null) {
            String fieldToCheckForCauseOfInjury = getFieldToCheckForCauseOfInjury(causeOfInjury);
            checkCheckboxField(fieldToCheckForCauseOfInjury, "Ja");
        }

        fillText(FIELD_DATUM_FOR_SKADAFORGIFTNING_AR_MAN_DAG, doiUtlatandeV1.getForgiftningDatum());
        adjustAndFill(FIELD_KORT_BESKRIVNING_AV_HUR_SKADANFORGIFTNINGEN_UPPKOM,
            doiUtlatandeV1.getForgiftningUppkommelse(), ADJUST_BY_1);
    }

    private void fillBasisForCauseOfDeath() {
        checkCheckboxField(FIELD_UNDERSOKNING_FORE_DODEN, hasDodsorsaksGrund(Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN) ? "Ja" : "");
        checkCheckboxField(FIELD_YTTRE_UNDERSOKNING_EFTER_DODEN, hasDodsorsaksGrund(Dodsorsaksgrund.UNDERSOKNING_EFTER_DODEN) ? "Ja" : "");
        checkCheckboxField(FIELD_KLINISK_OBDUKTION, hasDodsorsaksGrund(Dodsorsaksgrund.KLINISK_OBDUKTION) ? "Ja" : "");
        checkCheckboxField(FIELD_RATTSMEDICINSK_OBDUKTION, hasDodsorsaksGrund(Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION) ? "Ja" : "");
        checkCheckboxField(FIELD_RATTSMEDICINSK_LIKBESIKTNING, hasDodsorsaksGrund(Dodsorsaksgrund.RATTSMEDICINSK_BESIKTNING) ? "Ja" : "");
    }

    private boolean hasDodsorsaksGrund(Dodsorsaksgrund dodsorsaksgrund) {
        if (doiUtlatandeV1.getGrunder() != null) {
            return doiUtlatandeV1.getGrunder().contains(dodsorsaksgrund);
        }
        return false;
    }

    private void fillSignature(LocalDateTime signeringsDatum, HoSPersonal skapadAv) throws IOException, DocumentException {
        if (signeringsDatum != null) {
            adjustAndFill(FIELD_ORT_OCH_DATUM, signeringsDatum.format(DATE_FORMAT), ADJUST_BY_6);
            adjustAndFill(FIELD_LAKARENS_EFTERNAMN_OCH_FORNAMN, skapadAv.getFullstandigtNamn(), ADJUST_BY_6);
            adjustAndFill(FIELD_BEFATTNING, String.join(", ", skapadAv.getBefattningar()), ADJUST_BY_6);
        }
        adjustAndFill(FIELD_TJANSTESTALLE, String.join(", ", skapadAv.getVardenhet().getEnhetsnamn()
            + ", " + skapadAv.getVardenhet().getVardgivare().getVardgivarnamn()), ADJUST_BY_6);
        adjustAndFill(FIELD_UTDELNINGSADRESS, truncateTextIfNeeded(skapadAv.getVardenhet().getPostadress(),
            FIELD_UTDELNINGSADRESS), ADJUST_BY_6);
        adjustAndFill(FIELD_POSTNUMMER_2, skapadAv.getVardenhet().getPostnummer(), ADJUST_BY_3);
        adjustAndFill(FIELD_POSTORT_2, truncateTextIfNeeded(skapadAv.getVardenhet().getPostort(), FIELD_POSTORT_2), ADJUST_BY_3);
        adjustAndFill(FIELD_TELEFON_INKL_RIKTNUMMER, skapadAv.getVardenhet().getTelefonnummer(), ADJUST_BY_6);
        adjustAndFill(FIELD_EPOST, skapadAv.getVardenhet().getEpost(), ADJUST_BY_6);
    }

    private String getFieldToCheckForAccomodation(DodsplatsBoende accomodation) {
        switch (accomodation) {
            case SJUKHUS:
                return FIELD_DODSPLATS_SJUKUS;
            case ORDINART_BOENDE:
                return FIELD_DODSPLATS_ORDINART_BOENDE;
            case SARSKILT_BOENDE:
                return FIELD_DODSPLATS_SARSKILT_BOENDE;
            case ANNAN:
                return FIELD_DODSPLATS_ANNAN_OKAND;
            default:
                return "";
        }
    }

    private String getFieldToCheckForCauseOfInjury(ForgiftningOrsak causeOfInjury) {
        switch (causeOfInjury) {
            case OLYCKSFALL:
                return FIELD_OLYCKSFALL;
            case SJALVMORD:
                return FIELD_SJALVMORD;
            case AVSIKTLIGT_VALLAD:
                return FIELD_AVSIKTLIGT_VALLAD_AV_ANNAN;
            case OKLART:
                return FIELD_OKLART_OM_AVSIKT_FORELEGAT;
            default:
                return "";
        }
    }

    private String getFieldToCheckForSurgery(OmOperation operation) {
        switch (operation) {
            case JA:
                return FIELD_OPERERAD_JA;
            case NEJ:
                return FIELD_OPERERAD_NEJ;
            case UPPGIFT_SAKNAS:
                return FIELD_OPERERAD_UPPGIFT_SAKNAS;
            default:
                return "";
        }
    }

    private void setFieldFont(PdfDictionary drDictionary, String fieldDescriptionSecondaryCause) {
        // Sets font in causeOfDeathFields B-D based on font in field A. This is done because fields B-D
        // get deviating font from original PDF form.
        if (drDictionary != null && drDictionary.isDictionary()) {
            fields.getFieldItem(fieldDescriptionSecondaryCause).getMerged(0).put(PdfName.DR, drDictionary);
        }
    }

    private void setCauseOfDeathFieldFontSize(String fieldDescription, String text) throws DocumentException, IOException {
        final var calculatedFontSize = calculateFontSize(fieldDescription, text);
        fields.setFieldProperty(fieldDescription, "textsize", calculatedFontSize, null);
    }

    private float calculateFontSize(String fieldDescription, String text) throws DocumentException, IOException {
        if (text == null) {
            return MAX_FONT_SIZE;
        }

        final var fieldCoordinates = getFieldCoordinates(fieldDescription);
        final var fieldArea = getFieldRectangle(fieldCoordinates);
        final var fieldHeight = fieldArea.getHeight();
        final var fieldWidth = fieldArea.getWidth() - 4f;
        final var baseFont = BaseFont.createFont();
        final var phrase = new Phrase(new Chunk(text, new Font(baseFont, 0, 0, GrayColor.GRAYBLACK)));
        final var leadingFactor = baseFont.getFontDescriptor(BaseFont.BBOXURY, 1) - baseFont.getFontDescriptor(BaseFont.BBOXLLY, 1);

        final var columnText = new ColumnText(null);
        columnText.setSimpleColumn(0, -fieldHeight, fieldWidth, 0);
        columnText.setAlignment(Element.ALIGN_LEFT);
        columnText.setRunDirection(PdfWriter.RUN_DIRECTION_NO_BIDI);

        final var step = 0.2f;
        float fontSize = MAX_FONT_SIZE;
        for (; fontSize > MIN_FONT_SIZE; fontSize -= step) {
            columnText.setYLine(0);
            changeFontSize(phrase, fontSize);
            columnText.setText(phrase);
            columnText.setLeading(leadingFactor * fontSize);
            if ((columnText.go(true) & ColumnText.NO_MORE_COLUMN) == 0) {
                return fontSize;
            }
        }

        return MIN_FONT_SIZE;
    }

    private Rectangle getFieldRectangle(PdfArray fieldCoordinates) {
        return new Rectangle(fieldCoordinates.getAsNumber(0).floatValue(), fieldCoordinates.getAsNumber(1).floatValue(),
            fieldCoordinates.getAsNumber(2).floatValue(), fieldCoordinates.getAsNumber(FIELD_INDEX_3).floatValue() - 1f);
    }

    private void changeFontSize(Phrase phrase, float size) {
        for (Element element : phrase) {
            ((Chunk) element).getFont().setSize(size);
        }
    }
}
