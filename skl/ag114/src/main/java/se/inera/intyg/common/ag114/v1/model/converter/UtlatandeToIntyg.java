/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag114.v1.model.converter;

import com.google.common.base.Strings;
import se.inera.intyg.common.ag114.support.Ag114EntryPoint;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.ag114.v1.model.converter.InternalToTransportUtil.handleDiagnosSvar;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_9;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_1;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID_6;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID_9;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_9;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_DELSVAR_ID_5;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_ID_5;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NUVARANDE_ARBETE_DELSVAR_ID_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_ID_3;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_3;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.OVRIGT_DELSVAR_ID_8;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.OVRIGT_SVAR_ID_8;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_CODE_SYSTEM;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_DELSVAR_ID_1;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_1;
import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotNull;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(Ag114UtlatandeV1 utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, false);
        intyg.setTyp(getTypAvIntyg(utlatande));
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(Ag114UtlatandeV1 source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
        typAvIntyg.setDisplayName(Ag114EntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(Ag114UtlatandeV1 source) {
        List<Svar> svars = new ArrayList<>();

        List<Sysselsattning> sysselsattningList = new ArrayList<>();

        // Kategori 1
        int sysselsattningInstans = 1;
        if (source.getSysselsattning() != null) {
            for (Sysselsattning sysselsattning : source.getSysselsattning()) {
                if (sysselsattning.getTyp() != null) {
                    svars.add(aSvar(TYP_AV_SYSSELSATTNING_SVAR_ID_1, sysselsattningInstans++)
                            .withDelsvar(TYP_AV_SYSSELSATTNING_DELSVAR_ID_1,
                                    aCV(TYP_AV_SYSSELSATTNING_CODE_SYSTEM, sysselsattning.getTyp().getId(),
                                            sysselsattning.getTyp().getLabel()))
                            .build());
                }
            }
        }
        addIfNotBlank(svars, NUVARANDE_ARBETE_SVAR_ID_2, NUVARANDE_ARBETE_DELSVAR_ID_2, source.getNuvarandeArbete());

        // Kategori 2 Diagnos
        addIfNotNull(svars, ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_3, ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_ID_3, source.getOnskarFormedlaDiagnos());

        // Lägg endast till diagnos om önskar förmedla är true.
        if (source.getOnskarFormedlaDiagnos() != null && source.getOnskarFormedlaDiagnos()) {
            handleDiagnosSvar(svars, source.getDiagnoser());
        }

        // Kategori 3 Arbetsformaga
        addIfNotBlank(svars, NEDSATT_ARBETSFORMAGA_SVAR_ID_5, NEDSATT_ARBETSFORMAGA_DELSVAR_ID_5, source.getNedsattArbetsformaga());

        if (source.getArbetsformagaTrotsSjukdom() != null) {
            if (source.getArbetsformagaTrotsSjukdom() && !Strings.isNullOrEmpty(source.getArbetsformagaTrotsSjukdomBeskrivning())) {
                svars.add(aSvar(ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID_6).withDelsvar(ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_1,
                        source.getArbetsformagaTrotsSjukdom().toString()).withDelsvar(ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_2,
                                source.getArbetsformagaTrotsSjukdomBeskrivning())
                        .build());
            }
        }
        addIfNotBlank(svars, OVRIGT_SVAR_ID_8, OVRIGT_DELSVAR_ID_8, source.getOvrigaUpplysningar());

        // Kategori 6 Kontakt
        if (source.getKontaktMedArbetsgivaren() != null) {
            if (source.getKontaktMedArbetsgivaren() && !Strings.nullToEmpty(source.getAnledningTillKontakt()).trim().isEmpty()) {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID_9).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID_9,
                        source.getKontaktMedArbetsgivaren().toString())
                        .withDelsvar(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_9, source.getAnledningTillKontakt()).build());
            } else {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID_9).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID_9,
                        source.getKontaktMedArbetsgivaren().toString())
                        .build());
            }
        }


        // if (source.getUndersokningAvPatienten() != null) {
        // svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
        // .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
        // aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.UNDERSOKNING.transportId,
        // RespConstants.ReferensTyp.UNDERSOKNING.label))
        // .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
        // InternalConverterUtil.getInternalDateContent(source.getUndersokningAvPatienten()))
        // .build());
        // }
        // if (source.getJournaluppgifter() != null) {
        // svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
        // .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
        // aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.JOURNAL.transportId,
        // RespConstants.ReferensTyp.JOURNAL.label))
        // .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
        // InternalConverterUtil.getInternalDateContent(source.getJournaluppgifter()))
        // .build());
        // }
        // if (source.getAnhorigsBeskrivningAvPatienten() != null) {
        // svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
        // .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
        // aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.ANHORIGSBESKRIVNING.transportId,
        // RespConstants.ReferensTyp.ANHORIGSBESKRIVNING.label))
        // .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
        // InternalConverterUtil.getInternalDateContent(source.getAnhorigsBeskrivningAvPatienten()))
        // .build());
        // }
        // if (source.getAnnatGrundForMU() != null) {
        // svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
        // .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
        // aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.ANNAT.transportId,
        // RespConstants.ReferensTyp.ANNAT.label))
        // .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
        // InternalConverterUtil.getInternalDateContent(source.getAnnatGrundForMU()))
        // .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1,
        // source.getAnnatGrundForMUBeskrivning()).build());
        // }
        //
        // if (source.getKannedomOmPatient() != null) {
        // svars.add(aSvar(KANNEDOM_SVAR_ID_2)
        // .withDelsvar(KANNEDOM_DELSVAR_ID_2, InternalConverterUtil.getInternalDateContent(source.getKannedomOmPatient()))
        // .build());
        // }
        //
        // if (source.getUnderlagFinns() != null) {
        // svars.add(aSvar(UNDERLAGFINNS_SVAR_ID_3).withDelsvar(UNDERLAGFINNS_DELSVAR_ID_3,
        // source.getUnderlagFinns().toString()).build());
        // }
        //
        // int underlagInstans = 1;
        // for (Underlag underlag : source.getUnderlag()) {
        // svars.add(
        // aSvar(UNDERLAG_SVAR_ID_4, underlagInstans++)
        // .withDelsvar(UNDERLAG_TYP_DELSVAR_ID_4, underlag.getTyp() == null ? null
        // : aCV(UNDERLAG_CODE_SYSTEM, underlag.getTyp().getId(), underlag.getTyp().getLabel()))
        // .withDelsvar(UNDERLAG_DATUM_DELSVAR_ID_4, InternalConverterUtil.getInternalDateContent(underlag.getDatum()))
        // .withDelsvar(UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4, underlag.getHamtasFran()).build());
        // }
        //
        // handleDiagnosSvar(svars, source.getDiagnoser());
        //
        // addIfNotBlank(svars, FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15, FUNKTIONSNEDSATTNING_DEBUT_DELSVAR_ID_15,
        // source.getFunktionsnedsattningDebut());
        // addIfNotBlank(svars, FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16, FUNKTIONSNEDSATTNING_PAVERKAN_DELSVAR_ID_16,
        // source.getFunktionsnedsattningPaverkan());
        //
        // /* End complex object */
        //
        // addIfNotBlank(svars, OVRIGT_SVAR_ID_25, OVRIGT_DELSVAR_ID_25, buildOvrigaUpplysningar(source));
        //

        //
        // for (Tillaggsfraga tillaggsfraga : source.getTillaggsfragor()) {
        // addIfNotBlank(svars, tillaggsfraga.getId(), tillaggsfraga.getId() + ".1", tillaggsfraga.getSvar());
        // }

        return svars;
    }
}
