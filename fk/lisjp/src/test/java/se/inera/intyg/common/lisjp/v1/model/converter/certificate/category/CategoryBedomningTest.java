/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.category;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

class CategoryBedomningTest {

    private GrundData grundData;
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        final var unit = new Vardenhet();

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(unit);

        grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);

        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        private LisjpUtlatandeV1 internalCertificate;

        @BeforeEach
        void createInternalCertificateToConvert() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();

        }

        @Test
        void shouldIncludeCategoryElement() {
            final var expectedIndex = 17;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var category = certificate.getData().get(BEDOMNING_CATEGORY_ID);

            assertAll("Validating category",
                () -> assertEquals(BEDOMNING_CATEGORY_ID, category.getId()),
                () -> assertEquals(expectedIndex, category.getIndex()),
                () -> assertNull(category.getParent(), "Should not contain a parent"),
                () -> assertNull(category.getValue(), "Should not contain a value"),
                () -> assertNotNull(category.getConfig(), "Should include config")
            );
        }

        @Test
        void shouldIncludeCategoryConfig() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var category = certificate.getData().get(BEDOMNING_CATEGORY_ID);

            assertEquals(CertificateDataConfigType.CATEGORY, category.getConfig().getType());

            assertAll("Validating category configuration",
                () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing selected text"),
                () -> assertNull(category.getConfig().getDescription(), "Should not contain a description")
            );
        }

        @Test
        void shouldNotIncludeAnyValidation() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);
            final var question = certificate.getData().get(BEDOMNING_CATEGORY_ID);
            assertNull(question.getValidation(), "Should not contain any validation");
        }
    }
}
