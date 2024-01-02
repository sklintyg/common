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
package se.inera.intyg.common.af00213.v1.model.converter;

import se.inera.intyg.common.af00213.v1.model.converter.certificate.CategoryAktivitetsbegransning;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.CategoryArbetspaverkan;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.CategoryFunktionsNedsattning;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.CategoryOvrigt;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.CategoryUtredningBehandling;
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
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;

public final class InternalToCertificate {

    private InternalToCertificate() {

    }

    public static Certificate convert(Af00213UtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        int index = 0;

        return CertificateBuilder.create()
            .metadata(
                MetaDataGrundData.toCertificate(internalCertificate, texts)
            )
            .addElement(
                CategoryFunktionsNedsattning.toCertificate(index++, texts)
            )
            .addElement(
                QuestionHarFunktionsnedsattning.toCertificate(internalCertificate.getHarFunktionsnedsattning(), index++, texts)
            )
            .addElement(
                QuestionFunktionsnedsattning.toCertificate(internalCertificate.getFunktionsnedsattning(), index++, texts)
            )
            .addElement(
                CategoryAktivitetsbegransning.toCertificate(index++, texts)
            )
            .addElement(
                QuestionHarAktivitetsbegransning.toCertificate(internalCertificate.getHarAktivitetsbegransning(), index++, texts)
            )
            .addElement(
                QuestionAktivitetsbegransning.toCertificate(internalCertificate.getAktivitetsbegransning(), index++, texts)
            )
            .addElement(
                CategoryUtredningBehandling.toCertificate(index++, texts)
            )
            .addElement(
                QuestionHarUtredningBehandling.toCertificate(internalCertificate.getHarUtredningBehandling(), index++, texts)
            )
            .addElement(
                QuestionUtredningBehandling.toCertificate(internalCertificate.getUtredningBehandling(), index++, texts)
            )
            .addElement(
                CategoryArbetspaverkan.toCertificate(index++, texts)
            )
            .addElement(
                QuestionHarArbetspaverkan.toCertificate(internalCertificate.getHarArbetetsPaverkan(), index++, texts)
            )
            .addElement(
                QuestionArbetspaverkan.toCertificate(internalCertificate.getArbetetsPaverkan(), index++, texts)
            )
            .addElement(
                CategoryOvrigt.toCertificate(index++, texts)
            )
            .addElement(
                QuestionOvrigt.toCerticate(internalCertificate.getOvrigt(), index, texts)
            )
            .build();
    }
}
