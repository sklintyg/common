/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.support.api.dto;

import java.util.List;
import java.util.Objects;

public class AdditionalMetaData {

    private List<String> diagnoses;

    public List<String> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<String> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdditionalMetaData that = (AdditionalMetaData) o;

        if (diagnoses == null) {
            if (that.diagnoses != null) {
                return false;
            }
        } else if (!diagnoses.equals(that.diagnoses)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(diagnoses);
    }

    @Override
    public String toString() {
        return "AdditionalMetaData{"
            + "diagnoses=" + diagnoses
            + '}';
    }
}
