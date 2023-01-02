/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.doi.v1.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import se.inera.intyg.common.doi.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.mapper.Mapper;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;

public class DbToDoiMapper implements Mapper {

    private WebcertModelFactoryImpl factory = new WebcertModelFactoryImpl();

    private ObjectMapper objectMapper;

    private DoiUtlatandeV1 utlatande;

    public DbToDoiMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mapper map(Utlatande source, CreateDraftCopyHolder draftData) throws ModuleException {
        try {
            utlatande = factory.createCopy(draftData, source);
            return this;
        } catch (ConverterException e) {
            throw new ModuleException(
                String.format("Error mapping data from certificate '%s' to draft '%s'", source.getId(), draftData.getCertificateId()), e);
        }
    }

    @Override
    public String json() throws ModuleException {
        try {
            return objectMapper.writeValueAsString(utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize model", e);
        }
    }

    @Override
    public Utlatande utlatande() {
        return utlatande;
    }


}
