package se.inera.intyg.common.sos_parent.model.internal;

import java.time.LocalDate;

import se.inera.intyg.common.support.model.common.internal.Utlatande;

/**
 * Shared fields between Dödsbevis and Dödsorsaksintyg.
 */
public interface SosUtlatande extends Utlatande {

    String getIdentitetStyrkt();

    Boolean getDodsdatumSakert();

    String getDodsdatum();

    LocalDate getAntraffatDodDatum();

    String getDodsplatsKommun();

    DodsplatsBoende getDodsplatsBoende();

    Boolean getBarn();
}
