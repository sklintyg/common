/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.luae_na.v1.model.converter;

import static se.inera.intyg.common.fkparent.model.converter.InternalToTransportUtil.handleDiagnosSvar;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSLUTADBEHANDLING_DELSVAR_ID_18;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSLUTADBEHANDLING_SVAR_ID_18;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSGRUND_DELSVAR_ID_7;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_45;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSGRUND_SVAR_ID_7;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_ID_45;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORMAGATROTSBEGRANSNING_DELSVAR_ID_23;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORMAGATROTSBEGRANSNING_SVAR_ID_23;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSLAG_TILL_ATGARD_DELSVAR_ID_24;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSLAG_TILL_ATGARD_SVAR_ID_24;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID_14;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID_13;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID_8;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID_9;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID_11;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KANNEDOM_DELSVAR_ID_2;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KANNEDOM_SVAR_ID_2;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID_22;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NYDIAGNOS_SVAR_ID_45;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_DELSVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SJUKDOMSFORLOPP_DELSVAR_ID_5;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SJUKDOMSFORLOPP_SVAR_ID_5;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SUBSTANSINTAG_DELSVAR_ID_21;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SUBSTANSINTAG_SVAR_ID_21;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_DELSVAR_ID_3;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_DATUM_DELSVAR_ID_4;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_SVAR_ID_4;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_TYP_DELSVAR_ID_4;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getTypAvIntyg;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.fkparent.model.converter.RespConstants;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.luae_na.v1.model.internal.LuaenaUtlatandeV1;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(LuaenaUtlatandeV1 utlatande, WebcertModuleService webcertModuleService) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC);
        intyg.setTyp(getTypAvIntyg(KvIntygstyp.LUAE_NA));
        intyg.getSvar().addAll(getSvar(utlatande, webcertModuleService));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }


    private static List<Svar> getSvar(LuaenaUtlatandeV1 source, WebcertModuleService webcertModuleService) {
        List<Svar> svars = new ArrayList<>();

        int grundForMUInstans = 1;
        if (source.getUndersokningAvPatienten() != null && source.getUndersokningAvPatienten().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.UNDERSOKNING.transportId,
                        RespConstants.ReferensTyp.UNDERSOKNING.label))
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
                    InternalConverterUtil.getInternalDateContent(source.getUndersokningAvPatienten()))
                .build());
        }
        if (source.getJournaluppgifter() != null && source.getJournaluppgifter().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.JOURNAL.transportId,
                        RespConstants.ReferensTyp.JOURNAL.label))
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
                    InternalConverterUtil.getInternalDateContent(source.getJournaluppgifter()))
                .build());
        }
        if (source.getAnhorigsBeskrivningAvPatienten() != null && source.getAnhorigsBeskrivningAvPatienten().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.ANHORIGSBESKRIVNING.transportId,
                        RespConstants.ReferensTyp.ANHORIGSBESKRIVNING.label))
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
                    InternalConverterUtil.getInternalDateContent(source.getAnhorigsBeskrivningAvPatienten()))
                .build());
        }
        if (source.getAnnatGrundForMU() != null && source.getAnnatGrundForMU().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.ANNAT.transportId,
                        RespConstants.ReferensTyp.ANNAT.label))
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
                    InternalConverterUtil.getInternalDateContent(source.getAnnatGrundForMU()))
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1, source.getAnnatGrundForMUBeskrivning()).build());
        }

        if (source.getKannedomOmPatient() != null && source.getKannedomOmPatient().isValidDate()) {
            svars.add(aSvar(KANNEDOM_SVAR_ID_2).withDelsvar(KANNEDOM_DELSVAR_ID_2,
                InternalConverterUtil.getInternalDateContent(source.getKannedomOmPatient()))
                .build());
        }

        if (source.getUnderlagFinns() != null) {
            svars.add(aSvar(UNDERLAGFINNS_SVAR_ID_3).withDelsvar(UNDERLAGFINNS_DELSVAR_ID_3, source.getUnderlagFinns().toString()).build());
        }

        int underlagInstans = 1;
        for (Underlag underlag : source.getUnderlag()) {
            if (underlag.getDatum() != null && underlag.getDatum().isValidDate()) {
                svars.add(
                    aSvar(UNDERLAG_SVAR_ID_4, underlagInstans++)
                        .withDelsvar(UNDERLAG_TYP_DELSVAR_ID_4, underlag.getTyp() == null ? null
                            : aCV(UNDERLAG_CODE_SYSTEM, underlag.getTyp().getId(), underlag.getTyp().getLabel()))
                        .withDelsvar(UNDERLAG_DATUM_DELSVAR_ID_4, InternalConverterUtil.getInternalDateContent(underlag.getDatum()))
                        .withDelsvar(UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4, underlag.getHamtasFran()).build());
            }
        }

        addIfNotBlank(svars, SJUKDOMSFORLOPP_SVAR_ID_5, SJUKDOMSFORLOPP_DELSVAR_ID_5, source.getSjukdomsforlopp());

        handleDiagnosSvar(svars, source.getDiagnoser(), webcertModuleService);

        if (source.getDiagnosgrund() != null) {
            svars.add(aSvar(DIAGNOSGRUND_SVAR_ID_7).withDelsvar(DIAGNOSGRUND_DELSVAR_ID_7, source.getDiagnosgrund()).build());
        }

        if (source.getNyBedomningDiagnosgrund() != null) {
            if (source.getNyBedomningDiagnosgrund()) {
                svars.add(
                    aSvar(NYDIAGNOS_SVAR_ID_45)
                        .withDelsvar(DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_45, source.getNyBedomningDiagnosgrund().toString())
                        .withDelsvar(DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_ID_45, source.getDiagnosForNyBedomning())
                        .build());
            } else {
                svars.add(aSvar(NYDIAGNOS_SVAR_ID_45)
                    .withDelsvar(DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_45, Boolean.FALSE.toString()).build());
            }
        }

        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8, FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID_8,
            source.getFunktionsnedsattningIntellektuell());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9, FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID_9,
            source.getFunktionsnedsattningKommunikation());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10, FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10,
            source.getFunktionsnedsattningKoncentration());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11, FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID_11,
            source.getFunktionsnedsattningPsykisk());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12, FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12,
            source.getFunktionsnedsattningSynHorselTal());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13, FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID_13,
            source.getFunktionsnedsattningBalansKoordination());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14, FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID_14,
            source.getFunktionsnedsattningAnnan());
        addIfNotBlank(svars, AKTIVITETSBEGRANSNING_SVAR_ID_17, AKTIVITETSBEGRANSNING_DELSVAR_ID_17, source.getAktivitetsbegransning());
        addIfNotBlank(svars, AVSLUTADBEHANDLING_SVAR_ID_18, AVSLUTADBEHANDLING_DELSVAR_ID_18, source.getAvslutadBehandling());
        addIfNotBlank(svars, PAGAENDEBEHANDLING_SVAR_ID_19, PAGAENDEBEHANDLING_DELSVAR_ID_19, source.getPagaendeBehandling());
        addIfNotBlank(svars, PLANERADBEHANDLING_SVAR_ID_20, PLANERADBEHANDLING_DELSVAR_ID_20, source.getPlaneradBehandling());
        addIfNotBlank(svars, SUBSTANSINTAG_SVAR_ID_21, SUBSTANSINTAG_DELSVAR_ID_21, source.getSubstansintag());
        addIfNotBlank(svars, MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22, MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID_22,
            source.getMedicinskaForutsattningarForArbete());
        addIfNotBlank(svars, FORMAGATROTSBEGRANSNING_SVAR_ID_23, FORMAGATROTSBEGRANSNING_DELSVAR_ID_23,
            source.getFormagaTrotsBegransning());
        addIfNotBlank(svars, FORSLAG_TILL_ATGARD_SVAR_ID_24, FORSLAG_TILL_ATGARD_DELSVAR_ID_24, source.getForslagTillAtgard());
        addIfNotBlank(svars, OVRIGT_SVAR_ID_25, OVRIGT_DELSVAR_ID_25, buildOvrigaUpplysningar(source));

        if (source.getKontaktMedFk() != null) {
            if (source.getKontaktMedFk() && !Strings.nullToEmpty(source.getAnledningTillKontakt()).trim().isEmpty()) {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID_26).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID_26, source.getKontaktMedFk().toString())
                    .withDelsvar(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26, source.getAnledningTillKontakt()).build());
            } else {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID_26).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID_26, source.getKontaktMedFk().toString())
                    .build());
            }
        }

        for (Tillaggsfraga tillaggsfraga : source.getTillaggsfragor()) {
            addIfNotBlank(svars, tillaggsfraga.getId(), tillaggsfraga.getId() + ".1", tillaggsfraga.getSvar());
        }

        return svars;
    }

    // Original taken and then modified from luse/../UtlatandeToIntyg.java, INTYG-3024
    private static String buildOvrigaUpplysningar(LuaenaUtlatandeV1 source) {
        String motiveringTillInteBaseratPaUndersokning = null;
        String ovrigt = null;

        // Since INTYG-2949, we have to concatenate information in the Övrigt-fält again...
        if (!Strings.nullToEmpty(source.getMotiveringTillInteBaseratPaUndersokning()).trim().isEmpty()) {
            motiveringTillInteBaseratPaUndersokning = "Motivering till varför utlåtandet inte baseras på undersökning av patienten: "
                + source.getMotiveringTillInteBaseratPaUndersokning();
        }

        if (!Strings.nullToEmpty(source.getOvrigt()).trim().isEmpty()) {
            ovrigt = source.getOvrigt();
        }

        String ret = Joiner.on("\n").skipNulls().join(motiveringTillInteBaseratPaUndersokning, ovrigt);
        return !ret.trim().isEmpty() ? ret : null;
    }

}
