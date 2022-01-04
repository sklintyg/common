/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.mapper;

import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;

/**
 * @author Magnus Ekstrand on 2019-09-02.
 */
public interface Mapper {

     /**
      * Map data from an existing certificate to an already (newly created) existing draft.
      */
     Mapper map(Utlatande source, CreateDraftCopyHolder draftData) throws ModuleException;

     /**
      * Get internal model as JSON.
      *
      * @return
      * @throws ModuleException
      */
     String json() throws ModuleException;

     Utlatande utlatande();

}
