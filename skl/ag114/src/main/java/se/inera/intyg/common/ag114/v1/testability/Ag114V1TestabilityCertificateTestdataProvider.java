/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.ag114.v1.testability;

import java.util.HashMap;
import java.util.Map;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;
import se.inera.intyg.common.support.facade.util.TestabilityCertificateTestdataProvider;

public class Ag114V1TestabilityCertificateTestdataProvider implements TestabilityCertificateTestdataProvider {

    @Override
    public Map<String, CertificateDataValue> getMinimumValues() {
        final var values = new HashMap<String, CertificateDataValue>();
        return values;
    }

    @Override
    public Map<String, CertificateDataValue> getMaximumValues() {
        final var values = new HashMap<String, CertificateDataValue>();
        return values;
    }
}
