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
package se.inera.intyg.common.ts_diabetes.v2.integration;

import java.io.StringReader;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.ts_parent.integration.ResultTypeUtil;
import se.inera.intyg.common.util.logging.LogMarkers;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;
import se.inera.intygstjanster.ts.services.v1.IntygMeta;
import se.inera.intygstjanster.ts.services.v1.IntygStatus;
import se.inera.intygstjanster.ts.services.v1.Status;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class GetTSDiabetesResponderImpl implements GetTSDiabetesResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTSDiabetesResponderImpl.class);

    @Autowired(required = false)
    private ModuleContainerApi moduleContainer;

    @Override
    public GetTSDiabetesResponseType getTSDiabetes(String logicalAddress, GetTSDiabetesType request) {

        GetTSDiabetesResponseType response = new GetTSDiabetesResponseType();
        CertificateHolder certificate = null;

        String certificateId = request.getIntygsId();
        Personnummer personnummer = getPersonnummer(request);

        if (certificateId == null || certificateId.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing certificateId '.");
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "non-existing certificateId"));
            return response;
        }

        try {
            certificate = moduleContainer.getCertificate(certificateId, personnummer, false);
            if (personnummer != null && !certificate.getCivicRegistrationNumber().equals(personnummer)) {
                response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "nationalIdentityNumber mismatch"));
                return response;
            }
            if (certificate.isDeletedByCareGiver()) {
                response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR,
                    String.format("Certificate '%s' has been deleted by care giver", certificateId)));
            } else {
                TSDiabetesIntyg tsDiabetesIntyg = JAXB
                    .unmarshal(new StringReader(certificate.getOriginalCertificate()), RegisterTSDiabetesType.class).getIntyg();
                response.setIntyg(tsDiabetesIntyg);
                response.setMeta(createMetaData(certificate));
                if (certificate.isRevoked()) {
                    response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.REVOKED,
                        String.format("Certificate '%s' has been revoked", request.getIntygsId())));
                } else {
                    response.setResultat(ResultTypeUtil.okResult());
                }
            }
        } catch (InvalidCertificateException e) {
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
        }
        return response;
    }

    private IntygMeta createMetaData(CertificateHolder certificate) {
        IntygMeta intygMeta = new IntygMeta();
        intygMeta.setAdditionalInfo(certificate.getAdditionalInfo());
        intygMeta.setAvailable(String.valueOf(!certificate.isDeleted()));
        intygMeta.getStatus().addAll(convertToStatuses(certificate.getCertificateStates()));
        return intygMeta;
    }

    private Collection<? extends IntygStatus> convertToStatuses(List<CertificateStateHolder> certificateStates) {
        List<IntygStatus> statuses = new ArrayList<>();
        for (CertificateStateHolder csh : certificateStates) {
            statuses.add(convert(csh));
        }
        return statuses;
    }

    private IntygStatus convert(CertificateStateHolder source) {
        IntygStatus status = new IntygStatus();
        status.setTarget(source.getTarget());
        status.setTimestamp(source.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        status.setType(mapToStatus(source.getState()));
        return status;
    }


    private Personnummer getPersonnummer(GetTSDiabetesType request) {
        if (request.getPersonId() != null) {
            Optional<Personnummer> optional = Personnummer.createPersonnummer(request.getPersonId().getExtension());
            if (optional.isPresent()) {
                return optional.get();
            }
        }

        return null;
    }

    private Status mapToStatus(CertificateState state) {
        return Status.valueOf(state.name());
    }

}
