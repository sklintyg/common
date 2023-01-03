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
package se.inera.intyg.common.services.texts.repo;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.model.Tillaggsfraga;

@Repository
public class IntygTextsRepositoryImpl implements IntygTextsRepository {

    static final Logger LOG = LoggerFactory.getLogger(IntygTextsRepository.class);

    static final Pattern TILLAGGSFRAGA_REGEX = Pattern.compile("\\d{4}");
    static final String TEXTDATA_FILE_EXTENSION = ".xml";
    static final String LOCATION_NAME = "location";

    /**
     * The in-memory database of the texts available.
     *
     * Gets updated on a schedule defined in properties.
     */
    protected Set<IntygTexts> intygTexts;

    @Value("${texts.file.directory}")
    private String location;

    @Autowired
    ResourceLoader resourceLoader;

    /**
     * Initial setup of the in-memory database.
     */
    @PostConstruct
    public void init() throws IOException {
        // FIXME: Legacy support, can be removed when local config has been substituted by refdata (INTYG-7701)
        if (!ResourceUtils.isUrl(location)) {
            location = "file:" + location;
        }
        if (!location.endsWith("/*")) {
            location += location.endsWith("/") ? "*" : "/*";
        }
        update();
    }


    // returns all matching text resources
    private Set<IntygTexts> update0() throws IOException {
        return Stream.of(ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(location))
                .filter(this::isTextsResource)
                .map(this::parse)
                .filter(Objects::nonNull)
                .map(this::of)
                .collect(Collectors.toSet());
    }


    // parses resource, source resource is included as an attribute
    Element parse(Resource resource) {
        try {
            LOG.debug("Parse: " + resource);
            final Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(resource.getInputStream());
            final Element el = doc.getDocumentElement();
            el.setAttribute(LOCATION_NAME, resource.getURL().toExternalForm());
            return el;
        } catch (Exception e) {
            LOG.error("Error while parsing resource {}", resource, e);
        }
        return null;
    }

    // creates IntygsText from element
    IntygTexts of(Element element) {
        String version = element.getAttribute("version");
        String intygsTyp = element.getAttribute("typ").toLowerCase();
        LocalDate giltigFrom = getDate(element, "giltigFrom");
        LocalDate giltigTo = getDate(element, "giltigTom");
        SortedMap<String, String> texts = getTexter(element);
        List<Tillaggsfraga> tillaggsFragor = getTillaggsfragor0(element);

        IntygTexts newIntygTexts = new IntygTexts(version, intygsTyp, giltigFrom, giltigTo, texts, tillaggsFragor,
                getTextVersionProperties(element.getAttribute(LOCATION_NAME)));

        return newIntygTexts;

    }

    /**
     * Updates the texts.
     *
     * Will parse all files once more and add those not already in memory.
     */
    @Scheduled(cron = "${texts.update.cron}")
    public void update() throws IOException {
        this.intygTexts = update0();
    }

    protected LocalDate getDate(Element root, String id) {
        String date = Strings.nullToEmpty(root.getAttribute(id)).trim();
        return date.isEmpty() ? null : LocalDate.parse(date);
    }

    protected SortedMap<String, String> getTexter(Element element) {
        final SortedMap<String, String> texts = Maps.newTreeMap();
        final NodeList textsList = element.getElementsByTagName("text");
        for (int i = 0; i < textsList.getLength(); i++) {
            Element textElement = (Element) textsList.item(i);
            texts.put(textElement.getAttribute("id"), textElement.getTextContent());
        }
        return texts;
    }

    protected List<Tillaggsfraga> getTillaggsfragor(Document doc) {
        return getTillaggsfragor0(doc.getDocumentElement());
    }

    private List<Tillaggsfraga> getTillaggsfragor0(Element el) {
        List<Tillaggsfraga> tillaggsFragor = new ArrayList<>();
        NodeList tillaggList = el.getElementsByTagName("tillaggsfraga");
        for (int i = 0; i < tillaggList.getLength(); i++) {
            tillaggsFragor.add(getTillaggsFraga((Element) tillaggList.item(i)));
        }
        return tillaggsFragor;
    }
    /**
     * Retrieve the corresponding property file for a intyg texts xml file.
     */
    private Properties getTextVersionProperties(final String sourceName) {
        final Properties props = new Properties();
        String location = sourceName;
        final int index = sourceName.lastIndexOf('.');
        if (index != -1) {
            location = sourceName.substring(0, index);
        }
        location += ".properties";
        try {
            final Resource res = resourceLoader.getResource(location);
            props.load(res.getInputStream());
        } catch (IOException e) {
            LOG.error("Failed to load properties for text file " + location, e);
        }
        return props;
    }

    /**
     * Determines if the given file is a intyg texts XML source file candidate.
     */
    boolean isTextsResource(final Resource resource) {
        return resource.exists() && resource.getFilename().endsWith(TEXTDATA_FILE_EXTENSION);
    }

    private Tillaggsfraga getTillaggsFraga(Element element) {
        final String id = getTillaggsFragaId(element);
        return new Tillaggsfraga(id);
    }

    private String getTillaggsFragaId(Element element) {
        String id = element.getAttribute("id");
        if (Objects.nonNull(id)) {
            final Matcher m = TILLAGGSFRAGA_REGEX.matcher(id);
            if (m.find()) {
                return m.group();
            } else {
                throw new IllegalArgumentException("Tillaggsfraga with id " + id + "is of wrong format");
            }
        }
        throw new IllegalArgumentException("Tillaggsfraga has null id");
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
