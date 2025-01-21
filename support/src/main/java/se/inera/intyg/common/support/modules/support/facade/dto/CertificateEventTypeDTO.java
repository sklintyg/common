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
package se.inera.intyg.common.support.modules.support.facade.dto;

public enum CertificateEventTypeDTO {
    CREATED,
    DELETED,
    LOCKED,
    READY_FOR_SIGN,
    SIGNED,
    SENT,
    AVAILABLE_FOR_PATIENT,
    INCOMING_MESSAGE,
    INCOMING_MESSAGE_HANDLED,
    OUTGOING_MESSAGE,
    OUTGOING_MESSAGE_HANDLED,
    INCOMING_MESSAGE_REMINDER,
    INCOMING_ANSWER,
    REQUEST_FOR_COMPLEMENT,
    REVOKED,
    REPLACED,
    REPLACES,
    COMPLEMENTS,
    COMPLEMENTED,
    EXTENDED,
    CREATED_FROM,
    COPIED_BY,
    COPIED_FROM,
    RELATED_CERTIFICATE_REVOKED
}
