/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

import java.util.Map;
import java.util.Objects;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public final class ValueToolkit {

    private ValueToolkit() {

    }

    public static Boolean booleanValue(Map<String, CertificateDataElement> data, String questionId, String valueId) {
        final var dataValue = getValue(data, questionId);
        if (!(dataValue instanceof CertificateDataValueBoolean)) {
            return null;
        }

        final var booleanDataValue = (CertificateDataValueBoolean) dataValue;
        if (!Objects.equals(booleanDataValue.getId(), valueId)) {
            return null;
        }

        return booleanDataValue.getSelected();
    }

    public static String textValue(Map<String, CertificateDataElement> data, String questionId, String valueId) {
        final var dataValue = getValue(data, questionId);
        if (!(dataValue instanceof CertificateDataTextValue)) {
            return null;
        }

        final var textDataValue = (CertificateDataTextValue) dataValue;
        if (!Objects.equals(textDataValue.getId(), valueId)) {
            return null;
        }

        return textDataValue.getText();
    }

    private static CertificateDataValue getValue(Map<String, CertificateDataElement> data, String questionId) {
        final var element = data.get(questionId);
        if (element == null) {
            return null;
        }

        return element.getValue();
    }
}
