/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.luae_fs.v1.model.converter;

import static se.inera.intyg.common.fkparent.model.converter.InternalToTransportUtil.handleDiagnosSvar;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DEBUT_DELSVAR_ID_15;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PAVERKAN_DELSVAR_ID_16;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KANNEDOM_DELSVAR_ID_2;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KANNEDOM_SVAR_ID_2;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID_26;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.OVRIGT_DELSVAR_ID_25;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAGFINNS_DELSVAR_ID_3;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_CODE_SYSTEM;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_DATUM_DELSVAR_ID_4;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_SVAR_ID_4;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_TYP_DELSVAR_ID_4;
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
import se.inera.intyg.common.luae_fs.v1.model.internal.LuaefsUtlatandeV1;
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

    public static Intyg convert(LuaefsUtlatandeV1 utlatande, WebcertModuleService webcertModuleService) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC);
        intyg.setTyp(getTypAvIntyg(KvIntygstyp.LUAE_FS));
        intyg.getSvar().addAll(getSvar(utlatande, webcertModuleService));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static List<Svar> getSvar(LuaefsUtlatandeV1 source, WebcertModuleService webcertModuleService) {
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
            svars.add(aSvar(KANNEDOM_SVAR_ID_2)
                .withDelsvar(KANNEDOM_DELSVAR_ID_2, InternalConverterUtil.getInternalDateContent(source.getKannedomOmPatient()))
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

        handleDiagnosSvar(svars, source.getDiagnoser(), webcertModuleService);

        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15, FUNKTIONSNEDSATTNING_DEBUT_DELSVAR_ID_15,
            source.getFunktionsnedsattningDebut());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16, FUNKTIONSNEDSATTNING_PAVERKAN_DELSVAR_ID_16,
            source.getFunktionsnedsattningPaverkan());

        /* End complex object */

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
    private static String buildOvrigaUpplysningar(LuaefsUtlatandeV1 source) {
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
