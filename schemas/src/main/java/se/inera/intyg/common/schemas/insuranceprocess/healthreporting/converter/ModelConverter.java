/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter;

import com.google.common.base.Joiner;
import iso.v21090.dt.v1.II;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.healthreporting.medcertqa.v1.LakarutlatandeEnkelType;
import se.inera.ifv.insuranceprocess.healthreporting.medcertqa.v1.VardAdresseringsType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.builder.CertificateMetaTypeBuilder;
import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.validate.SamordningsnummerValidator;
import se.inera.intyg.schemas.contract.Personnummer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
            .signDate(source.getSignedDate() != null ? source.getSignedDate().toLocalDate() : null)
            .available(String.valueOf(!source.isDeleted()));

        CertificateMetaType meta = builder.build();

        meta.getStatus().addAll(CertificateStateHolderConverter.toCertificateStatusType(source.getCertificateStates()));
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

    public static RevokeType buildRevokeTypeFromUtlatande(Utlatande utlatande, String revokeMessage) {

        // Lakarutlatande
        LakarutlatandeEnkelType utlatandeType = toLakarutlatandeEnkelType(utlatande);

        // Vardadress
        VardAdresseringsType vardAdressType = toVardAdresseringsType(utlatande.getGrundData());

        RevokeType revokeType = new RevokeType();
        revokeType.setLakarutlatande(utlatandeType);
        revokeType.setAdressVard(vardAdressType);
        revokeType.setVardReferensId(buildVardReferensId(utlatande.getId(), LocalDateTime.now()));
        revokeType.setAvsantTidpunkt(LocalDateTime.now());
        revokeType.setMeddelande(revokeMessage);

        return revokeType;
    }

    public static String buildVardReferensId(String intygId, LocalDateTime ts) {
        return Joiner.on('-').join("REVOKE", intygId, ts.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSS")));
    }

    public static LakarutlatandeEnkelType toLakarutlatandeEnkelType(Utlatande utlatande) {
        LakarutlatandeEnkelType lakarutlatande = new LakarutlatandeEnkelType();

        lakarutlatande.setLakarutlatandeId(utlatande.getId());
        lakarutlatande.setSigneringsTidpunkt(utlatande.getGrundData().getSigneringsdatum());

        Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
        II patientIdHolder = new II();
        patientIdHolder.setRoot(SamordningsnummerValidator.isSamordningsNummer(Optional.of(personId))
            ? Constants.SAMORDNING_ID_OID
            : Constants.PERSON_ID_OID);
        patientIdHolder.setExtension(personId.getOriginalPnr());

        PatientType patient = new PatientType();
        patient.setPersonId(patientIdHolder);

        lakarutlatande.setPatient(patient);
        return lakarutlatande;
    }

    private static LocalDate toLocalDate(String date) {
        if (date == null) {
            return null;
        }
        return LocalDate.parse(date);
    }

}
