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
package se.inera.intyg.common.ag114.v1.model.converter;

import static se.inera.intyg.common.ag114.model.converter.RespConstants.TYP_AV_DIAGNOS_BESKRIVNING_DELSVAR_ID_4;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.TYP_AV_DIAGNOS_DELSVAR_ID_4;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

import java.util.List;

import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

/**
 * Util for AG1-14 diagnoser.
 */
public final class TransportToInternalUtil {

    private TransportToInternalUtil() {
    }

    public static void handleDiagnos(List<Diagnos> diagnoser, Svar svar) throws ConverterException {
        String diagnosKod = null;
        String diagnosKodSystem = null;
        String diagnosDisplayName = null;
        String diagnosBeskrivning = null;

        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TYP_AV_DIAGNOS_DELSVAR_ID_4:
                    CVType diagnos = getCVSvarContent(delsvar);
                    diagnosKod = diagnos.getCode();
                    diagnosDisplayName = diagnos.getDisplayName();
                    diagnosKodSystem = diagnos.getCodeSystem();
                    break;
                case TYP_AV_DIAGNOS_BESKRIVNING_DELSVAR_ID_4:
                    diagnosBeskrivning = getStringContent(delsvar);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown diagnos delsvar id: " + delsvar.getId());
            }
        }
        Diagnoskodverk diagnoskodverk = Diagnoskodverk.getEnumByCodeSystem(diagnosKodSystem);
        diagnoser.add(Diagnos.create(diagnosKod, diagnoskodverk.toString(), diagnosBeskrivning, diagnosDisplayName));

    }
}
