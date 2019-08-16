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
package se.inera.intyg.common.doi.v1.pdf;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
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

    public static final String DEFAULT_PDF_TEMPLATE = "pdf/blankett-dodsorsaksintyg-sosfs201419.pdf";

    /*
     * Acrofield names and values (extracted from pdf template file using util class
     * se.inera.intyg.common.doi.pdf.ListAcrofields)
     *
     * Maybe in the future, the issuer of the template (Socialstyrelsen) will provide a more programmer-friendly
     * version where values/names are more logical. But for now - this is what we have to work with.
     */
    // Type TEXT
    private static final String FIELD_EFTERNAMN = "Efternamn";

    // Type TEXT
    private static final String FIELD_BOSTADSADRESS = "Bostadsadress";

    // Type TEXT
    private static final String FIELD_TJANSTESTALLE = "Tjänsteställe";

    // Type TEXT
    private static final String FIELD_POSTORT_2 = "Postort_2";

    // Type RADIOBUTTON - values [dödfött,avlidet inom 28]
    private static final String FIELD_BARN_SOM_AVLIDIT = "Barn som avlidit";

    // Type TEXT
    private static final String FIELD_LAND_OM_EJ_STADIGVARANDE_BOSATT_I_SVERIGE = "Land om ej stadigvarande bosatt i Sverige";

    // Type TEXT
    private static final String FIELD_POSTNUMMER = "Postnummer";

    // Type TEXT
    private static final String FIELD_UTDELNINGSADRESS = "Utdelningsadress";

    // Type TEXT
    private static final String FIELD_ORT_OCH_DATUM = "Ort och datum";

    // Type TEXT
    private static final String FIELD_OPERATIONSDATUM_AR_MAN_DAG = "Operationsdatum år mån dag";

    // Type TEXT
    private static final String FIELD_TILLSTAND_SOM_FORANLEDDE_INGREPPET = "Tillstånd som föranledde ingreppet";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_RATTSMEDICINSK_OBDUKTION = "Rättsmedicinsk obduktion";

    // Type TEXT
    private static final String FIELD_EPOST = "Epost";

    // Type RADIOBUTTON - values [säkert,ej säkert]
    private static final String FIELD_DODSDATUM_SAKERT_EJ_SAKERT = "Dödsdatum säkert/ej säkert";

    // Type RADIOBUTTON - values [olycksfall,oklart,avsiktligt vållad av annan,självmord]
    private static final String FIELD_SKADA_FORGIFTNING = "Skada/förgiftning";

    // Type TEXT
    private static final String FIELD_LAKARENS_EFTERNAMN_OCH_FORNAMN = "Läkarens efternamn och förnamn";

    // Type TEXT
    private static final String FIELD_BEFATTNING = "Befattning";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_RATTSMEDICINSK_LIKBESIKTNING = "Rättsmedicinsk Likbesiktning";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_ERSATTER_TIDIGARE_UTFARDAT_INTYG = "Ersätter tidigare utfärdat intyg";

    // Type RADIOBUTTON - values [annan okänd,särskilt boende,sjukhus,ordinärt boende]
    private static final String FIELD_DODSPLATS = "Dödsplats";

    // Type TEXT
    private static final String FIELD_FORNAMN = "Förnamn";

    // Type TEXT
    private static final String FIELD_IDENTITETEN_STYRKT_GENOM = "Identiteten styrkt genom";

    // Type TEXT
    private static final String FIELD_DODSDATUM = "Dödsdatum";

    // Type RADIOBUTTON - values [Nej,Uppgift saknas,Ja]
    private static final String FIELD_OPERERAD_INOM_FYRA_VECKOR = "Opererad inom fyra veckor";

    // Type TEXT
    private static final String FIELD_KOMMUN_OM_OKAND_DODSPLATS_KOMMUNEN_DAR_KROPPEN_PATRAFFADES = "Kommun om okänd "
        + "dödsplats kommunen där kroppen påträffades";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_YTTRE_UNDERSOKNING_EFTER_DODEN = "Yttre undersökning efter döden";

    // Type TEXT
    private static final String FIELD_TELEFON_INKL_RIKTNUMMER = "Telefon inkl riktnummer";

    // Type TEXT
    private static final String FIELD_DATUM_FOR_SKADAFORGIFTNING_AR_MAN_DAG = "Datum för skadaförgiftning år mån dag";

    // Type TEXT
    private static final String FIELD_POSTNUMMER_2 = "Postnummer_2";

    // Type TEXT
    private static final String FIELD_POSTORT = "Postort";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_KLINISK = "Klinisk";

    // Type TEXT
    private static final String FIELD_PERSONNUMMERSAMORDNINGSNUMMER_12_SIFFROR = "Personnummersamordningsnummer 12 siffror";

    // Type CHECKBOX - values [Ja]
    private static final String FIELD_UNDERSOKNING_FORE_DODEN = "Undersökning före döden";

    // Type TEXT
    private static final String FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD = "Om dödsdatum ej säkert År mån dag anträffad död";

    // Type TEXT
    private static final String FIELD_KORT_BESKRIVNING_AV_HUR_SKADANFORGIFTNINGEN_UPPKOM = "Kort beskrivning av hur skadanförgiftningen"
        + " uppkom utan att röja eventuellt andra inblandades identiteter";

    // Dynamic fieldnames (A, B C etc)
    private static final String FIELD_DODSORSAK_FIRSTROW_BESKRIVNING = "Den terminala dödsorsaken %s";
    private static final String FIELD_DODSORSAK_ROW_BESKRIVNING = "Den terminala dödsorsaken var %s";
    private static final String FIELD_DODSORSAK_ROW_UNGEFARLIG_DEBUT = "Ungefärlig debut %s";
    private static final String FIELD_DODSORSAK_ROW_AKUT = "%s Akut";
    private static final String FIELD_DODSORSAK_ROW_KRONISK = "%s Kronisk";
    private static final String FIELD_DODSORSAK_ROW_INGEN_UPPGIFT = "%s Uppgift saknas";

    private static final String FIELD_BIDRAGANDE_DODSORSAK_ROW_BESKRIVNING = "Andra sjukdomar eller skador %d";
    private static final String FIELD_BIDRAGANDE_DODSORSAK_ROW_UNGEFARLIG_DEBUT = "Ungefärlig debut %d";
    private DoiUtlatandeV1 doiUtlatandeV1;

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

    private void fillAcroformFields() {
        fillRelation();
        fillPatientDetails();
        fillDodsDatum();
        fillDodsPlats();
        fillBarnAvlidet();
        fillOrsak();
        fillBidragandeOrsaker();
        fillOperation();
        fillSkadaForgiftning();
        fillDodsorsaksGrunder();

        fillUnderskrift(doiUtlatandeV1.getGrundData().getSigneringsdatum(), doiUtlatandeV1.getGrundData().getSkapadAv());

    }

    private void fillDodsorsaksGrunder() {
        checkCheckboxField(FIELD_UNDERSOKNING_FORE_DODEN, hasDodsorsaksGrund(Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN) ? "Ja" : "");
        checkCheckboxField(FIELD_YTTRE_UNDERSOKNING_EFTER_DODEN, hasDodsorsaksGrund(Dodsorsaksgrund.UNDERSOKNING_EFTER_DODEN) ? "Ja" : "");
        checkCheckboxField(FIELD_KLINISK, hasDodsorsaksGrund(Dodsorsaksgrund.KLINISK_OBDUKTION) ? "Ja" : "");
        checkCheckboxField(FIELD_RATTSMEDICINSK_OBDUKTION, hasDodsorsaksGrund(Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION) ? "Ja" : "");
        checkCheckboxField(FIELD_RATTSMEDICINSK_LIKBESIKTNING, hasDodsorsaksGrund(Dodsorsaksgrund.RATTSMEDICINSK_BESIKTNING) ? "Ja" : "");

    }

    private boolean hasDodsorsaksGrund(Dodsorsaksgrund dodsorsaksgrund) {
        if (doiUtlatandeV1.getGrunder() != null) {
            return doiUtlatandeV1.getGrunder().contains(dodsorsaksgrund);
        }
        return false;
    }

    private void fillSkadaForgiftning() {
        // Type RADIOBUTTON - values [olycksfall,oklart,avsiktligt vållad av annan,självmord]
        checkRadioField(FIELD_SKADA_FORGIFTNING, modelToPdf(doiUtlatandeV1.getForgiftningOrsak()));
        fillText(FIELD_DATUM_FOR_SKADAFORGIFTNING_AR_MAN_DAG, doiUtlatandeV1.getForgiftningDatum());
        fillText(FIELD_KORT_BESKRIVNING_AV_HUR_SKADANFORGIFTNINGEN_UPPKOM,
            doiUtlatandeV1.getForgiftningUppkommelse());
    }

    private void fillOperation() {
        // Type RADIOBUTTON - values [Nej,Uppgift saknas,Ja]
        checkRadioField(FIELD_OPERERAD_INOM_FYRA_VECKOR, modelToPdf(doiUtlatandeV1.getOperation()));
        fillText(FIELD_OPERATIONSDATUM_AR_MAN_DAG, doiUtlatandeV1.getOperationDatum());
        fillText(FIELD_TILLSTAND_SOM_FORANLEDDE_INGREPPET, doiUtlatandeV1.getOperationAnledning());
    }

    private void fillBidragandeOrsaker() {
        // Andra bidragande orsaker (1..8)
        if (doiUtlatandeV1.getBidragandeSjukdomar() != null) {
            for (int i = 0; i < doiUtlatandeV1.getBidragandeSjukdomar().size(); i++) {
                fillBidragandeOrsaksRow(i + 1, doiUtlatandeV1.getBidragandeSjukdomar().get(i));
            }
        }

    }

    private void fillBidragandeOrsaksRow(int index, Dodsorsak orsak) {
        String fieldBeskrivning;
        String fieldDebut;
        String fieldAkut;
        String fieldKronisk;
        String fieldUppgiftSaknas;

        fieldBeskrivning = String.format(FIELD_BIDRAGANDE_DODSORSAK_ROW_BESKRIVNING, index);
        fieldDebut = String.format(FIELD_BIDRAGANDE_DODSORSAK_ROW_UNGEFARLIG_DEBUT, index);
        fieldAkut = String.format(FIELD_DODSORSAK_ROW_AKUT, addChar('D', index));
        fieldKronisk = String.format(FIELD_DODSORSAK_ROW_KRONISK, addChar('D', index));
        fieldUppgiftSaknas = String.format(FIELD_DODSORSAK_ROW_INGEN_UPPGIFT, addChar('D', index));

        setOrsakFields(orsak, fieldBeskrivning, fieldDebut, fieldAkut, fieldKronisk, fieldUppgiftSaknas);
    }

    private void fillOrsak() {
        // Terminal dödsorsak
        fillDodsOrsaksRow(0, doiUtlatandeV1.getTerminalDodsorsak());

        // Följder (B,C,D)
        if (doiUtlatandeV1.getFoljd() != null) {
            for (int i = 0; i < doiUtlatandeV1.getFoljd().size(); i++) {
                fillDodsOrsaksRow(i + 1, doiUtlatandeV1.getFoljd().get(i));
            }
        }
    }

    private void fillDodsOrsaksRow(int index, Dodsorsak orsak) {

        String fieldBeskrivning;
        String fieldDebut;
        String fieldAkut;
        String fieldKronisk;
        String fieldUppgiftSaknas;

        if (index == 0) {
            // A (Terminal orsak)
            fieldBeskrivning = String.format(FIELD_DODSORSAK_FIRSTROW_BESKRIVNING, "A");
        } else {
            // A (Terminal orsak)
            fieldBeskrivning = String.format(FIELD_DODSORSAK_ROW_BESKRIVNING, addChar('A', index));
        }

        fieldDebut = String.format(FIELD_DODSORSAK_ROW_UNGEFARLIG_DEBUT, addChar('A', index));
        fieldAkut = String.format(FIELD_DODSORSAK_ROW_AKUT, addChar('A', index));
        fieldKronisk = String.format(FIELD_DODSORSAK_ROW_KRONISK, addChar('A', index));
        fieldUppgiftSaknas = String.format(FIELD_DODSORSAK_ROW_INGEN_UPPGIFT, addChar('A', index));

        setOrsakFields(orsak, fieldBeskrivning, fieldDebut, fieldAkut, fieldKronisk, fieldUppgiftSaknas);
    }

    private void setOrsakFields(Dodsorsak orsak, String fieldBeskrivning, String fieldDebut, String fieldAkut, String fieldKronisk,
        String fieldUppgiftSaknas) {
        if (orsak != null) {
            fillText(fieldBeskrivning, orsak.getBeskrivning());
            fillText(fieldDebut, orsak.getDatum());
            checkCheckboxField(fieldAkut, Specifikation.PLOTSLIG == orsak.getSpecifikation() ? "Ja" : "");
            checkCheckboxField(fieldKronisk, Specifikation.KRONISK == orsak.getSpecifikation() ? "Ja" : "");
            checkCheckboxField(fieldUppgiftSaknas, Specifikation.UPPGIFT_SAKNAS == orsak.getSpecifikation() ? "Ja" : "");
        }
    }

    private String addChar(char a, int i) {
        return Character.toString((char) (a + i));
    }

    protected void fillPatientDetails() {
        fillText(FIELD_PERSONNUMMERSAMORDNINGSNUMMER_12_SIFFROR,
            doiUtlatandeV1.getGrundData().getPatient().getPersonId().getPersonnummerWithDash());
        fillText(FIELD_EFTERNAMN, doiUtlatandeV1.getGrundData().getPatient().getEfternamn());
        fillText(FIELD_FORNAMN, doiUtlatandeV1.getGrundData().getPatient().getFornamn());
        fillText(FIELD_BOSTADSADRESS, doiUtlatandeV1.getGrundData().getPatient().getPostadress());
        fillText(FIELD_POSTNUMMER, doiUtlatandeV1.getGrundData().getPatient().getPostnummer());
        fillText(FIELD_POSTORT, doiUtlatandeV1.getGrundData().getPatient().getPostort());

        fillText(FIELD_IDENTITETEN_STYRKT_GENOM, doiUtlatandeV1.getIdentitetStyrkt());
        fillText(FIELD_LAND_OM_EJ_STADIGVARANDE_BOSATT_I_SVERIGE, doiUtlatandeV1.getLand());
    }

    private void fillRelation() {
        if (ersatterTidigareIntyg(doiUtlatandeV1.getGrundData().getRelation())) {
            // Type CHECKBOX - values [Ja]
            checkCheckboxField(FIELD_ERSATTER_TIDIGARE_UTFARDAT_INTYG, "Ja");
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

    private void fillBarnAvlidet() {
        // Type RADIOBUTTON - values [dödfött,avlidet inom 28]
        // NOTE: kan aldrig vara dödfött när utfärdat i WC
        checkRadioField(FIELD_BARN_SOM_AVLIDIT, Boolean.TRUE.equals(doiUtlatandeV1.getBarn()) ? "avlidet inom 28" : "");
    }

    private void fillDodsDatum() {
        // Type RADIOBUTTON - values [säkert,ej säkert]
        checkRadioField(FIELD_DODSDATUM_SAKERT_EJ_SAKERT, getRadioValueFromBoolean(doiUtlatandeV1.getDodsdatumSakert(), "", "säkert",
            "ej säkert"));
        // Type TEXT
        fillText(FIELD_DODSDATUM, doiUtlatandeV1.getDodsdatum());

        // Type TEXT
        fillText(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, doiUtlatandeV1.getAntraffatDodDatum());
    }

    private void fillDodsPlats() {

        // Type RADIOBUTTON - values [Sjukhus,Särskilt boende,Ordinärt boende,Annan / Okänd,]

        checkRadioField(FIELD_DODSPLATS, modelToPdf(doiUtlatandeV1.getDodsplatsBoende()));

        fillText(FIELD_KOMMUN_OM_OKAND_DODSPLATS_KOMMUNEN_DAR_KROPPEN_PATRAFFADES, doiUtlatandeV1.getDodsplatsKommun());
        fillText(FIELD_OM_DODSDATUM_EJ_SAKERT_AR_MAN_DAG_ANTRAFFAD_DOD, doiUtlatandeV1.getAntraffatDodDatum());
    }

    /**
     * Converts a {@link DodsplatsBoende} to the corresponding pdf template field value.
     *
     * @return pdf value
     */
    private String modelToPdf(DodsplatsBoende dodsplatsBoende) {
        if (dodsplatsBoende != null) {
            // Pdf equivalent values are
            // Type RADIOBUTTON - values [annan okänd,särskilt boende,sjukhus,ordinärt boende]
            switch (dodsplatsBoende) {
                case ANNAN:
                    return "annan okänd";
                case SJUKHUS:
                    return "sjukhus";
                case ORDINART_BOENDE:
                    return "ordinärt boende";
                case SARSKILT_BOENDE:
                    return "särskilt boende";
            }
        }
        return "";
    }

    /**
     * Converts a {@link ForgiftningOrsak} to the corresponding pdf template field value.
     *
     * @return pdf value
     */
    private String modelToPdf(ForgiftningOrsak forgiftningOrsak) {
        if (forgiftningOrsak != null) {

            // Pdf equivalent values are [olycksfall,oklart,avsiktligt vållad av annan,självmord]
            switch (forgiftningOrsak) {
                case OLYCKSFALL:
                    return "olycksfall";
                case SJALVMORD:
                    return "självmord";
                case AVSIKTLIGT_VALLAD:
                    return "avsiktligt vållad av annan";
                case OKLART:
                    return "oklart";
            }
        }
        return "";
    }

    /**
     * Converts a {@link OmOperation} to the corresponding pdf template field value.
     *
     * @return pdf value
     */
    private String modelToPdf(OmOperation operation) {

        if (operation != null) {
            // Pdf equivalent values are [Nej,Uppgift saknas,Ja]
            switch (operation) {
                case JA:
                    return "Ja";
                case NEJ:
                    return "Nej";
                case UPPGIFT_SAKNAS:
                    return "Uppgift saknas";
            }
        }
        return "";
    }
}
