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

package se.inera.intyg.common.luae_na.v1.model.converter;

import se.inera.intyg.common.luae_na.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.category.CategoryAktivietsbegransningar;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.category.CategoryBakgrund;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.category.CategoryDiagnos;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.category.CategoryFunktionsnedsattning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.category.CategoryGrundForMU;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.category.CategoryMedicinskBehandling;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.category.CategoryMedicinskaForutsattningarArbete;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.category.CategoryOvrigt;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionAktivetsbegransningarHeader;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionAktivitetsbegransningar;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionAnnatBeskrivning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionDiagnosForNyBedomning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionDiagnosgrund;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFormagaTrotsBegransning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionForslagTillAtgard;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningAnnan;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningBalansKoordination;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningIntellektuell;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningKommunikation;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningKoncentration;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningPsykisk;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningSynHorselTal;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionKannedomOmPatient;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingAvslutadBehandling;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingAvslutadBehandlingHeader;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingPagaendeBehandling;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingPagaendeBehandlingHeader;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingPlaneradBehandling;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingPlaneradBehandlingHeader;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingSubstansintag;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingSubstansintagHeader;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskaForutsattningarForArbete;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMotiveringTillInteBaseratPaUndersokning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionNyBedomningDiagnosgrund;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionOvrigt;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionSjukdomsforlopp;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionUnderlag;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionUnderlagBaseratPa;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionUnderlagFinns;
import se.inera.intyg.common.luae_na.v1.model.internal.LuaenaUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;

public class InternalToCertificate {


    public static Certificate toCertificate(LuaenaUtlatandeV1 internalCertificate, CertificateTextProvider textProvider) {
        int index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, textProvider))
            .addElement(
                CategoryGrundForMU.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionUnderlagBaseratPa.toCertificate(internalCertificate.getUndersokningAvPatienten(),
                    internalCertificate.getJournaluppgifter(), internalCertificate.getAnhorigsBeskrivningAvPatienten(),
                    internalCertificate.getAnnatGrundForMU(), index++, textProvider)
            )
            .addElement(
                QuestionAnnatBeskrivning.toCertificate(internalCertificate.getAnnatGrundForMUBeskrivning(), index++, textProvider)
            )
            .addElement(QuestionMotiveringTillInteBaseratPaUndersokning.toCertificate(
                internalCertificate.getMotiveringTillInteBaseratPaUndersokning(), index++, textProvider
            ))
            .addElement(
                QuestionKannedomOmPatient.toCertificate(internalCertificate.getKannedomOmPatient(), index++, textProvider)
            )
            .addElement(
                QuestionUnderlagFinns.toCertificate(internalCertificate.getUnderlagFinns(), index++, textProvider)
            )
            .addElement(
                QuestionUnderlag.toCertificate(internalCertificate.getUnderlag(), index++, textProvider)
            )
            .addElement(
                CategoryBakgrund.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionSjukdomsforlopp.toCertificate(internalCertificate.getSjukdomsforlopp(), index++, textProvider)
            )
            .addElement(
                CategoryDiagnos.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionDiagnoser.toCertificate(internalCertificate.getDiagnoser(), index++, textProvider)
            )
            .addElement(
                QuestionDiagnosgrund.toCertificate(internalCertificate.getDiagnosgrund(), index++, textProvider)
            )
            .addElement(
                QuestionNyBedomningDiagnosgrund.toCertificate(internalCertificate.getNyBedomningDiagnosgrund(), index++, textProvider)
            )
            .addElement(
                QuestionDiagnosForNyBedomning.toCertificate(internalCertificate.getDiagnosForNyBedomning(), index++, textProvider)
            )
            .addElement(
                CategoryFunktionsnedsattning.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionFunktionsnedsattningIntellektuell.toCertificate(internalCertificate.getFunktionsnedsattningIntellektuell(), index++,
                    textProvider)
            )
            .addElement(
                QuestionFunktionsnedsattningKommunikation.toCertificate(internalCertificate.getFunktionsnedsattningKommunikation(), index++,
                    textProvider)
            )
            .addElement(
                QuestionFunktionsnedsattningKoncentration.toCertificate(internalCertificate.getFunktionsnedsattningKoncentration(), index++,
                    textProvider)
            )
            .addElement(
                QuestionFunktionsnedsattningPsykisk.toCertificate(internalCertificate.getFunktionsnedsattningPsykisk(), index++,
                    textProvider)
            )
            .addElement(
                QuestionFunktionsnedsattningSynHorselTal.toCertificate(internalCertificate.getFunktionsnedsattningSynHorselTal(), index++,
                    textProvider)
            )
            .addElement(
                QuestionFunktionsnedsattningBalansKoordination.toCertificate(
                    internalCertificate.getFunktionsnedsattningBalansKoordination(), index++, textProvider)
            )
            .addElement(
                QuestionFunktionsnedsattningAnnan.toCertificate(internalCertificate.getFunktionsnedsattningAnnan(), index++, textProvider)
            )
            .addElement(
                CategoryAktivietsbegransningar.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionAktivetsbegransningarHeader.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionAktivitetsbegransningar.toCertificate(internalCertificate.getAktivitetsbegransning(), index++, textProvider)
            )
            .addElement(
                CategoryMedicinskBehandling.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionMedicinskBehandlingAvslutadBehandlingHeader.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionMedicinskBehandlingAvslutadBehandling.toCertificate(internalCertificate.getAvslutadBehandling(), index++,
                    textProvider)
            )
            .addElement(
                QuestionMedicinskBehandlingPagaendeBehandlingHeader.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionMedicinskBehandlingPagaendeBehandling.toCertificate(internalCertificate.getPagaendeBehandling(), index++,
                    textProvider)
            )
            .addElement(
                QuestionMedicinskBehandlingPlaneradBehandlingHeader.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionMedicinskBehandlingPlaneradBehandling.toCertificate(internalCertificate.getPlaneradBehandling(), index++,
                    textProvider)
            )
            .addElement(
                QuestionMedicinskBehandlingSubstansintagHeader.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionMedicinskBehandlingSubstansintag.toCertificate(internalCertificate.getSubstansintag(), index++, textProvider)
            )
            .addElement(
                CategoryMedicinskaForutsattningarArbete.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionMedicinskaForutsattningarForArbete.toCertificate(internalCertificate.getMedicinskaForutsattningarForArbete(),
                    index++, textProvider)
            )
            .addElement(
                QuestionFormagaTrotsBegransning.toCertificate(internalCertificate.getFormagaTrotsBegransning(), index++, textProvider)
            )
            .addElement(
                QuestionForslagTillAtgard.toCertificate(internalCertificate.getForslagTillAtgard(), index++, textProvider)
            )
            .addElement(
                CategoryOvrigt.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionOvrigt.toCertificate(internalCertificate.getOvrigt(), index++, textProvider)
            )
            .build();
    }
}
