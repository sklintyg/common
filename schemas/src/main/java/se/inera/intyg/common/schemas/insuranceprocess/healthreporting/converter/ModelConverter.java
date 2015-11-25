/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
 *
 * Inera Certificate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Certificate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter;

import org.joda.time.LocalDate;

import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.util.Iterables;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.healthreporting.medcertqa.v1.LakarutlatandeEnkelType;
import se.inera.ifv.insuranceprocess.healthreporting.medcertqa.v1.VardAdresseringsType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;
import se.inera.intyg.common.schemas.Constants;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.builder.CertificateMetaTypeBuilder;
import iso.v21090.dt.v1.II;

/**
 * @author andreaskaltenbach
 */
public final class ModelConverter {

    private ModelConverter() {
    }

    public static CertificateMetaType toCertificateMetaType(CertificateHolder source) {

        CertificateMetaTypeBuilder builder = new CertificateMetaTypeBuilder()
                .certificateId(source.getId())
                .certificateType(source.getType())
                .validity(toLocalDate(source.getValidFromDate()), toLocalDate(source.getValidToDate()))
                .issuerName(source.getSigningDoctorName())
                .facilityName(source.getCareUnitName())
                .signDate(toLocalDate(source.getSignedDate()))
                .available(source.isDeleted() ? "false" : "true");

        CertificateMetaType meta = builder.build();

        Iterables.addAll(meta.getStatus(), CertificateStateHolderConverter.toCertificateStatusType(source.getCertificateStates()));
        return builder.build();
    }

    public static VardAdresseringsType toVardAdresseringsType(GrundData intygMetaData) {
        VardAdresseringsType vardAdresseringsType = new VardAdresseringsType();

        EnhetType enhet = new EnhetType();

        II enhetsId = new II();
        enhetsId.setRoot(Constants.HSA_ID_OID);
        enhetsId.setExtension(intygMetaData.getSkapadAv().getVardenhet().getEnhetsid());
        enhet.setEnhetsId(enhetsId);
        enhet.setEnhetsnamn(intygMetaData.getSkapadAv().getVardenhet().getEnhetsnamn());

        if (intygMetaData.getSkapadAv().getVardenhet().getArbetsplatsKod() != null) {
            II arbetsplatsKod = new II();
            arbetsplatsKod.setRoot(Constants.ARBETSPLATS_KOD_OID);
            arbetsplatsKod.setExtension(intygMetaData.getSkapadAv().getVardenhet().getArbetsplatsKod());
            enhet.setArbetsplatskod(arbetsplatsKod);
        }

        VardgivareType vardGivare = new VardgivareType();

        II vardGivarId = new II();
        vardGivarId.setRoot(Constants.HSA_ID_OID);
        vardGivarId.setExtension(intygMetaData.getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        vardGivare.setVardgivareId(vardGivarId);
        vardGivare.setVardgivarnamn(intygMetaData.getSkapadAv().getVardenhet().getVardgivare().getVardgivarnamn());
        enhet.setVardgivare(vardGivare);

        HosPersonalType hosPersonal = new HosPersonalType();
        hosPersonal.setEnhet(enhet);
        hosPersonal.setFullstandigtNamn(intygMetaData.getSkapadAv().getFullstandigtNamn());

        II personalId = new II();
        personalId.setRoot(Constants.HSA_ID_OID);
        personalId.setExtension(intygMetaData.getSkapadAv().getPersonId());
        hosPersonal.setPersonalId(personalId);

        vardAdresseringsType.setHosPersonal(hosPersonal);
        return vardAdresseringsType;
    }

    public static LakarutlatandeEnkelType toLakarutlatandeEnkelType(Utlatande utlatande) {
        LakarutlatandeEnkelType lakarutlatande = new LakarutlatandeEnkelType();

        lakarutlatande.setLakarutlatandeId(utlatande.getId());

        lakarutlatande.setSigneringsTidpunkt(utlatande.getGrundData().getSigneringsdatum());

        PatientType patient = new PatientType();

        II patientIdHolder = new II();
        Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
        patientIdHolder.setRoot(personId.isSamordningsNummer() ? Constants.SAMORDNING_ID_OID : Constants.PERSON_ID_OID);
        patientIdHolder.setExtension(personId.getPersonnummer());
        patient.setPersonId(patientIdHolder);

        patient.setFullstandigtNamn(utlatande.getGrundData().getPatient().getFullstandigtNamn());
        lakarutlatande.setPatient(patient);

        return lakarutlatande;
    }

    private static LocalDate toLocalDate(Object date) {
        if (date == null) {
            return null;
        }
        return new LocalDate(date);
    }

}
