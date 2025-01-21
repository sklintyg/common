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
package se.inera.intyg.common.util.integration.interceptor;

import java.io.OutputStream;
import java.io.Writer;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.feature.transform.XSLTOutInterceptor.XSLTCachedOutputStreamCallback;
import org.apache.cxf.feature.transform.XSLTOutInterceptor.XSLTCachedWriter;
import org.apache.cxf.feature.transform.XSLTOutInterceptor.XSLTStreamWriter;
import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedWriter;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.staxutils.StaxUtils;

public abstract class CustomXSLTInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final TransformerFactory TRANSFORM_FACTORY = TransformerFactory.newInstance();
    private final Templates xsltTemplate;
    private String contextPropertyName;

    public CustomXSLTInterceptor(String xsltPath) {
        super(Phase.PRE_STREAM);
        addBefore(StaxOutInterceptor.class.getName());
        TRANSFORM_FACTORY.setURIResolver(new ClasspathUriResolver());

        try {
            final var xsltStream = ClassLoaderUtils.getResourceAsStream(xsltPath, this.getClass());
            if (xsltStream == null) {
                throw new IllegalArgumentException("Cannot load XSLT from path: " + xsltPath);
            }
            final var doc = StaxUtils.read(xsltStream);
            xsltTemplate = TRANSFORM_FACTORY.newTemplates(new DOMSource(doc));

        } catch (TransformerConfigurationException | XMLStreamException e) {
            throw new IllegalArgumentException(
                String.format("Cannot create XSLT template from path: %s",
                    xsltPath), e);
        }
    }

    protected Templates getXSLTTemplate() {
        return xsltTemplate;
    }

    public void setContextPropertyName(String propertyName) {
        contextPropertyName = propertyName;
    }

    protected boolean checkContextProperty(Message message) {
        return contextPropertyName != null
            && !MessageUtils.getContextualBoolean(message, contextPropertyName, false);
    }

    @Override
    public void handleMessage(Message message) {
        if (checkContextProperty(message)) {
            return;
        }

        // 1. Try to get and transform XMLStreamWriter message content
        final var xWriter = message.getContent(XMLStreamWriter.class);
        if (xWriter != null) {
            transformXWriter(message, xWriter);
        } else {
            // 2. Try to get and transform OutputStream message content
            final var out = message.getContent(OutputStream.class);
            if (out != null) {
                transformOS(message, out);
            } else {
                // 3. Try to get and transform Writer message content (actually used for JMS TextMessage)
                final var writer = message.getContent(Writer.class);
                if (writer != null) {
                    transformWriter(message, writer);
                }
            }
        }
    }

    protected void transformXWriter(Message message, XMLStreamWriter xWriter) {
        final var writer = new CachedWriter();
        final var delegate = StaxUtils.createXMLStreamWriter(writer);
        final var wrapper = new XSLTStreamWriter(getXSLTTemplate(), writer, delegate, xWriter);
        message.setContent(XMLStreamWriter.class, wrapper);
        message.put(AbstractOutDatabindingInterceptor.DISABLE_OUTPUTSTREAM_OPTIMIZATION,
            Boolean.TRUE);
    }

    protected void transformOS(Message message, OutputStream out) {
        final var wrapper = new CachedOutputStream();
        final var callback = new XSLTCachedOutputStreamCallback(getXSLTTemplate(), out);
        wrapper.registerCallback(callback);
        message.setContent(OutputStream.class, wrapper);
    }

    protected void transformWriter(Message message, Writer writer) {
        final var wrapper = new XSLTCachedWriter(getXSLTTemplate(), writer);
        message.setContent(Writer.class, wrapper);
    }

}
