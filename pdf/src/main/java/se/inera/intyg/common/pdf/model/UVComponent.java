/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.element.Div;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Undefined;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

public abstract class UVComponent {

    protected static final float KATEGORI_FONT_SIZE = 14f;
    protected static final float FRAGA_DELFRAGA_FONT_SIZE = 12f;
    protected static final float SVAR_FONT_SIZE = 12f;

    protected final UVRenderer renderer;

    protected Color white = new DeviceRgb(255, 255, 255);
    protected Color black = new DeviceRgb(0, 0, 0);
    protected Color ineraBlue = new DeviceRgb(67, 121, 154);

    protected Color wcColor02 = new DeviceRgb(0xFF, 0xEB, 0xBA);
    protected Color wcColor07 = new DeviceRgb(33, 33, 33);
    protected Color wcColor09 = new DeviceRgb(106, 106, 106);

    public UVComponent(UVRenderer renderer) {
        this.renderer = renderer;
    }

    public abstract void render(Div parent, ScriptObjectMirror currentUvNode);

    protected List<String> fromStringArray(Object arrayValues) {
        List<String> results = new ArrayList<>();
        if (arrayValues != null) {
            ScriptObjectMirror valuesRoot = (ScriptObjectMirror) arrayValues;
            for (Map.Entry<String, Object> entry : valuesRoot.entrySet()) {
                // Typically string or function
                if (entry.getValue() instanceof String) {
                    results.add((String) entry.getValue());
                }
            }
        }
        return results;
    }

    protected String getBooleanValue(String modelProp) {
        Object eval = renderer.eval(modelProp);
        if (eval != null) {
            if (eval instanceof Boolean) {
                return ((Boolean) eval).booleanValue() ? "Ja" : "Nej";
            }
        }
        return "ERROR";
    }

    protected boolean renderMe(ScriptObjectMirror obj) {
        boolean render = true;
        if (isNotEligibleForCheck(obj)) {
            return true;
        }

        // Show expression
        if (obj.containsKey("hideExpression")) {
            render = handleHideExpression(obj);
        } else if (obj.containsKey("showExpression")) {
            render = handleShowExpression(obj);
        }
        return render;
    }

    private boolean isNotEligibleForCheck(ScriptObjectMirror obj) {
        return !obj.containsKey("hideExpression") && !obj.containsKey("showExpression");
    }

    private boolean handleHideExpression(ScriptObjectMirror obj) {
        boolean render;
        Object hideExpression = obj.get("hideExpression");
        if (hideExpression instanceof ScriptObjectMirror) {
            Object result = ((ScriptObjectMirror) hideExpression).call(null, renderer.getIntygModel());
            render = !isTrue(result);
        } else {
            // handle as predicate expression...
            render = resolveHideExpression((String) hideExpression);
        }
        return render;
    }

    private boolean handleShowExpression(ScriptObjectMirror obj) {
        boolean render;
        Object showExpression = obj.get("showExpression");
        if (showExpression instanceof ScriptObjectMirror) {
            Object result = ((ScriptObjectMirror) showExpression).call(null, renderer.getIntygModel());
            render = isTrue(result);
        } else {
            // handle as predicate expression...
            render = resolveShowExpression((String) showExpression);
        }
        return render;
    }

    private boolean resolveHideExpression(String hideExpression) {
        boolean render;
        String expression = hideExpression;
        if (expression.startsWith("!")) {
            // negation
            Object eval = renderer.eval(expression.substring(1));
            render = isTrue(eval);
        } else {
            Object eval = renderer.eval(expression);
            render = !isTrue(eval);
        }
        return render;
    }

    private boolean resolveShowExpression(String expression) {
        boolean render;
        if (expression.startsWith("!")) {
            // negation
            Object eval = renderer.eval(expression.substring(1));
            render = !isTrue(eval);
        } else {
            Object eval = renderer.eval(expression);
            render = isTrue(eval);
        }
        return render;
    }

    private boolean isTrue(Object res) {
        if (res == null) {
            return false;
        }
        if (res instanceof Undefined) {
            return false;
        }
        if (res instanceof Boolean) {
            return ((Boolean) res).booleanValue();
        }
        return false;
    }
}
