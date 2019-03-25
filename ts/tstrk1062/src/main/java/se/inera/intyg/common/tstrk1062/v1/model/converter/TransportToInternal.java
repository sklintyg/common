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
package se.inera.intyg.common.tstrk1062.v1.model.converter;

import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getBooleanContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getPartialDateContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.isStringContent;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.BEDOMNING_UPPFYLLER_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.BEDOMNING_UPPFYLLER_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ID_KONTROLL_DELSVAR_ID_1;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ID_KONTROLL_SVAR_ID_1;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.OVRIGT_OVRIGA_KOMMENTARER_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.SYMPTOM_BEDOMNING_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.SYMPTOM_BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.SYMPTOM_PROGNOS_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.SYMPTOM_PROGNOS_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.internal.PrognosTillstand.PrognosTillstandTyp;

import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.tstrk1062.v1.model.internal.Bedomning;
import se.inera.intyg.common.tstrk1062.v1.model.internal.DiagnosFritext;
import se.inera.intyg.common.tstrk1062.v1.model.internal.DiagnosKodad;
import se.inera.intyg.common.tstrk1062.v1.model.internal.DiagnosRegistrering;
import se.inera.intyg.common.tstrk1062.v1.model.internal.IdKontroll;
import se.inera.intyg.common.tstrk1062.v1.model.internal.IntygAvser;
import se.inera.intyg.common.tstrk1062.v1.model.internal.Lakemedelsbehandling;
import se.inera.intyg.common.tstrk1062.v1.model.internal.PrognosTillstand;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static TsTrk1062UtlatandeV1 convert(Intyg source) throws ConverterException {
        final TsTrk1062UtlatandeV1.Builder utlatande = TsTrk1062UtlatandeV1.builder();

        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setTextVersion(source.getVersion());

        final GrundData grundData = TransportConverterUtil.getGrundData(source, false);
        utlatande.setGrundData(grundData);

        final String signature = TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift());
        utlatande.setSignature(signature);

        setSvar(utlatande, source);

        return utlatande.build();
    }

    private static void setSvar(TsTrk1062UtlatandeV1.Builder utlatande, Intyg source) throws ConverterException {
        final EnumSet<IntygAvser.BehorighetsTyp> intygAvserList = EnumSet.noneOf(IntygAvser.BehorighetsTyp.class);
        IdKontroll idKontroll = null;
        final List<DiagnosKodad> diagnosKodadList = new ArrayList<>();
        DiagnosFritext diagnosFritext = null;
        Boolean harHaft = null;
        Boolean pagar = null;
        String aktuell = null;
        Boolean pagatt = null;
        Boolean effekt = null;
        Boolean foljsamhet = null;
        String avslutadTidpunkt = null;
        String avslutadOrsak = null;
        String bedomningAvSymptom = null;
        PrognosTillstand prognosTillstand = null;
        String ovrigaKommentarer = null;
        final Set<Bedomning.BehorighetsTyp> bedomningUppfyllerBehorighetskrav = EnumSet.noneOf(Bedomning.BehorighetsTyp.class);

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
            case INTYG_AVSER_SVAR_ID_1:
                final IntygAvser.BehorighetsTyp intygAvser = getIntygAvser(svar.getDelsvar());
                if (intygAvser != null) {
                    intygAvserList.add(intygAvser);
                }
                break;
            case ID_KONTROLL_SVAR_ID_1:
                idKontroll = getIdKontroll(svar);
                break;
            case ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID:
                final DiagnosKodad diagnosKodad = getDiagnosKodad(svar.getDelsvar());
                if (diagnosKodad != null) {
                    diagnosKodadList.add(diagnosKodad);
                }
                break;
            case ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_ID:
                diagnosFritext = getDiagnosFritext(svar.getDelsvar());
                break;
            case LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID:
                harHaft = getLakemedelsbehandlingForekommit(svar);
                break;
            case LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID:
                pagar = getLakemedelsbehandlingPagar(svar);
                break;
            case LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID:
                aktuell = getLakemedelsbehandlingAktuell(svar);
                break;
            case LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID:
                pagatt = getLakemedelsbehandlingPagatt(svar);
                break;
            case LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID:
                effekt = getLakemedelsbehandlingEffekt(svar);
                break;
            case LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID:
                foljsamhet = getLakemedelsbehandlingFoljsamhet(svar);
                break;
            case LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID:
                avslutadTidpunkt = getLakemedelsbehandlingAvslutadTidpunkt(svar);
                avslutadOrsak = getLakemedelsbehandlingAvslutadOrsak(svar);
                break;
            case SYMPTOM_BEDOMNING_SVAR_ID:
                bedomningAvSymptom = getSymptomBedomning(svar.getDelsvar());
                break;
            case SYMPTOM_PROGNOS_SVAR_ID:
                prognosTillstand = getPrognosTillstand(svar.getDelsvar());
                break;
            case OVRIGT_OVRIGA_KOMMENTARER_SVAR_ID:
                ovrigaKommentarer = getOvrigaKommentarer(svar.getDelsvar());
                break;
            case BEDOMNING_UPPFYLLER_SVAR_ID:
                final Bedomning.BehorighetsTyp bedomningsTyp = getBehorighetsTyp(svar.getDelsvar());
                if (bedomningsTyp != null) {
                    bedomningUppfyllerBehorighetskrav.add(bedomningsTyp);
                }
                break;
            }
        }

        if (intygAvserList.size() > 0) {
            utlatande.setIntygAvser(IntygAvser.create(intygAvserList));
        }

        if (idKontroll != null) {
            utlatande.setIdKontroll(idKontroll);
        }

        if (diagnosKodadList.size() > 0) {
            utlatande.setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD));
            utlatande.setDiagnosKodad(diagnosKodadList);
        } else if (diagnosFritext != null) {
            utlatande.setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT));
            utlatande.setDiagnosFritext(diagnosFritext);
        }

        if (harHaft != null) {
            utlatande.setLakemedelsbehandling(
                    Lakemedelsbehandling.create(harHaft, pagar, aktuell, pagatt, effekt, foljsamhet, avslutadTidpunkt, avslutadOrsak));
        }

        if (bedomningAvSymptom != null) {
            utlatande.setBedomningAvSymptom(bedomningAvSymptom);
        }

        if (prognosTillstand != null) {
            utlatande.setPrognosTillstand(prognosTillstand);
        }

        if (ovrigaKommentarer != null) {
            utlatande.setOvrigaKommentarer(ovrigaKommentarer);
        }

        if (bedomningUppfyllerBehorighetskrav.size() > 0) {
            final Bedomning.Builder bedomning = Bedomning.builder();
            bedomning.setUppfyllerBehorighetskrav(bedomningUppfyllerBehorighetskrav);
            utlatande.setBedomning(bedomning.build());
        }
    }

    private static IntygAvser.BehorighetsTyp getIntygAvser(List<Delsvar> delsvarList) throws ConverterException {
        IntygAvser.BehorighetsTyp intygAvser = null;

        final CVType cvTypeValue = getCVValue(delsvarList, INTYG_AVSER_DELSVAR_ID_1);
        if (cvTypeValue != null) {
            intygAvser = IntygAvser.BehorighetsTyp.valueOf(cvTypeValue.getCode());
        }

        return intygAvser;
    }

    private static IdKontroll getIdKontroll(Svar svar) throws ConverterException {
        IdKontroll idKontroll = null;
        final CVType cvTypeValue = getCVValue(svar.getDelsvar(), ID_KONTROLL_DELSVAR_ID_1);
        if (cvTypeValue != null) {
            idKontroll = IdKontroll.create(IdKontrollKod.fromCode(cvTypeValue.getCode()));
        }
        return idKontroll;
    }

    private static DiagnosKodad getDiagnosKodad(List<Delsvar> delsvarList) throws ConverterException {
        DiagnosKodad diagnosKodad = null;

        String diagnosKod = null;
        String diagnosKodSystem = null;
        String diagnosDisplayName = null;
        final CVType diagnosCVValue = getCVValue(delsvarList, ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_ID);
        if (diagnosCVValue != null) {
            diagnosKod = diagnosCVValue.getCode();
            diagnosKodSystem = Diagnoskodverk.getEnumByCodeSystem(diagnosCVValue.getCodeSystem()).toString();
            diagnosDisplayName = diagnosCVValue.getDisplayName();
        }

        final String diagnosBeskrivning = getStringValue(delsvarList, ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_ID);

        String diagnosArtal = null;
        final InternalDate partialDateArtal = getPartialDateValue(delsvarList, ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_ID);
        if (partialDateArtal != null) {
            diagnosArtal = partialDateArtal.toString();
        }

        if (diagnosKod != null && diagnosKodSystem != null && diagnosBeskrivning != null && diagnosArtal != null) {
            diagnosKodad = DiagnosKodad.create(diagnosKod, diagnosKodSystem, diagnosBeskrivning, diagnosDisplayName,
                    diagnosArtal);
        }

        return diagnosKodad;
    }

    private static DiagnosFritext getDiagnosFritext(List<Delsvar> delsvarList) throws ConverterException {
        String diagnosText = getStringValue(delsvarList, ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_ID);

        String diagnosArtal = null;
        final InternalDate partialDateArtal = getPartialDateValue(delsvarList, ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_ID);
        if (partialDateArtal != null) {
            diagnosArtal = partialDateArtal.toString();
        }

        DiagnosFritext diagnosFritext = null;
        if (diagnosText != null && diagnosArtal != null) {
            diagnosFritext = DiagnosFritext.create(diagnosText, diagnosArtal);
        }

        return diagnosFritext;
    }

    private static Boolean getLakemedelsbehandlingForekommit(Svar svar) {
        return getBooleanValue(svar.getDelsvar(), LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID);
    }

    private static Boolean getLakemedelsbehandlingPagar(Svar svar) {
        return getBooleanValue(svar.getDelsvar(), LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID);
    }

    private static String getLakemedelsbehandlingAktuell(Svar svar) {
        return getStringValue(svar.getDelsvar(), LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID);
    }

    private static Boolean getLakemedelsbehandlingPagatt(Svar svar) {
        return getBooleanValue(svar.getDelsvar(), LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID);
    }

    private static Boolean getLakemedelsbehandlingEffekt(Svar svar) {
        return getBooleanValue(svar.getDelsvar(), LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID);
    }

    private static Boolean getLakemedelsbehandlingFoljsamhet(Svar svar) {
        return getBooleanValue(svar.getDelsvar(), LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID);
    }

    private static String getLakemedelsbehandlingAvslutadTidpunkt(Svar svar) {
        return getStringValue(svar.getDelsvar(), LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID);
    }

    private static String getLakemedelsbehandlingAvslutadOrsak(Svar svar) {
        return getStringValue(svar.getDelsvar(), LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID);
    }

    private static String getSymptomBedomning(List<Delsvar> delsvarList) {
        return getStringValue(delsvarList, SYMPTOM_BEDOMNING_DELSVAR_ID);
    }

    private static PrognosTillstand getPrognosTillstand(List<Delsvar> delsvarList) throws ConverterException {
        PrognosTillstandTyp prognosTillstandTyp = null;

        for (Delsvar delsvar : delsvarList) {
            if (delsvar.getId().equalsIgnoreCase(SYMPTOM_PROGNOS_DELSVAR_ID)) {
                if (!isStringContent(delsvar)) {
                    if (PrognosTillstandTyp.KANEJBEDOMA.getCode().equals(getCVSvarContent(delsvar).getCode())) {
                        prognosTillstandTyp = PrognosTillstandTyp.KANEJBEDOMA;
                    }
                } else {
                    if (getBooleanContent(delsvar)) {
                        prognosTillstandTyp = PrognosTillstandTyp.JA;
                    } else {
                        prognosTillstandTyp = PrognosTillstandTyp.NEJ;
                    }
                }
            }
        }

        PrognosTillstand prognosTillstand = null;
        if (prognosTillstandTyp != null) {
            prognosTillstand = PrognosTillstand.create(prognosTillstandTyp);
        }

        return prognosTillstand;
    }

    private static String getOvrigaKommentarer(List<Delsvar> delsvarList) {
        return getStringValue(delsvarList, OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_ID);
    }

    private static Bedomning.BehorighetsTyp getBehorighetsTyp(List<Delsvar> delsvarList) throws ConverterException {
        Bedomning.BehorighetsTyp behorighetsTyp = null;

        final CVType behorighetsTypCVValue = getCVValue(delsvarList, BEDOMNING_UPPFYLLER_DELSVAR_ID);
        if (behorighetsTypCVValue != null) {
            behorighetsTyp = Bedomning.BehorighetsTyp.valueOf(behorighetsTypCVValue.getCode());
        }

        return behorighetsTyp;
    }

    private static Boolean getBooleanValue(List<Delsvar> delsvarList, String delsvarId) {
        Boolean booleanValue = null;
        for (Delsvar delsvar : delsvarList) {
            if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                booleanValue = getBooleanContent(delsvar);
            }
        }
        return booleanValue;
    }

    private static String getStringValue(List<Delsvar> delsvarList, String delsvarId) {
        String stringValue = null;
        for (Delsvar delsvar : delsvarList) {
            if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                stringValue = getStringContent(delsvar);
            }
        }
        return stringValue;
    }

    private static InternalDate getPartialDateValue(List<Delsvar> delsvarList, String delsvarId) throws ConverterException {
        InternalDate partialDateValue = null;
        for (Delsvar delsvar : delsvarList) {
            if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                partialDateValue = new InternalDate(getPartialDateContent(delsvar).getValue().toString());
            }
        }
        return partialDateValue;
    }

    private static CVType getCVValue(List<Delsvar> delsvarList, String delsvarId) throws ConverterException {
        CVType cvTypeValue = null;
        for (Delsvar delsvar : delsvarList) {
            if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                cvTypeValue = getCVSvarContent(delsvar);
            }
        }
        return cvTypeValue;
    }
}
