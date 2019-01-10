/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1009.v1.model.converter;

import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.IDENTITET_STYRKT_GENOM_ID_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.tstrk1009.v1.model.internal.KvIdKontroll;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;

public final class TransportToInternal {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    private TransportToInternal() {
    }

    public static Tstrk1009UtlatandeV1 convert(final Intyg intygSource) throws ConverterException {
        Tstrk1009UtlatandeV1.Builder utlatandeBuilder = Tstrk1009UtlatandeV1.builder();
        setMetaData(utlatandeBuilder, intygSource);
        setSvar(utlatandeBuilder, intygSource);
        return utlatandeBuilder.build();
    }

    private static void setMetaData(Tstrk1009UtlatandeV1.Builder utlatandeBuilder, final Intyg intygSource) throws ConverterException {
        utlatandeBuilder.setId(intygSource.getIntygsId().getExtension());
        utlatandeBuilder.setGrundData(TransportConverterUtil.getGrundData(intygSource, true));
        utlatandeBuilder.setTextVersion(intygSource.getVersion());
        utlatandeBuilder.setSignature(TransportConverterUtil.signatureTypeToBase64(intygSource.getUnderskrift()));
    }

    private static void setSvar(Tstrk1009UtlatandeV1.Builder utlatandeBuilder, final Intyg intygSource) throws ConverterException {
        for (final Svar svar : intygSource.getSvar()) {
            switch (svar.getId()) {
                case IDENTITET_STYRKT_GENOM_ID_2:
                    handleIdentitetStyrktGenom(utlatandeBuilder, svar);
                    break;
            }
        }
    }

    private static void handleIdentitetStyrktGenom(Tstrk1009UtlatandeV1.Builder utlatandeBuilder, final Svar svar) throws ConverterException {
        for (final Svar.Delsvar delsvar : svar.getDelsvar()) {
            if (IDENTITET_STYRKT_GENOM_ID_2.equals(delsvar.getId())) {
                utlatandeBuilder.setIdentitetStyrktGenom(KvIdKontroll.fromCode(getCVSvarContent(delsvar).getCode()));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
