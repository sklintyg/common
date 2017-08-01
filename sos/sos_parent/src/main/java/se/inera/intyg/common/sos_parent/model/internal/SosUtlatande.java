package se.inera.intyg.common.sos_parent.model.internal;

import se.inera.intyg.common.support.model.common.internal.Utlatande;

import java.time.LocalDate;

/**
 * Shared fields between Dödsbevis and Dödsorsaksintyg.
 */
public interface SosUtlatande extends Utlatande {

    String getIdentitetStyrkt();

    boolean isDodsdatumSakert();

    String getDodsdatum();

    LocalDate getAntraffatDodDatum();

    String getDodsplatsKommun();

    DodsplatsBoende getDodsplatsBoende();

    boolean isBarn();
}
