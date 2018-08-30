/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.db.pdf;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import se.inera.intyg.common.db.model.internal.DbUtlatande;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.sos_parent.pdf.AbstractSoSPdfGenerator;
import se.inera.intyg.common.sos_parent.pdf.SoSPdfGeneratorException;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by marced on 2017-10-11.
 */
public class DbPdfGenerator extends AbstractSoSPdfGenerator {

    public static final String DEFAULT_PDF_TEMPLATE = "pdf/Blankett-Dodsbevis-2016-53.pdf";

    /*
     * Acrofield name constants (extracted from pdf template file using util class
     * se.inera.dbUtlatande.common.db.pdf.ListAcrofields)
     */

    // Type TEXT
    private static final String FIELD_EFTERNAMN = "Efternamn";

    // Type TEXT
    private static final String FIELD_TELEFON_INKL_RIKTNUMMER = "Telefon inkl riktnummer";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_ERSATTER_TIDIGARE_UTFARDAT_DODSBEVIS = "Ersätter tidigare utfärdat dödsbevis";

    // Type TEXT
    private static final String FIELD_BOSTADSADRESS = "Bostadsadress";

    // Type TEXT
    private static final String FIELD_TJANSTESTALLE = "Tjänsteställe";

    // Type TEXT
    private static final String FIELD_POSTNUMMER_2 = "Postnummer_2";

    // Type RADIOBUTTON - values [säkert,ej säkert]
    private static final String FIELD_GROUP2 = "Group2";

    // Type TEXT
    private static final String FIELD_POSTORT_2 = "Postort_2";

    // Type RADIOBUTTON - values [implantat Ja,Inplantat nej]
    private static final String FIELD_GROUP5 = "Group5";

    // Type RADIOBUTTON - values [avlägsnats nej,avlägsnats]
    private static final String FIELD_GROUP6 = "Group6";

    // Type TEXT
    private static final String FIELD_POSTORT = "Postort";

    // Type RADIOBUTTON - values [Sjukhus,Särskilt boende,Ordinärt boende,Annan / Okänd]
    private static final String FIELD_GROUP3 = "Group3";

    // Type RADIOBUTTON - values [dödfött,avlidet inom 28]
    private static final String FIELD_GROUP4 = "Group4";

    // Type RADIOBUTTON - values [Yttre undersök ja,Yttre undersök nej]
    private static final String FIELD_GROUP7 = "Group7";

    // Type RADIOBUTTON - values [Polisanm,Polisanm nej]
    private static final String FIELD_GROUP8 = "Group8";

    // Type TEXT
    private static final String FIELD_YTTRE_UNDERSOKNING_AR_MAN_DAG = "yttre undersökning år mån dag";

    // Type TEXT
    private static final String FIELD_FORNAMN = "Förnamn";

    // Type TEXT
    private static final String FIELD_IDENTITETEN_STYRKT_GENOM = "Identiteten styrkt genom";

    // Type TEXT
    private static final String FIELD_AR_MAN_DAG = "År mån dag";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_NEJ_SKA_GORAS = "Nej ska göras";

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
    private static final String FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD = "Om dödsdatum ej säkert År mån dag anträffad död";

    private DbUtlatande dbUtlatande;

    public DbPdfGenerator(DbUtlatande intyg, IntygTexts intygTexts, List<Status> statuses, UtkastStatus utkastStatus)
            throws SoSPdfGeneratorException {
        this(intyg, intygTexts, statuses, utkastStatus, true);
    }

    // Only called directly by tests.
    DbPdfGenerator(DbUtlatande utlatande, IntygTexts intygTexts, List<Status> statuses, UtkastStatus utkastStatus, boolean flatten)
            throws SoSPdfGeneratorException {
        try {
            this.dbUtlatande = utlatande;
            outputStream = new ByteArrayOutputStream();
            PdfReader pdfReader = getPdfReader(utlatande, intygTexts, DEFAULT_PDF_TEMPLATE);

            // If not removing Adobe Reader Usage rights signature, you get a warning when opening i Adobe Reader.
            pdfReader.removeUsageRights();
            PdfStamper pdfStamper = new PdfStamper(pdfReader, this.outputStream);
            fields = pdfStamper.getAcroFields();
            boolean isUtkast = UtkastStatus.DRAFT_COMPLETE == utkastStatus || UtkastStatus.DRAFT_INCOMPLETE == utkastStatus;
            boolean isLocked = UtkastStatus.DRAFT_LOCKED == utkastStatus;

            fillAcroformFields();

            if (!isUtkast) {
                // Only signed dbUtlatande prints should have this text
                markAsElectronicCopy(pdfStamper);
            }
            if (!isUtkast && !isLocked) {
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
        fillUnderskrift(dbUtlatande.getGrundData().getSigneringsdatum(), dbUtlatande.getGrundData().getSkapadAv());

    }

    private void fillRelation() {
        if (ersatterTidigareIntyg(dbUtlatande.getGrundData().getRelation())) {
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
        checkRadioField(FIELD_GROUP8, getRadioValueFromBoolean(dbUtlatande.getPolisanmalan(), "", "Polisanm nej", "Polisanm"));

    }

    private void fillYttreUndersokning() {

        if (dbUtlatande.getUndersokningYttre() != null) {
            if (dbUtlatande.getUndersokningYttre() == Undersokning.JA) {
                // Type RADIOBUTTON - values [Yttre undersök ja,Yttre undersök nej,]
                checkRadioField(FIELD_GROUP7, "Yttre undersök ja");
            } else if (dbUtlatande.getUndersokningYttre() == Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN) {
                // Type RADIOBUTTON - values [Yttre undersök ja,Yttre undersök nej,]
                checkRadioField(FIELD_GROUP7, "Yttre undersök nej");
            } else if (dbUtlatande.getUndersokningYttre() == Undersokning.UNDERSOKNING_SKA_GORAS) {
                // Type CHECKBOX - values [Ja,]
                checkCheckboxField(FIELD_NEJ_SKA_GORAS, "Ja");
            }
        }

        fillText(FIELD_YTTRE_UNDERSOKNING_AR_MAN_DAG, dbUtlatande.getUndersokningDatum());

    }

    private void fillExplosivaImplantat() {
        // Type RADIOBUTTON - values [implantat Ja,Inplantat nej,]
        checkRadioField(FIELD_GROUP5, getRadioValueFromBoolean(dbUtlatande.getExplosivImplantat(), "", "implantat Ja", "Inplantat nej"));

        // Type RADIOBUTTON - values [avlägsnats nej,avlägsnats,]
        checkRadioField(FIELD_GROUP6, getRadioValueFromBoolean(dbUtlatande.getExplosivAvlagsnat(), "", "avlägsnats", "avlägsnats nej"));
    }

    private void fillBarnAvlidet() {
        // Type RADIOBUTTON - values [dödfött,avlidet inom 28,]
        // NOTE: kan aldrig vara dödfött när utfärdat i WC
        checkRadioField(FIELD_GROUP4, Boolean.TRUE.equals(dbUtlatande.getBarn()) ? "avlidet inom 28" : "");
    }

    private void fillDodsDatum() {
        // Dödsdatum Säkert / ej säkert
        checkRadioField(FIELD_GROUP2, getRadioValueFromBoolean(dbUtlatande.getDodsdatumSakert(), "", "säkert", "ej säkert"));

        fillText(FIELD_AR_MAN_DAG, dbUtlatande.getDodsdatum());
        fillText(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, dbUtlatande.getAntraffatDodDatum());
    }

    private void fillDodsPlats() {

        // Type RADIOBUTTON - values [Sjukhus,Särskilt boende,Ordinärt boende,Annan / Okänd,]

        checkRadioField(FIELD_GROUP3, modelToPdf(dbUtlatande.getDodsplatsBoende()));

        fillText(FIELD_KOMMUN_OM_OKAND_DODSPLATS, dbUtlatande.getDodsplatsKommun());
        fillText(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, dbUtlatande.getAntraffatDodDatum());
    }

    protected void fillPatientDetails() {
        fillText(FIELD_PERSONNUMMERSAMORDNINGSNUMMER_12_SIFFROR, dbUtlatande.getGrundData().getPatient().getPersonId().getPersonnummer());
        fillText(FIELD_EFTERNAMN, dbUtlatande.getGrundData().getPatient().getEfternamn());
        fillText(FIELD_FORNAMN, dbUtlatande.getGrundData().getPatient().getFornamn());
        fillText(FIELD_BOSTADSADRESS, dbUtlatande.getGrundData().getPatient().getPostadress());
        fillText(FIELD_POSTNUMMER, dbUtlatande.getGrundData().getPatient().getPostnummer());
        fillText(FIELD_POSTORT, dbUtlatande.getGrundData().getPatient().getPostort());

        fillText(FIELD_IDENTITETEN_STYRKT_GENOM, dbUtlatande.getIdentitetStyrkt());
    }

    /**
     * Converts a {@link DodsplatsBoende} to the corresponding pdf template field value.
     *
     * @param dodsplatsBoende
     * @return
     */
    private String modelToPdf(DodsplatsBoende dodsplatsBoende) {
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
    }

}
