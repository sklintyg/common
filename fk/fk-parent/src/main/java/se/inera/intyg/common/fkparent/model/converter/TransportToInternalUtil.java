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
package se.inera.intyg.common.fkparent.model.converter;

import com.google.common.base.Strings;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

import java.util.List;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BIDIAGNOS_1_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BIDIAGNOS_2_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_DELSVAR_ID_6;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

public final class TransportToInternalUtil {

    private TransportToInternalUtil() {
    }

    public static void handleDiagnos(List<Diagnos> diagnoser, Svar svar) throws ConverterException {
        // Huvuddiagnos
        String diagnosKod = null;
        String diagnosKodSystem = null;
        String diagnosDisplayName = null;
        String diagnosBeskrivning = null;
        Diagnoskodverk diagnoskodverk = null;

        // Bi-diagnos 1
        String bidiagnosKod1 = null;
        String bidiagnosKodSystem1 = null;
        String bidiagnosDisplayName1 = null;
        String bidiagnosBeskrivning1 = null;
        Diagnoskodverk bidiagnoskodverk1 = null;

        // Bi-diagnos 2
        String bidiagnosKod2 = null;
        String bidiagnosKodSystem2 = null;
        String bidiagnosDisplayName2 = null;
        String bidiagnosBeskrivning2 = null;
        Diagnoskodverk bidiagnoskodverk2 = null;

        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case DIAGNOS_DELSVAR_ID_6:
                    CVType diagnos = getCVSvarContent(delsvar);
                    diagnosKod = diagnos.getCode();
                    diagnosDisplayName = diagnos.getDisplayName();
                    diagnosKodSystem = diagnos.getCodeSystem();
                    break;
                case DIAGNOS_BESKRIVNING_DELSVAR_ID_6:
                    diagnosBeskrivning = getStringContent(delsvar);
                    break;
                case BIDIAGNOS_1_DELSVAR_ID_6:
                    CVType bidiagnos1 = getCVSvarContent(delsvar);
                    bidiagnosKod1 = bidiagnos1.getCode();
                    bidiagnosDisplayName1 = bidiagnos1.getDisplayName();
                    bidiagnosKodSystem1 = bidiagnos1.getCodeSystem();
                    break;
                case BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6:
                    bidiagnosBeskrivning1 = getStringContent(delsvar);
                    break;
                case BIDIAGNOS_2_DELSVAR_ID_6:
                    CVType bidiagnos2 = getCVSvarContent(delsvar);
                    bidiagnosKod2 = bidiagnos2.getCode();
                    bidiagnosDisplayName2 = bidiagnos2.getDisplayName();
                    bidiagnosKodSystem2 = bidiagnos2.getCodeSystem();
                    break;
                case BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6:
                    bidiagnosBeskrivning2 = getStringContent(delsvar);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        diagnoskodverk = Diagnoskodverk.getEnumByCodeSystem(diagnosKodSystem);
        bidiagnoskodverk1 = Diagnoskodverk.getEnumByCodeSystem(bidiagnosKodSystem1);
        bidiagnoskodverk2 = Diagnoskodverk.getEnumByCodeSystem(bidiagnosKodSystem2);

        diagnoser.add(Diagnos.create(diagnosKod, diagnoskodverk.toString(), diagnosBeskrivning, diagnosDisplayName));

        if (bidiagnosKod1 != null && !Strings.isNullOrEmpty(bidiagnosBeskrivning1)) {
            diagnoser.add(Diagnos.create(bidiagnosKod1, bidiagnoskodverk1.toString(), bidiagnosBeskrivning1, bidiagnosDisplayName1));
        }
        if (bidiagnosKod2 != null && !Strings.isNullOrEmpty(bidiagnosBeskrivning2)) {
            diagnoser.add(Diagnos.create(bidiagnosKod2, bidiagnoskodverk2.toString(), bidiagnosBeskrivning2, bidiagnosDisplayName2));
        }
    }
}
