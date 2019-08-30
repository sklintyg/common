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
package se.inera.intyg.common.fkparent.pdf;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.pdf.model.FkPage;
import se.inera.intyg.common.fkparent.pdf.model.FkTillaggsFraga;
import se.inera.intyg.common.fkparent.pdf.model.PdfComponent;
import se.inera.intyg.common.fkparent.support.FkAbstractModuleEntryPoint;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

/**
 * Base class with common methods used by SMI type PDF definition construction.
 *
 * Created by marced on 2016-10-25.
 */
public class FkBasePdfDefinitionBuilder {

    protected static final String DATE_PATTERN = "yyyy-MM-dd";
    protected static final String PROPERTY_KEY_FORMID = "formId";
    protected static final String PROPERTY_KEY_FORMID_ROW2 = "formIdRow2";
    protected static final String PROPERTY_KEY_BLANKETT_ID = "blankettId";
    protected static final String PROPERTY_KEY_BLANKETT_VERSION = "blankettVersion";

    protected IntygTexts intygTexts;

    protected String getPropertyValue(String propertyName) {
        return this.intygTexts.getProperties().getProperty(propertyName, "");
    }

    protected String getPrintedByText(ApplicationOrigin applicationOrigin) {
        switch (applicationOrigin) {
            case WEBCERT:
                return "Intyget är utskrivet från Webcert";
            case MINA_INTYG:
                return "Intyget är utskrivet från Mina intyg";
            default:
                throw new IllegalArgumentException("Unknown ApplicationOrigin " + applicationOrigin);
        }
    }

    protected String getText(String key) {
        return getText(key, false);
    }

    protected String getText(String key, boolean allowMissing) {
        String text = intygTexts.getTexter().get(key);
        if (text == null && !allowMissing) {
            // Not finding a text is considered fatal
            throw new IllegalArgumentException(
                intygTexts.getIntygsTyp() + " (version " + intygTexts.getVersion() + ") dynamic text for key '" + key
                    + "' requested for PDF but was not found. Please check text sources / question id's");
        }
        return text;
    }

    protected boolean isSentToFk(List<Status> statuses) {
        return statuses != null && statuses.stream()
            .anyMatch(s -> CertificateState.SENT.equals(s.getType())
                && FkAbstractModuleEntryPoint.DEFAULT_RECIPIENT_ID.equals(s.getTarget()));
    }

    protected boolean isMakulerad(List<Status> statuses) {
        return statuses != null && statuses.stream()
            .anyMatch(s -> CertificateState.CANCELLED.equals(s.getType()));
    }

    protected String nullSafeString(String string) {
        return string != null ? string : "";
    }

    protected String nullSafeString(InternalDate date) {
        return date != null ? date.getDate() : "";
    }

    protected Diagnos safeGetDiagnos(ImmutableList<Diagnos> diagnoser, int index) {
        if (index < diagnoser.size()) {
            return diagnoser.get(index);
        }
        return Diagnos.create("", "", "", "");
    }

    protected boolean safeBoolean(Boolean b) {
        return b != null && b;
    }

    protected String concatStringList(List<String> strings) {
        StringJoiner sj = new StringJoiner(", ");
        for (String s : strings) {
            sj.add(s);
        }
        return sj.toString();
    }

    protected String buildVardEnhetAdress(Vardenhet ve) {
        StringBuilder sb = new StringBuilder();
        sb.append(nullSafeString(ve.getEnhetsnamn())).append("\n")
            .append(nullSafeString(ve.getPostadress())).append("\n")
            .append(nullSafeString(ve.getPostnummer())).append(" ").append(nullSafeString(ve.getPostort())).append("\n");
        if (nullSafeString(ve.getTelefonnummer()).length() > 0) {
            sb.append("Telefon: ").append(nullSafeString(ve.getTelefonnummer()));
        }

        return sb.toString();

    }

    protected FkPage buildTillagsfragorPage(ImmutableList<Tillaggsfraga> tillaggsfragor) {
        // Check if tillaggsfragor exists
        if (tillaggsfragor == null || tillaggsfragor.isEmpty()) {
            return null;
        }

        List<PdfComponent<? extends PdfComponent>> allElements = new ArrayList<>();

        // Sida 5 ar en extrasida, har lagger vi ev tillaggsfragor
        for (int i = 0; i < tillaggsfragor.size(); i++) {
            Tillaggsfraga tillaggsfraga = tillaggsfragor.get(i);

            String text = getText("DFR_" + tillaggsfraga.getId() + ".1.RBK", true);

            if (text != null) {
                allElements
                    .add(new FkTillaggsFraga((i + 1) + ". " + text, tillaggsfraga.getSvar()));
            }
        }

        if (allElements.isEmpty()) {
            return null;
        }

        FkPage thisPage = new FkPage("Tilläggsfrågor");
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    protected void clearSkapadAvForUtkast(GrundData grundData) {

        HoSPersonal skapadAv = grundData.getSkapadAv();

        skapadAv.setFullstandigtNamn("");
        skapadAv.setPersonId("");
        skapadAv.getVardenhet().setArbetsplatsKod("");
        skapadAv.getBefattningar().clear();
        skapadAv.getSpecialiteter().clear();
    }
}
