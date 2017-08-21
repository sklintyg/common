package se.inera.intyg.common.sos_parent.model.internal;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

/**
 * Shared fields between Dödsbevis and Dödsorsaksintyg.
 */
public interface SosUtlatande extends Utlatande {

    String getIdentitetStyrkt();

    Boolean getDodsdatumSakert();

    InternalDate getDodsdatum();

    InternalDate getAntraffatDodDatum();

    String getDodsplatsKommun();

    DodsplatsBoende getDodsplatsBoende();

    Boolean getBarn();
}
