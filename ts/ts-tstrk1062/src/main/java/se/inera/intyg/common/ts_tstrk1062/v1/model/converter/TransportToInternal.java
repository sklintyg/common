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
import static se.inera.intyg.common.ts_tstrk1062.v1.model.converter.TSTRK1062Constants.*;
import static se.inera.intyg.common.ts_tstrk1062.v1.model.internal.PrognosTillstand.PrognosTillstandTyp;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.*;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

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
        EnumSet<IntygAvser.BehorighetsTyp> intygAvserBehorigheter = EnumSet.noneOf(IntygAvser.BehorighetsTyp.class);

        Bedomning.Builder bedomning = Bedomning.builder();
        Set<Bedomning.BehorighetsTyp> bedomningUppfyllerBehorighetskrav = EnumSet.noneOf(Bedomning.BehorighetsTyp.class);

        List<DiagnosKodad> diagnosKodadList = new ArrayList<DiagnosKodad>();

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
                handleIntygAvser(intygAvserBehorigheter, svar);
                break;
            case ID_KONTROLL_SVAR_ID_1:
                handleIdKontroll(utlatande, svar);
                break;
            case ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID:
                handleDiagnosKodad(diagnosKodadList, svar);
                break;
            case ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_ID:
                handleDiagnosFritext(utlatande, svar);
                break;
            case LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID:
                harHaft = handleLakemedelsbehandlingForekommit(utlatande, svar);
                break;
            case LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID:
                pagar = handleLakemedelsbehandlingPagar(utlatande, svar);
                break;
            case LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID:
                aktuell = handleLakemedelsbehandlingAktuell(utlatande, svar);
                break;
            case LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID:
                pagatt = handleLakemedelsbehandlingPagatt(utlatande, svar);
                break;
            case LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID:
                effekt = handleLakemedelsbehandlingEffekt(utlatande, svar);
                break;
            case LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID:
                foljsamhet = handleLakemedelsbehandlingFoljsamhet(utlatande, svar);
                break;
            case LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID:
                avslutadTidpunkt = handleLakemedelsbehandlingAvslutadTidpunkt(utlatande, svar);
                avslutadOrsak = handleLakemedelsbehandlingAvslutadOrsak(utlatande, svar);
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

        if (!intygAvserBehorigheter.isEmpty()) {
            utlatande.setIntygAvser(IntygAvser.create(intygAvserBehorigheter));
        }

        utlatande.setLakemedelsbehandling(
                Lakemedelsbehandling.create(harHaft, pagar, aktuell, pagatt, effekt, foljsamhet, avslutadTidpunkt, avslutadOrsak));

        if (!bedomningUppfyllerBehorighetskrav.isEmpty()) {
            bedomning.setUppfyllerBehorighetskrav(bedomningUppfyllerBehorighetskrav);
        }
        utlatande.setBedomning(bedomning.build());

        if (diagnosKodadList.size() > 0) {
            utlatande.setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD));
            utlatande.setDiagnosKodad(diagnosKodadList);
        }
    }

    private static void handleIntygAvser(EnumSet<IntygAvser.BehorighetsTyp> intygAvserBehorigheter, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case INTYG_AVSER_DELSVAR_ID_1:
                intygAvserBehorigheter.add(IntygAvser.BehorighetsTyp.valueOf(getCVSvarContent(delsvar).getCode()));
                break;
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
            }
        }
    }

    private static void handleDiagnosKodad(List<DiagnosKodad> diagnosKodadList, Svar svar) throws ConverterException {
        String diagnosKod = null;
        String diagnosKodSystem = null;
        String diagnosBeskrivning = null;
        String diagnosDisplayName = null;
        String diagnosArtal = null;

        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_ID:
                diagnosKod = getCVSvarContent(delsvar).getCode();
                diagnosKodSystem = getCVSvarContent(delsvar).getCodeSystem();
                diagnosDisplayName = getCVSvarContent(delsvar).getDisplayName();
                break;
            case ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_ID:
                diagnosBeskrivning = getStringContent(delsvar);
                break;
            case ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_ID:
                diagnosArtal = getPartialDateContent(delsvar).getValue().toString();
                break;
            }
        }

        if (diagnosKod != null && diagnosKodSystem != null && diagnosBeskrivning != null && diagnosArtal != null) {
            final DiagnosKodad diagnosKodad = DiagnosKodad.create(diagnosKod, diagnosKodSystem, diagnosBeskrivning, diagnosDisplayName,
                    diagnosArtal);
            diagnosKodadList.add(diagnosKodad);
        }
    }

    private static void handleDiagnosFritext(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        utlatande.setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT));
        String diagnosFritext = "";
        String diagnosArtal = "";
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_ID:
                diagnosFritext = getStringContent(delsvar);
                break;
            case ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_ID:
                diagnosArtal = getPartialDateContent(delsvar).getValue().toString();
                break;
            }
        }

        utlatande.setDiagnosFritext(DiagnosFritext.create(diagnosFritext, diagnosArtal));
    }

    private static Boolean handleLakemedelsbehandlingForekommit(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID:
                return getBooleanContent(delsvar);
            }
        }
        return null;
    }

    private static Boolean handleLakemedelsbehandlingPagar(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID:
                return getBooleanContent(delsvar);
            }
        }
        return null;
    }

    private static String handleLakemedelsbehandlingAktuell(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID:
                return getStringContent(delsvar);
            }
        }
        return null;
    }

    private static Boolean handleLakemedelsbehandlingPagatt(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID:
                return getBooleanContent(delsvar);
            }
        }
        return null;
    }

    private static Boolean handleLakemedelsbehandlingEffekt(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID:
                return getBooleanContent(delsvar);
            }
        }
        return null;
    }

    private static Boolean handleLakemedelsbehandlingFoljsamhet(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID:
                return getBooleanContent(delsvar);
            }
        }
        return null;
    }

    private static InternalDate handleLakemedelsbehandlingAvslutadTidpunkt(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar)
            throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID:
                return new InternalDate(getPartialDateContent(delsvar).getValue().toString());
            }
        }
        return null;
    }

    private static String handleLakemedelsbehandlingAvslutadOrsak(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID:
                return getStringContent(delsvar);
            }
        }
        return null;
    }

    private static void handleSymptomBedomning(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case SYMPTOM_BEDOMNING_DELSVAR_ID:
                utlatande.setBedomningAvSymptom(getStringContent(delsvar));
                break;
            }
        }
    }

    private static void handlePrognosTillstand(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case SYMPTOM_PROGNOS_DELSVAR_ID:
                if (!isStringContent(delsvar)) {
                    if (PrognosTillstandTyp.KANEJBEDOMA.getCode().equals(getCVSvarContent(delsvar).getCode())) {
                        utlatande.setPrognosTillstand(PrognosTillstand.create(PrognosTillstandTyp.KANEJBEDOMA));
                    } else {
                        throw new IllegalArgumentException("Unknown code");
                    }
                } else {
                    if (getBooleanContent(delsvar)) {
                        utlatande.setPrognosTillstand(PrognosTillstand.create(PrognosTillstandTyp.JA));
                    } else {
                        utlatande.setPrognosTillstand(PrognosTillstand.create(PrognosTillstandTyp.NEJ));
                    }
                }
                break;
            }
        }
    }

    private static void handleOvrigaKommentarer(TsTstrk1062UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_ID:
                utlatande.setOvrigaKommentarer(getStringContent(delsvar));
                break;
            }
        }
    }

    private static void handleBedomning(Set<Bedomning.BehorighetsTyp> bedomningUppfyllerBehorighetskrav, Svar svar)
            throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case BEDOMNING_UPPFYLLER_DELSVAR_ID:
                bedomningUppfyllerBehorighetskrav.add(Bedomning.BehorighetsTyp.valueOf(getCVSvarContent(delsvar).getCode()));
                break;
            }
        }
    }
}
