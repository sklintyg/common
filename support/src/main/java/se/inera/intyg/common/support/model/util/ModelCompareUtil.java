package se.inera.intyg.common.support.model.util;

import se.inera.intyg.common.support.model.common.internal.Utlatande;

public interface ModelCompareUtil <T extends Utlatande> {
    boolean isValidForNotification(T utlatande);
}
