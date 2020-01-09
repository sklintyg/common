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
package se.inera.intyg.common.support.model.converter;

import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

/**
 * Factory for creating an editable model.
 */
public interface WebcertModelFactory<T extends Utlatande> {

    /**
     * Create a new draft pre-populated with the attached data.
     *
     * @param newDraftData {@link CreateNewDraftHolder}
     * @return {@link Utlatande} of implementing type
     * @throws ConverterException if an error occurs
     */
    T createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException;

    /**
     * Create a new draft as a copy of template and pre-populated with the attached data.
     *
     * @param copyData {@link CreateDraftCopyHolder}
     * @param template the {@link Utlatande} to copy
     * @return {@link Utlatande} of implementing type
     * @throws ConverterException if an error occurs
     */
    T createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException;

}
