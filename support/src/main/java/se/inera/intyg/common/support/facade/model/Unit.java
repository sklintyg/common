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

package se.inera.intyg.common.support.facade.model;

public class Unit {
    private String unitId;
    private String unitName;
    private String address;
    private String zipCode;
    private String city;
    private String phoneNumber;
    private String email;

    public static UnitBuilder create() {
        return new UnitBuilder();
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static final class UnitBuilder {

        private String unitId;
        private String unitName;
        private String address;
        private String zipCode;
        private String city;
        private String email;
        private String phoneNumber;

        private UnitBuilder() {

        }

        public UnitBuilder unitId(String unitId) {
            this.unitId = unitId;
            return this;
        }

        public UnitBuilder unitName(String unitName) {
            this.unitName = unitName;
            return this;
        }

        public UnitBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UnitBuilder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public UnitBuilder city(String city) {
            this.city = city;
            return this;
        }

        public UnitBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UnitBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public Unit build() {
            final var unit = new Unit();
            unit.setUnitId(unitId);
            unit.setUnitName(unitName);
            unit.setAddress(address);
            unit.setZipCode(zipCode);
            unit.setCity(city);
            unit.setEmail(email);
            unit.setPhoneNumber(phoneNumber);
            return unit;
        }
    }
}
