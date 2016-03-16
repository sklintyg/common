/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.HandelsekodEnum;
import se.inera.intyg.common.support.modules.support.api.notification.*;
import se.riv.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v2.CertificateStatusUpdateForCareType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public class CertificateStatusUpdateForCareTypeConverterTest {

    @Test
    public void testConvert() throws Exception {
        final String intygsId = "intygsid";
        final LocalDateTime handelsetid = LocalDateTime.now().minusDays(1);
        final HandelseType handelsetyp = HandelseType.INTYGSUTKAST_ANDRAT;
        final int antalFragor = 4;
        final int antalSvar = 3;
        final int antalHanteradeFragor = 2;
        final int antalHanteradeSvar = 1;
        final Intyg intyg = new Intyg();
        FragorOchSvar FoS = new FragorOchSvar(antalFragor, antalSvar, antalHanteradeFragor, antalHanteradeSvar);

        NotificationMessage msg = new NotificationMessage(intygsId, "luse", handelsetid, handelsetyp, "address", "", FoS,
                NotificationVersion.VERSION_2);
        CertificateStatusUpdateForCareType res = CertificateStatusUpdateForCareTypeConverter.convert(msg, intyg);

        assertEquals(intyg, res.getIntyg());
        assertEquals(HandelsekodEnum.ANDRAT.value(), res.getHandelse().getHandelsekod().getCode());
        assertEquals(HandelsekodEnum.ANDRAT.description(), res.getHandelse().getHandelsekod().getDisplayName());
        assertEquals(handelsetid, res.getHandelse().getTidpunkt());
        assertNotNull(res.getHandelse().getHandelsekod().getCodeSystem());
        assertNotNull(res.getHandelse().getHandelsekod().getCodeSystemName());
        assertEquals(antalFragor, res.getFragorOchSvar().getAntalFragor());
        assertEquals(antalSvar, res.getFragorOchSvar().getAntalSvar());
        assertEquals(antalHanteradeFragor, res.getFragorOchSvar().getAntalHanteradeFragor());
        assertEquals(antalHanteradeSvar, res.getFragorOchSvar().getAntalHanteradeSvar());
    }

    @Test
    public void testConvertToHandelsekodANDRAT() {
        assertEquals(HandelsekodEnum.ANDRAT, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.INTYGSUTKAST_ANDRAT));
    }

    @Test
    public void testConvertToHandelsekodHANFRA() {
        assertEquals(HandelsekodEnum.HANFRA, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.FRAGA_FRAN_FK_HANTERAD));
    }

    @Test
    public void testConvertToHandelsekodHANSVA() {
        assertEquals(HandelsekodEnum.HANSVA, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.SVAR_FRAN_FK_HANTERAD));
    }

    @Test
    public void testConvertToHandelsekodMAKULE() {
        assertEquals(HandelsekodEnum.MAKULE, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.INTYG_MAKULERAT));
    }

    @Test
    public void testConvertToHandelsekodNYFRFM() {
        assertEquals(HandelsekodEnum.NYFRFM, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.FRAGA_FRAN_FK));
    }

    @Test
    public void testConvertToHandelsekodNYFRTM() {
        assertEquals(HandelsekodEnum.NYFRTM, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.FRAGA_TILL_FK));
    }

    @Test
    public void testConvertToHandelsekodNYSVFM() {
        assertEquals(HandelsekodEnum.NYSVFM, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.SVAR_FRAN_FK));
    }

    @Test
    public void testConvertToHandelsekodRADERA() {
        assertEquals(HandelsekodEnum.RADERA, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.INTYGSUTKAST_RADERAT));
    }

    @Test
    public void testConvertToHandelsekodSIGNAT() {
        assertEquals(HandelsekodEnum.SIGNAT, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.INTYGSUTKAST_SIGNERAT));
    }

    @Test
    public void testConvertToHandelsekodSKAPAT() {
        assertEquals(HandelsekodEnum.SKAPAT, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.INTYGSUTKAST_SKAPAT));
    }

    @Test
    public void testConvertToHandelsekodSKICKA() {
        assertEquals(HandelsekodEnum.SKICKA, CertificateStatusUpdateForCareTypeConverter.convertToHandelsekod(HandelseType.INTYG_SKICKAT_FK));
    }

}
