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
package se.inera.intyg.common.tstrk1009.v1.model.converter;

import static se.inera.intyg.common.support.Constants.KV_INTYGET_AVSER_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_UTLATANDETYP_INTYG_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.NOT_AVAILABLE;
import static se.inera.intyg.common.ts_parent.model.converter.InternalToTransportUtil.getVersion;

import com.google.common.base.Strings;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.tstrk1009.support.Tstrk1009EntryPoint;
import se.inera.intyg.common.tstrk1009.v1.model.internal.IntygetAvserBehorighet;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;
import se.inera.intyg.schemas.contract.Personnummer;

public final class UtlatandeToIntyg {

    private static final String DEFAULT_VERSION = "6.7";

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(Tstrk1009UtlatandeV1 utlatande) {
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
        typAvIntyg.setCode(Tstrk1009EntryPoint.KV_UTLATANDETYP_INTYG_CODE);
        typAvIntyg.setCodeSystem(KV_UTLATANDETYP_INTYG_CODE_SYSTEM);
        typAvIntyg.setDisplayName(Tstrk1009EntryPoint.ISSUER_MODULE_NAME);
        return typAvIntyg;
    }

    private static void complementArbetsplatskodIfMissing(Intyg intyg) {
        if (Strings.nullToEmpty(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension()).trim().isEmpty()) {
            intyg.getSkapadAv().getEnhet().getArbetsplatskod().setExtension(NOT_AVAILABLE);
        }
    }

    private static void formatPersonId(Intyg intyg) {
        String personId = intyg.getPatient().getPersonId().getExtension();

        Optional<Personnummer> personnummer = Personnummer.createPersonnummer(personId);
        if (personnummer.isPresent()) {
            intyg.getPatient().getPersonId().setExtension(personnummer.get().getPersonnummerWithDash());
        }
    }

    private static List<Svar> getSvar(Tstrk1009UtlatandeV1 source) {
        List<Svar> svars = new ArrayList<>();

        int intygAvserInstans = 1;

        if (source.getIntygetAvserBehorigheter() != null) {
            for (IntygetAvserBehorighet korkortstyp : source.getIntygetAvserBehorigheter().getBehorigheter()) {
                IntygAvserKod intygAvser = IntygAvserKod.valueOf(korkortstyp.name());
                svars.add(aSvar(INTYG_AVSER_SVAR_ID_1, intygAvserInstans++)
                        .withDelsvar(INTYG_AVSER_DELSVAR_ID_1,
                                aCV(KV_INTYGET_AVSER_CODE_SYSTEM, intygAvser.getCode(), intygAvser.getDescription()))
                        .build());
            }
        }
        return svars;
    }

}
