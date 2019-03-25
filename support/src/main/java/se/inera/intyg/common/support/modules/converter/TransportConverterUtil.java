/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.converter;

import static java.nio.charset.StandardCharsets.UTF_8;


import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3._2000._09.xmldsig_.ObjectFactory;
import org.w3._2000._09.xmldsig_.SignatureType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Strings;

import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Befattning;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateTypeFormatEnum;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Specialistkompetens;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.UnderskriftType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Enhet;
import se.riv.clinicalprocess.healthcond.certificate.v3.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.IntygsStatus;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

/**
 * Provides utility methods for converting domain objects from transport format to internal Java format.
 */
public final class TransportConverterUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TransportConverterUtil.class);

    private TransportConverterUtil() {
    }

    /**
     * Checks if delsvar can be parsed as string content.
     *
     * @param delsvar the Delsvar to parse
     * @return if delsvar can be parsed as string content
     */
    public static boolean isStringContent(Delsvar delsvar) {
        return delsvar.getContent().stream().allMatch(a -> a instanceof String);
    }

    /**
     * Attempt to parse a non-empty String from a Delsvar.
     *
     * @param delsvar the Delsvar to parse
     * @return the non-empty String content of the Delsvar
     */
    public static String getStringContent(Delsvar delsvar) {
        return delsvar.getContent().stream()
                .map(content -> ((String) content).trim())
                .filter(content -> !content.isEmpty())
                .reduce("", String::concat);
    }

    public static Boolean getBooleanContent(Delsvar delsvar) {
        return Boolean.valueOf(getStringContent(delsvar));
    }

    /**
     * Attempt to parse a CVType from a Delsvar.
     *
     * @param delsvar the Delsvar to parse
     * @return the CVType converted from the delsvar
     * @throws ConverterException if the conversion was not successful
     */
    public static CVType getCVSvarContent(Delsvar delsvar) throws ConverterException {
        for (Object o : delsvar.getContent()) {
            if (o instanceof Node) {
                CVType cvType = new CVType();
                Node node = (Node) o;
                NodeList list = node.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    if (Node.ELEMENT_NODE != list.item(i).getNodeType()) {
                        continue;
                    }
                    String textContent = list.item(i).getTextContent();
                    switch (list.item(i).getLocalName()) {
                    case "code":
                        cvType.setCode(textContent);
                        break;
                    case "codeSystem":
                        cvType.setCodeSystem(textContent);
                        break;
                    case "codeSystemVersion":
                        cvType.setCodeSystemVersion(textContent);
                        break;
                    case "codeSystemName":
                        cvType.setCodeSystemName(textContent);
                        break;
                    case "displayName":
                        cvType.setDisplayName(textContent);
                        break;
                    case "originalText":
                        cvType.setOriginalText(textContent);
                        break;
                    default:
                        LOG.debug("Unexpected element found while parsing CVType: " + list.item(i).getNodeName());
                        break;
                    }
                }
                if (cvType.getCode() == null || cvType.getCodeSystem() == null) {
                    throw new ConverterException("Error while converting CVType, missing mandatory field");
                }
                return cvType;
            } else if (o instanceof JAXBElement) {
                @SuppressWarnings("unchecked")
                JAXBElement<CVType> jaxbCvType = (JAXBElement<CVType>) o;
                return jaxbCvType.getValue();
            }
        }
        throw new ConverterException("Unexpected outcome while converting CVType");
    }

    /**
     * Attempt to parse a {@link DatePeriodType} from a {@link Delsvar}.
     *
     * @param delsvar the delsvar to be converted
     * @throws ConverterException if the conversion was not successful
     */
    public static DatePeriodType getDatePeriodTypeContent(Delsvar delsvar) throws ConverterException {
        for (Object o : delsvar.getContent()) {
            if (o instanceof Node) {
                DatePeriodType datePeriodType = new DatePeriodType();
                Node node = (Node) o;
                NodeList list = node.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    if (Node.ELEMENT_NODE != list.item(i).getNodeType()) {
                        continue;
                    }
                    String textContent = list.item(i).getTextContent();
                    switch (list.item(i).getLocalName()) {
                    case "start":
                        datePeriodType.setStart(LocalDate.parse(textContent));
                        break;
                    case "end":
                        datePeriodType.setEnd(LocalDate.parse(textContent));
                        break;
                    default:
                        LOG.debug("Unexpected element found while parsing DatePeriodType");
                        break;
                    }
                }
                if (datePeriodType.getStart() == null || datePeriodType.getEnd() == null) {
                    throw new ConverterException("Error while converting DatePeriodType, missing mandatory field");
                }
                return datePeriodType;
            } else if (o instanceof JAXBElement) {
                @SuppressWarnings("unchecked")
                JAXBElement<DatePeriodType> jaxbCvType = (JAXBElement<DatePeriodType>) o;
                return jaxbCvType.getValue();
            }
        }
        throw new ConverterException("Unexpected outcome while converting DatePeriodType");
    }

    /**
     * Attempt to parse a {@link PartialDateType} from a {@link Delsvar}.
     *
     * @param delsvar the delsvar to be converted
     * @throws ConverterException if the conversion was not successful
     */
    public static PartialDateType getPartialDateContent(Delsvar delsvar) throws ConverterException {
        for (Object o : delsvar.getContent()) {
            if (o instanceof Node) {
                PartialDateType partialDateType = new PartialDateType();
                Node node = (Node) o;
                NodeList list = node.getChildNodes();
                String partialDateValue = null;
                for (int i = 0; i < list.getLength(); i++) {
                    String textContent = list.item(i).getTextContent();
                    if (list.item(i).getLocalName() != null) {
                        switch (list.item(i).getLocalName()) {
                            case "format":
                                partialDateType.setFormat(PartialDateTypeFormatEnum.fromValue(textContent));
                                break;
                            case "value":
                                partialDateValue = textContent;
                                break;
                        }
                    }
                }
                switch (partialDateType.getFormat()) {
                    case YYYY:
                        if (partialDateValue != null) {
                            partialDateType.setValue(Year.of(Integer.parseInt(partialDateValue)));
                        }
                        break;
                    case YYYY_MM_DD:
                        if (partialDateValue != null) {
                            partialDateType.setValue(LocalDate.parse(partialDateValue));
                        }
                        break;
                    default:
                        throw new ConverterException("Unexpected format while converting PartialDateType");
                }
                return partialDateType;
            }
            if (o instanceof JAXBElement) {
                JAXBElement<PartialDateType> jaxbType = (JAXBElement<PartialDateType>) o;
                return jaxbType.getValue();
            }
        }
        throw new ConverterException("Unexpected outcome while converting PartialDateType");
    }

    /**
     * Parses the {@link GrundData} from the source Intyg.
     *
     * @param source              the certificate to be converted
     * @param extendedPatientInfo boolean flag to determine if all patient info should be included (should never be used for certificates
     *                            on patients with sekretessmarkering)
     * @return the converted GrundData
     */
    public static GrundData getGrundData(Intyg source, boolean extendedPatientInfo) {
        GrundData grundData = new GrundData();
        grundData.setPatient(getPatient(source.getPatient(), extendedPatientInfo));
        grundData.setSkapadAv(getSkapadAv(source.getSkapadAv()));
        grundData.setSigneringsdatum(source.getSigneringstidpunkt());
        if (!isNullOrEmpty(source.getRelation())) {
            grundData.setRelation(getRelation(source));
        }
        return grundData;
    }

    /**
     * Creates the metadata for the certificate.
     *
     * @param source         the source certificate
     * @param additionalInfo the info to be displayed in Mina intyg
     * @return the meta data
     */
    public static CertificateMetaData getMetaData(Intyg source, String additionalInfo) {
        CertificateMetaData metaData = new CertificateMetaData();
        metaData.setCertificateId(source.getIntygsId().getExtension());
        metaData.setCertificateType(source.getTyp().getCode().toLowerCase());
        metaData.setIssuerName(source.getSkapadAv().getFullstandigtNamn());
        metaData.setFacilityName(source.getSkapadAv().getEnhet().getEnhetsnamn());
        metaData.setSignDate(source.getSigneringstidpunkt());
        metaData.setStatus(getStatusList(source.getStatus()));
        metaData.setAvailable(isAvailable(metaData.getStatus()));
        metaData.setAdditionalInfo(additionalInfo);
        return metaData;
    }

    private static boolean isAvailable(List<Status> statuses) {
        final Optional<Status> latestStatus = statuses.stream()
                .filter(s -> CertificateState.RESTORED.equals(s.getType()) || CertificateState.DELETED.equals(s.getType()))
                .sorted(Comparator.nullsFirst(Comparator.comparing(Status::getTimestamp).reversed())).findFirst();

        // If neither DELETED or RESTORED at all, it's considered available
        if (!latestStatus.isPresent()) {
            return true;
        } else {
            // It's available if the latest of these types of statues is a RESTORED status event
            return CertificateState.RESTORED.equals(latestStatus.get().getType());
        }
    }

    /**
     * Converts a list of statuses of transport format to internal representation.
     *
     * @param certificateStatuses the statuses to be converted
     * @return the converted statuses
     */
    public static List<Status> getStatusList(List<IntygsStatus> certificateStatuses) {
        List<Status> statuses = new ArrayList<>(certificateStatuses.size());
        for (IntygsStatus certificateStatus : certificateStatuses) {
            statuses.add(getStatus(certificateStatus));
        }
        return statuses;
    }

    /**
     * Converts a single status on transport format.
     *
     * @param certificateStatus the status to be converted
     * @return the converted status
     */
    public static Status getStatus(IntygsStatus certificateStatus) {
        return new Status(
                StatusKod.valueOf(certificateStatus.getStatus().getCode()).toCertificateState(),
                certificateStatus.getPart().getCode(),
                certificateStatus.getTidpunkt());
    }

    /**
     * Returns an internal representation of the creator of the certificate.
     *
     * @param source the creator in transport format
     * @return the converted creator
     */
    public static HoSPersonal getSkapadAv(HosPersonal source) {
        HoSPersonal personal = new HoSPersonal();
        personal.setPersonId(source.getPersonalId().getExtension());
        personal.setFullstandigtNamn(source.getFullstandigtNamn());
        personal.setForskrivarKod(source.getForskrivarkod());
        personal.setVardenhet(getVardenhet(source.getEnhet()));
        for (Befattning befattning : source.getBefattning()) {
            personal.getBefattningar().add(befattning.getCode());
        }
        for (Specialistkompetens kompetens : source.getSpecialistkompetens()) {
            if (kompetens.getDisplayName() != null) {
                personal.getSpecialiteter().add(kompetens.getDisplayName());
            }
        }
        return personal;
    }

    /**
     * Converts an Enhet to internal representation.
     *
     * @param source the transport representation
     * @return the converted Vardenhet
     */
    public static Vardenhet getVardenhet(Enhet source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setPostort(source.getPostort());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setEnhetsid(source.getEnhetsId().getExtension());
        vardenhet.setArbetsplatsKod(source.getArbetsplatskod().getExtension());
        vardenhet.setEnhetsnamn(source.getEnhetsnamn());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(getVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    /**
     * Converts a Vardgivare to internal representation.
     *
     * @param source the transport representation
     * @return the converted Vardgivare
     */
    public static Vardgivare getVardgivare(se.riv.clinicalprocess.healthcond.certificate.v3.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(source.getVardgivareId().getExtension());
        vardgivare.setVardgivarnamn(source.getVardgivarnamn());
        return vardgivare;
    }

    /**
     * Converts the transport representation of the patient to internal.
     *
     * @param source              the transport representation
     * @param extendedPatientInfo if sensitive patient data should be included
     *                            (should never be used for certificates which can be used for patients with sekretessmarkering)
     * @return the converted patient
     */
    public static Patient getPatient(se.riv.clinicalprocess.healthcond.certificate.v3.Patient source, boolean extendedPatientInfo) {
        String pnr = source.getPersonId().getExtension();
        Personnummer personnummer = Personnummer.createPersonnummer(pnr).get();

        Patient patient = new Patient();
        patient.setPersonId(personnummer);

        if (extendedPatientInfo) {
            patient.setEfternamn(source.getEfternamn());
            patient.setFornamn(source.getFornamn());
            patient.setMellannamn(source.getMellannamn());
            patient.setPostort(source.getPostort());
            patient.setPostnummer(source.getPostnummer());
            patient.setPostadress(source.getPostadress());

            if (Strings.nullToEmpty(source.getMellannamn()).trim().isEmpty()) {
                patient.setFullstandigtNamn(source.getFornamn() + " " + source.getEfternamn());
            } else {
                patient.setFullstandigtNamn(source.getFornamn() + " " + source.getMellannamn() + " " + source.getEfternamn());
            }
        }
        return patient;
    }

    public static String signatureTypeToBase64(UnderskriftType underskriftType) throws ConverterException {
        if (underskriftType == null || underskriftType.getSignature() == null) {
            return null;
        }
        try {
            JAXBElement<SignatureType> signature = new ObjectFactory().createSignature(underskriftType.getSignature());
            final String s = XmlMarshallerHelper.marshal(signature);
            return Base64.getEncoder().encodeToString(s.getBytes(UTF_8));
        } catch (Exception e) {
            throw new ConverterException("Error when converting SignatureType to String: " + e.getMessage());
        }
    }

    private static Relation getRelation(Intyg source) {
        return getOne(source.getRelation());
    }

    private static Relation getOne(List<se.riv.clinicalprocess.healthcond.certificate.v3.Relation> source) {
        se.riv.clinicalprocess.healthcond.certificate.v3.Relation sourceRelation = source.get(0);
        Relation relation = new Relation();
        relation.setRelationIntygsId(sourceRelation.getIntygsId().getExtension());
        String sourceTyp = sourceRelation.getTyp().getCode();
        relation.setRelationKod(RelationKod.fromValue(sourceTyp));
        return relation;
    }

    private static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
