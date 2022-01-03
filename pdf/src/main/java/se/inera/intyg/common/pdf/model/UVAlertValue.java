/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

import java.util.stream.Stream;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

/**
 * Renders an alert component consisting of a table where cell1 contains an icon and the other cell contains the
 * alert text.
 */
public class UVAlertValue extends UVComponent {

    // Points
    private static final float ALERT_TABLE_PADDING = 10f;
    private static final float ALERT_TABLE_MARGIN = 5f;

    private static final float ALERT_IMAGE_SIZE = 16f;
    private static final String ALERT_LEVEL_JSON_PROPERTY = "alertLevel";
    private static final Color WC_INFO_BG = WC_COLOR_03;
    private static final Color WC_INFO_TEXT = WC_COLOR_06;
    private static final Color WC_WARNING_BG = WC_COLOR_02;
    private static final Color WC_WARNING_TEXT = WC_COLOR_05;

    private static final Color MI_INFO_BG = MI_COLOR_23;
    private static final Color MI_INFO_TEXT = MI_COLOR_01;
    private static final Color MI_WARNING_BG = MI_COLOR_25;
    private static final Color MI_WARNING_TEXT = MI_COLOR_27;

    public UVAlertValue(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean render(Div parent, ScriptObjectMirror currentUvNode) {
        String labelKey = (String) currentUvNode.get(LABEL_KEY);
        if (labelKey == null) {
            return false;
        }

        // Check if we have hide/show expressions
        boolean render = show(currentUvNode);
        if (render) {

            String delFraga = renderer.getText(labelKey);

            AlertLevel alertLevel = AlertLevel.from((String) currentUvNode.get(ALERT_LEVEL_JSON_PROPERTY));
            PdfImageXObject observandumIcon = alertLevel.equals(AlertLevel.WARNING) ? renderer.getObservandumIcon()
                : renderer.getObservandumInfoIcon();

            AlertColors colors = AlertColors.from(renderer.getPrintConfig().getApplicationOrigin(), alertLevel);

            Table table = new Table(2)
                .setPadding(millimetersToPoints(ALERT_TABLE_PADDING))
                .setMargin(millimetersToPoints(ALERT_TABLE_MARGIN))
                .setKeepTogether(true);
            table.setBackgroundColor(colors.getBgColor());

            Cell iconCell = new Cell();
            iconCell.setBorder(Border.NO_BORDER);
            iconCell.add(new Image(observandumIcon, ALERT_IMAGE_SIZE));
            table.addCell(iconCell);

            Paragraph paragraph = new Paragraph();
            paragraph = paragraph
                .add(delFraga)
                .setFont(renderer.fragaDelFragaFont)
                .setFontColor(colors.getTextColor())
                .setFontSize(FRAGA_DELFRAGA_FONT_SIZE);
            Cell textCell = new Cell();
            textCell.setBorder(Border.NO_BORDER);
            textCell.add(paragraph);
            table.addCell(textCell);

            if (delFraga != null) {
                parent.add(table);
            }
        }
        return render;
    }

    private enum AlertLevel {

        INFO("info"),
        WARNING("warning");

        private final String configValue;

        AlertLevel(String configValue) {
            this.configValue = configValue;
        }

        static AlertLevel from(String configValue) {
            return Stream.of(AlertLevel.values())
                .filter(ac -> ac.configValue.equals(configValue))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Undefined AlertLevel for  " + configValue));
        }

    }

    @SuppressWarnings("ImmutableEnumChecker")
    private enum AlertColors {
        MI_WARN(ApplicationOrigin.MINA_INTYG, AlertLevel.WARNING, MI_WARNING_TEXT, MI_WARNING_BG),
        MI_INFO(ApplicationOrigin.MINA_INTYG, AlertLevel.INFO, MI_INFO_TEXT, MI_INFO_BG),
        WC_WARN(ApplicationOrigin.WEBCERT, AlertLevel.WARNING, WC_WARNING_TEXT, WC_WARNING_BG),
        WC_INFO(ApplicationOrigin.WEBCERT, AlertLevel.INFO, WC_INFO_TEXT, WC_INFO_BG);

        private final ApplicationOrigin origin;
        private final AlertLevel alertLevel;
        private final Color textColor;
        private final Color bgColor;

        AlertColors(ApplicationOrigin origin, AlertLevel alertLevel, Color textColor, Color bgColor) {
            this.origin = origin;
            this.alertLevel = alertLevel;
            this.textColor = textColor;
            this.bgColor = bgColor;
        }

        static AlertColors from(ApplicationOrigin origin, AlertLevel alertLevel) {
            return Stream.of(AlertColors.values())
                .filter(ac -> ac.getOrigin().equals(origin) && ac.getAlertLevel().equals(alertLevel))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Undefined AlertColors for origin " + origin + " / alertLevel " + alertLevel));
        }

        public ApplicationOrigin getOrigin() {
            return origin;
        }

        public AlertLevel getAlertLevel() {
            return alertLevel;
        }

        public Color getTextColor() {
            return textColor;
        }

        public Color getBgColor() {
            return bgColor;
        }
    }
}
