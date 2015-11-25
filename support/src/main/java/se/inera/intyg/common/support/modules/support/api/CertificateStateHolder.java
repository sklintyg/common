package se.inera.certificate.modules.support.api;

import org.joda.time.LocalDateTime;

import se.inera.certificate.model.CertificateState;

public class CertificateStateHolder {

        private String target;

        private CertificateState state;

        private LocalDateTime timestamp;

        public CertificateStateHolder() {
        }

        public CertificateStateHolder(String target, CertificateState state, LocalDateTime timestamp) {
            this.target = target;
            this.state = state;
            if (timestamp != null) {
                this.timestamp = timestamp;
            } else {
                this.timestamp = new LocalDateTime();
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
