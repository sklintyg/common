/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.support.api;

import java.time.LocalDateTime;

import se.inera.intyg.common.support.model.CertificateState;

public class CertificateStateHolder {

        private String target;

        private CertificateState state;

        private LocalDateTime timestamp;

        public CertificateStateHolder() {
            // Needed for deserialization
        }

        public CertificateStateHolder(String target, CertificateState state, LocalDateTime timestamp) {
            this.target = target;
            this.state = state;
            if (timestamp != null) {
                this.timestamp = timestamp;
            } else {
                this.timestamp = LocalDateTime.now();
            }
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public CertificateState getState() {
            return state;
        }

        public void setState(CertificateState state) {
            this.state = state;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "CertificateStateHolder{"
                    + "target='" + target + '\''
                    + ", state=" + state + ", timestamp="
                    + timestamp + '}';
        }
    }
