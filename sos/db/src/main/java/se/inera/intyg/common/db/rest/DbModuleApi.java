/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.db.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.db.model.converter.InternalToTransport;
import se.inera.intyg.common.db.model.converter.TransportToInternal;
import se.inera.intyg.common.db.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.db.model.internal.DbUtlatande;
import se.inera.intyg.common.db.support.DbModuleEntryPoint;
import se.inera.intyg.common.sos_parent.rest.SosParentModuleApi;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

public class DbModuleApi extends SosParentModuleApi<DbUtlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(DbModuleApi.class);

    public DbModuleApi() {
        super(DbUtlatande.class);
    }

    @Override
    protected DbUtlatande transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected RegisterCertificateType internalToTransport(DbUtlatande utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected Intyg utlatandeToIntyg(DbUtlatande utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected String getSchematronFileName() {
        return DbModuleEntryPoint.SCHEMATRON_FILE;
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin)
            throws ModuleException {
        throw new ModuleException("Not yet implemented");
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
            List<String> optionalFields) throws ModuleException {
        throw new RuntimeException("Not applicable for dodsbevis");
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        // This is used for Mina intyg and since that is unsupported for DOI we return empty string
        return "";
    }
}
