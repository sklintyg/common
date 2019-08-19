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

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.support.modules.transformer.XslTransformerType;

/**
 * @author Magnus Ekstrand on 2018-09-21.
 */
@SuppressWarnings("ImmutableEnumChecker")
public enum TsBasTransformerType implements XslTransformerType {
    TRANSPORT_TO_V1("transport-to-v1", "xsl/transportToV1.xsl"),
    TRANSPORT_TO_V3("transport-to-v3", "xsl/transportToV3.xsl"),
    V3_TO_V1("v3-to-v1", "xsl/V3ToV1.xsl");

    private final String name;
    private final String location;
    private final ThreadLocal<XslTransformer> holder;

    static Logger log = LoggerFactory.getLogger(TsBasTransformerType.class);

    TsBasTransformerType(String name, String location) {
        this.name = name;
        this.location = location;
        this.holder = new ThreadLocal<>();
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns transformer. Expensive operation so try to cache and reuse.
     *
     * @return the transformer (newly created or existing for the executing thread).
     */
    @Override
    public XslTransformer getTransformer() {
        XslTransformer t = holder.get();
        if (Objects.isNull(t)) {
            log.debug("create new {}", this.location);
            t = new XslTransformer(this.location);
            holder.set(t);
        } else {
            log.debug("reuse existing {}", this.location);
        }
        return t;
    }
}
