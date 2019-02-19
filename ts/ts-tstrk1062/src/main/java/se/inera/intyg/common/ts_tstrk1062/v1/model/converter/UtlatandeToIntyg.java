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

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;
import se.inera.intyg.common.ts_tstrk1062.support.TsTstrk1062EntryPoint;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.*;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateTypeFormatEnum;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.support.Constants.*;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.*;
import static se.inera.intyg.common.ts_parent.model.converter.InternalToTransportUtil.getVersion;
import static se.inera.intyg.common.ts_tstrk1062.v1.model.converter.TSTRK1062Constants.*;

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
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
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
            for (IntygAvser.BehorighetsTyp behorighetsTyp : source.getIntygAvser().getBehorigheter()) {
                IntygAvserKod intygAvser = IntygAvserKod.fromCode(behorighetsTyp.name());
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

        if (source.getDiagnosRegistrering() != null) {
            switch (source.getDiagnosRegistrering().getTyp()) {
                case DIAGNOS_KODAD:
                    handleDiagnosKodad(source.getDiagnosKodad(), svars);
                    break;
                case DIAGNOS_FRITEXT:
                    handleDiagnosFritext(source.getDiagnosFritext(), svars);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        if (source.getLakemedelsbehandling() != null) {
            handleLakemedelsbehandling(source.getLakemedelsbehandling(), svars);
        }

        if (source.getBedomningAvSymptom() != null) {
            svars.add(aSvar(SYMPTOM_BEDOMNING_SVAR_ID)
                    .withDelsvar(SYMPTOM_BEDOMNING_DELSVAR_ID, source.getBedomningAvSymptom())
                    .build());
        }

        if (source.getPrognosTillstand() != null) {
            final PrognosTillstand.PrognosTillstandTyp prognosTillstandTyp = source.getPrognosTillstand().getTyp();

            Object content = null;
            switch (prognosTillstandTyp) {
                case JA:
                case NEJ:
                    content = prognosTillstandTyp.getCode();
                    break;
                case KANEJBEDOMA:
                    content = aCV(KV_V3_CODE_SYSTEM_NULLFLAVOR_SYSTEM, prognosTillstandTyp.getCode(),
                            prognosTillstandTyp.getDescription());
                    break;
            }

            if (content != null) {
                svars.add(aSvar(SYMPTOM_PROGNOS_SVAR_ID)
                        .withDelsvar(SYMPTOM_PROGNOS_DELSVAR_ID, content)
                        .build());
            }
        }

        if (source.getOvrigaKommentarer() != null) {
            svars.add(aSvar(OVRIGT_OVRIGA_KOMMENTARER_SVAR_ID)
                    .withDelsvar(OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_ID, source.getOvrigaKommentarer())
                    .build());
        }

        if (source.getBedomning() != null) {
            int behorighetskravInstans = 1;
            for (Bedomning.BehorighetsTyp behorighetsTyp : source.getBedomning().getUppfyllerBehorighetskrav()) {
                KorkortsbehorighetKod korkortsbehorighetKod = KorkortsbehorighetKod.fromCode(behorighetsTyp.name());
                svars.add(aSvar(BEDOMNING_UPPFYLLER_SVAR_ID, behorighetskravInstans++)
                        .withDelsvar(BEDOMNING_UPPFYLLER_DELSVAR_ID,
                                aCV(KV_KORKORTSBEHORIGHET_CODE_SYSTEM, korkortsbehorighetKod.getCode(),
                                        korkortsbehorighetKod.getDescription()))
                        .build());
            }
        }

        return svars;
    }

    private static void handleDiagnosKodad(ImmutableList<DiagnosKodad> diagnosKodad, List<Svar> svars) {
        int diagnosKodadInstans = 1;
        for (DiagnosKodad diagnos : diagnosKodad) {
            svars.add(aSvar(ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID, diagnosKodadInstans++)
                    .withDelsvar(ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_ID,
                            aCV(Diagnoskodverk.valueOf(diagnos.getDiagnosKodSystem()).getCodeSystem(),
                                    diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                    .withDelsvar(ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_ID, diagnos.getDiagnosBeskrivning())
                    .withDelsvar(ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_ID,
                            aPartialDate(PartialDateTypeFormatEnum.YYYY, Year.of(Integer.parseInt(diagnos.getDiagnosArtal()))))
                    .build());
        }
    }

    private static void handleDiagnosFritext(DiagnosFritext diagnosFritext, List<Svar> svars) {
        SvarBuilder diagnosSvar = aSvar(ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_ID);
        diagnosSvar.withDelsvar(ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_ID, diagnosFritext.getDiagnosFritext())
                .withDelsvar(ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_ID,
                        aPartialDate(PartialDateTypeFormatEnum.YYYY, Year.of(Integer.parseInt(diagnosFritext.getDiagnosArtal())))
                );

        if (!diagnosSvar.delSvars.isEmpty()) {
            svars.add(diagnosSvar.build());
        }
    }

    private static void handleLakemedelsbehandling(Lakemedelsbehandling lakemedelsbehandling, List<Svar> svars) {
        if (lakemedelsbehandling.getHarHaft() != null) {
            svars.add(aSvar(LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID)
                    .withDelsvar(LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID, InternalConverterUtil.getBooleanContent(lakemedelsbehandling.getHarHaft()))
                    .build());
        }

        if (lakemedelsbehandling.getPagar() != null) {
            svars.add(aSvar(LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID)
                    .withDelsvar(LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID, InternalConverterUtil.getBooleanContent(lakemedelsbehandling.getPagar()))
                    .build());
        }

        if (lakemedelsbehandling.getAktuell() != null) {
            svars.add(aSvar(LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID)
                    .withDelsvar(LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID, lakemedelsbehandling.getAktuell())
                    .build());
        }

        if (lakemedelsbehandling.getPagatt() != null) {
            svars.add(aSvar(LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID)
                    .withDelsvar(LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID, InternalConverterUtil.getBooleanContent(lakemedelsbehandling.getPagatt()))
                    .build());
        }

        if (lakemedelsbehandling.getEffekt() != null) {
            svars.add(aSvar(LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID)
                    .withDelsvar(LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID, InternalConverterUtil.getBooleanContent(lakemedelsbehandling.getEffekt()))
                    .build());
        }

        if (lakemedelsbehandling.getFoljsamhet() != null) {
            svars.add(aSvar(LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID)
                    .withDelsvar(LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID, InternalConverterUtil.getBooleanContent(lakemedelsbehandling.getFoljsamhet()))
                    .build());
        }

        if (lakemedelsbehandling.getAvslutadTidpunkt() != null) {
            svars.add(aSvar(LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID)
                    .withDelsvar(LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID,
                            aPartialDate(PartialDateTypeFormatEnum.YYYY_MM_DD, lakemedelsbehandling.getAvslutadTidpunkt().asLocalDate()))
                    .withDelsvar(LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID, lakemedelsbehandling.getAvslutadOrsak())
                    .build());
        }
    }
}
