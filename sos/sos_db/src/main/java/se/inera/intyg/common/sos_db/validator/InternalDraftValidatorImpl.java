package se.inera.intyg.common.sos_db.validator;

import java.util.Collections;

import se.inera.intyg.common.sos_db.model.internal.DbUtlatande;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.InternalDraftValidator;

public class InternalDraftValidatorImpl implements InternalDraftValidator<DbUtlatande> {
    @Override
    public ValidateDraftResponse validateDraft(DbUtlatande utlatande) {
        return new ValidateDraftResponse(ValidationStatus.INVALID, Collections.emptyList());
    }
}
