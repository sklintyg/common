/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.facade.builder.config;

import se.inera.intyg.common.support.facade.model.config.CertificateDataConfig;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;


public abstract class AbstractCertificateDataConfigBuilder<T extends AbstractCertificateDataConfigBuilder> {

    protected String header;
    protected String icon;
    protected String text;
    protected String description;
    protected CertificateDataConfigTypes type;

    protected AbstractCertificateDataConfigBuilder(CertificateDataConfigTypes type) {
        this.type = type;
    }

    public T header(String header) {
        this.header = header;
        return (T) this;
    }

    public T icon(String icon) {
        this.icon = icon;
        return (T) this;
    }

    public T text(String text) {
        this.text = text;
        return (T) this;
    }

    public T description(String description) {
        this.description = description;
        return (T) this;
    }

    public abstract <T extends CertificateDataConfig> T build();
}
