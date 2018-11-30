/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag7804.v1.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Test;

import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.ag7804.model.internal.Prognos;
import se.inera.intyg.common.ag7804.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.ag7804.model.internal.PrognosTyp;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

public class TransportToInternalTest {

    public static Ag7804UtlatandeV1 getUtlatande() {
        Ag7804UtlatandeV1.Builder utlatande = Ag7804UtlatandeV1.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setTextVersion("1.0");
        utlatande.setUndersokningAvPatienten(new InternalDate(LocalDate.now()));
        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra", "Klämskada skuldra")),
                Diagnos.create("S48", "ICD_10_SE", "Klämskada arm", "Klämskada arm")));
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setPlaneradBehandling("Mer medicin");

        utlatande.setArbetslivsinriktadeAtgarder(
                asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)));

        utlatande.setSysselsattning(Arrays.asList(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.STUDIER)));
        utlatande.setPrognos(Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30));
        utlatande.setFunktionsnedsattning("Funktionsnedsättning");
//TODO: lägg till sjukskrivningar?
        utlatande.setOvrigt("Trevlig kille");
        utlatande.setKontaktMedFk(true);
        utlatande.setAnledningTillKontakt("Känner mig ensam");
        return utlatande.build();
    }

    @Test
    public void endToEnd() throws Exception {
        Ag7804UtlatandeV1 originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        Ag7804UtlatandeV1 convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        assertEquals(originalUtlatande, convertedIntyg);
    }

}
