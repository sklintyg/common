package se.inera.intyg.common.pdf.model;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Undefined;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

public class UVDelfraga extends UVComponent {

    public UVDelfraga(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        String labelKey = (String) currentUvNode.get("labelKey");
        if (labelKey == null) {
            return;
        }

        // Check if we have hide/show expressions
        if (renderMe(currentUvNode)) {
            String delFraga = renderer.getText(labelKey);
            if (delFraga != null) {
                parent.add(new Paragraph(delFraga)
                        .setMarginRight(millimetersToPoints(10f))
                        .setMarginLeft(millimetersToPoints(5f))
                        .setFont(renderer.fragaDelFragaFont)
                        .setFontColor(wcColor09)
                        .setFontSize(FRAGA_DELFRAGA_FONT_SIZE)
                        .setPadding(0f).setMarginTop(0f)
                        .setMarginBottom(0f));
            }
        } else {
            System.out.println("Skipping element " + labelKey + " due to show/hide expression");
        }
    }
}
