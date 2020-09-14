/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import java.io.ByteArrayOutputStream;
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
import se.inera.intyg.common.support.model.InternalDate;
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

    // Type TEXT
    private static final String FIELD_EFTERNAMN = "Efternamn";

    // Type TEXT
    private static final String FIELD_TELEFON_INKL_RIKTNUMMER = "Telefon inkl riktnummer";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_ERSATTER_TIDIGARE_UTFARDAT_DODSBEVIS = "Ersätter tidigare";

    // Type TEXT
    private static final String FIELD_BOSTADSADRESS = "Bostadsadress";

    // Type TEXT
    private static final String FIELD_TJANSTESTALLE = "Tjänsteställe";

    // Type TEXT
    private static final String FIELD_POSTNUMMER_2 = "Postnummer_2";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_DODSDATUM_SAKERT = "Säkert";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_DODSDATUM_EJ_SAKERT = "Ej säkert";

    // Type TEXT
    private static final String FIELD_POSTORT_2 = "Postort_2";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_JA_IMPLANTAT = "Ja";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_NEJ_IMPLANTAT = "Nej";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_JA_IMPLANTAT_AVLAGSNAT = "Ja implantat";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_NEJ_IMPLANTAT_AVLAGSNAT = "nej implantat";

    // Type TEXT
    private static final String FIELD_POSTORT = "Postort";

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
    private static final String FIELD_YTTRE_UNDERSOKNING_JA = "Yttre Ja";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_YTTRE_UNDERSOKNING_NEJ = "Yttre nej";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_NEJ_SKA_GORAS = "Yttre Nej, rättsmedicinsk";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_POLISANMALAN_JA = "Polisanmälan Ja";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_POLISANMALAN_NEJ = "Polisanmälan_nej";

    // Type TEXT
    private static final String FIELD_YTTRE_UNDERSOKNING_AR_MAN_DAG = "År mån dag Yttre";

    // Type TEXT
    private static final String FIELD_FORNAMN = "Förnamn";

    // Type TEXT
    private static final String FIELD_IDENTITETEN_STYRKT_GENOM = "Identiteten styrkt genom";

    // Type TEXT
    private static final String FIELD_AR_MAN_DAG_DODSDATUM = "År mån dag dödsdatum";

    // Type TEXT
    private static final String FIELD_EPOST = "Epost";

    // Type TEXT
    private static final String FIELD_POSTNUMMER = "Postnummer";

    // Type TEXT
    private static final String FIELD_UTDELNINGSADRESS = "Utdelningsadress";

    // Type TEXT
    private static final String FIELD_PERSONNUMMERSAMORDNINGSNUMMER_12_SIFFROR = "Personnummersamordningsnummer 12 siffror";

    // Type TEXT
    private static final String FIELD_ORT_OCH_DATUM = "Ort och datum";

    // Type TEXT
    private static final String FIELD_KOMMUN_OM_OKAND_DODSPLATS = "Kommun om okänd dödsplats kommunen där kroppen påträffades";

    // Type TEXT
    private static final String FIELD_LAKARENS_EFTERNAMN_OCH_FORNAMN = "Läkarens efternamn och förnamn";

    // Type TEXT
    private static final String FIELD_BEFATTNING = "Befattning";

    // Type TEXT
    private static final String FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD = "År mån dag Om dödsdatum";

    private DbUtlatandeV1 dbUtlatandeV1;

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

    private void fillAcroformFields() {
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
            // Type CHECKBOX - values [Ja]
            processCheckBoxField(FIELD_ERSATTER_TIDIGARE_UTFARDAT_DODSBEVIS, "Ja");
        }
    }

    private void fillSignature(LocalDateTime signeringsDatum, HoSPersonal skapadAv) {
        processTextField(FIELD_ORT_OCH_DATUM, signeringsDatum != null ? signeringsDatum.format(DATE_FORMAT) : "");
        processTextField(FIELD_LAKARENS_EFTERNAMN_OCH_FORNAMN, skapadAv.getFullstandigtNamn());
        processTextField(FIELD_BEFATTNING, String.join(", ", skapadAv.getBefattningar()));
        processTextField(FIELD_TJANSTESTALLE, String.join(", ", skapadAv.getVardenhet().getEnhetsnamn()
            + ", " + skapadAv.getVardenhet().getVardgivare().getVardgivarnamn()));
        processTextField(FIELD_UTDELNINGSADRESS, skapadAv.getVardenhet().getPostadress());
        processTextField(FIELD_POSTNUMMER_2, skapadAv.getVardenhet().getPostnummer());
        processTextField(FIELD_POSTORT_2, skapadAv.getVardenhet().getPostort());
        processTextField(FIELD_TELEFON_INKL_RIKTNUMMER, skapadAv.getVardenhet().getTelefonnummer());
        processTextField(FIELD_EPOST, skapadAv.getVardenhet().getEpost());
    }

    private void fillPoliceReport() {
        Boolean policeReport = dbUtlatandeV1.getPolisanmalan();
        if (policeReport != null) {
            if (policeReport) {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_POLISANMALAN_JA, "Ja");
            } else {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_POLISANMALAN_NEJ, "Ja");
            }
        }
    }

    private void fillExternalExamination() {

        if (dbUtlatandeV1.getUndersokningYttre() != null) {
            if (dbUtlatandeV1.getUndersokningYttre() == Undersokning.JA) {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_YTTRE_UNDERSOKNING_JA, "Ja");
            } else if (dbUtlatandeV1.getUndersokningYttre() == Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN) {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_YTTRE_UNDERSOKNING_NEJ, "Ja");
            } else if (dbUtlatandeV1.getUndersokningYttre() == Undersokning.UNDERSOKNING_SKA_GORAS) {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_NEJ_SKA_GORAS, "Ja");
            }
        }

        processDateField(FIELD_YTTRE_UNDERSOKNING_AR_MAN_DAG, dbUtlatandeV1.getUndersokningDatum());
    }

    private void fillExplosiveImplant() {

        Boolean explosiveImplant = dbUtlatandeV1.getExplosivImplantat();
        if (explosiveImplant != null) {
            if (explosiveImplant) {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_JA_IMPLANTAT, "Ja");
            } else {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_NEJ_IMPLANTAT, "Ja");
            }
        }

        Boolean explosiveImplantRemoved = dbUtlatandeV1.getExplosivAvlagsnat();
        if (explosiveImplantRemoved != null) {
            if (explosiveImplantRemoved) {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_JA_IMPLANTAT_AVLAGSNAT, "Ja");
            } else {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_NEJ_IMPLANTAT_AVLAGSNAT, "Ja");
            }
        }
    }

    private void fillDeathOfChild() {
        // NOTE: The db form option 'dödfött' is not possible in certificates issued in Webcert.
        Boolean childDeadWithin28Days = dbUtlatandeV1.getBarn();
        if (childDeadWithin28Days != null && childDeadWithin28Days) {
            // Type CHECKBOX - values [Ja]
            processCheckBoxField(FIELD_AVLIDET_INOM_28_DAGAR, "Ja");
        }
    }

    private void fillDateOfDeath() {

        Boolean certainDateOfDeath = dbUtlatandeV1.getDodsdatumSakert();
        if (certainDateOfDeath != null) {
            if (certainDateOfDeath) {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_DODSDATUM_SAKERT, "Ja");
            } else {
                // Type CHECKBOX - values [Ja]
                processCheckBoxField(FIELD_DODSDATUM_EJ_SAKERT, "Ja");
            }
        }

        processDateField(FIELD_AR_MAN_DAG_DODSDATUM, dbUtlatandeV1.getDodsdatum());
        fillText(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, dbUtlatandeV1.getAntraffatDodDatum());
    }

    private void fillPlaceOfDeath() {

        DodsplatsBoende dodsplatsBoende = dbUtlatandeV1.getDodsplatsBoende();
        if (dodsplatsBoende != null) {

            switch (dodsplatsBoende) {
                case SJUKHUS:
                    // Type CHECKBOX - values [Ja]
                    processCheckBoxField(FIELD_SJUKHUS, "Ja");
                    break;
                case ORDINART_BOENDE:
                    // Type CHECKBOX - values [Ja]
                    processCheckBoxField(FIELD_ORDINART_BOENDE, "Ja");
                    break;
                case SARSKILT_BOENDE:
                    // Type CHECKBOX - values [Ja]
                    processCheckBoxField(FIELD_SARSKILT_BOENDE, "Ja");
                    break;
                case ANNAN:
                    // Type CHECKBOX - values [Ja]
                    processCheckBoxField(FIELD_ANNAN_OKANT, "Ja");
            }
        }

        processTextField(FIELD_KOMMUN_OM_OKAND_DODSPLATS, dbUtlatandeV1.getDodsplatsKommun());
        fillText(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, dbUtlatandeV1.getAntraffatDodDatum());
    }

    protected void fillPatientDetails() {
        processTextField(FIELD_PERSONNUMMERSAMORDNINGSNUMMER_12_SIFFROR, dbUtlatandeV1.getGrundData().getPatient().getPersonId()
            .getPersonnummer());
        processTextField(FIELD_EFTERNAMN, dbUtlatandeV1.getGrundData().getPatient().getEfternamn());
        processTextField(FIELD_FORNAMN, dbUtlatandeV1.getGrundData().getPatient().getFornamn());
        processTextField(FIELD_BOSTADSADRESS, dbUtlatandeV1.getGrundData().getPatient().getPostadress());
        processTextField(FIELD_POSTNUMMER, dbUtlatandeV1.getGrundData().getPatient().getPostnummer());
        processTextField(FIELD_POSTORT, dbUtlatandeV1.getGrundData().getPatient().getPostort());

        processTextField(FIELD_IDENTITETEN_STYRKT_GENOM, dbUtlatandeV1.getIdentitetStyrkt());
    }

    private PdfArray getFieldCoordinates(String fieldId) {
        AcroFields.Item field = fields.getFieldItem(fieldId);
        PdfDictionary pdfDictionary = field.getWidget(0);
        return pdfDictionary.getAsArray(PdfName.RECT);
    }

    @SuppressWarnings("CheckStyle")
    private void processCheckBoxField(String fieldId, String value) {
        PdfArray fieldCoordinates = getFieldCoordinates(fieldId);
        fieldCoordinates.set(1, new PdfNumber(fieldCoordinates.getAsNumber(1).doubleValue() - 0.85));
        fieldCoordinates.set(3, new PdfNumber(fieldCoordinates.getAsNumber(3).doubleValue() - 0.85));
        fieldCoordinates.set(0, new PdfNumber(fieldCoordinates.getAsNumber(0).doubleValue() - 1));
        fieldCoordinates.set(2, new PdfNumber(fieldCoordinates.getAsNumber(2).doubleValue() - 1));
        checkCheckboxField(fieldId, value);
    }

    @SuppressWarnings("CheckStyle")
    private void processTextField(String fieldId, String value) {
        PdfArray fieldCoordinates = getFieldCoordinates(fieldId);
        double adjustment = fieldId.equals(FIELD_PERSONNUMMERSAMORDNINGSNUMMER_12_SIFFROR)
            || fieldId.equals(FIELD_KOMMUN_OM_OKAND_DODSPLATS) ? 8.0 : 6.0;
        fieldCoordinates.set(3, new PdfNumber(fieldCoordinates.getAsNumber(3).doubleValue() - adjustment));
        fillText(fieldId, value);
    }

    @SuppressWarnings("CheckStyle")
    private void processDateField(String fieldId, InternalDate date) {
        PdfArray fieldCoordinates = getFieldCoordinates(fieldId);
        double adjustment = fieldId.equals(FIELD_AR_MAN_DAG_DODSDATUM) ? 3.0 : 5.0;
        fieldCoordinates.set(3, new PdfNumber(fieldCoordinates.getAsNumber(3).doubleValue() - adjustment));
        fillText(fieldId, date);
    }
}
