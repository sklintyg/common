/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.modules.registry;

public class IntygModule implements Comparable<IntygModule> {

    private String id;

    private String label;

    private String description;

    private String detailedDescription;

    private String cssPath;

    private String scriptPath;

    private String dependencyDefinitionPath;

    public IntygModule(String id, String label, String description, String detailedDescription, String cssPath, String scriptPath,
            String dependencyDefinitionPath) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.detailedDescription = detailedDescription;
        this.cssPath = cssPath;
        this.scriptPath = scriptPath;
        this.dependencyDefinitionPath = dependencyDefinitionPath;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public String getCssPath() {
        return cssPath;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public String getDependencyDefinitionPath() {
        return dependencyDefinitionPath;
    }

    @Override
    public int compareTo(IntygModule o) {
        return getLabel().compareTo(o.getLabel());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntygModule) {
            IntygModule other = (IntygModule) obj;
            return id.equals(other.id);
        } else {
            return false;
        }
    }
}
