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

package se.inera.intyg.common.fk7263.model.converter.certificate;

import static se.inera.intyg.common.fk7263.support.Fk7263EntryPoint.MODULE_DETAILED_DESCRIPTION;
import static se.inera.intyg.common.fk7263.support.Fk7263EntryPoint.MODULE_NAME;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Patient;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.facade.testsetup.model.CommonMetaDataTest;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

class MetaDataGrundDataTest {

    private GrundData grundData;
    private CertificateMetadata metadata;

    @BeforeEach
    void setup() {
        final var unit = new Vardenhet();
        final var skapadAv = new HoSPersonal();
        final var patient = new se.inera.intyg.common.support.model.common.internal.Patient();
        metadata = CertificateMetadata.builder()
            .unit(Unit.builder().build())
            .patient(Patient.builder().build())
            .build();

        grundData = new GrundData();
        skapadAv.setVardenhet(unit);
        grundData.setSkapadAv(skapadAv);
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        grundData.setPatient(patient);
    }

    @Nested
    class ToCertificate {

        @Nested
        class IncludeTestCommonMetadata extends CommonMetaDataTest {

            @Override
            protected CertificateMetadata getMetaData() {
                return MetaDataGrundData.toCertificate((Fk7263Utlatande) getInternalCertificate());
            }

            @Override
            protected Utlatande getInternalCertificate() {
                final var utlatande = new Fk7263Utlatande();
                utlatande.setGrundData(grundData);
                utlatande.setId("id");
                return utlatande;
            }

            @Override
            protected String getName() {
                return MODULE_NAME;
            }

            @Override
            protected String getDescription() {
                return MODULE_DETAILED_DESCRIPTION;
            }

            @Override
            protected CertificateTextProvider getTextProvider() {
                return null;
            }
        }
    }
}
