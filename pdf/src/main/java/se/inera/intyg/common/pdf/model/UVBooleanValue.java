package se.inera.intyg.common.pdf.model;

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

public class UVBooleanValue extends UVComponent {

    public UVBooleanValue(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        String booleanValue = getBooleanValue((String) currentUvNode.get("modelProp"));
        parent.add(new Paragraph(booleanValue)
                .setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f))
                .setFont(renderer.svarFont)
                .setFontSize(SVAR_FONT_SIZE));
    }
}
