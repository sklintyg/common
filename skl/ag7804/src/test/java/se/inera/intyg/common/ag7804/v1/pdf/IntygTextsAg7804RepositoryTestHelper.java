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
package se.inera.intyg.common.ag7804.v1.pdf;

import com.google.common.collect.ImmutableMap;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepositoryImpl;

public class IntygTextsAg7804RepositoryTestHelper extends IntygTextsRepositoryImpl {

    public IntygTextsAg7804RepositoryTestHelper() {
        super.intygTexts = new HashSet<>();
    }

    @Override
    public void update() {

        try {
            Document e = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ClassPathResource("v1/text/texterMU_AG7804_v1.0.xml").getInputStream());
            Element root = e.getDocumentElement();
            String version = root.getAttribute("version");
            String intygsTyp = root.getAttribute("typ").toLowerCase();
            LocalDate giltigFrom = super.getDate(root, "giltigFrom");
            LocalDate giltigTo = super.getDate(root, "giltigTom");
            SortedMap texts = super.getTexter(root);
            List tillaggsFragor = this.getTillaggsfragor(e);

            Properties prop = new Properties();
            prop.putAll(ImmutableMap
                .of("formId", "FK 7804 (001 F 001) Fastställd av Försäkringskassan (TEST)", "blankettId", "7804", "blankettVersion", "01"));

            super.intygTexts.add(new IntygTexts(version, intygsTyp, giltigFrom, giltigTo, texts, tillaggsFragor, prop));

            e = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ClassPathResource("v1/text/texterMU_AG7804_v1.1.xml").getInputStream());
            root = e.getDocumentElement();
            version = root.getAttribute("version");
            intygsTyp = root.getAttribute("typ").toLowerCase();
            giltigFrom = super.getDate(root, "giltigFrom");
            giltigTo = super.getDate(root, "giltigTom");
            texts = super.getTexter(root);
            tillaggsFragor = this.getTillaggsfragor(e);

            prop = new Properties();
            prop.putAll(ImmutableMap
                .of("formId", "FK 7804 (001 F 001) Fastställd av Försäkringskassan (TEST)", "blankettId", "7804", "blankettVersion", "01"));

            super.intygTexts.add(new IntygTexts(version, intygsTyp, giltigFrom, giltigTo, texts, tillaggsFragor, prop));
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
    }
}
