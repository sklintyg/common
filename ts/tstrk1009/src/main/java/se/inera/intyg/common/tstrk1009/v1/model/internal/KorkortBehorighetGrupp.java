package se.inera.intyg.common.tstrk1009.v1.model.internal;

import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getABTraktorBehorigheter;
import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getAllaBehorigheter;
import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getCEBehorigHeter;
import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getDBehorigHeter;
import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getKanintetastallning;
import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getTaxiBehorigheter;

import java.util.EnumSet;

public enum KorkortBehorighetGrupp {
    ALLA(getAllaBehorigheter()),
    A_B_TRAKTOR(getABTraktorBehorigheter()),
    C_E(getCEBehorigHeter()),
    D(getDBehorigHeter()),
    TAXI(getTaxiBehorigheter()),
    KANINTETASTALLNING(getKanintetastallning());

    private final EnumSet<Korkortsbehorighet> korkortsbehorigheter;

    KorkortBehorighetGrupp(final EnumSet<Korkortsbehorighet> korkortsbehorigheter) {
        this.korkortsbehorigheter = korkortsbehorigheter;
    }

    public EnumSet<Korkortsbehorighet> getKorkortsbehorigheter() {
        return korkortsbehorigheter;
    }
}
