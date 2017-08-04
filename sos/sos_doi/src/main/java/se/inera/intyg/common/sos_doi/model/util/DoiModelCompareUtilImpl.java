package se.inera.intyg.common.sos_doi.model.util;

import se.inera.intyg.common.sos_doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.support.model.util.ModelCompareUtil;

public class DoiModelCompareUtilImpl implements ModelCompareUtil<DoiUtlatande> {
    @Override
    public boolean isValidForNotification(DoiUtlatande utlatande) {
        return true;
    }
}
