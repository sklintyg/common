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

    // Type RADIOBUTTON - values [säkert,ej säkert]
    //private static final String FIELD_GROUP2 = "Group2";
    private static final String FIELD_SAKERT = "Säkert";
    private static final String FIELD_EJ_SAKERT = "Ej säkert";

    // Type TEXT
    private static final String FIELD_POSTORT_2 = "Postort_2";

    // Type RADIOBUTTON - values [implantat Ja,Inplantat nej]
    //private static final String FIELD_GROUP5 = "Group5";
    private static final String FIELD_JA_IMPLANTAT = "Ja";
    private static final String FIELD_NEJ_IMPLANTAT = "Nej";

    // Type RADIOBUTTON - values [avlägsnats nej,avlägsnats]
    //private static final String FIELD_GROUP6 = "Group6";
    private static final String FIELD_JA_IMPLANTAT_AVLAGSNAT = "Ja implantat";
    private static final String FIELD_NEJ_IMPLANTAT_AVLAGSNAT = "nej implantat";

    // Type TEXT
    private static final String FIELD_POSTORT = "Postort";

    // Type RADIOBUTTON - values [Sjukhus,Särskilt boende,Ordinärt boende,Annan / Okänd]
    //private static final String FIELD_GROUP3 = "Group3";
    private static final String FIELD_SJUKHUS = "Sjukhus";
    private static final String FIELD_ORDINART_BOENDE = "Ordinärt boende";
    private static final String FIELD_SARSKILT_BOENDE = "Särskild boende";
    private static final String FIELD_ANNAN_OKANT = "Annan/okänd";

    // Type RADIOBUTTON - values [dödfött,avlidet inom 28]
    //private static final String FIELD_GROUP4 = "Group4";
    //private static final String FIELD_DODFOTT = "Dödfött";
    private static final String FIELD_AVLIDET_INOM_28 = "Avlidit";

    // Type RADIOBUTTON - values [Yttre undersök ja,Yttre undersök nej]
    //private static final String FIELD_GROUP7 = "Group7";
    private static final String FIELD_YTTRE_UNDERSOKNING_JA = "Yttre Ja";
    private static final String FIELD_YTTRE_UNDERSOKNING_NEJ = "Yttre nej";

    // Type RADIOBUTTON - values [Polisanm,Polisanm nej]
    //private static final String FIELD_GROUP8 = "Group8";
    private static final String FIELD_POLISANMALAN_JA = "Polisanmälan Ja";
    private static final String FIELD_POLISANMALAN_NEJ = "Polisanmälan_nej";

    // Type TEXT
    private static final String FIELD_YTTRE_UNDERSOKNING_AR_MAN_DAG = "År mån dag Yttre";

    // Type TEXT
    private static final String FIELD_FORNAMN = "Förnamn";

    // Type TEXT
    private static final String FIELD_IDENTITETEN_STYRKT_GENOM = "Identiteten styrkt genom";

    // Type TEXT
    private static final String FIELD_AR_MAN_DAG = "År mån dag dödsdatum";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_NEJ_SKA_GORAS = "Yttre Nej, rättsmedicinsk";

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
        fillDodsDatum();
        fillDodsPlats();
        fillBarnAvlidet();
        fillExplosivaImplantat();
        fillYttreUndersokning();
        fillPolisanmalan();
        fillUnderskrift(dbUtlatandeV1.getGrundData().getSigneringsdatum(), dbUtlatandeV1.getGrundData().getSkapadAv());

    }

    private void fillRelation() {
        if (ersatterTidigareIntyg(dbUtlatandeV1.getGrundData().getRelation())) {
            // Type CHECKBOX - values [Ja]
            checkCheckboxField(FIELD_ERSATTER_TIDIGARE_UTFARDAT_DODSBEVIS, "Ja");
        }
    }

    private void fillUnderskrift(LocalDateTime signeringsDatum, HoSPersonal skapadAv) {
        fillText(FIELD_ORT_OCH_DATUM, signeringsDatum != null ? signeringsDatum.format(DATE_FORMAT) : "");
        fillText(FIELD_LAKARENS_EFTERNAMN_OCH_FORNAMN, skapadAv.getFullstandigtNamn());
        fillText(FIELD_BEFATTNING, String.join(", ", skapadAv.getBefattningar()));
        fillText(FIELD_TJANSTESTALLE, String.join(", ", skapadAv.getVardenhet().getEnhetsnamn()
            + ", " + skapadAv.getVardenhet().getVardgivare().getVardgivarnamn()));
        fillText(FIELD_UTDELNINGSADRESS, skapadAv.getVardenhet().getPostadress());
        fillText(FIELD_POSTNUMMER_2, skapadAv.getVardenhet().getPostnummer());
        fillText(FIELD_POSTORT_2, skapadAv.getVardenhet().getPostort());
        fillText(FIELD_TELEFON_INKL_RIKTNUMMER, skapadAv.getVardenhet().getTelefonnummer());
        fillText(FIELD_EPOST, skapadAv.getVardenhet().getEpost());
    }

    private void fillPolisanmalan() {
        // Type RADIOBUTTON - values [Polisanm,Polisanm nej,]
        // NOTE: checkbox values are for some reason reversed in the pdf, so to check the Ja box, we must use the value
        // "Polisanm nej"!
        Boolean policeReport = dbUtlatandeV1.getPolisanmalan();
        if (policeReport != null) {
            if (policeReport) {
                checkCheckboxField(FIELD_POLISANMALAN_JA, "Ja");
            } else {
                checkCheckboxField(FIELD_POLISANMALAN_NEJ, "Ja");
            }
        }

        //checkCheckboxField(FIELD_POLISANMALAN_JA, getRadioValueFromBoolean(dbUtlatandeV1.getPolisanmalan(), "", "Polisanmälan Ja", ""));
        //checkCheckboxField(FIELD_POLISANMALAN_NEJ, getRadioValueFromBoolean(dbUtlatandeV1.getPolisanmalan(), "", "", "Polisanmälan_nej"));

    }

    private void fillYttreUndersokning() {

        if (dbUtlatandeV1.getUndersokningYttre() != null) {
            if (dbUtlatandeV1.getUndersokningYttre() == Undersokning.JA) {
                // Type RADIOBUTTON - values [Yttre undersök ja,Yttre undersök nej,]
                checkCheckboxField(FIELD_YTTRE_UNDERSOKNING_JA, "Ja");
            } else if (dbUtlatandeV1.getUndersokningYttre() == Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN) {
                // Type RADIOBUTTON - values [Yttre undersök ja,Yttre undersök nej,]
                checkCheckboxField(FIELD_YTTRE_UNDERSOKNING_NEJ, "Ja");
            } else if (dbUtlatandeV1.getUndersokningYttre() == Undersokning.UNDERSOKNING_SKA_GORAS) {
                // Type CHECKBOX - values [Ja,]
                checkCheckboxField(FIELD_NEJ_SKA_GORAS, "Ja");
            }
        }

        fillText(FIELD_YTTRE_UNDERSOKNING_AR_MAN_DAG, dbUtlatandeV1.getUndersokningDatum());
    }

    private void fillExplosivaImplantat() {
        // Type RADIOBUTTON - values [implantat Ja,Inplantat nej,]
        Boolean explosiveImplant = dbUtlatandeV1.getExplosivImplantat();
        if (explosiveImplant != null) {
            if (explosiveImplant) {
                checkCheckboxField(FIELD_JA_IMPLANTAT, "Ja");
            } else {
                checkCheckboxField(FIELD_NEJ_IMPLANTAT, "Ja");
            }
        }

        // Type RADIOBUTTON - values [avlägsnats nej,avlägsnats,]
        Boolean explosiveImplantRemoved = dbUtlatandeV1.getExplosivAvlagsnat();
        if (explosiveImplantRemoved != null) {
            if (explosiveImplantRemoved) {
                checkCheckboxField(FIELD_JA_IMPLANTAT_AVLAGSNAT, "Ja");
            } else {
                checkCheckboxField(FIELD_NEJ_IMPLANTAT_AVLAGSNAT, "Ja");
            }
        }
    }

    private void fillBarnAvlidet() {
        // Type RADIOBUTTON - values [dödfött,avlidet inom 28,]
        // NOTE: kan aldrig vara dödfött när utfärdat i WC
        //checkRadioField(FIELD_GROUP4, Boolean.TRUE.equals(dbUtlatandeV1.getBarn()) ? "avlidet inom 28" : "");
        //checkCheckboxField(FIELD_AVLIDET_INOM_28, Boolean.TRUE.equals(dbUtlatandeV1.getBarn()) ? "Ja" : "");
        Boolean childDeadWithin28Days = dbUtlatandeV1.getBarn())
        if (childDeadWithin28Days != null && childDeadWithin28Days) {
            checkCheckboxField(FIELD_AVLIDET_INOM_28, "Ja");
        }
    }

    private void fillDodsDatum() {
        // Dödsdatum Säkert / ej säkert
        Boolean certainDateOfDeath = dbUtlatandeV1.getDodsdatumSakert();
        if (certainDateOfDeath != null) {
            if (certainDateOfDeath) {
                checkCheckboxField(FIELD_SAKERT, "Ja");
            } else {
                checkCheckboxField(FIELD_EJ_SAKERT, "Ja");
            }
        }
        //checkCheckboxField(FIELD_SAKERT, getRadioValueFromBoolean(dbUtlatandeV1.getDodsdatumSakert(), "", "Ja", ""));
        //checkCheckboxField(FIELD_EJ_SAKERT, getRadioValueFromBoolean(dbUtlatandeV1.getDodsdatumSakert(), "", "", "Nej"));

        fillText(FIELD_AR_MAN_DAG, dbUtlatandeV1.getDodsdatum());
        fillText(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, dbUtlatandeV1.getAntraffatDodDatum());
    }

    private void fillDodsPlats() {
        // Type RADIOBUTTON - values [Sjukhus,Särskilt boende,Ordinärt boende,Annan / Okänd,]
        // Pdf equivalent values are [Sjukhus,Särskilt boende,Ordinärt boende,Annan / Okänd,]
        //checkRadioField(FIELD_GROUP3, modelToPdf(dbUtlatandeV1.getDodsplatsBoende()));
        DodsplatsBoende dodsplatsBoende = dbUtlatandeV1.getDodsplatsBoende();

        if (dodsplatsBoende != null) {

            switch (dodsplatsBoende) {
                case SJUKHUS:
                    checkCheckboxField(FIELD_SJUKHUS, "Ja");
                    break;
                case ORDINART_BOENDE:
                    checkCheckboxField(FIELD_ORDINART_BOENDE, "Ja");
                    break;
                case SARSKILT_BOENDE:
                    checkCheckboxField(FIELD_SARSKILT_BOENDE, "Ja");
                    break;
                case ANNAN:
                    checkCheckboxField(FIELD_ANNAN_OKANT, "Ja");
            }
        }

        fillText(FIELD_KOMMUN_OM_OKAND_DODSPLATS, dbUtlatandeV1.getDodsplatsKommun());
        fillText(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, dbUtlatandeV1.getAntraffatDodDatum());
    }

    protected void fillPatientDetails() {
        fillText(FIELD_PERSONNUMMERSAMORDNINGSNUMMER_12_SIFFROR, dbUtlatandeV1.getGrundData().getPatient().getPersonId().getPersonnummer());
        fillText(FIELD_EFTERNAMN, dbUtlatandeV1.getGrundData().getPatient().getEfternamn());
        fillText(FIELD_FORNAMN, dbUtlatandeV1.getGrundData().getPatient().getFornamn());
        fillText(FIELD_BOSTADSADRESS, dbUtlatandeV1.getGrundData().getPatient().getPostadress());
        fillText(FIELD_POSTNUMMER, dbUtlatandeV1.getGrundData().getPatient().getPostnummer());
        fillText(FIELD_POSTORT, dbUtlatandeV1.getGrundData().getPatient().getPostort());

        fillText(FIELD_IDENTITETEN_STYRKT_GENOM, dbUtlatandeV1.getIdentitetStyrkt());
    }

    /**
     * Converts a {@link DodsplatsBoende} to the corresponding pdf template field value.
     */
   /* private String modelToPdf(DodsplatsBoende dodsplatsBoende) {
        if (dodsplatsBoende != null) {
            // Pdf equivalent values are [Sjukhus,Särskilt boende,Ordinärt boende,Annan / Okänd,]
            switch (dodsplatsBoende) {
                case ANNAN:
                    return "Annan / Okänd";
                case SJUKHUS:
                    return "Sjukhus";
                case ORDINART_BOENDE:
                    return "Ordinärt boende";
                case SARSKILT_BOENDE:
                    return "Särskilt boende";
            }
        }
        return "";
    }*/

}
