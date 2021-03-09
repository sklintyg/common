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
        return "AdditionalMetaData{" +
            "diagnoses=" + diagnoses +
            '}';
    }
}
