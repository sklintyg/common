package se.inera.intyg.common.sos_db.model.util;

import se.inera.intyg.common.sos_db.model.internal.DbUtlatande;
import se.inera.intyg.common.support.model.util.ModelCompareUtil;

public class DbModelCompareUtilImpl implements ModelCompareUtil<DbUtlatande> {
    @Override
    public boolean isValidForNotification(DbUtlatande utlatande) {
        return true;
    }
}
