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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import se.inera.intyg.common.fkparent.pdf.model.FkPage;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.pdf.model.PdfComponent;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

import static se.inera.intyg.common.fkparent.pdf.PdfConstants.MINIMAL_ELECTRONIC_COPY_WATERMARK_TEXT;

/**
 * To be used to print the employee copy of LISJP.
 */
public class EmployeeLisjpPdfDefinitionBuilder extends AbstractLisjpPdfDefinitionBuilder {

    static final String CUSTOMIZED_ELECTRONIC_COPY_WATERMARK_TEXT =
            "Detta är en anpassad utskrift av ett elektroniskt intyg. Viss information i intyget har valts bort. "
            + "Intyget har signerats elektroniskt av intygsutfärdaren.";

    private List<String> optionalFields;

    public EmployeeLisjpPdfDefinitionBuilder(List<String> optionalFields) {
        this.optionalFields = optionalFields;
    }

    @Override
    void fillIntyg(FkPdfDefinition pdfDefinition, LisjpUtlatandeV1 intyg, boolean isUtkast, boolean isLockedUtkast,
                   List<Status> statuses, ApplicationOrigin applicationOrigin) throws IOException, DocumentException {
        pdfDefinition.addChild(createPage1(intyg, applicationOrigin));
        pdfDefinition.addChild(createPage2(intyg));
        pdfDefinition.addChild(createPage3(intyg));
        pdfDefinition.addChild(createPage4(intyg));
        // Only add tillaggsfragor page if there are some (and at least one is selected in the optionalFields)
        if (intyg.getTillaggsfragor() != null && intyg.getTillaggsfragor().size() > 0) {
            final FkPage tillaggsfragorPage = tillaggsfragorPage(intyg, optionalFields);
            if (tillaggsfragorPage != null) {
                pdfDefinition.addChild(tillaggsfragorPage);
            }
        }
    }

    public boolean hasDeselectedOptionalFields() {
        return this.optionalFields != null && this.optionalFields.stream().anyMatch(f -> f.startsWith("!"));
    }

    private void printCopyText(List<PdfComponent<?>> allElements, LisjpUtlatandeV1 intyg, ApplicationOrigin applicationOrigin) {
        if (applicationOrigin.equals(ApplicationOrigin.MINA_INTYG)) {
            if (!hasDeselectedOptionalFields()) {
                printElectronicCopy(allElements);
            } else {
                printMinimalElectronicCopy(allElements, CUSTOMIZED_ELECTRONIC_COPY_WATERMARK_TEXT);
            }
        } else {
            printMinimalElectronicCopy(allElements, MINIMAL_ELECTRONIC_COPY_WATERMARK_TEXT);
        }
    }

    private FkPage createPage1(LisjpUtlatandeV1 intyg, ApplicationOrigin applicationOrigin)
            throws IOException, DocumentException {
        List<PdfComponent<?>> allElements = new ArrayList<>();


        printCopyText(allElements, intyg, applicationOrigin);
        addPage1MiscFields(intyg, false, allElements);

        allElements.add(fraga1(intyg, optionalFields));
        allElements.add(fraga2(intyg));
        allElements.add(fraga3(intyg, optionalFields));
        allElements.add(fraga4(intyg, optionalFields));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage2(LisjpUtlatandeV1 intyg) throws IOException, DocumentException {
        List<PdfComponent<?>> allElements = new ArrayList<>();

        allElements.add(fraga5(intyg, optionalFields));
        allElements.add(fraga6(intyg, optionalFields));
        allElements.add(fraga7(intyg, optionalFields));
        allElements.add(fraga8p1(intyg));
        allElements.add(fraga8p2(intyg, optionalFields));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage3(LisjpUtlatandeV1 intyg) throws IOException, DocumentException {
        List<PdfComponent<?>> allElements = new ArrayList<>();

        allElements.add(fraga8p3(intyg));
        allElements.add(fraga8p4(intyg, optionalFields));
        allElements.add(fraga9(intyg));
        allElements.add(fraga10(intyg));
        allElements.add(fraga11(intyg, optionalFields));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage4(LisjpUtlatandeV1 intyg) throws IOException, DocumentException {
        List<PdfComponent<?>> allElements = new ArrayList<>();

        allElements.add(fraga12(intyg, optionalFields));
        allElements.add(fraga13(intyg));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }
}
