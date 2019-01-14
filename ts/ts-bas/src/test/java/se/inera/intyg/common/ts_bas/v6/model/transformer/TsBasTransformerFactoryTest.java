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
package se.inera.intyg.common.ts_bas.v6.model.transformer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.support.modules.transformer.XslTransformerFactory;
import se.inera.intyg.common.support.modules.transformer.XslTransformerType;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * @author Magnus Ekstrand on 2018-09-24.
 */
public class TsBasTransformerFactoryTest {

    @InjectMocks
    XslTransformerFactory xslTransformerFactory;

    @Before
    public void setUp() {
        Map<XslTransformerType, String> map = Stream.of(
                new AbstractMap.SimpleEntry<>(TsBasTransformerType.TRANSPORT_TO_V1, "xsl/transportToV1.xsl"),
                new AbstractMap.SimpleEntry<>(TsBasTransformerType.TRANSPORT_TO_V3, "xsl/transportToV3.xsl"),
                new AbstractMap.SimpleEntry<>(TsBasTransformerType.V3_TO_V1, "xsl/V3ToV1.xsl"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        xslTransformerFactory = new TsBasTransformerFactory(map);
    }

    @Test
    public void testGettingXslTransformers() {
        XslTransformer xslTransformer = xslTransformerFactory.get(TsBasTransformerType.TRANSPORT_TO_V1);
        assertEquals("xsl/transportToV1.xsl", xslTransformer.getXslHref());

        xslTransformer = xslTransformerFactory.get(TsBasTransformerType.TRANSPORT_TO_V3);
        assertEquals("xsl/transportToV3.xsl", xslTransformer.getXslHref());

        xslTransformer = xslTransformerFactory.get(TsBasTransformerType.V3_TO_V1);
        assertEquals("xsl/V3ToV1.xsl", xslTransformer.getXslHref());
    }

}
