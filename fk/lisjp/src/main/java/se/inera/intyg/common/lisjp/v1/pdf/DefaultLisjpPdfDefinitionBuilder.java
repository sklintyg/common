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
package se.inera.intyg.common.lisjp.v1.pdf;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.fkparent.pdf.model.FkPage;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.pdf.model.PdfComponent;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

/**
 * This is the default implementation of the print of LISJP.
 *
 * Contains all information.
 */
public class DefaultLisjpPdfDefinitionBuilder extends AbstractLisjpPdfDefinitionBuilder {

    @Override
    void fillIntyg(FkPdfDefinition pdfDefinition, LisjpUtlatandeV1 intyg, boolean isUtkast, boolean isLockedUtkast,
        List<Status> statuses, ApplicationOrigin applicationOrigin) throws IOException, DocumentException {

        pdfDefinition.addChild(createPage1(intyg, isUtkast, isLockedUtkast, statuses, applicationOrigin));
        pdfDefinition.addChild(createPage2(intyg));
        pdfDefinition.addChild(createPage3(intyg));
        pdfDefinition.addChild(createPage4(intyg));
        // Only add tillaggsfragor page if there are some
        if (intyg.getTillaggsfragor() != null && intyg.getTillaggsfragor().size() > 0) {
            final FkPage tillaggsfragorPage = tillaggsfragorPage(intyg);
            if (tillaggsfragorPage != null) {
                pdfDefinition.addChild(tillaggsfragorPage);
            }
        }
    }

    private FkPage createPage1(LisjpUtlatandeV1 intyg, boolean isUtkast, boolean isLockedUtkast, List<Status> statuses,
        ApplicationOrigin applicationOrigin)
        throws IOException, DocumentException {
        List<PdfComponent<?>> allElements = new ArrayList<>();

        boolean showFkAddress;
        if (applicationOrigin.equals(ApplicationOrigin.MINA_INTYG)) {
            // we never include FK address in MI prints..
            showFkAddress = false;
        } else {
            showFkAddress = !isSentToFk(statuses);
        }

        if (isSigned(isUtkast, isLockedUtkast)) {
            printElectronicCopyTitle(allElements);
        }

        addPage1MiscFields(intyg, showFkAddress, allElements);

        allElements.add(fraga1(intyg));
        allElements.add(fraga2(intyg));
        allElements.add(fraga3(intyg));
        allElements.add(fraga4(intyg));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private boolean isSigned(boolean isUtkast, boolean isLockedUtkast) {
        return !isUtkast && !isLockedUtkast;
    }

    private FkPage createPage2(LisjpUtlatandeV1 intyg) throws IOException, DocumentException {
        List<PdfComponent<?>> allElements = new ArrayList<>();

        allElements.add(fraga5(intyg));
        allElements.add(fraga6(intyg));
        allElements.add(fraga7(intyg));
        allElements.add(fraga8p1(intyg));
        allElements.add(fraga8p2(intyg));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage3(LisjpUtlatandeV1 intyg) throws IOException, DocumentException {
        List<PdfComponent<?>> allElements = new ArrayList<>();

        allElements.add(fraga8p3(intyg));
        allElements.add(fraga8p4(intyg));
        allElements.add(fraga9(intyg));
        allElements.add(fraga10(intyg));
        allElements.add(fraga11(intyg));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage4(LisjpUtlatandeV1 intyg) throws IOException, DocumentException {
        List<PdfComponent<?>> allElements = new ArrayList<>();

        allElements.add(fraga12(intyg));
        allElements.add(fraga13(intyg));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }
}
