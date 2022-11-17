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

import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_B_DATUM_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_B_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_B_LABEL;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_C_DATUM_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_C_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_C_LABEL;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_D_DATUM_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_D_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_D_LABEL;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.doi.v1.model.converter.certificate.category.CategoryDodsorsaksuppgifter;
import se.inera.intyg.common.doi.v1.model.converter.certificate.category.CategoryOperation;
import se.inera.intyg.common.doi.v1.model.converter.certificate.category.CategorySkadaForgiftning;
import se.inera.intyg.common.doi.v1.model.converter.certificate.category.CategoryTerminalDodsorsak;
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
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.sos_parent.model.converter.certificate.category.CategoryBarn;
import se.inera.intyg.common.sos_parent.model.converter.certificate.category.CategoryDodsdatumDodsplats;
import se.inera.intyg.common.sos_parent.model.converter.certificate.category.CategoryKompletterandePatientuppgifter;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionAntraffadDod;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionAutoFillMessageAfter28DaysBarn;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionAutoFillMessageWithin28DaysBarn;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionBarn;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsdatum;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsdatumSakert;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsplats;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsplatsBoende;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsplatsKommun;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionIdentitetenStyrkt;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionOsakertDodsdatum;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadEnum;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;

@Component
public class InternalToCertificate {

    public Certificate convert(DoiUtlatandeV1 internalCertificate, CertificateTextProvider texts,
        TypeAheadProvider typeAheadProvider) {
        int index = 0;
        final var municipalities = typeAheadProvider.getValues(TypeAheadEnum.MUNICIPALITIES);
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, texts))
            .addElement(
                CategoryKompletterandePatientuppgifter.toCertificate(index++, texts)
            )
            .addElement(
                QuestionIdentitetenStyrkt.toCertificate(internalCertificate.getIdentitetStyrkt(), index++, texts)
            )
            .addElement(
                QuestionLand.toCertificate(internalCertificate.getLand(), index++, texts)
            )
            .addElement(
                CategoryDodsdatumDodsplats.toCertificate(index++, texts)
            )
            .addElement(
                QuestionDodsdatumSakert.toCertificate(internalCertificate.getDodsdatumSakert(), index++, texts)
            )
            .addElement(
                QuestionDodsdatum.toCertificate(getDodsdatumValue(internalCertificate), index++, texts)
            )
            .addElement(
                QuestionOsakertDodsdatum.toCertificate(getDodsdatumOsakertValue(internalCertificate), index++, texts)
            )
            .addElement(
                QuestionAntraffadDod.toCertificate(toLocalDate(internalCertificate.getAntraffatDodDatum()), index++, texts)
            )
            .addElement(
                QuestionDodsplats.toCertificate(index++, texts)
            )
            .addElement(
                QuestionDodsplatsKommun.toCertificate(municipalities, internalCertificate.getDodsplatsKommun(), index++, texts)
            )
            .addElement(
                QuestionDodsplatsBoende.toCertificate(internalCertificate.getDodsplatsBoende(), index++, texts)
            )
            .addElement(
                CategoryBarn.toCertificate(index++, texts)
            )
            .addElement(
                QuestionBarn.toCertificate(internalCertificate.getGrundData().getPatient().getPersonId(),
                    internalCertificate.getBarn(), index++, texts)
            )
            .addElement(
                QuestionAutoFillMessageWithin28DaysBarn.toCertificate(internalCertificate.getGrundData().getPatient().getPersonId(),
                    index++, texts)
            )
            .addElement(
                QuestionAutoFillMessageAfter28DaysBarn.toCertificate(internalCertificate.getGrundData().getPatient().getPersonId(),
                    index++, texts)
            )
            .addElement(
                CategoryTerminalDodsorsak.toCertificate(index++, texts)
            )
            .addElement(
                QuestionTerminalDodsorsakSjukdom.toCertificate(index++, texts)
            )
            .addElement(
                QuestionTerminalDodsorsak.toCertificate(getTerminalDodsorsak(internalCertificate.getTerminalDodsorsak()), index++, texts)
            )
            .addElement(
                QuestionTerminalDodsorsakFoljdAv.toCertificate(getTerminalDodsorsak(internalCertificate.getFoljd().get(0)),
                    index++, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID)
            )
            .addElement(
                QuestionTerminalDodsorsakFoljdAv.toCertificate(getTerminalDodsorsak(internalCertificate.getFoljd().get(1)),
                    index++, texts, FOLJD_OM_DELSVAR_C_ID, FOLJD_OM_DELSVAR_C_LABEL, FOLJD_OM_DELSVAR_C_DATUM_ID)
            )
            .addElement(
                QuestionTerminalDodsorsakFoljdAv.toCertificate(getTerminalDodsorsak(internalCertificate.getFoljd().get(2)),
                    index++, texts, FOLJD_OM_DELSVAR_D_ID, FOLJD_OM_DELSVAR_D_LABEL, FOLJD_OM_DELSVAR_D_DATUM_ID)
            )
            .addElement(
                QuestionBidragandeSjukdomar.toCertificate(internalCertificate.getBidragandeSjukdomar(), index++, texts)
            )
            .addElement(
                CategoryOperation.toCertificate(index++, texts)
            )
            .addElement(
                QuestionOperation.toCertificate(internalCertificate.getOperation(), index++, texts)
            )
            .addElement(
                QuestionOperationDatum.toCertificate(toLocalDate(internalCertificate.getOperationDatum()), index++, texts)
            )
            .addElement(
                QuestionOperationAnledning.toCertificate(internalCertificate.getOperationAnledning(), index++, texts)
            )
            .addElement(
                CategorySkadaForgiftning.toCertificate(index++, texts)
            )
            .addElement(
                QuestionForgiftningOm.toCertificate(internalCertificate.getForgiftning(), index++, texts)
            )
            .addElement(
                QuestionForgiftningOrsak.toCertificate(internalCertificate.getForgiftningOrsak(), index++, texts)
            )
            .addElement(
                QuestionForgiftningDatum.toCertificate(toLocalDate(internalCertificate.getForgiftningDatum()), index++, texts)
            )
            .addElement(
                QuestionForgiftningUppkommelse.toCertificate(internalCertificate.getForgiftningUppkommelse(), index++, texts)
            )
            .addElement(
                CategoryDodsorsaksuppgifter.toCertificate(index++, texts)
            )
            .addElement(
                QuestionGrunderDodsorsaksuppgifter.toCertificate(internalCertificate.getGrunder(), index++, texts)
            )
            .build();
    }

    private static LocalDate getDodsdatumValue(DoiUtlatandeV1 internalCertificate) {
        if (isDodsdatumSakert(internalCertificate.getDodsdatumSakert())) {
            return toLocalDate(internalCertificate.getDodsdatum());
        }
        return null;
    }

    private static String getDodsdatumOsakertValue(DoiUtlatandeV1 internalCertificate) {
        if (isDodsdatumSakert(internalCertificate.getDodsdatumSakert())) {
            return null;
        }
        return internalCertificate.getDodsdatum() != null ? internalCertificate.getDodsdatum().toString() : null;
    }

    private static Dodsorsak getTerminalDodsorsak(Dodsorsak dodsorsak) {
        return dodsorsak != null ? dodsorsak : Dodsorsak.create(null, null, null);
    }

    private static Boolean isDodsdatumSakert(Boolean dodsdatumSakert) {
        return dodsdatumSakert != null && dodsdatumSakert;
    }

    private static LocalDate toLocalDate(InternalDate internalDate) {
        return (internalDate != null && internalDate.isValidDate()) ? internalDate.asLocalDate() : null;
    }
}
