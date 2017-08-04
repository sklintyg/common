package se.inera.intyg.common.sos_doi.validator;

import java.util.Collections;

import se.inera.intyg.common.sos_doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.InternalDraftValidator;

public class InternalDraftValidatorImpl implements InternalDraftValidator<DoiUtlatande> {
    @Override
    public ValidateDraftResponse validateDraft(DoiUtlatande utlatande) {
        return new ValidateDraftResponse(ValidationStatus.INVALID, Collections.emptyList());
    }
}
