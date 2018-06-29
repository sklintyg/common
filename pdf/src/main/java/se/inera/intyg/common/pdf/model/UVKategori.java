package se.inera.intyg.common.pdf.model;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

public class UVKategori extends UVComponent {


    public UVKategori(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        String labelKey = (String) currentUvNode.get("labelKey");
        String kategori = renderer.getText(labelKey);

        Div borderDiv = new Div();
        parent.setBorder(new SolidBorder(black, 0.5f));

        // Setting -0.5f padding makes sure the borders are drawn on top of each other... ugly.
        parent.setPadding(-0.5f);

        borderDiv.add(new Paragraph(kategori.toUpperCase())
                .setMarginTop(0f)
                .setMarginBottom(0f)
                .setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f))
                .setFont(renderer.kategoriFont)
                .setFontSize(KATEGORI_FONT_SIZE)
                .setFontColor(wcColor07)
                .setBackgroundColor(white));
        borderDiv.setBorder(new SolidBorder(black, 0.5f));
        borderDiv.setKeepTogether(true);
        parent.add(borderDiv);
    }
}
