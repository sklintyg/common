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
package se.inera.intyg.common.ag7804.converter;

import static se.inera.intyg.common.ag7804.converter.RespConstants.BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BIDIAGNOS_1_DELSVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BIDIAGNOS_2_DELSVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_DELSVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil.SvarBuilder;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public final class InternalToTransportUtil {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransportUtil.class);

    private InternalToTransportUtil() {
    }

    private static boolean isDiagnoseCodeValid(Diagnos diagnos, WebcertModuleService webcertModuleService) {
        if (webcertModuleService == null) {
            LOG.debug("No WebcertModuleService available for validation (happens when outside of Webcert context, e.g. Intygstjanst)");
            return true;
        }
        return webcertModuleService.validateDiagnosisCode(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem());
    }

    public static void handleDiagnosSvar(List<Svar> svars, List<Diagnos> diagnoser, WebcertModuleService webcertModuleService) {
        SvarBuilder diagnosSvar = aSvar(DIAGNOS_SVAR_ID_6);
        if (diagnoser != null) {
            for (int i = 0; i < diagnoser.size(); i++) {
                Diagnos diagnos = diagnoser.get(i);
                if (!isDiagnoseCodeValid(diagnos, webcertModuleService)) {
                    continue;
                }
                Diagnoskodverk diagnoskodverk = Diagnoskodverk.valueOf(diagnos.getDiagnosKodSystem());
                switch (i) {
                    case 0:
                        diagnosSvar.withDelsvar(DIAGNOS_DELSVAR_ID_6,
                            aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                            .withDelsvar(DIAGNOS_BESKRIVNING_DELSVAR_ID_6, diagnos.getDiagnosBeskrivning());
                        break;
                    case 1:
                        diagnosSvar.withDelsvar(BIDIAGNOS_1_DELSVAR_ID_6,
                            aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                            .withDelsvar(BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6, diagnos.getDiagnosBeskrivning());
                        break;
                    case 2:
                        diagnosSvar.withDelsvar(BIDIAGNOS_2_DELSVAR_ID_6,
                            aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                            .withDelsvar(BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6, diagnos.getDiagnosBeskrivning());
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            if (!diagnosSvar.delSvars.isEmpty()) {
                svars.add(diagnosSvar.build());
            }
        }
    }
}
