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
package se.inera.intyg.common.tstrk1062.v1.model.converter;

import java.util.ArrayList;
import java.util.List;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public class SvarsWrapper {

    private List<Svar> svarsList;

    public SvarsWrapper(List<Svar> svarsList) {
        initiate(svarsList);
    }

    private void initiate(List<Svar> svarsList) {
        this.svarsList = svarsList;
    }

    public List<Svar> getSvarList(String svarsId) {
        List<Svar> svarToReturn = new ArrayList<Svar>();
        for (Svar svar : this.svarsList) {
            if (svar.getId().equalsIgnoreCase(svarsId)) {
                svarToReturn.add(svar);
            }
        }
        return svarToReturn;
    }

    public Svar.Delsvar getDelsvar(String svarsId, String delsvarId) {
        List<Svar> svarToReturn = new ArrayList<Svar>();
        for (Svar svar : this.svarsList) {
            if (svar.getId().equalsIgnoreCase(svarsId)) {
                for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                    if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                        return delsvar;
                    }
                }
            }
        }
        return null;
    }

    public List<Svar.Delsvar> getAllDelsvar(String svarsId, String delsvarId) {
        List<Svar.Delsvar> delSvarToReturn = new ArrayList<Svar.Delsvar>();
        for (Svar svar : this.svarsList) {
            if (svar.getId().equalsIgnoreCase(svarsId)) {
                for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                    if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                        delSvarToReturn.add(delsvar);
                    }
                }
            }
        }
        return delSvarToReturn;
    }

    public Svar.Delsvar getDelsvar(String svarsId, int sequence, String delsvarId) {
        List<Svar> svarToReturn = new ArrayList<Svar>();
        for (Svar svar : this.svarsList) {
            if (svar.getId().equalsIgnoreCase(svarsId) && svar.getInstans() != null && svar.getInstans().intValue() == sequence) {
                for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                    if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                        return delsvar;
                    }
                }
            }
        }
        return null;
    }
}
