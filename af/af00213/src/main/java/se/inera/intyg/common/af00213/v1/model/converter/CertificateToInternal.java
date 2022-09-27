/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import se.inera.intyg.common.af00213.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionAktivitetsbegransning;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionArbetspaverkan;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionFunktionsnedsattning;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionHarAktivitetsbegransning;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionHarArbetspaverkan;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionHarFunktionsnedsattning;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionHarUtredningBehandling;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionOvrigt;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionUtredningBehandling;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;

public final class CertificateToInternal {

    private CertificateToInternal() {

    }

    public static Af00213UtlatandeV1 convert(Certificate certificate, Af00213UtlatandeV1 internalCertificate) {
        return Af00213UtlatandeV1.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(MetaDataGrundData.toInternal(certificate.getMetadata(), internalCertificate.getGrundData()))
            .setHarFunktionsnedsattning(QuestionHarFunktionsnedsattning.toInternal(certificate))
            .setFunktionsnedsattning(QuestionFunktionsnedsattning.toInternal(certificate))
            .setHarAktivitetsbegransning(QuestionHarAktivitetsbegransning.toInternal(certificate))
            .setAktivitetsbegransning(QuestionAktivitetsbegransning.toInternal(certificate))
            .setHarUtredningBehandling(QuestionHarUtredningBehandling.toInternal(certificate))
            .setUtredningBehandling(QuestionUtredningBehandling.toInternal(certificate))
            .setHarArbetetsPaverkan(QuestionHarArbetspaverkan.toInternal(certificate))
            .setArbetetsPaverkan(QuestionArbetspaverkan.toInternal(certificate))
            .setOvrigt(QuestionOvrigt.toInternal(certificate))
            .build();
    }
}
