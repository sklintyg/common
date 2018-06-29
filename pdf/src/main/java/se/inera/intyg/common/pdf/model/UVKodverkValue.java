package se.inera.intyg.common.pdf.model;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

public class UVKodverkValue extends UVComponent {

    public UVKodverkValue(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        List<String> kvModelProps = fromStringArray(currentUvNode.get("kvModelProps"));
        List<String> kvLabelKeys = fromStringArray(currentUvNode.get("kvLabelKeys"));

        List<String> kvParts = new ArrayList<>();
        for (int a = 0; a < kvLabelKeys.size(); a++) {
            String currentModelProp = kvModelProps.get(a);
            String currentLabelKey = kvLabelKeys.get(a);

            String nestedValue = (String) renderer.eval(currentModelProp);
            String textKey = currentLabelKey.replaceAll("\\{var\\}", nestedValue);
            String value = renderer.getText(textKey);
            kvParts.add(value);
        }

        parent.add(new Paragraph(kvParts.stream().collect(Collectors.joining(" ")))
                .setItalic()
                .setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f))
                .setFont(renderer.svarFont)
                .setFontSize(SVAR_FONT_SIZE)
                .setPadding(0f).setMarginTop(0f).setMarginBottom(0f));
    }
}
