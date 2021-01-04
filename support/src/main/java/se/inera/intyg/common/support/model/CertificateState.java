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
package se.inera.intyg.common.support.model;

/**
 * @author andreaskaltenbach
 */
public enum CertificateState {
    /**
     * This code is unused.
     */
    UNHANDLED,

    /**
     * The intyg is archived in Mina Intyg ('arkiverad').
     */
    DELETED,

    /**
     * The intyg is no longer archived in Mina Intyg.
     */
    RESTORED,

    /**
     * The intyg is 'makulerat'.
     */
    CANCELLED,

    /**
     * The intyg is sent to a recipient.
     */
    SENT,

    /**
     * The intyg was stored in Intygstjänsten.
     */
    RECEIVED,

    /**
     * Valid statuses in {@link se.inera.ifv.insuranceprocess.certificate.v1.StatusType}.
     */
    IN_PROGRESS,
    PROCESSED
}
