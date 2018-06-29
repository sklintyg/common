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
import java.util.stream.Collectors;

import com.itextpdf.layout.element.Div;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class UPComponent {
    private String type;
    private String labelKey;
    private List<String> modelProp = new ArrayList<>();
    private String listKey;
    private List<String> listKeyResults = new ArrayList<>();
    private String contentUrl;
    private String separator;
    private String noValue;

    // The value here is evaluated when building the UVComponent graph
    private boolean render = true;

    private List<String> headers = new ArrayList<>();
    private List<String> colProps = new ArrayList<>();
    private List<String> valueProps = new ArrayList<>();
    private List<UPComponent> components = new ArrayList<>();
    private List<String> kvModelProps = new ArrayList<>();
    private List<String> kvLabelKeys = new ArrayList<>();

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

    public List<String> getModelProp() {
        return modelProp;
    }

    public void setModelProp(List<String> modelProp) {
        this.modelProp = modelProp;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public List<UPComponent> getComponents() {
        return components;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void setComponents(List<UPComponent> components) {
        this.components = components;
    }

    public List<String> getColProps() {
        return colProps;
    }

    public void setColProps(List<String> colProps) {
        this.colProps = colProps;
    }

    public List<String> getValueProps() {
        return valueProps;
    }

    public void setValueProps(List<String> valueProps) {
        this.valueProps = valueProps;
    }

    public List<String> getKvModelProps() {
        return kvModelProps;
    }

    public void setKvModelProps(List<String> kvModelProps) {
        this.kvModelProps = kvModelProps;
    }

    public List<String> getKvLabelKeys() {
        return kvLabelKeys;
    }

    public void setKvLabelKeys(List<String> kvLabelKeys) {
        this.kvLabelKeys = kvLabelKeys;
    }

    public String getListKey() {
        return listKey;
    }

    public void setListKey(String listKey) {
        this.listKey = listKey;
    }

    public List<String> getListKeyResults() {
        return listKeyResults;
    }

    public void setListKeyResults(List<String> listKeyResults) {
        this.listKeyResults = listKeyResults;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getNoValue() {
        return noValue;
    }

    public void setNoValue(String noValue) {
        this.noValue = noValue;
    }

    @Override
    public String toString() {
        return (render ? "RENDER" : "HIDE") + "[type = " + type + ", labelKey = " + labelKey + ", modelProp = " + modelProp.toString() + ", headers = " + headers.stream().collect(Collectors.joining(", ")) + "]";
    }
}
