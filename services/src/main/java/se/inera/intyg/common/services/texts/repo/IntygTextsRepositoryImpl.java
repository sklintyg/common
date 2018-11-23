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
package se.inera.intyg.common.services.texts.repo;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.model.Tillaggsfraga;

@Repository
public class IntygTextsRepositoryImpl implements IntygTextsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(IntygTextsRepository.class);

    private static final String TILLAGGSFRAGA_REGEX = "\\d{4}";
    private static final String TEXTDATA_FILE_EXTENSION = ".xml";

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
        try (Stream<Path> stream = Files.walk(Paths.get(fileDirectory))) {
            stream.filter(IntygTextsRepositoryImpl::isIntygTextsFile).forEach((file) -> {
                try (InputStream fileInSt = Files.newInputStream(file)) {
                    LOG.debug("Updating intygtexts versions for " + file.getFileName());
                    Document doc = DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .parse(fileInSt);

                    Element root = doc.getDocumentElement();
                    String version = root.getAttribute("version");
                    String intygsTyp = root.getAttribute("typ").toLowerCase();
                    LocalDate giltigFrom = getDate(root, "giltigFrom");
                    LocalDate giltigTo = getDate(root, "giltigTom");
                    SortedMap<String, String> texts = getTexter(root);
                    List<Tillaggsfraga> tillaggsFragor = getTillaggsfragor(doc);

                    IntygTexts newIntygTexts = new IntygTexts(version, intygsTyp, giltigFrom, giltigTo, texts, tillaggsFragor,
                            getTextVersionProperties(file));
                    if (!intygTexts.contains(newIntygTexts)) {
                        LOG.debug("Adding new version of {} with version name {}", intygsTyp, version);
                        intygTexts.add(newIntygTexts);
                    }
                } catch (IllegalArgumentException e) {
                    LOG.error("Bad file in directory {}: {}", fileDirectory, e);
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    LOG.error("Error while reading file {}", file.getFileName(), e);
                }
            });
        } catch (IOException e) {
            LOG.error("Error while reading from directory {}", fileDirectory, e);
        }
    }

    protected LocalDate getDate(Element root, String id) {
        String date = Strings.nullToEmpty(root.getAttribute(id)).trim();
        return date.isEmpty() ? null : LocalDate.parse(date);
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

    /**
     * Retrieve the corresponding property file for a intyg texts xml file.
     */
    private Properties getTextVersionProperties(Path file) {
        String baseName = com.google.common.io.Files.getNameWithoutExtension(file.getName(file.getNameCount() - 1).toString());
        final Path propertiesFilePath = file.resolveSibling(baseName + ".properties");
        Properties props = new Properties();
        try (InputStream fileInSt = Files.newInputStream(propertiesFilePath)) {
            props.load(fileInSt);
            LOG.debug("Loaded " + props.stringPropertyNames().size() + " properties for " + propertiesFilePath);
        } catch (IOException e) {
            LOG.error("Failed to load properties for text file " + propertiesFilePath, e);
        }

        return props;
    }

    /**
     * Determines if the given file is a intyg texts XML source file candidate.
     */
    private static boolean isIntygTextsFile(Path file) {
        return Files.isRegularFile(file)
                && file.getName(file.getNameCount() - 1).toString().toLowerCase().endsWith(TEXTDATA_FILE_EXTENSION);
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
    public boolean isVersionSupported(final String intygsTyp, final String version) {
        return intygTexts.stream().anyMatch(isCorrectTypeAndVersionAndValid(intygsTyp, version));
    }

    @Override
    public String getLatestVersion(String intygsTyp) {
        IntygTexts res = intygTexts.stream()
                .filter(s -> s.getIntygsTyp().equals(intygsTyp))
                .filter(s -> s.getValidFrom() == null || !s.getValidFrom().isAfter(LocalDate.now()))
                .max(IntygTexts::compareVersions).orElse(null);
        return res == null ? null : res.getVersion();
    }

    @Override
    public String getLatestVersionForSameMajorVersion(String intygsTyp, String version) {
        checkArgument(!Strings.isNullOrEmpty(intygsTyp), "Missing required parameter intygsTyp.");
        checkArgument(!Strings.isNullOrEmpty(version), "Missing required parameter version.");
        String majorVersion = getMajorVersion(version);

        IntygTexts res = intygTexts.stream()
                .filter(s -> s.getIntygsTyp().equals(intygsTyp))
                .filter(s -> s.getValidFrom() == null || !s.getValidFrom().isAfter(LocalDate.now()))
                .filter(s -> majorVersion.equals(getMajorVersion(s.getVersion())))
                .max(IntygTexts::compareVersions).orElse(null);
        return res == null ? null : res.getVersion();
    }

    private String getMajorVersion(String version) {
        return version.split("\\.", 0)[0];
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
            LOG.error("Malformed version number {} for {}, message: {}", version, intygsTyp, e.getMessage());
            return null;
        }
        LOG.error("Tried to access texts for intyg of type {} and version {}, but this does not exist", intygsTyp, version);
        return null;
    }

    private Predicate<IntygTexts> isCorrectTypeAndVersionAndValid(final String typ, final String version) {
        final ChronoLocalDate now = ChronoLocalDate.from(LocalDateTime.now());

        return texter -> Objects.equals(typ, texter.getIntygsTyp())
                && Objects.equals(version, texter.getVersion())
                && !texter.getValidFrom().isAfter(now)
                && (isNull(texter.getValidTo()) || !texter.getValidTo().isBefore(now));
    }
}
