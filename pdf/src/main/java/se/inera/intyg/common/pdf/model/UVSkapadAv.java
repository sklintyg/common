/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.pdf.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;
import se.inera.intyg.common.support.services.BefattningService;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

/**
 * Renders a uv-skapad-av component.
 *
 * @author eriklupander
 */
public class UVSkapadAv extends UVComponent {

    public UVSkapadAv(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean render(Div parent, ScriptObjectMirror currentUvNode) {
        String modelProp = (String) currentUvNode.get(MODEL_PROP);

        StringBuilder intygsUtfardare = buildIntygsutfardare(modelProp);
        StringBuilder kontaktUppgifter = buildKontaktuppgifter(modelProp);
        String signaturDatum = buildSigneringsDatum();

        boolean isUtkast = renderer.getPrintConfig().isUtkast() || renderer.getPrintConfig().isLockedUtkast();
        boolean showSignatureLine = renderer.getPrintConfig().showSignatureLine();
        // Render
        parent.setKeepTogether(true);

        if (!isUtkast) {
            parent.add(new Paragraph("Intygsutfärdare:")
                .setMarginBottom(0f)
                .setFont(renderer.fragaDelFragaFont)
                .setFontSize(FRAGA_DELFRAGA_FONT_SIZE));
            parent.add(new Paragraph(intygsUtfardare.toString())
                .setMarginTop(0f)
                .setFont(renderer.svarFont)
                .setFontSize(SVAR_FONT_SIZE));
        }

        parent.add(new Paragraph("Kontaktuppgifter:")
            .setMarginBottom(0f)
            .setFont(renderer.fragaDelFragaFont)
            .setFontSize(FRAGA_DELFRAGA_FONT_SIZE));
        parent.add(new Paragraph(kontaktUppgifter.toString())
            .setMarginTop(0f)
            .setFont(renderer.svarFont)
            .setFontSize(SVAR_FONT_SIZE));

        // Endast ut om det är ett signerat intyg.
        if (!isUtkast) {
            parent.add(new Paragraph("Intyget signerades:")
                .setMarginBottom(0f)
                .setFont(renderer.fragaDelFragaFont)
                .setFontSize(FRAGA_DELFRAGA_FONT_SIZE));
            parent.add(new Paragraph(signaturDatum)
                .setMarginTop(0f)
                .setFont(renderer.svarFont)
                .setFontSize(SVAR_FONT_SIZE));
        }
        if (showSignatureLine) {
            // CHECKSTYLE:OFF MagicNumber
            Div lineDiv = new Div();
            lineDiv.setWidth(millimetersToPoints(50f));
            lineDiv.setMarginTop(20f);
            final SolidLine lineDrawer = new SolidLine(1f);
            lineDrawer.setColor(WC_COLOR_07);
            lineDiv.add(new LineSeparator(lineDrawer));
            parent.add(lineDiv);

            parent.add(new Paragraph("Intygsutfärdarens underskrift")
                .setMarginTop(5f)
                .setMarginBottom(0f)
                .setFont(renderer.svarFont)
                .setFontSize(SVAR_FONT_SIZE));
            // CHECKSTYLE:ON MagicNumber
        }

        return true;
    }

    private String buildSigneringsDatum() {
        String str = (String) renderer.evalValueFromModel("grundData.signeringsdatum");
        if (Strings.isNullOrEmpty(str)) {
            return "";
        }
        LocalDateTime signeringsDatum = LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return signeringsDatum.format(DateTimeFormatter.ISO_DATE);
    }

    private StringBuilder buildIntygsutfardare(String modelProp) {
        StringBuilder intygsUtfardare = new StringBuilder();
        intygsUtfardare.append(renderer.evalValueFromModel(modelProp + ".fullstandigtNamn").toString()).append("\n");

        // Befattningar
        List<String> befattningar = fromStringArray(renderer.evalValueFromModel(modelProp + ".befattningar"));
        if (befattningar.size() > 0) {
            intygsUtfardare.append(befattningar.stream()
                .map(befattningsKod -> BefattningService.getDescriptionFromCode(befattningsKod).orElse(befattningsKod))
                .collect(Collectors.joining(", "))).append("\n");
        }

        // Specialistkompetenser
        List<String> specialistkompentenser = fromStringArray(renderer.evalValueFromModel(modelProp + ".specialiteter"));
        if (specialistkompentenser.size() > 0) {
            intygsUtfardare.append(specialistkompentenser.stream().collect(Collectors.joining(", "))).append("\n");
        }

        // Leg yrkesgrupp.
        return intygsUtfardare;
    }

    private StringBuilder buildKontaktuppgifter(String modelProp) {
        StringBuilder kontaktUppgifter = new StringBuilder();

        Object eval = renderer.evalValueFromModel(modelProp + ".vardenhet.enhetsnamn");
        if (eval != null) {
            kontaktUppgifter.append(eval.toString())
                .append("\n");
        }

        eval = renderer.evalValueFromModel(modelProp + ".vardenhet.postadress");
        if (eval != null) {
            kontaktUppgifter.append(eval.toString())
                .append("\n");
        }

        eval = renderer.evalValueFromModel(modelProp + ".vardenhet.postnummer");
        if (eval != null) {
            kontaktUppgifter.append(eval.toString())
                .append(" ");
            Object evalPostort = renderer.evalValueFromModel(modelProp + ".vardenhet.postort");
            if (evalPostort != null) {
                kontaktUppgifter.append(evalPostort.toString());
            }
            kontaktUppgifter.append("\n");
        }

        eval = renderer.evalValueFromModel(modelProp + ".vardenhet.telefonnummer");
        if (eval != null) {
            kontaktUppgifter.append(eval.toString())
                .append("\n");
        }

        return kontaktUppgifter;
    }
}
