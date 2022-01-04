/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.db.v1.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfNumber;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.sos_parent.pdf.AbstractSoSPdfGenerator;
import se.inera.intyg.common.sos_parent.pdf.SoSPdfGeneratorException;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;

/**
 * Created by marced on 2017-10-11.
 */
public class DbPdfGenerator extends AbstractSoSPdfGenerator {

    public static final String DEFAULT_PDF_TEMPLATE = "pdf/Blankett-Dodsbevis-2018-54.pdf";

    /*
     * Acrofield name constants (extracted from pdf template file using util class
     * se.inera.dbUtlatande.common.db.pdf.ListAcrofields)
     */

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_ERSATTER_TIDIGARE_UTFARDAT_DODSBEVIS = "Ersätter tidigare";
    // Type TEXT
    private static final String FIELD_PERSONNUMMERSAMORDNINGSNUMMER_12_SIFFROR = "Personnummersamordningsnummer 12 siffror";
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
    private static final String FIELD_IDENTITETEN_STYRKT_GENOM = "Identiteten styrkt genom";


    // Type TEXT
    private static final String FIELD_AR_MAN_DAG_DODSDATUM = "År mån dag dödsdatum";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_DODSDATUM_SAKERT = "Säkert";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_DODSDATUM_EJ_SAKERT = "Ej säkert";
    // Type TEXT
    private static final String FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD = "År mån dag Om dödsdatum";


    // Type TEXT
    private static final String FIELD_KOMMUN_OM_OKAND_DODSPLATS = "Kommun om okänd dödsplats kommunen där kroppen påträffades";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_SJUKHUS = "Sjukhus";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_ORDINART_BOENDE = "Ordinärt boende";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_SARSKILT_BOENDE = "Särskild boende";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_ANNAN_OKANT = "Annan/okänd";


    // Type CHECKBOX - values [Ja]
    private static final String FIELD_AVLIDET_INOM_28_DAGAR = "Avlidit";


    // Type CHECKBOX - values [Ja]
    private static final String FIELD_JA_IMPLANTAT = "Ja";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_NEJ_IMPLANTAT = "Nej";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_JA_IMPLANTAT_AVLAGSNAT = "Ja implantat";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_NEJ_IMPLANTAT_AVLAGSNAT = "nej implantat";


    // Type CHECKBOX - values [Ja]
    private static final String FIELD_YTTRE_UNDERSOKNING_JA = "Yttre Ja";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_YTTRE_UNDERSOKNING_NEJ = "Yttre nej";
    // Type TEXT
    private static final String FIELD_YTTRE_UNDERSOKNING_AR_MAN_DAG = "År mån dag Yttre";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_NEJ_SKA_GORAS = "Yttre Nej, rättsmedicinsk";


    // Type CHECKBOX - values [Ja]
    private static final String FIELD_POLISANMALAN_JA = "Polisanmälan Ja";
    // Type CHECKBOX - values [Ja]
    private static final String FIELD_POLISANMALAN_NEJ = "Polisanmälan_nej";


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

    private static final double CHECKBOX_X_ADJUST = 1.0;
    private static final double CHECKBOX_Y_ADJUST = 0.85;

    private static final double ADJUST_BY_3 = 3.0;
    private static final double ADJUST_BY_5 = 5.0;
    private static final double ADJUST_BY_6 = 6.0;
    private static final double ADJUST_BY_9 = 9.0;


    private final DbUtlatandeV1 dbUtlatandeV1;

    public DbPdfGenerator(DbUtlatandeV1 intyg, IntygTexts intygTexts, List<Status> statuses, UtkastStatus utkastStatus)
        throws SoSPdfGeneratorException {
        this(intyg, intygTexts, statuses, utkastStatus, true);
    }

    // Only called directly by tests.
    DbPdfGenerator(DbUtlatandeV1 utlatande, IntygTexts intygTexts, List<Status> statuses, UtkastStatus utkastStatus, boolean flatten)
        throws SoSPdfGeneratorException {
        try {
            this.dbUtlatandeV1 = utlatande;
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
                // Only signed dbUtlatande prints should have these decorations
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
        fillExplosiveImplant();
        fillExternalExamination();
        fillPoliceReport();
        fillSignature(dbUtlatandeV1.getGrundData().getSigneringsdatum(), dbUtlatandeV1.getGrundData().getSkapadAv());
    }

    private void fillRelation() {
        if (ersatterTidigareIntyg(dbUtlatandeV1.getGrundData().getRelation())) {
            adjustAndFillCheckBox(FIELD_ERSATTER_TIDIGARE_UTFARDAT_DODSBEVIS, "Ja");
        }
    }

    private void fillSignature(LocalDateTime signeringsDatum, HoSPersonal skapadAv) throws IOException, DocumentException {
        if (signeringsDatum != null) {
            adjustAndFill(FIELD_ORT_OCH_DATUM, signeringsDatum.format(DATE_FORMAT), ADJUST_BY_6);
            adjustAndFill(FIELD_LAKARENS_EFTERNAMN_OCH_FORNAMN, skapadAv.getFullstandigtNamn(), ADJUST_BY_6);
            adjustAndFill(FIELD_BEFATTNING, String.join(", ", skapadAv.getBefattningar()), ADJUST_BY_6);
        }
        adjustAndFill(FIELD_TJANSTESTALLE, String.join(", ", skapadAv.getVardenhet().getEnhetsnamn()
            + ", " + skapadAv.getVardenhet().getVardgivare().getVardgivarnamn()), ADJUST_BY_6);
        adjustAndFill(FIELD_UTDELNINGSADRESS,
            truncateTextIfNeeded(skapadAv.getVardenhet().getPostadress(), FIELD_UTDELNINGSADRESS), ADJUST_BY_6);
        adjustAndFill(FIELD_POSTNUMMER_2, skapadAv.getVardenhet().getPostnummer(), ADJUST_BY_6);
        adjustAndFill(FIELD_POSTORT_2, truncateTextIfNeeded(skapadAv.getVardenhet().getPostort(), FIELD_POSTORT_2), ADJUST_BY_6);
        adjustAndFill(FIELD_TELEFON_INKL_RIKTNUMMER, skapadAv.getVardenhet().getTelefonnummer(), ADJUST_BY_6);
        adjustAndFill(FIELD_EPOST, skapadAv.getVardenhet().getEpost(), ADJUST_BY_6);
    }

    private void fillPoliceReport() {
        Boolean policeReport = dbUtlatandeV1.getPolisanmalan();
        if (policeReport != null) {
            if (policeReport) {
                adjustAndFillCheckBox(FIELD_POLISANMALAN_JA, "Ja");
            } else {
                adjustAndFillCheckBox(FIELD_POLISANMALAN_NEJ, "Ja");
            }
        }
    }

    private void fillExternalExamination() {

        if (dbUtlatandeV1.getUndersokningYttre() != null) {
            if (dbUtlatandeV1.getUndersokningYttre() == Undersokning.JA) {
                adjustAndFillCheckBox(FIELD_YTTRE_UNDERSOKNING_JA, "Ja");
            } else if (dbUtlatandeV1.getUndersokningYttre() == Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN) {
                adjustAndFillCheckBox(FIELD_YTTRE_UNDERSOKNING_NEJ, "Ja");
            } else if (dbUtlatandeV1.getUndersokningYttre() == Undersokning.UNDERSOKNING_SKA_GORAS) {
                adjustAndFillCheckBox(FIELD_NEJ_SKA_GORAS, "Ja");
            }
        }

        adjustAndFill(FIELD_YTTRE_UNDERSOKNING_AR_MAN_DAG, dbUtlatandeV1.getUndersokningDatum(), ADJUST_BY_5);
    }

    private void fillExplosiveImplant() {

        Boolean explosiveImplant = dbUtlatandeV1.getExplosivImplantat();
        if (explosiveImplant != null) {
            if (explosiveImplant) {
                adjustAndFillCheckBox(FIELD_JA_IMPLANTAT, "Ja");
            } else {
                adjustAndFillCheckBox(FIELD_NEJ_IMPLANTAT, "Ja");
            }
        }

        Boolean explosiveImplantRemoved = dbUtlatandeV1.getExplosivAvlagsnat();
        if (explosiveImplantRemoved != null) {
            if (explosiveImplantRemoved) {
                adjustAndFillCheckBox(FIELD_JA_IMPLANTAT_AVLAGSNAT, "Ja");
            } else {
                adjustAndFillCheckBox(FIELD_NEJ_IMPLANTAT_AVLAGSNAT, "Ja");
            }
        }
    }

    private void fillDeathOfChild() {
        // NOTE: The db form option 'dödfött' is not possible in certificates issued in Webcert.
        Boolean childDeadWithin28Days = dbUtlatandeV1.getBarn();
        if (childDeadWithin28Days != null && childDeadWithin28Days) {
            adjustAndFillCheckBox(FIELD_AVLIDET_INOM_28_DAGAR, "Ja");
        }
    }

    private void fillDateOfDeath() {

        Boolean certainDateOfDeath = dbUtlatandeV1.getDodsdatumSakert();
        if (certainDateOfDeath != null) {
            if (certainDateOfDeath) {
                adjustAndFillCheckBox(FIELD_DODSDATUM_SAKERT, "Ja");
            } else {
                adjustAndFillCheckBox(FIELD_DODSDATUM_EJ_SAKERT, "Ja");
            }
        }

        adjustAndFill(FIELD_AR_MAN_DAG_DODSDATUM, dbUtlatandeV1.getDodsdatum(), ADJUST_BY_3);
        fillText(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, dbUtlatandeV1.getAntraffatDodDatum());
    }

    private void fillPlaceOfDeath() {

        DodsplatsBoende dodsplatsBoende = dbUtlatandeV1.getDodsplatsBoende();
        if (dodsplatsBoende != null) {
            String accomodationAtTimeOfDeath = getAccomodationAtTimeOfDeath(dodsplatsBoende);
            adjustAndFillCheckBox(accomodationAtTimeOfDeath, "Ja");
        }

        adjustAndFill(FIELD_KOMMUN_OM_OKAND_DODSPLATS, dbUtlatandeV1.getDodsplatsKommun(), ADJUST_BY_9);
        fillText(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, dbUtlatandeV1.getAntraffatDodDatum());
    }

    protected void fillPatientDetails() throws IOException, DocumentException {
        adjustAndFill(FIELD_PERSONNUMMERSAMORDNINGSNUMMER_12_SIFFROR, dbUtlatandeV1.getGrundData().getPatient().getPersonId()
            .getPersonnummer(), ADJUST_BY_9);
        adjustAndFill(FIELD_EFTERNAMN, dbUtlatandeV1.getGrundData().getPatient().getEfternamn(), ADJUST_BY_6);
        adjustAndFill(FIELD_FORNAMN, dbUtlatandeV1.getGrundData().getPatient().getFornamn(), ADJUST_BY_6);
        adjustAndFill(FIELD_BOSTADSADRESS,
            truncateTextIfNeeded(dbUtlatandeV1.getGrundData().getPatient().getPostadress(), FIELD_BOSTADSADRESS), ADJUST_BY_6);
        adjustAndFill(FIELD_POSTNUMMER, dbUtlatandeV1.getGrundData().getPatient().getPostnummer(), ADJUST_BY_6);
        adjustAndFill(FIELD_POSTORT,
            truncateTextIfNeeded(dbUtlatandeV1.getGrundData().getPatient().getPostort(), FIELD_POSTORT), ADJUST_BY_6);

        adjustAndFill(FIELD_IDENTITETEN_STYRKT_GENOM, dbUtlatandeV1.getIdentitetStyrkt(), ADJUST_BY_6);
    }

    private String getAccomodationAtTimeOfDeath(DodsplatsBoende dodsplatsBoende) {
        switch (dodsplatsBoende) {
            case SJUKHUS:
                return FIELD_SJUKHUS;
            case ORDINART_BOENDE:
                return FIELD_ORDINART_BOENDE;
            case SARSKILT_BOENDE:
                return FIELD_SARSKILT_BOENDE;
            case ANNAN:
                return FIELD_ANNAN_OKANT;
            default :
                return "";
        }
    }

    // Adjusts the position of a checkbox field, Y-indexes are 1 (bottom) and 3 (top), X-indexes are 0 and 2.
    private void adjustAndFillCheckBox(String fieldId, String value) {
        if (value != null) {
            PdfArray fieldCoordinates = getFieldCoordinates(fieldId);
            fieldCoordinates.set(1, new PdfNumber(fieldCoordinates.getAsNumber(1).doubleValue() - CHECKBOX_Y_ADJUST));
            //noinspection CheckStyle
            fieldCoordinates.set(3, new PdfNumber(fieldCoordinates.getAsNumber(3).doubleValue() - CHECKBOX_Y_ADJUST));
            fieldCoordinates.set(0, new PdfNumber(fieldCoordinates.getAsNumber(0).doubleValue() - CHECKBOX_X_ADJUST));
            fieldCoordinates.set(2, new PdfNumber(fieldCoordinates.getAsNumber(2).doubleValue() - CHECKBOX_X_ADJUST));
            checkCheckboxField(fieldId, value);
        }
    }
}
