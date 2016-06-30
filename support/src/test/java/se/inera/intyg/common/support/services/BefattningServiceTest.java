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

import org.junit.Test;

public class BefattningServiceTest {

    private BefattningService befattningService = new BefattningService();

    @SuppressWarnings("unchecked")
    @Test
    public void initTest() throws Exception {
        befattningService.init();
        Field field1 = BefattningService.class.getDeclaredField("codeToDescription");
        Field field2 = BefattningService.class.getDeclaredField("descriptionToCode");
        field1.setAccessible(true);
        field2.setAccessible(true);
        Map<String, String> codeToDescription = (Map<String, String>) field1.get(befattningService);
        Map<String, String> descriptionToCode = (Map<String, String>) field2.get(befattningService);
        assertEquals(209, codeToDescription.size());
        assertEquals(209, descriptionToCode.size());
    }

    @Test
    public void getWhenBeanNotPresent() throws Exception {
        Field beanInstance = BefattningService.class.getDeclaredField("instance");
        beanInstance.setAccessible(true);
        beanInstance.set(befattningService, null);
        assertFalse(BefattningService.getDescriptionFromCode("203010").isPresent());
        assertFalse(BefattningService.getCodeFromDescription("Läkare legitimerad, specialiseringstjänstgöring").isPresent());
    }

    @Test
    public void getDescriptionFromCodeTest() throws Exception {
        befattningService.init();
        assertEquals("Läkare legitimerad, specialiseringstjänstgöring", BefattningService.getDescriptionFromCode("203010").get());
        assertEquals("Överläkare", BefattningService.getDescriptionFromCode("201010").get());
        assertEquals("Distriktssköterska", BefattningService.getDescriptionFromCode("206011").get());
        assertEquals("Tandläkare under specialiseringstjänstgöring", BefattningService.getDescriptionFromCode("251013").get());

        // trim whitespace from code
        assertEquals("Tandläkare under specialiseringstjänstgöring", BefattningService.getDescriptionFromCode("   251013     ").get());

        // null or empty codes returns empty optional
        assertFalse(BefattningService.getDescriptionFromCode(null).isPresent());
        assertFalse(BefattningService.getDescriptionFromCode("").isPresent());
        assertFalse(BefattningService.getDescriptionFromCode(" ").isPresent());
    }

    @Test
    public void getCodeFromDescriptionTest() throws Exception {
        befattningService.init();
        assertEquals("203010", BefattningService.getCodeFromDescription("Läkare legitimerad, specialiseringstjänstgöring").get());
        assertEquals("201010", BefattningService.getCodeFromDescription("Överläkare").get());
        assertEquals("206011", BefattningService.getCodeFromDescription("Distriktssköterska").get());
        assertEquals("251013", BefattningService.getCodeFromDescription("Tandläkare under specialiseringstjänstgöring").get());

        // trim whitespace from code
        assertEquals("251013", BefattningService.getCodeFromDescription("   Tandläkare under specialiseringstjänstgöring     ").get());

        // null or empty codes returns empty optional
        assertFalse(BefattningService.getCodeFromDescription(null).isPresent());
        assertFalse(BefattningService.getCodeFromDescription("").isPresent());
        assertFalse(BefattningService.getCodeFromDescription(" ").isPresent());
    }
}
