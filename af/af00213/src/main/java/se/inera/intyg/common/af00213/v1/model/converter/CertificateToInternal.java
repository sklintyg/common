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

package se.inera.intyg.common.af00213.v1.model.converter;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.grundData;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.model.common.internal.GrundData;

public final class CertificateToInternal {

    private CertificateToInternal() {

    }

    public static Af00213UtlatandeV1 convert(Certificate certificate, Af00213UtlatandeV1 internalCertificate) {
        final var harFunktionsnedsattning = getHarFunktionsnedsattning(certificate);
        final var funktionsnedsattning = getFunktionsnedsattning(certificate);
        final var harAktivitetsbegransning = getHarAktivitetsbegransning(certificate);
        final var aktivitetsbegransning = getAktivitetsbegransning(certificate);
        final var harUtredningBehandling = getHarUtredningBehandling(certificate);
        final var utredningBehandling = getUtredningBehandling(certificate);
        final var harArbetspaverkan = getHarArbetspaverkan(certificate);
        final var arbetspaverkan = getArbetspaverkan(certificate);
        final var ovrigt = getOvrigt(certificate);
        final var grundData = getGrundData(certificate.getMetadata(), internalCertificate.getGrundData());

        return Af00213UtlatandeV1.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(grundData)
            .setHarFunktionsnedsattning(harFunktionsnedsattning)
            .setFunktionsnedsattning(funktionsnedsattning)
            .setHarAktivitetsbegransning(harAktivitetsbegransning)
            .setAktivitetsbegransning(aktivitetsbegransning)
            .setHarUtredningBehandling(harUtredningBehandling)
            .setUtredningBehandling(utredningBehandling)
            .setHarArbetetsPaverkan(harArbetspaverkan)
            .setArbetetsPaverkan(arbetspaverkan)
            .setOvrigt(ovrigt)
            .build();
    }


    private static Boolean getHarFunktionsnedsattning(Certificate certificate) {
        return booleanValue(certificate.getData(), FUNKTIONSNEDSATTNING_DELSVAR_ID_11, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11);
    }

    private static String getFunktionsnedsattning(Certificate certificate) {
        return textValue(certificate.getData(), FUNKTIONSNEDSATTNING_DELSVAR_ID_12, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12);
    }

    private static Boolean getHarAktivitetsbegransning(Certificate certificate) {
        return booleanValue(certificate.getData(), AKTIVITETSBEGRANSNING_DELSVAR_ID_21, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21);
    }

    private static String getAktivitetsbegransning(Certificate certificate) {
        return textValue(certificate.getData(), AKTIVITETSBEGRANSNING_DELSVAR_ID_22, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22);
    }

    private static Boolean getHarUtredningBehandling(Certificate certificate) {
        return booleanValue(certificate.getData(), UTREDNING_BEHANDLING_DELSVAR_ID_31, UTREDNING_BEHANDLING_SVAR_JSON_ID_31);
    }

    private static String getUtredningBehandling(Certificate certificate) {
        return textValue(certificate.getData(), UTREDNING_BEHANDLING_DELSVAR_ID_32, UTREDNING_BEHANDLING_SVAR_JSON_ID_32);
    }

    private static Boolean getHarArbetspaverkan(Certificate certificate) {
        return booleanValue(certificate.getData(), ARBETETS_PAVERKAN_DELSVAR_ID_41, ARBETETS_PAVERKAN_SVAR_JSON_ID_41);
    }

    private static String getArbetspaverkan(Certificate certificate) {
        return textValue(certificate.getData(), ARBETETS_PAVERKAN_DELSVAR_ID_42, ARBETETS_PAVERKAN_SVAR_JSON_ID_42);
    }

    private static String getOvrigt(Certificate certificate) {
        return textValue(certificate.getData(), OVRIGT_DELSVAR_ID_5, OVRIGT_SVAR_JSON_ID_5);
    }

    private static GrundData getGrundData(CertificateMetadata metadata, GrundData grundData) {
        return grundData(metadata, grundData);
    }
}
