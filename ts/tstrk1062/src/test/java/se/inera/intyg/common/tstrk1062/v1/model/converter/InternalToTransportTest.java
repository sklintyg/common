package se.inera.intyg.common.tstrk1062.v1.model.converter;

import static junit.framework.TestCase.*;

import java.time.LocalDateTime;

import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

public class InternalToTransportTest {

    @Test(expected = ConverterException.class)
    public void testConvertSourceNull() throws Exception {
        InternalToTransport.convert(null);
    }

    @Test
    public void testConvertSourceWithoutMessage() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = getUtlatande();

        RegisterCertificateType tsTsrk1062 = InternalToTransport.convert(utlatande);

        assertNotNull("RegisterCertificateType should not be null", tsTsrk1062);
        assertNotNull("Intyg should not be null", tsTsrk1062.getIntyg());
        assertNull("SvarPa should be null", tsTsrk1062.getSvarPa());
    }

    @Test
    public void testConvertSourceWithMessage() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = getUtlatande();

        final Relation relation = new Relation();
        relation.setRelationKod(RelationKod.KOMPLT);
        relation.setMeddelandeId("MeddelandeId");
        relation.setRelationIntygsId("RelationsId");
        relation.setReferensId("ReferensId");

        utlatande.getGrundData().setRelation(relation);

        RegisterCertificateType tsTsrk1062 = InternalToTransport.convert(utlatande);

        assertNotNull("RegisterCertificateType should not be null", tsTsrk1062);
        assertNotNull("Intyg should not be null", tsTsrk1062.getIntyg());
        assertNotNull("SvarPa should not be null", tsTsrk1062.getSvarPa());
        assertEquals("MeddelandeId not equal", relation.getMeddelandeId(), tsTsrk1062.getSvarPa().getMeddelandeId());
        assertEquals("ReferensId not equal", relation.getReferensId(), tsTsrk1062.getSvarPa().getReferensId());
    }

    private TsTrk1062UtlatandeV1 getUtlatande() {
        final TsTrk1062UtlatandeV1.Builder builderTemplate = TsTrk1062UtlatandeV1.builder()
                .setGrundData(buildGrundData(LocalDateTime.now()));
        return builderTemplate.build();
    }

    private GrundData buildGrundData(LocalDateTime timeStamp) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("vardgivareNamn");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("enhetId");
        vardenhet.setEnhetsnamn("enhetNamn");
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("11111");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("0112312313");

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId("HSAID_123");
        skapadAv.setFullstandigtNamn("Torsten Ericsson");

        Patient patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        patient.setPostadress("postadress");
        patient.setPostnummer("11111");
        patient.setPostort("postort");

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }
}
