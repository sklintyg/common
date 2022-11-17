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

package se.inera.intyg.common.doi.v1.model.converter;

import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_B_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_C_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_D_ID;

import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.doi.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionBidragandeSjukdomar;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionForgiftningDatum;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionForgiftningOm;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionForgiftningOrsak;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionForgiftningUppkommelse;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionGrunderDodsorsaksuppgifter;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionLand;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionOperation;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionOperationAnledning;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionOperationDatum;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionTerminalDodsorsak;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionTerminalDodsorsakFoljdAv;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionTerminalDodsorsakSjukdom;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionAntraffadDod;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionBarn;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsdatum;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsdatumSakert;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsplatsBoende;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsplatsKommun;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionIdentitetenStyrkt;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionOsakertDodsdatum;
import se.inera.intyg.common.support.facade.model.Certificate;

@Component
public class CertificateToInternal {

    public DoiUtlatandeV1 convert(Certificate certificate, DoiUtlatandeV1 internalCertificate) {
        final var builder = DoiUtlatandeV1.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(MetaDataGrundData.toInternal(certificate.getMetadata(), internalCertificate.getGrundData()))
            .setIdentitetStyrkt(QuestionIdentitetenStyrkt.toInternal(certificate))
            .setLand(QuestionLand.toInternal(certificate))
            .setAntraffatDodDatum(QuestionAntraffadDod.toInternal(certificate))
            .setDodsplatsKommun(QuestionDodsplatsKommun.toInternal(certificate))
            .setDodsplatsBoende(QuestionDodsplatsBoende.toInternal(certificate))
            .setBarn(QuestionBarn.toInternal(certificate))
            .setTerminalDodsorsak(QuestionTerminalDodsorsak.toInternal(certificate))
            .setFoljd(QuestionTerminalDodsorsakFoljdAv.toInternal(
                certificate, List.of(FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_C_ID, FOLJD_OM_DELSVAR_D_ID)))
            .setBidragandeSjukdomar(QuestionBidragandeSjukdomar.toInternal(certificate))
            .setOperation(QuestionOperation.toInternal(certificate))
            .setOperationDatum(QuestionOperationDatum.toInternal(certificate))
            .setOperationAnledning(QuestionOperationAnledning.toInternal(certificate))
            .setForgiftning(QuestionForgiftningOm.toInternal(certificate))
            .setForgiftningOrsak(QuestionForgiftningOrsak.toInternal(certificate))
            .setForgiftningDatum(QuestionForgiftningDatum.toInternal(certificate))
            .setForgiftningUppkommelse(QuestionForgiftningUppkommelse.toInternal(certificate))
            .setGrunder(QuestionGrunderDodsorsaksuppgifter.toInternal(certificate));

        final var dodsdatumSakert = QuestionDodsdatumSakert.toInternal(certificate);
        if (dodsdatumSakert == null) {
            return builder.build();
        }

        builder.setDodsdatumSakert(dodsdatumSakert);
        if (dodsdatumSakert) {
            builder.setDodsdatum(QuestionDodsdatum.toInternal(certificate));
        } else {
            builder.setDodsdatum(QuestionOsakertDodsdatum.toInternal(certificate));
        }
        return builder.build();
    }
}
