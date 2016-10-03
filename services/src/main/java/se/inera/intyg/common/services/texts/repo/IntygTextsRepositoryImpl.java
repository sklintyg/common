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

package se.inera.intyg.common.services.texts.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.model.Tillaggsfraga;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class IntygTextsRepositoryImpl implements IntygTextsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(IntygTextsRepository.class);

    private static final String TILLAGGSFRAGA_REGEX = "\\d{4}";

    /**
     * The in-memory database of the texts available.
     *
     * Gets updated on a schedule defined in properties.
     */
    protected Set<IntygTexts> intygTexts;

    @Value("${texts.file.directory}")
    private String fileDirectory;

    /**
     * Initial setup of the in-memory database.
     */
    @PostConstruct
    public void init() {
        intygTexts = new HashSet<>();
        update();
    }

    /**
     * Updates the texts.
     *
     * Will parse all files once more and add those not already in memory.
     */
    @Scheduled(cron = "${texts.update.cron}")
    public void update() {
        try {
            Files.walk(Paths.get(fileDirectory)).filter((file) -> Files.isRegularFile(file)).forEach((file) -> {
                try {
                    Document doc = DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .parse(Files.newInputStream(file));

                    Element root = doc.getDocumentElement();
                    String version = root.getAttribute("version");
                    String intygsTyp = root.getAttribute("typ").toLowerCase();
                    String pdfPath = root.getAttribute("pdf");
                    LocalDate giltigFrom = getDate(root, "giltigFrom");
                    LocalDate giltigTo = getDate(root, "giltigTom");
                    SortedMap<String, String> texts = getTexter(root);
                    List<Tillaggsfraga> tillaggsFragor = getTillaggsfragor(doc);

                    IntygTexts newIntygTexts = new IntygTexts(version, intygsTyp, giltigFrom, giltigTo, texts, tillaggsFragor, pdfPath);
                    if (!intygTexts.contains(newIntygTexts)) {
                        LOG.debug("Adding new version of {} with version name {}", intygsTyp, version);
                        intygTexts.add(newIntygTexts);
                    }
                } catch (IllegalArgumentException e) {
                    LOG.error("Bad file in directory {}: {}", fileDirectory, e.getMessage());
                    e.printStackTrace();
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    LOG.error("Error while reading file {}", file.getFileName(), e);
                }
            });
        } catch (IOException e) {
            LOG.error("Error while reading from directory {}", fileDirectory, e);
        }
    }

    protected LocalDate getDate(Element root, String id) {
        String date = root.getAttribute(id);
        return date == null || "".equals(date) ? null : LocalDate.parse(date);
    }

    protected SortedMap<String, String> getTexter(Element element) {
        SortedMap<String, String> texts = new TreeMap<>();
        NodeList textsList = element.getElementsByTagName("text");
        for (int i = 0; i < textsList.getLength(); i++) {
            Element textElement = (Element) textsList.item(i);
            texts.put(textElement.getAttribute("id"), textElement.getTextContent());
        }
        return texts;
    }

    protected List<Tillaggsfraga> getTillaggsfragor(Document doc) {
        List<Tillaggsfraga> tillaggsFragor = new ArrayList<>();
        NodeList tillaggList = doc.getElementsByTagName("tillaggsfraga");
        for (int i = 0; i < tillaggList.getLength(); i++) {
            tillaggsFragor.add(getTillaggsFraga((Element) tillaggList.item(i)));
        }
        return tillaggsFragor;
    }

    private Tillaggsfraga getTillaggsFraga(Element element) {
        String id = getTillaggsFragaId(element);

        return new Tillaggsfraga(id);
    }

    private String getTillaggsFragaId(Element element) {
        String id = element.getAttribute("id");
        if (id != null) {
            Pattern p = Pattern.compile(TILLAGGSFRAGA_REGEX);
            Matcher m = p.matcher(id);
            if (m.find()) {
                id = m.group();
            } else {
                throw new IllegalArgumentException("Tillaggsfraga with id " + id + "is of wrong format");
            }
        } else {
            throw new IllegalArgumentException("Tillaggsfraga has null id");
        }
        return id;
    }

    @Override
    public String getLatestVersion(String intygsTyp) {
        IntygTexts res = intygTexts.stream()
                .filter((s) -> s.getIntygsTyp().equals(intygsTyp))
                .filter((s) -> s.getValidFrom() == null || !s.getValidFrom().isAfter(LocalDate.now()))
                .max(IntygTexts::compareVersions).orElse(null);
        return res == null ? null : res.getVersion();
    }

    @Override
    public IntygTexts getTexts(String intygsTyp, String version) {
        try {
            IntygTexts wanted = new IntygTexts(version, intygsTyp, null, null, null, null, null);
            for (IntygTexts intygText : intygTexts) {
                if (wanted.equals(intygText)) {
                    return intygText;
                }
            }
        } catch (IllegalArgumentException e) {
            LOG.error("Malformed version number {}, message: {}", version, e.getMessage());
            return null;
        }
        LOG.error("Tried to access texts for intyg of type {} and version {}, but this does not exist", intygsTyp, version);
        return null;
    }
}
