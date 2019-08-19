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
package se.inera.intyg.common.services.texts.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


import java.time.LocalDate;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.intyg.common.services.texts.model.IntygTexts;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepoTestConfig.class})
public class IntygTextsRepositoryImplTest {

    private static final String DEFAULT_INTYGSTYP = "test";
    private static final String DEFAULT_VERSION = "1.0";

    @Autowired
    private IntygTextsRepositoryImpl repo;


    @Test
    public void testGetLatestVersion() {
        repo.intygTexts = new HashSet<IntygTexts>() {
            {
                add(new IntygTexts("1.0", DEFAULT_INTYGSTYP, null, null, null, null, null));
                add(new IntygTexts("1.1", DEFAULT_INTYGSTYP, null, null, null, null, null));
                add(new IntygTexts("1.1.0.1", DEFAULT_INTYGSTYP, null, null, null, null, null));
                add(new IntygTexts("1", DEFAULT_INTYGSTYP, null, null, null, null, null));
            }
        };
        String result = repo.getLatestVersion(DEFAULT_INTYGSTYP);
        assertEquals("should return max version", "1.1.0.1", result);
    }

    @Test
    public void testGetLatestVersionNull() {
        repo.intygTexts = new HashSet<>();
        String result = repo.getLatestVersion(DEFAULT_INTYGSTYP);
        assertEquals("should return null", null, result);
    }

    @Test
    public void testGetLatestVersionValidFromFilter() {
        repo.intygTexts = new HashSet<IntygTexts>() {
            {
                add(new IntygTexts("1.0", DEFAULT_INTYGSTYP, null, null, null, null, null));
                add(new IntygTexts("1.1", DEFAULT_INTYGSTYP, LocalDate.now().minusDays(1), null, null, null, null));
                add(new IntygTexts("1.1.0.1", DEFAULT_INTYGSTYP, LocalDate.now().plusDays(1), null, null, null, null));
                add(new IntygTexts("1", DEFAULT_INTYGSTYP, null, null, null, null, null));
            }
        };
        String result = repo.getLatestVersion(DEFAULT_INTYGSTYP);
        assertEquals("should return max version", "1.1", result);
    }

    @Test
    public void testGetLatestVersionWithingMajorVersion() {
        repo.intygTexts = new HashSet<IntygTexts>() {
            {
                //2 valid minorversions in 1.x
                add(new IntygTexts("1.0", DEFAULT_INTYGSTYP, null, null, null, null, null));
                add(new IntygTexts("1.1", DEFAULT_INTYGSTYP, LocalDate.now().minusDays(1), null, null, null, null));
                //latest valid in of 1.x versions
                add(new IntygTexts("1.2", DEFAULT_INTYGSTYP, LocalDate.now().minusDays(1), null, null, null, null));
                //Not valid due to invalid validfrom
                add(new IntygTexts("1.3", DEFAULT_INTYGSTYP,  LocalDate.now().plusDays(5), null, null, null, null));
                //Next major, only 2.0 is valid
                add(new IntygTexts("2.0", DEFAULT_INTYGSTYP, LocalDate.now().minusDays(1), null, null, null, null));
                add(new IntygTexts("2.1", DEFAULT_INTYGSTYP, LocalDate.now().plusDays(1), null, null, null, null));
            }
        };
        assertEquals("1.2", repo.getLatestVersionForSameMajorVersion(DEFAULT_INTYGSTYP,"1.1"));
        assertEquals("2.0", repo.getLatestVersionForSameMajorVersion(DEFAULT_INTYGSTYP,"2"));
        assertEquals("2.0", repo.getLatestVersionForSameMajorVersion(DEFAULT_INTYGSTYP,"2.0"));
    }

    @Test
    public void testGetLatestVersionTypeFilter() {
        repo.intygTexts = new HashSet<IntygTexts>() {
            {
                add(new IntygTexts("1.0", DEFAULT_INTYGSTYP, null, null, null, null, null));
                add(new IntygTexts("1.1", DEFAULT_INTYGSTYP, null, null, null, null, null));
                add(new IntygTexts("1.1.0.1", "wrong-type", null, null, null, null, null));
                add(new IntygTexts("1", DEFAULT_INTYGSTYP, null, null, null, null, null));
                add(new IntygTexts("2", "wrong-type", null, null, null, null, null));
            }
        };
        String result = repo.getLatestVersion(DEFAULT_INTYGSTYP);
        assertEquals("should return max version", "1.1", result);
    }

    @Test
    public void testGetTextsSuccessful() {
        IntygTexts testData = new IntygTexts(DEFAULT_VERSION, DEFAULT_INTYGSTYP, null, null, null, null, null);
        repo.intygTexts = new HashSet<IntygTexts>() {
            {
                add(testData);
            }
        };
        assertEquals("should return the IntygText in set", testData, repo.getTexts(DEFAULT_INTYGSTYP, DEFAULT_VERSION));
    }

    @Test
    public void testGetTextsZeroPaddingDoesntMatter() {
        IntygTexts testData = new IntygTexts(DEFAULT_VERSION, DEFAULT_INTYGSTYP, null, null, null, null, null);
        repo.intygTexts = new HashSet<IntygTexts>() {
            {
                add(testData);
            }
        };
        assertEquals("should return the IntygText in set", testData, repo.getTexts(DEFAULT_INTYGSTYP, "01.000"));
    }

    @Test
    public void testGetTextsNull() {
        repo.intygTexts = new HashSet<>();
        assertNull("if no version of specified type exists it should return null", repo.getTexts(DEFAULT_INTYGSTYP, DEFAULT_VERSION));
    }

    @Test
    public void testGetTextsNotCareAboutValidFrom() {
        IntygTexts testData = new IntygTexts(DEFAULT_VERSION, DEFAULT_INTYGSTYP, LocalDate.now().plusYears(1), null, null, null, null);
        repo.intygTexts = new HashSet<IntygTexts>() {
            {
                add(testData);
            }
        };
        assertEquals("should return the IntygText in set", testData, repo.getTexts(DEFAULT_INTYGSTYP, DEFAULT_VERSION));
    }
}
