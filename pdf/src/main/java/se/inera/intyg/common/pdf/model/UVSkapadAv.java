package se.inera.intyg.common.pdf.model;

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

import java.util.List;
import java.util.stream.Collectors;

public class UVSkapadAv extends UVComponent {

    public UVSkapadAv(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        String modelProp = (String) currentUvNode.get("modelProp");

        StringBuilder intygsUtfardare = buildIntygsutfardare(modelProp);

        StringBuilder kontaktUppgifter = buildKontaktuppgifter(modelProp);

        // Render
        parent.add(new Paragraph("Intygsutfärdare:").setBold());
        parent.add(new Paragraph(intygsUtfardare.toString()));
        parent.add(new Paragraph("Kontaktuppgifter:").setBold());
        parent.add(new Paragraph(kontaktUppgifter.toString()));
    }

    private StringBuilder buildIntygsutfardare(String modelProp) {
        StringBuilder intygsUtfardare = new StringBuilder();
        intygsUtfardare.append(renderer.eval(modelProp + ".fullstandigtNamn").toString()).append("\n");

        // Befattningar
        List<String> befattningar = fromStringArray(renderer.eval(modelProp + ".befattningar"));
        if (befattningar.size() > 0) {
            intygsUtfardare.append(befattningar.stream().collect(Collectors.joining(" ,"))).append("\n");
        }

        // Specialistkompetenser
        List<String> specialistkompentenser = fromStringArray(renderer.eval(modelProp + ".specialiteter"));
        if (specialistkompentenser.size() > 0) {
            intygsUtfardare.append(befattningar.stream().collect(Collectors.joining(" ,"))).append("\n");
        }

        // Leg yrkesgrupp.
        return intygsUtfardare;
    }

    private StringBuilder buildKontaktuppgifter(String modelProp) {
        StringBuilder kontaktUppgifter = new StringBuilder();
        kontaktUppgifter.append(renderer.eval(modelProp + ".vardenhet.enhetsnamn").toString())
                .append("\n");

        kontaktUppgifter.append(renderer.eval(modelProp + ".vardenhet.postnummer").toString())
                .append(" ")
                .append(renderer.eval(modelProp + ".vardenhet.postadress").toString())
                .append("\n");
        kontaktUppgifter.append(renderer.eval(modelProp + ".vardenhet.telefonnummer").toString())
                .append("\n");
        return kontaktUppgifter;
    }
}
