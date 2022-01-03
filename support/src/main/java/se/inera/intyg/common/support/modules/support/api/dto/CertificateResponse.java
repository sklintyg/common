/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

import se.inera.intyg.common.support.model.common.internal.Utlatande;

public class CertificateResponse {

    private final String internalModel;
    private final Utlatande utlatande;
    private final CertificateMetaData metaData;
    private final boolean revoked;

    public CertificateResponse(String internalModel, Utlatande utlatande, CertificateMetaData metaData, boolean revoked) {
        hasText(internalModel, "'internalModel' must not be empty");
        notNull(metaData, "'metaData' must not be null");
        this.internalModel = internalModel;
        this.utlatande = utlatande;
        this.metaData = metaData;
        this.revoked = revoked;
    }

    public String getInternalModel() {
        return internalModel;
    }

    public Utlatande getUtlatande() {
        return utlatande;
    }

    public CertificateMetaData getMetaData() {
        return metaData;
    }

    public boolean isRevoked() {
        return revoked;
    }

}
