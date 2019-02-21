package se.inera.intyg.common.ts_tstrk1062.v1.model.converter;

import org.junit.Before;
import org.junit.Test;
import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_tstrk1062.support.TsTstrk1062EntryPoint;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.*;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;

public class UtlatandeToIntygTest {

    TsTstrk1062UtlatandeV1.Builder builderTemplate;

    @Before
    public void setUp() throws Exception {
        builderTemplate = TsTstrk1062UtlatandeV1.builder()
                .setId("intygsId")
                .setGrundData(buildGrundData(LocalDateTime.now()))
                .setIntygAvser(IntygAvser.create(EnumSet.of(IntygAvser.BehorighetsTyp.IAV11)))
                .setIdKontroll(IdKontroll.create(IdKontrollKod.KORKORT))
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create("Diagnoser", "2017"))
                .setLakemedelsbehandling(Lakemedelsbehandling.create(false, false, "", false, false, false, null, ""))
                .setBedomningAvSymptom("Bedömning av symptom")
                .setPrognosTillstand(PrognosTillstand.create(PrognosTillstand.PrognosTillstandTyp.JA))
                .setOvrigaKommentarer(null)
                .setBedomning(Bedomning.builder().setUppfyllerBehorighetskrav(EnumSet.of(Bedomning.BehorighetsTyp.VAR11)).build())
                .setTextVersion("TextVersion");

    }

    @Test
    public void convertUtlatandeToIntyg() throws Exception {
        TsTstrk1062UtlatandeV1 utlatande = builderTemplate.build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        //TODO assert arbetsplatskod
        // TODO assert underskrift
        assertIntygsTyp(utlatande, intyg);

        assertEquals("Intygsversion is not equal", utlatande.getTextVersion(), intyg.getVersion());

        assertSvar(utlatande, intyg);
    }

    private void assertIntygsTyp(TsTstrk1062UtlatandeV1 utlatande, Intyg intyg) {
        assertEquals("Intygstyp.code is not equal", TsTstrk1062EntryPoint.KV_UTLATANDETYP_INTYG_CODE, intyg.getTyp().getCode());
        assertEquals("Intygstyp.codeSystem is not equal", Constants.KV_INTYGSTYP_CODE_SYSTEM, intyg.getTyp().getCodeSystem());
        assertEquals("Intygstyp.displayName is not equal", TsTstrk1062EntryPoint.ISSUER_MODULE_NAME, intyg.getTyp().getDisplayName());
    }

    private void assertSvar(TsTstrk1062UtlatandeV1 utlatande, Intyg intyg) throws ConverterException {
        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());

        assertIntygAvser(utlatande.getIntygAvser(), svar.getAllDelsvar(TSTRK1062Constants.INTYG_AVSER_SVAR_ID_1, TSTRK1062Constants.INTYG_AVSER_DELSVAR_ID_1));
    }

    private void assertIntygAvser(IntygAvser intygAvser, List<Svar.Delsvar> delsvarList) throws ConverterException {
        final Set<IntygAvser.BehorighetsTyp> behorighetsTyper = intygAvser.getBehorigheter();

        assertEquals("Number of IntygAvser not equal", behorighetsTyper.size(), delsvarList.size());

        int i = 0;
        for (IntygAvser.BehorighetsTyp behorighetsTyp: behorighetsTyper) {
            final Svar.Delsvar delsvar = delsvarList.get(i++);
            assertEquals("Behorighetstyp not equal", behorighetsTyp.toString().equalsIgnoreCase(getCVSvarContent(delsvar).getCode()));
        }
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
