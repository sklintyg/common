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
package se.inera.intyg.common.pdf;

import java.util.ArrayList;
import java.util.List;

public class LayoutComponent {
    private String type;

    private String labelKey;

    private String modelProp;

    private String contentUrl;

    private String showExpression;

    private String hideExpression;

    private List<String> headers = new ArrayList<>();

    private List<String> valueProps = new ArrayList<>();

    private List<LayoutComponent> components = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }

    public String getModelProp() {
        return modelProp;
    }

    public void setModelProp(String modelProp) {
        this.modelProp = modelProp;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getHideExpression() {
        return hideExpression;
    }

    public void setHideExpression(String hideExpression) {
        this.hideExpression = hideExpression;
    }

    public String getShowExpression() {
        return showExpression;
    }

    public void setShowExpression(String showExpression) {
        this.showExpression = showExpression;
    }

    public List<LayoutComponent> getComponents() {
        return components;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void setComponents(List<LayoutComponent> components) {
        this.components = components;
    }

    public List<String> getValueProps() {
        return valueProps;
    }

    public void setValueProps(List<String> valueProps) {
        this.valueProps = valueProps;
    }

    @Override
    public String toString() {
        return "ClassPojo [type = " + type + ", modelProp = " + modelProp + "]";
    }
}
