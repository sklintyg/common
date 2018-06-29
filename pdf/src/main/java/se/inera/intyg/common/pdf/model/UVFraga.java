package se.inera.intyg.common.pdf.model;

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

public class UVFraga extends UVComponent {

    public UVFraga(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        if (!currentUvNode.containsKey("labelKey")) {
            return;
        }

        String labelKey = (String) currentUvNode.get("labelKey");

        String fraga = renderer.getText(labelKey);
        parent.add(new Paragraph(fraga)
                .setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f))
                .setFont(renderer.fragaDelFragaFont)
                .setFontColor(wcColor07)
                .setFontSize(FRAGA_DELFRAGA_FONT_SIZE)
        );
    }
}
