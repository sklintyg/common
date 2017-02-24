package se.inera.intyg.common.lisjp.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;

import se.inera.intyg.common.fkparent.pdf.model.FkPage;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.pdf.model.PdfComponent;
import se.inera.intyg.common.lisjp.model.internal.LisjpUtlatande;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

/**
 * To be used to print the employee copy of LISJP.
 */
public class EmployeeLisjpPdfDefinitionBuilder extends AbstractLisjpPdfDefinitionBuilder {

    private List<String> optionalFields;

    public EmployeeLisjpPdfDefinitionBuilder(List<String> optionalFields) {
        this.optionalFields = optionalFields;
    }

    @Override
    void fillIntyg(FkPdfDefinition pdfDefinition, LisjpUtlatande intyg, boolean isUtkast, List<Status> statuses,
            ApplicationOrigin applicationOrigin) throws IOException, DocumentException {
        pdfDefinition.addChild(createPage1(intyg));
        pdfDefinition.addChild(createPage2(intyg));
        pdfDefinition.addChild(createPage3(intyg));
        pdfDefinition.addChild(createPage4(intyg));
    }

    private FkPage createPage1(LisjpUtlatande intyg)
            throws IOException, DocumentException {
        List<PdfComponent> allElements = new ArrayList<>();

        printMinimalElectronicCopy(allElements);
        addPage1MiscFields(intyg, false, allElements);

        allElements.add(fraga1(intyg, optionalFields));
        allElements.add(fraga2(intyg, optionalFields));
        allElements.add(fraga3(intyg, optionalFields));
        allElements.add(fraga4(intyg, optionalFields));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage2(LisjpUtlatande intyg) throws IOException, DocumentException {
        List<PdfComponent> allElements = new ArrayList<>();

        allElements.add(fraga5(intyg, optionalFields));
        allElements.add(fraga6(intyg, optionalFields));
        allElements.add(fraga7(intyg, optionalFields));
        allElements.add(fraga8p1(intyg));
        allElements.add(fraga8p2(intyg, optionalFields));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage3(LisjpUtlatande intyg) throws IOException, DocumentException {
        List<PdfComponent> allElements = new ArrayList<>();

        allElements.add(fraga8p3(intyg));
        allElements.add(fraga8p4(intyg, optionalFields));
        allElements.add(fraga9(intyg));
        allElements.add(fraga10(intyg));
        allElements.add(fraga11(intyg, optionalFields));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage4(LisjpUtlatande intyg) throws IOException, DocumentException {
        List<PdfComponent> allElements = new ArrayList<>();

        allElements.add(fraga12(intyg, optionalFields));
        allElements.add(fraga13(intyg));

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }
}
