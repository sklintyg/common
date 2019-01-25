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

import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.*;
import static se.inera.intyg.common.ts_tstrk1062.v1.model.converter.RespConstants.*;

import java.util.EnumSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.*;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

import javax.annotation.Nullable;

public final class TransportToInternal {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    private TransportToInternal() {
    }

    public static TsTstrk1062UtlatandeV1 convert(Intyg source) throws ConverterException {
        TsTstrk1062UtlatandeV1.Builder utlatande = TsTstrk1062UtlatandeV1.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, true));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(TsTstrk1062UtlatandeV1.Builder utlatande, Intyg source) throws ConverterException {
        EnumSet<IntygAvserKategori> intygAvserSet = EnumSet.noneOf(IntygAvserKategori.class);

        Bedomning.Builder bedomning = Bedomning.builder();
        Set<Bedomning.BehorighetsTyp> bedomningUppfyllerBehorighetskrav = EnumSet.noneOf(Bedomning.BehorighetsTyp.class);

        Boolean harHaft = null;
        Boolean pagar = null;
        String aktuell = null;
        Boolean pagatt = null;
        Boolean effekt = null;
        Boolean foljsamhet = null;
        InternalDate avslutadTidpunkt = null;
        String avslutadOrsak = null;

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case INTYG_AVSER_SVAR_ID_1:
                    handleIntygAvser(utlatande, svar, intygAvserSet);
                    break;
                case ID_KONTROLL_SVAR_ID_1:
                    handleIdKontroll(utlatande, svar);
                    break;
                case ALLMANT_DIAGNOSKOD_KODAD_ALLMANT_SVAR_ID:
                    handleDiagnosKodad(utlatande, svar);
                    break;
                case ALLMANT_DIAGNOSKOD_FRITEXT_ALLMANT_SVAR_ID:
                    handleDiagnosFritext(utlatande, svar);
                    break;
                case LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID:
                    handleLakemedelsbehandlingForekommit(utlatande, svar, harHaft);
                case LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID:
                    handleLakemedelsbehandlingPagar(utlatande, svar, pagar);
                    break;
                case LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID:
                    handleLakemedelsbehandlingAktuell(utlatande, svar, aktuell);
                    break;
                case LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID:
                    handleLakemedelsbehandlingPagatt(utlatande, svar, pagatt);
                    break;
                case LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID:
                    handleLakemedelsbehandlingEffekt(utlatande, svar, effekt);
                    break;
                case LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID:
                    handleLakemedelsbehandlingFoljsamhet(utlatande, svar, foljsamhet);
                    break;
                case LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID:
                    handleLakemedelsbehandlingAvslutad(utlatande, svar, avslutadTidpunkt, avslutadOrsak);
                    break;
                case SYMPTOM_BEDOMNING_SVAR_ID:
                    handleSymptomBedomning(utlatande, svar);
                    break;
                case SYMPTOM_PROGNOS_SVAR_ID:
                    handlePrognosTillstand(utlatande, svar);
                    break;
                case OVRIGT_OVRIGA_KOMMENTARER_SVAR_ID:
                    handleOvrigaKommentarer(utlatande, svar);
                    break;
                case BEDOMNING_UPPFYLLER_SVAR_ID:
                    handleBedomning(bedomningUppfyllerBehorighetskrav, svar);
                    break;
            }
        }

        if (!bedomningUppfyllerBehorighetskrav.isEmpty()) {
            bedomning.setUppfyllerBehorighetskrav(bedomningUppfyllerBehorighetskrav);
        }

        utlatande.setIntygAvser(IntygAvser.create(intygAvserSet));
        utlatande.setLakemedelsbehandling(Lakemedelsbehandling.create(harHaft, pagar, aktuell, pagatt, effekt, foljsamhet, avslutadTidpunkt, avslutadOrsak));
        utlatande.setBedomning(bedomning.build());
    }

    private static void handleIntygAvser(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar,
                                         EnumSet<IntygAvserKategori> intygAvserSet) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case INTYG_AVSER_DELSVAR_ID_1:
                    intygAvserSet.add(IntygAvserKategori.valueOf(IntygAvserKod.fromCode(getCVSvarContent(delsvar).getCode()).name()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleIdKontroll(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ID_KONTROLL_DELSVAR_ID_1:
                    final IdKontroll idKontroll = IdKontroll.create(IdKontrollKod.fromCode(getCVSvarContent(delsvar).getCode()));
                    utlatande.setIdKontroll(idKontroll);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleDiagnosKodad(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        utlatande.setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD));
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ALLMANT_DIAGNOSKOD_KODAD_ALLMANT_KOD_DELSVAR_ID:
                    System.out.println(delsvar.toString());

            }
        }
    }

    private static void handleDiagnosFritext(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        utlatande.setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT));
        String diagnosFritext = "";
        String diagnosArtal = "";
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ALLMANT_DIAGNOSKOD_FRITEXT_ALLMANT_FRITEXT_DELSVAR_ID:
                    diagnosFritext = getStringContent(delsvar);
                    break;
                case ALLMANT_DIAGNOSKOD_FRITEXT_ALLMANT_FRITEXT_ARTAL_DELSVAR_ID:
                    diagnosArtal = getPartialDateContent(delsvar).getValue().toString();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        utlatande.setDiagnosFritext(DiagnosFritext.create(diagnosFritext, diagnosArtal));
    }

    private static void handleLakemedelsbehandlingForekommit(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar, Boolean harHaft) {
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID:
                    harHaft = getBooleanContent(delsvar);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleLakemedelsbehandlingPagar(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar, Boolean pagar) {
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID:
                    pagar = getBooleanContent(delsvar);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleLakemedelsbehandlingAktuell(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar, String aktuell) {
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID:
                    aktuell = getStringContent(delsvar);
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleLakemedelsbehandlingPagatt(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar, Boolean pagatt) {
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID:
                    pagatt = getBooleanContent(delsvar);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleLakemedelsbehandlingEffekt(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar, Boolean effekt) {
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID:
                    effekt = getBooleanContent(delsvar);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleLakemedelsbehandlingFoljsamhet(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar, Boolean foljsamhet) {
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID:
                    foljsamhet = getBooleanContent(delsvar);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleLakemedelsbehandlingAvslutad(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar, InternalDate avslutadTidpunkt, String avslutadOrsak) {
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID:
                    avslutadTidpunkt = new InternalDate(getStringContent(delsvar));
                    break;
                case LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID:
                    avslutadOrsak = getStringContent(delsvar);
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSymptomBedomning(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case SYMPTOM_BEDOMNING_DELSVAR_ID:
                    utlatande.setBedomningAvSymptom(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handlePrognosTillstand(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case SYMPTOM_PROGNOS_DELSVAR_ID:
                    utlatande.setPrognosTillstand(PrognosTillstand.create(
                            PrognosTillstand.PrognosTillstandTyp.fromCode(getCVSvarContent(delsvar).getCode())));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleOvrigaKommentarer(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar :
                svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_ID:
                    utlatande.setOvrigaKommentarer(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBedomning(Set<Bedomning.BehorighetsTyp> bedomningUppfyllerBehorighetskrav, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case BEDOMNING_UPPFYLLER_SVAR_ID:
                    bedomningUppfyllerBehorighetskrav.add(Bedomning.BehorighetsTyp.valueOf(getCVSvarContent(delsvar).getCode()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
