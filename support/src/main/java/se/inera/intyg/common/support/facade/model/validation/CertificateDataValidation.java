package se.inera.intyg.common.support.facade.model.validation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @Type(value = CertificateDataValidationMandatory.class, name = "MANDATORY_VALIDATION"),
    @Type(value = CertificateDataValidationShow.class, name = "SHOW_VALIDATION")
})
public interface CertificateDataValidation {

    CertificateDataValidationType getType();
}
