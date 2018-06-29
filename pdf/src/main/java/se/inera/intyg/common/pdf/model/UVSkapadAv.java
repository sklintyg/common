package se.inera.intyg.common.pdf.model;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

public class UVSkapadAv extends UVComponent {
    public UVSkapadAv(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        String modelProp = (String) currentUvNode.get("modelProp");

        /*
         * Intyget är utfärdat och signerat av:
         * Karin Persson
         * Tel: 054203408
         * nmt_vg1_ve1, nmt_vg1
         * Bryggaregatan 11, 65340 Karlstad
         */
        parent.setMarginTop(0f)
                .setMarginBottom(0f)
                .setPadding(16f)

                .setBold()
                .setFontSize(10f)
                .setFontColor(white)
                .setBackgroundColor(ineraBlue);

        parent.add(new Paragraph("Intyget är utfärdat och signerat av:"));
        Table skapadAv = new Table(new float[] { 12f, 12f });
        skapadAv.setWidth(400f);
        skapadAv.addHeaderCell(
                new Cell().setPadding(0f)
                        .setBorder(Border.NO_BORDER)
                        .add(
                                new Paragraph(renderer.eval(modelProp + ".fullstandigtNamn").toString()).setFontSize(10f)
                                        .setBold()));
        skapadAv.addHeaderCell(
                new Cell().setPadding(0f)
                        .setBorder(Border.NO_BORDER)
                        .add(
                                new Paragraph(renderer.eval(modelProp + ".vardenhet.enhetsnamn") + " "
                                        + renderer.eval(modelProp + ".vardenhet.vardgivare.vardgivarnamn"))
                                        .setFontSize(10f).setBold()));

        skapadAv.addCell(
                new Cell().setPadding(0f).setBorder(Border.NO_BORDER).add(
                        new Paragraph("Tel: " + renderer.eval(modelProp + ".vardenhet.telefonnummer"))
                                .setFontSize(10f)));

        skapadAv.addCell(
                new Cell().setPadding(0f).setBorder(Border.NO_BORDER).add(
                        new Paragraph(renderer.eval(modelProp + ".vardenhet.postadress") + ", "
                                + renderer.eval(modelProp + ".vardenhet.postnummer") + " "
                                + renderer.eval(modelProp + ".vardenhet.postort")).setFontSize(10f)));

        parent.add(skapadAv);
    }
}
