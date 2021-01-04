/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.v1.model.converter;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_DELSVAR_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_OM_DELSVAR_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_DELSVAR_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_DELSVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_DAGAR_TILL_ARBETE_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_DAGAR_TILL_ARBETE_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SJUKSKRIVNING_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_DELSVAR_ID_28;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aDatePeriod;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotNull;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getTypAvIntyg;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.fkparent.model.converter.InternalToTransportUtil;
import se.inera.intyg.common.fkparent.model.converter.RespConstants;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
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

    public static Intyg convert(LisjpUtlatandeV1 utlatande, WebcertModuleService webcertModuleService) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC);
        intyg.setTyp(getTypAvIntyg(KvIntygstyp.LISJP));
        intyg.getSvar().addAll(getSvar(utlatande, webcertModuleService));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static List<Svar> getSvar(LisjpUtlatandeV1 source, WebcertModuleService webcertModuleService) {
        List<Svar> svars = new ArrayList<>();

        addIfNotNull(svars, AVSTANGNING_SMITTSKYDD_SVAR_ID_27, AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27, source.getAvstangningSmittskydd());

        getGrundForMUSvar(source, svars);

        int sysselsattningInstans = 1;
        if (source.getSysselsattning() != null) {
            for (Sysselsattning sysselsattning : source.getSysselsattning()) {
                if (sysselsattning.getTyp() != null) {
                    svars.add(aSvar(TYP_AV_SYSSELSATTNING_SVAR_ID_28, sysselsattningInstans++)
                        .withDelsvar(TYP_AV_SYSSELSATTNING_DELSVAR_ID_28,
                            aCV(TYP_AV_SYSSELSATTNING_CODE_SYSTEM, sysselsattning.getTyp().getId(),
                                sysselsattning.getTyp().getLabel()))
                        .build());
                }
            }
        }

        addIfNotBlank(svars, NUVARANDE_ARBETE_SVAR_ID_29, NUVARANDE_ARBETE_DELSVAR_ID_29, source.getNuvarandeArbete());

        InternalToTransportUtil.handleDiagnosSvar(svars, source.getDiagnoser(), webcertModuleService);

        handleIcf(svars, FUNKTIONSNEDSATTNING_SVAR_ID_35, FUNKTIONSNEDSATTNING_DELSVAR_ID_35,
            source.getFunktionsnedsattning(), source.getFunktionsKategorier(),
            "Problem som påverkar patientens möjlighet att utföra sin sysselsättning:");

        handleIcf(svars, AKTIVITETSBEGRANSNING_SVAR_ID_17, AKTIVITETSBEGRANSNING_DELSVAR_ID_17,
            source.getAktivitetsbegransning(), source.getAktivitetsKategorier(),
            "Svårigheter som påverkar patientens sysselsättning:");

        addIfNotBlank(svars, PAGAENDEBEHANDLING_SVAR_ID_19, PAGAENDEBEHANDLING_DELSVAR_ID_19, source.getPagaendeBehandling());
        addIfNotBlank(svars, PLANERADBEHANDLING_SVAR_ID_20, PLANERADBEHANDLING_DELSVAR_ID_20, source.getPlaneradBehandling());

        int sjukskrivningInstans = 1;
        for (Sjukskrivning sjukskrivning : source.getSjukskrivningar()) {
            if (sjukskrivning.getPeriod() != null && sjukskrivning.getPeriod().isValid()) {
                svars.add(aSvar(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, sjukskrivningInstans++)
                    .withDelsvar(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32,
                        aCV(SJUKSKRIVNING_CODE_SYSTEM, sjukskrivning.getSjukskrivningsgrad().getId(),
                            sjukskrivning.getSjukskrivningsgrad().getLabel()))
                    .withDelsvar(BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32,
                        aDatePeriod(sjukskrivning.getPeriod().fromAsLocalDate(), sjukskrivning.getPeriod().tomAsLocalDate()))
                    .build());
            }
        }

        addIfNotBlank(svars, FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37, FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID_37,
            source.getForsakringsmedicinsktBeslutsstod());

        if (source.getArbetstidsforlaggning() != null) {
            if (source.getArbetstidsforlaggning() && !Strings.nullToEmpty(source.getArbetstidsforlaggningMotivering()).trim().isEmpty()) {
                svars.add(aSvar(ARBETSTIDSFORLAGGNING_SVAR_ID_33)
                    .withDelsvar(ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33, source.getArbetstidsforlaggning().toString())
                    .withDelsvar(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33, source.getArbetstidsforlaggningMotivering()).build());
            } else {
                svars.add(aSvar(ARBETSTIDSFORLAGGNING_SVAR_ID_33).withDelsvar(ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33,
                    source.getArbetstidsforlaggning().toString()).build());
            }
        }

        if (source.getArbetsresor() != null) {
            svars.add(aSvar(ARBETSRESOR_SVAR_ID_34).withDelsvar(ARBETSRESOR_OM_DELSVAR_ID_34, source.getArbetsresor().toString()).build());
        }

        if (source.getPrognos() != null && source.getPrognos().getTyp() != null) {
            if (source.getPrognos().getDagarTillArbete() != null) {
                svars.add(aSvar(PROGNOS_SVAR_ID_39).withDelsvar(PROGNOS_BESKRIVNING_DELSVAR_ID_39,
                    aCV(PROGNOS_CODE_SYSTEM, source.getPrognos().getTyp().getId(),
                        source.getPrognos().getTyp().getLabel()))
                    .withDelsvar(PROGNOS_DAGAR_TILL_ARBETE_DELSVAR_ID_39,
                        aCV(PROGNOS_DAGAR_TILL_ARBETE_CODE_SYSTEM, source.getPrognos().getDagarTillArbete().getId(),
                            source.getPrognos().getDagarTillArbete().getLabel()))
                    .build());
            } else {
                svars.add(aSvar(PROGNOS_SVAR_ID_39).withDelsvar(PROGNOS_BESKRIVNING_DELSVAR_ID_39,
                    aCV(PROGNOS_CODE_SYSTEM, source.getPrognos().getTyp().getId(),
                        source.getPrognos().getTyp().getLabel()))
                    .build());
            }
        }

        int arbetslivsinriktadeAtgarderInstans = 1;
        for (ArbetslivsinriktadeAtgarder atgarder : source.getArbetslivsinriktadeAtgarder()) {
            svars.add(aSvar(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, arbetslivsinriktadeAtgarderInstans++)
                .withDelsvar(ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40,
                    aCV(ARBETSLIVSINRIKTADE_ATGARDER_CODE_SYSTEM, atgarder.getTyp().getId(), atgarder.getTyp().getLabel()))
                .build());
        }

        addIfNotBlank(svars, ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44, ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_DELSVAR_ID_44,
            source.getArbetslivsinriktadeAtgarderBeskrivning());

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

    private static void getGrundForMUSvar(LisjpUtlatandeV1 source, List<Svar> svars) {
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

        if (source.getTelefonkontaktMedPatienten() != null && source.getTelefonkontaktMedPatienten().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.TELEFONKONTAKT.transportId,
                        RespConstants.ReferensTyp.TELEFONKONTAKT.label))
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
                    InternalConverterUtil.getInternalDateContent(source.getTelefonkontaktMedPatienten()))
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

        if (source.getAnnatGrundForMU() != null && source.getAnnatGrundForMU().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.ANNAT.transportId,
                        RespConstants.ReferensTyp.ANNAT.label))
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
                    InternalConverterUtil.getInternalDateContent(source.getAnnatGrundForMU()))
                .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1, source.getAnnatGrundForMUBeskrivning()).build());
        }
    }

    // Original taken and then modified from luse/../UtlatandeToIntyg.java, INTYG-3024
    private static String buildOvrigaUpplysningar(LisjpUtlatandeV1 source) {
        String motiveringTillInteBaseratPaUndersokning = null;
        String motiveringTillTidigSjukskrivning = null;
        String ovrigt = null;

        // Since INTYG-2949, we have to concatenate information in the Övrigt-fält again...
        if (!Strings.nullToEmpty(source.getMotiveringTillInteBaseratPaUndersokning()).trim().isEmpty()) {
            motiveringTillInteBaseratPaUndersokning = "Motivering till varför utlåtandet inte baseras på undersökning av patienten: "
                + source.getMotiveringTillInteBaseratPaUndersokning();
        }

        // INTYG-3207 motiveringTillTidigtStartdatumForSjukskrivning
        if (!Strings.nullToEmpty(source.getMotiveringTillTidigtStartdatumForSjukskrivning()).trim().isEmpty()) {
            motiveringTillTidigSjukskrivning = "Orsak för att starta perioden mer än 7 dagar bakåt i tiden: "
                + source.getMotiveringTillTidigtStartdatumForSjukskrivning();
        }

        if (!Strings.nullToEmpty(source.getOvrigt()).trim().isEmpty()) {
            ovrigt = source.getOvrigt();
        }

        String ret = Joiner.on("\n").skipNulls().join(motiveringTillInteBaseratPaUndersokning, motiveringTillTidigSjukskrivning, ovrigt);
        return !ret.trim().isEmpty() ? ret : null;
    }

    private static void handleIcf(List<Svar> svars, String svarsId, String delsvarsId, String content, List<String> aktiviteter,
        String icfHeader) {
        if (!Strings.nullToEmpty(content).trim().isEmpty()) {
            if (aktiviteter != null && aktiviteter.size() > 0) {
                StringBuilder icfContent = new StringBuilder();
                icfContent.append(icfHeader + "\n");
                for (String aktivitet : aktiviteter) {
                    icfContent.append(aktivitet);
                    if (aktiviteter.indexOf(aktivitet) != aktiviteter.size() - 1) {
                        icfContent.append(" - ");
                    }
                }
                icfContent.append("\n\n" + content);
                svars.add(aSvar(svarsId).withDelsvar(delsvarsId, icfContent.toString()).build());
            } else {
                svars.add(aSvar(svarsId).withDelsvar(delsvarsId, content).build());
            }
        }
    }

}
