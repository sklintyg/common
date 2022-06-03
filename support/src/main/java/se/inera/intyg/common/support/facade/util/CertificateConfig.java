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
package se.inera.intyg.common.support.facade.util;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;

import java.util.HashMap;
import java.util.Map;

public final class CertificateConfig {
    private CertificateTextProvider textProvider;
    private Map<String, CertificateConfigElement> elements;

    public CertificateTextProvider getTextProvider() {
        return textProvider;
    }

    public void setTextProvider(CertificateTextProvider textProvider) {
        this.textProvider = textProvider;
    }

    public Map<String, CertificateConfigElement> getElements() {
        return elements;
    }

    public void addElement(String key, CertificateConfigElement value) {
        if(elements == null) {
            elements = new HashMap<>();
        }
        elements.put(key, value);
    }

    public void addElement(String key, String valueId, String textId, String parent, int index, CertificateDataConfigTypes type) {
        final var element = CertificateConfigElement
                .builder()
                .id(key)
                .textId(textId)
                .valueId(valueId)
                .index(index)
                .configType(type)
                .parent(parent)
                .build();

        addElement(key, element);
    }

    public void addElement(String key, String textId, int index, CertificateDataConfigTypes type) {
        final var element = CertificateConfigElement
                .builder()
                .id(key)
                .index(index)
                .configType(type)
                .build();

        addElement(key, element);
    }


    public CertificateConfigElement getElement(String key) {
        return elements.get(key);
    }
}

