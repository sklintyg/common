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

package se.inera.intyg.common.support.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

@RunWith(MockitoJUnitRunner.class)
public class SpecialistkompetensServiceTest {

    @InjectMocks
    private SpecialistkompetensService specialistkompetensService;

    @Spy
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private Field fileName;

    @Before
    public void setup() throws Exception {
        fileName = SpecialistkompetensService.class.getDeclaredField("fileUrl");
        fileName.setAccessible(true);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void initTest() throws Exception {
        fileName.set(specialistkompetensService, "specialistkompetenser.csv");
        specialistkompetensService.init();
        Field field = SpecialistkompetensService.class.getDeclaredField("codeToDescription");
        field.setAccessible(true);
        Map<String, String> codeToDescription = (Map<String, String>) field.get(specialistkompetensService);
        assertEquals(243, codeToDescription.size());
    }

    @Test
    public void getDescriptionFromCodeWhenBeanNotPresent() throws Exception {
        fileName.set(specialistkompetensService, null);
        specialistkompetensService.init();
        assertFalse(SpecialistkompetensService.getDescriptionFromCode("7199").isPresent());
    }

    @Test
    public void getDescriptionFromCodeTest() throws Exception {
        fileName.set(specialistkompetensService, "specialistkompetenser.csv");
        specialistkompetensService.init();
        assertEquals("Hörselrubbningar", SpecialistkompetensService.getDescriptionFromCode("7199").get());
        assertEquals("Allmänmedicin", SpecialistkompetensService.getDescriptionFromCode("00").get());
        assertEquals("Kärlkirurgi", SpecialistkompetensService.getDescriptionFromCode("10104").get());
        assertEquals("Yrkes- och miljömedicin", SpecialistkompetensService.getDescriptionFromCode("99").get());

        // trim whitespace from code
        assertEquals("Yrkes- och miljömedicin", SpecialistkompetensService.getDescriptionFromCode("   99     ").get());

        // null or empty codes returns empty optional
        assertFalse(SpecialistkompetensService.getDescriptionFromCode(null).isPresent());
        assertFalse(SpecialistkompetensService.getDescriptionFromCode("").isPresent());
        assertFalse(SpecialistkompetensService.getDescriptionFromCode(" ").isPresent());
    }
}
