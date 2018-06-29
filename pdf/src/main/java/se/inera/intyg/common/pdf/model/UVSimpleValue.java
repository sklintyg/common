package se.inera.intyg.common.pdf.model;

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

public class UVSimpleValue extends UVComponent {

    public UVSimpleValue(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        String modelProp = (String) currentUvNode.get("modelProp");
        Object value = renderer.eval(modelProp);
        if (value != null) {
            parent.add(new Paragraph(value.toString()).setItalic()
                    .setMarginRight(millimetersToPoints(10f))
                    .setMarginLeft(millimetersToPoints(5f))
                    .setFont(renderer.svarFont)
                    .setFontSize(SVAR_FONT_SIZE)
                    .setPadding(0f).setMarginTop(0f).setMarginBottom(0f));
        }

    }
}
