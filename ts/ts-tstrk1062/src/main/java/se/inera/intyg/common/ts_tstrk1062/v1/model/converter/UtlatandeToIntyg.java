/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.ts_tstrk1062.v1.model.converter;

import static se.inera.intyg.common.support.Constants.*;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.*;
import static se.inera.intyg.common.ts_tstrk1062.v1.model.converter.RespConstants.*;
import static se.inera.intyg.common.ts_parent.model.converter.InternalToTransportUtil.getVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Strings;

import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_tstrk1062.support.TsTstrk1062EntryPoint;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.TsTstrk1062UtlatandeV1;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public final class UtlatandeToIntyg {

    private static final String DEFAULT_VERSION = "1.0";

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(TsTstrk1062UtlatandeV1 utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, true);

        complementArbetsplatskodIfMissing(intyg);

        intyg.setTyp(getTypAvIntyg());
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setVersion(getVersion(utlatande).orElse(DEFAULT_VERSION));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));

        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg() {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(TsTstrk1062EntryPoint.KV_UTLATANDETYP_INTYG_CODE);
        typAvIntyg.setCodeSystem(KV_UTLATANDETYP_INTYG_CODE_SYSTEM);
        typAvIntyg.setDisplayName(TsTstrk1062EntryPoint.ISSUER_MODULE_NAME);
        return typAvIntyg;
    }

    private static void complementArbetsplatskodIfMissing(Intyg intyg) {
        if (Strings.nullToEmpty(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension()).trim().isEmpty()) {
            intyg.getSkapadAv().getEnhet().getArbetsplatskod().setExtension(NOT_AVAILABLE);
        }
    }

    private static List<Svar> getSvar(TsTstrk1062UtlatandeV1 source) {
        List<Svar> svars = new ArrayList<>();

        int intygAvserInstans = 1;

        if (source.getIntygAvser() != null) {
            for (IntygAvserKategori korkortstyp : source.getIntygAvser().getKorkortstyp()) {
                IntygAvserKod intygAvser = IntygAvserKod.fromCode(korkortstyp.name());
                svars.add(aSvar(INTYG_AVSER_SVAR_ID_1, intygAvserInstans++)
                        .withDelsvar(INTYG_AVSER_DELSVAR_ID_1,
                                aCV(KV_INTYGET_AVSER_CODE_SYSTEM, intygAvser.getCode(), intygAvser.getDescription()))
                        .build());
            }
        }

        if (source.getIdKontroll() != null) {
            final IdKontrollKod idKontrollKod = source.getIdKontroll().getTyp();
            svars.add(aSvar(ID_KONTROLL_SVAR_ID_1, intygAvserInstans++)
                    .withDelsvar(ID_KONTROLL_DELSVAR_ID_1,
                            aCV(KV_ID_KONTROLL_CODE_SYSTEM, idKontrollKod.getCode(), idKontrollKod.getDescription()))
                    .build());
        }

        return svars;
    }
}
