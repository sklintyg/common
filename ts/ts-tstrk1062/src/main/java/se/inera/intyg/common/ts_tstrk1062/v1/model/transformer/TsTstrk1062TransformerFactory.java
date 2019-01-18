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
package se.inera.intyg.common.ts_tstrk1062.v1.model.transformer;

import java.util.Map;

import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.support.modules.transformer.XslTransformerFactory;
import se.inera.intyg.common.support.modules.transformer.XslTransformerType;

public class TsTstrk1062TransformerFactory extends XslTransformerFactory {

    private Map<XslTransformerType, String> transformerTypes;

    public TsTstrk1062TransformerFactory(Map<XslTransformerType, String> xslHrefMap) {
        this.transformerTypes = xslHrefMap;
    }

    @Override
    public XslTransformer get(XslTransformerType xslTransformerType) {
        String xslHref = transformerTypes.get(xslTransformerType);
        return new XslTransformer(xslHref);
    }

}
