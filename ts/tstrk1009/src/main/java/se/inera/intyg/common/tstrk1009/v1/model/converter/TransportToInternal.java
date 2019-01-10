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
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_SVAR_ID_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;
import java.util.EnumSet;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.tstrk1009.v1.model.internal.IntygetAvserBehorigheter;
import se.inera.intyg.common.tstrk1009.v1.model.internal.IntygetAvserBehorighet;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;

public final class TransportToInternal {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    private TransportToInternal() {
    }

    public static Tstrk1009UtlatandeV1 convert(Intyg source) throws ConverterException {
        Tstrk1009UtlatandeV1.Builder utlatande = Tstrk1009UtlatandeV1.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, true));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(Tstrk1009UtlatandeV1.Builder utlatande, Intyg source) throws ConverterException {
        EnumSet<IntygetAvserBehorighet> intygAvserSet = EnumSet.noneOf(IntygetAvserBehorighet.class);
        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case INTYG_AVSER_SVAR_ID_1:
                    handleIntygAvser(utlatande, svar, intygAvserSet);
                    break;
            }
        }

        utlatande.setIntygetAvserBehorigheter(IntygetAvserBehorigheter.create(intygAvserSet));
    }

    private static void handleIntygAvser(Tstrk1009UtlatandeV1.Builder utlatande, Svar svar,
                                         EnumSet<IntygetAvserBehorighet> intygAvserSet) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case INTYG_AVSER_DELSVAR_ID_1:
                    intygAvserSet.add(IntygetAvserBehorighet.valueOf(IntygAvserKod.fromCode(getCVSvarContent(delsvar).getCode()).name()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleIdentitetStyrktGenom(Tstrk1009UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

}
