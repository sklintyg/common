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
package se.inera.intyg.common.fk7263.model.converter.util;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

public class ConverterUtilTest {

    private static CustomObjectMapper objectMapper = new CustomObjectMapper();

    private String json = readRequestFromFile("/ConverterUtilTest/minimalt-fk7263-internal.json");

    @Test
    public void testConvertFromUtlatande() throws Exception {
        Fk7263Utlatande utlatande = objectMapper.readValue(json, Fk7263Utlatande.class);
        CertificateHolder holder = ConverterUtil.toCertificateHolder(utlatande);
        Assert.assertEquals("id", holder.getId());
        Assert.assertEquals("Enhetsid", holder.getCareUnitId());
        Assert.assertEquals("VardgivarId", holder.getCareGiverId());
    }

    private static String readRequestFromFile(String filePath) {
        try {
            return Resources.toString(new ClassPathResource(filePath).getURL(), Charsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

}
