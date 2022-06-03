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
package se.inera.intyg.common.support.facade.util;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfig;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;

public final class CertificateDataElementConfigFactory {
    public static CertificateDataConfig textarea(String id, String textId, CertificateTextProvider textProvider) {
        return CertificateDataConfigTextArea.builder()
                .id(id)
                .text(CertificateTextFactory.questionText(textProvider, textId))
                .description(CertificateTextFactory.questionDescription(textProvider, textId))
                .build();
    }

    public static CertificateDataConfig createBoolean(String id, String textId, CertificateTextProvider textProvider) {
        return CertificateDataConfigBoolean.builder()
                .id(id)
                .text(CertificateTextFactory.questionText(textProvider, textId))
                .description(CertificateTextFactory.questionDescription(textProvider, textId))
                .selectedText(CertificateTextFactory.getDefaultSelectedText(textProvider))
                .unselectedText(CertificateTextFactory.getDefaultUnselectedText(textProvider))
                .build();
    }
}
