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
package se.inera.intyg.common.support.xml;

import com.google.common.base.Splitter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

/**
 * Utility that aids in creating an XML validator from complex XSD files (which include and/or import other XSD files).
 * Create an instance of the builder and register all resources (XSD files) that the builder needs to know about. When a
 * {@link javax.xml.validation.Schema} is generated the root XSD must be specified.
 * <p>
 * Example:
 *
 * <pre>
 * SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
 * Source rootSource = schemaValidatorBuilder.registerResource(&quot;root-schema.xsd&quot;);
 * schemaValidatorBuilder.registerResource(&quot;included-schema.xsd&quot;);
 * schemaValidatorBuilder.registerResource(&quot;imported-schema.xsd&quot;);
 * Schema schema = schemaValidatorBuilder.build(rootSource);
 * </pre>
 */
public class SchemaValidatorBuilder {

    private final List<Source> sources;

    public SchemaValidatorBuilder() {
        this.sources = new ArrayList<>();
    }

    /**
     * Register an resource that can be fount on the classpath.
     *
     * @param classPathResouce The XSD classpath resource path.
     * @return The {@link javax.xml.transform.Source} representation of the resource.
     * Useful when calling {@link #build(javax.xml.transform.Source)} if the
     * specified resource is the root source.
     * @throws java.io.IOException if the resource could not be found.
     */
    public Source registerResource(String classPathResouce) throws IOException {
        Source source = new StreamSource(new ClassPathResource(classPathResouce).getInputStream());
        // We define the systemId as the last path segment of the classPathResource (the filename).
        source.setSystemId(getLastPathSegment(classPathResouce));
        sources.add(source);

        return source;
    }

    /**
     * Builds a {@link javax.xml.validation.Schema} that can be used to validate XML agains the complex XSD schema.
     *
     * @param rootSource The XSD that is the root schema (including and importing other schemas).
     * @return A schema used for validation.
     * @throws org.xml.sax.SAXException If the schema could not be generated.
     */
    public Schema build(Source rootSource) throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(new ResourceResolver());

        return schemaFactory.newSchema(rootSource);
    }

    private String getLastPathSegment(String path) {
        if (path.contains("/")) {
            List<String> splitString = Splitter.on('/').splitToList(path);
            return splitString.get(splitString.size() - 1);
        }
        return path;
    }

    /**
     * A simple {@link org.w3c.dom.ls.LSResourceResolver} implementation that resolves resources based on the sysmteId against those
     * registered in the builder. <br>
     * NOTE: The resources are returned as input streams, meaning they can only be read once.
     */
    private class ResourceResolver implements LSResourceResolver {

        @Override
        public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
            String lastPathSegment = getLastPathSegment(systemId);

            for (Source source : sources) {
                if (source.getSystemId().equals(lastPathSegment)) {
                    InputStream resourceAsStream = ((StreamSource) source).getInputStream();
                    return new LSInputImpl(publicId, lastPathSegment, resourceAsStream);
                }
            }

            return null;
        }

        private class LSInputImpl implements LSInput {

            private String publicId;

            private String systemId;

            private BufferedInputStream inputStream;

            LSInputImpl(String publicId, String sysId, InputStream input) {
                this.publicId = publicId;
                this.systemId = sysId;
                this.inputStream = new BufferedInputStream(input);
            }

            @Override
            public String getPublicId() {
                return publicId;
            }

            @Override
            public void setPublicId(String publicId) {
                this.publicId = publicId;
            }

            @Override
            public String getBaseURI() {
                return null;
            }

            @Override
            public InputStream getByteStream() {
                return inputStream;
            }

            @Override
            public boolean getCertifiedText() {
                return false;
            }

            @Override
            public Reader getCharacterStream() {
                return null;
            }

            @Override
            public String getEncoding() {
                return null;
            }

            @Override
            public String getStringData() {
                return null;
            }

            @Override
            public void setBaseURI(String baseURI) {
                // Do nothing
            }

            @Override
            public void setByteStream(InputStream byteStream) {
                // Do nothing
            }

            @Override
            public void setCertifiedText(boolean certifiedText) {
                // Do nothing
            }

            @Override
            public void setCharacterStream(Reader characterStream) {
                // Do nothing
            }

            @Override
            public void setEncoding(String encoding) {
                // Do nothing
            }

            @Override
            public void setStringData(String stringData) {
                // Do nothing
            }

            @Override
            public String getSystemId() {
                return systemId;
            }

            @Override
            public void setSystemId(String systemId) {
                this.systemId = systemId;
            }
        }
    }
}
