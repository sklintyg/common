/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotNull;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.base64StringToUnderskriftType;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getInternalDateContent;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getIntyg;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getTypAvIntyg;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.NOT_AVAILABLE;
import static se.inera.intyg.common.ts_parent.model.converter.InternalToTransportUtil.getVersion;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.ANMALAN_AVSER_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.ANMALAN_AVSER_SVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.INFORMATION_OM_TS_BESLUT_ONSKAS_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.INFORMATION_OM_TS_BESLUT_ONSKAS_SVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.INTYGET_AVSER_BEHORIGHET_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.INTYGET_AVSER_BEHORIGHET_SVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.KV_ID_KONTROLL_KODSYSTEM;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.KV_KORKORTSBEHORIGHET_KODSYSTEM;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.KV_KORKORTSOLAMPLIGHET_KODSYSTEM;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.MEDICINSKA_FORHALLANDEN_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.MEDICINSKA_FORHALLANDEN_SVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.SENASTE_UNDERSOKNINGSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.SENASTE_UNDERSOKNINGSDATUM_SVAR_ID;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.tstrk1009.v1.model.internal.KorkortBehorighetGrupp;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public final class UtlatandeToIntyg {

    private static final String DEFAULT_VERSION = "1.0";

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(Tstrk1009UtlatandeV1 utlatande) {
        Intyg intyg = getIntyg(utlatande, false);

        complementArbetsplatskodIfMissing(intyg);

        intyg.setTyp(getTypAvIntyg(KvIntygstyp.TSTRK1009));
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setVersion(getVersion(utlatande).orElse(DEFAULT_VERSION));
        intyg.setUnderskrift(base64StringToUnderskriftType(utlatande));

        return intyg;
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

    private static List<Svar> getSvar(final Tstrk1009UtlatandeV1 utlatande) {
        List<Svar> svarList = Lists.newArrayList();

        if (nonNull(utlatande.getIdentitetStyrktGenom())) {
            svarList.add(aSvar(IDENTITET_STYRKT_GENOM_SVAR_ID)
                .withDelsvar(IDENTITET_STYRKT_GENOM_DELSVAR_ID,
                    aCV(KV_ID_KONTROLL_KODSYSTEM,
                        utlatande.getIdentitetStyrktGenom().getTyp().getCode(), utlatande.getIdentitetStyrktGenom().getTyp()
                            .getDescription()))
                .build());
        }

        if (nonNull(utlatande.getAnmalanAvser())) {
            svarList.add(aSvar(ANMALAN_AVSER_SVAR_ID)
                .withDelsvar(ANMALAN_AVSER_DELSVAR_ID,
                    aCV(KV_KORKORTSOLAMPLIGHET_KODSYSTEM,
                        utlatande.getAnmalanAvser().getTyp().getCode(), utlatande.getAnmalanAvser().getTyp().getDescription()))
                .build());
        }

        addIfNotBlank(svarList, MEDICINSKA_FORHALLANDEN_SVAR_ID, MEDICINSKA_FORHALLANDEN_DELSVAR_ID, utlatande.getMedicinskaForhallanden());

        if (nonNull(utlatande.getSenasteUndersokningsdatum()) && utlatande.getSenasteUndersokningsdatum().isValidDate()) {
            svarList.add(aSvar(SENASTE_UNDERSOKNINGSDATUM_SVAR_ID)
                .withDelsvar(SENASTE_UNDERSOKNINGSDATUM_DELSVAR_ID,
                    getInternalDateContent(utlatande.getSenasteUndersokningsdatum()))
                .build());
        }

        int intygetAvserBehorigheterInstans = 1;
        if (nonNull(utlatande.getIntygetAvserBehorigheter())
            && isNotEmpty(utlatande.getIntygetAvserBehorigheter().getTyper())) {
            for (final KorkortBehorighetGrupp behorighetsGrupp : utlatande.getIntygetAvserBehorigheter().getTyper()) {
                for (final Korkortsbehorighet korkortsbehorighet : behorighetsGrupp.getKorkortsbehorigheter()) {
                    svarList.add(aSvar(INTYGET_AVSER_BEHORIGHET_SVAR_ID, intygetAvserBehorigheterInstans++)
                        .withDelsvar(INTYGET_AVSER_BEHORIGHET_DELSVAR_ID,
                            aCV(KV_KORKORTSBEHORIGHET_KODSYSTEM, korkortsbehorighet.getCode(), korkortsbehorighet.getValue()))
                        .build());
                }
            }
        }

        addIfNotNull(svarList, INFORMATION_OM_TS_BESLUT_ONSKAS_SVAR_ID, INFORMATION_OM_TS_BESLUT_ONSKAS_DELSVAR_ID,
            utlatande.getInformationOmTsBeslutOnskas());

        return svarList;
    }

}
